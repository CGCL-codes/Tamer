package org.tanso.fountain.router.testdemo;

import java.util.Iterator;
import java.util.LinkedList;
import org.tanso.fountain.router.util.*;

/**
 * The class is uesd to test ParaseNodeTreeXML class.
 * @author Song Huanhuan
 *
 */
public class TestXMLParase {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        String fileName = "./conf/node_tree.xml";
        ParaseNodeTreeXML xmlParase = new ParaseNodeTreeXML(fileName);
        xmlParase.createNodeTree();
        TreeOperation tree = TreeOperation.getTreeOperation();
        new TestXMLParase().TravalTree(tree.getRoot());
    }

    private void TravalTree(TreeNode root) {
        LinkedList<TreeNode> childNodes = null;
        if (root == null) {
            return;
        } else {
            System.out.println(root.selfInfo.getID());
            childNodes = root.childNodes;
            if (childNodes != null) {
                Iterator<TreeNode> it = childNodes.iterator();
                while (it.hasNext()) {
                    TravalTree(it.next());
                }
            }
        }
    }
}
