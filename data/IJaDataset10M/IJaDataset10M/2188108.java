package com.bardsoftware.foronuvolo.client;

import java.util.List;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface StartDiscussionServiceAsync extends MessageSendService {

    void startDiscussion(String message, List<String> tags, AsyncCallback<String> callback);
}
