package org.riverock.portlet.shop.resource;

import org.apache.log4j.Logger;
import org.riverock.webmill.container.resource.XmlResourceBundle;

/**
 * @author Serge Maslyukov
 *         Date: 01.12.2005
 *         Time: 15:12:25
 */
public class Commerce_en extends XmlResourceBundle {

    private static final Logger log = Logger.getLogger(Commerce_en.class);

    public void logError(String msg, Throwable th) {
        log.error(msg, th);
    }
}
