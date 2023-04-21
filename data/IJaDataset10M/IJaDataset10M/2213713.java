package net.sourceforge.transmogrify.symtab.printer;

import net.sourceforge.transmogrify.symtab.parser.SymTabAST;
import java.io.IOException;

public class ThrowPrinter extends DefaultPrinter {

    public ThrowPrinter(SymTabAST nodeToPrint) {
        super(nodeToPrint);
    }

    protected void printSelf(PrettyPrinter printout) throws IOException {
        printout.print(nodeToPrint.getText() + " ");
        SymTabAST child = (SymTabAST) nodeToPrint.getFirstChild();
        PrinterFactory.makePrinter(child).print(printout);
    }
}
