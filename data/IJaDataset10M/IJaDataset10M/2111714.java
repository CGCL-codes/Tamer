package org.adempiere.utils.translation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.text.ParseException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.bea.xml.stream.XMLWriterBase;

/**
 * <p>
 * Exports <code>AD*trl*.xml</code> files to intermediate files and imports
 * translations from intermediate to original template files (
 * <code>AD*trl*.xml</code> related teplates). The content of intermediate file
 * is UTF-8 encoded in the form shown bellow:
 * </p>
 * 
 * <ul style=" list-style-type: none;"> <li><b style="width='70px'">content:
 * </b><code>(&lt;line&gt;)*</code></li> <li><b style="width='70px'">line: </b>
 * <code>&lt;separator&gt;&lt;lineCount&gt;&lt;separator&gt;&lt;value&gt;</code>
 * </li> <li><b style="width='70px'">separator: </b><code>'-*-'</code></li> <li><b
 * style="width='70px'">value: </b>
 * <code>The value characters from the value tag</code></li> </ul>
 * 
 * <p>
 * In this way it is much simplier to use translation tools such <a
 * href='http://translate.google.com/'>Google Translate</a>.
 * </p>
 * 
 * @author mgifos
 * 
 */
public class IntermediateFileProvider {

    private static final Logger LOG = LoggerFactory.getLogger(IntermediateFileProvider.class);

    private long lineCounter;

    private static final String SEP_END = ">>>";

    private static final String SEP_START = "<<<";

    private static final String TAG_VALUE = "value";

    public long getLineCounter() {
        return lineCounter;
    }

    private String intermediateLine() {
        return SEP_START + lineCounter + SEP_END + " " + chars.trim();
    }

    /**
	 * Resets line counter of intermediate files generation. The next line count
	 * will be 1.
	 */
    public void resetLineCounter() {
        lineCounter = 0;
    }

    /**
	 * Exports input source <code>AD*trl*.xml</code> file to intermediate and
	 * generates <code>AD*trl*.xml</code> template at destination location.
	 * Intermediate is written down using <code>writer</code> argument.
	 * 
	 * @param src
	 *            Source file (<code>AD*trl*.xml</code>)
	 * @param dest
	 *            Destination file (<code>AD*trl*.xml</code> template)
	 * @param writer
	 *            Writer to write intermediate content down.
	 * @throws IOException
	 * @throws XMLStreamException
	 */
    public void export(final File src, final File dest, Writer writer) throws IOException, XMLStreamException {
        XMLStreamWriter xmlOut = new XMLWriterBase(new BufferedWriter(new FileWriter(dest)));
        xmlOut.writeStartDocument();
        InputStream in = new FileInputStream(src);
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader parser = factory.createXMLStreamReader(in);
        boolean valueTag = false;
        for (int event = parser.next(); event != XMLStreamConstants.END_DOCUMENT; event = parser.next()) {
            switch(event) {
                case XMLStreamConstants.START_ELEMENT:
                    valueTag = TAG_VALUE.equalsIgnoreCase(parser.getLocalName());
                    xmlOut.writeStartElement(parser.getLocalName());
                    for (int i = 0; i < parser.getAttributeCount(); i++) {
                        String localName = parser.getAttributeLocalName(i);
                        String value = parser.getAttributeValue(i);
                        xmlOut.writeAttribute(localName, value);
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if (valueTag) {
                        lineCounter++;
                        writer.write(intermediateLine() + "\n");
                        xmlOut.writeCharacters("" + lineCounter);
                    }
                    xmlOut.writeEndElement();
                    chars = "";
                    valueTag = false;
                    break;
                case XMLStreamConstants.CHARACTERS:
                    if (valueTag) chars += parser.getText(); else xmlOut.writeCharacters(parser.getText());
                    break;
                case XMLStreamConstants.CDATA:
                    xmlOut.writeCharacters(parser.getText());
                    break;
                case XMLStreamConstants.COMMENT:
                    xmlOut.writeComment(parser.getText());
                    break;
            }
        }
        parser.close();
        xmlOut.writeEndDocument();
        xmlOut.flush();
        xmlOut.close();
    }

    private String chars = "";

    public void validate(File intermediate) throws FileNotFoundException, IOException {
        LOG.info("Validation started ... ");
        BufferedReader reader = new BufferedReader(new FileReader(intermediate));
        try {
            String line = null;
            int errCount = 0;
            int prevId = -1;
            for (int lineNum = 1; (line = reader.readLine()) != null; lineNum++) {
                if (line.trim().length() == 0) {
                    LOG.error(String.format("Err: %-5s: Line: %-5s: Empty line. Line content: %s", ++errCount, lineNum, line));
                } else if (line.startsWith(SEP_START)) {
                    String sId = line.substring(line.indexOf(SEP_START) + SEP_START.length());
                    int endSepIndex = sId.indexOf(SEP_END) - SEP_END.length();
                    if (endSepIndex < 0) {
                        LOG.error(String.format("Err: %-5s: Line: %-5s: Missing second line separator. Line content: %s", ++errCount, lineNum, line));
                        break;
                    }
                    sId = sId.substring(0, endSepIndex + SEP_END.length()).trim();
                    try {
                        int id = Integer.valueOf(sId);
                        if (id <= prevId) {
                            LOG.error(String.format("Err: %-5s: Line: %-5s: Missing id: %s", ++errCount, lineNum, line));
                        }
                        prevId = id;
                    } catch (NumberFormatException e) {
                        LOG.error(String.format("Err: %-5s: Line: %-5s: wrong line id. Line content: %s", ++errCount, lineNum, line));
                    }
                }
            }
        } finally {
            reader.close();
        }
    }

    public void join(final File intermediate, final File template, final File translated) throws XMLStreamException, IOException, FileNotFoundException, ParseException {
        BufferedReader inReader = new BufferedReader(new FileReader(intermediate));
        XMLStreamWriter xmlOut = new XMLWriterBase(new BufferedWriter(new FileWriter(translated)));
        xmlOut.writeStartDocument();
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader parser = factory.createXMLStreamReader(new FileInputStream(template));
        boolean valueTag = false;
        String nextId = "";
        for (int event = parser.next(); event != XMLStreamConstants.END_DOCUMENT; event = parser.next()) {
            switch(event) {
                case XMLStreamConstants.START_ELEMENT:
                    valueTag = TAG_VALUE.equalsIgnoreCase(parser.getLocalName());
                    xmlOut.writeStartElement(parser.getLocalName());
                    for (int i = 0; i < parser.getAttributeCount(); i++) {
                        String localName = parser.getAttributeLocalName(i);
                        String value = parser.getAttributeValue(i);
                        xmlOut.writeAttribute(localName, value);
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if (valueTag) xmlOut.writeCharacters(nextTranslation(nextId, inReader));
                    xmlOut.writeEndElement();
                    nextId = "";
                    valueTag = false;
                    break;
                case XMLStreamConstants.CHARACTERS:
                    if (valueTag) nextId += parser.getText(); else xmlOut.writeCharacters(parser.getText());
                    break;
                case XMLStreamConstants.CDATA:
                    xmlOut.writeCharacters(parser.getText());
                    break;
                case XMLStreamConstants.COMMENT:
                    xmlOut.writeComment(parser.getText());
                    break;
            }
        }
        parser.close();
        xmlOut.writeEndDocument();
        xmlOut.flush();
        xmlOut.close();
    }

    private static boolean expectCh(final int expected, final Reader reader) throws IOException {
        return expected == reader.read();
    }

    private static boolean expectTag(final String tag, final Reader reader) throws IOException {
        for (int i = 0; i < tag.length(); i++) if (!expectCh(tag.charAt(i), reader)) return false;
        return true;
    }

    private static boolean expectId(final String id, final Reader reader) throws IOException {
        for (int i = 0; i < id.length(); i++) if (!expectCh(id.charAt(i), reader)) return false;
        return true;
    }

    /**
	 * @return Next translation text from the stream or null if there is end of
	 *         the stream exceeded.
	 */
    private static String nextTranslation(final String expectedId, final Reader reader) throws IOException, ParseException {
        reader.mark(1);
        if (reader.read() == -1) return null;
        reader.reset();
        if (!expectTag(SEP_START, reader)) throw new ParseException("Start tag expected in line: ", -1);
        if (!expectId(expectedId, reader)) throw new ParseException("Wrong expected ID: " + expectedId, -1);
        if (!expectTag(SEP_END, reader)) throw new ParseException("End tag expected in line: ", -1);
        StringBuilder retval = new StringBuilder(200);
        while (true) {
            reader.mark(SEP_START.length());
            final boolean startTagNext = expectTag(SEP_START, reader);
            reader.reset();
            if (startTagNext) break;
            int ch = reader.read();
            if (ch == -1) break;
            retval.append((char) ch);
        }
        return retval.toString().trim();
    }
}
