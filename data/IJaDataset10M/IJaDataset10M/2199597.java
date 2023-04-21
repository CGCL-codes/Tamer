package org.nfunk.jep.function.script;

import java.util.*;
import org.nfunk.jep.*;
import org.nfunk.jep.function.*;

public class CInteger extends PostfixMathCommand {

    public CInteger() {
        numberOfParameters = 1;
    }

    public void run(Stack inStack) throws ParseException {
        checkStack(inStack);
        String param = inStack.pop().toString();
        inStack.push("parseFloat(" + param + ")");
        return;
    }
}
