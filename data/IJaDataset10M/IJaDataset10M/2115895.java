package org.dvb.application.storage;

import org.dvb.application.AppID;
import org.dvb.application.AppProxy;

/**
 * <p>
 * An interface that shall be implemented by the objects that implement
 * javax.service.Service interface and where the MHP terminal supports storing
 * applications. MHP terminals which do not support storing applications shall
 * not return objects implementing this interface.
 * <p>
 * When this object represents a service that is retrieved from a network, e.g.
 * a broadcast service, storing applications using the methods in this interface
 * will install them in the receiver as broadcast service related stored
 * applications.
 * <p>
 * When this object represents a stored application service, storing
 * applications using the methods in this interface will add the application
 * into this service.
 * @since MHP1.1
 */
public interface ApplicationStorage extends javax.tv.service.Service {

    /**
	 * <p>
	 * Requests the MHP terminal to initiate the installation of an application
	 * into the MHP terminal.
	 * </p>
	 * <p>
	 * Applications should be prepared for the platform consulting the end user
	 * of the MHP terminal for the permission to install the application
	 * </p>
	 * <p>
	 * Note: This method is synchronous and will block until the installation is
	 * either completed or fails.
	 * @param app an AppProxy representing the application to be installed
	 * @param autoStart true, if the application becomes an autostart
	 *            application in the stored application service; false, if the
	 *            application becomes a normal present (non-autostart)
	 *            application in the stored application service. This parameter
	 *            is not used if storing a broadcast service related
	 *            application.
	 * @param args parameters to be available to the application when started.
	 *            Passing in either null or an array of size zero indicates no
	 *            parameters are to be available. These parameters shall be
	 *            available to applications when running as part of the stored
	 *            application service using the Xlet property
	 *            "dvb.installer.parameters".
	 * @throws InvalidApplicationException thrown if the application is not
	 *             valid for installation into this service. For a broadcast
	 *             service, only applications signalled in that service are
	 *             valid. For a stand-alone stored service, all applications
	 *             identified as able to run stand-alone in their application
	 *             description file are defined to be valid for the purposes of
	 *             this exception.
	 * @throws UserRejectedInstallException thrown if the end user rejects the
	 *             installation
	 * @throws NotEnoughResourcesException thrown if the MHP terminal does not
	 *             have enough resources, e.g. storage space, available for the
	 *             application
	 * @throws InvalidDescriptionFileException thrown if the application
	 *             description file is not valid
	 * @throws ApplicationDownloadException thrown if the downloading of the
	 *             application files was not successful
	 * @throws java.lang.SecurityException Thrown if the application calling
	 *             this method does not have an ApplicationStoragePermission
	 *             with action "store" or "*" for the organisation_id of the
	 *             application to be stored. Also thrown for a stand-alone
	 *             stored service if the application calling this method does
	 *             not have the same organisation_id as the application which
	 *             originally created the stand-alone stored service.
	 * @since MHP1.1
	 */
    public void initiateApplicationInstall(AppProxy app, boolean autoStart, String args[]) throws InvalidApplicationException, UserRejectedInstallException, NotEnoughResourcesException, InvalidDescriptionFileException, ApplicationDownloadException;

    /**
	 * Lists the AppIDs of the applications that are stored within this service.
	 * @return an array of AppID object representing the stored application
	 * @throws SecurityException Thrown for broadcast services if the service
	 *             represented by this object is not the currently selected
	 *             service of the the service context in which the calling
	 *             application is running. Thrown for a stand-alone stored
	 *             service if the application calling this method does not have
	 *             the same organisation_id as the application which originally
	 *             created the stand-alone stored service.
	 * @since MHP1.1
	 */
    public AppID[] getStoredAppIDs();

    /**
	 * Return the version number of the stored application whose AppID is given
	 * as a parameter.
	 * @param appId the AppID of the application whose version is queried
	 * @return the version number of the stored application, returns -1 if the
	 *         application given as a parameter is not stored
	 * @throws SecurityException Thrown for broadcast services if the service
	 *             represented by this object is not the currently selected
	 *             service of the the service context in which the calling
	 *             application is running. Thrown for a stand-alone stored
	 *             service if the application calling this method does not have
	 *             the same organisation_id as the application which originally
	 *             created the stand-alone stored service.
	 * @since MHP1.1
	 */
    public int getVersionNumber(AppID appId);

    /**
	 * <p>
	 * Requests the MHP terminal to initiate the removal of an application
	 * stored in the MHP terminal from this service.
	 * <p>
	 * Applications should be prepared for the platform consulting the end user
	 * of the MHP terminal for the permission to remove the application
	 * </p>
	 * <p>
	 * If the application identified by the AppID passed in as a parameter is
	 * not installed in this service, the method shall fail silently.
	 * @param app AppID of the application to be removed
	 * @throws java.lang.SecurityException thrown if the application calling
	 *             this method does not have an ApplicationStoragePermission
	 *             with action "remove" or "*" for the organisation_id of the
	 *             application to be removed. Also thrown for a stand-alone
	 *             stored service if the application calling this method does
	 *             not have the same organisation_id as the application which
	 *             originally created the stand-alone stored service.
	 * @see ApplicationStoragePermission
	 * @since MHP1.1
	 */
    public void initiateApplicationRemoval(AppID app);
}
