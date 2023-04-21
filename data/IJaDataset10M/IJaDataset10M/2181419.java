package com.skruk.elvis.servlets;

import com.skruk.elvis.db.xml.DbEngine;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.xmldb.api.modules.*;

/**
 * DOCUMENT ME!
 *
 * @author     skruk
 * @created    19 grudzień 2003
 * @version
 */
public class ScanServlet extends HttpServlet {

    /**
	 * Initializes the servlet.
	 *
	 * @param  config                Description of the Parameter
	 * @exception  ServletException  Description of the Exception
	 */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    /** Destroys the servlet. */
    public void destroy() {
    }

    /**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
	 *
	 * @param  request               servlet request
	 * @param  response              servlet response
	 * @exception  ServletException  Description of the Exception
	 * @exception  IOException       Description of the Exception
	 */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/xml; charset=UTF-8");
        String docId = request.getParameter("docId");
        String raw = request.getParameter("raw");
        PrintWriter out = response.getWriter();
        if (docId == null) {
            out.println("<html>");
            out.println("<head>");
            out.println("<title>ScanServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<center><h1>.:: ScanServlet ::.</h1></center>");
            out.println("</body>");
            out.println("</html>");
        } else {
            this.transform(docId, raw, out);
        }
        out.close();
    }

    /**
	 * Description of the Method
	 *
	 * @param  docId  Description of the Parameter
	 * @param  raw    Description of the Parameter
	 * @param  out    Description of the Parameter
	 */
    public void transform(String docId, String raw, PrintWriter out) {
        try {
            DbEngine dbe = DbEngine.borrowEngine(DbEngine.S_ELVIS_DOC_COL);
            XMLResource resource = dbe.getDocument(docId);
            DbEngine.returnEngine(dbe);
            if (!"true".equals(raw)) {
                javax.xml.transform.Source source = new javax.xml.transform.dom.DOMSource(resource.getContentAsDOM());
                java.util.Map map = new java.util.HashMap();
                map.put("docId", docId);
                out.println(com.skruk.elvis.beans.XsltHelper.getInstance().transformRaw(source, "scannedBookChapter", map));
            } else {
                out.println(resource.getContent().toString());
            }
        } catch (org.xmldb.api.base.XMLDBException ex) {
            System.err.println("ScanServlet[81]:" + ex);
        } catch (com.skruk.elvis.db.DbException ex2) {
            System.err.println("ScanServlet[81]:" + ex2);
        } catch (Exception ex3) {
            System.err.println("[ERROR] Pooling error (ScanServlet:167) " + ex3);
        }
    }

    /**
	 * Handles the HTTP <code>GET</code> method.
	 *
	 * @param  request               servlet request
	 * @param  response              servlet response
	 * @exception  ServletException  Description of the Exception
	 * @exception  IOException       Description of the Exception
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
	 * Handles the HTTP <code>POST</code> method.
	 *
	 * @param  request               servlet request
	 * @param  response              servlet response
	 * @exception  ServletException  Description of the Exception
	 * @exception  IOException       Description of the Exception
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
	 * Returns a short description of the servlet.
	 *
	 * @return    The servletInfo value
	 */
    public String getServletInfo() {
        return "Scan Servlet";
    }
}
