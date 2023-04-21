package org.apache.xerces.impl.xs.psvi;

/**
 * A read-only interface that represents a namespace schema information item.
 *
 * @author Sandy Gao, IBM
 * @version $Id: XSNamespaceItem.java,v 1.1.1.1 2002/10/31 15:40:36 pettys Exp $
 */
public interface XSNamespaceItem {

    /**
     * [schema namespace]
     * @see <a href="http://www.w3.org/TR/xmlschema-1/#nsi-schema_namespace">[schema namespace]</a>
     * @return The target namespace of this item.
     */
    public String getSchemaNamespace();

    /**
     * Returns a list of top-level components, i.e. element declarations,
     * attribute declarations, etc.<p>
     * Note that  <code>XSTypeDefinition#SIMPLE_TYPE</code> and
     * <code>XSTypeDefinition#COMPLEX_TYPE</code> can also be used as the
     * <code>objectType</code> to retrieve only complex types or simple types,
     * instead of all types.
     * @param objectType The type of the declaration, i.e.
     *   ELEMENT_DECLARATION, ATTRIBUTE_DECLARATION, etc.
     * @return A list of top-level definition of the specified type in
     *   <code>objectType</code> or <code>null</code>.
     */
    public XSNamedMap getComponents(short objectType);

    /**
     * Convenience method. Returns a top-level simple or complex type
     * definition.
     * @param name The name of the definition.
     * @return An <code>XSTypeDefinition</code> or null if such definition
     *   does not exist.
     */
    public XSTypeDefinition getTypeDefinition(String name);

    /**
     * Convenience method. Returns a top-level attribute declaration.
     * @param name The name of the declaration.
     * @return A top-level attribute declaration or null if such declaration
     *   does not exist.
     */
    public XSAttributeDeclaration getAttributeDecl(String name);

    /**
     * Convenience method. Returns a top-level element declaration.
     * @param name The name of the declaration.
     * @return A top-level element declaration or null if such declaration
     *   does not exist.
     */
    public XSElementDeclaration getElementDecl(String name);

    /**
     * Convenience method. Returns a top-level attribute group definition.
     * @param name The name of the definition.
     * @return A top-level attribute group definition or null if such
     *   definition does not exist.
     */
    public XSAttributeGroupDefinition getAttributeGroup(String name);

    /**
     * Convenience method. Returns a top-level model group definition.
     *
     * @param name      The name of the definition.
     * @return A top-level model group definition definition or null if such
     *         definition does not exist.
     */
    public XSModelGroupDefinition getModelGroupDefinition(String name);

    /**
     * Convenience method. Returns a top-level notation declaration.
     *
     * @param name      The name of the declaration.
     * @return A top-level notation declaration or null if such declaration
     *         does not exist.
     */
    public XSNotationDeclaration getNotationDecl(String name);

    /**
     * [document]
     * @see <a href="http://www.w3.org/TR/xmlschema-1/#sd-document">[document]</a>
     * @return a list of document information item
     */
    public ObjectList getDocuments();

    /**
     * [document location]
     * @see <a href="http://www.w3.org/TR/xmlschema-1/#sd-document_location">[document location]</a>
     * @return a list of document information item
     */
    public StringList getDocumentLocations();
}
