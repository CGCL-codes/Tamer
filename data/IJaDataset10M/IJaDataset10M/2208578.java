package net.sf.rcer.rfcgen.mapping;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Function Module POJO Parameter Mapping</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.sf.rcer.rfcgen.mapping.FunctionModulePOJOParameterMapping#getName <em>Name</em>}</li>
 *   <li>{@link net.sf.rcer.rfcgen.mapping.FunctionModulePOJOParameterMapping#isInactive <em>Inactive</em>}</li>
 *   <li>{@link net.sf.rcer.rfcgen.mapping.FunctionModulePOJOParameterMapping#getAttribute <em>Attribute</em>}</li>
 *   <li>{@link net.sf.rcer.rfcgen.mapping.FunctionModulePOJOParameterMapping#getStructureMapping <em>Structure Mapping</em>}</li>
 *   <li>{@link net.sf.rcer.rfcgen.mapping.FunctionModulePOJOParameterMapping#isExternal <em>External</em>}</li>
 *   <li>{@link net.sf.rcer.rfcgen.mapping.FunctionModulePOJOParameterMapping#getExternalClass <em>External Class</em>}</li>
 *   <li>{@link net.sf.rcer.rfcgen.mapping.FunctionModulePOJOParameterMapping#getComment <em>Comment</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.sf.rcer.rfcgen.mapping.MappingPackage#getFunctionModulePOJOParameterMapping()
 * @model
 * @generated
 */
public interface FunctionModulePOJOParameterMapping extends EObject {

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
   * @see net.sf.rcer.rfcgen.mapping.MappingPackage#getFunctionModulePOJOParameterMapping_Name()
   * @model
   * @generated
   */
    String getName();

    /**
   * Sets the value of the '{@link net.sf.rcer.rfcgen.mapping.FunctionModulePOJOParameterMapping#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
    void setName(String value);

    /**
   * Returns the value of the '<em><b>Inactive</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Inactive</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Inactive</em>' attribute.
   * @see #setInactive(boolean)
   * @see net.sf.rcer.rfcgen.mapping.MappingPackage#getFunctionModulePOJOParameterMapping_Inactive()
   * @model
   * @generated
   */
    boolean isInactive();

    /**
   * Sets the value of the '{@link net.sf.rcer.rfcgen.mapping.FunctionModulePOJOParameterMapping#isInactive <em>Inactive</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Inactive</em>' attribute.
   * @see #isInactive()
   * @generated
   */
    void setInactive(boolean value);

    /**
   * Returns the value of the '<em><b>Attribute</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Attribute</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Attribute</em>' attribute.
   * @see #setAttribute(String)
   * @see net.sf.rcer.rfcgen.mapping.MappingPackage#getFunctionModulePOJOParameterMapping_Attribute()
   * @model
   * @generated
   */
    String getAttribute();

    /**
   * Sets the value of the '{@link net.sf.rcer.rfcgen.mapping.FunctionModulePOJOParameterMapping#getAttribute <em>Attribute</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Attribute</em>' attribute.
   * @see #getAttribute()
   * @generated
   */
    void setAttribute(String value);

    /**
   * Returns the value of the '<em><b>Structure Mapping</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Structure Mapping</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Structure Mapping</em>' reference.
   * @see #setStructureMapping(StructurePOJOMapping)
   * @see net.sf.rcer.rfcgen.mapping.MappingPackage#getFunctionModulePOJOParameterMapping_StructureMapping()
   * @model
   * @generated
   */
    StructurePOJOMapping getStructureMapping();

    /**
   * Sets the value of the '{@link net.sf.rcer.rfcgen.mapping.FunctionModulePOJOParameterMapping#getStructureMapping <em>Structure Mapping</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Structure Mapping</em>' reference.
   * @see #getStructureMapping()
   * @generated
   */
    void setStructureMapping(StructurePOJOMapping value);

    /**
   * Returns the value of the '<em><b>External</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>External</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>External</em>' attribute.
   * @see #setExternal(boolean)
   * @see net.sf.rcer.rfcgen.mapping.MappingPackage#getFunctionModulePOJOParameterMapping_External()
   * @model
   * @generated
   */
    boolean isExternal();

    /**
   * Sets the value of the '{@link net.sf.rcer.rfcgen.mapping.FunctionModulePOJOParameterMapping#isExternal <em>External</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>External</em>' attribute.
   * @see #isExternal()
   * @generated
   */
    void setExternal(boolean value);

    /**
   * Returns the value of the '<em><b>External Class</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>External Class</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>External Class</em>' reference.
   * @see #setExternalClass(Import)
   * @see net.sf.rcer.rfcgen.mapping.MappingPackage#getFunctionModulePOJOParameterMapping_ExternalClass()
   * @model
   * @generated
   */
    Import getExternalClass();

    /**
   * Sets the value of the '{@link net.sf.rcer.rfcgen.mapping.FunctionModulePOJOParameterMapping#getExternalClass <em>External Class</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>External Class</em>' reference.
   * @see #getExternalClass()
   * @generated
   */
    void setExternalClass(Import value);

    /**
   * Returns the value of the '<em><b>Comment</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Comment</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Comment</em>' attribute.
   * @see #setComment(String)
   * @see net.sf.rcer.rfcgen.mapping.MappingPackage#getFunctionModulePOJOParameterMapping_Comment()
   * @model
   * @generated
   */
    String getComment();

    /**
   * Sets the value of the '{@link net.sf.rcer.rfcgen.mapping.FunctionModulePOJOParameterMapping#getComment <em>Comment</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Comment</em>' attribute.
   * @see #getComment()
   * @generated
   */
    void setComment(String value);
}
