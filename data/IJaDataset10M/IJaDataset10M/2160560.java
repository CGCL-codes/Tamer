package com.sun.org.apache.bcel.internal.generic;

/** 
 * CALOAD - Load char from array
 * <PRE>Stack: ..., arrayref, index -&gt; ..., value</PRE>
 *
 * @version $Id: CALOAD.java,v 1.1.2.1 2005/07/31 23:45:13 jeffsuttor Exp $
 * @author  <A HREF="mailto:markus.dahm@berlin.de">M. Dahm</A>
 */
public class CALOAD extends ArrayInstruction implements StackProducer {

    /** Load char from array
   */
    public CALOAD() {
        super(com.sun.org.apache.bcel.internal.Constants.CALOAD);
    }

    /**
   * Call corresponding visitor method(s). The order is:
   * Call visitor methods of implemented interfaces first, then
   * call methods according to the class hierarchy in descending order,
   * i.e., the most specific visitXXX() call comes last.
   *
   * @param v Visitor object
   */
    public void accept(Visitor v) {
        v.visitStackProducer(this);
        v.visitExceptionThrower(this);
        v.visitTypedInstruction(this);
        v.visitArrayInstruction(this);
        v.visitCALOAD(this);
    }
}
