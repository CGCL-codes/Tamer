package java5parsingCheckstyleGrammar;

import java.util.BitSet;
import antlr.CommonAST;
import antlr.Token;
import antlr.collections.AST;

/**
 * An extension of the CommonAST that records the line and column
 * number.  The idea was taken from <a target="_top"
 * href="http://www.jguru.com/jguru/faq/view.jsp?EID=62654">Java Guru
 * FAQ: How can I include line numbers in automatically generated
 * ASTs?</a>.
 * @author Oliver Burn
 * @author lkuehne
 * @version 1.0
 * @see <a href="http://www.antlr.org/">ANTLR Website</a>
 */
public final class DetailAST extends CommonAST {

    /** constant to indicate if not calculated the child count */
    private static final int NOT_INITIALIZED = Integer.MIN_VALUE;

    /** the line number **/
    private int mLineNo = NOT_INITIALIZED;

    /** the column number **/
    private int mColumnNo = NOT_INITIALIZED;

    /** number of children */
    private int mChildCount = NOT_INITIALIZED;

    /** the parent token */
    private DetailAST mParent;

    /** previous sibling */
    private DetailAST mPreviousSibling;

    /**
     * All token types in this branch.
     * Token 'x' (where x is an int) is in this branch
     * if mBranchTokenTypes.get(x) is true.
     */
    private BitSet mBranchTokenTypes;

    /** {@inheritDoc} */
    public void initialize(Token aTok) {
        super.initialize(aTok);
        mLineNo = aTok.getLine();
        mColumnNo = aTok.getColumn() - 1;
    }

    /** {@inheritDoc} */
    public void initialize(AST aAST) {
        final DetailAST da = (DetailAST) aAST;
        setText(da.getText());
        setType(da.getType());
        mLineNo = da.getLineNo();
        mColumnNo = da.getColumnNo();
    }

    /**
     * Sets this AST's first Child.
     * @param aAST the new first child
     */
    public void setFirstChild(AST aAST) {
        mChildCount = NOT_INITIALIZED;
        super.setFirstChild(aAST);
        if (aAST != null) {
            ((DetailAST) aAST).setParent(this);
        }
    }

    /**
     * Sets AST's next sibling.
     * @param aAST the new next sibling
     */
    public void setNextSibling(AST aAST) {
        super.setNextSibling(aAST);
        if ((aAST != null) && (mParent != null)) {
            ((DetailAST) aAST).setParent(mParent);
        }
        if (aAST != null) {
            ((DetailAST) aAST).setPreviousSibling(this);
        }
    }

    /**
     * Sets previous sibling.
     * @param aAST a previous sibling
     */
    void setPreviousSibling(DetailAST aAST) {
        mPreviousSibling = aAST;
    }

    /**
     * Adds new child to AST.
     * @param aAST the new child
     */
    public void addChild(AST aAST) {
        super.addChild(aAST);
        if (aAST != null) {
            ((DetailAST) aAST).setParent(this);
            ((DetailAST) getFirstChild()).setParent(this);
        }
    }

    /**
     * Returns the number of child nodes one level below this node. That is is
     * does not recurse down the tree.
     * @return the number of child nodes
     */
    public int getChildCount() {
        if (mChildCount == NOT_INITIALIZED) {
            mChildCount = 0;
            AST child = getFirstChild();
            while (child != null) {
                mChildCount += 1;
                child = child.getNextSibling();
            }
        }
        return mChildCount;
    }

    void setParent(DetailAST aParent) {
        mParent = aParent;
        final DetailAST nextSibling = (DetailAST) getNextSibling();
        if (nextSibling != null) {
            nextSibling.setParent(aParent);
            nextSibling.setPreviousSibling(this);
        }
    }

    /**
     * Returns the parent token.
     * @return the parent token
     */
    public DetailAST getParent() {
        return mParent;
    }

    /** @return the line number **/
    public int getLineNo() {
        if (mLineNo == NOT_INITIALIZED) {
            final DetailAST child = (DetailAST) getFirstChild();
            final DetailAST sibling = (DetailAST) getNextSibling();
            if (child != null) {
                return child.getLineNo();
            } else if (sibling != null) {
                return sibling.getLineNo();
            }
        }
        return mLineNo;
    }

    /** @return the column number **/
    public int getColumnNo() {
        if (mColumnNo == NOT_INITIALIZED) {
            final DetailAST child = (DetailAST) getFirstChild();
            final DetailAST sibling = (DetailAST) getNextSibling();
            if (child != null) {
                return child.getColumnNo();
            } else if (sibling != null) {
                return sibling.getColumnNo();
            }
        }
        return mColumnNo;
    }

    /** @return the last child node */
    public DetailAST getLastChild() {
        AST ast = getFirstChild();
        while ((ast != null) && (ast.getNextSibling() != null)) {
            ast = ast.getNextSibling();
        }
        return (DetailAST) ast;
    }

    /**
     * @return the token types that occur in the branch as a sorted set.
     */
    private BitSet getBranchTokenTypes() {
        if (mBranchTokenTypes == null) {
            mBranchTokenTypes = new BitSet();
            mBranchTokenTypes.set(getType());
            DetailAST child = (DetailAST) getFirstChild();
            while (child != null) {
                final BitSet childTypes = child.getBranchTokenTypes();
                mBranchTokenTypes.or(childTypes);
                child = (DetailAST) child.getNextSibling();
            }
        }
        return mBranchTokenTypes;
    }

    /**
     * Checks if this branch of the parse tree contains a token
     * of the provided type.
     * @param aType a TokenType
     * @return true if and only if this branch (including this node)
     * contains a token of type <code>aType</code>.
     */
    public boolean branchContains(int aType) {
        return getBranchTokenTypes().get(aType);
    }

    /**
     * Returns the number of direct child tokens that have the specified type.
     * @param aType the token type to match
     * @return the number of matching token
     */
    public int getChildCount(int aType) {
        int count = 0;
        for (AST i = getFirstChild(); i != null; i = i.getNextSibling()) {
            if (i.getType() == aType) {
                count++;
            }
        }
        return count;
    }

    /**
     * Returns the previous sibling or null if no such sibling exists.
     * @return the previous sibling or null if no such sibling exists.
     */
    public DetailAST getPreviousSibling() {
        return mPreviousSibling;
    }

    /**
     * Returns the first child token that makes a specified type.
     * @param aType the token type to match
     * @return the matching token, or null if no match
     */
    public DetailAST findFirstToken(int aType) {
        DetailAST retVal = null;
        for (AST i = getFirstChild(); i != null; i = i.getNextSibling()) {
            if (i.getType() == aType) {
                retVal = (DetailAST) i;
                break;
            }
        }
        return retVal;
    }

    /** {@inheritDoc} */
    public String toString() {
        return super.toString() + "[" + getLineNo() + "x" + getColumnNo() + "]";
    }
}
