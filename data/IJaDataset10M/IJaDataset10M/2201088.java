package langnstats.project.ParserTools;

import java.util.ArrayList;

public class Node {

    ArrayList<Node> children = new ArrayList<Node>();

    Node parent = null;

    String label = "";

    public Node(ArrayList<Node> children, String label, Node parent) {
        super();
        this.parent = parent;
        this.children = children;
        this.label = label;
    }

    public Node getParent() {
        return parent;
    }

    public Node getNextLeave() {
        Node currentNode = this;
        while (true) {
            if (currentNode == null) return null;
            if (currentNode.getParent().getChildren().size() > 1) {
                return currentNode.getParent().getSecondToLastChild().getLastLeave();
            } else currentNode = currentNode.getParent();
        }
    }

    public Node getSecondNextLeave() {
        Node currentNode = this;
        while (true) {
            if (currentNode == null) return null;
            if (currentNode.getParent().getChildren().size() > 2) {
                return currentNode.getParent().getThirdToLastChild().getLastLeave();
            } else if (currentNode.getParent().getChildren().size() > 1) {
                return currentNode.getParent().getNextLeave();
            } else currentNode = currentNode.getParent();
        }
    }

    public Node getLastLeave() {
        Node currentNode = this;
        while (true) {
            if (currentNode.getChildren().size() == 0) return currentNode; else currentNode = currentNode.getLastChild();
        }
    }

    public ArrayList<Node> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<Node> children) {
        this.children = children;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public void addChild(Node node) {
        children.add(node);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Node getLastChild() {
        if (children.size() > 0) {
            return children.get(children.size() - 1);
        }
        return null;
    }

    public Node getSecondToLastChild() {
        if (children.size() > 1) {
            return children.get(children.size() - 2);
        }
        return null;
    }

    public Node getThirdToLastChild() {
        if (children.size() > 2) {
            return children.get(children.size() - 3);
        }
        return null;
    }
}
