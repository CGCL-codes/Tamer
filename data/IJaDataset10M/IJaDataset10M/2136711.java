package org.dbe.kb.toolkit.proxyutils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;
import javax.jmi.model.ModelElement;
import javax.jmi.reflect.RefBaseObject;
import javax.jmi.reflect.RefClass;
import javax.jmi.reflect.RefObject;
import javax.jmi.reflect.RefPackage;

/**
 * The ContextRoot class is the root of all nodes. It reperesents the query and
 * Mof Class hierarchy of the metamodel. It is the root node of the tree control
 * of FormulateQueryWizardPage class.
 * 
 * @author George Kotopoulos
 * @version 1.0
 */
public class ContextRoot extends AbstractNode implements IRootNode {

    /** The MofClass beeing the query context */
    private RefObject[] context;

    /** The children of this node. Has always only one, a NavigationNode */
    private Object[] children = new Object[1];

    /** The package path to the MofClass beeing query context */
    private Vector[] path;

    /** The attribute nodes. The actual constraints of this query. */
    private HashSet attributeNodes;

    /** The metamodel name to which this query applies to */
    private String metamodel;

    /** The query name */
    private String name;

    private boolean isSelected;

    private boolean isInstance;

    /**
     * Creates a new query, with a name and for a context.
     * 
     * @param root The MofClass beeing the context of this query.
     * @param name The name of the query.
     */
    public ContextRoot(RefBaseObject root, String name, QueryNode parent, String metamodel) {
        this(root, name, metamodel);
        this.parent = parent;
    }

    /**
     * Creates a new query, with a name and for a context.
     * 
     * @param root The MofClass beeing the context of this query.
     * @param name The name of the query.
     */
    public ContextRoot(RefBaseObject root, String name, String metamodel) {
        Vector roots = new Vector();
        roots.add(root);
        init(roots, name, metamodel);
    }

    /**
     * Creates a new query, with a name and for a context.
     * 
     * @param root The MofClass beeing the context of this query.
     * @param name The name of the query.
     */
    public ContextRoot(Collection root, String name, String metamodel) {
        init(root, name, metamodel);
    }

    private void init(Collection root, String name, String metamodel) {
        path = new Vector[root.size()];
        context = new RefObject[root.size()];
        children = new Object[root.size()];
        isInstance = root.toArray()[0] instanceof RefObject;
        if (isInstance) nameAdapter = (metamodel.equals("BML")) ? new BmlInstanceNameAdapter() : (NameAdapter) new SslInstanceNameAdapter(); else nameAdapter = new DefaultNameAdapter();
        int i = 0;
        for (Iterator iter = root.iterator(); iter.hasNext(); i++) {
            RefBaseObject element = (RefBaseObject) iter.next();
            if (element instanceof RefClass) {
                context[i] = element.refMetaObject();
                path[i] = new Vector();
                path[i].add(nameAdapter.getName(context[i]));
                RefPackage pack = element.refImmediatePackage();
                while (pack != null) {
                    path[i].add(0, ((ModelElement) pack.refMetaObject()).getName());
                    pack = pack.refImmediatePackage();
                }
                path[i].set(0, metamodel);
            } else {
                context[i] = (RefObject) element;
                path[i] = new Vector();
                path[i].add("IMM");
                path[i].add("Object");
            }
            children[i] = new NavigationNode(context[i], this, nameAdapter);
        }
        attributeNodes = new HashSet();
        this.metamodel = metamodel;
        this.name = name;
        isSelected = true;
    }

    /**
     * Gets the context of the query.
     * 
     * @return The MofClass context of the query.
     */
    public RefObject getContext(int index) {
        if (index < context.length) return context[index]; else return context[0];
    }

    /**
     * Returns true if this node has children. False if it is a leaf node.
     * 
     * @return True if this node has children. False if it is a leaf node.
     * @see org.dbe.dtool.nodes.AbstractNode#hasChildren()
     */
    public boolean hasChildren() {
        return isSelected;
    }

    /**
     * Gets a String representation of the name of the node.
     * 
     * @return a String representation of the name of the node.
     * @see org.dbe.dtool.nodes.AbstractNode#getName()
     */
    public String getName() {
        return metamodel;
    }

    /**
     * Gets a String representation of the query name.
     * 
     * @return a String representation of the query name.
     */
    public String getQueryName() {
        return name;
    }

    /**
     * Gets the result type of this context, i.e. the metamodel.
     * 
     * @return The result type of this context, i.e. the metamodel.
     */
    public String getResultType() {
        return metamodel;
    }

    /**
     * Gets the Object of the current node. It is the context and its' type is
     * MofClass
     * 
     * @return The MofClass of the context
     * @see org.dbe.dtool.nodes.AbstractNode#getObject()
     */
    public Object getObject() {
        return context;
    }

    /**
     * Returns an array with this nodes children.
     * 
     * @return An array with this nodes children.
     * @see org.dbe.dtool.nodes.AbstractNode#getChildren()
     */
    public Object[] getChildren() {
        return children;
    }

    /**
     * Gets a Vector containg the package path to the context Mof class of this
     * query.
     * 
     * @return a Vector containg the package path to the context Mof class of
     *         this query.
     */
    public Vector getPath() {
        return path[0];
    }

    public Vector getPath(int index) {
        if (index < path.length) return path[index]; else return path[0];
    }

    /**
     * Adds a ValueNode to the query.
     * 
     * @param node The node to be added.
     * @return true if added, false otherwise.
     */
    protected boolean addNode(ValueNode node) {
        attributeNodes.remove(node);
        return attributeNodes.add(node);
    }

    /**
     * Remove a ValueNode fr5om the query.
     * 
     * @param node The ValueNode to be removed.
     * @return true if removed successfully, false otherwise.
     */
    protected boolean removeNode(ValueNode node) {
        return attributeNodes.remove(node);
    }

    /**
     * Returns true is the query has at least one value node, false otherwise.
     * 
     * @return true is the query has at least one value node, false otherwise.
     */
    public boolean hasNodes() {
        return !attributeNodes.isEmpty();
    }

    /**
     * Gets a int flag for the type of the ValueNodes. Valid values are NONE
     * (0), HARD (1), SOFT (2) and ALL (3)
     * 
     * @return NONE (0), HARD (1), SOFT (2) and ALL (3).
     */
    public int getNodesFlag() {
        int result = NONE;
        for (Iterator iter = attributeNodes.iterator(); iter.hasNext() && result != ALL; ) {
            ValueNode node = (ValueNode) iter.next();
            result = result | ((node.isHard()) ? HARD : SOFT);
        }
        return result;
    }

    /**
     * Gets an iterator with the ValueNodes of this query.
     * 
     * @return The ValueNodes of this query.
     */
    public Iterator getNodes() {
        return attributeNodes.iterator();
    }

    /**
     * Gets an iterator with the ValueNodes of this query.
     * 
     * @return The ValueNodes of this query.
     */
    public Collection getNodesAsCollection() {
        return attributeNodes;
    }

    /**
     * Gets an Iterator with the ValueNodes of a certain type (hard or soft
     * constrains)
     * 
     * @param isHard if the ValueNodes to be returned are hard constrains or
     *            not.
     * @return An Iterator with the ValueNodes of a certain type (hard or soft
     *         constrains)
     */
    public Iterator getNodes(boolean isHard) {
        return getNodesAsVector(isHard).iterator();
    }

    /**
     * Gets an Iterator with the ValueNodes of a certain type (hard or soft
     * constrains)
     * 
     * @param isHard if the ValueNodes to be returned are hard constrains or
     *            not.
     * @return An Iterator with the ValueNodes of a certain type (hard or soft
     *         constrains)
     */
    protected Vector getNodesAsVector(boolean isHard) {
        Vector result = new Vector();
        for (Iterator iter = attributeNodes.iterator(); iter.hasNext(); ) {
            ValueNode node = (ValueNode) iter.next();
            if (node.isHard() == isHard) {
                result.add(node);
            }
        }
        return result;
    }

    public boolean isInstance() {
        return isInstance;
    }

    public String getMetamodel() {
        return metamodel;
    }

    protected void setSelected(boolean val) {
        isSelected = val;
    }

    public boolean isSelected() {
        return isSelected;
    }
}
