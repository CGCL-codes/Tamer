package org.springframework.webflow.engine.builder;

public class SimpleFlowBuilder extends AbstractFlowBuilder {

    public void buildStates() {
        addEndState("end");
    }
}
