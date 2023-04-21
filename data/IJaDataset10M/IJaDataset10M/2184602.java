package org.dbe.kb.metamodel.upm.preferences;

/**
 * UserPreference object instance interface.
 *  
 * <p><em><strong>Note:</strong> This type should not be subclassed or implemented 
 * by clients. It is generated from a MOF metamodel and automatically implemented 
 * by MDR (see <a href="http://mdr.netbeans.org/">mdr.netbeans.org</a>).</em></p>
 */
public interface UserPreference extends org.dbe.kb.metamodel.upm.preferences.Label {

    /**
     * Returns the value of attribute name.
     * @return Value of attribute name.
     */
    public java.lang.String getName();

    /**
     * Sets the value of name attribute. See {@link #getName} for description 
     * on the attribute.
     * @param newValue New value to be set.
     */
    public void setName(java.lang.String newValue);

    /**
     * Returns the value of reference constraints.
     * @return Value of reference constraints. Element type: {@link org.dbe.kb.metamodel.upm.preferences.ConstrainedLabel}
     */
    public java.util.Collection getConstraints();
}
