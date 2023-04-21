package org.personalsmartspace.lm.bayesian.impl.bayesianLibrary.bayesianLearner.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;
import org.personalsmartspace.lm.bayesian.impl.bayesianLibrary.bayesianLearner.exceptions.NodeValueTextNotInNodeRangeException;
import org.personalsmartspace.lm.bayesian.impl.bayesianLibrary.bayesianLearner.interfaces.BayesLearner;
import org.personalsmartspace.lm.bayesian.impl.bayesianLibrary.bayesianLearner.interfaces.InstantiatedRV;
import org.personalsmartspace.lm.bayesian.impl.bayesianLibrary.bayesianLearner.interfaces.JointMeasurement;
import org.personalsmartspace.lm.bayesian.impl.bayesianLibrary.bayesianLearner.interfaces.RandomVariable;
import org.personalsmartspace.lm.bayesian.impl.bayesianLibrary.bayesianLearner.interfaces.TimeStamp;

public class SimpleJointMeasurement implements JointMeasurement {

    private static final int MaxNumberOfRanges = 100;

    private TimeStamp timeStamp;

    Map instantiatedRVs;

    private String strRep;

    public SimpleJointMeasurement() {
        this.instantiatedRVs = new HashMap();
        this.strRep = "SimpleJointMeasurement: ";
    }

    public TimeStamp getTimeStamp() {
        return this.timeStamp;
    }

    public Map getInstantiatedRV() {
        return this.instantiatedRVs;
    }

    public void add(InstantiatedRV irv) {
        this.instantiatedRVs.put(irv.getRV(), irv);
        this.strRep = this.strRep + " " + irv.toString();
    }

    public void addAll(Set irvs) {
        InstantiatedRV[] irvs_array = (InstantiatedRV[]) irvs.toArray(new InstantiatedRV[0]);
        for (int i = 0; i < irvs_array.length; i++) {
            this.add(irvs_array[i]);
        }
    }

    public String toString() {
        return this.strRep;
    }

    /** @param filePath the name of the file to open. Not sure if it can accept URLs or just filenames. Path handling could be better, and buffer sizes are hardcoded
	    */
    private static String readFileAsString(String filePath) throws java.io.IOException {
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();
        return fileData.toString();
    }

    /** 
		    * @see readFileAsString
		    * First line omitted if counter>0;
		    */
    private static String readFilesAsString(String filePath, int counter) throws java.io.IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String result = "";
        String temp = reader.readLine() + "\n";
        if (counter == 0) result += temp;
        while ((temp = reader.readLine()) != null) {
            result += temp + "\n";
        }
        reader.close();
        return result;
    }

    public static SimpleJointMeasurement[] computeFromDataFile(Map rvmap, String filename) {
        try {
            return SimpleJointMeasurement.computeFromData(rvmap, readFileAsString(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static SimpleJointMeasurement[] computeFromDataFiles(Map rvmap, File[] files) {
        String complete = "";
        int counter = 0;
        for (File f : files) {
            try {
                complete += readFilesAsString(f.getAbsolutePath(), counter);
            } catch (IOException e) {
                e.printStackTrace();
            }
            counter++;
        }
        return SimpleJointMeasurement.computeFromData(rvmap, complete);
    }

    public static SimpleJointMeasurement[] computeFromData(Map rvmap, String data) {
        if (BayesLearner.DebugLevel > 0) System.out.println("data:" + data);
        if (BayesLearner.DebugLevel > 0) System.out.println("\n data end");
        String[] entries = data.split("\\f|\\r|\\n", 0);
        if (BayesLearner.DebugLevel > 0) System.out.println(entries.length);
        RandomVariable[] rvs = null;
        rvmap.clear();
        int counter = 0;
        String[] rvnames = null;
        SortedSet[] rvranges = null;
        for (int i = 0; i < entries.length; i++) {
            if (entries[i].length() > 1) {
                String[] values = entries[i].split("\\t", 0);
                if (counter == 0) {
                    rvnames = new String[values.length];
                    if (BayesLearner.DebugLevel > 0) System.out.println("Counter: " + counter + " e: " + entries[i]);
                    rvranges = new TreeSet[values.length];
                    rvs = new RandomVariable[values.length];
                    for (int j = 0; j < values.length; j++) {
                        rvnames[j] = values[j].trim();
                        rvranges[j] = new TreeSet();
                        if (BayesLearner.DebugLevel > 0) System.out.println("getting name: " + rvnames[j]);
                    }
                } else {
                    if (BayesLearner.DebugLevel > 0) System.out.println("Counter: " + counter + " v: " + entries[i]);
                    for (int j = 0; j < values.length; j++) {
                        if (values[j].trim().length() > 0) rvranges[j].add(values[j].trim());
                    }
                }
                counter++;
            }
        }
        SimpleJointMeasurement[] ret = new SimpleJointMeasurement[counter - 1];
        for (int r = 0; r < rvnames.length; r++) {
            SimpleRandomVariable rv = new SimpleRandomVariable(rvnames[r], 1, new Vector(rvranges[r]));
            rvs[r] = rv;
            rvmap.put(rvnames[r], rv);
        }
        counter = 0;
        for (int i = 0; i < entries.length; i++) {
            if (entries[i].length() > 1) {
                String[] values = entries[i].split("\\t", 0);
                if (counter != 0) {
                    ret[counter - 1] = new SimpleJointMeasurement();
                    for (int j = 0; j < values.length; j++) {
                        SimpleInstantiatedRV sirv = null;
                        try {
                            boolean missing = (values[j].trim().length() <= 0);
                            sirv = new SimpleInstantiatedRV(rvs[j], missing, values[j].trim());
                        } catch (NodeValueTextNotInNodeRangeException e) {
                            e.printStackTrace();
                        }
                        ret[counter - 1].add(sirv);
                    }
                }
                counter++;
            }
        }
        if (BayesLearner.DebugLevel > 0) for (int r = 0; r < ret.length; r++) System.out.println("Result: \n" + ret[r]);
        return ret;
    }
}
