package com.google.gdata.data.docs;

import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.docs.RevisionFeed;
import com.google.gdata.data.extensions.FeedLink;
import com.google.gdata.util.Namespaces;

/**
 * Represents a FeedLink referring to a RevisionFeed.
 *
 * 
 */
@ExtensionDescription.Default(nsAlias = Namespaces.gAlias, nsUri = Namespaces.g, localName = "feedLink", isRepeatable = true)
public class DocumentListRevisionFeedLink extends FeedLink<RevisionFeed> {

    public DocumentListRevisionFeedLink() {
        super(RevisionFeed.class);
    }

    @Override
    public void declareExtensions(ExtensionProfile extProfile) {
        super.declareExtensions(extProfile);
        ExtensionProfile ep = new ExtensionProfile();
        new RevisionFeed().declareExtensions(ep);
        extProfile.declareFeedLinkProfile(ep);
    }
}
