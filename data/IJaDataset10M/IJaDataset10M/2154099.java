package org.spice.servlet.inject;

import java.util.List;
import org.spice.servlet.core.components.ComponentFactory;
import org.spice.servlet.core.components.Resource;

public class InstanceWrapper {

    public final InstanceFactory registry = new InstanceFactory() {

        @Override
        public List<ComponentFactory> setInstance(Resource resource) throws Exception {
            List<ComponentFactory> factories = resource.getResources();
            for (ComponentFactory factory : factories) {
                if (!factory.isInterface()) {
                    Object instance = factory.getComponentClass().newInstance();
                    factory.setInstance(instance);
                }
            }
            return factories;
        }
    };

    public void getInjection(final Resource resource) throws Exception {
        final List<ComponentFactory> components = registry.setInstance(resource);
        FieldInject inject = new InjectionStrategy(components);
        inject.inject(resource);
    }
}
