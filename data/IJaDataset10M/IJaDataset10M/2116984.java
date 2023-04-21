package com.rapidminer.tools.math.function.expressions.text;

import java.util.Stack;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;
import com.rapidminer.tools.math.function.UnknownValue;

/**
 * Returns true if and only if the given text contains the specified search string.
 * 
 * @author Ingo Mierswa
 */
public class Contains extends PostfixMathCommand {

    public Contains() {
        numberOfParameters = 2;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void run(Stack stack) throws ParseException {
        checkStack(stack);
        Object searchStringObject = stack.pop();
        Object textObject = stack.pop();
        if (textObject == UnknownValue.UNKNOWN_NOMINAL || searchStringObject == UnknownValue.UNKNOWN_NOMINAL) {
            stack.push(UnknownValue.UNKNOWN_BOOLEAN);
            return;
        }
        if (!(textObject instanceof String) || !(searchStringObject instanceof String)) {
            throw new ParseException("Invalid argument types, must be (string, string)");
        }
        String text = (String) textObject;
        String searchString = (String) searchStringObject;
        stack.push(text.contains(searchString));
    }
}
