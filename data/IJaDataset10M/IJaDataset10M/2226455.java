package org.verus.ngl.web.master;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.verus.ngl.web.util.NewGenWebUtility;
import org.verus.ngl.sl.utilities.*;

/**
 *
 * @author root
 */
public class admin extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/x-java-jnlp-file");
        PrintWriter out = response.getWriter();
        java.util.Properties props = new java.util.Properties();
        NewGenLibRoot nglRoot = (NewGenLibRoot) NGLBeanFactory.getInstance().getBean("newGenLibRoot");
        props.load(new java.io.FileInputStream(nglRoot.getRoot() + "/SystemFiles/ENV_VAR.txt"));
        String ipaddress = "";
        String port = "";
        ipaddress = props.getProperty("IPADDRESS");
        port = props.getProperty("PORT");
        if (ipaddress == null || ipaddress.equals("")) {
            ipaddress = "localhost";
        }
        if (port == null || port.equals("")) {
            port = "8080";
        }
        out.println("          <?xml version=\"1.0\" encoding=\"UTF-8\"?> ");
        out.println("          <jnlp spec=\"1.0+\" codebase=\"http://" + ipaddress + ":" + port + "/NGL\" href=\"http://" + ipaddress + ":" + port + "/NGL/admin\">  ");
        out.println("          <information>  ");
        out.println("          <title>NewGenLib</title>  ");
        out.println("          <vendor>KIIKM and VSPL</vendor>  ");
        out.println("          <homepage href=\"http://www.newgenlib.com\"/>  ");
        out.println("          <description>New generation in library automation</description>  ");
        out.println("          <description kind=\"short\"></description>  ");
        out.println("          <icon href=\"http://" + ipaddress + ":" + port + "/NGL/newgenliblogo.GIF\"/>  ");
        out.println("          <offline-allowed/>  ");
        out.println("          </information>  ");
        out.println("          <security>  ");
        out.println("          <all-permissions/>  ");
        out.println("          </security>  ");
        out.println("          <resources>  ");
        out.println("          <j2se version=\"1.6\"/>  ");
        out.println("          <jar href=\"http://" + ipaddress + ":" + port + "/NGL/NGLv3Client.jar\"/>  ");
        out.println("          <jar href=\"http://" + ipaddress + ":" + port + "/NGL/jdom.jar\"/>  ");
        out.println("          <jar href=\"http://" + ipaddress + ":" + port + "/NGL/swingx-0.9.0.jar\"/>  ");
        out.println("          <jar href=\"http://" + ipaddress + ":" + port + "/NGL/NGLUtilities.jar\"/>  ");
        out.println("          </resources>  ");
        out.println("<application-desc main-class=\"org.verus.ngl.client.main.NGLMain\"> ");
        out.println("<argument>" + ipaddress + "</argument> ");
        out.println("<argument>" + port + "</argument> ");
        out.println("</application-desc> ");
        out.println("</jnlp>");
        out.close();
    }

    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }
}
