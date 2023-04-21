package com.aptana.ide.sax;

import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.Hashtable;
import java.util.Stack;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import com.aptana.ide.io.SourceWriter;

/**
 * @author Kevin Lindsey
 */
public class Schema {

    private Hashtable _elementsByName;

    private SchemaElement _rootElement;

    private Stack _elementStack;

    private SchemaElement _currentElement;

    private Object _handler;

    private Class _handlerClass;

    /**
	 * Get the Class of the handler object used for element transition event handling
	 * 
	 * @return The handler object's class
	 */
    public Class getHandlerClass() {
        return this._handlerClass;
    }

    /**
	 * Get the SchemaElement that serves as the root of this schema state machine
	 * 
	 * @return This schema's root element (like #document)
	 */
    public SchemaElement getRootElement() {
        return this._rootElement;
    }

    /**
	 * Determine if the given element name exists in this schema
	 * 
	 * @param name
	 *            The name of the element to test
	 * @return Returns true if the given element name exists in this schema
	 */
    public boolean hasElement(String name) {
        return this._elementsByName.containsKey(name);
    }

    /**
	 * Determine if this schema definition is complete and valid
	 * 
	 * @return Returns true if this schema is complete and valid
	 */
    public boolean isValid() {
        return true;
    }

    /**
	 * Set the root element of this schema. If the element does not exist, it will be added automatically to this schema
	 * 
	 * @param name
	 *            The name of the element to set as the root element
	 */
    public void setRootElement(String name) {
        SchemaElement target;
        if (this.hasElement(name)) {
            target = (SchemaElement) this._elementsByName.get(name);
        } else {
            target = this.createElement(name);
        }
        this._rootElement.addTransition(target);
    }

    /**
	 * Create a new instance of SchemaGraph
	 * 
	 * @param handler
	 *            The handler to be used for event callbacks
	 */
    public Schema(Object handler) {
        this._handler = handler;
        if (handler != null) {
            this._handlerClass = handler.getClass();
        }
        this._elementsByName = new Hashtable();
        this._elementStack = new Stack();
        this._rootElement = new SchemaElement(this, "#document");
    }

    /**
	 * Create a new SchemaElement for the given name. If the element name has already been created, then return that
	 * previous instance
	 * 
	 * @param name
	 *            The name of the element to create
	 * @return Returns the SchemaElement for the given name
	 */
    public SchemaElement createElement(String name) {
        SchemaElement result = null;
        if (this.hasElement(name)) {
            result = (SchemaElement) this._elementsByName.get(name);
        } else {
            result = new SchemaElement(this, name);
            this._elementsByName.put(name, result);
        }
        return result;
    }

    /**
	 * Try to move to a new element along a valid transition
	 * 
	 * @param namespaceURI
	 * @param localName
	 * @param qualifiedName
	 * @param attributes
	 * @throws InvalidTransitionException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws SAXException
	 */
    public void moveTo(String namespaceURI, String localName, String qualifiedName, Attributes attributes) throws InvalidTransitionException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, SAXException {
        if (this._currentElement.isValidTransition(localName) == false) {
            Object[] messageArgs = new Object[] { localName, this._currentElement.getName() };
            String message = MessageFormat.format(Messages.Schema_Invalid_Child, messageArgs);
            SourceWriter writer = new SourceWriter();
            writer.println();
            writer.println(message);
            this.buildErrorMessage(writer, localName, attributes);
            throw new InvalidTransitionException(writer.toString());
        }
        this._elementStack.push(this._currentElement);
        this._currentElement = this._currentElement.moveTo(localName);
        this._currentElement.validateAttributes(attributes);
        if (this._handler != null && this._currentElement.hasOnEnterMethod()) {
            this._currentElement.getOnEnterMethod().invoke(this._handler, new Object[] { namespaceURI, localName, qualifiedName, attributes });
        }
    }

    /**
	 * buildErrorMessage
	 *
	 * @param writer
	 * @param localName
	 * @param attributes
	 */
    public void buildErrorMessage(SourceWriter writer, String localName, Attributes attributes) {
        writer.println().println(Messages.Schema_Element_Stack_Trace);
        for (int i = 0; i < Messages.Schema_Element_Stack_Trace.length(); i++) {
            writer.print("=");
        }
        writer.println();
        for (int i = 1; i < this._elementStack.size(); i++) {
            SchemaElement element = (SchemaElement) this._elementStack.get(i);
            writer.printlnWithIndent(element.toString()).increaseIndent();
        }
        if (localName.equals(this._currentElement.getName()) == false) {
            writer.printlnWithIndent(this._currentElement.toString()).increaseIndent();
        }
        writer.printWithIndent("<").print(localName);
        for (int i = 0; i < attributes.getLength(); i++) {
            writer.print(" ").print(attributes.getLocalName(i)).print("=\"").print(attributes.getValue(i)).print("\"");
        }
        writer.println("/>");
        if (localName.equals(this._currentElement.getName()) == false) {
            writer.decreaseIndent().printWithIndent("</").print(this._currentElement.getName()).println(">");
        }
        for (int i = this._elementStack.size() - 1; i > 0; i--) {
            SchemaElement element = (SchemaElement) this._elementStack.get(i);
            writer.decreaseIndent().printWithIndent("</").print(element.getName()).println(">");
        }
    }

    /**
	 * Exit the current element
	 * 
	 * @param namespaceURI
	 * @param localName
	 * @param qualifiedName
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
    public void exitElement(String namespaceURI, String localName, String qualifiedName) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (this._handler != null && this._currentElement.hasOnExitMethod()) {
            this._currentElement.getOnExitMethod().invoke(this._handler, new Object[] { namespaceURI, localName, qualifiedName });
        }
        this._currentElement = (SchemaElement) this._elementStack.pop();
    }

    /**
	 * Prepare this schema for a new parse
	 */
    public void reset() {
        if (this._rootElement == null) {
            throw new IllegalStateException(Messages.Schema_Missing_Root_Element);
        }
        this._elementStack.clear();
        this._currentElement = this._rootElement;
    }
}
