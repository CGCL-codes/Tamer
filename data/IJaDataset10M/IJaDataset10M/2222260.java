package org.sf.xrime.algorithms.MST;

import org.apache.hadoop.util.ToolRunner;

/**
 * This class is used to run the MST algorithm
 * @author YangYIn
 */
public class MSTRunner {

    /**
   * Main method 
   * @param args
   */
    public static void main(String[] args) {
        runMST(args);
    }

    public static void runMST(String[] args) {
        try {
            int res = ToolRunner.run(new MSTAlgorithm(), args);
            System.exit(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
