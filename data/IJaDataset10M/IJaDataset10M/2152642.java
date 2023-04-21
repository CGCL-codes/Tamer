package org.jfugue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Sequence;

public final class TimeFactor {

    public static double DEFAULT_BPM = 128.0d;

    public static int QUARTER_DURATIONS_IN_WHOLE = 4;

    public static final double getTimeFactor(Sequence sequence, double bpm) {
        double divisionType = sequence.getDivisionType();
        double resolution = sequence.getResolution();
        if (divisionType == Sequence.PPQ) {
        } else {
        }
        if (bpm == 0.0) {
            bpm = DEFAULT_BPM;
        }
        return 60000.0 / (resolution * bpm);
    }

    public static final byte[] convertBPMToBytes(int tempo) {
        double tempoInMsPerBeat = TimeFactor.convertBPMToMicrosecondsPerBeat(tempo);
        double d1 = Math.floor(tempoInMsPerBeat / 16384.0);
        double d2 = Math.floor((tempoInMsPerBeat % 16384.0) / 128.0);
        double d3 = Math.floor((tempoInMsPerBeat % 16384.0) % 128.0);
        return new byte[] { (byte) d1, (byte) d2, (byte) d3 };
    }

    public static final int parseMicrosecondsPerBeat(MetaMessage message) {
        byte bytes[] = message.getMessage();
        int microseconds = (int) bytes[3] * 65536 + (int) bytes[4] * 256 + (int) bytes[5];
        return microseconds;
    }

    /** Converts microseconds per beat to BPM -- and vice versa */
    public static final double convertMicrosecondsPerBeatToBPM(double value) {
        double microsecondsPerMinute = 60000000.0D;
        if (value == 0.0d) {
            return 0.0d;
        }
        return microsecondsPerMinute / value;
    }

    /** Converts microseconds per beat to BPM -- and vice versa */
    public static final double convertBPMToMicrosecondsPerBeat(int bpm) {
        double microsecondsPerMinute = 60000000.0D;
        if (bpm == 0) {
            return 0;
        }
        return microsecondsPerMinute / bpm;
    }

    /**
     * Takes all of the MIDI events in the given Sequence, sorts them according to
     * when they are to be played, and sends the events to the MidiMessageRecipient
     * when the each event is ready to be played.
     * 
     * @param sequence The Sequence with messages to sort and deliver
     * @param recipient the handler of the delivered message
     */
    public static final void sortAndDeliverMidiMessages(Sequence sequence, MidiMessageRecipient recipient) {
        double timeFactor = 4.0;
        Map<Long, List<MidiEvent>> timeMap = new HashMap<Long, List<MidiEvent>>();
        long longestTime = TimeEventManager.sortSequenceByTimestamp(sequence, timeMap);
        long lastTime = 0;
        for (long time = 0; time <= longestTime; time++) {
            List<MidiEvent> midiEventList = (List<MidiEvent>) timeMap.get(time);
            if (midiEventList != null) {
                for (MidiEvent event : midiEventList) {
                    MidiMessage message = event.getMessage();
                    if ((message.getMessage().length >= 2) && (message.getMessage()[1] == 0x51) && (message instanceof MetaMessage)) {
                        double bpm = TimeFactor.convertMicrosecondsPerBeatToBPM(parseMicrosecondsPerBeat((MetaMessage) message)) * 4.0;
                        timeFactor = TimeFactor.getTimeFactor(sequence, bpm);
                    }
                    recipient.messageReady(message, time);
                }
                try {
                    long sleepTime = (int) (((time - lastTime) * (timeFactor * TimeFactor.QUARTER_DURATIONS_IN_WHOLE + 0.20)));
                    Thread.sleep(sleepTime);
                    lastTime = time;
                } catch (Exception ex) {
                    throw new JFugueException(JFugueException.ERROR_SLEEP);
                }
            }
        }
    }
}
