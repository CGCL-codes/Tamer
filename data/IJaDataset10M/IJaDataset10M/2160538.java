package apollo.dataadapter.otter.parser;

import org.xml.sax.*;
import org.xml.sax.ext.*;
import org.xml.sax.helpers.*;
import org.xml.sax.helpers.DefaultHandler;
import apollo.datamodel.*;

/**
 * I add on the assembly start of the current sequence fragment.
**/
public class SequenceFragmentVersionTagHandler extends TagHandler {

    public void handleEndElement(OtterContentHandler theContentHandler, String namespaceURI, String localName, String qualifiedName) {
        AssemblyFeature annotation = (AssemblyFeature) theContentHandler.getStackObject();
        annotation.setVersion(Integer.valueOf(getCharacters()).intValue());
        super.handleEndElement(theContentHandler, namespaceURI, localName, qualifiedName);
    }

    public String getFullName() {
        return "otter:sequence_set:sequence_fragment:version";
    }

    public String getLeafName() {
        return "version";
    }
}
