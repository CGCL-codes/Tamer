package com.googlecode.sarasvati.visitor;

import com.googlecode.sarasvati.ArcToken;
import com.googlecode.sarasvati.NodeToken;

/**
 * Class which provides default implementations in {@link TokenVisitor}.
 *
 * @author Paul Lorenz
 */
public class TokenVisitorAdaptor implements TokenVisitor {

    @Override
    public boolean follow(final ArcToken child) {
        return true;
    }

    @Override
    public void visit(final NodeToken token) {
    }

    @Override
    public void visit(final ArcToken token) {
    }
}
