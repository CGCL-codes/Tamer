package com.j2biz.blogunity.web.actions.secure.blog;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.j2biz.blogunity.dao.BlogDAO;
import com.j2biz.blogunity.exception.BlogunityException;
import com.j2biz.blogunity.i18n.I18N;
import com.j2biz.blogunity.i18n.I18NStatusFactory;
import com.j2biz.blogunity.pojo.Blog;
import com.j2biz.blogunity.web.ActionResultFactory;
import com.j2biz.blogunity.web.IActionResult;
import com.j2biz.blogunity.web.actions.secure.MyAbstractAction;

/**
 * 
 * @author michelson
 * @version $$
 * @since 0.1
 * 
 *  
 */
public class FavoriteBlogDeleteConfirmAction extends MyAbstractAction {

    private static final IActionResult DELETE_CONFIRM_FORWARD = ActionResultFactory.buildForward("/jsp/secure/deleteFavoriteBlogConfirm.jsp");

    public IActionResult execute(HttpServletRequest request, HttpServletResponse response) throws BlogunityException {
        String blogId = request.getParameter("id");
        Blog b = (new BlogDAO()).getBlogByID(Long.parseLong(blogId));
        if (!user.getFavoriteBlogs().contains(b)) throw new BlogunityException(I18NStatusFactory.create(I18N.ERRORS.USER_NOT_AUTHORIZED_FOR_EXECUTION));
        request.setAttribute("requestedBlog", b);
        navigationStack.push(ActionResultFactory.buildRedirect(I18N.MESSAGES.NAVI_DELETE_FAVORITE_BLOG, currentActionPath));
        return DELETE_CONFIRM_FORWARD;
    }
}
