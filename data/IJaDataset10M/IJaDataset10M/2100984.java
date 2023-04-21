package org.springframework.webflow.config;

import org.springframework.webflow.definition.registry.FlowDefinitionRegistry;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistryImpl;
import org.springframework.webflow.engine.model.registry.FlowModelRegistry;
import org.springframework.webflow.engine.model.registry.FlowModelRegistryImpl;

/**
 * Flow registry implementation created by FlowRegistryFactoryBean.
 * @author Keith Donald
 */
class DefaultFlowRegistry extends FlowDefinitionRegistryImpl {

    private FlowModelRegistry flowModelRegistry = new FlowModelRegistryImpl();

    public FlowModelRegistry getFlowModelRegistry() {
        return flowModelRegistry;
    }

    public void setParent(FlowDefinitionRegistry parent) {
        super.setParent(parent);
        if (parent instanceof DefaultFlowRegistry) {
            DefaultFlowRegistry parentFlowRegistry = (DefaultFlowRegistry) parent;
            flowModelRegistry.setParent(parentFlowRegistry.getFlowModelRegistry());
        }
    }
}
