package ttsim;

/**
 * <p>Description: </p>
 * @author Faisal Aslam
 * @version 1.0
 */
public class NeighborAndProb {

    short nodeId = 0;

    double probability = 0;

    public NeighborAndProb(short neighborNodeId, double probability) {
        this.nodeId = neighborNodeId;
        this.probability = probability;
    }

    @Override
    public String toString() {
        return "(" + nodeId + ", " + probability + ")";
    }
}
