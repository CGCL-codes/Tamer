package edu.colorado.emml.ensemble_deprecated;

import weka.core.Instances;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import edu.colorado.emml.metrics.Accuracy;
import edu.colorado.emml.metrics.IMetric;

/**
 * Created by: Sam
 * Jan 2, 2008 at 9:45:08 PM
 */
public class SortByAccuracy {

    public static int[] sortDecreasing(ClassifierList ensemble, Instances validationSet, IMetric metric) {
        final HashMap<Integer, Double> map = new HashMap<Integer, Double>();
        for (int i = 0; i < ensemble.getPredictionSetCount(); i++) {
            map.put(i, metric.evaluate(ensemble.getPredictionSet(i), validationSet));
        }
        ArrayList<Integer> keys = new ArrayList<Integer>(map.keySet());
        Collections.sort(keys, new Comparator<Integer>() {

            public int compare(Integer o1, Integer o2) {
                return -Double.compare(map.get(o1), map.get(o2));
            }
        });
        int[] val = new int[keys.size()];
        for (int i = 0; i < val.length; i++) {
            val[i] = keys.get(i);
        }
        return val;
    }

    public static void sortByAccuracy(File file) throws ClassNotFoundException, IOException {
        final EnsembleData librarySet = new EnsembleData(file);
        final Accuracy metric = new Accuracy();
        System.out.println("index\t" + "train\t" + "test\t" + "name\t");
        int[] sorted = sortDecreasing(librarySet.getValLibrary(), librarySet.getValInstances(), metric);
        for (int index : sorted) {
            System.out.println(index + "\t" + metric.evaluate(librarySet.getTrainLibrary().getPredictionSet(index), librarySet.getTrainInstances()) + "\t" + metric.evaluate(librarySet.getTestLibrary().getPredictionSet(index), librarySet.getTestInstances()) + "\t" + toSingleLine(librarySet.getTrainLibrary().getPredictionSet(index).getDescription()));
        }
    }

    public static String toSingleLine(String description) {
        return description.replace('\n', '\t').replace('\r', '\t');
    }

    public static void main(String[] args) throws Exception {
        sortByAccuracy(new File("C:\\workingcopy\\emml\\svn\\trunk\\dist\\model-library_0"));
    }
}
