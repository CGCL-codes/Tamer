package net.sourceforge.jenesis4java;

/**
 * <code>Literal</code> subinterface for <code>byte</code> literals.
 */
public interface ByteLiteral extends Literal {

    /**
     * Returns the <code>byte</code> value of the <code>Literal</code>.
     */
    byte toByte();
}
