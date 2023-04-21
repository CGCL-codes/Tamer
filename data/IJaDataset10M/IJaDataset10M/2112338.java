package net.sf.pathfinder.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Path implements Serializable {

    private List<PathStep> path = new ArrayList<PathStep>();

    private List<Node> backTrackingNodes = new ArrayList<Node>();

    /**
	 * Creates a new Path
	 */
    public Path() {
    }

    /**
	 * Gets the nodes of the path
	 * @return The nodes
	 */
    public List<PathStep> getPathSteps() {
        return path;
    }

    public int getLength() {
        int length = 0;
        for (PathStep step : path) {
            if (step.next != null) {
                length += step.next.getLength();
            }
        }
        return length;
    }

    /**
	 * Gets a list overheaded nodes
	 * @return the nodes
	 */
    public List<Node> getBackTrackingNodes() {
        return this.backTrackingNodes;
    }

    public static class PathStep implements Serializable {

        public Node node;

        public Edge next;

        public PathStep(Node node, Edge next) {
            this.node = node;
            this.next = next;
        }
    }

    /**
	 * Gets the start node of the path
	 * @return The start node
	 */
    public Node getStart() {
        if (path.size() > 0) {
            return path.get(0).node;
        }
        return null;
    }

    /**
	 * Gets the destination node of the path
	 * @return The destination node
	 */
    public Node getDestination() {
        if (path.size() > 0) {
            return path.get(path.size() - 1).node;
        }
        return null;
    }
}
