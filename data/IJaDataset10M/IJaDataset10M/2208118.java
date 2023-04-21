package com.liferay.portal.lar;

import com.liferay.portal.kernel.util.StringPool;

/**
 * <a href="PortletDataHandlerControl.java.html"><b><i>View Source</i></b></a>
 *
 * @author Raymond Augé
 *
 */
public class PortletDataHandlerControl {

    public static String getNamespacedControlName(String namespace, String controlName) {
        StringBuilder sb = new StringBuilder();
        sb.append(StringPool.UNDERLINE);
        sb.append(namespace);
        sb.append(StringPool.UNDERLINE);
        sb.append(controlName);
        return sb.toString();
    }

    public PortletDataHandlerControl(String namespace, String controlName) {
        this(namespace, controlName, false);
    }

    public PortletDataHandlerControl(String namespace, String controlName, boolean disabled) {
        _namespace = namespace;
        _controlName = controlName;
        _disabled = disabled;
    }

    public String getNamespace() {
        return _namespace;
    }

    public String getControlName() {
        return _controlName;
    }

    public String getNamespacedControlName() {
        return getNamespacedControlName(_namespace, getControlName());
    }

    public boolean isDisabled() {
        return _disabled;
    }

    private String _namespace;

    private String _controlName;

    private boolean _disabled;
}
