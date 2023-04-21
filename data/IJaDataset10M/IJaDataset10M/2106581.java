package com.trazere.util.trigger;

import com.trazere.util.lang.MutableBoolean;

/**
 * The {@link AbstractTrigger} abstract class provides skeletons for writing triggers.
 */
public abstract class AbstractTrigger implements Trigger {

    /** State of the trigger. */
    private final MutableBoolean _state = new MutableBoolean(false);

    public boolean isSet() {
        return _state.get();
    }

    public boolean begin() {
        final boolean set = doBegin();
        if (set && !_state.get()) {
            _state.set(true);
            return true;
        } else {
            return false;
        }
    }

    /**
	 * Begin a session.
	 * 
	 * @return <code>true</code> when the trigger is set, <code>false</code> otherwise.
	 */
    protected abstract boolean doBegin();

    public boolean end() {
        final boolean set = doEnd();
        if (!set && _state.get()) {
            _state.set(false);
            return true;
        } else {
            return false;
        }
    }

    /**
	 * End a session.
	 * 
	 * @return <code>true</code> when the trigger is set, <code>false</code> otherwise.
	 */
    protected abstract boolean doEnd();
}
