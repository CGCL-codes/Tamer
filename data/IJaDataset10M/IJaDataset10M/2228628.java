package org.apache.jetspeed.modules.actions.controllers;

import org.apache.jetspeed.portal.Portlet;
import org.apache.jetspeed.portal.PortletSet;
import org.apache.jetspeed.portal.PortletController;
import org.apache.jetspeed.portal.PortletControllerConfig;
import org.apache.jetspeed.portal.PanedPortletController;
import org.apache.jetspeed.services.resources.JetspeedResources;
import org.apache.turbine.util.RunData;
import org.apache.velocity.context.Context;

/**
 * This action builds a context suitable for controllers portlets
 * in panes, ie with only a subsetof defined portlets defined at any
 * time.
 * Should be associated with a controller implementing PanedPortletController
 * to work correctly
 * 
 * @author <a href="mailto:raphael@apache.org">Rapha�l Luta</a>
 * @author <a href="mailto:taylor@apache.org">David Sean Taylor</a>
 *
 * @version $Id: PanedControllerAction.java,v 1.13 2004/02/23 02:49:58 jford Exp $
 */
public class PanedControllerAction extends RowColumnControllerAction {

    /** 
     * Adds a "pane" portlet object in the context which represents the
     * currently selected pane
     */
    protected void buildNormalContext(PortletController controller, Context context, RunData rundata) {
        PanedPortletController cont = (PanedPortletController) controller;
        PortletSet myPortlets = cont.getPortlets();
        PortletControllerConfig conf = cont.getConfig();
        Portlet portlet = null;
        String paneID = null;
        String paneName = rundata.getParameters().getString(JetspeedResources.PATH_PANENAME_KEY);
        if (null != paneName) {
            portlet = myPortlets.getPortletByName(paneName);
            if (portlet != null) {
                paneID = portlet.getID();
                rundata.getParameters().setString(JetspeedResources.PATH_PANEID_KEY, paneID);
            }
        }
        if (null == portlet) {
            paneID = cont.retrievePaneID(rundata, true);
            portlet = myPortlets.getPortletByID(paneID);
            if (null == portlet) {
                paneID = cont.retrievePaneID(rundata, false);
                portlet = myPortlets.getPortletByID(paneID);
            }
        }
        if (portlet != null) {
            context.put("pane", portlet);
            String state = portlet.getAttribute("_menustate", "open", rundata);
            {
                cont.savePaneID(rundata, paneID);
            }
        }
    }
}
