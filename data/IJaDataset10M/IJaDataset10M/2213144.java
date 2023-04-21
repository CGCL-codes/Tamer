package net.sourceforge.processdash.data.compiler.function;

import java.util.List;
import net.sourceforge.processdash.data.ListData;
import net.sourceforge.processdash.data.compiler.AbstractFunction;
import net.sourceforge.processdash.data.compiler.ExpressionContext;
import net.sourceforge.processdash.hier.DashHierarchy;
import net.sourceforge.processdash.hier.PropertyKey;

public class Hierleaves extends AbstractFunction {

    /** Perform a procedure call.
     *
     * This method <b>must</b> be thread-safe.
     */
    public Object call(List arguments, ExpressionContext context) {
        String prefix;
        if (!arguments.isEmpty()) prefix = asStringVal(getArg(arguments, 0)); else prefix = context.get(ExpressionContext.PREFIXVAR_NAME).format();
        if (prefix == null) return null;
        try {
            ListData hierItem = (ListData) context.get(DashHierarchy.DATA_REPOSITORY_NAME);
            DashHierarchy hier = (DashHierarchy) hierItem.get(0);
            PropertyKey key = hier.findExistingKey(prefix);
            if (key == null) return null;
            ListData result = new ListData();
            collect(result, hier, key);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void collect(ListData result, DashHierarchy hier, PropertyKey key) {
        int numChildren = hier.getNumChildren(key);
        if (numChildren == 0) {
            result.add(key.path());
        } else {
            for (int i = 0; i < numChildren; i++) {
                PropertyKey child = hier.getChildKey(key, i);
                collect(result, hier, child);
            }
        }
    }
}
