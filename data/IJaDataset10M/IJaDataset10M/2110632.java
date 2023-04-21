package persistence.antlr;

import persistence.antlr.collections.AST;

public class ASTIterator {

    protected AST cursor = null;

    protected AST original = null;

    public ASTIterator(AST t) {
        original = cursor = t;
    }

    /** Is 'sub' a subtree of 't' beginning at the root? */
    public boolean isSubtree(AST t, AST sub) {
        AST sibling;
        if (sub == null) {
            return true;
        }
        if (t == null) {
            if (sub != null) return false;
            return true;
        }
        for (sibling = t; sibling != null && sub != null; sibling = sibling.getNextSibling(), sub = sub.getNextSibling()) {
            if (sibling.getType() != sub.getType()) return false;
            if (sibling.getFirstChild() != null) {
                if (!isSubtree(sibling.getFirstChild(), sub.getFirstChild())) return false;
            }
        }
        return true;
    }

    /** Find the next subtree with structure and token types equal to
     * those of 'template'.
     */
    public AST next(AST template) {
        AST t = null;
        AST sibling = null;
        if (cursor == null) {
            return null;
        }
        for (; cursor != null; cursor = cursor.getNextSibling()) {
            if (cursor.getType() == template.getType()) {
                if (cursor.getFirstChild() != null) {
                    if (isSubtree(cursor.getFirstChild(), template.getFirstChild())) {
                        return cursor;
                    }
                }
            }
        }
        return t;
    }
}
