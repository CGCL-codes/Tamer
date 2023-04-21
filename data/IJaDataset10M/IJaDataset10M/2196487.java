package uk.ac.imperial.ma.metric.computerese.classic;

public class Algebra {

    public Algebra() {
    }

    public static TreeNode substitute(TreeNode oldTree, String oldVariable, TreeNode newSubtree) throws Exception {
        TreeNode newTree;
        if (oldTree == null) newTree = null; else if (((String) (oldTree.getContents())).equals(oldVariable)) newTree = new TreeNode(newSubtree.getContents(), newSubtree.getLeft(), newSubtree.getRight()); else if (!oldTree.hasChild()) newTree = new TreeNode(oldTree.getContents(), null, null); else newTree = new TreeNode(oldTree.getContents(), substitute(oldTree.getLeft(), oldVariable, newSubtree), substitute(oldTree.getRight(), oldVariable, newSubtree));
        return newTree;
    }

    public static Parser substitute(Parser oldParser, String oldVariable, Parser newSubparser, String[] vars) throws Exception {
        TreeNode newTree = substitute(oldParser.getTree(), oldVariable, newSubparser.getTree());
        return new Parser(newTree, vars);
    }

    public static Parser substitute(Parser oldParser, String oldVariable, Parser newSubparser) throws Exception {
        TreeNode newTree = substitute(oldParser.getTree(), oldVariable, newSubparser.getTree());
        return new Parser(newTree);
    }

    public static String substitute(String oldString, String oldVariable, String newSubstring) throws Exception {
        Parser oldParser = new Parser(oldString);
        Parser newSubparser = new Parser(newSubstring);
        TreeNode oldTree = oldParser.getTree();
        TreeNode newSubtree = newSubparser.getTree();
        TreeNode newTree = substitute(oldTree, oldVariable, newSubtree);
        return TreeFormatter.simpleExpressionString(newTree);
    }

    private static TreeNode simpleCombine(String oper, TreeNode leftTree, TreeNode rightTree) {
        return new TreeNode(oper, leftTree, rightTree);
    }

    private static TreeNode numericCombine(String oper, double leftDouble, double rightDouble) throws Exception {
        double answerDouble;
        if (oper.equals("plus")) answerDouble = leftDouble + rightDouble; else if (oper.equals("subtract")) answerDouble = leftDouble - rightDouble; else if (oper.equals("times")) answerDouble = leftDouble * rightDouble; else if (oper.equals("divide")) answerDouble = leftDouble / rightDouble; else if (oper.equals("power")) answerDouble = Math.pow(leftDouble, rightDouble); else throw new Exception();
        return new TreeNode(("" + answerDouble), null, null);
    }

    private static boolean nullRightOperation(String oper, TreeNode rightTree) {
        String rightContents = (String) (rightTree.getContents());
        boolean childless = !rightTree.hasChild();
        boolean null0 = (oper.equals("plus") || oper.equals("subtract")) && (rightContents.equals("0") || rightContents.equals("0.0"));
        boolean null1 = (oper.equals("times") || oper.equals("divide") || oper.equals("power")) && (rightContents.equals("1") || rightContents.equals("1.0"));
        return (childless && (null0 || null1));
    }

    private static boolean nullLeftOperation(String oper, TreeNode leftTree) {
        String leftContents = (String) (leftTree.getContents());
        boolean childless = !leftTree.hasChild();
        boolean null0 = (oper.equals("plus")) && (leftContents.equals("0") || leftContents.equals("0.0"));
        boolean null1 = (oper.equals("times")) && (leftContents.equals("1") || leftContents.equals("1.0"));
        return (childless && (null0 || null1));
    }

    private static boolean equalsZero(String oper, TreeNode leftTree, TreeNode rightTree) {
        String leftContents = (String) (leftTree.getContents());
        String rightContents = (String) (rightTree.getContents());
        boolean leftChildless = !leftTree.hasChild();
        boolean rightChildless = !rightTree.hasChild();
        boolean leftZero = (leftContents.equals("0") || leftContents.equals("0.0"));
        boolean rightZero = (rightContents.equals("0") || rightContents.equals("0.0"));
        return oper.equals("times") && ((leftChildless && leftZero) || (rightChildless && rightZero));
    }

    private static TreeNode nonNumericCombine(String oper, TreeNode leftTree, TreeNode rightTree) throws Exception {
        if (nullRightOperation(oper, rightTree)) return leftTree; else if (nullLeftOperation(oper, leftTree)) return rightTree; else if (equalsZero(oper, leftTree, rightTree)) return new TreeNode("0", null, null); else return simpleCombine(oper, leftTree, rightTree);
    }

    public static TreeNode combine(String oper, TreeNode leftTree, TreeNode rightTree) throws Exception {
        oper = oper.toLowerCase();
        String leftContents = (String) (leftTree.getContents());
        String rightContents = (String) (rightTree.getContents());
        if (!leftTree.hasChild() && !rightTree.hasChild()) {
            try {
                double leftDouble = (new Double(leftContents)).doubleValue();
                double rightDouble = (new Double(rightContents)).doubleValue();
                return numericCombine(oper, leftDouble, rightDouble);
            } catch (NumberFormatException nfe) {
                return nonNumericCombine(oper, leftTree, rightTree);
            }
        } else return nonNumericCombine(oper, leftTree, rightTree);
    }

    public static Parser combine(String oper, Parser leftParser, Parser rightParser, String[] vars) throws Exception {
        TreeNode newTree = combine(oper, leftParser.getTree(), rightParser.getTree());
        return new Parser(newTree, vars);
    }

    public static Parser combine(String oper, Parser leftParser, Parser rightParser) throws Exception {
        TreeNode newTree = combine(oper, leftParser.getTree(), rightParser.getTree());
        return new Parser(newTree);
    }

    public static String combine(String oper, String leftString, String rightString) throws Exception {
        Parser leftParser = new Parser(leftString);
        Parser rightParser = new Parser(rightString);
        TreeNode leftTree = leftParser.getTree();
        TreeNode rightTree = rightParser.getTree();
        TreeNode newTree = combine(oper, leftTree, rightTree);
        return TreeFormatter.simpleExpressionString(newTree);
    }
}
