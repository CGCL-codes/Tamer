package promidi;

/**
 * A track handles all midiEvents of a song for a certain midiout. You can directly 
 * add Events like Notes or ControllerChanges to it or also work with patterns.
 * @example promidi_sequencer
 * @related Song
 * @related Sequencer
 * @related Pattern
 */
public class Track extends Pattern {

    /**
	 * The midiOutput of the Track
	 */
    private MidiOut midiOut;

    /**
	 * Song the track is added to
	 */
    private Song song;

    /**
	 * Creates a new track using the given name and MidiOut
	 * @param i_name String: name for the track
	 * @param i_midiOut MidiOut: midi out the events are send to
	 * @example promidi_sequencer
	 * @related MidiOut
	 */
    public Track(final String i_name, final MidiOut i_midiOut) {
        super(i_name, 0);
        midiOut = i_midiOut;
    }

    /**
	 * Sends a note off to all 128 midi notes. 
	 * @param i_doControllers, if true also the controller data is set to 0
	 */
    void sendMidiPanic(final boolean i_doControllers) {
        for (int data1 = 0; data1 < 128; data1++) {
            midiOut.sendEvent(MidiEvent.NOTE_OFF, data1, 0);
        }
        if (i_doControllers) {
            for (int data1 = 0; data1 < 128; data1++) {
                midiOut.sendEvent(MidiEvent.CONTROL_CHANGE, data1, 0);
            }
        }
    }

    /**
	 * Resets the midi controllers at the given tick. THis method is called 
	 * by the sequencer when looping a sequence, or when starting playback in 
	 * the middle of the song.
	 * @param i_tick
	 */
    void resetControllers(final long i_tick) {
        resetControllers(i_tick, midiOut);
    }

    /**
	 * Used by the sequencer to play the events for the given tick.
	 * @param tick ,thats MidiEvents has to be returned
	 */
    void sendEventsForTick(final long i_tick) {
        sendEventsForTick(i_tick, midiOut);
    }

    /**
	 * Returns the song the track was added to
	 * @return
	 */
    Song getSong() {
        return song;
    }

    /**
	 * Set the song the track was added to
	 * @param i_song
	 */
    void setSong(final Song i_song) {
        song = i_song;
    }

    /**
	 * Returns the MidiOutput of the track.
	 * @return MidiOut, the Midi Output of the track
	 * @related Track
	 */
    public MidiOut getMidiOut() {
        return midiOut;
    }

    /**
	 * Sets the MidiOut of the Track
	 * @param midiOut MidiOut, the new MidiOutput of the track
	 * @related Track
	 */
    public void setMidiOut(final MidiOut i_midiOut) {
        midiOut = i_midiOut;
    }
}
