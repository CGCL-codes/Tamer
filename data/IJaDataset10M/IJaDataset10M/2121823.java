package org.jfonia.musicxml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.jfonia.musicxml.model.Attributes;
import org.jfonia.musicxml.model.BarLine;
import org.jfonia.musicxml.model.Clef;
import org.jfonia.musicxml.model.Creator;
import org.jfonia.musicxml.model.Degree;
import org.jfonia.musicxml.model.Direction;
import org.jfonia.musicxml.model.DirectionType;
import org.jfonia.musicxml.model.Harmony;
import org.jfonia.musicxml.model.Key;
import org.jfonia.musicxml.model.Lyric;
import org.jfonia.musicxml.model.Measure;
import org.jfonia.musicxml.model.Note;
import org.jfonia.musicxml.model.Part;
import org.jfonia.musicxml.model.PartContext;
import org.jfonia.musicxml.model.Pitch;
import org.jfonia.musicxml.model.PitchClass;
import org.jfonia.musicxml.model.ScoreHeader;
import org.jfonia.musicxml.model.ScorePartwise;
import org.jfonia.musicxml.model.Time;
import org.jfonia.musicxml.model.TimeModification;
import org.jfonia.musicxml.model.Tuplet;
import org.jfonia.musicxml.model.Value;
import org.jfonia.util.Logger;
import org.jfonia.util.Math;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author Wijnand
 * 
 */
public class MusicXMLImporter {

    private Logger<Throwable> logger = new Logger<Throwable>();

    private Logger<Throwable> internalLogger = new Logger<Throwable>();

    private HarmonyParser harmonyParser = null;

    public HarmonyParser getHarmonyParser() {
        return harmonyParser;
    }

    public void setHarmonyParser(HarmonyParser hp) {
        this.harmonyParser = hp;
    }

    public Logger<Throwable> getInternalLogger() {
        return internalLogger;
    }

    public void setInternalLogger(Logger<Throwable> internalLogger) {
        this.internalLogger = internalLogger;
    }

    public Logger<Throwable> getLogger() {
        return logger;
    }

    public void setLogger(Logger<Throwable> logger) {
        this.logger = logger;
    }

    public ScorePartwise readScore(SimpleDOMParser builder, InputStream inputStream, String voicePartId, String harmonyPartId) throws IOException, SAXException, MusicXMLImportException {
        try {
            builder.parse(new InputSource(inputStream));
            Document doc = builder.getDocument();
            Node scorePartwiseNode = doc.getDocumentElement();
            if (!scorePartwiseNode.getNodeName().equals("score-partwise")) throw logger.log(new CodedMusicXMLImportException("SCORE", "", scorePartwiseNode));
            return parseScorePartwise(scorePartwiseNode, voicePartId, harmonyPartId);
        } catch (IOException e) {
            throw logger.log(e);
        } catch (SAXException e) {
            throw logger.log(e);
        }
    }

    protected ScorePartwise parseScorePartwise(Node scorePartwiseNode, String voicePartId, String harmonyPartId) throws CodedMusicXMLImportException {
        ScorePartwise score = new ScorePartwise(scorePartwiseNode);
        score.setScoreHeader(parseScoreHeader(scorePartwiseNode));
        HashMap<String, Part> partMap = new HashMap<String, Part>();
        NodeList scoreChildren = scorePartwiseNode.getChildNodes();
        for (int i = 0; i < scoreChildren.getLength(); i++) {
            Node childNode = scoreChildren.item(i);
            if (childNode.getNodeName().equals("part")) {
                String id = SimpleDOMParser.findAttribute(childNode, "id");
                if (id == null) throw logger.log(new CodedMusicXMLImportException("PART_ID_MISSING", "", childNode));
                partMap.put(id, parsePart(childNode, id, score.getScoreHeader().isStopTiesOmitted()));
            }
        }
        score.setPartMap(partMap);
        Part voicePart = null;
        if (voicePartId != null) {
            voicePart = partMap.get(voicePartId);
            if (voicePart == null) throw logger.log(new CodedMusicXMLImportException("PART_MISSING", voicePartId, null)); else if (voicePart.getPartContext().isContainsBackupOrForward()) throw logger.log(new CodedMusicXMLImportException("BACKUP_OR_FORWARD", voicePartId, null));
        } else {
            voicePart = searchVoicePart(partMap);
            if (voicePart == null) logger.log(new CodedMusicXMLImportException("MELODY_NOT_FOUND", "", null)); else logger.log(new CodedMusicXMLImportException("MELODY_USING", voicePart.getId(), null));
        }
        score.setVoicePart(voicePart);
        if (voicePart != null && voicePart.getPartContext().isContainsChords()) logger.log(new CodedMusicXMLImportException("CHORD", "", null));
        Part harmonyPart = null;
        if (harmonyPartId != null) {
            harmonyPart = partMap.get(harmonyPartId);
            if (harmonyPart == null) throw logger.log(new CodedMusicXMLImportException("PART_MISSING", harmonyPartId, null));
        } else {
            harmonyPart = searchHarmonyPart(partMap);
            if (harmonyPart == null) logger.log(new CodedMusicXMLImportException("HARMONYPART_NOT_FOUND", "", null));
        }
        score.setHarmonyPart(harmonyPart);
        return score;
    }

    /**
	 * search first part which contains notes but no backup/forward and no chords 
	 * if no such found, search first part which contains notes but no backup/forward (but possibly chords)
	 * if no such part found, return null 
	 */
    protected Part searchVoicePart(Map<String, Part> partMap) {
        for (Part part : partMap.values()) {
            PartContext pc = part.getPartContext();
            if (!pc.isContainsNotes()) logger.log(new CodedMusicXMLImportException("MISSING_NOTES", part.getId(), null)); else if (pc.isContainsChords()) logger.log(new CodedMusicXMLImportException("CHORD", part.getId(), null)); else return part;
        }
        for (Part part : partMap.values()) {
            PartContext pc = part.getPartContext();
            if (pc.isContainsNotes()) return part;
        }
        return null;
    }

    /**
	 * search first part which contains notes and no chords. return null if no such part found.
	 */
    protected Part searchHarmonyPart(Map<String, Part> partMap) {
        for (Part part : partMap.values()) if (part.getPartContext().getLastHarmony() != null) return part;
        return null;
    }

    protected ScoreHeader parseScoreHeader(Node scorePartwiseNode) {
        ScoreHeader scoreHeader = new ScoreHeader(scorePartwiseNode);
        Node workNode = SimpleDOMParser.findChild(scorePartwiseNode, "work");
        if (workNode != null) {
            Node workNumberNode = SimpleDOMParser.findChild(workNode, "work-number");
            if (workNumberNode != null) scoreHeader.setWorkNumber(workNumberNode.getTextContent());
            Node workTitleNode = SimpleDOMParser.findChild(workNode, "work-title");
            if (workTitleNode != null) scoreHeader.setWorkTitle(workTitleNode.getTextContent());
        }
        Node movementNumberNode = SimpleDOMParser.findChild(scorePartwiseNode, "movement-number");
        if (movementNumberNode != null) scoreHeader.setMovementNumber(movementNumberNode.getTextContent());
        Node movementTitleNode = SimpleDOMParser.findChild(scorePartwiseNode, "movement-title");
        if (movementTitleNode != null) scoreHeader.setMovementTitle(movementTitleNode.getTextContent());
        Node identificationNode = SimpleDOMParser.findChild(scorePartwiseNode, "identification");
        if (identificationNode != null) {
            Node sourceNode = SimpleDOMParser.findChild(identificationNode, "source");
            if (sourceNode != null) scoreHeader.setSource(sourceNode.getTextContent());
            Node rightsNode = SimpleDOMParser.findChild(identificationNode, "rights");
            if (rightsNode != null) scoreHeader.setRights(rightsNode.getTextContent());
            NodeList idChildren = identificationNode.getChildNodes();
            for (int i = 0; i < idChildren.getLength(); i++) {
                Node childNode = idChildren.item(i);
                if (childNode.getNodeName().equals("creator")) {
                    String name = childNode.getTextContent();
                    String type = SimpleDOMParser.findAttribute(childNode, "type");
                    if ("composer".equals(type)) scoreHeader.addComposer(name); else if ("poet".equals(type)) scoreHeader.addPoet(name); else scoreHeader.addCreator(new Creator(type, name));
                }
            }
            for (int i = 0; i < idChildren.getLength(); i++) {
                Node childNode = idChildren.item(i);
                if (childNode.getNodeName().equals("encoding")) {
                    NodeList encodingChildren = childNode.getChildNodes();
                    for (int j = 0; j < encodingChildren.getLength(); j++) {
                        Node childNode2 = encodingChildren.item(j);
                        if (childNode2.getNodeName().equals("software")) {
                            String software = childNode2.getTextContent();
                            if (software != null && software.indexOf("Dolet") >= 0 && software.indexOf("Sibelius") >= 0) scoreHeader.setStopTiesOmitted(true);
                        }
                    }
                }
            }
        }
        return scoreHeader;
    }

    protected Part parsePart(Node partNode, String id, boolean stopTiesOmitted) throws CodedMusicXMLImportException {
        Part part = new Part(partNode);
        PartContext partContext = new PartContext();
        part.setPartContext(partContext);
        part.setId(id);
        NodeList partChildren = partNode.getChildNodes();
        for (int pci = 0; pci < partChildren.getLength(); pci++) {
            Node partChildNode = partChildren.item(pci);
            if (partChildNode.getNodeName().equals("measure")) part.addMeasure(parseMeasure(partChildNode, partContext, stopTiesOmitted));
        }
        List<Measure> measureList = part.getMeasureList();
        for (int mi = 1; mi < measureList.size(); mi++) {
            BarLine bla = measureList.get(mi - 1).getLeftBarLine();
            BarLine blb = measureList.get(mi).getLeftBarLine();
            if (bla != null && bla.isBackward() && blb != null && blb.isForward()) {
                bla.setBackward(false);
                blb.setBackward(true);
            }
        }
        part.setDivisions(partContext.getDivisions());
        part.setLyricsNumbers(partContext.getLyricsNumbers());
        return part;
    }

    protected Measure parseMeasure(Node measureNode, PartContext partContext, boolean stopTiesOmitted) throws CodedMusicXMLImportException {
        Measure measure = new Measure(measureNode);
        boolean isFirst = (partContext.getLastMeasure() == null);
        partContext.setLastMeasure(measure);
        measure.setDuration(0);
        measure.setRemainingDuration(partContext.getExpectedMeasureDuration());
        if (isFirst) {
            measure.addElement(Attributes.getDefault());
        }
        Note lastNote = null;
        NodeList measureChildren = measureNode.getChildNodes();
        for (int mci = 0; mci < measureChildren.getLength(); mci++) {
            Node measureChildNode = measureChildren.item(mci);
            if (measureChildNode.getNodeName().equals("print")) {
                String sNewSystem = SimpleDOMParser.findAttribute(measureChildNode, "new-system");
                if (sNewSystem != null && sNewSystem.compareTo("yes") == 0) {
                    measure.setNewSystem(true);
                }
            } else if (measureChildNode.getNodeName().equals("attributes")) {
                Attributes attr = parseAttributes(measureChildNode, partContext);
                if (isFirst && measure.getElements().size() == 1) {
                    measure.getElements().set(0, attr);
                } else measure.addElement(attr);
            } else if (measureChildNode.getNodeName().equals("direction")) {
                Direction d = parseDirection(measureChildNode, partContext);
                if (harmonyParser == null) throw new RuntimeException("HarmonyParser not set");
                DirectionType directionType = d.getDirectionType();
                if (directionType != null) {
                    Harmony h = harmonyParser.parse(directionType.getWords(), measureChildNode);
                    if (h != null) {
                        h.setStartTime(partContext.getPartDuration());
                        partContext.updateHarmonyDuration();
                        if (partContext.getLastHarmony() != null) partContext.getLastHarmony().setDuration(partContext.getPartDuration() - partContext.getLastHarmony().getStartTime());
                        partContext.setLastHarmony(h);
                        measure.addElement(partContext.getLastHarmony());
                    } else measure.addElement(d);
                }
            } else if (measureChildNode.getNodeName().equals("note")) {
                Note note = parseNote(measureChildNode, partContext, stopTiesOmitted);
                lastNote = note;
                if (note != null && note.getVoice() == 1) measure.addElement(note);
            } else if (measureChildNode.getNodeName().equals("harmony")) {
                partContext.updateHarmonyDuration();
                if (partContext.getLastHarmony() != null) partContext.getLastHarmony().setDuration(partContext.getPartDuration() - partContext.getLastHarmony().getStartTime());
                Harmony h = parseHarmony(measureChildNode, partContext);
                h.setStartTime(partContext.getPartDuration());
                partContext.setLastHarmony(h);
                measure.addElement(partContext.getLastHarmony());
            } else if (measureChildNode.getNodeName().equals("barline")) {
                BarLine barLine = parseBarLine(measureChildNode, partContext);
                if ("left".equals(barLine.getLocation())) {
                    if (measure.getLeftBarLine() != null) logger.log(new CodedMusicXMLImportException("BAR_MULTIPLE_LEFT", "", measureNode)); else measure.setLeftBarLine(barLine);
                }
                if ("right".equals(barLine.getLocation())) {
                    if (measure.getRightBarLine() != null) logger.log(new CodedMusicXMLImportException("BAR_MULTIPLE_RIGHT", "", measureNode)); else measure.setRightBarLine(barLine);
                }
            } else if (measureChildNode.getNodeName().equals("backup")) partContext.setContainsBackupOrForward(true);
        }
        if (partContext.getLastHarmony() != null) partContext.getLastHarmony().setDuration(partContext.getPartDuration() - partContext.getLastHarmony().getStartTime());
        if (lastNote != null && lastNote.getTuplet() != null && lastNote.getTuplet().getType() != null) {
            lastNote.getTuplet().setType("stop");
        }
        return measure;
    }

    protected Attributes parseAttributes(Node attributesNode, PartContext partContext) throws CodedMusicXMLImportException {
        Attributes attributes = new Attributes(attributesNode);
        Node divisionsNode = SimpleDOMParser.findChild(attributesNode, "divisions");
        if (divisionsNode != null) {
            if (partContext.getDivisions() != 0) logger.log(new CodedMusicXMLImportException("DIV_CHANGE", "", divisionsNode)); else {
                attributes.setDivisions(SimpleDOMParser.parseTextContentToInt(divisionsNode));
                partContext.setDivisions(attributes.getDivisions());
            }
        }
        Node clefNode = SimpleDOMParser.findChild(attributesNode, "clef");
        if (clefNode != null) attributes.setClef(parseClef(clefNode, partContext));
        Node keyNode = SimpleDOMParser.findChild(attributesNode, "key");
        if (keyNode != null) attributes.setKey(parseKey(keyNode, partContext));
        Node timeNode = SimpleDOMParser.findChild(attributesNode, "time");
        if (timeNode != null) attributes.setTime(parseTime(timeNode, partContext));
        return attributes;
    }

    protected Clef parseClef(Node clefNode, PartContext partContext) throws CodedMusicXMLImportException {
        Clef clef = new Clef(clefNode);
        Node signNode = SimpleDOMParser.findChild(clefNode, "sign");
        if (signNode == null) throw logger.log(new CodedMusicXMLImportException("CLEF_MISSING_SIGN", "", clefNode)); else {
            String sign = signNode.getTextContent();
            if (!sign.equals("G") && !sign.equals("F")) logger.log(new CodedMusicXMLImportException("CLEF_SIGN_UNKNOWN", sign, signNode));
            clef.setSign(sign);
        }
        return clef;
    }

    protected Key parseKey(Node keyNode, PartContext partContext) throws CodedMusicXMLImportException {
        Key key = new Key(keyNode);
        Node fifthsNode = SimpleDOMParser.findChild(keyNode, "fifths");
        if (fifthsNode == null) throw logger.log(new CodedMusicXMLImportException("KEY_MISSING_FIFTHS", "key should contain fifths", keyNode)); else key.setFifths(SimpleDOMParser.parseTextContentToInt(fifthsNode));
        return key;
    }

    protected Time parseTime(Node timeNode, PartContext partContext) throws CodedMusicXMLImportException {
        int beats = 0;
        int beatType = 0;
        Node beatsNode = SimpleDOMParser.findChild(timeNode, "beats");
        if (beatsNode == null) throw logger.log(new CodedMusicXMLImportException("TIME_MISSING_BEATS", "", timeNode)); else beats = SimpleDOMParser.parseTextContentToInt(beatsNode);
        Node beatTypeNode = SimpleDOMParser.findChild(timeNode, "beat-type");
        if (beatTypeNode == null) throw logger.log(new CodedMusicXMLImportException("TIME_MISSING_BEATTYPE", "", timeNode)); else beatType = SimpleDOMParser.parseTextContentToInt(beatTypeNode);
        int h = partContext.getDivisions() * beats * 4;
        if (h % beatType != 0) throw internalLogger.log(new CodedMusicXMLImportException("TIME_PROBLEM", "" + h + "," + beatType, beatTypeNode));
        partContext.setExpectedMeasureDuration(h / beatType);
        partContext.getLastMeasure().setRemainingDuration(partContext.getExpectedMeasureDuration() - partContext.getLastMeasure().getDuration());
        return new Time(timeNode, beats, beatType);
    }

    protected Direction parseDirection(Node directionNode, PartContext partContext) throws CodedMusicXMLImportException {
        Direction direction = new Direction(directionNode);
        Node directionTypeNode = SimpleDOMParser.findChild(directionNode, "direction-type");
        if (directionTypeNode != null) {
            direction.setDirectionType(parseDirectionType(directionTypeNode, partContext));
        }
        return direction;
    }

    protected DirectionType parseDirectionType(Node directionTypeNode, PartContext partContext) {
        DirectionType dirType = new DirectionType(directionTypeNode);
        Node wordsNode = SimpleDOMParser.findChild(directionTypeNode, "words");
        if (wordsNode != null) {
            dirType.setWords(wordsNode.getTextContent());
            if (dirType.getWords().length() == 0) logger.log(new CodedMusicXMLImportException("DIRECTION_TYPE_WORDS_EMPTY", "", wordsNode));
        }
        Node rehearsalNode = SimpleDOMParser.findChild(directionTypeNode, "rehearsal");
        if (rehearsalNode != null) {
            dirType.setRehearsal(rehearsalNode.getTextContent());
            logger.log(new CodedMusicXMLImportException("REHEARSAL_MARKS", "", wordsNode));
        }
        Node codaNode = SimpleDOMParser.findChild(directionTypeNode, "coda");
        if (codaNode != null) dirType.setCoda(true);
        Node segnoNode = SimpleDOMParser.findChild(directionTypeNode, "segno");
        if (segnoNode != null) dirType.setSegno(true);
        return dirType;
    }

    protected BarLine parseBarLine(Node barLineNode, PartContext partContext) throws CodedMusicXMLImportException {
        BarLine barLine = new BarLine(barLineNode);
        String location = SimpleDOMParser.findAttribute(barLineNode, "location");
        if (location == null) throw logger.log(new CodedMusicXMLImportException("BAR_MISSING_LOCATION", "", barLineNode));
        barLine.setLocation(location);
        Node barStyleNode = SimpleDOMParser.findChild(barLineNode, "bar-style");
        if (barStyleNode != null) barLine.setBarStyle(barStyleNode.getTextContent());
        String repeatDirection = null;
        Node repeatNode = SimpleDOMParser.findChild(barLineNode, "repeat");
        if (repeatNode != null) {
            repeatDirection = SimpleDOMParser.findAttribute(repeatNode, "direction");
            if (repeatDirection == null) throw logger.log(new CodedMusicXMLImportException("BAR_REPEAT_MISSING_DIRECTION", "", repeatNode));
            if (repeatDirection.equals("forward")) barLine.setForward(true); else if (repeatDirection.equals("backward")) barLine.setBackward(true); else logger.log(new CodedMusicXMLImportException("BAR_REPEAT_DIRECTION_UNKNOWN", repeatDirection, repeatNode));
        }
        Node endingNode = SimpleDOMParser.findChild(barLineNode, "ending");
        if (endingNode != null) {
            String endingType = SimpleDOMParser.findAttribute(endingNode, "type");
            if (endingType == null) throw logger.log(new CodedMusicXMLImportException("BAR_ENDING_MISSING_TYPE", "", endingNode));
            barLine.setEndingType(endingType);
            if (!("start".equals(endingType) || "stop".equals(endingType) || "discontinue".equals(endingType))) logger.log(new CodedMusicXMLImportException("BAR_ENDING_TYPE_UNKNOWN", endingType, endingNode));
            String endingNumber = SimpleDOMParser.findAttribute(endingNode, "number");
            if (endingNumber == null) throw logger.log(new CodedMusicXMLImportException("BAR_ENDING_MISSING_NUMBER", "", endingNode));
            barLine.setEndingNumber(endingNumber);
        }
        return barLine;
    }

    /**
	 * note: startTime and duration not set here
	 */
    protected Harmony parseHarmony(Node harmonyNode, PartContext partContext) throws CodedMusicXMLImportException {
        Harmony harmony = new Harmony(harmonyNode);
        String kind = "";
        ArrayList<Degree> alterationList = new ArrayList<Degree>();
        Node kindNode = SimpleDOMParser.findChild(harmonyNode, "kind");
        if (kindNode != null) {
            kind = kindNode.getTextContent().trim();
        }
        if (kind.equals("none")) {
            harmony.setChordType(null);
        } else {
            Node rootNode = SimpleDOMParser.findChild(harmonyNode, "root");
            if (rootNode == null) throw logger.log(new CodedMusicXMLImportException("HARMONY_MISSING_ROOT", "", harmonyNode)); else harmony.setRootPitchClass(parsePitchClass(rootNode, "root-step", "root-alter"));
            Node bassNode = SimpleDOMParser.findChild(harmonyNode, "bass");
            if (bassNode != null) harmony.setBassPitchClass(parsePitchClass(bassNode, "bass-step", "bass-alter"));
            NodeList children = harmonyNode.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                Node childNode = children.item(i);
                if (childNode.getNodeName().equals("degree")) alterationList.add(parseDegree(childNode, partContext));
            }
            if (harmonyParser == null) throw new RuntimeException("HarmonyParser not set in Harmony");
            harmony.setChordType(harmonyParser.determineType(kind, alterationList));
        }
        return harmony;
    }

    protected Degree parseDegree(Node degreeNode, PartContext partContext) throws CodedMusicXMLImportException {
        Degree degree = new Degree(degreeNode);
        Node degreeValueNode = SimpleDOMParser.findChild(degreeNode, "degree-value");
        if (degreeValueNode == null) throw logger.log(new CodedMusicXMLImportException("ALT_DEGREE_MISSING_VALUE", "", degreeNode));
        degree.setDegreeValue(SimpleDOMParser.parseTextContentToInt(degreeValueNode));
        Node degreeAlterNode = SimpleDOMParser.findChild(degreeNode, "degree-alter");
        int degreeAlter;
        if (degreeAlterNode == null) degreeAlter = 0; else {
            degreeAlter = SimpleDOMParser.parseTextContentToInt(degreeAlterNode);
            if (degreeAlter < -1 || degreeAlter > 1) logger.log(new CodedMusicXMLImportException("ALT_DOUBLE", degreeAlterNode.getTextContent(), degreeAlterNode));
        }
        degree.setDegreeAlter(degreeAlter);
        Node degreeTypeNode = SimpleDOMParser.findChild(degreeNode, "degree-type");
        if (degreeTypeNode == null) throw logger.log(new CodedMusicXMLImportException("ALT_DEGREE_MISSING_TYPE", "", degreeNode));
        String degreeType = degreeTypeNode.getTextContent();
        if (!"alter".equals(degreeType) && !"add".equals(degreeType) && !"subtract".equals(degreeType)) throw logger.log(new CodedMusicXMLImportException("ALT_DEGREE_TYPE_UNKNOWN", degreeType, degreeNode));
        degree.setDegreeType(degreeType);
        return degree;
    }

    protected Note parseNote(Node noteNode, PartContext partContext, boolean stopTiesOmitted) throws CodedMusicXMLImportException {
        Note note = new Note(noteNode);
        if (partContext.getDivisions() == 0) throw logger.log(new CodedMusicXMLImportException("DIV_NOT_SET", "", noteNode));
        if (partContext.getExpectedMeasureDuration() == -1) throw logger.log(new CodedMusicXMLImportException("TIMESIG_NOT_SET", "", noteNode));
        partContext.setContainsNotes(true);
        if (SimpleDOMParser.findChild(noteNode, "chord") != null) {
            partContext.setContainsChords(true);
            return null;
        }
        if (SimpleDOMParser.findChild(noteNode, "grace") != null) {
            if (!partContext.isContainsGraceNotes()) logger.log(new CodedMusicXMLImportException("GRACENOTE", "", noteNode));
            partContext.setContainsGraceNotes(true);
            return null;
        }
        NodeList noteChildren = noteNode.getChildNodes();
        if (SimpleDOMParser.findChild(noteNode, "rest") != null) note.setPitch(null); else {
            Node pitchNode = SimpleDOMParser.findChild(noteNode, "pitch");
            if (pitchNode == null) throw logger.log(new CodedMusicXMLImportException("NOTE_MISSING_PITCH", "", noteNode)); else note.setPitch(parsePitch(pitchNode));
        }
        Node timeModificationNode = SimpleDOMParser.findChild(noteNode, "time-modification");
        if (timeModificationNode != null) note.setTimeModification(parseTimeModification(timeModificationNode));
        Node notationsNode = SimpleDOMParser.findChild(noteNode, "notations");
        if (notationsNode != null) {
            Node tupletNode = tupletNode = SimpleDOMParser.findChild(notationsNode, "tuplet");
            if (tupletNode != null) note.setTuplet(parseTuplet(tupletNode));
        }
        Node durationNode = SimpleDOMParser.findChild(noteNode, "duration");
        if (durationNode == null) throw logger.log(new CodedMusicXMLImportException("NOTE_MISSING_DURATION", "", noteNode));
        int duration = SimpleDOMParser.parseTextContentToInt(durationNode);
        note.setDuration(duration);
        partContext.setPartDuration(partContext.getPartDuration() + duration);
        assert (partContext.getLastMeasure() != null);
        partContext.getLastMeasure().setDuration(partContext.getLastMeasure().getDuration() + duration);
        partContext.getLastMeasure().setRemainingDuration(partContext.getLastMeasure().getRemainingDuration() - duration);
        note.setValue(parseValue(noteNode));
        boolean tieStart = false;
        boolean tieStop = false;
        if (stopTiesOmitted && partContext.isTiedToPrev()) tieStop = true;
        for (int nci = 0; nci < noteChildren.getLength(); nci++) {
            if (noteChildren.item(nci).getNodeName().equals("tie")) {
                Node tieNode = noteChildren.item(nci);
                String tieType = SimpleDOMParser.findAttribute(tieNode, "type");
                if (tieType == null) throw logger.log(new CodedMusicXMLImportException("NOTE_TIE_MISSING_TYPE", "", tieNode));
                if (tieType.equals("start")) tieStart = true; else if (tieType.equals("stop")) tieStop = true; else throw logger.log(new CodedMusicXMLImportException("NOTE_TIE_TYPE_UNKNOWN", tieType, tieNode));
            }
        }
        note.setTieStart(tieStart);
        note.setTieStop(tieStop);
        if (notationsNode != null) {
            NodeList notationsChildren = notationsNode.getChildNodes();
            for (int i = 0; i < notationsChildren.getLength(); i++) {
                Node childNode = notationsChildren.item(i);
                if (childNode.getNodeName().equals("slur")) {
                    String slurType = SimpleDOMParser.findAttribute(childNode, "type");
                    if (slurType == null) throw logger.log(new CodedMusicXMLImportException("NOTE_SLUR_MISSING_TYPE", "", childNode));
                    if ("start".equals(slurType)) tieStart = true; else if ("stop".equals(slurType)) tieStop = true; else throw logger.log(new CodedMusicXMLImportException("NOTE_SLUR_TYPE_UNKNOWN", slurType, childNode));
                } else if (childNode.getNodeName().equals("tied")) {
                    String tieType = SimpleDOMParser.findAttribute(childNode, "type");
                    if (tieType == null) throw logger.log(new CodedMusicXMLImportException("NOTE_TIE_MISSING_TYPE", "", childNode));
                    if (tieType.equals("start")) tieStart = true; else if (tieType.equals("stop")) tieStop = true; else throw logger.log(new CodedMusicXMLImportException("NOTE_TIE_TYPE_UNKNOWN", tieType, childNode));
                }
            }
            note.setTieStart(tieStart);
            note.setTieStop(tieStop);
        }
        note.setLyrics(new TreeMap<Integer, Lyric>());
        for (int nci = 0; nci < noteChildren.getLength(); nci++) {
            if (noteChildren.item(nci).getNodeName().equals("lyric")) {
                Node lyricNode = noteChildren.item(nci);
                Lyric lyric = parseLyric(lyricNode);
                int num = lyric.getNumber();
                partContext.getLyricsNumbers().add(num);
                note.getLyrics().put(num, lyric);
            }
        }
        if (tieStart) partContext.setTiedToPrev(true); else if (tieStop) partContext.setTiedToPrev(false);
        Node voiceNode = SimpleDOMParser.findChild(noteNode, "voice");
        int voice;
        if (voiceNode == null) voice = 1; else voice = SimpleDOMParser.parseTextContentToInt(voiceNode);
        note.setVoice(voice);
        if (voice > 1 && note.getPitch() != null) {
            if (!partContext.isContainsSecondVoiceWithNotes()) logger.log(new CodedMusicXMLImportException("NOTE_SECOND_VOICE", "", voiceNode));
            partContext.setContainsSecondVoiceWithNotes(true);
        }
        return note;
    }

    protected PitchClass parsePitchClass(Node pitchNode, String stepName, String alterName) throws CodedMusicXMLImportException {
        String step = "";
        int alter = 0;
        Node stepNode = SimpleDOMParser.findChild(pitchNode, stepName);
        if (stepNode == null) throw logger.log(new CodedMusicXMLImportException("PITCH_MISSING_STEP", stepName, pitchNode));
        step = stepNode.getTextContent().toUpperCase();
        if (step.length() != 1) throw logger.log(new CodedMusicXMLImportException("PITCH_STEP_NOT_CHAR", "", pitchNode));
        if ("ABCDEFGH".indexOf(step.charAt(0)) == -1) throw logger.log(new CodedMusicXMLImportException("PITCH_STEP_UNKOWN", step, pitchNode));
        Node alterNode = SimpleDOMParser.findChild(pitchNode, alterName);
        if (alterNode != null) alter = SimpleDOMParser.parseTextContentToInt(alterNode);
        return new PitchClass(pitchNode, step, alter);
    }

    protected Pitch parsePitch(Node pitchNode) throws CodedMusicXMLImportException {
        PitchClass pitchClass = parsePitchClass(pitchNode, "step", "alter");
        int octave = 0;
        Node octaveNode = SimpleDOMParser.findChild(pitchNode, "octave");
        if (octaveNode == null) throw logger.log(new CodedMusicXMLImportException("PITCH_MISSING_OCTAVE", "", pitchNode)); else octave = SimpleDOMParser.parseTextContentToInt(octaveNode);
        return new Pitch(pitchNode, pitchClass, octave);
    }

    protected Value parseValue(Node noteNode) throws CodedMusicXMLImportException {
        int numDots = 0;
        NodeList noteChildren = noteNode.getChildNodes();
        for (int nci = 0; nci < noteChildren.getLength(); nci++) {
            if (noteChildren.item(nci).getNodeName().equals("dot")) {
                numDots++;
            }
        }
        String type = null;
        Node typeNode = SimpleDOMParser.findChild(noteNode, "type");
        if (typeNode != null) {
            type = typeNode.getTextContent();
            if (type == null) throw logger.log(new CodedMusicXMLImportException("NOTE_TYPE_EMPTY", "", typeNode));
        } else {
            if (numDots > 0) throw logger.log(new CodedMusicXMLImportException("DOT_WITHOUT_TYPE", "", noteNode));
            return null;
        }
        return new Value(noteNode, type, numDots);
    }

    protected TimeModification parseTimeModification(Node timeModificationNode) throws CodedMusicXMLImportException {
        int actualNotes = 1;
        int normalNotes = 1;
        Node actualNotesNode = SimpleDOMParser.findChild(timeModificationNode, "actual-notes");
        if (actualNotesNode == null) throw logger.log(new CodedMusicXMLImportException("TUPLET_TIMEMOD_MISSING_ACTUAL", "time-modification should contain actual-notes", timeModificationNode)); else actualNotes = SimpleDOMParser.parseTextContentToInt(actualNotesNode);
        Node normalNotesNode = SimpleDOMParser.findChild(timeModificationNode, "normal-notes");
        if (normalNotesNode == null) throw logger.log(new CodedMusicXMLImportException("TUPLET_TIMEMOD_MISSING_NORMAL", "time-modification should contain normal-notes", timeModificationNode)); else normalNotes = SimpleDOMParser.parseTextContentToInt(normalNotesNode);
        return new TimeModification(timeModificationNode, actualNotes, normalNotes);
    }

    protected Tuplet parseTuplet(Node tupletNode) throws CodedMusicXMLImportException {
        String type = SimpleDOMParser.findAttribute(tupletNode, "type");
        if (type == null) throw logger.log(new CodedMusicXMLImportException("TUPLET_MISSING_TYPE", "", tupletNode));
        if (!type.equals("start") && !type.equals("stop")) throw logger.log(new CodedMusicXMLImportException("TUPLET_TYPE_UNKNOWN", type, tupletNode));
        return new Tuplet(tupletNode, type);
    }

    protected Lyric parseLyric(Node lyricNode) throws CodedMusicXMLImportException {
        Lyric lyric = new Lyric(lyricNode);
        String numberS = SimpleDOMParser.findAttribute(lyricNode, "number");
        if (numberS != null) lyric.setNumber(SimpleDOMParser.parseToInt(numberS, lyricNode)); else lyric.setNumber(1);
        Node textNode = SimpleDOMParser.findChild(lyricNode, "text");
        if (textNode == null) throw logger.log(new CodedMusicXMLImportException("NOTE_LYRIC_MISSING_TEXT", "", lyricNode));
        lyric.setText(textNode.getTextContent());
        Node syllabicNode = SimpleDOMParser.findChild(lyricNode, "syllabic");
        if (syllabicNode != null) lyric.setSyllabic(syllabicNode.getTextContent());
        return lyric;
    }
}
