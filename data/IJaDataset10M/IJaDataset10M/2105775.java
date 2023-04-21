package proguard.optimize.info;

import proguard.classfile.*;
import proguard.classfile.constant.*;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.util.SimplifiedVisitor;
import proguard.classfile.visitor.*;

/**
 * This ClassVisitor marks all class members that can not be made private in the
 * classes that it visits, and in the classes to which they refer.
 *
 * @author Eric Lafortune
 */
public class NonPrivateMemberMarker extends SimplifiedVisitor implements ClassVisitor, ConstantVisitor, MemberVisitor {

    private final MethodImplementationFilter filteredMethodMarker = new MethodImplementationFilter(this);

    public void visitProgramClass(ProgramClass programClass) {
        programClass.constantPoolEntriesAccept(this);
        programClass.methodAccept(ClassConstants.INTERNAL_METHOD_NAME_CLINIT, ClassConstants.INTERNAL_METHOD_TYPE_CLINIT, this);
        programClass.methodAccept(ClassConstants.INTERNAL_METHOD_NAME_INIT, ClassConstants.INTERNAL_METHOD_TYPE_INIT, this);
        programClass.methodsAccept(filteredMethodMarker);
    }

    public void visitLibraryClass(LibraryClass libraryClass) {
        libraryClass.methodsAccept(this);
    }

    public void visitAnyConstant(Clazz clazz, Constant constant) {
    }

    public void visitStringConstant(Clazz clazz, StringConstant stringConstant) {
        Clazz referencedClass = stringConstant.referencedClass;
        if (referencedClass != null && !referencedClass.equals(clazz)) {
            stringConstant.referencedMemberAccept(this);
        }
    }

    public void visitAnyRefConstant(Clazz clazz, RefConstant refConstant) {
        Clazz referencedClass = refConstant.referencedClass;
        if (referencedClass != null && !referencedClass.equals(clazz) || !refConstant.getClassName(clazz).equals(clazz.getName())) {
            refConstant.referencedMemberAccept(this);
        }
    }

    public void visitProgramField(ProgramClass programClass, ProgramField programField) {
        markCanNotBeMadePrivate(programField);
    }

    public void visitLibraryField(LibraryClass libraryClass, LibraryField libraryField) {
        markCanNotBeMadePrivate(libraryField);
    }

    public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod) {
        markCanNotBeMadePrivate(programMethod);
    }

    public void visitLibraryMethod(LibraryClass libraryClass, LibraryMethod libraryMethod) {
        markCanNotBeMadePrivate(libraryMethod);
    }

    private static void markCanNotBeMadePrivate(Field field) {
        FieldOptimizationInfo info = FieldOptimizationInfo.getFieldOptimizationInfo(field);
        if (info != null) {
            info.setCanNotBeMadePrivate();
        }
    }

    /**
     * Returns whether the given field can be made private.
     */
    public static boolean canBeMadePrivate(Field field) {
        FieldOptimizationInfo info = FieldOptimizationInfo.getFieldOptimizationInfo(field);
        return info != null && info.canBeMadePrivate();
    }

    private static void markCanNotBeMadePrivate(Method method) {
        MethodOptimizationInfo info = MethodOptimizationInfo.getMethodOptimizationInfo(method);
        if (info != null) {
            info.setCanNotBeMadePrivate();
        }
    }

    /**
     * Returns whether the given method can be made private.
     */
    public static boolean canBeMadePrivate(Method method) {
        MethodOptimizationInfo info = MethodOptimizationInfo.getMethodOptimizationInfo(method);
        return info != null && info.canBeMadePrivate();
    }
}
