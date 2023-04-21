package edu.sharif.ce.dml.mobisim.evaluator.model.network;

import edu.sharif.ce.dml.common.logic.entity.Node;
import edu.sharif.ce.dml.common.logic.entity.SnapShot;
import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Masoud
 * Date: Jun 14, 2008
 * Time: 3:57:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class AveragePower extends NetworkEvaluator {

    double totalPower = 0;

    int numberOfData = 0;

    public void evaluate(SnapShot snapShot) {
        for (Node node : snapShot.getNodeShadows()) {
            if (node.isActive()) {
                totalPower += Node.convertRangetoPower(node.getRange());
                numberOfData++;
            }
        }
    }

    public void reset() {
        totalPower = 0;
        numberOfData = 0;
    }

    public List print() {
        return Arrays.asList(totalPower / numberOfData);
    }

    public List<String> getLabels() {
        return Arrays.asList("Average Power");
    }
}
