package org.alcibiade.eternity.editor.application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.alcibiade.eternity.editor.log.NullLog;
import org.alcibiade.eternity.editor.model.GridModel;
import org.alcibiade.eternity.editor.model.operation.GridFiller;
import org.alcibiade.eternity.editor.solver.ClusterManager;
import org.alcibiade.eternity.editor.solver.swap.SwappingSolver;
import org.alcibiade.eternity.editor.solver.swap.WeightedRandomMkI;

public class RandomTuner {

    private static final int SIZE_MIN = 6;

    private static final int SIZE_MAX = 6;

    private static final double WEIGHT_STEP = 1.0;

    private static final double WEIGHT_MIN = 1;

    private static final double WEIGHT_MAX = 10;

    private static final int LAUNCHES = 200;

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        try {
            int launches = LAUNCHES;
            int sizeMin = SIZE_MIN;
            int sizeMax = SIZE_MAX;
            double weightMin = WEIGHT_MIN;
            double weightMax = WEIGHT_MAX;
            double weightStep = WEIGHT_STEP;
            switch(args.length) {
                case 6:
                    weightStep = Double.parseDouble(args[5]);
                case 5:
                    weightMax = Double.parseDouble(args[4]);
                case 4:
                    weightMin = Double.parseDouble(args[3]);
                case 3:
                    sizeMax = Integer.parseInt(args[2]);
                case 2:
                    sizeMin = Integer.parseInt(args[1]);
                case 1:
                    launches = Integer.parseInt(args[0]);
            }
            runTuning(launches, sizeMin, sizeMax, weightMin, weightMax, weightStep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void runTuning(int launches, int sizeMin, int sizeMax, double weightMin, double weightMax, double weightStep) throws InterruptedException {
        {
            StringBuilder line = new StringBuilder();
            line.append("        ");
            for (int size = sizeMin; size <= sizeMax; size++) {
                String weightText = String.format("%02d", size);
                line.append(String.format("%12s", weightText));
            }
            System.out.println(line);
        }
        for (double weight = weightMin; weight <= weightMax; weight += weightStep) {
            System.out.print(String.format(" %+4.3f ", weight));
            for (int modelSize = sizeMin; modelSize <= sizeMax; modelSize++) {
                List<Long> iterations = new ArrayList<Long>();
                long failures = 0;
                while (iterations.size() < launches && failures < 1) {
                    GridModel problem = new GridModel(modelSize);
                    GridFiller filler = new GridFiller(problem);
                    filler.fillRandom((int) (modelSize * 1.5));
                    problem.shuffle();
                    GridModel solution = new GridModel(modelSize);
                    ClusterManager clusterManager = new ClusterManager(new NullLog());
                    SwappingSolver solver = new WeightedRandomMkI(problem, solution, clusterManager, (int) weight);
                    solver.start();
                    solver.join((long) (Math.pow(3, modelSize + 1) * 1000));
                    if (solver.isAlive()) {
                        solver.interrupt();
                        solver.join();
                        failures++;
                    } else {
                        iterations.add(solver.getIterations());
                    }
                }
                String cyclesText = "";
                if (iterations.size() == launches) {
                    Collections.sort(iterations);
                    long average = iterations.get(launches / 2);
                    cyclesText = Long.toString(average);
                } else {
                    cyclesText = "/";
                }
                System.out.print(String.format("%12s", cyclesText));
                System.out.flush();
            }
            if (Math.abs(weight) < (weightStep / 2)) {
                System.out.print(" ----------");
            }
            System.out.println();
            System.out.flush();
        }
    }
}
