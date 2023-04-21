package debug;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import org.deft.repository.ast.TreeNode;
import org.deft.repository.ast.annotation.Templates;
import org.deft.repository.ast.annotation.astname.AstNameInformation;

public class DeftASTFrame extends JFrame {

    public DeftASTFrame(TreeNode tree) {
        super("TreeViewer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setup(tree);
    }

    private void setup(TreeNode tree) {
        AntlrASTModel model = new AntlrASTModel(tree);
        JTree jtree = new MyJTree(model);
        setContentPane(new JScrollPane(jtree));
    }

    private class AntlrASTModel implements TreeModel {

        private TreeNode root;

        public AntlrASTModel(TreeNode root) {
            this.root = root;
        }

        public Object getRoot() {
            return root;
        }

        public Object getChild(Object parent, int index) {
            TreeNode p = (TreeNode) parent;
            return p.getChildren().get(index);
        }

        public int getChildCount(Object parent) {
            TreeNode p = (TreeNode) parent;
            return p.getChildren().size();
        }

        public boolean isLeaf(Object node) {
            TreeNode n = (TreeNode) node;
            return n.getChildren().size() == 0;
        }

        public int getIndexOfChild(Object parent, Object child) {
            TreeNode p = (TreeNode) parent;
            for (int i = 0; i < p.getChildren().size(); i++) {
                if (p.getChildren().get(i) == child) {
                    return i;
                }
            }
            return -1;
        }

        public void addTreeModelListener(TreeModelListener l) {
        }

        public void removeTreeModelListener(TreeModelListener l) {
        }

        public void valueForPathChanged(TreePath path, Object newValue) {
        }
    }

    private class MyJTree extends JTree {

        public MyJTree(TreeModel root) {
            super(root);
        }

        public String convertValueToText(Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            TreeNode node = (TreeNode) value;
            AstNameInformation nameInfo = (AstNameInformation) node.getInformation(Templates.ASTNAME);
            return node.getName() + (nameInfo == null ? "" : " <" + nameInfo.getAstName() + ">") + " [" + node.getOffset() + " - " + node.getEndOffset() + "]" + " [" + node.getStartLine() + ":" + node.getStartCol() + "-" + node.getEndLine() + ":" + node.getEndCol() + "]";
        }
    }
}
