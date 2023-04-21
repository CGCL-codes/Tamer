package org.csiro.darjeeling.infuser.structure.visitors.replaceactions;

import org.apache.bcel.Constants;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InvokeInstruction;
import org.csiro.darjeeling.infuser.instructions.INVOKEINTERFACE;
import org.csiro.darjeeling.infuser.instructions.INVOKESPECIAL;
import org.csiro.darjeeling.infuser.instructions.INVOKESTATIC;
import org.csiro.darjeeling.infuser.instructions.INVOKEVIRTUAL;
import org.csiro.darjeeling.infuser.structure.LocalId;
import org.csiro.darjeeling.infuser.structure.elements.AbstractMethodDefinition;
import org.csiro.darjeeling.infuser.structure.elements.AbstractMethodImplementation;
import org.csiro.darjeeling.infuser.structure.elements.AbstractInfusion;
import org.csiro.darjeeling.infuser.structure.elements.internal.InternalMethodDefinition;
import org.csiro.darjeeling.infuser.structure.visitors.InstructionReplaceAction;

public class InvokeInstructionAction implements InstructionReplaceAction {

    private AbstractInfusion infusion;

    public InvokeInstructionAction(AbstractInfusion infusion) {
        this.infusion = infusion;
    }

    public LocalId lookupStaticMethod(InvokeInstruction oldInvoke, ConstantPoolGen cpg) {
        AbstractMethodDefinition methodDef = InternalMethodDefinition.fromInvokeInstruction(oldInvoke, cpg);
        AbstractMethodImplementation methodImpl = infusion.lookupMethodImplemention(methodDef, oldInvoke.getReferenceType(cpg).toString());
        if (methodImpl == null) throw new IllegalStateException("Method not found: " + methodDef.getDescriptor() + ":" + methodDef.getMethodName() + " in class " + oldInvoke.getReferenceType(cpg).toString());
        return methodImpl.getGlobalId().resolve(infusion);
    }

    public INVOKESTATIC replaceInvokeStatic(org.apache.bcel.generic.INVOKESTATIC oldInvoke, ConstantPoolGen cpg) {
        return new INVOKESTATIC(lookupStaticMethod(oldInvoke, cpg));
    }

    public INVOKESPECIAL replaceInvokeSpecial(org.apache.bcel.generic.INVOKESPECIAL oldInvoke, ConstantPoolGen cpg) {
        return new INVOKESPECIAL(lookupStaticMethod(oldInvoke, cpg));
    }

    public INVOKEVIRTUAL replaceInvokeVirtual(org.apache.bcel.generic.INVOKEVIRTUAL oldInvoke, ConstantPoolGen cpg) {
        AbstractMethodDefinition methodDef = infusion.lookupMethodDefinition(InternalMethodDefinition.fromInvokeInstruction(oldInvoke, cpg));
        return new INVOKEVIRTUAL(methodDef.getGlobalId().resolve(infusion), methodDef.getNrParameters());
    }

    public INVOKEINTERFACE replaceInvokeInterface(org.apache.bcel.generic.INVOKEINTERFACE oldInvoke, ConstantPoolGen cpg) {
        AbstractMethodDefinition methodDef = infusion.lookupMethodDefinition(InternalMethodDefinition.fromInvokeInstruction(oldInvoke, cpg));
        return new INVOKEINTERFACE(methodDef.getGlobalId().resolve(infusion), methodDef.getNrParameters());
    }

    public void replace(InstructionHandle handle, ConstantPoolGen cpg) {
        Instruction instruction = handle.getInstruction();
        switch(instruction.getOpcode()) {
            case Constants.INVOKESTATIC:
                handle.setInstruction(replaceInvokeStatic((org.apache.bcel.generic.INVOKESTATIC) instruction, cpg));
                break;
            case Constants.INVOKESPECIAL:
                handle.setInstruction(replaceInvokeSpecial((org.apache.bcel.generic.INVOKESPECIAL) instruction, cpg));
                break;
            case Constants.INVOKEVIRTUAL:
                handle.setInstruction(replaceInvokeVirtual((org.apache.bcel.generic.INVOKEVIRTUAL) instruction, cpg));
                break;
            case Constants.INVOKEINTERFACE:
                handle.setInstruction(replaceInvokeInterface((org.apache.bcel.generic.INVOKEINTERFACE) instruction, cpg));
                break;
            default:
                break;
        }
    }
}
