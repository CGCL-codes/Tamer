package net.sf.moviekebab.toolset.thread;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;

/**
 * Utility class for doing things with threads.
 *
 * @author Laurent Caillette
 */
public class ThreadTools {

    private ThreadTools() {
    }

    /**
   * Attempts to find an existing subgroup and creates it if it doesn't exist.
   * Search of an existing <code>ThreadGroup</code> is done on a best effort
   * basis because of concurrency support by <code>ThreadGroup</code>.
   *
   * @param parent parent <code>ThreadGroup</code>, must not be null.
   * @return a non-null <code>ThreadGroup</code> object.
   */
    public static ThreadGroup getSubGroup(ThreadGroup parent, String subGroupName) {
        if (null == parent) {
            throw new NullArgumentException("parent");
        }
        if (StringUtils.isBlank(subGroupName)) {
            throw new IllegalArgumentException("subGroupName");
        }
        final int activeGroupCount = parent.activeGroupCount();
        final ThreadGroup[] subGroups = new ThreadGroup[10 + activeGroupCount * 5];
        final int groupCount = parent.enumerate(subGroups);
        for (int i = 0; i < groupCount; i++) {
            final ThreadGroup subGroup = subGroups[i];
            if (subGroupName.equals(subGroup.getName())) {
                return subGroup;
            }
        }
        return new ThreadGroup(parent, subGroupName);
    }

    /**
   * Creates a top-level <code>ThreadGroup</code> with the short name of the
   * given class.
   * Supposed to be used only by tests.
   *
   * @param aClass a non-null class.
   * @return a non-null ThreadGroup.
   */
    public static ThreadGroup createThreadGroup(Class aClass) {
        return new ThreadGroup(ClassUtils.getShortClassName(aClass));
    }
}
