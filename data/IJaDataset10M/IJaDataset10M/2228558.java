package backend.parser.biopax.entity.interaction.physical.conversion;

import java.io.InputStream;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import org.codehaus.stax2.XMLStreamReader2;
import com.ctc.wstx.stax.WstxInputFactory;
import backend.core.AbstractONDEXGraph;
import backend.core.security.Session;
import backend.parser.biopax.entity.EntityParser;
import backend.parser.biopax.registers.XrefRegister;

public class InteractionParser extends EntityParser {

    public InteractionParser(WstxInputFactory factory, InputStream stream, XrefRegister xrefRegisters) {
        super(factory, stream, xrefRegisters);
    }

    @Override
    public void parseToGraph(Session s, AbstractONDEXGraph og) {
        try {
            XMLStreamReader2 reader = (XMLStreamReader2) factory.createXMLStreamReader(stream);
            String element = null;
            while (reader.hasNext()) {
                int event = reader.next();
                switch(event) {
                    case XMLStreamConstants.START_DOCUMENT:
                        break;
                    case XMLStreamConstants.START_ELEMENT:
                        element = reader.getLocalName();
                        break;
                    case XMLStreamConstants.CHARACTERS:
                        break;
                    case XMLStreamConstants.END_ELEMENT:
                        element = reader.getLocalName();
                        break;
                    case XMLStreamConstants.END_DOCUMENT:
                        break;
                }
            }
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }
}
