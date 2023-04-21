package com.ryanm.util.text;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Useful for string set lookups and command completion stuff
 * 
 * @author ryanm
 */
public class RadixTree {

    private Node root = new Node("");

    private final boolean caseSensitive;

    /**
	 * @param caseSensitive
	 *           <code>true</code> if case matters. Note that a
	 *           case-insensitive {@link RadixTree} will convert all
	 *           strings passed to it for insertion or query to lower
	 *           case.
	 */
    public RadixTree(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
        root.isString = false;
    }

    /**
	 * Adds string to the set
	 * 
	 * @param string
	 */
    public void add(CharSequence string) {
        if (!caseSensitive) {
            string = string.toString().toLowerCase();
        }
        root.addString(string);
    }

    /**
	 * Removes a string from the set
	 * 
	 * @param string
	 */
    public void remove(CharSequence string) {
        if (!caseSensitive) {
            string = string.toString().toLowerCase();
        }
        root.removeString(string);
    }

    /**
	 * Tests if the string is contained in the set
	 * 
	 * @param string
	 * @return <code>true</code> if the entire string is contained in
	 *         the tree
	 */
    public boolean contains(CharSequence string) {
        if (!caseSensitive) {
            string = string.toString().toLowerCase();
        }
        return findPredecessor(string).length() == string.length();
    }

    /**
	 * Finds the substring of the string that is in the set
	 * 
	 * @param string
	 * @return The substring that belongs
	 */
    public String findPredecessor(CharSequence string) {
        if (!caseSensitive) {
            string = string.toString().toLowerCase();
        }
        StringBuilder buff = new StringBuilder();
        root.findPredecessor(string, buff);
        return buff.toString();
    }

    /**
	 * Finds possible completions that fit in the set
	 * 
	 * @param string
	 * @param depth
	 *           How deeply to search the tree, the maximum number of
	 *           decisions that need to be made to type any one
	 *           completion
	 * @return A list of possible completions
	 */
    public List<String> findSuccessors(CharSequence string, int depth) {
        if (!caseSensitive) {
            string = string.toString().toLowerCase();
        }
        List<String> completions = new LinkedList<String>();
        root.findSuccessors(string, depth, completions);
        return completions;
    }

    @Override
    public String toString() {
        StringBuilder buff = new StringBuilder();
        root.buildString(buff, -1);
        return buff.toString();
    }

    private class Node implements Comparable<Node> {

        private CharSequence value;

        private Node[] children = new Node[0];

        /**
		 * Indicates that the string ending at this node is a string in
		 * the set
		 */
        private boolean isString = true;

        private Node(CharSequence string) {
            value = string;
        }

        private void findSuccessors(CharSequence string, int depth, List<String> completions) {
            int d = findDivergenceIndex(string);
            if (d < value.length() || d == string.length()) {
                StringBuilder prefix = new StringBuilder(value.subSequence(d, value.length()));
                if (isString) {
                    completions.add(prefix.toString());
                }
                if (depth > 0) {
                    for (int i = 0; i < children.length; i++) {
                        children[i].getCompletions(prefix, depth - 1, completions);
                    }
                }
            } else {
                Node c = findChild(string.charAt(d));
                if (c != null) {
                    c.findSuccessors(string.subSequence(d, string.length()), depth, completions);
                }
            }
        }

        private void getCompletions(StringBuilder prefix, int depth, List<String> completions) {
            int pl = prefix.length();
            prefix.append(value);
            if (isString) {
                completions.add(prefix.toString());
            }
            if (depth > 0) {
                for (int i = 0; i < children.length; i++) {
                    children[i].getCompletions(prefix, depth - 1, completions);
                }
            }
            prefix.delete(pl, prefix.length());
        }

        private void addString(CharSequence string) {
            int d = findDivergenceIndex(string);
            if (d < value.length()) {
                Node child = new Node(value.subSequence(d, value.length()));
                child.children = children;
                child.isString = isString;
                value = value.subSequence(0, d);
                children = new Node[] { child };
                isString = false;
            }
            if (d == string.length() && d > 0) {
                isString = true;
            } else {
                Node c = findChild(string.charAt(d));
                if (c != null) {
                    c.addString(string.subSequence(d, string.length()));
                } else {
                    insertNode(new Node(string.subSequence(d, string.length())));
                }
            }
        }

        private void removeString(CharSequence string) {
            int d = findDivergenceIndex(string);
            if (d == value.length() && d == string.length()) {
                isString = false;
                if (children.length == 1) {
                    StringBuilder buff = new StringBuilder(value);
                    buff.append(children[0].value);
                    value = buff;
                    isString = children[0].isString;
                    children = children[0].children;
                }
            } else {
                if (d == value.length()) {
                    Node c = findChild(string.charAt(d));
                    if (c != null) {
                        c.removeString(string.subSequence(d, string.length()));
                    }
                }
            }
        }

        private void findPredecessor(CharSequence string, StringBuilder buff) {
            int d = findDivergenceIndex(string);
            if (d == value.length() && d <= string.length()) {
                buff.append(value.subSequence(0, d));
                if (d < string.length()) {
                    CharSequence c = string.subSequence(d, string.length());
                    Node child = findChild(c.charAt(0));
                    child.findPredecessor(c, buff);
                }
            }
        }

        private Node findChild(char c) {
            for (int i = 0; i < children.length; i++) {
                if (c == children[i].value.charAt(0)) {
                    return children[i];
                }
            }
            return null;
        }

        private int findDivergenceIndex(CharSequence string) {
            int d = 0;
            while (d < value.length() && d < string.length() && value.charAt(d) == string.charAt(d)) {
                d++;
            }
            return d;
        }

        private void insertNode(Node child) {
            int i = Arrays.binarySearch(children, child);
            assert i < 0;
            i += 1;
            i = -i;
            Node[] nc = new Node[children.length + 1];
            System.arraycopy(children, 0, nc, 0, i);
            nc[i] = child;
            if (i < nc.length) {
                System.arraycopy(children, i, nc, i + 1, children.length - i);
            }
            children = nc;
        }

        @Override
        public int compareTo(Node o) {
            return TextUtils.compareTo(value, o.value);
        }

        private void buildString(StringBuilder buff, int indent) {
            for (int i = 0; i < indent; i++) {
                buff.append(" ");
            }
            if (isString) {
                buff.append("\"");
            }
            buff.append(value);
            if (isString) {
                buff.append("\"");
            }
            indent++;
            for (int i = 0; i < children.length; i++) {
                buff.append("\n");
                children[i].buildString(buff, indent);
            }
        }
    }
}
