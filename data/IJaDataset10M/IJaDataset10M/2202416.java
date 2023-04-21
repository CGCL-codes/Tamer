package org.springframework.richclient.application.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.richclient.application.ApplicationWindow;
import org.springframework.richclient.application.ApplicationWindowFactory;

/**
 * <code>ApplicationWindowFactory</code> implementation for
 * <code>DefaultApplicationWindow</code>.
 * 
 * @author Peter De Bruycker
 * 
 */
public class DefaultApplicationWindowFactory implements ApplicationWindowFactory {

    private static final Log logger = LogFactory.getLog(DefaultApplicationWindowFactory.class);

    public ApplicationWindow createApplicationWindow() {
        logger.info("Creating new DefaultApplicationWindow");
        return new DefaultApplicationWindow();
    }
}
