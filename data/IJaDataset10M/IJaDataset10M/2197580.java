package rcsceneTests.caseStudies.matchAnalysis;

import java.io.PrintWriter;
import java.util.ArrayList;
import rcscene.Action;
import rcscene.ActionWeights;
import rcscene.ObjectWeights;
import rcscene.validation.SceneStats;
import rcscene.validation.Validator;
import rcscene.validation.FuzzyValidator;
import rcscene.validation.CrispValidator;
import rcscene.validation.MatchSolution;

/** This class is used to analyze the distances between both
 * correct and incorrect matchs. This will help us examine the
 * size and coverage of the case base. 
 * 
 * @author Michael Floyd
 * @since May 16 2007
 *
 */
public class MatchAnalysis {

    private static final int sceneSel = 2;

    private static final float[] oWeights = { 1, 1, 1, 1, 1, 1, 1 };

    private static final float[] aWeights = { 1, 1, 1, 1, 1, 1, 1 };

    private static final int distCal = 7;

    private static final int numBest = 1;

    public static void main(String[] args) {
        System.out.println("Usage: MatchAnalysis trainingFile testingFile correctOutputFile incorrectOutputFile");
        String trainingFile = "training.scene";
        String testingFile = "testing.scene";
        String correctOutputFile = "correctOutputFile.txt";
        String incorrectOutputFile = "incorrectOutputFile.txt";
        if (args.length >= 4) {
            trainingFile = args[0];
            testingFile = args[1];
            correctOutputFile = args[2];
            incorrectOutputFile = args[3];
        }
        System.out.println("trainingFile = " + trainingFile);
        System.out.println("testingFile = " + testingFile);
        ObjectWeights ow = new ObjectWeights(oWeights);
        ActionWeights aw = new ActionWeights(aWeights);
        System.out.println("weights = " + ow.toString());
        FuzzyValidator myVal = new FuzzyValidator(trainingFile, testingFile, sceneSel, distCal, numBest, ow, aw, true);
        System.out.println("about to start validating");
        PrintWriter MyOut = new PrintWriter(System.out);
        SceneStats stats = myVal.runValidation(MyOut);
        ArrayList<MatchSolution> correct = stats.getCorrectDistances();
        ArrayList<MatchSolution> incorrect = stats.getIncorrectDistances();
        ArrayList<MatchSolution> correctDash = new ArrayList<MatchSolution>();
        ArrayList<MatchSolution> correctKick = new ArrayList<MatchSolution>();
        ArrayList<MatchSolution> correctTurn = new ArrayList<MatchSolution>();
        for (int ii = 0; ii < correct.size(); ii++) {
            MatchSolution ms = correct.get(ii);
            if (ms.getActionCandidate() == Action.ACTION_DASH) {
                correctDash.add(ms);
            } else if (ms.getActionCandidate() == Action.ACTION_KICK) {
                correctKick.add(ms);
            } else if (ms.getActionCandidate() == Action.ACTION_TURN) {
                correctTurn.add(ms);
            } else {
                throw new RuntimeException("We were not expecting this action. This code needs to be modified.");
            }
        }
        float corrAve = calculateMean(correct);
        float corrDashAve = calculateMean(correctDash);
        float corrKickAve = calculateMean(correctKick);
        float corrTurnAve = calculateMean(correctTurn);
        float incorrAve = calculateMean(incorrect);
        System.out.println("Experiment Complete.");
    }

    /** This method is used to calculate the mean value
	 * of an array list of MatchSolutions.
	 * 
	 * @param list The list of MatchSolutions
	 * @return The mean value
	 * @author Michael Floyd
	 * @since May 16 2007
	 */
    private static float calculateMean(ArrayList<MatchSolution> list) {
        float total = 0;
        for (MatchSolution value : list) {
            total += value.getDistance();
        }
        if (list.size() == 0) {
            return -1;
        }
        return total / list.size();
    }

    /** Goes through the list and returns the minimum value
	 * 
	 * @param list The collection of floats
	 * @return The minimum value in the list
	 */
    private static float calculateMin(ArrayList<Float> list) {
        if (list.size() == 0) {
            return -1;
        }
        float currentMin = list.get(0).floatValue();
        for (Float value : list) {
            currentMin = Math.min(currentMin, value.floatValue());
        }
        return currentMin;
    }

    /** Goes through the list and returns the maximum value
	 * 
	 * @param list The collection of floats
	 * @return The maximum value in the list
	 */
    private static float calculateMax(ArrayList<Float> list) {
        if (list.size() == 0) {
            return -1;
        }
        float currentMax = list.get(0).floatValue();
        for (Float value : list) {
            currentMax = Math.max(currentMax, value.floatValue());
        }
        return currentMax;
    }
}
