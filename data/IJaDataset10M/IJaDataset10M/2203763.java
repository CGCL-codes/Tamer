package org.privale.node;

public class NodeInterfaceConnector {

    protected void Init(NodeAbstract n) {
        LNode = n;
    }

    protected NodeAbstract getNode() {
        return LNode;
    }

    private transient NodeAbstract LNode;
}
