package net.sourceforge.pebble.web.action;

import net.sourceforge.pebble.Constants;
import net.sourceforge.pebble.domain.*;
import net.sourceforge.pebble.util.SecurityUtils;
import net.sourceforge.pebble.web.view.View;
import net.sourceforge.pebble.web.view.impl.BlogEntryFormView;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;

/**
 * Adds a new blog entry. This is called to create a blank blog entry
 * to populate a HTML form containing the contents of that entry.
 *
 * @author    Simon Brown
 */
public class AddBlogEntryAction extends SecureAction {

    /**
   * Peforms the processing associated with this action.
   *
   * @param request  the HttpServletRequest instance
   * @param response the HttpServletResponse instance
   * @return the name of the next view
   */
    public View process(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        Blog blog = (Blog) getModel().get(Constants.BLOG_KEY);
        BlogEntry blogEntryToClone = null;
        String entryToClone = request.getParameter("entryToClone");
        if (entryToClone != null && entryToClone.length() > 0) {
            BlogService service = new BlogService();
            try {
                blogEntryToClone = service.getBlogEntry(blog, entryToClone);
            } catch (BlogServiceException e) {
                throw new ServletException(e);
            }
        }
        BlogEntry entry = new BlogEntry(blog);
        if (blogEntryToClone != null) {
            entry.setTitle(blogEntryToClone.getTitle());
            entry.setSubtitle(blogEntryToClone.getSubtitle());
            entry.setBody(blogEntryToClone.getBody());
            entry.setExcerpt(blogEntryToClone.getExcerpt());
            entry.setTimeZoneId(blogEntryToClone.getTimeZoneId());
            entry.setCommentsEnabled(blogEntryToClone.isCommentsEnabled());
            entry.setTrackBacksEnabled(blogEntryToClone.isTrackBacksEnabled());
            Iterator it = blogEntryToClone.getCategories().iterator();
            while (it.hasNext()) {
                entry.addCategory((Category) it.next());
            }
            entry.setTags(blogEntryToClone.getTags());
        } else {
            entry.setTitle("Title");
            entry.setBody("<p>\n\n</p>");
        }
        entry.setAuthor(SecurityUtils.getUsername());
        getModel().put(Constants.BLOG_ENTRY_KEY, entry);
        return new BlogEntryFormView();
    }

    /**
   * Gets a list of all roles that are allowed to access this action.
   *
   * @return  an array of Strings representing role names
   * @param request
   */
    public String[] getRoles(HttpServletRequest request) {
        return new String[] { Constants.BLOG_CONTRIBUTOR_ROLE };
    }
}
