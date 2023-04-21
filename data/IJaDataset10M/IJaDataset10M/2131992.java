package AccordionBacDrawer;

import java.awt.Color;
import java.util.TreeSet;
import java.util.Iterator;
import AccordionDrawer.*;

/**
 * A class representing a list of RangeInTrees. A RangeInTree
 * represents ordered pair (min,max)) of TreeNodes.
 *
 * This class is a helper class for TreeJuxtaposer and keeps a
 * resizeable array of RangeInTree's.
 *
 * Note that each RangeInTrees item in a RangeList can be associated
 * with a different Tree.
 * 
 * @see TreeJuxtaposer.RangeInList
 * @see AccordionDrawer.TreeNode
 * 
 * @author  Tamara Munzner
 * */
public class RangeList extends AbstractRangeList {

    TreeSet ranges;

    boolean enabled;

    Color col;

    int key;

    boolean thisTreeOnly;

    ColorTree rangeTree;

    public RangeList(int i) {
        col = new Color((float) .5, (float) .5, (float) .5);
        enabled = true;
        ranges = new TreeSet();
        thisTreeOnly = false;
        key = i;
    }

    public RangeList(int i, Color c, boolean on, boolean only) {
        key = i;
        col = c;
        enabled = on;
        ranges = new TreeSet();
        thisTreeOnly = only;
    }

    public void addRange(int min, int max, Bac bac, boolean draw, boolean collideDetect) {
        RangeInBac addBac = new RangeInBac(min, max, bac);
        RangeInBac intersectRange;
        if (collideDetect) {
            Iterator iter = ranges.iterator();
            while (iter.hasNext()) {
                RangeInBac currRange = (RangeInBac) iter.next();
                intersectRange = currRange.intersects(addBac, draw);
                if (intersectRange != null) {
                    iter.remove();
                    addBac = intersectRange;
                }
            }
        }
        insertInOrder(addBac, draw);
    }

    public void removeRange(int min, int max, Bac bac) {
        System.err.println("can't remove a range like this");
        RangeInBac r = new RangeInBac(min, max, bac);
        ranges.remove(r);
    }

    private void insertInOrder(RangeInBac bac, boolean draw) {
        ranges.add(bac);
    }

    public void clear() {
        ranges.clear();
        rangeTree = null;
    }

    void print() {
        RangeInBac rt;
        try {
            RangeList temp = (RangeList) this.clone();
            for (int i = 0; i < ranges.size(); i++) {
                rt = (RangeInBac) temp.ranges.first();
                temp.ranges.remove(rt);
                System.out.println(i + " treekey " + rt.bac.getKey() + " min " + rt.min + " max " + rt.max);
            }
        } catch (CloneNotSupportedException e) {
            System.out.println("Cloning should work for rangeLists");
            return;
        }
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean on) {
        enabled = on;
    }

    public void setColor(Color c) {
        col = new Color(c.getRed(), c.getGreen(), c.getBlue(), key);
    }

    public Color getColor() {
        if (col != null && col.getAlpha() != key) col = new Color(col.getRed(), col.getGreen(), col.getBlue(), key);
        return col;
    }

    public boolean getThisTreeOnly() {
        return thisTreeOnly;
    }

    void setThisTreeOnly(boolean on) {
        thisTreeOnly = on;
    }

    public int getKey() {
        return key;
    }

    public int size() {
        return ranges.size();
    }

    public double[] getSizesOfAllRanges(SplitLine splitLine) {
        Iterator iter = ranges.iterator();
        double[] size = new double[size()];
        int i = 0;
        while (iter.hasNext()) {
            RangeInBac range = (RangeInBac) iter.next();
            size[i++] = range.getSize(splitLine);
        }
        return size;
    }

    public String toString() {
        String returnString = key + "[";
        Iterator iter = ranges.iterator();
        RangeInBac next = null;
        if (iter.hasNext()) next = (RangeInBac) iter.next();
        while (next != null && iter.hasNext()) {
            returnString += next + ",\n\t";
            next = (RangeInBac) iter.next();
        }
        if (ranges.size() > 0) returnString += next;
        returnString += "]";
        return returnString;
    }

    /**
	 * @return
	 */
    public TreeSet getRanges() {
        return ranges;
    }

    public RangeInBac getHead() {
        return (RangeInBac) ranges.first();
    }

    /**
	 * @param i
	 */
    public void setKey(int i) {
        key = i;
    }

    /**
	 * @param list
	 */
    public void setRanges(TreeSet list) {
        ranges = list;
    }

    public int getNumRanges() {
        return ranges.size();
    }

    /**
	 * @return
	 */
    public ColorTree getColorTree() {
        return rangeTree;
    }

    /**
	 * @param tree
	 */
    public void setColorTree(ColorTree tree) {
        rangeTree = tree;
    }

    public int[] getSplitIndices(boolean horizontal) {
        int xy = horizontal ? AccordionBacDrawer.X : AccordionBacDrawer.Y;
        int[] returnArray = new int[ranges.size() * 2];
        Iterator iter = ranges.iterator();
        int i = 0;
        while (iter.hasNext()) {
            RangeInBac currRange = (RangeInBac) iter.next();
            returnArray[i * 2] = currRange.min - 1;
            returnArray[i * 2 + 1] = currRange.max;
            i++;
        }
        return returnArray;
    }

    public AbstractRangeList onlyThisAD(AccordionDrawer d) {
        return this;
    }

    public AbstractRangeList flipRangeToShrink(int xy, AccordionDrawer ad) {
        RangeList returnRangeList = new RangeList(this.key);
        int splitLineSize = ad.getSplitLine(xy).getSize() - 1;
        if (ranges.size() == 0) {
            return null;
        }
        Iterator flipIter = ranges.iterator();
        RangeInBac prev = (RangeInBac) flipIter.next();
        if (prev.min > 0) returnRangeList.addRange(0, prev.min - 1, prev.bac, false, false);
        while (flipIter.hasNext()) {
            RangeInBac curr = (RangeInBac) flipIter.next();
            if (prev.bac != curr.bac) System.out.println("Error, sequences don't match: " + curr.bac + " " + prev.bac);
            returnRangeList.addRange(prev.max + 1, curr.min - 1, prev.bac, false, false);
            prev = curr;
        }
        if (prev.max < splitLineSize) returnRangeList.addRange(prev.max + 1, splitLineSize, prev.bac, false, false);
        return returnRangeList;
    }

    public double[] getSizesOfAllRanges(SplitLine splitLine, int frameNum) {
        Iterator iter = ranges.iterator();
        double[] size = new double[size()];
        int i = 0;
        while (iter.hasNext()) {
            RangeInBac range = (RangeInBac) iter.next();
            size[i++] = range.getSize(splitLine);
        }
        return size;
    }

    public double getUnshrinkableTotal(AccordionDrawer ad, SplitLine splitLine, int frameNum) {
        double size = 0.0;
        if (ranges.size() == 0) return size;
        Iterator iter = ranges.iterator();
        RangeInBac ris = (RangeInBac) iter.next();
        RangeInBac betweenRanges = new RangeInBac(0, ris.min, ris.bac);
        double betweenSize = betweenRanges.getSize(splitLine);
        if (betweenSize < ad.minContextPeriphery) size += betweenSize;
        int prevMax = ris.max;
        while (iter.hasNext()) {
            ris = (RangeInBac) iter.next();
            if (iter.hasNext()) {
                betweenRanges = new RangeInBac(prevMax, ris.min, ris.bac);
                betweenSize = betweenRanges.getSize(splitLine);
                if (betweenSize < ad.minContextInside) size += betweenSize;
            }
            prevMax = ris.max;
        }
        betweenRanges = new RangeInBac(ris.max, splitLine.getSize(), ris.bac);
        betweenSize = betweenRanges.getSize(splitLine);
        if (betweenSize < ad.minContextPeriphery) size += betweenSize;
        return size;
    }
}

;
