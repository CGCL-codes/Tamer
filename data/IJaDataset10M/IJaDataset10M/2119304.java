package org.enml.net;

/**
 * @author  Pag
 * @model
 */
public interface LinearInfrastructure extends Infrastructure {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    String copyright = "enml.org (C) 2007";

    /**
	 * @return  <code>LinearInfrastructure length</code>
	 * @model  required="true"
	 */
    double getLength();

    /**
	 * Sets the value of the ' {@link org.enml.net.LinearInfrastructure#getLength  <em>Length</em>} ' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value  the new value of the '<em>Length</em>' attribute.
	 * @see #getLength()
	 * @generated
	 */
    void setLength(double value);
}
