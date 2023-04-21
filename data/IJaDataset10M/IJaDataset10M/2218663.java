package org.jsresources.apps.esemiso.data;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;
import org.jsresources.apps.jmvp.manager.FileModelPersistenceHandler;
import org.jsresources.apps.esemiso.Debug;
import org.jsresources.apps.esemiso.Version;
import org.jsresources.apps.esemiso.data.Instrument.PitchType;
import org.jsresources.apps.esemiso.data.Instrument.StrokeType;
import org.jsresources.apps.esemiso.util.XmlException;

/**
 * Persistence handler for RhythmModel.
 * 
 * This class handles saving and loading a rhythm in the MusicXML format.
 *
 * @see RhythmModel
 */
public class XmlPersistenceHandler implements FileModelPersistenceHandler<RhythmModel> {

    /**
	 * If true, the system identifier of the MsicXML DTDs is modified to 
	 * point to a local version.
	 * 
	 * @see RhythmHandler.resolveEntity
	 */
    private static final boolean MAP_DTDS_TO_LOCAL = true;

    public void write(RhythmModel rhythm, File file) throws Exception {
        Writer writer = new BufferedWriter(new FileWriter(file));
        writeXml(rhythm, writer);
        writer.close();
    }

    /** Writes a rhythm as MusicXML.
	 *
	 * @param r the rhythm model
	 * @param writer the output destination
	 *
	 * @throws IOException if an I/O error occures
	 */
    private static void writeXml(RhythmModel r, Writer writer) throws IOException {
        writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n");
        writer.write("<!DOCTYPE score-partwise PUBLIC \"-//Recordare/DTD MusicXML 1.1 Partwise//EN\" \"http://www.musicxml.org/dtds/partwise.dtd\">\n");
        writer.write("<score-partwise version=\"1.1\">\n");
        writeIdentification(r, writer);
        writeDefaults(r, writer);
        writePartList(r, writer);
        writeParts(r, writer);
        writer.write("</score-partwise>\n");
    }

    /**
	 * Writes MusicXML identification for a rhythm.
	 *
	 * This includes information about the encoder and the instrument set.
	 *
	 * @param r the rhythm model
	 * @param writer the output destination
	 *
	 * @throws IOException if an I/O error occures
	 */
    private static void writeIdentification(RhythmModel r, Writer writer) throws IOException {
        writer.write("  <identification>\n");
        writer.write("    <encoding>\n");
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = format.format(new Date());
        writer.write("      <encoding-date>" + strDate + "</encoding-date>\n");
        writer.write("      <software>" + Version.PROGRAM_NAME + " " + Version.VERSION + "</software>\n");
        writer.write("    </encoding>\n");
        writer.write("    <miscellaneous>\n");
        writer.write("      <miscellaneous-field name=\"instrument-set\">" + r.getInstrumentSet().getName() + "</miscellaneous-field>\n");
        writer.write("    </miscellaneous>\n");
        writer.write("  </identification>\n");
    }

    /**
	 * Writes MusicXML defaults for a rhythm.
	 *
	 * This includes information about the encoder and the instrument set.
	 *
	 * @param r the rhythm model
	 * @param writer the output destination
	 *
	 * @throws IOException if an I/O error occures
	 */
    private static void writeDefaults(RhythmModel r, Writer writer) throws IOException {
        writer.write("  <defaults>\n");
        writer.write("    <page-layout>\n");
        writer.write("      <page-height>" + r.getPageHeight() + "</page-height>\n");
        writer.write("      <page-width>" + r.getPageWidth() + "</page-width>\n");
        writer.write("      <page-margins type=\"both\">\n");
        writer.write("        <left-margin>" + r.getLeftMargin() + "</left-margin>\n");
        writer.write("        <right-margin>" + r.getRightMargin() + "</right-margin>\n");
        writer.write("        <top-margin>" + r.getTopMargin() + "</top-margin>\n");
        writer.write("        <bottom-margin>" + r.getBottomMargin() + "</bottom-margin>\n");
        writer.write("      </page-margins>\n");
        writer.write("    </page-layout>\n");
        writer.write("  </defaults>\n");
    }

    /** Writes MusicXML part list for a rhythm.
	 *
	 * @param r the rhythm model
	 * @param writer the output destination
	 *
	 * @throws IOException if an I/O error occures
	 */
    private static void writePartList(RhythmModel r, Writer writer) throws IOException {
        writer.write("  <part-list>\n");
        for (int nTrack = 0; nTrack < r.getTrackCount(); nTrack++) {
            writer.write("    <score-part id=\"" + getPartId(nTrack) + "\">\n");
            writer.write("      <part-name>" + r.getTrackTitle(nTrack) + "</part-name>\n");
            Instrument instrument = r.getTrackInstrument(nTrack);
            if (instrument != null) {
                writer.write("      <score-instrument id=\"" + getInstrumentId(nTrack) + "\">\n");
                writer.write("        <instrument-name>" + instrument.getName() + "</instrument-name>\n");
                writer.write("      </score-instrument>\n");
            }
            writer.write("    </score-part>\n");
        }
        writer.write("  </part-list>\n");
    }

    /** Writes MusicXML parts for a rhythm.
	 *
	 * @param r the rhythm model
	 * @param writer the output destination
	 *
	 * @throws IOException if an I/O error occures
	 */
    private static void writeParts(RhythmModel r, Writer writer) throws IOException {
        boolean[] bTempoWritten = new boolean[1];
        bTempoWritten[0] = false;
        for (int nTrack = 0; nTrack < r.getTrackCount(); nTrack++) {
            writer.write("  <part id=\"" + getPartId(nTrack) + "\">\n");
            for (int nMeasure = 0; nMeasure < r.getMeasureCount(); nMeasure++) {
                writeMeasure(r, nTrack, nMeasure, bTempoWritten, writer);
            }
            writer.write("  </part>\n");
        }
    }

    /** Writes a measure of a rhythm as MusicXML.
	 *
	 * @param r the rhythm model
	 * @param nTrack the track of the measure to write
	 * @param nMeasure the measure number of the measure to write
	 * @param writer the output destination
	 *
	 * @throws IOException if an I/O error occures
	 */
    private static void writeMeasure(RhythmModel r, int nTrack, int nMeasure, boolean[] bTempoWritten, Writer writer) throws IOException {
        int nDivisions = r.getMeasureTickCount(nMeasure) / 4;
        writer.write("    <measure number=\"" + (nMeasure + 1) + "\">\n");
        if (needAttributes(r, nMeasure)) {
            writer.write("      <attributes>\n");
            if (needTime(r, nMeasure)) {
                writer.write("        <divisions>" + nDivisions + "</divisions>\n");
                writer.write("        <time>\n");
                writer.write("          <beats>" + 4 + "</beats>\n");
                writer.write("          <beat-type>" + 4 + "</beat-type>\n");
                writer.write("        </time>\n");
            }
            if (isFirst(nMeasure)) {
                writer.write("        <clef>\n");
                writer.write("          <sign>Percussion</sign>\n");
                writer.write("        </clef>\n");
                writer.write("        <staff-details>\n");
                writer.write("          <staff-lines>1</staff-lines>\n");
                writer.write("        </staff-details>\n");
            }
            writer.write("      </attributes>\n");
        }
        if (!bTempoWritten[0]) {
            writer.write("      <sound tempo=\"" + r.getTempo() + "\"/>\n");
            bTempoWritten[0] = true;
        }
        int nTick = 0;
        while (nTick < r.getMeasureTickCount(nMeasure)) {
            writer.write("      <note>\n");
            PitchType pitchType = r.getCellPitchType(nTrack, nMeasure, nTick);
            StrokeType strokeType = r.getCellStrokeType(nTrack, nMeasure, nTick);
            if (strokeType != null) {
                writer.write("        <unpitched>\n");
                String strDisplayStep;
                switch(pitchType) {
                    case HIGH:
                        strDisplayStep = "A";
                        break;
                    case MIDDLE:
                    case DEFAULT:
                        strDisplayStep = "G";
                        break;
                    case LOW:
                        strDisplayStep = "F";
                        break;
                    default:
                        strDisplayStep = "X";
                        break;
                }
                writer.write("          <display-step>" + strDisplayStep + "</display-step>\n");
                writer.write("          <display-octave>5</display-octave>\n");
                writer.write("        </unpitched>\n");
                writer.write("        <duration>1</duration>\n");
                nTick++;
            } else {
                int nRestStartTick = nTick;
                while (nTick < r.getMeasureTickCount(nMeasure) && r.getCellStrokeType(nTrack, nMeasure, nTick) == null) {
                    nTick++;
                }
                int nDuration = nTick - nRestStartTick;
                writer.write("        <rest/>\n");
                writer.write("        <duration>" + nDuration + "</duration>\n");
            }
            writer.write("      </note>\n");
        }
        writer.write("    </measure>\n");
    }

    private static String getPartId(int nTrack) {
        return "P" + nTrack;
    }

    private static String getInstrumentId(int nTrack) {
        return "I" + nTrack;
    }

    private static boolean isFirst(int nMeasure) {
        return nMeasure == 0;
    }

    private static boolean needAttributes(RhythmModel r, int nMeasure) {
        return isFirst(nMeasure) || needTime(r, nMeasure);
    }

    private static boolean needTime(RhythmModel r, int nMeasure) {
        return isFirst(nMeasure);
    }

    /** Reads a MusicXML file and create a rhythm model from it.
	 *
	 * @param file the file to read
	 * @return the rhythm model
	 * @throws IOException if an I/O error occures
	 */
    public RhythmModel read(File file) throws Exception {
        RhythmModel rhythm = readXml(file);
        return rhythm;
    }

    private static RhythmModel readXml(File file) throws Exception {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(true);
            SAXParser parser = factory.newSAXParser();
            RhythmHandler handler = new RhythmHandler();
            parser.parse(file, handler);
            RhythmModel rhythm = handler.getRhythmModel();
            return rhythm;
        } catch (SAXParseException e) {
            throw new XmlException(e);
        }
    }

    /**
	 * SAX 2 event handler for reading rhythm files into
	 * RhythmModel objects.
	 */
    private static class RhythmHandler extends DefaultHandler {

        /**
		 * Elements that use character mode.
		 */
        private enum CharMode {

            PART_NAME("part-name"), INSTRUMENT_NAME("instrument-name"), DIVISIONS("divisions"), BEATS("beats"), BEAT_TYPE("beat-type"), SIGN("sign"), DISPLAY_STEP("display-step"), DISPLAY_OCTAVE("display-octave"), DURATION("duration"), /**
			 * This is handled specially by MiscellaneousFieldElementHandler.
			 */
            MISCELLANEOUS_FIELD(null);

            private String m_strElementName;

            private CharMode(String strElementName) {
                m_strElementName = strElementName;
            }

            public String getElementName() {
                return m_strElementName;
            }
        }

        /**
		 * The SAX2 locator object used in this parsing.
		 */
        private Locator m_locator;

        /**
		 * The element handlers used.
		 * 
		 * <p>This map maps element names to element handler instances.</p>
		 * 
		 * @see #addElementHandler(ElementHandler)
		 */
        private Map<String, ElementHandler> m_elementHandlers;

        /** The RhythmModel constructed while reading.
		 */
        private RhythmModel m_rhythm;

        /** List of part ids.
		 *
		 * This list holds the ids of part elements in the order in
		 * which they appear in the part-list.
		 */
        private List<String> m_partIds;

        /** Map of part ids to Instrument instances.
		 *
		 * The part ids used as keys in this map are the same that are
		 * members of m_partIds.
		 */
        private Map<String, Instrument> m_partIdToInstrument;

        /** Map of part ids to track numbers.
		 *
		 * The part ids used as keys in this map are the same that are
		 * members of m_partIds.
		 */
        private Map<String, Integer> m_partIdToTrackNumber;

        private InstrumentSet m_instrumentSet;

        private Instrument m_instrument;

        private String m_strPartId;

        private String m_strPartName;

        private String m_strInstrumentName;

        private int m_nDivisions;

        private int m_nBeats;

        private int m_nBeatType;

        private int m_nOctave;

        private int m_nDuration;

        /**
		 * The current character mode.
		 * 
		 * May be null if character mode is off.
		 */
        private CharMode m_charMode;

        private String m_strCharacters;

        private String m_strMiscellaneousFieldName;

        private String m_strMiscellaneousFieldValue;

        private int m_nCurrentTrack;

        private int m_nCurrentMeasure;

        private int m_nCurrentTick;

        /** Flag for "real" note.
		 *
		 * This value is true for "real" (sounding) notes and false
		 * for rests.
		 */
        private boolean m_bNote;

        /** Constructor.
		 */
        public RhythmHandler() {
            m_charMode = null;
            m_elementHandlers = new HashMap<String, ElementHandler>();
            addElementHandler(new AttributesElementHandler());
            addElementHandler(new MeasureElementHandler());
            addElementHandler(new MiscellaneousFieldElementHandler());
            addElementHandler(new NoteElementHandler());
            addElementHandler(new PartElementHandler());
            addElementHandler(new PartListElementHandler());
            addElementHandler(new RestElementHandler());
            addElementHandler(new ScoreInstrumentElementHandler());
            addElementHandler(new ScorePartElementHandler());
            addElementHandler(new ScorePartwiseElementHandler());
            addElementHandler(new ScoreTimewiseElementHandler());
            addElementHandler(new SoundElementHandler());
            addElementHandler(new UnpitchedElementHandler());
        }

        /**
		 * Registers an ElementHandler.
		 *
		 * @param handler the element handler to register
		 */
        private void addElementHandler(ElementHandler handler) {
            m_elementHandlers.put(handler.getElementName(), handler);
        }

        public void startElement(String uri, String localName, String qName, Attributes attributes) {
            ElementHandler handler = m_elementHandlers.get(qName);
            if (handler != null) {
                handler.startElement(attributes);
            } else {
                for (CharMode charMode : CharMode.values()) {
                    if (qName.equals(charMode.getElementName())) {
                        m_charMode = charMode;
                        m_strCharacters = "";
                        break;
                    }
                }
            }
        }

        public void endElement(String uri, String localName, String qName) {
            ElementHandler handler = m_elementHandlers.get(qName);
            if (handler != null) {
                handler.endElement();
            } else {
                for (CharMode charMode : CharMode.values()) {
                    if (qName.equals(charMode.getElementName())) {
                        handleCharacterMode();
                        m_charMode = null;
                        m_strCharacters = null;
                        break;
                    }
                }
            }
        }

        private void handleCharacterMode() {
            switch(m_charMode) {
                case PART_NAME:
                    m_strPartName = m_strCharacters;
                    break;
                case INSTRUMENT_NAME:
                    m_strInstrumentName = m_strCharacters;
                    break;
                case DIVISIONS:
                    m_nDivisions = Integer.parseInt(m_strCharacters);
                    break;
                case BEATS:
                    m_nBeats = Integer.parseInt(m_strCharacters);
                    break;
                case BEAT_TYPE:
                    m_nBeatType = Integer.parseInt(m_strCharacters);
                    break;
                case SIGN:
                    break;
                case DISPLAY_STEP:
                    break;
                case DISPLAY_OCTAVE:
                    m_nOctave = Integer.parseInt(m_strCharacters);
                    break;
                case DURATION:
                    m_nDuration = Integer.parseInt(m_strCharacters);
                    break;
                case MISCELLANEOUS_FIELD:
            }
        }

        public void characters(char[] ch, int start, int length) {
            String strChars = new String(ch, start, length);
            m_strCharacters += strChars;
        }

        public InputSource resolveEntity(String publicId, String systemId) throws IOException, SAXException {
            if (MAP_DTDS_TO_LOCAL && systemId.startsWith("http://www.musicxml.org/dtds/")) {
                ClassLoader classLoader = this.getClass().getClassLoader();
                URL url = classLoader.getResource("dtd/musicxml/" + systemId.substring(29));
                InputSource source = new InputSource(url.toString());
                return source;
            } else {
                return null;
            }
        }

        public void setDocumentLocator(Locator locator) {
            m_locator = locator;
        }

        /**
		 * Overridden to throw the passed exception so that non fatal errors
		 * are propagated.
		 */
        public void error(SAXParseException e) throws SAXException {
            throw e;
        }

        /**
		 * Base class for element handlers.
		 * 
		 * <p>An element handler contains the methods to handle the opening and
		 * closing occurance of an element.</p>
		 */
        private abstract class ElementHandler {

            /**
			 * The name of the element handled by this handler.
			 * 
			 * @see #getElementName()
			 */
            private String m_strElementName;

            /**
			 * Constructor.
			 *
			 * @param strElementName the name of the element handled by this handler
			 * 
			 * @see #getElementName()
			 */
            protected ElementHandler(String strElementName) {
                m_strElementName = strElementName;
            }

            /**
			 * Obtains the name of the element handled by this handler.
			 * @return the name of the element
			 */
            public String getElementName() {
                return m_strElementName;
            }

            public void startElement(Attributes attributes) {
            }

            public void endElement() {
            }
        }

        /**
		 * Handler for the &lt;score-partwise&gt; element.
		 */
        private class ScorePartwiseElementHandler extends ElementHandler {

            public ScorePartwiseElementHandler() {
                super("score-partwise");
            }
        }

        /**
		 * Handler for the &lt;part-list&gt; element.
		 */
        private class PartListElementHandler extends ElementHandler {

            public PartListElementHandler() {
                super("part-list");
            }

            public void startElement(Attributes attributes) {
                m_partIds = new ArrayList<String>();
                m_partIdToInstrument = new HashMap<String, Instrument>();
            }

            public void endElement() {
                int nTracks = m_partIds.size();
                m_rhythm = new RhythmModel(nTracks, 0, m_instrumentSet);
                m_partIdToTrackNumber = new HashMap<String, Integer>();
                for (int nTrack = 0; nTrack < nTracks; nTrack++) {
                    String strId = m_partIds.get(nTrack);
                    m_rhythm.setTrackInstrument(nTrack, m_partIdToInstrument.get(strId));
                    m_partIdToTrackNumber.put(strId, nTrack);
                }
            }
        }

        /**
		 * Handler for the &lt;score-timewise&gt; element.
		 */
        private class ScoreTimewiseElementHandler extends ElementHandler {

            public ScoreTimewiseElementHandler() {
                super("score-timewise");
            }

            public void startElement(Attributes attributes) {
                throw new XmlException("currently, score-timewise is not supported! Please convert to score-partwise.", m_locator);
            }
        }

        /**
		 * Handler for the &lt;score-part&gt; element.
		 */
        private class ScorePartElementHandler extends ElementHandler {

            public ScorePartElementHandler() {
                super("score-part");
            }

            public void startElement(Attributes attributes) {
                m_strPartId = attributes.getValue("id");
            }

            public void endElement() {
                if (Debug.getTraceXmlParsing()) Debug.out("score-part: " + m_strPartId);
                if (Debug.getTraceXmlParsing()) Debug.out("score-part: " + m_strPartName);
                if (m_strPartId != null && m_strPartName != null) {
                    m_partIds.add(m_strPartId);
                    m_partIdToInstrument.put(m_strPartId, m_instrument);
                }
            }
        }

        /**
		 * Handler for the &lt;part&gt; element.
		 */
        private class PartElementHandler extends ElementHandler {

            public PartElementHandler() {
                super("part");
            }

            public void startElement(Attributes attributes) {
                String strPartId = attributes.getValue("id");
                m_nCurrentTrack = m_partIdToTrackNumber.get(strPartId);
                if (Debug.getTraceXmlParsing()) Debug.out("part: id: " + strPartId);
                if (Debug.getTraceXmlParsing()) Debug.out("part: track number: " + m_nCurrentTrack);
            }

            public void endElement() {
                m_nCurrentTrack = -1;
            }
        }

        /**
		 * Handler for the &lt;score-instrument&gt; element.
		 */
        private class ScoreInstrumentElementHandler extends ElementHandler {

            public ScoreInstrumentElementHandler() {
                super("score-instrument");
            }

            public void endElement() {
                if (m_strInstrumentName != null) {
                    String strInstrumentName = m_strInstrumentName.trim();
                    m_instrument = m_instrumentSet.getInstrument(strInstrumentName);
                    if (m_instrument == null) {
                        String strMessage = "instrument '" + strInstrumentName + "' not available in instrument set '" + m_instrumentSet.getName() + "'";
                        throw new XmlException(strMessage, m_locator);
                    }
                }
            }
        }

        /**
		 * Handler for the &lt;measure&gt; element.
		 */
        private class MeasureElementHandler extends ElementHandler {

            public MeasureElementHandler() {
                super("measure");
            }

            public void startElement(Attributes attributes) {
                int nMeasureNumber = Integer.parseInt(attributes.getValue("number"));
                if (Debug.getTraceXmlParsing()) Debug.out("measure: number: " + nMeasureNumber);
                while (m_rhythm.getMeasureCount() < nMeasureNumber) {
                    m_rhythm.insertMeasure(RhythmModel.AT_END);
                }
                m_nCurrentMeasure = nMeasureNumber - 1;
                m_nCurrentTick = 0;
            }

            public void endElement() {
                m_nCurrentMeasure = -1;
            }
        }

        /**
		 * Handler for the &lt;rest&gt; element.
		 */
        private class RestElementHandler extends ElementHandler {

            public RestElementHandler() {
                super("rest");
            }

            public void startElement(Attributes attributes) {
                m_bNote = false;
            }
        }

        /**
		 * Handler for the &lt;unpitched&gt; element.
		 */
        private class UnpitchedElementHandler extends ElementHandler {

            public UnpitchedElementHandler() {
                super("unpitched");
            }

            public void startElement(Attributes attributes) {
                m_bNote = true;
            }
        }

        /**
		 * Handler for the &lt;sound&gt; element.
		 */
        private class SoundElementHandler extends ElementHandler {

            public SoundElementHandler() {
                super("sound");
            }

            public void startElement(Attributes attributes) {
                String strTempo = attributes.getValue("tempo");
                if (strTempo != null) {
                    int nTempo = Integer.parseInt(strTempo);
                    m_rhythm.setTempo(nTempo);
                }
            }
        }

        /**
		 * Handler for the &lt;attributes&gt; element.
		 */
        private class AttributesElementHandler extends ElementHandler {

            public AttributesElementHandler() {
                super("attributes");
            }

            public void endElement() {
                m_rhythm.setMeasureTimeProperties(m_nCurrentMeasure, new TimeSignature(m_nBeats, m_nBeatType), m_nDivisions);
            }
        }

        /**
		 * Handler for the &lt;note&gt; element.
		 */
        private class NoteElementHandler extends ElementHandler {

            public NoteElementHandler() {
                super("note");
            }

            public void endElement() {
                if (m_bNote) {
                    Instrument instr = m_rhythm.getTrackInstrument(m_nCurrentTrack);
                    StrokeType strokeType = instr.getStrokeType("default");
                    m_rhythm.setCellPitchAndStrokeType(m_nCurrentTrack, m_nCurrentMeasure, m_nCurrentTick, PitchType.DEFAULT, strokeType);
                }
                m_nCurrentTick += m_nDuration;
            }
        }

        /**
		 * Handler for the &lt;miscellaneous-field&gt; element.
		 */
        private class MiscellaneousFieldElementHandler extends ElementHandler {

            public MiscellaneousFieldElementHandler() {
                super("miscellaneous-field");
            }

            public void startElement(Attributes attributes) {
                m_strMiscellaneousFieldName = attributes.getValue("name");
                m_charMode = CharMode.MISCELLANEOUS_FIELD;
                m_strCharacters = "";
            }

            public void endElement() {
                if ("instrument-set".equals(m_strMiscellaneousFieldName)) {
                    String strInstrumentSetName = m_strCharacters;
                    if (m_instrumentSet == null) {
                        m_instrumentSet = InstrumentSetList.getInstrumentSet(strInstrumentSetName);
                        if (m_instrumentSet == null) {
                            String strMessage = "instrument set '" + strInstrumentSetName + "' not available";
                            throw new XmlException(strMessage, m_locator);
                        }
                    } else {
                        throw new XmlException("instrument set is already specified", m_locator);
                    }
                }
                m_charMode = null;
                m_strCharacters = null;
                m_strMiscellaneousFieldName = null;
                m_strMiscellaneousFieldValue = null;
            }
        }

        /**
		 * Obtains the created rhythm model.
		 *
		 *<p>This method can be called after the parsing; then it will return
		 * the rhythm model created from the information read from the XML
		 * file.</p>
		 *
		 * @return the new rhythm model
		 */
        public RhythmModel getRhythmModel() {
            return m_rhythm;
        }
    }
}
