package com.hs.mail.imap.message.search;

/**
 * This class implements search-criteria for the Message Subject header.
 * 
 * @author Won Chul Doh
 * @since Jan 30, 2010
 * 
 */
public final class SubjectKey extends StringKey {

    public SubjectKey(String pattern) {
        super(pattern);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof SubjectKey)) return false;
        return super.equals(obj);
    }

    @Override
    public boolean isComposite() {
        return false;
    }
}
