package scelect.server.core;

/**
 *
 * @author Calulo
 */
public class ResultsStruct {

    private final String[] positions;

    private final String[][] candidates;

    private final int[][] votes;

    public ResultsStruct(String[] positions, String[][] candidates, int[][] votes) {
        this.positions = positions;
        this.candidates = candidates;
        this.votes = votes;
    }

    public String[] getPositions() {
        return positions;
    }

    public String[][] getCandidates() {
        return candidates;
    }

    public int[][] getVoteData() {
        return votes;
    }
}
