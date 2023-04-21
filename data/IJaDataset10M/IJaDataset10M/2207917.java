package com.vce.election.domain;

import com.vce.*;
import com.vce.election.Election;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

/**
 *
 * @author Ggutierrez
 */
public class ElectionTest extends AbstractTransactionalDataSourceSpringContextTests {

    private Election election;

    public ElectionTest(String testName) {
        super(testName);
    }

    public Election getElection() {
        return election;
    }

    public void setElection(Election election) {
        this.election = election;
    }

    @Override
    public String[] getConfigLocations() {
        return new String[] { "META-INF/application.spring.xml" };
    }

    public void testGetYear() {
        Year year = election.getYear(1L);
        assertNotNull(year);
    }

    public void testGetUser() {
        User user = election.getUser(1L);
        assertNotNull(user);
    }

    public void testGetElectionType() {
        ElectionType electionType = election.getElectionType(1L);
        assertNotNull(electionType);
        electionType = election.getCurrentElectionType(true);
        assertNotNull(electionType);
    }

    public void testGetParty() {
        Party party = election.getParty(1L);
        assertNotNull(party);
    }

    public void testGetCandidate() {
        Candidate candidate = election.getCandidate(1L);
        assertNotNull(candidate);
    }

    public void testGetPosition() {
        Position position = election.getPosition(1L);
        assertNotNull(position);
    }

    public void testGetState() {
        State state = election.getState(1L);
        assertNotNull(state);
        state = election.findState("ma");
        System.out.println("State found: " + state.getName());
        assertNotNull(state);
    }

    public void testFindGenerals() {
        List<Year> years = election.findYears();
        assertEquals(2, years.size());
        List<User> users = election.findUsers();
        assertEquals(8, users.size());
    }

    public void testFindElectionComponents() {
        List<ElectionType> electionTypes = election.findElectionTypes();
        assertEquals(4, electionTypes.size());
        List<Party> parties = election.findParties();
        assertEquals(13, parties.size());
        List<Candidate> candidates = election.findCandidates();
        assertEquals(2, candidates.size());
        List<Position> position = election.findPositions();
        assertEquals(7, position.size());
        List<State> states = election.findStates();
        assertEquals(18, states.size());
        List<Town> towns = election.findTowns();
        assertEquals(159, towns.size());
    }

    public void testFindElectionComponentsFiltered() {
        Year year = election.getYear(2L);
        ElectionType electionType = election.getCurrentElectionType(true);
        List<ElectionType> electionTypes = election.findElectionTypes(year);
        assertEquals(2, electionTypes.size());
        List<Party> parties = election.findParties(electionType);
        assertEquals(5, parties.size());
        Party party = election.getParty(9L);
        List<Candidate> candidates = election.findCandidates(party);
        assertEquals(1, candidates.size());
        candidates = election.findCandidates(year, party);
        assertEquals(1, candidates.size());
        State state = election.getState(6L);
        List<Town> towns = election.findTowns(state);
        assertEquals(14, towns.size());
        User user = election.getUser(1L);
        System.out.println("User: " + user.getFirstName());
        List<Jrv> jrvs = election.findJrvs("6080", user, Counting.RAPIDO, Round.PRIMERA);
        assertEquals(141, jrvs.size());
        System.out.println("Entering find Inserted Jrvs");
        List<Jrv> jrvs2 = election.findInsertedJrvs("6", user, Counting.PARALELO, Round.PRIMERA);
        assertEquals(8, jrvs2.size());
        System.out.println("Finishing find Inserted Jrvs");
    }

    public void testAuthenticate() {
        User user = election.authenticate("ggutierrez", "abc");
        assertNotNull(user);
        assertEquals(user.getFirstName(), "Giovanella");
    }

    public void testFindElectionStalls() {
        ElectionType electionType = election.getCurrentElectionType(true);
        assertNotNull(electionType);
        List<Party> parties = election.findParties(electionType);
        System.out.println("Only Parties");
        for (Party party : parties) {
            System.out.println("Stall: " + party.getStall() + "; Party: " + party.getInititals());
        }
        TreeMap<Party, Long> stalls = election.findElectionStalls(electionType);
        assertNotNull(stalls);
        System.out.println("Ordered Map");
        Set entries = stalls.entrySet();
        Iterator iter = entries.iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            System.out.println("Map ** Stall: " + ((Party) key).getStall() + "; Party: " + ((Party) key).getInititals() + "; Votes: " + (Long) value);
        }
        List<Object[]> arrStalls = new ArrayList();
        parties = election.findParties(electionType);
        for (Party party : parties) {
            Object[] temp = new Object[3];
            temp[0] = party.getId();
            temp[1] = party.getInititals();
            temp[2] = 0L;
            arrStalls.add(temp);
        }
        List stallList = new ArrayList(election.findElectionStalls(electionType).entrySet());
        System.out.println("Parties");
        Iterator iterator = stallList.iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            System.out.println("Stall: " + ((Party) key).getStall() + "; Party: " + ((Party) key).getInititals() + "; Votes: " + (Long) value);
        }
    }

    public void testLastInsertedDocument() {
        User user = election.getUser(1L);
        assertNotNull(user);
        assertEquals(user.getFirstName(), "Giovanella");
        Document document = election.getlastInsertedDocument(user, Counting.RAPIDO);
        assertNotNull(document);
    }

    public void testTotalInsertedDocuments() {
        User user = election.getUser(1L);
        assertNotNull(user);
        int total = election.getTotalInsertedDocuments(user, Counting.RAPIDO);
    }

    public void testPadronLists() {
        Town town = election.getTown(1L);
        assertNotNull(town);
        List<BallotCentre> ballots1 = town.getBallots();
        assertNotNull(ballots1);
        assertEquals(38, ballots1.size());
        List<BallotCentre> ballots = election.findBallotCentres(town);
        assertNotNull(ballots);
        assertEquals(37, ballots.size());
        BallotCentre ballot = ballots.get(0);
        System.out.println("Ballot " + ballot.getDescription());
        List<Jrv> jrvs = election.findJrvs(ballot);
        assertNotNull(jrvs);
        assertEquals(2, jrvs.size());
    }

    public void testGetDocument() {
        System.out.println("getDocument");
        Jrv jrv = election.getJrv(5702L);
        assertNotNull(jrv);
    }

    public void testFindJrvsNotInserted() {
        BallotCentre bc = election.getBallotCentre(1512L);
        List<Jrv> jrvs = election.findJrvs("6060", bc, Counting.RAPIDO, Round.PRIMERA);
        assertNotNull(jrvs);
        assertEquals(0, jrvs.size());
    }

    public void testFindJrvs() {
        BallotCentre ballot = election.getBallotCentre(1485L);
        List<Jrv> jrvs = election.findJrvs("6052", ballot, Counting.RAPIDO, Round.PRIMERA);
        assertNotNull(jrvs);
        for (Jrv jrv : jrvs) {
            System.out.println("Jrv " + jrv.getCode());
        }
    }

    public void testGetPadron() {
        List<Padron> electors = election.findElectors("001");
        assertNotNull(electors);
        assertEquals(electors.size(), 2);
        assertEquals(electors.get(0).getFirstName(), "GIOVANELLA");
        System.out.println("Jrv: " + electors.get(0).getJrv().getCode());
    }

    public void testFindResults() {
        Jrv jrv = election.getJrv(5141L);
        assertNotNull(jrv);
        List<Object[]> results = new ArrayList<Object[]>();
        try {
            results = election.findResults(jrv, Counting.RAPIDO);
        } catch (Exception ex) {
            Logger.getLogger(ElectionTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Town Id: " + results.get(0)[0]);
        assertNotNull(results);
    }
}
