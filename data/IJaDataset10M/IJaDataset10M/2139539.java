package com.rapidminer.tools.math.function.expressions.text;

import java.util.Stack;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;
import com.rapidminer.tools.math.function.UnknownValue;

/**
 * Returns the character at the desired position of the given string.
 * 
 * @author Ingo Mierswa
 */
public class CharAt extends PostfixMathCommand {

    public CharAt() {
        numberOfParameters = 2;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void run(Stack stack) throws ParseException {
        checkStack(stack);
        Object positionObject = stack.pop();
        Object textObject = stack.pop();
        if (textObject == UnknownValue.UNKNOWN_NOMINAL) {
            stack.push(UnknownValue.UNKNOWN_NOMINAL);
            return;
        }
        if (!(textObject instanceof String)) {
            throw new ParseException("Invalid argument type, must be (string, number)");
        }
        if (!(positionObject instanceof Number)) {
            throw new ParseException("Invalid argument type, must be (string, number)");
        }
        String text = (String) textObject;
        int position = ((Number) positionObject).intValue();
        if (position < 0 || position >= text.length()) stack.push(UnknownValue.UNKNOWN_NOMINAL); else stack.push(text.charAt(position));
    }
}
