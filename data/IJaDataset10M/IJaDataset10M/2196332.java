package org.jikesrvm.compilers.opt.util;

import java.util.Enumeration;

/**
 * List of Graph nodes.
 *
 * comments: should a doubly linked list implement Enumeration?
 */
class SpaceEffGraphNodeList implements Enumeration<SpaceEffGraphNodeList> {

    SpaceEffGraphNode _node;

    SpaceEffGraphNodeList _next;

    SpaceEffGraphNodeList _prev;

    SpaceEffGraphNodeList() {
        _node = null;
        _next = null;
        _prev = null;
    }

    public boolean hasMoreElements() {
        return _next != null;
    }

    public SpaceEffGraphNodeList nextElement() {
        SpaceEffGraphNodeList tmp = _next;
        _next = _next._next;
        return tmp;
    }

    SpaceEffGraphNode node() {
        return _node;
    }

    SpaceEffGraphNodeList next() {
        return _next;
    }

    SpaceEffGraphNodeList prev() {
        return _prev;
    }
}
