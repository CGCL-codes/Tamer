package it.unisannio.rcost.callgraphanalyzer;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Interface</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link it.unisannio.rcost.callgraphanalyzer.Interface#getMethodsList <em>Methods</em>}</li>
 *   <li>{@link it.unisannio.rcost.callgraphanalyzer.Interface#getFieldsList <em>Fields</em>}</li>
 *   <li>{@link it.unisannio.rcost.callgraphanalyzer.Interface#isIsStatic <em>Is Static</em>}</li>
 *   <li>{@link it.unisannio.rcost.callgraphanalyzer.Interface#isIsStrictFp <em>Is Strict Fp</em>}</li>
 *   <li>{@link it.unisannio.rcost.callgraphanalyzer.Interface#getScopeModifier <em>Scope Modifier</em>}</li>
 *   <li>{@link it.unisannio.rcost.callgraphanalyzer.Interface#getFlagsModifier <em>Flags Modifier</em>}</li>
 *   <li>{@link it.unisannio.rcost.callgraphanalyzer.Interface#getInnerModulesList <em>Inner Modules</em>}</li>
 *   <li>{@link it.unisannio.rcost.callgraphanalyzer.Interface#getCompilationUnit <em>Compilation Unit</em>}</li>
 * </ul>
 * </p>
 *
 * @see it.unisannio.rcost.callgraphanalyzer.CallGraphPackage#getInterface()
 * @model
 * @generated
 */
public interface Interface extends NodeContainer {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    Method[] getMethods();

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    Method getMethods(int index);

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    int getMethodsLength();

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    void setMethods(Method[] newMethods);

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    void setMethods(int index, Method element);

    /**
	 * Returns the value of the '<em><b>Methods</b></em>' containment reference list.
	 * The list contents are of type {@link it.unisannio.rcost.callgraphanalyzer.Method}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Methods</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Methods</em>' containment reference list.
	 * @see it.unisannio.rcost.callgraphanalyzer.CallGraphPackage#getInterface_Methods()
	 * @model containment="true"
	 * @generated
	 */
    EList<Method> getMethodsList();

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    Field[] getFields();

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    Field getFields(int index);

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    int getFieldsLength();

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    void setFields(Field[] newFields);

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    void setFields(int index, Field element);

    /**
	 * Returns the value of the '<em><b>Fields</b></em>' containment reference list.
	 * The list contents are of type {@link it.unisannio.rcost.callgraphanalyzer.Field}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fields</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fields</em>' containment reference list.
	 * @see it.unisannio.rcost.callgraphanalyzer.CallGraphPackage#getInterface_Fields()
	 * @model containment="true"
	 * @generated
	 */
    EList<Field> getFieldsList();

    /**
	 * Returns the value of the '<em><b>Is Static</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Is Static</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Is Static</em>' attribute.
	 * @see #setIsStatic(boolean)
	 * @see it.unisannio.rcost.callgraphanalyzer.CallGraphPackage#getInterface_IsStatic()
	 * @model
	 * @generated
	 */
    boolean isIsStatic();

    /**
	 * Sets the value of the '{@link it.unisannio.rcost.callgraphanalyzer.Interface#isIsStatic <em>Is Static</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Is Static</em>' attribute.
	 * @see #isIsStatic()
	 * @generated
	 */
    void setIsStatic(boolean value);

    /**
	 * Returns the value of the '<em><b>Is Strict Fp</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Is Strict Fp</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Is Strict Fp</em>' attribute.
	 * @see #setIsStrictFp(boolean)
	 * @see it.unisannio.rcost.callgraphanalyzer.CallGraphPackage#getInterface_IsStrictFp()
	 * @model
	 * @generated
	 */
    boolean isIsStrictFp();

    /**
	 * Sets the value of the '{@link it.unisannio.rcost.callgraphanalyzer.Interface#isIsStrictFp <em>Is Strict Fp</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Is Strict Fp</em>' attribute.
	 * @see #isIsStrictFp()
	 * @generated
	 */
    void setIsStrictFp(boolean value);

    /**
	 * Returns the value of the '<em><b>Scope Modifier</b></em>' attribute.
	 * The literals are from the enumeration {@link it.unisannio.rcost.callgraphanalyzer.ScopeModifier}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Scope Modifier</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Scope Modifier</em>' attribute.
	 * @see it.unisannio.rcost.callgraphanalyzer.ScopeModifier
	 * @see #setScopeModifier(ScopeModifier)
	 * @see it.unisannio.rcost.callgraphanalyzer.CallGraphPackage#getInterface_ScopeModifier()
	 * @model
	 * @generated
	 */
    ScopeModifier getScopeModifier();

    /**
	 * Sets the value of the '{@link it.unisannio.rcost.callgraphanalyzer.Interface#getScopeModifier <em>Scope Modifier</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Scope Modifier</em>' attribute.
	 * @see it.unisannio.rcost.callgraphanalyzer.ScopeModifier
	 * @see #getScopeModifier()
	 * @generated
	 */
    void setScopeModifier(ScopeModifier value);

    /**
	 * Returns the value of the '<em><b>Flags Modifier</b></em>' attribute.
	 * The literals are from the enumeration {@link it.unisannio.rcost.callgraphanalyzer.FlagsModifier}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Flags Modifier</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Flags Modifier</em>' attribute.
	 * @see it.unisannio.rcost.callgraphanalyzer.FlagsModifier
	 * @see #setFlagsModifier(FlagsModifier)
	 * @see it.unisannio.rcost.callgraphanalyzer.CallGraphPackage#getInterface_FlagsModifier()
	 * @model
	 * @generated
	 */
    FlagsModifier getFlagsModifier();

    /**
	 * Sets the value of the '{@link it.unisannio.rcost.callgraphanalyzer.Interface#getFlagsModifier <em>Flags Modifier</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Flags Modifier</em>' attribute.
	 * @see it.unisannio.rcost.callgraphanalyzer.FlagsModifier
	 * @see #getFlagsModifier()
	 * @generated
	 */
    void setFlagsModifier(FlagsModifier value);

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    Interface[] getInnerModules();

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    Interface getInnerModules(int index);

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    int getInnerModulesLength();

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    void setInnerModules(Interface[] newInnerModules);

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    void setInnerModules(int index, Interface element);

    /**
	 * Returns the value of the '<em><b>Inner Modules</b></em>' containment reference list.
	 * The list contents are of type {@link it.unisannio.rcost.callgraphanalyzer.Interface}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Inner Modules</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Inner Modules</em>' containment reference list.
	 * @see it.unisannio.rcost.callgraphanalyzer.CallGraphPackage#getInterface_InnerModules()
	 * @model containment="true"
	 * @generated
	 */
    EList<Interface> getInnerModulesList();

    /**
	 * Returns the value of the '<em><b>Compilation Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Compilation Unit</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Compilation Unit</em>' attribute.
	 * @see #setCompilationUnit(String)
	 * @see it.unisannio.rcost.callgraphanalyzer.CallGraphPackage#getInterface_CompilationUnit()
	 * @model
	 * @generated
	 */
    String getCompilationUnit();

    /**
	 * Sets the value of the '{@link it.unisannio.rcost.callgraphanalyzer.Interface#getCompilationUnit <em>Compilation Unit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Compilation Unit</em>' attribute.
	 * @see #getCompilationUnit()
	 * @generated
	 */
    void setCompilationUnit(String value);

    void addInnerModule(Interface module);

    void addMethod(Method method);

    void addField(Field field);
}
