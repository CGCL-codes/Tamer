package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.stateremoval.ordering.userinteractive;

import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.stateremoval.ordering.Orderer;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping.stateremoval.ordering.OrdererFactory;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;
import org.openide.util.lookup.ServiceProvider;

/**
 * Factory for {@link UserInteractive}.
 *
 * @author anti
 */
@ServiceProvider(service = OrdererFactory.class)
public class UserInteractiveFactory implements OrdererFactory {

    /**
   * Canonical name.
   */
    public static final String NAME = "TwoStepClusterProcessorAutomatonMergingStateRegexpAutomatonSimplifierStateRemovalOrdererUserInteractive";

    /**
   * Name presented to user.
   */
    public static final String DISPLAY_NAME = "User Interactive";

    private static final Logger LOG = Logger.getLogger(UserInteractiveFactory.class);

    @Override
    public <T> Orderer<T> create() {
        LOG.debug("Creating new " + NAME);
        return new UserInteractive<T>();
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
        sb.append(" orders states in automaton by prompting user which state remove first.");
        return sb.toString();
    }

    @Override
    public String getDisplayName() {
        return DISPLAY_NAME;
    }
}
