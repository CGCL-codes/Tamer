package org.riverock.portlet.resource;

import org.apache.log4j.Logger;
import org.riverock.webmill.container.resource.CustomXmlResourceBundle;

/**
 * @author Serge Maslyukov
 */
public class SiteHamradio_ru extends CustomXmlResourceBundle {

    private static final Logger log = Logger.getLogger(SiteHamradio_ru.class);

    public void logError(String msg, Throwable th) {
        log.error(msg, th);
    }
}
