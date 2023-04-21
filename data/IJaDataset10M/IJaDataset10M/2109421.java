package org.apache.jetspeed.portal.portlets;

import org.apache.jetspeed.portal.PortletConfig;

/**
* Extend AbstractPortlet to re-define the handle used for portlet caching:
* This handle uses the portlet's unique id and the portal page's id to form
* a portlet instance id for caching.
* @author <a href="mailto:taylor@apache.org">David Sean Taylor</a>
* @author <a href="mailto:ggolden@apache.org">Glenn R. Golden</a>
*/
public class AbstractInstancePortlet extends AbstractPortlet {

    /**
    * Construct the handle used for caching.
    * @param config The config object, expected to be a PortletConfig.
    */
    public static Object getHandle(Object config) {
        PortletConfig pc = null;
        if (!(config instanceof PortletConfig)) {
            return null;
        }
        pc = (PortletConfig) config;
        StringBuffer handle = new StringBuffer(256);
        handle.append(pc.getPageId());
        handle.append('/');
        handle.append(pc.getPortletId());
        return handle.toString();
    }
}
