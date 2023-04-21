package net.sourceforge.transumanza.task.load;

import net.sourceforge.transumanza.base.Component;

/**
 * Utility class for reading default stats from component
 * 
 * @author <a href="mailto:giokarka@users.sourceforge.net">Giorgio Carchedi</a>
 */
public class StatsInfoHelper {

    public static int getTotalCount(Component component) {
        return getIntPropertyFromInfo(component, DefaultStats.TOTAL_COUNT_KEY);
    }

    public static int getSuccessCount(Component component) {
        return getIntPropertyFromInfo(component, DefaultStats.SUCCESS_COUNT_KEY);
    }

    public static int getSkipCount(Component component) {
        return getIntPropertyFromInfo(component, DefaultStats.SKIP_COUNT_KEY);
    }

    public static int getErrorCount(Component component) {
        return getIntPropertyFromInfo(component, DefaultStats.ERROR_COUNT_KEY);
    }

    private static int getIntPropertyFromInfo(Component component, String property) {
        int ret = -1;
        try {
            ret = ((Integer) component.getInfo().get(property)).intValue();
        } catch (Exception e) {
        }
        return ret;
    }
}
