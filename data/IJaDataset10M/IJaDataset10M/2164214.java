package org.dvb.media;

import javax.tv.locator.InvalidLocatorException;
import javax.tv.locator.Locator;
import javax.tv.service.selection.InsufficientResourcesException;
import javax.tv.service.selection.InvalidServiceComponentException;

/**
 * DVBMediaSelectControl extends <code>MediaSelectControl</code> allowing the
 * selection of different kinds of content in a running <code>Player</code>.
 * The extension is to allow the selection in a single operation of all the
 * media service components in a service without needing knowledge about which
 * media service components are present in that service.
 * @see javax.tv.media.MediaSelectControl
 * @since MHP 1.0.2
 */
public interface DVBMediaSelectControl extends javax.tv.media.MediaSelectControl {

    /**
	 * Selects for presentation the media service components from a service. If
	 * some content is currently playing, it is replaced in its entirety by the
	 * media service components from the specified service. This is an
	 * asynchronous operation that is completed upon receipt of a
	 * MediaSelectEvent. Note that for most selections that imply a different
	 * time base or otherwise change synchronization relationships, a
	 * RestartingEvent will be posted by the Player. The rules for deciding
	 * which media service components shall be presented are defined in the main
	 * body of this specification.
	 * @param l the locator for a service
	 * @throws InvalidLocatorException If the locator provided does not
	 *             reference a service.
	 * @throws InvalidServiceComponentException If the locator provided does not
	 *             reference a service which contains at least one media service
	 *             component
	 * @throws InsufficientResourcesException If the operation cannot be
	 *             completed due to a lack of system resources.
	 */
    public void selectServiceMediaComponents(Locator l) throws InvalidLocatorException, InvalidServiceComponentException, InsufficientResourcesException;
}
