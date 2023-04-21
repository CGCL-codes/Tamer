package net.sf.clairv.search.pattern.bnf;

import net.sf.clairv.search.pattern.PatternContext;

/**
 * @author qiuyin
 *
 */
public class AstTree {

    AstNode root;

    public AstTree(AstNode root) {
        this.root = root;
    }

    public AstNode getRoot() {
        return root;
    }

    public String evaluate(PatternContext context) {
        if (root != null) {
            return root.evaluate(context).toString();
        } else {
            return null;
        }
    }

    public void walk() {
        walkRecursive(root, 0);
    }

    private void walkRecursive(AstNode node, int indent) {
        for (int i = 0; i < indent; i++) {
            System.out.print("\t");
        }
        System.out.println(node.toString());
        AstNode[] children = node.children();
        for (int i = 0; i < children.length; i++) {
            walkRecursive(children[i], indent + 1);
        }
    }
}
