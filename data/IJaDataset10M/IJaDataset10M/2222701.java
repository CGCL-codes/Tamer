package org.dev2live.parse.Xmi1_2_Uml1_4;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.dev2live.parse.XmiParser;
import org.dev2live.xmi.EndNode;
import org.dev2live.xmi.Node;

/**
 * @author bertram
 *
 */
public class FinalParser implements XmiParser {

    private final String name = "FinalState";

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Node parse(XMLStreamReader staxXmlReader) throws XMLStreamException {
        String id = staxXmlReader.getAttributeValue(null, "xmi.id");
        String name = staxXmlReader.getAttributeValue(null, "name");
        if ("".equals(id) || null == id || "".equals(name) || null == name) return null;
        EndNode node = new EndNode(id, name);
        ParserUtil.parseInnerParamNode(staxXmlReader, node, this.name);
        return node;
    }
}
