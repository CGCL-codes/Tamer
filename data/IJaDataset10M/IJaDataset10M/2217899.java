package nz.ac.waikato.mcennis.rat.parser.xmlHandler;

import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.parser.ParsedObject;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.actor.ActorFactory;
import org.xml.sax.Locator;

/**

 * Class that parses artist info from LastFM. It provides either null or an actor object

 * depending on whether or not the actor existed in the LastFM database.  If there are fewer

 * than 3 similar artists, it is assumed that the artist name is noise and returns null.

 * 

 *

 * @author Daniel McEnnis

 * 

 */
public class LastFmArtistHandler extends Handler {

    Actor artist = null;

    public Vector<String> artistList = new Vector<String>();

    int similarArtistCount = 0;

    int state = 0;

    public static final int NAME = 1;

    public static final int START = 0;

    private Locator locator;

    /** Creates a new instance of ArtistHandler */
    public LastFmArtistHandler() {
        super();
        properties.get("ParserClass").add("LastFmArtistHandler");
        properties.get("Name").add("LastFmArtistHandler");
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        try {
            if (localName.equalsIgnoreCase("artist")) {
                similarArtistCount++;
            } else if (localName.equalsIgnoreCase("name")) {
                state = NAME;
            }
        } catch (NullPointerException ex) {
            Logger.getLogger(LastFmArtistHandler.class.getName()).log(Level.SEVERE, "NullPointerException at line " + locator.getLineNumber());
        }
    }

    @Override
    public void startDocument() throws SAXException {
        try {
            artistList.clear();
            artist = ActorFactory.newInstance().create("Artist", "");
        } catch (NullPointerException ex) {
            Logger.getLogger(LastFmArtistHandler.class.getName()).log(Level.SEVERE, "NullPointerException at line " + locator.getLineNumber());
        }
    }

    @Override
    public void endDocument() throws SAXException {
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        try {
            if (localName.equalsIgnoreCase("name")) {
                state = START;
            }
        } catch (NullPointerException ex) {
            Logger.getLogger(LastFmArtistHandler.class.getName()).log(Level.SEVERE, "Null Pointer Exception at line " + locator.getLineNumber());
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        try {
            if (state == NAME) {
                artistList.add(new String(ch, start, length));
            }
        } catch (NullPointerException ex) {
            Logger.getLogger(LastFmArtistHandler.class.getName()).log(Level.SEVERE, "Null Pointer Exception at line " + locator.getLineNumber());
        }
    }

    @Override
    public ParsedObject get() {
        if (similarArtistCount <= 3) {
            artist = null;
        }
        return artist;
    }

    @Override
    public Handler duplicate() {
        return new LastFmArtistHandler();
    }

    @Override
    public void set(ParsedObject o) {
        if (o instanceof Actor) {
            artist = (Actor) o;
        }
    }

    @Override
    public void setDocumentLocator(Locator locator) {
        this.locator = locator;
    }
}
