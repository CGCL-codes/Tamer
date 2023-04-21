package org.snipsnap.graph.graph;

import java.util.*;

public abstract class BaseNode implements Node {

    private String name;

    private ArrayList nodelist;

    protected ArrayList attributeList = new ArrayList();

    private int x;

    private int y;

    private int height;

    private int width;

    public BaseNode(String name) {
        this(name, new ArrayList());
    }

    public BaseNode(String name, ArrayList nodelist) {
        this.name = name;
        this.nodelist = nodelist;
    }

    public ArrayList getAttributeList() {
        return attributeList;
    }

    public String getAttribute(String name) {
        String result = null;
        name = ":" + name;
        Iterator iterator = attributeList.iterator();
        while (iterator.hasNext()) {
            String attribute = (String) iterator.next();
            if (attribute.startsWith(name)) {
                result = attribute.substring(name.length() + 1);
            }
        }
        return result;
    }

    public void removeAttributes() {
        attributeList = new ArrayList();
    }

    public ArrayList getNameAttributes() {
        ArrayList namelist = new ArrayList();
        Iterator iterator = attributeList.iterator();
        while (iterator.hasNext()) {
            String s = (String) iterator.next();
            if (!(s.startsWith(":"))) {
                namelist.add(s);
            }
        }
        return namelist;
    }

    public void addAttribute(String attribute) {
        attributeList.add(attribute);
    }

    public void addAttributeList(ArrayList attributes) {
        Iterator iterator = attributes.iterator();
        while (iterator.hasNext()) {
            String str = (String) iterator.next();
            addAttribute(str);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList getChildrenList() {
        return nodelist;
    }

    public void addChild(Node node) {
        nodelist.add(node);
    }

    public void addChildrenList(ArrayList childrenList) {
        Iterator iterator = childrenList.iterator();
        while (iterator.hasNext()) {
            Node node = (BaseNode) iterator.next();
            nodelist.add(node);
        }
    }

    public void removeChild(Node node) {
        nodelist.remove(node);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String toString() {
        String attributesString = "";
        String children = "";
        if (getAttributeList().size() > 0) {
            Iterator iterator = getAttributeList().iterator();
            while (iterator.hasNext()) {
                attributesString = attributesString + "[" + (String) (iterator.next()) + "]";
            }
        } else {
            attributesString = "";
        }
        if (nodelist != null) {
            Iterator it = nodelist.iterator();
            while (it.hasNext()) {
                TreeNode node = (TreeNode) it.next();
                children = children + " (" + node + ")";
            }
        }
        return name + attributesString + children;
    }
}
