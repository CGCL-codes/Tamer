package AccordionLRACDrawer;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;
import AccordionDrawer.AccordionDrawer;
import AccordionDrawer.DrawableRange;
import LRAC.LiveRAC;

public class Groups {

    AccordionLRACDrawer lrd;

    LiveRAC lr;

    /**
	 * Rearrangable list of RangeLists (groups)
	 */
    LinkedList<MetricRangeList> groups;

    Vector<MetricRangeList> groupsByIndex;

    /** these numbers are permanent, but the ordering of the groups
	 * changes to reflect user interactions for which color wins out
	 * in a tie
	 */
    public static int flashGroup;

    public static int critGroup;

    public static int foundGroupDraw;

    public static int foundGroupMark;

    public Groups(AccordionLRACDrawer lrd) {
        this.lrd = lrd;
        groups = new LinkedList<MetricRangeList>();
        groupsByIndex = new Vector<MetricRangeList>(7);
        Color flashCol = new Color(128, 128, 128, 128);
        Color critCol = Color.RED;
        Color foundCol = Device.foundColor;
        critGroup = addGroup(false, critCol);
        setGroupVerticalDrawingRange(0.0, 1.0, critGroup);
        setGroupHorizontalDrawingRange(0.0, 1.0, critGroup);
        setGroupEnable(critGroup, true);
        flashGroup = addGroup(false, flashCol);
        setGroupVerticalDrawingRange(0.0, 0.05, flashGroup);
        setGroupEnable(flashGroup, false);
        foundGroupDraw = addGroup(false, null);
        foundGroupMark = addGroup(true, foundCol);
        setGroupVerticalDrawingRange(0.95, 0.99, foundGroupMark);
        setGroupEnable(foundGroupDraw, false);
    }

    /**
	 * Defines a new group.  
	 * @param thisTreeOnly
	 * @param col
	 * @return
	 */
    public int addGroup(boolean thisTreeOnly, Color col) {
        int groupCount = groups.size();
        Color realColor = null;
        if (col != null) realColor = new Color(col.getRed(), col.getGreen(), col.getBlue(), groupCount);
        MetricRangeList g = new MetricRangeList(groupCount, realColor, true, thisTreeOnly);
        System.out.println("adding group " + groupCount);
        groups.add(g);
        groupsByIndex.add(g);
        return groupCount;
    }

    public void setGroupColor(int group, Color c) {
        ((MetricRangeList) groupsByIndex.get(group)).setColor(c);
    }

    public void setGroupEnable(int group, boolean on) {
        ((MetricRangeList) groupsByIndex.get(group)).setEnabled(on);
    }

    public void addNodesToGroup(int min, int max, int group, Device dev) {
        MetricRangeList addToGroup = (MetricRangeList) groupsByIndex.get(group);
        addToGroup.addRange(min, max, dev, false, group == foundGroupDraw);
    }

    public void addNodesToGroup(MetricRangeList r, int group, Device dev) {
        Iterator iter = r.getRanges().iterator();
        while (iter.hasNext()) {
            DrawableRange currRange = (DrawableRange) iter.next();
            addNodesToGroup(currRange.getMin(), currRange.getMax(), group, dev);
        }
    }

    public void addNodesToGroup(int min, int max, int group, Device dev, boolean checkCollide) {
        MetricRangeList addToGroup = (MetricRangeList) groupsByIndex.get(group);
        addToGroup.addRange(min, max, dev, group == foundGroupDraw, checkCollide);
    }

    public void addNodesToGroup(MetricRangeList r, int group) {
        Iterator iter = r.getRanges().iterator();
        while (iter.hasNext()) {
            MetricRange currRange = (MetricRange) iter.next();
            addNodesToGroup(currRange.getMin(), currRange.getMax(), group, currRange.getDevice());
        }
    }

    public void removeNodesFromGroup(int min, int max, int group, Device dev) {
        ((MetricRangeList) groupsByIndex.get(group)).removeRange(min, max, dev);
    }

    public void removeDeviceFromGroup(Device dev, int group) {
        ((MetricRangeList) groupsByIndex.get(group)).removeDevice(dev);
    }

    public void removeDeviceFromAllGroups(Device dev) {
        Iterator<MetricRangeList> it = groups.iterator();
        while (it.hasNext()) {
            it.next().removeDevice(dev);
        }
    }

    public Color getGroupColor(int group) {
        return ((MetricRangeList) groupsByIndex.get(group)).getColor();
    }

    public void clearGroup(int group) {
        ((MetricRangeList) groupsByIndex.get(group)).clear();
    }

    public void clearAllGroups() {
        Iterator<MetricRangeList> it = groups.iterator();
        while (it.hasNext()) {
            it.next().clear();
        }
    }

    public void unmarkGroup(int group) {
        changedMarks();
        clearGroup(group);
    }

    public MetricRangeList getGroup(int group) {
        return ((MetricRangeList) groupsByIndex.get(group));
    }

    public double[] getGroupDrawingRange(int groupNum) {
        MetricRangeList rl = getGroup(groupNum);
        if (rl != null) return rl.getVerticalDrawingRange();
        return null;
    }

    public void setGroupVerticalDrawingRange(double start, double end, int group) {
        double[] range = { start, end };
        getGroup(group).setVerticalDrawingRange(range);
    }

    public void setGroupHorizontalDrawingRange(double start, double end, int group) {
        double[] range = { start, end };
        getGroup(group).setHorizontalDrawingRange(range);
    }

    public void changedMarks() {
        lrd.changedMarks();
    }

    /** Return one MetricCell per group range item.
	 * @author = Tamara Munzner, Serdar Tasiran
	 */
    public ArrayList getGroupForest(int groupindex, AccordionLRACDrawer asd) {
        MetricRangeList group = getGroup(groupindex);
        ArrayList forestRoots = new ArrayList();
        Iterator groupIter = group.getRanges().iterator();
        while (groupIter.hasNext()) {
            MetricRange r = (MetricRange) groupIter.next();
            {
                Device dev = r.getDevice();
                for (int i = r.getMin(); i <= r.getMax(); i++) forestRoots.add(dev.getMetric(i));
            }
        }
        return forestRoots;
    }

    /**
	 * Resizes the specified group, either by growing it or shrinking it
	 * note: animated transition based on numSteps of animation
	 * 
	 * 
	 */
    public void growGroup(int groupindex, boolean grow, int numSteps, boolean horizontal, boolean vertical) {
        lrd.endAllTransitions();
        MetricRangeList group = lrd.seededGroups.getGroup(groupindex);
        Hashtable newToMove = new Hashtable();
        System.out.println("Group before:" + group);
        if (horizontal) {
            if (!grow) group = (MetricRangeList) group.flipRangeToShrink(AccordionDrawer.X, lrd);
            MetricRangeList blockForest = new MetricRangeList(group, lrd.getListOfMetrics(), lrd.metricAxis);
            System.out.println("  Block: " + blockForest);
            lrd.metricAxis.resizeForest(blockForest, numSteps, newToMove, lrd.getInflateIncr());
            System.out.println("Move queue: " + lrd.toMove);
        }
        if (vertical) {
            if (!grow) {
                System.out.println("shrink temp disabled in Y dir");
            } else {
                MetricRangeList blockForest = new MetricRangeList(group, lrd.getListOfDevices(), lrd.deviceAxis);
                System.out.println(" Block: " + blockForest);
                lrd.deviceAxis.resizeForest(blockForest, numSteps, newToMove, lrd.getInflateIncr());
                System.out.println("Move queue: " + lrd.toMove);
            }
        }
        lrd.toMove = newToMove;
        lrd.requestRedraw();
    }

    /**
	 * @return Returns the groups.
	 */
    public LinkedList<MetricRangeList> getGroups() {
        return groups;
    }
}
