package ao.ai.evo.coding.diversity;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Enumeration;

/**
 * Ripped off from
 *  http://www.merriampark.com/comb.htm
 *
 * Note: NOT threadsafe.
 */
public class Combiner<T> implements Enumeration<T[]> {

    private T items[];

    private int a[];

    private int n;

    private int r;

    private long numLeft;

    private long total;

    private T scratchpad[];

    public Combiner(T toCombine[], int r) {
        assert toCombine.length >= r && r >= 0;
        this.n = toCombine.length;
        this.r = r;
        this.items = toCombine;
        scratchpad = Arrays.copyOf(items, r);
        a = new int[r];
        total = choose(n, r);
        reset();
    }

    private void reset() {
        for (int i = 0; i < a.length; i++) {
            a[i] = i;
        }
        numLeft = total;
    }

    public boolean hasMoreElements() {
        return numLeft > 0;
    }

    @SuppressWarnings("unchecked")
    public T[] nextElement() {
        if (!hasMoreElements()) return null;
        for (int i = 0; i < r; i++) {
            scratchpad[i] = items[a[i]];
        }
        moveIndex();
        return scratchpad.clone();
    }

    public void moveIndex() {
        if (numLeft-- == total) return;
        int i = r - 1;
        while (a[i] == n - r + i) i--;
        a[i]++;
        for (int j = i + 1; j < r; j++) {
            a[j] = a[i] + j - i;
        }
    }

    private static long choose(int n, int r) {
        if (n < 21) {
            long nFact = smallFactorial(n);
            long rFact = smallFactorial(r);
            return nFact / (rFact * smallFactorial(n - r));
        } else {
            BigInteger nFact = bigFactorial(n);
            BigInteger rFact = bigFactorial(r);
            return nFact.divide((rFact.multiply(bigFactorial(n - r)))).longValue();
        }
    }

    private static long smallFactorial(int n) {
        long fact = 1;
        for (int i = n; i > 1; i--) fact *= i;
        return fact;
    }

    private static BigInteger bigFactorial(int n) {
        BigInteger fact = BigInteger.ONE;
        for (int i = n; i > 1; i--) {
            fact = fact.multiply(BigInteger.valueOf(i));
        }
        return fact;
    }
}
