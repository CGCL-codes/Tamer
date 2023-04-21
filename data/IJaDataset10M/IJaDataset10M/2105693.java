package org.nfunk.jep.function.jsp;

import java.util.*;
import org.nfunk.jep.*;
import org.nfunk.jep.function.*;
import jvc.util.*;

public class CDouble extends PostfixMathCommand {

    public CDouble() {
        numberOfParameters = 1;
    }

    public void run(Stack inStack) throws ParseException {
        checkStack(inStack);
        Object param = inStack.pop();
        double re = DoubleUtils.strToDouble(param.toString());
        inStack.push(new Double(re));
        return;
    }
}
