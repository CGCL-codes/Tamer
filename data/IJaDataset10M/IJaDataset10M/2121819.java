package org.xaware.ide.xadev.datamodel;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Stack;
import java.util.Vector;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertySource2;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.xaware.ide.xadev.XA_Designer_Plugin;
import org.xaware.ide.xadev.gui.actions.ActionController;
import org.xaware.ide.xadev.gui.editor.XAInternalFrame;
import org.xaware.shared.util.logging.XAwareLogger;

/**
 * Customized Tree item which represents node in the Tree.
 *
 * @author Saritha
 * @author T Vasu
 * @version 1.0
*/
public class DefaultMutableTreeNode implements MutableTreeNode {

    /** Unique serial number for Serialization. */
    private static final long serialVersionUID = 4227521340422711970L;

    /** XAwareLogger instance. */
    public static XAwareLogger logger = XAwareLogger.getXAwareLogger(ActionController.class.getName());

    /** User object holds the specific object. */
    protected Object userObject;

    /** TreeItem name */
    private String name;

    /** Flag for node type (leaf/non-leaf) */
    private boolean isLeaf = true;

    /** Parent TreeItem */
    protected DefaultMutableTreeNode parent;

    /** Holds child TreeItems */
    protected Vector children = null;

    /** Holds the value indicates whether the tree allows the children or not. */
    protected boolean allowsChildren = true;

    protected boolean initialBuildComplete = false;

    /**
	 * Creates a new ClassPathTreeItem object.
	 *
	 * @param aName String
	 */
    public DefaultMutableTreeNode(final String aName) {
        name = aName;
    }

    /**
	 * Creates a new XATreeItem object.
	 *
	 * @param inContent JDOMContent
	 */
    public DefaultMutableTreeNode(final Object inContent) {
        userObject = inContent;
    }

    /**
	 * Initializes children vector.
	 */
    private void initializeChildren() {
        if (children == null) {
            children = new Vector();
        }
    }

    /**
	 * Returns name of the TreeItem
	 *
	 * @return String
	 */
    public String getName() {
        return name;
    }

    /**
	 * Sets leaf node flag
	 *
	 * @param aIsLeaf boolean
	 */
    public void setIsLeaf(final boolean aIsLeaf) {
        isLeaf = aIsLeaf;
    }

    /**
	 * Returns leaf node status flag value
	 *
	 * @return boolean
	 */
    public boolean getIsLeaf() {
        return isLeaf;
    }

    /**
	 * Sets node name
	 *
	 * @param aName String
	 */
    public void setName(final String aName) {
        name = aName;
    }

    /**
	 * Retruns children TreeItems
	 *
	 * @return ArrayList
	 */
    public Enumeration children() {
        initializeChildren();
        return children.elements();
    }

    /**
	 * Returns parent Item
	 *
	 * @return ClassPathTreeItem
	 */
    public MutableTreeNode getParent() {
        return parent;
    }

    /**
	 * Sets the given Object as parent Item
	 *
	 * @param aParent ClassPathTreeItem
	 */
    public void setParent(final DefaultMutableTreeNode aParent) {
        parent = aParent;
    }

    /**
	 * Sets the given object as child
	 *
	 * @param child Object
	 */
    public void setChildren(final Object child) {
        initializeChildren();
        children.add(child);
        isLeaf = false;
    }

    /**
	 * Gets the user object
	 *
	 * @return Object specific user object.
	 */
    public Object getUserObject() {
        return userObject;
    }

    /**
	 * Sets the user object.
	 *
	 * @param userObject The user object to be set.
	 */
    public void setUserObject(final Object userObject) {
        this.userObject = userObject;
    }

    /**
	 * Inserts the given node at specified index.
	 *
	 * @param child Node to be inserted.
	 * @param index Index at which the given node to be inserted.
	 */
    public void insert(final MutableTreeNode child, final int index) {
        initializeChildren();
        children.add(index, child);
    }

    /**
	 * Removes the specific node specified by index.
	 *
	 * @param index Index of the node to be removed.
	 */
    public void remove(final int index) {
        initializeChildren();
        children.remove(index);
    }

    /**
	 * Removes the given node.
	 *
	 * @param node Node to be removed
	 */
    public void remove(final MutableTreeNode node) {
        initializeChildren();
        children.remove(node);
    }

    /**
	 * Removes the node from its parent
	 */
    public void removeFromParent() {
        getParent().remove(this);
        parent = null;
    }

    /**
	 * Sets the given node as a parent
	 *
	 * @param newParent Node to be set as a parent.
	 */
    public void setParent(final MutableTreeNode newParent) {
        parent = (DefaultMutableTreeNode) newParent;
    }

    /**
	 * Returns true/false indicates whether it allows the children or not.
	 *
	 * @return true
	 */
    public boolean getAllowsChildren() {
        return true;
    }

    /**
	 * Gets the children at given index
	 *
	 * @param childIndex children index.
	 *
	 * @return MutableTreeNode instance.
	 */
    public MutableTreeNode getChildAt(final int childIndex) {
        initializeChildren();
        return (MutableTreeNode) children.get(childIndex);
    }

    /**
	 * Returns the mutable treenode at the specified index.
	 *
	 * @return MutableTreeNode
	 */
    public int getChildCount() {
        initializeChildren();
        return children.size();
    }

    /**
	 * Returns the index of the given node.
	 *
	 * @param node Node of which we need to get the index.
	 *
	 * @return int Index of the given node.
	 */
    public int getIndex(final MutableTreeNode node) {
        initializeChildren();
        return children.indexOf(node);
    }

    /**
	 * Returns true if the node contains contains no children otherwise false.
	 *
	 * @return true/false
	 */
    public boolean isLeaf() {
        initializeChildren();
        if (children.size() == 0) {
            return true;
        }
        return false;
    }

    /**
	 * Path of the given the node.
	 *
	 * @return Array of the nodes.
	 */
    public MutableTreeNode[] getPath() {
        return getPathToRoot(this, 0);
    }

    /**
	 * Returns the array of nodes upto root element.
	 *
	 * @param aNode Node from which we need the path upto its rootelement.
	 * @param depth depth of node.
	 *
	 * @return Array of the nodes.
	 */
    protected MutableTreeNode[] getPathToRoot(final DefaultMutableTreeNode aNode, int depth) {
        MutableTreeNode[] retNodes;
        if (aNode == null) {
            if (depth == 0) {
                return null;
            } else {
                retNodes = new MutableTreeNode[depth];
            }
        } else {
            depth++;
            retNodes = getPathToRoot((DefaultMutableTreeNode) aNode.getParent(), depth);
            retNodes[retNodes.length - depth] = aNode;
        }
        return retNodes;
    }

    /**
	 * Returns name of the node
	 *
	 * @return String
	 */
    @Override
    public String toString() {
        return name;
    }

    /**
	 * Sets flag for adding children to the node
	 *
	 * @param state boolean
	 */
    public void setAllowsChildren(final boolean state) {
        allowsChildren = state;
    }

    /**
	 * Removes children for this node
	 */
    public void removeAllChildren() {
        for (int i = getChildCount() - 1; i >= 0; i--) {
            remove(i);
        }
    }

    /**
	 * Adds the given node as child
	 *
	 * @param newChild MutableTreeNode
	 */
    public void add(final MutableTreeNode newChild) {
        initializeChildren();
        if ((newChild != null) && (newChild.getParent() == this)) {
            insert(newChild, getChildCount() - 1);
        } else {
            insert(newChild, getChildCount());
        }
    }

    /**
	 * Creates customized enumeration using Stack
	 *
	 * @return Enumeration
	 */
    public Enumeration preorderEnumeration() {
        return new PreorderEnumeration(this);
    }

    /**
	 * Customized Enumeration object for retriving next element.
	 *
	 * @author Srinivas Ch
	 * @version 1.0
	*/
    final class PreorderEnumeration implements Enumeration {

        /** Stack instance */
        protected Stack stack;

        /**
		 * Creates a new PreorderEnumeration object.
		 *
		 * @param rootNode MutableTreeNode
		 */
        public PreorderEnumeration(final MutableTreeNode rootNode) {
            super();
            final Vector v = new Vector(1);
            v.addElement(rootNode);
            stack = new Stack();
            stack.push(v.elements());
        }

        /**
		 * Returns true if this enumeration contains more elements.
		 *
		 * @return boolean
		 */
        public boolean hasMoreElements() {
            return (!stack.empty() && ((Enumeration) stack.peek()).hasMoreElements());
        }

        /**
		 * Returns the next MutableTreeNode of this enumeration
		 *
		 * @return Object
		 */
        public Object nextElement() {
            final Enumeration enumer = (Enumeration) stack.peek();
            final MutableTreeNode node = (MutableTreeNode) enumer.nextElement();
            final Enumeration children = node.children();
            if (!enumer.hasMoreElements()) {
                stack.pop();
            }
            if (children.hasMoreElements()) {
                stack.push(children);
            }
            return node;
        }
    }

    /**
     * Returns true if this node is the root of the tree.  The root is
     * the only node in the tree with a null parent; every tree has exactly
     * one root.
     *
     * @return	true if this node is the root of its tree
     */
    public boolean isRoot() {
        return getParent() == null;
    }

    /**
     * Returns the number of levels above this node -- the distance from
     * the root to this node.  If this node is the root, returns 0.
     *
     * @return	the number of levels above this node
     */
    public int getLevel() {
        DefaultMutableTreeNode ancestor;
        int levels = 0;
        ancestor = this;
        while ((ancestor = (XMLTreeNode) ancestor.getParent()) != null) {
            levels++;
        }
        return levels;
    }

    private static final String ELEMENT_NODE_ID = "xaware.Element.DefaultMutableTreeNode";

    private static final String ATTRIBUTE_NODE_ID = "xaware.Attribute.DefaultMutableTreeNode";

    private static final TextPropertyDescriptor ELEMENT_NODE_PROPERTY_DESCRIPTOR = new TextPropertyDescriptor(ELEMENT_NODE_ID, "Element");

    private static final TextPropertyDescriptor ATTRIBUTE_NODE_PROPERTY_DESCRIPTOR = new TextPropertyDescriptor(ATTRIBUTE_NODE_ID, "Attribute");

    static {
        ELEMENT_NODE_PROPERTY_DESCRIPTOR.setCategory("Element");
        ELEMENT_NODE_PROPERTY_DESCRIPTOR.setAlwaysIncompatible(false);
        ATTRIBUTE_NODE_PROPERTY_DESCRIPTOR.setCategory("Attribute");
        ATTRIBUTE_NODE_PROPERTY_DESCRIPTOR.setAlwaysIncompatible(false);
    }

    private static final IPropertyDescriptor[] DESCRIPTORS = { ELEMENT_NODE_PROPERTY_DESCRIPTOR, ATTRIBUTE_NODE_PROPERTY_DESCRIPTOR };

    /**
	    * Returns a value for this property source that can be edited in a
	    * property sheet.
	    */
    public Object getEditableValue() {
        System.out.println("In getEditableValue");
        if (userObject instanceof IPropertySource2) {
            return ((IPropertySource2) userObject).getEditableValue();
        } else {
            return this;
        }
    }

    /**
	    * Returns the list of property descriptors for this property
	    * source. The <code>getPropertyValue</code> and
	    * <code>setPropertyValue</code> methods are used to read and
	    * write the actual property values by specifying the property ids
	    * from these property descriptors.
	    */
    public IPropertyDescriptor[] getPropertyDescriptors() {
        if (userObject instanceof IPropertySource2) {
            return ((IPropertySource2) userObject).getPropertyDescriptors();
        } else {
            return DESCRIPTORS;
        }
    }

    /**
	    * Returns the value of the property with the given id if it has
	    * one. Returns <code>null</code> if the property's value is
	    * <code>null</code> value or if this source does not have the
	    * specified property.
	    * 
	    * @see #setPropertyValue
	    * @param id the id of the property being set
	    * @return the value of the property, or <code>null</code>
	    */
    public Object getPropertyValue(final Object id) {
        System.out.println("id: " + id);
        if (userObject instanceof IPropertySource2) {
            return ((IPropertySource2) userObject).getPropertyValue(id);
        } else {
            return userObject;
        }
    }

    /**
	    * Returns whether the value of the property with the given id has
	    * changed from its default value. Returns <code>false</code> if
	    * this source does not have the specified property.
	    * 
	    * @param id the id of the property
	    * @return <code>true</code> if the value of the specified
	    *         property has changed from its original default value,
	    *         <code>true</code> if the specified property does not
	    *         have a meaningful default value, and <code>false</code>
	    *         if this source does not have the specified property
	    * @see IPropertySource2#isPropertyResettable(Object)
	    * @see #resetPropertyValue(Object)
	    */
    public boolean isPropertySet(final Object id) {
        if (userObject instanceof IPropertySource2) {
            return ((IPropertySource2) userObject).isPropertySet(id);
        }
        return true;
    }

    /**
	    * Returns whether the value of the property with the specified id
	    * is resettable to a default value.
	    * 
	    * @param id the id of the property
	    * @return <code>true</code> if the property with the specified
	    *         id has a meaningful default value to which it can be
	    *         resetted, and <code>false</code> otherwise
	    * @see IPropertySource#resetPropertyValue(Object)
	    * @see IPropertySource#isPropertySet(Object)
	    */
    public boolean isPropertyResettable(final Object id) {
        if (userObject instanceof IPropertySource2) {
            return ((IPropertySource2) userObject).isPropertyResettable(id);
        }
        return false;
    }

    /**
	    * Resets the property with the given id to its default value if
	    * possible. Does nothing if the notion of a default value is not
	    * meaningful for the specified property, or if the property's
	    * value cannot be changed, or if this source does not have the
	    * specified property.
	    * 
	    * Callers will check if this <code>IPropertySource</code>
	    * implements <code>IPropertySource2</code> and this method will
	    * only be called if
	    * <code>IPropertySource2#isPropertyResettable(Object)</code>
	    * returns <code>true</code> for the property with the given id.
	    * </p>
	    * 
	    * @param id the id of the property being reset
	    * @see #isPropertySet(Object)
	    * @see IPropertySource2#isPropertyResettable(Object)
	    */
    public void resetPropertyValue(final Object id) {
        if (userObject instanceof IPropertySource2) {
            ((IPropertySource2) userObject).resetPropertyValue(id);
            refreshTree();
        }
    }

    /**
	    * Sets the property with the given id if possible. Does nothing if
	    * the property's value cannot be changed or if this source does
	    * not have the specified property.
	    * 
	    * @see #getPropertyValue
	    * @see #getEditableValue
	    * @param id the id of the property being set
	    * @param value the new value for the property; <code>null</code>
	    *           is allowed
	    */
    public void setPropertyValue(final Object id, final Object value) {
        if (userObject instanceof IPropertySource2) {
            try {
                ((IPropertySource2) userObject).setPropertyValue(id, value);
                refreshTree();
            } catch (final RuntimeException e) {
                logger.fine("Set of property value: " + value + " failed with exception: " + e.getMessage());
            }
        }
    }

    protected void refreshTree() {
        if (!initialBuildComplete) {
            return;
        }
        final IWorkbenchWindow activeWindow = XA_Designer_Plugin.getDefault().getWorkbench().getActiveWorkbenchWindow();
        if (activeWindow != null) {
            final IWorkbenchPage page = activeWindow.getActivePage();
            if (page != null) {
                final IEditorPart iep = page.getActiveEditor();
                if (iep instanceof XAInternalFrame) {
                    ((XAInternalFrame) iep).getTreeEditor().getTree().refreshTree();
                }
            }
        }
    }

    public void setInitialBuildComplete(final boolean p_initialBuildComplete) {
        this.initialBuildComplete = p_initialBuildComplete;
        initializeChildren();
        for (final Iterator iter = children.iterator(); iter.hasNext(); ) {
            final DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) iter.next();
            childNode.setInitialBuildComplete(p_initialBuildComplete);
        }
    }
}
