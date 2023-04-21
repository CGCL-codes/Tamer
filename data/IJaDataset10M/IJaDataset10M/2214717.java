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
public class ViewBudgetServlet extends HttpServlet {

    servlet.util.NewGenXMLGenerator newGenXMLGenerator = null;

    servlet.util.HomeFactory homeFactory = null;

    /** Initializes the servlet.
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        newGenXMLGenerator = servlet.util.NewGenXMLGenerator.getInstance();
        homeFactory = servlet.util.HomeFactory.getInstance();
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
        response.setContentType("text/html");
        java.io.InputStream is = request.getInputStream();
        String xmlStr = newGenXMLGenerator.getXMLDocument(is);
        org.jdom.Element root = newGenXMLGenerator.getRootElementFromXMLDocument(xmlStr);
        ejb.bprocess.administration.ViewBudgetSourceSession viewBudgetSourceSession = null;
        try {
            viewBudgetSourceSession = ((ejb.bprocess.administration.ViewBudgetSourceSessionHome) homeFactory.getRemoteHome("ViewBudgetSourceSession")).create();
            if (viewBudgetSourceSession != null) {
                if (root.getAttributeValue("no").equals("1")) {
                    System.out.println("This is Servlet No 1:");
                    xmlStr = viewBudgetSourceSession.getBudgetSourceDetails(xmlStr);
                    java.io.OutputStream os = response.getOutputStream();
                    newGenXMLGenerator.compressXMLDocument(os, xmlStr);
                } else if (root.getAttributeValue("no").equals("2")) {
                    System.out.println("This is Servlet No 2:");
                    xmlStr = viewBudgetSourceSession.getModifyBudgetSourceDetails(xmlStr);
                    java.io.OutputStream os = response.getOutputStream();
                    newGenXMLGenerator.compressXMLDocument(os, xmlStr);
                } else if (root.getAttributeValue("no").equals("3")) {
                    System.out.println("This is Servlet No 3:");
                    xmlStr = viewBudgetSourceSession.deleteBudgetSource(xmlStr);
                    java.io.OutputStream os = response.getOutputStream();
                    newGenXMLGenerator.compressXMLDocument(os, xmlStr);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
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
}
