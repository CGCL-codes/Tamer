package com.scythebill.birdlist.model.util;

import java.util.Collection;
import java.util.List;
import com.google.common.collect.Lists;

/**
 * Implements a prefix trie.  Values (of arbitrary type) are associated with strings, and
 * can be looked up by any prefix of that string.
 * <p>
 * TODO: not inherently thread-safe , so needs to be published in a thread-safe manner.
 * Consider support an immutable subset API, where publishing to that API can do a memory
 * optimization step to trim down resource requirements and use more efficient memory structures
 * (e.g., instead of a List of Nodes, using an array-by-character-value).  Worth measuring first
 * how much memory these tries are actually using. 
 */
public class Trie<T> {

    private final List<Node<T>> nodes;

    public Trie() {
        nodes = Lists.newArrayList();
    }

    /**
   * Add a value associated with a name.
   */
    public void add(String name, T value) {
        for (int i = 0; i < nodes.size(); i++) {
            Node<T> node = nodes.get(i);
            Node<T> destinationNode = node.add(name, 0, value);
            if (destinationNode == null) continue;
            if (destinationNode != node) {
                nodes.set(i, destinationNode);
            }
            return;
        }
        nodes.add(new LeafNode<T>(value, name));
    }

    /**
   * Find all values that match a prefix
   */
    public Collection<T> findMatches(String prefix) {
        List<T> results = Lists.newArrayList();
        findMatches(results, prefix, null);
        return results;
    }

    /**
   * Find all values that match a prefix, filling in
   * a results collection.
   */
    public void findMatches(Collection<T> results, String prefix, Collection<T> within) {
        for (Node<T> node : nodes) {
            if (node.findMatches(prefix, results, within)) break;
        }
    }

    /**
   * Remove a particular object associated with a name
   */
    public boolean remove(String name, T value) {
        for (int i = 0; i < nodes.size(); i++) {
            Node<T> node = nodes.get(i);
            RemoveAction action = node.remove(name, value);
            switch(action) {
                case NOT_FOUND_BUT_DONE_LOOKING:
                    return false;
                case FOUND_WITHIN:
                    return true;
                case FOUND_AND_IM_GONE:
                    nodes.remove(i);
                    return true;
                case NOT_FOUND:
                    break;
            }
        }
        return false;
    }

    /** Base class for a node */
    private abstract static class Node<T> {

        public abstract boolean findMatches(String prefix, Collection<T> results, Collection<T> within);

        public abstract Node<T> add(String name, int index, T value);

        public abstract RemoveAction remove(String name, T value);

        protected abstract void allValues(Collection<T> results, Collection<T> within);
    }

    private static class InternalNode<T> extends Node<T> {

        private final List<Node<T>> children;

        private final char character;

        public InternalNode(char c) {
            character = c;
            children = Lists.newArrayList();
        }

        @Override
        public boolean findMatches(String prefix, Collection<T> results, Collection<T> within) {
            if (normalizedCharAt(prefix, 0) == character) {
                if (prefix.length() == 1) {
                    allValues(results, within);
                } else {
                    String prefixMinusFirst = prefix.substring(1);
                    for (Node<T> node : children) {
                        if (node.findMatches(prefixMinusFirst, results, within)) break;
                    }
                }
                return true;
            }
            return false;
        }

        @Override
        protected void allValues(Collection<T> results, Collection<T> within) {
            for (Node<T> node : children) {
                node.allValues(results, within);
            }
        }

        @Override
        public Node<T> add(String name, int index, T value) {
            if (name.length() > index && normalizedCharAt(name, index) == character) {
                for (int i = 0; i < children.size(); i++) {
                    Node<T> node = children.get(i);
                    Node<T> destinationNode = node.add(name, index + 1, value);
                    if (destinationNode == null) continue;
                    if (destinationNode != node) {
                        children.set(i, destinationNode);
                    }
                    return this;
                }
                children.add(new LeafNode<T>(value, name.substring(index + 1)));
                return this;
            }
            return null;
        }

        @Override
        public RemoveAction remove(String name, T value) {
            if (name.length() > 0 && normalizedCharAt(name, 0) == character) {
                name = name.substring(1);
                for (int i = 0; i < children.size(); i++) {
                    Node<T> node = children.get(i);
                    RemoveAction action = node.remove(name, value);
                    if (action == RemoveAction.NOT_FOUND) continue;
                    if (action == RemoveAction.FOUND_AND_IM_GONE) {
                        children.remove(i);
                    }
                    break;
                }
                return (children.isEmpty() ? RemoveAction.FOUND_AND_IM_GONE : RemoveAction.FOUND_WITHIN);
            }
            return RemoveAction.NOT_FOUND;
        }
    }

    /** List of possible states when recursing through a trie removing a node. */
    private static enum RemoveAction {

        NOT_FOUND, NOT_FOUND_BUT_DONE_LOOKING, FOUND_WITHIN, FOUND_AND_IM_GONE
    }

    private static class LeafNode<T> extends Node<T> {

        private int length;

        private String suffix;

        private char firstChar;

        private final List<T> values;

        public LeafNode(T firstValue, String suffix) {
            values = Lists.newArrayList();
            values.add(firstValue);
            this.suffix = normalizeString(suffix);
            storeLengthAndFirstChar();
        }

        @Override
        public boolean findMatches(String prefix, Collection<T> results, Collection<T> within) {
            if (suffix.startsWith(prefix)) {
                allValues(results, within);
                return true;
            } else {
                return false;
            }
        }

        @Override
        protected void allValues(Collection<T> results, Collection<T> within) {
            if (within == null) {
                results.addAll(values);
            } else {
                int size = values.size();
                for (int i = 0; i < size; i++) {
                    T value = values.get(i);
                    if (within.contains(value)) results.add(value);
                }
            }
        }

        private boolean _matches(String prefix, int index) {
            if (prefix.length() - index != length) return false;
            return suffix.regionMatches(0, prefix, index, length);
        }

        @Override
        public RemoveAction remove(String name, T value) {
            if (name.equals(suffix)) {
                values.remove(value);
                return (values.isEmpty() ? RemoveAction.FOUND_AND_IM_GONE : RemoveAction.FOUND_WITHIN);
            } else {
                return RemoveAction.NOT_FOUND;
            }
        }

        @Override
        public Node<T> add(String name, int index, T value) {
            if (_matches(name, index)) {
                values.add(value);
                return this;
            }
            if (name.length() == index) return null;
            char c = normalizedCharAt(name, index);
            if (c == firstChar) {
                InternalNode<T> newNode = new InternalNode<T>(c);
                suffix = suffix.substring(1);
                storeLengthAndFirstChar();
                newNode.children.add(this);
                return newNode.add(name, index, value);
            }
            return null;
        }

        private void storeLengthAndFirstChar() {
            length = suffix.length();
            if (length == 0) firstChar = (char) 0; else firstChar = normalizedCharAt(suffix, 0);
        }

        @Override
        public String toString() {
            return "[Leaf:" + firstChar + "," + suffix + "]";
        }
    }

    /**
   * Normalize a string for searching. 
   * 
   * TODO: refactor this out of the Trie code;  normalization should ideally be 
   */
    public static String normalizeString(String s) {
        int length = s.length();
        StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < length; i++) builder.append(normalizedCharAt(s, i));
        return builder.toString();
    }

    private static char normalizedCharAt(String s, int index) {
        return normalizeChar(s.charAt(index));
    }

    private static char normalizeChar(char c) {
        c = Character.toLowerCase(c);
        switch(c) {
            case 224:
            case 225:
            case 226:
            case 227:
            case 228:
            case 229:
                return 'a';
            case 231:
                return 'c';
            case 232:
            case 233:
            case 234:
            case 235:
                return 'e';
            case 236:
            case 237:
            case 238:
            case 239:
                return 'i';
            case 241:
                return 'n';
            case 240:
            case 242:
            case 243:
            case 244:
            case 245:
            case 246:
            case 248:
                return 'o';
            case 249:
            case 250:
            case 251:
            case 252:
                return 'u';
            case 253:
            case 255:
                return 'y';
            default:
                return c;
        }
    }
}
