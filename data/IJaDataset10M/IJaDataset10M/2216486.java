package net.teqlo.components;

import net.teqlo.TeqloException;
import net.teqlo.db.ActivityLookup;
import net.teqlo.db.User;

/**
 * A transistor is a gated activity. The gate data is inspected at runtime by the activity,
 * and it decides whether enter gated state (isGated) or not. If isGated, then the activity, its
 * sequence and the context are all suspended.
 * @author jthwaites
 *
 */
public abstract class AbstractTransistor extends AbstractActivity implements Transistor {

    protected Object gate = null;

    /**
	 * Constructs a gateable activity using the supplied activity lookup
	 * @param al
	 */
    public AbstractTransistor(User user, ActivityLookup al) {
        super(user, al);
    }

    /**
	 * Sets the gate object to the supplied value
	 * @param gate Object which may result in this activity coming out of the gated state
	 */
    public void setGate(Object gate) throws TeqloException {
        this.gate = gate;
    }

    /**
	 * Returns the gate object which has previously been set, if any
	 * @return Object gate value which may result in this activity coming out of the gated state
	 */
    public Object getGate() {
        return this.gate;
    }
}
