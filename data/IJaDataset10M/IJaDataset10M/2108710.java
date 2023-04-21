package org.bpmn.bpel.exporter.common;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import bpmn.FlowObject;

/**
 * @author jassuncao
 *
 */
public class MergeNode extends Node {

    private Edge output;

    private Set<Edge> inputs = new HashSet<Edge>();

    private Token token = null;

    private int arrived = 0;

    private boolean parcial;

    private boolean loopMerge = false;

    private SplitNode loopBegin;

    private int expected = 0;

    private FlowObject flowObject;

    public MergeNode(int id) {
        super(id);
    }

    public MergeNode(FlowObject flowObject, int expected) {
        super(newId());
        this.flowObject = flowObject;
        this.expected = expected;
    }

    public boolean isInputsCompleted() {
        return this.expected == inputs.size();
    }

    public void arrived(Token token) {
        if (this.token != null) this.token = ((Token) this.token).combineToken(token); else this.token = token;
        ++arrived;
    }

    public boolean isReady() {
        return inputs.size() == arrived;
    }

    public Token getToken() {
        return this.token;
    }

    public void setParcial(boolean b) {
        this.parcial = b;
    }

    public boolean isParcial() {
        return parcial;
    }

    public boolean isLoopMerge() {
        return loopMerge;
    }

    public void setLoopMerge(boolean loopMerge) {
        this.loopMerge = loopMerge;
    }

    @Override
    public Edge getInput() {
        return inputs.iterator().next();
    }

    public void setLoopBegin(SplitNode split) {
        this.loopBegin = split;
        this.loopMerge = true;
    }

    public SplitNode getLoopBegin() {
        return loopBegin;
    }

    public SplitNode accept(BackwardVisitor visitor, MergeNode reference) {
        return visitor.backwardVisit(this, reference);
    }

    public void accept(Visitor visitor, Token token) {
        visitor.visit(this, token);
    }

    @Override
    public void addIncomingConnection(Edge edge) {
        this.inputs.add(edge);
    }

    @Override
    public void addOutgoingConnection(Edge edge) {
        if (this.output == null) this.output = edge; else throw new RuntimeException("MergeNode Output already connected");
    }

    @Override
    public void removeIncomingConnection(Edge edge) {
        this.inputs.remove(edge);
    }

    @Override
    public void removeOutgoingConnection(Edge edge) {
        this.output = null;
    }

    public Iterator<Edge> getInputs() {
        return inputs.iterator();
    }

    public int getNInputs() {
        return inputs.size();
    }

    public FlowObject getFlowObject() {
        return flowObject;
    }

    @Override
    public String toString() {
        if (flowObject != null) return "[Merge(" + flowObject.getName() + ")]";
        return "Merge" + id;
    }

    public void accept(Visitor2 visitor, Object param) {
        visitor.visit(this, param);
    }

    public Edge getOutput() {
        return output;
    }
}
