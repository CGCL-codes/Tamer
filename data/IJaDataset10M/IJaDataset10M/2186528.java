package net.sf.smbt.i2c;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>I2CGRD Pin</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.sf.smbt.i2c.I2CGRDPin#getGrdConnection <em>Grd Connection</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.sf.smbt.i2c.I2cPackage#getI2CGRDPin()
 * @model
 * @generated
 */
public interface I2CGRDPin extends AbstractI2CConnector {

    /**
	 * Returns the value of the '<em><b>Grd Connection</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Grd Connection</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Grd Connection</em>' reference.
	 * @see #setGrdConnection(I2CGRDWire)
	 * @see net.sf.smbt.i2c.I2cPackage#getI2CGRDPin_GrdConnection()
	 * @model
	 * @generated
	 */
    I2CGRDWire getGrdConnection();

    /**
	 * Sets the value of the '{@link net.sf.smbt.i2c.I2CGRDPin#getGrdConnection <em>Grd Connection</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Grd Connection</em>' reference.
	 * @see #getGrdConnection()
	 * @generated
	 */
    void setGrdConnection(I2CGRDWire value);
}
