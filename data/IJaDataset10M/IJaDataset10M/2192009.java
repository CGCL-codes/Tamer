package proguard.classfile.visitor;

import proguard.classfile.*;

/**
 * This ClassFileVisitor lets a given MemberInfoVisitor visit all MethodMemberInfo
 * objects of the class files it visits.
 *
 * @author Eric Lafortune
 */
public class AllMethodVisitor implements ClassFileVisitor {

    private MemberInfoVisitor memberInfoVisitor;

    public AllMethodVisitor(MemberInfoVisitor memberInfoVisitor) {
        this.memberInfoVisitor = memberInfoVisitor;
    }

    public void visitProgramClassFile(ProgramClassFile programClassFile) {
        programClassFile.methodsAccept(memberInfoVisitor);
    }

    public void visitLibraryClassFile(LibraryClassFile libraryClassFile) {
        libraryClassFile.methodsAccept(memberInfoVisitor);
    }
}
