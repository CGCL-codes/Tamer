package org.deved.antlride.core.parser.ast;

import java.util.Iterator;
import org.eclipse.dltk.utils.CorePrinter;

public class AntlrAlternativeAST extends AntlrStatementAST<AntlrStatementAST> implements Iterable<AntlrStatementAST> {

    protected AntlrAlternativeAST() {
    }

    @Override
    public int getKind() {
        return AntlrASTConstants.RULE_ALTERNATIVE;
    }

    @Override
    public void addStatement(AntlrStatementAST statement) {
        if (statement instanceof AntlrBlockAST) {
            AntlrBlockAST block = (AntlrBlockAST) statement;
            if (block.getStatementCount() == 1) {
                AntlrAlternativeAST alternative = block.getAlternative(0);
                if (alternative.getStatementCount() == 1) {
                    alternative.getStatement(0).setMultiplicity(block.getMultiplicity());
                    AntlrStatementAST stt = alternative.getStatement(0);
                    super.addStatement(stt);
                    return;
                } else {
                }
            }
        }
        super.addStatement(statement);
    }

    @Override
    public void printNode(CorePrinter output) {
        output.formatPrint("");
        for (AntlrStatementAST statement : statements) {
            if (statement.isNot()) {
                output.print("~");
            }
            statement.printNode(output);
            output.print(" ");
        }
    }

    public Iterator<AntlrStatementAST> iterator() {
        return statements.iterator();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        Iterator<AntlrStatementAST> iterator = statements.iterator();
        while (iterator.hasNext()) {
            builder.append(iterator.next());
        }
        if (builder.length() == 0) {
            builder.append("empty");
        }
        return builder.toString();
    }
}
