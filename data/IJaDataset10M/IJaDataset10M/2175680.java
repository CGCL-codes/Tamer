package net.sourceforge.dbtoolbox.visitor;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import net.sourceforge.dbtoolbox.model.DatabaseMD;
import net.sourceforge.dbtoolbox.model.ElementMD;
import net.sourceforge.dbtoolbox.model.PropertyMD;

/**
 * Metadata visitor which prints textually the content of database
 * @author gerald
 */
public class PrintMDVisitor extends MDVisitor {

    /**
     * Output stream
     */
    private PrintStream printStream;

    /**
     * Depth in the tree and prefix length
     */
    protected int depth;

    /**
     * Minimal constructor
     * @param printStream Print stream
     */
    public PrintMDVisitor(PrintStream printStream) {
        this.printStream = printStream;
    }

    protected void postPrintElement(ElementMD element) {
        printStream.println();
        depth++;
        super.visitElement(element);
        depth--;
    }

    protected void prePrintElement(ElementMD element) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            stringBuilder.append("\t");
        }
        stringBuilder.append("- ");
        printStream.print(stringBuilder.toString());
    }

    protected void printElement(ElementMD element) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(element.getElementType()).append(" ");
        stringBuilder.append(element.getName());
        printStream.print(stringBuilder.toString());
    }

    @Override
    public void visitDatabase(DatabaseMD database) {
        printStream.println("Database");
        super.visitDatabase(database);
    }

    @Override
    public void visitElement(ElementMD element) {
        prePrintElement(element);
        printElement(element);
        postPrintElement(element);
    }

    Set<String> propertyNames = new HashSet<String>(Arrays.asList("databaseProductName", "databaseProductVersion", "driverName", "driverVersion", "userName", "URL"));

    protected void printProperty(PropertyMD property) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Property ");
        stringBuilder.append(property.getName());
        stringBuilder.append("=").append(property.getStringValue());
        printStream.print(stringBuilder.toString());
    }

    @Override
    public void visitProperty(PropertyMD property) {
        if (propertyNames.contains(property.getName())) {
            prePrintElement(property);
            printProperty(property);
            postPrintElement(property);
        }
    }
}
