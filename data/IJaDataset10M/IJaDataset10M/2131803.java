package com.akjava.wiki.client.core;

import java.util.List;
import java.util.Vector;
import com.akjava.wiki.client.util.SystemUtils;

/**
 * 
 *
 */
public class ElementImp extends NodeImp implements Element {

    private List nodeList = new Vector();

    private LineParser[] lineParsers;

    private StringParser[] stringParsers;

    private Element parent;

    public Element getParent() {
        return parent;
    }

    public void setParent(Element parent) {
        this.parent = parent;
    }

    public LineParser[] getLineParses() {
        return DefaultParsers.getLineParsers();
    }

    public StringParser[] getStringParsers() {
        return DefaultParsers.getStringParsers();
    }

    public void addNode(Node node) {
        nodeList.add(node);
    }

    public void removeNode(Node node) {
        nodeList.remove(node);
    }

    public void removeAllNode() {
        nodeList.removeAll(nodeList);
    }

    public Node getNodeAt(int index) {
        return (Node) nodeList.get(index);
    }

    public int countNode() {
        return nodeList.size();
    }

    public String toString() {
        return toString("");
    }

    public String toString(String head) {
        String result = super.toString() + SystemUtils.LINE_SEPARATOR;
        for (int i = 0; i < countNode(); i++) {
            if (getNodeAt(i) instanceof Element) {
                result += head + "\t" + ((Element) getNodeAt(i)).toString(head + "\t") + SystemUtils.LINE_SEPARATOR;
            } else {
                result += head + "\t" + getNodeAt(i).toString() + SystemUtils.LINE_SEPARATOR;
            }
        }
        return result;
    }

    public Element breakUp(Element element, String line) {
        return element;
    }
}
