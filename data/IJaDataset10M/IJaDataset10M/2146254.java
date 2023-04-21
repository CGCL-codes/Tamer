package org.digitalcure.refactordw.core;

import org.digitalcure.refactordw.util.UnsupportedOSException;

/**
 * Checks if this application is compatible to the the host 
 * operating system that tries to run this application.
 *  
 * @author novotny
 * @version 1.0
 * @since 1.2, 02.06.2009
 * @lastChange $Date$ by $Author$
 */
public final class OSChecker {

    /**
     * Private constructor.
     */
    private OSChecker() {
        super();
    }

    /**
     * The name of the system property OS Name.
     */
    protected static final String PROP_OS_NAME_KEY = "os.name";

    /**
     * The compatible OS. This phrase must be included in OS name in order to indicate an OS as compatible.
     */
    protected static final String COMPABILE_HOST_OS = "Linux";

    /**
     * Checks if the application is compatible to the host operating system.
     * @throws UnsupportedOSException if the application is incompatible
     */
    public static void check() throws UnsupportedOSException {
        final String hostOS = System.getProperty("os.name");
        if (hostOS.contains(COMPABILE_HOST_OS)) {
            return;
        }
        throw new UnsupportedOSException(hostOS);
    }
}
