package playground.thibautd.jointtrips.replanning.modules.jointplanoptimizer.pipeddecoder;

import java.util.ArrayList;
import java.util.List;
import org.jgap.IChromosome;
import playground.thibautd.jointtrips.population.JointPlan;
import playground.thibautd.jointtrips.replanning.modules.jointplanoptimizer.JointPlanOptimizerDecoder;

/**
 * Modular decoder, decoding each "dimension" one after the other.
 * @author thibautd
 */
public class JointPlanOptimizerPipedDecoder implements JointPlanOptimizerDecoder {

    private final List<JointPlanOptimizerDimensionDecoder> decoders = new ArrayList<JointPlanOptimizerDimensionDecoder>();

    private final JointPlan plan;

    public JointPlanOptimizerPipedDecoder(final JointPlan plan) {
        this.plan = plan;
    }

    /**
	 * Add a decoder.
	 * The order is important.
	 */
    public void addDecoder(final JointPlanOptimizerDimensionDecoder decoder) {
        this.decoders.add(decoder);
    }

    /**
	 * execute the decoders in the order they were added.
	 */
    @Override
    public JointPlan decode(final IChromosome chromosome) {
        JointPlan outputPlan = this.plan;
        for (JointPlanOptimizerDimensionDecoder decoder : this.decoders) {
            outputPlan = decoder.decode(chromosome, outputPlan);
        }
        return outputPlan;
    }
}
