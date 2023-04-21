package utility;

import java.io.*;
import java.sql.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.sql.DataSource;

public class DeleteRecord extends HttpServlet {

    private static final long serialVersionUID = 9222177154133363975L;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void destroy() {
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        PreparedStatement pstm = null;
        Connection cn = null;
        try {
            String idView = request.getParameter("idview");
            String pk_value = request.getParameter("pk_value");
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            DataSource ds = (DataSource) envCtx.lookup("jdbc/fisr");
            cn = ds.getConnection();
            String table = ViewsXML.getAttribute(idView, "name");
            int pk_pos = Integer.parseInt(ViewsXML.getAttribute(idView, "pk"));
            String[] pk_info = ViewsXML.getPk(idView, (pk_pos - 1));
            String pk_field = pk_info[0];
            String pk_type = pk_info[1];
            String sql = "DELETE FROM " + table + " WHERE " + pk_field + " = ?";
            pstm = cn.prepareStatement(sql);
            if (pk_type.equals("varchar")) {
                pstm.setString(1, pk_value);
            } else if ((pk_type.equals("int") || (pk_type.equals("counter")))) {
                pstm.setInt(1, Integer.parseInt(pk_value));
            }
            int flag = pstm.executeUpdate();
            out.println(flag);
        } catch (Exception e) {
            e.printStackTrace(out);
        } finally {
            try {
                if (pstm != null) pstm.close();
                if (cn != null) cn.close();
            } catch (SQLException e) {
                e.printStackTrace(out);
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    public String getServletInfo() {
        return "Delete record";
    }
}
