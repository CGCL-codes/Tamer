package org.enml.measures;

import java.util.Date;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.enml.notes.Note;
import org.enml.validity.Validity;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Measure</b></em>'. <!-- end-user-doc --> <p> The following features are supported: <ul> <li> {@link org.enml.measures.Measure#getScope  <em>Scope</em>} </li> <li> {@link org.enml.measures.Measure#getDate  <em>Date</em>} </li> <li> {@link org.enml.measures.Measure#getValidity  <em>Validity</em>} </li> <li> {@link org.enml.measures.Measure#getNotes  <em>Notes</em>} </li> </ul> </p>
 * @see org.enml.measures.MeasuresPackage#getMeasure()
 * @model
 * @generated
 */
public interface Measure extends EObject {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    String copyright = "enml.org (C) 2007";

    /**
	 * Returns the value of the '<em><b>Scope</b></em>' attribute. <!-- begin-user-doc --> <p> If the meaning of the '<em>Scope</em>' attribute isn't clear, there really should be more of a description here... </p> <!-- end-user-doc -->
	 * @return  the value of the '<em>Scope</em>' attribute.
	 * @see #setScope(int)
	 * @see org.enml.measures.MeasuresPackage#getMeasure_Scope()
	 * @model  required="true"
	 * @generated
	 */
    int getScope();

    /**
	 * Sets the value of the ' {@link org.enml.measures.Measure#getScope  <em>Scope</em>} ' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value  the new value of the '<em>Scope</em>' attribute.
	 * @see #getScope()
	 * @generated
	 */
    void setScope(int value);

    /**
	 * Returns the value of the '<em><b>Date</b></em>' attribute. <!-- begin-user-doc --> <p> </p> <!-- end-user-doc -->
	 * @return  the value of the '<em>Date</em>' attribute.
	 * @see #setDate(Date)
	 * @see org.enml.measures.MeasuresPackage#getMeasure_Date()
	 * @model  required="true"
	 * @generated
	 */
    Date getDate();

    /**
	 * Sets the value of the ' {@link org.enml.measures.Measure#getDate  <em>Date</em>} ' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value  the new value of the '<em>Date</em>' attribute.
	 * @see #getDate()
	 * @generated
	 */
    void setDate(Date value);

    /**
	 * @return  <code>Measure validity</code>
	 * @model  required="true" opposite="measure"
	 */
    Validity getValidity();

    /**
	 * Sets the value of the ' {@link org.enml.measures.Measure#getValidity  <em>Validity</em>} ' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value  the new value of the '<em>Validity</em>' reference.
	 * @see #getValidity()
	 * @generated
	 */
    void setValidity(Validity value);

    /**
	 * @return <code>Measure note</code>
	 * @model containment="true" opposite="measure"
	 */
    EList<Note> getNotes();
}
