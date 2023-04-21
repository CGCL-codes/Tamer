package org.game.thyvin.logic.pathfinding.stop;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import net.sf.thyvin.node.search.IStopFunction;
import org.game.thyvin.ai.influencemap.UnitInfluenceFunction;
import org.game.thyvin.logic.room.ThyvinSampleNode;

public class InfluenceStopFunction implements IStopFunction<ThyvinSampleNode> {

    private Map<ThyvinSampleNode, Double> nodeToInfluence;

    private UnitInfluenceFunction unitInfluenceFunction;

    private double stopRange;

    private int allySign;

    public InfluenceStopFunction(double stopRange, UnitInfluenceFunction unitInfluenceFunction) {
        this.stopRange = stopRange;
        this.unitInfluenceFunction = unitInfluenceFunction;
    }

    public void initialize(boolean ally) {
        nodeToInfluence = new HashMap<ThyvinSampleNode, Double>();
        allySign = ally ? 1 : -1;
    }

    public Map<ThyvinSampleNode, Double> getNodeToInfluence() {
        return nodeToInfluence;
    }

    public Collection<ThyvinSampleNode> getEndNodes() {
        return new ArrayList<ThyvinSampleNode>();
    }

    public boolean stop(ThyvinSampleNode node, double dist) {
        if (dist > stopRange) {
            return true;
        }
        nodeToInfluence.put(node, allySign * unitInfluenceFunction.transformDistanceToInfluence(dist));
        return false;
    }
}
