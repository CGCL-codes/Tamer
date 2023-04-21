package proguard.classfile.visitor;

import proguard.classfile.*;
import proguard.classfile.visitor.*;

/**
 * This ClassFileVisitor counts the number of classes that has been visited.
 *
 * @author Eric Lafortune
 */
public class ClassCounter implements ClassFileVisitor {

    private int count;

    /**
     * Returns the number of classes that has been visited so far.
     */
    public int getCount() {
        return count;
    }

    public void visitLibraryClassFile(LibraryClassFile libraryClassFile) {
        count++;
    }

    public void visitProgramClassFile(ProgramClassFile programClassFile) {
        count++;
    }
}
