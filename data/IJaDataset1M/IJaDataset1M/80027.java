package org.opennms.web.performance;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.opennms.core.resource.Vault;

/**
 * A servlet that creates a plain text file with the list of RRD files.
 *
 * @author <A HREF="mailto:larry@opennms.org">Lawrence Karnowski</A>
 * @author <A HREF="http://www.opennms.org/">OpenNMS</A>
 */
public class RRDListServlet extends HttpServlet {

    /** Encapsulates the logic for this servlet. */
    protected PerformanceModel model;

    /**
	* Initializes this servlet by reading the rrdtool-graph properties file.
	*/
    public void init() throws ServletException {
        try {
            this.model = new PerformanceModel(Vault.getHomeDir());
        } catch (Exception e) {
            throw new ServletException("Could not initialize the performance model", e);
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/xml");
        PrintWriter out = response.getWriter();
        try {
            PerformanceModel.QueryableNode[] nodes = this.model.getQueryableNodes();
            for (int i = 0; i < nodes.length; i++) {
                if ((nodes[i].nodeLabel != null) && (!nodes[i].nodeLabel.equals(""))) {
                    out.println(nodes[i].nodeLabel + ", " + nodes[i].nodeId);
                } else {
                    out.println("&lt;blank&gt; (change this later)");
                }
            }
            out.close();
        } catch (java.sql.SQLException e) {
            throw new ServletException("An error occurred while trying to search for nodes with performance data: " + e.getLocalizedMessage(), e);
        }
    }
}
