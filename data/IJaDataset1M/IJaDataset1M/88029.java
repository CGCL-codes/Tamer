package zombiedefense.map;

/**
 * A node object used for pathfinding algorithms.
 * @author Connor Willison
 */
public class Node implements Comparable {

    public enum ListValue {

        None, OpenList, ClosedList
    }

    ;

    public static final int straightMovementCost = 10;

    public static final int diagonalMovementCost = 14;

    private int x, y;

    private int distFromStart;

    private int distFromEnd;

    private int sumDist;

    private Node parent;

    private ListValue listValue = ListValue.None;

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setSumDist(int sumDist) {
        this.sumDist = sumDist;
    }

    public int getDistFromEnd() {
        return distFromEnd;
    }

    public void setDistFromEnd(int distFromEnd) {
        this.distFromEnd = distFromEnd;
    }

    public int getDistFromStart() {
        return distFromStart;
    }

    public void setDistFromStart(int distFromStart) {
        this.distFromStart = distFromStart;
    }

    public int getSumDist() {
        return sumDist;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public ListValue getListValue() {
        return listValue;
    }

    public void setListValue(ListValue listValue) {
        this.listValue = listValue;
    }

    /**
     * Calculates whether or not the path
     * from the start, through this node, to the toNode
     * is shorter than the toNode's current path.
     * @param toNode
     * @return 
     */
    public boolean isBetterPath(Node toNode) {
        return getDistFromStart() + getMovementCost(toNode) < toNode.getDistFromStart();
    }

    /**
     * Returns the movement cost that will be encountered
     * on the trip to the specified node from the current node.
     * Should be used only for nodes that are adjacent in the grid.
     * @param toNode
     * @return 
     */
    public int getMovementCost(Node toNode) {
        int xDiff = Math.abs(toNode.getX() - getX());
        int yDiff = Math.abs(toNode.getY() - getY());
        if (xDiff == 0) {
            return yDiff * straightMovementCost;
        } else if (yDiff == 0) {
            return xDiff * straightMovementCost;
        } else {
            return xDiff * diagonalMovementCost;
        }
    }

    /**
     * Gets the "Manhattan Distance" from this node to the
     * "toNode", which is defined as the sum of the absolute differences
     * between their x and y values multiplied by the orthogonal movement cost
     * per node.
     * @param toNode
     * @return 
     */
    public int getManhattanDistance(Node toNode) {
        return (Math.abs(toNode.getX() - getX()) + Math.abs(toNode.getY() - getY())) * straightMovementCost;
    }

    public int compareTo(Object t) {
        if (t instanceof Node) {
            Node n = (Node) t;
            if (n.getSumDist() < getSumDist()) {
                return 1;
            } else if (n.getSumDist() > getSumDist()) {
                return -1;
            } else {
                return 0;
            }
        }
        return 0;
    }
}
