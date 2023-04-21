package com.hack23.cia.web.impl.ui.viewfactory.api.admin;

import com.hack23.cia.model.api.dto.application.UserSessionDto;
import com.hack23.cia.web.api.common.ControllerAction;

/**
 * The Class AgentDeploymentModelAndView.
 */
public class AgentDeploymentModelAndView extends AbstractAdminModelAndView {

    /**
     * The Enum AgentDeployment.
     */
    public enum AgentDeployment {

        /** The Agent deployment view. */
        AgentDeploymentView
    }

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The agent deployment. */
    private final AgentDeployment agentDeployment;

    /**
     * Instantiates a new agent deployment model and view.
     * 
     * @param userSessionDTO the user session dto
     * @param controllerAction the controller action
     * @param agentDeployment the agent deployment
     */
    public AgentDeploymentModelAndView(final UserSessionDto userSessionDTO, final ControllerAction controllerAction, final AgentDeployment agentDeployment) {
        super(userSessionDTO, controllerAction);
        this.agentDeployment = agentDeployment;
    }

    /**
     * Gets the agent deployment view specification.
     * 
     * @return the agent deployment view specification
     */
    public final AgentDeployment getAgentDeploymentViewSpecification() {
        return agentDeployment;
    }

    @Override
    public final String getViewSpecificationDescription() {
        return agentDeployment.toString();
    }
}
