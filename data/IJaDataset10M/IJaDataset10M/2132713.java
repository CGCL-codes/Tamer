package Slee11;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Usage Parameter Name Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link Slee11.UsageParameterNameType#getValue <em>Value</em>}</li>
 *   <li>{@link Slee11.UsageParameterNameType#getId <em>Id</em>}</li>
 *   <li>{@link Slee11.UsageParameterNameType#getNotificationsEnabled <em>Notifications Enabled</em>}</li>
 * </ul>
 * </p>
 *
 * @see Slee11.Slee11Package#getUsageParameterNameType()
 * @model extendedMetaData="name='usage-parameter-name_._type' kind='simple'"
 * @generated
 */
public interface UsageParameterNameType extends EObject {

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
	 * @see Slee11.Slee11Package#getUsageParameterNameType_Value()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="name=':0' kind='simple'"
	 * @generated
	 */
    String getValue();

    /**
	 * Sets the value of the '{@link Slee11.UsageParameterNameType#getValue <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value</em>' attribute.
	 * @see #getValue()
	 * @generated
	 */
    void setValue(String value);

    /**
	 * Returns the value of the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Id</em>' attribute.
	 * @see #setId(String)
	 * @see Slee11.Slee11Package#getUsageParameterNameType_Id()
	 * @model id="true" dataType="org.eclipse.emf.ecore.xml.type.ID"
	 *        extendedMetaData="kind='attribute' name='id' namespace='##targetNamespace'"
	 * @generated
	 */
    String getId();

    /**
	 * Sets the value of the '{@link Slee11.UsageParameterNameType#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
    void setId(String value);

    /**
	 * Returns the value of the '<em><b>Notifications Enabled</b></em>' attribute.
	 * The literals are from the enumeration {@link Slee11.NotificationsEnabledType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Notifications Enabled</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Notifications Enabled</em>' attribute.
	 * @see Slee11.NotificationsEnabledType
	 * @see #isSetNotificationsEnabled()
	 * @see #unsetNotificationsEnabled()
	 * @see #setNotificationsEnabled(NotificationsEnabledType)
	 * @see Slee11.Slee11Package#getUsageParameterNameType_NotificationsEnabled()
	 * @model unsettable="true" required="true"
	 *        extendedMetaData="kind='attribute' name='notifications-enabled' namespace='##targetNamespace'"
	 * @generated
	 */
    NotificationsEnabledType getNotificationsEnabled();

    /**
	 * Sets the value of the '{@link Slee11.UsageParameterNameType#getNotificationsEnabled <em>Notifications Enabled</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Notifications Enabled</em>' attribute.
	 * @see Slee11.NotificationsEnabledType
	 * @see #isSetNotificationsEnabled()
	 * @see #unsetNotificationsEnabled()
	 * @see #getNotificationsEnabled()
	 * @generated
	 */
    void setNotificationsEnabled(NotificationsEnabledType value);

    /**
	 * Unsets the value of the '{@link Slee11.UsageParameterNameType#getNotificationsEnabled <em>Notifications Enabled</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetNotificationsEnabled()
	 * @see #getNotificationsEnabled()
	 * @see #setNotificationsEnabled(NotificationsEnabledType)
	 * @generated
	 */
    void unsetNotificationsEnabled();

    /**
	 * Returns whether the value of the '{@link Slee11.UsageParameterNameType#getNotificationsEnabled <em>Notifications Enabled</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Notifications Enabled</em>' attribute is set.
	 * @see #unsetNotificationsEnabled()
	 * @see #getNotificationsEnabled()
	 * @see #setNotificationsEnabled(NotificationsEnabledType)
	 * @generated
	 */
    boolean isSetNotificationsEnabled();
}
