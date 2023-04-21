package com.sample.strutsliferay.portlet;

import com.liferay.portlet.StrutsPortlet;
import java.io.IOException;
import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * <a href="SampleStrutsLiferayPortlet.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class SampleStrutsLiferayPortlet extends StrutsPortlet {

    public void doView(RenderRequest req, RenderResponse res) throws IOException, PortletException {
        PortletSession ses = req.getPortletSession();
        ses.setAttribute("chart_name", "Soda Survey", PortletSession.APPLICATION_SCOPE);
        super.doView(req, res);
    }
}
