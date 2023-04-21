package playground.thibautd.jointtrips.replanning.strategies;

import org.matsim.core.controler.Controler;
import playground.thibautd.jointtrips.replanning.JointPlanStrategy;
import playground.thibautd.jointtrips.replanning.modules.jointplanoptimizer.JointPlanOptimizerModule;
import playground.thibautd.jointtrips.replanning.selectors.PlanWithLongestTypeSelector;
import playground.thibautd.jointtrips.replanning.selectors.RandomPlanSelectorWithoutCasts;

/**
 * a {@link JointPlanStrategy} using a {@link JointPlanOptimizerModule}.
 * The plan to modify is selected using a {@link PlanWithLongestTypeSelector}
 *
 * @author thibautd
 */
public class ReplanningStrategy extends JointPlanStrategy {

    public ReplanningStrategy(final Controler controler) {
        super(new RandomPlanSelectorWithoutCasts());
        this.addStrategyModule(new JointPlanOptimizerModule(controler));
    }
}
