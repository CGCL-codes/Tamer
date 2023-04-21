package elapse.domain;

import java.util.jar.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author mnieber
 */
public abstract class XMLHandler extends DefaultHandler {

    public abstract boolean startParsing(String uri, String name, String qName, Attributes atts);

    public abstract boolean endParsing(String uri, String name, String qName);
}
