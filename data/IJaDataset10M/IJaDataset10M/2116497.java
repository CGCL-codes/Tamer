package promidi;

import promidi.Controller;
import promidi.Note;

abstract class MidiEventProcessor {

    long startTick;

    long endTick;

    boolean[] channels = new boolean[16];

    abstract void processNoteEvent(Note event);

    abstract void processControllerEvent(Controller event);

    /**
	 * Set channels that may pass through the filter
	 * @param channels Array of integers containing channel numbers
	 */
    void setChannels(int[] channels) {
        for (int n = 0; n < 16; n++) this.channels[n] = false;
        for (int i = 0; i < channels.length; i++) this.channels[i] = true;
    }

    boolean canProcessChannel(int channel) {
        return channels[channel];
    }

    long getStartTick() {
        return startTick;
    }

    void setStartTick(long startTick) {
        this.startTick = startTick;
    }

    long getEndTick() {
        return endTick;
    }

    void setEndTick(long endTick) {
        this.endTick = endTick;
    }
}
