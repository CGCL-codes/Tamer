package com.frinika.sequencer.midieventprocessor;

import com.frinika.sequencer.MidiEventProcessor;
import com.frinika.sequencer.model.ControllerEvent;
import com.frinika.sequencer.model.NoteEvent;

/**
 * 
 * @author Peter Johan Salomonsen
 *
 */
public class Transpose extends MidiEventProcessor {

    int amount;

    public Transpose(int amount) {
        this.amount = amount;
    }

    @Override
    public void processNoteEvent(NoteEvent event) {
        event.setNote(event.getNote() + amount);
    }

    @Override
    public void processControllerEvent(ControllerEvent event) {
    }
}
