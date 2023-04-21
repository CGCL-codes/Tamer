package ballsoffury.playground.model;

import java.util.Vector;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 * <code>LevelHandler</code> allows for management of the game. Various methods 
 * are included in this class to show various screens, alter preferences,  
 * and exit the application.
 *
 * @author Corey Oliver
 * @version $Revision:$ $Date:$
 */
public class LevelHandler extends DefaultHandler {

    private int line = 0;

    String name;

    int width, height, depth;

    Vector<String> lines = new Vector<String>();

    public LevelHandler() {
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if ("ballsoffury".equals(qName)) {
            name = attributes.getValue("name");
            width = Integer.valueOf(attributes.getValue("width"));
            height = Integer.valueOf(attributes.getValue("height"));
            depth = Integer.valueOf(attributes.getValue("layers"));
        } else if ("line".equals(qName)) {
            String data = attributes.getValue("data");
            lines.add(data);
            line++;
        }
    }
}
