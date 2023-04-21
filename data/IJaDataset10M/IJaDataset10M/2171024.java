package com.kwoksys.action.blogs;

import com.kwoksys.action.common.template.FooterTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.biz.ServiceAPI;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.blogs.BlogService;
import com.kwoksys.biz.blogs.dao.BlogQueries;
import com.kwoksys.biz.blogs.dto.BlogPost;
import com.kwoksys.biz.system.dto.Category;
import com.kwoksys.biz.system.dto.Link;
import com.kwoksys.framework.connection.database.QueryBits;
import com.kwoksys.framework.system.Access;
import com.kwoksys.framework.system.Localization;
import com.kwoksys.framework.configs.AppPaths;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Action class for portal index page.
 */
public class BlogIndexAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        AccessUser user = Access.getUser(request);
        List commentOptions = Arrays.asList(new LabelValueBean(Localization.getContent(request, "core.selectbox.selectOne"), String.valueOf(BlogPost.POST_ALLOW_COMMENT_SELECT_ONE)), new LabelValueBean(Localization.getContent(request, "common.boolean.yes_no.yes"), String.valueOf(BlogPost.POST_ALLOW_COMMENT_YES)), new LabelValueBean(Localization.getContent(request, "common.boolean.yes_no.no"), String.valueOf(BlogPost.POST_ALLOW_COMMENT_NO)));
        QueryBits query = new QueryBits();
        query.setOrderByColumn(BlogQueries.getOrderByColumn(Category.CATEGORY_NAME));
        List postCategoryIdList = new ArrayList();
        List postCategoryIdLabel = new ArrayList();
        postCategoryIdLabel.add(new LabelValueBean(Localization.getContent(request, "core.selectbox.selectOne"), ""));
        BlogService blogService = ServiceAPI.getBlogService();
        for (Category category : blogService.getCategories(query)) {
            Map map = new HashMap();
            map.put("postListParam", "?cmd=search&categoryId=" + category.getId());
            map.put("categoryName", category.getName());
            map.put("categoryId", category.getId());
            map.put("postCount", category.getCountObjects());
            postCategoryIdList.add(map);
            postCategoryIdLabel.add(new LabelValueBean(category.getName(), String.valueOf(category.getId())));
        }
        List linkList = new ArrayList();
        if (Access.hasPermission(user, AppPaths.BLOG_POST_LIST)) {
            Map linkMap = new HashMap();
            linkMap.put("urlPath", AppPaths.BLOG_POST_LIST + "?cmd=showAll");
            linkMap.put("urlText", Localization.getContent(request, "portal.index.showAllPosts"));
            linkList.add(linkMap);
        }
        request.setAttribute("postAllowCommentOptions", commentOptions);
        request.setAttribute("postListPath", AppPaths.BLOG_POST_LIST);
        request.setAttribute("postCategoryIdList", postCategoryIdList);
        request.setAttribute("postCategoryIdLabel", postCategoryIdLabel);
        request.setAttribute("linkList", linkList);
        HeaderTemplate header = new HeaderTemplate();
        header.setTitleKey("core.moduleName.6");
        if (Access.hasPermission(user, AppPaths.BLOG_CATEGORY_LIST_PATH)) {
            Link link = new Link();
            link.setPath(AppPaths.ROOT + AppPaths.BLOG_CATEGORY_LIST_PATH);
            link.setTitleKey("blogs.categoryList.header");
            header.addHeaderCmds(link);
        }
        if (Access.hasPermission(user, AppPaths.BLOG_POST_ADD_PATH)) {
            Link link = new Link();
            link.setPath(AppPaths.ROOT + AppPaths.BLOG_POST_ADD_PATH);
            link.setTitleKey("portal.cmd.blogPostAdd");
            header.addHeaderCmds(link);
        }
        header.apply(request);
        new FooterTemplate().apply(request);
        return mapping.findForward("success");
    }
}
