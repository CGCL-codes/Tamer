package org.havi.ui;

/**
 * An {@link org.havi.ui.HEmulatedGraphicsDeviceHEmulatedGraphicsDevice}is a
 * &quot;virtual&quot; graphics device that has the capability to be configured
 * to perform one (of many) possible emulations. For example, in the DVB context
 * a 4:3 television might have an {@link org.havi.ui.HEmulatedGraphicsDevice
 * HEmulatedGraphicsDevice} that had an {@link
 * org.havi.ui.HEmulatedGraphicsConfiguration HEmulatedGraphicsConfiguration}
 * that emulated a virtual 14:9 display. The 14:9
 * {@link org.havi.ui.HEmulatedGraphicsConfiguration
 * HEmulatedGraphicsConfiguration} would be used for rendering into from AWT,
 * whilst being displayed on the &quot;true&quot; 4:3 physical display. The
 * relationship between the emulation and implementation is encapsulated within
 * the {@linkorg.havi.ui.HEmulatedGraphicsConfiguration
 * HEmulatedGraphicsConfiguration}.
 * <p>
 * An {@link org.havi.ui.HEmulatedGraphicsDeviceHEmulatedGraphicsDevice}
 * transforms both AWT pixel-oriented drawing operations and AWT user-input
 * event coordinates, this is performed outside of the Java application
 * (typically in hardware).
 * <p>
 * An {@link org.havi.ui.HEmulatedGraphicsDeviceHEmulatedGraphicsDevice}may (of
 * necessity) modify coordinates for Components and/or events to the nearest
 * physical / virtual pixel --- authors should not depend on single pixel
 * accuracy.
 * <p>
 * There is no difference to a Java application between an {@link
 * org.havi.ui.HGraphicsDevice HGraphicsDevice} and an {@link
 * org.havi.ui.HEmulatedGraphicsDevice HEmulatedGraphicsDevice}, except for the
 * implication of possible rounding errors in integer pixel positions, e.g.
 * Component placement and/or resolution of events.
 * <p>
 * Java2D mechanisms should behave as per their normal semantics, with respect
 * to display on-screen.
 */
public abstract class HEmulatedGraphicsDevice extends HGraphicsDevice {

    /**
	 * It is not intended that applications should directly construct
	 * {@link org.havi.ui.HEmulatedGraphicsDeviceHEmulatedGraphicsDevice}
	 * objects.
	 * <p>
	 * Creates an {@link org.havi.ui.HEmulatedGraphicsDevice
	 * HEmulatedGraphicsDevice} object. See the class description for details of
	 * constructor parameters and default values.
	 */
    protected HEmulatedGraphicsDevice() {
    }

    /**
	 * Set the graphics configuration for the device.
	 * @param hegc the {@link HEmulatedGraphicsConfiguration
	 *            HEmulatedGraphicsConfiguration } to which this device should
	 *            be set.
	 * @return A boolean indicating whether the configuration could be applied
	 *         successfully. If the configuration could not be applied
	 *         successfully, the configuration after this method may not match
	 *         the configuration of the device prior to this method being called
	 *         --- applications should take steps to determine whether a partial
	 *         change of settings has been made.
	 * @exception SecurityException if the application does not have sufficient
	 *                rights to set the configuration for this device.
	 * @exception HPermissionDeniedException ({@link
	 *                org.havi.ui.HPermissionDeniedException
	 *                HPermissionDeniedException}) if the application does not
	 *                currently have the right to set the configuration for this
	 *                device.
	 * @exception HConfigurationException ({@link
	 *                org.havi.ui.HConfigurationException
	 *                HConfigurationException}) if the specified configuration
	 *                is not valid for this device.
	 */
    public boolean setGraphicsConfiguration(HEmulatedGraphicsConfiguration hegc) throws SecurityException, org.havi.ui.HPermissionDeniedException, org.havi.ui.HConfigurationException {
        return (false);
    }
}
