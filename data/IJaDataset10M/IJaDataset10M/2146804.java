package rcsceneTests.caseStudies.paramFitting;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import org.jgap.*;
import org.jgap.impl.*;
import rcscene.Action;
import rcscene.validation.ActionWithStatistics;
import rcscene.validation.SceneStats;
import rcscene.validation.ValidationStatistics;

public class ParameterFitter {

    public static void main(String[] args) throws InvalidConfigurationException {
        System.out.println("Usage: ParameterFitter trainingFile testingFile popSize generations scoresOutputFile weightsOutputFile");
        String scoresOutputFile = "myscores.txt";
        String weightsOutputFile = "myweights.txt";
        int MAX_POPULATION_SIZE = 50;
        int MAX_ALLOWED_GENERATIONS = 1000;
        String trainingFile = "training.scene";
        String testingFile = "testing.scene";
        if (args.length >= 6) {
            trainingFile = args[0];
            testingFile = args[1];
            MAX_POPULATION_SIZE = Integer.parseInt(args[2]);
            MAX_ALLOWED_GENERATIONS = Integer.parseInt(args[3]);
            scoresOutputFile = args[4];
            weightsOutputFile = args[5];
        } else {
            System.out.println("Not enough parameters supplied. Using defaults.");
        }
        System.out.println("trainingFile = " + trainingFile);
        System.out.println("testingFile = " + testingFile);
        System.out.println("popSize = " + MAX_POPULATION_SIZE);
        System.out.println("generations = " + MAX_ALLOWED_GENERATIONS);
        System.out.println("scoresOutputFile = " + scoresOutputFile);
        System.out.println("weightsOutputFile = " + weightsOutputFile);
        FileOutputStream outScore;
        FileOutputStream outWeight;
        PrintStream pScore = null;
        PrintStream pWeight = null;
        try {
            outScore = new FileOutputStream(scoresOutputFile);
            pScore = new PrintStream(outScore);
            outWeight = new FileOutputStream(weightsOutputFile);
            pWeight = new PrintStream(outWeight);
        } catch (Exception e) {
            System.err.println("Error opening output files");
            return;
        }
        Configuration conf = new DefaultConfiguration();
        FitnessFunction myFunc = new PercentageFitnessFunction(trainingFile, testingFile);
        try {
            conf.setFitnessFunction(myFunc);
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
        Gene[] sampleGenes = new Gene[7];
        sampleGenes[0] = new DoubleGene(0, 1);
        sampleGenes[1] = new DoubleGene(0, 1);
        sampleGenes[2] = new DoubleGene(0, 1);
        sampleGenes[3] = new DoubleGene(0, 1);
        sampleGenes[4] = new DoubleGene(0, 1);
        sampleGenes[5] = new DoubleGene(0, 1);
        sampleGenes[6] = new DoubleGene(0, 1);
        Chromosome sampleChromosome = new Chromosome(sampleGenes);
        IChromosome bestSolutionSoFar = null;
        try {
            conf.addNaturalSelector(new TournamentSelector(4, .7), true);
            conf.getGeneticOperators().clear();
            conf.addGeneticOperator(new MutationOperator(20));
            conf.setSampleChromosome(sampleChromosome);
            conf.setPopulationSize(MAX_POPULATION_SIZE);
            Genotype population = Genotype.randomInitialGenotype(conf);
            System.out.print("Currently on generation : ");
            for (int i = 0; i < MAX_ALLOWED_GENERATIONS; i++) {
                System.out.print(i + 1 + ".");
                population.evolve();
                bestSolutionSoFar = population.getFittestChromosome();
                Gene[] genes = bestSolutionSoFar.getGenes();
                StringBuffer b = new StringBuffer();
                for (int ii = 0; ii < 7; ii++) {
                    ((Double) genes[ii].getAllele()).floatValue();
                    b.append(" " + genes[ii].getAllele());
                }
                pWeight.println(b.toString());
                SceneStats myStats = ((PercentageFitnessFunction) myFunc).getStats(bestSolutionSoFar);
                ValidationStatistics myVals = myStats.getStats();
                double accuracyTotal = myVals.getPercentageTOTAL();
                double recallKick = myVals.getPercentageKICK();
                double recallDash = myVals.getPercentageDASH();
                double recallTurn = myVals.getPercentageTURN();
                ArrayList<ActionWithStatistics> confMatrix = myStats.getCollection();
                ActionWithStatistics kick_row = confMatrix.get(Action.ACTION_KICK);
                ActionWithStatistics dash_row = confMatrix.get(Action.ACTION_DASH);
                ActionWithStatistics turn_row = confMatrix.get(Action.ACTION_TURN);
                int kick_cor = kick_row.getCorrectCount();
                int kick_incor = kick_row.getIncorrectCount();
                int dash_cor = dash_row.getCorrectCount();
                int dash_incor = dash_row.getIncorrectCount();
                int turn_cor = turn_row.getCorrectCount();
                int turn_incor = turn_row.getIncorrectCount();
                double precKick = (double) (100 * kick_cor) / (double) (kick_cor + kick_incor);
                double precDash = (double) (100 * dash_cor) / (double) (dash_cor + dash_incor);
                double precTurn = (double) (100 * turn_cor) / (double) (turn_cor + turn_incor);
                double f1Kick = 2 * precKick * recallKick / (precKick + recallKick);
                double f1Dash = 2 * precDash * recallDash / (precDash + recallDash);
                double f1Turn = 2 * precTurn * recallTurn / (precTurn + recallTurn);
                pScore.println(bestSolutionSoFar.getFitnessValue() + " \t" + f1Kick + "\t" + f1Dash + "\t" + f1Turn + "\t" + accuracyTotal + "\t" + recallKick + "\t" + recallDash + "\t" + recallTurn + "\t" + precKick + "\t" + precDash + "\t" + precTurn);
            }
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
        pWeight.close();
        pScore.close();
    }
}
