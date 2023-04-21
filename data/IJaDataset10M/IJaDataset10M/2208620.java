package proguard.classfile.visitor;

import proguard.classfile.*;

/**
 * This interface specifies the methods for a visitor of
 * <code>Clazz</code> objects.
 *
 * @author Eric Lafortune
 */
public interface ClassVisitor {

    public void visitProgramClass(ProgramClass programClass);

    public void visitLibraryClass(LibraryClass libraryClass);
}
