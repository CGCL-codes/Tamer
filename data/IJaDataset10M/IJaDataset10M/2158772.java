package de.mpiwg.vspace.metamodel;

import org.eclipse.emf.ecore.EObject;

/**
 * @model
 *
 */
public interface Sequence extends EObject {

    /**
	 * @model
	 *
	 */
    public String getTitle();

    /**
	 * Sets the value of the '{@link de.mpiwg.vspace.metamodel.Sequence#getTitle <em>Title</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Title</em>' attribute.
	 * @see #getTitle()
	 * @generated
	 */
    void setTitle(String value);

    /**
	 * @model
	 *
	 */
    public String getDescription();

    /**
	 * Sets the value of the '{@link de.mpiwg.vspace.metamodel.Sequence#getDescription <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Description</em>' attribute.
	 * @see #getDescription()
	 * @generated
	 */
    void setDescription(String value);

    /**
	 * @model
	 */
    public String getSlideOrder();

    /**
	 * Sets the value of the '{@link de.mpiwg.vspace.metamodel.Sequence#getSlideOrder <em>Slide Order</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Slide Order</em>' attribute.
	 * @see #getSlideOrder()
	 * @generated
	 */
    void setSlideOrder(String value);

    public String[] getSlideIds();

    /**
	 * @model
	 */
    public String getSequenceId();

    /**
	 * Sets the value of the '{@link de.mpiwg.vspace.metamodel.Sequence#getSequenceId <em>Sequence Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Sequence Id</em>' attribute.
	 * @see #getSequenceId()
	 * @generated
	 */
    void setSequenceId(String value);
}
