package org.springframework.webflow.engine.impl;

import org.springframework.core.io.ClassPathResource;
import org.springframework.webflow.definition.FlowDefinition;
import org.springframework.webflow.definition.registry.FlowDefinitionConstructionException;
import org.springframework.webflow.definition.registry.FlowDefinitionLocator;
import org.springframework.webflow.definition.registry.FlowDefinitionResource;
import org.springframework.webflow.definition.registry.NoSuchFlowDefinitionException;
import org.springframework.webflow.engine.impl.FlowExecutionImplStateRestorer;
import org.springframework.webflow.execution.FlowExecution;
import org.springframework.webflow.execution.repository.continuation.SerializedFlowExecutionContinuation;
import org.springframework.webflow.test.execution.AbstractXmlFlowExecutionTests;

/**
 * Tests dealing with restoration of nested subflows.
 * 
 * @author Erwin Vervaet
 */
public class NestedSubflowRestorationTests extends AbstractXmlFlowExecutionTests implements FlowDefinitionLocator {

    protected FlowDefinitionResource getFlowDefinitionResource() {
        return new FlowDefinitionResource(new ClassPathResource("nestedSubflow.xml", NestedSubflowRestorationTests.class));
    }

    public FlowDefinition getFlowDefinition(String id) throws NoSuchFlowDefinitionException, FlowDefinitionConstructionException {
        return getFlowDefinition();
    }

    public void testNestedFlows() {
        startFlow();
        assertFlowExecutionActive();
        assertActiveFlowEquals("nestedSubflow");
        assertCurrentStateEquals("view1");
        signalEvent("start");
        assertFlowExecutionActive();
        assertActiveFlowEquals("subflowDef3");
        assertCurrentStateEquals("view4");
        FlowExecution flowExecution = getFlowExecution();
        flowExecution = new SerializedFlowExecutionContinuation(flowExecution, false).unmarshal();
        flowExecution = new FlowExecutionImplStateRestorer(this).restoreState(flowExecution, null);
        updateFlowExecution(flowExecution);
        signalEvent("continue");
        assertFlowExecutionEnded();
    }
}
