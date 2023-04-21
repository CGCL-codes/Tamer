package net.sourceforge.pebble.web.view.impl;

import net.sourceforge.pebble.Constants;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.web.view.HtmlView;

/**
 * Represents a page allowing the user to publish/unpublish a blog entry.
 *
 * @author    Simon Brown
 */
public class PublishBlogEntryView extends HtmlView {

    /**
   * Gets the title of this view.
   *
   * @return the title as a String
   */
    public String getTitle() {
        BlogEntry blogEntry = (BlogEntry) getModel().get(Constants.BLOG_ENTRY_KEY);
        return blogEntry.getTitle();
    }

    /**
   * Gets the URI that this view represents.
   *
   * @return the URI as a String
   */
    public String getUri() {
        return "/WEB-INF/jsp/publishBlogEntry.jsp";
    }
}
