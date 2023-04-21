package org.dev2live.parse.Xmi1_2_Uml1_4;

import static org.junit.Assert.*;
import java.io.Reader;
import java.io.StringReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import junit.framework.Assert;
import org.dev2live.xmi.ParameterNode;
import org.dev2live.xmi.PartitionNode;
import org.dev2live.xmi.StereotypeNode;
import org.junit.Test;

/**
 * @author bertram
 *
 */
public class StereotypeParserTest {

    /**
	 * Test method for {@link org.dev2live.parse.Xmi1_2_Uml1_4.StereotypeParser#getName()}.
	 */
    @Test
    public void testGetName() {
        Assert.assertEquals("Stereotype", new StereotypeParser().getName());
    }

    /**
	 * Test method for {@link org.dev2live.parse.Xmi1_2_Uml1_4.StereotypeParser#parse(javax.xml.stream.XMLStreamReader)}.
	 */
    @Test
    public void testParse() {
        StringBuffer xml = new StringBuffer("<?xml version = '1.0' encoding = 'UTF-8' ?>\r\n");
        xml.append("<XMI xmi.version = '1.2' xmlns:UML = 'org.omg.xmi.namespace.UML' timestamp = 'Thu Jul 30 22:30:47 CEST 2009'>").append("<XMI.header><XMI.documentation><XMI.exporter>ArgoUML (using Netbeans XMI Writer version 1.0)</XMI.exporter>").append("<XMI.exporterVersion>0.28(6) revised on $Date: 2007-05-12 08:08:08 +0200 (Sat, 12 May 2007) $ </XMI.exporterVersion>").append("</XMI.documentation><XMI.metamodel xmi.name=\"UML\" xmi.version=\"1.4\"/></XMI.header>");
        xml.append("<XMI.content><UML:Model xmi.id = '127-0-1-1-1c45a5b6:121265ccc8b:-8000:0000000000001057' name = 'Test' isSpecification = 'false' isRoot = 'false' isLeaf = 'false' isAbstract = 'false'>").append("<UML:Namespace.ownedElement>");
        xml.append("<UML:Stereotype xmi.id = '127-0-1-1-1c45a5b6:121265ccc8b:-8000:0000000000001082' name = 'ExecuteCommand' isSpecification = 'false' isRoot = 'false' isLeaf = 'false' isAbstract = 'false'>").append("<UML:Stereotype.baseClass>ActionState</UML:Stereotype.baseClass>").append("<UML:Stereotype.definedTag>").append("<UML:TagDefinition xmi.id = '127-0-1-1-1c45a5b6:121265ccc8b:-8000:0000000000001083' name = 'command' isSpecification = 'false'>").append("<UML:TagDefinition.multiplicity>").append("<UML:Multiplicity xmi.id = '127-0-1-1--720bb9f2:121d3efbda8:-8000:0000000000001235'>").append("<UML:Multiplicity.range>").append("<UML:MultiplicityRange xmi.id = '127-0-1-1--720bb9f2:121d3efbda8:-8000:0000000000001234' lower = '1' upper = '1'/>").append("</UML:Multiplicity.range></UML:Multiplicity>").append("</UML:TagDefinition.multiplicity></UML:TagDefinition>").append("<UML:TagDefinition xmi.id = '127-0-1-1-1c45a5b6:121265ccc8b:-8000:0000000000001086' name = 'arguments' isSpecification = 'false'>").append("<UML:TagDefinition.multiplicity>").append("<UML:Multiplicity xmi.id = '127-0-1-1-1c45a5b6:121265ccc8b:-8000:0000000000001087'>").append("<UML:Multiplicity.range>").append("<UML:MultiplicityRange xmi.id = '127-0-1-1-1c45a5b6:121265ccc8b:-8000:0000000000001088' lower = '0' upper = '1'/>").append("</UML:Multiplicity.range></UML:Multiplicity>").append("</UML:TagDefinition.multiplicity></UML:TagDefinition>").append("<UML:TagDefinition xmi.id = '127-0-1-1--720bb9f2:121d3efbda8:-8000:000000000000122F' name = 'source' isSpecification = 'false'>").append("<UML:TagDefinition.multiplicity>").append("<UML:Multiplicity xmi.id = '127-0-1-1--720bb9f2:121d3efbda8:-8000:0000000000001233'>").append("<UML:Multiplicity.range>").append("<UML:MultiplicityRange xmi.id = '127-0-1-1--720bb9f2:121d3efbda8:-8000:0000000000001232' lower = '1' upper = '1'/>").append("</UML:Multiplicity.range></UML:Multiplicity>").append("</UML:TagDefinition.multiplicity></UML:TagDefinition>").append("</UML:Stereotype.definedTag></UML:Stereotype>");
        xml.append("</UML:Namespace.ownedElement></UML:Model></XMI.content></XMI>");
        Reader input = new StringReader(xml.toString());
        StereotypeParser parser = new StereotypeParser();
        StereotypeNode node = null;
        XMLInputFactory factory = (XMLInputFactory) XMLInputFactory.newInstance();
        try {
            XMLStreamReader staxXmlReader = (XMLStreamReader) factory.createXMLStreamReader(input);
            for (int event = staxXmlReader.next(); event != XMLStreamConstants.END_DOCUMENT; event = staxXmlReader.next()) {
                if (event == XMLStreamConstants.START_ELEMENT) {
                    String element = staxXmlReader.getLocalName();
                    if (parser.getName().equals(element)) {
                        node = (StereotypeNode) parser.parse(staxXmlReader);
                        break;
                    }
                }
            }
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(node);
        Assert.assertEquals("ID", "127-0-1-1-1c45a5b6:121265ccc8b:-8000:0000000000001082", node.getId());
        Assert.assertEquals("Name", "ExecuteCommand", node.getName());
        Assert.assertEquals("Stereotype.baseClass", "ActionState", node.getBaseClass());
        ParameterNode[] parameters = node.getParameters();
        Assert.assertEquals("Count of contents", 3, parameters.length);
        Assert.assertEquals("Paramter command", "command", node.getParameter("127-0-1-1-1c45a5b6:121265ccc8b:-8000:0000000000001083").getName());
        Assert.assertEquals("Paramter arguments", "arguments", node.getParameter("127-0-1-1-1c45a5b6:121265ccc8b:-8000:0000000000001086").getName());
        Assert.assertEquals("Paramter source", "source", node.getParameter("127-0-1-1--720bb9f2:121d3efbda8:-8000:000000000000122F").getName());
    }
}
