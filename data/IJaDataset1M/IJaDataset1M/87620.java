package org.robcash.commons.plugin;

import java.util.Collection;

/**
 * Plug-in managers manage the lifecycle of a plug-in and provide access to
 * them upon request. Plug-in managers can automatically discover plug-ins
 * when they themselves are instantiated. The auto discovery process consists
 * of locating a plug-in, validating it, instantiating it if it is not already
 * instantiated, and registering it.
 * @author Rob Cash
 * @param <P> Type of plug-ins that is managed.
 */
public interface PlugInManager<P> {

    /**
	 * Discover, validate, instantiate if necessary, and register plug-ins.
	 * This will cause the {@link PlugIn#onRegister()} method to be called
	 * for plug-ins that are registered.
	 * @throws PlugInManagerException Thrown if discovery, validation,
	 * instantiation, or registration of plug-ins fails.
	 */
    void discoverPlugIns() throws PlugInManagerException;

    /**
	 * Register a plug-in that has been instantiated outside of the
	 * plug-in manager. This will cause the {@link PlugIn#onRegister()}
	 * method to be called for plug-ins that are registered. Plug-ins
	 * that are already registered with this plug-in manager will not
	 * be registered again unless they are first unregistered.
	 * @param plugIn Plug-in to register.
	 * @return Returns {@code true} if the plug-in was registered, or
	 * {@code false} if it was not.
	 * @throws InvalidPlugInException Thrown if the plug-in was not valid.
	 * @throws PlugInException Thrown if there was an error from the
	 * plug-in when it was registered.
	 */
    boolean registerPlugIn(P plugIn) throws InvalidPlugInException, PlugInException;

    /**
	 * Unregister a plug-in that was previously registered. This can
	 * include plug-ins that were registered automatically as a part
	 * of the discovery process.
	 * @param plugIn Plug-in to unregister.
	 * @return {@code true} is returned if {@code plugIn} was successfully
	 * unregistered; {@code false} is returned if {@code plugIn} is not
	 * registered at alll
	 * @throws PlugInException Thrown if there was an error from the
	 * plug-in when it was unregistered.
	 */
    boolean unregisterPlugIn(P plugIn) throws PlugInException;

    /**
	 * Returns the plug-ins that have been registered, whether through
	 * automatic discovery or manual registration.
	 * @return Registered plug-ins. This will never be {@code null}.
	 */
    Collection<P> getPlugIns();
}
