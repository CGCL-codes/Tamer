package org.databene.benerator.engine.statement;

import org.databene.benerator.engine.BeneratorContext;
import org.databene.benerator.engine.Statement;
import org.databene.commons.SpeechUtil;
import org.databene.script.Expression;
import org.databene.script.expression.ExpressionUtil;

/**
 * Prints out a message to the console.<br/>
 * <br/>
 * Created at 22.07.2009 07:13:28
 * @since 0.6.0
 * @author Volker Bergmann
 */
public class EchoStatement implements Statement {

    private final Expression<String> messageEx;

    private final Expression<String> typeEx;

    public EchoStatement(Expression<String> messageEx, Expression<String> typeEx) {
        this.messageEx = messageEx;
        this.typeEx = typeEx;
    }

    public Expression<?> getExpression() {
        return messageEx;
    }

    public boolean execute(BeneratorContext context) {
        String message = ExpressionUtil.evaluate(messageEx, context);
        String type = ExpressionUtil.evaluate(typeEx, context);
        if ("speech".equals(type) && SpeechUtil.speechSupported()) SpeechUtil.say(message); else System.out.println(message);
        return true;
    }
}
