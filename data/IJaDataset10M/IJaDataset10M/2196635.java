package net.sourceforge.processdash.data.compiler.function;

import java.util.Iterator;
import java.util.List;
import net.sourceforge.processdash.data.StringData;
import net.sourceforge.processdash.data.compiler.AbstractFunction;
import net.sourceforge.processdash.data.compiler.ExpressionContext;

public class Join extends AbstractFunction {

    /** Perform a procedure call.
     *
     * This method <b>must</b> be thread-safe.
     */
    public Object call(List arguments, ExpressionContext context) {
        StringBuffer result = new StringBuffer();
        String delimiter = asString(getArg(arguments, 0));
        if (delimiter == null) delimiter = "";
        Iterator i = collapseLists(arguments, 1).iterator();
        String item;
        boolean needDelimiter = false;
        while (i.hasNext()) {
            item = asStringVal(i.next());
            if (item == null) continue;
            if (needDelimiter) result.append(delimiter);
            result.append(item);
            needDelimiter = true;
        }
        return StringData.create(result.toString());
    }
}
