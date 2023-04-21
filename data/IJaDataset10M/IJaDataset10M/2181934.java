package edu.univalle.lingweb.rest;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import edu.univalle.lingweb.Common;
import edu.univalle.lingweb.model.DataManagerUser;
import edu.univalle.lingweb.persistence.MaUser;

/**
 * @author raju
 *
 * @web.servlet name = "ApplicationXMLRest"
 *              display-name = "REST web service for application i18n"
 *              load-on-startup = "1"
 * @web.servlet-mapping url-pattern = "/lzproject/rest/application/*"
 */
public class ApplicationI18NController extends BaseRestController {

    /**
	 * Log4J logger.
	 * @uml.property  name="log"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    protected Logger log = Logger.getLogger(ApplicationI18NController.class);

    /**
     * The JSP containing the localized strings for LZProject.
     */
    private static final String APPLICATION_XML_JSP = Common.LINGWEBROOT + "/webservice/ApplicationXML.jsp";

    /**
     * Pass the request to the localization file to the ApplicationXML.jsp
     *
     * <p>
     * As there's no data being passed to this REST controller
     * it's safe to use HTTP GET here.
     *
     *
     * @param request
     *            an {@link HttpServletRequest} object that contains the request
     *            the client has made of the servlet
     *
     * @param response
     *            an {@link HttpServletResponse} object that contains the
     *            response the servlet sends to the client
     *
     * @exception IOException
     *                if an input or output error is detected when the servlet
     *                handles the GET request
     *
     * @exception ServletException
     *                if the request for the GET could not be handled
     *
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (log.isDebugEnabled()) {
            this.getRestMethod(request.getRequestURI(), request.getContextPath(), "project");
        }
        MaUser maUser = (MaUser) request.getSession().getAttribute("user");
        if (request.getParameter("updateLanguage") != null) {
            DataManagerUser.updateLanguageCode(new Long(request.getParameter("userLoginId")), request.getParameter("language"));
        }
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(APPLICATION_XML_JSP);
        log.debug("Forwarding to " + APPLICATION_XML_JSP);
        dispatcher.forward(request, response);
    }

    /**
		 */
    public void ssss() {
    }
}
