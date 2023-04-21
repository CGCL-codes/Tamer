package org.dengues.commons.notifications;

/**
 * Qiang.Zhang.Adolf@gmail.com class global comment. Detailled comment <br/>
 * 
 * $Id: Dengues.epf 2008-1-6 qiang.zhang $
 * 
 */
public interface IDenguesNotificationsManager {

    /**
     * Qiang.Zhang.Adolf@gmail.com Comment method "showPopup".
     */
    void showPopup();

    /**
     * Qiang.Zhang.Adolf@gmail.com Comment method "addNotificationProvider".
     * 
     * @param notificationProvider
     */
    void addNotificationProvider(INotificationProvider notificationProvider);

    void removeNotificationProvider(INotificationProvider notificationProvider);
}
