package nl.hhs.i2.team5.happer.gui.util;

import java.util.ArrayList;
import java.util.Collections;
import nl.hhs.i2.team5.happer.gui.Speelveld;

/**
 *
 * @author Team 5
 */
public class PathFinder {

    private ArrayList closed = new ArrayList();

    private SortedList open = new SortedList();

    private Node[][] nodes;

    private int maxSearchDistance;

    private int Width;

    private int Height;

    private boolean allowDiagMovement;

    private boolean MensGevonden = false;

    private Speelveld speelveld = null;

    public PathFinder(Speelveld speelveld, int Width, int Height, int MaxSearchDistance, boolean allowDiagMovement) {
        this.Width = Width;
        this.Height = Height;
        this.allowDiagMovement = allowDiagMovement;
        this.maxSearchDistance = MaxSearchDistance;
        this.speelveld = speelveld;
        nodes = new Node[Width][Height];
        for (int x = 0; x < Width; x++) {
            for (int y = 0; y < Height; y++) {
                nodes[x][y] = new Node(x, y);
            }
        }
    }

    /**
         * @param sx The x coordinate of the start location
	 * @param sy The y coordinate of the start location
	 * @param tx The x coordinate of the target location
	 * @param ty Teh y coordinate of the target location

         */
    public Path FindPath(int sx, int sy, int tx, int ty) {
        nodes[sx][sy].cost = 0;
        nodes[sx][sy].depth = 0;
        closed.clear();
        open.clear();
        nodes[tx][ty].parent = null;
        open.add(nodes[sx][sy]);
        int maxDepth = 0;
        while ((maxDepth < maxSearchDistance) && (open.size() != 0)) {
            Node current = getFirstInOpen();
            if (MensGevonden) break;
            removeFromOpen(current);
            addToClosed(current);
            for (int x = -1; x < 2; x++) {
                for (int y = -1; y < 2; y++) {
                    if ((x == 0) && (y == 0)) continue;
                    if (!allowDiagMovement) if ((x != 0) && (y != 0)) continue;
                    int xp = x + current.x;
                    int yp = y + current.y;
                    if (isValidLocation(xp, yp)) {
                        int nextStepCost = current.cost + getMovementCost(current.x, current.y, xp, yp);
                        Node neighbour = nodes[xp][yp];
                        if (nextStepCost < neighbour.cost) {
                            if (inOpenList(neighbour)) {
                                removeFromOpen(neighbour);
                            }
                            if (inClosedList(neighbour)) {
                                removeFromClosed(neighbour);
                            }
                        }
                        if (!inOpenList(neighbour) && !(inClosedList(neighbour))) {
                            neighbour.cost = nextStepCost;
                            neighbour.heuristic = getHeuristics(xp, yp, tx, ty);
                            maxDepth = Math.max(maxDepth, neighbour.setParent(current));
                            addToOpen(neighbour);
                        }
                    }
                }
            }
        }
        if (nodes[tx][ty].parent == null) return null;
        Path path = new Path();
        Node target = nodes[tx][ty];
        while (target != nodes[sx][sy]) {
            path.prependStep(target.x, target.y);
            target = target.parent;
        }
        path.prependStep(sx, sy);
        return path;
    }

    protected int getHeuristics(int currentX, int currentY, int targetX, int targetY) {
        int H = 0;
        H = 10 * (Math.abs(currentX - targetX) + Math.abs(currentY - targetY));
        return H;
    }

    protected int getMovementCost(int startX, int startY, int nextX, int nextY) {
        if (startX == nextX) return 10;
        if (startY == nextY) return 10;
        return 14;
    }

    protected boolean isValidLocation(int x, int y) {
        Coordinaten coordinaat = new Coordinaten(x, y);
        if (!isBinnenSpeelveld(x, y)) return false;
        if (speelveld.isLocatieBezet(coordinaat)) {
            if (speelveld.isMens(coordinaat)) {
                MensGevonden = true;
                return true;
            }
            return false;
        }
        return true;
    }

    public boolean isBinnenSpeelveld(int x, int y) {
        if (x < 0) return false;
        if (y < 0) return false;
        if (x > this.Width - 1) return false;
        if (y > this.Height - 1) return false;
        return true;
    }

    public boolean isIngesloten(Coordinaten Coordinaat) {
        boolean isIngesloten = true;
        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                if ((x != 0) && (y != 0)) continue;
                if ((x == 0) && (y == 0)) continue;
                int xp = x + Coordinaat.getXCoordinaat();
                int yp = y + Coordinaat.getYCoordinaat();
                if (isBinnenSpeelveld(xp, yp)) if (!speelveld.isBlok(new Coordinaten(xp, yp))) return false;
            }
        }
        return isIngesloten;
    }

    /**
	 * Get the first element from the open list. This is the next
	 * one to be searched.
	 * 
	 * @return The first element in the open list
	 */
    protected Node getFirstInOpen() {
        return (Node) open.first();
    }

    /**
	 * Add a node to the open list
	 * 
	 * @param node The node to be added to the open list
	 */
    protected void addToOpen(Node node) {
        open.add(node);
    }

    /**
	 * Check if a node is in the open list
	 * 
	 * @param node The node to check for
	 * @return True if the node given is in the open list
	 */
    protected boolean inOpenList(Node node) {
        return open.contains(node);
    }

    /**
	 * Remove a node from the open list
	 * 
	 * @param node The node to remove from the open list
	 */
    protected void removeFromOpen(Node node) {
        open.remove(node);
    }

    /**
	 * Add a node to the closed list
	 * 
	 * @param node The node to add to the closed list
	 */
    protected void addToClosed(Node node) {
        closed.add(node);
    }

    /**
	 * Check if the node supplied is in the closed list
	 * 
	 * @param node The node to search for
	 * @return True if the node specified is in the closed list
	 */
    protected boolean inClosedList(Node node) {
        return closed.contains(node);
    }

    /**
	 * Remove a node from the closed list
	 * 
	 * @param node The node to remove from the closed list
	 */
    protected void removeFromClosed(Node node) {
        closed.remove(node);
    }

    /**
	 * A simple sorted list
	 *
	 * @author kevin
	 */
    private class SortedList {

        /** The list of elements */
        private ArrayList list = new ArrayList();

        /**
		 * Retrieve the first element from the list
		 *  
		 * @return The first element from the list
		 */
        public Object first() {
            return list.get(0);
        }

        /**
		 * Empty the list
		 */
        public void clear() {
            list.clear();
        }

        /**
		 * Add an element to the list - causes sorting
		 * 
		 * @param o The element to add
		 */
        public void add(Object o) {
            list.add(o);
            Collections.sort(list);
        }

        /**
		 * Remove an element from the list
		 * 
		 * @param o The element to remove
		 */
        public void remove(Object o) {
            list.remove(o);
        }

        /**
		 * Get the number of elements in the list
		 * 
		 * @return The number of element in the list
 		 */
        public int size() {
            return list.size();
        }

        /**
		 * Check if an element is in the list
		 * 
		 * @param o The element to search for
		 * @return True if the element is in the list
		 */
        public boolean contains(Object o) {
            return list.contains(o);
        }
    }

    /**
	 * A single node in the search graph
	 */
    private class Node implements Comparable {

        /** The x coordinate of the node */
        private int x;

        /** The y coordinate of the node */
        private int y;

        /** The path cost for this node */
        private int cost;

        /** The parent of this node, how we reached it in the search */
        private Node parent;

        /** The heuristic cost of this node */
        private int heuristic;

        /** The search depth of this node */
        private int depth;

        private int Step;

        /**
		 * Create a new node
		 * 
		 * @param x The x coordinate of the node
		 * @param y The y coordinate of the node
		 */
        public Node(int x, int y) {
            this.x = x;
            this.y = y;
        }

        /**
		 * Set the parent of this node
		 * 
		 * @param parent The parent node which lead us to this node
		 * @return The depth we have no reached in searching
		 */
        public int setParent(Node parent) {
            depth = parent.depth + 1;
            this.parent = parent;
            return depth;
        }

        public void setStep(int Position) {
            this.Step = Position;
        }

        /**
		 * @see Comparable#compareTo(Object)
		 */
        public int compareTo(Object other) {
            Node o = (Node) other;
            float f = heuristic + cost;
            float of = o.heuristic + o.cost;
            if (f < of) {
                return -1;
            } else if (f > of) {
                return 1;
            } else {
                return 0;
            }
        }
    }
}
