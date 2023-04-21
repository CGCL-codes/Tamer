package net.community.chest.math.test;

import java.util.Collections;
import java.util.List;
import net.community.chest.lang.math.NumberTables;
import net.community.chest.util.collection.CollectionsUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * <P>Copyright as per GPLv2</P>
 * @author Lyor G.
 * @since Jun 16, 2011 11:32:42 AM
 */
public class PrimalityTester extends Assert {

    public PrimalityTester() {
        super();
    }

    public static final boolean isPrime(final long value) {
        return (findFirstFactor(value) == null);
    }

    /**
	 * @param value Original value
	 * @return First factor that is &gt;1 and &lt;<I>value</I> that is a factor
	 * of the original value - <code>null</code> if no factor found (i.e., prime)
	 */
    public static final Long findFirstFactor(final long value) {
        if (value < 0L) return findFirstFactor(0L - value);
        if (value <= 1L) return null;
        if (value == 2L) return null;
        if ((value & 0x01) == 0L) return Long.valueOf(2L);
        final long maxFactor = Math.round(Math.sqrt(value));
        for (long factor = 3L; factor < maxFactor; factor++) {
            if ((value % factor) == 0L) return Long.valueOf(factor);
        }
        return null;
    }

    private static interface PrimeHandler {

        boolean handlePrime(long number);
    }

    public static final Integer ZERO_POSITION = Integer.valueOf(0);

    @Test
    public void testPrimeBitPositions() {
        generatePrimes(3L, Integer.MAX_VALUE, new PrimeHandler() {

            @Override
            public boolean handlePrime(final long number) {
                final List<Integer> bitPos = NumberTables.getSetBitPositions(number);
                final int numBits = (bitPos == null) ? 0 : bitPos.size();
                if (number != 2L) {
                    assertTrue("No bit positions extracted for " + number, numBits > 0);
                    final int zeroPos = (numBits > 0) ? bitPos.indexOf(ZERO_POSITION) : (-1);
                    assertTrue("No zero bit extracted for " + number, zeroPos >= 0);
                    assertTrue("No bit other than zero extracted for " + number, numBits > 1);
                    bitPos.remove(zeroPos);
                }
                Collections.sort(bitPos);
                final String posValue = String.valueOf(bitPos), revValue = String.valueOf(CollectionsUtils.reverse(bitPos));
                System.out.append("\t").append(String.valueOf(number)).append(": ").append(posValue).append('\t').append(revValue).println();
                return true;
            }
        });
    }

    protected long generatePrimes(final PrimeHandler handler) {
        return generatePrimes(2L, handler);
    }

    protected long generatePrimes(final long startPos, final PrimeHandler handler) {
        return generatePrimes(startPos, Long.MAX_VALUE, handler);
    }

    protected long generatePrimes(final long startPos, final long endPos, final PrimeHandler handler) {
        final long startTime = System.nanoTime();
        long numPrimes = 0L;
        for (long curValue = Math.max(2L, startPos); (curValue > 0L) && (curValue < endPos); curValue++) {
            if (!isPrime(curValue)) continue;
            numPrimes++;
            if (!handler.handlePrime(curValue)) break;
        }
        final long endTime = System.nanoTime(), duration = endTime - startTime;
        System.out.append("Processed ").append(String.valueOf(numPrimes)).append(" primes in ").append(String.valueOf(duration)).append(" nano").println();
        return numPrimes;
    }
}
