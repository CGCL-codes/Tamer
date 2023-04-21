package org.xaware.ide.xadev.datamodel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.jdom.Attribute;
import org.jdom.Comment;
import org.jdom.Element;
import org.jdom.Text;
import org.xaware.ide.xadev.XA_Designer_Plugin;
import org.xaware.ide.xadev.exception.UnsupportedFlavorException;
import org.xaware.ide.xadev.gui.editor.XAInternalFrame;
import org.xaware.shared.util.logging.XAwareLogger;

/**
 * Represents the tree node in the Tree.
 *
 * @author Purna
 * @version 1.0
 */
public class XMLTreeNode extends DefaultMutableTreeNode implements XATreeNode {

    /** Version id being used while serialization/de-serialization */
    private static final long serialVersionUID = -7125724107137947310L;

    /** Logger for this class. */
    private static final XAwareLogger logger = XAwareLogger.getXAwareLogger(XMLTreeNode.class.getName());

    /** Content in the tree item */
    protected JDOMContent myContent;

    /**
	 * Creates a new XMLTreeNode object.
	 *
	 * @param inContent JDOMContent object
	 */
    public XMLTreeNode(final JDOMContent inContent) {
        super(inContent);
        myContent = inContent;
        if (inContent.getType() == JDOMContent.ELEMENT) {
            if (((Element) inContent.getContent()).isRootElement()) {
                parent = null;
            }
            if (this.getChildCount() > 0) {
                allowsChildren = true;
                setIsLeaf(false);
            } else {
                allowsChildren = false;
            }
        } else {
            allowsChildren = false;
        }
        setInitialBuildComplete(true);
    }

    /**
	 * Returns the childs of the parent.
	 *
	 * @return Vector Holds the children.
	 */
    protected Vector myChildren() {
        if (children == null) {
            children = new Vector();
            if (allowsChildren && myContent.getContent() instanceof Element) {
                final Element myElem = ((Element) myContent.getContent());
                final Iterator itr1 = myElem.getAttributes().iterator();
                while (itr1.hasNext()) {
                    final Attribute obj = (Attribute) itr1.next();
                    final JDOMContent kidContent = JDOMContentFactory.createJDOMContent(obj);
                    kidContent.isCatalogNode = myContent.isCatalogNode;
                    final XMLTreeNode tmp = new XMLTreeNode(kidContent);
                    tmp.setParent(this);
                    children.add(tmp);
                }
                final Iterator itr = ((Element) myContent.getContent()).getContent().iterator();
                while (itr.hasNext()) {
                    final Object o = itr.next();
                    if (!(o instanceof Text)) {
                        final JDOMContent kidContent = JDOMContentFactory.createJDOMContent(o);
                        kidContent.isCatalogNode = myContent.isCatalogNode;
                        final XMLTreeNode tmp = new XMLTreeNode(kidContent);
                        tmp.setParent(this);
                        children.add(tmp);
                    }
                }
            }
        }
        return children;
    }

    /**
	 * Returns the Content of the tree item.
	 *
	 * @return JDOMContent Content of the tree item.
	 */
    public JDOMContent getJDOMContent() {
        return myContent;
    }

    /**
	 * Removes the children from the selected node.
	 */
    @Override
    public void removeAllChildren() {
        try {
            myContent.removeAllContent();
            myChildren().removeAllElements();
        } catch (final Throwable t) {
            logger.info("Throwable caught removing all children : " + t);
            logger.printStackTrace(t);
        }
    }

    /**
	 * Removes all the attributes
	 */
    public void removeAllAttributes() {
        try {
            if (myContent.getType() == JDOMContent.ELEMENT) {
                final List dupChildren = new ArrayList(children);
                for (final Iterator childrenItr = dupChildren.iterator(); childrenItr.hasNext(); ) {
                    final XMLTreeNode child = (XMLTreeNode) childrenItr.next();
                    if (child.getJDOMContent().getType() == JDOMContent.ATTRIBUTE) {
                        this.remove(child);
                    }
                }
            }
        } catch (final Throwable t) {
            logger.info("Throwable caught removing all children : " + t);
            logger.printStackTrace(t);
        }
    }

    /**
	 * Adds a child node to the given parent.
	 *
	 * @param newChild Child to be added.
	 *
	 * @return int Position where the child hasbeen added.
	 */
    public int add(final XATreeNode newChild) {
        final int index = myContent.add(((XMLTreeNode) newChild).getJDOMContent());
        newChild.setParent(this);
        children = null;
        allowsChildren = true;
        setIsLeaf(false);
        return index;
    }

    /**
	 * Marks the selected node
	 *
	 * @param inSelected True/false whether to mark the node or not.
	 */
    public void markSelected(final boolean inSelected) {
    }

    /**
	 * Returns false.
	 *
	 * @return boolean
	 */
    public boolean isMarkSelected() {
        return false;
    }

    /**
	 * Sets the path value for the node.
	 *
	 * @param inText Path value to be set.
	 */
    public void setPathValue(final String inText) {
        if (myContent.getContent() instanceof Attribute) {
            ((Attribute) myContent.getContent()).setValue(inText);
        } else {
            myContent.setText(inText);
        }
    }

    /**
	 * Gets the path value for the node
	 *
	 * @return Path value of the selected node.
	 */
    public String getPathValue() {
        if (myContent.getContent() instanceof Attribute) {
            return ((Attribute) myContent.getContent()).getValue();
        } else if (myContent.getContent() instanceof Element) {
            return ((Element) myContent.getContent()).getText();
        } else if (myContent.getContent() instanceof Comment) {
            return ((Comment) myContent.getContent()).getText();
        } else if (myContent.getContent() instanceof Text) {
            return ((Text) myContent.getContent()).getText();
        } else {
            Object obj = myContent.getContent();
            return obj.toString();
        }
    }

    /**
	 * Inserts the node at the specified position.
	 *
	 * @param child Child to be added
	 * @param index Position at which the child to be added.
	 */
    @Override
    public void insert(final MutableTreeNode child, final int index) {
        allowsChildren = true;
        int adjustedIndex = index;
        final int attrCount = getJDOMContent().getAttributeCount();
        if (((XMLTreeNode) child).getJDOMContent().getType() != JDOMContent.ATTRIBUTE) {
            if (index < attrCount) {
                adjustedIndex += attrCount;
            }
        }
        myContent.insert(((XMLTreeNode) child).getJDOMContent(), adjustedIndex);
        child.setParent(this);
        if (adjustedIndex > myChildren().size()) {
            myChildren().add(child);
        } else {
            myChildren().insertElementAt(child, adjustedIndex);
        }
    }

    /**
	 * Removes the content from the tree item specified by the given index.
	 *
	 * @param index Position of the tree item.
	 */
    @Override
    public void remove(final int index) {
        final XMLTreeNode tNode = (XMLTreeNode) myChildren().elementAt(index);
        final JDOMContent tmp = tNode.getJDOMContent();
        myContent.removeContent(tmp);
        myChildren().removeElementAt(index);
    }

    /**
	 * Removes the specific tree item.
	 *
	 * @param node Treeitem to be removed.
	 */
    @Override
    public void remove(final MutableTreeNode node) {
        final JDOMContent content = ((XMLTreeNode) node).getJDOMContent();
        if (content.getContent() instanceof Attribute && myContent.getContent() instanceof Element) {
            final Attribute attr = (Attribute) content.getContent();
            ((Element) myContent.getContent()).removeAttribute(attr.getName(), attr.getNamespace());
        } else {
            myContent.removeContent(((XMLTreeNode) node).getJDOMContent());
        }
        myChildren().removeElement(node);
    }

    /**
	 * Removes the current tree item from its parent.
	 */
    @Override
    public void removeFromParent() {
        if (parent != null) {
            ((XMLTreeNode) parent).remove(this);
        }
        myContent.detach();
    }

    /**
	 * Sets the given tree item as a parent.
	 *
	 * @param newParent Tree item to be set as a parent.
	 */
    @Override
    public void setParent(final DefaultMutableTreeNode newParent) {
        parent = newParent;
    }

    /**
	 * Sets the user obejct.
	 *
	 * @param object Item to be set for user object.
	 */
    @Override
    public void setUserObject(final Object object) {
        if (object instanceof String) {
            setPathValue((String) object);
        }
        userObject = object;
    }

    /**
	 * Returns the children.
	 *
	 * @return The list of children.
	 */
    @Override
    public Enumeration children() {
        return myChildren().elements();
    }

    /**
	 * Returns the value which indicates whether to allow the children or not.
	 *
	 * @return boolean True/false
	 */
    @Override
    public boolean getAllowsChildren() {
        return allowsChildren;
    }

    /**
	 * Returns the node positioned at the given index
	 *
	 * @param index Index of the node to be retrieved.
	 *
	 * @return Node positioned at the given index.
	 */
    @Override
    public MutableTreeNode getChildAt(final int index) {
        return (DefaultMutableTreeNode) myChildren().elementAt(index);
    }

    /**
	 * Returns the child count
	 *
	 * @return int Child count.
	 */
    @Override
    public int getChildCount() {
        return myChildren().size();
    }

    /**
	 * Returns the index of the given node.
	 *
	 * @param node Tree item
	 *
	 * @return int Position of the given node in the children list.
	 */
    @Override
    public int getIndex(final MutableTreeNode node) {
        return myChildren().indexOf(node);
    }

    /**
	 * Overriding the equal method
	 *
	 * @param o Specified object
	 *
	 * @return True/false which indicates whether the given is equal to the
	 *         parent object.
	 */
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof XMLTreeNode)) {
            return false;
        }
        final XMLTreeNode node = (XMLTreeNode) o;
        if (myContent.getContent() != node.getJDOMContent().getContent()) {
            return false;
        }
        return true;
    }

    /**
	 * Returns the parent item.
	 *
	 * @return XATreeItem Preant item.
	 */
    @Override
    public MutableTreeNode getParent() {
        return parent;
    }

    /**
	 * Returns the value to indicate the node contains the children or not.
	 *
	 * @return True if the node contains children else false.
	 */
    @Override
    public boolean isLeaf() {
        return (myChildren().size() > 0) ? true : false;
    }

    /**
	 * Overrides the toString in JDOMContent.
	 *
	 * @return String
	 */
    @Override
    public String toString() {
        return myContent.toString();
    }

    /**
	 * Returns the value to indicate whether the data flavor is supported or
	 * not.
	 *
	 * @param byteArrTrans Given transfer object
	 *
	 * @return True if the given transfer object supports the speicific data
	 *         flavor else false.
	 */
    public boolean isDataFlavorSupported(final ByteArrayTransfer byteArrTrans) {
        return myContent.isDataFlavorSupported(byteArrTrans);
    }

    /**
	 * Converts the Byte array transfer data to a specific object.
	 *
	 * @param byteArrTrans Transferable object array.
	 *
	 * @return Specific data object.
	 *
	 * @throws UnsupportedFlavorException Throws when the unsupported flavor is
	 *         encountered.
	 * @throws IOException Throws when the problem occurred in byte array
	 *         transfer.
	 */
    public Object getTransferData(final ByteArrayTransfer byteArrTrans) throws UnsupportedFlavorException, IOException {
        return myContent.getTransferData(byteArrTrans);
    }

    /**
	 * Returns the array of the transferable data flavors.
	 *
	 * @return ByteArrayTransfer Array of the transferable data flavors.
	 */
    public ByteArrayTransfer[] getTransferDataFlavors() {
        return JDOMContent.flavors;
    }

    /**
	 * Returns the tooltip text.
	 *
	 * @return String Tooltip text.
	 */
    public String getToolTipText() {
        String value = "";
        if (myContent.getType() == JDOMContent.ELEMENT) {
            value = ((Element) myContent.getContent()).getText().trim();
            if (!value.equals("")) {
                value = ((Element) myContent.getContent()).getQualifiedName() + "=" + value;
            } else {
                value = ((Element) myContent.getContent()).getQualifiedName();
            }
            return value;
        } else if (myContent.getType() == JDOMContent.ATTRIBUTE) {
            value = ((Attribute) myContent.getContent()).getValue().trim();
            if (!value.equals("")) {
                value = ((Attribute) myContent.getContent()).getQualifiedName() + "=" + value;
            } else {
                value = ((Attribute) myContent.getContent()).getQualifiedName();
            }
            return value;
        } else if (myContent.getType() == JDOMContent.COMMENT) {
            value = ((Comment) myContent.getContent()).getText().trim();
            return value;
        } else {
            return null;
        }
    }

    /**
	 * Returns the path of the specific node.
	 *
	 * @return String Path of the specific node.
	 */
    public String getNodePath() {
        String path = "";
        String tmpStr = "";
        final MutableTreeNode[] pathArray = getPath();
        for (int i = 0; i < pathArray.length; i++) {
            final XMLTreeNode tmpNode = (XMLTreeNode) pathArray[i];
            if (tmpNode.myContent.myContent instanceof Element) {
                tmpStr = ((Element) tmpNode.myContent.myContent).getQualifiedName();
                if (!path.equals("")) {
                    path = path + "/" + tmpStr;
                } else {
                    path = tmpStr;
                }
            }
        }
        return "/" + path + "/";
    }

    /**
	 * Returns the path of the selected tree item
	 *
	 * @return Array of the TreeItems.
	 */
    @Override
    public MutableTreeNode[] getPath() {
        return super.getPath();
    }

    @Override
    public void refreshTree() {
        if (!initialBuildComplete) {
            return;
        }
        final IWorkbenchWindow activeWindow = XA_Designer_Plugin.getDefault().getWorkbench().getActiveWorkbenchWindow();
        if (activeWindow != null) {
            final IWorkbenchPage page = activeWindow.getActivePage();
            if (page != null) {
                final IEditorPart iep = page.getActiveEditor();
                if (iep instanceof XAInternalFrame) {
                    ((XAInternalFrame) iep).getTreeEditor().getTree().refreshTree(this);
                }
            }
        }
    }

    /**
     * Finds the first content object of type Element starting at the
     * given node and moving up the structure.
     * @param node
     * @return
     */
    public static Element getElementForTreeNode(final XMLTreeNode node) {
        if (node == null) {
            return null;
        }
        final Object obj = node.getJDOMContent().myContent;
        if (obj instanceof Element) {
            return (Element) obj;
        }
        return getElementForTreeNode((XMLTreeNode) node.getParent());
    }
}
