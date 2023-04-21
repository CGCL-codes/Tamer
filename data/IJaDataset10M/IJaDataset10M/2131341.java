package com.u2d.umlc.deployment;

/**
 *
 * @author ryan
 */
public class Application extends GraphDeploymentState {

    /** Creates a new instance of Application */
    public Application() {
        transitions = new String[] { "Generate JMatter Application" };
    }

    public GraphDeploymentState transition(String transition, DeploymentInformation info) {
        return this;
    }

    public String[] getTransitions() {
        return transitions;
    }
}
