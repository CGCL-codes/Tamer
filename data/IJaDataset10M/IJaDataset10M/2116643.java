package xml;

import context.*;
import java.io.IOException;
import java.io.Reader;
import java.util.Hashtable;

public class XMLElementReader implements Debug.Constants {

    private Reader reader;

    public interface Listener {

        public void treatXML(XMLElement elt);
    }

    public XMLElementReader(Reader reader) {
        this.reader = reader;
    }

    public void close() throws IOException {
        reader.close();
    }

    public void parse(Listener listener) throws IOException {
        if (DEBUG) Debug.log("XMLElementReader'run", "XMLElementReader -- Started");
        XMLElement currentElement = null;
        MiniXmlParser parser = new MiniXmlParser(reader);
        parser.setRelaxed(true);
        WHILE: while (true) {
            int type = parser.getNextToken();
            switch(type) {
                case MiniXmlParser.START_TAG:
                    {
                        String name = parser.getName();
                        Hashtable attributes = parser.getAttributes();
                        currentElement = new XMLElement(currentElement, name, attributes);
                    }
                    break;
                case MiniXmlParser.END_TAG:
                    {
                        String text = parser.getText();
                        if (text != null && text.length() > 0) {
                            if (currentElement != null) currentElement.addText(text);
                        }
                        if (currentElement != null) {
                            XMLElement parent = currentElement.getParent();
                            if (parent == null) {
                                if (LOG_XML) Debug.log("XMLElementReader.parse", "<<  READ: " + currentElement);
                                listener.treatXML(currentElement);
                            } else parent.addChild(currentElement);
                            currentElement = parent;
                        }
                    }
                    break;
                case MiniXmlParser.END_OF_FILE:
                    {
                        if (LOG_XML) Debug.log("XMLElementReader.parse", "<<  END OF FILE");
                        break WHILE;
                    }
            }
        }
    }
}
