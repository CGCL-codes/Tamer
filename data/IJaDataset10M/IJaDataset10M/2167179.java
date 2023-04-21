package com.liferay.taglib.portlet;

import javax.portlet.PortletRequest;

/**
 * <a href="ResourceURLTag.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class ResourceURLTag extends ActionURLTag {

    public String getLifecycle() {
        return PortletRequest.RESOURCE_PHASE;
    }
}
