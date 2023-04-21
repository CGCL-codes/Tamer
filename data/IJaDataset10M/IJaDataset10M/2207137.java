package proguard.classfile.attribute.preverification;

import proguard.classfile.*;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.preverification.visitor.VerificationTypeVisitor;

/**
 * This VerificationType represents a UninitializedThis type.
 *
 * @author Eric Lafortune
 */
public class UninitializedThisType extends VerificationType {

    public int getTag() {
        return UNINITIALIZED_THIS_TYPE;
    }

    public void accept(Clazz clazz, Method method, CodeAttribute codeAttribute, int instructionOffset, VerificationTypeVisitor verificationTypeVisitor) {
        verificationTypeVisitor.visitUninitializedThisType(clazz, method, codeAttribute, instructionOffset, this);
    }

    public void stackAccept(Clazz clazz, Method method, CodeAttribute codeAttribute, int instructionOffset, int stackIndex, VerificationTypeVisitor verificationTypeVisitor) {
        verificationTypeVisitor.visitStackUninitializedThisType(clazz, method, codeAttribute, instructionOffset, stackIndex, this);
    }

    public void variablesAccept(Clazz clazz, Method method, CodeAttribute codeAttribute, int instructionOffset, int variableIndex, VerificationTypeVisitor verificationTypeVisitor) {
        verificationTypeVisitor.visitVariablesUninitializedThisType(clazz, method, codeAttribute, instructionOffset, variableIndex, this);
    }

    public String toString() {
        return "u:this";
    }
}
