package com.google.gdata.data.sites;

import com.google.gdata.util.common.xml.XmlNamespace;
import com.google.gdata.data.BaseFeed;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Link;
import com.google.gdata.data.extensions.FeedLink;

/**
 * Describes a site feed.
 *
 * 
 */
public class SiteFeed extends BaseFeed<SiteFeed, SiteEntry> {

    /**
   * Default mutable constructor.
   */
    public SiteFeed() {
        super(SiteEntry.class);
    }

    /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link BaseFeed} instance.
   *
   * @param sourceFeed source feed
   */
    public SiteFeed(BaseFeed<?, ?> sourceFeed) {
        super(SiteEntry.class, sourceFeed);
    }

    @Override
    public void declareExtensions(ExtensionProfile extProfile) {
        if (extProfile.isDeclared(SiteFeed.class)) {
            return;
        }
        super.declareExtensions(extProfile);
        extProfile.declare(SiteFeed.class, new ExtensionDescription(FeedLink.class, new XmlNamespace("gd", "http://schemas.google.com/g/2005"), "feedLink", true, false, false));
        new FeedLink().declareExtensions(extProfile);
        extProfile.declare(SiteFeed.class, SitesLink.getDefaultDescription(true, true));
    }

    /**
   * Returns the sites ACL feed link.
   *
   * @return sites ACL feed link
   */
    public FeedLink getFeedLink() {
        return getExtension(FeedLink.class);
    }

    /**
   * Sets the sites ACL feed link.
   *
   * @param feedLink sites ACL feed link or <code>null</code> to reset
   */
    public void setFeedLink(FeedLink feedLink) {
        if (feedLink == null) {
            removeExtension(FeedLink.class);
        } else {
            setExtension(feedLink);
        }
    }

    /**
   * Returns whether it has the sites ACL feed link.
   *
   * @return whether it has the sites ACL feed link
   */
    public boolean hasFeedLink() {
        return hasExtension(FeedLink.class);
    }

    /**
   * Returns the link that provides the URI that can be used to edit the entry.
   *
   * @return Link that provides the URI that can be used to edit the entry or
   *     {@code null} for none.
   */
    public Link getEditLink() {
        return getLink(Link.Rel.ENTRY_EDIT, Link.Type.ATOM);
    }

    @Override
    protected void validate() {
    }

    @Override
    public String toString() {
        return "{SiteFeed " + super.toString() + "}";
    }
}
