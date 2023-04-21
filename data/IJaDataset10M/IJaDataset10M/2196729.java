package org.fressia.core.sbes;

import org.fressia.util.ResourceReferenceUtils;
import org.fressia.util.XmlUtils;

/**
 * This class encapsulates the base logic of a structured boolean expression (sbe).
 * <p>
 * A SBB is a text statement that follows the format:
 * <p>
 * <i>&lt;type&gt; : &lt;condition&gt; {&lt;argument&gt;}</i>
 * <p>
 * Where:
 * <ul>
 *  <li> <i>type</i> is the expression type.
 *  <li> <i>condition</i> is the condition used to get the expression value.
 *  <li> <i>argument</i> is the expression argument - to make the further evaluation against
 *  the target.
 * </ul>
 * <p>
 * Examples:
 * <p>
 * 1. <code>"text : contains {hola}"</code> <p>
 * It's an expression of type 'text' that evaluates to 'true' if the target text contains the
 * string 'hola'.
 * <p>
 * 2. <code>"xml : valid {}"</code> <p>
 * It's an expression of type 'xml' that that evaluates to 'true' if the target xml is valid.
 * <p>
 * You can group boolean expressions by logical expressions. For further details see {@link SbeEvaluator}.
 * <p>
 *
 * @author Alvaro Egana
 *
 * @see SbeEvaluator
 *
 */
public abstract class Sbe {

    private String argument;

    public Sbe(String argument) {
        this.argument = argument;
    }

    /**
     * Evaluates <b><code>this</code></b> expression against a target.
     *
     * @param target
     * @param targetType
     * @return <b><code>True</code></b> or <b><code>false</code></b> depending
     * on the evaluation result.
     * @throws SbeEvaluationException
     */
    public <T> boolean evalExpression(T target, Class<?> targetType) throws SbeEvaluationException {
        try {
            return expressionArgument(target, targetType);
        } catch (Throwable e) {
            throw new SbeEvaluationException(e);
        }
    }

    /**
     * This is the method which is specific to an expression type
     * and condition.
     */
    protected abstract <T> boolean expressionArgument(T target, Class<?> targetType) throws SbeEvaluationException;

    /**
     * 
     * @param resolveResReferences If true the resource references will be 
     * resolved.
     * @return This Sbe argument.
     */
    protected String getArgument(boolean resolveResReferences) {
        return getArgument(resolveResReferences, false);
    }

    /**
     * 
     * @param resolveResReferences If true the resource references will be 
     * resolved.
     * @param includeLineSeparator True if you want lines separators to be 
     * included in the content.
     * @return This Sbe argument.
     */
    protected String getArgument(boolean resolveResReferences, boolean includeLineSeparator) {
        if (!resolveResReferences) return getArgument(); else return ResourceReferenceUtils.resolveResourceReferences(getArgument(), includeLineSeparator);
    }

    private String getArgument() {
        return XmlUtils.unflatten(argument);
    }

    public static class SyntaxisConstants {

        public static final String EXPRESSION = "\\p{Print}*";

        public static final String SPACE = "\\p{Space}*";

        public static final String ARG_START = "{";

        public static final String ARG_END = "}";

        public static final String ARGUMENT = "\\" + ARG_START + SPACE + EXPRESSION + SPACE + "\\" + ARG_END;

        public static final String COND_SIGN = ":";
    }
}
