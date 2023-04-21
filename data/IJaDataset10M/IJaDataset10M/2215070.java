package seevolution.trees;

import java.awt.*;
import java.util.*;
import seevolution.*;
import seevolution.trees.phylogeny.*;

/**
 * This class encapsulates a generic tree that can be represented in a TreePanel. Apart from the tree structure, this class includes several methods
 * used in the graphical representation.
 * @author Andres Esteban Marcos
 * @version 1.0
 */
public class Tree {

    private TreeNode root;

    private TreeNode origin, destination, highlightedNode;

    private TreePosition marker, position, lastPosition;

    private TreeListener listener;

    private LinkedList<TreeEvent> path;

    private LinkedList<TreePosition> nodePath;

    private int currentNode;

    private int currentEvent;

    private static final int MAX_PATH_LENGTH = 20;

    /**
	 * Creates a new empty tree
	 */
    public Tree() {
        origin = destination = null;
        marker = position = null;
        listener = null;
    }

    /**
	 * Creates a new tree with the designated root
	 * @param root The node that will act as the root of this tree
	 */
    public Tree(TreeNode root) {
        this();
        this.root = root;
    }

    /**
	 * Returns the length of the longest branch of the tree
	 * @return The length of the tree
	 */
    public int getLength() {
        return root.getLength();
    }

    /**
	 * Returns the current origin of the path
	 * @return The origin
	 */
    public TreeNode getOrigin() {
        return origin;
    }

    /**
	 * Returns the event path from the origin to the destination
	 * @return An ordered list containing all the events from the origin to the destination
	 */
    public LinkedList<TreeEvent> getPath() {
        return path;
    }

    /**
	 * Returns the root of the tree
	 * @return
	 */
    public TreeNode getRoot() {
        return root;
    }

    /**
	 * Changes the destination of the path. If the new destination is different to the old one, the path is computed again
	 * @param node The new destination
	 */
    public void setDestination(TreeNode node) {
        if (destination != node) {
            destination = node;
            computePath();
        }
    }

    /**
	 * Sets the destination as the currently highlighted node.
	 * @param x The x ccordinate of the mouse
	 * @param y The y coordinate of the mouse 
	 */
    public void setDestination(int x, int y) {
        if (highlightedNode != null) {
            destination = highlightedNode;
            computePath();
        }
    }

    /**
	 * Sets a new TreeListener, which will be notified when certain events take place
	 * @param listener The new listener
	 */
    public void setListener(TreeListener listener) {
        this.listener = listener;
    }

    /**
	 * Displays a marker at position (x,y) in the TreePanel, given that it lays on the tree
	 * @param x The x coordinate in the TreePanel
	 * @param y The y coordinate in the TreePanel
	 */
    public void setMarker(int x, int y) {
        marker = root.getPosition(x, y);
        if (marker != null && marker.position == -1) {
            highlightedNode = marker.node;
            marker = null;
        } else {
            highlightedNode = null;
        }
    }

    /**
	 * Changes the origin of the path. If the new origin is different to the old one, the path is computed again
	 * @param node The new destination
	 */
    public void setOrigin(TreeNode node) {
        if (origin != node) {
            origin = node;
            computePath();
        }
    }

    /**
	 * Sets the origin as the currently highlighted node.
	 * @param x The x ccordinate of the mouse
	 * @param y The y coordinate of the mouse 
	 */
    public void setOrigin(int x, int y) {
        if (highlightedNode != null) {
            origin = highlightedNode;
            computePath();
        }
    }

    public void randomLeafOriginAndDestination() {
        if (root == null) return;
        Vector<TreeNode> leaves = new Vector<TreeNode>();
        Stack<TreeNode> s = new Stack<TreeNode>();
        s.push(root);
        while (s.size() > 0) {
            TreeNode t = s.pop();
            for (int i = 0; i < t.getChildrenCount(); i++) s.push(t.getChild(i));
            if (t.getChildrenCount() == 0) leaves.add(t);
        }
        if (leaves.size() == 1) {
            origin = root;
            destination = leaves.firstElement();
        } else if (leaves.size() > 1) {
            Random r = new Random();
            int i = r.nextInt(leaves.size());
            int j = i;
            while (j == i) j = r.nextInt(leaves.size());
            origin = leaves.elementAt(i);
            destination = leaves.elementAt(j);
        }
        computePath();
    }

    /**
	 * Moves the position marker to represent a change in the position along the path
	 * @param eventNumber
	 */
    private void setPosition(int eventNumber) {
        currentEvent = eventNumber;
        position = indexToPosition(eventNumber);
    }

    /**
	 * Sets the position marker at the point clicked on the TreePanel
	 * @param x The x coordinate on the TreePanel
	 * @param y The y coordinate on the TreePanel
	 */
    public void setPosition(int x, int y) {
        TreePosition newPosition = root.getPosition(x, y);
        if (newPosition != null && newPosition.position == -1) newPosition = null;
        if (newPosition == null) return;
        int index = positionToIndex(newPosition);
        if (index != -1) {
            boolean forward = index >= currentEvent;
        } else {
            System.out.println("The new position is NOT on the path");
            if (origin != null && newPosition.node.containsNode(origin)) destination = newPosition.node.getParent(); else destination = newPosition.node;
            computePath();
            index = positionToIndex(newPosition);
        }
        if (listener != null) {
            TreeEvent missedEvents[] = new TreeEvent[index];
            for (int i = 0; i < missedEvents.length; i++) missedEvents[i] = path.get(i);
            listener.positionChange(missedEvents, false);
        }
        setPosition(index);
    }

    /**
	 * Changes the root node
	 * @param root The new node
	 */
    public void setRoot(TreeNode root) {
        this.root = root;
    }

    /**
	 * Calculates the list of events present between the origin and the destination
	 * @return The new path
	 */
    public LinkedList<TreeEvent> computePath() {
        if (origin == null || destination == null || origin == destination) return null;
        TreeNode temp = origin;
        LinkedList<TreeNode> upPath = null, downPath = null;
        upPath = new LinkedList();
        while (temp != null) {
            if (temp == destination) break; else if ((downPath = temp.getPath(destination)) != null) {
                break;
            } else if (temp.getParent() != null) {
                upPath.addLast(temp);
                temp = temp.getParent();
            } else break;
        }
        LinkedList<TreeEvent> eventList = new LinkedList();
        LinkedList<TreePosition> nodeList = new LinkedList();
        while ((temp = (TreeNode) upPath.poll()) != null) {
            TreeEvent events[] = temp.getEvents();
            nodeList.addLast(new TreePosition(temp, true));
            for (int i = events.length - 1; i >= 0; i--) eventList.addLast(events[i].reverse());
        }
        if (downPath != null) {
            while ((temp = downPath.poll()) != null) {
                nodeList.addLast(new TreePosition(temp, false));
                TreeEvent events[] = temp.getEvents();
                if (events != null) for (int i = 0; i < events.length; i++) eventList.addLast(events[i]);
            }
        }
        path = eventList;
        nodePath = nodeList;
        setPosition(0);
        if (listener != null) listener.pathChange(origin, path);
        return eventList;
    }

    /**
	 * Indicates whether there are more events on the current path
	 * @return True if there are more events
	 */
    public boolean hasMoreEvents() {
        boolean more = path != null && currentEvent < path.size();
        if (!more && path != null) setPosition(path.size() + 1);
        return more;
    }

    /**
	 * Uses the provided Graphics2D to paint a highlight around the currently highlighted node. The color is whichever is active in the graphics
	 * @param g
	 */
    public void highlightNode(Graphics2D g) {
        if (highlightedNode != null) highlightedNode.highlightNode(g);
    }

    /**
	 * Converts a position in the event path to a position in the Tree
	 * @param eventNumber The index in the event path
	 * @return The position corresponding to the event number
	 */
    private TreePosition indexToPosition(int eventNumber) {
        if (path == null) return null;
        TreeNode tempNode = destination;
        boolean up = nodePath.get(nodePath.size() - 1).up;
        if (eventNumber < path.size()) {
            int tempEventIndex = 0;
            for (int nodeIndex = 0; nodeIndex < nodePath.size(); nodeIndex++) {
                tempNode = nodePath.get(nodeIndex).node;
                up = nodePath.get(nodeIndex).up;
                tempEventIndex += tempNode.getEvents().length;
                if (tempEventIndex > eventNumber) {
                    int soFar = tempEventIndex - tempNode.getEvents().length;
                    int newInd = eventNumber - soFar;
                    if (up) newInd = tempNode.getEvents().length - newInd;
                    return new TreePosition(tempNode, tempNode.indexToPosition(newInd));
                }
            }
        }
        return new TreePosition(tempNode, 1f);
    }

    /**
	 * Returns the next event in the event path from the origin to the destination
	 * @return The next event
	 */
    public TreeEvent nextEvent() {
        if (path != null) {
            setPosition(currentEvent);
            return path.get(currentEvent++);
        }
        return null;
    }

    /**
	 * Paints this tree using the Graphics object provided
	 * @param left The left margin
	 * @param right The right margin
	 * @param top The top margin
	 * @param scale The scale used to convert the tree length to pixel length
	 * @param g The graphics object on which the tree is painted
	 */
    public void paint(int left, int right, int top, float scale, Graphics g) {
        if (root != null) root.paint(left, right, (left + right) / 2, top, scale, g);
    }

    /**
	 * Paints the circle that identifies the destination on the provided graphics
	 * @param g The graphics object on which the destination is painted
	 */
    public void paintDestination(Graphics2D g) {
        if (destination != null) destination.highlightNode(g);
    }

    /**
	 * Repaints the last position on the event path. Generally used to erase it by painting with a transparent color.
	 * @param g The graphics object on which the last position is painted.
	 */
    public void paintLastPosition(Graphics2D g) {
        if (lastPosition != null) lastPosition.node.paintPosition(lastPosition.position, g);
    }

    /**
	 * Paints the marker that identifies the position of the mouse on the tree
	 * @param g The graphics object on which the marker is painted
	 */
    public void paintMarker(Graphics2D g) {
        if (marker != null) marker.node.paintMarker(marker.position, g);
    }

    /**
	 * Paints the circle that identifies the origin on the provided graphics
	 * @param g The graphics object on which the destination is painted
	 */
    public void paintOrigin(Graphics2D g) {
        if (origin != null) origin.highlightNode(g);
    }

    /**
	 * Paints the rectangle that represents the current position on the event path
	 * @param g The graphics object on which the position is painted.
	 */
    public void paintPosition(Graphics2D g) {
        lastPosition = position;
        if (position != null) position.node.paintPosition(position.position, g);
    }

    /**
	 *Converts a position on the tree to an index on the event path
	 * @param pos The position on the tree
	 * @return The index on the path
	 */
    private int positionToIndex(TreePosition pos) {
        if (path == null) return -1;
        boolean included = false;
        int acc = 0;
        TreeNode node = null;
        boolean up = false;
        for (int i = 0; i < nodePath.size(); i++) {
            node = nodePath.get(i).node;
            if (node == pos.node) {
                included = true;
                up = nodePath.get(i).up;
                break;
            }
            acc += node.getEvents().length;
        }
        if (!included) return -1;
        int res = node.positionToIndex(pos.position);
        if (up) res = node.getEvents().length - res - 1;
        return acc + res;
    }
}
