package com.musparke.midi.event;

import com.musparke.midi.model.Velocity;

public class VelocityEvent extends MusicXmlMidiEvent {

    private static final long serialVersionUID = 1L;

    private Velocity velocity;

    public VelocityEvent(Object source, Velocity velocity) {
        super(source);
        this.velocity = velocity;
    }

    public Velocity getVelocity() {
        return velocity;
    }

    public void setVelocity(Velocity velocity) {
        this.velocity = velocity;
    }
}
