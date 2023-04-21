package net.sourceforge.yavp.vote;

import java.util.*;
import org.w3c.dom.*;
import net.sourceforge.yavp.xml.*;
import net.sourceforge.yavp.xml.NodeManager.DocumentUninitializedException;

/**Class for handling voting on groups of Candidates.  Subclasses
 * of this class must provide a method to determine whether a Voter
 * is valid or not.
 * 
 * @author Brandon Sanderson
 * @param <VINF>
 *            The Vote implementation used by this AbstractVoteProcedure
 * @param <VEXE>
 *            The Voter class to be used by this AbstractVoteProcedure
 */
public abstract class AbstractVoteProcedure<VINF extends Vote, VEXE extends Voter> implements XMLCodable {

    /**HashMap of Candidates to number of votes.*/
    private HashMap<Candidate, Integer> candidates;

    private boolean votesAllowed = false;

    private boolean voteComplete = false;

    /**Class for comparing Candidates according to the number of
	 * votes each candidate has in this procedure.  Candidates
	 * with less votes are considered 'greater' so that they
	 * will appear at the beginning of Lists/Arrays.
	 * 
	 * NOTE: compare method is inconsistant with equals.
	 * 
	 * @author Brandon Sanderson
	 *
	 */
    public class ProcCandidateComparator implements Comparator<Candidate> {

        @Override
        public int compare(Candidate o1, Candidate o2) {
            if (!candidates.containsKey(o1) || !candidates.containsKey(o2)) {
                throw new IllegalArgumentException("One of the given candidates is invalid.");
            }
            return candidates.get(o2) - candidates.get(o1);
        }
    }

    /**Creates an AbstractVoteProcedure that uses the given
	 * Candidates.  These candidates can then be voted for using
	 * the </code>voteFor<code> method
	 * 
	 * @param candidates
	 *            The candidates for this AbstractVoteProcedure to use.
	 */
    public AbstractVoteProcedure(Candidate[] candidates) {
        this.candidates = new HashMap<Candidate, Integer>(candidates.length);
        for (Candidate c : candidates) {
            this.candidates.put(c, 0);
        }
    }

    public AbstractVoteProcedure(Node n) {
        candidates = new HashMap<Candidate, Integer>();
        xmlDecode(n);
    }

    /**Gets the candidates used by this AbstractVoteProcedure.
	 * 
	 * @throws NoSuchCandidateException
	 *             Thrown if <code>vFor</code> is not a valid Candidate.
	 */
    public Candidate[] getCandidates() {
        return candidates.keySet().toArray(new Candidate[0]);
    }

    /**Determines whether or not the given candidate is part of
	 * this AbstractVoteProcedure.
	 * 
	 * @param tst The candidate to check
	 * @return <code>true</code> if <code>tst</code> is part of
	 * this voting procedure, <code>false</code> otherwise.
	 */
    public boolean isCandidate(Candidate tst) {
        return candidates.containsKey(tst);
    }

    /**Adds a vote for the specified Candidate.  This method
	 * performs checks and other necessities to ensure that the
	 * same Voter cannot vote twice, and that the Voter is valid.
	 * 
	 * @param vote The Candidate to vote for
	 * @param votePlacer The Voter placing the vote
	 * @return <code>true</code> if the vote is successful,
	 * <code>false</code> if <code>votePlacer</code> is invalid.
	 * @throws NoSuchCandidateException Thrown if <code>voteFor</code>
	 * is not one of the candidates for this group.
	 */
    public final synchronized boolean vote(VINF vote, VEXE votePlacer) throws NoSuchCandidateException {
        if (!votesAllowed || voteComplete) {
            return false;
        }
        return executeVote(vote, votePlacer);
    }

    /**
	 * Executes a vote. This method is called by the
	 * <code>vote(VINF,VEXE)</code> method to actually execute the vote, and add
	 * vote counts to the proper candidates. The <code>executeVote</code> method
	 * of AbstractVoteProcedure gives one vote count to the
	 * <code>vote.winningVote()</code> Candidate.
	 * 
	 * @param vote
	 *            The Vote object that holds information on who to give votes
	 *            to.
	 * @param votePlacer
	 *            The Voter placing the vote.
	 * @return <code>true</code> if votes are placed, <code>false</code> if any
	 *         or all of the vote placements fail.
	 * @throws NoSuchCandidateException
	 *             Thrown if a Candidate cannot be found.
	 */
    protected boolean executeVote(VINF vote, VEXE votePlacer) throws NoSuchCandidateException {
        Candidate voteFor = vote.winningVote();
        if (!isValidVoter(votePlacer)) {
            return false;
        }
        addVotes(voteFor, 1);
        return true;
    }

    protected void addVotes(Candidate vfor, int numvotes) throws NoSuchCandidateException {
        if (!candidates.containsKey(vfor)) {
            throw new NoSuchCandidateException(vfor);
        } else {
            candidates.put(vfor, candidates.get(vfor) + numvotes);
        }
    }

    /**
	 * Gets the candidate in this CandidateGroup with the most votes. This
	 * method returns the first Candidate in the list with the greatest number
	 * of votes held by any candidate. Therefore, if there are two candidates
	 * with the same number of votes, which one this method will find is
	 * unknown.
	 * 
	 * @return A Candidate with the highest number of votes, or
	 *         <code>null</code> if there are no candidates in this
	 *         CandidateGroup.
	 */
    public synchronized Candidate getLeader() {
        int highest = 0;
        for (Candidate c : candidates.keySet()) {
            if (candidates.get(c) > highest) {
                highest = candidates.get(c);
            }
        }
        for (Candidate c : candidates.keySet()) {
            if (candidates.get(c) >= highest) {
                return c;
            }
        }
        return null;
    }

    /**
	 * Retrieves the candidates at the given place. Places are number from 0
	 * upwards.<br>
	 * i.e. - The candidates returned by getPlace(0) are the leaders.
	 * 
	 * @param i
	 *            The place of the candidate, from 0 upwards
	 * @return The Candidates at the given place. If i is invalid, an array of
	 *         Candidates with a length of 0 is returned.
	 */
    public synchronized Candidate[] getPlace(int i) {
        ArrayList<Integer> numVotes = new ArrayList<Integer>(candidates.values());
        if (i < 0 || i >= numVotes.size()) {
            return new Candidate[0];
        }
        Collections.sort(numVotes);
        Collections.reverse(numVotes);
        int votes = numVotes.get(i);
        ArrayList<Candidate> finalCandidates = new ArrayList<Candidate>();
        for (Candidate c : this.candidates.keySet()) {
            if (this.candidates.get(c) == votes) {
                finalCandidates.add(c);
            }
        }
        return finalCandidates.toArray(new Candidate[0]);
    }

    /**
	 * Retrieves the current number of votes for a candidate.
	 * 
	 * @param c
	 *            The candidate to determine the number of votes for.
	 * @return The number of votes for <code>c</code>. The exact meaning of this
	 *         number may change depending on the voting procedure.
	 * @throws NoSuchCandidateException
	 *             Thrown if Candidate <code>c</code> is not a Candidate for
	 *             this VoteProcedure
	 */
    public synchronized int getVotes(Candidate c) throws NoSuchCandidateException {
        if (!candidates.containsKey(c)) {
            throw new NoSuchCandidateException(c);
        }
        return candidates.get(c);
    }

    public synchronized int maxPlace() {
        ArrayList<Integer> numVotes = new ArrayList<Integer>(candidates.values());
        return numVotes.size() - 1;
    }

    public synchronized boolean isTied() {
        return getPlace(0).length > 1;
    }

    public void setVotingEnabled(boolean allow) {
        votesAllowed = allow;
    }

    /**
	 * @return whether or not votes are allowed
	 */
    public boolean isVotingEnabled() {
        return votesAllowed;
    }

    public void endVote() {
        voteComplete = true;
    }

    public boolean isEnded() {
        return voteComplete;
    }

    public Candidate[] getSortedCandidates() {
        Candidate[] rankCandids = candidates.keySet().toArray(new Candidate[0]);
        Arrays.sort(rankCandids, new ProcCandidateComparator());
        return rankCandids;
    }

    public int getTotalVotes() {
        int total = 0;
        for (int i : candidates.values()) {
            total += i;
        }
        return total;
    }

    /**Tests whether the given voter can vote in this
	 * voting procedure.
	 *  
	 *  @param checkVotePermission The Voter to check permissions for.
	 */
    public abstract boolean isValidVoter(VEXE checkVotePermission);

    private static final String CANDID_SUPER_E_NAME = "vote-candidates";

    private static final String CANDID_E_NAME = "candidate";

    private static final String CANDID_VOTES_ATTR_NAME = "votes";

    private static final String VALLOW_E_NAME = "vote-enabled";

    private static final String VCOMPLETE_E_NAME = "vote-complete";

    private static final String TOTAL_VOTES_E_NAME = "total-votes";

    @Override
    public void xmlEncode(Node to, Document domDoc) {
        NodeManager manager = new NodeManager(to, domDoc);
        try {
            NodeManager candidManager = manager.createManager(CANDID_SUPER_E_NAME);
            for (Candidate c : candidates.keySet()) {
                int votes = candidates.get(c);
                Element candidate = candidManager.createXMLCodableElement(CANDID_E_NAME, c);
                candidate.setAttribute(CANDID_VOTES_ATTR_NAME, "" + votes);
            }
            manager.createTextTag(VALLOW_E_NAME, "" + votesAllowed);
            manager.createTextTag(VCOMPLETE_E_NAME, "" + voteComplete);
        } catch (DocumentUninitializedException e) {
            throw new IllegalArgumentException("Invalid Node/Document");
        }
    }

    @Override
    public boolean xmlDecode(Node n) {
        NodeManager manage = new NodeManager(n);
        Node candids = manage.findNode(CANDID_SUPER_E_NAME);
        if (candids == null) {
            return false;
        }
        NodeManager candidManager = new NodeManager(candids);
        for (Node candidate : candidManager.findAllNodes(CANDID_E_NAME)) {
            int votes = 0;
            if (candidate instanceof Element) {
                Element cElem = (Element) candidate;
                try {
                    votes = Integer.parseInt(cElem.getAttribute(CANDID_VOTES_ATTR_NAME));
                } catch (NumberFormatException nfe) {
                    throw new IllegalArgumentException("Illegal votes value " + "in candidate attribute - " + cElem.getAttribute(CANDID_VOTES_ATTR_NAME), nfe);
                }
            }
            Candidate candid = new Candidate(candidate);
            candidates.put(candid, votes);
        }
        Node allowed = manage.findNode(VALLOW_E_NAME);
        votesAllowed = Boolean.getBoolean(allowed.getTextContent());
        Node complete = manage.findNode(VCOMPLETE_E_NAME);
        voteComplete = Boolean.getBoolean(complete.getTextContent());
        return true;
    }

    public void xmlWriteResults(Node n, Document d) {
        Candidate[] sorted = getSortedCandidates();
        NodeManager manager = new NodeManager(n, d);
        try {
            NodeManager candidManager = manager.createManager(CANDID_SUPER_E_NAME);
            for (Candidate c : sorted) {
                int votes = candidates.get(c);
                Element candidate = candidManager.createXMLCodableElement(CANDID_E_NAME, c);
                candidate.setAttribute(CANDID_VOTES_ATTR_NAME, "" + votes);
            }
            manager.createTextTag(TOTAL_VOTES_E_NAME, "" + getTotalVotes());
            manager.createTextTag(VALLOW_E_NAME, "" + votesAllowed);
            manager.createTextTag(VCOMPLETE_E_NAME, "" + voteComplete);
        } catch (DocumentUninitializedException e) {
            throw new IllegalArgumentException("Invalid Node/Document");
        }
    }
}
