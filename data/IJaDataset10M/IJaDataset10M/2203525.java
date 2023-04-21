package simpleorm.simpleweb.eg.simple;

import simpleorm.simpleweb.core.WTree;
import simpleorm.simpleweb.core.WPage;
import simpleorm.simpleweb.core.WPagelet;

public class WTreePage extends WPage {

    public final WTreePagelet pagelet = new WTreePagelet(this);

    public WTree getTree() {
        return pagelet.tree;
    }

    public static class WTreePagelet extends WPagelet {

        WTree tree = new WTree();

        public WTreePagelet(WPage wpage) {
            super(wpage, "tree");
        }

        protected void onPreMaybeSubmitted() {
            WTree.WTreeNode p = tree.addNode(null, "First", "treetest.jsp");
            WTree.WTreeNode s = tree.addNode(p, "Sub1", "treetest.jsp");
            tree.addNode(s, "SubSub", "treetest.jsp");
            tree.addNode(p, "Sub2", "treetest.jsp");
            tree.addNode(null, "Second", "treetest.jsp");
        }
    }
}
