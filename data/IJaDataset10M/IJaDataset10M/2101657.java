package awilkins.eclipse.jdt.properties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 *
 *         Style - Code Templates
 */
public abstract class PropertyNode {

    private PropertyNode parent;

    private PropertyAccessor propertyAccessor;

    /**
   *
   */
    protected PropertyNode() {
        this(null);
    }

    protected PropertyNode(PropertyNode parent) {
        setParent(parent);
    }

    protected abstract PropertyNode[] doGetChlidren();

    public PropertyNode[] getAllChildren() {
        List allKids = new ArrayList();
        getAllChildren(this, allKids);
        return toArray(allKids);
    }

    /**
   * @param allKids
   */
    private void getAllChildren(PropertyNode parent, List allKids) {
        PropertyNode[] children = parent.getChlidren();
        for (int i = 0; i < children.length; i++) {
            PropertyNode node = children[i];
            allKids.add(node);
            getAllChildren(node, allKids);
        }
    }

    public PropertyNode[] getChlidren() {
        return weed(doGetChlidren());
    }

    public abstract String getName();

    /**
   * @return Returns the parent.
   */
    public PropertyNode getParent() {
        return parent;
    }

    /**
   * @return Returns the PropertyAccessor.
   */
    public PropertyAccessor getPropertyAccessor() {
        return propertyAccessor;
    }

    public boolean isAllowedPropertyAccessor() {
        return false;
    }

    /**
   * Convenience method that returns true if this property node can contain others.
   * @return true if modifies
   */
    public abstract boolean isContainerProperty();

    /**
   * Convenience method that returns true if this property can modify the state of an instance of the type it belongs to.
   * This is equivalent to isWritableProperty() for all but ConstructorPropertyNode, which should return true always.
   * @return true if modifies
   */
    public boolean isModificationProperty() {
        return isWritableProperty();
    }

    /**
   * 
   * @return true if the property can be accessed via an accessor, and the accessor can write to the property
   */
    public abstract boolean isWritableProperty();

    /**
   * @param parent
   *          The parent to set.
   */
    void setParent(PropertyNode parent) {
        this.parent = parent;
    }

    /**
   * @param propertyAccessor
   *          The PropertyAccessor to set.
   */
    public void setPropertyAccessor(PropertyAccessor propertyAccessor) {
        if (!isAllowedPropertyAccessor()) {
            throw new IllegalStateException("Not allowed to set propertyAccessor");
        }
        this.propertyAccessor = propertyAccessor;
    }

    public String toString() {
        return getName();
    }

    protected static PropertyNode[] findNodesNamed(Collection nodes, String name) {
        List nodeList = new ArrayList();
        for (Iterator nodeIter = nodes.iterator(); nodeIter.hasNext(); ) {
            PropertyNode node = (PropertyNode) nodeIter.next();
            if (node.getName().equals(name)) {
                nodeList.add(node);
            }
        }
        return toArray(nodeList);
    }

    protected static PropertyNode[] findNodesNamed(PropertyNode[] nodes, String name) {
        if (nodes == null) {
            return new PropertyNode[0];
        }
        return findNodesNamed(toList(nodes), name);
    }

    protected static String getPropertyName(String name) {
        if (name == null) {
            return null;
        }
        if (name.length() == 0) {
            return name;
        }
        char[] chars = name.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return new String(chars);
    }

    public static PropertyNode[] toArray(Collection nodes) {
        if (nodes == null) {
            return new PropertyNode[0];
        }
        PropertyNode[] pNodes = new PropertyNode[nodes.size()];
        nodes.toArray(pNodes);
        return pNodes;
    }

    public static List toList(PropertyNode[] nodes) {
        if (nodes == null) {
            return null;
        }
        return new ArrayList(Arrays.asList(nodes));
    }

    private static PropertyNode[] weed(PropertyNode[] nodes) {
        if (nodes == null) {
            return new PropertyNode[0];
        }
        List nodeList = toList(nodes);
        try {
            boolean removed = nodeList.remove(null);
            while (removed) {
                removed = nodeList.remove(null);
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return toArray(nodeList);
    }
}
