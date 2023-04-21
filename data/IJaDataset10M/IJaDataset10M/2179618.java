package com.hack23.cia.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.hack23.cia.model.sweden.Ballot;
import com.hack23.cia.model.sweden.CommitteeReport;
import com.hack23.cia.model.sweden.ParliamentMember;
import com.hack23.cia.model.sweden.ParliamentMemberVoteCompareResult;
import com.hack23.cia.model.sweden.PartyVoteSummary;
import com.hack23.cia.model.sweden.Vote;
import com.hack23.cia.model.sweden.CommitteeReport.ImportStatus;
import com.hack23.cia.model.sweden.Vote.Position;
import com.hack23.cia.service.dao.BallotDAO;
import com.hack23.cia.service.dao.CommiteeReportDAO;
import com.hack23.cia.service.dao.ParliamentMemberDAO;

/**
 * The Class LoaderServiceImpl.
 */
@Transactional(propagation = Propagation.REQUIRED)
public class LoaderServiceImpl implements LoaderService {

    /**
     * The log.
     */
    private static Log log = LogFactory.getLog(LoaderServiceImpl.class);

    /**
     * The commitee report dao.
     */
    private final CommiteeReportDAO commiteeReportDAO;

    /**
     * The ballot dao.
     */
    private final BallotDAO ballotDAO;

    /**
     * The parliament member dao.
     */
    private final ParliamentMemberDAO parliamentMemberDAO;

    /**
     * Instantiates a new loader service impl.
     * 
     * @param commiteeReportDAO the commitee report dao
     * @param ballotDAO the ballot dao
     * @param parliamentMemberDAO the parliament member dao
     */
    public LoaderServiceImpl(final CommiteeReportDAO commiteeReportDAO, final BallotDAO ballotDAO, final ParliamentMemberDAO parliamentMemberDAO) {
        super();
        this.commiteeReportDAO = commiteeReportDAO;
        this.ballotDAO = ballotDAO;
        this.parliamentMemberDAO = parliamentMemberDAO;
    }

    @Override
    public final void addBetankandeInformation(final Long id, final Date decidedDateIfAny, final List<Ballot> findVotedballot) {
        CommitteeReport commiteeReport = commiteeReportDAO.load(id);
        commiteeReport.setImportStatus(ImportStatus.Completed);
        commiteeReport.setDecisionDate(decidedDateIfAny);
        if (findVotedballot != null) {
            for (Ballot ballot : findVotedballot) {
                ballot.setDatum(decidedDateIfAny);
                ballot.setCommiteeReport(commiteeReport);
                commiteeReport.getBallots().add(ballot);
            }
        }
        commiteeReportDAO.save(commiteeReport);
        log.info("CommiteeReport updated " + commiteeReport.getDecisionDate() + "  ballots : " + commiteeReport.getBallots().size());
    }

    @Override
    public final void addIfNotExist(final CommitteeReport commiteeReport) {
        CommitteeReport exist = commiteeReportDAO.findByName(commiteeReport.getName());
        if (exist == null) {
            commiteeReportDAO.save(commiteeReport);
            log.info("CommiteeReport saved " + commiteeReport.getName());
        }
    }

    @Override
    public final void addBallotInformation(final Long ballotId, final Set<Vote> rostResult) {
        Ballot ballot = ballotDAO.load(ballotId);
        if (rostResult.size() == Ballot.TOTAL_VOTES) {
            Map<String, ParliamentMember> allMap = createMap(parliamentMemberDAO.getAll());
            for (Vote vote : rostResult) {
                vote.setDatum(ballot.getCommiteeReport().getDecisionDate());
                vote.getParliamentMember().createFuzzyKey();
                ParliamentMember parliamentMember = allMap.get(vote.getParliamentMember().getFuzzyKey());
                if (parliamentMember == null) {
                    parliamentMember = parliamentMemberDAO.save(vote.getParliamentMember());
                    allMap.put(parliamentMember.getFuzzyKey(), parliamentMember);
                }
                parliamentMember.newVote(vote);
                vote.setBallot(ballot);
                ballot.getVotes().add(vote);
                parliamentMemberDAO.save(parliamentMember);
            }
            ballot.setImportStatus(Ballot.ImportStatus.Completed);
            ballotDAO.save(ballot);
            log.info("Ballot saved " + ballot.getDescription());
        }
    }

    /**
     * Calc friends.
     * 
     * @param first the first
     * @param parliamentMember the parliament member
     * @param parliamentMember2 the parliament member2
     * 
     * @return the parliament member vote compare result
     */
    private ParliamentMemberVoteCompareResult calcFriends(final List<Vote> first, final ParliamentMember parliamentMember, final ParliamentMember parliamentMember2) {
        int total = 0;
        int with = 0;
        for (Vote vote : first) {
            Position position = vote.getPosition();
            Vote vote2 = vote.getBallot().getVoteForParliamentMember(parliamentMember2);
            if (vote2 != null) {
                Position position2 = vote2.getPosition();
                if (!((Position.Absent.equals(position)) || (Position.Absent.equals(position2)) || (Position.Neutral.equals(position)) || (Position.Neutral.equals(position2)))) {
                    if (position.equals(position2)) {
                        with++;
                    }
                    total++;
                }
            }
        }
        float result = -1f;
        if (total != 0) {
            result = (with * 100f) / total;
        } else {
            result = -1;
        }
        return new ParliamentMemberVoteCompareResult(new Date(), total, result, parliamentMember, parliamentMember2);
    }

    @Override
    public final void clearRebelsAndOpponents() {
        List<ParliamentMember> all = parliamentMemberDAO.getAll();
        for (ParliamentMember parliamentMember : all) {
            parliamentMember.clearRebelsAndOpponents();
        }
        parliamentMemberDAO.saveAll(all);
    }

    /**
     * Creates the map.
     * 
     * @param all the all
     * 
     * @return the map< string, parliament member>
     */
    private Map<String, ParliamentMember> createMap(final List<ParliamentMember> all) {
        Map<String, ParliamentMember> map = new HashMap<String, ParliamentMember>();
        for (ParliamentMember parliamentMember : all) {
            map.put(parliamentMember.getFuzzyKey(), parliamentMember);
        }
        return map;
    }

    public final void createParliamentMemberVoteCompareResults() {
        List<ParliamentMemberVoteCompareResult> result = new ArrayList<ParliamentMemberVoteCompareResult>();
        List<ParliamentMember> list = parliamentMemberDAO.getCurrentList();
        for (ParliamentMember parliamentMember : list) {
            List<Vote> position = parliamentMemberDAO.findVotes(parliamentMember.getId());
            for (ParliamentMember parliamentMember2 : list) {
                if (!parliamentMember.getId().equals(parliamentMember2.getId())) {
                    result.add(calcFriends(position, parliamentMember, parliamentMember2));
                }
            }
            log.info("Generated friends " + parliamentMember.getName() + " " + parliamentMember.getParty());
        }
        parliamentMemberDAO.saveParliamentMemberRostCompareResult(result);
    }

    public final void deleteParliamentMemberRostCompareResults() {
        this.parliamentMemberDAO.deleteAllParliamentMemberRostCompareResults();
    }

    @Override
    public final void generateRebelsAndOpponents(final Long ballotId) {
        Ballot ballot = ballotDAO.load(ballotId);
        Set<ParliamentMember> update = new HashSet<ParliamentMember>();
        for (Vote vote : ballot.getVotes()) {
            if (!(vote.getPosition().equals(Position.Absent) || vote.getPosition().equals(Position.Neutral))) {
                ParliamentMember parliamentMember = vote.getParliamentMember();
                if (!ballot.getVoteSummary().getWinningPosition().equals(vote.getPosition())) {
                    parliamentMember.setOpponent(parliamentMember.getOpponent() + 1);
                    update.add(parliamentMember);
                }
                PartyVoteSummary partyRostSummarary = ballot.getVoteSummary().getPartyRostSummarary(parliamentMember.getParty());
                if (!partyRostSummarary.getWinningPosition().equals(vote.getPosition())) {
                    parliamentMember.setRebel(parliamentMember.getRebel() + 1);
                    update.add(parliamentMember);
                }
            }
        }
        parliamentMemberDAO.saveAll(update);
    }

    @Override
    public final void generateRostSummary(final Long ballotId) {
        Ballot ballot = ballotDAO.load(ballotId);
        for (Vote vote : ballot.getVotes()) {
            ballot.getVoteSummary().newVote(vote);
        }
        ballot.getVoteSummary().calcWinningPosition();
        ballotDAO.save(ballot);
    }

    @Override
    public final List<Ballot> getAllCompletedBallotar() {
        return ballotDAO.getAllCompleted();
    }

    @Override
    public final List<CommitteeReport> getAllCreatedBetankanden() {
        return commiteeReportDAO.getAllCreated();
    }

    @Override
    public final List<Ballot> getAllCreatedBallotar() {
        return ballotDAO.getAllCreated();
    }

    public final List<ParliamentMember> getCurrentList() {
        return parliamentMemberDAO.getCurrentList();
    }

    @Override
    public final void updateParliamentMembersHref(final Map<Long, String> updates) {
        List<ParliamentMember> list = new ArrayList<ParliamentMember>();
        for (Entry<Long, String> entry : updates.entrySet()) {
            ParliamentMember parliamentMember = this.parliamentMemberDAO.load(entry.getKey());
            if (parliamentMember != null) {
                parliamentMember.setHref(entry.getValue());
                list.add(parliamentMember);
            }
        }
        this.parliamentMemberDAO.saveAll(list);
    }

    @Override
    public final void updateParliamentMembersEnglishWikiHref(final Map<Long, String> updates) {
        List<ParliamentMember> list = new ArrayList<ParliamentMember>();
        for (Entry<Long, String> entry : updates.entrySet()) {
            ParliamentMember parliamentMember = this.parliamentMemberDAO.load(entry.getKey());
            if (parliamentMember != null) {
                parliamentMember.setEnglishWikiHref(entry.getValue());
                list.add(parliamentMember);
            }
        }
        this.parliamentMemberDAO.saveAll(list);
    }

    @Override
    public final void updateParliamentMembersWikiHref(final Map<Long, String> updates) {
        List<ParliamentMember> list = new ArrayList<ParliamentMember>();
        for (Entry<Long, String> entry : updates.entrySet()) {
            ParliamentMember parliamentMember = this.parliamentMemberDAO.load(entry.getKey());
            if (parliamentMember != null) {
                parliamentMember.setWikiHref(entry.getValue());
                list.add(parliamentMember);
            }
        }
        this.parliamentMemberDAO.saveAll(list);
    }
}
