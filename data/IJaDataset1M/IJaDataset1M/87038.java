package com.hack23.cia.service.impl.control.agent.sweden.impl.operations.chart;

import java.util.List;
import com.hack23.cia.model.api.sweden.content.BallotData;
import com.hack23.cia.model.api.sweden.events.ParliamentYearData;
import com.hack23.cia.service.impl.control.agent.sweden.api.BallotAgentSupportService;

/**
 * The Class BallotChartAgentOperation.
 */
public class BallotChartAgentOperation extends AbstractParliamentChartAgentOperation<BallotData> {

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
        List<ParliamentYearData> years;
        years = ballotSupportService.getParliamentYearsData();
        for (final ParliamentYearData parliamentYearData : years) {
            if (parliamentYearData.getDataUpdating()) {
                for (final BallotData ballot : ballotSupportService.getAllBallots(parliamentYearData)) {
                    ballotSupportService.generateBallotCharts(ballot.getId());
                }
            }
        }
    }
}
