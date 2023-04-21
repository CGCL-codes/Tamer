package org.thotheolh.bmics.util;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author thotheolh
 */
public class PageCollection {

    private ArrayList<Block> collection;

    private int pageNum;

    public PageCollection(int pageNumber) {
        collection = new ArrayList<Block>();
        pageNum = pageNumber;
    }

    public void randomizeBlocks() {
        Random random = new Random();
        int numOfShifts = random.nextInt((getCollectionLength() / 2));
        for (int i = 0; i < numOfShifts; i++) {
            int posA = random.nextInt();
            int posB = random.nextInt();
            Block blockA = this.getBlock(posA);
            Block blockB = this.getBlock(posB);
            collection.set(posA, blockB);
            collection.set(posB, blockA);
        }
    }

    public void addBlock(Block b) {
        collection.add(b);
    }

    public Block getBlock(int pos) {
        return collection.get(pos);
    }

    public void removeBlock(int pos) {
        collection.remove(pos);
    }

    public void clearPage() {
        collection.clear();
    }

    public int getCollectionLength() {
        return collection.size();
    }

    /**
     * @return the pageNum
     */
    public int getPageNum() {
        return pageNum;
    }

    /**
     * @param pageNum the pageNum to set
     */
    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }
}
