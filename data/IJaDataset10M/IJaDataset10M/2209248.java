package org.simpleframework.xml.core;

/**
 * The <code>Instance</code> object creates a type that is represented 
 * by an XML element. Typically the <code>getInstance</code> method 
 * acts as a proxy to the classes new instance method, which takes no 
 * arguments. Simply delegating to <code>Class.newInstance</code> will 
 * sometimes not be sufficient, is such cases reflectively acquiring 
 * the classes constructor may be required in order to pass arguments.
 * 
 * @author Niall Gallagher
 * 
 * @see org.simpleframework.xml.strategy.Value
 */
interface Instance {

    /**
    * This method is used to acquire an instance of the type that
    * is defined by this object. If for some reason the type can
    * not be instantiated an exception is thrown from this.
    * 
    * @return an instance of the type this object represents
    */
    public Object getInstance() throws Exception;

    /**
    * This method is used acquire the value from the type and if
    * possible replace the value for the type. If the value can
    * not be replaced then an exception should be thrown. This 
    * is used to allow primitives to be inserted into a graph.
    * 
    * @param value this is the value to insert as the type
    * 
    * @return an instance of the type this object represents
    */
    public Object setInstance(Object value) throws Exception;

    /**
    * This is used to determine if the type is a reference type.
    * A reference type is a type that does not require any XML
    * deserialization based on its annotations. Values that are
    * references could be substitutes objects or existing ones. 
    * 
    * @return this returns true if the object is a reference
    */
    public boolean isReference();

    /**
    * This is the type of the object instance that will be created
    * by the <code>getInstance</code> method. This allows the 
    * deserialization process to perform checks against the field.
    * 
    * @return the type of the object that will be instantiated
    */
    public Class getType();
}
