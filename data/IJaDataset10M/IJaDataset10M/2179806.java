package de.abg.jreichert.serviceqos.model.wsdl.impl;

import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;
import de.abg.jreichert.serviceqos.model.wsdl.DescriptionType;
import de.abg.jreichert.serviceqos.model.wsdl.DocumentRoot;
import de.abg.jreichert.serviceqos.model.wsdl.EndpointType;
import de.abg.jreichert.serviceqos.model.wsdl.InterfaceType;
import de.abg.jreichert.serviceqos.model.wsdl.ServiceType;
import de.abg.jreichert.serviceqos.model.wsdl.WsdlPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Service Type</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>
 * {@link de.abg.jreichert.serviceqos.model.wsdl.impl.ServiceTypeImpl#getName
 * <em>Name</em>}</li>
 * <li>
 * {@link de.abg.jreichert.serviceqos.model.wsdl.impl.ServiceTypeImpl#getGroup
 * <em>Group</em>}</li>
 * <li>
 * {@link de.abg.jreichert.serviceqos.model.wsdl.impl.ServiceTypeImpl#getInterface
 * <em>Interface</em>}</li>
 * <li>
 * {@link de.abg.jreichert.serviceqos.model.wsdl.impl.ServiceTypeImpl#getReferencedInterface
 * <em>Referenced Interface</em>}</li>
 * <li>
 * {@link de.abg.jreichert.serviceqos.model.wsdl.impl.ServiceTypeImpl#getEndpoint
 * <em>Endpoint</em>}</li>
 * <li>
 * {@link de.abg.jreichert.serviceqos.model.wsdl.impl.ServiceTypeImpl#getAny
 * <em>Any</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class ServiceTypeImpl extends ServiceComponentImpl implements ServiceType {

    /**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
    protected static final String NAME_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
    protected String name = NAME_EDEFAULT;

    /**
	 * The cached value of the '{@link #getGroup() <em>Group</em>}' attribute
	 * list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getGroup()
	 * @generated
	 * @ordered
	 */
    protected FeatureMap group;

    /**
	 * The default value of the '{@link #getInterface() <em>Interface</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getInterface()
	 * @generated
	 * @ordered
	 */
    protected static final String INTERFACE_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getInterface() <em>Interface</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getInterface()
	 * @generated
	 * @ordered
	 */
    protected String interface_ = INTERFACE_EDEFAULT;

    /**
	 * The cached value of the '{@link #getReferencedInterface()
	 * <em>Referenced Interface</em>}' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getReferencedInterface()
	 * @generated
	 * @ordered
	 */
    protected InterfaceType referencedInterface;

    /**
	 * This is true if the Referenced Interface reference has been set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
    protected boolean referencedInterfaceESet;

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    protected ServiceTypeImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return WsdlPackage.Literals.SERVICE_TYPE;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    public String getName() {
        return name;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    public void setName(String newName) {
        String oldName = name;
        name = newName;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, WsdlPackage.SERVICE_TYPE__NAME, oldName, name));
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    public FeatureMap getGroup() {
        if (group == null) {
            group = new BasicFeatureMap(this, WsdlPackage.SERVICE_TYPE__GROUP);
        }
        return group;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    public String getInterface() {
        return interface_;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 */
    public void setInterface(String newInterface) {
        String oldInterface = interface_;
        interface_ = newInterface;
        referencedInterface = null;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, WsdlPackage.SERVICE_TYPE__INTERFACE, oldInterface, interface_));
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 */
    public InterfaceType getReferencedInterface() {
        if (referencedInterface == null) {
            String name = getInterface();
            if (name != null) {
                int index = name.indexOf(":");
                if (index > 0) {
                    name = name.substring(index + 1);
                }
                EList<InterfaceType> interfaces = new BasicEList<InterfaceType>();
                if (eContainer instanceof DocumentRoot) {
                    DocumentRoot doc = (DocumentRoot) eContainer;
                    if (doc != null) {
                        DescriptionType desc = doc.getDescription();
                        if (desc != null) {
                            interfaces = desc.getInterface();
                        }
                    }
                } else {
                    DescriptionType desc = (DescriptionType) eContainer;
                    if (desc != null) {
                        interfaces = desc.getInterface();
                    }
                }
                for (InterfaceType it : interfaces) {
                    if (name.equals(it.getName())) {
                        referencedInterface = it;
                        break;
                    }
                }
            }
        }
        return referencedInterface;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    public void setReferencedInterface(InterfaceType newReferencedInterface) {
        InterfaceType oldReferencedInterface = referencedInterface;
        referencedInterface = newReferencedInterface;
        boolean oldReferencedInterfaceESet = referencedInterfaceESet;
        referencedInterfaceESet = true;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, WsdlPackage.SERVICE_TYPE__REFERENCED_INTERFACE, oldReferencedInterface, referencedInterface, !oldReferencedInterfaceESet));
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    public void unsetReferencedInterface() {
        InterfaceType oldReferencedInterface = referencedInterface;
        boolean oldReferencedInterfaceESet = referencedInterfaceESet;
        referencedInterface = null;
        referencedInterfaceESet = false;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.UNSET, WsdlPackage.SERVICE_TYPE__REFERENCED_INTERFACE, oldReferencedInterface, null, oldReferencedInterfaceESet));
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    public boolean isSetReferencedInterface() {
        return referencedInterfaceESet;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    public EList<EndpointType> getEndpoint() {
        return getGroup().list(WsdlPackage.Literals.SERVICE_TYPE__ENDPOINT);
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    public FeatureMap getAny() {
        return (FeatureMap) getGroup().<FeatureMap.Entry>list(WsdlPackage.Literals.SERVICE_TYPE__ANY);
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case WsdlPackage.SERVICE_TYPE__GROUP:
                return ((InternalEList<?>) getGroup()).basicRemove(otherEnd, msgs);
            case WsdlPackage.SERVICE_TYPE__ENDPOINT:
                return ((InternalEList<?>) getEndpoint()).basicRemove(otherEnd, msgs);
            case WsdlPackage.SERVICE_TYPE__ANY:
                return ((InternalEList<?>) getAny()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case WsdlPackage.SERVICE_TYPE__NAME:
                return getName();
            case WsdlPackage.SERVICE_TYPE__GROUP:
                if (coreType) return getGroup();
                return ((FeatureMap.Internal) getGroup()).getWrapper();
            case WsdlPackage.SERVICE_TYPE__INTERFACE:
                return getInterface();
            case WsdlPackage.SERVICE_TYPE__REFERENCED_INTERFACE:
                return getReferencedInterface();
            case WsdlPackage.SERVICE_TYPE__ENDPOINT:
                return getEndpoint();
            case WsdlPackage.SERVICE_TYPE__ANY:
                if (coreType) return getAny();
                return ((FeatureMap.Internal) getAny()).getWrapper();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch(featureID) {
            case WsdlPackage.SERVICE_TYPE__NAME:
                setName((String) newValue);
                return;
            case WsdlPackage.SERVICE_TYPE__GROUP:
                ((FeatureMap.Internal) getGroup()).set(newValue);
                return;
            case WsdlPackage.SERVICE_TYPE__INTERFACE:
                setInterface((String) newValue);
                return;
            case WsdlPackage.SERVICE_TYPE__REFERENCED_INTERFACE:
                setReferencedInterface((InterfaceType) newValue);
                return;
            case WsdlPackage.SERVICE_TYPE__ENDPOINT:
                getEndpoint().clear();
                getEndpoint().addAll((Collection<? extends EndpointType>) newValue);
                return;
            case WsdlPackage.SERVICE_TYPE__ANY:
                ((FeatureMap.Internal) getAny()).set(newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    @Override
    public void eUnset(int featureID) {
        switch(featureID) {
            case WsdlPackage.SERVICE_TYPE__NAME:
                setName(NAME_EDEFAULT);
                return;
            case WsdlPackage.SERVICE_TYPE__GROUP:
                getGroup().clear();
                return;
            case WsdlPackage.SERVICE_TYPE__INTERFACE:
                setInterface(INTERFACE_EDEFAULT);
                return;
            case WsdlPackage.SERVICE_TYPE__REFERENCED_INTERFACE:
                unsetReferencedInterface();
                return;
            case WsdlPackage.SERVICE_TYPE__ENDPOINT:
                getEndpoint().clear();
                return;
            case WsdlPackage.SERVICE_TYPE__ANY:
                getAny().clear();
                return;
        }
        super.eUnset(featureID);
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    @Override
    public boolean eIsSet(int featureID) {
        switch(featureID) {
            case WsdlPackage.SERVICE_TYPE__NAME:
                return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
            case WsdlPackage.SERVICE_TYPE__GROUP:
                return group != null && !group.isEmpty();
            case WsdlPackage.SERVICE_TYPE__INTERFACE:
                return INTERFACE_EDEFAULT == null ? interface_ != null : !INTERFACE_EDEFAULT.equals(interface_);
            case WsdlPackage.SERVICE_TYPE__REFERENCED_INTERFACE:
                return isSetReferencedInterface();
            case WsdlPackage.SERVICE_TYPE__ENDPOINT:
                return !getEndpoint().isEmpty();
            case WsdlPackage.SERVICE_TYPE__ANY:
                return !getAny().isEmpty();
        }
        return super.eIsSet(featureID);
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    @Override
    public String toString() {
        if (eIsProxy()) return super.toString();
        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (name: ");
        result.append(name);
        result.append(", group: ");
        result.append(group);
        result.append(", interface: ");
        result.append(interface_);
        result.append(')');
        return result.toString();
    }
}
