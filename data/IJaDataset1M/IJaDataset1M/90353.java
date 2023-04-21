package org.opennms.web.map;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.batik.dom.util.DOMUtilities;
import org.w3c.dom.Document;

/**
 * This class should be called from inside of an <embed> tag.  We
 * generate and emit an SVG document of the tree map of nodes.  (this
 * used to be svg.jsp but it's neater to put it into a servlet where
 * it really belongs.
 *
 * @author <A HREF="mailto:dglidden@opennms.org">Derek Glidden</A>
 * @author <A HREF="http://www.nksi.com/">NKSi</A>
 */
public class SVGServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        response.setContentType("image/svg+xml");
        try {
            DocumentGenerator docgen = (DocumentGenerator) request.getSession().getAttribute("docgen");
            Document doc = docgen.getHostDocument(false);
            PrintWriter docwriter = response.getWriter();
            DOMUtilities.writeDocument(doc, docwriter);
            docwriter.flush();
            docwriter.close();
        } catch (IOException e) {
            log("IOException in SVGServlet");
            log(e.toString());
        } catch (Exception e) {
            log("Exception in SVGServlet");
            log(e.toString());
        }
    }
}
