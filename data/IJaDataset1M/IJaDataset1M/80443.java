package com.polytech.forum;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

public class AddForum extends HttpServlet {

    /**
	 * 
	 */
    private static final long serialVersionUID = 5633584753935412550L;

    DBConnectie db = new DBConnectie(Variable.getDb(), Variable.getDbLogin(), Variable.getDbPassword());

    static Logger logger = Logger.getLogger(AddForum.class);

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        request.setCharacterEncoding("UTF-8");
        try {
            logger.info(request.getParameter("username") + " added forum " + request.getParameter("title"));
            int lastforum_id = Integer.parseInt(request.getParameter("lastforum_id"));
            String forum_id = Integer.toString(lastforum_id + 1);
            String title = request.getParameter("title");
            if (title.equals("")) {
                title = "No title";
            } else {
                title = Filter.filterAll(title);
            }
            String forum_info = request.getParameter("forum_info");
            forum_info = Filter.filterAll(forum_info);
            db.connect();
            String query = "INSERT INTO forum_forums(forum_id,title,forum_info) " + "VALUES('" + forum_id + "','" + title + "','" + forum_info + "')";
            System.out.println(query);
            db.query(query);
            db.close();
            response.sendRedirect(Variable.getForumPath() + "index.jsp");
        } catch (Exception e) {
            out.println(e);
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
