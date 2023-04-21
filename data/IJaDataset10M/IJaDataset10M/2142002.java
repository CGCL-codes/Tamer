package com.google.gdata.data.youtube;

import com.google.gdata.data.AbstractExtension;
import com.google.gdata.data.ExtensionDescription;

/**
 * Object representation for the yt:private tag.
 *
 * 
 */
@ExtensionDescription.Default(nsAlias = YouTubeNamespace.PREFIX, nsUri = YouTubeNamespace.URI, localName = "private")
public class YtPrivate extends AbstractExtension {

    /** Creates an empty private tag. */
    public YtPrivate() {
    }
}
