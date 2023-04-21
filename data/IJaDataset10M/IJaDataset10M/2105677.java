package org.xteam.cs.runtime.inc;

import java.util.ArrayList;
import java.util.List;

public abstract class ParentNode extends Node {

    private List<Node> children;

    private int textExtentCache;

    public ParentNode() {
        super();
        this.children = new ArrayList<Node>();
        textExtentCache = -1;
    }

    public void add(Node node) {
        children.add(node);
        node.setParent(this);
    }

    public int childCount() {
        return children.size();
    }

    public Node childAt(int i) {
        return (Node) children.get(i);
    }

    public int indexOf(Node child) {
        return children.indexOf(child);
    }

    public void replace(Node node, Node newNode) {
        int index = indexOf(node);
        if (index >= 0) {
            children.set(index, newNode);
            newNode.parent = this;
            invalidateTextExtentCache();
            invalidateTextOffsetCacheFollowing(index + 1);
        }
    }

    public void insert(int i, Node node) {
        children.add(i, node);
        node.setParent(this);
        invalidateTextExtentCache();
        invalidateTextOffsetCacheFollowing(i + 1);
    }

    public void remove(Node node) {
        int i = children.indexOf(node);
        children.remove(node);
        invalidateTextExtentCache();
        invalidateTextOffsetCacheFollowing(i);
    }

    public int length() {
        if (textExtentCache < 0) computeTextExtentCache();
        return textExtentCache;
    }

    protected void invalidateTextExtentCache() {
        if (textExtentCache >= 0) {
            textExtentCache = -1;
            if (parent != null) parent.invalidateTextExtentCache();
        }
    }

    /**
     * Invalidate the text offset of all the children following child.
     * Then apply recursively on ourself.
     *
     * @param child
     */
    protected void invalidateTextOffsetCacheFollowing(Node child) {
        invalidateTextOffsetCacheFollowing(children.indexOf(child) + 1);
    }

    private void invalidateTextOffsetCacheFollowing(int i) {
        for (; i < childCount(); ++i) {
            childAt(i).invalidateTextOffsetCache();
        }
        if (parent != null) parent.invalidateTextOffsetCacheFollowing(this);
    }

    /**
	 * Return the previous sibling node of <code>point</code>.
	 * 
	 * @param point
	 * @return
	 */
    public Node previousOf(Node point) {
        int index = indexOf(point);
        if (index > 0) return childAt(index - 1);
        if (parent != null) return parent.previousOf(this);
        return null;
    }

    /**
     * Return the next sibling node of <code>point</code>.
     *   
     * @param point
     * @return
     */
    public Node nextOf(Node point) {
        int index = indexOf(point);
        if (index < (childCount() - 1)) return childAt(index + 1);
        return parent.nextOf(this);
    }

    public Token firstToken() {
        if (childCount() > 0) return childAt(0).firstToken();
        return parent.nextOf(this).firstToken();
    }

    public Token lastToken() {
        if (childCount() > 0) return childAt(childCount() - 1).lastToken();
        return parent.previousOf(this).lastToken();
    }

    public void insert(String text, int offset, int toRemove) {
        for (int i = 0; i < children.size(); ++i) {
            if (toRemove == 0 && text.length() == 0) break;
            Node node = childAt(i);
            if (node.getOffset() <= offset && offset <= (node.getOffset() + node.length())) {
                int nodeOffset = offset - node.getOffset();
                int maxToRemove = Math.min(node.length() - nodeOffset, toRemove);
                if (text.length() > 0 || maxToRemove > 0) node.insert(text, offset, maxToRemove);
                toRemove -= maxToRemove;
                text = "";
                setFlag(NESTED_CHANGE);
            }
        }
    }

    /**
     * Get the text string from this root node. offset is global
     * to the document.
     * 
     * @param offset
     * @param length
     */
    public String getText(int offset, int length) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < children.size() && length > 0; ++i) {
            Node node = childAt(i);
            if (node.getOffset() <= offset && offset < (node.getOffset() + node.length())) {
                String text = node.getText(offset, length);
                if (text.length() > 0) {
                    buffer.append(text);
                    offset += text.length();
                    length -= text.length();
                }
            }
        }
        return buffer.toString();
    }

    public void getText(StringBuffer buffer) {
        for (int i = 0; i < children.size(); ++i) {
            childAt(i).getText(buffer);
        }
    }

    /**
     * Recompute the offset of our children.
     */
    protected void computeChildTextOffset() {
        int offset = getOffset();
        for (int i = 0; i < children.size(); ++i) {
            Node node = childAt(i);
            node.setTextOffsetCache(offset);
            offset += node.length();
        }
    }

    /**
     * If our offset is changed invalidate our children
     * offset cache.
     * 
     * @param offset
     */
    protected void setTextOffsetCache(int offset) {
        super.setTextOffsetCache(offset);
        for (int i = 0; i < children.size(); ++i) {
            Node node = childAt(i);
            node.invalidateTextOffsetCache();
        }
    }

    private void computeTextExtentCache() {
        textExtentCache = 0;
        for (int i = 0; i < children.size(); ++i) {
            Node node = childAt(i);
            textExtentCache += node.length();
        }
    }

    public void unsetAllFlag(int flag) {
        super.unsetAllFlag(flag);
        for (int i = 0; i < childCount(); ++i) childAt(i).unsetAllFlag(flag);
    }

    public String toString() {
        String clsName = getClass().getName();
        int pos = clsName.lastIndexOf('.');
        return clsName.substring(pos >= 0 ? pos + 1 : 0) + super.toString();
    }
}
