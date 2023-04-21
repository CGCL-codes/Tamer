package jhomenet.commons;

import jhomenet.commons.auth.AuthFacade;
import jhomenet.commons.configuration.ServerSystemConfiguration;
import jhomenet.commons.hw.mngt.HardwareManager;
import jhomenet.commons.responsive.ResponsiveManager;
import jhomenet.commons.work.WorkQueue;
import jhomenet.commons.weather.WeatherGatewayManager;

/**
 * Server context interface.
 * 
 * @author Dave Irwin (jhomenet at gmail dot com)
 */
public interface ServerContext {

    /**
	 * 
	 * @return
	 */
    public WeatherGatewayManager getWeatherChannelManager();

    /**
	 * 
	 * @return
	 */
    public AuthFacade getAuthFacade();

    /**
	 * 
	 * @return
	 */
    public ServerSystemConfiguration getServerConfiguration();

    /**
	 * 
	 * @return
	 */
    public Library getServerLibrary();

    /**
	 * 
	 * @return
	 */
    public Server getServer();

    /**
	 * @return
	 */
    public HardwareManager getHardwareManager();

    /**
	 * 
	 * @return
	 */
    public ResponsiveManager getResponsiveManager();

    /**
	 * @return
	 */
    public WorkQueue getWorkQueue();

    /**
	 * 
	 * @return
	 */
    public IServiceManager getServiceManager();
}
