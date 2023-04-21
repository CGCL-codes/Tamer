package org.kompiro.readviewer.ui;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.kompiro.readviewer.service.INotificationMessage;
import org.kompiro.readviewer.service.INotificationMessage.Mock;

public class ContentsFilterTest {

    private List<INotificationMessage> notifications;

    private Mock data10;

    private Mock data20;

    @Before
    public void setUpNotifications() {
        notifications = new ArrayList<INotificationMessage>();
        data10 = new INotificationMessage.Mock() {

            @Override
            public Date getPublishDate() {
                Date date = new Date();
                date.setTime(10);
                return date;
            }
        };
        notifications.add(data10);
        data20 = new INotificationMessage.Mock() {

            @Override
            public Date getPublishDate() {
                Date date = new Date();
                date.setTime(20);
                return date;
            }
        };
        notifications.add(data20);
    }

    @Test
    public void getContentsFirst() {
        Date epocAfter30Second = new Date();
        epocAfter30Second.setTime(30L);
        ContentsFilter filter = new ContentsFilter(epocAfter30Second);
        List<INotificationMessage> contents = filter.filteredByDatetime(notifications, 0L);
        assertEquals(2, contents.size());
        assertEquals(data10, contents.get(0));
        assertEquals(data20, contents.get(1));
    }

    @Test
    public void getContentsBeforeLastUpdateAt13Second() throws Exception {
        Date epocAfter30Second = new Date();
        epocAfter30Second.setTime(30L);
        ContentsFilter filter = new ContentsFilter(epocAfter30Second);
        List<INotificationMessage> contents = filter.filteredByDatetime(notifications, 13L);
        assertEquals(1, contents.size());
        assertEquals(data20, contents.get(0));
    }

    @Test
    public void getContentsDontSetPublishTime() throws Exception {
        Date epoc = new Date();
        epoc.setTime(0L);
        ContentsFilter filter = new ContentsFilter(epoc);
        List<INotificationMessage> currentNortificationList = new ArrayList<INotificationMessage>();
        List<INotificationMessage> contents = filter.filteredByDatetime(currentNortificationList, 0L);
        assertEquals(0, contents.size());
        currentNortificationList.add(new Mock());
        contents = filter.filteredByDatetime(currentNortificationList, 0L);
        assertEquals(0, contents.size());
    }
}
