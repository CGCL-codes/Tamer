package com.patientis.framework.controls.tree;

import java.util.List;
import java.util.ArrayList;
import com.patientis.framework.controls.forms.IContainer;
import com.patientis.framework.controls.exceptions.ISContainerTreeEmptyException;

/**
 * ISTreeBuilder generates tree structures
 * 
 * Design Patterns: <a href="/functionality/rm/1000059.html">Trees</a>
 * <br/>
 */
public class ISTreeBuilder {

    /**
	 * Create a tree model from the definition of the parent and child
	 * 
	 * @param list
	 * @return
	 */
    public static ISTreeModel createTreeModel(List<IParentChild> list) {
        ISTreeNode root = new ISTreeNode("ISTreeModel " + list.size());
        for (IParentChild pc : list) {
            if (pc.isParentRootLevel()) {
                pc.setValid(false);
                addChild(root, pc.getParentId(), pc.getParentObject(), pc.getParentDisplay(), list);
            } else if (pc.isChildRootLevel()) {
                pc.setValid(false);
                addChild(root, pc.getChildId(), pc.getChildObject(), pc.getChildDisplay(), list);
            }
        }
        return new ISTreeModel(root);
    }

    /**
	 * Recursively add the children to the parent
	 * 
	 * @param parent
	 * @param childObject
	 * @param display
	 * @param list
	 */
    private static void addChild(ISTreeNode parent, Object childId, Object childObject, String childDisplay, List<IParentChild> list) {
        ISTreeNode childNode = new ISTreeNode(childObject);
        childNode.setNodeLabel(childDisplay);
        parent.add(childNode);
        for (IParentChild pc : list) {
            if (pc.isValid() && childId != null && childId.equals(pc.getParentId())) {
                addChild(childNode, pc.getChildId(), pc.getChildObject(), pc.getChildDisplay(), list);
            }
        }
    }

    /**
	 * Create a container hierarchy
	 * 
	 * @param list
	 * @return
	 */
    public static IContainer createContainerTree(ArrayList<IParentChild> list) throws ISContainerTreeEmptyException {
        IContainer root = null;
        for (IParentChild pc : list) {
            if (pc.isParentRootLevel()) {
                pc.setValid(false);
                root = addChild(root, pc.getParentId(), pc.getParentObject(), list);
            } else if (pc.isChildRootLevel()) {
                pc.setValid(false);
                root = addChild(root, pc.getChildId(), pc.getChildObject(), list);
            }
        }
        if (root == null) {
            throw new ISContainerTreeEmptyException();
        }
        return root;
    }

    /**
	 * Recursively add the child containers to the parent
	 * 
	 * @param parent
	 * @param childObject
	 * @param display
	 * @param list
	 */
    private static IContainer addChild(IContainer parent, Object childId, Object childObject, ArrayList<IParentChild> list) {
        IContainer childContainer = (IContainer) childObject;
        IContainer root = null;
        if (parent != null) {
            parent.add(childContainer);
        } else {
            root = childContainer;
        }
        for (IParentChild pc : list) {
            if (pc.isValid() && childId != null && childId.equals(pc.getParentId())) {
                addChild(childContainer, pc.getChildId(), pc.getChildObject(), list);
            }
        }
        return root;
    }
}
