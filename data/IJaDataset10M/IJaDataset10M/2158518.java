package net.sourceforge.olympos.dsl.domain.domain.impl;

import net.sourceforge.olympos.dsl.domain.domain.AssociationOrderConfiguration;
import net.sourceforge.olympos.dsl.domain.domain.ClassConfiguration;
import net.sourceforge.olympos.dsl.domain.domain.DomainPackage;
import net.sourceforge.olympos.dsl.domain.domain.LabelConfiguration;
import net.sourceforge.olympos.dsl.domain.domain.OrderByConfiguration;
import net.sourceforge.olympos.dsl.domain.domain.PrimaryKeyConfiguration;
import net.sourceforge.olympos.dsl.domain.domain.SearchableConfiguration;
import net.sourceforge.olympos.dsl.domain.domain.TableConfiguration;
import net.sourceforge.olympos.dsl.domain.domain.VisibilityConfiguration;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Class Configuration</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.sourceforge.olympos.dsl.domain.domain.impl.ClassConfigurationImpl#getTable <em>Table</em>}</li>
 *   <li>{@link net.sourceforge.olympos.dsl.domain.domain.impl.ClassConfigurationImpl#getPrimaryKey <em>Primary Key</em>}</li>
 *   <li>{@link net.sourceforge.olympos.dsl.domain.domain.impl.ClassConfigurationImpl#getOrderBy <em>Order By</em>}</li>
 *   <li>{@link net.sourceforge.olympos.dsl.domain.domain.impl.ClassConfigurationImpl#getAssociationOrder <em>Association Order</em>}</li>
 *   <li>{@link net.sourceforge.olympos.dsl.domain.domain.impl.ClassConfigurationImpl#getLabel <em>Label</em>}</li>
 *   <li>{@link net.sourceforge.olympos.dsl.domain.domain.impl.ClassConfigurationImpl#getVisibility <em>Visibility</em>}</li>
 *   <li>{@link net.sourceforge.olympos.dsl.domain.domain.impl.ClassConfigurationImpl#getSearchable <em>Searchable</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ClassConfigurationImpl extends MinimalEObjectImpl.Container implements ClassConfiguration {

    /**
	 * The cached value of the '{@link #getTable() <em>Table</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTable()
	 * @generated
	 * @ordered
	 */
    protected TableConfiguration table;

    /**
	 * The cached value of the '{@link #getPrimaryKey() <em>Primary Key</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPrimaryKey()
	 * @generated
	 * @ordered
	 */
    protected PrimaryKeyConfiguration primaryKey;

    /**
	 * The cached value of the '{@link #getOrderBy() <em>Order By</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOrderBy()
	 * @generated
	 * @ordered
	 */
    protected OrderByConfiguration orderBy;

    /**
	 * The cached value of the '{@link #getAssociationOrder() <em>Association Order</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAssociationOrder()
	 * @generated
	 * @ordered
	 */
    protected AssociationOrderConfiguration associationOrder;

    /**
	 * The cached value of the '{@link #getLabel() <em>Label</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLabel()
	 * @generated
	 * @ordered
	 */
    protected LabelConfiguration label;

    /**
	 * The cached value of the '{@link #getVisibility() <em>Visibility</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVisibility()
	 * @generated
	 * @ordered
	 */
    protected VisibilityConfiguration visibility;

    /**
	 * The cached value of the '{@link #getSearchable() <em>Searchable</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSearchable()
	 * @generated
	 * @ordered
	 */
    protected SearchableConfiguration searchable;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected ClassConfigurationImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return DomainPackage.Literals.CLASS_CONFIGURATION;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public TableConfiguration getTable() {
        return table;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetTable(TableConfiguration newTable, NotificationChain msgs) {
        TableConfiguration oldTable = table;
        table = newTable;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DomainPackage.CLASS_CONFIGURATION__TABLE, oldTable, newTable);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setTable(TableConfiguration newTable) {
        if (newTable != table) {
            NotificationChain msgs = null;
            if (table != null) msgs = ((InternalEObject) table).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - DomainPackage.CLASS_CONFIGURATION__TABLE, null, msgs);
            if (newTable != null) msgs = ((InternalEObject) newTable).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - DomainPackage.CLASS_CONFIGURATION__TABLE, null, msgs);
            msgs = basicSetTable(newTable, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, DomainPackage.CLASS_CONFIGURATION__TABLE, newTable, newTable));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public PrimaryKeyConfiguration getPrimaryKey() {
        return primaryKey;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetPrimaryKey(PrimaryKeyConfiguration newPrimaryKey, NotificationChain msgs) {
        PrimaryKeyConfiguration oldPrimaryKey = primaryKey;
        primaryKey = newPrimaryKey;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DomainPackage.CLASS_CONFIGURATION__PRIMARY_KEY, oldPrimaryKey, newPrimaryKey);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setPrimaryKey(PrimaryKeyConfiguration newPrimaryKey) {
        if (newPrimaryKey != primaryKey) {
            NotificationChain msgs = null;
            if (primaryKey != null) msgs = ((InternalEObject) primaryKey).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - DomainPackage.CLASS_CONFIGURATION__PRIMARY_KEY, null, msgs);
            if (newPrimaryKey != null) msgs = ((InternalEObject) newPrimaryKey).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - DomainPackage.CLASS_CONFIGURATION__PRIMARY_KEY, null, msgs);
            msgs = basicSetPrimaryKey(newPrimaryKey, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, DomainPackage.CLASS_CONFIGURATION__PRIMARY_KEY, newPrimaryKey, newPrimaryKey));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public OrderByConfiguration getOrderBy() {
        return orderBy;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetOrderBy(OrderByConfiguration newOrderBy, NotificationChain msgs) {
        OrderByConfiguration oldOrderBy = orderBy;
        orderBy = newOrderBy;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DomainPackage.CLASS_CONFIGURATION__ORDER_BY, oldOrderBy, newOrderBy);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setOrderBy(OrderByConfiguration newOrderBy) {
        if (newOrderBy != orderBy) {
            NotificationChain msgs = null;
            if (orderBy != null) msgs = ((InternalEObject) orderBy).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - DomainPackage.CLASS_CONFIGURATION__ORDER_BY, null, msgs);
            if (newOrderBy != null) msgs = ((InternalEObject) newOrderBy).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - DomainPackage.CLASS_CONFIGURATION__ORDER_BY, null, msgs);
            msgs = basicSetOrderBy(newOrderBy, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, DomainPackage.CLASS_CONFIGURATION__ORDER_BY, newOrderBy, newOrderBy));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public AssociationOrderConfiguration getAssociationOrder() {
        return associationOrder;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetAssociationOrder(AssociationOrderConfiguration newAssociationOrder, NotificationChain msgs) {
        AssociationOrderConfiguration oldAssociationOrder = associationOrder;
        associationOrder = newAssociationOrder;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DomainPackage.CLASS_CONFIGURATION__ASSOCIATION_ORDER, oldAssociationOrder, newAssociationOrder);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setAssociationOrder(AssociationOrderConfiguration newAssociationOrder) {
        if (newAssociationOrder != associationOrder) {
            NotificationChain msgs = null;
            if (associationOrder != null) msgs = ((InternalEObject) associationOrder).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - DomainPackage.CLASS_CONFIGURATION__ASSOCIATION_ORDER, null, msgs);
            if (newAssociationOrder != null) msgs = ((InternalEObject) newAssociationOrder).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - DomainPackage.CLASS_CONFIGURATION__ASSOCIATION_ORDER, null, msgs);
            msgs = basicSetAssociationOrder(newAssociationOrder, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, DomainPackage.CLASS_CONFIGURATION__ASSOCIATION_ORDER, newAssociationOrder, newAssociationOrder));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public LabelConfiguration getLabel() {
        return label;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetLabel(LabelConfiguration newLabel, NotificationChain msgs) {
        LabelConfiguration oldLabel = label;
        label = newLabel;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DomainPackage.CLASS_CONFIGURATION__LABEL, oldLabel, newLabel);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setLabel(LabelConfiguration newLabel) {
        if (newLabel != label) {
            NotificationChain msgs = null;
            if (label != null) msgs = ((InternalEObject) label).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - DomainPackage.CLASS_CONFIGURATION__LABEL, null, msgs);
            if (newLabel != null) msgs = ((InternalEObject) newLabel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - DomainPackage.CLASS_CONFIGURATION__LABEL, null, msgs);
            msgs = basicSetLabel(newLabel, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, DomainPackage.CLASS_CONFIGURATION__LABEL, newLabel, newLabel));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public VisibilityConfiguration getVisibility() {
        return visibility;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetVisibility(VisibilityConfiguration newVisibility, NotificationChain msgs) {
        VisibilityConfiguration oldVisibility = visibility;
        visibility = newVisibility;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DomainPackage.CLASS_CONFIGURATION__VISIBILITY, oldVisibility, newVisibility);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setVisibility(VisibilityConfiguration newVisibility) {
        if (newVisibility != visibility) {
            NotificationChain msgs = null;
            if (visibility != null) msgs = ((InternalEObject) visibility).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - DomainPackage.CLASS_CONFIGURATION__VISIBILITY, null, msgs);
            if (newVisibility != null) msgs = ((InternalEObject) newVisibility).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - DomainPackage.CLASS_CONFIGURATION__VISIBILITY, null, msgs);
            msgs = basicSetVisibility(newVisibility, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, DomainPackage.CLASS_CONFIGURATION__VISIBILITY, newVisibility, newVisibility));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public SearchableConfiguration getSearchable() {
        return searchable;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetSearchable(SearchableConfiguration newSearchable, NotificationChain msgs) {
        SearchableConfiguration oldSearchable = searchable;
        searchable = newSearchable;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DomainPackage.CLASS_CONFIGURATION__SEARCHABLE, oldSearchable, newSearchable);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setSearchable(SearchableConfiguration newSearchable) {
        if (newSearchable != searchable) {
            NotificationChain msgs = null;
            if (searchable != null) msgs = ((InternalEObject) searchable).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - DomainPackage.CLASS_CONFIGURATION__SEARCHABLE, null, msgs);
            if (newSearchable != null) msgs = ((InternalEObject) newSearchable).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - DomainPackage.CLASS_CONFIGURATION__SEARCHABLE, null, msgs);
            msgs = basicSetSearchable(newSearchable, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, DomainPackage.CLASS_CONFIGURATION__SEARCHABLE, newSearchable, newSearchable));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case DomainPackage.CLASS_CONFIGURATION__TABLE:
                return basicSetTable(null, msgs);
            case DomainPackage.CLASS_CONFIGURATION__PRIMARY_KEY:
                return basicSetPrimaryKey(null, msgs);
            case DomainPackage.CLASS_CONFIGURATION__ORDER_BY:
                return basicSetOrderBy(null, msgs);
            case DomainPackage.CLASS_CONFIGURATION__ASSOCIATION_ORDER:
                return basicSetAssociationOrder(null, msgs);
            case DomainPackage.CLASS_CONFIGURATION__LABEL:
                return basicSetLabel(null, msgs);
            case DomainPackage.CLASS_CONFIGURATION__VISIBILITY:
                return basicSetVisibility(null, msgs);
            case DomainPackage.CLASS_CONFIGURATION__SEARCHABLE:
                return basicSetSearchable(null, msgs);
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
            case DomainPackage.CLASS_CONFIGURATION__TABLE:
                return getTable();
            case DomainPackage.CLASS_CONFIGURATION__PRIMARY_KEY:
                return getPrimaryKey();
            case DomainPackage.CLASS_CONFIGURATION__ORDER_BY:
                return getOrderBy();
            case DomainPackage.CLASS_CONFIGURATION__ASSOCIATION_ORDER:
                return getAssociationOrder();
            case DomainPackage.CLASS_CONFIGURATION__LABEL:
                return getLabel();
            case DomainPackage.CLASS_CONFIGURATION__VISIBILITY:
                return getVisibility();
            case DomainPackage.CLASS_CONFIGURATION__SEARCHABLE:
                return getSearchable();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch(featureID) {
            case DomainPackage.CLASS_CONFIGURATION__TABLE:
                setTable((TableConfiguration) newValue);
                return;
            case DomainPackage.CLASS_CONFIGURATION__PRIMARY_KEY:
                setPrimaryKey((PrimaryKeyConfiguration) newValue);
                return;
            case DomainPackage.CLASS_CONFIGURATION__ORDER_BY:
                setOrderBy((OrderByConfiguration) newValue);
                return;
            case DomainPackage.CLASS_CONFIGURATION__ASSOCIATION_ORDER:
                setAssociationOrder((AssociationOrderConfiguration) newValue);
                return;
            case DomainPackage.CLASS_CONFIGURATION__LABEL:
                setLabel((LabelConfiguration) newValue);
                return;
            case DomainPackage.CLASS_CONFIGURATION__VISIBILITY:
                setVisibility((VisibilityConfiguration) newValue);
                return;
            case DomainPackage.CLASS_CONFIGURATION__SEARCHABLE:
                setSearchable((SearchableConfiguration) newValue);
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
            case DomainPackage.CLASS_CONFIGURATION__TABLE:
                setTable((TableConfiguration) null);
                return;
            case DomainPackage.CLASS_CONFIGURATION__PRIMARY_KEY:
                setPrimaryKey((PrimaryKeyConfiguration) null);
                return;
            case DomainPackage.CLASS_CONFIGURATION__ORDER_BY:
                setOrderBy((OrderByConfiguration) null);
                return;
            case DomainPackage.CLASS_CONFIGURATION__ASSOCIATION_ORDER:
                setAssociationOrder((AssociationOrderConfiguration) null);
                return;
            case DomainPackage.CLASS_CONFIGURATION__LABEL:
                setLabel((LabelConfiguration) null);
                return;
            case DomainPackage.CLASS_CONFIGURATION__VISIBILITY:
                setVisibility((VisibilityConfiguration) null);
                return;
            case DomainPackage.CLASS_CONFIGURATION__SEARCHABLE:
                setSearchable((SearchableConfiguration) null);
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
            case DomainPackage.CLASS_CONFIGURATION__TABLE:
                return table != null;
            case DomainPackage.CLASS_CONFIGURATION__PRIMARY_KEY:
                return primaryKey != null;
            case DomainPackage.CLASS_CONFIGURATION__ORDER_BY:
                return orderBy != null;
            case DomainPackage.CLASS_CONFIGURATION__ASSOCIATION_ORDER:
                return associationOrder != null;
            case DomainPackage.CLASS_CONFIGURATION__LABEL:
                return label != null;
            case DomainPackage.CLASS_CONFIGURATION__VISIBILITY:
                return visibility != null;
            case DomainPackage.CLASS_CONFIGURATION__SEARCHABLE:
                return searchable != null;
        }
        return super.eIsSet(featureID);
    }
}
