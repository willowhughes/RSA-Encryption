import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;

import static java.nio.charset.StandardCharsets.UTF_8;

public class RSAMain {
        public static void main (String[] args) {

                Random rand = new Random();
                int keyLength = 100;
                int e = 0;
                int d = 0;

                //find p
                int p = findRandomPrime(rand, keyLength);

                //find q
                int q = findRandomPrime(rand, keyLength);
                System.out.println("p = " + p + " \nq = " + q);

                //find n
                int n = p * q;
                System.out.println("n = " + n);
                BigInteger bigN = BigInteger.valueOf(n);                               //turn n into big integer for encrypting and decrypting

                //find totient
                int totient = (p - 1) * (q - 1);
                System.out.println("totient = " + totient);

                //find e
                boolean copri = false;
                while (!copri) {                                                //uses coprime method to find if a random number between 1
                        e = findRandomPrime(rand, totient);                    //and the totient that is coprime to the totient ----via eclideans
                        copri = coprime(e, totient);                          //algorithm called within the coprime method
                }
                System.out.println("e = " + e);

                //find d
                //get_d(totient, e);
                boolean evendivi = false;
                while (!evendivi) {                                                                  //https://youtu.be/aJ9HAdiAnIU
                        for (int k = 2; k < totient; k++) {                                         //a shortcut I found on youtube is to rearrange
                                evendivi = isEvenlyDivisable(1 + (k * (totient)), e);           //the algorithm to find d with a new value, k, and
                                d = (1 + (k * (totient))) / e;                                    //to increment k until a least value of k solves to
                                if (evendivi) {                                                  //a solution such that d is an integer...
                                        if (d < 0) {                                            //The isEvenlyDivisable method proves that when the
                                                d = d + totient;                               //correct value of k is found, d is, in fact, an integer
                                        }                                                     //and breaks out of the search
                                        System.out.println("d = " + d);
                                        break;
                                }
                        }
                }
                System.out.println("Our public key (n, e) is (" + n + ", " + e + ")");
                System.out.println("Our private key (n, d) is (" + n + ", " + d + ")");

                //int temp;
                boolean loop = true;
                Scanner console = new Scanner(System.in);
                String answer;
                while (loop) {
                        System.out.println("Would you like to generate keys and encrypt a message or decrypt a message(type e or d)?");
                        answer = console.next();
                        if (answer.charAt(0) == 'e' || answer.charAt(0) == 'E') {
                                System.out.println("what is your message?");
                                answer = console.next();

                                /*temp = Integer.parseInt(answer);
                                BigInteger message = BigInteger.valueOf(temp);                  //for future implementation of number messages
                                BigInteger cipher = (message.pow(e)).mod(bigN);*/

                                BigInteger cipher = encrypt(answer, bigN, e);
                                System.out.println("cipher = " + cipher);

                        } else if (answer.charAt(0) == 'd' || answer.charAt(0) == 'D') {
                                System.out.println("what is the cipher?");
                                BigInteger ans = BigInteger.valueOf(Integer.parseInt(console.next()));

                                System.out.println("what is n?");
                                BigInteger tempN = BigInteger.valueOf(n);                                               //temporary shortcut
                                //BigInteger tempN = BigInteger.valueOf(Integer.parseInt(console.next()));

                                System.out.println("what is d?");
                                int tempD = d;                                                                          //temporary shortcut
                                //int tempD = Integer.parseInt(console.next());

                                decrypt(ans, tempN, tempD);
                                loop = false;
                        }
                }
        }

        public static BigInteger encrypt(String answer, BigInteger bigN, int e) {
                byte[] string_to_byte = answer.getBytes();
                BigInteger byte_to_bigint = new BigInteger(string_to_byte);
                return byte_to_bigint.modPow(BigInteger.valueOf(e), bigN);
        }

        public static void decrypt(BigInteger ans, BigInteger n, int d) {
                BigInteger decrypted = ans.modPow(BigInteger.valueOf(d), n);
                byte[] bigint_to_byte = decrypted.toByteArray();
                String byte_to_string = new String(bigint_to_byte, UTF_8);
                System.out.println(byte_to_string);
        }

        public static int __gcd(int a, int b)
        {
                // Everything divides 0
                if (a == 0 || b == 0)
                        return 0;

                // base case
                if (a == b)
                        return a;

                // a is greater
                if (a > b)
                        return __gcd(a-b, b);

                return __gcd(a, b-a);
        }

        public static boolean coprime(int a, int b) {
                if ( __gcd(a, b) == 1) {
                        return true;
                }
                else {
                        return false;
                }
        }

        public static boolean isPrime(int n) {
                boolean prime = true;

                int i = 2;
                while(i <= n/2) {
                        if(n % i == 0) {
                                prime = false;
                                break;
                        }
                        i++;
                }
                return prime;
        }

        public static int findRandomPrime (Random rand, int keyLength) {                            //randomizes numbers until a random prime number is found
                boolean prime = false;
                int temp = 2;
                while (!prime) {
                        temp = rand.nextInt(keyLength);
                        while (temp < 2) {                                              //loops generation incase a 0 or 1 is generated
                                temp = rand.nextInt(keyLength);
                        }
                        prime = isPrime(temp);
                }
                return temp;
        }

        public static boolean isEvenlyDivisable(int a, int b) {
                return a % b == 0;
        }
}