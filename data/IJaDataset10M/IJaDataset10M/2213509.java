package servlet.administration;

import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author  Administrator
 * @version
 */
public class MeetingNameServlet extends HttpServlet {

    servlet.util.NewGenXMLGenerator newGenXMLGenerator = null;

    /** Initializes the servlet.
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try {
            home = (ejb.bprocess.administration.MeetingNameHome) ejb.bprocess.util.HomeFactory.getInstance().getRemoteHome("MeetingName");
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
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException {
        try {
            String xml = "";
            java.io.InputStream ios = request.getInputStream();
            String xmlStr = newGenXMLGenerator.getXMLDocument(ios);
            org.jdom.Document doc = newGenXMLGenerator.getDocFromXMLDocument(xmlStr);
            String operationid = doc.getRootElement().getAttributeValue("no");
            if (operationid.equals("1")) {
                xml = (new servlet.administration.MeetingNameHandler(home)).getNamesSearchAsExactPhrase(doc);
                System.out.println("xml: " + xml);
            } else if (operationid.equals("2")) {
                xml = (new servlet.administration.MeetingNameHandler(home)).compareWithWholeData(doc);
                System.out.println("xml: " + xml);
            } else if (operationid.equals("3")) {
                xml = (new servlet.administration.MeetingNameHandler(home)).saveMeetingName(doc);
                System.out.println("xml: " + xml);
            } else if (operationid.equals("4")) {
                xml = (new servlet.administration.MeetingNameHandler(home)).modifyMeetingName(doc);
                System.out.println("xml: " + xml);
            } else if (operationid.equals("5")) {
                xml = (new servlet.administration.MeetingNameHandler(home)).deleteAuthorityFile(doc);
            } else if (operationid.equals("6")) {
                System.out.println("in MeetingNameServlet..." + xmlStr);
                xml = (new servlet.administration.MeetingNameHandler(home)).setSearch(doc);
                System.out.println("Response in Meeting Name Servlet........." + xml);
            } else if (operationid.equals("7")) {
                xml = (new servlet.administration.MeetingNameHandler(home)).getAuthorisedHeadingDetails(doc);
            } else if (operationid.equals("8")) {
                xml = (new servlet.administration.MeetingNameHandler(home)).getSeeAlsoDetails(doc);
            } else if (operationid.equals("9")) {
                xml = (new servlet.administration.MeetingNameHandler(home)).createMeetingNameAuthorityRecord(xmlStr);
                System.out.println("in Meeting Name Servlet..." + xml);
            } else if (operationid.equals("10")) {
                xml = (new servlet.administration.MeetingNameHandler(home)).modifyAuthority(doc);
                System.out.println("in Meeting Name Servlet..." + xml);
            } else if (operationid.equals("11")) {
                xml = (new servlet.administration.MeetingNameHandler(home)).getSeeAlsoTermsForModify(doc);
                System.out.println("in Meeting Name Servlet..." + xml);
            } else if (operationid.equals("12")) {
                xml = (new servlet.administration.MeetingNameHandler(home)).deleteAuthorityFile2(doc);
                System.out.println("in Meeting Name Servlet..." + xml);
            } else if (operationid.equals("13")) {
                System.out.println("in MeetingNameServlet..." + xmlStr);
                xml = (new servlet.administration.MeetingNameHandler(home)).setSearchSolr(doc);
                System.out.println("Response in Meeting Name Servlet........." + xml);
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException {
        processRequest(request, response);
    }

    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException {
        processRequest(request, response);
    }

    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }

    private ejb.bprocess.administration.MeetingNameHome home;
}
