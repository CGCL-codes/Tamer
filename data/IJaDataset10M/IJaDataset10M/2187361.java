package sourceleads;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import sourceleads.*;

public class OpenAttachedFile extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        ConnectionProvider connectionProvider = (ConnectionProvider) session.getAttribute("ConnectionProvider");
        Connection connection = connectionProvider.getConnection();
        String QueId = request.getParameter("connId");
        int QueryId = Integer.parseInt(QueId);
        String query = "select AttachmentName ,ContentType, Attachment  from Attachment where AttachmentID =" + QueryId;
        System.out.println("Query on  OpenAttachedFile class is ---->" + query);
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String attachedFile = rs.getString(1);
                String FileContentType = rs.getString(2);
                response.setContentType(FileContentType);
                response.setHeader("Content-Disposition", "attachment;filename=" + attachedFile);
                PrintWriter out = response.getWriter();
                InputStream inputStr = rs.getBinaryStream(3);
                int bit = 256;
                while ((bit) >= 0) {
                    bit = inputStr.read();
                    out.write(bit);
                }
                out.flush();
                inputStr.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void destroy() {
    }
}
