package com.hack23.cia.service.impl.admin.agent.sweden.impl.operations.chart;

import com.hack23.cia.model.impl.sweden.ParliamentMember;
import com.hack23.cia.service.impl.admin.agent.sweden.api.CommitteeAgentSupportService;

/**
 * The Class CommitteeChartAgentOperation.
 */
public class CommitteeChartAgentOperation extends AbstractParliamentChartAgentOperation<ParliamentMember> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The committee agent support service. */
    private final CommitteeAgentSupportService committeeAgentSupportService;

    /**
	 * Instantiates a new committee chart agent operation.
	 * 
	 * @param disabled the disabled
	 * @param agentSupportService the agent support service
	 * @param agentName the agent name
	 * @param agentOperativeCode the agent operative code
	 */
    public CommitteeChartAgentOperation(final boolean disabled, final CommitteeAgentSupportService agentSupportService, final String agentName, final String agentOperativeCode) {
        super(disabled, agentName, agentOperativeCode, agentSupportService);
        committeeAgentSupportService = agentSupportService;
    }

    @Override
    public final void executeOperation() {
        this.committeeAgentSupportService.generateCommitteesCharts();
    }
}
