package com.bardsoftware.foronuvolo.data;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import javax.cache.Cache;
import com.bardsoftware.foronuvolo.server.MessageCache;
import com.bardsoftware.foronuvolo.server.WikiMessageFormatter;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.repackaged.com.google.common.base.Pair;

public class FeedItem {

    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    private AnswersViewItem myMessage;

    private DiscussionsViewItem myDiscussion;

    public FeedItem(Discussion d, Message m, Cache messageCache, WikiMessageFormatter formatter) {
        myMessage = new AnswersViewItem(m, new MessageCache<Key, String>(messageCache, "fa_"));
        myDiscussion = new DiscussionsViewItem(d, new MessageCache<Key, Pair<String, Boolean>>(messageCache, "t_"), formatter);
    }

    public String getID() {
        return myDiscussion.getRefID();
    }

    public String getViewLink() {
        return myDiscussion.getViewLink();
    }

    public String getText() {
        return myMessage.getText();
    }

    public String getUpdated() {
        return DATE_FORMAT.format(myMessage.getCreation());
    }

    public String getTitle() {
        String title = myDiscussion.getTitle();
        if ("".equals(title)) {
            title = "untitled discussion";
        }
        return MessageFormat.format("{0} says in \"{1}\"", myMessage.getUserDisplayName(), title);
    }
}
