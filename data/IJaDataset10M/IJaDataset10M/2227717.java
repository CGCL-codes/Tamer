package edu.sharif.ce.dml.mobisim.evaluator.model.network;

import edu.sharif.ce.dml.common.logic.entity.Node;
import edu.sharif.ce.dml.common.logic.entity.SnapShot;
import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Masoud
 * Date: Jul 19, 2007
 * Time: 8:55:16 PM
 */
public class AverageLifeTime extends NetworkEvaluator {

    private long totalLife = 0;

    private int nodesNumber = 0;

    public void evaluate(SnapShot snapShot) {
        Node[] nodes = snapShot.getNodeShadows();
        nodesNumber = nodes.length;
        for (Node node : nodes) {
            totalLife += node.isActive() ? sampleTime : 0;
        }
    }

    public void reset() {
        nodesNumber = 0;
        totalLife = 0;
    }

    public List print() {
        return Arrays.asList(1.0 * totalLife / nodesNumber);
    }

    public List<String> getLabels() {
        return Arrays.asList("Avg LifeTime");
    }
}
