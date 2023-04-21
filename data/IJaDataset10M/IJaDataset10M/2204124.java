package com.tivo.jipviewer;

import java.awt.BorderLayout;
import java.awt.Container;
import java.util.Enumeration;
import javax.swing.JTree;
import com.tivo.jipviewer.ByPackageViewer.TreeNode;

public class ByBundleViewer extends Container {

    private static final long serialVersionUID = 1L;

    private static JTree mBundleTree;

    ByBundleViewer(JipRun run) {
        setLayout(new BorderLayout());
        add(makeTree(run), BorderLayout.CENTER);
    }

    static JTree makeTree(JipRun run) {
        TreeNode root = makeBundleTree(run);
        mBundleTree = new JTree(root);
        return mBundleTree;
    }

    public static JTree getTree() {
        return mBundleTree;
    }

    private static TreeNode makeBundleTree(JipRun run) {
        TreeNode root = new TreeNode("root");
        for (Long threadId : run.threads()) {
            for (JipFrame f : run.interactions(threadId)) {
                visitFrameForBundling(root, f);
            }
        }
        root.computeTotals();
        return root;
    }

    private static void visitFrameForBundling(TreeNode root, JipFrame frame) {
        String bundleName = frame.getMethod().getBundleName();
        String className = frame.getMethod().getClassName();
        TreeNode node = findOrCreateBundleNode(root, bundleName, className);
        node.addFrame(frame);
        for (JipFrame childFrame : frame.getChildren()) {
            visitFrameForBundling(root, childFrame);
        }
    }

    private static TreeNode findOrCreateBundleNode(TreeNode rootNode, String bundleName, String className) {
        TreeNode bundleNode = findOrCreateNode(rootNode, bundleName);
        return findOrCreateNode(bundleNode, className);
    }

    private static TreeNode findOrCreateNode(TreeNode node, String nodeName) {
        if (nodeName.equals("")) {
            return node;
        }
        for (Enumeration kids = node.children(); kids.hasMoreElements(); ) {
            TreeNode kid = (TreeNode) kids.nextElement();
            if (kid.getName().equals(nodeName)) {
                return findOrCreateNode(kid, "");
            }
        }
        TreeNode newKid = new TreeNode(nodeName);
        node.add(newKid);
        return findOrCreateNode(newKid, "");
    }
}
