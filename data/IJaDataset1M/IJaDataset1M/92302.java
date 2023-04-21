package local.media;

import java.io.*;
import java.util.Hashtable;
import java.util.Enumeration;

/** MixerLine is a simple G711 mixer with M input lines (OutputStreams)
* and N output lines (InputStreams). 
* <p/>
* Each line has an identifier (Object) used as key when adding or
* removing the line. 
*/
public class Mixer {

    /** The splitter lines (as Hashtable of Object->SplitterLine). */
    Hashtable<Object, SplitterLine> m_hashSplitterLines;

    /** The mixer lines (as Hashtable of Object->MixerLine). */
    Hashtable<Object, MixerLine> m_hashMixerLines;

    /** Creates a new Mixer. */
    public Mixer() {
        m_hashSplitterLines = new Hashtable<Object, SplitterLine>();
        m_hashMixerLines = new Hashtable<Object, MixerLine>();
    }

    /** Close the Mixer. */
    public void close() throws IOException {
        for (Enumeration<SplitterLine> enumeration = m_hashSplitterLines.elements(); enumeration.hasMoreElements(); ) enumeration.nextElement().close();
        for (Enumeration<MixerLine> enumeration = m_hashMixerLines.elements(); enumeration.hasMoreElements(); ) enumeration.nextElement().close();
        m_hashMixerLines = null;
        m_hashSplitterLines = null;
    }

    /** Adds a new input line. */
    public OutputStream newInputLine(Object objectID) throws IOException {
        SplitterLine sl = new SplitterLine(objectID);
        for (Enumeration<Object> enumeration = m_hashMixerLines.keys(); enumeration.hasMoreElements(); ) {
            Object mid = enumeration.nextElement();
            if (!mid.equals(objectID)) {
                ExtendedPipedInputStream is = new ExtendedPipedInputStream();
                ExtendedPipedOutputStream os = new ExtendedPipedOutputStream(is);
                sl.addLine(mid, os);
                m_hashMixerLines.get(mid).addLine(objectID, is);
            }
        }
        m_hashSplitterLines.put(objectID, sl);
        return sl;
    }

    /** Removes a input line. */
    public void removeInputLine(Object objectID) {
        SplitterLine sl = (SplitterLine) m_hashSplitterLines.get(objectID);
        m_hashSplitterLines.remove(objectID);
        for (Enumeration<Object> enumeration = m_hashMixerLines.keys(); enumeration.hasMoreElements(); ) {
            Object mid = enumeration.nextElement();
            if (!mid.equals(objectID)) {
                sl.removeLine(mid);
                m_hashMixerLines.get(mid).removeLine(objectID);
            }
        }
        try {
            sl.close();
        } catch (Exception e) {
        }
    }

    /** Adds a new output line. */
    public InputStream newOutputLine(Object id) throws IOException {
        MixerLine ml = new MixerLine(id);
        for (Enumeration<Object> enumeration = m_hashSplitterLines.keys(); enumeration.hasMoreElements(); ) {
            Object sid = enumeration.nextElement();
            if (!sid.equals(id)) {
                ExtendedPipedInputStream is = new ExtendedPipedInputStream();
                ExtendedPipedOutputStream os = new ExtendedPipedOutputStream(is);
                ml.addLine(sid, is);
                m_hashSplitterLines.get(sid).addLine(id, os);
            }
        }
        m_hashMixerLines.put(id, ml);
        return ml;
    }

    /** Removes a output line. */
    public void removeOutputLine(Object id) {
        MixerLine ml = m_hashMixerLines.get(id);
        m_hashMixerLines.remove(id);
        for (Enumeration<Object> enumeration = m_hashSplitterLines.keys(); enumeration.hasMoreElements(); ) {
            Object sid = enumeration.nextElement();
            if (!sid.equals(id)) {
                ml.removeLine(sid);
                m_hashSplitterLines.get(sid).removeLine(id);
            }
        }
        try {
            ml.close();
        } catch (Exception e) {
        }
    }
}
