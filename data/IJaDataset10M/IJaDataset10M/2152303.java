package playground.mfeil;

import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.core.scoring.ScoringFunction;
import org.matsim.core.scoring.ScoringFunctionFactory;

/**
 * A factory to create {@link JohScoringFunctionForChessboard}s.
 *
 * @author mfeil
 */
public class EstimatedJohScoringFunctionFactory implements ScoringFunctionFactory {

    Network network;

    public EstimatedJohScoringFunctionFactory(Network network) {
        this.network = network;
    }

    public ScoringFunction createNewScoringFunction(final Plan plan) {
        return new EstimatedJohScoringFunction(plan, network);
    }
}
