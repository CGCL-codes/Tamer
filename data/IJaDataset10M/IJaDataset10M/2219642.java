package uk.ac.imperial.ma.metric.parsing;

public class TreeSimplification {

    public TreeSimplification() {
    }

    public static TreeNode simplify1(TreeNode inTree) {
        if (inTree.equals(null)) return null;
        String inContents = (String) (inTree.getContents());
        if (!inTree.hasChild()) return new TreeNode(inContents, null, null);
        if (!inTree.isBinary()) return new TreeNode(inContents, simplify1(inTree.getLeft()), null);
        TreeNode inLeft = inTree.getLeft();
        TreeNode inRight = inTree.getRight();
        if (inContents.equals("times") || inContents.equals("visibletimes")) {
            String inLeftString = (String) (inLeft.getContents());
            String inRightString = (String) (inRight.getContents());
            if (inLeftString.equals("1~")) return new TreeNode(simplify1(inRight));
            if (inRightString.equals("1~")) return new TreeNode(simplify1(inLeft));
            if (inLeftString.equals("subtract")) {
                TreeNode inLeftLeft = inLeft.getLeft();
                TreeNode inLeftRight = inLeft.getRight();
                String inLeftLeftString = (String) (inLeftLeft.getContents());
                String inLeftRightString = (String) (inLeftRight.getContents());
                if ((inLeftLeftString.equals("") || inLeftLeftString.equals("0~")) && inLeftRightString.equals("1~")) return new TreeNode("subtract", new TreeNode("", null, null), simplify1(inRight));
            }
            if (inRightString.equals("subtract")) {
                TreeNode inRightLeft = inRight.getLeft();
                TreeNode inRightRight = inRight.getRight();
                String inRightLeftString = (String) (inRightLeft.getContents());
                String inRightRightString = (String) (inRightRight.getContents());
                if ((inRightLeftString.equals("") || inRightLeftString.equals("0~")) && inRightRightString.equals("1~")) return new TreeNode("subtract", new TreeNode("", null, null), simplify1(inLeft));
            }
        }
        if (inContents.equals("divide")) {
            String inLeftString = (String) (inLeft.getContents());
            String inRightString = (String) (inRight.getContents());
            if (inRightString.equals("1~")) return new TreeNode(simplify1(inLeft));
            if (inLeftString.equals("subtract")) {
                TreeNode inLeftLeft = inLeft.getLeft();
                TreeNode inLeftRight = inLeft.getRight();
                String inLeftLeftString = (String) (inLeftLeft.getContents());
                String inLeftRightString = (String) (inLeftRight.getContents());
                if ((inLeftLeftString.equals("") || inLeftLeftString.equals("0~")) && inLeftRightString.equals("1~")) {
                    TreeNode newRight = new TreeNode("divide", new TreeNode("1~", null, null), inRight);
                    return new TreeNode("subtract", new TreeNode("", null, null), simplify1(newRight));
                }
            }
            if (inRightString.equals("subtract")) {
                TreeNode inRightLeft = inRight.getLeft();
                TreeNode inRightRight = inRight.getRight();
                String inRightLeftString = (String) (inRightLeft.getContents());
                String inRightRightString = (String) (inRightRight.getContents());
                if ((inRightLeftString.equals("") || inRightLeftString.equals("0~")) && inRightRightString.equals("1~")) return new TreeNode("subtract", new TreeNode("", null, null), simplify1(inLeft));
            }
        }
        if (inContents.equals("power")) {
            String inLeftString = (String) (inLeft.getContents());
            String inRightString = (String) (inRight.getContents());
            if (inLeftString.equals("1~")) return new TreeNode("1~", null, null);
            if (inRightString.equals("1~")) return new TreeNode(simplify1(inLeft));
        }
        return new TreeNode(inContents, simplify1(inLeft), simplify1(inRight));
    }

    public static TreeNode oneStepSimplifyPlusMinus(TreeNode inTree) {
        if (inTree.equals(null)) return null;
        String inContents = (String) (inTree.getContents());
        if (!inTree.hasChild()) return new TreeNode(inContents, null, null);
        if (!inTree.isBinary()) return new TreeNode(inContents, oneStepSimplifyPlusMinus(inTree.getLeft()), null);
        TreeNode inLeft = inTree.getLeft();
        TreeNode inRight = inTree.getRight();
        if (inContents.equals("times")) {
            String inLeftString = (String) (inLeft.getContents());
            String inRightString = (String) (inRight.getContents());
            if (inLeftString.equals("subtract")) {
                TreeNode inLeftLeft = inLeft.getLeft();
                TreeNode inLeftRight = inLeft.getRight();
                String inLeftLeftString = (String) (inLeftLeft.getContents());
                if (inLeftLeftString.equals("") || inLeftLeftString.equals("0~")) {
                    TreeNode newRight = new TreeNode("times", inLeftRight, inRight);
                    return new TreeNode("subtract", new TreeNode("", null, null), oneStepSimplifyPlusMinus(newRight));
                }
            }
            if (inRightString.equals("subtract")) {
                TreeNode inRightLeft = inRight.getLeft();
                TreeNode inRightRight = inRight.getRight();
                String inRightLeftString = (String) (inRightLeft.getContents());
                if (inRightLeftString.equals("") || inRightLeftString.equals("0~")) {
                    TreeNode newRight = new TreeNode("times", inLeft, inRightRight);
                    return new TreeNode("subtract", new TreeNode("", null, null), oneStepSimplifyPlusMinus(newRight));
                }
            }
        }
        if (inContents.equals("divide")) {
            String inLeftString = (String) (inLeft.getContents());
            String inRightString = (String) (inRight.getContents());
            if (inLeftString.equals("subtract")) {
                TreeNode inLeftLeft = inLeft.getLeft();
                TreeNode inLeftRight = inLeft.getRight();
                String inLeftLeftString = (String) (inLeftLeft.getContents());
                if (inLeftLeftString.equals("") || inLeftLeftString.equals("0~")) {
                    TreeNode newRight = new TreeNode("divide", inLeftRight, inRight);
                    return new TreeNode("subtract", new TreeNode("", null, null), oneStepSimplifyPlusMinus(newRight));
                }
            }
            if (inRightString.equals("subtract")) {
                TreeNode inRightLeft = inRight.getLeft();
                TreeNode inRightRight = inRight.getRight();
                String inRightLeftString = (String) (inRightLeft.getContents());
                if (inRightLeftString.equals("") || inRightLeftString.equals("0~")) {
                    TreeNode newRight = new TreeNode("divide", inLeft, inRightRight);
                    return new TreeNode("subtract", new TreeNode("", null, null), oneStepSimplifyPlusMinus(newRight));
                }
            }
        }
        if (inContents.equals("plus")) {
            String inRightString = (String) (inRight.getContents());
            if (inRightString.equals("subtract")) {
                TreeNode inRightLeft = inRight.getLeft();
                TreeNode inRightRight = inRight.getRight();
                String inRightLeftString = (String) (inRightLeft.getContents());
                if (inRightLeftString.equals("") || inRightLeftString.equals("0~")) {
                    return new TreeNode("subtract", oneStepSimplifyPlusMinus(inLeft), oneStepSimplifyPlusMinus(inRightRight));
                }
            }
        }
        if (inContents.equals("subtract")) {
            String inLeftString = (String) (inLeft.getContents());
            String inRightString = (String) (inRight.getContents());
            if (inRightString.equals("subtract")) {
                TreeNode inRightLeft = inRight.getLeft();
                TreeNode inRightRight = inRight.getRight();
                String inRightLeftString = (String) (inRightLeft.getContents());
                if (inRightLeftString.equals("") || inRightLeftString.equals("0~")) {
                    if (inLeftString.equals("")) return new TreeNode(oneStepSimplifyPlusMinus(inRightRight)); else return new TreeNode("plus", oneStepSimplifyPlusMinus(inLeft), oneStepSimplifyPlusMinus(inRightRight));
                }
            }
        }
        return new TreeNode(inContents, oneStepSimplifyPlusMinus(inLeft), oneStepSimplifyPlusMinus(inRight));
    }

    public static TreeNode oneStepSimplify0(TreeNode inTree) {
        if (inTree.equals(null)) return null;
        String inContents = (String) (inTree.getContents());
        if (!inTree.hasChild()) return new TreeNode(inContents, null, null);
        if (!inTree.isBinary()) return new TreeNode(inContents, oneStepSimplify0(inTree.getLeft()), null);
        TreeNode inLeft = inTree.getLeft();
        TreeNode inRight = inTree.getRight();
        if (inContents.equals("times") || inContents.equals("visibletimes")) {
            String inLeftString = (String) (inLeft.getContents());
            String inRightString = (String) (inRight.getContents());
            if (inLeftString.equals("0~")) return new TreeNode("0~", null, null);
            if (inRightString.equals("0~")) return new TreeNode("0~", null, null);
        }
        if (inContents.equals("divide")) {
            String inLeftString = (String) (inLeft.getContents());
            String inRightString = (String) (inRight.getContents());
            if (inLeftString.equals("0~")) return new TreeNode("0~", null, null);
        }
        if (inContents.equals("plus")) {
            String inLeftString = (String) (inLeft.getContents());
            String inRightString = (String) (inRight.getContents());
            if (inLeftString.equals("0~") || inLeftString.equals("")) return new TreeNode(oneStepSimplify0(inRight));
            if (inRightString.equals("0~") || inRightString.equals("")) return new TreeNode(oneStepSimplify0(inLeft));
        }
        if (inContents.equals("subtract")) {
            String inLeftString = (String) (inLeft.getContents());
            String inRightString = (String) (inRight.getContents());
            if (inLeftString.equals("0~") || inLeftString.equals("")) return new TreeNode("subtract", new TreeNode("", null, null), oneStepSimplify0(inRight));
            if (inRightString.equals("0~") || inRightString.equals("")) return new TreeNode(oneStepSimplify0(inLeft));
        }
        if (inContents.equals("power")) {
            String inLeftString = (String) (inLeft.getContents());
            String inRightString = (String) (inRight.getContents());
            if (inLeftString.equals("0~")) return new TreeNode("0~", null, null);
            if (inRightString.equals("0~")) return new TreeNode("1~", null, null);
        }
        return new TreeNode(inContents, oneStepSimplify0(inLeft), oneStepSimplify0(inRight));
    }

    public static TreeNode simplifyPlusMinus(TreeNode inTree) {
        TreeNode outTree = oneStepSimplifyPlusMinus(inTree);
        while (!TreeFormatter.simpleExpressionString(outTree).equals(TreeFormatter.simpleExpressionString(inTree))) {
            inTree = outTree;
            outTree = oneStepSimplifyPlusMinus(inTree);
        }
        return outTree;
    }

    public static TreeNode simplify0(TreeNode inTree) {
        TreeNode outTree = oneStepSimplify0(inTree);
        while (!TreeFormatter.simpleExpressionString(outTree).equals(TreeFormatter.simpleExpressionString(inTree))) {
            inTree = outTree;
            outTree = oneStepSimplify0(inTree);
        }
        return outTree;
    }

    public static TreeNode simplify(TreeNode inTree) {
        return simplifyPlusMinus(simplify1(simplify0(inTree)));
    }
}
