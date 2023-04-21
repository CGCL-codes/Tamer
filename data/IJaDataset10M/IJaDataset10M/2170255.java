package org.rdv.rbnb;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

/**
 * A class that extends the java Properties (@see java.util.Properties) in order to manage
 * an event marker. An event marker describes an event that has a timestamp.
 * 
 * @author Moji Soltani
 * @author Jason P. Hanley
 */
public class EventMarker extends Properties implements Comparable<EventMarker> {

    /** serialization version identifier */
    private static final long serialVersionUID = 3659100029067547637L;

    /**
   * The mime type to use for event markers
   */
    public static final String MIME_TYPE = "text/x-eventmarker";

    /**
   * Construct an event marker.
   */
    public EventMarker() {
        super();
    }

    /**
   * Populate this event with the properties from the serialized xml given. 
   * 
   * @param eventXml                           the event marker xml
   * @throws InvalidPropertiesFormatException  if there is an error parsing the
   *                                           xml
   * @throws IOException                       if there is an error reading the
   *                                           xml
   */
    public void setFromEventXml(String eventXml) throws InvalidPropertiesFormatException, IOException {
        ReaderToStream inStream = new ReaderToStream(new StringReader(eventXml));
        this.loadFromXML(inStream);
    }

    /**
   * Serializes this object to XML.
   * 
   * @return              the xml of this event marker
   * @throws IOException  if there is an error writing the xml
   */
    public String toEventXmlString() throws IOException {
        StringWriter sw = new StringWriter();
        this.storeToXML((OutputStream) (new WriterToStream(sw)), null);
        return sw.toString();
    }

    /**
   * Helper inner class to transform a StringWriter to an OutputStream 
   */
    private class WriterToStream extends OutputStream {

        StringWriter itsStringWriter;

        WriterToStream(StringWriter sw) {
            itsStringWriter = sw;
        }

        public void write(int ch) throws IOException {
            itsStringWriter.write(ch);
        }
    }

    /**
   * Helper inner class to transform a StringReader to an InputStream
   */
    private class ReaderToStream extends InputStream {

        StringReader itsStringReader;

        ReaderToStream(StringReader sw) {
            itsStringReader = sw;
        }

        public int read() throws IOException {
            return itsStringReader.read();
        }
    }

    /**
   * Compare two event markers. This will only compare their respective
   * timestamps.
   * 
   * @param marker  the marker to compare to
   */
    public int compareTo(EventMarker marker) {
        double time = Double.parseDouble(getProperty("timestamp"));
        double otherTime = Double.parseDouble(marker.getProperty("timestamp"));
        if (time < otherTime) {
            return -1;
        } else if (time > otherTime) {
            return 1;
        } else {
            return 0;
        }
    }
}
