package voxspellcheck;

import java.util.Vector;
import java.util.Stack;
import java.io.DataInputStream;
import org.gjt.sp.util.Log;

public class OffsetTrie implements SpellCheck {

    private class Node {

        public int pos;

        public Node[] children;

        public Node() {
            pos = 0;
            children = null;
        }

        public boolean contains(Character other) throws java.io.IOException {
            Character c;
            input.setPos(pos);
            short length = stream.readShort();
            for (int i = 0; i < length; i++) {
                try {
                    c = stream.readChar();
                } catch (java.io.IOException ex) {
                    return false;
                }
                if (c.equals(other)) {
                    return true;
                }
            }
            return false;
        }

        public Node getNextNode(Character other) throws java.io.IOException {
            Character c;
            input.setPos(pos);
            short length = stream.readShort();
            for (int i = 0; i < length; i++) {
                try {
                    c = stream.readChar();
                } catch (java.io.IOException ex) {
                    return null;
                }
                if (c.equals(other)) {
                    return children[i];
                }
            }
            return null;
        }

        public String toString() {
            Character c;
            String s = new String();
            input.setPos(pos);
            short length;
            try {
                length = stream.readShort();
            } catch (java.io.IOException ex) {
                return null;
            }
            for (int i = 0; i < length; i++) {
                try {
                    c = stream.readChar();
                } catch (java.io.IOException ex) {
                    return null;
                }
                if (c.equals(Character.MIN_VALUE)) s += " " + "MIN_VALUE"; else s += " " + c;
            }
            return s;
        }
    }

    public Node root;

    public TrieInputStream input;

    public DataInputStream stream;

    public OffsetTrie() {
        input = null;
        stream = null;
        root = null;
    }

    protected void read(Node node) throws java.io.IOException {
        node.pos = input.getPos();
        short length = stream.readShort();
        node.children = new Node[length];
        Character[] chars = new Character[length];
        for (int i = 0; i < length; ++i) {
            chars[i] = stream.readChar();
        }
        for (int i = 0; i < length; ++i) {
            if (!chars[i].equals(Character.MIN_VALUE)) {
                node.children[i] = new Node();
                read(node.children[i]);
            }
        }
    }

    public void read(byte[] data) throws java.io.IOException {
        input = new TrieInputStream(data);
        stream = new DataInputStream(input);
        root = new Node();
        read(root);
    }

    protected boolean find(Node node, String word) {
        if (word.length() == 0) {
            try {
                if (node.contains(Character.MIN_VALUE)) return true;
            } catch (java.io.IOException ex) {
                Log.log(Log.DEBUG, node, "exception " + ex);
            }
            return false;
        }
        Character cur = word.charAt(0);
        boolean contains;
        try {
            contains = node.contains(cur);
        } catch (java.io.IOException ex) {
            return false;
        }
        if (!contains) {
            return false;
        }
        Node next;
        try {
            next = node.getNextNode(cur);
        } catch (java.io.IOException ex) {
            return false;
        }
        if (next != null) return find(next, word.substring(1));
        return false;
    }

    public boolean find(String word) {
        input.setPos(0);
        return find(root, word);
    }

    protected Node findNode(String prefix, Node node) {
        if (prefix.length() == 0) return node;
        Character cur = prefix.charAt(0);
        Node next;
        try {
            next = node.getNextNode(cur);
        } catch (java.io.IOException ex) {
            return null;
        }
        if (next != null) return findNode(prefix.substring(1), next);
        return null;
    }

    protected int bloom(String s) {
        int res = 0;
        for (Character c : s.toCharArray()) {
            int i = Character.getNumericValue(c);
            res |= (1 << (i & 0x1f));
        }
        return res;
    }

    protected void getWords(Vector<String> vec, Stack<Character> stack, Node node, int filter) throws java.io.IOException {
        input.setPos(node.pos);
        short length = stream.readShort();
        Character[] chars = new Character[length];
        for (int i = 0; i < length; ++i) {
            chars[i] = stream.readChar();
        }
        for (int i = 0; i < length; ++i) {
            if (chars[i].equals(Character.MIN_VALUE)) {
                char[] wc = new char[stack.size()];
                for (int j = 0; j < stack.size(); ++j) {
                    wc[j] = stack.get(j);
                }
                String s = new String(wc);
                if (((filter ^ bloom(s)) & filter) == 0) vec.add(s);
            } else {
                Node next;
                stack.push(chars[i]);
                next = node.getNextNode(chars[i]);
                if (next != null) getWords(vec, stack, next, filter);
                stack.pop();
            }
        }
    }

    public Vector<String> getWords() {
        Vector<String> vec = new Vector<String>();
        Stack<Character> stack = new Stack<Character>();
        try {
            getWords(vec, stack, root, 0);
        } catch (java.io.IOException ex) {
            Log.log(Log.DEBUG, this, "exception " + ex);
            return new Vector<String>();
        }
        return vec;
    }

    public Vector<String> getWords(String prefix) {
        Vector<String> vec = new Vector<String>();
        if (prefix.length() == 0) return vec;
        Stack<Character> stack = new Stack<Character>();
        stack.push(prefix.charAt(0));
        Node node = findNode(prefix.substring(0, 1), root);
        if (node != null) {
            try {
                getWords(vec, stack, node, bloom(prefix));
            } catch (java.io.IOException ex) {
                Log.log(Log.DEBUG, this, "exception " + ex);
                return new Vector<String>();
            }
        }
        return vec;
    }
}
