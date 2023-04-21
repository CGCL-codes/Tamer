package com.twilio.sdk.resource.list;

import java.util.Map;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.resource.ListResource;
import com.twilio.sdk.resource.instance.Notification;

/**
 * The Class NotificationList.
 * 
 * For more information see {@see <a href="http://www.twilio.com/docs/api/rest/notification">http://www.twilio.com/docs/api/rest/notification}
 */
public class NotificationList extends ListResource<Notification> {

    /**
	 * Instantiates a new notification list.
	 *
	 * @param client the client
	 */
    public NotificationList(TwilioRestClient client) {
        super(client);
    }

    /**
	 * Instantiates a new notification list.
	 *
	 * @param client the client
	 * @param filters the filters
	 */
    public NotificationList(TwilioRestClient client, Map<String, String> filters) {
        super(client, filters);
    }

    @Override
    protected String getResourceLocation() {
        return "/" + TwilioRestClient.DEFAULT_VERSION + "/Accounts/" + this.getRequestAccountSid() + "/Notifications.json";
    }

    @Override
    protected Notification makeNew(TwilioRestClient client, Map<String, Object> params) {
        return new Notification(client, params);
    }

    @Override
    protected String getListKey() {
        return "notifications";
    }
}
