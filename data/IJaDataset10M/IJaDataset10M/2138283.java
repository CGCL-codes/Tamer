package mdr.heterogeneity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import algorithm.CombinationGenerator;
import mdr.data.DataFile;
import algorithm.Subdivision;
import mdr.BestKFoldCVResult;
import mdr.SavedModels;
import mdr.Combination;

/**
 *
 * @author Guo-Bo Chen
 */
public abstract class AbstractHeteroMergeSearch {

    protected int count;

    protected boolean isMooreMDR;

    protected int order;

    protected int numTraits;

    protected double[] offset;

    protected double[][] currBestStatistic;

    protected int[] SNPIndex;

    protected HashSet<Integer> testedInteraction = new HashSet();

    protected HashMap dataPartitionMap;

    protected HashMap<String, HeteroCombination> ModelHubs;

    protected HashMap<Integer, BestKFoldCVResult> bestKFoldResultMap = new HashMap();

    protected HashMap<Integer, SavedModels> bestSavedModelsMap = new HashMap();

    protected ArrayList<Combination> cvTestingSet = new ArrayList();

    protected Combination model;

    protected CombinationGenerator comGenerator;

    protected Subdivision subdivision;

    protected SavedModels savedModels;

    protected BestKFoldCVResult bestKFoldResult;

    public AbstractHeteroMergeSearch(Subdivision sd, CombinationGenerator cg, int traits, double[] os, boolean ismooremdr) {
        subdivision = sd;
        comGenerator = cg;
        numTraits = traits;
        offset = new double[os.length];
        System.arraycopy(os, 0, offset, 0, os.length);
        isMooreMDR = ismooremdr;
        dataPartitionMap = subdivision.getDivision();
        currBestStatistic = new double[numTraits][];
        ModelHubs = new HashMap();
        for (int i = 0; i < numTraits; i++) {
            currBestStatistic[i] = new double[subdivision.getInterval()];
        }
        for (int i = 0; i < subdivision.getInterval(); i++) {
            Combination testingMap = new Combination();
            cvTestingSet.add(testingMap);
        }
    }

    public int size() {
        return testedInteraction.size();
    }

    public int getOrder() {
        return order;
    }

    public boolean isMooreMDR() {
        return isMooreMDR;
    }

    public abstract void search(int or, int pheIdx);

    public abstract void print();

    public abstract void print(int or);
}
