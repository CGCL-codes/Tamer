package ontorama.webkbtools.query.parser.xml;

import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Iterator;
import java.io.Reader;
import ontorama.webkbtools.query.parser.Parser;
import ontorama.webkbtools.datamodel.OntologyType;
import ontorama.webkbtools.datamodel.OntologyTypeImplementation;
import ontorama.OntoramaConfig;
import ontorama.webkbtools.util.NoSuchRelationLinkException;
import ontorama.webkbtools.util.ParserException;

public class XmlParser implements Parser {

    /**
   * Hashtable to hold all OntologyTypes that we are creating
   */
    private Hashtable ontHash;

    public XmlParser() {
        ontHash = new Hashtable();
    }

    public Iterator getOntologyTypeIterator(Reader reader) throws ParserException {
        try {
            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(reader);
            List nodes = doc.getRootElement().getChildren("node");
            Iterator it = nodes.iterator();
            while (it.hasNext()) {
                Element element = (Element) it.next();
                parseElements(element);
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            e.printStackTrace();
        }
        return ontHash.values().iterator();
    }

    /**
     * Read elements into hashtable
     * @todo    replace int 1 in folowing statements:
     * type.isRelationType(subType, 1) and type.addRelationType(subType, 1)
     * with something more meaninfull.
     */
    private void parseElements(Element element) throws NoSuchRelationLinkException {
        String typeName = element.getAttributeValue("name");
        OntologyType type = (OntologyTypeImplementation) ontHash.get(typeName);
        if (type == null) {
            type = new OntologyTypeImplementation(typeName);
            ontHash.put(typeName, type);
        }
        List childrenElements = element.getChildren("child");
        Iterator it = childrenElements.iterator();
        while (it.hasNext()) {
            Element childElement = (Element) it.next();
            String subTypeName = childElement.getAttributeValue("name");
            OntologyType subType = (OntologyTypeImplementation) ontHash.get(subTypeName);
            if (subType == null) {
                subType = new OntologyTypeImplementation(subTypeName);
                ontHash.put(subTypeName, subType);
            }
            if (!type.isRelationType(subType, 1)) {
                type.addRelationType(subType, 1);
            }
        }
    }
}
