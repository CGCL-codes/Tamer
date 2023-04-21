package edu.asu.commons.event;

import edu.asu.commons.conf.ExperimentConfiguration;
import edu.asu.commons.net.Identifier;

public class ConfigurationEvent<T extends ExperimentConfiguration<?>> extends AbstractEvent {

    private static final long serialVersionUID = 1153572405897631171L;

    private final T configuration;

    public ConfigurationEvent(Identifier id, T configuration) {
        super(id);
        this.configuration = configuration;
    }

    public T getConfiguration() {
        return configuration;
    }
}
