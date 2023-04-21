package com.directmodelling.impl;

import java.util.HashSet;
import java.util.Set;
import com.directmodelling.api.Value;
import com.directmodelling.api.Updates.Receiver;
import com.directmodelling.api.Updates.Tracker;

/**
 * Very simple implementation of {@link Tracker}. Does not work for either Swing
 * or GWT! Essentially it is only useful for unit tests where you call
 * {@link ExplicitUpdatesTracker#runUpdates()} to explicitly update everything.
 * 
 */
public class ExplicitUpdatesTracker implements Tracker {

    private final Set<Receiver> receivers = new HashSet<Receiver>();

    private boolean somethingChanged;

    @Override
    public void aValueChanged(Value<?> v) {
        somethingChanged = true;
    }

    @Override
    public void registerForChanges(Receiver ru) {
        receivers.add(ru);
    }

    @Override
    public void unregister(Receiver ru) {
        receivers.remove(ru);
    }

    /** Explicitly update everything that was registered. */
    public void runUpdates() {
        if (somethingChanged) for (Receiver receiver : receivers) {
            receiver.valuesChanged();
        }
    }
}
