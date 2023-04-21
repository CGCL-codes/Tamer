package org.kompiro.percs.service.gmail;

import java.util.Properties;
import org.kompiro.readviewer.service.INotificationService;
import org.kompiro.readviewer.service.StatusHandler;
import org.kompiro.readviewer.ui.INotificationServiceFactory;

public class GmailNotificationServiceFactory implements INotificationServiceFactory {

    public GmailNotificationServiceFactory() {
    }

    public INotificationService createService(Properties properties) {
        GmailService service = null;
        try {
            service = new GmailService(properties);
        } catch (Exception e) {
            StatusHandler.fail(e, "can't create GmailService.", true);
        }
        return service;
    }
}
