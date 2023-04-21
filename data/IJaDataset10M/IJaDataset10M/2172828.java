package com.sun.j3d.utils.geometry.compression;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * This class maintains a map from compression stream elements (tokens) onto
 * HuffmanNode objects.  A HuffmanNode contains a tag describing the
 * associated token's data length, right shift value, and absolute/relative
 * status.<p>
 * 
 * The tags are computed using Huffman's algorithm to build a binary tree with
 * a minimal total weighted path length.  The frequency of each token is
 * used as its node's weight when building the tree. The path length from the
 * root to the token's node then indicates the bit length that should be used
 * for that token's tag in order to minimize the total size of the compressed
 * stream.
 */
class HuffmanTable {

    private static final int MAX_TAG_LENGTH = 6;

    private HuffmanNode positions[];

    private HuffmanNode normals[];

    private HuffmanNode colors[];

    /**
     * Create a new HuffmanTable with entries for all possible position,
     * normal, and color tokens.
     */
    HuffmanTable() {
        colors = new HuffmanNode[544];
        positions = new HuffmanNode[544];
        normals = new HuffmanNode[112];
    }

    private final int getPositionIndex(int len, int shift, boolean absolute) {
        return (absolute ? 1 : 0) * 272 + len * 16 + shift;
    }

    private final int getNormalIndex(int length, int shift, boolean absolute) {
        return (absolute ? 1 : 0) * 56 + length * 7 + shift;
    }

    private final int getColorIndex(int length, int shift, boolean absolute) {
        return getPositionIndex(length, shift, absolute);
    }

    /**
     * Add a position entry with the given length, shift, and absolute
     * status.
     *
     * @param length number of bits in each X, Y, and Z component
     * @param shift number of trailing zeros in each component
     * @param absolute if false, value represented is a delta from the
     * previous vertex in the compression stream
     */
    void addPositionEntry(int length, int shift, boolean absolute) {
        addEntry(positions, getPositionIndex(length, shift, absolute), length, shift, absolute);
    }

    /**
     * Get the position entry associated with the specified length, shift, and
     * absolute status.  This will contain a tag indicating the actual
     * encoding to be used in the compression command stream, not necessarily
     * the same as the original length and shift with which the the entry was
     * created.
     *
     * @param length number of bits in each X, Y, and Z component
     * @param shift number of trailing zeros in each component
     * @param absolute if false, value represented is a delta from the
     * previous vertex in the compression stream
     * @return HuffmanNode mapped to the specified parameters
     */
    HuffmanNode getPositionEntry(int length, int shift, boolean absolute) {
        return getEntry(positions, getPositionIndex(length, shift, absolute));
    }

    /**
     * Add a color entry with the given length, shift, and absolute
     * status.
     *
     * @param length number of bits in each R, G, B, and A component
     * @param shift number of trailing zeros in each component
     * @param absolute if false, value represented is a delta from the
     * previous color in the compression stream
     */
    void addColorEntry(int length, int shift, boolean absolute) {
        addEntry(colors, getColorIndex(length, shift, absolute), length, shift, absolute);
    }

    /**
     * Get the color entry associated with the specified length, shift, and
     * absolute status.  This will contain a tag indicating the actual
     * encoding to be used in the compression command stream, not necessarily
     * the same as the original length and shift with which the the entry was
     * created.
     *
     * @param length number of bits in each R, G, B, and A component
     * @param shift number of trailing zeros in each component
     * @param absolute if false, value represented is a delta from the
     * previous color in the compression stream
     * @return HuffmanNode mapped to the specified parameters
     */
    HuffmanNode getColorEntry(int length, int shift, boolean absolute) {
        return getEntry(colors, getColorIndex(length, shift, absolute));
    }

    /**
     * Add a normal entry with the given length, shift, and absolute
     * status.
     *
     * @param length number of bits in each U and V component
     * @param shift number of trailing zeros in each component
     * @param absolute if false, value represented is a delta from the
     * previous normal in the compression stream
     */
    void addNormalEntry(int length, int shift, boolean absolute) {
        addEntry(normals, getNormalIndex(length, shift, absolute), length, shift, absolute);
    }

    /**
     * Get the normal entry associated with the specified length, shift, and
     * absolute status.  This will contain a tag indicating the actual
     * encoding to be used in the compression command stream, not necessarily
     * the same as the original length and shift with which the the entry was
     * created.
     *
     * @param length number of bits in each U and V component
     * @param shift number of trailing zeros in each component
     * @param absolute if false, value represented is a delta from the
     * previous normal in the compression stream
     * @return HuffmanNode mapped to the specified parameters
     */
    HuffmanNode getNormalEntry(int length, int shift, boolean absolute) {
        return getEntry(normals, getNormalIndex(length, shift, absolute));
    }

    private void addEntry(HuffmanNode table[], int index, int length, int shift, boolean absolute) {
        if (table[index] == null) table[index] = new HuffmanNode(length, shift, absolute); else if (table[index].cleared()) table[index].set(length, shift, absolute);
        table[index].addCount();
    }

    private HuffmanNode getEntry(HuffmanNode table[], int index) {
        HuffmanNode t = table[index];
        while (t.merged()) t = t.getMergeNode();
        return t;
    }

    private void getEntries(HuffmanNode table[], Collection c) {
        for (int i = 0; i < table.length; i++) if (table[i] != null && !table[i].cleared() && table[i].hasCount() && !table[i].merged()) c.add(table[i]);
    }

    /**
     * Clear this HuffmanTable instance.
     */
    void clear() {
        for (int i = 0; i < positions.length; i++) if (positions[i] != null) positions[i].clear();
        for (int i = 0; i < colors.length; i++) if (colors[i] != null) colors[i].clear();
        for (int i = 0; i < normals.length; i++) if (normals[i] != null) normals[i].clear();
    }

    /**
     * Compute optimized tags for each position, color, and normal entry.
     */
    void computeTags() {
        LinkedList nodeList = new LinkedList();
        getEntries(positions, nodeList);
        computeTags(nodeList, 3);
        nodeList.clear();
        getEntries(colors, nodeList);
        computeTags(nodeList, 3);
        nodeList.clear();
        getEntries(normals, nodeList);
        computeTags(nodeList, 2);
    }

    private void computeTags(LinkedList nodes, int minComponentCount) {
        HuffmanNode node0, node1, node2;
        if (nodes.isEmpty()) return;
        while (true) {
            Collections.sort(nodes, HuffmanNode.frequencyComparator);
            node0 = (HuffmanNode) nodes.removeFirst();
            while (nodes.size() > 0) {
                node1 = (HuffmanNode) nodes.removeFirst();
                node2 = new HuffmanNode();
                node2.addChildren(node0, node1);
                addNodeInOrder(nodes, node2, HuffmanNode.frequencyComparator);
                node0 = (HuffmanNode) nodes.removeFirst();
            }
            node0.collectLeaves(0, 0, nodes);
            Collections.sort(nodes, HuffmanNode.tagLengthComparator);
            if (((HuffmanNode) nodes.getFirst()).tagLength > MAX_TAG_LENGTH) {
                merge(nodes);
            } else {
                expand(nodes, minComponentCount);
                break;
            }
        }
    }

    private void merge(LinkedList nodes) {
        ListIterator i = nodes.listIterator(0);
        HuffmanNode node0, node1, node2;
        int index = 0;
        while (i.hasNext()) {
            node0 = (HuffmanNode) i.next();
            if (node0.unmergeable()) continue;
            i.remove();
            while (i.hasNext()) {
                node1 = (HuffmanNode) i.next();
                if (node0.mergeInto(node1)) {
                    i.remove();
                    while (i.hasNext()) {
                        node2 = (HuffmanNode) i.next();
                        if (node1.tokenEquals(node2)) {
                            node1.mergeInto(node2);
                            return;
                        }
                    }
                    i.add(node1);
                    return;
                }
            }
            node0.setUnmergeable();
            i.add(node0);
            i = nodes.listIterator(0);
        }
    }

    private void expand(LinkedList nodes, int minComponentCount) {
        Iterator i = nodes.iterator();
        while (i.hasNext()) {
            HuffmanNode n = (HuffmanNode) i.next();
            while (n.tagLength + (minComponentCount * (n.dataLength - n.shift)) < 6) {
                n.incrementLength();
            }
        }
    }

    private void addNodeInOrder(LinkedList l, HuffmanNode node, Comparator c) {
        ListIterator i = l.listIterator(0);
        while (i.hasNext()) {
            HuffmanNode n = (HuffmanNode) i.next();
            if (c.compare(n, node) > 0) {
                n = (HuffmanNode) i.previous();
                break;
            }
        }
        i.add(node);
    }

    /**
     * Create compression stream commands for decompressors to use to set up
     * their decompression tables.
     *
     * @param output CommandStream which receives the compression commands
     */
    void outputCommands(CommandStream output) {
        LinkedList nodeList = new LinkedList();
        getEntries(positions, nodeList);
        outputCommands(nodeList, output, CommandStream.POSITION_TABLE);
        nodeList.clear();
        getEntries(colors, nodeList);
        outputCommands(nodeList, output, CommandStream.COLOR_TABLE);
        nodeList.clear();
        getEntries(normals, nodeList);
        outputCommands(nodeList, output, CommandStream.NORMAL_TABLE);
    }

    private void outputCommands(Collection nodes, CommandStream output, int tableId) {
        Iterator i = nodes.iterator();
        while (i.hasNext()) {
            HuffmanNode n = (HuffmanNode) i.next();
            int addressRange = (1 << n.tagLength) | n.tag;
            int dataLength = (n.dataLength == 16 ? 0 : n.dataLength);
            int command = CommandStream.SET_TABLE | (tableId << 1) | (addressRange >> 6);
            long body = ((addressRange & 0x3f) << 9) | (dataLength << 5) | (n.absolute ? 0x10 : 0) | n.shift;
            output.addCommand(command, 8, body, 15);
        }
    }

    /**
     * Print a collection of HuffmanNode objects to standard out.
     *
     * @param header descriptive string
     * @param nodes Collection of HuffmanNode objects to print
     */
    void print(String header, Collection nodes) {
        System.out.println(header + "\nentries: " + nodes.size() + "\n");
        Iterator i = nodes.iterator();
        while (i.hasNext()) {
            HuffmanNode n = (HuffmanNode) i.next();
            System.out.println(n.toString() + "\n");
        }
    }

    /**
     * Print the contents of this instance to standard out.
     */
    void print() {
        LinkedList nodeList = new LinkedList();
        getEntries(positions, nodeList);
        Collections.sort(nodeList, HuffmanNode.frequencyComparator);
        print("\nposition tokens and tags", nodeList);
        nodeList.clear();
        getEntries(colors, nodeList);
        Collections.sort(nodeList, HuffmanNode.frequencyComparator);
        print("\ncolor tokens and tags", nodeList);
        nodeList.clear();
        getEntries(normals, nodeList);
        Collections.sort(nodeList, HuffmanNode.frequencyComparator);
        print("\nnormal tokens and tags", nodeList);
    }
}
