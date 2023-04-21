package net.sf.kosmagene.ejb.campaign.universe;

import net.sf.kosmagene.ejb.campaign.CampaignPackage;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see net.sf.kosmagene.ejb.campaign.universe.UniverseFactory
 * @model kind="package"
 * @generated
 */
public interface UniversePackage extends EPackage {

    /**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    String eNAME = "universe";

    /**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    String eNS_URI = "http:///net/sf/kosmagene/j2ee/ejb/campaign/universe.ecore";

    /**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    String eNS_PREFIX = "universe";

    /**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    UniversePackage eINSTANCE = net.sf.kosmagene.ejb.campaign.universe.impl.UniversePackageImpl.init();

    /**
	 * The meta object id for the '{@link net.sf.kosmagene.ejb.campaign.universe.impl.AstronomicalObjectImpl <em>Astronomical Object</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.kosmagene.ejb.campaign.universe.impl.AstronomicalObjectImpl
	 * @see net.sf.kosmagene.ejb.campaign.universe.impl.UniversePackageImpl#getAstronomicalObject()
	 * @generated
	 */
    int ASTRONOMICAL_OBJECT = 0;

    /**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ASTRONOMICAL_OBJECT__ID = CampaignPackage.IDENTIFIABLE__ID;

    /**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ASTRONOMICAL_OBJECT__VERSION = CampaignPackage.IDENTIFIABLE__VERSION;

    /**
	 * The feature id for the '<em><b>Archetype</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ASTRONOMICAL_OBJECT__ARCHETYPE = CampaignPackage.IDENTIFIABLE_FEATURE_COUNT + 0;

    /**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ASTRONOMICAL_OBJECT__NAME = CampaignPackage.IDENTIFIABLE_FEATURE_COUNT + 1;

    /**
	 * The feature id for the '<em><b>Environment Index</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ASTRONOMICAL_OBJECT__ENVIRONMENT_INDEX = CampaignPackage.IDENTIFIABLE_FEATURE_COUNT + 2;

    /**
	 * The feature id for the '<em><b>Evolution Max</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ASTRONOMICAL_OBJECT__EVOLUTION_MAX = CampaignPackage.IDENTIFIABLE_FEATURE_COUNT + 3;

    /**
	 * The feature id for the '<em><b>Evolution Index</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ASTRONOMICAL_OBJECT__EVOLUTION_INDEX = CampaignPackage.IDENTIFIABLE_FEATURE_COUNT + 4;

    /**
	 * The feature id for the '<em><b>Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ASTRONOMICAL_OBJECT__DESCRIPTION = CampaignPackage.IDENTIFIABLE_FEATURE_COUNT + 5;

    /**
	 * The feature id for the '<em><b>Cluster</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ASTRONOMICAL_OBJECT__CLUSTER = CampaignPackage.IDENTIFIABLE_FEATURE_COUNT + 6;

    /**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ASTRONOMICAL_OBJECT__TYPE = CampaignPackage.IDENTIFIABLE_FEATURE_COUNT + 7;

    /**
	 * The number of structural features of the '<em>Astronomical Object</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ASTRONOMICAL_OBJECT_FEATURE_COUNT = CampaignPackage.IDENTIFIABLE_FEATURE_COUNT + 8;

    /**
	 * The meta object id for the '{@link net.sf.kosmagene.ejb.campaign.universe.impl.MinorObjectImpl <em>Minor Object</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.kosmagene.ejb.campaign.universe.impl.MinorObjectImpl
	 * @see net.sf.kosmagene.ejb.campaign.universe.impl.UniversePackageImpl#getMinorObject()
	 * @generated
	 */
    int MINOR_OBJECT = 1;

    /**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int MINOR_OBJECT__ID = ASTRONOMICAL_OBJECT__ID;

    /**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int MINOR_OBJECT__VERSION = ASTRONOMICAL_OBJECT__VERSION;

    /**
	 * The feature id for the '<em><b>Archetype</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int MINOR_OBJECT__ARCHETYPE = ASTRONOMICAL_OBJECT__ARCHETYPE;

    /**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int MINOR_OBJECT__NAME = ASTRONOMICAL_OBJECT__NAME;

    /**
	 * The feature id for the '<em><b>Environment Index</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int MINOR_OBJECT__ENVIRONMENT_INDEX = ASTRONOMICAL_OBJECT__ENVIRONMENT_INDEX;

    /**
	 * The feature id for the '<em><b>Evolution Max</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int MINOR_OBJECT__EVOLUTION_MAX = ASTRONOMICAL_OBJECT__EVOLUTION_MAX;

    /**
	 * The feature id for the '<em><b>Evolution Index</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int MINOR_OBJECT__EVOLUTION_INDEX = ASTRONOMICAL_OBJECT__EVOLUTION_INDEX;

    /**
	 * The feature id for the '<em><b>Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int MINOR_OBJECT__DESCRIPTION = ASTRONOMICAL_OBJECT__DESCRIPTION;

    /**
	 * The feature id for the '<em><b>Cluster</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int MINOR_OBJECT__CLUSTER = ASTRONOMICAL_OBJECT__CLUSTER;

    /**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int MINOR_OBJECT__TYPE = ASTRONOMICAL_OBJECT__TYPE;

    /**
	 * The feature id for the '<em><b>Major Object</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int MINOR_OBJECT__MAJOR_OBJECT = ASTRONOMICAL_OBJECT_FEATURE_COUNT + 0;

    /**
	 * The number of structural features of the '<em>Minor Object</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int MINOR_OBJECT_FEATURE_COUNT = ASTRONOMICAL_OBJECT_FEATURE_COUNT + 1;

    /**
	 * The meta object id for the '{@link net.sf.kosmagene.ejb.campaign.universe.impl.MajorObjectImpl <em>Major Object</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.kosmagene.ejb.campaign.universe.impl.MajorObjectImpl
	 * @see net.sf.kosmagene.ejb.campaign.universe.impl.UniversePackageImpl#getMajorObject()
	 * @generated
	 */
    int MAJOR_OBJECT = 2;

    /**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int MAJOR_OBJECT__ID = ASTRONOMICAL_OBJECT__ID;

    /**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int MAJOR_OBJECT__VERSION = ASTRONOMICAL_OBJECT__VERSION;

    /**
	 * The feature id for the '<em><b>Archetype</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int MAJOR_OBJECT__ARCHETYPE = ASTRONOMICAL_OBJECT__ARCHETYPE;

    /**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int MAJOR_OBJECT__NAME = ASTRONOMICAL_OBJECT__NAME;

    /**
	 * The feature id for the '<em><b>Environment Index</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int MAJOR_OBJECT__ENVIRONMENT_INDEX = ASTRONOMICAL_OBJECT__ENVIRONMENT_INDEX;

    /**
	 * The feature id for the '<em><b>Evolution Max</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int MAJOR_OBJECT__EVOLUTION_MAX = ASTRONOMICAL_OBJECT__EVOLUTION_MAX;

    /**
	 * The feature id for the '<em><b>Evolution Index</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int MAJOR_OBJECT__EVOLUTION_INDEX = ASTRONOMICAL_OBJECT__EVOLUTION_INDEX;

    /**
	 * The feature id for the '<em><b>Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int MAJOR_OBJECT__DESCRIPTION = ASTRONOMICAL_OBJECT__DESCRIPTION;

    /**
	 * The feature id for the '<em><b>Cluster</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int MAJOR_OBJECT__CLUSTER = ASTRONOMICAL_OBJECT__CLUSTER;

    /**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int MAJOR_OBJECT__TYPE = ASTRONOMICAL_OBJECT__TYPE;

    /**
	 * The number of structural features of the '<em>Major Object</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int MAJOR_OBJECT_FEATURE_COUNT = ASTRONOMICAL_OBJECT_FEATURE_COUNT + 0;

    /**
	 * The meta object id for the '{@link net.sf.kosmagene.ejb.campaign.universe.impl.AstronomicalDescriptionImpl <em>Astronomical Description</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.kosmagene.ejb.campaign.universe.impl.AstronomicalDescriptionImpl
	 * @see net.sf.kosmagene.ejb.campaign.universe.impl.UniversePackageImpl#getAstronomicalDescription()
	 * @generated
	 */
    int ASTRONOMICAL_DESCRIPTION = 3;

    /**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ASTRONOMICAL_DESCRIPTION__ID = CampaignPackage.IDENTIFIABLE__ID;

    /**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ASTRONOMICAL_DESCRIPTION__VERSION = CampaignPackage.IDENTIFIABLE__VERSION;

    /**
	 * The number of structural features of the '<em>Astronomical Description</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ASTRONOMICAL_DESCRIPTION_FEATURE_COUNT = CampaignPackage.IDENTIFIABLE_FEATURE_COUNT + 0;

    /**
	 * The meta object id for the '{@link net.sf.kosmagene.ejb.campaign.universe.impl.ClusterImpl <em>Cluster</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.kosmagene.ejb.campaign.universe.impl.ClusterImpl
	 * @see net.sf.kosmagene.ejb.campaign.universe.impl.UniversePackageImpl#getCluster()
	 * @generated
	 */
    int CLUSTER = 4;

    /**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CLUSTER__ID = CampaignPackage.IDENTIFIABLE__ID;

    /**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CLUSTER__VERSION = CampaignPackage.IDENTIFIABLE__VERSION;

    /**
	 * The feature id for the '<em><b>Free Slot Count</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CLUSTER__FREE_SLOT_COUNT = CampaignPackage.IDENTIFIABLE_FEATURE_COUNT + 0;

    /**
	 * The feature id for the '<em><b>Last Plugin Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CLUSTER__LAST_PLUGIN_DATE = CampaignPackage.IDENTIFIABLE_FEATURE_COUNT + 1;

    /**
	 * The feature id for the '<em><b>Cluster Network</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CLUSTER__CLUSTER_NETWORK = CampaignPackage.IDENTIFIABLE_FEATURE_COUNT + 2;

    /**
	 * The feature id for the '<em><b>Astronomical Objects</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CLUSTER__ASTRONOMICAL_OBJECTS = CampaignPackage.IDENTIFIABLE_FEATURE_COUNT + 3;

    /**
	 * The feature id for the '<em><b>Neighbour Clusters</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CLUSTER__NEIGHBOUR_CLUSTERS = CampaignPackage.IDENTIFIABLE_FEATURE_COUNT + 4;

    /**
	 * The number of structural features of the '<em>Cluster</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CLUSTER_FEATURE_COUNT = CampaignPackage.IDENTIFIABLE_FEATURE_COUNT + 5;

    /**
	 * The meta object id for the '{@link net.sf.kosmagene.ejb.campaign.universe.impl.ClusterNetworkImpl <em>Cluster Network</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.kosmagene.ejb.campaign.universe.impl.ClusterNetworkImpl
	 * @see net.sf.kosmagene.ejb.campaign.universe.impl.UniversePackageImpl#getClusterNetwork()
	 * @generated
	 */
    int CLUSTER_NETWORK = 5;

    /**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CLUSTER_NETWORK__ID = CampaignPackage.IDENTIFIABLE__ID;

    /**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CLUSTER_NETWORK__VERSION = CampaignPackage.IDENTIFIABLE__VERSION;

    /**
	 * The feature id for the '<em><b>Is Trunk</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CLUSTER_NETWORK__IS_TRUNK = CampaignPackage.IDENTIFIABLE_FEATURE_COUNT + 0;

    /**
	 * The feature id for the '<em><b>Clusters</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CLUSTER_NETWORK__CLUSTERS = CampaignPackage.IDENTIFIABLE_FEATURE_COUNT + 1;

    /**
	 * The number of structural features of the '<em>Cluster Network</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CLUSTER_NETWORK_FEATURE_COUNT = CampaignPackage.IDENTIFIABLE_FEATURE_COUNT + 2;

    /**
	 * The meta object id for the '{@link net.sf.kosmagene.ejb.campaign.universe.impl.WormHoleImpl <em>Worm Hole</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.kosmagene.ejb.campaign.universe.impl.WormHoleImpl
	 * @see net.sf.kosmagene.ejb.campaign.universe.impl.UniversePackageImpl#getWormHole()
	 * @generated
	 */
    int WORM_HOLE = 6;

    /**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WORM_HOLE__ID = CampaignPackage.IDENTIFIABLE__ID;

    /**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WORM_HOLE__VERSION = CampaignPackage.IDENTIFIABLE__VERSION;

    /**
	 * The feature id for the '<em><b>Bounds</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WORM_HOLE__BOUNDS = CampaignPackage.IDENTIFIABLE_FEATURE_COUNT + 0;

    /**
	 * The number of structural features of the '<em>Worm Hole</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WORM_HOLE_FEATURE_COUNT = CampaignPackage.IDENTIFIABLE_FEATURE_COUNT + 1;

    /**
	 * The meta object id for the '{@link net.sf.kosmagene.ejb.campaign.universe.impl.WormHoleBoundImpl <em>Worm Hole Bound</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.kosmagene.ejb.campaign.universe.impl.WormHoleBoundImpl
	 * @see net.sf.kosmagene.ejb.campaign.universe.impl.UniversePackageImpl#getWormHoleBound()
	 * @generated
	 */
    int WORM_HOLE_BOUND = 7;

    /**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WORM_HOLE_BOUND__ID = ASTRONOMICAL_OBJECT__ID;

    /**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WORM_HOLE_BOUND__VERSION = ASTRONOMICAL_OBJECT__VERSION;

    /**
	 * The feature id for the '<em><b>Archetype</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WORM_HOLE_BOUND__ARCHETYPE = ASTRONOMICAL_OBJECT__ARCHETYPE;

    /**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WORM_HOLE_BOUND__NAME = ASTRONOMICAL_OBJECT__NAME;

    /**
	 * The feature id for the '<em><b>Environment Index</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WORM_HOLE_BOUND__ENVIRONMENT_INDEX = ASTRONOMICAL_OBJECT__ENVIRONMENT_INDEX;

    /**
	 * The feature id for the '<em><b>Evolution Max</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WORM_HOLE_BOUND__EVOLUTION_MAX = ASTRONOMICAL_OBJECT__EVOLUTION_MAX;

    /**
	 * The feature id for the '<em><b>Evolution Index</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WORM_HOLE_BOUND__EVOLUTION_INDEX = ASTRONOMICAL_OBJECT__EVOLUTION_INDEX;

    /**
	 * The feature id for the '<em><b>Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WORM_HOLE_BOUND__DESCRIPTION = ASTRONOMICAL_OBJECT__DESCRIPTION;

    /**
	 * The feature id for the '<em><b>Cluster</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WORM_HOLE_BOUND__CLUSTER = ASTRONOMICAL_OBJECT__CLUSTER;

    /**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WORM_HOLE_BOUND__TYPE = ASTRONOMICAL_OBJECT__TYPE;

    /**
	 * The feature id for the '<em><b>Potential</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WORM_HOLE_BOUND__POTENTIAL = ASTRONOMICAL_OBJECT_FEATURE_COUNT + 0;

    /**
	 * The feature id for the '<em><b>Is Obscured</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WORM_HOLE_BOUND__IS_OBSCURED = ASTRONOMICAL_OBJECT_FEATURE_COUNT + 1;

    /**
	 * The feature id for the '<em><b>Worm Hole</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WORM_HOLE_BOUND__WORM_HOLE = ASTRONOMICAL_OBJECT_FEATURE_COUNT + 2;

    /**
	 * The feature id for the '<em><b>Bound Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WORM_HOLE_BOUND__BOUND_TYPE = ASTRONOMICAL_OBJECT_FEATURE_COUNT + 3;

    /**
	 * The number of structural features of the '<em>Worm Hole Bound</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WORM_HOLE_BOUND_FEATURE_COUNT = ASTRONOMICAL_OBJECT_FEATURE_COUNT + 4;

    /**
	 * The meta object id for the '{@link net.sf.kosmagene.ejb.campaign.universe.WormHoleBoundType <em>Worm Hole Bound Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.kosmagene.ejb.campaign.universe.WormHoleBoundType
	 * @see net.sf.kosmagene.ejb.campaign.universe.impl.UniversePackageImpl#getWormHoleBoundType()
	 * @generated
	 */
    int WORM_HOLE_BOUND_TYPE = 8;

    /**
	 * The meta object id for the '{@link net.sf.kosmagene.ejb.campaign.universe.AstronomicalObjectType <em>Astronomical Object Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.kosmagene.ejb.campaign.universe.AstronomicalObjectType
	 * @see net.sf.kosmagene.ejb.campaign.universe.impl.UniversePackageImpl#getAstronomicalObjectType()
	 * @generated
	 */
    int ASTRONOMICAL_OBJECT_TYPE = 9;

    /**
	 * Returns the meta object for class '{@link net.sf.kosmagene.ejb.campaign.universe.AstronomicalObject <em>Astronomical Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Astronomical Object</em>'.
	 * @see net.sf.kosmagene.ejb.campaign.universe.AstronomicalObject
	 * @generated
	 */
    EClass getAstronomicalObject();

    /**
	 * Returns the meta object for the attribute '{@link net.sf.kosmagene.ejb.campaign.universe.AstronomicalObject#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see net.sf.kosmagene.ejb.campaign.universe.AstronomicalObject#getName()
	 * @see #getAstronomicalObject()
	 * @generated
	 */
    EAttribute getAstronomicalObject_Name();

    /**
	 * Returns the meta object for the attribute '{@link net.sf.kosmagene.ejb.campaign.universe.AstronomicalObject#getEnvironmentIndex <em>Environment Index</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Environment Index</em>'.
	 * @see net.sf.kosmagene.ejb.campaign.universe.AstronomicalObject#getEnvironmentIndex()
	 * @see #getAstronomicalObject()
	 * @generated
	 */
    EAttribute getAstronomicalObject_EnvironmentIndex();

    /**
	 * Returns the meta object for the attribute '{@link net.sf.kosmagene.ejb.campaign.universe.AstronomicalObject#getEvolutionMax <em>Evolution Max</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Evolution Max</em>'.
	 * @see net.sf.kosmagene.ejb.campaign.universe.AstronomicalObject#getEvolutionMax()
	 * @see #getAstronomicalObject()
	 * @generated
	 */
    EAttribute getAstronomicalObject_EvolutionMax();

    /**
	 * Returns the meta object for the attribute '{@link net.sf.kosmagene.ejb.campaign.universe.AstronomicalObject#getEvolutionIndex <em>Evolution Index</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Evolution Index</em>'.
	 * @see net.sf.kosmagene.ejb.campaign.universe.AstronomicalObject#getEvolutionIndex()
	 * @see #getAstronomicalObject()
	 * @generated
	 */
    EAttribute getAstronomicalObject_EvolutionIndex();

    /**
	 * Returns the meta object for the containment reference '{@link net.sf.kosmagene.ejb.campaign.universe.AstronomicalObject#getDescription <em>Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Description</em>'.
	 * @see net.sf.kosmagene.ejb.campaign.universe.AstronomicalObject#getDescription()
	 * @see #getAstronomicalObject()
	 * @generated
	 */
    EReference getAstronomicalObject_Description();

    /**
	 * Returns the meta object for the reference '{@link net.sf.kosmagene.ejb.campaign.universe.AstronomicalObject#getCluster <em>Cluster</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Cluster</em>'.
	 * @see net.sf.kosmagene.ejb.campaign.universe.AstronomicalObject#getCluster()
	 * @see #getAstronomicalObject()
	 * @generated
	 */
    EReference getAstronomicalObject_Cluster();

    /**
	 * Returns the meta object for the attribute '{@link net.sf.kosmagene.ejb.campaign.universe.AstronomicalObject#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see net.sf.kosmagene.ejb.campaign.universe.AstronomicalObject#getType()
	 * @see #getAstronomicalObject()
	 * @generated
	 */
    EAttribute getAstronomicalObject_Type();

    /**
	 * Returns the meta object for class '{@link net.sf.kosmagene.ejb.campaign.universe.MinorObject <em>Minor Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Minor Object</em>'.
	 * @see net.sf.kosmagene.ejb.campaign.universe.MinorObject
	 * @generated
	 */
    EClass getMinorObject();

    /**
	 * Returns the meta object for the reference '{@link net.sf.kosmagene.ejb.campaign.universe.MinorObject#getMajorObject <em>Major Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Major Object</em>'.
	 * @see net.sf.kosmagene.ejb.campaign.universe.MinorObject#getMajorObject()
	 * @see #getMinorObject()
	 * @generated
	 */
    EReference getMinorObject_MajorObject();

    /**
	 * Returns the meta object for class '{@link net.sf.kosmagene.ejb.campaign.universe.MajorObject <em>Major Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Major Object</em>'.
	 * @see net.sf.kosmagene.ejb.campaign.universe.MajorObject
	 * @generated
	 */
    EClass getMajorObject();

    /**
	 * Returns the meta object for class '{@link net.sf.kosmagene.ejb.campaign.universe.AstronomicalDescription <em>Astronomical Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Astronomical Description</em>'.
	 * @see net.sf.kosmagene.ejb.campaign.universe.AstronomicalDescription
	 * @generated
	 */
    EClass getAstronomicalDescription();

    /**
	 * Returns the meta object for class '{@link net.sf.kosmagene.ejb.campaign.universe.Cluster <em>Cluster</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Cluster</em>'.
	 * @see net.sf.kosmagene.ejb.campaign.universe.Cluster
	 * @generated
	 */
    EClass getCluster();

    /**
	 * Returns the meta object for the attribute '{@link net.sf.kosmagene.ejb.campaign.universe.Cluster#getFreeSlotCount <em>Free Slot Count</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Free Slot Count</em>'.
	 * @see net.sf.kosmagene.ejb.campaign.universe.Cluster#getFreeSlotCount()
	 * @see #getCluster()
	 * @generated
	 */
    EAttribute getCluster_FreeSlotCount();

    /**
	 * Returns the meta object for the attribute '{@link net.sf.kosmagene.ejb.campaign.universe.Cluster#getLastPluginDate <em>Last Plugin Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Last Plugin Date</em>'.
	 * @see net.sf.kosmagene.ejb.campaign.universe.Cluster#getLastPluginDate()
	 * @see #getCluster()
	 * @generated
	 */
    EAttribute getCluster_LastPluginDate();

    /**
	 * Returns the meta object for the container reference '{@link net.sf.kosmagene.ejb.campaign.universe.Cluster#getClusterNetwork <em>Cluster Network</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Cluster Network</em>'.
	 * @see net.sf.kosmagene.ejb.campaign.universe.Cluster#getClusterNetwork()
	 * @see #getCluster()
	 * @generated
	 */
    EReference getCluster_ClusterNetwork();

    /**
	 * Returns the meta object for the containment reference list '{@link net.sf.kosmagene.ejb.campaign.universe.Cluster#getAstronomicalObjects <em>Astronomical Objects</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Astronomical Objects</em>'.
	 * @see net.sf.kosmagene.ejb.campaign.universe.Cluster#getAstronomicalObjects()
	 * @see #getCluster()
	 * @generated
	 */
    EReference getCluster_AstronomicalObjects();

    /**
	 * Returns the meta object for the reference list '{@link net.sf.kosmagene.ejb.campaign.universe.Cluster#getNeighbourClusters <em>Neighbour Clusters</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Neighbour Clusters</em>'.
	 * @see net.sf.kosmagene.ejb.campaign.universe.Cluster#getNeighbourClusters()
	 * @see #getCluster()
	 * @generated
	 */
    EReference getCluster_NeighbourClusters();

    /**
	 * Returns the meta object for class '{@link net.sf.kosmagene.ejb.campaign.universe.ClusterNetwork <em>Cluster Network</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Cluster Network</em>'.
	 * @see net.sf.kosmagene.ejb.campaign.universe.ClusterNetwork
	 * @generated
	 */
    EClass getClusterNetwork();

    /**
	 * Returns the meta object for the attribute '{@link net.sf.kosmagene.ejb.campaign.universe.ClusterNetwork#isIsTrunk <em>Is Trunk</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Is Trunk</em>'.
	 * @see net.sf.kosmagene.ejb.campaign.universe.ClusterNetwork#isIsTrunk()
	 * @see #getClusterNetwork()
	 * @generated
	 */
    EAttribute getClusterNetwork_IsTrunk();

    /**
	 * Returns the meta object for the containment reference list '{@link net.sf.kosmagene.ejb.campaign.universe.ClusterNetwork#getClusters <em>Clusters</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Clusters</em>'.
	 * @see net.sf.kosmagene.ejb.campaign.universe.ClusterNetwork#getClusters()
	 * @see #getClusterNetwork()
	 * @generated
	 */
    EReference getClusterNetwork_Clusters();

    /**
	 * Returns the meta object for class '{@link net.sf.kosmagene.ejb.campaign.universe.WormHole <em>Worm Hole</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Worm Hole</em>'.
	 * @see net.sf.kosmagene.ejb.campaign.universe.WormHole
	 * @generated
	 */
    EClass getWormHole();

    /**
	 * Returns the meta object for the reference '{@link net.sf.kosmagene.ejb.campaign.universe.WormHole#getBounds <em>Bounds</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Bounds</em>'.
	 * @see net.sf.kosmagene.ejb.campaign.universe.WormHole#getBounds()
	 * @see #getWormHole()
	 * @generated
	 */
    EReference getWormHole_Bounds();

    /**
	 * Returns the meta object for class '{@link net.sf.kosmagene.ejb.campaign.universe.WormHoleBound <em>Worm Hole Bound</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Worm Hole Bound</em>'.
	 * @see net.sf.kosmagene.ejb.campaign.universe.WormHoleBound
	 * @generated
	 */
    EClass getWormHoleBound();

    /**
	 * Returns the meta object for the attribute '{@link net.sf.kosmagene.ejb.campaign.universe.WormHoleBound#getPotential <em>Potential</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Potential</em>'.
	 * @see net.sf.kosmagene.ejb.campaign.universe.WormHoleBound#getPotential()
	 * @see #getWormHoleBound()
	 * @generated
	 */
    EAttribute getWormHoleBound_Potential();

    /**
	 * Returns the meta object for the attribute '{@link net.sf.kosmagene.ejb.campaign.universe.WormHoleBound#isIsObscured <em>Is Obscured</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Is Obscured</em>'.
	 * @see net.sf.kosmagene.ejb.campaign.universe.WormHoleBound#isIsObscured()
	 * @see #getWormHoleBound()
	 * @generated
	 */
    EAttribute getWormHoleBound_IsObscured();

    /**
	 * Returns the meta object for the reference '{@link net.sf.kosmagene.ejb.campaign.universe.WormHoleBound#getWormHole <em>Worm Hole</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Worm Hole</em>'.
	 * @see net.sf.kosmagene.ejb.campaign.universe.WormHoleBound#getWormHole()
	 * @see #getWormHoleBound()
	 * @generated
	 */
    EReference getWormHoleBound_WormHole();

    /**
	 * Returns the meta object for the attribute '{@link net.sf.kosmagene.ejb.campaign.universe.WormHoleBound#getBoundType <em>Bound Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Bound Type</em>'.
	 * @see net.sf.kosmagene.ejb.campaign.universe.WormHoleBound#getBoundType()
	 * @see #getWormHoleBound()
	 * @generated
	 */
    EAttribute getWormHoleBound_BoundType();

    /**
	 * Returns the meta object for enum '{@link net.sf.kosmagene.ejb.campaign.universe.WormHoleBoundType <em>Worm Hole Bound Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Worm Hole Bound Type</em>'.
	 * @see net.sf.kosmagene.ejb.campaign.universe.WormHoleBoundType
	 * @generated
	 */
    EEnum getWormHoleBoundType();

    /**
	 * Returns the meta object for enum '{@link net.sf.kosmagene.ejb.campaign.universe.AstronomicalObjectType <em>Astronomical Object Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Astronomical Object Type</em>'.
	 * @see net.sf.kosmagene.ejb.campaign.universe.AstronomicalObjectType
	 * @generated
	 */
    EEnum getAstronomicalObjectType();

    /**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
    UniverseFactory getUniverseFactory();

    /**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
    interface Literals {

        /**
		 * The meta object literal for the '{@link net.sf.kosmagene.ejb.campaign.universe.impl.AstronomicalObjectImpl <em>Astronomical Object</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.kosmagene.ejb.campaign.universe.impl.AstronomicalObjectImpl
		 * @see net.sf.kosmagene.ejb.campaign.universe.impl.UniversePackageImpl#getAstronomicalObject()
		 * @generated
		 */
        EClass ASTRONOMICAL_OBJECT = eINSTANCE.getAstronomicalObject();

        /**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute ASTRONOMICAL_OBJECT__NAME = eINSTANCE.getAstronomicalObject_Name();

        /**
		 * The meta object literal for the '<em><b>Environment Index</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute ASTRONOMICAL_OBJECT__ENVIRONMENT_INDEX = eINSTANCE.getAstronomicalObject_EnvironmentIndex();

        /**
		 * The meta object literal for the '<em><b>Evolution Max</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute ASTRONOMICAL_OBJECT__EVOLUTION_MAX = eINSTANCE.getAstronomicalObject_EvolutionMax();

        /**
		 * The meta object literal for the '<em><b>Evolution Index</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute ASTRONOMICAL_OBJECT__EVOLUTION_INDEX = eINSTANCE.getAstronomicalObject_EvolutionIndex();

        /**
		 * The meta object literal for the '<em><b>Description</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference ASTRONOMICAL_OBJECT__DESCRIPTION = eINSTANCE.getAstronomicalObject_Description();

        /**
		 * The meta object literal for the '<em><b>Cluster</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference ASTRONOMICAL_OBJECT__CLUSTER = eINSTANCE.getAstronomicalObject_Cluster();

        /**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute ASTRONOMICAL_OBJECT__TYPE = eINSTANCE.getAstronomicalObject_Type();

        /**
		 * The meta object literal for the '{@link net.sf.kosmagene.ejb.campaign.universe.impl.MinorObjectImpl <em>Minor Object</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.kosmagene.ejb.campaign.universe.impl.MinorObjectImpl
		 * @see net.sf.kosmagene.ejb.campaign.universe.impl.UniversePackageImpl#getMinorObject()
		 * @generated
		 */
        EClass MINOR_OBJECT = eINSTANCE.getMinorObject();

        /**
		 * The meta object literal for the '<em><b>Major Object</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference MINOR_OBJECT__MAJOR_OBJECT = eINSTANCE.getMinorObject_MajorObject();

        /**
		 * The meta object literal for the '{@link net.sf.kosmagene.ejb.campaign.universe.impl.MajorObjectImpl <em>Major Object</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.kosmagene.ejb.campaign.universe.impl.MajorObjectImpl
		 * @see net.sf.kosmagene.ejb.campaign.universe.impl.UniversePackageImpl#getMajorObject()
		 * @generated
		 */
        EClass MAJOR_OBJECT = eINSTANCE.getMajorObject();

        /**
		 * The meta object literal for the '{@link net.sf.kosmagene.ejb.campaign.universe.impl.AstronomicalDescriptionImpl <em>Astronomical Description</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.kosmagene.ejb.campaign.universe.impl.AstronomicalDescriptionImpl
		 * @see net.sf.kosmagene.ejb.campaign.universe.impl.UniversePackageImpl#getAstronomicalDescription()
		 * @generated
		 */
        EClass ASTRONOMICAL_DESCRIPTION = eINSTANCE.getAstronomicalDescription();

        /**
		 * The meta object literal for the '{@link net.sf.kosmagene.ejb.campaign.universe.impl.ClusterImpl <em>Cluster</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.kosmagene.ejb.campaign.universe.impl.ClusterImpl
		 * @see net.sf.kosmagene.ejb.campaign.universe.impl.UniversePackageImpl#getCluster()
		 * @generated
		 */
        EClass CLUSTER = eINSTANCE.getCluster();

        /**
		 * The meta object literal for the '<em><b>Free Slot Count</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute CLUSTER__FREE_SLOT_COUNT = eINSTANCE.getCluster_FreeSlotCount();

        /**
		 * The meta object literal for the '<em><b>Last Plugin Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute CLUSTER__LAST_PLUGIN_DATE = eINSTANCE.getCluster_LastPluginDate();

        /**
		 * The meta object literal for the '<em><b>Cluster Network</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference CLUSTER__CLUSTER_NETWORK = eINSTANCE.getCluster_ClusterNetwork();

        /**
		 * The meta object literal for the '<em><b>Astronomical Objects</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference CLUSTER__ASTRONOMICAL_OBJECTS = eINSTANCE.getCluster_AstronomicalObjects();

        /**
		 * The meta object literal for the '<em><b>Neighbour Clusters</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference CLUSTER__NEIGHBOUR_CLUSTERS = eINSTANCE.getCluster_NeighbourClusters();

        /**
		 * The meta object literal for the '{@link net.sf.kosmagene.ejb.campaign.universe.impl.ClusterNetworkImpl <em>Cluster Network</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.kosmagene.ejb.campaign.universe.impl.ClusterNetworkImpl
		 * @see net.sf.kosmagene.ejb.campaign.universe.impl.UniversePackageImpl#getClusterNetwork()
		 * @generated
		 */
        EClass CLUSTER_NETWORK = eINSTANCE.getClusterNetwork();

        /**
		 * The meta object literal for the '<em><b>Is Trunk</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute CLUSTER_NETWORK__IS_TRUNK = eINSTANCE.getClusterNetwork_IsTrunk();

        /**
		 * The meta object literal for the '<em><b>Clusters</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference CLUSTER_NETWORK__CLUSTERS = eINSTANCE.getClusterNetwork_Clusters();

        /**
		 * The meta object literal for the '{@link net.sf.kosmagene.ejb.campaign.universe.impl.WormHoleImpl <em>Worm Hole</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.kosmagene.ejb.campaign.universe.impl.WormHoleImpl
		 * @see net.sf.kosmagene.ejb.campaign.universe.impl.UniversePackageImpl#getWormHole()
		 * @generated
		 */
        EClass WORM_HOLE = eINSTANCE.getWormHole();

        /**
		 * The meta object literal for the '<em><b>Bounds</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference WORM_HOLE__BOUNDS = eINSTANCE.getWormHole_Bounds();

        /**
		 * The meta object literal for the '{@link net.sf.kosmagene.ejb.campaign.universe.impl.WormHoleBoundImpl <em>Worm Hole Bound</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.kosmagene.ejb.campaign.universe.impl.WormHoleBoundImpl
		 * @see net.sf.kosmagene.ejb.campaign.universe.impl.UniversePackageImpl#getWormHoleBound()
		 * @generated
		 */
        EClass WORM_HOLE_BOUND = eINSTANCE.getWormHoleBound();

        /**
		 * The meta object literal for the '<em><b>Potential</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute WORM_HOLE_BOUND__POTENTIAL = eINSTANCE.getWormHoleBound_Potential();

        /**
		 * The meta object literal for the '<em><b>Is Obscured</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute WORM_HOLE_BOUND__IS_OBSCURED = eINSTANCE.getWormHoleBound_IsObscured();

        /**
		 * The meta object literal for the '<em><b>Worm Hole</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference WORM_HOLE_BOUND__WORM_HOLE = eINSTANCE.getWormHoleBound_WormHole();

        /**
		 * The meta object literal for the '<em><b>Bound Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute WORM_HOLE_BOUND__BOUND_TYPE = eINSTANCE.getWormHoleBound_BoundType();

        /**
		 * The meta object literal for the '{@link net.sf.kosmagene.ejb.campaign.universe.WormHoleBoundType <em>Worm Hole Bound Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.kosmagene.ejb.campaign.universe.WormHoleBoundType
		 * @see net.sf.kosmagene.ejb.campaign.universe.impl.UniversePackageImpl#getWormHoleBoundType()
		 * @generated
		 */
        EEnum WORM_HOLE_BOUND_TYPE = eINSTANCE.getWormHoleBoundType();

        /**
		 * The meta object literal for the '{@link net.sf.kosmagene.ejb.campaign.universe.AstronomicalObjectType <em>Astronomical Object Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.kosmagene.ejb.campaign.universe.AstronomicalObjectType
		 * @see net.sf.kosmagene.ejb.campaign.universe.impl.UniversePackageImpl#getAstronomicalObjectType()
		 * @generated
		 */
        EEnum ASTRONOMICAL_OBJECT_TYPE = eINSTANCE.getAstronomicalObjectType();
    }
}
