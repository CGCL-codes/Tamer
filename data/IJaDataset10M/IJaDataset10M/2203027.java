package net.sf.rcer.rom.abapobj;

import net.sf.rcer.conn.locales.Locale;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Interface Method</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.sf.rcer.rom.abapobj.InterfaceMethod#getName <em>Name</em>}</li>
 *   <li>{@link net.sf.rcer.rom.abapobj.InterfaceMethod#getDescription <em>Description</em>}</li>
 *   <li>{@link net.sf.rcer.rom.abapobj.InterfaceMethod#getScope <em>Scope</em>}</li>
 *   <li>{@link net.sf.rcer.rom.abapobj.InterfaceMethod#isEventHandler <em>Event Handler</em>}</li>
 *   <li>{@link net.sf.rcer.rom.abapobj.InterfaceMethod#getEventClass <em>Event Class</em>}</li>
 *   <li>{@link net.sf.rcer.rom.abapobj.InterfaceMethod#getEventName <em>Event Name</em>}</li>
 *   <li>{@link net.sf.rcer.rom.abapobj.InterfaceMethod#isClassBasedExceptions <em>Class Based Exceptions</em>}</li>
 *   <li>{@link net.sf.rcer.rom.abapobj.InterfaceMethod#getParameters <em>Parameters</em>}</li>
 *   <li>{@link net.sf.rcer.rom.abapobj.InterfaceMethod#getExceptions <em>Exceptions</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.sf.rcer.rom.abapobj.ABAPObjectsPackage#getInterfaceMethod()
 * @model
 * @generated
 */
public interface InterfaceMethod extends EObject {

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
	 * @see net.sf.rcer.rom.abapobj.ABAPObjectsPackage#getInterfaceMethod_Name()
	 * @model unique="false" required="true" ordered="false"
	 * @generated
	 */
    String getName();

    /**
	 * Sets the value of the '{@link net.sf.rcer.rom.abapobj.InterfaceMethod#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
    void setName(String value);

    /**
	 * Returns the value of the '<em><b>Description</b></em>' map.
	 * The key is of type {@link net.sf.rcer.conn.locales.Locale},
	 * and the value is of type {@link java.lang.String},
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Description</em>' map isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Description</em>' map.
	 * @see net.sf.rcer.rom.abapobj.ABAPObjectsPackage#getInterfaceMethod_Description()
	 * @model mapType="net.sf.rcer.rom.LocalizedString<net.sf.rcer.rom.Locale, org.eclipse.emf.ecore.EString>" changeable="false" ordered="false"
	 * @generated
	 */
    EMap<Locale, String> getDescription();

    /**
	 * Returns the value of the '<em><b>Scope</b></em>' attribute.
	 * The literals are from the enumeration {@link net.sf.rcer.rom.abapobj.MethodScope}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Scope</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Scope</em>' attribute.
	 * @see net.sf.rcer.rom.abapobj.MethodScope
	 * @see #setScope(MethodScope)
	 * @see net.sf.rcer.rom.abapobj.ABAPObjectsPackage#getInterfaceMethod_Scope()
	 * @model unique="false" required="true" ordered="false"
	 * @generated
	 */
    MethodScope getScope();

    /**
	 * Sets the value of the '{@link net.sf.rcer.rom.abapobj.InterfaceMethod#getScope <em>Scope</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Scope</em>' attribute.
	 * @see net.sf.rcer.rom.abapobj.MethodScope
	 * @see #getScope()
	 * @generated
	 */
    void setScope(MethodScope value);

    /**
	 * Returns the value of the '<em><b>Event Handler</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Event Handler</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Event Handler</em>' attribute.
	 * @see #setEventHandler(boolean)
	 * @see net.sf.rcer.rom.abapobj.ABAPObjectsPackage#getInterfaceMethod_EventHandler()
	 * @model unique="false" required="true" ordered="false"
	 * @generated
	 */
    boolean isEventHandler();

    /**
	 * Sets the value of the '{@link net.sf.rcer.rom.abapobj.InterfaceMethod#isEventHandler <em>Event Handler</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Event Handler</em>' attribute.
	 * @see #isEventHandler()
	 * @generated
	 */
    void setEventHandler(boolean value);

    /**
	 * Returns the value of the '<em><b>Event Class</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Event Class</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Event Class</em>' attribute.
	 * @see #setEventClass(String)
	 * @see net.sf.rcer.rom.abapobj.ABAPObjectsPackage#getInterfaceMethod_EventClass()
	 * @model unique="false" ordered="false"
	 * @generated
	 */
    String getEventClass();

    /**
	 * Sets the value of the '{@link net.sf.rcer.rom.abapobj.InterfaceMethod#getEventClass <em>Event Class</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Event Class</em>' attribute.
	 * @see #getEventClass()
	 * @generated
	 */
    void setEventClass(String value);

    /**
	 * Returns the value of the '<em><b>Event Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Event Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Event Name</em>' attribute.
	 * @see #setEventName(String)
	 * @see net.sf.rcer.rom.abapobj.ABAPObjectsPackage#getInterfaceMethod_EventName()
	 * @model unique="false" ordered="false"
	 * @generated
	 */
    String getEventName();

    /**
	 * Sets the value of the '{@link net.sf.rcer.rom.abapobj.InterfaceMethod#getEventName <em>Event Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Event Name</em>' attribute.
	 * @see #getEventName()
	 * @generated
	 */
    void setEventName(String value);

    /**
	 * Returns the value of the '<em><b>Class Based Exceptions</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Class Based Exceptions</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Class Based Exceptions</em>' attribute.
	 * @see #setClassBasedExceptions(boolean)
	 * @see net.sf.rcer.rom.abapobj.ABAPObjectsPackage#getInterfaceMethod_ClassBasedExceptions()
	 * @model unique="false" required="true" ordered="false"
	 * @generated
	 */
    boolean isClassBasedExceptions();

    /**
	 * Sets the value of the '{@link net.sf.rcer.rom.abapobj.InterfaceMethod#isClassBasedExceptions <em>Class Based Exceptions</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Class Based Exceptions</em>' attribute.
	 * @see #isClassBasedExceptions()
	 * @generated
	 */
    void setClassBasedExceptions(boolean value);

    /**
	 * Returns the value of the '<em><b>Parameters</b></em>' reference list.
	 * The list contents are of type {@link net.sf.rcer.rom.abapobj.MethodParameter}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parameters</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parameters</em>' reference list.
	 * @see net.sf.rcer.rom.abapobj.ABAPObjectsPackage#getInterfaceMethod_Parameters()
	 * @model keys="name"
	 * @generated
	 */
    EList<MethodParameter> getParameters();

    /**
	 * Returns the value of the '<em><b>Exceptions</b></em>' reference list.
	 * The list contents are of type {@link net.sf.rcer.rom.abapobj.MethodException}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Exceptions</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Exceptions</em>' reference list.
	 * @see net.sf.rcer.rom.abapobj.ABAPObjectsPackage#getInterfaceMethod_Exceptions()
	 * @model keys="name"
	 * @generated
	 */
    EList<MethodException> getExceptions();
}
