package jtweet.web;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import twitter4j.Query;
import twitter4j.TwitterException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jtweet.hack.TweetHelper;
import jtweet.util.Utils;
import jtweet.web.BaseServlet;
import jtweet.web.template.TexttoHTML;

@SuppressWarnings("serial")
public class SearchServlet extends BaseServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        getSearch(req, resp);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        getSearch(req, resp);
    }

    public void getSearch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html; charset=UTF-8");
        if (!isLogin(req)) {
            redirectIndex(resp);
            return;
        }
        init_twitter(req, resp);
        String s = req.getParameter("s");
        if (Utils.isEmptyOrNull(s)) {
            resp.sendRedirect("/home");
            return;
        }
        Query q = new Query(s);
        HashMap<String, Object> root = new HashMap<String, Object>();
        freemarker.template.Configuration config = new freemarker.template.Configuration();
        config.setDirectoryForTemplateLoading(new File("template"));
        config.setDefaultEncoding("UTF-8");
        try {
            root.put("title", "搜索");
            root.put("title_en", "Search");
            root.put("texttohtml", new TexttoHTML());
            root.put("login_user", login_user);
            root.put("tweets", TweetHelper.parseTweets(twitter.search(q).getTweets()));
            root.put("s", s);
            String[] js = { "/js/timeline.js" };
            root.put("js", js);
            Template t = config.getTemplate("search.ftl");
            t.process(root, resp.getWriter());
        } catch (TwitterException e) {
            if (e.getStatusCode() == 401) {
                redirectIndex(resp);
            } else if (e.getStatusCode() > 0) {
                resp.sendError(e.getStatusCode());
            } else {
                resp.getOutputStream().println("Error Message: " + e.getMessage());
            }
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }
}
