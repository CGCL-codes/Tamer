package com.liferay.sampletest.portlet;

import com.liferay.portal.kernel.util.MimeTypesUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.util.servlet.PortletResponseUtil;
import java.io.IOException;
import java.io.InputStream;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <a href="TestPortlet.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class TestPortlet extends GenericPortlet {

    public void doDispatch(RenderRequest renderRequest, RenderResponse renderResponse) throws IOException, PortletException {
        String jspPage = ParamUtil.getString(renderRequest, "jspPage", "/view.jsp");
        if (jspPage.equals("/renderResponseponse/buffer_size.jsp")) {
            testResponseBufferSize(renderResponse);
        }
        include(jspPage, renderRequest, renderResponse);
    }

    public void serveResource(ResourceRequest renderRequest, ResourceResponse renderResponse) throws IOException, PortletException {
        String fileName = renderRequest.getResourceID();
        InputStream is = getPortletContext().getResourceAsStream("/WEB-INF/images/logo.png");
        String contentType = MimeTypesUtil.getContentType(fileName);
        PortletResponseUtil.sendFile(renderResponse, fileName, is, contentType);
    }

    protected void include(String path, RenderRequest renderRequest, RenderResponse renderResponse) throws IOException, PortletException {
        PortletRequestDispatcher portletRequestDispatcher = getPortletContext().getRequestDispatcher(path);
        if (portletRequestDispatcher == null) {
            _log.error(path + " is not a valid include");
        } else {
            portletRequestDispatcher.include(renderRequest, renderResponse);
        }
    }

    protected void testResponseBufferSize(RenderResponse renderResponse) {
        _log.info("Original buffer size " + renderResponse.getBufferSize());
        renderResponse.setBufferSize(12345);
        _log.info("New buffer size " + renderResponse.getBufferSize());
    }

    private static Log _log = LogFactory.getLog(TestPortlet.class);
}
