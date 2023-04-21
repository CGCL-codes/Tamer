package com.sun.org.apache.bcel.internal.generic;

/** 
 * DNEG - Negate double
 * <PRE>Stack: ..., value.word1, value.word2 -&gt; ..., result.word1, result.word2</PRE>
 *
 * @version $Id: DNEG.java,v 1.1.2.1 2005/07/31 23:45:17 jeffsuttor Exp $
 * @author  <A HREF="mailto:markus.dahm@berlin.de">M. Dahm</A>
 */
public class DNEG extends ArithmeticInstruction {

    public DNEG() {
        super(com.sun.org.apache.bcel.internal.Constants.DNEG);
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
        v.visitTypedInstruction(this);
        v.visitStackProducer(this);
        v.visitStackConsumer(this);
        v.visitArithmeticInstruction(this);
        v.visitDNEG(this);
    }
}
