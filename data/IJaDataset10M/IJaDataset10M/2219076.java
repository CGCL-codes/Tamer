package fi.jab.esb.config.xml.impl;

import fi.jab.esb.config.xml.SqlMessageFilterType;
import fi.jab.esb.config.xml.XmlPackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Sql Message Filter Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link fi.jab.esb.config.xml.impl.SqlMessageFilterTypeImpl#isErrorDelete <em>Error Delete</em>}</li>
 *   <li>{@link fi.jab.esb.config.xml.impl.SqlMessageFilterTypeImpl#getInsertTimestampColumn <em>Insert Timestamp Column</em>}</li>
 *   <li>{@link fi.jab.esb.config.xml.impl.SqlMessageFilterTypeImpl#getMessageColumn <em>Message Column</em>}</li>
 *   <li>{@link fi.jab.esb.config.xml.impl.SqlMessageFilterTypeImpl#getMessageIdColumn <em>Message Id Column</em>}</li>
 *   <li>{@link fi.jab.esb.config.xml.impl.SqlMessageFilterTypeImpl#getOrderBy <em>Order By</em>}</li>
 *   <li>{@link fi.jab.esb.config.xml.impl.SqlMessageFilterTypeImpl#isPostDelete <em>Post Delete</em>}</li>
 *   <li>{@link fi.jab.esb.config.xml.impl.SqlMessageFilterTypeImpl#getStatusColumn <em>Status Column</em>}</li>
 *   <li>{@link fi.jab.esb.config.xml.impl.SqlMessageFilterTypeImpl#getTablename <em>Tablename</em>}</li>
 *   <li>{@link fi.jab.esb.config.xml.impl.SqlMessageFilterTypeImpl#getWhereCondition <em>Where Condition</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SqlMessageFilterTypeImpl extends EObjectImpl implements SqlMessageFilterType {

    /**
   * The default value of the '{@link #isErrorDelete() <em>Error Delete</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isErrorDelete()
   * @generated
   * @ordered
   */
    protected static final boolean ERROR_DELETE_EDEFAULT = true;

    /**
   * The cached value of the '{@link #isErrorDelete() <em>Error Delete</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isErrorDelete()
   * @generated
   * @ordered
   */
    protected boolean errorDelete = ERROR_DELETE_EDEFAULT;

    /**
   * This is true if the Error Delete attribute has been set.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
    protected boolean errorDeleteESet;

    /**
   * The default value of the '{@link #getInsertTimestampColumn() <em>Insert Timestamp Column</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getInsertTimestampColumn()
   * @generated
   * @ordered
   */
    protected static final String INSERT_TIMESTAMP_COLUMN_EDEFAULT = "insert_timestamp";

    /**
   * The cached value of the '{@link #getInsertTimestampColumn() <em>Insert Timestamp Column</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getInsertTimestampColumn()
   * @generated
   * @ordered
   */
    protected String insertTimestampColumn = INSERT_TIMESTAMP_COLUMN_EDEFAULT;

    /**
   * This is true if the Insert Timestamp Column attribute has been set.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
    protected boolean insertTimestampColumnESet;

    /**
   * The default value of the '{@link #getMessageColumn() <em>Message Column</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMessageColumn()
   * @generated
   * @ordered
   */
    protected static final String MESSAGE_COLUMN_EDEFAULT = "message";

    /**
   * The cached value of the '{@link #getMessageColumn() <em>Message Column</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMessageColumn()
   * @generated
   * @ordered
   */
    protected String messageColumn = MESSAGE_COLUMN_EDEFAULT;

    /**
   * This is true if the Message Column attribute has been set.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
    protected boolean messageColumnESet;

    /**
   * The default value of the '{@link #getMessageIdColumn() <em>Message Id Column</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMessageIdColumn()
   * @generated
   * @ordered
   */
    protected static final String MESSAGE_ID_COLUMN_EDEFAULT = "message_id";

    /**
   * The cached value of the '{@link #getMessageIdColumn() <em>Message Id Column</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMessageIdColumn()
   * @generated
   * @ordered
   */
    protected String messageIdColumn = MESSAGE_ID_COLUMN_EDEFAULT;

    /**
   * This is true if the Message Id Column attribute has been set.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
    protected boolean messageIdColumnESet;

    /**
   * The default value of the '{@link #getOrderBy() <em>Order By</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOrderBy()
   * @generated
   * @ordered
   */
    protected static final String ORDER_BY_EDEFAULT = "";

    /**
   * The cached value of the '{@link #getOrderBy() <em>Order By</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOrderBy()
   * @generated
   * @ordered
   */
    protected String orderBy = ORDER_BY_EDEFAULT;

    /**
   * This is true if the Order By attribute has been set.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
    protected boolean orderByESet;

    /**
   * The default value of the '{@link #isPostDelete() <em>Post Delete</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isPostDelete()
   * @generated
   * @ordered
   */
    protected static final boolean POST_DELETE_EDEFAULT = true;

    /**
   * The cached value of the '{@link #isPostDelete() <em>Post Delete</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isPostDelete()
   * @generated
   * @ordered
   */
    protected boolean postDelete = POST_DELETE_EDEFAULT;

    /**
   * This is true if the Post Delete attribute has been set.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
    protected boolean postDeleteESet;

    /**
   * The default value of the '{@link #getStatusColumn() <em>Status Column</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getStatusColumn()
   * @generated
   * @ordered
   */
    protected static final String STATUS_COLUMN_EDEFAULT = "status";

    /**
   * The cached value of the '{@link #getStatusColumn() <em>Status Column</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getStatusColumn()
   * @generated
   * @ordered
   */
    protected String statusColumn = STATUS_COLUMN_EDEFAULT;

    /**
   * This is true if the Status Column attribute has been set.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
    protected boolean statusColumnESet;

    /**
   * The default value of the '{@link #getTablename() <em>Tablename</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTablename()
   * @generated
   * @ordered
   */
    protected static final String TABLENAME_EDEFAULT = null;

    /**
   * The cached value of the '{@link #getTablename() <em>Tablename</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTablename()
   * @generated
   * @ordered
   */
    protected String tablename = TABLENAME_EDEFAULT;

    /**
   * The default value of the '{@link #getWhereCondition() <em>Where Condition</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getWhereCondition()
   * @generated
   * @ordered
   */
    protected static final String WHERE_CONDITION_EDEFAULT = "";

    /**
   * The cached value of the '{@link #getWhereCondition() <em>Where Condition</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getWhereCondition()
   * @generated
   * @ordered
   */
    protected String whereCondition = WHERE_CONDITION_EDEFAULT;

    /**
   * This is true if the Where Condition attribute has been set.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
    protected boolean whereConditionESet;

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    protected SqlMessageFilterTypeImpl() {
        super();
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    protected EClass eStaticClass() {
        return XmlPackage.Literals.SQL_MESSAGE_FILTER_TYPE;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public boolean isErrorDelete() {
        return errorDelete;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setErrorDelete(boolean newErrorDelete) {
        boolean oldErrorDelete = errorDelete;
        errorDelete = newErrorDelete;
        boolean oldErrorDeleteESet = errorDeleteESet;
        errorDeleteESet = true;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, XmlPackage.SQL_MESSAGE_FILTER_TYPE__ERROR_DELETE, oldErrorDelete, errorDelete, !oldErrorDeleteESet));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void unsetErrorDelete() {
        boolean oldErrorDelete = errorDelete;
        boolean oldErrorDeleteESet = errorDeleteESet;
        errorDelete = ERROR_DELETE_EDEFAULT;
        errorDeleteESet = false;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.UNSET, XmlPackage.SQL_MESSAGE_FILTER_TYPE__ERROR_DELETE, oldErrorDelete, ERROR_DELETE_EDEFAULT, oldErrorDeleteESet));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public boolean isSetErrorDelete() {
        return errorDeleteESet;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public String getInsertTimestampColumn() {
        return insertTimestampColumn;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setInsertTimestampColumn(String newInsertTimestampColumn) {
        String oldInsertTimestampColumn = insertTimestampColumn;
        insertTimestampColumn = newInsertTimestampColumn;
        boolean oldInsertTimestampColumnESet = insertTimestampColumnESet;
        insertTimestampColumnESet = true;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, XmlPackage.SQL_MESSAGE_FILTER_TYPE__INSERT_TIMESTAMP_COLUMN, oldInsertTimestampColumn, insertTimestampColumn, !oldInsertTimestampColumnESet));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void unsetInsertTimestampColumn() {
        String oldInsertTimestampColumn = insertTimestampColumn;
        boolean oldInsertTimestampColumnESet = insertTimestampColumnESet;
        insertTimestampColumn = INSERT_TIMESTAMP_COLUMN_EDEFAULT;
        insertTimestampColumnESet = false;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.UNSET, XmlPackage.SQL_MESSAGE_FILTER_TYPE__INSERT_TIMESTAMP_COLUMN, oldInsertTimestampColumn, INSERT_TIMESTAMP_COLUMN_EDEFAULT, oldInsertTimestampColumnESet));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public boolean isSetInsertTimestampColumn() {
        return insertTimestampColumnESet;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public String getMessageColumn() {
        return messageColumn;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setMessageColumn(String newMessageColumn) {
        String oldMessageColumn = messageColumn;
        messageColumn = newMessageColumn;
        boolean oldMessageColumnESet = messageColumnESet;
        messageColumnESet = true;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, XmlPackage.SQL_MESSAGE_FILTER_TYPE__MESSAGE_COLUMN, oldMessageColumn, messageColumn, !oldMessageColumnESet));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void unsetMessageColumn() {
        String oldMessageColumn = messageColumn;
        boolean oldMessageColumnESet = messageColumnESet;
        messageColumn = MESSAGE_COLUMN_EDEFAULT;
        messageColumnESet = false;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.UNSET, XmlPackage.SQL_MESSAGE_FILTER_TYPE__MESSAGE_COLUMN, oldMessageColumn, MESSAGE_COLUMN_EDEFAULT, oldMessageColumnESet));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public boolean isSetMessageColumn() {
        return messageColumnESet;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public String getMessageIdColumn() {
        return messageIdColumn;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setMessageIdColumn(String newMessageIdColumn) {
        String oldMessageIdColumn = messageIdColumn;
        messageIdColumn = newMessageIdColumn;
        boolean oldMessageIdColumnESet = messageIdColumnESet;
        messageIdColumnESet = true;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, XmlPackage.SQL_MESSAGE_FILTER_TYPE__MESSAGE_ID_COLUMN, oldMessageIdColumn, messageIdColumn, !oldMessageIdColumnESet));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void unsetMessageIdColumn() {
        String oldMessageIdColumn = messageIdColumn;
        boolean oldMessageIdColumnESet = messageIdColumnESet;
        messageIdColumn = MESSAGE_ID_COLUMN_EDEFAULT;
        messageIdColumnESet = false;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.UNSET, XmlPackage.SQL_MESSAGE_FILTER_TYPE__MESSAGE_ID_COLUMN, oldMessageIdColumn, MESSAGE_ID_COLUMN_EDEFAULT, oldMessageIdColumnESet));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public boolean isSetMessageIdColumn() {
        return messageIdColumnESet;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public String getOrderBy() {
        return orderBy;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setOrderBy(String newOrderBy) {
        String oldOrderBy = orderBy;
        orderBy = newOrderBy;
        boolean oldOrderByESet = orderByESet;
        orderByESet = true;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, XmlPackage.SQL_MESSAGE_FILTER_TYPE__ORDER_BY, oldOrderBy, orderBy, !oldOrderByESet));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void unsetOrderBy() {
        String oldOrderBy = orderBy;
        boolean oldOrderByESet = orderByESet;
        orderBy = ORDER_BY_EDEFAULT;
        orderByESet = false;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.UNSET, XmlPackage.SQL_MESSAGE_FILTER_TYPE__ORDER_BY, oldOrderBy, ORDER_BY_EDEFAULT, oldOrderByESet));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public boolean isSetOrderBy() {
        return orderByESet;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public boolean isPostDelete() {
        return postDelete;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setPostDelete(boolean newPostDelete) {
        boolean oldPostDelete = postDelete;
        postDelete = newPostDelete;
        boolean oldPostDeleteESet = postDeleteESet;
        postDeleteESet = true;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, XmlPackage.SQL_MESSAGE_FILTER_TYPE__POST_DELETE, oldPostDelete, postDelete, !oldPostDeleteESet));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void unsetPostDelete() {
        boolean oldPostDelete = postDelete;
        boolean oldPostDeleteESet = postDeleteESet;
        postDelete = POST_DELETE_EDEFAULT;
        postDeleteESet = false;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.UNSET, XmlPackage.SQL_MESSAGE_FILTER_TYPE__POST_DELETE, oldPostDelete, POST_DELETE_EDEFAULT, oldPostDeleteESet));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public boolean isSetPostDelete() {
        return postDeleteESet;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public String getStatusColumn() {
        return statusColumn;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setStatusColumn(String newStatusColumn) {
        String oldStatusColumn = statusColumn;
        statusColumn = newStatusColumn;
        boolean oldStatusColumnESet = statusColumnESet;
        statusColumnESet = true;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, XmlPackage.SQL_MESSAGE_FILTER_TYPE__STATUS_COLUMN, oldStatusColumn, statusColumn, !oldStatusColumnESet));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void unsetStatusColumn() {
        String oldStatusColumn = statusColumn;
        boolean oldStatusColumnESet = statusColumnESet;
        statusColumn = STATUS_COLUMN_EDEFAULT;
        statusColumnESet = false;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.UNSET, XmlPackage.SQL_MESSAGE_FILTER_TYPE__STATUS_COLUMN, oldStatusColumn, STATUS_COLUMN_EDEFAULT, oldStatusColumnESet));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public boolean isSetStatusColumn() {
        return statusColumnESet;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public String getTablename() {
        return tablename;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setTablename(String newTablename) {
        String oldTablename = tablename;
        tablename = newTablename;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, XmlPackage.SQL_MESSAGE_FILTER_TYPE__TABLENAME, oldTablename, tablename));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public String getWhereCondition() {
        return whereCondition;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setWhereCondition(String newWhereCondition) {
        String oldWhereCondition = whereCondition;
        whereCondition = newWhereCondition;
        boolean oldWhereConditionESet = whereConditionESet;
        whereConditionESet = true;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, XmlPackage.SQL_MESSAGE_FILTER_TYPE__WHERE_CONDITION, oldWhereCondition, whereCondition, !oldWhereConditionESet));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void unsetWhereCondition() {
        String oldWhereCondition = whereCondition;
        boolean oldWhereConditionESet = whereConditionESet;
        whereCondition = WHERE_CONDITION_EDEFAULT;
        whereConditionESet = false;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.UNSET, XmlPackage.SQL_MESSAGE_FILTER_TYPE__WHERE_CONDITION, oldWhereCondition, WHERE_CONDITION_EDEFAULT, oldWhereConditionESet));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public boolean isSetWhereCondition() {
        return whereConditionESet;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case XmlPackage.SQL_MESSAGE_FILTER_TYPE__ERROR_DELETE:
                return isErrorDelete() ? Boolean.TRUE : Boolean.FALSE;
            case XmlPackage.SQL_MESSAGE_FILTER_TYPE__INSERT_TIMESTAMP_COLUMN:
                return getInsertTimestampColumn();
            case XmlPackage.SQL_MESSAGE_FILTER_TYPE__MESSAGE_COLUMN:
                return getMessageColumn();
            case XmlPackage.SQL_MESSAGE_FILTER_TYPE__MESSAGE_ID_COLUMN:
                return getMessageIdColumn();
            case XmlPackage.SQL_MESSAGE_FILTER_TYPE__ORDER_BY:
                return getOrderBy();
            case XmlPackage.SQL_MESSAGE_FILTER_TYPE__POST_DELETE:
                return isPostDelete() ? Boolean.TRUE : Boolean.FALSE;
            case XmlPackage.SQL_MESSAGE_FILTER_TYPE__STATUS_COLUMN:
                return getStatusColumn();
            case XmlPackage.SQL_MESSAGE_FILTER_TYPE__TABLENAME:
                return getTablename();
            case XmlPackage.SQL_MESSAGE_FILTER_TYPE__WHERE_CONDITION:
                return getWhereCondition();
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
            case XmlPackage.SQL_MESSAGE_FILTER_TYPE__ERROR_DELETE:
                setErrorDelete(((Boolean) newValue).booleanValue());
                return;
            case XmlPackage.SQL_MESSAGE_FILTER_TYPE__INSERT_TIMESTAMP_COLUMN:
                setInsertTimestampColumn((String) newValue);
                return;
            case XmlPackage.SQL_MESSAGE_FILTER_TYPE__MESSAGE_COLUMN:
                setMessageColumn((String) newValue);
                return;
            case XmlPackage.SQL_MESSAGE_FILTER_TYPE__MESSAGE_ID_COLUMN:
                setMessageIdColumn((String) newValue);
                return;
            case XmlPackage.SQL_MESSAGE_FILTER_TYPE__ORDER_BY:
                setOrderBy((String) newValue);
                return;
            case XmlPackage.SQL_MESSAGE_FILTER_TYPE__POST_DELETE:
                setPostDelete(((Boolean) newValue).booleanValue());
                return;
            case XmlPackage.SQL_MESSAGE_FILTER_TYPE__STATUS_COLUMN:
                setStatusColumn((String) newValue);
                return;
            case XmlPackage.SQL_MESSAGE_FILTER_TYPE__TABLENAME:
                setTablename((String) newValue);
                return;
            case XmlPackage.SQL_MESSAGE_FILTER_TYPE__WHERE_CONDITION:
                setWhereCondition((String) newValue);
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
            case XmlPackage.SQL_MESSAGE_FILTER_TYPE__ERROR_DELETE:
                unsetErrorDelete();
                return;
            case XmlPackage.SQL_MESSAGE_FILTER_TYPE__INSERT_TIMESTAMP_COLUMN:
                unsetInsertTimestampColumn();
                return;
            case XmlPackage.SQL_MESSAGE_FILTER_TYPE__MESSAGE_COLUMN:
                unsetMessageColumn();
                return;
            case XmlPackage.SQL_MESSAGE_FILTER_TYPE__MESSAGE_ID_COLUMN:
                unsetMessageIdColumn();
                return;
            case XmlPackage.SQL_MESSAGE_FILTER_TYPE__ORDER_BY:
                unsetOrderBy();
                return;
            case XmlPackage.SQL_MESSAGE_FILTER_TYPE__POST_DELETE:
                unsetPostDelete();
                return;
            case XmlPackage.SQL_MESSAGE_FILTER_TYPE__STATUS_COLUMN:
                unsetStatusColumn();
                return;
            case XmlPackage.SQL_MESSAGE_FILTER_TYPE__TABLENAME:
                setTablename(TABLENAME_EDEFAULT);
                return;
            case XmlPackage.SQL_MESSAGE_FILTER_TYPE__WHERE_CONDITION:
                unsetWhereCondition();
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
            case XmlPackage.SQL_MESSAGE_FILTER_TYPE__ERROR_DELETE:
                return isSetErrorDelete();
            case XmlPackage.SQL_MESSAGE_FILTER_TYPE__INSERT_TIMESTAMP_COLUMN:
                return isSetInsertTimestampColumn();
            case XmlPackage.SQL_MESSAGE_FILTER_TYPE__MESSAGE_COLUMN:
                return isSetMessageColumn();
            case XmlPackage.SQL_MESSAGE_FILTER_TYPE__MESSAGE_ID_COLUMN:
                return isSetMessageIdColumn();
            case XmlPackage.SQL_MESSAGE_FILTER_TYPE__ORDER_BY:
                return isSetOrderBy();
            case XmlPackage.SQL_MESSAGE_FILTER_TYPE__POST_DELETE:
                return isSetPostDelete();
            case XmlPackage.SQL_MESSAGE_FILTER_TYPE__STATUS_COLUMN:
                return isSetStatusColumn();
            case XmlPackage.SQL_MESSAGE_FILTER_TYPE__TABLENAME:
                return TABLENAME_EDEFAULT == null ? tablename != null : !TABLENAME_EDEFAULT.equals(tablename);
            case XmlPackage.SQL_MESSAGE_FILTER_TYPE__WHERE_CONDITION:
                return isSetWhereCondition();
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
        result.append(" (errorDelete: ");
        if (errorDeleteESet) result.append(errorDelete); else result.append("<unset>");
        result.append(", insertTimestampColumn: ");
        if (insertTimestampColumnESet) result.append(insertTimestampColumn); else result.append("<unset>");
        result.append(", messageColumn: ");
        if (messageColumnESet) result.append(messageColumn); else result.append("<unset>");
        result.append(", messageIdColumn: ");
        if (messageIdColumnESet) result.append(messageIdColumn); else result.append("<unset>");
        result.append(", orderBy: ");
        if (orderByESet) result.append(orderBy); else result.append("<unset>");
        result.append(", postDelete: ");
        if (postDeleteESet) result.append(postDelete); else result.append("<unset>");
        result.append(", statusColumn: ");
        if (statusColumnESet) result.append(statusColumn); else result.append("<unset>");
        result.append(", tablename: ");
        result.append(tablename);
        result.append(", whereCondition: ");
        if (whereConditionESet) result.append(whereCondition); else result.append("<unset>");
        result.append(')');
        return result.toString();
    }
}
