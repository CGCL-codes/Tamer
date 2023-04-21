package map;

import java.util.Vector;

/**
 * The <code>DefaultNode</code> class represents an internal map node. Each
 * cell in the map consists of a <code>DefaultNode</code>.
 */
public class DefaultNode implements Node {

    /** The bitmask associated with this node. */
    public long bits;

    /** The parent of this node. */
    protected Node parent;

    /** The child nodes of this node. */
    protected Vector children;

    /** The cost associated with this node. */
    protected float cost;

    /** The total cost associate with this node. */
    protected float total;

    /** The score associated with this node. */
    protected float score;

    /** The current state of this node. */
    protected int state;

    /** The horizontal component of this node. */
    protected float x;

    /** The vertical component of this node. */
    protected float y;

    /** The z coordinate of this node. */
    protected float z;

    /** Specifies whether this node is currently visible. */
    protected float visibility;

    /**
     * Creates a new <code>DefaultNode</code>.
     */
    public DefaultNode() {
        this(null);
    }

    /**
     * Creates a new <code>DefaultNode</code> with the specified
     * parent node. The node is initially positionend at (0,0) and
     * is not solid.
     *
     * @param parent The parent of this node.
     */
    public DefaultNode(Node parent) {
        this(null, 0, 0, 0);
    }

    /**
     * Creates a new <code>DefaultNode</code> with the specified
     * parent and attributes.
     *
     * @param parent The parent of this node.
     * @param bits The flags associated with this node.
     * @param x The horizontal component of this node.
     * @param y The vertical component of this node.
     */
    public DefaultNode(Node parent, long bits, int x, int y) {
        this.parent = parent;
        this.bits = bits;
        this.x = x;
        this.y = y;
        this.state = UNSET;
        this.total = score = cost = 0;
        children = new Vector();
    }

    /**
     * Adds a child node to this node.
     * @param n The node to add.
     */
    public void addChildNode(Node n) {
        children.add(n);
    }

    /**
     * Sets a specific child node given by index to the Node 
     * given by n.
     *
     * @param index The index of the child to change.
     * @param n The node to set in the place of index.
     */
    public void setChildNode(int index, Node node) {
        children.set(index, node);
    }

    /**
     * Returns the child at the specified location <code>index</code>.
     * @return The child at the specified location <code>index</code>.
     */
    public Node getChildNode(int index) {
        return (Node) children.get(index);
    }

    /**
     * Returns the children of this <code>Node</code>.
     * @return The children of this <code>Node</code>.
     */
    public Node[] getAllChildren() {
        Node[] temp = new Node[children.size()];
        children.copyInto(temp);
        return temp;
    }

    /**
     * Sets the parent of this node to the specified node.
     * @param n The new parent node.
     */
    public void setParent(Node n) {
        parent = n;
    }

    /**
     * Returns this nodes parent.
     * @return this nodes parent.
     */
    public Node getParent() {
        return parent;
    }

    /**
     * Sets the positional coordinates of this node to the
     * specified values.
     *
     * @param x The horizontal component.
     * @param y The vertical component.
     */
    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setLocation(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Returns the horizontal component of this node.
     * @return the horizontal component of this node.
     */
    public float getX() {
        return x;
    }

    /**
     * Returns the vertical component of this node.
     * @return the vettical component of this node.
     */
    public float getY() {
        return y;
    }

    /**
     * Returns the z coordinate of this node.
     * @return the z coordinate of this node.
     */
    public float getZ() {
        return z;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setZ(float z) {
        this.z = z;
    }

    /**
     * Sets the cost associated with entering this node.
     * @param cost the cost associated with entering this node.
     */
    public void setCost(float cost) {
        this.cost = cost;
    }

    /**
     * Returns the cost associated with entering this node.
     * @return The cost associated with entering this node.
     */
    public float getCost() {
        return cost;
    }

    /**
     * Sets the total cost associated with this node. The
     * total cost is the cost of travelling from the start
     * node to this node.
     *
     * @param total The total cost to be associated with
     *              this node.
     */
    public void setTotal(float total) {
        this.total = total;
    }

    /**
     * Returns the total cost associated with this node.
     * @return The total cost associated with this node.
     */
    public float getTotal() {
        return total;
    }

    /**
     * Sets the score associated with this node. The score 
     * determines how good this node is in relation to other
     * nodes and is used for ordering them amongst each other.
     *
     * @param score The score to be associated with this node.
     */
    public void setScore(float score) {
        this.score = score;
    }

    /**
     * Returns the score associated with this node.
     * @return the score associated with this node.
     */
    public float getScore() {
        return score;
    }

    /**
     * Specifies whether this node has already been processed 
     * and no longer is considered for further processing.
     *
     * @param b A boolean value that specifies whether
     *          this node has been processed.
     */
    public void setClosed(boolean b) {
        state = b ? state | CLOSED : state & ~CLOSED;
    }

    /**
     * Returns true if this node has been processed.
     * @return True if this node has been processed.
     */
    public boolean isClosed() {
        return (state & CLOSED) > 0;
    }

    /**
     * Specifies whether this node is currently being
     * considered for further processing.
     *
     * @param b A boolean value that specifies whether
     *          this node is currently open.
     */
    public void setOpen(boolean b) {
        state = b ? state | OPEN : state & ~OPEN;
    }

    /**
     * Returns true if this node is currently being 
     * considered for further processing.
     *
     * @return True if this node is open.
     */
    public boolean isOpen() {
        return (state & OPEN) > 0;
    }

    /**
     * Sets the data associated with this node. The handles
     * the way the node is displayed.
     *
     * @param data The data to be associated with this node.
     */
    public void setData(long data) {
        this.bits = data;
    }

    /**
     * Retrieves the data associated with this node.
     * @return the data associated with this node.
     */
    public long getData() {
        return bits;
    }

    /**
     * Specifies whether this node is solid (ie. the
     * cost of entering the node is infinite.)
     *
     * @param b A boolean value that specifies whether
     *          this node is solid.
     */
    public void setSolid(boolean b) {
        throw new RuntimeException("Method not implemented!");
    }

    /**
     * Returns whether this node is considered solid (ie. the
     * cost of entering the node is infinite.)
     *
     * @return True if this node is solid.
     */
    public boolean isSolid() {
        throw new RuntimeException("Method not implemented!");
    }

    /**
     * Specifies whether this node is currently visible.
     *
     * @param b A boolean value that specifies whether
     *          this node is currently visible.
     */
    public void setVisible(boolean b) {
        this.visibility = b ? 1.0f : 0.0f;
    }

    /**
     * Returns whether this node is considered visible.
     * @return True if this node is visible.
     */
    public boolean isVisible() {
        return visibility > 0.0f;
    }

    /**
     * Sets the degree of visibility of this node. 
     * @param value the degree of visibility.
     */
    public void setVisibility(float value) {
        this.visibility = value;
    }

    /**
     * Returns the current degree of visibility of this node.
     * @return the current degree of visibility of this node.
     */
    public float getVisibility() {
        return visibility;
    }

    /**
     * Compares this Node with the specified Object for order.
     * Note: This method imposes ordering which is inconsistant
     * with equals.
     *
     * @param obj The object which is to be compared.
     * @return An integer value of -1, 0, or 1, as this node
     *         is less than, equal to, or greater than obj.
     *
     * @throws ClassCastException if obj is not of type Node.
     */
    public int compareTo(Object obj) {
        return compareTo((DefaultNode) obj);
    }

    public int compareTo(DefaultNode n) {
        return score < n.score ? -1 : (score > n.score ? 1 : 0);
    }

    public String toString() {
        return "DefaultNode[x=" + x + ",y=" + y + ",score=" + score + "]";
    }
}
