package model.storage;

import java.util.*;
import model.storage.Artifact;
import java.awt.Color;
import java.util.StringTokenizer;

public class Group {

    public static enum groupType {

        userDefined, deepSearch, initialSearch, importSearch, systemGroup
    }

    ;

    private groupType type;

    private String name = "";

    private Color colour;

    private String dynamicChildren;

    /** Stores a reference to the parent Group in the tree */
    private Group parent = null;

    /** Stores the artifacts that belong directly to this group, along with
	 * a unique identifying key */
    private HashMap<String, Artifact> artifacts = null;

    /** Stores the Group objects that are children of this Group */
    private ArrayList<Group> children = null;

    /** empty constructor required for the Serializable interface. */
    public Group() {
    }

    /** Instantiates the Group, with the <i>p</i> parameter specifying
	 * a reference to this Group's parent node. Then adds this Group to the 
	 * parent's children collection.
	 * @param name
	 * @param p
	 */
    public Group(String name, Group p) {
        artifacts = new HashMap<String, Artifact>();
        this.name = name;
        this.parent = p;
        this.type = groupType.userDefined;
        if (parent != null) {
            parent.addChild(this);
        }
    }

    /**
	 * Set the type of the group
	 * @param type The group type
	 */
    public void setType(groupType type) {
        this.type = type;
    }

    /**
	 * Get the type of the group
	 * @return The type of the group
	 */
    public groupType getType() {
        return this.type;
    }

    /** Adds an Artifact to this Group's collection */
    public void addArtifact(Artifact a) {
        artifacts.put(a.getKey(), a);
        a.addGroup(this);
    }

    /** Adds the specified Group as a child to this node */
    public void addChild(Group g) {
        if (children == null) {
            children = new ArrayList<Group>();
        }
        if (children.contains(g) == false) {
            children.add(g);
        }
    }

    /** Returns TRUE if the current Group node contains the specified Artifact <i>a</i>
	 * as part of its Artifact collection, else FALSE.
	 * @param a
	 * @return
	 */
    public boolean containsArtifact(Artifact a) {
        return artifacts.containsKey(a.getKey());
    }

    /** Searches the the current node and all descendants for a match on the specified
	 * String <i>name</i>.
	 * @param name
	 * @return
	 */
    public boolean containsGroup(String name) {
        boolean result = false;
        if (this.name.equalsIgnoreCase(name)) {
            result = true;
        }
        if (this.isLeaf()) {
            return result;
        }
        for (int i = 0; i < children.size(); i++) {
            result = result || children.get(i).containsGroup(name);
        }
        return result;
    }

    /** Searches the the current node and all descendants for a match on the specified
	 * Group <i>g</i>.
	 * @param g
	 * @return
	 */
    public boolean containsGroup(Group g) {
        boolean result = false;
        if (this == g) {
            result = true;
        }
        if (this.isLeaf()) {
            return result;
        }
        for (int i = 0; i < children.size(); i++) {
            result = result || children.get(i).containsGroup(g);
        }
        return result;
    }

    /** 
	 * Searches through the specified Group node, and all descendant nodes of the specified Group node, checking each descendant's
	 * Artifact collection for matches to the specified Artifact <i>a</i>. If such a match is
	 * found, the Group node location for that Artifact is appended to the returned list.
	 * 
	 * @param a
	 * @return
	 */
    public ArrayList<Group> descendantsContainArtifact(Artifact a) {
        ArrayList<Group> result = new ArrayList<Group>();
        if (this.containsArtifact(a)) {
            result.add(this);
        }
        if (this.isLeaf()) {
            return result;
        }
        for (int i = 0; i < children.size(); i++) {
            result.addAll(children.get(i).descendantsContainArtifact(a));
        }
        return result;
    }

    /** This method returns a list of Artifact objects contained within all
	 * child Groups of this node.
	 * 
	 * <ul>
	 *  <li>If this node is a leaf, just return its collection of Artifacts.</li>
	 *  <li>Else, recursively return the result on all children nodes.</li>
	 * </ul>
	 * 
	 * @return
	 */
    public synchronized HashMap<String, Artifact> getAllChildArtifacts() {
        HashMap<String, Artifact> result = new HashMap<String, Artifact>();
        if (this.isLeaf()) {
            return this.artifacts;
        } else {
            result.putAll(this.artifacts);
            for (int i = 0; i < children.size(); i++) {
                result.putAll(children.get(i).getAllChildArtifacts());
            }
            return result;
        }
    }

    /** 
	 * 
	 * @return The collection of Artifacts at this Group node.
	 */
    public HashMap<String, Artifact> getArtifacts() {
        return this.artifacts;
    }

    /**
	 * Returns this Group's children collection.
	 * @return
	 */
    public ArrayList<Group> getChildren() {
        return this.children;
    }

    /** Returns this Group's colour attribute */
    public Color getColour() {
        return this.colour;
    }

    /**
	 * Returns a string literal representation of this 
	 * Group's colour attribute. The format is
	 * 
	 * R,G,B
	 * 
	 * @return
	 */
    public String getColourAsRGB() {
        String result = "";
        if (colour != null) {
            result += Integer.toString(colour.getRed()) + "," + Integer.toString(colour.getGreen()) + "," + Integer.toString(colour.getBlue());
        }
        return result;
    }

    /**
	 * Returns the Group specified by the String <i>name</i>.
	 * @param name
	 * @return Group object if found, else empty Group
	 */
    public Group getGroup(String name) {
        ArrayList<Group> result = getGroups(name);
        for (int i = 0; i < result.size(); i++) {
            return result.get(i);
        }
        return null;
    }

    /**
	 * Returns a list of Groups that match the given name.
	 * Since it is expected that Group names are unique, this method
	 * should only ever return either NULL, or an ArrayList of size 1.
	 * @param name
	 * @return
	 */
    private ArrayList<Group> getGroups(String name) {
        ArrayList<Group> result = new ArrayList<Group>();
        if (this.name.equalsIgnoreCase(name)) {
            result.add(this);
        }
        if (this.isLeaf()) {
            return result;
        }
        for (int i = 0; i < children.size(); i++) {
            result.addAll(children.get(i).getGroups(name));
        }
        return result;
    }

    public String getName() {
        return this.name;
    }

    /** Returns the parent node of this Group */
    public Group getParent() {
        return this.parent;
    }

    /** Returns an ArrayList of Group objects which contains the set of nodes traversed to
	 * reach the current Group node. This method is used to determine what groups a particular
	 * Artifact belongs to.
	 * @return NULL if the Group node is actually the root, otherwise, an ArrayList containing 
	 * all Groups that are ancestors of the current node (including the node itself).
	 */
    public ArrayList<Group> getPath() {
        ArrayList<Group> result = new ArrayList<Group>();
        if (this.parent == null) {
            return result;
        }
        result.add(this);
        result.addAll(this.parent.getPath());
        return result;
    }

    /**
	 * Returns the status of whether this Group has its colour attribute set or not.
	 * @return
	 */
    public boolean hasColour() {
        return (this.colour != null);
    }

    /** Checks whether the specified Group <i>g</i> is a descendant of this node. */
    public boolean isAncestorOf(Group g) {
        if (this.parent == null) {
            return true;
        }
        if (g.getPath().contains(this)) {
            return true;
        } else {
            return false;
        }
    }

    /** Tests whether this Group is a leaf in the tree */
    public boolean isLeaf() {
        if (children == null) {
            return true;
        } else {
            return false;
        }
    }

    /** Removes the specified Artifact from this Group's collection. */
    public void removeArtifact(Artifact a) {
        if (this.containsArtifact(a)) {
            this.artifacts.remove(a.getKey());
            a.deleteGroup(this);
        }
    }

    /** Removes the specified Group <i>g</i> from this node's children. */
    public void removeChild(Group g) {
        if (g != null && !this.isLeaf()) {
            this.children.remove(g);
        }
    }

    /** Sets the colour attribute for this Group */
    public void setColour(Color colour) {
        this.colour = colour;
    }

    /** Sets the colour attribute for this Group
	 * using a R,G,B string literal representation.
	 * @param rgb
	 */
    public void setColourAsRGB(String rgb) {
        StringTokenizer tokenizer = new StringTokenizer(rgb, ",");
        int r = 0, g = 0, b = 0;
        while (tokenizer.hasMoreTokens()) {
            r = Integer.parseInt(tokenizer.nextToken());
            g = Integer.parseInt(tokenizer.nextToken());
            b = Integer.parseInt(tokenizer.nextToken());
        }
        this.colour = new Color(r, g, b);
    }

    /** Sets the name for this Group */
    public void setName(String name) {
        this.name = name;
    }

    /** Sets the parent for this Group */
    public void setParent(Group parent) {
        this.parent = parent;
    }

    /**
	 * Get the Dynamic groups children string
	 * @return Dynamic groups children text
	 */
    public String getDynamicChildren() {
        return this.dynamicChildren;
    }

    /**
	 * Set the Dynamic groups children string
	 * @param dynamicChildren The dynamic groups children
	 */
    public void setDynamicChildren(String dynamicChildren) {
        this.dynamicChildren = dynamicChildren;
    }

    /** Ignore this method for now, it's supposed to  show the tree in STDOUT but 
	 * I haven't found a nice way to represent it yet. 
	 */
    public String toString() {
        return this.getName();
    }
}
