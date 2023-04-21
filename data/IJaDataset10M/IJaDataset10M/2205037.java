package cx.ath.contribs.internal.xerces.dom3.as;

/**
 * @deprecated
 *  This interface allows creation of an <code>ASModel</code>. The expectation 
 * is that an instance of the <code>DOMImplementationAS</code> interface can 
 * be obtained by using binding-specific casting methods on an instance of 
 * the <code>DOMImplementation</code> interface when the DOM implementation 
 * supports the feature "<code>AS-EDIT</code>". 
 * <p>See also the <a href='http://www.w3.org/TR/2001/WD-DOM-Level-3-ASLS-20011025'>Document Object Model (DOM) Level 3 Abstract Schemas and Load
and Save Specification</a>.
 */
public interface DOMImplementationAS {

    /**
     * Creates an ASModel.
     * @param isNamespaceAware Allow creation of <code>ASModel</code> with 
     *   this attribute set to a specific value.
     * @return A <code>null</code> return indicates failure.what is a 
     *   failure? Could be a system error.
     */
    public ASModel createAS(boolean isNamespaceAware);

    /**
     * Creates an <code>DOMASBuilder</code>.Do we need the method since we 
     * already have <code>DOMImplementationLS.createDOMParser</code>?
     * @return a DOMASBuilder
     */
    public DOMASBuilder createDOMASBuilder();

    /**
     * Creates an <code>DOMASWriter</code>.
     * @return a DOMASWriter
     */
    public DOMASWriter createDOMASWriter();
}
