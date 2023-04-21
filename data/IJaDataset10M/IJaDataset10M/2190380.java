package com.joebertj.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.w3c.tools.crypt.Md5;
import com.joebertj.helper.DatabaseConnection;

/**
 * Servlet implementation class for Servlet: Login
 *
 */
public class TestLogin extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {

    /**
	 * 
	 */
    private static final long serialVersionUID = -7904370882680674851L;

    private static Logger log = Logger.getLogger(TestLogin.class);

    public TestLogin() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession(true);
            String username = request.getParameter("user");
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("SELECT password,level FROM users LEFT JOIN branch ON users.branch=branch.branchid WHERE username = ? AND (users.archive=0 AND branch.archive=0)");
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            String badLogin = "No such user or bad password!";
            if (rs.first()) {
                Md5 md5 = new Md5(request.getParameter("pass"));
                md5.processString();
                String password = md5.getStringDigest();
                String password2 = rs.getString(1);
                log.info(password);
                log.info(password2);
                int level = rs.getInt(2);
                if (password.equals(password2)) {
                    session.setAttribute("authenticated", username);
                    session.setAttribute("level", new Integer(level));
                    String redirectURL = (String) session.getAttribute("referer");
                    if (redirectURL != null) {
                        session.setAttribute("referer", "index.jsp");
                        response.sendRedirect(redirectURL);
                    } else {
                        response.sendRedirect("index.jsp");
                    }
                } else {
                    String redirectURL = "common/login.jsp?msg=" + badLogin;
                    response.sendRedirect(redirectURL);
                }
            } else {
                String redirectURL = "common/login.jsp?msg=" + badLogin;
                response.sendRedirect(redirectURL);
            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }
}
