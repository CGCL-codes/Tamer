package org.mobicents.ssf.flow.definition.registry;

import org.mobicents.ssf.flow.config.spring.SipFlowDefinitionResource;
import org.mobicents.ssf.flow.definition.FlowDefinition;
import org.mobicents.ssf.flow.engine.builder.template.FlowTemplate;

public interface FlowDefinitionHolder {

    FlowTemplate getFlowTemplate();

    FlowDefinition getFlowDefinition();

    String getFlowDefinitionId();

    SipFlowDefinitionResource getResource();

    void refresh();
}
