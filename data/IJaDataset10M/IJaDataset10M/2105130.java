package gov.sandia.ccaffeine.dc.user_iface.MVC.event;

import java.util.EventObject;

/**
 * Used to notify components that an entity
 * wants to either retrieve the value of a port
 * property or wants to set the value of a port
 * property.
 * A view entity might
 * respond by either displaying the current
 * value of one or more port properties
 * or by setting the value of one port property.
 */
public class PortPropertiesEvent extends EventObject {

    static final long serialVersionUID = 1;

    protected int numberOfArguments = 0;

    /**
   * Get the number of arguments in the "port-properties"
   * command.
   */
    public int getNumberOfArguments() {
        return (this.numberOfArguments);
    }

    /**
   * The name of the component that contains the property
   * The name is usually the java class name of the component
   * (without the package name) concatenated with an index number.
   * Example:  "StartComponent0"
   */
    protected String componentInstanceName = null;

    /**
     * Get the name of the cca component that
     * contains the property.
     * @return the name of the compoonent that
     * contains the property.
     */
    public String getComponentInstanceName() {
        return (this.componentInstanceName);
    }

    protected String portName = null;

    /**
     * Get the name of the port that contains the property.
     * @return
     */
    public String getPortName() {
        return (this.portName);
    }

    protected String propertyName = null;

    /**
     * If we are getting or setting the value
     * of a specific property then we need the
     * name of the property.
     * @return The name of the property.
     */
    public String getPropertyName() {
        return (this.propertyName);
    }

    /**
     * If we are setting the value of a
     * property then we need the datatype
     * of the value.
     */
    protected String dataTypeOfPropertyValue = null;

    /**
     * If we are setting the value of a
     * property then we need the datatype
     * of the value.
     */
    public String getDataTypeOfPropertyValue() {
        return (this.dataTypeOfPropertyValue);
    }

    protected String propertyValue = null;

    /**
     * If we are setting the value of a
     * property then we need the new value.
     * @return The value of the property.
     */
    public String getPropertyValue() {
        return (this.propertyValue);
    }

    /**
     * Used to notify components that an entity
     * wants to either retrieve the values of all
     * the properties that are contained inside
     * of a port.
     * A view entity might
     * respond by displaying the current
     * values of the port properties.
     * @param source The entity that created this event.
     * @param componentInstanceName
     * The name of the component that contains the property
     * The name is usually the java class name of the component
     * (without the package name) concatenated with an index number.
     * Example:  "StartComponent0"
     */
    public PortPropertiesEvent(Object source, String componentInstanceName) {
        super(source);
        this.numberOfArguments = 1;
        this.componentInstanceName = componentInstanceName;
        this.propertyName = null;
        this.dataTypeOfPropertyValue = null;
        this.propertyValue = null;
    }

    /**
     * Used to notify components that an entity
     * wants to either retrieve the values of all
     * the properties that are contained inside
     * of a port.
     * A view entity might
     * respond by displaying the current
     * values of the port properties.
     * @param source The entity that created this event.
     * @param numberOfArguments
     * The number of arguments in the "port-properties"
     * command.
     * @param componentInstanceName
     * The name of the component that contains the property
     * The name is usually the java class name of the component
     * (without the package name) concatenated with an index number.
     * Example:  "StartComponent0"
     */
    public PortPropertiesEvent(Object source, int numberOfArguments, String componentInstanceName) {
        super(source);
        this.numberOfArguments = numberOfArguments;
        this.componentInstanceName = componentInstanceName;
        this.propertyName = null;
        this.dataTypeOfPropertyValue = null;
        this.propertyValue = null;
    }

    /**
     * Used to notify components that an entity
     * wants to either retrieve the value of a port
     * property or wants to set the value of a port
     * property.
     * A view entity might
     * respond by either displaying the current
     * value of a port property or by setting the value
     * of a port property.
     * @param source The entity that created this event.
     * @param componentInstanceName
     * The name of the component that contains the property
     * The name is usually the java class name of the component
     * (without the package name) concatenated with an index number.
     * Example:  "StartComponent0"
     * @param portName The name of the port that contains the component.
     * @param propertyName If we want to
     * get or set the value of a specific
     * port property, then we need the
     * name of the property.
     */
    public PortPropertiesEvent(Object source, String componentInstanceName, String portName, String propertyName) {
        super(source);
        this.numberOfArguments = 2;
        this.componentInstanceName = componentInstanceName;
        this.propertyName = propertyName;
        this.dataTypeOfPropertyValue = null;
        this.propertyValue = null;
    }

    /**
     * Used to notify components that an entity
     * wants to either retrieve the value of a port
     * property or wants to set the value of a port
     * property.
     * A view entity might
     * respond by either displaying the current
     * value of a port property or by setting the value
     * of a port property.
     * @param source The entity that created this event.
     * @param numberOfArguments
     * The number of arguments in the "port-properties"
     * command.
     * @param componentInstanceName
     * The name of the component that contains the property
     * The name is usually the java class name of the component
     * (without the package name) concatenated with an index number.
     * Example:  "StartComponent0"
     * @param portName The name of the port that contains the property
     * @param propertyName If we want to
     * get or set the value of a specific
     * port property, then we need the
     * name of the property.
     */
    public PortPropertiesEvent(Object source, int numberOfArguments, String componentInstanceName, String portName, String propertyName) {
        super(source);
        this.numberOfArguments = numberOfArguments;
        this.componentInstanceName = componentInstanceName;
        this.portName = portName;
        this.propertyName = propertyName;
        this.dataTypeOfPropertyValue = null;
        this.propertyValue = null;
    }

    /**
     * Used to notify components that an entity
     * wants to either retrieve the value of a port
     * property or wants to set the value of a port
     * property.
     * A view entity might
     * respond by either displaying the current
     * value of a port property or by setting the value
     * of a port property.
     * @param source The entity that created this event.
     * @param numberOfArguments
     * The number of arguments in the "port-properties"
     * command.
     * @param componentInstanceName
     * The name of the component that contains the property
     * The name is usually the java class name of the component
     * (without the package name) concatenated with an index number.
     * Example:  "StartComponent0"
     * @param portName The name of the port that contains the property.
     * @param propertyName If we want to
     * get or set the value of a specific
     * port property, then we need the
     * name of the property.
     * @param dataTypeOfPropertyValue If we are
     * setting the value of a specific port property,
     * then we need the datatype
     * of the property value.
     * @param propertyValue If we are
     * setting the value of a specific port property,
     * then we need the value of the property.
     */
    public PortPropertiesEvent(Object source, int numberOfArguments, String componentInstanceName, String portName, String propertyName, String dataTypeOfPropertyValue, String propertyValue) {
        super(source);
        this.numberOfArguments = numberOfArguments;
        this.componentInstanceName = componentInstanceName;
        this.portName = portName;
        this.propertyName = propertyName;
        this.dataTypeOfPropertyValue = dataTypeOfPropertyValue;
        this.propertyValue = propertyValue;
    }
}
