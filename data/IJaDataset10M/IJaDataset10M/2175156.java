package playground.thibautd.jointtrips.replanning.modules.jointplanoptimizer;

import java.util.Random;
import org.jgap.Genotype;
import org.jgap.InvalidConfigurationException;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.core.gbl.MatsimRandom;
import org.matsim.core.router.PlansCalcRoute;
import org.matsim.core.scoring.ScoringFunctionFactory;
import org.matsim.population.algorithms.PlanAlgorithm;
import playground.thibautd.jointtrips.config.JointReplanningConfigGroup;
import playground.thibautd.jointtrips.population.JointPlan;
import playground.thibautd.jointtrips.replanning.JointPlanAlgorithm;
import playground.thibautd.jointtrips.replanning.modules.jointplanoptimizer.configuration.JointPlanOptimizerJGAPConfiguration;
import playground.thibautd.jointtrips.replanning.modules.jointplanoptimizer.configuration.JointPlanOptimizerProcessBuilder;
import playground.thibautd.jointtrips.replanning.modules.jointplanoptimizer.configuration.JointPlanOptimizerSemanticsBuilder;
import playground.thibautd.jointtrips.replanning.modules.jointplanoptimizer.costestimators.JointPlanOptimizerLegTravelTimeEstimatorFactory;

/**
 * {@link PlanAlgorithm} aimed at optimizing joint plans with a genetic algorithm.
 * @author thibautd
 */
public class JointPlanOptimizer extends JointPlanAlgorithm {

    private final JointReplanningConfigGroup configGroup;

    private final String outputPath;

    private final JointPlanOptimizerSemanticsBuilder semanticsBuilder;

    private final JointPlanOptimizerProcessBuilder processBuilder;

    private final Random randomGenerator = MatsimRandom.getLocalInstance();

    public JointPlanOptimizer(final JointReplanningConfigGroup configGroup, final JointPlanOptimizerSemanticsBuilder semanticsBuilder, final JointPlanOptimizerProcessBuilder processBuilder, final String iterationOutputPath) {
        this.configGroup = configGroup;
        this.semanticsBuilder = semanticsBuilder;
        this.processBuilder = processBuilder;
        this.outputPath = iterationOutputPath;
    }

    /**
	 * the actual optimisation algorithm, operating on a joint plan.
	 */
    @Override
    public void run(final JointPlan plan) {
        if (!isOptimizablePlan(plan)) {
            return;
        }
        JointPlanOptimizerJGAPConfiguration jgapConfig = new JointPlanOptimizerJGAPConfiguration(plan, semanticsBuilder, processBuilder, configGroup, outputPath, randomGenerator.nextLong());
        Genotype gaPopulation;
        try {
            gaPopulation = Genotype.randomInitialGenotype(jgapConfig);
        } catch (InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
        if (this.configGroup.getFitnessToMonitor()) {
            gaPopulation.evolve(jgapConfig.getEvolutionMonitor());
        } else {
            gaPopulation.evolve(this.configGroup.getMaxIterations());
        }
        jgapConfig.finish();
        JointPlan evolvedPlan = jgapConfig.getDecoder().decode(gaPopulation.getFittestChromosome());
        plan.resetFromPlan(evolvedPlan);
        plan.resetScores();
    }

    private boolean isOptimizablePlan(final JointPlan plan) {
        for (Plan indivPlan : plan.getIndividualPlans().values()) {
            if (indivPlan.getPlanElements().size() > 1) {
                return true;
            }
        }
        return false;
    }
}
