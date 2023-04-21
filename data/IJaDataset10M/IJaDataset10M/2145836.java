package fi.yukatan.webmail.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.apache.commons.dbutils.DbUtils;

/**
 * TODO
 */
public class MessageSourceServlet extends HttpServlet {

    private DataSource datasource;

    public void init() throws ServletException {
        try {
            Context initial = new InitialContext();
            Context context = (Context) initial.lookup("java:comp/env");
            datasource = (DataSource) context.lookup("jdbc/yukatan");
        } catch (NamingException ex) {
            throw new ServletException(ex);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int msgno = Integer.parseInt(request.getPathInfo().substring(1));
            Connection connection = datasource.getConnection();
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                ps = connection.prepareStatement("SELECT msgsource FROM message WHERE msgno=?");
                ps.setInt(1, msgno);
                rs = ps.executeQuery();
                if (rs.next()) {
                    byte[] source = rs.getBytes(1);
                    response.setContentType(getInitParameter("mimetype"));
                    response.addHeader("Content-Disposition", getInitParameter("disposition"));
                    response.setContentLength(source.length);
                    response.getOutputStream().write(source);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
            } catch (NumberFormatException ex) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            } finally {
                DbUtils.closeQuietly(rs);
                DbUtils.closeQuietly(ps);
                DbUtils.closeQuietly(connection);
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }
}
