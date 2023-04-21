package playground.thibautd.jointtrips.scoring;

import java.util.Collection;
import org.matsim.api.core.v01.population.Plan;

/**
 * @author thibautd
 */
public class HomogeneousScoreAggregatorFactory implements ScoresAggregatorFactory {

    @Override
    public ScoresAggregator createScoresAggregator(final Collection<? extends Plan> individualPlans) {
        return new HomogeneousScoreAggregator(individualPlans);
    }
}
