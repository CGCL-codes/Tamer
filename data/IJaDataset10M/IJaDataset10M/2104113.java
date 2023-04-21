package playground.johannes.socialnetworks.survey.ivt2009;

import org.matsim.api.core.v01.population.Person;
import org.matsim.contrib.sna.graph.AbstractSparseGraphBuilder;

/**
 * @author illenberger
 *
 */
public class SampledSocialNetBuilder<P extends Person> extends AbstractSparseGraphBuilder<SampledSocialNet<P>, SampledEgo<P>, SampledSocialTie> {

    public SampledSocialNetBuilder() {
        super(new SampledSocialNetFactory<P>());
    }

    @Override
    public SampledEgo<P> addVertex(SampledSocialNet<P> g) {
        throw new UnsupportedOperationException("Don't know what to with that...");
    }

    public SampledEgo<P> addVertex(SampledSocialNet<P> g, P person, int iteration) {
        SampledEgo<P> ego = new SampledEgo<P>(person);
        ego.detect(iteration);
        if (insertVertex(g, ego)) return ego; else return null;
    }
}
