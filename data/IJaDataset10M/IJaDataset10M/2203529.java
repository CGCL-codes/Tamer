package org.plog4u.wiki.actions.mediawiki.connect;

public class Stored {

    String actionURL;

    String charSet;

    String title;

    Content content;

    boolean conflict;

    /** contains the reloaded remote Content or the remote conflict Content */
    public Stored(String actionURL, String charSet, String title, Content content, boolean conflict) {
        this.actionURL = actionURL;
        this.charSet = charSet;
        this.title = title;
        this.content = content;
        this.conflict = conflict;
    }
}
