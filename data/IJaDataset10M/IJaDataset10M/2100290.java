package edu.ucla.stat.SOCR.analyses.data;

import java.util.*;
import edu.ucla.stat.SOCR.util.*;

public class SurvivalList extends DataCase {

    protected SurvivalObject[] objectArray;

    protected String groupName;

    protected int count = 0;

    protected ArrayList<SurvivalObject> objectList = new ArrayList<SurvivalObject>();

    private TreeSet<Double> sortedTimeSet = new TreeSet<Double>();

    private TreeMap<Double, SurvivalSingleList> timeSetLabelMap = new TreeMap<Double, SurvivalSingleList>();

    private int numberNewList = 0;

    private int totalNumberList = 0;

    private double maxTime = 0;

    private double minTime = Double.POSITIVE_INFINITY;

    private int numberGroups;

    private double[] survivalTimeArray = null;

    private int[] survivalAtRiskArray = null;

    private double[] survivalRateArray = null;

    private double[] upperCIArray = null;

    private double[] lowerCIArray = null;

    private double[] survivalSEArray = null;

    private ArrayList<Double> survivalTimeList = null;

    private ArrayList<Double> survivalRateList = null;

    private ArrayList<Integer> survivalAtRiskList = null;

    private ArrayList<Double> upperCIList = null;

    private ArrayList<Double> lowerCIList = null;

    private ArrayList<Double> survivalSEList = null;

    private String timeList = "";

    public SurvivalList(String groupName) {
        this.groupName = groupName;
        survivalTimeList = new ArrayList<Double>();
        survivalRateList = new ArrayList<Double>();
        upperCIList = new ArrayList<Double>();
        lowerCIList = new ArrayList<Double>();
        survivalAtRiskList = new ArrayList<Integer>();
        survivalSEList = new ArrayList<Double>();
    }

    public void add(SurvivalObject object) {
        objectList.add(count, object);
        sortedTimeSet.add(new Double(object.getTime()));
        addNumberAtRisk(object);
        count++;
    }

    public void addNumberAtRisk(SurvivalObject object) {
        SurvivalSingleList tempList = (SurvivalSingleList) timeSetLabelMap.get(new Double(object.getTime()));
        int temp = 0;
        if (tempList != null) {
            tempList.add(object);
        } else {
            tempList = new SurvivalSingleList(groupName + " " + object.getTime());
            tempList.add(object);
            timeSetLabelMap.put(new Double(object.getTime()), tempList);
            numberNewList++;
        }
        totalNumberList++;
    }

    public void rearrangeNumberAtRisk() {
        if (!sortedTimeSet.isEmpty()) {
            Iterator<Double> iterator = sortedTimeSet.iterator();
            double timeLabel = 0;
            int timeCount = 0;
            Double key = null;
            SurvivalSingleList currentSingleList = null;
            SurvivalSingleList prevSingleList = null;
            while (iterator.hasNext()) {
                key = (Double) iterator.next();
                timeLabel = key.doubleValue();
                if (timeSetLabelMap.containsKey(key)) {
                    currentSingleList = (SurvivalSingleList) timeSetLabelMap.get(key);
                    currentSingleList.examList();
                    if (timeLabel == getMinTime()) {
                        prevSingleList = currentSingleList;
                    } else {
                        if (currentSingleList.allAreCensored()) {
                            for (int j = 0; j < currentSingleList.getSize(); j++) {
                                prevSingleList.add((SurvivalObject) currentSingleList.getObjectList().get(j));
                                currentSingleList.setSize(currentSingleList.getSize() + 1);
                            }
                            timeSetLabelMap.remove(key);
                        } else {
                            prevSingleList = currentSingleList;
                        }
                    }
                }
            }
        }
    }

    public double getMaxTimeAll() {
        if (maxTime == 0) {
            int size = objectList.size();
            objectArray = new SurvivalObject[size];
            for (int i = 0; i < size; i++) {
                objectArray[i] = (SurvivalObject) objectList.get(i);
                if (maxTime < objectArray[i].getTime()) {
                    maxTime = objectArray[i].getTime();
                }
            }
            return maxTime;
        } else {
            return maxTime;
        }
    }

    private double getMaxTime() {
        if (maxTime == 0) {
            int size = objectList.size();
            objectArray = new SurvivalObject[size];
            for (int i = 0; i < size; i++) {
                objectArray[i] = (SurvivalObject) objectList.get(i);
                if (maxTime < objectArray[i].getTime() && objectArray[i].getCensor() != SurvivalObject.CENSORED_CONSTANT) {
                    maxTime = objectArray[i].getTime();
                }
            }
            return maxTime;
        } else {
            return maxTime;
        }
    }

    private double getMinTime() {
        if (minTime == Double.POSITIVE_INFINITY) {
            int size = objectList.size();
            objectArray = new SurvivalObject[size];
            for (int i = 0; i < size; i++) {
                objectArray[i] = (SurvivalObject) objectList.get(i);
                if (minTime > objectArray[i].getTime()) {
                    minTime = objectArray[i].getTime();
                }
            }
            return minTime;
        } else {
            return minTime;
        }
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    protected void listToSurvivalObjectArray() {
        int size = objectList.size();
        objectArray = new SurvivalObject[size];
        for (int i = 0; i < size; i++) {
            objectArray[i] = (SurvivalObject) objectList.get(i);
        }
    }

    protected double[] listToDoubleArray(ArrayList<Double> list) {
        int size = list.size();
        double[] result = new double[size];
        for (int i = 0; i < size; i++) {
            result[i] = ((Double) list.get(i)).doubleValue();
        }
        return result;
    }

    protected int[] listToIntArray(ArrayList<Integer> list) {
        int size = list.size();
        int[] result = new int[size];
        for (int i = 0; i < size; i++) {
            result[i] = ((Integer) list.get(i)).intValue();
        }
        return result;
    }

    public double[] getSurvivalTimeArray() {
        survivalTimeArray = listToDoubleArray(survivalTimeList);
        for (int i = 0; i < survivalTimeArray.length; i++) {
        }
        return survivalTimeArray;
    }

    public int[] getSurvivalAtRiskArray() {
        survivalAtRiskArray = listToIntArray(survivalAtRiskList);
        for (int i = 0; i < survivalAtRiskArray.length; i++) {
        }
        return survivalAtRiskArray;
    }

    public double[] getSurvivalSEArray() {
        survivalSEArray = listToDoubleArray(survivalSEList);
        for (int i = 0; i < survivalSEArray.length; i++) {
        }
        return survivalSEArray;
    }

    public double[] getSurvivalRateArray() {
        survivalRateArray = listToDoubleArray(survivalRateList);
        for (int j = 0; j < survivalRateArray.length; j++) {
        }
        return survivalRateArray;
    }

    public double[] getUpperCIArray() {
        upperCIArray = listToDoubleArray(upperCIList);
        for (int j = 0; j < upperCIArray.length; j++) {
        }
        return upperCIArray;
    }

    public double[] getLowerCIArray() {
        lowerCIArray = listToDoubleArray(lowerCIList);
        for (int j = 0; j < lowerCIArray.length; j++) {
        }
        return lowerCIArray;
    }

    public TreeSet<Double> getSortedTimeSet() {
        return sortedTimeSet;
    }

    public void printSortedTimeSet() {
        if (!sortedTimeSet.isEmpty()) {
            Iterator<Double> iterator = sortedTimeSet.iterator();
            double timeLabel = 0;
            int timeCount = 0;
            Double key = null;
            SurvivalSingleList currentSingle = null;
            while (iterator.hasNext()) {
                key = (Double) iterator.next();
                timeLabel = key.doubleValue();
                if (timeSetLabelMap.containsKey(key)) {
                    currentSingle = (SurvivalSingleList) timeSetLabelMap.get(key);
                    currentSingle.printList();
                }
            }
        }
    }

    public void listSurvivalRate() {
        if (!sortedTimeSet.isEmpty()) {
            Iterator<Double> iterator = sortedTimeSet.iterator();
            double timeLabel = 0;
            int timeCount = 0;
            Double key = null;
            SurvivalSingleList currentSingle = null;
            double currentTotal = count;
            double currentAtRisk = count;
            double currentSurvivalRate = 0;
            double cumulativeRate = 1;
            int currentNumberEvent = 0;
            double currentVarSurvival = 0;
            double currentSurvivalSE = 0;
            double currentHazard = 0;
            double currentVarHazard = 0;
            double currentSEHazard = 0;
            double hazardCIUpper = 0, hazardCILower = 0;
            int i = 0;
            while (iterator.hasNext()) {
                key = (Double) iterator.next();
                timeLabel = key.doubleValue();
                if (timeSetLabelMap.containsKey(key)) {
                    currentSingle = (SurvivalSingleList) timeSetLabelMap.get(key);
                    currentSurvivalRate = 1 - (currentSingle.getNumberDead() / currentTotal);
                    currentSurvivalRate = cumulativeRate * currentSurvivalRate;
                    cumulativeRate = currentSurvivalRate;
                    currentNumberEvent = currentSingle.getNumberDead();
                    currentVarHazard += currentNumberEvent / currentAtRisk / (currentAtRisk - currentNumberEvent);
                    currentVarSurvival = currentVarHazard * (currentSurvivalRate * currentSurvivalRate);
                    currentSurvivalSE = Math.sqrt(currentVarSurvival);
                    currentHazard += currentNumberEvent / currentAtRisk;
                    currentSEHazard = Math.sqrt(currentVarHazard);
                    double k0 = 1.645;
                    hazardCIUpper = currentSurvivalRate * Math.exp(k0 * currentSEHazard);
                    hazardCILower = currentSurvivalRate * Math.exp(-k0 * currentSEHazard);
                    if (hazardCIUpper > 1) {
                        hazardCIUpper = 1;
                    }
                    if (hazardCILower > 1) {
                        hazardCILower = 1;
                    }
                    survivalTimeList.add(i, key);
                    survivalAtRiskList.add(i, new Integer((int) currentAtRisk));
                    survivalRateList.add(i, new Double(currentSurvivalRate));
                    survivalSEList.add(i, new Double(currentSurvivalSE));
                    upperCIList.add(i, new Double(hazardCIUpper));
                    lowerCIList.add(i, new Double(hazardCILower));
                    currentAtRisk = currentTotal - currentSingle.getSize();
                    currentTotal = currentAtRisk;
                    i++;
                }
            }
        }
    }

    public SurvivalObject[] getObjectArray() {
        listToSurvivalObjectArray();
        return objectArray;
    }

    public void printList() {
        SurvivalObject currentObject = null;
        listToSurvivalObjectArray();
        for (int i = 0; i < objectList.size(); i++) {
            currentObject = objectArray[i];
        }
    }

    public void constructSurvivalResult() {
        double[] time = { 1, 10, 22, 7, 3, 32, 12, 23, 8, 22, 17, 6, 2, 16, 11, 34, 8, 32, 12, 25, 2, 11, 5, 20, 4, 19, 15, 6, 8, 17, 23, 35, 5, 6, 11, 13, 4, 9, 1, 6, 8, 10 };
        byte[] censor = { 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0 };
        String[] treat = { "control", "6-MP", "control", "6-MP", "control", "6-MP", "control", "6-MP", "control", "6-MP", "control", "6-MP", "control", "6-MP", "control", "6-MP", "control", "6-MP", "control", "6-MP", "control", "6-MP", "control", "6-MP", "control", "6-MP", "control", "6-MP", "control", "6-MP", "control", "6-MP", "control", "6-MP", "control", "6-MP", "control", "6-MP", "control", "6-MP", "control", "6-MP" };
        int numberGroups = 2;
        String[] groupNames = new String[] { "6-MP", "control" };
        ArrayList[] indexOfGroup = new ArrayList[numberGroups];
        SurvivalList[] survList = new SurvivalList[] { new SurvivalList("6-MP"), new SurvivalList("control") };
        for (int i = 0; i < time.length; i++) {
            if (censor[i] == SurvivalObject.CENSORED_CONSTANT) {
            }
        }
        for (int i = 0; i < time.length; i++) {
            for (int j = 0; j < numberGroups; j++) {
                if (treat[i].equals(groupNames[j])) {
                    survList[j].add(new SurvivalObject(time[i], censor[i], i));
                }
            }
        }
        for (int j = 0; j < numberGroups; j++) {
            survList[j].printList();
        }
        QSortAlgorithm quick = new QSortAlgorithm();
        try {
            for (int j = 0; j < numberGroups; j++) {
                quick.sort(survList[j].getObjectArray());
                survList[j].getSortedTimeSet();
                survList[j].rearrangeNumberAtRisk();
                survList[j].printSortedTimeSet();
                survList[j].listSurvivalRate();
            }
        } catch (Exception e) {
        }
    }
}
