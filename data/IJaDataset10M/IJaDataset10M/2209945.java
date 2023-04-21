package org.jikesrvm.compilers.opt.util;

final class SpaceEffGraphNodeListHeader {

    SpaceEffGraphNodeList _first;

    SpaceEffGraphNodeList _last;

    SpaceEffGraphNodeList first() {
        return _first;
    }

    SpaceEffGraphNodeList last() {
        return _last;
    }

    public void append(SpaceEffGraphNode node) {
        SpaceEffGraphNodeList p = new SpaceEffGraphNodeList();
        p._node = node;
        SpaceEffGraphNodeList last = _last;
        if (last == null) {
            _first = p;
            _last = p;
        } else {
            last._next = p;
            p._prev = last;
            _last = p;
        }
    }

    public boolean add(SpaceEffGraphNode node) {
        SpaceEffGraphNodeList p = first();
        SpaceEffGraphNodeList prev = first();
        if (p == null) {
            p = new SpaceEffGraphNodeList();
            p._node = node;
            _first = p;
            _last = p;
            return true;
        }
        while (p != null) {
            if (p._node == node) {
                return false;
            }
            prev = p;
            p = p._next;
        }
        prev._next = new SpaceEffGraphNodeList();
        prev._next._node = node;
        prev._next._prev = prev;
        _last = prev._next;
        return true;
    }
}
