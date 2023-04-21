package com.hack23.cia.service.impl.admin.agent.sweden.impl;

import gnu.trove.THashMap;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.hack23.cia.model.sweden.impl.Ballot;
import com.hack23.cia.model.sweden.impl.CommitteeReport;
import com.hack23.cia.model.sweden.impl.ParliamentMember;
import com.hack23.cia.model.sweden.impl.ParliamentYear;
import com.hack23.cia.model.sweden.impl.PoliticalParty;
import com.hack23.cia.service.impl.admin.agent.sweden.api.AgentSupportService;
import com.hack23.cia.service.impl.admin.agent.sweden.api.BallotAgent;
import com.hack23.cia.service.impl.admin.agent.sweden.api.CommitteeReportAgent;
import com.hack23.cia.service.impl.admin.agent.sweden.api.ParliamentMemberAgent;
import com.hack23.cia.service.impl.admin.agent.sweden.api.PoliticalPartyAgent;

/**
 * The Class ParliamentYearAgentOperation.
 */
public class ParliamentYearAgentOperation extends AbstractParliamentAgentOperation<ParliamentYear> {

    /** The Constant BET. */
    private static final String BET = "bet=";

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The ballot agent. */
    private final BallotAgent ballotAgent;

    /** The commitee report agent. */
    private final CommitteeReportAgent commiteeReportAgent;

    /** The disabled. */
    private final boolean disabled;

    /** The agent support service. */
    private final AgentSupportService agentSupportService;

    /** The logger. */
    private final Logger logger = LoggerFactory.getLogger(ParliamentYearAgentOperation.class);

    /** The parliament member agent. */
    private final ParliamentMemberAgent parliamentMemberAgent;

    /** The political party agent. */
    private final PoliticalPartyAgent politicalPartyAgent;

    /**
	 * Instantiates a new parliament year agent operation.
	 * 
	 * @param agentName
	 *            the agent name
	 * @param agentOperativeCode
	 *            the agent operative code
	 * @param commiteeReportAgent
	 *            the commitee report agent
	 * @param ballotAgent
	 *            the ballot agent
	 * @param parliamentMemberAgent
	 *            the parliament member agent
	 * @param politicalPartyAgent
	 *            the political party agent
	 * @param agentSupportService
	 *            the agent support service
	 * @param disabled
	 *            the disabled
	 */
    public ParliamentYearAgentOperation(final String agentName, final String agentOperativeCode, final CommitteeReportAgent commiteeReportAgent, final BallotAgent ballotAgent, final ParliamentMemberAgent parliamentMemberAgent, final PoliticalPartyAgent politicalPartyAgent, final AgentSupportService agentSupportService, final boolean disabled) {
        super(agentName, agentOperativeCode, agentSupportService);
        this.commiteeReportAgent = commiteeReportAgent;
        this.ballotAgent = ballotAgent;
        this.parliamentMemberAgent = parliamentMemberAgent;
        this.politicalPartyAgent = politicalPartyAgent;
        this.agentSupportService = agentSupportService;
        this.disabled = disabled;
    }

    @Override
    public final void executeOperation() {
        logger.info("Job Started");
        if (!disabled) {
            try {
                this.parliamentMemberAgent.initData();
                List<PoliticalParty> politicalParties = politicalPartyAgent.getCurrentList();
                for (PoliticalParty politcalParty : politicalParties) {
                    agentSupportService.addIfNotExist(politcalParty);
                }
                List<CommitteeReport> currentList = commiteeReportAgent.getCurrentList();
                for (CommitteeReport commiteeReport : currentList) {
                    agentSupportService.addIfNotExist(commiteeReport);
                }
                for (CommitteeReport commiteeReport : agentSupportService.getAllCreatedCommitteeReports()) {
                    if (commiteeReport.getHref().contains(BET)) {
                        Date decidedDateIfAny = commiteeReportAgent.getDecidedDateIfAny(commiteeReport);
                        if (decidedDateIfAny != null) {
                            agentSupportService.addCommitteeReportInformation(commiteeReport.getId(), decidedDateIfAny, ballotAgent.findBallots(commiteeReport));
                        }
                    }
                }
                for (Ballot ballot : agentSupportService.getAllCreatedBallotar()) {
                    try {
                        if (ballot.getCommiteeReport().getHref().contains(BET)) {
                            agentSupportService.addBallotInformation(ballot.getId(), ballotAgent.getVoteResult(ballot));
                        }
                    } catch (RuntimeException runtimeException) {
                        logger.error("Rollback done : Ballot problem " + ballot.getDescription() + " " + ballot.getOverviewHref() + " " + ballot.getVoteResultsHref(), runtimeException);
                    }
                }
                this.agentSupportService.generateParliamentCharts();
                for (PoliticalParty politicalParty : this.agentSupportService.getAllPoliticalParties()) {
                    this.agentSupportService.generatePoliticalPartyCharts(politicalParty.getId());
                }
                for (ParliamentMember parliamentMember : agentSupportService.getCurrentList()) {
                    this.agentSupportService.generateParliamentMemberCharts(parliamentMember.getId());
                }
                updateRiksLedamotsHomePagesRef();
                updateRiksLedamotsWikiRef();
                updateRiksLedamotsEnglishWikiRef();
                this.agentSupportService.deleteParliamentMemberVoteCompareResults();
                logger.info("Deleted ParliamentMemberVoteCompareResults");
                this.agentSupportService.createParliamentMemberVoteCompareResults();
                logger.info("Created ParliamentMemberVoteCompareResults");
            } catch (Exception e) {
                logger.error("Job Failed", e);
            }
        }
        logger.info("Job Ended");
    }

    /**
	 * Update riks ledamots english wiki ref.
	 */
    private void updateRiksLedamotsEnglishWikiRef() {
        final Map<Long, String> updates = new THashMap<Long, String>();
        for (ParliamentMember parliamentMember : agentSupportService.getCurrentList()) {
            if (parliamentMember.getEnglishWikiHref() == null) {
                final String href = this.parliamentMemberAgent.getEnglishWikiHref(parliamentMember);
                if (href != null) {
                    updates.put(parliamentMember.getId(), href);
                }
            }
        }
        agentSupportService.updateParliamentMembersEnglishWikiHref(updates);
    }

    /**
	 * Update riks ledamots home pages ref.
	 */
    private void updateRiksLedamotsHomePagesRef() {
        final Map<Long, String> updates = new THashMap<Long, String>();
        for (ParliamentMember parliamentMember : agentSupportService.getCurrentList()) {
            if (parliamentMember.getHref() == null) {
                final String href = this.parliamentMemberAgent.getHref(parliamentMember);
                if (href != null) {
                    updates.put(parliamentMember.getId(), href);
                }
            }
        }
        agentSupportService.updateParliamentMembersHref(updates);
    }

    /**
	 * Update riks ledamots wiki ref.
	 */
    private void updateRiksLedamotsWikiRef() {
        final Map<Long, String> updates = new THashMap<Long, String>();
        for (ParliamentMember parliamentMember : agentSupportService.getCurrentList()) {
            if (parliamentMember.getWikiHref() == null) {
                final String href = this.parliamentMemberAgent.getWikiHref(parliamentMember);
                if (href != null) {
                    updates.put(parliamentMember.getId(), href);
                }
            }
        }
        agentSupportService.updateParliamentMembersWikiHref(updates);
    }
}
