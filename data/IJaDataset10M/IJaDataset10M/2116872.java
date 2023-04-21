package jhomenet.server.hw.driver;

import jhomenet.commons.hw.driver.HardwareContainer;

/**
 * Used to wrap an underlying container implementation into a
 * <code>IContainer</code> interface object.
 *
 * @author Dave Irwin (jhomenet at gmail dot com)
 */
public interface ContainerWrapper<C> extends HardwareContainer {

    /**
     * Get a reference to the actual container implementation.
     * 
     * @return A reference to the actual container implementation
     */
    public C getContainerImplementation();

    /**
     * 
     * @return
     */
    public Hub getHub();

    /**
     * 
     * @return
     */
    public HubChannel getHubChannel();

    /**
     * 
     * @return
     */
    public HubPort getHubPort();
}
