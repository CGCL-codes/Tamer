package proguard.optimize;

import proguard.classfile.*;
import proguard.classfile.visitor.*;

/**
 * This ClassFileVisitor marks all methods that can not be made private in the
 * classes that it visits, and in the classes to which they refer.
 *
 * @author Eric Lafortune
 */
public class NonPrivateMethodMarker implements ClassFileVisitor, CpInfoVisitor, MemberInfoVisitor {

    private MethodImplementationFilter filteredMethodMarker = new MethodImplementationFilter(this);

    public void visitProgramClassFile(ProgramClassFile programClassFile) {
        programClassFile.methodAccept(ClassConstants.INTERNAL_METHOD_NAME_CLINIT, ClassConstants.INTERNAL_METHOD_TYPE_CLINIT, this);
        programClassFile.methodAccept(ClassConstants.INTERNAL_METHOD_NAME_INIT, ClassConstants.INTERNAL_METHOD_TYPE_INIT, this);
        programClassFile.constantPoolEntriesAccept(this);
        programClassFile.methodsAccept(filteredMethodMarker);
    }

    public void visitLibraryClassFile(LibraryClassFile libraryClassFile) {
        libraryClassFile.methodsAccept(this);
    }

    public void visitIntegerCpInfo(ClassFile classFile, IntegerCpInfo integerCpInfo) {
    }

    public void visitLongCpInfo(ClassFile classFile, LongCpInfo longCpInfo) {
    }

    public void visitFloatCpInfo(ClassFile classFile, FloatCpInfo floatCpInfo) {
    }

    public void visitDoubleCpInfo(ClassFile classFile, DoubleCpInfo doubleCpInfo) {
    }

    public void visitStringCpInfo(ClassFile classFile, StringCpInfo stringCpInfo) {
    }

    public void visitUtf8CpInfo(ClassFile classFile, Utf8CpInfo utf8CpInfo) {
    }

    public void visitFieldrefCpInfo(ClassFile classFile, FieldrefCpInfo fieldrefCpInfo) {
    }

    public void visitClassCpInfo(ClassFile classFile, ClassCpInfo classCpInfo) {
    }

    public void visitNameAndTypeCpInfo(ClassFile classFile, NameAndTypeCpInfo nameAndTypeCpInfo) {
    }

    public void visitInterfaceMethodrefCpInfo(ClassFile classFile, InterfaceMethodrefCpInfo interfaceMethodrefCpInfo) {
        interfaceMethodrefCpInfo.referencedMemberInfoAccept(this);
    }

    public void visitMethodrefCpInfo(ClassFile classFile, MethodrefCpInfo methodrefCpInfo) {
        ClassFile referencedClassFile = methodrefCpInfo.referencedClassFile;
        if (referencedClassFile != null && !referencedClassFile.equals(classFile)) {
            methodrefCpInfo.referencedMemberInfoAccept(this);
        }
    }

    public void visitProgramFieldInfo(ProgramClassFile programClassFile, ProgramFieldInfo programFieldInfo) {
    }

    public void visitProgramMethodInfo(ProgramClassFile programClassFile, ProgramMethodInfo programMethodInfo) {
        markCanNotBeMadePrivate(programMethodInfo);
    }

    public void visitLibraryFieldInfo(LibraryClassFile libraryClassFile, LibraryFieldInfo libraryFieldInfo) {
    }

    public void visitLibraryMethodInfo(LibraryClassFile libraryClassFile, LibraryMethodInfo libraryMethodInfo) {
        markCanNotBeMadePrivate(libraryMethodInfo);
    }

    public static void markCanNotBeMadePrivate(MethodInfo methodInfo) {
        MethodOptimizationInfo info = MethodOptimizationInfo.getMethodOptimizationInfo(methodInfo);
        if (info != null) {
            info.setCanNotBeMadePrivate();
        }
    }

    public static boolean canBeMadePrivate(MethodInfo methodInfo) {
        MethodOptimizationInfo info = MethodOptimizationInfo.getMethodOptimizationInfo(methodInfo);
        return info != null && info.canBeMadePrivate();
    }
}
