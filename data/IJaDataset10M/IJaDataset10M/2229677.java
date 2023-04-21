package proguard.classfile.visitor;

import proguard.classfile.*;

/**
 * This ClassFileVisitor lets a given MemberInfoVisitor visit all MemberInfo
 * objects of the class files it visits.
 *
 * @author Eric Lafortune
 */
public class AllMemberInfoVisitor implements ClassFileVisitor {

    private MemberInfoVisitor memberInfoVisitor;

    public AllMemberInfoVisitor(MemberInfoVisitor memberInfoVisitor) {
        this.memberInfoVisitor = memberInfoVisitor;
    }

    public void visitProgramClassFile(ProgramClassFile programClassFile) {
        programClassFile.fieldsAccept(memberInfoVisitor);
        programClassFile.methodsAccept(memberInfoVisitor);
    }

    public void visitLibraryClassFile(LibraryClassFile libraryClassFile) {
        libraryClassFile.fieldsAccept(memberInfoVisitor);
        libraryClassFile.methodsAccept(memberInfoVisitor);
    }
}
