package org.middleheaven.ui;

import java.util.Collection;
import org.middleheaven.core.wiring.service.Service;

public interface UIService {

    /**
	 * Retrieves all environments of a given type
	 * @param type the environment type
	 * @return
	 */
    public Collection<UIEnvironment> getUIEnvironment(UIEnvironmentType type);

    /**
	 * Retrieves a previously registered user interface environment by its name;
	 * @param envName name of the environment
	 * @return
	 */
    public UIEnvironment getUIEnvironment(String envName);

    public UIEnvironment getDefaultUIEnvironment(UIEnvironmentType type);

    /**
	 * Registers an user interface environment
	 * @param env the user interface environment to register
	 * @param isTypeDeault true is this environment is the default for its type. false otherwise. 
	 * @throws  UIException exception is thrown if a default type is already set for the type
	 */
    public void registerEnvironment(UIEnvironment env, boolean isTypeDeault) throws UIException;

    public void unRegisterEnvironment(UIEnvironment env);
}
