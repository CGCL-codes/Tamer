package com.sun.script.jelly;

import org.apache.commons.jelly.JellyContext;
import org.apache.commons.jelly.JellyException;
import org.apache.commons.jelly.expression.Expression;
import org.apache.commons.jelly.expression.ExpressionSupport;
import org.apache.commons.jelly.expression.ExpressionFactory;
import org.apache.commons.jelly.expression.jexl.JexlExpression;
import javax.script.*;

public class ScriptExpressionFactory implements ExpressionFactory {

    private ScriptTagLibrary tagLibrary;

    ScriptExpressionFactory(ScriptTagLibrary tagLibrary) {
        this.tagLibrary = tagLibrary;
    }

    public Expression createExpression(final String text) throws JellyException {
        return new ExpressionSupport() {

            public String getExpressionText() {
                return "${" + text + "}";
            }

            public Object evaluate(JellyContext context) {
                ScriptJellyContext sjc = (ScriptJellyContext) context;
                ScriptEngine engine = sjc.getScriptEngine();
                if (engine == null) {
                    engine = tagLibrary.getDefaultScriptEngine();
                    sjc.setScriptEngine(engine);
                }
                try {
                    return engine.eval(text, sjc.getScriptContext());
                } catch (ScriptException exp) {
                    throw new RuntimeException(exp);
                }
            }
        };
    }
}
