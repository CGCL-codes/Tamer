package org.mockito.asm.tree;

import java.util.Map;
import org.mockito.asm.MethodVisitor;

/**
 * A node that represents a type instruction. A type instruction is an
 * instruction that takes a type descriptor as parameter.
 * 
 * @author Eric Bruneton
 */
public class TypeInsnNode extends AbstractInsnNode {

    /**
     * The operand of this instruction. This operand is an internal name (see
     * {@link org.mockito.asm.Type}).
     */
    public String desc;

    /**
     * Constructs a new {@link TypeInsnNode}.
     * 
     * @param opcode the opcode of the type instruction to be constructed. This
     *        opcode must be NEW, ANEWARRAY, CHECKCAST or INSTANCEOF.
     * @param desc the operand of the instruction to be constructed. This
     *        operand is an internal name (see {@link org.mockito.asm.Type}).
     */
    public TypeInsnNode(final int opcode, final String desc) {
        super(opcode);
        this.desc = desc;
    }

    /**
     * Sets the opcode of this instruction.
     * 
     * @param opcode the new instruction opcode. This opcode must be NEW,
     *        ANEWARRAY, CHECKCAST or INSTANCEOF.
     */
    public void setOpcode(final int opcode) {
        this.opcode = opcode;
    }

    public int getType() {
        return TYPE_INSN;
    }

    public void accept(final MethodVisitor mv) {
        mv.visitTypeInsn(opcode, desc);
    }

    public AbstractInsnNode clone(final Map labels) {
        return new TypeInsnNode(opcode, desc);
    }
}
