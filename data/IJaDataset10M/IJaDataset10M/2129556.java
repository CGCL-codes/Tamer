package net.sourceforge.pebble.web.action;

import net.sourceforge.pebble.Constants;
import net.sourceforge.pebble.api.decorator.ContentDecoratorContext;
import net.sourceforge.pebble.util.SecurityUtils;
import net.sourceforge.pebble.util.CookieUtils;
import net.sourceforge.pebble.domain.*;
import net.sourceforge.pebble.web.view.NotFoundView;
import net.sourceforge.pebble.web.view.View;
import net.sourceforge.pebble.web.view.impl.BlogEntryView;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;

/**
 * Finds a particular blog entry, ready to be displayed.
 *
 * @author    Simon Brown
 */
public class ViewBlogEntryAction extends AbstractCommentAction {

    /**
   * Peforms the processing associated with this action.
   *
   * @param request  the HttpServletRequest instance
   * @param response the HttpServletResponse instance
   * @return the name of the next view
   */
    public View process(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        Blog blog = (Blog) getModel().get(Constants.BLOG_KEY);
        String entryId = request.getParameter("entry");
        BlogEntry blogEntry = null;
        if (entryId != null) {
            BlogService service = new BlogService();
            try {
                blogEntry = service.getBlogEntry(blog, entryId);
            } catch (BlogServiceException e) {
                throw new ServletException(e);
            }
        }
        if (blogEntry == null) {
            return new NotFoundView();
        } else if (!blogEntry.isPublished() && !(SecurityUtils.isUserAuthorisedForBlog(blog))) {
            return new NotFoundView();
        } else {
            getModel().put(Constants.BLOG_ENTRY_KEY, blogEntry);
            getModel().put(Constants.MONTHLY_BLOG, blog.getBlogForDay(blogEntry.getDate()).getMonth());
            getModel().put("displayMode", "detail");
            Cookie rememberMe = CookieUtils.getCookie(request.getCookies(), "rememberMe");
            if (rememberMe != null) {
                getModel().put("rememberMe", "true");
            }
            ContentDecoratorContext decoratorContext = new ContentDecoratorContext();
            decoratorContext.setView(ContentDecoratorContext.DETAIL_VIEW);
            decoratorContext.setMedia(ContentDecoratorContext.HTML_PAGE);
            Comment comment = createBlankComment(blog, blogEntry, request);
            Comment decoratedComment = (Comment) comment.clone();
            blog.getContentDecoratorChain().decorate(decoratorContext, decoratedComment);
            getModel().put("decoratedComment", decoratedComment);
            getModel().put("undecoratedComment", comment);
            return new BlogEntryView();
        }
    }
}
