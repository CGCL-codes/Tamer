package moea.moga.examples;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import moea.commons.Population;
import moea.moga.algorithms.Moea;
import moea.moga.algorithms.Vega;
import moea.moga.genome.Chromosome;

public class ChowPaperThreaded extends ChowPaper {

    private static final long serialVersionUID = 1L;

    public static int genToMaxThread = 0;

    public ChowPaperThreaded() {
        super();
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 4) {
            System.out.println("Arguments: Function Numbits Evaluations Size");
            System.out.println("Recommended: [UGLY|BAD] [40|60] 5000 200");
            System.out.println("Recommended: [UGLY|BAD] [40|60] 25000 2000");
            return;
        }
        ChowPaper.initializeProblem(args);
        BufferedWriter out = new BufferedWriter(new FileWriter(new File("ChowPaperThreaded_" + args[0] + "_" + args[1] + "_" + args[2] + "_" + args[3] + ".txt")));
        int[] genToMax = new int[ChowPaper.numTrials];
        for (int i = 0; i < ChowPaper.numTrials; i++) {
            genToMax[i] = 0;
            genToMaxThread = Integer.MAX_VALUE;
            out.write("Iteration number: " + i + "...");
            System.out.print("Iteration number: " + i + "...");
            int numSubPops = 2;
            ArrayList<Population<Chromosome>> populations = new ArrayList<Population<Chromosome>>();
            for (int j = 0; j < numSubPops; j++) {
                Population<Chromosome> popIni = new Population<Chromosome>();
                for (int k = 0; k < Integer.valueOf(args[3]); ++k) {
                    ChowPaperThreaded individual = new ChowPaperThreaded();
                    popIni.add(individual);
                }
                populations.add(popIni);
            }
            ArrayList<Moea> algorithms = new ArrayList<Moea>();
            for (int j = 0; j < numSubPops; j++) algorithms.add(new Vega("Island" + j, populations.get(j), Integer.valueOf(args[2]), 0.60, 1.0 / M));
            for (int j = 0; j < numSubPops - 1; j++) algorithms.get(j).addNeighbor(algorithms.get(j + 1));
            algorithms.get(numSubPops - 1).addNeighbor(algorithms.get(0));
            for (int j = 0; j < numSubPops; j++) algorithms.get(j).start();
            for (int j = 0; j < numSubPops; j++) algorithms.get(j).join();
            if (ChowPaperThreaded.genToMaxThread < Integer.MAX_VALUE) genToMax[i] = ChowPaperThreaded.genToMaxThread; else genToMax[i] = 0;
            out.write("done with optimal at generation: " + genToMax[i] + "\n");
            System.out.println("done with optimal at generation: " + genToMax[i]);
        }
        printStats(out, genToMax);
        out.flush();
        out.close();
    }
}
