package net.sf.devtool.casaapp.guiDSL;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Input Property</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.sf.devtool.casaapp.guiDSL.InputProperty#getValue <em>Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.sf.devtool.casaapp.guiDSL.GuiDSLPackage#getInputProperty()
 * @model
 * @generated
 */
public interface InputProperty extends EObject {

    /**
   * Returns the value of the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Value</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Value</em>' attribute.
   * @see #setValue(String)
   * @see net.sf.devtool.casaapp.guiDSL.GuiDSLPackage#getInputProperty_Value()
   * @model
   * @generated
   */
    String getValue();

    /**
   * Sets the value of the '{@link net.sf.devtool.casaapp.guiDSL.InputProperty#getValue <em>Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Value</em>' attribute.
   * @see #getValue()
   * @generated
   */
    void setValue(String value);
}
