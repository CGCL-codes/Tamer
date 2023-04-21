package com.hs.mail.imap.message.request.ext;

import com.hs.mail.imap.message.request.SearchRequest;
import com.hs.mail.imap.message.search.SearchKey;
import com.hs.mail.imap.message.search.SortKey;

/**
 * 
 * @author Won Chul Doh
 * @since 31 Oct, 2010
 *
 */
public class SortRequest extends SearchRequest {

    private final SortKey sortKey;

    public SortRequest(String tag, String command, String charset, SortKey sortKey, SearchKey searchKey, boolean useUID) {
        super(tag, command, charset, searchKey, useUID);
        this.sortKey = sortKey;
    }

    public SortKey getSortKey() {
        return sortKey;
    }
}
