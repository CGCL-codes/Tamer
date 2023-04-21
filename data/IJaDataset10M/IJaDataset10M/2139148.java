package nl.adaptivity.adapt.script;

import nl.adaptivity.adapt.Event;
import nl.adaptivity.adapt.ObjectImplInterface;

/**
 * A class that wraps event objects. It allows Events to provide their own
 * context.
 * 
 * @author Paul de Vrieze
 * @version 1.0
 */
public class EventObjectSupport extends ReadonlyObjectWrapper<ObjectImplInterface> {

    private final Event aEvent;

    /**
   * Create a new EventObjectSupport.
   * 
   * @param pEvent The event to wrap.
   * @param pParent The parent context.
   */
    public EventObjectSupport(final Event pEvent, final ObjectSupport pParent) {
        super(pParent.getUserModel(), pParent.getObject());
        aEvent = pEvent;
    }

    /** {@inheritDoc} */
    @Override
    public EventSupport getContext() {
        return new EventSupport(getUserModel(), getObject(), aEvent, super.getContext());
    }
}
