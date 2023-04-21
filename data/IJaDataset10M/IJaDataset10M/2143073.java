package oracle.toplink.libraries.asm.tree;

import oracle.toplink.libraries.asm.CodeVisitor;

/**
 * A node that represents a bytecode instruction.
 * 
 * @author Eric Bruneton
 */
public abstract class AbstractInsnNode {

    /**
   * The opcode of this instruction.
   */
    protected int opcode;

    /**
   * Constructs a new {@link AbstractInsnNode AbstractInsnNode} object.
   *
   * @param opcode the opcode of the instruction to be constructed.
   */
    protected AbstractInsnNode(final int opcode) {
        this.opcode = opcode;
    }

    /**
   * Returns the opcode of this instruction.
   *
   * @return the opcode of this instruction.
   */
    public int getOpcode() {
        return opcode;
    }

    /**
   * Makes the given code visitor visit this instruction.
   *
   * @param cv a code visitor.
   */
    public abstract void accept(final CodeVisitor cv);
}
