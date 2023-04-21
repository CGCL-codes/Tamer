package net.lukemurphey.nsia;

/**
 * This class is intended to analyze operands to determine a numerical operation may result in a overflow. Such an overflow
 * may cause security checks to fail or allow other undesired operation. Calling these methods must be done with care since Java
 * may perform casting that renders the functions useless.
 * @author luke
 *
 */
public class NumericalOverflowAnalysis {

    /**
	 * Method determines if a long can be incremented by one safely. Note that argument is intended to be the
	 * operand that <i>will</i> be incremented. Passing the value after it has been incremented may cause the 
	 * operand to have been overflowed already.
	 * @param operand The value to be incremented.
	 * @return
	 */
    public static boolean assertSafeIncrement(long operand) {
        if (operand == Long.MAX_VALUE) return false; else return true;
    }

    /**
	 * Method determines if a double can be incremented by one safely. Note that argument is intended to be the
	 * operand that <i>will</i> be incremented. Passing the value after it has been incremented may cause the 
	 * operand to have been overflowed already.
	 * @param operand The value to be incremented.
	 * @return
	 */
    public static boolean assertSafeIncrement(double operand) {
        if (operand == Double.MAX_VALUE) return false; else return true;
    }

    /**
	 * Method determines if an Integer can be incremented by one safely. Note that argument is intended to be the
	 * operand that <i>will</i> be incremented. Passing the value after it has been incremented may cause the 
	 * operand to have been overflowed already.
	 * @param operand The value to be incremented.
	 * @return
	 */
    public static boolean assertSafeIncrement(int operand) {
        if (operand == Integer.MAX_VALUE) return false; else return true;
    }
}
