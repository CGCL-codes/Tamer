package org.opensourcedea.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.opensourcedea.dea.*;
import java.util.ArrayList;

public class LibraryTestNCOGRSTest {

    DEAProblem tester = new DEAProblem(20, 4);

    public void buildDEAProblem(ModelType ModelType) {
        tester.setModelType(ModelType);
        tester.setVariableNames(TestData.createTestVariableNames());
        tester.setVariableOrientations(TestData.createTestVariableOrientation());
        tester.setVariableTypes(TestData.createTestVariableTypes(VariableType.NON_CONTROLLABLE));
        tester.setDataMatrix(TestData.createTestDataMatrix());
        tester.setDMUNames(TestData.createTestDMUNames());
    }

    private double[] createDEAModelObjectives() {
        double[] Objectives = new double[20];
        Objectives[0] = 0.5410900168092301;
        Objectives[1] = 0.7923450622702364;
        Objectives[2] = 0.5799603531126015;
        Objectives[3] = 0.08872161496639956;
        Objectives[4] = 0.6130079082518556;
        Objectives[5] = 1;
        Objectives[6] = 1;
        Objectives[7] = 0.8278323638283929;
        Objectives[8] = 0.6711283626435885;
        Objectives[9] = 1;
        Objectives[10] = 0.004257039777425628;
        Objectives[11] = 1;
        Objectives[12] = 0.18013028878550508;
        Objectives[13] = 0.01943492591965882;
        Objectives[14] = 0.9517801244767826;
        Objectives[15] = 0.3613071403803979;
        Objectives[16] = 1;
        Objectives[17] = 1;
        Objectives[18] = 1;
        Objectives[19] = 1;
        return Objectives;
    }

    private int[] createSolRanks() {
        int[] ranks = new int[20];
        ranks[0] = 15;
        ranks[1] = 11;
        ranks[2] = 14;
        ranks[3] = 18;
        ranks[4] = 13;
        ranks[5] = 1;
        ranks[6] = 1;
        ranks[7] = 10;
        ranks[8] = 12;
        ranks[9] = 1;
        ranks[10] = 20;
        ranks[11] = 1;
        ranks[12] = 17;
        ranks[13] = 19;
        ranks[14] = 9;
        ranks[15] = 16;
        ranks[16] = 1;
        ranks[17] = 1;
        ranks[18] = 1;
        ranks[19] = 1;
        return ranks;
    }

    private ArrayList<NonZeroLambda>[] getTestReferenceSet() {
        @SuppressWarnings("unchecked") ArrayList<NonZeroLambda>[] referenceSets = new ArrayList[20];
        ArrayList<NonZeroLambda> refSet = new ArrayList<NonZeroLambda>();
        refSet.add(new NonZeroLambda(2, 0.007189579752136141));
        refSet.add(new NonZeroLambda(6, 0.3912021791716576));
        refSet.add(new NonZeroLambda(8, 0.35360483668180226));
        refSet.add(new NonZeroLambda(17, 0.4480034043944039));
        referenceSets[0] = refSet;
        refSet = new ArrayList<NonZeroLambda>();
        refSet.add(new NonZeroLambda(5, 0.9258403799000708));
        refSet.add(new NonZeroLambda(17, 0.1738665586340592));
        refSet.add(new NonZeroLambda(18, 0.10029306146586985));
        referenceSets[1] = refSet;
        refSet = new ArrayList<NonZeroLambda>();
        refSet.add(new NonZeroLambda(6, 1));
        referenceSets[6] = refSet;
        refSet = new ArrayList<NonZeroLambda>();
        refSet.add(new NonZeroLambda(5, 0.20892795337036443));
        refSet.add(new NonZeroLambda(16, 0.4575133124776671));
        refSet.add(new NonZeroLambda(17, 0.06029815533813504));
        refSet.add(new NonZeroLambda(18, 0.4732605788138335));
        referenceSets[15] = refSet;
        return referenceSets;
    }

    private double[][] getTestSlackValues() {
        double[][] slackValues = new double[20][4];
        slackValues[1][0] = 443.24386013394735;
        slackValues[2][0] = 114.42135774083252;
        slackValues[12][0] = 435.45313301785075;
        return slackValues;
    }

    private double[][] getTestProjectionValues() {
        double[][] projectionValues = new double[20][4];
        projectionValues[0][0] = 411.17;
        projectionValues[0][1] = 83;
        projectionValues[0][2] = 1877.18;
        projectionValues[0][3] = 2486.2221778419844;
        projectionValues[3][0] = 361.0;
        projectionValues[3][1] = 83;
        projectionValues[3][2] = 1250.71;
        projectionValues[3][3] = 1712.5477264761507;
        projectionValues[10][0] = 384.149;
        projectionValues[10][1] = 65;
        projectionValues[10][2] = 1409.55;
        projectionValues[10][3] = 2645.030488018905;
        return projectionValues;
    }

    private double[][] getTestWeightValues() {
        double[][] weightValues = new double[20][6];
        weightValues[0][2] = 2.56107331034458E-4;
        weightValues[0][3] = 0;
        weightValues[0][4] = 0;
        weightValues[0][5] = 7.433452020778272E-4;
        weightValues[3][2] = 0.004643324461596525;
        weightValues[3][3] = 0;
        weightValues[3][4] = 0;
        weightValues[3][5] = 0.00658154534684744;
        weightValues[10][2] = 0.11088657634610363;
        weightValues[10][3] = 0;
        weightValues[10][4] = 0;
        weightValues[10][5] = 0.08880994671403199;
        weightValues[14][2] = 5.093244392838733E-4;
        weightValues[14][3] = 0;
        weightValues[14][4] = 0;
        weightValues[14][5] = 5.211264669710045E-4;
        return weightValues;
    }

    @Test
    public void testNCOGRS() {
        buildDEAProblem(ModelType.NC_O_GRS);
        try {
            tester.setRTSLowerBound(0.8);
            tester.setRTSUpperBound(1.2);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        try {
            tester.solve();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        try {
            assertArrayEquals(tester.getObjectives(), createDEAModelObjectives(), 0.0001);
            assertArrayEquals(tester.getRanks(true, RankingType.STANDARD, 8), createSolRanks());
            ArrayList<Integer> l = new ArrayList<Integer>();
            l.add(0);
            l.add(1);
            l.add(6);
            l.add(15);
            for (Integer i : l) {
                ArrayList<NonZeroLambda> refSet = getTestReferenceSet()[i];
                for (int nzlIndex = 0; nzlIndex < refSet.size(); nzlIndex++) {
                    assertEquals(refSet.get(nzlIndex).getDMUIndex(), tester.getReferenceSet(i).get(nzlIndex).getDMUIndex());
                    assertEquals(refSet.get(nzlIndex).getLambdaValue(), tester.getReferenceSet(i).get(nzlIndex).getLambdaValue(), 0.0001);
                }
            }
            l.clear();
            l.add(3);
            l.add(10);
            l.add(13);
            for (Integer i : l) {
                double[] slackValues = getTestSlackValues()[i];
                for (int sIndex = 0; sIndex < slackValues.length; sIndex++) {
                    assertEquals(slackValues[sIndex], tester.getSlacks(i)[sIndex], 0.1);
                }
            }
            l.clear();
            l.add(0);
            l.add(3);
            l.add(10);
            for (Integer i : l) {
                double[] projValues = getTestProjectionValues()[i];
                for (int pIndex = 0; pIndex < projValues.length; pIndex++) {
                    assertEquals(projValues[pIndex], tester.getProjections(i)[pIndex], 0.1);
                }
            }
            l.clear();
            l.add(0);
            l.add(3);
            l.add(10);
            l.add(14);
            for (Integer i : l) {
                double[] weightValues = getTestWeightValues()[i];
                for (int wIndex = 2; wIndex < weightValues.length; wIndex++) {
                    assertEquals(weightValues[wIndex], tester.getWeight(i)[wIndex], 0.001);
                }
            }
            assertEquals(tester.getOptimisationStatus(), SolverReturnStatus.OPTIMAL_SOLUTION_FOUND);
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            assertTrue(false);
        }
    }
}
