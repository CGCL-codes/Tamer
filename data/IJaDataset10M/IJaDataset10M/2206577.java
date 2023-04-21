package com.sun.org.apache.bcel.internal.generic;

/** 
 * IFGE - Branch if int comparison with zero succeeds
 *
 * <PRE>Stack: ..., value -&gt; ...</PRE>
 *
 * @version $Id: IFGE.java,v 1.1.2.1 2005/07/31 23:44:50 jeffsuttor Exp $
 * @author  <A HREF="mailto:markus.dahm@berlin.de">M. Dahm</A>
 */
public class IFGE extends IfInstruction {

    /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
    IFGE() {
    }

    public IFGE(InstructionHandle target) {
        super(com.sun.org.apache.bcel.internal.Constants.IFGE, target);
    }

    /**
   * @return negation of instruction
   */
    public IfInstruction negate() {
        return new IFLT(target);
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
        v.visitStackConsumer(this);
        v.visitBranchInstruction(this);
        v.visitIfInstruction(this);
        v.visitIFGE(this);
    }
}
