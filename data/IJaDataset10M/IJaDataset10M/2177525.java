package org.semtinel.core.experiments.api;

import java.util.EventListener;

/**
 * A listener interface for receiving experiment events.
 * @see ExperimentEvent
 * @author Anastasia Pasenkova
 */
public interface ExperimentListener extends EventListener {

    /**
     * Invoked when the {@link Experiment} is closed, that is when the corresponding top component is closed.
     * @param e The experiment event
     */
    public void experimentClosed(ExperimentEvent e);

    /**
     * Invoked when the display name of the {@link Experiment} changes
     * @param e The experiment event
     */
    public void nameChanged(ExperimentEvent e);
}
