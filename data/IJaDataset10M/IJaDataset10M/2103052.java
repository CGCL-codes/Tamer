package com.liferay.taglib.ui;

import com.liferay.taglib.util.IncludeTag;
import javax.servlet.ServletRequest;

/**
 * <a href="NavigationTag.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 * @author Jorge Ferrer
 *
 */
public class NavigationTag extends IncludeTag {

    public int doStartTag() {
        ServletRequest req = pageContext.getRequest();
        req.setAttribute("liferay-ui:navigation:bulletStyle", _bulletStyle);
        req.setAttribute("liferay-ui:navigation:displayStyle", _displayStyle);
        req.setAttribute("liferay-ui:navigation:headerType", _headerType);
        req.setAttribute("liferay-ui:navigation:rootLayoutType", _rootLayoutType);
        req.setAttribute("liferay-ui:navigation:rootLayoutLevel", String.valueOf(_rootLayoutLevel));
        req.setAttribute("liferay-ui:navigation:includedLayouts", _includedLayouts);
        return EVAL_BODY_BUFFERED;
    }

    public void setBulletStyle(String bulletStyle) {
        _bulletStyle = bulletStyle;
    }

    public void setDisplayStyle(String displayStyle) {
        _displayStyle = displayStyle;
    }

    public void setHeaderType(String headerType) {
        _headerType = headerType;
    }

    public void setRootLayoutType(String rootLayoutType) {
        _rootLayoutType = rootLayoutType;
    }

    public void setRootLayoutLevel(int rootLayoutLevel) {
        _rootLayoutLevel = rootLayoutLevel;
    }

    public void setIncludedLayouts(String includedLayouts) {
        _includedLayouts = includedLayouts;
    }

    protected String getDefaultPage() {
        return _PAGE;
    }

    private static final String _PAGE = "/html/taglib/ui/navigation/page.jsp";

    private String _bulletStyle = "1";

    private String _displayStyle = "1";

    private String _headerType = "none";

    private String _rootLayoutType = "absolute";

    private int _rootLayoutLevel = 1;

    private String _includedLayouts = "auto";
}
