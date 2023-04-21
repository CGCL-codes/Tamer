package org.dbe.kb.metamodel.oql.core.expressions;

/**
 * EnumLiteralExp object instance interface.
 *  
 * <p><em><strong>Note:</strong> This type should not be subclassed or implemented 
 * by clients. It is generated from a MOF metamodel and automatically implemented 
 * by MDR (see <a href="http://mdr.netbeans.org/">mdr.netbeans.org</a>).</em></p>
 */
public interface EnumLiteralExp extends org.dbe.kb.metamodel.oql.core.expressions.LiteralExp {

    /**
     * Returns the value of reference refferedEnumeration.
     * @return Value of reference refferedEnumeration.
     */
    public javax.jmi.model.EnumerationType getRefferedEnumeration();

    /**
     * Sets the value of reference refferedEnumeration. See {@link #getRefferedEnumeration} 
     * for description on the reference.
     * @param newValue New value to be set.
     */
    public void setRefferedEnumeration(javax.jmi.model.EnumerationType newValue);
}
