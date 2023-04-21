package fr.esrf.tango.pogo.pogoDsl.impl;

import fr.esrf.tango.pogo.pogoDsl.AttrProperties;
import fr.esrf.tango.pogo.pogoDsl.Attribute;
import fr.esrf.tango.pogo.pogoDsl.FireEvents;
import fr.esrf.tango.pogo.pogoDsl.InheritanceStatus;
import fr.esrf.tango.pogo.pogoDsl.PogoDslPackage;
import fr.esrf.tango.pogo.pogoDsl.Type;
import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EDataTypeEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Attribute</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link fr.esrf.tango.pogo.pogoDsl.impl.AttributeImpl#getName <em>Name</em>}</li>
 *   <li>{@link fr.esrf.tango.pogo.pogoDsl.impl.AttributeImpl#getAttType <em>Att Type</em>}</li>
 *   <li>{@link fr.esrf.tango.pogo.pogoDsl.impl.AttributeImpl#getDataType <em>Data Type</em>}</li>
 *   <li>{@link fr.esrf.tango.pogo.pogoDsl.impl.AttributeImpl#getRwType <em>Rw Type</em>}</li>
 *   <li>{@link fr.esrf.tango.pogo.pogoDsl.impl.AttributeImpl#getDisplayLevel <em>Display Level</em>}</li>
 *   <li>{@link fr.esrf.tango.pogo.pogoDsl.impl.AttributeImpl#getPolledPeriod <em>Polled Period</em>}</li>
 *   <li>{@link fr.esrf.tango.pogo.pogoDsl.impl.AttributeImpl#getMaxX <em>Max X</em>}</li>
 *   <li>{@link fr.esrf.tango.pogo.pogoDsl.impl.AttributeImpl#getMaxY <em>Max Y</em>}</li>
 *   <li>{@link fr.esrf.tango.pogo.pogoDsl.impl.AttributeImpl#getAssociatedAttr <em>Associated Attr</em>}</li>
 *   <li>{@link fr.esrf.tango.pogo.pogoDsl.impl.AttributeImpl#getMemorized <em>Memorized</em>}</li>
 *   <li>{@link fr.esrf.tango.pogo.pogoDsl.impl.AttributeImpl#getMemorizedAtInit <em>Memorized At Init</em>}</li>
 *   <li>{@link fr.esrf.tango.pogo.pogoDsl.impl.AttributeImpl#getChangeEvent <em>Change Event</em>}</li>
 *   <li>{@link fr.esrf.tango.pogo.pogoDsl.impl.AttributeImpl#getArchiveEvent <em>Archive Event</em>}</li>
 *   <li>{@link fr.esrf.tango.pogo.pogoDsl.impl.AttributeImpl#getDataReadyEvent <em>Data Ready Event</em>}</li>
 *   <li>{@link fr.esrf.tango.pogo.pogoDsl.impl.AttributeImpl#getStatus <em>Status</em>}</li>
 *   <li>{@link fr.esrf.tango.pogo.pogoDsl.impl.AttributeImpl#getProperties <em>Properties</em>}</li>
 *   <li>{@link fr.esrf.tango.pogo.pogoDsl.impl.AttributeImpl#getAllocReadMember <em>Alloc Read Member</em>}</li>
 *   <li>{@link fr.esrf.tango.pogo.pogoDsl.impl.AttributeImpl#getIsDynamic <em>Is Dynamic</em>}</li>
 *   <li>{@link fr.esrf.tango.pogo.pogoDsl.impl.AttributeImpl#getReadExcludedStates <em>Read Excluded States</em>}</li>
 *   <li>{@link fr.esrf.tango.pogo.pogoDsl.impl.AttributeImpl#getWriteExcludedStates <em>Write Excluded States</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AttributeImpl extends MinimalEObjectImpl.Container implements Attribute {

    /**
   * The default value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
    protected static final String NAME_EDEFAULT = null;

    /**
   * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
    protected String name = NAME_EDEFAULT;

    /**
   * The default value of the '{@link #getAttType() <em>Att Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAttType()
   * @generated
   * @ordered
   */
    protected static final String ATT_TYPE_EDEFAULT = null;

    /**
   * The cached value of the '{@link #getAttType() <em>Att Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAttType()
   * @generated
   * @ordered
   */
    protected String attType = ATT_TYPE_EDEFAULT;

    /**
   * The cached value of the '{@link #getDataType() <em>Data Type</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDataType()
   * @generated
   * @ordered
   */
    protected Type dataType;

    /**
   * The default value of the '{@link #getRwType() <em>Rw Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRwType()
   * @generated
   * @ordered
   */
    protected static final String RW_TYPE_EDEFAULT = null;

    /**
   * The cached value of the '{@link #getRwType() <em>Rw Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRwType()
   * @generated
   * @ordered
   */
    protected String rwType = RW_TYPE_EDEFAULT;

    /**
   * The default value of the '{@link #getDisplayLevel() <em>Display Level</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDisplayLevel()
   * @generated
   * @ordered
   */
    protected static final String DISPLAY_LEVEL_EDEFAULT = null;

    /**
   * The cached value of the '{@link #getDisplayLevel() <em>Display Level</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDisplayLevel()
   * @generated
   * @ordered
   */
    protected String displayLevel = DISPLAY_LEVEL_EDEFAULT;

    /**
   * The default value of the '{@link #getPolledPeriod() <em>Polled Period</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPolledPeriod()
   * @generated
   * @ordered
   */
    protected static final String POLLED_PERIOD_EDEFAULT = null;

    /**
   * The cached value of the '{@link #getPolledPeriod() <em>Polled Period</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPolledPeriod()
   * @generated
   * @ordered
   */
    protected String polledPeriod = POLLED_PERIOD_EDEFAULT;

    /**
   * The default value of the '{@link #getMaxX() <em>Max X</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMaxX()
   * @generated
   * @ordered
   */
    protected static final String MAX_X_EDEFAULT = null;

    /**
   * The cached value of the '{@link #getMaxX() <em>Max X</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMaxX()
   * @generated
   * @ordered
   */
    protected String maxX = MAX_X_EDEFAULT;

    /**
   * The default value of the '{@link #getMaxY() <em>Max Y</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMaxY()
   * @generated
   * @ordered
   */
    protected static final String MAX_Y_EDEFAULT = null;

    /**
   * The cached value of the '{@link #getMaxY() <em>Max Y</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMaxY()
   * @generated
   * @ordered
   */
    protected String maxY = MAX_Y_EDEFAULT;

    /**
   * The default value of the '{@link #getAssociatedAttr() <em>Associated Attr</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAssociatedAttr()
   * @generated
   * @ordered
   */
    protected static final String ASSOCIATED_ATTR_EDEFAULT = null;

    /**
   * The cached value of the '{@link #getAssociatedAttr() <em>Associated Attr</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAssociatedAttr()
   * @generated
   * @ordered
   */
    protected String associatedAttr = ASSOCIATED_ATTR_EDEFAULT;

    /**
   * The default value of the '{@link #getMemorized() <em>Memorized</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMemorized()
   * @generated
   * @ordered
   */
    protected static final String MEMORIZED_EDEFAULT = null;

    /**
   * The cached value of the '{@link #getMemorized() <em>Memorized</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMemorized()
   * @generated
   * @ordered
   */
    protected String memorized = MEMORIZED_EDEFAULT;

    /**
   * The default value of the '{@link #getMemorizedAtInit() <em>Memorized At Init</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMemorizedAtInit()
   * @generated
   * @ordered
   */
    protected static final String MEMORIZED_AT_INIT_EDEFAULT = null;

    /**
   * The cached value of the '{@link #getMemorizedAtInit() <em>Memorized At Init</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMemorizedAtInit()
   * @generated
   * @ordered
   */
    protected String memorizedAtInit = MEMORIZED_AT_INIT_EDEFAULT;

    /**
   * The cached value of the '{@link #getChangeEvent() <em>Change Event</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getChangeEvent()
   * @generated
   * @ordered
   */
    protected FireEvents changeEvent;

    /**
   * The cached value of the '{@link #getArchiveEvent() <em>Archive Event</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getArchiveEvent()
   * @generated
   * @ordered
   */
    protected FireEvents archiveEvent;

    /**
   * The cached value of the '{@link #getDataReadyEvent() <em>Data Ready Event</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDataReadyEvent()
   * @generated
   * @ordered
   */
    protected FireEvents dataReadyEvent;

    /**
   * The cached value of the '{@link #getStatus() <em>Status</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getStatus()
   * @generated
   * @ordered
   */
    protected InheritanceStatus status;

    /**
   * The cached value of the '{@link #getProperties() <em>Properties</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getProperties()
   * @generated
   * @ordered
   */
    protected AttrProperties properties;

    /**
   * The default value of the '{@link #getAllocReadMember() <em>Alloc Read Member</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAllocReadMember()
   * @generated
   * @ordered
   */
    protected static final String ALLOC_READ_MEMBER_EDEFAULT = null;

    /**
   * The cached value of the '{@link #getAllocReadMember() <em>Alloc Read Member</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAllocReadMember()
   * @generated
   * @ordered
   */
    protected String allocReadMember = ALLOC_READ_MEMBER_EDEFAULT;

    /**
   * The default value of the '{@link #getIsDynamic() <em>Is Dynamic</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getIsDynamic()
   * @generated
   * @ordered
   */
    protected static final String IS_DYNAMIC_EDEFAULT = null;

    /**
   * The cached value of the '{@link #getIsDynamic() <em>Is Dynamic</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getIsDynamic()
   * @generated
   * @ordered
   */
    protected String isDynamic = IS_DYNAMIC_EDEFAULT;

    /**
   * The cached value of the '{@link #getReadExcludedStates() <em>Read Excluded States</em>}' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getReadExcludedStates()
   * @generated
   * @ordered
   */
    protected EList<String> readExcludedStates;

    /**
   * The cached value of the '{@link #getWriteExcludedStates() <em>Write Excluded States</em>}' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getWriteExcludedStates()
   * @generated
   * @ordered
   */
    protected EList<String> writeExcludedStates;

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    protected AttributeImpl() {
        super();
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    protected EClass eStaticClass() {
        return PogoDslPackage.Literals.ATTRIBUTE;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public String getName() {
        return name;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setName(String newName) {
        String oldName = name;
        name = newName;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, PogoDslPackage.ATTRIBUTE__NAME, oldName, name));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public String getAttType() {
        return attType;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setAttType(String newAttType) {
        String oldAttType = attType;
        attType = newAttType;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, PogoDslPackage.ATTRIBUTE__ATT_TYPE, oldAttType, attType));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public Type getDataType() {
        return dataType;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public NotificationChain basicSetDataType(Type newDataType, NotificationChain msgs) {
        Type oldDataType = dataType;
        dataType = newDataType;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, PogoDslPackage.ATTRIBUTE__DATA_TYPE, oldDataType, newDataType);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setDataType(Type newDataType) {
        if (newDataType != dataType) {
            NotificationChain msgs = null;
            if (dataType != null) msgs = ((InternalEObject) dataType).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - PogoDslPackage.ATTRIBUTE__DATA_TYPE, null, msgs);
            if (newDataType != null) msgs = ((InternalEObject) newDataType).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - PogoDslPackage.ATTRIBUTE__DATA_TYPE, null, msgs);
            msgs = basicSetDataType(newDataType, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, PogoDslPackage.ATTRIBUTE__DATA_TYPE, newDataType, newDataType));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public String getRwType() {
        return rwType;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setRwType(String newRwType) {
        String oldRwType = rwType;
        rwType = newRwType;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, PogoDslPackage.ATTRIBUTE__RW_TYPE, oldRwType, rwType));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public String getDisplayLevel() {
        return displayLevel;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setDisplayLevel(String newDisplayLevel) {
        String oldDisplayLevel = displayLevel;
        displayLevel = newDisplayLevel;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, PogoDslPackage.ATTRIBUTE__DISPLAY_LEVEL, oldDisplayLevel, displayLevel));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public String getPolledPeriod() {
        return polledPeriod;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setPolledPeriod(String newPolledPeriod) {
        String oldPolledPeriod = polledPeriod;
        polledPeriod = newPolledPeriod;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, PogoDslPackage.ATTRIBUTE__POLLED_PERIOD, oldPolledPeriod, polledPeriod));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public String getMaxX() {
        return maxX;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setMaxX(String newMaxX) {
        String oldMaxX = maxX;
        maxX = newMaxX;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, PogoDslPackage.ATTRIBUTE__MAX_X, oldMaxX, maxX));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public String getMaxY() {
        return maxY;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setMaxY(String newMaxY) {
        String oldMaxY = maxY;
        maxY = newMaxY;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, PogoDslPackage.ATTRIBUTE__MAX_Y, oldMaxY, maxY));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public String getAssociatedAttr() {
        return associatedAttr;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setAssociatedAttr(String newAssociatedAttr) {
        String oldAssociatedAttr = associatedAttr;
        associatedAttr = newAssociatedAttr;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, PogoDslPackage.ATTRIBUTE__ASSOCIATED_ATTR, oldAssociatedAttr, associatedAttr));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public String getMemorized() {
        return memorized;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setMemorized(String newMemorized) {
        String oldMemorized = memorized;
        memorized = newMemorized;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, PogoDslPackage.ATTRIBUTE__MEMORIZED, oldMemorized, memorized));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public String getMemorizedAtInit() {
        return memorizedAtInit;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setMemorizedAtInit(String newMemorizedAtInit) {
        String oldMemorizedAtInit = memorizedAtInit;
        memorizedAtInit = newMemorizedAtInit;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, PogoDslPackage.ATTRIBUTE__MEMORIZED_AT_INIT, oldMemorizedAtInit, memorizedAtInit));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public FireEvents getChangeEvent() {
        return changeEvent;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public NotificationChain basicSetChangeEvent(FireEvents newChangeEvent, NotificationChain msgs) {
        FireEvents oldChangeEvent = changeEvent;
        changeEvent = newChangeEvent;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, PogoDslPackage.ATTRIBUTE__CHANGE_EVENT, oldChangeEvent, newChangeEvent);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setChangeEvent(FireEvents newChangeEvent) {
        if (newChangeEvent != changeEvent) {
            NotificationChain msgs = null;
            if (changeEvent != null) msgs = ((InternalEObject) changeEvent).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - PogoDslPackage.ATTRIBUTE__CHANGE_EVENT, null, msgs);
            if (newChangeEvent != null) msgs = ((InternalEObject) newChangeEvent).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - PogoDslPackage.ATTRIBUTE__CHANGE_EVENT, null, msgs);
            msgs = basicSetChangeEvent(newChangeEvent, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, PogoDslPackage.ATTRIBUTE__CHANGE_EVENT, newChangeEvent, newChangeEvent));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public FireEvents getArchiveEvent() {
        return archiveEvent;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public NotificationChain basicSetArchiveEvent(FireEvents newArchiveEvent, NotificationChain msgs) {
        FireEvents oldArchiveEvent = archiveEvent;
        archiveEvent = newArchiveEvent;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, PogoDslPackage.ATTRIBUTE__ARCHIVE_EVENT, oldArchiveEvent, newArchiveEvent);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setArchiveEvent(FireEvents newArchiveEvent) {
        if (newArchiveEvent != archiveEvent) {
            NotificationChain msgs = null;
            if (archiveEvent != null) msgs = ((InternalEObject) archiveEvent).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - PogoDslPackage.ATTRIBUTE__ARCHIVE_EVENT, null, msgs);
            if (newArchiveEvent != null) msgs = ((InternalEObject) newArchiveEvent).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - PogoDslPackage.ATTRIBUTE__ARCHIVE_EVENT, null, msgs);
            msgs = basicSetArchiveEvent(newArchiveEvent, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, PogoDslPackage.ATTRIBUTE__ARCHIVE_EVENT, newArchiveEvent, newArchiveEvent));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public FireEvents getDataReadyEvent() {
        return dataReadyEvent;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public NotificationChain basicSetDataReadyEvent(FireEvents newDataReadyEvent, NotificationChain msgs) {
        FireEvents oldDataReadyEvent = dataReadyEvent;
        dataReadyEvent = newDataReadyEvent;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, PogoDslPackage.ATTRIBUTE__DATA_READY_EVENT, oldDataReadyEvent, newDataReadyEvent);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setDataReadyEvent(FireEvents newDataReadyEvent) {
        if (newDataReadyEvent != dataReadyEvent) {
            NotificationChain msgs = null;
            if (dataReadyEvent != null) msgs = ((InternalEObject) dataReadyEvent).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - PogoDslPackage.ATTRIBUTE__DATA_READY_EVENT, null, msgs);
            if (newDataReadyEvent != null) msgs = ((InternalEObject) newDataReadyEvent).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - PogoDslPackage.ATTRIBUTE__DATA_READY_EVENT, null, msgs);
            msgs = basicSetDataReadyEvent(newDataReadyEvent, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, PogoDslPackage.ATTRIBUTE__DATA_READY_EVENT, newDataReadyEvent, newDataReadyEvent));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public InheritanceStatus getStatus() {
        return status;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public NotificationChain basicSetStatus(InheritanceStatus newStatus, NotificationChain msgs) {
        InheritanceStatus oldStatus = status;
        status = newStatus;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, PogoDslPackage.ATTRIBUTE__STATUS, oldStatus, newStatus);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setStatus(InheritanceStatus newStatus) {
        if (newStatus != status) {
            NotificationChain msgs = null;
            if (status != null) msgs = ((InternalEObject) status).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - PogoDslPackage.ATTRIBUTE__STATUS, null, msgs);
            if (newStatus != null) msgs = ((InternalEObject) newStatus).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - PogoDslPackage.ATTRIBUTE__STATUS, null, msgs);
            msgs = basicSetStatus(newStatus, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, PogoDslPackage.ATTRIBUTE__STATUS, newStatus, newStatus));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public AttrProperties getProperties() {
        return properties;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public NotificationChain basicSetProperties(AttrProperties newProperties, NotificationChain msgs) {
        AttrProperties oldProperties = properties;
        properties = newProperties;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, PogoDslPackage.ATTRIBUTE__PROPERTIES, oldProperties, newProperties);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setProperties(AttrProperties newProperties) {
        if (newProperties != properties) {
            NotificationChain msgs = null;
            if (properties != null) msgs = ((InternalEObject) properties).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - PogoDslPackage.ATTRIBUTE__PROPERTIES, null, msgs);
            if (newProperties != null) msgs = ((InternalEObject) newProperties).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - PogoDslPackage.ATTRIBUTE__PROPERTIES, null, msgs);
            msgs = basicSetProperties(newProperties, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, PogoDslPackage.ATTRIBUTE__PROPERTIES, newProperties, newProperties));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public String getAllocReadMember() {
        return allocReadMember;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setAllocReadMember(String newAllocReadMember) {
        String oldAllocReadMember = allocReadMember;
        allocReadMember = newAllocReadMember;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, PogoDslPackage.ATTRIBUTE__ALLOC_READ_MEMBER, oldAllocReadMember, allocReadMember));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public String getIsDynamic() {
        return isDynamic;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setIsDynamic(String newIsDynamic) {
        String oldIsDynamic = isDynamic;
        isDynamic = newIsDynamic;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, PogoDslPackage.ATTRIBUTE__IS_DYNAMIC, oldIsDynamic, isDynamic));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EList<String> getReadExcludedStates() {
        if (readExcludedStates == null) {
            readExcludedStates = new EDataTypeEList<String>(String.class, this, PogoDslPackage.ATTRIBUTE__READ_EXCLUDED_STATES);
        }
        return readExcludedStates;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EList<String> getWriteExcludedStates() {
        if (writeExcludedStates == null) {
            writeExcludedStates = new EDataTypeEList<String>(String.class, this, PogoDslPackage.ATTRIBUTE__WRITE_EXCLUDED_STATES);
        }
        return writeExcludedStates;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case PogoDslPackage.ATTRIBUTE__DATA_TYPE:
                return basicSetDataType(null, msgs);
            case PogoDslPackage.ATTRIBUTE__CHANGE_EVENT:
                return basicSetChangeEvent(null, msgs);
            case PogoDslPackage.ATTRIBUTE__ARCHIVE_EVENT:
                return basicSetArchiveEvent(null, msgs);
            case PogoDslPackage.ATTRIBUTE__DATA_READY_EVENT:
                return basicSetDataReadyEvent(null, msgs);
            case PogoDslPackage.ATTRIBUTE__STATUS:
                return basicSetStatus(null, msgs);
            case PogoDslPackage.ATTRIBUTE__PROPERTIES:
                return basicSetProperties(null, msgs);
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
            case PogoDslPackage.ATTRIBUTE__NAME:
                return getName();
            case PogoDslPackage.ATTRIBUTE__ATT_TYPE:
                return getAttType();
            case PogoDslPackage.ATTRIBUTE__DATA_TYPE:
                return getDataType();
            case PogoDslPackage.ATTRIBUTE__RW_TYPE:
                return getRwType();
            case PogoDslPackage.ATTRIBUTE__DISPLAY_LEVEL:
                return getDisplayLevel();
            case PogoDslPackage.ATTRIBUTE__POLLED_PERIOD:
                return getPolledPeriod();
            case PogoDslPackage.ATTRIBUTE__MAX_X:
                return getMaxX();
            case PogoDslPackage.ATTRIBUTE__MAX_Y:
                return getMaxY();
            case PogoDslPackage.ATTRIBUTE__ASSOCIATED_ATTR:
                return getAssociatedAttr();
            case PogoDslPackage.ATTRIBUTE__MEMORIZED:
                return getMemorized();
            case PogoDslPackage.ATTRIBUTE__MEMORIZED_AT_INIT:
                return getMemorizedAtInit();
            case PogoDslPackage.ATTRIBUTE__CHANGE_EVENT:
                return getChangeEvent();
            case PogoDslPackage.ATTRIBUTE__ARCHIVE_EVENT:
                return getArchiveEvent();
            case PogoDslPackage.ATTRIBUTE__DATA_READY_EVENT:
                return getDataReadyEvent();
            case PogoDslPackage.ATTRIBUTE__STATUS:
                return getStatus();
            case PogoDslPackage.ATTRIBUTE__PROPERTIES:
                return getProperties();
            case PogoDslPackage.ATTRIBUTE__ALLOC_READ_MEMBER:
                return getAllocReadMember();
            case PogoDslPackage.ATTRIBUTE__IS_DYNAMIC:
                return getIsDynamic();
            case PogoDslPackage.ATTRIBUTE__READ_EXCLUDED_STATES:
                return getReadExcludedStates();
            case PogoDslPackage.ATTRIBUTE__WRITE_EXCLUDED_STATES:
                return getWriteExcludedStates();
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
            case PogoDslPackage.ATTRIBUTE__NAME:
                setName((String) newValue);
                return;
            case PogoDslPackage.ATTRIBUTE__ATT_TYPE:
                setAttType((String) newValue);
                return;
            case PogoDslPackage.ATTRIBUTE__DATA_TYPE:
                setDataType((Type) newValue);
                return;
            case PogoDslPackage.ATTRIBUTE__RW_TYPE:
                setRwType((String) newValue);
                return;
            case PogoDslPackage.ATTRIBUTE__DISPLAY_LEVEL:
                setDisplayLevel((String) newValue);
                return;
            case PogoDslPackage.ATTRIBUTE__POLLED_PERIOD:
                setPolledPeriod((String) newValue);
                return;
            case PogoDslPackage.ATTRIBUTE__MAX_X:
                setMaxX((String) newValue);
                return;
            case PogoDslPackage.ATTRIBUTE__MAX_Y:
                setMaxY((String) newValue);
                return;
            case PogoDslPackage.ATTRIBUTE__ASSOCIATED_ATTR:
                setAssociatedAttr((String) newValue);
                return;
            case PogoDslPackage.ATTRIBUTE__MEMORIZED:
                setMemorized((String) newValue);
                return;
            case PogoDslPackage.ATTRIBUTE__MEMORIZED_AT_INIT:
                setMemorizedAtInit((String) newValue);
                return;
            case PogoDslPackage.ATTRIBUTE__CHANGE_EVENT:
                setChangeEvent((FireEvents) newValue);
                return;
            case PogoDslPackage.ATTRIBUTE__ARCHIVE_EVENT:
                setArchiveEvent((FireEvents) newValue);
                return;
            case PogoDslPackage.ATTRIBUTE__DATA_READY_EVENT:
                setDataReadyEvent((FireEvents) newValue);
                return;
            case PogoDslPackage.ATTRIBUTE__STATUS:
                setStatus((InheritanceStatus) newValue);
                return;
            case PogoDslPackage.ATTRIBUTE__PROPERTIES:
                setProperties((AttrProperties) newValue);
                return;
            case PogoDslPackage.ATTRIBUTE__ALLOC_READ_MEMBER:
                setAllocReadMember((String) newValue);
                return;
            case PogoDslPackage.ATTRIBUTE__IS_DYNAMIC:
                setIsDynamic((String) newValue);
                return;
            case PogoDslPackage.ATTRIBUTE__READ_EXCLUDED_STATES:
                getReadExcludedStates().clear();
                getReadExcludedStates().addAll((Collection<? extends String>) newValue);
                return;
            case PogoDslPackage.ATTRIBUTE__WRITE_EXCLUDED_STATES:
                getWriteExcludedStates().clear();
                getWriteExcludedStates().addAll((Collection<? extends String>) newValue);
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
            case PogoDslPackage.ATTRIBUTE__NAME:
                setName(NAME_EDEFAULT);
                return;
            case PogoDslPackage.ATTRIBUTE__ATT_TYPE:
                setAttType(ATT_TYPE_EDEFAULT);
                return;
            case PogoDslPackage.ATTRIBUTE__DATA_TYPE:
                setDataType((Type) null);
                return;
            case PogoDslPackage.ATTRIBUTE__RW_TYPE:
                setRwType(RW_TYPE_EDEFAULT);
                return;
            case PogoDslPackage.ATTRIBUTE__DISPLAY_LEVEL:
                setDisplayLevel(DISPLAY_LEVEL_EDEFAULT);
                return;
            case PogoDslPackage.ATTRIBUTE__POLLED_PERIOD:
                setPolledPeriod(POLLED_PERIOD_EDEFAULT);
                return;
            case PogoDslPackage.ATTRIBUTE__MAX_X:
                setMaxX(MAX_X_EDEFAULT);
                return;
            case PogoDslPackage.ATTRIBUTE__MAX_Y:
                setMaxY(MAX_Y_EDEFAULT);
                return;
            case PogoDslPackage.ATTRIBUTE__ASSOCIATED_ATTR:
                setAssociatedAttr(ASSOCIATED_ATTR_EDEFAULT);
                return;
            case PogoDslPackage.ATTRIBUTE__MEMORIZED:
                setMemorized(MEMORIZED_EDEFAULT);
                return;
            case PogoDslPackage.ATTRIBUTE__MEMORIZED_AT_INIT:
                setMemorizedAtInit(MEMORIZED_AT_INIT_EDEFAULT);
                return;
            case PogoDslPackage.ATTRIBUTE__CHANGE_EVENT:
                setChangeEvent((FireEvents) null);
                return;
            case PogoDslPackage.ATTRIBUTE__ARCHIVE_EVENT:
                setArchiveEvent((FireEvents) null);
                return;
            case PogoDslPackage.ATTRIBUTE__DATA_READY_EVENT:
                setDataReadyEvent((FireEvents) null);
                return;
            case PogoDslPackage.ATTRIBUTE__STATUS:
                setStatus((InheritanceStatus) null);
                return;
            case PogoDslPackage.ATTRIBUTE__PROPERTIES:
                setProperties((AttrProperties) null);
                return;
            case PogoDslPackage.ATTRIBUTE__ALLOC_READ_MEMBER:
                setAllocReadMember(ALLOC_READ_MEMBER_EDEFAULT);
                return;
            case PogoDslPackage.ATTRIBUTE__IS_DYNAMIC:
                setIsDynamic(IS_DYNAMIC_EDEFAULT);
                return;
            case PogoDslPackage.ATTRIBUTE__READ_EXCLUDED_STATES:
                getReadExcludedStates().clear();
                return;
            case PogoDslPackage.ATTRIBUTE__WRITE_EXCLUDED_STATES:
                getWriteExcludedStates().clear();
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
            case PogoDslPackage.ATTRIBUTE__NAME:
                return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
            case PogoDslPackage.ATTRIBUTE__ATT_TYPE:
                return ATT_TYPE_EDEFAULT == null ? attType != null : !ATT_TYPE_EDEFAULT.equals(attType);
            case PogoDslPackage.ATTRIBUTE__DATA_TYPE:
                return dataType != null;
            case PogoDslPackage.ATTRIBUTE__RW_TYPE:
                return RW_TYPE_EDEFAULT == null ? rwType != null : !RW_TYPE_EDEFAULT.equals(rwType);
            case PogoDslPackage.ATTRIBUTE__DISPLAY_LEVEL:
                return DISPLAY_LEVEL_EDEFAULT == null ? displayLevel != null : !DISPLAY_LEVEL_EDEFAULT.equals(displayLevel);
            case PogoDslPackage.ATTRIBUTE__POLLED_PERIOD:
                return POLLED_PERIOD_EDEFAULT == null ? polledPeriod != null : !POLLED_PERIOD_EDEFAULT.equals(polledPeriod);
            case PogoDslPackage.ATTRIBUTE__MAX_X:
                return MAX_X_EDEFAULT == null ? maxX != null : !MAX_X_EDEFAULT.equals(maxX);
            case PogoDslPackage.ATTRIBUTE__MAX_Y:
                return MAX_Y_EDEFAULT == null ? maxY != null : !MAX_Y_EDEFAULT.equals(maxY);
            case PogoDslPackage.ATTRIBUTE__ASSOCIATED_ATTR:
                return ASSOCIATED_ATTR_EDEFAULT == null ? associatedAttr != null : !ASSOCIATED_ATTR_EDEFAULT.equals(associatedAttr);
            case PogoDslPackage.ATTRIBUTE__MEMORIZED:
                return MEMORIZED_EDEFAULT == null ? memorized != null : !MEMORIZED_EDEFAULT.equals(memorized);
            case PogoDslPackage.ATTRIBUTE__MEMORIZED_AT_INIT:
                return MEMORIZED_AT_INIT_EDEFAULT == null ? memorizedAtInit != null : !MEMORIZED_AT_INIT_EDEFAULT.equals(memorizedAtInit);
            case PogoDslPackage.ATTRIBUTE__CHANGE_EVENT:
                return changeEvent != null;
            case PogoDslPackage.ATTRIBUTE__ARCHIVE_EVENT:
                return archiveEvent != null;
            case PogoDslPackage.ATTRIBUTE__DATA_READY_EVENT:
                return dataReadyEvent != null;
            case PogoDslPackage.ATTRIBUTE__STATUS:
                return status != null;
            case PogoDslPackage.ATTRIBUTE__PROPERTIES:
                return properties != null;
            case PogoDslPackage.ATTRIBUTE__ALLOC_READ_MEMBER:
                return ALLOC_READ_MEMBER_EDEFAULT == null ? allocReadMember != null : !ALLOC_READ_MEMBER_EDEFAULT.equals(allocReadMember);
            case PogoDslPackage.ATTRIBUTE__IS_DYNAMIC:
                return IS_DYNAMIC_EDEFAULT == null ? isDynamic != null : !IS_DYNAMIC_EDEFAULT.equals(isDynamic);
            case PogoDslPackage.ATTRIBUTE__READ_EXCLUDED_STATES:
                return readExcludedStates != null && !readExcludedStates.isEmpty();
            case PogoDslPackage.ATTRIBUTE__WRITE_EXCLUDED_STATES:
                return writeExcludedStates != null && !writeExcludedStates.isEmpty();
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
        result.append(" (name: ");
        result.append(name);
        result.append(", attType: ");
        result.append(attType);
        result.append(", rwType: ");
        result.append(rwType);
        result.append(", displayLevel: ");
        result.append(displayLevel);
        result.append(", polledPeriod: ");
        result.append(polledPeriod);
        result.append(", maxX: ");
        result.append(maxX);
        result.append(", maxY: ");
        result.append(maxY);
        result.append(", associatedAttr: ");
        result.append(associatedAttr);
        result.append(", memorized: ");
        result.append(memorized);
        result.append(", memorizedAtInit: ");
        result.append(memorizedAtInit);
        result.append(", allocReadMember: ");
        result.append(allocReadMember);
        result.append(", isDynamic: ");
        result.append(isDynamic);
        result.append(", readExcludedStates: ");
        result.append(readExcludedStates);
        result.append(", writeExcludedStates: ");
        result.append(writeExcludedStates);
        result.append(')');
        return result.toString();
    }
}
