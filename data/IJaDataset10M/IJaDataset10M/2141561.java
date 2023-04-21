package playground.thibautd.jointtrips.replanning.modules.jointplanoptimizer.fitness;

import java.util.List;
import org.jgap.IChromosome;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.api.core.v01.population.PlanElement;
import org.matsim.core.router.PlansCalcRoute;
import org.matsim.core.scoring.ScoringFunction;
import org.matsim.core.scoring.ScoringFunctionFactory;
import org.matsim.core.utils.misc.Time;
import playground.thibautd.jointtrips.config.JointReplanningConfigGroup;
import playground.thibautd.jointtrips.population.JointPlan;
import playground.thibautd.jointtrips.replanning.modules.jointplanoptimizer.JointPlanOptimizerDecoder;
import playground.thibautd.jointtrips.replanning.modules.jointplanoptimizer.costestimators.JointPlanOptimizerLegTravelTimeEstimatorFactory;
import playground.thibautd.jointtrips.replanning.modules.jointplanoptimizer.pipeddecoder.JointPlanOptimizerDecoderFactory;

/**
 * A fitness function which uses a {@link JointPlanOptimizerDecoderFactory}
 * decoder and scores the resulting plan.
 *
 * @author thibautd
 */
public class JointPlanOptimizerFitnessFunction extends AbstractJointPlanOptimizerFitnessFunction {

    private static final long serialVersionUID = 1L;

    private final JointPlanOptimizerDecoder decoder;

    private final ScoringFunctionFactory scoringFunctionFactory;

    public JointPlanOptimizerFitnessFunction(final JointPlan plan, final JointReplanningConfigGroup configGroup, final JointPlanOptimizerLegTravelTimeEstimatorFactory legTravelTimeEstimatorFactory, final PlansCalcRoute routingAlgorithm, final Network network, final int numJointEpisodes, final int numEpisodes, final int nMembers, final ScoringFunctionFactory scoringFunctionFactory) {
        this(new JointPlanOptimizerDecoderFactory(plan, configGroup, legTravelTimeEstimatorFactory, routingAlgorithm, network, numJointEpisodes, numEpisodes, nMembers).createDecoder(), scoringFunctionFactory);
    }

    public JointPlanOptimizerFitnessFunction(final JointPlanOptimizerDecoder decoder, final ScoringFunctionFactory scoringFunctionFactory) {
        super();
        this.decoder = decoder;
        this.scoringFunctionFactory = scoringFunctionFactory;
    }

    @Override
    protected double evaluate(final IChromosome chromosome) {
        JointPlan plan = this.decoder.decode(chromosome);
        double score = this.getScore(plan);
        return score;
    }

    private double getScore(final JointPlan plan) {
        ScoringFunction fitnessFunction;
        Activity currentActivity;
        Leg currentLeg;
        for (Plan indivPlan : plan.getIndividualPlans().values()) {
            fitnessFunction = this.scoringFunctionFactory.createNewScoringFunction(indivPlan);
            List<PlanElement> elements = indivPlan.getPlanElements();
            Activity lastActivity = (Activity) elements.get(elements.size() - 1);
            for (PlanElement pe : elements) {
                if (pe instanceof Activity) {
                    currentActivity = (Activity) pe;
                    if (currentActivity == lastActivity) {
                        currentActivity.setEndTime(Time.UNDEFINED_TIME);
                    }
                    fitnessFunction.handleActivity(currentActivity);
                } else if (pe instanceof Leg) {
                    currentLeg = (Leg) pe;
                    fitnessFunction.handleLeg(currentLeg);
                } else {
                    throw new IllegalArgumentException("unrecognized plan element type");
                }
            }
            fitnessFunction.finish();
            indivPlan.setScore(fitnessFunction.getScore());
        }
        return plan.getScore();
    }

    @Override
    public JointPlanOptimizerDecoder getDecoder() {
        return this.decoder;
    }
}
