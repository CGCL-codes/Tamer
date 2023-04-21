package org.enml.measures;

import java.util.Date;
import org.eclipse.emf.ecore.EObject;

/**
 * @author  Pag
 * @model
 */
public interface TimeHistorySample extends EObject {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    String copyright = "enml.org (C) 2007";

    /**
	 * @return  <code>TimeHistorySample valueId</code>
	 * @model  required="true"
	 */
    long getValueId();

    /**
	 * Sets the value of the ' {@link org.enml.measures.TimeHistorySample#getValueId  <em>Value Id</em>} ' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value  the new value of the '<em>Value Id</em>' attribute.
	 * @see #getValueId()
	 * @generated
	 */
    void setValueId(long value);

    /**
	 * @return  <code>TimeHistorySample timestamp</code>
	 * @model  required="true"
	 */
    Date getTimestamp();

    /**
	 * Sets the value of the ' {@link org.enml.measures.TimeHistorySample#getTimestamp  <em>Timestamp</em>} ' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value  the new value of the '<em>Timestamp</em>' attribute.
	 * @see #getTimestamp()
	 * @generated
	 */
    void setTimestamp(Date value);

    /**
	 * @return  <code>TimeHistorySample values array</code>
	 * @model  required="true"
	 */
    double[] getValues();

    /**
	 * Sets the value of the ' {@link org.enml.measures.TimeHistorySample#getValues  <em>Values</em>} ' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value  the new value of the '<em>Values</em>' attribute.
	 * @see #getValues()
	 * @generated
	 */
    void setValues(double[] value);

    /**
	 * @return  <code>TimeHistory</code> reference
	 * @model  required="true" opposite="samples"
	 */
    TimeHistory getTimeHistory();

    /**
	 * Sets the value of the ' {@link org.enml.measures.TimeHistorySample#getTimeHistory  <em>Time History</em>} ' container reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value  the new value of the '<em>Time History</em>' container reference.
	 * @see #getTimeHistory()
	 * @generated
	 */
    void setTimeHistory(TimeHistory value);
}
