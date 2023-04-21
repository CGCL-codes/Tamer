package base;

import java.io.File;
import java.util.Vector;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import util.Tools;

/**
 * Base parser class for XML-based playlist formats
 * 
 * @author Peter Andrews 
 * @version 1.0
 */
public class XMLPlaylistParser extends DefaultHandler {

    protected static Vector<Object> list;

    protected static String songAttrs[] = new String[3], chars, relRoot;

    protected Song tempSong;

    /**
     * Parses the file specified by the path in <code>fileName</code>. This class merely constructs
     * a new <code>file</code> object and passes it to the <code>parseFile(File)</code> method.
     * 
     * @param fileName  String that contains the path to the playlist to interpret.
     * @return A Vector containing the name and elements of the playlist, or <code>null</code> if there was an
     * error while parsing. The playlist name is in slot 0 (zero).
     * @see #parseFile(File)
     */
    public static Vector<Object> parseFile(String fileName) {
        return parseFile(new File(fileName), new XMLPlaylistParser());
    }

    /**
     * Parses the file in <code>parseMe</code>.
     * 
     * @param parseMe   A <code>File</code> object that points to the file to be parsed. No error checking is done
     * to ensure that it actually <i>is</i> a playlist, so check beforehand.
     * @param handler   The <code>DocumentHandler</code> object that should be used to interpret the data
     * @return A Vector containing the name and elements of the playlist, or <code>null</code> if there was an
     * error while parsing. The name is in slot 0 (zero).
     */
    public static Vector<Object> parseFile(File parseMe, XMLPlaylistParser handler) {
        list = new Vector<Object>(0);
        chars = Tools.getSuffix(parseMe);
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(parseMe, handler);
        } catch (Throwable t) {
            return null;
        }
        return list;
    }

    /**
     * Called when parsing begins. Nothing is done here.
     * 
     * @throws SAXException
     */
    @Override
    public void startDocument() throws SAXException {
    }

    /**
     * Called when parsing is finished. Nothing is done here.
     * 
     * @throws SAXException
     */
    @Override
    public void endDocument() throws SAXException {
    }

    /**
     * Called whenever a beginning element tag is reached (like &lt;entry&gt;). It gets the
     * name of the tag, then compares it to a list. It then performs the actions below based on what
     * the tag is.
     * <ul>
     * <li>playlist - calls <code>ensureCapacity</code> on the storage vector to make sure that there's enough
     * room for all of the items in the playlist, as well as the playlist's name. It then adds a <code>String</code>
     * object that contains the name of the playlist.</li>
     * <li>entry - gets the playstring attribute from the tag and takes out any "file:" reference at the beginning</li>
     * </ul>
     * 
     * @throws SAXException
     * @see #endElement
     */
    @Override
    public void startElement(String namespaceURI, String lName, String qName, Attributes attrs) throws SAXException {
        String eName = lName;
        if ("".equals(eName)) eName = qName;
    }

    /**
     * Called whenever an ending tag is encountered (like &lt;/entry&gt;). It gets the name of the element that
     * is being ended and performs the following actions based on that name:.
     * <ul>
     * <li>entry - constructs a new <code>Song</code> object based on the playstring gathered from the start
     * tag and adds it to the Vector.
     * <li>Name - copies the <code>chars</code> string to the songAttrs array, storing the song title. It can
     * do this because the raw text immediately preceeding this ending tag was just stored to <code>chars</code>.
     * <li>Length - copies the <code>chars</code> string to the songAttrs array, storing the song's length as
     * a String. It can do this because the raw text immediately preceeding this ending tag was just stored to <code>chars</code>.
     * </ul>
     * 
     * @throws SAXException
     * @see #characters
     */
    @Override
    public void endElement(String namespaceURI, String sName, String qName) throws SAXException {
        String eName = sName;
        if ("".equals(eName)) eName = qName;
    }

    /**
     * Called whenever raw characters are encountered (outside of an element tag). It simply turns them into a String
     * for later use (if any).
     * 
     * @throws SAXException
     */
    @Override
    public void characters(char buf[], int offset, int len) throws SAXException {
        chars = new String(buf, offset, len);
    }
}
