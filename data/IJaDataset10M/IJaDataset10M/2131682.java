package com.sun.org.apache.bcel.internal.generic;

/** 
 * Super class for GOTO
 *
 * @version $Id: GotoInstruction.java,v 1.1.2.1 2005/07/31 23:44:41 jeffsuttor Exp $
 * @author  <A HREF="mailto:markus.dahm@berlin.de">M. Dahm</A>
 */
public abstract class GotoInstruction extends BranchInstruction implements UnconditionalBranch {

    GotoInstruction(short opcode, InstructionHandle target) {
        super(opcode, target);
    }

    /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
    GotoInstruction() {
    }
}
