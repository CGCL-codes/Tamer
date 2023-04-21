package de.wadndadn.commons.rss;

import static java.util.Collections.unmodifiableList;
import java.net.URL;
import java.util.List;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Represents a particular RSS feed.
 * <p>
 * Based on <a href="http://www.sitepoint.com/rss-feeds-jsp-based-web-apps/">Use Custom Tags to
 * Aggregate RSS Feeds into JSP-Based Web Apps</a>.
 * 
 * @author Simon Brown
 * @author TODO
 * 
 * @since TODO
 */
public final class RssFeed {

    /**
     * The url to the feed.
     */
    private final URL url;

    /**
     * The items contained within the feed.
     */
    private final List<RssItem> items;

    /**
     * Constructor.
     * 
     * @param items
     *            TODO Document
     */
    public RssFeed(final URL url, final List<RssItem> items) {
        this.url = url;
        this.items = items;
    }

    /**
     * Gets the url to this RSS feed.
     * 
     * @return the url to this RSS feed
     */
    public URL getUrl() {
        return url;
    }

    /**
     * Gets a list of all items within this RSS feed.
     * 
     * @return all {@link RssItem}s within this RSS feed
     */
    public List<RssItem> getItems() {
        return unmodifiableList(items);
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(19, 53).append(url).append(items).toHashCode();
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object that) {
        if (that == null) {
            return false;
        }
        if (this == that) {
            return true;
        }
        if (!(that instanceof RssFeed)) {
            return false;
        }
        RssFeed thatRssFeed = (RssFeed) that;
        return new EqualsBuilder().append(url, thatRssFeed.url).append(items, thatRssFeed.items).isEquals();
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer(url.toString());
        return sb.toString();
    }
}
