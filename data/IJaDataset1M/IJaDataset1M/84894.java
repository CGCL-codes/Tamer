package org.nextplus.blog;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

public class User extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public User() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String method = request.getParameter("method");
        if (method == null) {
            method = "";
        }
        if (method.equals("login")) {
            login(request, response);
        } else if (method.equals("logout")) {
            logout(request, response);
        }
    }

    @SuppressWarnings({ "unchecked", "deprecation", "rawtypes" })
    protected void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String sql = "select * from  users where username=? and password=? ";
        Object params[] = { username, password };
        DataSource ds = null;
        try {
            Context context = new InitialContext();
            ds = (DataSource) context.lookup("java:comp/env/jdbc/blog");
        } catch (Exception e) {
            System.out.println("获取blog数据源错误！");
        }
        List<UserContent> list = null;
        UserContent userContent = null;
        try {
            QueryRunner qr = new QueryRunner(ds);
            list = (List<UserContent>) qr.query(sql, params, new BeanListHandler(UserContent.class));
            request.setAttribute("list", list);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (list.size() > 0) {
            userContent = list.get(0);
            HttpSession session = request.getSession();
            session.setAttribute("userContent", userContent);
            response.sendRedirect("/nextplus-blog/admin/admin.jsp");
        } else {
            request.setAttribute("message", "用户名或者密码错误，请检查！");
            response.sendRedirect("/nextplus-blog/admin/login.jsp");
        }
    }

    protected void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.invalidate();
        response.sendRedirect("/nextplus-blog/admin/login.jsp");
    }
}
