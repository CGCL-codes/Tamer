package net.sf.remilama;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeException;
import org.artofsolving.jodconverter.office.OfficeManager;

public class OfficeManagerProvider {

    private static Log logger = LogFactory.getLog(OfficeManagerProvider.class);

    private OfficeManager officeManager;

    public void init() {
        officeManager = new DefaultOfficeManagerConfiguration().setTaskQueueTimeout(30000L).buildOfficeManager();
        try {
            officeManager.start();
        } catch (OfficeException e) {
            try {
                officeManager.stop();
                officeManager.start();
            } catch (OfficeException ignore) {
                logger.error(ignore);
            }
        }
    }

    public OfficeManager get() {
        return officeManager;
    }

    public void destroy() {
        if (officeManager != null) officeManager.stop();
    }
}
