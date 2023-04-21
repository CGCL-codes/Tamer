package com.gnizr.web.action.feed;

import java.util.List;
import java.util.Properties;
import com.gnizr.core.feed.FeedSubscriptionManager;
import com.gnizr.db.dao.DaoResult;
import com.gnizr.db.dao.FeedSubscription;
import com.gnizr.web.action.AbstractLoggedInUserAction;
import com.gnizr.web.action.robot.rss.RssRobotService;

public class ListSubscriptions extends AbstractLoggedInUserAction {

    /**
	 * 
	 */
    private static final long serialVersionUID = 9044043394043967437L;

    private List<FeedSubscription> subscriptions;

    private FeedSubscriptionManager feedSubscriptionManager;

    public FeedSubscriptionManager getFeedSubscriptionManager() {
        return feedSubscriptionManager;
    }

    public void setFeedSubscriptionManager(FeedSubscriptionManager feedSubscriptionManager) {
        this.feedSubscriptionManager = feedSubscriptionManager;
    }

    public List<FeedSubscription> getSubscriptions() {
        return subscriptions;
    }

    public boolean isServiceEnabled() {
        Properties prpt = getGnizrConfiguration().getAppProperties();
        String v = prpt.getProperty(RssRobotService.SERVICE_ENABLED_KEY, "false");
        return Boolean.parseBoolean(v);
    }

    @Override
    protected String go() throws Exception {
        resolveUser();
        if (user != null) {
            try {
                DaoResult<FeedSubscription> result = feedSubscriptionManager.listSubscription(user);
                this.subscriptions = result.getResult();
            } catch (Exception e) {
                addActionError(e.toString());
                return ERROR;
            }
        }
        return SUCCESS;
    }

    @Override
    protected boolean isStrictLoggedInUserMode() {
        return false;
    }
}
