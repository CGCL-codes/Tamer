package proguard.shrink;

import proguard.classfile.*;
import proguard.classfile.util.*;
import proguard.classfile.visitor.*;
import java.io.*;

/**
 * This ClassFileVisitor prints out the class files and class members that have been
 * marked as being used (or not used).
 *
 * @see UsageMarker
 *
 * @author Eric Lafortune
 */
public class UsagePrinter implements ClassFileVisitor, MemberInfoVisitor {

    private boolean printUnusedItems;

    private PrintStream ps;

    private String className;

    /**
     * Creates a new UsagePrinter that prints to <code>System.out</code>.
     * @param printUsedItems a flag that indicates whether only unused items
     * should be printed, or alternatively, only used items.
     */
    public UsagePrinter(boolean printUnusedItems) {
        this(printUnusedItems, System.out);
    }

    /**
     * Creates a new UsagePrinter that prints to the given stream.
     * @param printUsedItems a flag that indicates whether only unused items
     * should be printed, or alternatively, only used items.
     * @param printStream the stream to which to print
     */
    public UsagePrinter(boolean printUnusedItems, PrintStream printStream) {
        this.printUnusedItems = printUnusedItems;
        this.ps = printStream;
    }

    public void visitProgramClassFile(ProgramClassFile programClassFile) {
        if (UsageMarker.isUsed(programClassFile)) {
            if (printUnusedItems) {
                className = programClassFile.getName();
                programClassFile.fieldsAccept(this);
                programClassFile.methodsAccept(this);
                className = null;
            } else {
                ps.println(ClassUtil.externalClassName(programClassFile.getName()));
            }
        } else {
            if (printUnusedItems) {
                ps.println(ClassUtil.externalClassName(programClassFile.getName()));
            }
        }
    }

    public void visitLibraryClassFile(LibraryClassFile libraryClassFile) {
    }

    public void visitProgramFieldInfo(ProgramClassFile programClassFile, ProgramFieldInfo programFieldInfo) {
        if (UsageMarker.isUsed(programFieldInfo) ^ printUnusedItems) {
            printClassNameHeader();
            ps.println("    " + lineNumberRange(programClassFile, programFieldInfo) + ClassUtil.externalFullFieldDescription(programFieldInfo.getAccessFlags(), programFieldInfo.getName(programClassFile), programFieldInfo.getDescriptor(programClassFile)));
        }
    }

    public void visitProgramMethodInfo(ProgramClassFile programClassFile, ProgramMethodInfo programMethodInfo) {
        if (UsageMarker.isUsed(programMethodInfo) ^ printUnusedItems) {
            printClassNameHeader();
            ps.println("    " + lineNumberRange(programClassFile, programMethodInfo) + ClassUtil.externalFullMethodDescription(programClassFile.getName(), programMethodInfo.getAccessFlags(), programMethodInfo.getName(programClassFile), programMethodInfo.getDescriptor(programClassFile)));
        }
    }

    public void visitLibraryFieldInfo(LibraryClassFile libraryClassFile, LibraryFieldInfo libraryFieldInfo) {
    }

    public void visitLibraryMethodInfo(LibraryClassFile libraryClassFile, LibraryMethodInfo libraryMethodInfo) {
    }

    /**
     * Prints the class name field. The field is then cleared, so it is not
     * printed again.
     */
    private void printClassNameHeader() {
        if (className != null) {
            ps.println(ClassUtil.externalClassName(className) + ":");
            className = null;
        }
    }

    /**
     * Returns the line number range of the given class member, followed by a
     * colon, or just an empty String if no range is available.
     */
    private static String lineNumberRange(ProgramClassFile programClassFile, ProgramMemberInfo programMemberInfo) {
        String range = programMemberInfo.getLineNumberRange(programClassFile);
        return range != null ? (range + ":") : "";
    }
}
