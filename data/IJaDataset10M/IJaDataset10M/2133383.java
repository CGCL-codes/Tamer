package com.kwoksys.action.blogs;

import com.kwoksys.action.common.template.FooterTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.biz.ServiceAPI;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.blogs.BlogService;
import com.kwoksys.biz.blogs.dao.BlogQueries;
import com.kwoksys.biz.blogs.dto.BlogPost;
import com.kwoksys.biz.system.dto.Category;
import com.kwoksys.framework.configs.AppPaths;
import com.kwoksys.framework.connection.database.QueryBits;
import com.kwoksys.framework.system.Localization;
import com.kwoksys.framework.system.Access;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Action class for adding blog post.
 */
public class BlogPostAddAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        AccessUser user = Access.getUser(request);
        BlogPost post = new BlogPost();
        BlogPostAddForm actionForm = (BlogPostAddForm) form;
        if (getErrors(request).isEmpty()) {
            actionForm.setPostTitle(post.getPostTitle());
            actionForm.setPostBody(post.getPostBody());
            actionForm.setPostAllowComment(post.getPostAllowComment());
            actionForm.setPostCategoryId(post.getCategoryId());
        }
        List commentOptions = new ArrayList();
        commentOptions.add(new LabelValueBean(Localization.getContent(request, "blogs.colData.post_allow_comment.1"), String.valueOf(BlogPost.POST_ALLOW_COMMENT_YES)));
        commentOptions.add(new LabelValueBean(Localization.getContent(request, "blogs.colData.post_allow_comment.0"), String.valueOf(BlogPost.POST_ALLOW_COMMENT_NO)));
        String postCreatorText = Localization.getContent(request, "blogs.colName.creation_preview_info", new Object[] { user.getDisplayName() });
        QueryBits query = new QueryBits();
        query.setOrderByColumn(BlogQueries.getOrderByColumn(Category.CATEGORY_NAME));
        List categoryIdOptions = new ArrayList();
        BlogService blogService = ServiceAPI.getBlogService();
        for (Category category : blogService.getCategories(query)) {
            categoryIdOptions.add(new LabelValueBean(category.getName(), String.valueOf(category.getId())));
        }
        request.setAttribute("formAction", AppPaths.BLOG_POST_ADD_2_PATH);
        request.setAttribute("formCancelAction", AppPaths.ROOT + AppPaths.BLOG_POST_LIST);
        request.setAttribute("postAllowCommentOptions", commentOptions);
        request.setAttribute("postCategoryIdOptions", categoryIdOptions);
        request.setAttribute("postCreatorText", postCreatorText);
        HeaderTemplate header = new HeaderTemplate();
        header.setOnloadJavascript("focusField(document.blogPostAddForm.postTitle);");
        header.apply(request);
        new FooterTemplate().apply(request);
        return mapping.findForward("success");
    }
}
