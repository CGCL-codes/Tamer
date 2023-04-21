package com.javaeedev.web.blog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import com.javaeedev.domain.Article;
import com.javaeedev.domain.BlogCategory;
import com.javaeedev.domain.User;
import com.javaeedev.util.HttpUtil;
import com.javaeedev.web.AbstractBaseController;
import com.javaeedev.web.Page;

/**
 * Show articles in specified category.
 * 
 * @author Xuefeng
 * 
 * @spring.bean name="/blog/category.jspx"
 * @spring.property name="signon" value="false"
 */
public class CategoryController extends AbstractBaseController {

    @Override
    @SuppressWarnings("unchecked")
    protected ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String categoryId = HttpUtil.getString(request, "categoryId");
        BlogCategory category = facade.queryBlogCategory(categoryId);
        User user = category.getUser();
        List<BlogCategory> categories = facade.queryBlogCategories(user);
        List<Article> recentArticles = facade.queryArticles(user, new Page(1, 5));
        int pageIndex = HttpUtil.getInt(request, "page", 1);
        Page page = new Page(pageIndex);
        List<Article> articles = facade.queryArticles(category, page);
        Map map = new HashMap();
        map.put("user", user);
        map.put("category", category);
        map.put("categories", categories);
        map.put("page", page);
        map.put("articles", articles);
        map.put("recentArticles", recentArticles);
        return new ModelAndView("/blog/theme/" + fileFacade.getTheme(user.getTheme()) + "/category.htm", map);
    }
}
