package org.yawlfoundation.yawl.engine.announcement;

import org.yawlfoundation.yawl.elements.YAWLServiceReference;
import org.yawlfoundation.yawl.engine.YWorkItem;

/**
 * @author Mike Fowler
 *         Date: May 14, 2008
 */
public class NewWorkItemAnnouncement implements Announcement {

    private YAWLServiceReference yawlService;

    private YWorkItem item;

    private AnnouncementContext context;

    public NewWorkItemAnnouncement(YAWLServiceReference yawlService, YWorkItem item, AnnouncementContext context) {
        this.yawlService = yawlService;
        this.item = item;
        this.context = context;
    }

    public YAWLServiceReference getYawlService() {
        return yawlService;
    }

    public YWorkItem getItem() {
        return item;
    }

    public AnnouncementContext getContext() {
        return context;
    }
}
