package org.bpmn.bpel.exporter.common;

/**
 * @author jassuncao
 *
 */
public class EndNode extends Node {

    private Edge input;

    public EndNode(int id) {
        super(id);
    }

    public EndNode() {
        super(newId());
    }

    @Override
    public String toString() {
        return "End" + id;
    }

    @Override
    public void addOutgoingConnection(Edge edge) {
        throw new RuntimeException("AddOutput not supported");
    }

    @Override
    public Edge getInput() {
        return input;
    }

    public SplitNode accept(BackwardVisitor visitor, MergeNode reference) {
        return visitor.backwardVisit(this, reference);
    }

    public void accept(Visitor visitor, Token token) {
        visitor.visit(this, token);
    }

    @Override
    public void addIncomingConnection(Edge edge) {
        this.input = edge;
    }

    @Override
    public void removeIncomingConnection(Edge edge) {
        this.input = null;
    }

    @Override
    public void removeOutgoingConnection(Edge edge) {
    }

    public void accept(Visitor2 visitor, Object param) {
        visitor.visit(this, param);
    }
}
