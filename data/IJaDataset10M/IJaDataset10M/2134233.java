package firegui;

public class Tree {

    Node root;

    int totalnodes = 0;

    int leafNodes = 0;

    Tree() {
        root = null;
    }

    public Tree(Node r) {
        root = r;
    }

    public void computeNodePositions(Node root, int prevXPos) {
        int YSCALE = 200;
        if (root != null) {
            root.setXpos(root.getStartingPos() + prevXPos);
            root.setYpos(root.getDepth() * YSCALE);
            for (int i = 0; i < root.getChildren().size(); i++) {
                computeNodePositions(root.getChildren().get(i), root.getXpos());
            }
        }
    }

    public void treeWidth(Node root) {
        if (root.getChildren().size() == 0) {
            root.setWidth(300);
        } else {
            int nodeSize = 0;
            for (int i = 0; i < root.getChildren().size(); i++) {
                treeWidth(root.getChildren().get(i));
                nodeSize += root.getChildren().get(i).getWidth();
                if (i == 0) {
                    root.getChildren().get(i).setStartingPos(0);
                } else {
                    root.getChildren().get(i).setStartingPos(root.getChildren().get(i - 1).getStartingPos() + root.getChildren().get(i - 1).getWidth());
                }
            }
            root.setWidth(nodeSize);
        }
    }

    public void inorder_traversal(Node t, int depth, int siblings, int numChild, int range1, int range2) {
        if (t != null) {
            t.setXpos((range2 - range1) / 2 + range1);
            t.setYpos(depth);
            for (int i = 0; i < t.getChildren().size(); i++) {
                int childSpace = (range2 - range1) / (t.getChildren().size());
                int childRangeLow = range1 + i * childSpace;
                int childRangeHi = range1 + (i + 1) * childSpace;
                inorder_traversal(t.getChildren().get(i), depth + 1, t.getChildren().size(), i, childRangeLow, childRangeHi);
            }
        }
    }

    public void countLeaves(Node root) {
        if (root != null) {
            if (root.getChildren().size() == 0) {
                leafNodes++;
            } else {
                for (int i = 0; i < root.getChildren().size(); i++) {
                    if (root.getChildren().get(i) != null) {
                        countLeaves(root.getChildren().get(i));
                    }
                }
            }
        }
    }
}
