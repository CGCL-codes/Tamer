package org.emftext.language.models;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.emftext.language.models.Model#getName <em>Name</em>}</li>
 *   <li>{@link org.emftext.language.models.Model#getSize1 <em>Size1</em>}</li>
 *   <li>{@link org.emftext.language.models.Model#getSize2 <em>Size2</em>}</li>
 *   <li>{@link org.emftext.language.models.Model#getSize3 <em>Size3</em>}</li>
 *   <li>{@link org.emftext.language.models.Model#getHair <em>Hair</em>}</li>
 *   <li>{@link org.emftext.language.models.Model#getLips <em>Lips</em>}</li>
 *   <li>{@link org.emftext.language.models.Model#getEyes <em>Eyes</em>}</li>
 *   <li>{@link org.emftext.language.models.Model#getLegs <em>Legs</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.emftext.language.models.ModelsPackage#getModel()
 * @model
 * @generated
 */
public interface Model extends EObject {

    /**
   * Returns the value of the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Name</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Name</em>' attribute.
   * @see #setName(String)
   * @see org.emftext.language.models.ModelsPackage#getModel_Name()
   * @model
   * @generated
   */
    String getName();

    /**
   * Sets the value of the '{@link org.emftext.language.models.Model#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
    void setName(String value);

    /**
   * Returns the value of the '<em><b>Size1</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Size1</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Size1</em>' attribute.
   * @see #setSize1(int)
   * @see org.emftext.language.models.ModelsPackage#getModel_Size1()
   * @model
   * @generated
   */
    int getSize1();

    /**
   * Sets the value of the '{@link org.emftext.language.models.Model#getSize1 <em>Size1</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Size1</em>' attribute.
   * @see #getSize1()
   * @generated
   */
    void setSize1(int value);

    /**
   * Returns the value of the '<em><b>Size2</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Size2</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Size2</em>' attribute.
   * @see #setSize2(int)
   * @see org.emftext.language.models.ModelsPackage#getModel_Size2()
   * @model
   * @generated
   */
    int getSize2();

    /**
   * Sets the value of the '{@link org.emftext.language.models.Model#getSize2 <em>Size2</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Size2</em>' attribute.
   * @see #getSize2()
   * @generated
   */
    void setSize2(int value);

    /**
   * Returns the value of the '<em><b>Size3</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Size3</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Size3</em>' attribute.
   * @see #setSize3(int)
   * @see org.emftext.language.models.ModelsPackage#getModel_Size3()
   * @model
   * @generated
   */
    int getSize3();

    /**
   * Sets the value of the '{@link org.emftext.language.models.Model#getSize3 <em>Size3</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Size3</em>' attribute.
   * @see #getSize3()
   * @generated
   */
    void setSize3(int value);

    /**
   * Returns the value of the '<em><b>Hair</b></em>' attribute.
   * The literals are from the enumeration {@link org.emftext.language.models.Color}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Hair</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Hair</em>' attribute.
   * @see org.emftext.language.models.Color
   * @see #setHair(Color)
   * @see org.emftext.language.models.ModelsPackage#getModel_Hair()
   * @model
   * @generated
   */
    Color getHair();

    /**
   * Sets the value of the '{@link org.emftext.language.models.Model#getHair <em>Hair</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Hair</em>' attribute.
   * @see org.emftext.language.models.Color
   * @see #getHair()
   * @generated
   */
    void setHair(Color value);

    /**
   * Returns the value of the '<em><b>Lips</b></em>' attribute.
   * The literals are from the enumeration {@link org.emftext.language.models.Color}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Lips</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Lips</em>' attribute.
   * @see org.emftext.language.models.Color
   * @see #setLips(Color)
   * @see org.emftext.language.models.ModelsPackage#getModel_Lips()
   * @model
   * @generated
   */
    Color getLips();

    /**
   * Sets the value of the '{@link org.emftext.language.models.Model#getLips <em>Lips</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Lips</em>' attribute.
   * @see org.emftext.language.models.Color
   * @see #getLips()
   * @generated
   */
    void setLips(Color value);

    /**
   * Returns the value of the '<em><b>Eyes</b></em>' attribute.
   * The literals are from the enumeration {@link org.emftext.language.models.Color}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Eyes</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Eyes</em>' attribute.
   * @see org.emftext.language.models.Color
   * @see #setEyes(Color)
   * @see org.emftext.language.models.ModelsPackage#getModel_Eyes()
   * @model
   * @generated
   */
    Color getEyes();

    /**
   * Sets the value of the '{@link org.emftext.language.models.Model#getEyes <em>Eyes</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Eyes</em>' attribute.
   * @see org.emftext.language.models.Color
   * @see #getEyes()
   * @generated
   */
    void setEyes(Color value);

    /**
   * Returns the value of the '<em><b>Legs</b></em>' attribute.
   * The literals are from the enumeration {@link org.emftext.language.models.Size}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Legs</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Legs</em>' attribute.
   * @see org.emftext.language.models.Size
   * @see #setLegs(Size)
   * @see org.emftext.language.models.ModelsPackage#getModel_Legs()
   * @model
   * @generated
   */
    Size getLegs();

    /**
   * Sets the value of the '{@link org.emftext.language.models.Model#getLegs <em>Legs</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Legs</em>' attribute.
   * @see org.emftext.language.models.Size
   * @see #getLegs()
   * @generated
   */
    void setLegs(Size value);
}
