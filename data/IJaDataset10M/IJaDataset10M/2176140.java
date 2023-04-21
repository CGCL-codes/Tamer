package org.imogene.notifier.server.services;

import org.imogene.notifier.server.common.NotificationInstance;

/**
 * Interface that describes a notification service
 * @author Medes-IMPS 
 */
public interface NotificationService {

    /**
	 * send a notification instance
	 * @param notification the notification to send
	 */
    public void sendNotification(NotificationInstance notification);
}
