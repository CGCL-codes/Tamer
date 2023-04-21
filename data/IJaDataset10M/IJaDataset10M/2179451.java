package triebag.tries;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple online compact String trie.
 *
 * @author mdakin
 *
 */
public class StringTrie {

    private Node root = new Node(false, null);

    public int nodesCreated;

    static Alphabet alphabet = new TurkishAlphabet();

    public void add(String s) {
        if (s == null) {
            throw new NullPointerException("Input key can not be null");
        }
        byte[] indexedChars = alphabet.toIndexes(s);
        Node node = root;
        Node previousNode = null;
        int i = 0;
        int fragmentSplitIndex = 0;
        while (node != null) {
            previousNode = node;
            node = node.getChildNode(indexedChars[i]);
            if (node == null) {
                previousNode.addChild(new Node(true, getSuffix(indexedChars, i)));
            } else {
                fragmentSplitIndex = getSplitPoint(indexedChars, i, node.fragment);
                i += fragmentSplitIndex;
                if ((fragmentSplitIndex < node.fragment.length) || (i == indexedChars.length && fragmentSplitIndex == node.fragment.length)) {
                    break;
                }
            }
        }
        if (node != null) {
            Node newNode = new Node(node.wordNode, getSuffix(node.fragment, fragmentSplitIndex));
            if (i == indexedChars.length) {
                node.wordNode = true;
                if (fragmentSplitIndex < node.fragment.length) {
                    newNode.children = node.children;
                    node.splitAndAdd(newNode, fragmentSplitIndex);
                }
            } else {
                Node n2 = new Node(true, getSuffix(indexedChars, i));
                newNode.children = node.children;
                node.splitAndAdd(newNode, fragmentSplitIndex);
                node.addChild(n2);
                node.wordNode = false;
            }
        }
    }

    static int getSplitPoint(byte[] input, int start1, byte[] fragment) {
        int fragmentIndex = 0;
        while (start1 < input.length && fragmentIndex < fragment.length && input[start1++] == fragment[fragmentIndex]) {
            fragmentIndex++;
        }
        return fragmentIndex;
    }

    private static byte[] getSuffix(byte[] arr, int index) {
        byte[] res = new byte[arr.length - index];
        System.arraycopy(arr, index, res, 0, arr.length - index);
        return res;
    }

    public String toFlatString() {
        return root.dump(true);
    }

    public String toDeepString() {
        return root.dump(false);
    }

    public Node getRoot() {
        return root;
    }

    public String getInfo() {
        return "Nodes created: " + nodesCreated;
    }

    /**
   * 
   * @author mdakin
   */
    public static class Node {

        private byte[] fragment;

        int attribute;

        boolean wordNode;

        private ArrayList<Node> children;

        public Node(boolean wordNode, byte[] fragment) {
            this.wordNode = wordNode;
            this.fragment = fragment;
            resetChildren();
        }

        @SuppressWarnings("unchecked")
        public void resetChildren() {
            children = new ArrayList<Node>();
        }

        public void addChild(Node node) {
            int index = 0;
            int x = node.getChar();
            int counter = 0;
            for (int i = 0; i < children.size(); i++) {
                if (x < children.get(i).getChar()) {
                    break;
                }
                counter++;
            }
            children.add(counter, node);
        }

        public void splitAndAdd(Node node, int fragmentSplitIndex) {
            fragment = Arrays.copyOf(fragment, fragmentSplitIndex);
            resetChildren();
            addChild(node);
        }

        public String getString() {
            return getFragmentString();
        }

        public char[] getChars() {
            char[] chars = new char[fragment.length];
            for (int i = 0; i < fragment.length; i++) {
                chars[i] = alphabet.getChar(fragment[i]);
            }
            return chars;
        }

        public Node getChildNode(byte c) {
            if (children == null) return null;
            for (Node node : children) {
                if (node.fragment[0] == c) return node;
            }
            return null;
        }

        @SuppressWarnings("unchecked")
        public Node[] getChildren() {
            return children.toArray(new Node[children.size()]);
        }

        @Override
        public String toString() {
            String s = getString() + " : ";
            if (children != null) {
                s += "( ";
                for (Node node : children) {
                    if (node != null) {
                        byte b = node.getChar();
                        if (b != -1) {
                            s += alphabet.getChar(b) + " ";
                        }
                    }
                }
                s += ")";
            } else {
                s += ".";
            }
            if (wordNode) {
                s += " * ";
            }
            return s;
        }

        private byte getChar() {
            if (fragment == null) {
                return '#';
            }
            return fragment[0];
        }

        private String getFragmentString() {
            if (fragment == null) {
                return "#";
            }
            StringBuilder frs = new StringBuilder();
            for (byte b : fragment) {
                frs.append(alphabet.getChar(b));
            }
            return frs.toString();
        }

        /**
     * Returns string representation of node and all child nodes until leafs.
     *
     * @param b string buffer to append.
     * @param level level of the operation
     */
        private void toDeepString(StringBuffer b, int level) {
            char[] indentChars = new char[level * 2];
            for (int i = 0; i < indentChars.length; i++) indentChars[i] = ' ';
            b.append(indentChars).append(this.toString());
            b.append("\n");
            if (children != null) {
                for (Node subNode : this.children) {
                    if (subNode != null) {
                        subNode.toDeepString(b, level + 1);
                    }
                }
            }
        }

        /**
     *
     * Flat string representation of node and all child nodes.
     * Used for testing purposes only. Given a tree like this:
     *
     *      a
     *     / \
     *    ba  c*
     *   /
     *  e*
     * 
     * This method returns: a:(bc)|ba:(e)|e:(.)*|c:(.)*
     *
     * @param b stringbuffer to append.
     */
        public final void toFlatString(StringBuffer b) {
            b.append(this.toString().replaceAll(" ", "")).append("|");
            if (children != null) {
                for (Node subNode : this.children) {
                    if (subNode != null) {
                        subNode.toFlatString(b);
                    }
                }
            }
        }

        /**
     * Returns string representation of Node (and subnodes) for testing.
     *
     * @param flat : if true, returns a flat version of node and all sub nodes
     * using a depth first traversal. if false, returns multiline, indented
     * version of node tree.
     * @return a flat or tree string representation of trie.
     */
        public final String dump(boolean flat) {
            StringBuffer b = new StringBuffer();
            if (flat) {
                toFlatString(b);
            } else {
                toDeepString(b, 0);
            }
            return b.toString();
        }

        public boolean hasString() {
            return wordNode;
        }

        /**
     * Writes content of node and all sub nodes recursively to data output stream.
     * TODO(mdakin): Serialized size could be improved by writing less for nodes
     * containing less chars.
     * @param dos Data output stream
     * @throws IOException if something goes wrong during write.
     */
        public void serialize(DataOutputStream dos) throws IOException {
            dos.writeInt(fragment.length);
            for (byte c : fragment) {
                dos.writeChar(c);
            }
            dos.writeInt(attribute);
            if (children == null) {
                return;
            }
            for (Node child : children) {
                child.serialize(dos);
            }
        }

        public void deserialize(DataInputStream dis) throws IOException {
        }
    }

    public void save(BufferedOutputStream bufferedOutputStream) {
    }
}
