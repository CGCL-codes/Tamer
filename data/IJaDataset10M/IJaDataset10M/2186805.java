package de.bielefeld.uni.cebitec.r2cat.gui;

import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.Vector;
import de.bielefeld.uni.cebitec.qgram.Match;
import de.bielefeld.uni.cebitec.qgram.MatchList;

/**
 * Thi class visualizes a match as a diagonal line.
 * @author Peter Husemann
 * 
 */
public class MatchDisplayerList implements Iterable<MatchDisplayer> {

    private MatchList alignmentsPositions;

    private Vector<MatchDisplayer> matchDisplayerList;

    private boolean displayReversedComplements = false;

    private boolean displayOffsets;

    private boolean needsRegeneration = false;

    /**
	 * Defines the selection type.
	 * <ul>
	 * <li> ONLY means that the current selection is cleared and the new
	 * selection becomes the actual one
	 * <li> ADD add to the current selection
	 * <li> REMOVE remove from the current selection
	 * <li> TOGGLE switch; if selected unselect and vice versa
	 * </ul>
	 * 
	 * @author Peter Husemann
	 * 
	 */
    public static enum SelectionType {

        ONLY, ADD, REMOVE, TOGGLE
    }

    ;

    public MatchDisplayerList(MatchList matchList) {
        this.alignmentsPositions = matchList;
        matchDisplayerList = new Vector<MatchDisplayer>();
    }

    /**
	 * Generates a new list of {@link MatchDisplayer}s which are
	 * scaled to a given width and heigth.
	 * 
	 * @param width
	 *            maximum width in pixel for the target(s)
	 * @param heigth
	 *            maximum heigth for the stacked queries
	 */
    public void generateMatchDisplayerList(int width, int heigth) {
        double normalisationX = 1;
        double normalisationY = 1;
        if (!alignmentsPositions.isEmpty()) {
            normalisationX = (double) width / alignmentsPositions.getStatistics().getTargetsSize();
            MatchDisplayer.setNormalisationFactorX(normalisationX);
            normalisationY = (double) heigth / alignmentsPositions.getStatistics().getQueriesSize();
            MatchDisplayer.setNormalisationFactorY(normalisationY);
            matchDisplayerList.clear();
            for (Match ap : alignmentsPositions) {
                matchDisplayerList.add(new MatchDisplayer(ap));
            }
        }
    }

    /**
	 * Rescales the {@link MatchDisplayer}s to a given width and
	 * height. <br>
	 * If the list was not generated: fallback and do it. <br>
	 * 
	 * @param width
	 * @param heigth
	 */
    public void rescaleMatchDisplayerList(int width, int heigth) {
        double normalisationX = 1;
        double normalisationY = 1;
        normalisationX = (double) width / alignmentsPositions.getStatistics().getTargetsSize();
        MatchDisplayer.setNormalisationFactorX(normalisationX);
        normalisationY = (double) heigth / alignmentsPositions.getStatistics().getQueriesSize();
        MatchDisplayer.setNormalisationFactorY(normalisationY);
        if (this.isGenerated()) {
            for (MatchDisplayer matchDisplayer : matchDisplayerList) {
                matchDisplayer.rescale(displayOffsets, displayReversedComplements);
            }
        } else {
            this.generateMatchDisplayerList(width, heigth);
        }
    }

    public void regenerate() {
        for (MatchDisplayer matchDisplayer : matchDisplayerList) {
            matchDisplayer.rescale(displayOffsets, displayReversedComplements);
        }
        needsRegeneration = false;
    }

    public Iterator<MatchDisplayer> iterator() {
        return matchDisplayerList.iterator();
    }

    /**
	 * Returns the closest alignment to a given point.
	 * 
	 * @param point
	 *            given point
	 * @return MatchDisplayer which is the closest
	 */
    public MatchDisplayer getClosestHit(Point2D.Double point) {
        double smallestDist = Double.MAX_VALUE;
        MatchDisplayer smallest = new MatchDisplayer();
        for (MatchDisplayer elem : matchDisplayerList) {
            if (elem.ptSegDist(point) < smallestDist) {
                smallestDist = elem.ptSegDist(point);
                smallest = elem;
            }
        }
        return smallest;
    }

    /**
	 * Generates a vector of all matches which are near the given
	 * point regarding a given distance
	 * 
	 * @param point
	 * @param distance
	 * @return Vector of MatchDisplayers
	 */
    public Vector<MatchDisplayer> getNearHits(Point2D.Double point, Double distance) {
        Vector<MatchDisplayer> hits = null;
        for (MatchDisplayer elem : matchDisplayerList) {
            if (elem.ptSegDist(point) < distance) {
                if (hits == null) {
                    hits = new Vector<MatchDisplayer>();
                }
                hits.add(elem);
            }
        }
        return hits;
    }

    /**
	 * Unmarks all alignments and notifies all registered observers.
	 */
    public void unmakAll() {
        alignmentsPositions.unmarkAllAlignments();
    }

    public void setAllVisible() {
        for (MatchDisplayer elem : matchDisplayerList) {
            elem.setInvisible(false);
        }
    }

    /**
	 * Creates a histogram of the coverage of the target sequence with alignable
	 * positions.
	 * 
	 * @param numberOfBuckets
	 *            this parameter gives the number of buckets that the histogram
	 *            should have.
	 * @return
	 */
    public double[] getTargetHistogram(int numberOfBuckets) {
        double[] histogram;
        if (numberOfBuckets <= 1) {
            histogram = new double[1];
            histogram[0] = 1;
            return histogram;
        }
        histogram = new double[numberOfBuckets];
        long maximum = alignmentsPositions.getStatistics().getTargetsSize();
        long bucketSize = (long) ((double) (maximum + 1) / (numberOfBuckets - 1));
        long firstBucket = 0;
        long lastBucket = 0;
        long maximumCountOfABucket = 0;
        for (Match ap : alignmentsPositions) {
            firstBucket = (ap.getTargetStart() + ap.getTarget().getOffset()) / bucketSize;
            lastBucket = (ap.getTargetEnd() + ap.getTarget().getOffset()) / bucketSize;
            if (firstBucket > lastBucket) {
                long tmp = lastBucket;
                lastBucket = firstBucket;
                firstBucket = tmp;
            }
            for (int i = (int) firstBucket; i <= lastBucket; i++) {
                try {
                    histogram[i] += 1.;
                    if (histogram[i] > maximumCountOfABucket) {
                        maximumCountOfABucket = (long) histogram[i];
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println(this.getClass() + ":\nHistogram bin should be smaller than " + histogram.length + " but is " + i);
                }
            }
        }
        for (int i = 0; i < histogram.length; i++) {
            histogram[i] /= maximumCountOfABucket;
        }
        return histogram;
    }

    /**
	 * Returns whether or not the List of displayable alignments has been
	 * generated or not.
	 * 
	 * @return
	 */
    public boolean isGenerated() {
        if (matchDisplayerList.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public boolean needsRegeneration() {
        return needsRegeneration;
    }

    public void setNeedsRegeneration(boolean reg) {
        this.needsRegeneration = reg;
    }

    /**
	 * Marks all alignments in a rectangle between a given top left and bottom
	 * right point. the type of selection can be specified by the markOperation.
	 * 
	 * @param topLeft
	 * @param bottomRight
	 * @param markOperation
	 */
    public void markArea(Point2D.Double topLeft, Point2D.Double bottomRight, SelectionType markOperation) {
        for (MatchDisplayer elem : matchDisplayerList) {
            if (markOperation == SelectionType.ONLY) {
                elem.setSelected(false);
            }
            if (elem.x1 > topLeft.x && elem.x1 < bottomRight.x && elem.x2 > topLeft.x && elem.x2 < bottomRight.x && elem.y1 > topLeft.y && elem.y1 < bottomRight.y && elem.y2 > topLeft.y && elem.y2 < bottomRight.y) {
                if (markOperation == SelectionType.ADD || markOperation == SelectionType.ONLY) {
                    elem.setSelected(true);
                } else if (markOperation == SelectionType.REMOVE) {
                    elem.setSelected(false);
                } else if (markOperation == SelectionType.TOGGLE) {
                    elem.switchSelected();
                }
            }
        }
        if (alignmentsPositions.hasChanged()) {
            alignmentsPositions.markQueriesWithSelectedAps();
            alignmentsPositions.notifyObservers(MatchList.NotifyEvent.MARK);
        }
    }

    public boolean isEmpty() {
        return matchDisplayerList.isEmpty();
    }

    public int size() {
        return alignmentsPositions.size();
    }

    /**
	 * Removes all displayable elements
	 */
    public void clear() {
        matchDisplayerList.clear();
    }

    /**
	 * 
	 * <ul>
	 * <li> display all alignments in the direction as stored
	 * <li> display all alignments reverse complemented if set so
	 * </ul>
	 * 
	 */
    public void showReversedComplements(boolean flip) {
        this.needsRegeneration = true;
        this.displayReversedComplements = flip;
    }

    public void setDisplayOffsets(boolean displayOffsets) {
        this.needsRegeneration = true;
        this.displayOffsets = displayOffsets;
    }

    public boolean getDisplayOffsets() {
        return displayOffsets;
    }
}
