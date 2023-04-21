package evolaris.framework.blog.web.action;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import evolaris.framework.blog.business.BlogManager;
import evolaris.framework.blog.datamodel.Article;
import evolaris.framework.blog.datamodel.Blog;
import evolaris.framework.blog.datamodel.Comment;
import evolaris.framework.blog.web.DisplayableArticle;
import evolaris.framework.blog.web.DisplayableComment;
import evolaris.framework.sys.business.PermissionManager;
import evolaris.framework.sys.business.UserManagerBase;
import evolaris.framework.sys.business.exception.InputException;
import evolaris.framework.sys.web.action.AnonymousLocalizedAction;
import evolaris.framework.um.datamodel.User;
import evolaris.framework.blog.web.form.BlogArticleCommentForm;

/**
 * @author robert.brandner
 *
 */
public class ArticleAction extends AnonymousLocalizedAction {

    public static final int NOT_REVIEWED = 0;

    public static final int RELEASED = 1;

    public static final int BLOCKED = 2;

    @SuppressWarnings("unused")
    private static final Logger LOGGER = Logger.getLogger(ArticleAction.class);

    protected ActionForward executeAccordingToMethod(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse resp, String method) throws Exception {
        if ("view".equals(method)) {
            return view(mapping, form, req, resp);
        }
        if ("deleteComment".equals(method)) {
            return deleteComment(mapping, form, req, resp);
        }
        if ("releaseComment".equals(method)) {
            return changeCommentStatus(mapping, form, req, resp, RELEASED);
        }
        if ("blockComment".equals(method)) {
            return changeCommentStatus(mapping, form, req, resp, BLOCKED);
        }
        if ("releaseArticle".equals(method)) {
            return changeArticleStatus(mapping, form, req, resp, RELEASED);
        }
        if ("blockArticle".equals(method)) {
            return changeArticleStatus(mapping, form, req, resp, BLOCKED);
        }
        return super.executeAccordingToMethod(mapping, form, req, resp, method);
    }

    private ActionForward changeCommentStatus(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse resp, int newStatus) {
        if (!req.isUserInRole("blog_reviewer")) {
            throw new InputException(getLocalizedMessage("BloggingWeb", "blog.insufficientRights"));
        }
        BlogManager blogMgr = new BlogManager(locale, session);
        String idParam = req.getParameter("id") != null ? req.getParameter("id") : (String) req.getSession().getAttribute("id");
        Comment comment = blogMgr.getComment(Long.parseLong(idParam));
        if (comment == null) {
            throw new InputException(getLocalizedMessage("BloggingWeb", "blog.CommentNotFound", idParam));
        }
        Blog blog = blogMgr.getBlog(comment.getArticle().getBlog().getId());
        Set<Long> permissions = getPermissions(blog, webUser);
        if (!permissions.contains(PermissionManager.READ_PERMISSION)) {
            throw new InputException(getLocalizedMessage("BloggingWeb", "blog.insufficientRights"));
        }
        comment.setReviewStatus(newStatus);
        blogMgr.modifyComment(comment);
        ActionForward fwd = mapping.findForward("statusChanged");
        ActionForward newFwd = new ActionForward(fwd);
        newFwd.setPath(fwd.getPath() + "&id=" + comment.getArticle().getId());
        return newFwd;
    }

    private ActionForward changeArticleStatus(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse resp, int newStatus) {
        if (!req.isUserInRole("blog_reviewer")) {
            throw new InputException(getLocalizedMessage("BloggingWeb", "blog.insufficientRights"));
        }
        BlogManager blogMgr = new BlogManager(locale, session);
        String idParam = req.getParameter("id") != null ? req.getParameter("id") : (String) req.getSession().getAttribute("id");
        Article article = blogMgr.getArticle(Long.parseLong(idParam));
        if (article == null) {
            throw new InputException(getLocalizedMessage("BloggingWeb", "blog.ArticleNotFound", idParam));
        }
        Set<Long> permissions = getPermissions(article.getBlog(), webUser);
        if (!permissions.contains(PermissionManager.READ_PERMISSION)) {
            throw new InputException(getLocalizedMessage("BloggingWeb", "blog.insufficientRights"));
        }
        article.setReviewStatus(newStatus);
        blogMgr.modifyArticle(article);
        ActionForward fwd = mapping.findForward("statusChanged");
        ActionForward newFwd = new ActionForward(fwd);
        newFwd.setPath(fwd.getPath() + "&id=" + article.getId());
        return newFwd;
    }

    protected ActionForward view(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse resp) {
        return view(mapping, form, req, resp, false);
    }

    /**
	 * view an article specified by id 
	 */
    protected ActionForward view(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse resp, boolean viewReleasedCommentsOnly) {
        BlogManager blogMgr = new BlogManager(locale, session);
        String idParam = req.getParameter("id");
        Article article = blogMgr.getArticle(Long.parseLong(idParam));
        if (article == null) {
            throw new InputException(getLocalizedMessage("BloggingWeb", "blog.ArticleNotFound", idParam));
        }
        Blog blog = blogMgr.getBlog(article.getBlog().getId());
        Set<Long> permissions = getPermissions(blog, webUser);
        if (!permissions.contains(PermissionManager.READ_PERMISSION)) {
            throw new InputException(getLocalizedMessage("BloggingWeb", "blog.insufficientRights"));
        }
        req.setAttribute("blog", blog);
        req.getSession().setAttribute("blogCode", blog.getCode());
        DisplayableArticle da = new DisplayableArticle(article, true);
        if (da.getTitle() == null || da.getTitle().trim().length() == 0) {
            da.setTitle(getLocalizedMessage("BloggingWeb", "blog.noTitle"));
        }
        req.setAttribute("article", da);
        req.setAttribute("mayEditArticle", webUser != null && ((article.getAuthor() != null && webUser.getId() == article.getAuthor().getId() && permissions.contains(PermissionManager.WRITE_PERMISSION)) || permissions.contains(PermissionManager.EDIT_OTHERS_PERMISSION)));
        req.setAttribute("mayAddComment", permissions.contains(PermissionManager.ADD_COMMENT_PERMISSION));
        if (article.getAuthor() != null) {
            session.load(User.class, article.getAuthor().getId());
        }
        List<DisplayableComment> comments = new ArrayList<DisplayableComment>();
        for (Comment c : article.getComments()) {
            if (viewReleasedCommentsOnly && c.getReviewStatus() != 1) {
            } else {
                DisplayableComment dc = new DisplayableComment(c);
                comments.add(dc);
            }
        }
        da.setCommentCount(comments.size());
        req.setAttribute("commentList", comments);
        req.setAttribute("dateArchiveList", blogMgr.getArchives(blog));
        req.setAttribute("labelCloud", blogMgr.getLabelCloud(blog, webUser, null, 20));
        req.setAttribute("previousarticle", blogMgr.getPreviousArticle(article));
        req.setAttribute("nextarticle", blogMgr.getNextArticle(article));
        try {
            req.setAttribute("articleRssUrl", getBaseUrl(req) + "/viewBlog.do?method=rss&id=" + blog.getId());
            req.setAttribute("commentRssUrl", getBaseUrl(req) + "/viewBlog.do?method=commentrss&id=" + blog.getId());
        } catch (Exception e) {
            LOGGER.error("Failure creating rssUrls: " + e.getMessage(), e);
        }
        return mapping.findForward("view");
    }

    public ActionForward addComment(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse resp) {
        BlogArticleCommentForm f = (BlogArticleCommentForm) form;
        BlogManager blogMgr = new BlogManager(locale, session);
        Article article = blogMgr.getArticle(f.getArticleId());
        if (article == null) {
            throw new InputException(getLocalizedMessage("BloggingWeb", "blog.ArticleNotFound", f.getArticleId()));
        }
        Set<Long> permissions = getPermissions(article.getBlog(), webUser);
        if (!permissions.contains(PermissionManager.ADD_COMMENT_PERMISSION)) {
            throw new InputException(getLocalizedMessage("BloggingWeb", "blog.insufficientRights"));
        }
        if (f.getContent() != null && f.getContent().trim().length() > 0) {
            Comment comment = blogMgr.addComment(article, f.getContent(), webUser);
            LOGGER.info("User " + UserManagerBase.toString(webUser) + " added comment #" + comment.getId() + " to article #" + article.getId() + " (" + article.getTitle() + ") of blog #" + article.getBlog().getId() + " (" + article.getBlog().getName() + ")");
        }
        ActionForward fwd = injectId(mapping.findForward("view"), f.getArticleId());
        return fwd;
    }

    public ActionForward deleteComment(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse resp) {
        if (webUser == null) {
            throw new InputException(getResources(req).getMessage(locale, "blog.AnonymousEditingNotAllowed"));
        }
        BlogManager blogMgr = new BlogManager(locale, session);
        String idParam = req.getParameter("id");
        Comment comment = blogMgr.getComment(Long.parseLong(idParam));
        if (comment == null) {
            throw new InputException(getLocalizedMessage("BloggingWeb", "blog.CommentNotFound", idParam));
        }
        Set<Long> permissions = getPermissions(comment.getArticle().getBlog(), webUser);
        if (!permissions.contains(PermissionManager.WRITE_PERMISSION)) {
            throw new InputException(getLocalizedMessage("BloggingWeb", "blog.insufficientRights"));
        }
        Long id = comment.getArticle().getId();
        blogMgr.deleteComment(comment);
        LOGGER.info("User " + UserManagerBase.toString(webUser) + " deleted comment #" + comment.getId() + " (" + comment.getContent() + ") of article #" + comment.getArticle().getId() + " (" + comment.getArticle().getTitle() + ") of blog #" + comment.getArticle().getBlog().getId() + " (" + comment.getArticle().getBlog().getName() + ")");
        return injectId(mapping.findForward("deleted"), id);
    }

    @Override
    public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse resp) {
        BlogArticleCommentForm f = (BlogArticleCommentForm) form;
        return injectId(mapping.findForward("cancelled"), f.getArticleId());
    }

    @Override
    protected ActionForward defaultMethod(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse resp) {
        return view(mapping, form, req, resp);
    }

    @Override
    protected Map getKeyMethodMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("blog.addComment", "addComment");
        return map;
    }

    protected ActionForward injectId(ActionForward fwd, Long id) {
        ActionForward newFwd = new ActionForward(fwd);
        newFwd.setPath(fwd.getPath() + "&id=" + id);
        return newFwd;
    }

    /**
	 * Get permissions for specified blog for specified user.
	 * @param blog
	 * @return
	 */
    protected Set<Long> getPermissions(Blog blog, User user) {
        PermissionManager permissionMgr = new PermissionManager(locale, session);
        return permissionMgr.getPermissions(blog, user);
    }

    protected String getBaseUrl(HttpServletRequest request) throws Exception {
        String file = request.getRequestURI();
        file = file.substring(0, file.lastIndexOf("/"));
        URL reconstructedURL = new URL(request.getScheme(), request.getServerName(), request.getServerPort(), file);
        return reconstructedURL.toString();
    }
}
