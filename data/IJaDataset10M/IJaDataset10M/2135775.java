package com.hack23.cia.service.impl.admin.agent.sweden.impl.operations.chart;

import com.hack23.cia.model.impl.sweden.Ballot;
import com.hack23.cia.service.impl.admin.agent.sweden.api.BallotAgentSupportService;

/**
 * The Class BallotChartAgentOperation.
 */
public class BallotChartAgentOperation extends AbstractParliamentChartAgentOperation<Ballot> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The ballot support service. */
    private final BallotAgentSupportService ballotSupportService;

    /**
	 * Instantiates a new ballot chart agent operation.
	 * 
	 * @param disabled the disabled
	 * @param agentSupportService the agent support service
	 * @param agentName the agent name
	 * @param agentOperativeCode the agent operative code
	 */
    public BallotChartAgentOperation(final boolean disabled, final BallotAgentSupportService agentSupportService, final String agentName, final String agentOperativeCode) {
        super(disabled, agentName, agentOperativeCode, agentSupportService);
        ballotSupportService = agentSupportService;
    }

    @Override
    public final void executeOperation() {
        for (Ballot ballot : this.ballotSupportService.getAllBallots()) {
            this.ballotSupportService.generateBallotCharts(ballot.getId());
        }
    }
}
