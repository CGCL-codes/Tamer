package org.tokaf.algorithm;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import org.tokaf.MyTopKCompareValues;
import org.tokaf.TopKElement;
import org.tokaf.bestcolumnfinder.BestColumnFinder;
import org.tokaf.bestcolumnfinder.MissingValuesFinder;
import org.tokaf.datasearcher.DataSearcher;
import org.tokaf.rater.Rater;

/**
 * <p> Base class for all top-k algorithms. Contains useful general methods.
 * </p> <p> Copyright (c) 2006 </p>
 * @author Alan Eckhardt
 * @version 1.0
 */
public abstract class Algorithm {

    protected boolean end = false;

    protected int actColumn = 0;

    protected HashMap topKlist = new HashMap();

    protected Rater rater;

    protected DataSearcher[] data;

    protected BestColumnFinder finder;

    protected int K;

    protected String prefix;

    protected boolean[] finished;

    long step;

    long maxStep;

    public long runTime = -1;

    public long bcfTime = 0;

    public long rateTime = 0;

    public long ratingTime = 0;

    public long checkEndTime = 0;

    public long dataTime = 0;

    public long sortTime = 0;

    public long searchTime = 0;

    /**
	 * Finds the entity el in the list.
	 * @param el TopKElement
	 * @return the found entity.
	 */
    public TopKElement findEntity(TopKElement el) {
        if (el == null) return null;
        return (TopKElement) topKlist.get(el.name);
    }

    /**
	 * Write statistics about algorithm to a .cvs file.
	 * @param fileName
	 * @param additionalInfo
	 */
    public void writeStats(String fileName, String[] additionalInfo) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(fileName, true));
            Calendar cas = Calendar.getInstance();
            out.write(cas.get(Calendar.YEAR) + "." + (cas.get(Calendar.MONTH) + 1) + "." + cas.get(Calendar.DAY_OF_MONTH) + " " + cas.get(Calendar.HOUR_OF_DAY) + ":" + cas.get(Calendar.MINUTE) + ";");
            out.write(this.getClass().getSimpleName() + ";");
            out.write(finder.getClass().getSimpleName() + ";");
            out.write(rater.getClass().getSimpleName() + ";");
            out.write(data[0].getClass().getSimpleName() + ";");
            out.write(data[0].getNormalizer().getClass().getSimpleName() + ";");
            out.write("" + K + ";");
            out.write("" + data.length + ";");
            out.write(bcfTime + ";");
            out.write(rateTime + ";");
            out.write(dataTime + ";");
            out.write(ratingTime + ";");
            out.write(checkEndTime + ";");
            out.write(sortTime + ";");
            out.write(searchTime + ";");
            out.write(step + ";");
            if (finder instanceof MissingValuesFinder) {
                out.write("" + ((MissingValuesFinder) finder).getDepth() + ";");
            } else out.write("" + 0 + ";");
            out.write(0 + ";");
            out.write(0 + ";");
            for (int i = 0; i < additionalInfo.length; i++) out.write(additionalInfo[i] + ";");
            out.write("\n");
            out.close();
        } catch (IOException e) {
        }
    }

    /**
	 * Computes for every datasearcher the average of ratios between the number
	 * of step, in which the element was found, and the number of step, in which
	 * it was acknowledged, that it is among top k. The second number is equal
	 * to the actual position of datasearcher.
	 * @return the ratio for each datasearcher.
	 */
    public double[] getRatioSeenValidate() {
        ArrayList al = createArrayList();
        double[] res = new double[data.length];
        for (int i = 0; i < res.length; i++) res[i] = 0;
        for (int i = 0; i < al.size() && i < K; i++) {
            for (int j = 0; j < res.length; j++) {
                TopKElement el = (TopKElement) al.get(i);
                if (el.getPosition(j) != -1) res[j] += el.getPosition(j);
            }
        }
        for (int i = 0; i < res.length; i++) if (K > al.size()) res[i] = res[i] / (data[i].getPosistion() * al.size()); else res[i] = res[i] / (data[i].getPosistion() * K);
        return res;
    }

    /**
	 * Computes for every datasearcher the average of ratios between the number
	 * of step, in which the element was found, and the number of step, in which
	 * it was acknowledged, that it is among top k. The second number is equal
	 * to the actual position of datasearcher.
	 * @return the ratio for each datasearcher.
	 */
    public double[] getAverageSeen() {
        ArrayList al = createArrayList();
        double[] res = new double[data.length];
        for (int i = 0; i < res.length; i++) res[i] = 0;
        for (int j = 0; j < res.length; j++) {
            int i = 0;
            for (i = 0; i < al.size() && i < K; i++) {
                TopKElement el = (TopKElement) al.get(i);
                if (el.getFound(j) != -1) res[j] += el.getFound(j);
            }
            res[j] /= i;
        }
        return res;
    }

    /**
	 * Transforms HashMap into ArrayList for better handling.
	 * @return arraylist of top k objects.
	 */
    protected ArrayList createArrayList() {
        ArrayList al = new ArrayList(topKlist.values());
        Collections.sort(al, new MyTopKCompareValues());
        return al;
    }

    /**
	 * @return Threshold value, if exists.
	 */
    public abstract TopKElement getThreshold();

    /**
	 * @return DataSearchers of this Algorithm.
	 */
    public DataSearcher[] getDataSearchers() {
        return data;
    }

    /**
	 * Performs the run of algorithm. It can be limited by maxSteps.
	 * @return ArrayList of top k elements.
	 */
    public abstract ArrayList run();

    /**
	 * @return current top-k set.
	 */
    public ArrayList getTopK() {
        return createArrayList();
    }

    /**
	 * @return the hashmap containing all elements.
	 */
    public HashMap getTopKHashMap() {
        return topKlist;
    }

    /**
	 * Do one step of algorithm.
	 * @return int
	 */
    public abstract int step();

    /**
	 * Checks every datasearcher, whether it has ended.
	 * @return whether every datasearcher ended.
	 */
    protected boolean checkEnd() {
        for (int i = 0; i < data.length; i++) if (finished[i] != true) return false;
        return true;
    }

    public Algorithm(DataSearcher[] data, Rater rater, BestColumnFinder finder, String prefix, int K) {
        this.data = data;
        this.rater = rater;
        this.finder = finder;
        this.prefix = prefix;
        this.K = K;
        this.finished = new boolean[data.length];
        for (int i = 0; i < finished.length; i++) finished[i] = false;
    }

    /**
	 * @return K, where K is the number of returned elements.
	 */
    public int getK() {
        return K;
    }

    /**
	 * Sets number of desired objects to given K.
	 * @param k
	 */
    public void setK(int k) {
        K = k;
    }

    /**
	 * @return BestColumnFinder associated with algorithm.
	 */
    public BestColumnFinder getFinder() {
        return finder;
    }

    /**
	 * @param finder BestColumnFinder to be associated with algorithm.
	 */
    public void setFinder(BestColumnFinder finder) {
        this.finder = finder;
    }

    /**
	 * @return number of max steps done by algorithm. -1 represents no
	 *         limitation.
	 */
    public long getMaxStep() {
        return maxStep;
    }

    /**
	 * Sets the number of max steps done by algorithm. -1 represents no
	 * limitation.
	 * @param maxStep
	 */
    public void setMaxStep(long maxStep) {
        this.maxStep = maxStep;
    }

    /**
	 * @return Rater associated to algorithm.
	 */
    public Rater getRater() {
        return rater;
    }

    /**
	 * Sets rater to algorithm.
	 * @param rater
	 */
    public void setRater(Rater rater) {
        this.rater = rater;
    }
}
