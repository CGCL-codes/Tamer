package org.socialresume.client.model.action;

import net.customware.gwt.dispatch.shared.Action;

public class GetUpdates implements Action<GetUpdatesResult> {

    private static final long serialVersionUID = -3439238482369570649L;

    private String feedLogin;

    private Long lastFeedId;

    public GetUpdates() {
    }

    public GetUpdates(String feedLogin, Long lastFeedId) {
        this.feedLogin = feedLogin;
        this.lastFeedId = lastFeedId;
    }

    public String getFeedLogin() {
        return feedLogin;
    }

    public Long getLastFeedId() {
        return lastFeedId;
    }
}
