package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.simplifiernull;

import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.AutomatonSimplifier;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying.AutomatonSimplifierFactory;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;
import org.openide.util.lookup.ServiceProvider;

/**
 * Factory for {@link SimplifierNull}.
 *
 * @author anti
 */
@ServiceProvider(service = AutomatonSimplifierFactory.class)
public class SimplifierNullFactory implements AutomatonSimplifierFactory {

    private static final Logger LOG = Logger.getLogger(SimplifierNullFactory.class);

    /**
   * Canonical name.
   */
    public static final String NAME = "TwoStepClusterProcessorAutomatonMergingStateAutomatonSimplifierNull";

    /**
   * Name presented to user.
   */
    public static final String DISPLAY_NAME = "Null";

    @Override
    public <T> AutomatonSimplifier<T> create() {
        LOG.debug("Creating new " + NAME);
        return new SimplifierNull<T>();
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
    public String getUserModuleDescription() {
        final StringBuilder sb = new StringBuilder(getDisplayName());
        sb.append(" does nothing.");
        return sb.toString();
    }

    @Override
    public List<String> getCapabilities() {
        return Collections.<String>emptyList();
    }

    @Override
    public String getDisplayName() {
        return DISPLAY_NAME;
    }
}
