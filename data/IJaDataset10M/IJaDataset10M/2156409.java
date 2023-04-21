package net.sourceforge.pebble.web.action;

import net.sourceforge.pebble.Constants;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.Tag;
import net.sourceforge.pebble.web.view.ForwardView;
import net.sourceforge.pebble.web.view.View;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Performs a search on the current blog.
 *
 * @author    Simon Brown
 */
public class AdvancedSearchAction extends Action {

    /**
   * Peforms the processing associated with this action.
   *
   * @param request  the HttpServletRequest instance
   * @param response the HttpServletResponse instance
   * @return the name of the next view
   */
    public View process(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        Blog blog = (Blog) request.getAttribute(Constants.BLOG_KEY);
        StringBuffer query = new StringBuffer();
        addTerm(query, "title", request.getParameter("title"));
        addTerm(query, "body", request.getParameter("body"));
        addTerms(query, "category", request.getParameterValues("category"));
        addTerm(query, "author", request.getParameter("author"));
        String tags = request.getParameter("tags");
        if (tags != null) {
            String s[] = tags.split(",");
            for (int i = 0; i < s.length; i++) {
                s[i] = Tag.encode(s[i].trim());
            }
            addTerms(query, "tag", s);
        }
        try {
            String encodedQuery = URLEncoder.encode(query.toString(), blog.getCharacterEncoding());
            return new ForwardView("/search.action?query=" + encodedQuery);
        } catch (UnsupportedEncodingException uee) {
            throw new ServletException(uee);
        }
    }

    private void addTerm(StringBuffer query, String key, String value) {
        if (value != null && value.trim().length() > 0) {
            if (query.length() > 0) {
                query.append(" AND ");
            }
            query.append(key + ":" + value.trim());
        }
    }

    private void addTerms(StringBuffer query, String key, String terms[]) {
        if (terms != null) {
            for (int i = 0; i < terms.length; i++) {
                addTerm(query, key, terms[i]);
            }
        }
    }
}
