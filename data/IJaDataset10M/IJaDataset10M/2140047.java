package backend.parser.biopax.entity.physicalentity;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import java.io.InputStream;
import java.util.HashMap;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import org.codehaus.stax2.XMLStreamReader2;
import backend.core.AbstractONDEXGraph;
import backend.core.security.Session;
import backend.parser.biopax.registers.XrefRegister;
import com.ctc.wstx.stax.WstxInputFactory;

public class PhysicalEntityReferences extends PhysicalEntityParser {

    private static final String BP_EREF_PHYSICAL_ENTITY = "PHYSICAL-ENTITY";

    private HashMap<String, IntOpenHashSet> physicalEntityReferenceToPhysicalEntityIDs = new HashMap<String, IntOpenHashSet>();

    public PhysicalEntityReferences(WstxInputFactory factory, InputStream stream) {
        super(factory, stream, new XrefRegister());
    }

    public PhysicalEntityReferences(WstxInputFactory factory, InputStream stream, XrefRegister xrefRegisters) {
        super(factory, stream, xrefRegisters);
    }

    public void parse() {
        try {
            XMLStreamReader2 reader = (XMLStreamReader2) factory.createXMLStreamReader(stream);
            String element = null;
            int deapth = 0;
            Integer physicalId = null;
            while (reader.hasNext()) {
                int event = reader.next();
                switch(event) {
                    case XMLStreamConstants.START_DOCUMENT:
                        break;
                    case XMLStreamConstants.START_ELEMENT:
                        element = reader.getLocalName();
                        deapth++;
                        if (element.equalsIgnoreCase(BP_E_PHYSICAL_ENTITY_PARTICIPANT)) {
                            physicalId = parsePhysicalEntityParticipantID(reader);
                            if (physicalId == 8405) {
                                System.err.println(physicalId);
                            }
                            deapth = 0;
                        }
                        if (element.equalsIgnoreCase(BP_EREF_PHYSICAL_ENTITY) && physicalId != null && deapth == 1) {
                            for (int i = 0; i < reader.getAttributeCount(); i++) {
                                String attName = reader.getAttributeName(i).getLocalPart();
                                if (attName.equalsIgnoreCase(RESOURCE)) {
                                    String reference = reader.getAttributeValue(i).substring(1);
                                    IntOpenHashSet set = physicalEntityReferenceToPhysicalEntityIDs.get(reference);
                                    if (set == null) {
                                        set = new IntOpenHashSet();
                                        physicalEntityReferenceToPhysicalEntityIDs.put(reference, set);
                                    }
                                    set.add(physicalId);
                                }
                            }
                        }
                        break;
                    case XMLStreamConstants.CHARACTERS:
                        break;
                    case XMLStreamConstants.END_ELEMENT:
                        element = reader.getLocalName();
                        deapth--;
                        if (element.equalsIgnoreCase(BP_E_PHYSICAL_ENTITY_PARTICIPANT)) {
                            physicalId = null;
                        }
                        break;
                    case XMLStreamConstants.END_DOCUMENT:
                        break;
                }
            }
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    public static Integer parsePhysicalEntityParticipantID(XMLStreamReader2 reader) {
        for (int i = 0; i < reader.getAttributeCount(); i++) {
            String attName = reader.getAttributeName(i).getLocalPart();
            if (attName.equalsIgnoreCase(ID)) {
                String value = reader.getAttributeValue(i);
                return Integer.parseInt(value.substring(BP_REF_PHYSICAL_ENTITY_PARTICIPANT.length()));
            }
        }
        return null;
    }

    public HashMap<String, IntOpenHashSet> getPhysicalEntityReferenceToPhysicalEntityIDs() {
        return physicalEntityReferenceToPhysicalEntityIDs;
    }

    @Override
    public void parseToGraph(Session s, AbstractONDEXGraph og) {
        parse();
    }
}
