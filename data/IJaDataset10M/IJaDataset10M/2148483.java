package com.francetelecom.rd.maps.semeuse.t31d2_slachecking.jmxnotification;

import javax.management.MBeanNotificationInfo;
import javax.management.NotificationBroadcasterSupport;

/**
 * ---------------------------------------------------------
 * 
 * @Software_Name : SLO Monitoring
 * @Version : 1.0.0
 * 
 * @Copyright � 2009 France Telecom
 * @License: This software is distributed under the GNU Lesser General Public
 *           License (Version 2.1) as published by the Free Software Foundation,
 *           the text of which is available at
 *           http://www.gnu.org/licenses/lgpl-2.1.html or see the "license.txt"
 *           file for more details.
 * 
 * @--------------------------------------------------------
 * 
 * @Created : 02/2009
 * @Author(s) : Antonin CHAZALET
 * @Contact: antonin.chazalet@gmail.com
 * 
 * @Description :
 * 
 * @--------------------------------------------------------
 */
public class SLACheckingJMXNotificationSender extends NotificationBroadcasterSupport implements SLACheckingJMXNotificationSenderMBean {

    public String returnTestString() {
        return "Work.";
    }

    public Object returnTestObject() {
        Object result = new Object();
        return result;
    }

    public void sendSLACheckingNotification(NotificationSLACheckingOk sLACN_) {
        sendNotification(sLACN_);
    }

    public void sendSLACheckingViolationNotification(NotificationSLACheckingViolation sLACVN_) {
        sendNotification(sLACVN_);
    }

    public void sendSLACheckingFailedNotification(NotificationSLACheckingFailed sLACFN_) {
        sendNotification(sLACFN_);
    }

    /**
	 * Returns an array indicating, for each notification this MBean may send,
	 * the name of the Java class of the notification and the notification
	 * type.</p>
	 * 
	 * @return the array of possible notifications.
	 */
    public MBeanNotificationInfo[] getNotificationInfo() {
        String[] type_1 = new String[] { NotificationSLACheckingOk.class.getName() };
        String name_1 = NotificationSLACheckingOk.class.getName();
        String description_1 = "Short description to explain the " + NotificationSLACheckingOk.class.getName() + " notification which can be emitted.";
        MBeanNotificationInfo info_1 = new MBeanNotificationInfo(type_1, name_1, description_1);
        String[] type_2 = new String[] { NotificationSLACheckingViolation.class.getName() };
        String name_2 = NotificationSLACheckingViolation.class.getName();
        String description_2 = "Short description to explain the " + NotificationSLACheckingViolation.class.getName() + " notification which can be emitted.";
        MBeanNotificationInfo info_2 = new MBeanNotificationInfo(type_2, name_2, description_2);
        String[] type_3 = new String[] { NotificationSLACheckingFailed.class.getName() };
        String name_3 = NotificationSLACheckingFailed.class.getName();
        String description_3 = "Short description to explain the " + NotificationSLACheckingFailed.class.getName() + " notification which can be emitted.";
        MBeanNotificationInfo info_3 = new MBeanNotificationInfo(type_3, name_3, description_3);
        return new MBeanNotificationInfo[] { info_1, info_2, info_3 };
    }
}
