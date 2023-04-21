package com.jandan.web.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.servlet.view.RedirectView;
import com.jandan.logic.JWordzFacade;
import com.jandan.ui.client.util.ClientUtil;
import com.jandan.ui.model.Article;
import com.jandan.ui.model.Word;
import com.jandan.ui.model.WordLib;

public class JandanArticleController extends MultiActionController {

    private JWordzFacade jwordz;

    private String jandanImagePath;

    public void setJwordz(JWordzFacade jwordz) {
        this.jwordz = jwordz;
    }

    public void setJandanImagePath(String jandanImagePath) {
        this.jandanImagePath = jandanImagePath;
    }

    public ModelAndView displayAll(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String pageIndex = request.getParameter("pageIndex");
        int pi = ClientUtil.FIRST_PAGE_INDEX;
        int limit = ClientUtil.ARITCLE_PAGE_LIMIT;
        if (pageIndex != null) {
            pi = Integer.parseInt(pageIndex);
        }
        int totalArticleCount = jwordz.getTotalArticleCount();
        List<Article> articleList = jwordz.getAllArticleList(pi * limit, limit);
        if (articleList == null) {
            articleList = new ArrayList<Article>();
        }
        Map map = new HashMap();
        map.put("totalArticleCount", totalArticleCount);
        map.put("articleList", articleList);
        return new ModelAndView("admin_article", map);
    }

    public ModelAndView displayForm(HttpServletRequest request, HttpServletResponse reponse) {
        String articleID = request.getParameter("articleID");
        ModelAndView model = this.getModelWithArticle("admin_editArticleForm", articleID);
        return model;
    }

    public ModelAndView add(HttpServletRequest request, HttpServletResponse reponse) {
        String title = request.getParameter("title");
        String link = request.getParameter("link");
        String description = request.getParameter("description");
        String imagePath = jandanImagePath + request.getParameter("imagePath");
        Article article = new Article();
        article.setTitle(title);
        article.setDescription(description);
        article.setLink(link);
        article.setImagePath(imagePath);
        jwordz.insertArticle(article);
        return new ModelAndView(new RedirectView("article.htm"), "message", "文章添加成功");
    }

    public ModelAndView edit(HttpServletRequest request, HttpServletResponse reponse) {
        long articleID = Long.parseLong(request.getParameter("articleID"));
        String title = request.getParameter("title");
        String link = request.getParameter("link");
        String description = request.getParameter("description");
        String imagePath = jandanImagePath + request.getParameter("imagePath");
        Article article = new Article();
        article.setArticleID(articleID);
        article.setTitle(title);
        article.setDescription(description);
        article.setLink(link);
        article.setImagePath(imagePath);
        jwordz.updateArticle(article);
        return new ModelAndView(new RedirectView("article.htm"), "message", "文章保存成功");
    }

    public ModelAndView delete(HttpServletRequest request, HttpServletResponse reponse) {
        long articleID = Long.parseLong(request.getParameter("articleID"));
        jwordz.deleteArticle(articleID);
        return new ModelAndView(new RedirectView("article.htm"), "message", "文章删除成功");
    }

    private ModelAndView getModelWithArticle(String viewName, String articleID) {
        ModelAndView model = new ModelAndView(viewName);
        if (articleID != null) {
            long id = Long.parseLong(articleID);
            Article article = jwordz.getArticleByArticleID(id);
            model.addObject("article", article);
        }
        return model;
    }
}
