package uk.ac.ed.rapid.portlets;

import java.io.IOException;
import java.util.List;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.UnavailableException;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import uk.ac.ed.rapid.common.RapidEngine;
import uk.ac.ed.rapid.data.RapidData;
import uk.ac.ed.rapid.exception.RapidException;

/**
 *
 * @author Jos Koetsier National e-Science Centre Edinburgh, UK
 */
public class RapidPortlet extends GenericPortlet {

    @Override
    public void init(PortletConfig config) throws PortletException, UnavailableException {
        super.init(config);
    }

    public static String getUserName(PortletRequest request) {
        return request.getRemoteUser();
    }

    @Override
    public void destroy() {
    }

    @Override
    protected void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException {
        response.setContentType("text/html");
        PortletSession session = request.getPortletSession();
        RapidData rapidData = null;
        try {
            rapidData = PortletSessionManager.getRapidSession(session, getUserName(request)).getRapidData();
            String page = rapidData.getPage();
            PortletRequestDispatcher rd = getPortletContext().getRequestDispatcher("/jsp/" + page + ".jsp");
            rd.include(request, response);
        } catch (SAXException ex) {
            String errorPage = "<H1>An XML parsing error has occurred</H1><p>";
            errorPage += ex.getMessage() + "<p>";
            errorPage += "<form action=" + response.createActionURL() + " method='post'>";
            errorPage += "<input type='submit' name='button' value='restart portlet'/>";
            errorPage += "</form>";
            response.getWriter().println(errorPage);
            request.setAttribute("initialise", "true");
        } catch (Exception ex) {
            String errorPage = "<H1>An error has occurred</H1><p>";
            errorPage += ex.getMessage() + "<p>";
            errorPage += ex.getClass() + "<p>";
            for (StackTraceElement element : ex.getStackTrace()) errorPage += element.toString() + "<p>";
            errorPage += "<form action=" + response.createActionURL() + " method='post'>";
            errorPage += "<input type='submit' name='button' value='restart portlet'/>";
            errorPage += "</form>";
            response.getWriter().println(errorPage);
            request.setAttribute("initialise", "true");
        }
    }

    @Override
    public void processAction(ActionRequest request, ActionResponse response) throws PortletException, IOException {
        Logger log = Logger.getLogger(RapidPortlet.class);
        PortletRequestParser parser = new PortletRequestParser();
        try {
            parser.parse(request);
        } catch (RapidException ex) {
            log.error("ERROR: " + ex.getMessage());
        }
        PortletSession session = request.getPortletSession();
        String initialise = parser.getFormValue("initialise");
        if (initialise != null) try {
            PortletSessionManager.Initialise(session, getUserName(request));
            return;
        } catch (Exception ex) {
            log.error("Error initialising session " + ex.getMessage());
        }
        RapidData rapidData = null;
        try {
            rapidData = PortletSessionManager.getRapidSession(session, getUserName(request)).getRapidData();
        } catch (Exception ex) {
            log.error("Error getting rapid session " + ex.getMessage());
            return;
        }
        boolean multipleSubmissions = true;
        synchronized (rapidData) {
            int serial = PortletSessionManager.getSerial(session);
            List<String> serialFormValues = parser.getFormValueList("serial");
            if (serialFormValues == null || serialFormValues.size() != 1) log.error("Error serial number not in page!"); else {
                String serialFromForm = parser.getFormValueList("serial").get(0);
                if (serialFromForm.equals(String.valueOf(serial))) {
                    multipleSubmissions = false;
                    PortletSessionManager.incSerial(session);
                }
            }
        }
        if (multipleSubmissions) {
            log.debug("Multiple submissions detected. This is probably due to a user doubleclicking or going back manually.");
            return;
        }
        String errorMessage = RapidEngine.action(rapidData, parser);
        if (errorMessage != null) response.setRenderParameter("errormessage", errorMessage);
    }
}
