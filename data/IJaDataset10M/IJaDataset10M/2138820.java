package com.liferay.portlet;

import java.io.IOException;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletMode;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.WindowState;

/**
 * <a href="LiferayPortlet.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 * @version $Revision: 1.1 $
 *
 */
public class LiferayPortlet extends GenericPortlet {

    protected void doDispatch(RenderRequest req, RenderResponse res) throws IOException, PortletException {
        WindowState state = req.getWindowState();
        if (!state.equals(WindowState.MINIMIZED)) {
            PortletMode mode = req.getPortletMode();
            if (mode.equals(PortletMode.VIEW)) {
                doView(req, res);
            } else if (mode.equals(LiferayPortletMode.ABOUT)) {
                doAbout(req, res);
            } else if (mode.equals(LiferayPortletMode.CONFIG)) {
                doConfig(req, res);
            } else if (mode.equals(PortletMode.EDIT)) {
                doEdit(req, res);
            } else if (mode.equals(LiferayPortletMode.EDIT_DEFAULTS)) {
                doEditDefaults(req, res);
            } else if (mode.equals(PortletMode.HELP)) {
                doHelp(req, res);
            } else if (mode.equals(LiferayPortletMode.PREVIEW)) {
                doPreview(req, res);
            } else if (mode.equals(LiferayPortletMode.PRINT)) {
                doPrint(req, res);
            } else {
                throw new PortletException(mode.toString());
            }
        }
    }

    protected void doAbout(RenderRequest req, RenderResponse res) throws IOException, PortletException {
        throw new PortletException("doAbout method not implemented");
    }

    protected void doConfig(RenderRequest req, RenderResponse res) throws IOException, PortletException {
        throw new PortletException("doConfig method not implemented");
    }

    protected void doEditDefaults(RenderRequest req, RenderResponse res) throws IOException, PortletException {
        throw new PortletException("doEditDefaults method not implemented");
    }

    protected void doPreview(RenderRequest req, RenderResponse res) throws IOException, PortletException {
        throw new PortletException("doPreview method not implemented");
    }

    protected void doPrint(RenderRequest req, RenderResponse res) throws IOException, PortletException {
        throw new PortletException("doPrint method not implemented");
    }
}
