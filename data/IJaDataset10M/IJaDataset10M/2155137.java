package org.inxar.jenesis;

/**
 * <code>Expression</code> subinterface for array initializers.  The
 * array initialization expression has a few interesting features.
 * Notice, the argument is <code>Object</code>.  The contract is that
 * the <code>Object</code> must be of runtime-type
 * <code>Expression</code> or, more likely, <code>Object[]</code>.  If
 * the type is <code>Object[]</code>, then each element of that array
 * will recursively be introspected, checking again to see if the type
 * is <code>Expression</code> or <code>Object[]</code>.  In this
 * manner, one can arbitratily nest expressions.  This is the only way
 * I know how to accomplish this behavior (ie not knowing the
 * dimensionality of an argument at compile-time).
 * 
 * <P>
 *
 * For example, say we wanted to make the array:
 * 
 * <PRE>
 * int[][] aai = { {1, 2} , {3, 4} };
 * </PRE>
 * One could do this by:
 * <PRE>
 * Expression[] a1 = new Expression[2];
 * a1[0] = new IntLiteralImpl(1);
 * a1[1] = new IntLiteralImpl(2);
 * Expression[] a2 = new Expression[2];
 * a2[0] = new IntLiteralImpl(3);
 * a2[1] = new IntLiteralImpl(4);
 * Object[] a3 = new Object[2];
 * a3[0] = a1;
 * a3[1] = a2;
 * ArrayDeclarationStatement asd = new ArrayDeclarationStatementImpl(); // fictional implementation
 * asd.setInitialization(a3);
 * </PRE> 
 */
public interface ArrayInitializer extends Expression {

    /**
     * Gets the array initialization expressions.
     */
    Object getArgs();

    /**
     * Sets the array initialization expressions as an arbitrarily
     * nested array of <code>Expression[]</code> <i>or</i> as a single
     * <code>Expression</code>.  
     */
    void setArgs(Object o);
}
