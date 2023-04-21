package net.sf.portletunit.portlet.simpleform;

import java.io.IOException;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

/**
 * 
 * @author <a href="mailto:lpmsmith@ihc.com">Matthew O. Smith</a>
 * @version $Revision: 1.2 $
 * 
 **/
public class SimpleFormPortlet extends GenericPortlet {

    private static String viewJsp = "/WEB-INF/jsp/simpleform.jsp";

    int count = 0;

    public void processAction(ActionRequest request, ActionResponse response) {
        count++;
        System.out.println("processAction:" + count);
    }

    /**
	 * Use the jsp page defined in <code>viewJsp</code> as what to show for the VIEW
	 * @param request a <code>RenderRequest</code> VALUE
	 * @param response a <code>RenderResponse</code> VALUE
	 * @exception PortletException if an error occurs
	 * @exception IOException if an error occurs
	 */
    public void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException {
        response.setContentType("text/html");
        request.setAttribute("count", new Integer(count));
        PortletContext context = getPortletContext();
        PortletRequestDispatcher dispatcher = context.getRequestDispatcher(viewJsp);
        dispatcher.include(request, response);
    }
}
