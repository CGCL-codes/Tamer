package org.socialresume.client.service;

import java.util.List;
import org.socialresume.client.model.FeedItem;
import org.socialresume.client.model.FeedTypeEnum;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {

    void getUpdates(FeedTypeEnum feedType, String feedUrl, AsyncCallback<List<FeedItem>> callback);

    void getTwitterUpdates(String twitterLogin, Long lastUpdateId, AsyncCallback<List<FeedItem>> callback);
}
