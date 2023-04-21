package org.inxar.jenesis;

/**
 * <code>Statement</code> subinterface for the <code>try catch
 * finally</code> construct.  
 */
public interface Try extends Statement, Block {

    /**
     * Gets the list of catches as an iterator of <code>Catch</code>.
     */
    Iterator getCatches();

    /**
     * Gets the optional <code>Finally</code> statement.
     */
    Finally getFinally();

    /**
     * Adds a new <code>Catch</code> statement to this <code>Try</code>.
     */
    Catch newCatch(Type type, String name);
}
