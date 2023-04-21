package com.liferay.portlet.tags.model;

import java.io.Serializable;

/**
 * <a href="TagsAssetType.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class TagsAssetType implements Serializable {

    public long getClassNameId() {
        return _classNameId;
    }

    public void setClassNameId(long classNameId) {
        _classNameId = classNameId;
    }

    public String getClassName() {
        return _className;
    }

    public void setClassName(String className) {
        _className = className;
    }

    public String getPortletId() {
        return _portletId;
    }

    public void setPortletId(String portletId) {
        _portletId = portletId;
    }

    public String getPortletTitle() {
        return _portletTitle;
    }

    public void setPortletTitle(String portletTitle) {
        _portletTitle = portletTitle;
    }

    private long _classNameId;

    private String _className;

    private String _portletId;

    private String _portletTitle;
}
