package org.systemsbiology.apps.gui.server.provider;

import org.systemsbiology.apps.gui.server.provider.access.IAccessInfoProvider;
import org.systemsbiology.apps.gui.server.provider.access.AccessInfoProvider;
import org.systemsbiology.apps.gui.server.provider.lob.ILobProvider;
import org.systemsbiology.apps.gui.server.provider.lob.LobProvider;
import org.systemsbiology.apps.gui.server.provider.location.ILocationInfoProvider;
import org.systemsbiology.apps.gui.server.provider.location.LocationInfoProvider;
import org.systemsbiology.apps.gui.server.provider.msinstrument.IMSInstrumentProvider;
import org.systemsbiology.apps.gui.server.provider.msinstrument.MSInstrumentProvider;
import org.systemsbiology.apps.gui.server.provider.program.IProgramInfoProvider;
import org.systemsbiology.apps.gui.server.provider.program.ProgramInfoProvider;
import org.systemsbiology.apps.gui.server.provider.project.IProjectInfoProvider;
import org.systemsbiology.apps.gui.server.provider.project.ProjectInfoProvider;
import org.systemsbiology.apps.gui.server.provider.properties.IPropertiesProvider;
import org.systemsbiology.apps.gui.server.provider.properties.PropertiesProvider;
import org.systemsbiology.apps.gui.server.provider.user.IUserInfoProvider;
import org.systemsbiology.apps.gui.server.provider.user.UserInfoProvider;

/**
 * Creates and retrieves instances of the different providers.
 * If there are more than one options for a particular provider, this class will
 * decide which one to initialize. 
 * 
 * @author Mark Christiansen
 * @author Chris Kwok
 *
 */
public class ProviderFactory {

    private static final ProviderFactory instance = new ProviderFactory();

    private ProviderFactory() {
    }

    /**
	 * Get the singleton instance of the provider factory. 
	 * @return instance of provider factory
	 */
    public static ProviderFactory instance() {
        return instance;
    }

    /**
	 * @return instance of access provider
	 */
    public IAccessInfoProvider getAccessInfoProvider() {
        return AccessInfoProvider.instance();
    }

    /**
	 * @return instance of user provider
	 */
    public IUserInfoProvider getUserInfoProvider() {
        return UserInfoProvider.instance();
    }

    /**
	 * @return instance of location provider
	 */
    public ILocationInfoProvider getLocationInfoProvider() {
        return LocationInfoProvider.instance();
    }

    /**
	 * @return instance of program information provider
	 */
    public IProgramInfoProvider getProgramInfoProvider() {
        return ProgramInfoProvider.instance();
    }

    /**
	 * @return instance of project information provider
	 */
    public IProjectInfoProvider getProjectInfoProvider() {
        return ProjectInfoProvider.instance();
    }

    /**
	 * @return instance of mass spec instrument provider
	 */
    public IMSInstrumentProvider getMSInstrumentProvider() {
        return MSInstrumentProvider.instance();
    }

    /**
	 * @return instance of properties providers
	 */
    public IPropertiesProvider getPropertiesProvider() {
        return PropertiesProvider.instance();
    }

    /**
	 * @return instance of lob (large object) provider
	 */
    public ILobProvider getLobProvider() {
        return LobProvider.instance();
    }
}
