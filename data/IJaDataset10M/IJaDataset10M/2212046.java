package com.greentea.relaxation.jnmf.tests;

import com.greentea.relaxation.algorithms.art2m.Art2mAlgorithm;
import com.greentea.relaxation.jnmf.util.data.DataUtils;
import com.greentea.relaxation.jnmf.util.data.Sample;
import com.greentea.relaxation.jnmf.util.data.TrainingDataset;
import org.apache.commons.lang.math.RandomUtils;

public class Art2mTest {

    /**
    * @param args
    */
    public static void main(String[] args) {
        Art2mTest test = new Art2mTest();
        test.runTest();
    }

    public void runTest() {
        Art2mAlgorithm algorithm = new Art2mAlgorithm();
        algorithm.setP(0.6);
        algorithm.setLearningData(createLearningData());
        algorithm.startLearning();
        System.out.println("");
        for (int i = 0; i < 30; ++i) {
            for (int j = 0; j < 30; ++j) {
                algorithm.step(DataUtils.asList(i, j));
                System.out.print(" " + algorithm.getNetwork().getLayers().getLast().getMinActivationNeuronIndex());
            }
            System.out.println();
        }
    }

    TrainingDataset createLearningData() {
        TrainingDataset data = new TrainingDataset(2, 0);
        for (int i = 0; i < 100; ++i) {
            data.add(new Sample(null, DataUtils.asList(RandomUtils.nextInt(30), RandomUtils.nextInt(30)), null));
        }
        return data;
    }
}
