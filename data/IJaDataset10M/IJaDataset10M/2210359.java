package ru.adv.cache;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;

class Queue {

    private LinkedList _queue;

    private Map _map;

    private class Node {

        CacheNode _node;

        public Node(CacheNode node) {
            _node = node;
        }
    }

    Queue() {
        _queue = new LinkedList();
        _map = new HashMap();
    }

    synchronized void add(CacheNode cacheNode) {
        if (null == cacheNode) return;
        Node node = new Node(cacheNode);
        _queue.add(node);
        _map.put(cacheNode.getKey(), node);
    }

    synchronized CacheNode get() {
        CacheNode result = null;
        Node node = removeFirst();
        while (null != node) {
            result = node._node;
            if (null != result) {
                _map.remove(result.getKey());
                break;
            }
            node = (Node) removeFirst();
        }
        return result;
    }

    synchronized CacheNode node(Object key) {
        CacheNode result = null;
        Node node = (Node) _map.remove(key);
        if (null != node) {
            result = node._node;
            node._node = null;
        }
        return result;
    }

    synchronized int size() {
        return _map.size();
    }

    private Node removeFirst() {
        Node result;
        try {
            result = (Node) _queue.removeFirst();
        } catch (NoSuchElementException e) {
            result = null;
        }
        return result;
    }
}
