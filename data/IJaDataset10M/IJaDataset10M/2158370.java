package jm.midi;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;
import jm.JMC;
import jm.midi.event.CChange;
import jm.midi.event.EndTrack;
import jm.midi.event.Event;
import jm.midi.event.KeySig;
import jm.midi.event.NoteOn;
import jm.midi.event.PChange;
import jm.midi.event.TempoEvent;
import jm.midi.event.TimeSig;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import ren.util.ODouble;
import ren.util.PO;

/**
 * A MIDI parser 
 * @author Andrew Sorensen
 */
public final class MidiParser implements JMC {

    /**
	 * Convert a SMF into the jMusic data type
	 */
    public static void SMFToScore(Score score, SMF smf) {
        System.out.println("Convert SMF to JM");
        Enumeration enumr = smf.getTrackList().elements();
        while (enumr.hasMoreElements()) {
            Part part = new Part();
            Track smfTrack = (Track) enumr.nextElement();
            Vector evtList = smfTrack.getEvtList();
            Vector phrVct = new Vector();
            sortEvents(score, evtList, phrVct, smf, part);
            for (int i = 0; i < phrVct.size(); i++) {
                part.addPhrase((Phrase) phrVct.elementAt(i));
            }
            score.addPart(part);
            score.clean();
        }
    }

    private static void sortEvents(Score score, Vector evtList, Vector phrVct, SMF smf, Part part) {
        double startTime = 0.0;
        double[] currentLength = new double[100];
        Note[] curNote = new Note[100];
        int numOfPhrases = 0;
        double oldTime = 0.0;
        int phrIndex = 0;
        for (int i = 0; i < evtList.size(); i++) {
            Event evt = (Event) evtList.elementAt(i);
            startTime += (double) evt.getTime() / (double) smf.getPPQN();
            if (evt.getID() == 007) {
                PChange pchg = (PChange) evt;
                part.setInstrument(pchg.getValue());
            } else if (evt.getID() == 020) {
                TempoEvent t = (TempoEvent) evt;
                score.setTempo(t.getTempo());
            } else if (evt.getID() == 005) {
                NoteOn noteOn = (NoteOn) evt;
                part.setChannel(noteOn.getMidiChannel());
                short pitch = noteOn.getPitch();
                int dynamic = noteOn.getVelocity();
                short midiChannel = noteOn.getMidiChannel();
                if (dynamic > 0) {
                    noteOn(phrIndex, curNote, smf, i, currentLength, startTime, phrVct, midiChannel, pitch, dynamic, evtList);
                }
            }
        }
    }

    private static void noteOn(int phrIndex, Note[] curNote, SMF smf, int i, double[] currentLength, double startTime, Vector phrVct, short midiChannel, short pitch, int dynamic, Vector evtList) {
        phrIndex = -1;
        for (int p = 0; p < phrVct.size(); p++) {
            if (currentLength[p] <= (startTime + 0.08)) {
                phrIndex = p;
                break;
            }
        }
        if (phrIndex == -1) {
            phrIndex = phrVct.size();
            phrVct.addElement(new Phrase(startTime));
            currentLength[phrIndex] = startTime;
        }
        if ((startTime > currentLength[phrIndex]) && (curNote[phrIndex] != null)) {
            double newTime = startTime - currentLength[phrIndex];
            if (newTime < 0.25) {
                double length = curNote[phrIndex].getRhythmValue();
                curNote[phrIndex].setRhythmValue(length + newTime);
            } else {
                Note restNote = new Note(REST, newTime, 0);
                restNote.setPan(midiChannel);
                restNote.setDuration(newTime);
                restNote.setOffset(0.0);
                ((Phrase) phrVct.elementAt(phrIndex)).addNote(restNote);
            }
            currentLength[phrIndex] += newTime;
        }
        double time = MidiUtil.getEndEvt(pitch, evtList, i) / (double) smf.getPPQN();
        Note tempNote = new Note(pitch, time, dynamic);
        tempNote.setDuration(time);
        curNote[phrIndex] = tempNote;
        ((Phrase) phrVct.elementAt(phrIndex)).addNote(curNote[phrIndex]);
        currentLength[phrIndex] += curNote[phrIndex].getRhythmValue();
    }

    public static void scoreToSMF(Score score, SMF smf) {
        scoreToSMF(score, smf, true);
    }

    public static void scoreToSMF(Score score, SMF smf, boolean cofroze) {
        if (VERBOSE) System.out.println("Converting to SMF data structure...");
        double partTempoMultiplier = 1.0;
        double scoreTempo = score.getTempo();
        int phraseNumb;
        Phrase phrase1, phrase2;
        Track smfT = new Track();
        smfT.addEvent(new TempoEvent(0, score.getTempo()));
        smfT.addEvent(new TimeSig(0, score.getNumerator(), score.getDenominator()));
        smfT.addEvent(new KeySig(0, score.getKeySignature()));
        smfT.addEvent(new EndTrack());
        smf.getTrackList().addElement(smfT);
        int partCount = 0;
        Enumeration enumr = score.getPartList().elements();
        while (enumr.hasMoreElements()) {
            Track smfTrack = new Track();
            Part inst = ((Part) enumr.nextElement()).copy();
            if (cofroze) {
                if (inst.getChannel() == 0) {
                    try {
                        Exception e = new Exception("need to set cofroze to false");
                        e.fillInStackTrace();
                        throw e;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                inst.setChannel(inst.getChannel() - 1);
            }
            System.out.print("    Part " + partCount + " '" + inst.getTitle() + "' to SMF Track on Ch. " + inst.getChannel() + ": ");
            partCount++;
            if (inst.getTempo() != Part.DEFAULT_TEMPO) partTempoMultiplier = scoreTempo / inst.getTempo(); else partTempoMultiplier = 1.0;
            phraseNumb = inst.getPhraseList().size();
            for (int i = 0; i < phraseNumb; i++) {
                phrase1 = (Phrase) inst.getPhraseList().elementAt(i);
                for (int j = 0; j < phraseNumb; j++) {
                    phrase2 = (Phrase) inst.getPhraseList().elementAt(j);
                    if (phrase2.getStartTime() > phrase1.getStartTime()) {
                        inst.getPhraseList().setElementAt(phrase2, i);
                        inst.getPhraseList().setElementAt(phrase1, j);
                        break;
                    }
                }
            }
            HashMap midiEvents = new HashMap();
            if (inst.getTempo() != Part.DEFAULT_TEMPO) {
                addEvent(midiEvents, 0.0, new TempoEvent(inst.getTempo()));
            }
            if (inst.getInstrument() != NO_INSTRUMENT) {
                addEvent(midiEvents, 0.0, new PChange((short) inst.getInstrument(), (short) inst.getChannel(), 0));
            }
            if (inst.getNumerator() != NO_NUMERATOR) {
                addEvent(midiEvents, 0.0, new TimeSig(inst.getNumerator(), inst.getDenominator()));
            }
            if (inst.getKeySignature() != NO_KEY_SIGNATURE) {
                addEvent(midiEvents, 0.0, new KeySig(inst.getKeySignature(), inst.getKeyQuality()));
            }
            Enumeration partEnum = inst.getPhraseList().elements();
            double max = 0;
            double startTime = 0.0;
            double offsetValue = 0.0;
            ODouble prest = (new ODouble()).construct(-0.1);
            int phraseCounter = 0;
            double phraseTempoMultiplier = 1.0;
            while (partEnum.hasMoreElements()) {
                Phrase phrase = (Phrase) partEnum.nextElement();
                Enumeration phraseEnum = phrase.getNoteList().elements();
                startTime = phrase.getStartTime() * partTempoMultiplier;
                if (phrase.getInstrument() != NO_INSTRUMENT) {
                    addEvent(midiEvents, startTime, new PChange((short) phrase.getInstrument(), (short) inst.getChannel(), 0));
                }
                if (phrase.getTempo() != Phrase.DEFAULT_TEMPO) {
                    phraseTempoMultiplier = (scoreTempo * partTempoMultiplier) / phrase.getTempo();
                } else {
                    phraseTempoMultiplier = partTempoMultiplier;
                }
                int noteCounter = 0;
                System.out.print(" Phrase " + phraseCounter++ + ":");
                double pan = -1.0;
                resetTicker();
                while (phraseEnum.hasMoreElements()) {
                    Note note = (Note) phraseEnum.nextElement();
                    offsetValue = note.getOffset();
                    if (note.getPan() != pan) {
                        pan = note.getPan();
                        addEvent(midiEvents, startTime + offsetValue, new CChange((short) 10, (short) (pan * 127), (short) inst.getChannel(), 0));
                    }
                    int pitch = 0;
                    pitch = note.getPitch();
                    if (pitch != REST) {
                        addEvent(midiEvents, startTime + offsetValue, new NoteOn((short) pitch, (short) note.getDynamic(), (short) inst.getChannel(), 0));
                        double endTime = startTime + (note.getDuration() * phraseTempoMultiplier);
                        addEvent(midiEvents, endTime + offsetValue, new NoteOn((short) pitch, (short) 0, (short) inst.getChannel(), 0));
                    }
                    PO.p("st = " + startTime + "pitch = " + pitch);
                    startTime += tickRounder(note.getRhythmValue() * phraseTempoMultiplier);
                    System.out.print(".");
                }
            }
            Object[] keys = midiEvents.keySet().toArray();
            Arrays.sort(keys);
            double st = 0.0;
            double sortStart;
            int time;
            resetTicker();
            for (int index = 0; index < keys.length; index++) {
                Event[] evs = (Event[]) midiEvents.get(keys[index]);
                sortStart = ((Double) keys[index]).doubleValue();
                time = (int) (((((sortStart - st) * (double) smf.getPPQN())) * partTempoMultiplier) + 0.5);
                st = sortStart;
                PO.p("time = " + time);
                evs[0].setTime(time);
                smfTrack.addEvent(evs[0]);
                for (int i = 1; i < evs.length; i++) {
                    evs[i].setTime(0);
                    if (evs[i] instanceof NoteOn && ((NoteOn) evs[i]).getVelocity() > 0) PO.p("    ev " + i + " = " + ((NoteOn) evs[i]).getPitch());
                    smfTrack.addEvent(evs[i]);
                }
            }
            smfTrack.addEvent(new EndTrack());
            smf.getTrackList().addElement(smfTrack);
            System.out.println();
        }
    }

    private static void addEvent(HashMap hm, double d, Event e) {
        Double k = new Double(d);
        if (hm.containsKey(k)) {
            Object ob = hm.get(k);
            if (!(ob instanceof Event[])) {
                PO.p("!!!! not event [], ob type = " + ob.getClass().getName());
            }
            Event[] evs = (Event[]) hm.get(k);
            Event[] nevs = new Event[evs.length + 1];
            for (int i = 0; i < evs.length; i++) {
                nevs[i] = evs[i];
            }
            nevs[nevs.length - 1] = e;
            hm.put(k, nevs);
        } else {
            Event[] nevs = { e };
            hm.put(k, nevs);
        }
    }

    private static boolean zeroVelEventQ(Event e) {
        if (e.getID() == 5) {
            if (((NoteOn) e).getVelocity() == 0) return true;
        }
        return false;
    }

    private static double tickRemainder = 0.0;

    private static void resetTicker() {
        tickRemainder = 0.0;
    }

    /**
	 * We need to call this any time we calculate unusual time values,
	 * to prevent time creep due to the MIDI tick roundoff error.
	 * This method wriiten by Bob Lee.
	 */
    private static double tickRounder(double timeValue) {
        final double tick = 1. / 480.;
        final double halfTick = 1. / 960.;
        int ticks = (int) (timeValue * 480.);
        double rounded = ((double) ticks) * tick;
        tickRemainder += timeValue - rounded;
        if (tickRemainder > halfTick) {
            rounded += tick;
            tickRemainder -= tick;
        }
        return rounded;
    }
}
