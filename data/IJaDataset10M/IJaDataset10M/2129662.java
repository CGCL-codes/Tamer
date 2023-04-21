package gpl.scotlandyard.beans.basics;

import gpl.scotlandyard.beans.utils.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Node structure contains a number, a graphical location and a list of {@link Link}.
 * <p>
 * Number number is equal to the number of the case.
 * </p>
 * <p>
 * Location gives the point where is located the case on the board (relative to board left top corner)
 * </p>
 * <p>
 * List of {@link Link} contains all links whith {@link Node} that can be reached from this one.
 * </p>
 * @author Norbert Martin */
public class Node implements Comparable<Node> {

    /** Singleton for factory method getUnknownNode(). */
    private static final Node UNKNOWN = createUnknownNode();

    /** Returns an unknown node with number 0 and location 0,0.
	 * @return unknown node */
    public static Node getUnknownNode() {
        return UNKNOWN;
    }

    /**
	 * @return
	 */
    private static Node createUnknownNode() {
        return new Node(0) {

            @Override
            public String toString() {
                return "?";
            }
        };
    }

    private final List<Link> links = new ArrayList<Link>();

    private final Point location = new Point();

    private final int number;

    /** Creates a new node with this number and no links.
	 * @param number */
    public Node(int position) {
        this.number = position;
    }

    /** Adds a link from this node to n and a link from n to this node.
	 * @param n node to linked
	 * @param t type of link */
    public void addFullLink(Node n, Ticket t) {
        addLink(n, t);
        n.addLink(this, t);
    }

    /** Adds a link the from this node to n.
	 * @param n node to linked
	 * @param t type of link */
    public void addLink(Node n, Ticket t) {
        Link l = new Link(this, n, t);
        links.add(l);
    }

    /** Returns the links of the node (unmodifiable)
	 * @return links */
    public List<Link> getLinks() {
        return Collections.unmodifiableList(links);
    }

    /** Returns location of this node on the screen.
	 * @return location */
    public Point getLocation() {
        return location.clone();
    }

    /** Returns the number of this node.
	 * @return number */
    public int getNumber() {
        return number;
    }

    /** Sets location of this node on the screen.
	 * @param x
	 * @param y */
    public void setLocation(int x, int y) {
        location.move(x, y);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Node) {
            return number == ((Node) obj).number;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return number;
    }

    @Override
    public String toString() {
        return number + "";
    }

    @Override
    public int compareTo(Node o) {
        return number - o.number;
    }
}
