package com.rapidminer.tools.math.function.expressions.text;

import java.util.Stack;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;
import com.rapidminer.tools.math.function.UnknownValue;

/**
 * Removes leading or trailing white space.
 * 
 * @author Ingo Mierswa
 */
public class Trim extends PostfixMathCommand {

    public Trim() {
        numberOfParameters = 1;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void run(Stack stack) throws ParseException {
        checkStack(stack);
        Object textObject = stack.pop();
        if (textObject == UnknownValue.UNKNOWN_NOMINAL) {
            stack.push(UnknownValue.UNKNOWN_NOMINAL);
            return;
        }
        if (!(textObject instanceof String)) {
            throw new ParseException("Invalid argument type, must be (string)");
        }
        String text = (String) textObject;
        stack.push(text.trim());
    }
}
