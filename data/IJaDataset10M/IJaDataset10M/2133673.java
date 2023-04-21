package org.jazzteam.chat;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jazzteam.util.DatabaseConnector;

public class UserChat extends HttpServlet {

    private DatabaseConnector database = null;

    private ResultSet chatMessage = null;

    private String content = "";

    private Date date = null;

    private DateFormat dateFormat = null;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException {
        content = request.getParameter("message");
        try {
            if (database == null) {
                database = new DatabaseConnector();
            }
            if (content.length() > 0) {
                date = new Date();
                dateFormat = new SimpleDateFormat("yy-MM-dd");
                database.insert("insert into chatmessage values (\"" + content.toString() + "\",\"" + dateFormat.format(date) + "\",\"" + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds() + "\"" + ");");
                content = "";
            }
        } catch (ClassNotFoundException e) {
        } catch (SQLException e) {
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException {
        try {
            database = new DatabaseConnector();
            chatMessage = database.query("SELECT * FROM chatmessage");
            content = "";
            while (chatMessage.next()) {
                content += chatMessage.getString("timepost") + "  " + chatMessage.getString("datepost") + "\n" + chatMessage.getString("message") + "\n";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(content);
    }
}
