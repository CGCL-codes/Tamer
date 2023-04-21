package br.com.pedroboechat.a16bitrpg.ai;

import java.util.List;
import jgame.JGPoint;

public class AStarPathfinder extends Pathfinder {

    private static AStarPathfinder instance = null;

    private AStarPathfinder() {
    }

    public static AStarPathfinder getInstance() {
        if (instance == null) instance = new AStarPathfinder();
        return instance;
    }

    public List<JGPoint> search(JGPoint startLoc, JGPoint goalLoc) {
        return search(startLoc, goalLoc, null);
    }

    /**
	 * Derived from pseudo-code in: "The Basics of A* for Path Planning", Bryan
	 * Stout In 'Game Programming Gems', Mike DeLoura (ed.) Charles River Media,
	 * 2000, part 3.3, pp. 254-263
	 */
    public List<JGPoint> search(JGPoint startLoc, JGPoint goalLoc, List<JGPoint> bannedPoints) {
        double newCost;
        TileNode bestNode, newNode;
        TileNode startNode = new TileNode(startLoc);
        startNode.costToGoal(goalLoc);
        TilesPriQueue open = new TilesPriQueue(startNode);
        TilesList closed = new TilesList();
        while (open.size() > 0) {
            bestNode = open.removeFirst();
            while (bannedPoints.contains(bestNode.getPoint()) && open.size() > 0) bestNode = open.removeFirst();
            if (goalLoc.equals(bestNode.getPoint())) return bestNode.buildPath(); else {
                for (int i = 0; i < 4; i++) {
                    if ((newNode = bestNode.makeNeighbour(i)) != null) {
                        newCost = newNode.getCostFromStart();
                        TileNode oldVer;
                        if (((oldVer = open.findNode(newNode.getPoint())) != null) && (oldVer.getCostFromStart() <= newCost)) continue; else if (((oldVer = closed.findNode(newNode.getPoint())) != null) && (oldVer.getCostFromStart() <= newCost)) continue; else {
                            newNode.costToGoal(goalLoc);
                            closed.delete(newNode.getPoint());
                            open.delete(newNode.getPoint());
                            open.add(newNode);
                        }
                    }
                }
            }
            closed.add(bestNode);
        }
        return null;
    }
}
