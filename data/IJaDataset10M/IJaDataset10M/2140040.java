package proguard.classfile.editor;

import proguard.classfile.*;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.util.SimplifiedVisitor;
import proguard.classfile.visitor.MemberVisitor;

/**
 * This ConstantVisitor adds all class members that it visits to the given
 * target class.
 *
 * @author Eric Lafortune
 */
public class MemberAdder extends SimplifiedVisitor implements MemberVisitor {

    private static final boolean DEBUG = false;

    private static final Attribute[] EMPTY_ATTRIBUTES = new Attribute[0];

    private final ProgramClass targetClass;

    private final ConstantAdder constantAdder;

    private final ClassEditor classEditor;

    private final ConstantPoolEditor constantPoolEditor;

    public MemberAdder(ProgramClass targetClass) {
        this.targetClass = targetClass;
        constantAdder = new ConstantAdder(targetClass);
        classEditor = new ClassEditor(targetClass);
        constantPoolEditor = new ConstantPoolEditor(targetClass);
    }

    public void visitProgramField(ProgramClass programClass, ProgramField programField) {
        String name = programField.getName(programClass);
        String descriptor = programField.getDescriptor(programClass);
        int accessFlags = programField.getAccessFlags();
        ProgramField targetField = (ProgramField) targetClass.findField(name, descriptor);
        if (targetField != null) {
            int targetAccessFlags = targetField.getAccessFlags();
            if ((targetAccessFlags & (ClassConstants.INTERNAL_ACC_PRIVATE | ClassConstants.INTERNAL_ACC_STATIC)) != 0) {
                if (DEBUG) {
                    System.out.println("MemberAdder: renaming field [" + targetClass + "." + targetField.getName(targetClass) + " " + targetField.getDescriptor(targetClass) + "]");
                }
                targetField.u2nameIndex = constantPoolEditor.addUtf8Constant(newUniqueMemberName(name, targetClass.getName()));
            }
        }
        if (DEBUG) {
            System.out.println("MemberAdder: copying field [" + programClass + "." + programField.getName(programClass) + " " + programField.getDescriptor(programClass) + "] into [" + targetClass.getName() + "]");
        }
        ProgramField newProgramField = new ProgramField(accessFlags, constantAdder.addConstant(programClass, programField.u2nameIndex), constantAdder.addConstant(programClass, programField.u2descriptorIndex), 0, programField.u2attributesCount > 0 ? new Attribute[programField.u2attributesCount] : EMPTY_ATTRIBUTES, programField.referencedClass);
        newProgramField.setVisitorInfo(programField);
        programField.attributesAccept(programClass, new AttributeAdder(targetClass, newProgramField, false));
        classEditor.addField(newProgramField);
    }

    public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod) {
        String name = programMethod.getName(programClass);
        String descriptor = programMethod.getDescriptor(programClass);
        int accessFlags = programMethod.getAccessFlags();
        ProgramMethod targetMethod = (ProgramMethod) targetClass.findMethod(name, descriptor);
        if (targetMethod != null) {
            if ((accessFlags & ClassConstants.INTERNAL_ACC_ABSTRACT) != 0) {
                if (DEBUG) {
                    System.out.println("MemberAdder: skipping abstract method [" + programClass.getName() + "." + programMethod.getName(programClass) + programMethod.getDescriptor(programClass) + "] into [" + targetClass.getName() + "]");
                }
                return;
            }
            int targetAccessFlags = targetMethod.getAccessFlags();
            if ((targetAccessFlags & ClassConstants.INTERNAL_ACC_ABSTRACT) != 0) {
                if (DEBUG) {
                    System.out.println("MemberAdder: updating method [" + programClass.getName() + "." + programMethod.getName(programClass) + programMethod.getDescriptor(programClass) + "] into [" + targetClass.getName() + "]");
                }
                targetMethod.u2accessFlags = accessFlags & ~ClassConstants.INTERNAL_ACC_FINAL;
                programMethod.attributesAccept(programClass, new AttributeAdder(targetClass, targetMethod, true));
                return;
            }
            if (DEBUG) {
                System.out.println("MemberAdder: renaming method [" + targetClass.getName() + "." + targetMethod.getName(targetClass) + targetMethod.getDescriptor(targetClass) + "]");
            }
            targetMethod.u2nameIndex = constantPoolEditor.addUtf8Constant(newUniqueMemberName(name, descriptor));
        }
        if (DEBUG) {
            System.out.println("MemberAdder: copying method [" + programClass.getName() + "." + programMethod.getName(programClass) + programMethod.getDescriptor(programClass) + "] into [" + targetClass.getName() + "]");
        }
        ProgramMethod newProgramMethod = new ProgramMethod(accessFlags & ~ClassConstants.INTERNAL_ACC_FINAL, constantAdder.addConstant(programClass, programMethod.u2nameIndex), constantAdder.addConstant(programClass, programMethod.u2descriptorIndex), 0, programMethod.u2attributesCount > 0 ? new Attribute[programMethod.u2attributesCount] : EMPTY_ATTRIBUTES, programMethod.referencedClasses != null ? (Clazz[]) programMethod.referencedClasses.clone() : null);
        newProgramMethod.setVisitorInfo(programMethod);
        programMethod.attributesAccept(programClass, new AttributeAdder(targetClass, newProgramMethod, false));
        classEditor.addMethod(newProgramMethod);
    }

    /**
     * Returns a unique class member name, based on the given name and descriptor.
     */
    private String newUniqueMemberName(String name, String descriptor) {
        return name.equals(ClassConstants.INTERNAL_METHOD_NAME_INIT) ? ClassConstants.INTERNAL_METHOD_NAME_INIT : name + ClassConstants.SPECIAL_MEMBER_SEPARATOR + Long.toHexString(Math.abs((descriptor).hashCode()));
    }
}
