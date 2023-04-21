package net.sourceforge.phpdt.internal.compiler.ast;

import gatchan.phpparser.parser.PHPParser;
import net.sourceforge.phpdt.internal.compiler.ast.declarations.VariableUsage;
import java.util.List;

/**
 * @author Matthieu Casanova
 */
public class PrintExpression extends Expression {

    private final Expression expression;

    public PrintExpression(Expression expression, int sourceStart, int sourceEnd, int beginLine, int endLine, int beginColumn, int endColumn) {
        super(Type.INTEGER, sourceStart, sourceEnd, beginLine, endLine, beginColumn, endColumn);
        this.expression = expression;
    }

    /**
	 * Return the expression as String.
	 *
	 * @return the expression
	 */
    @Override
    public String toStringExpression() {
        return "print " + expression.toStringExpression();
    }

    /**
	 * Get the variables from outside (parameters, globals ...)
	 *
	 * @param list the list where we will put variables
	 */
    @Override
    public void getOutsideVariable(List<VariableUsage> list) {
        expression.getOutsideVariable(list);
    }

    /**
	 * get the modified variables.
	 *
	 * @param list the list where we will put variables
	 */
    @Override
    public void getModifiedVariable(List<VariableUsage> list) {
        expression.getModifiedVariable(list);
    }

    /**
	 * Get the variables used.
	 *
	 * @param list the list where we will put variables
	 */
    @Override
    public void getUsedVariable(List<VariableUsage> list) {
        expression.getUsedVariable(list);
    }

    @Override
    public AstNode subNodeAt(int line, int column) {
        return expression.isAt(line, column) ? expression : null;
    }

    @Override
    public void analyzeCode(PHPParser parser) {
        expression.analyzeCode(parser);
    }
}
