package org.plog4u.wiki.editor;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IResourcePropertyConstants;
import org.eclipse.ui.views.properties.PropertyDescriptor;

public class WPPropertySource implements IPropertySource {

    /**
   * The <code>IResource</code> property key for Wikipedia timestamp.
   */
    public static final String P_TIMESTAMP = "org.plog4u.wiki.timestamp";

    private static PropertyDescriptor wpEditorDescriptor;

    {
        wpEditorDescriptor = new PropertyDescriptor(P_TIMESTAMP, "Wikipedia Timestamp");
        wpEditorDescriptor.setAlwaysIncompatible(true);
        wpEditorDescriptor.setCategory(IResourcePropertyConstants.P_FILE_SYSTEM_CATEGORY);
    }

    public WPPropertySource(WikiEditor source) {
    }

    public IPropertyDescriptor[] getPropertyDescriptors() {
        IPropertyDescriptor[] p = new IPropertyDescriptor[1];
        p[0] = wpEditorDescriptor;
        return p;
    }

    public Object getPropertyValue(Object key) {
        if (key.equals(P_TIMESTAMP)) return "4711";
        return null;
    }

    public Object getEditableValue() {
        return null;
    }

    public boolean isPropertySet(Object id) {
        return id.equals(P_TIMESTAMP);
    }

    public void resetPropertyValue(Object id) {
    }

    public void setPropertyValue(Object id, Object value) {
    }
}
