package org.slasoi.models.scm;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.slasoi.monitoring.common.features.ComponentMonitoringFeatures;
import org.slasoi.monitoring.common.features.MonitoringFeature;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Service Implementation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.slasoi.models.scm.ServiceImplementation#getType <em>Type</em>}</li>
 *   <li>{@link org.slasoi.models.scm.ServiceImplementation#getArtefactsList <em>Artefacts</em>}</li>
 *   <li>{@link org.slasoi.models.scm.ServiceImplementation#getID <em>ID</em>}</li>
 *   <li>{@link org.slasoi.models.scm.ServiceImplementation#getServiceImplementationName <em>Service Implementation Name</em>}</li>
 *   <li>{@link org.slasoi.models.scm.ServiceImplementation#getDescription <em>Description</em>}</li>
 *   <li>{@link org.slasoi.models.scm.ServiceImplementation#getVersion <em>Version</em>}</li>
 *   <li>{@link org.slasoi.models.scm.ServiceImplementation#getProvisioningInformation <em>Provisioning Information</em>}</li>
 *   <li>{@link org.slasoi.models.scm.ServiceImplementation#getComponentMonitoringFeaturesList <em>Component Monitoring Features</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.slasoi.models.scm.ServiceConstructionModelPackage#getServiceImplementation()
 * @model
 * @generated
 */
public interface ServiceImplementation extends EObject {

    /**
	 * Returns the value of the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' reference.
	 * @see #setType(ServiceType)
	 * @see org.slasoi.models.scm.ServiceConstructionModelPackage#getServiceImplementation_Type()
	 * @model required="true"
	 * @generated
	 */
    ServiceType getType();

    /**
	 * Sets the value of the '{@link org.slasoi.models.scm.ServiceImplementation#getType <em>Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' reference.
	 * @see #getType()
	 * @generated
	 */
    void setType(ServiceType value);

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    ImplementationArtefact[] getArtefacts();

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    ImplementationArtefact getArtefacts(int index);

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    int getArtefactsLength();

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    void setArtefacts(ImplementationArtefact[] newArtefacts);

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    void setArtefacts(int index, ImplementationArtefact element);

    /**
	 * Returns the value of the '<em><b>Artefacts</b></em>' containment reference list.
	 * The list contents are of type {@link org.slasoi.models.scm.ImplementationArtefact}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Artefacts</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Artefacts</em>' containment reference list.
	 * @see org.slasoi.models.scm.ServiceConstructionModelPackage#getServiceImplementation_Artefacts()
	 * @model containment="true" required="true"
	 * @generated
	 */
    EList<ImplementationArtefact> getArtefactsList();

    /**
	 * Returns the value of the '<em><b>ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>ID</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>ID</em>' attribute.
	 * @see #setID(String)
	 * @see org.slasoi.models.scm.ServiceConstructionModelPackage#getServiceImplementation_ID()
	 * @model id="true" required="true"
	 * @generated
	 */
    String getID();

    /**
	 * Sets the value of the '{@link org.slasoi.models.scm.ServiceImplementation#getID <em>ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>ID</em>' attribute.
	 * @see #getID()
	 * @generated
	 */
    void setID(String value);

    /**
	 * Returns the value of the '<em><b>Service Implementation Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Service Implementation Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Service Implementation Name</em>' attribute.
	 * @see #setServiceImplementationName(String)
	 * @see org.slasoi.models.scm.ServiceConstructionModelPackage#getServiceImplementation_ServiceImplementationName()
	 * @model required="true"
	 * @generated
	 */
    String getServiceImplementationName();

    /**
	 * Sets the value of the '{@link org.slasoi.models.scm.ServiceImplementation#getServiceImplementationName <em>Service Implementation Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Service Implementation Name</em>' attribute.
	 * @see #getServiceImplementationName()
	 * @generated
	 */
    void setServiceImplementationName(String value);

    /**
	 * Returns the value of the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Description</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Description</em>' attribute.
	 * @see #setDescription(String)
	 * @see org.slasoi.models.scm.ServiceConstructionModelPackage#getServiceImplementation_Description()
	 * @model
	 * @generated
	 */
    String getDescription();

    /**
	 * Sets the value of the '{@link org.slasoi.models.scm.ServiceImplementation#getDescription <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Description</em>' attribute.
	 * @see #getDescription()
	 * @generated
	 */
    void setDescription(String value);

    /**
	 * Returns the value of the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Version</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Version</em>' attribute.
	 * @see #setVersion(String)
	 * @see org.slasoi.models.scm.ServiceConstructionModelPackage#getServiceImplementation_Version()
	 * @model
	 * @generated
	 */
    String getVersion();

    /**
	 * Sets the value of the '{@link org.slasoi.models.scm.ServiceImplementation#getVersion <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Version</em>' attribute.
	 * @see #getVersion()
	 * @generated
	 */
    void setVersion(String value);

    /**
	 * Returns the value of the '<em><b>Provisioning Information</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Provisioning Information</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Provisioning Information</em>' containment reference.
	 * @see #setProvisioningInformation(ProvisioningInformation)
	 * @see org.slasoi.models.scm.ServiceConstructionModelPackage#getServiceImplementation_ProvisioningInformation()
	 * @model containment="true"
	 * @generated
	 */
    ProvisioningInformation getProvisioningInformation();

    /**
	 * Sets the value of the '{@link org.slasoi.models.scm.ServiceImplementation#getProvisioningInformation <em>Provisioning Information</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Provisioning Information</em>' containment reference.
	 * @see #getProvisioningInformation()
	 * @generated
	 */
    void setProvisioningInformation(ProvisioningInformation value);

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    ComponentMonitoringFeatures[] getComponentMonitoringFeatures();

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    ComponentMonitoringFeatures getComponentMonitoringFeatures(int index);

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    int getComponentMonitoringFeaturesLength();

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    void setComponentMonitoringFeatures(ComponentMonitoringFeatures[] newComponentMonitoringFeatures);

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    void setComponentMonitoringFeatures(int index, ComponentMonitoringFeatures element);

    /**
	 * Returns the value of the '<em><b>Component Monitoring Features</b></em>' containment reference list.
	 * The list contents are of type {@link org.slasoi.monitoring.common.features.ComponentMonitoringFeatures}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Component Monitoring Features</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Component Monitoring Features</em>' containment reference list.
	 * @see org.slasoi.models.scm.ServiceConstructionModelPackage#getServiceImplementation_ComponentMonitoringFeatures()
	 * @model containment="true"
	 * @generated
	 */
    EList<ComponentMonitoringFeatures> getComponentMonitoringFeaturesList();

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
    EList<Dependency> getDependencies();

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
    EList<ConfigurableServiceFeature> getConfigurableServiceFeatures();
}
