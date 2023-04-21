package org.kompiro.readviewer.ui;

import java.util.Properties;
import org.kompiro.readviewer.service.INotificationService;

public interface INotificationServiceFactory {

    public INotificationService createService(Properties properties);
}
