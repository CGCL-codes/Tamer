package com.aptana.ide.sax;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Hashtable;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import com.aptana.ide.io.SourceWriter;

/**
 * @author Kevin Lindsey
 */
public class SchemaElement {

    private static final Class[] enterSignature = new Class[] { String.class, String.class, String.class, Attributes.class };

    private static final Class[] exitSignature = new Class[] { String.class, String.class, String.class };

    private static final Integer REQUIRED = new Integer(0);

    private static final Integer OPTIONAL = new Integer(1);

    private String _name;

    private Schema _owningSchema;

    private Hashtable _transitions;

    private Hashtable _attributes;

    private ArrayList _requiredAttributes;

    private String _instanceAttributes;

    private Method _onEnter;

    private Method _onExit;

    /**
	 * Get the name associated with this Schema node
	 * 
	 * @return this node's name
	 */
    public String getName() {
        return this._name;
    }

    /**
	 * Return the Method to call when entering this element
	 * 
	 * @return The Method to invoke. This value can be null if there is no OnEnter event handler associated with this
	 *         element
	 */
    public Method getOnEnterMethod() {
        return this._onEnter;
    }

    /**
	 * Set the method to call after entering this element
	 * 
	 * @param onEnterMethod
	 *            The name of the method to call on the schema's handler object when we enter this element
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
    public void setOnEnter(String onEnterMethod) throws SecurityException, NoSuchMethodException {
        Class handlerClass = this._owningSchema.getHandlerClass();
        if (handlerClass != null) {
            this._onEnter = handlerClass.getMethod(onEnterMethod, enterSignature);
        } else {
            this._onEnter = null;
        }
    }

    /**
	 * Return the Method to call when exiting this element
	 * 
	 * @return The Method to invoke. This value can be null if there is no OnExit event handler associated with this
	 *         element
	 */
    public Method getOnExitMethod() {
        return this._onExit;
    }

    /**
	 * Set the method to call before exiting this element
	 * 
	 * @param onExitMethod
	 *            The name of the method to call on the schema's handler object when we exit this element
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
    public void setOnExit(String onExitMethod) throws SecurityException, NoSuchMethodException {
        Class handlerClass = this._owningSchema.getHandlerClass();
        if (handlerClass != null) {
            this._onExit = handlerClass.getMethod(onExitMethod, exitSignature);
        } else {
            this._onExit = null;
        }
    }

    /**
	 * Determine if this element has a definition for the specified attribute name
	 * 
	 * @param name
	 *            The name of the attribute to test
	 * @return Returns true if this element has an entry for the specified attribute name
	 */
    public boolean hasAttribute(String name) {
        return (this._attributes.containsKey(name));
    }

    /**
	 * Determine if this element has an associated OnEnter handler
	 * 
	 * @return Returns true if this element has an OnEnter handler
	 */
    public boolean hasOnEnterMethod() {
        return (this._onEnter != null);
    }

    /**
	 * Determine if this element has an associated OnExit handler
	 * 
	 * @return Returns true if this element has an OnExit handler
	 */
    public boolean hasOnExitMethod() {
        return (this._onExit != null);
    }

    /**
	 * Determine if the specified attribute name is optional on this element
	 * 
	 * @param name
	 *            The name of the attribute to test
	 * @return Returns true if the specified attribute name does not have to exist on this element
	 */
    public boolean isOptionalAttribute(String name) {
        boolean result = false;
        if (this.isValidAttribute(name)) {
            result = ((Integer) this._attributes.get(name)) == REQUIRED;
        }
        return result;
    }

    /**
	 * Determine if the specified attribute name is required on this element
	 * 
	 * @param name
	 *            The name of the attribute to test
	 * @return Returns true if the specified attribute name must exist on this element
	 */
    public boolean isRequiredAttribute(String name) {
        boolean result = false;
        if (this.isValidAttribute(name)) {
            result = ((Integer) this._attributes.get(name)) == REQUIRED;
        }
        return result;
    }

    /**
	 * Determine if the specified attribute name is allowed on this element
	 * 
	 * @param name
	 *            The name of the attribute to test
	 * @return Returns true if the specified attribute name is allowed on this element
	 */
    public boolean isValidAttribute(String name) {
        return (this._attributes.containsKey(name));
    }

    /**
	 * Determine if this node can transition to another node using the given name
	 * 
	 * @param name
	 *            The name of the node to test as a possible transition target
	 * @return Returns true if this node can transition to the given node name
	 */
    public boolean isValidTransition(String name) {
        return this._transitions.containsKey(name);
    }

    /**
	 * Create a new instance of SchemaNode
	 * 
	 * @param owningSchema
	 *            The schema that owns this element
	 * @param name
	 *            The name of this node
	 */
    public SchemaElement(Schema owningSchema, String name) {
        if (owningSchema == null) {
            throw new IllegalArgumentException(Messages.SchemaElement_Undefined_Owning_Schema);
        }
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException(Messages.SchemaElement_Undefined_Name);
        }
        this._owningSchema = owningSchema;
        this._name = name;
        this._transitions = new Hashtable();
        this._attributes = new Hashtable();
        this._requiredAttributes = new ArrayList();
    }

    /**
	 * Add an attribute to this element
	 * 
	 * @param name
	 *            The name of the attribute
	 * @param usage
	 *            The usage requirements for the attribute
	 */
    public void addAttribute(String name, String usage) {
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException(Messages.SchemaElement_Undefined_Name);
        }
        if (this.hasAttribute(name)) {
            String msg = "The attribute '" + name + "' has already been defined on " + this._name;
            throw new IllegalArgumentException(msg);
        }
        Integer usageValue;
        if (usage != null) {
            if (usage.equals("required")) {
                usageValue = REQUIRED;
            } else if (usage.equals("optional")) {
                usageValue = OPTIONAL;
            } else {
                String msg = usage + " is not a valid 'usage' attribute value. <attribute>'s usage attribute must equal 'required' or 'optional'";
                throw new IllegalArgumentException(msg);
            }
        } else {
            usageValue = REQUIRED;
        }
        this._attributes.put(name, usageValue);
        if (usageValue == REQUIRED) {
            this._requiredAttributes.add(name);
        }
    }

    /**
	 * Add a transition out of this node to another node
	 * 
	 * @param node
	 *            The node to which this node can transition
	 */
    public void addTransition(SchemaElement node) {
        if (node == null) {
            throw new IllegalArgumentException(Messages.SchemaElement_Undefined_Node);
        }
        String nodeName = node.getName();
        if (this.isValidTransition(nodeName)) {
            String msg = "A node name '" + nodeName + "' has already been added to " + this._name;
            throw new IllegalArgumentException(msg);
        }
        this._transitions.put(nodeName, node);
    }

    /**
	 * Get the named SchemaElement that transitions from this element
	 * 
	 * @param name
	 *            The name of the SchemaElement to transition to
	 * @return The new SchemaElement
	 */
    public SchemaElement moveTo(String name) {
        return (SchemaElement) this._transitions.get(name);
    }

    /**
	 * Validate the list of attributes against this element's definition. Required attributes must exist and no
	 * attributes can be in the list that have not been defined for this element.
	 * 
	 * @param attributes
	 *            The list of attributes to test
	 * @throws SAXException
	 */
    public void validateAttributes(Attributes attributes) throws SAXException {
        if (attributes.getLength() > 0) {
            this._instanceAttributes = "";
            for (int i = 0; i < attributes.getLength(); i++) {
                String key = attributes.getLocalName(i);
                String value = attributes.getValue(i);
                this._instanceAttributes += " " + key + "=\"" + value + "\"";
            }
        }
        for (int i = 0; i < this._requiredAttributes.size(); i++) {
            String name = (String) this._requiredAttributes.get(i);
            String value = attributes.getValue(name);
            if (value == null) {
                SourceWriter writer = new SourceWriter();
                writer.print("<").print(this._name).print("> requires a '").print(name).println("' attribute");
                this._owningSchema.buildErrorMessage(writer, this._name, attributes);
                throw new SAXException(writer.toString());
            }
        }
        for (int i = 0; i < attributes.getLength(); i++) {
            String name = attributes.getLocalName(i);
            if (this._attributes.containsKey(name) == false) {
                SourceWriter writer = new SourceWriter();
                writer.print("Invalid attribute '").print(name).print("' on <").print(this._name).println(">");
                this._owningSchema.buildErrorMessage(writer, this._name, attributes);
                throw new SAXException(writer.toString());
            }
        }
    }

    /**
	 * @see java.lang.Object#toString()
	 */
    public String toString() {
        String result = "<" + this._name;
        if (this._instanceAttributes != null) {
            result += this._instanceAttributes;
        }
        result += ">";
        return result;
    }
}
