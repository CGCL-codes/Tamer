package org.databene.benerator.engine.expression;

import org.databene.commons.Context;
import org.databene.script.Expression;
import org.databene.script.Script;
import org.databene.script.ScriptUtil;
import org.databene.script.expression.ConstantExpression;
import org.databene.script.expression.DynamicExpression;

/**
 * Expression that evaluates a script.<br/><br/>
 * Created: 27.10.2009 13:48:11
 * @since 0.6.0
 * @author Volker Bergmann
 */
public class ScriptExpression<E> extends DynamicExpression<E> {

    private Script script;

    private Expression<E> defaultValueExpression;

    public ScriptExpression(String script) {
        this(ScriptUtil.parseScriptText(script), (E) null);
    }

    public ScriptExpression(Script script) {
        this(script, (E) null);
    }

    public ScriptExpression(Script script, E defaultValue) {
        this(script, (defaultValue != null ? new ConstantExpression<E>(defaultValue) : null));
    }

    private ScriptExpression(Script script, Expression<E> defaultValueExpression) {
        this.script = script;
        this.defaultValueExpression = defaultValueExpression;
    }

    public static <T> Expression<T> createWithDefaultExpression(Script script, Expression<T> defaultValueExpression) {
        return new ScriptExpression<T>(script, defaultValueExpression);
    }

    @SuppressWarnings("unchecked")
    public E evaluate(Context context) {
        if (script == null) return (defaultValueExpression != null ? defaultValueExpression.evaluate(context) : null); else return (E) script.evaluate(context);
    }

    @Override
    public String toString() {
        return script.toString();
    }
}
