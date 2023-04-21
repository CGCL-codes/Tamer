package tei.cr.filters;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Map;
import java.util.HashMap;
import org.xml.sax.SAXException;
import tei.cr.pipeline.AbstractBase;
import tei.cr.pipeline.FilterByNames;
import tei.cr.pipeline.WrongArgsException;
import tei.cr.querydoc.FilterArguments;
import tei.cr.teiDocument.TeiDocument;

/**
 * Convert the content received in Syntext text format.
 *
 * @author Sylvain Loiseau &lt;sloiseau@u-paris10.fr&gt;
 *
 * @see <a href="http://www.irit.fr/RFIEC/syntex/">Syntex</a>.
 */
public final class SyntexFormat extends AbstractBase {

    private String syntexTextUri = null;

    private Writer syntexTextWriter;

    /**
     * Collecting the characters() callbacks content.
     */
    private StringBuffer syntexLineBuffer;

    /**
     * Namming the text through a {@link ExtractLocation}
     * filter (optionnal).
     */
    private ExtractLocation textNames;

    private boolean useTEILocator = false;

    /**
     * Index of the texts, starting
     * at <code>1</code>
     */
    private int textIndex = 0;

    /**
     * check uniqueness of the names provided throught
     * {@link #textNames}
     */
    private Map noDuplicateTextNames = new HashMap();

    private Logger log = Logger.getLogger(getClass().getName());

    /** 
     * Don't use <code>System.getProperty("line.separator")</code> as
     * line separator, since the DTM software is for Windows only.
     */
    private static final String CR = "\r\n";

    private static final String SPACE = " ";

    private static final int TEXT_NAME_MAX_LENGTH = 20;

    private static final int TEXT_LINE_MAX_LENGTH = 160;

    private static final Pattern BLANKS = Pattern.compile("\\s+");

    private static final Pattern NL = Pattern.compile("(\\s|\n|\r\n)+");

    public void setArguments(FilterArguments fA, FilterByNames nH, TeiDocument doc) throws WrongArgsException {
        String uri = fA.getTextNotNullOrEmpty(FilterArguments.SYNTEX_FORMAT_URI);
        setSyntexTextUri(uri);
        String locatorHandlerName = fA.getText(FilterArguments.SYNTEX_FORMAT_TEILOCATOR);
        if (locatorHandlerName != null) {
            ExtractLocation l = (ExtractLocation) nH.get(locatorHandlerName);
            if (l == null) {
                throw new WrongArgsException("No filter with name \"" + locatorHandlerName + "\" found in the pipeline");
            }
            setTextNames(l);
        }
    }

    /**
     * The uri for creating the "DTM type 1" text file.
     *
     * @param uri an URI.
     */
    public void setSyntexTextUri(String uri) {
        this.syntexTextUri = uri;
    }

    /**
     * A {@link ExtractLocation} for namming the texts in the file
     * created. This argument is optionnal.
     *
     * @param locator An instance of the <code>TEILocator</code>
     * interface.
     */
    public void setTextNames(ExtractLocation locator) {
        this.textNames = locator;
        this.useTEILocator = true;
    }

    /**
     * Create the file.
     */
    public void startPipeline() throws FilterException {
        syntexLineBuffer = new StringBuffer(TEXT_LINE_MAX_LENGTH + 20);
        try {
            syntexTextWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(syntexTextUri), "ISO-8859-1"));
        } catch (IOException e) {
            throw new FilterException("Error while creating the file: " + e.getMessage(), e);
        }
        super.startPipeline();
    }

    /**
     * Beginnig of new text in the DTM file.
     */
    public void startDocument() throws SAXException {
        textIndex++;
        String textName;
        if (useTEILocator) {
            textName = textNames.getReference();
            if (textName.length() > TEXT_NAME_MAX_LENGTH) {
                log.warning("A name with length longer than " + TEXT_NAME_MAX_LENGTH + " will be truncated.");
                textName = textName.substring(0, TEXT_NAME_MAX_LENGTH);
            }
            if (noDuplicateTextNames.containsKey(textName)) {
                throw new FilterException("Duplicate text name: " + textName);
            }
            noDuplicateTextNames.put(textName, null);
        } else {
            textName = String.valueOf(textIndex);
        }
        write(CR);
        write("<#");
        write(textName);
        write(">");
        write(CR);
        super.startDocument();
    }

    /**
     * Insert content in the text file, managing line length.
     */
    public void characters(char[] buf, int offset, int len) throws SAXException {
        if (syntexLineBuffer.length() > TEXT_LINE_MAX_LENGTH) {
            throw new IllegalStateException("The lines cannot be greater than " + TEXT_LINE_MAX_LENGTH + " characters. Unable to split the line: " + syntexLineBuffer.toString());
        }
        syntexLineBuffer.append(buf, offset, len);
        if ((syntexLineBuffer.length()) > TEXT_LINE_MAX_LENGTH) {
            String bufferised = syntexLineBuffer.toString();
            Matcher m = BLANKS.matcher(bufferised);
            int index = 0;
            int lastIndex = 0;
            while (m.find()) {
                lastIndex = index;
                index = m.start();
                if (index > TEXT_LINE_MAX_LENGTH) {
                    if (lastIndex == 0) {
                        throw new IllegalStateException("Unable to split: no blank before " + index + " ; content : " + bufferised);
                    }
                    flushLine(bufferised.substring(0, lastIndex));
                    bufferised = bufferised.substring(lastIndex);
                    m = BLANKS.matcher(bufferised);
                    index = 0;
                    lastIndex = 0;
                }
            }
            if (bufferised.length() > TEXT_LINE_MAX_LENGTH) {
                if (lastIndex != 0) {
                    flushLine(bufferised.substring(0, lastIndex));
                    bufferised = bufferised.substring(lastIndex);
                } else {
                    log.severe("LastIndex before error: " + lastIndex);
                    throw new IllegalStateException("The buffer cannot be greater than " + TEXT_LINE_MAX_LENGTH + " characters. Length: " + bufferised.length() + ". Unsplittable string: " + bufferised);
                }
            }
            syntexLineBuffer = new StringBuffer(bufferised);
        }
        super.characters(buf, offset, len);
    }

    public void ignorableWhitespace(char[] ch, int start, int len) throws SAXException {
        characters(ch, start, len);
    }

    /**
     * End of a text in the DTM file.
     */
    public void endDocument() throws SAXException {
        flushLine(syntexLineBuffer.toString());
    }

    /**
     * End of the DTM file.
     */
    public void endPipeline() throws FilterException {
        flushLine(syntexLineBuffer.toString());
        flush();
        super.endPipeline();
    }

    private void flushLine(String line) throws FilterException {
        line = NL.matcher(line).replaceAll(SPACE);
        write(line);
        write(CR);
        syntexLineBuffer = new StringBuffer();
    }

    private void write(String text) throws FilterException {
        try {
            syntexTextWriter.write(text);
        } catch (IOException ioE) {
            throw new FilterException("Error while writing into \"" + syntexTextUri + "\": " + ioE.getMessage(), ioE);
        }
    }

    private void flush() throws FilterException {
        try {
            syntexTextWriter.flush();
        } catch (IOException ioE) {
            throw new FilterException("Error while flushing \"" + syntexTextUri + "\": " + ioE.getMessage(), ioE);
        }
    }
}
