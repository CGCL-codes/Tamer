package csheets.ext.assertion;

import java.io.Serializable;
import java.util.Iterator;
import csheets.core.IllegalValueTypeException;
import csheets.core.Value;

/** Abstract class representing an assertion. This class contains the common
	data and functionality for assertion (USAssertion and SGAssertion).
	@author Peter Palotas
*/
public abstract class Assertion implements Serializable {

    /** Represents the intervals of allowed values for this assertion. */
    protected MultiInterval intervals = new MultiInterval();

    /** Indicates wether only integer values are allowed */
    protected boolean isInteger;

    /** Enumeration indicating the result of a validation of a value. */
    public enum Result {

        /** Indicates the assertion succeeded. */
        OK, /** Indicates the assertion failed. */
        FAILED, /** Indicates there was no data to validate. */
        NO_DATA, /** Indicates the assertion failed because the data was not a number. */
        NAN
    }

    ;

    /**
	 * The possible results when two assertions are compared.
	 * @see AssertableCell#assertAssertions()
	 */
    public enum ComparisonResult {

        /** Denotes that the assertions on this cell are okay */
        OK, /** Denotes that the user supplied assertion and the system generated assertion
		    associated with this cell do <i>not</i> represent the same range(s) of values. */
        NON_EQUAL, /** Denotes that the system generated assertion for this cell can not be generated because the
		    formula in this cell contains a possible illegal mathematic operation. Such as division by
		    zero or calculations with an imaginary result. (Due to either an error in the formula
		    itself or in the assertion(s) of its precedents. */
        ILLEGAL_INTERVAL {

            /** A message describing the error */
            private String errorMsg;

            public void setErrorMsg(String msg) {
                errorMsg = msg;
            }

            public String getErrorMsg() {
                return errorMsg;
            }
        }
        ;

        public void setErrorMsg(String msg) {
        }

        public String getErrorMsg() {
            return null;
        }
    }

    ;

    /** Returns an iterator over the intervals representing all allowed values in
	    this assertion. Note that this iterator cannot be used to modify the underlying
	    collection.
	    @return Returns an iterator over the intervals representing all allowed values in
	    this assertion. */
    public Iterator<Interval> getIntervalIterator() {
        return intervals.iterator();
    }

    /** Returns the MultiInterval specifying the valid values for this assertion.
		@return the MultiInterval specifying the valid values for this assertion.*/
    public MultiInterval getMultiInterval() {
        return intervals;
    }

    /** Checks if this assertion allows only integer values.
		@return <code>true</code> if this assertion will validate only integer values
			    successfully, and <code>false</code> otherwise. */
    public boolean allowsIntegersOnly() {
        return isInteger;
    }

    /**
	 * Checks if the current assertion holds for the given value. The value
	 * may be any object.
	 * @param value An arbritrary object. The value the assertion will be verified
	 *				against will be retrieved from the object using its <code>toString()</code> method.
	 * @return <ul>
				<li><code>Result.NO_DATA</code> if <code>value</code> is <code>null</code> or
					<code>value.toString()</code> returns an empty string.
				<li><code>Result.NAN</code> if the string returned by <code>value.toString()</code>
					does not represent a number parsable by <code>Double.parseDouble()</code>.
				<li><code>Result.OK</code> if assertion holds for the value.
				<li><code>Result.FAILED</code> if the assertion failed for the value, unless
					one of the reasons above.
			</ul>
	 */
    public Result validate(Value value) {
        if (value.toAny() == null) return Result.NO_DATA;
        try {
            return validate(value.toDouble());
        } catch (IllegalValueTypeException e) {
            return Result.NAN;
        }
    }

    /**
	 * Checks if the current assertion holds for the given value
	 * @return <ul>
			   <li><code>Result.OK</code> if the assertion holds for the value,
	 *		   <li><code>Result.NAN</code> if <code>value == Double.NaN</code>
	 *		   <li><code>Result.FAILED</code> otherwise.
			   </ul>
	 */
    public Result validate(double value) {
        if (Double.isNaN(value)) return Result.NAN;
        if (isInteger && (Math.ceil(value) != Math.floor(value))) {
            return Result.FAILED;
        }
        return intervals.contains(value) ? Result.OK : Result.FAILED;
    }

    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof Assertion)) return false;
        Assertion a = (Assertion) o;
        return intervals.equals(a.intervals);
    }

    public String toString() {
        String s = "";
        for (Iterator<Interval> iter = intervals.iterator(); iter.hasNext(); ) {
            Interval i = iter.next();
            s += i.toString();
            if (iter.hasNext()) s += " or ";
        }
        return s;
    }
}
