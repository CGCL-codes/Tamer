package org.ucl.xpath.ast;

import java.util.*;

/**
 * Abstract class for a Binary operation.
 */
public abstract class BinExpr extends Expr {

    private XPathNode _left;

    private XPathNode _right;

    /**
	 * Constructor for BinExpr.
	 * @param l left xpath node for the operation.
	 * @param r right xpath node for the operation.
	 */
    public BinExpr(XPathNode l, XPathNode r) {
        _left = l;
        _right = r;
    }

    /**
	 * Left xpath node.
	 * @return Left node.
	 */
    public XPathNode left() {
        return _left;
    }

    /**
	 * Right xpath node.
	 * @return Right node.
	 */
    public XPathNode right() {
        return _right;
    }

    /**
	 * Set the left xpath node.
	 * @param n Left node.
	 */
    public void set_left(XPathNode n) {
        _left = n;
    }

    /**
	 * Set the right xpath node.
	 * @param n Right node.
	 */
    public void set_right(XPathNode n) {
        _right = n;
    }
}
