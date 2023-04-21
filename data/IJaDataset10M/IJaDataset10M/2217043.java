package org.dave.bracket.properties;

public class TreeSynchronizer {

    Properties props;

    Node rootNode;

    TreeSynchronizer(Properties props, Node rootNode) {
        super();
        this.props = props;
        this.rootNode = rootNode;
    }

    void synch() {
        props.clear();
        visit(rootNode, null);
    }

    void visit(Node node, String path) {
        String currentPath = path;
        if (currentPath == null) currentPath = node.getName(); else currentPath += "." + node.getName();
        if (node.hasValue()) {
            props.getPropertyMap().put(currentPath, node.getValue());
        }
        for (Node n : node.getChildren()) {
            visit(n, currentPath);
        }
    }
}
