package ecosim;

import java.util.ArrayList;

/**
 *  A Newick tree node potentially contains a name, a parent node,
 *  a distance from the parent node, and a list of all child nodes.
 *
 *  @author Andrew Warner & Jason Wood
 */
public class NewickTreeNode {

    /**
     *  Constructor for objects of class NewickTreeNode.
     *
     *  @param name The name of this treenode.
     *  @param distance The distance from the parent node.
     *  @param parent The parent NewickTreeNode of this node.
     *  @param children A list of NewickTreeNodes which are the children of this node.
     */
    public NewickTreeNode(String name, double distance, NewickTreeNode parent, ArrayList<NewickTreeNode> children) {
        this.name = name;
        this.distance = distance;
        this.parent = parent;
        this.children = children;
    }

    /**
     *  Default constructor for object of class NewickTreeNode.  This node
     *  will have no name, distance, parent, or children unless defined later.
     */
    public NewickTreeNode() {
        this("", 0.0, null, new ArrayList<NewickTreeNode>());
    }

    /**
     *  Set the name of this node.
     *
     *  @param name The name of this node.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *  Set the distance from the parent node to this node.
     *
     *  @param distance The distance to the parent.
     */
    public void setDistance(double distance) {
        this.distance = distance;
    }

    /**
     *  Set the parent of this node.
     *
     *  @param parent The parent of this node.
     */
    public void setParent(NewickTreeNode parent) {
        this.parent = parent;
    }

    /**
     *  Add a child to this node.
     *
     *  @param child The child to add.
     */
    public void addChild(NewickTreeNode child) {
        child.setParent(this);
        children.add(child);
    }

    /**
     *  Returns the name of this node.
     *
     *  @return String containing the name of this node.
     */
    public String getName() {
        return name;
    }

    /**
     *  Get the distance from the parent node to this node.
     *
     *  @return double containing the distance to the parent.
     */
    public double getDistance() {
        return distance;
    }

    /**
     *  Returns the parent of this node.
     *
     *  @return NewickTreeNode containing the parent of this node.
     */
    public NewickTreeNode getParent() {
        return parent;
    }

    /**
     *  Returns the children of this node.
     *
     *  @return ArrayList<NewickTreeNode> containing the children of this node.
     */
    public ArrayList<NewickTreeNode> getChildren() {
        return children;
    }

    /**
     *  Returns a boolean stating whether this node is a leaf or not.
     *
     *  @return A boolean stating whether this node is a leaf or not.
     */
    public boolean isLeafNode() {
        return children.isEmpty();
    }

    /**
     *  Returns the distance of this node from the root node.
     *
     *  @return The distance of this node from the root node.
     */
    public double distanceFromRootNode() {
        double distanceFromRoot = 0.0;
        if (parent != null) {
            distanceFromRoot = distance + parent.distanceFromRootNode();
        }
        return distanceFromRoot;
    }

    /**
     *  Returns whether this node is the root node or not.
     *
     *  @return True if this node is the root node.
     */
    public boolean isRootNode() {
        return parent == null;
    }

    /**
     *  Returns an array of nodes that are descendants of this node.
     *
     *  @return The descendants of this node.
     */
    public ArrayList<NewickTreeNode> getDescendants() {
        ArrayList<NewickTreeNode> descendants = new ArrayList<NewickTreeNode>();
        if (children.size() > 0) {
            for (int i = 0; i < children.size(); i++) {
                NewickTreeNode child = children.get(i);
                if (child.isLeafNode()) {
                    descendants.add(child);
                } else {
                    descendants.addAll(child.getDescendants());
                }
            }
        }
        return descendants;
    }

    /**
     *  Returns the number of living descendants of this node.
     *
     *  @return The number of living descendants of this node.
     */
    public int numberOfDescendants() {
        int descendants = 0;
        if (children.size() > 0) {
            for (int i = 0; i < children.size(); i++) {
                if (children.get(i).isLeafNode()) {
                    descendants++;
                } else {
                    descendants += children.get(i).numberOfDescendants();
                }
            }
        } else {
            descendants = 1;
        }
        return descendants;
    }

    /**
     *  Returns this node formatted as a String.
     *
     *  @return String containing the name and distance of this node.
     */
    public String toString() {
        String newick = "";
        for (int i = 0; i < children.size(); i++) {
            if (i != 0) {
                newick += ",";
            }
            newick += children.get(i);
        }
        if (newick.length() > 0) {
            newick = "(" + newick + ")";
        }
        newick += name + ":" + distance;
        if (parent == null) {
            newick += ";";
        }
        return newick;
    }

    /**
     *  The name of this node.
     */
    private String name;

    /**
     *  The distance of this node from it's parent.
     */
    private double distance;

    /**
     *  The parent of this node.
     */
    private NewickTreeNode parent;

    /**
     *  A list of children of this node.
     */
    private ArrayList<NewickTreeNode> children;
}
