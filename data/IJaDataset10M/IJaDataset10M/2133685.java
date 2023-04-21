package org.jdesktop.jdic.desktop.internal.impl;

import org.jdesktop.jdic.desktop.internal.ServiceManager;

/**
 * The <code>ServiceManagerStub</code> class implements the particular
 * lookup of services. The request in ServiceManager to lookup services is 
 * delegated to this object.
 * 
 * @see org.jdesktop.jdic.desktop.internal.ServiceManager
 */
public class ServiceManagerStub {

    /**
     * Suppress default constructor for noninstantiability.
     */
    private ServiceManagerStub() {
    }

    /**
     * Gets the requested service object according to the given service name.
     * 
     * @param serviceName the given service name.
     * @throws IllegalArgumentException if there is no approprate service according
     *         to the given service name, or UnsupportedOperationException if we've 
     *         got unsupported system mailer.
     * @throws UnsupportedOperationException if the current mailer is not supported
     *         for this operation.
     * @return the requested service object.
     */
    public static Object getService(String serviceName) throws IllegalArgumentException, UnsupportedOperationException {
        if (serviceName.equals(ServiceManager.LAUNCH_SERVICE)) {
            return new GnomeLaunchService();
        } else if (serviceName.equals(ServiceManager.BROWSER_SERVICE)) {
            return new GnomeBrowserService();
        } else if (serviceName.equals(ServiceManager.MAILER_SERVICE)) {
            String defMailerPath = GnomeUtility.getDefaultMailerPath();
            if (defMailerPath.indexOf(DesktopConstants.EVO_MAILER) != -1) {
                return new GnomeEvoMailer(defMailerPath.trim());
            } else if (defMailerPath.indexOf(DesktopConstants.MOZ_MAILER) != -1 || defMailerPath.indexOf(DesktopConstants.THBD_MAILER) != -1) {
                return new GnomeMozMailer(defMailerPath.trim());
            } else {
                throw new UnsupportedOperationException("Current system default mailer is not supported.");
            }
        } else {
            throw new IllegalArgumentException("The requested service is not supported.");
        }
    }
}
