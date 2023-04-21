package org.opennms.web.admin.nodeManagement;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.opennms.netmgt.config.DataSourceFactory;

/**
 * A servlet that handles querying the database for node information
 * 
 * @author <A HREF="mailto:tarus@opennms.org">Tarus Balog </A>
 * @author <A HREF="http://www.opennms.org/">OpenNMS </A>
 */
public class DeleteGetNodesServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String NODE_QUERY = "SELECT nodeid, nodelabel FROM node WHERE nodetype != 'D' ORDER BY nodelabel, nodeid";

    public void init() throws ServletException {
        try {
            DataSourceFactory.init();
        } catch (Exception e) {
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession user = request.getSession(true);
        try {
            user.setAttribute("listAll.delete.jsp", getAllNodes(user));
        } catch (SQLException e) {
            throw new ServletException(e);
        }
        RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/admin/delete.jsp");
        dispatcher.forward(request, response);
    }

    /**
     */
    private List<ManagedNode> getAllNodes(HttpSession userSession) throws SQLException {
        Connection connection = null;
        List<ManagedNode> allNodes = new ArrayList<ManagedNode>();
        int lineCount = 0;
        try {
            connection = DataSourceFactory.getInstance().getConnection();
            Statement stmt = connection.createStatement();
            ResultSet nodeSet = stmt.executeQuery(NODE_QUERY);
            if (nodeSet != null) {
                while (nodeSet.next()) {
                    ManagedNode newNode = new ManagedNode();
                    newNode.setNodeID(nodeSet.getInt(1));
                    newNode.setNodeLabel(nodeSet.getString(2));
                    allNodes.add(newNode);
                }
            }
            userSession.setAttribute("lineItems.delete.jsp", new Integer(lineCount));
            nodeSet.close();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                }
            }
        }
        return allNodes;
    }
}
