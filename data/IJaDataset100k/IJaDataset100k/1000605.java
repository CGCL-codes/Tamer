package net.sourceforge.phpdt.internal.compiler.ast;

import gatchan.phpparser.parser.PHPParser;
import net.sourceforge.phpdt.internal.compiler.ast.declarations.VariableUsage;
import java.util.List;

/**
 * an else statement.
 * it's else
 *
 * @author Matthieu Casanova
 */
public class Else extends Statement {

    /**
	 * the statements.
	 */
    private final Statement[] statements;

    /**
	 * An else statement bad version ( : endif).
	 *
	 * @param statements  the statements
	 * @param sourceStart the starting offset
	 * @param sourceEnd   the ending offset
	 */
    public Else(Statement[] statements, int sourceStart, int sourceEnd, int beginLine, int endLine, int beginColumn, int endColumn) {
        super(sourceStart, sourceEnd, beginLine, endLine, beginColumn, endColumn);
        this.statements = statements;
    }

    /**
	 * An else statement good version
	 *
	 * @param statement   the statement (it could be a block)
	 * @param sourceStart the starting offset
	 * @param sourceEnd   the ending offset
	 */
    public Else(Statement statement, int sourceStart, int sourceEnd, int beginLine, int endLine, int beginColumn, int endColumn) {
        super(sourceStart, sourceEnd, beginLine, endLine, beginColumn, endColumn);
        statements = new Statement[1];
        statements[0] = statement;
    }

    /**
	 * Return the object into String.
	 *
	 * @param tab how many tabs (not used here
	 * @return a String
	 */
    @Override
    public String toString(int tab) {
        StringBuilder buff = new StringBuilder(tabString(tab));
        buff.append("else \n");
        Statement statement;
        for (int i = 0; i < statements.length; i++) {
            statement = statements[i];
            buff.append(statement.toString(tab + 1)).append('\n');
        }
        return buff.toString();
    }

    /**
	 * Get the variables from outside (parameters, globals ...)
	 *
	 * @param list the list where we will put variables
	 */
    @Override
    public void getOutsideVariable(List<VariableUsage> list) {
        for (int i = 0; i < statements.length; i++) {
            statements[i].getOutsideVariable(list);
        }
    }

    /**
	 * get the modified variables.
	 *
	 * @param list the list where we will put variables
	 */
    @Override
    public void getModifiedVariable(List<VariableUsage> list) {
        for (int i = 0; i < statements.length; i++) {
            statements[i].getModifiedVariable(list);
        }
    }

    /**
	 * Get the variables used.
	 *
	 * @param list the list where we will put variables
	 */
    @Override
    public void getUsedVariable(List<VariableUsage> list) {
        for (int i = 0; i < statements.length; i++) {
            statements[i].getUsedVariable(list);
        }
    }

    @Override
    public AstNode subNodeAt(int line, int column) {
        for (int i = 0; i < statements.length; i++) {
            Statement statement = statements[i];
            if (statement.isAt(line, column)) return statement.subNodeAt(line, column);
        }
        return null;
    }

    @Override
    public void analyzeCode(PHPParser parser) {
        for (int i = 0; i < statements.length; i++) {
            statements[i].analyzeCode(parser);
        }
    }
}
