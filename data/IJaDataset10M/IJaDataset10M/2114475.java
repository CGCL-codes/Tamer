package com.walker.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.json.JSONObject;
import com.walker.model.User;

/**
 * Servlet implementation class for Servlet: UserName
 *
 */
public class UserName extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {

    static final long serialVersionUID = 1L;

    public UserName() {
        super();
    }

    private Connection getConnection() throws Exception {
        InitialContext ctx = new InitialContext();
        DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/cs420");
        return ds.getConnection();
    }

    public void getUserName(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, Exception {
        Connection con = getConnection();
        String result = "";
        try {
            PreparedStatement ps = con.prepareStatement("select * from user_info ");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getString(1);
            }
            JSONObject obj = new JSONObject();
            obj.put("username", result);
            result = obj.toString();
            System.err.println(result);
            rs.close();
            ps.close();
        } finally {
            if (con != null) {
                con.close();
            }
        }
        PrintWriter out = response.getWriter();
        out.println(result);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        System.err.println("Hello");
        try {
            if ("getUserName".equals(action)) {
                getUserName(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
