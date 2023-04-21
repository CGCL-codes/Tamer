package com.googlecode.sarasvati.test.framework;

import junit.framework.Assert;
import com.googlecode.sarasvati.ArcToken;
import com.googlecode.sarasvati.ExecutionType;
import com.googlecode.sarasvati.Node;

public class TestArcToken extends TestToken<ArcToken> {

    private final TestNodeToken parent;

    private TestNodeToken childToken;

    protected Node childNode;

    private final boolean pending;

    public TestArcToken(final int lineNumber, final TestNodeToken parent, final boolean pending, final boolean complete, final ExecutionType executionType) {
        super(lineNumber, complete, executionType);
        this.parent = parent;
        this.pending = pending;
    }

    public TestNodeToken getParent() {
        return parent;
    }

    public TestNodeToken getChildToken() {
        return childToken;
    }

    public void setChildToken(final TestNodeToken childToken) {
        this.childToken = childToken;
    }

    public Node getChildNode() {
        return childNode;
    }

    public void setChildNode(final Node childNode) {
        this.childNode = childNode;
    }

    public boolean isPending() {
        return pending;
    }

    @Override
    public void validate() {
        Assert.assertEquals("Parent token does not match on " + toString(), parent.getToken(), getToken().getParentToken());
        Assert.assertEquals("IsPending? does not match on " + toString(), pending, getToken().isPending());
        if (isComplete()) {
            Assert.assertNotNull("Completed test arc token should have child token: " + toString(), childToken);
        } else {
            Assert.assertNotNull("Incomplete test arc token should have child node: " + toString(), childNode);
            Assert.assertEquals("Child node does not match on " + toString(), childNode, getToken().getArc().getEndNode());
        }
        super.validate();
    }

    @Override
    public String toString() {
        return "[TestArcToken parentId=" + getParent().getId() + " line=" + getLineNumber() + "]";
    }
}
