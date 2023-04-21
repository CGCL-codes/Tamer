package org.nfunk.jep.function.jsp;

import java.util.*;
import org.nfunk.jep.*;
import org.nfunk.jep.function.*;
import jvc.util.*;

public class CInteger extends PostfixMathCommand {

    public CInteger() {
        numberOfParameters = 1;
    }

    public void run(Stack inStack) throws ParseException {
        checkStack(inStack);
        Object param = inStack.pop();
        inStack.push(new Integer(IntegerUtils.StrtoInt(param.toString(), 0)));
        return;
    }
}
