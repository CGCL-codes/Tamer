package org.jfree.report.data;

import java.util.ArrayList;

/**
 * A precompute-node represents a resolved element or section of the report definition. Unlike the structural nodes,
 * these nodes can always have childs.
 * <p/>
 * The resulting tree gets pruned as early as possible - nodes which do not contain precomputed or preserved expressions
 * will not be stored.
 *
 * @author Thomas Morgner
 */
public class PrecomputeNodeImpl implements PrecomputeNode {

    private PrecomputeNodeImpl parent;

    private PrecomputeNodeImpl next;

    private PrecomputeNodeImpl firstChild;

    private PrecomputeNodeImpl lastChild;

    private PrecomputeNodeKey key;

    private ArrayList functionResults;

    private ArrayList functionNames;

    public PrecomputeNodeImpl(PrecomputeNodeKey key) {
        if (key == null) {
            throw new NullPointerException();
        }
        this.key = key;
    }

    public PrecomputeNodeKey getKey() {
        return key;
    }

    public PrecomputeNode getParent() {
        return parent;
    }

    protected void setParent(final PrecomputeNodeImpl parent) {
        this.parent = parent;
    }

    public PrecomputeNode getNext() {
        return next;
    }

    protected void setNext(final PrecomputeNodeImpl next) {
        this.next = next;
    }

    public PrecomputeNode getFirstChild() {
        return firstChild;
    }

    protected void setFirstChild(final PrecomputeNodeImpl firstChild) {
        this.firstChild = firstChild;
    }

    public PrecomputeNode getLastChild() {
        return lastChild;
    }

    protected void setLastChild(final PrecomputeNodeImpl lastChild) {
        this.lastChild = lastChild;
    }

    public void add(PrecomputeNodeImpl node) {
        if (firstChild == null) {
            firstChild = node;
            firstChild.setParent(this);
            lastChild = node;
            return;
        }
        lastChild.setNext(node);
        lastChild.setParent(this);
    }

    public void addFunction(final String name, final Object value) {
        if (this.functionNames == null) {
            functionNames = new ArrayList();
            functionResults = new ArrayList();
        }
        this.functionNames.add(name);
        this.functionResults.add(value);
    }

    public int getFunctionCount() {
        if (functionNames == null) {
            return 0;
        }
        return functionNames.size();
    }

    public String getFunctionName(int idx) {
        if (functionNames == null) {
            throw new IndexOutOfBoundsException();
        }
        return (String) functionNames.get(idx);
    }

    public Object getFunctionResult(int idx) {
        if (functionResults == null) {
            throw new IndexOutOfBoundsException();
        }
        return functionResults.get(idx);
    }

    public void prune() {
        if (parent == null) {
            return;
        }
        if (parent.getLastChild() != this) {
            throw new IllegalStateException("Cannot prune. Not the last child.");
        }
        if (parent.getFirstChild() == this) {
            parent.setFirstChild(null);
        }
        parent.setLastChild(null);
    }
}
