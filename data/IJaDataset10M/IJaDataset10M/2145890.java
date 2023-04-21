package bpiwowar.argparser.checkers;

import java.util.ArrayList;

/**
 * Verify that all conditions are OK
 * @author bpiwowar
 * @date Nov 29, 2007
 *
 */
public class AndCheckers extends ArrayList<ValueChecker> implements ValueChecker {

    private static final long serialVersionUID = -1302340030565308838L;

    public boolean check(Object object) {
        for (ValueChecker checker : this) if (!checker.check(object)) return false;
        return true;
    }

    public String getDescription() {
        String desc = null;
        for (ValueChecker checker : this) if (desc == null) desc = "(" + checker.getDescription(); else desc += " AND " + checker.getDescription();
        return desc + ")";
    }
}
