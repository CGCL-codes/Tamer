package com.keyc.cms.servlet;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.keyc.cms.action.PostAction;
import com.keyc.cms.bean.Post;

public class PostManager extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostAction postAction = new PostAction();
        HttpSession session = request.getSession(true);
        String operation = request.getParameter("OPERATIONP");
        int ret = 0;
        if ("ADDPOST".equals(operation)) {
            String newsId = request.getParameter("pnewsid");
            String author = request.getParameter("pauthor");
            String email = request.getParameter("pauthoremail");
            String content = request.getParameter("pcontent");
            ret = postAction.addPost(newsId, author, email, content);
            if (ret > 0) {
                ArrayList<Post> list = postAction.getPostListByNewsID(newsId);
                session.removeAttribute("NEWSPOSTLIST");
                session.setAttribute("NEWSPOSTLIST", list);
            }
            response.sendRedirect("/site01/news.jsp?newsid=" + newsId + "&ret=" + ret);
        } else {
            response.sendRedirect("/site01/index.jsp");
        }
    }
}
