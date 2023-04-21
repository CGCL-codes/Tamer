package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.stateremoval.ordering.weighted;

import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.stateremoval.ordering.Orderer;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.stateremoval.ordering.OrdererFactory;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;
import org.openide.util.lookup.ServiceProvider;

/**
 * Factory for {@link Weighted}.
 *
 * @author anti
 */
@ServiceProvider(service = OrdererFactory.class)
public class WeightedFactory implements OrdererFactory {

    /**
   * Canonical name
   */
    public static final String NAME = "TwoStepClusterProcessorAutomatonMergingStateRegexpAutomatonSimplifierStateRemovalOrdererWeighted";

    /**
   * Name presented to user.
   */
    public static final String DISPLAY_NAME = "Weighted";

    private static final Logger LOG = Logger.getLogger(WeightedFactory.class);

    @Override
    public <T> Orderer<T> create() {
        LOG.debug("Creating new " + NAME);
        return new Weighted<T>();
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getModuleDescription() {
        return getDisplayName();
    }

    @Override
    public List<String> getCapabilities() {
        return Collections.<String>emptyList();
    }

    @Override
    public String getUserModuleDescription() {
        final StringBuilder sb = new StringBuilder(getDisplayName());
        sb.append(" weights states and returns state with minimum weight to be removed." + " Weight of the state is the sum of length of all regular expressions" + " on in-transitions, out-transitions and loops.");
        return sb.toString();
    }

    @Override
    public String getDisplayName() {
        return DISPLAY_NAME;
    }
}
