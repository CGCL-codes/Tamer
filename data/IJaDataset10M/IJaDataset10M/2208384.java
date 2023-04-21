package org.ximtec.igesture.batch.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;
import org.sigtec.util.Constant;
import org.ximtec.igesture.batch.BatchTools;

/**
 * Implements the power set parameter of the batch process.
 * 
 * @version 1.0 Dec 2006
 * @author Ueli Kurmann, igesture@uelikurmann.ch
 * @author Beat Signer, signer@inf.ethz.ch
 */
public class BatchPowerSetValue {

    private List<String> values;

    private int min;

    private int max;

    public BatchPowerSetValue() {
        values = new ArrayList<String>();
    }

    public void addValue(String value) {
        values.add(value);
    }

    public List<String> getValues() {
        return values;
    }

    /**
    * Creates the power set for the given list.
    * 
    * @param list the comma separated list.
    * @param min the minimal length of the lists.
    * @param max the maximal length of the lists.
    * @return the list containing the power set of lists with the given
    *         constraint.
    */
    public static List<String> createPowerSet(String list, int min, int max) {
        final List<String> result = new ArrayList<String>();
        final StringTokenizer tokenizer = new StringTokenizer(list.trim(), Constant.COMMA);
        final HashSet<String> set = new HashSet<String>();
        while (tokenizer.hasMoreTokens()) {
            final String token = tokenizer.nextToken();
            set.add(token);
        }
        HashSet<HashSet<String>> powerSet = BatchTools.getPowerSet(set);
        powerSet = BatchTools.filterSet(powerSet, min, max);
        for (final HashSet<String> hashSet : powerSet) {
            final StringBuilder stringBuilder = new StringBuilder();
            for (final String s : hashSet) {
                stringBuilder.append(s + Constant.COMMA);
            }
            if (stringBuilder.length() > 0) {
                result.add(stringBuilder.toString().substring(0, stringBuilder.length() - 1));
            } else {
                result.add(stringBuilder.toString());
            }
        }
        return result;
    }

    /**
    * Sets the maximal length of the list,
    * 
    * @param max the maximal length of the list. The value should be not larger
    *            than the length of the original list but it should be larger
    *            than getMin().
    */
    public void setMax(int max) {
        this.max = max;
    }

    /**
    * Returns the maximal length of the list.
    * 
    * @return the maximal length of the list.
    */
    public int getMax() {
        return max;
    }

    /**
    * Sets the minimal length of the list.
    * 
    * @param min the minimal length of the list. The value should be larger than
    *            0 and smaller than getMax().
    */
    public void setMin(int min) {
        this.min = min;
    }

    /**
    * Returns the minimal length of the list.
    * 
    * @return the minimal length of the power set. The length should be larger
    *         than 0 and smaller than getMax().
    */
    public int getMin() {
        return min;
    }
}
