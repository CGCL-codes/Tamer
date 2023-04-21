package fr.univartois.cril.xtext.als;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Open</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link fr.univartois.cril.xtext.als.Open#getOpenName <em>Open Name</em>}</li>
 *   <li>{@link fr.univartois.cril.xtext.als.Open#getLeft <em>Left</em>}</li>
 *   <li>{@link fr.univartois.cril.xtext.als.Open#getPreviousNameAs <em>Previous Name As</em>}</li>
 *   <li>{@link fr.univartois.cril.xtext.als.Open#getRefname <em>Refname</em>}</li>
 *   <li>{@link fr.univartois.cril.xtext.als.Open#getRight <em>Right</em>}</li>
 *   <li>{@link fr.univartois.cril.xtext.als.Open#getNameAs <em>Name As</em>}</li>
 * </ul>
 * </p>
 *
 * @see fr.univartois.cril.xtext.als.AlsPackage#getOpen()
 * @model
 * @generated
 */
public interface Open extends EObject {

    /**
   * Returns the value of the '<em><b>Open Name</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Open Name</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Open Name</em>' containment reference.
   * @see #setOpenName(OpenName)
   * @see fr.univartois.cril.xtext.als.AlsPackage#getOpen_OpenName()
   * @model containment="true"
   * @generated
   */
    OpenName getOpenName();

    /**
   * Sets the value of the '{@link fr.univartois.cril.xtext.als.Open#getOpenName <em>Open Name</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Open Name</em>' containment reference.
   * @see #getOpenName()
   * @generated
   */
    void setOpenName(OpenName value);

    /**
   * Returns the value of the '<em><b>Left</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Left</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Left</em>' containment reference.
   * @see #setLeft(LeftSquareBracketKeyword)
   * @see fr.univartois.cril.xtext.als.AlsPackage#getOpen_Left()
   * @model containment="true"
   * @generated
   */
    LeftSquareBracketKeyword getLeft();

    /**
   * Sets the value of the '{@link fr.univartois.cril.xtext.als.Open#getLeft <em>Left</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Left</em>' containment reference.
   * @see #getLeft()
   * @generated
   */
    void setLeft(LeftSquareBracketKeyword value);

    /**
   * Returns the value of the '<em><b>Previous Name As</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Previous Name As</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Previous Name As</em>' containment reference.
   * @see #setPreviousNameAs(AsName)
   * @see fr.univartois.cril.xtext.als.AlsPackage#getOpen_PreviousNameAs()
   * @model containment="true"
   * @generated
   */
    AsName getPreviousNameAs();

    /**
   * Sets the value of the '{@link fr.univartois.cril.xtext.als.Open#getPreviousNameAs <em>Previous Name As</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Previous Name As</em>' containment reference.
   * @see #getPreviousNameAs()
   * @generated
   */
    void setPreviousNameAs(AsName value);

    /**
   * Returns the value of the '<em><b>Refname</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Refname</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Refname</em>' containment reference.
   * @see #setRefname(ReferencesName)
   * @see fr.univartois.cril.xtext.als.AlsPackage#getOpen_Refname()
   * @model containment="true"
   * @generated
   */
    ReferencesName getRefname();

    /**
   * Sets the value of the '{@link fr.univartois.cril.xtext.als.Open#getRefname <em>Refname</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Refname</em>' containment reference.
   * @see #getRefname()
   * @generated
   */
    void setRefname(ReferencesName value);

    /**
   * Returns the value of the '<em><b>Right</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Right</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Right</em>' containment reference.
   * @see #setRight(RightSquareBracketKeyword)
   * @see fr.univartois.cril.xtext.als.AlsPackage#getOpen_Right()
   * @model containment="true"
   * @generated
   */
    RightSquareBracketKeyword getRight();

    /**
   * Sets the value of the '{@link fr.univartois.cril.xtext.als.Open#getRight <em>Right</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Right</em>' containment reference.
   * @see #getRight()
   * @generated
   */
    void setRight(RightSquareBracketKeyword value);

    /**
   * Returns the value of the '<em><b>Name As</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Name As</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Name As</em>' containment reference.
   * @see #setNameAs(AsName)
   * @see fr.univartois.cril.xtext.als.AlsPackage#getOpen_NameAs()
   * @model containment="true"
   * @generated
   */
    AsName getNameAs();

    /**
   * Sets the value of the '{@link fr.univartois.cril.xtext.als.Open#getNameAs <em>Name As</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name As</em>' containment reference.
   * @see #getNameAs()
   * @generated
   */
    void setNameAs(AsName value);
}
