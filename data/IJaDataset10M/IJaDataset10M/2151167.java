package ch.iserver.ace.test;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.BeanUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;
import ch.iserver.ace.Operation;

/**
 * Default implementation of the ScenarioLoader interface. Reads scenarios from
 * an xml file that is valid with respect to a schema (/test/scenario.xsd).
 */
public class DefaultScenarioLoader implements ScenarioLoader {

    static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";

    static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";

    static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";

    /** default implementation of scenario loader interface */
    public DefaultScenarioLoader() {
    }

    /**
	 * {@inheritDoc}
	 */
    public void loadScenario(ScenarioBuilder scenarioBuilder, InputStream source) throws IOException {
        InputStream schema = getClass().getResourceAsStream("/test/scenario.xsd");
        SAXBuilder builder = createSAXBuilder(schema);
        try {
            Document doc = builder.build(source);
            Element root = doc.getRootElement();
            String initialState = root.getAttributeValue("initial");
            String finalState = root.getAttributeValue("final");
            scenarioBuilder.init(initialState, finalState);
            processOperations(scenarioBuilder, root.getChildren("operation"));
            processSites(scenarioBuilder, root.getChildren("site"));
        } catch (JDOMException e) {
            throw new ScenarioLoaderException(e);
        }
    }

    protected void processOperations(ScenarioBuilder builder, List operations) {
        Iterator it = operations.iterator();
        while (it.hasNext()) {
            Element operationEl = (Element) it.next();
            String id = operationEl.getAttributeValue("id");
            String type = operationEl.getAttributeValue("type");
            try {
                Operation operation = (Operation) Class.forName(type).newInstance();
                Map params = getProperties(operationEl);
                BeanUtils.populate(operation, params);
                builder.addOperation(id, operation);
            } catch (InstantiationException e) {
                throw new ScenarioLoaderException(e);
            } catch (IllegalAccessException e) {
                throw new ScenarioLoaderException(e);
            } catch (InvocationTargetException e) {
                throw new ScenarioLoaderException(e);
            } catch (ClassNotFoundException e) {
                throw new ScenarioLoaderException(e);
            }
        }
    }

    protected Map getProperties(Element el) {
        Map result = new HashMap();
        Iterator it = el.getChildren("property").iterator();
        while (it.hasNext()) {
            Element property = (Element) it.next();
            String name = property.getAttributeValue("name");
            String value = property.getAttributeValue("value");
            result.put(name, value);
        }
        return result;
    }

    protected void processSites(ScenarioBuilder builder, List sites) {
        Iterator it = sites.iterator();
        while (it.hasNext()) {
            Element siteEl = (Element) it.next();
            String siteId = siteEl.getAttributeValue("id");
            builder.startSite(siteId);
            processSiteChildren(builder, siteEl.getChildren());
            builder.endSite();
        }
    }

    protected void processSiteChildren(ScenarioBuilder builder, List children) {
        Iterator it = children.iterator();
        while (it.hasNext()) {
            Element childEl = (Element) it.next();
            String ref = childEl.getAttributeValue("ref");
            if ("generate".equals(childEl.getName())) {
                builder.addGeneration(ref);
            } else if ("receive".equals(childEl.getName())) {
                builder.addReception(ref);
            }
        }
    }

    protected SAXBuilder createSAXBuilder(InputStream schema) throws IOException {
        SAXBuilder builder = new SAXBuilder("org.apache.xerces.parsers.SAXParser");
        builder.setProperty(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
        builder.setProperty(JAXP_SCHEMA_SOURCE, schema);
        builder.setErrorHandler(new DefaultHandler() {

            public void error(SAXParseException e) throws SAXException {
                throw e;
            }
        });
        return builder;
    }
}
