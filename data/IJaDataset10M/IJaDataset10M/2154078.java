package com.j2biz.blogunity.web.actions.secure.blog;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import com.j2biz.blogunity.dao.BlogDAO;
import com.j2biz.blogunity.exception.BlogunityException;
import com.j2biz.blogunity.i18n.I18N;
import com.j2biz.blogunity.i18n.I18NStatusFactory;
import com.j2biz.blogunity.pojo.Blog;
import com.j2biz.blogunity.web.ActionResultFactory;
import com.j2biz.blogunity.web.IActionResult;
import com.j2biz.blogunity.web.actions.secure.MyAbstractAction;

/**
 * @author michelson
 * @version $$
 * @since 0.1
 * 
 *  
 */
public class EditBlogFormAction extends MyAbstractAction {

    private static final IActionResult BLOG_FORM_FORWARD = ActionResultFactory.buildForward("/jsp/secure/editBlogForm.jsp");

    public IActionResult execute(HttpServletRequest request, HttpServletResponse response) throws BlogunityException {
        String id = request.getParameter("id");
        if (StringUtils.isEmpty(id)) {
            id = (String) request.getAttribute("id");
            if (StringUtils.isEmpty(id)) throw new BlogunityException(I18NStatusFactory.create(I18N.ERRORS.ID_NOT_SETTED, "Blog"));
        }
        BlogDAO dao = new BlogDAO();
        Blog b = dao.getBlogByID(Long.parseLong(id));
        if (user.getId().longValue() != b.getFounder().getId().longValue() && !user.isAdministrator()) throw new BlogunityException(I18NStatusFactory.create(I18N.ERRORS.USER_NOT_AUTHORIZED_FOR_EXECUTION));
        request.setAttribute("requestedBlog", b);
        navigationStack.popAllExceptFirst();
        navigationStack.push(ActionResultFactory.buildRedirect(I18N.MESSAGES.NAVI_EDIT_BLOG, currentActionPath));
        return BLOG_FORM_FORWARD;
    }
}
