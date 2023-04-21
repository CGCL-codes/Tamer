package org.nfunk.jep.function.jsp;

import java.util.*;
import org.nfunk.jep.*;
import org.nfunk.jep.function.*;
import java.util.regex.*;

public class RegExp extends PostfixMathCommand {

    private String StrPattern;

    public RegExp(String StrPattern) {
        numberOfParameters = 1;
        this.StrPattern = StrPattern;
    }

    public void run(Stack inStack) throws ParseException {
        checkStack(inStack);
        Object param = inStack.pop();
        Pattern p = Pattern.compile(StrPattern);
        Matcher m = p.matcher(param.toString());
        if (m.find()) inStack.push(new Double(1)); else inStack.push(new Double(0));
        return;
    }
}
