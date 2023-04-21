package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import beans.DBconn;

/**
 * Servlet implementation class for Servlet: AdminAgentDelete
 *
 */
public class AdminAgentDelete extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {

    public AdminAgentDelete() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("agent_delete");
        String userid = request.getParameter("userid");
        String sql = "delete from HP_ADMINISTRATOR.agent_reg where userid like '" + userid + "'";
        System.out.println(sql);
        DBconn dbc = new DBconn();
        Connection conn;
        Statement st;
        String send = "";
        conn = dbc.getConnection();
        try {
            st = conn.createStatement();
            int del = st.executeUpdate(sql);
            if (del > 0) send = "deleted"; else send = "error";
        } catch (SQLException e) {
            e.printStackTrace();
            send = "error";
        }
        response.getWriter().print(send);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
