package be.lassi.control.midi;

import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

/**
 * Implementation of the <code>Transmitter</code> interface 
 * for testing purposes.
 */
public class MockTransmitter implements Transmitter {

    private Receiver receiver = new MockReceiver();

    /**
     * {@inheritDoc}
     */
    public void close() {
    }

    /**
     * {@inheritDoc}
     */
    public Receiver getReceiver() {
        return receiver;
    }

    /**
     * {@inheritDoc}
     */
    public void setReceiver(final Receiver receiver) {
        this.receiver = receiver;
    }
}
