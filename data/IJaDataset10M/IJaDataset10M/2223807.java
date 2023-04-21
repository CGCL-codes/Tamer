package net.sourceforge.transmogrify.symtab.printer;

import net.sourceforge.transmogrify.symtab.parser.*;
import java.io.*;

public class CasePrinter extends DefaultPrinter {

    public CasePrinter(SymTabAST nodeToPrint) {
        super(nodeToPrint);
    }

    public void printSelf(PrettyPrinter printout) throws IOException {
        printout.print(nodeToPrint.getText() + " ");
        PrinterFactory.makePrinter((SymTabAST) nodeToPrint.getFirstChild()).print(printout);
        printout.println(":");
    }
}
