package com.trazere.util.trigger;

/**
 * The {@link Trigger} class represents triggers.
 */
public interface Trigger {

    /**
	 * Indicate whether the receiver trigger is set.
	 * 
	 * @return <code>true</code> when the trigger is set, <code>false</code> otherwise.
	 */
    public boolean isSet();

    /**
	 * Begin a session.
	 * 
	 * @return <code>true</code> when the trigger is being set, <code>false</code> when its state does not changed.
	 */
    public boolean begin();

    /**
	 * End a session.
	 * 
	 * @return <code>true</code> when the trigger is being reset, <code>false</code> when its state does not changed.
	 */
    public boolean end();
}
