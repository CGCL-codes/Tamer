package freemarker.core;

import java.io.IOException;
import freemarker.template.TemplateException;

/**
 * An instruction that outputs the value of an <tt>Expression</tt>.
 */
final class DollarVariable extends TemplateElement {

    private final Expression expression;

    private final Expression escapedExpression;

    DollarVariable(Expression expression, Expression escapedExpression) {
        this.expression = expression;
        this.escapedExpression = escapedExpression;
    }

    /**
     * Outputs the string value of the enclosed expression.
     */
    void accept(Environment env) throws TemplateException, IOException {
        env.getOut().write(escapedExpression.getStringValue(env));
    }

    public String getCanonicalForm() {
        return "${" + expression.getCanonicalForm() + "}";
    }

    public String getDescription() {
        return this.getSource() + (expression == escapedExpression ? "" : " escaped ${" + escapedExpression.getCanonicalForm() + "}");
    }

    boolean heedsOpeningWhitespace() {
        return true;
    }

    boolean heedsTrailingWhitespace() {
        return true;
    }
}
