package org.apache.xerces.xs;

/**
 *  Represents a PSVI item for one element information item. 
 */
public interface ElementPSVI extends ItemPSVI {

    /**
     * [element declaration]: an item isomorphic to the element declaration 
     * used to validate this element. 
     */
    public XSElementDeclaration getElementDeclaration();

    /**
     *  [notation]: the notation declaration. 
     */
    public XSNotationDeclaration getNotation();

    /**
     * [nil]: true if clause 3.2 of Element Locally Valid (Element) (3.3.4) is 
     * satisfied, otherwise false. 
     */
    public boolean getNil();

    /**
     * schema information: the schema information property if it is the 
     * validation root, <code>null</code> otherwise. 
     */
    public XSModel getSchemaInformation();
}
