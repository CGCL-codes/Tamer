package org.jsresources.apps.esemiso.data;

import java.io.BufferedWriter;
import java.io.Writer;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;
import org.jsresources.apps.esemiso.Debug;
import org.jsresources.apps.esemiso.data.Instrument.PitchType;
import org.jsresources.apps.esemiso.data.Instrument.StrokeType;
import org.jsresources.apps.esemiso.util.XmlException;

/** A collection of instruments.
 *
 * @author Matthias Pfisterer
 * @see Instrument
 */
public class InstrumentSet {

    /** The name of the instrument set.
	 *
	 * @see #getName()
	 */
    private String m_strName;

    /** Map of instruments.
	 *
	 * Instrument names are keys, Instrument instances are values.
	 *
	 * @see #addInstrument
	 * @see #getInstrument
	 * @see #getInstrumentNames
	 * @see #getInstruments
	 */
    private Map<String, Instrument> m_instruments;

    /** Names of all instruments in the order they were added to the set.
	 *
	 * @see #getInstrumentNames
	 */
    private List<String> m_instrumentNames;

    /** Constructor.
	 *
	 * @param strName name of the set
	 *
	 * @see #getName()
	 */
    public InstrumentSet(String strName) {
        m_strName = strName;
        m_instruments = new HashMap<String, Instrument>();
        m_instrumentNames = new ArrayList<String>();
    }

    /** Returns the name of the instrument set.
	 *
	 * @see #m_strName
	 * @see #InstrumentSet(String)
	 */
    public String getName() {
        return m_strName;
    }

    /** Add an instrument to the set.
	 *
	 * @param instrument the instrument to add
	 */
    public void addInstrument(Instrument instrument) {
        String strInstrumentName = instrument.getName();
        m_instruments.put(strInstrumentName, instrument);
        if (!m_instrumentNames.contains(strInstrumentName)) {
            m_instrumentNames.add(strInstrumentName);
        }
    }

    /** Obtains an instrument.
	 *
	 * @param strName the name of the desired instrument
	 * @return the instrument or null of no instrument for the given
	 * name is available
	 */
    public Instrument getInstrument(String strName) {
        return m_instruments.get(strName);
    }

    /** Obtains all instrument names.
	 *
	 * @return the names of all available instruments. The set may be
	 * empty if there are no instruments registered.
	 */
    public List<String> getInstrumentNames() {
        return Collections.unmodifiableList(m_instrumentNames);
    }

    /** Obtains all instruments.
	 *
	 * @return all available instruments. The collection may be empty
	 * if there are no instruments registered.
	 */
    public Collection<Instrument> getInstruments() {
        return m_instruments.values();
    }

    /** Saves the instrument set to a file.
	 *
	 * This method saves the instrument set including all its
	 * instruments as a XML description in the specified file.  The
	 * file is overwritten if it exists and created if it does not
	 * exist.
	 *
	 * @param file the File to save in
	 * @throws IOException If an I/O error occurs
	 */
    public void save(File file) throws IOException {
        Writer writer = new BufferedWriter(new FileWriter(file));
        writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
        writer.write("<!DOCTYPE instrument-set SYSTEM \"http://www.jsresources.org/dtd/esemiso/instrument-set.dtd\">\n");
        writer.write("<instrument-set name=\"" + getName() + "\">\n");
        Iterator<Instrument> it = getInstruments().iterator();
        while (it.hasNext()) {
            Instrument instrument = it.next();
            writeInstrument(writer, instrument);
        }
        writer.write("</instrument-set>\n");
        writer.close();
    }

    /** Writes the description of an instrument as XML.
	 * @param writer the Writer to write the output to
	 * @param instrument the Instrument to write out as a description
	 * @throws IOException if an I/O error occurs
	 */
    private void writeInstrument(Writer writer, Instrument instrument) throws IOException {
        Set<StrokeType> strokes = instrument.getStrokeTypes();
        EnumSet<PitchType> pitches = instrument.getPitchTypes();
        writer.write("  <instrument name=\"" + instrument.getName() + "\">\n");
        writer.write("    <pitches>\n");
        for (PitchType pitch : pitches) {
            writer.write("      <pitch name=\"" + pitch.getName() + "\"/>\n");
        }
        writer.write("    </pitches>\n");
        writer.write("    <strokes>\n");
        for (StrokeType stroke : strokes) {
            writer.write("      <stroke name=\"" + stroke.getName() + "\" notehead=\"" + stroke.getNoteheadType().getName() + "\"/>\n");
        }
        writer.write("    </strokes>\n");
        writer.write("    <keys>\n");
        for (PitchType pitch : pitches) {
            for (StrokeType stroke : strokes) {
                writer.write("      <key stroke=\"" + stroke.getName() + "\" pitch=\"" + pitch.getName() + "\" key=\"" + instrument.getKey(pitch, stroke) + "\" icontype=\"" + instrument.getIconTypeName(pitch, stroke) + "\"/>\n");
            }
        }
        writer.write("    </keys>\n");
        writer.write("  </instrument>\n\n");
    }

    /** Reads an instrument file and return an InstrumentSet from it.
	 *
	 * @param file the file to read from
	 */
    public static InstrumentSet load(File file) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(true);
            SAXParser parser = factory.newSAXParser();
            InstrumentSetHandler handler = new InstrumentSetHandler();
            parser.parse(file, handler);
            return handler.getInstrumentSet();
        } catch (SAXParseException e) {
            throw new XmlException(e);
        } catch (SAXException e) {
            Exception x = e.getException();
            ((x == null) ? e : x).printStackTrace();
            return null;
        } catch (Throwable t) {
            Debug.out(t);
            return null;
        }
    }

    /**
	 * SAX 2 event handler for reading instrumene files into
	 * InstrumentSet objects.
	 */
    private static class InstrumentSetHandler extends DefaultHandler {

        private InstrumentSet m_instrumentSet;

        private Instrument m_instrument;

        private Set<StrokeType> m_strokes;

        private EnumSet<PitchType> m_pitches;

        public void startElement(String uri, String localName, String qName, Attributes attributes) {
            if (qName.equals("instrument-set")) {
                String strName = attributes.getValue("name");
                m_instrumentSet = new InstrumentSet(strName);
            } else if (qName.equals("instrument")) {
                String strName = attributes.getValue("name");
                m_instrument = new Instrument(strName);
                m_instrumentSet.addInstrument(m_instrument);
            } else if (qName.equals("pitches")) {
                m_pitches = EnumSet.noneOf(PitchType.class);
            } else if (qName.equals("strokes")) {
                m_strokes = new HashSet<StrokeType>();
            } else if (qName.equals("pitch")) {
                String strName = attributes.getValue("name");
                PitchType type = PitchType.getPitchType(strName);
                if (type != null) {
                    m_pitches.add(type);
                }
            } else if (qName.equals("stroke")) {
                String strName = attributes.getValue("name");
                String strNoteheadName = attributes.getValue("notehead");
                NoteheadType noteheadType = NoteheadType.getNoteheadType(strNoteheadName);
                StrokeType type = new StrokeType(strName, noteheadType);
                m_strokes.add(type);
            } else if (qName.equals("key")) {
                String strValue = attributes.getValue("pitch");
                PitchType pitchType = PitchType.getPitchType(strValue);
                strValue = attributes.getValue("stroke");
                StrokeType strokeType = m_instrument.getStrokeType(strValue);
                strValue = attributes.getValue("key");
                String strIconTypeName = attributes.getValue("icontype");
                int nKey = -1;
                try {
                    nKey = Integer.parseInt(strValue);
                } catch (NumberFormatException e) {
                    Debug.out(e);
                }
                if (pitchType != null && strokeType != null) {
                    if (nKey != -1) {
                        m_instrument.setKey(pitchType, strokeType, nKey);
                    }
                    if (strIconTypeName != null) {
                        m_instrument.setIconTypeName(pitchType, strokeType, strIconTypeName);
                    }
                }
            }
        }

        public void endElement(String uri, String localName, String qName) {
            if (qName.equals("instrument")) {
                m_instrument = null;
            } else if (qName.equals("pitches")) {
                m_instrument.setPitchTypes(m_pitches);
                m_pitches = null;
            } else if (qName.equals("strokes")) {
                m_instrument.setStrokeTypes(m_strokes);
                m_strokes = null;
            }
        }

        public InputSource resolveEntity(String publicId, String systemId) throws IOException, SAXException {
            if (Debug.getTraceXmlParsing()) Debug.out("InstrumentSet.resolveEntity(): systemId: " + systemId);
            if (systemId.startsWith("http://www.jsresources.org/dtd/esemiso/")) {
                ClassLoader classLoader = this.getClass().getClassLoader();
                URL url = classLoader.getResource("dtd/" + systemId.substring(39));
                if (Debug.getTraceXmlParsing()) Debug.out("InstrumentSet.resolveEntity(): URL: " + url);
                return new InputSource(url.toString());
            } else {
                return null;
            }
        }

        /**
		 * Overridden to throw the passed exception so that validation errors
		 * handled.
		 */
        public void error(SAXParseException e) throws SAXException {
            throw e;
        }

        /** Returns the InstrumentSet created by reading the file.
		 *
		 * The result of this call only returns a valid InstrumentSet
		 * if this handler has been used to read an entire instrument
		 * file. If this method is called before the reading of the
		 * file is completed, <code>null</code> or an incomplete set
		 * may be returned.
		 *
		 * @return the created InstrumentSet
		 */
        public InstrumentSet getInstrumentSet() {
            return m_instrumentSet;
        }
    }
}
