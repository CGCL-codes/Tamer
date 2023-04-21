package com.thesett.aima.logic.fol;

/**
 * IntegerType is the base type for all integer numeric literals.
 *
 * <pre><p/><table id="crc"><caption>CRC Card</caption>
 * <tr><th> Responsibilities <th> Collaborations
 * <tr><td> Indicate that a term is an integer.
 * <tr><td> Indicate that a term is not a real number.
 * </table></pre>
 *
 * @author Rupert Smith
 */
public abstract class IntegerType extends NumericType {

    /**
     * Determines whether a number is an integer.
     *
     * @return <tt>true</tt> if a number is an integer, <tt>false</tt> otherwise.
     */
    public boolean isInteger() {
        return true;
    }

    /** {@inheritDoc} */
    public void accept(TermVisitor visitor) {
        if (visitor instanceof IntegerTypeVisitor) {
            ((IntegerTypeVisitor) visitor).visit(this);
        } else {
            super.accept(visitor);
        }
    }
}
