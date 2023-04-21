package by.brsu.portal.servlets;

import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import by.brsu.portal.PortalTechnicalException;

/**
 * @author Roman Ulezlo
 * 
 */
public class ActionHandler extends DefaultHandler {

    private String actionName;

    private String className;

    private String currentAction;

    private String currentElement;

    private String forwardURL;

    private String errorURL;

    private List<String> roles = new ArrayList<String>(0);

    public ActionHandler(String actionName) {
        className = "";
        forwardURL = "";
        currentAction = "";
        currentElement = "";
        errorURL = "";
        this.actionName = actionName;
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            String schemaLang = "http://www.w3.org/2001/XMLSchema";
            SchemaFactory schemaFactory = SchemaFactory.newInstance(schemaLang);
            Schema schema = schemaFactory.newSchema(new StreamSource(getClass().getResourceAsStream("schema.xsd")));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(getClass().getResourceAsStream("actions.xml")));
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(new InputSource(getClass().getResourceAsStream("actions.xml")), this);
        } catch (Throwable t) {
            new PortalTechnicalException("xml invalid");
            System.out.print("xml invalid");
        }
    }

    @Override
    public void startElement(String namespaceURI, String localName, String qualifiedName, Attributes attributes) throws SAXException {
        if (qualifiedName.equals("action")) {
            if (attributes.getValue("name").equals(actionName)) {
                currentAction = actionName;
            }
        }
        if (currentAction.equals(actionName)) {
            currentElement = qualifiedName;
        }
    }

    @Override
    public void endElement(String namespaceURI, String simpleName, String qualifiedName) throws SAXException {
        if ((qualifiedName.equals("action")) && (currentAction != "")) currentAction = "";
    }

    @Override
    public void characters(char buf[], int offset, int len) throws SAXException {
        if ((currentElement.equals("class")) && (currentAction.equals(actionName))) {
            className = new String(buf, offset, len);
            currentElement = "";
        }
        if ((currentElement.equals("forward-url")) && (currentAction.equals(actionName))) {
            forwardURL = new String(buf, offset, len);
            currentElement = "";
        }
        if ((currentElement.equals("error-url")) && (currentAction.equals(actionName))) {
            errorURL = new String(buf, offset, len);
            currentElement = "";
        }
        if ((currentElement.equals("role")) && (currentAction.equals(actionName))) {
            roles.add(new String(buf, offset, len));
            currentElement = "";
        }
    }

    public String getClassName() {
        return className;
    }

    public String getForwardURL() {
        return forwardURL;
    }

    public String getErrorURL() {
        return errorURL;
    }

    public List<String> getRoles() {
        return roles;
    }
}
