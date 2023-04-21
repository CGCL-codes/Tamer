package proguard.classfile.attribute;

import proguard.classfile.*;
import proguard.classfile.visitor.*;

/**
 * This MemberInfoVisitor lets a given AttrInfoVisitor visit all AttrInfo
 * objects of the program class members it visits.
 *
 * @author Eric Lafortune
 */
public class AllAttrInfoVisitor implements MemberInfoVisitor {

    private AttrInfoVisitor attrInfoVisitor;

    public AllAttrInfoVisitor(AttrInfoVisitor attrInfoVisitor) {
        this.attrInfoVisitor = attrInfoVisitor;
    }

    public void visitProgramFieldInfo(ProgramClassFile programClassFile, ProgramFieldInfo programFieldInfo) {
        programFieldInfo.attributesAccept(programClassFile, attrInfoVisitor);
    }

    public void visitProgramMethodInfo(ProgramClassFile programClassFile, ProgramMethodInfo programMethodInfo) {
        programMethodInfo.attributesAccept(programClassFile, attrInfoVisitor);
    }

    public void visitLibraryFieldInfo(LibraryClassFile libraryClassFile, LibraryFieldInfo libraryFieldInfo) {
    }

    public void visitLibraryMethodInfo(LibraryClassFile libraryClassFile, LibraryMethodInfo libraryMethodInfo) {
    }
}
