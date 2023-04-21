package AccordionSequenceDrawer;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;
import AccordionDrawer.SplitAxis;
import AccordionDrawer.SplitLine;

/**
 * @author jslack
 * 
 * ColumnColorList: tree of colors for sequential sequences that share the same color value
 * 
 * Each vertical splitCell (split cells that belong to splitLine[X] have a horizontal offset and can be seen
 * as a representative of a column, hence vertical), has a culling object for its range.  The culling
 * object is created on demand when the splitCell reaches the size of a block (or smaller) in the rendering
 * process (the size of each block required for a rendering is computed before a scene is drawn by
 * asd.preDrawNewScene when it computes the listOfSites).  If the cullingObject of a splitCell in listOfSites
 * is a columnColorList, then we have previously computed the colors for sequence blocks in listOfSequences
 * and can use the columnColors tree to retrieve colors for any range of sequences for this splitCell.
 * 
 * When we need to compute the ColumnColorList for a splitCell, we create the ColumnColorList for its
 * children (this process is recursive, so if the children are already ColumnColorLists we don't need to
 * recompute, both children are null causes the base case where we create a new ColumnNumberList for that
 * splitCell) which creates the columnNumberList for their parent splitCell as the cullingObject.  After the children
 * are created, the columnNumberList (an array of integers that for each sequence for this range of sites)
 * is tallied and we determine colors for each sequence for each range of sites.  The colors that represent
 * each sequence are combined into ranges (a ColumnColorSet) and added to the columnColors treeset.  The
 * columnNumberList is added to the parent of this splitCell's cullingObject (which is either null or a
 * columnNumberList).
 *
 */
public class ColumnColorList {

    public static final int leftBound = SplitAxis.minBound;

    public static final int rightBound = SplitAxis.maxBound;

    TreeSet columnColors;

    static ColumnNumberSet globalCount = new ColumnNumberSet(null, ' ');

    public ColumnColorList(SplitLine line, SplitAxis splitAxis, ArrayList sequences) {
        if (line.getCullingObject() instanceof ColumnColorList) {
            System.out.println("Error: culling object should be null or columnnumberlist when calling column color list constructor");
            return;
        }
        if (splitAxis == null || line == null) System.out.println("either line or axis is null: " + splitAxis + " " + line + " ");
        SplitLine left = line.getLeftChild();
        SplitLine right = line.getRightChild();
        SplitLine[] bounds = splitAxis.getBounds(line);
        if (left == null && right == null) line.setCullingObject(new ColumnNumberList(sequences, splitAxis.getSplitIndex(bounds[leftBound]) + 1, splitAxis.getSplitIndex(bounds[rightBound])));
        if (left != null && !(left.getCullingObject() instanceof ColumnColorList)) new ColumnColorList(left, splitAxis, sequences);
        if (right != null && !(right.getCullingObject() instanceof ColumnColorList)) new ColumnColorList(right, splitAxis, sequences);
        if (!(line.getCullingObject() instanceof ColumnNumberList)) {
            System.out.println("Error: constructing kids as column color lists should make the parent a number list");
            return;
        }
        line.setCullingObject(new ColumnColorList(line, splitAxis, sequences, true));
    }

    private ColumnColorList(SplitLine line, SplitAxis splitAxis, ArrayList sequences, boolean dummy) {
        SplitLine leftKid = line.getLeftChild();
        SplitLine rightKid = line.getRightChild();
        if (line.getCullingObject() instanceof ColumnNumberList && (leftKid == null || leftKid.getCullingObject() instanceof ColumnColorList) && (rightKid == null || rightKid.getCullingObject() instanceof ColumnColorList)) {
            SplitLine[] ancestors = splitAxis.getBounds(line);
            if (leftKid != null && rightKid == null) {
                line.setCullingObject(new ColumnNumberList((ColumnNumberList) line.getCullingObject(), new ColumnNumberList(sequences, splitAxis.getSplitIndex(ancestors[rightBound]), splitAxis.getSplitIndex(ancestors[rightBound]))));
            }
            columnColors = new TreeSet();
            ColumnNumberList cnl = (ColumnNumberList) line.getCullingObject();
            int start = 0;
            ColumnNumberSet currCns = cnl.get(start);
            char currColor;
            char prevColor = currCns.getColor();
            ColumnNumberSet columnCNS = new ColumnNumberSet(null, prevColor);
            for (int i = 1; i < cnl.columnNumberList.size(); i++) {
                currCns = cnl.get(i);
                currColor = currCns.getColor();
                columnCNS.addColor(currColor);
                if (currColor != prevColor) {
                    columnColors.add(new ColumnColorSet(prevColor, start, i - 1));
                    start = i;
                }
                prevColor = currColor;
            }
            columnColors.add(new ColumnColorSet(prevColor, start, cnl.columnNumberList.size() - 1));
            globalCount.addColor(columnCNS.getColor());
            SplitLine parent = line.getParent();
            if (parent != null) parent.setCullingObject(new ColumnNumberList((ColumnNumberList) parent.getCullingObject(), cnl));
        } else {
            System.out.println("Something is the wrong type in the private constructor for column color list");
        }
    }

    public void addToColors(int min, int max, char color) {
        columnColors.add(new ColumnColorSet(color, min, max));
    }

    public Color getColor(int min, int max) {
        char returnColor = '-';
        ColumnColorSet fakeSet = new ColumnColorSet('-', min, max);
        ColumnColorSet.getColor = true;
        if (columnColors.contains(fakeSet)) {
            returnColor = ColumnColorSet.returnColorSet.colorLetter;
            ColumnColorSet.returnColorSet = null;
        }
        return Sequence.getColor(returnColor);
    }

    public String toString() {
        Iterator iter = columnColors.iterator();
        String str = "[";
        while (iter.hasNext()) {
            ColumnColorSet ccs = (ColumnColorSet) iter.next();
            if (iter.hasNext()) str += ccs + ", "; else str += ccs + "]";
        }
        return str;
    }
}
