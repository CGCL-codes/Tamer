package de.sweetpete.percussionist.sequencer;

import java.beans.PropertyChangeEvent;

public interface SequencerView {

    public abstract void sequencerStateChange(final PropertyChangeEvent evt);
}
