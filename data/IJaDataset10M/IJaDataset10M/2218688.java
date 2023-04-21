package de.molimo.container.parser;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;
import de.molimo.container.Helper;
import de.molimo.container.IContentManager;
import de.molimo.container.exceptions.BuilderException;
import de.molimo.container.exceptions.FactoryException;
import de.molimo.container.exceptions.ObjectNotFoundException;

/**
 * @author Marcus Schiesser
 * @version 1.0
 */
public class DefaultParserHandler extends DefaultHandler implements ICreator {

    protected ParserContext context;

    protected String actContent;

    protected Attributes actAttributes;

    protected IFactory factory;

    private Locator locator;

    private IBuilder builder;

    private IContentManager manager;

    private List extensions;

    private ResourceBundle variableMap = ResourceBundle.getBundle("de.molimo.container.parser.VariableMap");

    public DefaultParserHandler() {
        super();
        context = new ParserContext();
        extensions = new LinkedList();
    }

    public Object getObject() throws ObjectNotFoundException {
        for (int i = 0; i < extensions.size(); i++) {
            IExtension extension = (IExtension) extensions.get(i);
            if (extension instanceof ICreator) return ((ICreator) extension).getObject();
        }
        throw new ObjectNotFoundException("no creator - could not build an object!");
    }

    public void setDocumentLocator(Locator locator) {
        this.locator = locator;
    }

    public void addExtension(IExtension extension) {
        extensions.add(extension);
        addExtensionToBuilder(extension);
    }

    private void addExtensionToBuilder(IExtension extension) {
        if (builder != null && extension instanceof IBuilderListener) {
            builder.addBuilderListener((IBuilderListener) extension);
        }
    }

    public void setBuilder(IBuilder builder) {
        this.builder = builder;
        for (int i = 0; i < extensions.size(); i++) {
            IExtension extension = (IExtension) extensions.get(i);
            addExtensionToBuilder(extension);
        }
    }

    public void setContentManager(IContentManager manager) {
        this.manager = manager;
    }

    public void startElement(String namespaceURI, String sName, String qName, Attributes attrs) throws SAXException {
        actContent = new String();
        Helper.getLogger().debug("Processing Tag <" + qName + ">");
        try {
            if (context.isInContext(IConstants.tOBJECTS)) {
                if (qName.equalsIgnoreCase(IConstants.tLOCAL)) builder.setLocalMap();
                if (qName.equalsIgnoreCase(IConstants.tSHARED)) builder.setGlobalMap();
            } else if (context.isInContext(IConstants.tLOCAL) || context.isInContext(IConstants.tSHARED)) {
                if (qName.equalsIgnoreCase(IConstants.tOBJECT)) builder.addObject(attrs.getValue(IConstants.aNAME), attrs.getValue(IConstants.aCLASS));
                if (qName.equalsIgnoreCase(IConstants.tEJB)) builder.addEJBHome(attrs.getValue(IConstants.aNAME), attrs.getValue(IConstants.aJNDINAME));
                if (qName.equalsIgnoreCase(IConstants.tVARIABLE)) {
                    String type = attrs.getValue(IConstants.aTYPE);
                    type = type.toUpperCase();
                    String typeClass = variableMap.getString(type);
                    builder.addVariable(attrs.getValue(IConstants.aNAME), typeClass, attrs.getValue(IConstants.aVALUE));
                }
            } else if (qName.equalsIgnoreCase(IConstants.tMETHOD)) {
                if (context.isInContext(IConstants.tOBJECT)) builder.invokeMethod(attrs.getValue(IConstants.aNAME), attrs.getValue(IConstants.aVALUE));
            } else if (qName.equalsIgnoreCase(IConstants.tEVENT)) {
                if (context.isInContext(IConstants.tEVENTS)) builder.addEvent(attrs.getValue(IConstants.aNAME), attrs.getValue(IConstants.aCLASS), attrs.getValue(IConstants.aOBJECT), attrs.getValue(IConstants.aBINDMETHOD), attrs.getValue(IConstants.aFUNCTION));
            } else if (context.isInContext(IConstants.tFUNCTIONS)) {
                if (qName.equalsIgnoreCase(IConstants.tBIND)) builder.addBind(attrs.getValue(IConstants.aNAME), attrs.getValue(IConstants.aFUNCTION));
                if (qName.equalsIgnoreCase(IConstants.tFUNCTION)) {
                    String inStr = attrs.getValue(IConstants.aIN);
                    String outStr = attrs.getValue(IConstants.aOUT);
                    int in = 1, out = 1;
                    if (inStr != null) in = Integer.parseInt(inStr);
                    if (outStr != null) out = Integer.parseInt(outStr);
                    builder.addFunction(attrs.getValue(IConstants.aNAME), attrs.getValue(IConstants.aCLASS), attrs.getValue(IConstants.aOBJECT), in, out);
                }
            } else if (context.isInContext(IConstants.tBIND)) {
                if (qName.equalsIgnoreCase(IConstants.tINPUT)) builder.addInput(attrs.getValue(IConstants.aOBJECT), attrs.getValue(IConstants.aPROPERTY));
                if (qName.equalsIgnoreCase(IConstants.tOUTPUT)) builder.addOutput(attrs.getValue(IConstants.aOBJECT), attrs.getValue(IConstants.aPROPERTY));
            } else if (context.isInContext(IConstants.tFUNCTION)) {
                if (qName.equalsIgnoreCase(IConstants.tFUNCTION)) builder.addFunctionReference(attrs.getValue(IConstants.aNAME));
            }
        } catch (BuilderException e) {
            throw new SAXParseException(null, locator, e);
        } catch (FactoryException e) {
            throw new SAXParseException(null, locator, e);
        }
        actAttributes = new AttributesImpl(attrs);
        context.pushElement(qName);
    }

    public void endElement(String namespaceURI, String sName, String qName) throws SAXException {
        context.popElement();
        Helper.getLogger().debug("Processing Tag </" + qName + ">");
        try {
            if (qName.equalsIgnoreCase(IConstants.tPARAM)) {
                if (context.isInContext(IConstants.tFUNCTION)) {
                    String extern = actAttributes.getValue(IConstants.aEXTERN);
                    if (extern != null) {
                        URL url = manager.getResource(extern);
                        actContent = Helper.readStream(url.openStream());
                    }
                    builder.addFunctionParameter(actContent);
                }
            }
            Iterator iter = extensions.iterator();
            while (iter.hasNext()) {
                IExtension extension = (IExtension) iter.next();
                String[] extraTags = extension.getExtraTags();
                for (int i = 0; i < extraTags.length; i++) {
                    if (qName.equalsIgnoreCase(extraTags[i])) {
                        extension.handleExtraTag(i, actContent);
                    }
                }
            }
        } catch (IOException e) {
            throw new SAXParseException(null, locator, e);
        } catch (BuilderException e) {
            throw new SAXParseException(null, locator, e);
        }
    }

    public void startPrefixMapping(String prefix, String uri) {
    }

    public void characters(char[] ch, int start, int length) {
        String val = String.copyValueOf(ch, start, length);
        actContent += val;
    }
}
