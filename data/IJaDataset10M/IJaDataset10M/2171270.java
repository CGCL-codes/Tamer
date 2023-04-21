package org.neodatis.btree.tool;

import org.neodatis.btree.IBTreeNode;
import org.neodatis.btree.IBTreeNodeOneValuePerKey;
import org.neodatis.btree.IKeyAndValue;
import org.neodatis.btree.exception.BTreeNodeValidationException;

public class BTreeValidator {

    private static boolean on = false;

    public static boolean isOn() {
        return BTreeValidator.on;
    }

    public static void setOn(boolean on) {
        BTreeValidator.on = on;
    }

    public static void checkDuplicateChildren(IBTreeNode node1, IBTreeNode node2) {
        if (!on) {
            return;
        }
        for (int i = 0; i < node1.getNbChildren(); i++) {
            IBTreeNode child1 = node1.getChildAt(i, true);
            for (int j = 0; j < node2.getNbChildren(); j++) {
                if (child1 == node2.getChildAt(j, true)) {
                    throw new BTreeNodeValidationException("Duplicated node : " + child1);
                }
            }
        }
    }

    public static void validateNode(IBTreeNode node, boolean isRoot) {
        if (!on) {
            return;
        }
        validateNode(node);
        if (isRoot && node.hasParent()) {
            throw new BTreeNodeValidationException("Root node with a parent: " + node.toString());
        }
        if (!isRoot && !node.hasParent()) {
            throw new BTreeNodeValidationException("Internal node without parent: " + node.toString());
        }
    }

    public static void validateNode(IBTreeNode node) {
        if (!on) {
            return;
        }
        int nbKeys = node.getNbKeys();
        if (node.hasParent() && nbKeys < node.getDegree() - 1) {
            throw new BTreeNodeValidationException("Node with less than " + (node.getDegree() - 1) + " keys");
        }
        int maxNbKeys = node.getDegree() * 2 - 1;
        int nbChildren = node.getNbChildren();
        int maxNbChildren = node.getDegree() * 2;
        if (nbChildren != 0 && nbKeys == 0) {
            throw new BTreeNodeValidationException("Node with no key but with children : " + node);
        }
        for (int i = 0; i < nbKeys; i++) {
            if (node.getKeyAndValueAt(i) == null) {
                throw new BTreeNodeValidationException("Null key at " + i + " on node " + node.toString());
            }
            checkValuesOfChild(node.getKeyAndValueAt(i), node.getChildAt(i, false));
        }
        for (int i = nbKeys; i < maxNbKeys; i++) {
            if (node.getKeyAndValueAt(i) != null) {
                throw new BTreeNodeValidationException("Not Null key at " + i + " on node " + node.toString());
            }
        }
        IBTreeNode previousNode = null;
        for (int i = 0; i < nbChildren; i++) {
            if (node.getChildAt(i, false) == null) {
                throw new BTreeNodeValidationException("Null child at index " + i + " on node " + node.toString());
            }
            if (previousNode != null && previousNode == node.getChildAt(i, false)) {
                throw new BTreeNodeValidationException("Two equals children at index " + i + " : " + previousNode.toString());
            }
            previousNode = node.getChildAt(i, false);
        }
        for (int i = nbChildren; i < maxNbChildren; i++) {
            if (node.getChildAt(i, false) != null) {
                throw new BTreeNodeValidationException("Not Null child at " + i + " on node " + node.toString());
            }
        }
    }

    private static void checkValuesOfChild(IKeyAndValue key, IBTreeNode node) {
        if (!on) {
            return;
        }
        if (node == null) {
            return;
        }
        for (int i = 0; i < node.getNbKeys(); i++) {
            if (node.getKeyAndValueAt(i).getKey().compareTo(key.getKey()) >= 0) {
                throw new BTreeNodeValidationException("Left child with values bigger than pivot " + key + " : " + node.toString());
            }
        }
    }

    public static boolean searchKey(Comparable key, IBTreeNodeOneValuePerKey node) {
        if (!on) {
            return false;
        }
        for (int i = 0; i < node.getNbKeys(); i++) {
            if (node.getKeyAndValueAt(i).getKey().compareTo(key) == 0) {
                return true;
            }
        }
        return false;
    }
}
