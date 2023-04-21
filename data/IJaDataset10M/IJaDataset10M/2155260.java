package rcsceneTests.caseStudies.paramFitting;

import java.util.ArrayList;
import org.jgap.FitnessFunction;
import org.jgap.Gene;
import org.jgap.IChromosome;
import rcscene.Action;
import rcscene.ActionWeights;
import rcscene.ObjectWeights;
import rcscene.validation.ActionWithStatistics;
import rcscene.validation.SceneStats;
import rcscene.validation.ValidationStatistics;
import rcscene.validation.Validator;

public class PercentageFitnessFunction extends FitnessFunction {

    private static final long serialVersionUID = 1L;

    private Validator myVal;

    private final int sceneSel = 2;

    private final float[] aWeights = { 1, 1, 1, 1, 1, 1, 1 };

    private final int distCal = 0;

    private final int numBest = 1;

    public PercentageFitnessFunction(String trainingFile, String testingFile) {
        ActionWeights aw = new ActionWeights(aWeights);
        ObjectWeights ow = new ObjectWeights();
        try {
            myVal = new Validator(trainingFile, testingFile, sceneSel, distCal, numBest, ow, aw);
        } catch (IllegalArgumentException e) {
            System.err.println("Bad file names supplied, closing program.");
            System.exit(1);
        }
    }

    public double evaluate(IChromosome a_subject) {
        float[] oWeights = new float[7];
        Gene[] genes = a_subject.getGenes();
        StringBuffer b = new StringBuffer();
        for (int ii = 0; ii < 7; ii++) {
            oWeights[ii] = ((Double) genes[ii].getAllele()).floatValue();
            b.append(" " + oWeights[ii]);
        }
        ObjectWeights ow = new ObjectWeights(oWeights);
        myVal.setObjectWeights(ow);
        SceneStats myStats = myVal.runValidation();
        ValidationStatistics myVals = myStats.getStats();
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
        double fitness = (f1Kick + f1Dash + f1Turn) / 3;
        return fitness;
    }

    public SceneStats getStats(IChromosome bestSolutionSoFar) {
        float[] oWeights = new float[7];
        Gene[] genes = bestSolutionSoFar.getGenes();
        StringBuffer b = new StringBuffer();
        for (int ii = 0; ii < 7; ii++) {
            oWeights[ii] = ((Double) genes[ii].getAllele()).floatValue();
            b.append(" " + oWeights[ii]);
        }
        ObjectWeights ow = new ObjectWeights(oWeights);
        myVal.setObjectWeights(ow);
        SceneStats ss = myVal.runValidation();
        return ss;
    }
}
