package org.larz.dom3.dm.dm.impl;

import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.larz.dom3.dm.dm.AbstractElement;
import org.larz.dom3.dm.dm.DmPackage;
import org.larz.dom3.dm.dm.Dom3Mod;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Dom3 Mod</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.larz.dom3.dm.dm.impl.Dom3ModImpl#getModname <em>Modname</em>}</li>
 *   <li>{@link org.larz.dom3.dm.dm.impl.Dom3ModImpl#getDesc <em>Desc</em>}</li>
 *   <li>{@link org.larz.dom3.dm.dm.impl.Dom3ModImpl#getIcon <em>Icon</em>}</li>
 *   <li>{@link org.larz.dom3.dm.dm.impl.Dom3ModImpl#getVersion <em>Version</em>}</li>
 *   <li>{@link org.larz.dom3.dm.dm.impl.Dom3ModImpl#getDomversion <em>Domversion</em>}</li>
 *   <li>{@link org.larz.dom3.dm.dm.impl.Dom3ModImpl#getElements <em>Elements</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class Dom3ModImpl extends MinimalEObjectImpl.Container implements Dom3Mod {

    /**
   * The default value of the '{@link #getModname() <em>Modname</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getModname()
   * @generated
   * @ordered
   */
    protected static final String MODNAME_EDEFAULT = null;

    /**
   * The cached value of the '{@link #getModname() <em>Modname</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getModname()
   * @generated
   * @ordered
   */
    protected String modname = MODNAME_EDEFAULT;

    /**
   * The default value of the '{@link #getDesc() <em>Desc</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDesc()
   * @generated
   * @ordered
   */
    protected static final String DESC_EDEFAULT = null;

    /**
   * The cached value of the '{@link #getDesc() <em>Desc</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDesc()
   * @generated
   * @ordered
   */
    protected String desc = DESC_EDEFAULT;

    /**
   * The default value of the '{@link #getIcon() <em>Icon</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getIcon()
   * @generated
   * @ordered
   */
    protected static final String ICON_EDEFAULT = null;

    /**
   * The cached value of the '{@link #getIcon() <em>Icon</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getIcon()
   * @generated
   * @ordered
   */
    protected String icon = ICON_EDEFAULT;

    /**
   * The default value of the '{@link #getVersion() <em>Version</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getVersion()
   * @generated
   * @ordered
   */
    protected static final String VERSION_EDEFAULT = null;

    /**
   * The cached value of the '{@link #getVersion() <em>Version</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getVersion()
   * @generated
   * @ordered
   */
    protected String version = VERSION_EDEFAULT;

    /**
   * The default value of the '{@link #getDomversion() <em>Domversion</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDomversion()
   * @generated
   * @ordered
   */
    protected static final String DOMVERSION_EDEFAULT = null;

    /**
   * The cached value of the '{@link #getDomversion() <em>Domversion</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDomversion()
   * @generated
   * @ordered
   */
    protected String domversion = DOMVERSION_EDEFAULT;

    /**
   * The cached value of the '{@link #getElements() <em>Elements</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getElements()
   * @generated
   * @ordered
   */
    protected EList<AbstractElement> elements;

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    protected Dom3ModImpl() {
        super();
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    protected EClass eStaticClass() {
        return DmPackage.eINSTANCE.getDom3Mod();
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public String getModname() {
        return modname;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setModname(String newModname) {
        String oldModname = modname;
        modname = newModname;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, DmPackage.DOM3_MOD__MODNAME, oldModname, modname));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public String getDesc() {
        return desc;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setDesc(String newDesc) {
        String oldDesc = desc;
        desc = newDesc;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, DmPackage.DOM3_MOD__DESC, oldDesc, desc));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public String getIcon() {
        return icon;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setIcon(String newIcon) {
        String oldIcon = icon;
        icon = newIcon;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, DmPackage.DOM3_MOD__ICON, oldIcon, icon));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public String getVersion() {
        return version;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setVersion(String newVersion) {
        String oldVersion = version;
        version = newVersion;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, DmPackage.DOM3_MOD__VERSION, oldVersion, version));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public String getDomversion() {
        return domversion;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setDomversion(String newDomversion) {
        String oldDomversion = domversion;
        domversion = newDomversion;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, DmPackage.DOM3_MOD__DOMVERSION, oldDomversion, domversion));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EList<AbstractElement> getElements() {
        if (elements == null) {
            elements = new EObjectContainmentEList<AbstractElement>(AbstractElement.class, this, DmPackage.DOM3_MOD__ELEMENTS);
        }
        return elements;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case DmPackage.DOM3_MOD__ELEMENTS:
                return ((InternalEList<?>) getElements()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case DmPackage.DOM3_MOD__MODNAME:
                return getModname();
            case DmPackage.DOM3_MOD__DESC:
                return getDesc();
            case DmPackage.DOM3_MOD__ICON:
                return getIcon();
            case DmPackage.DOM3_MOD__VERSION:
                return getVersion();
            case DmPackage.DOM3_MOD__DOMVERSION:
                return getDomversion();
            case DmPackage.DOM3_MOD__ELEMENTS:
                return getElements();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch(featureID) {
            case DmPackage.DOM3_MOD__MODNAME:
                setModname((String) newValue);
                return;
            case DmPackage.DOM3_MOD__DESC:
                setDesc((String) newValue);
                return;
            case DmPackage.DOM3_MOD__ICON:
                setIcon((String) newValue);
                return;
            case DmPackage.DOM3_MOD__VERSION:
                setVersion((String) newValue);
                return;
            case DmPackage.DOM3_MOD__DOMVERSION:
                setDomversion((String) newValue);
                return;
            case DmPackage.DOM3_MOD__ELEMENTS:
                getElements().clear();
                getElements().addAll((Collection<? extends AbstractElement>) newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    public void eUnset(int featureID) {
        switch(featureID) {
            case DmPackage.DOM3_MOD__MODNAME:
                setModname(MODNAME_EDEFAULT);
                return;
            case DmPackage.DOM3_MOD__DESC:
                setDesc(DESC_EDEFAULT);
                return;
            case DmPackage.DOM3_MOD__ICON:
                setIcon(ICON_EDEFAULT);
                return;
            case DmPackage.DOM3_MOD__VERSION:
                setVersion(VERSION_EDEFAULT);
                return;
            case DmPackage.DOM3_MOD__DOMVERSION:
                setDomversion(DOMVERSION_EDEFAULT);
                return;
            case DmPackage.DOM3_MOD__ELEMENTS:
                getElements().clear();
                return;
        }
        super.eUnset(featureID);
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    public boolean eIsSet(int featureID) {
        switch(featureID) {
            case DmPackage.DOM3_MOD__MODNAME:
                return MODNAME_EDEFAULT == null ? modname != null : !MODNAME_EDEFAULT.equals(modname);
            case DmPackage.DOM3_MOD__DESC:
                return DESC_EDEFAULT == null ? desc != null : !DESC_EDEFAULT.equals(desc);
            case DmPackage.DOM3_MOD__ICON:
                return ICON_EDEFAULT == null ? icon != null : !ICON_EDEFAULT.equals(icon);
            case DmPackage.DOM3_MOD__VERSION:
                return VERSION_EDEFAULT == null ? version != null : !VERSION_EDEFAULT.equals(version);
            case DmPackage.DOM3_MOD__DOMVERSION:
                return DOMVERSION_EDEFAULT == null ? domversion != null : !DOMVERSION_EDEFAULT.equals(domversion);
            case DmPackage.DOM3_MOD__ELEMENTS:
                return elements != null && !elements.isEmpty();
        }
        return super.eIsSet(featureID);
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    public String toString() {
        if (eIsProxy()) return super.toString();
        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (modname: ");
        result.append(modname);
        result.append(", desc: ");
        result.append(desc);
        result.append(", icon: ");
        result.append(icon);
        result.append(", version: ");
        result.append(version);
        result.append(", domversion: ");
        result.append(domversion);
        result.append(')');
        return result.toString();
    }
}
