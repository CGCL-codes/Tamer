package org.compiere.cm;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.compiere.util.*;
import org.compiere.cm.utils.*;
import org.compiere.cm.xml.*;

/**
 * Broadcast Servlet
 * 
 * @author $Author$
 * @version $Id$
 */
public class StageBroadcast extends HttpServletCM {

    /**	serialVersionUID	*/
    private static final long serialVersionUID = 7348394433516908807L;

    /**
     * Handle Get Request
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession sess = request.getSession(true);
        sess.setMaxInactiveInterval(WebEnv.TIMEOUT);
        StringBuffer output = new StringBuffer();
        if (configLoaded && !fatalError) {
            String acceptLanguage = request.getHeader("Accept-Language");
            String acceptCharset = request.getHeader("Accept-Charset");
            LocaleHandler lhandler = new LocaleHandler(acceptLanguage, acceptCharset);
            RequestAnalyzer thisRequest = new RequestAnalyzer(this, request, true, "");
            if (thisRequest.getIsRedirect()) {
                response.sendRedirect(thisRequest.getRedirectURL());
            } else {
                Generator thisXMLGen = new Generator(this, request, thisRequest, new StringBuffer(""));
                String xmlCode = thisXMLGen.get();
                String xslCode = templateCache.getCM_Template(thisRequest.getCM_Container().getCM_Template_ID(), thisRequest.getWebProject().get_ID()).getTemplateXST();
                try {
                    output.append(XSLTProcessor.run(request, xslCode, xmlCode));
                } catch (Exception E) {
                    response.sendError(500);
                }
                response.setContentType("text/html; charset=" + lhandler.getCharset());
                response.setHeader("CMBuild", buildDate);
                PrintWriter out;
                out = response.getWriter();
                out.print(output.toString());
                out.close();
            }
        } else if (fatalError) {
            PrintWriter out;
            out = response.getWriter();
            out.print("<H1>Fatal Error:" + ErrorMessage + "</H1>");
            out.close();
        }
    }

    /**
     * Process Post Request - handled by Get
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
