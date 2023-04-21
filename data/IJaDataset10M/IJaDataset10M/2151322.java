package triebag.tries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Stack;

public class SimpleTrie<T> implements Trie<T>, Iterable<T> {

    private final Node<T> root;

    SimpleTrie() {
        root = new Node<T>();
    }

    public void add(CharSequence str, T item) {
        Node<T> node = root;
        Node<T> previousNode = null;
        int i = 0;
        while (i < str.length()) {
            previousNode = node;
            node = node.getChildNode(str.charAt(i));
            if (node == null) break;
            i++;
        }
        if (i < str.length()) {
            node = previousNode;
            while (i < str.length()) {
                node = node.addNode(str.charAt(i++));
            }
        }
        node.item = item;
    }

    public T getItem(CharSequence prefix) {
        Node<T> node = root;
        int i = 0;
        while (i < prefix.length() && node != null) {
            node = node.getChildNode(prefix.charAt(i));
            i++;
        }
        if (node != null) {
            return node.getItem();
        }
        return null;
    }

    public Collection<T> getItemsInString(CharSequence str) {
        Node<T> node = root;
        int i = 0;
        Collection<T> res = new ArrayList<T>();
        while (i < str.length() && node != null) {
            node = node.getChildNode(str.charAt(i));
            if (node != null && node.item != null) {
                res.add(node.getItem());
            }
            i++;
        }
        return res;
    }

    public Iterator<T> getItemsWithPrefix(CharSequence prefix) {
        Node<T> node = root;
        Node<T> previousNode = root;
        int i = 0;
        while (i < prefix.length() && node != null) {
            previousNode = node;
            node = node.getChildNode(prefix.charAt(i));
            i++;
        }
        return new SimplePrefixTrieIterator(previousNode);
    }

    public Iterator<T> iterator() {
        return new SimplePrefixTrieIterator(root);
    }

    private class SimplePrefixTrieIterator implements Iterator<T> {

        Stack<Node<T>> others = new Stack<Node<T>>();

        Node<T> nextNodeWithItem = null;

        SimplePrefixTrieIterator(Node<T> startNode) {
            others.push(startNode);
        }

        private void walkToNextFullNode() {
            nextNodeWithItem = null;
            while (!others.empty()) {
                Node<T> n = others.pop();
                if (n.item != null) {
                    nextNodeWithItem = n;
                }
            }
        }

        public boolean hasNext() {
            walkToNextFullNode();
            return nextNodeWithItem != null;
        }

        public T next() {
            return nextNodeWithItem.item;
        }

        public void remove() {
        }
    }

    private static class Node<T> {

        T item;

        Map<Character, Node<T>> children;

        public Node<T> addNode(char c) {
            if (children == null) {
                children = new HashMap<Character, Node<T>>();
            }
            Node<T> emptyNode = new Node<T>();
            children.put(c, emptyNode);
            return emptyNode;
        }

        Node<T> getChildNode(char c) {
            if (children == null) {
                return null;
            }
            return children.get(c);
        }

        T getItem() {
            return item;
        }
    }
}
