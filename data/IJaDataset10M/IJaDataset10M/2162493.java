package proguard.optimize.peephole;

import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.visitor.*;
import proguard.classfile.instruction.*;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.util.SimplifiedVisitor;

/**
 * This AttributeVisitor finds all instruction offsets, branch targets, and
 * exception targets in the CodeAttribute objects that it visits.
 *
 * @author Eric Lafortune
 */
public class ReachableCodeMarker extends SimplifiedVisitor implements AttributeVisitor, InstructionVisitor, ExceptionInfoVisitor {

    private boolean[] isReachable = new boolean[ClassConstants.TYPICAL_CODE_LENGTH];

    private boolean next;

    private boolean evaluateExceptions;

    /**
     * Returns whether the instruction at the given offset is reachable in
     * the CodeAttribute that was visited most recently.
     */
    public boolean isReachable(int offset) {
        return isReachable[offset];
    }

    /**
     * Returns whether any of the instructions at the given offsets are
     * reachable in the CodeAttribute that was visited most recently.
     */
    public boolean isReachable(int startOffset, int endOffset) {
        for (int offset = startOffset; offset < endOffset; offset++) {
            if (isReachable[offset]) {
                return true;
            }
        }
        return false;
    }

    public void visitAnyAttribute(Clazz clazz, Attribute attribute) {
    }

    public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute) {
        int codeLength = codeAttribute.u4codeLength;
        if (isReachable.length < codeLength) {
            isReachable = new boolean[codeLength];
        } else {
            for (int index = 0; index < codeLength; index++) {
                isReachable[index] = false;
            }
        }
        markCode(clazz, method, codeAttribute, 0);
        do {
            evaluateExceptions = false;
            codeAttribute.exceptionsAccept(clazz, method, this);
        } while (evaluateExceptions);
    }

    public void visitSimpleInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, SimpleInstruction simpleInstruction) {
        byte opcode = simpleInstruction.opcode;
        if (opcode == InstructionConstants.OP_IRETURN || opcode == InstructionConstants.OP_LRETURN || opcode == InstructionConstants.OP_FRETURN || opcode == InstructionConstants.OP_DRETURN || opcode == InstructionConstants.OP_ARETURN || opcode == InstructionConstants.OP_RETURN || opcode == InstructionConstants.OP_ATHROW) {
            next = false;
        }
    }

    public void visitConstantInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, ConstantInstruction constantInstruction) {
    }

    public void visitVariableInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, VariableInstruction variableInstruction) {
        if (variableInstruction.opcode == InstructionConstants.OP_RET) {
            next = false;
        }
    }

    public void visitBranchInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, BranchInstruction branchInstruction) {
        markBranchTarget(clazz, method, codeAttribute, offset + branchInstruction.branchOffset);
        byte opcode = branchInstruction.opcode;
        if (opcode == InstructionConstants.OP_GOTO || opcode == InstructionConstants.OP_GOTO_W) {
            next = false;
        }
    }

    public void visitAnySwitchInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, SwitchInstruction switchInstruction) {
        markBranchTarget(clazz, method, codeAttribute, offset + switchInstruction.defaultOffset);
        markBranchTargets(clazz, method, codeAttribute, offset, switchInstruction.jumpOffsets);
        next = false;
    }

    public void visitExceptionInfo(Clazz clazz, Method method, CodeAttribute codeAttribute, ExceptionInfo exceptionInfo) {
        if (!isReachable(exceptionInfo.u2handlerPC) && isReachable(exceptionInfo.u2startPC, exceptionInfo.u2endPC)) {
            markCode(clazz, method, codeAttribute, exceptionInfo.u2handlerPC);
            evaluateExceptions = true;
        }
    }

    /**
     * Marks the branch targets of the given jump offsets for the instruction
     * at the given offset.
     */
    private void markBranchTargets(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, int[] jumpOffsets) {
        for (int index = 0; index < jumpOffsets.length; index++) {
            markCode(clazz, method, codeAttribute, offset + jumpOffsets[index]);
        }
    }

    /**
     * Marks the branch target at the given offset.
     */
    private void markBranchTarget(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset) {
        boolean oldNext = next;
        markCode(clazz, method, codeAttribute, offset);
        next = oldNext;
    }

    /**
     * Marks the code starting at the given offset.
     */
    private void markCode(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset) {
        boolean oldNext = next;
        byte[] code = codeAttribute.code;
        while (!isReachable[offset]) {
            Instruction instruction = InstructionFactory.create(code, offset);
            isReachable[offset] = true;
            next = true;
            instruction.accept(clazz, method, codeAttribute, offset, this);
            if (!next) {
                break;
            }
            offset += instruction.length(offset);
        }
        next = oldNext;
    }
}
