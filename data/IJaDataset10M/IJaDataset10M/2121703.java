package org.nakedobjects.metamodel.interactions;

import org.nakedobjects.applib.Identifier;
import org.nakedobjects.applib.events.CollectionUsabilityEvent;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.consent.InteractionContextType;
import org.nakedobjects.metamodel.consent.InteractionInvocationMethod;

/**
 * See {@link InteractionContext} for overview; analogous to {@link CollectionUsabilityEvent}.
 */
public class CollectionUsabilityContext extends UsabilityContext<CollectionUsabilityEvent> {

    public CollectionUsabilityContext(final AuthenticationSession session, final InteractionInvocationMethod invocationMethod, final NakedObject target, final Identifier identifier) {
        super(InteractionContextType.COLLECTION_USABLE, session, invocationMethod, identifier, target);
    }

    @Override
    public CollectionUsabilityEvent createInteractionEvent() {
        return new CollectionUsabilityEvent(getTarget().getObject(), getIdentifier());
    }
}
