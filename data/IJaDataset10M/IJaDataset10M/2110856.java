package net.sf.traser.propagation;

import net.sf.traser.configuration.composite.Managable;
import net.sf.traser.configuration.parameterized.Parameterized;
import net.sf.traser.databinding.base.CreateEvent;

/**
 * Forwards the updates of properties to other items after translating the event.
 * @author Marcell Szathmári
 */
public interface GuardExpression extends Parameterized, Managable<String> {

    /**
     * Tells whether the trigger is fired by the event or not.
     * @param event the event that is examined.
     * @return true if the trigger is fired.
     */
    boolean holds(CreateEvent event);
}
