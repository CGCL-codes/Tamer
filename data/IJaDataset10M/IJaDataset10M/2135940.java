package org.slasoi.models.scm.impl;

import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.slasoi.models.scm.ConfigurableServiceFeature;
import org.slasoi.models.scm.Dependency;
import org.slasoi.models.scm.ImplementationArtefact;
import org.slasoi.models.scm.ProvisioningInformation;
import org.slasoi.models.scm.ServiceConstructionModelPackage;
import org.slasoi.models.scm.ServiceImplementation;
import org.slasoi.models.scm.ServiceType;
import org.slasoi.monitoring.common.features.ComponentMonitoringFeatures;
import org.slasoi.monitoring.common.features.MonitoringFeature;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Service Implementation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.slasoi.models.scm.impl.ServiceImplementationImpl#getType <em>Type</em>}</li>
 *   <li>{@link org.slasoi.models.scm.impl.ServiceImplementationImpl#getArtefactsList <em>Artefacts</em>}</li>
 *   <li>{@link org.slasoi.models.scm.impl.ServiceImplementationImpl#getID <em>ID</em>}</li>
 *   <li>{@link org.slasoi.models.scm.impl.ServiceImplementationImpl#getServiceImplementationName <em>Service Implementation Name</em>}</li>
 *   <li>{@link org.slasoi.models.scm.impl.ServiceImplementationImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link org.slasoi.models.scm.impl.ServiceImplementationImpl#getVersion <em>Version</em>}</li>
 *   <li>{@link org.slasoi.models.scm.impl.ServiceImplementationImpl#getProvisioningInformation <em>Provisioning Information</em>}</li>
 *   <li>{@link org.slasoi.models.scm.impl.ServiceImplementationImpl#getComponentMonitoringFeaturesList <em>Component Monitoring Features</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ServiceImplementationImpl extends EObjectImpl implements ServiceImplementation {

    /**
	 * The cached value of the '{@link #getType() <em>Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
    protected ServiceType type;

    /**
	 * The cached value of the '{@link #getArtefactsList() <em>Artefacts</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getArtefactsList()
	 * @generated
	 * @ordered
	 */
    protected EList<ImplementationArtefact> artefacts;

    /**
	 * The empty value for the '{@link #getArtefacts() <em>Artefacts</em>}' array accessor.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getArtefacts()
	 * @generated
	 * @ordered
	 */
    protected static final ImplementationArtefact[] ARTEFACTS_EEMPTY_ARRAY = new ImplementationArtefact[0];

    /**
	 * The default value of the '{@link #getID() <em>ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getID()
	 * @generated
	 * @ordered
	 */
    protected static final String ID_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getID() <em>ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getID()
	 * @generated
	 * @ordered
	 */
    protected String id = ID_EDEFAULT;

    /**
	 * The default value of the '{@link #getServiceImplementationName() <em>Service Implementation Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getServiceImplementationName()
	 * @generated
	 * @ordered
	 */
    protected static final String SERVICE_IMPLEMENTATION_NAME_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getServiceImplementationName() <em>Service Implementation Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getServiceImplementationName()
	 * @generated
	 * @ordered
	 */
    protected String serviceImplementationName = SERVICE_IMPLEMENTATION_NAME_EDEFAULT;

    /**
	 * The default value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
    protected static final String DESCRIPTION_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
    protected String description = DESCRIPTION_EDEFAULT;

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
	 * The cached value of the '{@link #getProvisioningInformation() <em>Provisioning Information</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProvisioningInformation()
	 * @generated
	 * @ordered
	 */
    protected ProvisioningInformation provisioningInformation;

    /**
	 * The cached value of the '{@link #getComponentMonitoringFeaturesList() <em>Component Monitoring Features</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getComponentMonitoringFeaturesList()
	 * @generated
	 * @ordered
	 */
    protected EList<ComponentMonitoringFeatures> componentMonitoringFeatures;

    /**
	 * The empty value for the '{@link #getComponentMonitoringFeatures() <em>Component Monitoring Features</em>}' array accessor.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getComponentMonitoringFeatures()
	 * @generated
	 * @ordered
	 */
    protected static final ComponentMonitoringFeatures[] COMPONENT_MONITORING_FEATURES_EEMPTY_ARRAY = new ComponentMonitoringFeatures[0];

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected ServiceImplementationImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return ServiceConstructionModelPackage.Literals.SERVICE_IMPLEMENTATION;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ServiceType getType() {
        if (type != null && type.eIsProxy()) {
            InternalEObject oldType = (InternalEObject) type;
            type = (ServiceType) eResolveProxy(oldType);
            if (type != oldType) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, ServiceConstructionModelPackage.SERVICE_IMPLEMENTATION__TYPE, oldType, type));
            }
        }
        return type;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ServiceType basicGetType() {
        return type;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setType(ServiceType newType) {
        ServiceType oldType = type;
        type = newType;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ServiceConstructionModelPackage.SERVICE_IMPLEMENTATION__TYPE, oldType, type));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ImplementationArtefact[] getArtefacts() {
        if (artefacts == null || artefacts.isEmpty()) return ARTEFACTS_EEMPTY_ARRAY;
        BasicEList<ImplementationArtefact> list = (BasicEList<ImplementationArtefact>) artefacts;
        list.shrink();
        return (ImplementationArtefact[]) list.data();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ImplementationArtefact getArtefacts(int index) {
        return getArtefactsList().get(index);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public int getArtefactsLength() {
        return artefacts == null ? 0 : artefacts.size();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setArtefacts(ImplementationArtefact[] newArtefacts) {
        ((BasicEList<ImplementationArtefact>) getArtefactsList()).setData(newArtefacts.length, newArtefacts);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setArtefacts(int index, ImplementationArtefact element) {
        getArtefactsList().set(index, element);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<ImplementationArtefact> getArtefactsList() {
        if (artefacts == null) {
            artefacts = new EObjectContainmentEList<ImplementationArtefact>(ImplementationArtefact.class, this, ServiceConstructionModelPackage.SERVICE_IMPLEMENTATION__ARTEFACTS);
        }
        return artefacts;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getID() {
        return id;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setID(String newID) {
        String oldID = id;
        id = newID;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ServiceConstructionModelPackage.SERVICE_IMPLEMENTATION__ID, oldID, id));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getServiceImplementationName() {
        return serviceImplementationName;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setServiceImplementationName(String newServiceImplementationName) {
        String oldServiceImplementationName = serviceImplementationName;
        serviceImplementationName = newServiceImplementationName;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ServiceConstructionModelPackage.SERVICE_IMPLEMENTATION__SERVICE_IMPLEMENTATION_NAME, oldServiceImplementationName, serviceImplementationName));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getDescription() {
        return description;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setDescription(String newDescription) {
        String oldDescription = description;
        description = newDescription;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ServiceConstructionModelPackage.SERVICE_IMPLEMENTATION__DESCRIPTION, oldDescription, description));
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
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ServiceConstructionModelPackage.SERVICE_IMPLEMENTATION__VERSION, oldVersion, version));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ProvisioningInformation getProvisioningInformation() {
        return provisioningInformation;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetProvisioningInformation(ProvisioningInformation newProvisioningInformation, NotificationChain msgs) {
        ProvisioningInformation oldProvisioningInformation = provisioningInformation;
        provisioningInformation = newProvisioningInformation;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ServiceConstructionModelPackage.SERVICE_IMPLEMENTATION__PROVISIONING_INFORMATION, oldProvisioningInformation, newProvisioningInformation);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setProvisioningInformation(ProvisioningInformation newProvisioningInformation) {
        if (newProvisioningInformation != provisioningInformation) {
            NotificationChain msgs = null;
            if (provisioningInformation != null) msgs = ((InternalEObject) provisioningInformation).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ServiceConstructionModelPackage.SERVICE_IMPLEMENTATION__PROVISIONING_INFORMATION, null, msgs);
            if (newProvisioningInformation != null) msgs = ((InternalEObject) newProvisioningInformation).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ServiceConstructionModelPackage.SERVICE_IMPLEMENTATION__PROVISIONING_INFORMATION, null, msgs);
            msgs = basicSetProvisioningInformation(newProvisioningInformation, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ServiceConstructionModelPackage.SERVICE_IMPLEMENTATION__PROVISIONING_INFORMATION, newProvisioningInformation, newProvisioningInformation));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ComponentMonitoringFeatures[] getComponentMonitoringFeatures() {
        if (componentMonitoringFeatures == null || componentMonitoringFeatures.isEmpty()) return COMPONENT_MONITORING_FEATURES_EEMPTY_ARRAY;
        BasicEList<ComponentMonitoringFeatures> list = (BasicEList<ComponentMonitoringFeatures>) componentMonitoringFeatures;
        list.shrink();
        return (ComponentMonitoringFeatures[]) list.data();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ComponentMonitoringFeatures getComponentMonitoringFeatures(int index) {
        return getComponentMonitoringFeaturesList().get(index);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public int getComponentMonitoringFeaturesLength() {
        return componentMonitoringFeatures == null ? 0 : componentMonitoringFeatures.size();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setComponentMonitoringFeatures(ComponentMonitoringFeatures[] newComponentMonitoringFeatures) {
        ((BasicEList<ComponentMonitoringFeatures>) getComponentMonitoringFeaturesList()).setData(newComponentMonitoringFeatures.length, newComponentMonitoringFeatures);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setComponentMonitoringFeatures(int index, ComponentMonitoringFeatures element) {
        getComponentMonitoringFeaturesList().set(index, element);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<ComponentMonitoringFeatures> getComponentMonitoringFeaturesList() {
        if (componentMonitoringFeatures == null) {
            componentMonitoringFeatures = new EObjectContainmentEList<ComponentMonitoringFeatures>(ComponentMonitoringFeatures.class, this, ServiceConstructionModelPackage.SERVICE_IMPLEMENTATION__COMPONENT_MONITORING_FEATURES);
        }
        return componentMonitoringFeatures;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<Dependency> getDependencies() {
        throw new UnsupportedOperationException();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<ConfigurableServiceFeature> getConfigurableServiceFeatures() {
        throw new UnsupportedOperationException();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case ServiceConstructionModelPackage.SERVICE_IMPLEMENTATION__ARTEFACTS:
                return ((InternalEList<?>) getArtefactsList()).basicRemove(otherEnd, msgs);
            case ServiceConstructionModelPackage.SERVICE_IMPLEMENTATION__PROVISIONING_INFORMATION:
                return basicSetProvisioningInformation(null, msgs);
            case ServiceConstructionModelPackage.SERVICE_IMPLEMENTATION__COMPONENT_MONITORING_FEATURES:
                return ((InternalEList<?>) getComponentMonitoringFeaturesList()).basicRemove(otherEnd, msgs);
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
            case ServiceConstructionModelPackage.SERVICE_IMPLEMENTATION__TYPE:
                if (resolve) return getType();
                return basicGetType();
            case ServiceConstructionModelPackage.SERVICE_IMPLEMENTATION__ARTEFACTS:
                return getArtefactsList();
            case ServiceConstructionModelPackage.SERVICE_IMPLEMENTATION__ID:
                return getID();
            case ServiceConstructionModelPackage.SERVICE_IMPLEMENTATION__SERVICE_IMPLEMENTATION_NAME:
                return getServiceImplementationName();
            case ServiceConstructionModelPackage.SERVICE_IMPLEMENTATION__DESCRIPTION:
                return getDescription();
            case ServiceConstructionModelPackage.SERVICE_IMPLEMENTATION__VERSION:
                return getVersion();
            case ServiceConstructionModelPackage.SERVICE_IMPLEMENTATION__PROVISIONING_INFORMATION:
                return getProvisioningInformation();
            case ServiceConstructionModelPackage.SERVICE_IMPLEMENTATION__COMPONENT_MONITORING_FEATURES:
                return getComponentMonitoringFeaturesList();
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
            case ServiceConstructionModelPackage.SERVICE_IMPLEMENTATION__TYPE:
                setType((ServiceType) newValue);
                return;
            case ServiceConstructionModelPackage.SERVICE_IMPLEMENTATION__ARTEFACTS:
                getArtefactsList().clear();
                getArtefactsList().addAll((Collection<? extends ImplementationArtefact>) newValue);
                return;
            case ServiceConstructionModelPackage.SERVICE_IMPLEMENTATION__ID:
                setID((String) newValue);
                return;
            case ServiceConstructionModelPackage.SERVICE_IMPLEMENTATION__SERVICE_IMPLEMENTATION_NAME:
                setServiceImplementationName((String) newValue);
                return;
            case ServiceConstructionModelPackage.SERVICE_IMPLEMENTATION__DESCRIPTION:
                setDescription((String) newValue);
                return;
            case ServiceConstructionModelPackage.SERVICE_IMPLEMENTATION__VERSION:
                setVersion((String) newValue);
                return;
            case ServiceConstructionModelPackage.SERVICE_IMPLEMENTATION__PROVISIONING_INFORMATION:
                setProvisioningInformation((ProvisioningInformation) newValue);
                return;
            case ServiceConstructionModelPackage.SERVICE_IMPLEMENTATION__COMPONENT_MONITORING_FEATURES:
                getComponentMonitoringFeaturesList().clear();
                getComponentMonitoringFeaturesList().addAll((Collection<? extends ComponentMonitoringFeatures>) newValue);
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
            case ServiceConstructionModelPackage.SERVICE_IMPLEMENTATION__TYPE:
                setType((ServiceType) null);
                return;
            case ServiceConstructionModelPackage.SERVICE_IMPLEMENTATION__ARTEFACTS:
                getArtefactsList().clear();
                return;
            case ServiceConstructionModelPackage.SERVICE_IMPLEMENTATION__ID:
                setID(ID_EDEFAULT);
                return;
            case ServiceConstructionModelPackage.SERVICE_IMPLEMENTATION__SERVICE_IMPLEMENTATION_NAME:
                setServiceImplementationName(SERVICE_IMPLEMENTATION_NAME_EDEFAULT);
                return;
            case ServiceConstructionModelPackage.SERVICE_IMPLEMENTATION__DESCRIPTION:
                setDescription(DESCRIPTION_EDEFAULT);
                return;
            case ServiceConstructionModelPackage.SERVICE_IMPLEMENTATION__VERSION:
                setVersion(VERSION_EDEFAULT);
                return;
            case ServiceConstructionModelPackage.SERVICE_IMPLEMENTATION__PROVISIONING_INFORMATION:
                setProvisioningInformation((ProvisioningInformation) null);
                return;
            case ServiceConstructionModelPackage.SERVICE_IMPLEMENTATION__COMPONENT_MONITORING_FEATURES:
                getComponentMonitoringFeaturesList().clear();
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
            case ServiceConstructionModelPackage.SERVICE_IMPLEMENTATION__TYPE:
                return type != null;
            case ServiceConstructionModelPackage.SERVICE_IMPLEMENTATION__ARTEFACTS:
                return artefacts != null && !artefacts.isEmpty();
            case ServiceConstructionModelPackage.SERVICE_IMPLEMENTATION__ID:
                return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
            case ServiceConstructionModelPackage.SERVICE_IMPLEMENTATION__SERVICE_IMPLEMENTATION_NAME:
                return SERVICE_IMPLEMENTATION_NAME_EDEFAULT == null ? serviceImplementationName != null : !SERVICE_IMPLEMENTATION_NAME_EDEFAULT.equals(serviceImplementationName);
            case ServiceConstructionModelPackage.SERVICE_IMPLEMENTATION__DESCRIPTION:
                return DESCRIPTION_EDEFAULT == null ? description != null : !DESCRIPTION_EDEFAULT.equals(description);
            case ServiceConstructionModelPackage.SERVICE_IMPLEMENTATION__VERSION:
                return VERSION_EDEFAULT == null ? version != null : !VERSION_EDEFAULT.equals(version);
            case ServiceConstructionModelPackage.SERVICE_IMPLEMENTATION__PROVISIONING_INFORMATION:
                return provisioningInformation != null;
            case ServiceConstructionModelPackage.SERVICE_IMPLEMENTATION__COMPONENT_MONITORING_FEATURES:
                return componentMonitoringFeatures != null && !componentMonitoringFeatures.isEmpty();
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
        result.append(" (ID: ");
        result.append(id);
        result.append(", ServiceImplementationName: ");
        result.append(serviceImplementationName);
        result.append(", Description: ");
        result.append(description);
        result.append(", Version: ");
        result.append(version);
        result.append(')');
        return result.toString();
    }
}
