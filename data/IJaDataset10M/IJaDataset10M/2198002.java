package tests;

import java.util.ArrayList;
import genetic.CalculatorFactory;
import genetic.CalculatorSettings;
import genetic.DistanceCalculator;
import genetic.DistanceCalculatorsBagSettings;
import genetic.FullSubtreeCalculatorSettings;
import genetic.ProblemInstance;
import genetic.ProblemStats;
import genetic.Run;
import genetic.StructuralDistanceSettings;
import junit.framework.TestCase;

/**
 * @author   Fabrizio Pastore [ fabrizio.pastore@gmail.com ]
 */
public class ProblemStatsTest extends TestCase {

    ProblemInstance pi;

    static int numTypes = 2;

    public static void main(String[] args) {
        junit.swingui.TestRunner.run(ProblemStatsTest.class);
    }

    public ProblemStatsTest(String arg0) {
        super(arg0);
    }

    protected void setUp() throws Exception {
        super.setUp();
        pi = TestGeneratorTree.getProblemInstance();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        pi = null;
    }

    public void testGetAllValues() {
        double val;
        int max = pi.getGeneticSettings().getMaxGen();
        ArrayList<CalculatorSettings> cs = new ArrayList<CalculatorSettings>();
        cs.add(new FullSubtreeCalculatorSettings());
        cs.add(new StructuralDistanceSettings());
        DistanceCalculatorsBagSettings bag = new DistanceCalculatorsBagSettings();
        pi.getGeneticSettings().setCalculatorBagSettings(bag);
        int nruns = 100;
        for (int j = 0; j < nruns; j++) {
            double avg = 0.0;
            Run run = new Run();
            for (int i = 1; i <= max + 1; i++) {
                val = (1.0 - 1.0 / (double) i);
                avg += val;
                run.addMedF(val);
                run.addBestFit(val);
                run.setOptimusGen(3);
                int t = 0;
                for (DistanceCalculator.type type : pi.getGeneticSettings().getCalculatorBagSettings().getCalculatorsTypes()) {
                    t++;
                    run.addDist(type, val / (double) t);
                    run.addFDC(type, val / (double) t);
                }
            }
            pi.addRun(run);
        }
        ProblemStats ps = new ProblemStats(pi);
        ArrayList<Double> valuesTypes[] = ps.getAllValues();
        int t = 0;
        for (ArrayList<Double> values : valuesTypes) {
            t++;
            System.out.println(t);
            int i = 0;
            for (Double value : values) {
                i++;
                val = (1.0 - 1.0 / (double) i);
                if (t == 3) val = 3.0;
                if (t > 3) val = val / (double) t;
                System.out.println("Run " + i + " " + val + "value :" + value);
                assertTrue((value - val) < 1.0 / 16.0);
            }
        }
    }
}
