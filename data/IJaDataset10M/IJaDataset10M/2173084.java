package com.apelon.dts.db.subset.expression;

import com.apelon.graph.dag.Node;
import com.apelon.common.log4j.Categories;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * <p>Title:  TreePathWalker </p>
 * <p>Description:  A tree traversal class which keeps a list of all its descandants
 * and list of nodes at each level for easy query building. </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Apelon Inc. </p>
 *
 * @author Apelon Inc.
 * @version DTS 3.4.0
 * @since 3.4.0
 */
public class TreePathWalker {

    public static int INCLUDE_BRANCH_TYPE = 1;

    public static int EXCLUDE_BRANCH_TYPE = 2;

    public TreePathWalker(String identifier, Node rootNode, int branchType) {
        this.identifier = identifier;
        this.rootNode = rootNode;
        this.branchType = branchType;
    }

    public void walk() {
        nodes = new ArrayList(5);
        this.incLevelNodes = new HashMap(5);
        this.excLevelNodes = new HashMap(5);
        this.getChildren(rootNode, 0);
        print();
    }

    public int getType() {
        return this.branchType;
    }

    private void getChildren(Node node, int level) {
        ArrayList rootArray = new ArrayList(5);
        Object rootMetaObj = (node.metaData()).getMetaObject();
        rootArray.add(rootMetaObj);
        this.incLevelNodes.put(new Integer(level), rootArray);
        Object nodeMetaObj = (node.metaData()).getMetaObject();
        ArrayList children = node.children();
        Categories.dataDb().debug(Utilities.numOfIndent(level) + "# of siblings for node [" + Utilities.getText(nodeMetaObj) + "] = " + children.size() + " at level " + level);
        for (int i = 0; i < children.size(); i++) {
            Node nextNode = (Node) children.get(i);
            Object metaObj = (nextNode.metaData()).getMetaObject();
            nodes.add(metaObj);
            Categories.dataDb().debug(Utilities.numOfIndent(level) + "Node(c) = " + " [" + Utilities.getText(metaObj) + "]");
            boolean excluded = false;
            if (metaObj instanceof String) {
                if (metaObj.toString().equals("EXCLUDE")) {
                    excluded = true;
                }
            }
            saveChildren(level + 1, metaObj, excluded);
            computeChildren(level + 1, nextNode, excluded);
        }
    }

    private void computeChildren(int level, Node node, boolean excluded) {
        ArrayList children = node.children();
        for (int i = 0; i < children.size(); i++) {
            Node nextNode = (Node) children.get(i);
            Object metaObj = (nextNode.metaData()).getMetaObject();
            nodes.add(metaObj);
            Categories.dataDb().debug(Utilities.numOfIndent(level) + "Node(c) = " + " [" + Utilities.getText(metaObj) + "]");
            if (metaObj instanceof String) {
                if (metaObj.toString().equals("EXCLUDE")) {
                    excluded = true;
                }
                saveChildren(level + 1, metaObj, excluded);
            } else {
                saveChildren(level + 1, metaObj, excluded);
            }
            computeChildren(level + 1, nextNode, excluded);
        }
    }

    private void saveChildren(int level, Object node, boolean excluded) {
        HashMap typeList = null;
        if (!excluded) {
            typeList = this.incLevelNodes;
        } else {
            typeList = this.excLevelNodes;
        }
        ArrayList allNodesAtCurrentLevel = (ArrayList) typeList.get(new Integer(level));
        if (allNodesAtCurrentLevel == null || (allNodesAtCurrentLevel.size() == 0)) {
            allNodesAtCurrentLevel = new ArrayList(5);
            allNodesAtCurrentLevel.add(node);
            typeList.put(new Integer(level), allNodesAtCurrentLevel);
        } else {
            allNodesAtCurrentLevel.add(node);
            typeList.put(new Integer(level), allNodesAtCurrentLevel);
        }
    }

    public ArrayList getDescandants() {
        return this.nodes;
    }

    public HashMap getLevelNodesMap() {
        return this.incLevelNodes;
    }

    public HashMap getExcLevelNodesMap() {
        return this.excLevelNodes;
    }

    public String toString() {
        return this.identifier;
    }

    private void print() {
        Categories.dataDb().debug("\n~~~~~~~~~ Tree Walker View (in reverse order) ~~~~~~~~~~~~~~~");
        int maxLevels = getLevelNodesMap().size();
        for (int i = maxLevels; i >= 0; i--) {
            ArrayList al = (ArrayList) this.incLevelNodes.get(new Integer(i));
            if (al != null) {
                for (int n = 0; n < al.size(); n++) {
                    Categories.dataDb().debug(Utilities.numOfIndent(i) + "Node=" + Utilities.getText(al.get(n)) + " at level [" + i + "]");
                }
            }
        }
        Categories.dataDb().debug("~~~~~~~~~~~~~~~~~~~~~~~~\n");
    }

    private String identifier = "";

    private ArrayList nodes = null;

    private Node rootNode = null;

    private HashMap incLevelNodes = null;

    private HashMap excLevelNodes = null;

    private int branchType = -1;
}
