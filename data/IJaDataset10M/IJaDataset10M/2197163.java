package edu.ucsd.ncmir.ontology;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

/**
 * A node in the <code>Ontology</code>.
 *
 * @author Steve Lamont
 * @version prototype
 */
public class OntologyNode implements Comparable<OntologyNode>, Serializable {

    private static final long serialVersionUID = 1L;

    private SerializableHashtable<String, Property> _property_table;

    /**
     * Creates an instance of <code>OntologyNode</code>.
     * @param name the name of this node.
     * @param owl_model a link to the database.
     */
    OntologyNode(String name, String internal_name, SerializableHashtable<String, Property> property_table) {
        this.setName(name);
        this.setInternalName(internal_name);
        this._property_table = property_table;
    }

    /**
     * Creates an instance of <code>OntologyNode</code> with an empty
     * set of name-property pairs for this node.
     * @param name the name of this node.
     */
    public OntologyNode(String name) {
        this.setName(name);
    }

    private String _internal_name = "unknown";

    private void setInternalName(String internal_name) {
        this._internal_name = internal_name;
    }

    public String getInternalName() {
        return this._internal_name;
    }

    private String _name;

    private void setName(String name) {
        this._name = name;
    }

    private String getName() {
        return this._name;
    }

    /**
     * Returns parent of this node.  If this is the top level node,
     * returns <code>null</code>.
     * @return the parent node.
     */
    private OntologyNode _parent = null;

    public OntologyNode getParent() {
        return this._parent;
    }

    private void setParent(OntologyNode parent) {
        this._parent = parent;
    }

    /**
     * Returns an array of one or more children of this node. If this
     * is a leaf node, returns <code>null</code>
     * @return an array of children or <code>null</null> if a leaf node.
     */
    public OntologyNode[] getChildren() {
        OntologyNode[] result = null;
        synchronized (this._children) {
            ArrayList<OntologyNode> list = new ArrayList<OntologyNode>();
            for (OntologyNode on : this._children.values()) if (!on.toString().startsWith("@")) list.add(on);
            result = list.toArray(new OntologyNode[list.size()]);
            Arrays.sort(result);
        }
        return result;
    }

    /**
     * Returns <code>true</code> if the node is a leaf node (has no
     * children) or <code>false</code> if children exist.
     * @return the leafness of the node.
     */
    public boolean isLeaf() {
        boolean leaf;
        synchronized (this._children) {
            leaf = this._children.size() == 0;
        }
        return leaf;
    }

    /**
     * Returns a child node by name.
     * @param name the name of the node.
     * @return the node, if it exists, otherwise <code>null</code>.
     */
    public OntologyNode getChild(String name) {
        OntologyNode node;
        synchronized (this._children) {
            node = this._children.get(name);
        }
        return node;
    }

    private class Children extends SerializableHashtable<String, OntologyNode> {

        private static final long serialVersionUID = 42L;
    }

    private final Children _children = new Children();

    /**
     * Adds a child node to this node.
     * @param ontologyNode the node to be added.  If the node
     * pre-exists, it is replaced.
     * @return returns <code>true</code> if the node pre-exists,
     * otherwise <code>false</code>.
     */
    public boolean addChild(OntologyNode ontologyNode) {
        OntologyNode previous;
        synchronized (this._children) {
            previous = this._children.put(ontologyNode.toString(), ontologyNode);
        }
        ontologyNode.setParent(this);
        return previous != null;
    }

    /**
     * Deletes a child and all its children recursively.
     * @param name the name of the node to be deleted. 
     * @return returns <code>true</code> if the node existed,
     * otherwise <code>false</code>.
     */
    public boolean deleteChild(String name) {
        OntologyNode previous = null;
        OntologyNode delete_node = this.getChild(name);
        if (delete_node != null) synchronized (this._children) {
            OntologyNode[] children = delete_node.getChildren();
            if (children != null) for (int i = 0; i < children.length; i++) delete_node.deleteChild(children[i]);
            previous = this._children.remove(name);
        }
        return previous != null;
    }

    /**
     * Deletes a child and all its children recursively.
     * @param ontologyNode the node to be deleted. 
     * @return returns <code>true</code> if the node existed,
     * otherwise <code>false</code>.
     */
    public boolean deleteChild(OntologyNode ontologyNode) {
        return this.deleteChild(ontologyNode.toString());
    }

    /**
     * Reparents this node, removing it from the current parent's list
     * of children and adding it to the new parent's list.
     * @param newParent the new parent of this node.
     */
    public void reparent(OntologyNode newParent) {
        this._parent._children.remove(this.toString());
        this._parent = null;
        newParent.addChild(this);
    }

    /**
     * Returns a <code>String</code> representation of this node.
     * @return the <code>String</code> representation.
     */
    @Override
    public String toString() {
        return this.getName();
    }

    /**
     * Returns the property specified by the name.
     * @param property_name the name of the property.
     * @return the property value.  Returns <code>null</code> if the
     * property does not exist or is not set.
     */
    public Property getProperty(String property_name) {
        return this._property_table.get(property_name);
    }

    /**
     * Returns an alphabetically organized list of properties.
     *
     * @return the property list;
     */
    public Property[] getProperties() {
        Property[] list;
        if (this._property_table != null) {
            Collection<Property> values = this._property_table.values();
            list = values.toArray(new Property[values.size()]);
            Arrays.sort(list);
        } else list = new Property[0];
        return list;
    }

    private class Restrictions extends ArrayList<String> {

        private static final long serialVersionUID = 42L;
    }

    private SerializableHashtable<String, Restrictions> _restrictions = new SerializableHashtable<String, Restrictions>();

    void addRestriction(String restriction) {
        String[] parts = restriction.replaceAll("[()]*", "").split("[\t ]");
        Restrictions rlist = this._restrictions.get(parts[0]);
        if (rlist == null) rlist = new Restrictions();
        rlist.add(restriction);
        this._restrictions.put(parts[0], rlist);
    }

    public String[] getRestrictionSubjects() {
        Set<String> keys = this._restrictions.keySet();
        String[] list = keys.toArray(new String[keys.size()]);
        Arrays.sort(list);
        return list;
    }

    public String[] getRestrictionList(String restriction) {
        Restrictions rlist = this._restrictions.get(restriction);
        String[] restrictions;
        if (rlist != null) {
            int size = rlist.size();
            restrictions = rlist.toArray(new String[size]);
        } else restrictions = new String[0];
        return restrictions;
    }

    /**
     * Sets the name-property pair.  If no name-property pair exists
     * by the name specified, a pair is created.  Otherwise the pair
     * is created.
     * @param property_name the name of the property.
     * @param property the property.
     */
    public void setProperty(String property_name, Property property) {
    }

    /**
     * Returns an array of property names.
     * @return an array of <code>String</code>.
     */
    public String[] getPropertyNames() {
        Set<String> keys = this._property_table.keySet();
        String[] list = keys.toArray(new String[keys.size()]);
        Arrays.sort(list);
        return list;
    }

    /**
     * Deletes an <code>Property</code> by name.
     * @param name the name of the <code>Property</code>.
     * @return <code>true</code> if the <code>Property</code> existed
     * and was deleted, otherwise returns <code>false</code>.
     */
    public boolean deleteProperty(String name) {
        return true;
    }

    /**
     * Atomically updates a <code>Property</code>.
     * @param property_name the name of the <code>Property</code> to
     * be updated.
     * @param property_updater a subclass of
     * <code>PropertyHandler</code> containing a public
     * <code>handler()</code> method.  This method is called from
     * within a synchronized code block, guaranteeing that no other
     * thread or process, local or remote, can update the
     * <code>Property</code> simultaneous.
     */
    public void updateProperty(String property_name, PropertyHandler property_updater) {
    }

    /**
     * Adds an <code>Instantiation</code> of this <code>OntologyNode</code>.
     * If an <code>Instantiation</code> by that name exists, it will be silently
     * replaced.
     * 
     * 
     * @param instance the instance.
     */
    public void addInstance(Instantiation instance) {
    }

    /**
     * Returns an ordered list of all <code>Instantiation</code>s.
     * 
     * 
     * @return the list of <code>Instances</code>.
     */
    public Instantiation[] getAllInstances() {
        return null;
    }

    /**
     * Returns an <code>Instantiation</code> by name.
     * 
     * 
     * @param name the name of the <code>Instantiation</code>.
     * @return the <code>IExample/code> or <code>null</code> if no
     * such <code>IExample/code> exists.
     */
    public Instantiation getInstance(String name) {
        return null;
    }

    /**
     * Deletes an <code>Instantiation</code> by name.
     * 
     * 
     * @param name the name of the <code>Instantiation</code>.
     * @return <code>true</code> if the <code>IExample/code> existed
     * and was deleted, otherwise returns <code>false</code>.
     */
    public boolean deleteInstance(String name) {
        return false;
    }

    /**
     * Deletes the <code>Instantiation</code>.
     * 
     * 
     * @param instance the <code>Instantiation</code> to be deleted.
     * @return <code>true</code> if the <code>IExample/code> existed
     * and was deleted, otherwise returns <code>false</code>.
     */
    public boolean deleteInstance(Instantiation instance) {
        return false;
    }

    /**
     * Compares nodes by name.
     * @param node a node to be compared.
     */
    public int compareTo(OntologyNode node) {
        return this.toString().compareTo(node.toString());
    }
}
