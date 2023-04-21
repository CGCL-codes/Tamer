package Agents;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import BotEnvironment.SearchBot.BotSearch;
import BotEnvironment.SearchBot.Node;
import BotEnvironment.SearchBot.SBConstants;

public abstract class bestrada_AbstractSearchAgent extends BotSearch {

    public static final int MOVEMENT_COST = 20;

    public static final int TURN_COST = 10;

    /**
    * pretty much a struct for extra node information
    * @author bestrada
    */
    class NodeInfo implements Comparable<NodeInfo> {

        public NodeInfo(Node n) {
            this.node = n;
        }

        /** what direction your guy faces if he moved here */
        public int facing;

        /** how much it costs to get to this spot */
        public int pathCost;

        /** estimated cost heuristic (e.g. distance to goal) */
        public int heuristic;

        /** the "breadcrumb" if this is on the path to the goal */
        public int nextMove;

        /** a reference to this node's parent */
        public Node parent;

        /** a reference to this node itself */
        public Node node;

        public int compareTo(NodeInfo n) {
            return (this.heuristic + this.pathCost) - (n.heuristic + n.pathCost);
        }
    }

    protected Map<Node, NodeInfo> _nodeInfo;

    private Queue<Node> _fringe;

    private boolean _firstStep;

    protected bestrada_AbstractSearchAgent() {
        _firstStep = false;
        _nodeInfo = new HashMap<Node, NodeInfo>();
        _fringe = new PriorityQueue<Node>(1024, new NodeComparator());
    }

    protected abstract String getSearchMethodName();

    class NodeComparator implements Comparator<Node> {

        public int compare(Node n1, Node n2) {
            return _nodeInfo.get(n1).compareTo(_nodeInfo.get(n2));
        }
    }

    @Override
    protected void reset() {
        super.reset();
        _nodeInfo.clear();
        _firstStep = false;
    }

    @Override
    public void movementStep() {
        int direction = _nodeInfo.get(super.getBotLocation()).nextMove;
        super.turnTo(direction);
        super.moveForward();
    }

    private long searchStepCounter = 0;

    @Override
    public void searchStep() {
        if (!_firstStep) {
            _firstStep = true;
            NodeInfo ni = new NodeInfo(super.getStartingLocation());
            ni.facing = SBConstants.EAST;
            ni.parent = null;
            ni.heuristic = this.heuristic(super.getStartingLocation());
            ni.pathCost = super.getStartingLocation().getCost();
            _nodeInfo.put(super.getStartingLocation(), ni);
            _fringe.add(super.getBotLocation());
            super.log("Using " + getSearchMethodName() + " Search Method");
            super.log("Map Name: " + "\n\n");
        }
        if (_fringe.isEmpty()) return;
        Node node = _fringe.remove();
        moveSearchLocation(node);
        NodeInfo ni = _nodeInfo.get(node);
        super.log("Current Node: (" + node.getX() + ", " + node.getY() + ")");
        super.log("----------------------------");
        super.log("search steps: " + searchStepCounter++);
        super.log("Path Cost: " + ni.pathCost);
        super.log("Heuristic: " + ni.heuristic);
        super.log("Total Cost: " + (ni.pathCost + ni.heuristic));
        super.log("\n");
        if (super.getGoalFound()) {
            for (Node n = node; null != n; ) {
                Node parent = _nodeInfo.get(n).parent;
                if (null == parent) break;
                int move = getMove(parent, n);
                _nodeInfo.get(parent).nextMove = move;
                n = parent;
            }
        } else {
            this.processNode(getNorthOfSearchLocation(), node);
            this.processNode(getSouthOfSearchLocation(), node);
            this.processNode(getEastOfSearchLocation(), node);
            this.processNode(getWestOfSearchLocation(), node);
        }
    }

    /** determines if this node needs to get added to the fringe and processed */
    private void processNode(Node n, Node parent) {
        if (null != n && !n.getIsWall()) {
            NodeInfo ni = new NodeInfo(n);
            ni.parent = parent;
            ni.facing = this.getMove(parent, n);
            ni.heuristic = heuristic(n);
            ni.pathCost = pathCost(ni);
            if (!_nodeInfo.containsKey(n) || 0 > ni.compareTo(_nodeInfo.get(n))) {
                _nodeInfo.put(n, ni);
                _fringe.add(n);
            }
        }
    }

    protected abstract int pathCost(NodeInfo ni);

    protected abstract int heuristic(Node n);

    protected int getTravelCost(Node from, Node to) {
        int cost = bestrada_AbstractSearchAgent.MOVEMENT_COST;
        NodeInfo fni = _nodeInfo.get(from);
        switch(fni.facing) {
            case SBConstants.NORTH:
                switch(this.getMove(from, to)) {
                    case SBConstants.EAST:
                    case SBConstants.WEST:
                        cost += bestrada_UniformCostSearchAgent.TURN_COST;
                        break;
                    case SBConstants.SOUTH:
                        cost += (bestrada_UniformCostSearchAgent.TURN_COST * 2);
                        break;
                }
                break;
            case SBConstants.SOUTH:
                switch(this.getMove(from, to)) {
                    case SBConstants.EAST:
                    case SBConstants.WEST:
                        cost += bestrada_AbstractSearchAgent.TURN_COST;
                        break;
                    case SBConstants.NORTH:
                        cost += (bestrada_UniformCostSearchAgent.TURN_COST * 2);
                        break;
                }
                break;
            case SBConstants.EAST:
                switch(this.getMove(from, to)) {
                    case SBConstants.SOUTH:
                    case SBConstants.NORTH:
                        cost += bestrada_UniformCostSearchAgent.TURN_COST;
                        break;
                    case SBConstants.WEST:
                        cost += (bestrada_UniformCostSearchAgent.TURN_COST * 2);
                        break;
                }
                break;
            case SBConstants.WEST:
                switch(this.getMove(from, to)) {
                    case SBConstants.SOUTH:
                    case SBConstants.NORTH:
                        cost += bestrada_UniformCostSearchAgent.TURN_COST;
                        break;
                    case SBConstants.EAST:
                        cost += (bestrada_UniformCostSearchAgent.TURN_COST * 2);
                        break;
                }
                break;
        }
        return cost;
    }

    protected int getMove(Node from, Node to) {
        int result = -1;
        if (to == from.getNorth()) result = SBConstants.NORTH; else if (to == from.getEast()) result = SBConstants.EAST; else if (to == from.getSouth()) result = SBConstants.SOUTH; else if (to == from.getWest()) result = SBConstants.WEST;
        if (result == -1) {
            super.log("Hey, couldn't find the direction!");
        }
        return result;
    }
}
