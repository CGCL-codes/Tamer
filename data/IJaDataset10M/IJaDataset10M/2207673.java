package org.apache.tomcat.util.modeler;

import java.io.Serializable;
import javax.management.MBeanNotificationInfo;

/**
 * <p>Internal configuration information for a <code>Notification</code>
 * descriptor.</p>
 *
 * @author Craig R. McClanahan
 * @version $Revision: 467222 $ $Date: 2006-10-24 05:17:11 +0200 (Tue, 24 Oct 2006) $
 */
public class NotificationInfo extends FeatureInfo implements Serializable {

    static final long serialVersionUID = -6319885418912650856L;

    /**
     * The <code>ModelMBeanNotificationInfo</code> object that corresponds
     * to this <code>NotificationInfo</code> instance.
     */
    transient MBeanNotificationInfo info = null;

    protected String notifTypes[] = new String[0];

    /**
     * Override the <code>description</code> property setter.
     *
     * @param description The new description
     */
    public void setDescription(String description) {
        super.setDescription(description);
        this.info = null;
    }

    /**
     * Override the <code>name</code> property setter.
     *
     * @param name The new name
     */
    public void setName(String name) {
        super.setName(name);
        this.info = null;
    }

    /**
     * The set of notification types for this MBean.
     */
    public String[] getNotifTypes() {
        return (this.notifTypes);
    }

    /**
     * Add a new notification type to the set managed by an MBean.
     *
     * @param notifType The new notification type
     */
    public void addNotifType(String notifType) {
        synchronized (notifTypes) {
            String results[] = new String[notifTypes.length + 1];
            System.arraycopy(notifTypes, 0, results, 0, notifTypes.length);
            results[notifTypes.length] = notifType;
            notifTypes = results;
            this.info = null;
        }
    }

    /**
     * Create and return a <code>ModelMBeanNotificationInfo</code> object that
     * corresponds to the attribute described by this instance.
     */
    public MBeanNotificationInfo createNotificationInfo() {
        if (info != null) return (info);
        info = new MBeanNotificationInfo(getNotifTypes(), getName(), getDescription());
        return (info);
    }

    /**
     * Return a string representation of this notification descriptor.
     */
    public String toString() {
        StringBuffer sb = new StringBuffer("NotificationInfo[");
        sb.append("name=");
        sb.append(name);
        sb.append(", description=");
        sb.append(description);
        sb.append(", notifTypes=");
        sb.append(notifTypes.length);
        sb.append("]");
        return (sb.toString());
    }
}
