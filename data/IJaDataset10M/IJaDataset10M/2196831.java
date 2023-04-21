package pub.servlets;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.sql.*;
import pub.db.Pool;
import pub.db.ArticleTable;
import pub.utils.StringUtils;
import pub.servlets.Login;

public class UpdateArticleWithId extends PubServlet {

    public void doService(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String return_url = request.getParameter("return_url");
        if (return_url == null) {
            throw new ServletException("return_url is a required parameter");
        }
        String user_id = Login.getUserId(request);
        if (user_id == null) {
            Login.redirectToLoginServlet(request, response);
            return;
        }
        updateArticle(conn, user_id, request);
        response.sendRedirect(return_url);
    }

    private void updateArticle(Connection conn, String user_id, HttpServletRequest request) {
        ArticleTable update = new ArticleTable(conn);
        String article_id = StringUtils.safeGetParameter("article_id", request, "");
        try {
            update.updateThisField(article_id, "scanned_date", StringUtils.safeGetParameter("scanned_date", request, ""), user_id);
            update.updateThisField(article_id, "scanned_by", StringUtils.safeGetParameter("scanned_by", request, ""), user_id);
            update.updateThisField(article_id, "is_local_hard", StringUtils.safeGetParameter("is_local_hard", request, ""), user_id);
            update.updateThisField(article_id, "is_downloaded", StringUtils.safeGetParameter("is_downloaded", request, ""), user_id);
            update.updateThisField(article_id, "is_scanned", StringUtils.safeGetParameter("is_scanned", request, ""), user_id);
            update.updateThisField(article_id, "year", StringUtils.safeGetParameter("year", request, ""), user_id);
            update.updateThisField(article_id, "volume", StringUtils.safeGetParameter("volume", request, ""), user_id);
            update.updateThisField(article_id, "page_start", StringUtils.safeGetParameter("page_start", request, ""), user_id);
            update.updateThisField(article_id, "issue", StringUtils.safeGetParameter("issue", request, ""), user_id);
            update.updateThisField(article_id, "type", StringUtils.safeGetParameter("article_type", request, ""), user_id);
            update.updateThisField(article_id, "reference_id", StringUtils.safeGetParameter("reference_id", request, ""), user_id);
            update.updateObsoleteField(article_id, StringUtils.safeGetParameter("is_obsolete", request, ""), StringUtils.safeGetParameter("replaced_by", request, ""), user_id);
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e);
        }
    }
}
