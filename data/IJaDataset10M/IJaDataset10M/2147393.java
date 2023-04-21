package com.aptana.ide.sax;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * @author Kevin Lindsey
 */
public final class SchemaBuilder extends ValidatingReader {

    private static SchemaBuilder _builder = null;

    private Schema _newSchema;

    private Stack _elementStack;

    private SchemaElement _currentElement;

    /**
	 * Create a new instance of SchemaParser
	 * 
	 * @param schema
	 * @throws SchemaInitializationException
	 */
    private SchemaBuilder() throws SchemaInitializationException {
        this._elementStack = new Stack();
        try {
            buildSchemaSchema();
        } catch (SecurityException e) {
            String msg = Messages.SchemaBuilder_Insufficient_Reflection_Security;
            SchemaInitializationException ie = new SchemaInitializationException(msg, e);
            throw ie;
        } catch (NoSuchMethodException e) {
            String msg = Messages.SchemaBuilder_Missing_Handler_Method;
            SchemaInitializationException ie = new SchemaInitializationException(msg, e);
            throw ie;
        }
    }

    /**
	 * Create the state machine that loads and recognizes our schema xml format
	 * 
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
    private void buildSchemaSchema() throws SecurityException, NoSuchMethodException {
        Schema schema = this._schema;
        SchemaElement root = schema.createElement("schema");
        schema.setRootElement("schema");
        SchemaElement element = schema.createElement("element");
        root.addTransition(element);
        element.addAttribute("name", "required");
        element.addAttribute("type", "optional");
        element.addAttribute("onEnter", "optional");
        element.addAttribute("onExit", "optional");
        element.setOnEnter("startElementElement");
        element.setOnExit("exitElementElement");
        SchemaElement attribute = schema.createElement("attribute");
        element.addTransition(attribute);
        attribute.addAttribute("name", "required");
        attribute.addAttribute("usage", "optional");
        attribute.setOnEnter("startAttributeElement");
        SchemaElement childElement = schema.createElement("child-element");
        element.addTransition(childElement);
        childElement.addAttribute("name", "required");
        childElement.setOnEnter("startChildElementElement");
    }

    /**
	 * Finish processing the specified element
	 * 
	 * @param namespaceURI
	 * @param localName
	 * @param qualifiedName
	 */
    public void exitElementElement(String namespaceURI, String localName, String qualifiedName) {
        this._currentElement = (SchemaElement) this._elementStack.pop();
    }

    /**
	 * Load an xml schema that describes and recognizes a specific xml format
	 * 
	 * @param filename
	 *            The name of the xml schema file to load
	 * @param handler
	 *            The handler to use for event callbacks
	 * @return A validating XML reader that will recognize and validate against the loaded schema
	 * @throws SchemaInitializationException
	 */
    public static Schema fromXML(String filename, Object handler) throws SchemaInitializationException {
        FileInputStream fi = null;
        Schema schema = null;
        try {
            fi = new FileInputStream(filename);
            schema = fromXML(fi, handler);
        } catch (FileNotFoundException e) {
            String msg = Messages.SchemaBuilder_File_Unlocatable + filename;
            SchemaInitializationException ie = new SchemaInitializationException(msg, e);
            throw ie;
        } finally {
            try {
                fi.close();
            } catch (IOException e) {
            }
        }
        return schema;
    }

    /**
	 * Load an xml schema that describes and recognizes a specific xml format
	 * 
	 * @param in
	 *            The input stream of xml schema data
	 * @param handler
	 *            The handler to use for event callbacks
	 * @return A validating XML reader that will recognize and validate against the loaded schema
	 * @throws SchemaInitializationException
	 */
    public static Schema fromXML(InputStream in, Object handler) throws SchemaInitializationException {
        Schema result = new Schema(handler);
        if (_builder == null) {
            _builder = new SchemaBuilder();
        }
        _builder._newSchema = result;
        try {
            _builder.read(in);
        } catch (ParserConfigurationException e) {
            String msg = Messages.SchemaBuilder_SAX_Parser_Initialization_Error;
            SchemaInitializationException ie = new SchemaInitializationException(msg, e);
            throw ie;
        } catch (SAXException e) {
            String msg = Messages.SchemaBuilder_SAX_Parser_Error;
            SchemaInitializationException ie = new SchemaInitializationException(msg, e);
            throw ie;
        } catch (IOException e) {
            String msg = Messages.SchemaBuilder_IO_Error;
            SchemaInitializationException ie = new SchemaInitializationException(msg, e);
            throw ie;
        }
        return result;
    }

    /**
	 * Process an &lt;attribute&gt;
	 * 
	 * @param namespaceURI
	 * @param localName
	 * @param qualifiedName
	 * @param attributes
	 */
    public void startAttributeElement(String namespaceURI, String localName, String qualifiedName, Attributes attributes) {
        String name = attributes.getValue("name");
        String usage = attributes.getValue("usage");
        this._currentElement.addAttribute(name, usage);
    }

    /**
	 * Process a &lt;child-element&gt;
	 * 
	 * @param namespaceURI
	 * @param localName
	 * @param qualifiedName
	 * @param attributes
	 */
    public void startChildElementElement(String namespaceURI, String localName, String qualifiedName, Attributes attributes) {
        String elementName = attributes.getValue("name");
        SchemaElement element = this._newSchema.createElement(elementName);
        this._currentElement.addTransition(element);
    }

    /**
	 * startDocument handler
	 * 
	 * @throws SAXException
	 */
    public void startDocument() throws SAXException {
        super.startDocument();
        this._elementStack.clear();
        this._currentElement = this._newSchema.getRootElement();
    }

    /**
	 * Start processing a element element
	 * 
	 * @param namespaceURI
	 * @param localName
	 * @param qualifiedName
	 * @param attributes
	 * @throws SAXException
	 */
    public void startElementElement(String namespaceURI, String localName, String qualifiedName, Attributes attributes) throws SAXException {
        String elementName = attributes.getValue("name");
        String elementType = attributes.getValue("type");
        String onEnter = attributes.getValue("onEnter");
        String onExit = attributes.getValue("onExit");
        SchemaElement element = this._newSchema.createElement(elementName);
        if (elementType != null && elementType.equals("root")) {
            this._currentElement.addTransition(element);
        }
        if (onEnter != null && onEnter.length() > 0) {
            try {
                element.setOnEnter(onEnter);
            } catch (SecurityException e) {
                throw new SAXException(Messages.SchemaBuilder_OnEnter_Add_Error, e);
            } catch (NoSuchMethodException e) {
                throw new SAXException(Messages.SchemaBuilder_OnEnter_Add_Error, e);
            }
        }
        if (onExit != null && onExit.length() > 0) {
            try {
                element.setOnExit(onExit);
            } catch (SecurityException e) {
                throw new SAXException(Messages.SchemaBuilder_OnExit_Add_Error, e);
            } catch (NoSuchMethodException e) {
                throw new SAXException(Messages.SchemaBuilder_OnExit_Add_Error, e);
            }
        }
        this._elementStack.push(this._currentElement);
        this._currentElement = element;
    }
}
