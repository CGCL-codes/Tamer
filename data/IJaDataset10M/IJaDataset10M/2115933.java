package org.apache.shindig.gadgets.rewrite;

import org.apache.shindig.common.uri.Uri;

/**
 * Rewrite a link
 */
public interface LinkRewriter {

    public String rewrite(String link, Uri context);
}
