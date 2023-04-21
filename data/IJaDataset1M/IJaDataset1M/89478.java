package com.the_eventhorizon.todo.commons.taskTagDecorator.model;

import java.util.Arrays;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IMarkerDelta;
import org.osgi.service.prefs.BackingStoreException;

/**
 * Task tag abstraction (i.e. todo, etc).
 * 
 * @author pkrupets
 */
public class TaskTag {

    /**
     * @param name must not be <code>null</code> or empty.
     * 
     * @param priority must not be <code>null</code>.
     * 
     * @param provider must not be <code>null</code>.
     */
    public TaskTag(String name, ePriority priority, boolean caseSensitive, TaskTagProvider provider) {
        if (priority == null) {
            throw new IllegalArgumentException("priority must not be null!");
        }
        if (provider == null) {
            throw new IllegalArgumentException("provider must not be null!");
        }
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException("name must not be null or empty!");
        }
        this.name = name;
        this.upperCaseName = name.toUpperCase();
        this.provider = provider;
        this.priority = priority;
        this.caseSensitive = caseSensitive;
    }

    /**
     * @return cannot be <code>null</code> or empty.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Default task tag priority (note that priority can be ignored if user chooses to draw decorate
     * task tag with lower priority before one with the higher).
     * 
     * @return cannot be <code>null</code>.
     */
    public ePriority getPriority() {
        return this.priority;
    }

    /**
     * Provider to which this tag task belongs.
     * 
     * @return cannot be <code>null</code>.
     */
    public TaskTagProvider getProvider() {
        return this.provider;
    }

    public boolean isCaseSensitive() {
        return this.caseSensitive;
    }

    /**
     * @param marker cannot be <code>null</code>.
     * 
     * @throws CoreException 
     */
    public boolean isStringTagged(IMarker marker) throws CoreException {
        return isStringTagged((String) marker.getAttribute(IMarker.MESSAGE));
    }

    /**
     * @param delta cannot be <code>null</code>.
     */
    public boolean isStringTagged(IMarkerDelta delta) {
        return isStringTagged((String) delta.getAttribute(IMarker.MESSAGE));
    }

    /**
     * @param taskTagStr cannot be <code>null</code>. 
     */
    protected boolean isStringTagged(String taskTagStr) {
        if (taskTagStr == null) {
            return false;
        }
        if (this.caseSensitive) {
            return taskTagStr.startsWith(this.name);
        } else {
            return taskTagStr.toUpperCase().startsWith(this.upperCaseName);
        }
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof TaskTag)) {
            return false;
        }
        TaskTag tt = (TaskTag) obj;
        return this.name.equals(tt.name) && this.provider.equals(tt.provider);
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return Arrays.hashCode(new Object[] { this.name, this.provider });
    }

    /**
     * @param prefs must not be <code>null</code>.
     * 
     * @param prefix must not be <code>null</code>.
     * 
     * @param unifiedProvider must not be <code>null</code>.
     * 
     * @return <code>null</code> if task tag cannot be created.
     */
    public static TaskTag restoreVersion2(Preferences prefs, String prefix, TaskTagProvider unifiedProvider) {
        String name = prefs.getString(prefix + SUFFIX_NAME);
        int priority = prefs.getInt(prefix + SUFFIX_PRIORITY);
        boolean caseSensitive = prefs.getBoolean(prefix + SUFFIX_CASE_SENSITIVE);
        try {
            return new TaskTag(name, ePriority.values()[priority], caseSensitive, unifiedProvider);
        } catch (Exception ignore) {
            return null;
        }
    }

    /**
     * @param node must not be <code>null</code>.
     * 
     * @param unifiedProvider must not be <code>null</code>.
     */
    public static TaskTag restoreVersion3(org.osgi.service.prefs.Preferences node, TaskTagProvider unifiedProvider) {
        String name = node.get(SUFFIX_NAME, null);
        int priority = node.getInt(SUFFIX_PRIORITY, -1);
        boolean caseSensitive = node.getBoolean(SUFFIX_CASE_SENSITIVE, true);
        try {
            return new TaskTag(name, ePriority.values()[priority], caseSensitive, unifiedProvider);
        } catch (Throwable ignore) {
            try {
                node.removeNode();
                node.flush();
            } catch (BackingStoreException e) {
            }
        }
        return null;
    }

    /**
     * @param node must not be <code>null</code>.
     */
    public void storeVersion3(org.osgi.service.prefs.Preferences node) {
        node.put(SUFFIX_NAME, this.name);
        node.putInt(SUFFIX_PRIORITY, this.priority.ordinal());
        node.putBoolean(SUFFIX_CASE_SENSITIVE, this.caseSensitive);
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("TaskTag{provider=");
        sb.append(this.provider.getName());
        sb.append(",name=");
        sb.append(this.name);
        sb.append('}');
        return sb.toString();
    }

    private String name;

    private ePriority priority;

    private String upperCaseName;

    private boolean caseSensitive;

    private TaskTagProvider provider;

    private static final String SUFFIX_NAME = "name";

    private static final String SUFFIX_PRIORITY = "priority";

    private static final String SUFFIX_CASE_SENSITIVE = "case_sensitive";
}
