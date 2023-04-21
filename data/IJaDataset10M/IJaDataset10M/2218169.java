package org.blojsom.plugin.admin;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.blojsom.fetcher.Fetcher;
import org.blojsom.fetcher.FetcherException;
import org.blojsom.blog.Blog;
import org.blojsom.blog.Entry;
import org.blojsom.blog.User;
import org.blojsom.plugin.PluginException;
import org.blojsom.util.BlojsomConstants;
import org.blojsom.util.BlojsomUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author David Czarnecki
 * @version $Id: EditBlogPreferencesPlugin.java,v 1.2 2006/09/18 18:30:53 czarneckid Exp $
 */
public class EditBlogPreferencesPlugin extends BaseAdminPlugin {

    private static Log _logger = LogFactory.getLog(EditBlogPropertiesPlugin.class);

    private static final String EDIT_BLOG_PREFERENCES_PAGE = "/org/blojsom/plugin/admin/templates/admin-edit-blog-preferences";

    private static final String FAILED_EDIT_PREFERENCES_PERMISSION_KEY = "failed.edit.preferences.permission.text";

    private static final String EDIT_BLOG_PREFERENCES_PERMISSION = "edit_blog_preferences_permission";

    private static final String EDIT_BLOG_PREFERENCES_ACTION = "edit-blog-preferences";

    private static final String BLOJSOM_USER_OBJECT = "BLOJSOM_USER_OBJECT";

    private Fetcher _fetcher;

    /**
     * Default constructor.
     */
    public EditBlogPreferencesPlugin() {
    }

    /**
     * Set the {@link Fetcher}
     *
     * @param fetcher {@link Fetcher}
     */
    public void setFetcher(Fetcher fetcher) {
        _fetcher = fetcher;
    }

    /**
     * Process the blog entries
     *
     * @param httpServletRequest  Request
     * @param httpServletResponse Response
     * @param blog                {@link Blog} instance
     * @param context             Context
     * @param entries             Blog entries retrieved for the particular request
     * @return Modified set of blog entries
     * @throws PluginException If there is an error processing the blog entries
     */
    public Entry[] process(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Blog blog, Map context, Entry[] entries) throws PluginException {
        if (!authenticateUser(httpServletRequest, httpServletResponse, context, blog)) {
            httpServletRequest.setAttribute(BlojsomConstants.PAGE_PARAM, ADMIN_LOGIN_PAGE);
            return entries;
        }
        String username = getUsernameFromSession(httpServletRequest, blog);
        if (!checkPermission(blog, null, username, EDIT_BLOG_PREFERENCES_PERMISSION)) {
            httpServletRequest.setAttribute(BlojsomConstants.PAGE_PARAM, ADMIN_ADMINISTRATION_PAGE);
            addOperationResultMessage(context, getAdminResource(FAILED_EDIT_PREFERENCES_PERMISSION_KEY, FAILED_EDIT_PREFERENCES_PERMISSION_KEY, blog.getBlogAdministrationLocale()));
            return entries;
        }
        String action = BlojsomUtils.getRequestValue(ACTION_PARAM, httpServletRequest);
        if (BlojsomUtils.checkNullOrBlank(action)) {
            _logger.debug("User did not request edit action");
            httpServletRequest.setAttribute(BlojsomConstants.PAGE_PARAM, ADMIN_ADMINISTRATION_PAGE);
        } else if (PAGE_ACTION.equals(action)) {
            _logger.debug("User requested edit page");
            httpServletRequest.setAttribute(BlojsomConstants.PAGE_PARAM, EDIT_BLOG_PREFERENCES_PAGE);
        } else if (EDIT_BLOG_PREFERENCES_ACTION.equals(action)) {
            _logger.debug("User requested edit action");
            try {
                User user = _fetcher.loadUser(blog, username);
                String blogPropertyValue = BlojsomUtils.getRequestValue(BlojsomConstants.USE_RICHTEXT_EDITOR_PREFERENCE, httpServletRequest);
                user.getMetaData().put(BlojsomConstants.USE_RICHTEXT_EDITOR_PREFERENCE, blogPropertyValue);
                blogPropertyValue = BlojsomUtils.getRequestValue(BlojsomConstants.DISPLAY_RESPONSE_TEXT_PREFERENCE, httpServletRequest);
                user.getMetaData().put(BlojsomConstants.DISPLAY_RESPONSE_TEXT_PREFERENCE, blogPropertyValue);
                _fetcher.saveUser(blog, user);
            } catch (FetcherException e) {
                if (_logger.isErrorEnabled()) {
                    _logger.error(e);
                }
            }
            httpServletRequest.setAttribute(BlojsomConstants.PAGE_PARAM, EDIT_BLOG_PREFERENCES_PAGE);
        }
        try {
            context.put(BLOJSOM_USER_OBJECT, _fetcher.loadUser(blog, username));
        } catch (FetcherException e) {
            if (_logger.isErrorEnabled()) {
                _logger.error(e);
            }
        }
        return entries;
    }
}
