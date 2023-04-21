package org.nakedobjects.metamodel.interactions;

import static org.nakedobjects.metamodel.util.NakedObjectUtils.unwrap;
import org.nakedobjects.applib.Identifier;
import org.nakedobjects.applib.events.ObjectTitleEvent;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.consent.InteractionContextType;
import org.nakedobjects.metamodel.consent.InteractionInvocationMethod;

/**
 * See {@link InteractionContext} for overview; analogous to {@link ObjectTitleEvent}.
 */
public class ObjectTitleContext extends AccessContext<ObjectTitleEvent> {

    private final String title;

    public ObjectTitleContext(final AuthenticationSession session, final InteractionInvocationMethod invocationMethod, final NakedObject target, final Identifier identifier, final String title) {
        super(InteractionContextType.OBJECT_TITLE, session, invocationMethod, identifier, target);
        this.title = title;
    }

    @Override
    public ObjectTitleEvent createInteractionEvent() {
        return new ObjectTitleEvent(unwrap(getTarget()), getIdentifier(), getTitle());
    }

    private String getTitle() {
        return title;
    }
}
