package com.bardsoftware.foronuvolo.client;

import java.util.List;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("sd")
public interface StartDiscussionService extends RemoteService {

    public String startDiscussion(String message, List<String> tags);

    public String sendMessage(String message);
}
