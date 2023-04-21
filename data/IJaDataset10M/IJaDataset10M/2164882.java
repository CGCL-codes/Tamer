package cx.ath.contribs.internal.xerces.xs;

/**
 * The <code>XSObject</code> is a base object for the XML Schema component 
 * model. 
 */
public interface XSObject {

    /**
     *  The <code>type</code> of this object, i.e. 
     * <code>ELEMENT_DECLARATION</code>. 
     */
    public short getType();

    /**
     * The name of type <code>NCName</code>, as defined in XML Namespaces, of 
     * this declaration specified in the <code>{name}</code> property of the 
     * component or <code>null</code> if the definition of this component 
     * does not have a <code>{name}</code> property. For anonymous types, 
     * the processor must construct and expose an anonymous type name that 
     * is distinct from the name of every named type and the name of every 
     * other anonymous type.
     */
    public String getName();

    /**
     *  The [target namespace] of this object, or <code>null</code> if it is 
     * unspecified. 
     */
    public String getNamespace();

    /**
     * A namespace schema information item corresponding to the target 
     * namespace of the component, if it is globally declared; or 
     * <code>null</code> otherwise.
     */
    public XSNamespaceItem getNamespaceItem();
}
