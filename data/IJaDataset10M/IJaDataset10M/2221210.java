package com.rapidminer.tools.math.function.expressions.text;

import java.util.Stack;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;
import com.rapidminer.tools.math.function.UnknownValue;

/**
 * Returns true if and only if the given text matches the given expression.
 * 
 * @author Ingo Mierswa
 */
public class Matches extends PostfixMathCommand {

    public Matches() {
        numberOfParameters = 2;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void run(Stack stack) throws ParseException {
        checkStack(stack);
        Object expressionObject = stack.pop();
        Object textObject = stack.pop();
        if (expressionObject == UnknownValue.UNKNOWN_NOMINAL || textObject == UnknownValue.UNKNOWN_NOMINAL) {
            stack.push(UnknownValue.UNKNOWN_BOOLEAN);
            return;
        }
        if (!(textObject instanceof String) || !(expressionObject instanceof String)) {
            throw new ParseException("Invalid argument types, must be (string, string)");
        }
        String text = (String) textObject;
        String expression = (String) expressionObject;
        stack.push(text.matches(expression));
    }
}
