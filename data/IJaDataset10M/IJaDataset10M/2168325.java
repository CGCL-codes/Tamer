package net.sourceforge.pplay.digraph;

import java.awt.Point;
import java.util.Enumeration;

public class Node {

    public static final int UNKNOWN_POSITION = -1;

    public static final int NODE_SIZE = 50;

    private int id;

    private String name;

    private Point position;

    private NodeList parentList;

    private NodeList childList;

    private EffectList effectList;

    /** Creates new Node */
    public Node(int nodeId, String nodeName, Point nodePosition) {
        id = nodeId;
        name = nodeName;
        position = new Point(nodePosition);
        parentList = null;
        childList = null;
        effectList = null;
    }

    public int getNumEffects() {
        if (effectList == null) {
            return 0;
        } else {
            return effectList.size();
        }
    }

    public int getNumChildren() {
        if (childList == null) {
            return 0;
        } else {
            return childList.size();
        }
    }

    public int getNumParents() {
        if (parentList == null) {
            return 0;
        } else {
            return parentList.size();
        }
    }

    public int getId() {
        return id;
    }

    public Point getPosition() {
        return new Point(position);
    }

    public void setPosition(Point p) {
        position = new Point(p);
    }

    public String getName() {
        return name;
    }

    public void setName(String nodeName) {
        name = nodeName;
    }

    public void addChild(Node child) {
        if (childList == null) {
            childList = new NodeList();
        }
        try {
            childList.add(child);
        } catch (DuplicateNodeException e) {
        }
    }

    public void removeChild(Node child) throws NoSuchNodeException {
        childList.remove(child);
    }

    public void addParent(Node parent, Effect effect) {
        effect.setParent(parent);
        effect.setChild(this);
        if (parentList == null) {
            parentList = new NodeList();
        }
        try {
            parentList.add(parent);
        } catch (DuplicateNodeException e) {
        }
        if (effectList == null) {
            effectList = new EffectList();
        }
        effectList.add(effect);
    }

    public void removeParent(Node parent) throws NoSuchNodeException {
        parentList.remove(parent);
        try {
            effectList.remove(effectList.getEffect(parent));
        } catch (NoSuchEffectException e) {
            System.err.println("Error: Effect Not Found in Effect List (removeParent)");
        }
    }

    public Effect getEffect(Node parent) throws NoSuchEffectException {
        return effectList.getEffect(parent);
    }

    public void remove() {
        if (parentList != null) {
            for (Enumeration e = parentList.elements(); e.hasMoreElements(); ) {
                Node node = (Node) e.nextElement();
                try {
                    node.removeChild(this);
                } catch (NoSuchNodeException exception) {
                    System.err.println("Error: Node Not Found in Parent List (remove)");
                }
            }
        }
        if (childList != null) {
            for (Enumeration e = childList.elements(); e.hasMoreElements(); ) {
                Node node = (Node) e.nextElement();
                try {
                    node.removeParent(this);
                } catch (NoSuchNodeException exception) {
                    System.err.println("Error: Node Not Found in Child List (remove)");
                }
            }
        }
        effectList = null;
    }

    public String toString() {
        StringBuffer ds = new StringBuffer();
        ds.append("Node\n");
        ds.append("Id: " + getId() + "\n");
        ds.append("Name: " + getName() + "\n");
        ds.append("Position: " + getPosition().x + "," + getPosition().y + "\n");
        ds.append("Children: \n");
        if (childList != null) {
            ds.append(childList.toString());
        }
        ds.append("Parents: \n");
        if (parentList != null) {
            ds.append(parentList.toString());
        }
        ds.append("Effects: \n");
        if (effectList != null) {
            ds.append(effectList.toString());
        }
        return ds.toString();
    }

    public NodeList getChildList() {
        return childList;
    }

    public NodeList getParentList() {
        return parentList;
    }

    public EffectList getEffectList() {
        return effectList;
    }
}
