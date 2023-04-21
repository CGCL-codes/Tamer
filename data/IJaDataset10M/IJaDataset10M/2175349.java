package net.sf.rcer.rfcgen.mapping;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Function Module POJO Request Response Mapping</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.sf.rcer.rfcgen.mapping.FunctionModulePOJORequestResponseMapping#getRequestClassName <em>Request Class Name</em>}</li>
 *   <li>{@link net.sf.rcer.rfcgen.mapping.FunctionModulePOJORequestResponseMapping#getRequestParameters <em>Request Parameters</em>}</li>
 *   <li>{@link net.sf.rcer.rfcgen.mapping.FunctionModulePOJORequestResponseMapping#getResponseClassName <em>Response Class Name</em>}</li>
 *   <li>{@link net.sf.rcer.rfcgen.mapping.FunctionModulePOJORequestResponseMapping#getResponseParameters <em>Response Parameters</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.sf.rcer.rfcgen.mapping.MappingPackage#getFunctionModulePOJORequestResponseMapping()
 * @model
 * @generated
 */
public interface FunctionModulePOJORequestResponseMapping extends FunctionModulePOJOMapping {

    /**
   * Returns the value of the '<em><b>Request Class Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Request Class Name</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Request Class Name</em>' attribute.
   * @see #setRequestClassName(String)
   * @see net.sf.rcer.rfcgen.mapping.MappingPackage#getFunctionModulePOJORequestResponseMapping_RequestClassName()
   * @model
   * @generated
   */
    String getRequestClassName();

    /**
   * Sets the value of the '{@link net.sf.rcer.rfcgen.mapping.FunctionModulePOJORequestResponseMapping#getRequestClassName <em>Request Class Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Request Class Name</em>' attribute.
   * @see #getRequestClassName()
   * @generated
   */
    void setRequestClassName(String value);

    /**
   * Returns the value of the '<em><b>Request Parameters</b></em>' containment reference list.
   * The list contents are of type {@link net.sf.rcer.rfcgen.mapping.FunctionModulePOJOParameterMapping}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Request Parameters</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Request Parameters</em>' containment reference list.
   * @see net.sf.rcer.rfcgen.mapping.MappingPackage#getFunctionModulePOJORequestResponseMapping_RequestParameters()
   * @model containment="true"
   * @generated
   */
    EList<FunctionModulePOJOParameterMapping> getRequestParameters();

    /**
   * Returns the value of the '<em><b>Response Class Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Response Class Name</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Response Class Name</em>' attribute.
   * @see #setResponseClassName(String)
   * @see net.sf.rcer.rfcgen.mapping.MappingPackage#getFunctionModulePOJORequestResponseMapping_ResponseClassName()
   * @model
   * @generated
   */
    String getResponseClassName();

    /**
   * Sets the value of the '{@link net.sf.rcer.rfcgen.mapping.FunctionModulePOJORequestResponseMapping#getResponseClassName <em>Response Class Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Response Class Name</em>' attribute.
   * @see #getResponseClassName()
   * @generated
   */
    void setResponseClassName(String value);

    /**
   * Returns the value of the '<em><b>Response Parameters</b></em>' containment reference list.
   * The list contents are of type {@link net.sf.rcer.rfcgen.mapping.FunctionModulePOJOParameterMapping}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Response Parameters</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Response Parameters</em>' containment reference list.
   * @see net.sf.rcer.rfcgen.mapping.MappingPackage#getFunctionModulePOJORequestResponseMapping_ResponseParameters()
   * @model containment="true"
   * @generated
   */
    EList<FunctionModulePOJOParameterMapping> getResponseParameters();
}
