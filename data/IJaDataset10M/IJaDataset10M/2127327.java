package net.ddaniels.euler.problem50;

import java.util.ArrayList;

/**
 * The prime 41, can be written as the sum of six consecutive primes:
 * 41 = 2 + 3 + 5 + 7 + 11 + 13
 * 
 * This is the longest sum of consecutive primes that adds to a prime below
 * one-hundred.
 * 
 * The longest sum of consecutive primes below one-thousand that adds to a
 * prime, contains 21 terms, and is equal to 953.
 * 
 * Which prime, below one-million, can be written as the sum of the most
 * consecutive primes?
 * 
 * @author DanielsDJ
 * 
 */
public class LargestConsecutivePrime {

    private static boolean isInit = false;

    private static boolean[] isPrime;

    private static final int SIEVE_SIZE = 1000000;

    public static ArrayList<Integer> consecutivePrimes = new ArrayList<Integer>();

    /**
    * @param args
    */
    public static void main(String[] args) {
        initSieve(SIEVE_SIZE);
        int count = 0;
        for (int i = 0; i < SIEVE_SIZE; i++) {
            if (isPrime(i)) {
                consecutivePrimes.add(i);
                count++;
            }
        }
        int largestSumPrime = findLongestConsecutivePrimeSum(consecutivePrimes, SIEVE_SIZE);
        System.out.println("Largest consecutive sum prime below " + SIEVE_SIZE + ": " + largestSumPrime);
    }

    /**
     * Finds the prime number that equals the sum of the longest consecutive prime numbers below 
     * the sieve_size.
     * 
     * It accomplishes this by looking at the largest sequence length and then sliding the 
     * search sequence along the list of prime number sum sequence, searching for the longest consectuive sum
     * that equals a prime number.
     * 
     * @param primes
     * @param sieve_size
     * @return
     */
    private static int findLongestConsecutivePrimeSum(ArrayList<Integer> primes, int sieve_size) {
        int largestConsecPrime = 0;
        int sequenceLength = primes.size();
        int lower = 0;
        int upper = lower + sequenceLength;
        int consecSum;
        while (largestConsecPrime == 0 && sequenceLength > 0) {
            lower = 0;
            upper = lower + sequenceLength;
            consecSum = 0;
            for (int i = lower; i < upper; i++) {
                consecSum += primes.get(i);
                if (consecSum >= sieve_size) break;
            }
            while (consecSum < sieve_size) {
                if (isPrime(consecSum)) {
                    largestConsecPrime = consecSum;
                    break;
                } else {
                    lower++;
                    upper++;
                    if (upper < primes.size()) {
                        consecSum -= primes.get(lower - 1);
                        consecSum += primes.get(upper - 1);
                    } else break;
                }
            }
            sequenceLength--;
        }
        printSequence(sequenceLength, lower, upper, largestConsecPrime);
        return largestConsecPrime;
    }

    private static void printSequence(int sequenceLength, int i, int upper, int consecSum) {
        if (i < 0) return;
        int testSum = 0;
        int p;
        System.out.println("Sequence length:" + sequenceLength + " primes from: ");
        for (int j = i; j < upper - 1; j++) {
            p = consecutivePrimes.get(j);
            testSum += p;
            System.out.print(p + " + ");
        }
        p = consecutivePrimes.get(upper - 1);
        testSum += p;
        System.out.println(p);
    }

    public static boolean isPrime(int n) {
        return isPrime[n];
    }

    public static void initSieve(int N) {
        isPrime = new boolean[N + 1];
        for (int i = 2; i <= N; i++) isPrime[i] = true;
        for (int i = 2; i * i <= N; i++) {
            if (isPrime[i]) {
                for (int j = i; i * j <= N; j++) isPrime[i * j] = false;
            }
        }
    }
}
