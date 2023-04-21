package com.jeantessier.dependency;

import java.util.*;

public class VisitorDecorator implements Visitor {

    private Visitor delegate;

    public Visitor getDelegate() {
        return delegate;
    }

    public void setDelegate(Visitor delegate) {
        this.delegate = delegate;
    }

    public void traverseNodes(Collection<? extends Node> nodes) {
        getDelegate().traverseNodes(nodes);
    }

    public void traverseInbound(Collection<? extends Node> nodes) {
        getDelegate().traverseInbound(nodes);
    }

    public void traverseOutbound(Collection<? extends Node> nodes) {
        getDelegate().traverseOutbound(nodes);
    }

    public void visitPackageNode(PackageNode node) {
        node.accept(getDelegate());
    }

    public void visitInboundPackageNode(PackageNode node) {
        node.acceptInbound(getDelegate());
    }

    public void visitOutboundPackageNode(PackageNode node) {
        node.acceptOutbound(getDelegate());
    }

    public void visitClassNode(ClassNode node) {
        node.accept(getDelegate());
    }

    public void visitInboundClassNode(ClassNode node) {
        node.acceptInbound(getDelegate());
    }

    public void visitOutboundClassNode(ClassNode node) {
        node.acceptOutbound(getDelegate());
    }

    public void visitFeatureNode(FeatureNode node) {
        node.accept(getDelegate());
    }

    public void visitInboundFeatureNode(FeatureNode node) {
        node.acceptInbound(getDelegate());
    }

    public void visitOutboundFeatureNode(FeatureNode node) {
        node.acceptOutbound(getDelegate());
    }
}
