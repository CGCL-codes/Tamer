package net.sf.kerner.commons;

/**
 * <p>
 * A {@code HumanReadableSizePrinter} can be used to get a human readable string
 * based expression from any byte based number.
 * </p>
 * <p>
 * Example: <br>
 * Print a file size of 1024 bytes in a human readable format.
 * 
 * <pre>
 * System.out.println(new HumanReadableAmountPrinter(1024));
 * </pre>
 * 
 * Will print the string "1K".
 * </p>
 * <p>
 * This class is fully threadsave.
 * </p>
 * 
 * @author <a href="mailto:alex.kerner.24@googlemail.com">Alexander Kerner</a>
 * @version 2010-10-01
 * 
 */
public class HumanReadableAmountPrinter {

    private static final String POST_FIX_B = "B";

    private static final String POST_FIX_K = "K";

    private static final String POST_FIX_M = "M";

    private static final String POST_FIX_G = "G";

    private final long oneK;

    private final long oneM;

    private final long oneG;

    private final long amount;

    /**
	 * <p>
	 * Construct a new {@code HumanReadableAMountPrinter}.
	 * </p>
	 * 
	 * @param amount
	 *            byte based number that is printed
	 * @param usePowerOf1000
	 *            if true, 1000 bytes equals 1K byte; otherwise 1024 bytes equal
	 *            1K byte
	 */
    public HumanReadableAmountPrinter(long amount, boolean usePowerOf1000) {
        if (usePowerOf1000) oneK = 1000L; else oneK = 1024L;
        oneM = oneK * oneK;
        oneG = oneK * oneM;
        this.amount = amount;
    }

    /**
	 * <p>
	 * Construct a new {@code HumanReadableAMountPrinter}.
	 * </p>
	 * 
	 * @param amount
	 *            byte based number that is printed
	 */
    public HumanReadableAmountPrinter(long amount) {
        this(amount, false);
    }

    public String toString() {
        String postFix = POST_FIX_B;
        long sizeCp = amount;
        if (sizeCp / oneG > 0) {
            postFix = POST_FIX_G;
            sizeCp = sizeCp / oneG;
        } else if (sizeCp / oneM > 0) {
            postFix = POST_FIX_M;
            sizeCp = sizeCp / oneM;
        } else if (sizeCp / oneK > 0) {
            postFix = POST_FIX_K;
            sizeCp = sizeCp / oneK;
        } else {
        }
        return sizeCp + postFix;
    }
}
