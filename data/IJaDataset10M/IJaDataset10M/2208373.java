package net.sourceforge.phpdt.internal.compiler.ast;

import gatchan.phpparser.parser.PHPParser;
import net.sourceforge.phpdt.internal.compiler.ast.declarations.VariableUsage;
import java.util.List;

/**
 * A Block. { statements }.
 *
 * @author Matthieu Casanova
 */
public class Block extends Statement {

    /**
	 * An array of statements inside the block.
	 */
    private final Statement[] statements;

    /**
	 * Create a block.
	 *
	 * @param statements  the statements
	 * @param sourceStart starting offset
	 * @param sourceEnd   ending offset
	 */
    public Block(Statement[] statements, int sourceStart, int sourceEnd, int beginLine, int endLine, int beginColumn, int endColumn) {
        super(sourceStart, sourceEnd, beginLine, endLine, beginColumn, endColumn);
        this.statements = statements;
    }

    /**
	 * tell if the block is empty.
	 *
	 * @return the block is empty if there are no statements in it
	 */
    @Override
    public boolean isEmptyBlock() {
        return statements == null;
    }

    /**
	 * Return the block as String.
	 *
	 * @param tab how many tabs
	 * @return the string representation of the block
	 */
    @Override
    public String toString(int tab) {
        String s = AstNode.tabString(tab);
        StringBuilder buff = new StringBuilder(s);
        buff.append("{\n");
        if (statements != null) {
            for (Statement statement : statements) {
                buff.append(statement.toString(tab + 1)).append(";\n");
            }
        }
        buff.append("}\n");
        return buff.toString();
    }

    /**
	 * Get the variables from outside (parameters, globals ...)
	 *
	 * @param list the list where we will put variables
	 */
    @Override
    public void getOutsideVariable(List<VariableUsage> list) {
        for (Statement statement : statements) {
            statement.getOutsideVariable(list);
        }
    }

    /**
	 * get the modified variables.
	 *
	 * @param list the list where we will put variables
	 */
    @Override
    public void getModifiedVariable(List<VariableUsage> list) {
        for (Statement statement : statements) {
            statement.getModifiedVariable(list);
        }
    }

    /**
	 * Get the variables used.
	 *
	 * @param list the list where we will put variables
	 */
    @Override
    public void getUsedVariable(List<VariableUsage> list) {
        for (Statement statement : statements) {
            statement.getUsedVariable(list);
        }
    }

    public Statement[] getStatements() {
        return statements;
    }

    @Override
    public AstNode subNodeAt(int line, int column) {
        for (Statement statement : statements) {
            if (statement.isAt(line, column)) return statement.subNodeAt(line, column);
        }
        return null;
    }

    @Override
    public void analyzeCode(PHPParser parser) {
        for (Statement statement : statements) {
            statement.analyzeCode(parser);
        }
    }
}
