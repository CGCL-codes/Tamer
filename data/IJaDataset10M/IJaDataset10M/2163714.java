package org.middleheaven.core.bootstrap.activation;

import org.middleheaven.core.wiring.BeanDependencyModel;

public interface ActivatorDependencyResolver {

    public void resolveDependency(Class<? extends ServiceActivator> activatorType, BeanDependencyModel model);
}
