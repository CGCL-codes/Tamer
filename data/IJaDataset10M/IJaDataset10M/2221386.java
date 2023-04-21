package org.infoeng.ofbiz.ltans.ers;

import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.util.encoders.*;
import org.infoeng.ofbiz.ltans.util.LtansUtils;
import org.infoeng.ofbiz.ltans.util.ByteArrayComparator;
import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MerkleTree {

    private Node root;

    public MessageDigest md;

    private Map<Integer, Node[]> levelMap;

    protected int treeHeight;

    public MerkleTree(String digestAlgorithm) {
        try {
            md = MessageDigest.getInstance(digestAlgorithm);
        } catch (Exception e) {
        }
        levelMap = new HashMap<Integer, Node[]>();
    }

    public MerkleTree() {
        try {
            md = MessageDigest.getInstance(LtansUtils.DIGEST_ALGORITHM);
        } catch (Exception e) {
        }
        levelMap = new HashMap<Integer, Node[]>();
    }

    public int getTreeHeight() {
        return treeHeight;
    }

    public byte[] getRootDigest() {
        return root.getBytes();
    }

    public Node[] getLevel(Integer x) {
        Node[] levelNodes = levelMap.get(x);
        return levelNodes;
    }

    public void setLevel(Integer x, Node[] nodeLevel) {
        levelMap.put(x, nodeLevel);
    }

    public void resetDigest() {
        md.reset();
    }

    public void updateDigest(byte[] updateBytes) {
        md.update(updateBytes);
    }

    public byte[] digest() {
        return md.digest();
    }

    public byte[] digest(byte[] inBytes) {
        return md.digest(inBytes);
    }

    public void setRoot(Node nd) {
        root = nd;
    }

    public Node getRoot() {
        return root;
    }

    public PartialHashtree getAuthenticationPath(byte[] digestBytes) {
        Node[] nodeList = this.getLevel(new Integer(0));
        Node leafNode = null;
        for (int x = 0; x < nodeList.length; x++) {
            if (Arrays.equals(nodeList[x].getBytes(), digestBytes)) {
                leafNode = nodeList[x];
                break;
            }
        }
        if (leafNode == null) return null;
        return getAuthenticationPath(leafNode);
    }

    public PartialHashtree getAuthenticationPath(Node leafNode) {
        PartialHashtree retTree = new PartialHashtree();
        Node tmpNode = leafNode;
        while (true) {
            byte[] siblingBytes = getSiblingBytes(tmpNode);
            if (siblingBytes == null) break;
            DEROctetString dos = new DEROctetString(siblingBytes);
            retTree.addOctetString(dos);
            tmpNode = tmpNode.getParent();
            if (tmpNode == null) break;
        }
        return retTree;
    }

    public byte[] getSiblingBytes(Node nd) {
        Node parentNode = nd.getParent();
        byte[] retBytes = null;
        byte[] nodeBytes = nd.getBytes();
        if (parentNode == null) return null;
        Node leftNode = parentNode.getLeft();
        Node rightNode = parentNode.getRight();
        if (Arrays.equals(nd.getBytes(), leftNode.getBytes())) retBytes = rightNode.getBytes(); else if (Arrays.equals(nd.getBytes(), rightNode.getBytes())) retBytes = leftNode.getBytes();
        return retBytes;
    }

    public static MerkleTree buildTree(List<byte[]> digestBytesList) {
        if (digestBytesList == null || (digestBytesList.size() < 5)) return null;
        MerkleTree mt = new MerkleTree();
        Collections.sort(digestBytesList, new ByteArrayComparator());
        int byteArrayLen = digestBytesList.size();
        if (byteArrayLen < 2) {
            return null;
        }
        int height = (int) Math.floor(Math.log((double) byteArrayLen) / Math.log(2.0));
        while (byteArrayLen > Math.pow(2.0, (double) height)) {
            height++;
        }
        int totalLeaves = (int) Math.pow(2, height);
        Node[] leaves = new Node[totalLeaves];
        for (int m = 0; m < totalLeaves; m++) {
            leaves[m] = new Node();
            if (m < byteArrayLen) {
                byte[] digestBytes = digestBytesList.get(m);
                leaves[m].setBytes(digestBytes);
            } else {
                byte[] tmpByte = new byte[1];
                tmpByte[0] = 0;
                leaves[m].setBytes(tmpByte);
            }
        }
        mt.setLevel(new Integer(0), leaves);
        int x = 0;
        while (true) {
            Node[] nextLevel = evolveNextLevel(mt, x);
            if (nextLevel.length == 1) {
                mt.setRoot(nextLevel[0]);
                mt.treeHeight = x;
                break;
            }
            x++;
        }
        return mt;
    }

    private static Node[] evolveNextLevel(MerkleTree mt, int levelInt) {
        Node[] subArray = mt.getLevel(new Integer(levelInt));
        int supArrayLen = subArray.length / 2;
        Node[] supArray = new Node[supArrayLen];
        for (int x = 0; x < supArray.length; x++) {
            supArray[x] = new Node();
            byte[] lBytes = subArray[2 * x].getBytes();
            byte[] rBytes = subArray[2 * x + 1].getBytes();
            byte[] parentBytes = produceParentBytes(mt, lBytes, rBytes);
            supArray[x].setBytes(parentBytes);
            supArray[x].setLeft(subArray[2 * x]);
            supArray[x].setRight(subArray[2 * x + 1]);
        }
        mt.setLevel(new Integer(levelInt + 1), supArray);
        return supArray;
    }

    private static byte[] produceParentBytes(MerkleTree mt, byte[] lBytes, byte[] rBytes) {
        boolean lChildNull = false;
        boolean rChildNull = false;
        byte[] retBytes = null;
        if (lBytes.length == 1 && lBytes[0] == 0) lChildNull = true;
        if (rBytes.length == 1 && rBytes[0] == 0) rChildNull = true;
        if (!lChildNull && !rChildNull) {
            mt.resetDigest();
            mt.updateDigest(lBytes);
            mt.updateDigest(rBytes);
            retBytes = mt.digest();
        } else if (!lChildNull && rChildNull) {
            retBytes = LtansUtils.duplicateByteArray(lBytes);
        } else if (lChildNull && !rChildNull) {
            retBytes = LtansUtils.duplicateByteArray(rBytes);
        } else if (lChildNull && rChildNull) {
            retBytes = new byte[1];
            retBytes[0] = 0;
        }
        return retBytes;
    }

    public int size() {
        return (size(root));
    }

    private int size(Node node) {
        if (node == null) return (0); else {
            return (size(node.left) + 1 + size(node.right));
        }
    }

    public int maxDepth() {
        return (maxDepth(root));
    }

    private int maxDepth(Node node) {
        if (node == null) {
            return (0);
        } else {
            int lDepth = maxDepth(node.left);
            int rDepth = maxDepth(node.right);
            return (Math.max(lDepth, rDepth) + 1);
        }
    }

    /**
     *   
     */
    public boolean sameTree(MerkleTree other) {
        return (sameTree(root, other.root));
    }

    /**
     *
     *      Recursive helper -- recurs down two trees in parallel,
     *      checking to see if they are identical.
     */
    boolean sameTree(Node a, Node b) {
        if (a == null && b == null) return (true); else if (a != null && b != null) {
            return (Arrays.equals(a.data, b.data) && sameTree(a.left, b.left) && sameTree(a.right, b.right));
        } else return (false);
    }

    public String toString() {
        return toString(false);
    }

    public String toString(boolean detailed) {
        int levelInt = 0;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while (true) {
            Node[] levelNodes = this.getLevel(new Integer(levelInt));
            if (levelNodes == null) {
                break;
            }
            levelInt++;
            try {
                baos.write(new String("Level " + levelInt + " has " + levelNodes.length + " nodes.\n\n").getBytes());
            } catch (Exception e) {
            }
            if (detailed) {
                try {
                    for (int x = 0; x < levelNodes.length; x++) {
                        byte[] nodeBytes = levelNodes[x].getBytes();
                        String digestStr = new String(Base64.encode(nodeBytes));
                        baos.write(new String("Digest string: " + digestStr + "\n").getBytes());
                    }
                    baos.write("\n\n".getBytes());
                } catch (Exception e) {
                }
            }
        }
        return baos.toString();
    }

    public static class Node {

        public Node left;

        public Node right;

        public Node parent;

        byte[] data;

        Node() {
            left = null;
            right = null;
            parent = null;
            data = null;
        }

        Node(byte[] newData) {
            left = null;
            right = null;
            parent = null;
            data = newData;
        }

        public void setLeft(Node leftNode) {
            left = leftNode;
            leftNode.parent = this;
        }

        public void setRight(Node rightNode) {
            right = rightNode;
            rightNode.parent = this;
        }

        public void setParentLeftChild(Node parentNode) {
            parent = parentNode;
            parentNode.left = this;
        }

        public void setParentRightChild(Node parentNode) {
            parent = parentNode;
            parentNode.right = this;
        }

        public Node getLeft() {
            return left;
        }

        public Node getRight() {
            return right;
        }

        public Node getParent() {
            return parent;
        }

        public byte[] getBytes() {
            return data;
        }

        public void setBytes(byte[] newData) {
            data = newData;
        }
    }
}
