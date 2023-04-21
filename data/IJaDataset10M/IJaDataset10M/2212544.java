package org.apache.jetspeed.services.forward.configuration.impl;

import java.util.Map;
import java.util.HashMap;
import org.apache.jetspeed.services.forward.configuration.PortletForward;

/**
 * Portlet Forward implementation
 *
 * @author <a href="mailto:taylor@apache.org">David Sean Taylor</a>
 * @version $Id: PortletForwardImpl.java,v 1.4 2004/02/23 03:50:10 jford Exp $
 */
public class PortletForwardImpl implements PortletForward, java.io.Serializable {

    private String portlet;

    private String forward;

    private String target;

    private Map queryParams = new HashMap();

    public String getPortlet() {
        return this.portlet;
    }

    public void setPortlet(String portlet) {
        this.portlet = portlet;
    }

    public String getForward() {
        return this.forward;
    }

    public void setForward(String forward) {
        this.forward = forward;
    }

    public String getTarget() {
        return this.target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Map getQueryParams() {
        return this.queryParams;
    }

    public void setQueryParams(Map queryParams) {
        this.queryParams = queryParams;
    }
}
