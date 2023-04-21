package com.jaspersoft.jasperserver.api.metadata.common.service.impl;

import java.util.List;
import java.util.ArrayList;
import com.jaspersoft.jasperserver.api.common.domain.ExecutionContext;
import com.jaspersoft.jasperserver.api.common.domain.Id;
import com.jaspersoft.jasperserver.api.metadata.common.domain.Notification;
import com.jaspersoft.jasperserver.api.metadata.common.domain.impl.NotificationImpl;
import com.jaspersoft.jasperserver.api.metadata.common.service.NotificationService;
import com.jaspersoft.jasperserver.api.metadata.view.domain.FilterCriteria;

/**
 * @author swood
 * @version $Id: NotificationServiceImpl.java 2 2006-04-30 16:19:39Z sgwood $
 */
public class NotificationServiceImpl implements NotificationService {

    public List getNotifications(ExecutionContext context, FilterCriteria criteria) {
        List list = new ArrayList();
        Notification notification = new NotificationImpl();
        list.add(notification);
        return list;
    }

    public Notification getNotification(ExecutionContext context, Id id) {
        return new NotificationImpl();
    }
}
