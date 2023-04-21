package net.sourceforge.transmogrify.symtab.printer;

import net.sourceforge.transmogrify.symtab.parser.SymTabAST;
import java.io.*;

/**
 * prints the extends clause of class or interface
 */
public class ExtendsPrinter extends ChildIgnoringPrinter {

    public ExtendsPrinter(SymTabAST nodeToPrint) {
        super(nodeToPrint);
    }

    /**
   * prints the extends clause of a class or interface to the PrettyPrinter
   *
   * @param printout the PrettyPrinter to print to
   */
    public void printSelf(PrettyPrinter printout) throws IOException {
        SymTabAST classItExtends = (SymTabAST) nodeToPrint.getFirstChild();
        if (classItExtends != null) {
            printout.print(" extends ");
            PrinterFactory.makePrinter(classItExtends).print(printout);
        }
    }
}
