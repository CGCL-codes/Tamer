package org.jfugue.parsers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import nu.xom.Attribute;
import nu.xom.Builder;
import nu.xom.DocType;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.Node;
import nu.xom.ParsingException;
import nu.xom.ValidityException;
import org.jfugue.JFugueException;
import org.jfugue.MusicStringRenderer;
import org.jfugue.PatternInterface;
import org.jfugue.Player;
import org.jfugue.elements.Instrument;
import org.jfugue.elements.KeySignature;
import org.jfugue.elements.Measure;
import org.jfugue.elements.Note;
import org.jfugue.elements.Tempo;
import org.jfugue.elements.Voice;
import org.apache.log4j.Logger;

class XMLpart extends Object {

    public String ID;

    public String part_name;

    public String score_instruments;

    public String midi_instruments;

    public XMLpart() {
        ID = "";
        part_name = "";
        score_instruments = "";
        midi_instruments = "";
    }
}

;

/**
 * voiceDef
 * MusicString voice can be a combination of part and voice
 */
class voiceDef {

    int part;

    int voice;
}

/**
 * Parses a MusicXML file, and fires events for <code>ParserListener</code> interfaces
 * when tokens are interpreted. The <code>ParserListener</code> does intelligent things
 * with the resulting events, such as create music, draw sheet music, or
 * transform the data.
 *
 * As of Version 3.0, the Parser supports turning MIDI Sequences into JFugue Patterns with the parse(Sequence)
 * method.  In this case, the ParserListeners established by a ParserBuilder use the parsed
 * events to construct the Pattern string.
 *
 * MusicXmlParser.parse can be called with a file name, File, InputStream, or Reader
 * 
 * @author E.Philip Sobolik
 *
 */
public final class MusicXmlParser extends Parser {

    private Builder xomBuilder;

    private Document xomDoc;

    private String[] volumes = { "pppppp", "ppppp", "pppp", "ppp", "pp", "p", "mp", "mf", "f", "ff", "fff", "ffff", "fffff", "ffffff" };

    private byte minVelocity = 10;

    private byte maxVelocity = 127;

    private byte curVelocity = Note.DEFAULT_VELOCITY;

    private byte beats;

    private byte divisions;

    private int curVoice;

    private int nextVoice;

    private voiceDef[] voices;

    public MusicXmlParser() {
        xomBuilder = new Builder();
        beats = 1;
        divisions = 1;
        curVoice = -1;
        nextVoice = 0;
        voices = new voiceDef[15];
    }

    public void parse(String musicXmlString) {
        try {
            xomDoc = xomBuilder.build(musicXmlString, (String) null);
        } catch (ValidityException e) {
            e.printStackTrace();
        } catch (ParsingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        parse();
    }

    public void parse(File fileXMLin) {
        try {
            xomDoc = xomBuilder.build(fileXMLin);
        } catch (ValidityException e) {
            e.printStackTrace();
        } catch (ParsingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        parse();
    }

    public void parse(FileInputStream fisXMLin) {
        try {
            xomDoc = xomBuilder.build(fisXMLin);
        } catch (ValidityException e) {
            e.printStackTrace();
        } catch (ParsingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        parse();
    }

    public void parse(Reader rXMLin) {
        try {
            xomDoc = xomBuilder.build(rXMLin);
        } catch (ValidityException e) {
            e.printStackTrace();
        } catch (ParsingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        parse();
    }

    /** The default value for the Tempo. */
    private int tempo = 120;

    /**
     * Sets the tempo for the current song.  Tempo is measured in "pulses per quarter".
     * The parser uses this value to convert note durations, which are relative values and
     * not directly related to time measurements, into actual times.  For example, a whole
     * note has the same duration as four quarter notes, but neither a whole note nor a
     * quarter note equates to any real-life time delay until it's multplied by the tempo.
     *
     * The default value for Tempo is 120 pulses per quarter.
     *
     * @param tempo the tempo for the current song, in pulses per quarter.
     */
    protected void setTempo(int tempo) {
        this.tempo = tempo;
    }

    /**
     * Returns the tempo for the current song.
     */
    protected int getTempo() {
        return this.tempo;
    }

    /**
     * Parses a MusicXML file and fires events to subscribed <code>ParserListener</code>
     * interfaces.  As the file is parsed, events are sent
     * to <code>ParserLisener</code> interfaces, which are responsible for doing
     * something interesting with the music data, such as playing the music,
     * displaying it as sheet music, or transforming the pattern.
     *
     * the input is a XOM Document, which has been built previously
     * @throws Exception if there is an error parsing the pattern
     */
    public void parse() throws JFugueException {
        DocType docType = xomDoc.getDocType();
        Element root = xomDoc.getRootElement();
        if (docType.getRootElementName().compareToIgnoreCase("score-partwise") == 0) {
            Element partlist = root.getFirstChildElement("part-list");
            Elements parts = partlist.getChildElements();
            XMLpart[] partHeaders = new XMLpart[parts.size()];
            for (int p = 0; p < parts.size(); ++p) {
                partHeaders[p] = new XMLpart();
                parsePartHeader(parts.get(p), partHeaders[p]);
            }
            parts = root.getChildElements("part");
            for (int p = 0; p < parts.size(); ++p) {
                parsePart(p, parts.get(p), partHeaders);
            }
        }
    }

    /**
	 * Parses a <code>part</code> element in the <code>part-list</code> section
	 * @param part is the <code>part</code> element
	 * @param partHeader is the array of <code>XMLpart</code> classes that stores
	 * the <code>part-list</code> elements
	 */
    private void parsePartHeader(Element part, XMLpart partHeader) {
        if (part.getLocalName().equals("part-group")) {
            return;
        }
        Attribute ID = part.getAttribute("id");
        partHeader.ID = ID.getValue();
        Element partName = part.getFirstChildElement("part-name");
        partHeader.part_name = partName.getValue();
        int x;
        Elements scoreInsts = part.getChildElements("score-instrument");
        for (x = 0; x < scoreInsts.size(); ++x) {
            partHeader.score_instruments += scoreInsts.get(x).getValue();
            if (x < scoreInsts.size() - 1) partHeader.score_instruments += "~";
        }
        Elements midiInsts = part.getChildElements("midi-instrument");
        for (x = 0; x < midiInsts.size(); ++x) {
            Element midi_instrument = midiInsts.get(x);
            Element midi_channel = midi_instrument.getFirstChildElement("midi-channel");
            String midiChannel = (midi_channel == null) ? "" : midi_channel.getValue();
            if (midiChannel.length() > 0) {
                partHeader.midi_instruments += midiChannel;
                partHeader.midi_instruments += "|";
            }
            Element midi_inst = midi_instrument.getFirstChildElement("midi-name");
            String midiInst = (midi_inst == null) ? "" : midi_inst.getValue();
            if (midiInst.length() < 1) {
                Element midi_bank = midi_instrument.getFirstChildElement("midi-bank");
                midiInst = (midi_bank == null) ? "" : midi_bank.getValue();
                if (midiInst.length() < 1) {
                    Element midi_program = midi_instrument.getFirstChildElement("program");
                    midiInst = (midi_program == null) ? "" : midi_program.getValue();
                }
            }
            partHeader.midi_instruments += midiInst;
            if (x < midiInsts.size() - 1) partHeader.midi_instruments += "~";
        }
    }

    /**
	 * Parses a <code>part</code> and fires all the appropriate note events
	 * @param part is the entire <code>part</part>
	 * @param partHeaders is the array of XMLpart classes that contains
	 * instrument info for the <code>part</code>s
	 */
    private void parsePart(int p, Element part, XMLpart[] partHeaders) {
        for (int x = 0; x < partHeaders.length; ++x) {
            if (part.getAttribute("id").getValue().equals(partHeaders[x].ID)) {
                if (partHeaders[x].midi_instruments.length() < 1) {
                    parseVoice(p, x);
                    parsePartElementInstruments(p, partHeaders[x].part_name);
                } else {
                    parsePartElementInstruments(p, partHeaders[x].midi_instruments);
                }
            }
        }
        Elements measures = part.getChildElements("measure");
        for (int m = 0; m < measures.size(); ++m) {
            Element measure = measures.get(m);
            Element attributes = measure.getFirstChildElement("attributes");
            if (attributes != null) {
                byte key = 0, scale = 0;
                Element attr = attributes.getFirstChildElement("key");
                if (attr != null) {
                    Element eKey = attr.getFirstChildElement("fifths");
                    if (eKey != null) key = Byte.parseByte(eKey.getValue());
                    Element eMode = attr.getFirstChildElement("mode");
                    if (eMode != null) {
                        String mode = eMode.getValue();
                        if (mode.compareToIgnoreCase("major") == 0) scale = 0; else if (mode.compareToIgnoreCase("minor") == 0) scale = 1; else throw new JFugueException(JFugueException.KEYSIG_EXC, mode);
                    }
                } else scale = 0;
                fireKeySignatureEvent(new KeySignature(key, scale));
                Element element_divisions = attributes.getFirstChildElement("divisions");
                if (element_divisions != null) this.divisions = Byte.valueOf(element_divisions.getValue());
                Element element_time = attributes.getFirstChildElement("time");
                if (element_time != null) {
                    Element element_beats = element_time.getFirstChildElement("beats");
                    if (element_beats != null) this.beats = Byte.valueOf(element_beats.getValue());
                }
            }
            Element direction = measure.getFirstChildElement("direction");
            if (direction != null) {
                Element directionType = direction.getFirstChildElement("direction-type");
                if (directionType != null) {
                    Element metronome = directionType.getFirstChildElement("metronome");
                    if (metronome != null) {
                        Element beatUnit = metronome.getFirstChildElement("beat-unit");
                        String sBeatUnit = beatUnit.getValue();
                        if (sBeatUnit.compareToIgnoreCase("quarter") != 0) throw new JFugueException(JFugueException.BEAT_UNIT_MUST_BE_QUARTER, sBeatUnit);
                        Element bpm = metronome.getFirstChildElement("per-minute");
                        if (bpm != null) {
                            this.setTempo(BPMtoPPM(Float.parseFloat(bpm.getValue())));
                            fireTempoEvent(new Tempo(this.getTempo()));
                        }
                    }
                }
            }
            Elements notes = measure.getChildElements("note");
            for (int n = 0; n < notes.size(); ++n) parseNote(p, notes.get(n));
            fireMeasureEvent(new Measure());
        }
    }

    /**
	 * parses MusicXML note Element
	 * @param note is the note Element to parse
	 */
    private void parseNote(int p, Element note) {
        Note newNote = new Note();
        boolean isRest = false;
        boolean isStartOfTie = false;
        boolean isEndOfTie = false;
        byte noteNumber = 0;
        byte octaveNumber = 0;
        double decimalDuration;
        if (note.getFirstChildElement("grace") != null) return;
        Element voice = note.getFirstChildElement("voice");
        if (voice != null) parseVoice(p, Integer.parseInt(voice.getValue()));
        Element pitch = note.getFirstChildElement("pitch");
        if (pitch != null) {
            String sStep = pitch.getFirstChildElement("step").getValue();
            switch(sStep.charAt(0)) {
                case 'C':
                    noteNumber = 0;
                    break;
                case 'D':
                    noteNumber = 2;
                    break;
                case 'E':
                    noteNumber = 4;
                    break;
                case 'F':
                    noteNumber = 5;
                    break;
                case 'G':
                    noteNumber = 7;
                    break;
                case 'A':
                    noteNumber = 9;
                    break;
                case 'B':
                    noteNumber = 11;
                    break;
            }
            Element Alter = pitch.getFirstChildElement("alter");
            if (Alter != null) {
                String sAlter = Alter.getValue();
                if (sAlter != null) {
                    noteNumber += Integer.parseInt(sAlter);
                    if (noteNumber > 11) noteNumber = 0; else if (noteNumber < 0) noteNumber = 11;
                }
            }
            Element Octave = pitch.getFirstChildElement("octave");
            if (Octave != null) {
                String sOctave = Octave.getValue();
                if (sOctave != null) octaveNumber = Byte.parseByte(sOctave);
            }
            int intNoteNumber = (octaveNumber * 12) + noteNumber;
            if (intNoteNumber > 127) {
                throw new JFugueException(JFugueException.NOTE_OCTAVE_EXC, "", Integer.toString(intNoteNumber));
            }
            noteNumber = (byte) intNoteNumber;
        } else isRest = true;
        {
            Element element_duration = note.getFirstChildElement("duration");
            decimalDuration = (element_duration == null) ? beats * divisions : Double.parseDouble(element_duration.getValue()) / (beats * divisions);
        }
        double PPW = (double) this.getTempo() * 4.0;
        long duration = (long) (PPW * decimalDuration);
        Element notations = note.getFirstChildElement("notations");
        if (notations != null) {
            Element tied = notations.getFirstChildElement("tied");
            if (tied != null) {
                Attribute tiedType = tied.getAttribute("type");
                {
                    String sTiedType = tiedType.getValue();
                    if (sTiedType.compareToIgnoreCase("start") == 0) isStartOfTie = true; else if (sTiedType.compareToIgnoreCase("end") == 0) isEndOfTie = true;
                }
            }
            Element dynamics = notations.getFirstChildElement("dynamics");
            if (dynamics != null) {
                Node dynamic = dynamics.getChild(0);
                if (dynamic != null) {
                    for (int x = 0; x < this.volumes.length; ++x) {
                        if (dynamic.getValue().compareToIgnoreCase(this.volumes[x]) == 0) {
                            this.curVelocity = (byte) (((this.maxVelocity - this.minVelocity) / (this.volumes.length - 1)) * x);
                        }
                    }
                }
            }
        }
        byte attackVelocity = this.curVelocity;
        byte decayVelocity = this.curVelocity;
        if (isRest) {
            newNote.setRest(true);
            newNote.setMillisDuration(duration);
            newNote.setAttackVelocity((byte) 0);
            newNote.setDecayVelocity((byte) 0);
        } else {
            newNote.setValue(noteNumber);
            newNote.setMillisDuration(duration);
            newNote.setStartOfTie(isStartOfTie);
            newNote.setEndOfTie(isEndOfTie);
            newNote.setAttackVelocity(attackVelocity);
            newNote.setDecayVelocity(decayVelocity);
        }
        Element element_chord = note.getFirstChildElement("chord");
        newNote.setType((element_chord == null) ? Note.FIRST : Note.PARALLEL);
        newNote.setDecimalDuration(decimalDuration);
        fireNoteEvent(newNote);
    }

    /**
     * Parses a voice and fires a voice element
     * @param v is the voice number 1 - 16
     * @throws JFugueException if there is a problem parsing the element
     */
    private void parseVoice(int p, int v) throws JFugueException {
        int voiceNumber = -1;
        for (int x = 0; x < this.nextVoice; ++x) if (p == voices[x].part && v == voices[x].voice) voiceNumber = x;
        if (voiceNumber == -1) {
            voiceNumber = nextVoice;
            voices[voiceNumber] = new voiceDef();
            voices[voiceNumber].part = p;
            voices[voiceNumber].voice = v;
            ++nextVoice;
        }
        if (voiceNumber != this.curVoice) fireVoiceEvent(new Voice((byte) voiceNumber));
        curVoice = voiceNumber;
    }

    /**
     * Parses a <code>XMLpart.midi_instruments</code> and fires a voice or
     * instrument events
     * @param instruments is the <code>XMLpart.midiinstruments</code> string to parse
     * Can be a list of ~ separated pairs - midi-channel|InstName where InstName 
     * can be a midi-name, midi-bank, or program Element  
     */
    private void parsePartElementInstruments(int p, String instruments) {
        if (instruments.indexOf('~') > -1) {
            String[] instArray = instruments.split("~");
            String[] midiArray = instArray[0].split("|");
            if (midiArray.length > 0 && midiArray[0].length() > 0) parseVoice(p, Integer.parseInt(midiArray[0]) - 1);
            if (midiArray.length != 1) parseInstrument(midiArray[1]);
        } else {
            if (instruments.charAt(instruments.length() - 1) == '|') {
                instruments = instruments.substring(0, instruments.length() - 1);
            }
            parseInstrument(instruments);
        }
    }

    /**
     * parses <code>inst</code> and fires an Instrument Event
     * @param inst is a String that represents the instrument.  If it is a numeric
     * value, it is interpreted as a midi-bank or program.  If it is an instrument
     * name, it is looked up in the Dictionary as an instrument name.
     */
    private void parseInstrument(String inst) {
        byte instrumentNumber;
        try {
            instrumentNumber = Byte.parseByte(inst);
        } catch (NumberFormatException e) {
            instrumentNumber = getByteFromDictionary(inst);
        }
        Logger.getRootLogger().trace("Instrument element: inst = " + inst);
        if (instrumentNumber > -1) fireInstrumentEvent(new Instrument(instrumentNumber));
    }

    /**
     * converts beats per minute (BPM) to pulses per minute (PPM) assuming 240 pulses per second
     * In MusicXML, BPM can be fractional, so <code>BPMtoPPM</code> takes a float argument
     * @param bpm
     * @return ppm
     */
    public static int BPMtoPPM(float bpm) {
        return (new Float((60.f * 240.f) / bpm).intValue());
    }

    /**
	 ** Used for diagnostic purposes.  main() makes calls to test the Pattern-to-MusicXML
	 ** parser.    
	 ** If you make any changes to this parser, run
	 ** this method ("java org.jfugue.MusicStringParser"), and make sure everything 
	 ** works correctly.
	 ** @param args not used
	 **/
    public static void main(String[] args) {
        testMusicXmlParser();
    }

    private static void testMusicXmlParser() {
        File fileXML = new File("/users/epsobolik/documents/binchois.xml");
        try {
            FileInputStream fisXML = new FileInputStream(fileXML);
            FileChannel fc = fisXML.getChannel();
            ByteBuffer buf = ByteBuffer.allocate((int) fc.size());
            fc.read(buf);
            buf.flip();
            MusicXmlParser MusicXMLIn = new MusicXmlParser();
            MusicStringRenderer MusicStringOut = new MusicStringRenderer();
            MusicXMLIn.addParserListener(MusicStringOut);
            MusicXMLIn.parse(fileXML);
            PatternInterface p = MusicStringOut.getPattern();
            p.insert("T60");
            System.out.println(p.toString());
            System.out.print('\n');
            Player player = new Player();
            player.play(p);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
