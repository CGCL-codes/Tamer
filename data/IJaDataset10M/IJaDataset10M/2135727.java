package org.nexopenframework.deployment.processor;

import java.util.Set;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p>Comment here</p>
 * 
 * @author <a href="mailto:fme@nextret.net">Francesc Xavier Magdaleno</a>
 * @version 1.0
 * @since 1.0
 */
public interface DeployerUnitListener {

    void onInstalledServices(Set services);

    void onUnistalledServices(Set services);
}
