package pt.utl.ist.lucene.treceval.handlers;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Field;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.XPath;
import java.io.IOException;
import java.util.*;

/**
 * XML Resource Handler will Call Field Handler for each given field
 * Call indexer in a callback with fields and ResourceId
 *
 * @author Jorge Machado
 * @date 21/Ago/2008
 * @see pt.utl.ist.lucene.treceval.handlers
 */
public class XmlResourceHandler implements ResourceHandler {

    private static final Logger logger = Logger.getLogger(XmlResourceHandler.class);

    private String resourceXpath;

    private String idXpath;

    private String idXpathSecondary;

    private List<XmlFieldHandler> fieldHandlers;

    private Map<String, String> namespaces = null;

    public XmlResourceHandler(String resourceXpath, String idXpath, List<XmlFieldHandler> fieldHandlers) {
        this.resourceXpath = resourceXpath;
        this.idXpath = idXpath;
        this.fieldHandlers = fieldHandlers;
    }

    public XmlResourceHandler(String resourceXpath, String idXpath, List<XmlFieldHandler> fieldHandlers, Map<String, String> namespaces) {
        this.resourceXpath = resourceXpath;
        this.idXpath = idXpath;
        this.fieldHandlers = fieldHandlers;
        this.namespaces = namespaces;
    }

    public XmlResourceHandler(String resourceXpath, String idXpath, String idXpathSecondary, List<XmlFieldHandler> fieldHandlers, Map<String, String> namespaces) {
        this.resourceXpath = resourceXpath;
        this.idXpath = idXpath;
        this.idXpathSecondary = idXpathSecondary;
        this.fieldHandlers = fieldHandlers;
        this.namespaces = namespaces;
    }

    public String getResourceXpath() {
        return resourceXpath;
    }

    public IdMap handle(Element element) throws IOException {
        XPath idXPath = element.createXPath(idXpath);
        if (namespaces != null) idXPath.setNamespaceURIs(namespaces);
        Node id = idXPath.selectSingleNode(element);
        if (id == null && idXpathSecondary != null) {
            idXPath = element.createXPath(idXpathSecondary);
            if (namespaces != null) idXPath.setNamespaceURIs(namespaces);
            id = idXPath.selectSingleNode(element);
        } else if (id == null && idXpathSecondary == null) {
            logger.error("Doc without id field: " + element);
            return null;
        }
        Map<String, String> textFields = new HashMap<String, String>();
        Map<String, String> storedFields = new HashMap<String, String>();
        Map<String, Field> uniqueFields = new HashMap<String, Field>();
        Collection<IdMap.TextField> isolatedFields = new HashSet<IdMap.TextField>();
        for (XmlFieldHandler handler : fieldHandlers) {
            XPath xPath = element.createXPath(handler.getFieldXpath());
            if (namespaces != null) xPath.setNamespaceURIs(namespaces);
            List<Node> nodes = xPath.selectNodes(element);
            for (Node node : nodes) {
                FilteredFields fields = handler.getFields(node);
                if (fields != null && fields.getTextFields() != null) {
                    for (Map.Entry<String, String> entry : fields.getTextFields().entrySet()) {
                        String old = textFields.get(entry.getKey());
                        String newOne = entry.getValue();
                        if (old == null) textFields.put(entry.getKey(), newOne); else textFields.put(entry.getKey(), old + ' ' + newOne);
                    }
                }
                if (fields != null && fields.getStoredTextFields() != null) {
                    for (Map.Entry<String, String> entry : fields.getStoredTextFields().entrySet()) {
                        String old = storedFields.get(entry.getKey());
                        if (old == null) storedFields.put(entry.getKey(), entry.getValue()); else storedFields.put(entry.getKey(), old + ' ' + entry.getValue());
                    }
                }
                if (fields != null && fields.getIsolatedTextFields() != null) {
                    for (FilteredFields.TextField t : fields.getIsolatedTextFields()) {
                        isolatedFields.add(new IdMap.TextField(t.name, t.value));
                    }
                }
                if (fields != null && fields.getPreparedFields() != null) {
                    for (Field f : fields.getPreparedFields()) {
                        Field old = uniqueFields.get(f.name());
                        if (old == null) uniqueFields.put(f.name(), f); else logger.error("Not UNIQUE Prepared field " + f.name() + " in doc: " + id.getText());
                    }
                }
            }
        }
        if (id instanceof Element) return new IdMap(((Element) id).getTextTrim(), textFields, storedFields, new ArrayList<Field>(uniqueFields.values()), isolatedFields); else return new IdMap(id.getText().trim(), textFields, storedFields, new ArrayList<Field>(uniqueFields.values()), isolatedFields);
    }

    public String getIdXpath() {
        return idXpath;
    }

    public String getIdXpathSecondary() {
        return idXpathSecondary;
    }

    public List<XmlFieldHandler> getFieldHandlers() {
        return fieldHandlers;
    }

    public Map<String, String> getNamespaces() {
        return namespaces;
    }
}
