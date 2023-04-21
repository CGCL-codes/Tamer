package org.broad.igv.bbfile;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;
import org.broad.tribble.SeekableStreamUtils;
import org.broad.tribble.LittleEndianInputStream;
import net.sf.samtools.util.SeekableStream;

/**
 * File retrieved from BigWig project at Google code on July 26, 2011, revision
 * 36
 * 
 * http://code.google.com/p/bigwig/
 * 
 * This project provides java readers for the UCSC's BigWig and BigBed formats.
 * It was originally developed by Martin Decautis and Jim Robinson for the
 * Integrative Genomics Viewer (http://www.broadinstitute.org/igv). Thanks to
 * Jim Kent and Ann Zweig and from UCSC for their assistance.
 * 
 * 
 * Modification to work with GenomeView by Thomas Abeel.
 * 
 * @author Martin Decautis
 * @author Jim Robinson
 * @author Thomas Abeel
 * 
 */
public class RPTree {

    private static Logger log = Logger.getLogger(RPTree.class.getCanonicalName());

    public final int RPTREE_NODE_FORMAT_SIZE = 4;

    public final int RPTREE_NODE_LEAF_ITEM_SIZE = 32;

    public final int RPTREE_NODE_CHILD_ITEM_SIZE = 24;

    private int uncompressBuffSize;

    private boolean isLowToHigh;

    private long rpTreeOffset;

    private RPTreeHeader rpTreeHeader;

    private RPChromosomeRegion chromosomeBounds;

    private int order;

    private RPTreeNode rootNode;

    private long nodeCount;

    private long leafCount;

    public RPTree(SeekableStream fis, long fileOffset, boolean isLowToHigh, int uncompressBuffSize) {
        rpTreeOffset = fileOffset;
        this.uncompressBuffSize = uncompressBuffSize;
        this.isLowToHigh = isLowToHigh;
        rpTreeHeader = new RPTreeHeader(fis, rpTreeOffset, isLowToHigh);
        if (!rpTreeHeader.isHeaderOK()) {
            int badMagic = rpTreeHeader.getMagic();
            log.severe("Error reading R+ tree header: bad magic = " + badMagic);
            throw new RuntimeException("Error reading R+ tree header: bad magic = " + badMagic);
        }
        order = rpTreeHeader.getBlockSize();
        chromosomeBounds = new RPChromosomeRegion(rpTreeHeader.getStartChromID(), rpTreeHeader.getStartBase(), rpTreeHeader.getEndChromID(), rpTreeHeader.getEndBase());
        long nodeOffset = rpTreeOffset + rpTreeHeader.getHeaderSize();
        RPTreeNode parentNode = null;
        rootNode = readRPTreeNode(fis, nodeOffset, isLowToHigh);
    }

    public RPTree(int order) {
        this.order = order;
        chromosomeBounds = null;
    }

    public int getUncompressBuffSize() {
        return uncompressBuffSize;
    }

    public boolean isIsLowToHigh() {
        return isLowToHigh;
    }

    public int getOrder() {
        return order;
    }

    public RPTreeHeader getRPTreeHeader() {
        return rpTreeHeader;
    }

    public long getItemCount() {
        return rpTreeHeader.getItemCount();
    }

    public RPChromosomeRegion getChromosomeBounds() {
        return chromosomeBounds;
    }

    public long getNodeCount() {
        return nodeCount;
    }

    public RPChromosomeRegion getChromosomeRegion(int startChromID, int endChromID) {
        RPChromosomeRegion region;
        RPTreeNode thisNode = rootNode;
        RPChromosomeRegion seedRegion = null;
        region = findChromosomeRegion(thisNode, startChromID, endChromID, seedRegion);
        return region;
    }

    public ArrayList<RPChromosomeRegion> getAllChromosomeRegions() {
        RPTreeNode thisNode = rootNode;
        ArrayList<RPChromosomeRegion> regionList = new ArrayList<RPChromosomeRegion>();
        findAllChromosomeRegions(thisNode, regionList);
        return regionList;
    }

    public ArrayList<RPTreeLeafNodeItem> getChromosomeDataHits(RPChromosomeRegion selectionRegion, boolean contained) {
        ArrayList<RPTreeLeafNodeItem> leafHitItems = new ArrayList<RPTreeLeafNodeItem>();
        if (selectionRegion == null) return leafHitItems;
        findChromosomeRegionItems(rootNode, selectionRegion, leafHitItems);
        return leafHitItems;
    }

    public void print() {
        if (!rpTreeHeader.isHeaderOK()) {
            int badMagic = rpTreeHeader.getMagic();
            log.severe("Error reading R+ tree header: bad magic = " + badMagic);
            return;
        }
        rpTreeHeader.print();
        if (rootNode != null) rootNode.printItems();
    }

    private RPChromosomeRegion findChromosomeRegion(RPTreeNode thisNode, int startChromID, int endChromID, RPChromosomeRegion region) {
        int hitValue;
        RPChromosomeRegion bounds;
        if (thisNode.isLeaf()) {
            int nLeaves = thisNode.getItemCount();
            for (int index = 0; index < nLeaves; ++index) {
                RPTreeLeafNodeItem leaf = (RPTreeLeafNodeItem) thisNode.getItem(index);
                bounds = leaf.getChromosomeBounds();
                if (startChromID >= bounds.getStartChromID() && startChromID <= bounds.getEndChromID() || endChromID >= bounds.getStartChromID() && endChromID <= bounds.getEndChromID()) {
                    if (region == null) region = new RPChromosomeRegion(bounds); else region = region.getExtremes(bounds);
                }
            }
        } else {
            int nNodes = thisNode.getItemCount();
            for (int index = 0; index < nNodes; ++index) {
                RPTreeChildNodeItem childItem = (RPTreeChildNodeItem) thisNode.getItem(index);
                bounds = childItem.getChromosomeBounds();
                if (startChromID >= bounds.getStartChromID() && startChromID <= bounds.getEndChromID() || endChromID >= bounds.getStartChromID() && endChromID <= bounds.getEndChromID()) {
                    RPTreeNode childNode = childItem.getChildNode();
                    region = findChromosomeRegion(childNode, startChromID, endChromID, region);
                }
            }
        }
        return region;
    }

    private void findAllChromosomeRegions(RPTreeNode thisNode, ArrayList<RPChromosomeRegion> regionList) {
        if (thisNode.isLeaf()) {
            int nLeaves = thisNode.getItemCount();
            for (int index = 0; index < nLeaves; ++index) {
                RPTreeLeafNodeItem leaf = (RPTreeLeafNodeItem) thisNode.getItem(index);
                RPChromosomeRegion region = leaf.getChromosomeBounds();
                regionList.add(region);
            }
        } else {
            int nNodes = thisNode.getItemCount();
            for (int index = 0; index < nNodes; ++index) {
                RPTreeChildNodeItem childItem = (RPTreeChildNodeItem) thisNode.getItem(index);
                RPTreeNode childNode = childItem.getChildNode();
                findAllChromosomeRegions(childNode, regionList);
            }
        }
    }

    private void findChromosomeRegionItems(RPTreeNode thisNode, RPChromosomeRegion selectionRegion, ArrayList<RPTreeLeafNodeItem> leafHitItems) {
        int hitValue;
        if (selectionRegion == null) return;
        hitValue = thisNode.compareRegions(selectionRegion);
        if (Math.abs(hitValue) >= 2) return;
        if (thisNode.isLeaf()) {
            int nLeaves = thisNode.getItemCount();
            for (int index = 0; index < nLeaves; ++index) {
                RPTreeLeafNodeItem leafItem = (RPTreeLeafNodeItem) thisNode.getItem(index);
                hitValue = leafItem.compareRegions(selectionRegion);
                if (Math.abs(hitValue) < 2) {
                    leafHitItems.add(leafItem);
                } else if (hitValue > 1) break;
            }
        } else {
            int nNodes = thisNode.getItemCount();
            for (int index = 0; index < nNodes; ++index) {
                RPTreeChildNodeItem childItem = (RPTreeChildNodeItem) thisNode.getItem(index);
                hitValue = childItem.compareRegions(selectionRegion);
                if (Math.abs(hitValue) < 2) {
                    RPTreeNode childNode = childItem.getChildNode();
                    findChromosomeRegionItems(childNode, selectionRegion, leafHitItems);
                } else if (hitValue > 1) break;
            }
        }
    }

    private RPTreeNode readRPTreeNode(SeekableStream fis, long fileOffset, boolean isLowToHigh) {
        LittleEndianInputStream lbdis = null;
        DataInputStream bdis = null;
        byte[] buffer = new byte[RPTREE_NODE_FORMAT_SIZE];
        RPTreeNode thisNode = null;
        RPTreeNode childNode = null;
        byte type;
        boolean isLeaf;
        byte bval;
        int itemCount;
        int itemSize;
        long dataOffset;
        long dataSize;
        try {
            fis.seek(fileOffset);
            SeekableStreamUtils.readFully(buffer, fis);
            if (isLowToHigh) lbdis = new LittleEndianInputStream(new ByteArrayInputStream(buffer)); else bdis = new DataInputStream(new ByteArrayInputStream(buffer));
            if (isLowToHigh) type = lbdis.readByte(); else type = bdis.readByte();
            if (type == 1) {
                isLeaf = true;
                itemSize = RPTREE_NODE_LEAF_ITEM_SIZE;
                thisNode = new RPTreeLeafNode();
            } else {
                isLeaf = false;
                itemSize = RPTREE_NODE_CHILD_ITEM_SIZE;
                thisNode = new RPTreeChildNode();
            }
            nodeCount++;
            if (isLowToHigh) {
                bval = lbdis.readByte();
                itemCount = lbdis.readShort();
            } else {
                bval = bdis.readByte();
                itemCount = bdis.readShort();
            }
            int itemBlockSize = itemCount * itemSize;
            buffer = new byte[itemBlockSize];
            SeekableStreamUtils.readFully(buffer, fis);
            if (isLowToHigh) lbdis = new LittleEndianInputStream(new ByteArrayInputStream(buffer)); else bdis = new DataInputStream(new ByteArrayInputStream(buffer));
            int startChromID, endChromID;
            int startBase, endBase;
            for (int item = 0; item < itemCount; ++item) {
                if (isLowToHigh) {
                    startChromID = lbdis.readInt();
                    startBase = lbdis.readInt();
                    endChromID = lbdis.readInt();
                    endBase = lbdis.readInt();
                } else {
                    startChromID = bdis.readInt();
                    startBase = bdis.readInt();
                    endChromID = bdis.readInt();
                    endBase = bdis.readInt();
                }
                if (isLeaf) {
                    if (isLowToHigh) {
                        dataOffset = lbdis.readLong();
                        dataSize = lbdis.readLong();
                    } else {
                        dataOffset = bdis.readLong();
                        dataSize = bdis.readLong();
                    }
                    RPTreeLeafNodeItem leafItem = new RPTreeLeafNodeItem(startChromID, startBase, endChromID, endBase, dataOffset, dataSize);
                    thisNode.insertItem(leafItem);
                } else {
                    if (isLowToHigh) dataOffset = lbdis.readLong(); else dataOffset = bdis.readLong();
                    childNode = readRPTreeNode(fis, dataOffset, isLowToHigh);
                    RPTreeChildNodeItem childItem = new RPTreeChildNodeItem(startChromID, startBase, endChromID, endBase, childNode);
                    thisNode.insertItem(childItem);
                }
                fileOffset += itemSize;
            }
        } catch (IOException ex) {
            log.severe("Error reading in R+ tree nodes: " + ex);
            throw new RuntimeException("Error reading R+ tree nodes: \n", ex);
        }
        return thisNode;
    }
}
