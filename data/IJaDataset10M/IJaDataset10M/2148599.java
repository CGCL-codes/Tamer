package de.tud.eclipse.plugins.controlflow.model.simple;

import de.tud.eclipse.plugins.controlflow.model.ICFlowConnection;
import de.tud.eclipse.plugins.controlflow.model.ICFlowNode;
import de.tud.eclipse.plugins.controlflow.model.simple.CFlowNode;

/**
 * Basic implementation of a simple branch connection.
 * @author Leo Nobach, E. Stoffregen
 *
 */
public class CFlowConnection extends AttributeObject implements ICFlowConnection {

    private CFlowNode node1;

    private CFlowNode node2;

    public CFlowConnection(CFlowNode node1, CFlowNode node2) {
        this.node1 = node1;
        this.node2 = node2;
        node1.addConnection(this, true);
        node2.addConnection(this, false);
    }

    @Override
    public ICFlowNode getSourceNode() {
        return node1;
    }

    @Override
    public ICFlowNode getTargetNode() {
        return node2;
    }

    @Override
    public int getType() {
        return ICFlowConnection.TYPE_BRANCH;
    }
}
