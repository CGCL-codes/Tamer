package net.sourceforge.transmogrify.symtab.printer;

import net.sourceforge.transmogrify.symtab.parser.*;
import java.io.*;

/**
 * prints out a list of statements or expressions indented one level
 */
public class BlockPrinter extends DefaultPrinter {

    public BlockPrinter(SymTabAST nodeToPrint) {
        super(nodeToPrint);
    }

    /**
    * prints the block.  If the text node of the block is an opening
    * brace ( '{' ), opening and closing braces are printed.  The children
    * of the block are indented according to the PrettyPrinter.
    *
    * @param printout the printer object you would like to print to.
    */
    public void printSelf(PrettyPrinter printout) throws IOException {
        if (nodeToPrint.getText().equals("{")) {
            printOpenBrace(printout);
        }
        printout.indent();
        SymTabASTIterator children = nodeToPrint.getChildren();
        int previousType = 0;
        while (children.hasNext()) {
            SymTabAST child = children.nextChild();
            int currentType = child.getType();
            if (PrinterProperties.hasNewLineBetweenExpressionGroups() && previousType != 0 && currentType != JavaTokenTypes.RCURLY) {
                if (currentType != previousType || currentType == JavaTokenTypes.LITERAL_if || currentType == JavaTokenTypes.LITERAL_try || currentType == JavaTokenTypes.LITERAL_do || currentType == JavaTokenTypes.LITERAL_for || currentType == JavaTokenTypes.LITERAL_while || currentType == JavaTokenTypes.LITERAL_synchronized || currentType == JavaTokenTypes.LITERAL_switch || currentType == JavaTokenTypes.LITERAL_finally) {
                    newline(printout);
                }
            }
            PrinterFactory.makePrinter(child).print(printout);
            switch(currentType) {
                case JavaTokenTypes.EXPR:
                case JavaTokenTypes.LITERAL_throw:
                case JavaTokenTypes.LITERAL_return:
                case JavaTokenTypes.CTOR_CALL:
                case JavaTokenTypes.SUPER_CTOR_CALL:
                    endStatement(printout);
                    newline(printout);
                    break;
            }
            previousType = currentType;
        }
    }

    public void printTrailingFormat(PrettyPrinter printout) throws IOException {
        printout.unindent();
        if (nodeToPrint.getText().equals("{")) {
            printCloseBrace(printout);
        }
    }
}
