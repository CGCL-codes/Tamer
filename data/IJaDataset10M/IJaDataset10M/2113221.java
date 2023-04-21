package org.nakedobjects.nos.client.dnd.content;

import org.nakedobjects.noa.adapter.NakedReference;
import org.nakedobjects.noa.reflect.Consent;
import org.nakedobjects.noa.reflect.NakedObjectAction;
import org.nakedobjects.noa.reflect.NakedObjectAction.Type;
import org.nakedobjects.nof.core.reflect.Allow;
import org.nakedobjects.nof.core.util.ToString;
import org.nakedobjects.nos.client.dnd.View;
import org.nakedobjects.nos.client.dnd.action.AbstractUserAction;

public abstract class AbstractObjectOption extends AbstractUserAction {

    protected final NakedObjectAction action;

    protected final NakedReference target;

    protected AbstractObjectOption(final NakedObjectAction action, final NakedReference target, final String name) {
        super(name);
        this.action = action;
        this.target = target;
    }

    public Consent disabled(final View view) {
        Consent usableForUser = action.isUsable();
        if (usableForUser.isVetoed()) {
            return usableForUser;
        }
        Consent usableInState = action.isUsable(target);
        if (usableInState.isVetoed()) {
            return usableInState;
        }
        Consent validParameters = checkValid();
        if (validParameters != null && validParameters.isVetoed()) {
            return validParameters;
        }
        String desc = action.getDescription();
        String description = getName(view) + (desc.length() == 0 ? "" : ": " + desc);
        return new Allow(description);
    }

    protected Consent checkValid() {
        return null;
    }

    public String getHelp(final View view) {
        return action.getHelp();
    }

    public Type getType() {
        return action.getType();
    }

    public String toString() {
        return new ToString(this).append("action", action).toString();
    }
}
