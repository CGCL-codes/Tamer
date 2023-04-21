package playground.yu.integration.cadyts.parameterCalibration.withCarCounts.experiment.generalFrejingerScoring;

import java.util.ArrayList;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.core.scoring.CharyparNagelScoringParameters;
import org.matsim.core.scoring.ScoringFunction;
import org.matsim.core.scoring.charyparNagel.LegScoringFunction;
import org.matsim.core.scoring.interfaces.ActivityScoring;
import org.matsim.core.scoring.interfaces.AgentStuckScoring;
import org.matsim.core.scoring.interfaces.BasicScoring;
import org.matsim.core.scoring.interfaces.LegScoring;
import org.matsim.core.scoring.interfaces.MoneyScoring;

public class ScoringFunctionAccumulator4PC2 implements ScoringFunction {

    protected CharyparNagelScoringParameters params;

    protected ArrayList<BasicScoring> basicScoringFunctions = new ArrayList<BasicScoring>();

    private ArrayList<ActivityScoring> activityScoringFunctions = new ArrayList<ActivityScoring>();

    private ArrayList<MoneyScoring> moneyScoringFunctions = new ArrayList<MoneyScoring>();

    private ArrayList<LegScoring> legScoringFunctions = new ArrayList<LegScoring>();

    private ArrayList<AgentStuckScoring> agentStuckScoringFunctions = new ArrayList<AgentStuckScoring>();

    private ArrayList<PathSizeScoringFunction> pathSizeScoringFunctions = new ArrayList<PathSizeScoringFunction>();

    private double perfAttr, travTimeAttrCar, lnPathSizeAttr;

    private int nbSpeedBumpsAttr, nbLeftTurnsAttr, nbIntersectionsAttr;

    public ScoringFunctionAccumulator4PC2(CharyparNagelScoringParameters params) {
        this.params = params;
    }

    public double getPerfAttr() {
        return perfAttr;
    }

    public double getTravTimeAttrCar() {
        return travTimeAttrCar;
    }

    public int getNbSpeedBumps() {
        return nbSpeedBumpsAttr;
    }

    public int getNbLeftTurns() {
        return nbLeftTurnsAttr;
    }

    public int getNbIntersections() {
        return nbIntersectionsAttr;
    }

    public double getLnPathSizeAttr() {
        return lnPathSizeAttr;
    }

    public void addMoney(double amount) {
        for (MoneyScoring moneyScoringFunction : moneyScoringFunctions) {
            moneyScoringFunction.addMoney(amount);
        }
    }

    public void agentStuck(double time) {
        for (AgentStuckScoring agentStuckScoringFunction : agentStuckScoringFunctions) {
            agentStuckScoringFunction.agentStuck(time);
        }
    }

    public void startActivity(double time, Activity act) {
        for (ActivityScoring activityScoringFunction : activityScoringFunctions) {
            activityScoringFunction.startActivity(time, act);
        }
    }

    public void endActivity(double time) {
        for (ActivityScoring activityScoringFunction : activityScoringFunctions) {
            activityScoringFunction.endActivity(time);
        }
    }

    public void startLeg(double time, Leg leg) {
        for (LegScoring legScoringFunction : legScoringFunctions) {
            legScoringFunction.startLeg(time, leg);
        }
    }

    public void endLeg(double time) {
        for (LegScoring legScoringFunction : legScoringFunctions) {
            legScoringFunction.endLeg(time);
        }
    }

    public void finish() {
        for (BasicScoring basicScoringFunction : basicScoringFunctions) {
            basicScoringFunction.finish();
        }
    }

    /**
	 * Add the score of all functions.
	 */
    public double getScore() {
        double score = 0.0;
        for (BasicScoring basicScoringFunction : basicScoringFunctions) {
            double fracScore = basicScoringFunction.getScore();
            score += fracScore;
            if (basicScoringFunction instanceof ActivityScoring) {
                perfAttr = params.marginalUtilityOfPerforming_s != 0d ? fracScore / (params.marginalUtilityOfPerforming_s * 3600d) : 0d;
            } else if (basicScoringFunction instanceof LegScoringFunction) {
                LegScoringFunction4PC2 legScoringFunction = (LegScoringFunction4PC2) basicScoringFunction;
                travTimeAttrCar = legScoringFunction.getTravTimeAttrCar();
                nbSpeedBumpsAttr = legScoringFunction.getNbSpeedBumpsAttr();
                nbLeftTurnsAttr = legScoringFunction.getNbLeftTurnsAttr();
                nbIntersectionsAttr = legScoringFunction.getNbIntersectionsAttr();
            } else if (basicScoringFunction instanceof PathSizeScoringFunction) {
                lnPathSizeAttr = ((PathSizeScoringFunction) basicScoringFunction).getLnPathSizeAttr();
            }
        }
        return score;
    }

    public void reset() {
        for (BasicScoring basicScoringFunction : basicScoringFunctions) {
            basicScoringFunction.reset();
        }
    }

    /**
	 * add the scoring function the list of functions, it implemented the
	 * interfaces.
	 * 
	 * @param scoringFunction
	 */
    public void addScoringFunction(BasicScoring scoringFunction) {
        basicScoringFunctions.add(scoringFunction);
        if (scoringFunction instanceof ActivityScoring) {
            activityScoringFunctions.add((ActivityScoring) scoringFunction);
        }
        if (scoringFunction instanceof AgentStuckScoring) {
            agentStuckScoringFunctions.add((AgentStuckScoring) scoringFunction);
        }
        if (scoringFunction instanceof LegScoring) {
            legScoringFunctions.add((LegScoring) scoringFunction);
        }
        if (scoringFunction instanceof MoneyScoring) {
            moneyScoringFunctions.add((MoneyScoring) scoringFunction);
        }
        if (scoringFunction instanceof PathSizeScoringFunction) {
            pathSizeScoringFunctions.add((PathSizeScoringFunction) scoringFunction);
        }
    }

    public ArrayList<ActivityScoring> getActivityScoringFunctions() {
        return activityScoringFunctions;
    }
}
