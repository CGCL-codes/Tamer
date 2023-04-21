package pl.otros.logview.accept.query.org.apache.log4j.rule;

import pl.otros.logview.LogData;
import java.awt.*;
import java.io.Serializable;
import java.util.Map;

/**
 * A Rule class which also holds a color.
 * 
 * @author Scott Deboy (sdeboy@apache.org)
 * @author Krzysztof Otrebski
 */
public class ColorRule extends AbstractRule implements Serializable {

    /**
   * Serialization id.
   */
    static final long serialVersionUID = -794434783372847773L;

    /**
   * Wrapped rule.
   */
    private final Rule rule;

    /**
   * Foreground color.
   */
    private final Color foregroundColor;

    /**
   * Background color.
   */
    private final Color backgroundColor;

    /**
   * Expression.
   */
    private final String expression;

    /**
   * Create new instance.
   * 
   * @param expression
   *          expression.
   * @param rule
   *          rule.
   * @param backgroundColor
   *          background color.
   * @param foregroundColor
   *          foreground color.
   */
    public ColorRule(final String expression, final Rule rule, final Color backgroundColor, final Color foregroundColor) {
        super();
        this.expression = expression;
        this.rule = rule;
        this.backgroundColor = backgroundColor;
        this.foregroundColor = foregroundColor;
    }

    /**
   * Get rule.
   * 
   * @return underlying rule.
   */
    public Rule getRule() {
        return rule;
    }

    /**
   * Get foreground color.
   * 
   * @return foreground color.
   */
    public Color getForegroundColor() {
        return foregroundColor;
    }

    /**
   * Get background color.
   * 
   * @return background color.
   */
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    /**
   * Get expression.
   * 
   * @return expression.
   */
    public String getExpression() {
        return expression;
    }

    /**
   * {@inheritDoc}
   */
    public boolean evaluate(final LogData event, @SuppressWarnings("rawtypes") Map matches) {
        return (rule != null && rule.evaluate(event, null));
    }

    /**
   * {@inheritDoc}
   */
    public String toString() {
        StringBuffer buf = new StringBuffer("color rule - expression: ");
        buf.append(expression);
        buf.append(", rule: ");
        buf.append(rule);
        buf.append(" bg: ");
        buf.append(backgroundColor);
        buf.append(" fg: ");
        buf.append(foregroundColor);
        return buf.toString();
    }
}
