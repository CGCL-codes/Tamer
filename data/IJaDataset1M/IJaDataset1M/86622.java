package org.jfugue;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Sequence;
import org.jfugue.parsers.MidiParser;

/**
 * This class can be used in conjunction with a call to Player.play() to
 * inform your application about musical events before they happen.  This
 * is useful if you're creating an application that requires advance notice
 * of a musical event - for example, an animation program that must wind up
 * or swing an arm back before striking a note.
 *   
 * This feature is covered in detail in "The Complete Guide to JFugue"
 *   
 * @author David Koelle
 * @version 3.0
 */
public class Anticipator {

    protected MidiParser parser;

    public Anticipator() {
        this.parser = new MidiParser();
    }

    /**
     * Adds a <code>ParserListener</code>.
     *
     * @param listener the listener to remove
     */
    public void addParserListener(ParserListener listener) {
        this.parser.addParserListener(listener);
    }

    /**
     * Removes a <code>ParserListener</code>.
     *
     * @param listener the listener to remove
     */
    public void removeParserListener(ParserListener listener) {
        this.parser.removeParserListener(listener);
    }

    protected void play(final Sequence sequence) {
        final Thread anticipatingThread = new Thread() {

            public void run() {
                TimeFactor.sortAndDeliverMidiMessages(sequence, new MidiMessageRecipient() {

                    public void messageReady(MidiMessage message, long timestamp) {
                        parser.parse(message, timestamp);
                    }
                });
            }
        };
        anticipatingThread.start();
    }
}
