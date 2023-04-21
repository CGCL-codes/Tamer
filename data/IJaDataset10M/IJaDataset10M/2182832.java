package de.bielefeld.uni.cebitec.cav.datamodel;

import java.util.Collections;
import java.util.Vector;
import javax.swing.ProgressMonitor;
import de.bielefeld.uni.cebitec.cav.controller.GuiController;

/**
 * Sorts the contigs based on their hits with a sliding window approach. The idea
 * is from Jochen Blom. For each contig a histogram is calculated of the number
 * of matching qgrams. Then a sliding window with the size of the contig is
 * moved over the histogram and the total count is computed. The index with the
 * highest count (center of mass) is then used for sorting.
 * 
 * (The sorter only changes the center of mass value to the index with the
 * highest windowcount. after that the queryOrder Vector in
 * AlignmentPositionsList has to be sorted.)
 * 
 * (The sorter can be run as a thread, in order not to block the gui)
 * 
 * @author phuseman
 * 
 */
public class ContigSorter implements Runnable {

    private AlignmentPositionsList apl;

    private int[] histogram;

    private ProgressMonitor progress;

    private GuiController guiController;

    /**
	 * Gives the {@link AlignmentPositionsList} which should be sorted.
	 * 
	 * @param apl
	 */
    public ContigSorter(AlignmentPositionsList apl) {
        this.apl = apl;
    }

    /**
	 * An array of the reference genome size is created. This is used as a
	 * histogram for the hits of one contig (at a time). For each contig all
	 * matches (their qhits) are added to the appropriate positions in the
	 * histogram. Then a window with the size of the contigs is slid over the
	 * histogram and the sum of all hits is calculated. The index with the
	 * highest number is remembered. It is most likely the position where one
	 * would put this contig. This position is saved in the centerOfMass
	 * variable of each contig. Thus it can be easily sorted.
	 * 
	 */
    public void sort() {
        int arraySize = (int) apl.getStatistics().getTargetsSize() + 1;
        histogram = new int[arraySize];
        DNASequence query;
        for (int i = 0; i < apl.getQueries().size(); i++) {
            query = apl.getQueries().get(i);
            for (AlignmentPosition ap : apl) {
                if (progress != null && progress.isCanceled()) {
                    return;
                }
                if (query.equals(ap.getQuery())) {
                    enterApToHistogram(ap);
                }
            }
            int bestIndex = getIndexOfHighestWindowCountFromHistogram((int) query.getSize());
            apl.getQueries().get(i).setSortKey(bestIndex);
            this.resetHistogram();
            reportProgress(((double) i / apl.getQueries().size()));
            if (progress != null && progress.isCanceled()) {
                return;
            }
        }
        reportProgress(1.);
        if (guiController != null) {
            guiController.sortContigsDone(this);
        }
    }

    /**
	 * Enters the maximum number of supporting qgrams to each position of the
	 * target historgram. If the qgrams are not present use pseudocount of one.
	 * 
	 * @param ap
	 *            hit, match
	 */
    private void enterApToHistogram(AlignmentPosition ap) {
        int qhits = ap.getNumberOfQHits();
        if (qhits <= 0) {
            qhits = 1;
        }
        int start = (int) (ap.getTargetStart() + ap.getTarget().getOffset());
        int stop = (int) (ap.getTargetEnd() + ap.getTarget().getOffset());
        for (int i = start; i < stop; i++) {
            if (i >= histogram.length || i < 0) {
                break;
            }
            if (histogram[i] < qhits) {
                histogram[i] = qhits;
            }
        }
    }

    /**
	 * Sets the histogram to zero.
	 */
    private void resetHistogram() {
        for (int i = 0; i < histogram.length; i++) {
            histogram[i] = 0;
        }
    }

    /**
	 * Computes the cummulated count for each possible window of the given size
	 * and returns the endposition of the window with the highest count.
	 * 
	 * @param windowSize
	 *            Size of the window.
	 * @return index with highest cumulated count
	 */
    private int getIndexOfHighestWindowCountFromHistogram(int windowSize) {
        double highestValue = -1;
        double actualValue = 0;
        int indexOfHighestValue = 0;
        for (int i = 0; i < windowSize; i++) {
            actualValue += histogram[i];
        }
        for (int i = windowSize; i < histogram.length; i++) {
            if ((histogram[i] + histogram[i - windowSize]) == 0) {
                continue;
            }
            actualValue -= histogram[i - windowSize];
            if (histogram[i] != 0) {
                actualValue += histogram[i];
                if (actualValue > highestValue) {
                    highestValue = actualValue;
                    indexOfHighestValue = i;
                }
            }
        }
        return indexOfHighestValue;
    }

    /**
	 * If a MatchDialog is registered the output will be written in its
	 * textfield. Otherwise on the command line.
	 * 
	 * @param percentDone
	 *            how far are we?
	 * @param s
	 *            explaining sentence
	 */
    public void reportProgress(double percentDone) {
        if (progress != null) {
            progress.setProgress((int) (percentDone * 100));
        }
    }

    @SuppressWarnings("unchecked")
    public Vector<DNASequence> getQueryOrder() {
        Vector<DNASequence> order = apl.getQueries();
        Collections.sort(order);
        return order;
    }

    @Override
    public void run() {
        if (apl != null && !apl.isEmpty()) {
            this.sort();
        }
    }

    public void register(GuiController guiController) {
        this.guiController = guiController;
    }

    /**
	 * Registers the ProgressMonitor, to pass progress changes to it.
	 * 
	 * @param matchDialog
	 */
    public void register(ProgressMonitor progress) {
        this.progress = progress;
    }
}
