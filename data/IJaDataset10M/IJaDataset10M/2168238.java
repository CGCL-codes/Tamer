package com.oat.experimenter.examples;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;
import com.oat.experimenter.stats.AnalysisException;
import com.oat.experimenter.stats.analysis.twopopulation.MannWhitneyUTest;

/**
 * Description: Example of pair-wise non-parametric comparisons between samples
 *  
 * Date: 27/08/2007<br/>
 * @author Jason Brownlee 
 *
 * <br/>
 * <pre>
 * Change History
 * ----------------------------------------------------------------------------
 * 
 * </pre>
 */
public class ExamplePairwiseNonParametric {

    public static void main(String[] args) {
        double[][][] results = new double[3][][];
        Random r = new Random(1);
        for (int file = 0; file < results.length; file++) {
            double fileOffset = (file + 1) * 10;
            results[file] = new double[30][];
            for (int line = 0; line < results[file].length; line++) {
                results[file][line] = new double[4];
                for (int variable = 0; variable < results[file][line].length; variable++) {
                    results[file][line][variable] = (r.nextDouble() * fileOffset);
                }
            }
        }
        int selectedProble = 0;
        ExamplePairwiseNonParametric example = new ExamplePairwiseNonParametric();
        example.pairwiseMatrixComparisonParametric(results, selectedProble);
    }

    public void pairwiseMatrixComparisonParametric(double[][][] results, int selectedProble) {
        double[][] samples = new double[results.length][];
        for (int file = 0; file < samples.length; file++) {
            samples[file] = new double[results[file].length];
            for (int line = 0; line < samples[file].length; line++) {
                samples[file][line] = results[file][line][selectedProble];
            }
        }
        MannWhitneyUTest[][] testsResilts = pairwiseMatrixComparisonParametric(samples);
        System.out.println("is-different matrix");
        for (int x = 0; x < testsResilts.length; x++) {
            for (int y = 0; y < testsResilts[x].length; y++) {
                System.out.print(testsResilts[x][y].isPopulationsDifferent() + ", ");
            }
            System.out.println();
        }
        System.out.println();
        System.out.println("p-value matrix");
        NumberFormat f = DecimalFormat.getInstance();
        for (int x = 0; x < testsResilts.length; x++) {
            for (int y = 0; y < testsResilts[x].length; y++) {
                System.out.print(f.format(testsResilts[x][y].getPValue()) + ", ");
            }
            System.out.println();
        }
    }

    public MannWhitneyUTest[][] pairwiseMatrixComparisonParametric(double[][] results) {
        MannWhitneyUTest[][] tests = new MannWhitneyUTest[results.length][results.length];
        for (int x = 0; x < tests.length; x++) {
            for (int y = 0; y < tests[x].length; y++) {
                tests[x][y] = new MannWhitneyUTest();
                try {
                    tests[x][y].evaluate(results[x], results[y]);
                } catch (AnalysisException e) {
                    throw new RuntimeException("Something unexpected happened in the example: " + e.getMessage(), e);
                }
            }
        }
        return tests;
    }
}
