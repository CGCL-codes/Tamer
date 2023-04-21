package org.tei.comparator.web.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface HighlightSourceServiceAsync {

    public void getHighlightedText(String sourceId, String derivedId, AsyncCallback<String> callback);
}
