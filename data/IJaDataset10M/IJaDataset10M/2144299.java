package vobs.webapp;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author jbf
 * @version
 */
public class Das2Servlet extends HttpServlet {

    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/x-java-jnlp-file");
        PrintWriter out = response.getWriter();
        String arg0 = request.getParameter("arg0");
        String url = request.getRequestURL().toString();
        int i = url.indexOf("das2");
        String codeBase = url.substring(0, i);
        HashMap fieldMap = new HashMap();
        fieldMap.put("__CODEBASE__", codeBase);
        String href = "das2";
        if (request.getQueryString() != null) href += "?" + request.getQueryString();
        fieldMap.put("__HREF__", codeBase + href);
        fieldMap.put("__ARG__", arg0);
        URL templateUrl;
        String templateSurl = request.getParameter("jnlpTemplate");
        if (!templateSurl.contains("://")) {
            templateSurl = codeBase + templateSurl;
        }
        templateUrl = new URL(templateSurl);
        InputStream templateInputStream = templateUrl.openStream();
        BufferedReader templateReader = new BufferedReader(new InputStreamReader(templateInputStream));
        String s = templateReader.readLine();
        while (s != null) {
            int i0 = 0, i1 = 0;
            while ((i0 = s.indexOf("__", i1)) != -1) {
                i1 = s.indexOf("__", i0 + 1);
                i1 += 2;
                String field = s.substring(i0, i1);
                s = s.replace(field, (String) fieldMap.get(field));
            }
            out.println(s);
            s = templateReader.readLine();
        }
        out.close();
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
