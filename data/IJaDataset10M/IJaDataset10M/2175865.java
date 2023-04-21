package net.sf.kosmagene.ejb.campaign.universe.impl;

import java.util.Collection;
import java.util.Date;
import net.sf.kosmagene.ejb.campaign.impl.IdentifiableImpl;
import net.sf.kosmagene.ejb.campaign.universe.AstronomicalObject;
import net.sf.kosmagene.ejb.campaign.universe.AstronomicalObjectType;
import net.sf.kosmagene.ejb.campaign.universe.Cluster;
import net.sf.kosmagene.ejb.campaign.universe.ClusterNetwork;
import net.sf.kosmagene.ejb.campaign.universe.MajorObject;
import net.sf.kosmagene.ejb.campaign.universe.MinorObject;
import net.sf.kosmagene.ejb.campaign.universe.UniversePackage;
import net.sf.kosmagene.ejb.campaign.universe.WormHoleBound;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Cluster</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.sf.kosmagene.ejb.campaign.universe.impl.ClusterImpl#getFreeSlotCount <em>Free Slot Count</em>}</li>
 *   <li>{@link net.sf.kosmagene.ejb.campaign.universe.impl.ClusterImpl#getLastPluginDate <em>Last Plugin Date</em>}</li>
 *   <li>{@link net.sf.kosmagene.ejb.campaign.universe.impl.ClusterImpl#getClusterNetwork <em>Cluster Network</em>}</li>
 *   <li>{@link net.sf.kosmagene.ejb.campaign.universe.impl.ClusterImpl#getAstronomicalObjects <em>Astronomical Objects</em>}</li>
 *   <li>{@link net.sf.kosmagene.ejb.campaign.universe.impl.ClusterImpl#getNeighbourClusters <em>Neighbour Clusters</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ClusterImpl extends IdentifiableImpl implements Cluster {

    /**
	 * The default value of the '{@link #getFreeSlotCount() <em>Free Slot Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFreeSlotCount()
	 * @generated
	 * @ordered
	 */
    protected static final int FREE_SLOT_COUNT_EDEFAULT = 0;

    /**
	 * The cached value of the '{@link #getFreeSlotCount() <em>Free Slot Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFreeSlotCount()
	 * @generated
	 * @ordered
	 */
    protected int freeSlotCount = FREE_SLOT_COUNT_EDEFAULT;

    /**
	 * The default value of the '{@link #getLastPluginDate() <em>Last Plugin Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLastPluginDate()
	 * @generated
	 * @ordered
	 */
    protected static final Date LAST_PLUGIN_DATE_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getLastPluginDate() <em>Last Plugin Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLastPluginDate()
	 * @generated
	 * @ordered
	 */
    protected Date lastPluginDate = LAST_PLUGIN_DATE_EDEFAULT;

    /**
	 * The cached value of the '{@link #getAstronomicalObjects() <em>Astronomical Objects</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAstronomicalObjects()
	 * @generated
	 * @ordered
	 */
    protected EList<AstronomicalObject> astronomicalObjects;

    /**
	 * The cached value of the '{@link #getNeighbourClusters() <em>Neighbour Clusters</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNeighbourClusters()
	 * @generated
	 * @ordered
	 */
    protected EList<Cluster> neighbourClusters;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected ClusterImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return UniversePackage.Literals.CLUSTER;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public int getFreeSlotCount() {
        return freeSlotCount;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setFreeSlotCount(int newFreeSlotCount) {
        int oldFreeSlotCount = freeSlotCount;
        freeSlotCount = newFreeSlotCount;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, UniversePackage.CLUSTER__FREE_SLOT_COUNT, oldFreeSlotCount, freeSlotCount));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Date getLastPluginDate() {
        return lastPluginDate;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setLastPluginDate(Date newLastPluginDate) {
        Date oldLastPluginDate = lastPluginDate;
        lastPluginDate = newLastPluginDate;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, UniversePackage.CLUSTER__LAST_PLUGIN_DATE, oldLastPluginDate, lastPluginDate));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ClusterNetwork getClusterNetwork() {
        if (eContainerFeatureID() != UniversePackage.CLUSTER__CLUSTER_NETWORK) return null;
        return (ClusterNetwork) eContainer();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetClusterNetwork(ClusterNetwork newClusterNetwork, NotificationChain msgs) {
        msgs = eBasicSetContainer((InternalEObject) newClusterNetwork, UniversePackage.CLUSTER__CLUSTER_NETWORK, msgs);
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setClusterNetwork(ClusterNetwork newClusterNetwork) {
        if (newClusterNetwork != eInternalContainer() || (eContainerFeatureID() != UniversePackage.CLUSTER__CLUSTER_NETWORK && newClusterNetwork != null)) {
            if (EcoreUtil.isAncestor(this, newClusterNetwork)) throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
            NotificationChain msgs = null;
            if (eInternalContainer() != null) msgs = eBasicRemoveFromContainer(msgs);
            if (newClusterNetwork != null) msgs = ((InternalEObject) newClusterNetwork).eInverseAdd(this, UniversePackage.CLUSTER_NETWORK__CLUSTERS, ClusterNetwork.class, msgs);
            msgs = basicSetClusterNetwork(newClusterNetwork, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, UniversePackage.CLUSTER__CLUSTER_NETWORK, newClusterNetwork, newClusterNetwork));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<AstronomicalObject> getAstronomicalObjects() {
        if (astronomicalObjects == null) {
            astronomicalObjects = new EObjectContainmentEList<AstronomicalObject>(AstronomicalObject.class, this, UniversePackage.CLUSTER__ASTRONOMICAL_OBJECTS);
        }
        return astronomicalObjects;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<Cluster> getNeighbourClusters() {
        if (neighbourClusters == null) {
            neighbourClusters = new EObjectResolvingEList<Cluster>(Cluster.class, this, UniversePackage.CLUSTER__NEIGHBOUR_CLUSTERS);
        }
        return neighbourClusters;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<AstronomicalObject> getAstronomicalObjects(AstronomicalObjectType type) {
        throw new UnsupportedOperationException();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public MajorObject getMajorAstronomicalObject() {
        throw new UnsupportedOperationException();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<EList<MinorObject>> getMinorAstronomicalObjects() {
        throw new UnsupportedOperationException();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<EList<WormHoleBound>> getWormHoleBounds() {
        throw new UnsupportedOperationException();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case UniversePackage.CLUSTER__CLUSTER_NETWORK:
                if (eInternalContainer() != null) msgs = eBasicRemoveFromContainer(msgs);
                return basicSetClusterNetwork((ClusterNetwork) otherEnd, msgs);
        }
        return super.eInverseAdd(otherEnd, featureID, msgs);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case UniversePackage.CLUSTER__CLUSTER_NETWORK:
                return basicSetClusterNetwork(null, msgs);
            case UniversePackage.CLUSTER__ASTRONOMICAL_OBJECTS:
                return ((InternalEList<?>) getAstronomicalObjects()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
        switch(eContainerFeatureID()) {
            case UniversePackage.CLUSTER__CLUSTER_NETWORK:
                return eInternalContainer().eInverseRemove(this, UniversePackage.CLUSTER_NETWORK__CLUSTERS, ClusterNetwork.class, msgs);
        }
        return super.eBasicRemoveFromContainerFeature(msgs);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case UniversePackage.CLUSTER__FREE_SLOT_COUNT:
                return getFreeSlotCount();
            case UniversePackage.CLUSTER__LAST_PLUGIN_DATE:
                return getLastPluginDate();
            case UniversePackage.CLUSTER__CLUSTER_NETWORK:
                return getClusterNetwork();
            case UniversePackage.CLUSTER__ASTRONOMICAL_OBJECTS:
                return getAstronomicalObjects();
            case UniversePackage.CLUSTER__NEIGHBOUR_CLUSTERS:
                return getNeighbourClusters();
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
            case UniversePackage.CLUSTER__FREE_SLOT_COUNT:
                setFreeSlotCount((Integer) newValue);
                return;
            case UniversePackage.CLUSTER__LAST_PLUGIN_DATE:
                setLastPluginDate((Date) newValue);
                return;
            case UniversePackage.CLUSTER__CLUSTER_NETWORK:
                setClusterNetwork((ClusterNetwork) newValue);
                return;
            case UniversePackage.CLUSTER__ASTRONOMICAL_OBJECTS:
                getAstronomicalObjects().clear();
                getAstronomicalObjects().addAll((Collection<? extends AstronomicalObject>) newValue);
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
            case UniversePackage.CLUSTER__FREE_SLOT_COUNT:
                setFreeSlotCount(FREE_SLOT_COUNT_EDEFAULT);
                return;
            case UniversePackage.CLUSTER__LAST_PLUGIN_DATE:
                setLastPluginDate(LAST_PLUGIN_DATE_EDEFAULT);
                return;
            case UniversePackage.CLUSTER__CLUSTER_NETWORK:
                setClusterNetwork((ClusterNetwork) null);
                return;
            case UniversePackage.CLUSTER__ASTRONOMICAL_OBJECTS:
                getAstronomicalObjects().clear();
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
            case UniversePackage.CLUSTER__FREE_SLOT_COUNT:
                return freeSlotCount != FREE_SLOT_COUNT_EDEFAULT;
            case UniversePackage.CLUSTER__LAST_PLUGIN_DATE:
                return LAST_PLUGIN_DATE_EDEFAULT == null ? lastPluginDate != null : !LAST_PLUGIN_DATE_EDEFAULT.equals(lastPluginDate);
            case UniversePackage.CLUSTER__CLUSTER_NETWORK:
                return getClusterNetwork() != null;
            case UniversePackage.CLUSTER__ASTRONOMICAL_OBJECTS:
                return astronomicalObjects != null && !astronomicalObjects.isEmpty();
            case UniversePackage.CLUSTER__NEIGHBOUR_CLUSTERS:
                return neighbourClusters != null && !neighbourClusters.isEmpty();
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
        result.append(" (freeSlotCount: ");
        result.append(freeSlotCount);
        result.append(", lastPluginDate: ");
        result.append(lastPluginDate);
        result.append(')');
        return result.toString();
    }
}
