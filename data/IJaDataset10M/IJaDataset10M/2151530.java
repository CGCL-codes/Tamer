package sudoku;

import solver.Als;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hobiwan
 */
public class AlsInSolutionStep implements Cloneable {

    private List<Integer> indices = new ArrayList<Integer>();

    private List<Integer> candidates = new ArrayList<Integer>();

    private int chainPenalty = -1;

    public AlsInSolutionStep() {
    }

    public void addIndex(int index) {
        indices.add(index);
    }

    public void addCandidate(int cand) {
        candidates.add(cand);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        AlsInSolutionStep newAls = (AlsInSolutionStep) super.clone();
        newAls.indices = (List<Integer>) ((ArrayList<Integer>) indices).clone();
        newAls.candidates = (List<Integer>) ((ArrayList<Integer>) candidates).clone();
        return newAls;
    }

    public List<Integer> getIndices() {
        return indices;
    }

    public void setIndices(List<Integer> indices) {
        this.indices = indices;
    }

    public List<Integer> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<Integer> candidates) {
        this.candidates = candidates;
    }

    public int getChainPenalty() {
        if (chainPenalty == -1) {
            chainPenalty = Als.getChainPenalty(indices.size());
        }
        return chainPenalty;
    }

    public void setChainPenalty(int chainPenalty) {
        this.chainPenalty = chainPenalty;
    }
}
