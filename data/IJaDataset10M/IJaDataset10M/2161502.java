package org.drools.benchmark.waltz;

import java.io.Serializable;

/**
 * 
 * @author Alexander Bagerman
 *
 */
public class Stage implements Serializable {

    private static final long serialVersionUID = -2143733895059840082L;

    public static final int START = 0;

    public static final int DUPLICATE = 1;

    public static final int DETECT_JUNCTIONS = 2;

    public static final int FIND_INITIAL_BOUNDARY = 3;

    public static final int FIND_SECOND_BOUNDARY = 4;

    public static final int LABELING = 5;

    public static final int PLOT_REMAINING_EDGES = 9;

    public static final int DONE = 10;

    private int value;

    public Stage() {
    }

    public Stage(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public static int resolveStageValue(String str) {
        if (str.equals("start")) {
            return 0;
        } else if (str.equals("duplicate")) {
            return 1;
        } else if (str.equals("detect_junctions")) {
            return 2;
        } else if (str.equals("find_initial_boundary")) {
            return 3;
        } else if (str.equals("find_second_boundary")) {
            return 4;
        } else if (str.equals("labeling")) {
            return 5;
        } else if (str.equals("plot_remaining_edges")) {
            return 9;
        } else if (str.equals("done")) {
            return 10;
        } else return -9999999;
    }

    public String toString() {
        return "{Stage value=" + this.value + "}";
    }
}
