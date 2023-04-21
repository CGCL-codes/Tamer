package org.gridbus.broker.gui.rule;

import org.w3c.dom.Node;

/**
 * @author Xingchen Chu
 * @version 1.0
 * <code> NotImplementRule </node>
 */
public final class NotImplementRule extends AbstractRule {

    public NotImplementRule(Node node) {
        super(node);
    }

    public boolean verifyPrecondition() {
        setError("'" + getNode().getNodeName() + "' element is not implement yet in Gridbus Broker 2.4");
        return false;
    }

    public boolean verifyPostcondition() {
        setError("'" + getNode().getNodeName() + "' element is not implement yet in Gridbus Broker 2.4");
        return false;
    }

    public boolean verify() {
        setError("'" + getNode().getNodeName() + "' element is not implement yet in Gridbus Broker 2.4");
        return false;
    }
}
