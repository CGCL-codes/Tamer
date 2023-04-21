package org.nakedobjects.metamodel.interactions;

import static org.nakedobjects.metamodel.util.NakedObjectUtils.unwrap;
import org.nakedobjects.applib.Identifier;
import org.nakedobjects.applib.events.ActionArgumentEvent;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.consent.InteractionContextType;
import org.nakedobjects.metamodel.consent.InteractionInvocationMethod;

/**
 * See {@link InteractionContext} for overview; analogous to {@link ActionArgumentEvent}.
 */
public class ActionArgumentContext extends ValidityContext<ActionArgumentEvent> implements ProposedHolder {

    private final NakedObject[] args;

    private final int position;

    private final NakedObject proposed;

    public ActionArgumentContext(final AuthenticationSession session, final InteractionInvocationMethod invocationMethod, final NakedObject target, final Identifier id, final NakedObject[] args, final int position) {
        super(InteractionContextType.ACTION_PROPOSED_ARGUMENT, session, invocationMethod, id, target);
        this.args = args;
        this.position = position;
        this.proposed = args[position];
    }

    public NakedObject[] getArgs() {
        return args;
    }

    public int getPosition() {
        return position;
    }

    public NakedObject getProposed() {
        return proposed;
    }

    @Override
    public ActionArgumentEvent createInteractionEvent() {
        return new ActionArgumentEvent(unwrap(getTarget()), getIdentifier(), unwrap(getArgs()), getPosition());
    }
}
