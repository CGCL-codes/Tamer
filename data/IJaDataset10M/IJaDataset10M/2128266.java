package net.sf.devtool.casaapp.guiDSL;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Screen</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.sf.devtool.casaapp.guiDSL.Screen#getName <em>Name</em>}</li>
 *   <li>{@link net.sf.devtool.casaapp.guiDSL.Screen#getScreenkind <em>Screenkind</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.sf.devtool.casaapp.guiDSL.GuiDSLPackage#getScreen()
 * @model
 * @generated
 */
public interface Screen extends EObject {

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
   * @see net.sf.devtool.casaapp.guiDSL.GuiDSLPackage#getScreen_Name()
   * @model
   * @generated
   */
    String getName();

    /**
   * Sets the value of the '{@link net.sf.devtool.casaapp.guiDSL.Screen#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
    void setName(String value);

    /**
   * Returns the value of the '<em><b>Screenkind</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Screenkind</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Screenkind</em>' containment reference.
   * @see #setScreenkind(ScreenKind)
   * @see net.sf.devtool.casaapp.guiDSL.GuiDSLPackage#getScreen_Screenkind()
   * @model containment="true"
   * @generated
   */
    ScreenKind getScreenkind();

    /**
   * Sets the value of the '{@link net.sf.devtool.casaapp.guiDSL.Screen#getScreenkind <em>Screenkind</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Screenkind</em>' containment reference.
   * @see #getScreenkind()
   * @generated
   */
    void setScreenkind(ScreenKind value);
}
