package com.jeantessier.dependency;

import java.util.*;

public class FeatureResolver implements Visitor {

    public void traverseNodes(Collection<? extends Node> nodes) {
        for (Node node : nodes) {
            node.accept(this);
        }
    }

    public void traverseInbound(Collection<? extends Node> nodes) {
        throw new UnsupportedOperationException("not implemented yet.");
    }

    public void traverseOutbound(Collection<? extends Node> nodes) {
        throw new UnsupportedOperationException("not implemented yet.");
    }

    public void visitPackageNode(PackageNode node) {
        traverseNodes(node.getClasses());
    }

    public void visitInboundPackageNode(PackageNode node) {
    }

    public void visitOutboundPackageNode(PackageNode node) {
    }

    public void visitClassNode(ClassNode node) {
        traverseNodes(node.getFeatures());
    }

    public void visitInboundClassNode(ClassNode node) {
    }

    public void visitOutboundClassNode(ClassNode node) {
    }

    public void visitFeatureNode(FeatureNode node) {
        String featureName = node.getSimpleName();
        for (FeatureNode inheritedFeature : node.getClassNode().getInheritedFeatures(featureName)) {
            for (Node dependent : node.getInboundDependencies()) {
                dependent.addDependency(inheritedFeature);
            }
        }
    }

    public void visitInboundFeatureNode(FeatureNode node) {
    }

    public void visitOutboundFeatureNode(FeatureNode node) {
    }
}
