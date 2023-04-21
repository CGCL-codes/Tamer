package servlet.administration;

import java.io.*;
import java.net.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author  Administrator
 * @version
 */
public class SMGSPServlet extends HttpServlet {

    servlet.util.NewGenXMLGenerator newGenXMLGenerator = null;

    /** Initializes the servlet.
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try {
            home = (ejb.bprocess.administration.GeneralSetUpSessionHome) ejb.bprocess.util.HomeFactory.getInstance().getRemoteHome("GeneralSetUpSession");
        } catch (Exception exp) {
            exp.printStackTrace(System.out);
        }
        newGenXMLGenerator = servlet.util.NewGenXMLGenerator.getInstance();
    }

    /** Destroys the servlet.
     */
    public void destroy() {
    }

    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String xml = "";
            java.io.InputStream ios = request.getInputStream();
            String xmlStr = newGenXMLGenerator.getXMLDocument(ios);
            org.jdom.Document doc = newGenXMLGenerator.getDocFromXMLDocument(xmlStr);
            String operationid = doc.getRootElement().getAttributeValue("no");
            if (operationid.equals("1")) {
                xml = (new servlet.administration.SMGSPHandler(home)).getGSP(doc);
                System.out.println("xml: " + xml);
            } else if (operationid.equals("2")) {
                xml = (new servlet.administration.SMGSPHandler(home)).updateGSP(doc);
                System.out.println("xml: " + xml);
            }
            java.io.OutputStream os = response.getOutputStream();
            newGenXMLGenerator.compressXMLDocument(os, xml);
        } catch (Exception exp) {
            System.out.println("In servlet: " + exp);
        }
    }

    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }

    private ejb.bprocess.administration.GeneralSetUpSessionHome home;
}
