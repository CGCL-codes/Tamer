package au.org.tpac.portal.gwt.client.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import au.org.tpac.portal.domain.Category;
import au.org.tpac.portal.domain.Dataset;
import au.org.tpac.portal.domain.DlpUser;
import au.org.tpac.portal.domain.Notification;
import au.org.tpac.portal.gwt.client.data.UserCreationResponse;
import com.google.gwt.user.client.rpc.RemoteService;

/**
 * The Interface NotificationService.
 */
public interface NotificationService extends RemoteService {

    /**
	 * Runs a notification immediately as passed, instead of as scheduled
	 * @param notification	 
	 */
    public void runNotificationNow(Notification notification);

    /**
	 * Update a notification. An id of 0 is treated as a new notification.
	 * @param recipient
	 */
    public void update(Notification notification);

    /**
	 * List notifications
	 */
    public List<Notification> list();

    /**
	 * Remove a notification
	 * @param notification
	 */
    public void delete(Notification notification);
}
