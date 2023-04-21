package Trading.impl;

import Trading.Strategy;
import Trading.TradingPackage;
import java.util.Date;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Strategy</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link Trading.impl.StrategyImpl#getStrategyId <em>Strategy Id</em>}</li>
 *   <li>{@link Trading.impl.StrategyImpl#getComment <em>Comment</em>}</li>
 *   <li>{@link Trading.impl.StrategyImpl#getMode <em>Mode</em>}</li>
 *   <li>{@link Trading.impl.StrategyImpl#getCreationDate <em>Creation Date</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class StrategyImpl extends EObjectImpl implements Strategy {

    /**
	 * The default value of the '{@link #getStrategyId() <em>Strategy Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStrategyId()
	 * @generated
	 * @ordered
	 */
    protected static final long STRATEGY_ID_EDEFAULT = 0L;

    /**
	 * The cached value of the '{@link #getStrategyId() <em>Strategy Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStrategyId()
	 * @generated
	 * @ordered
	 */
    protected long strategyId = STRATEGY_ID_EDEFAULT;

    /**
	 * The default value of the '{@link #getComment() <em>Comment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getComment()
	 * @generated
	 * @ordered
	 */
    protected static final String COMMENT_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getComment() <em>Comment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getComment()
	 * @generated
	 * @ordered
	 */
    protected String comment = COMMENT_EDEFAULT;

    /**
	 * The default value of the '{@link #getMode() <em>Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMode()
	 * @generated
	 * @ordered
	 */
    protected static final String MODE_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getMode() <em>Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMode()
	 * @generated
	 * @ordered
	 */
    protected String mode = MODE_EDEFAULT;

    /**
	 * The default value of the '{@link #getCreationDate() <em>Creation Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCreationDate()
	 * @generated
	 * @ordered
	 */
    protected static final Date CREATION_DATE_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getCreationDate() <em>Creation Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCreationDate()
	 * @generated
	 * @ordered
	 */
    protected Date creationDate = CREATION_DATE_EDEFAULT;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected StrategyImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return TradingPackage.Literals.STRATEGY;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public long getStrategyId() {
        return strategyId;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setStrategyId(long newStrategyId) {
        long oldStrategyId = strategyId;
        strategyId = newStrategyId;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, TradingPackage.STRATEGY__STRATEGY_ID, oldStrategyId, strategyId));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getComment() {
        return comment;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setComment(String newComment) {
        String oldComment = comment;
        comment = newComment;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, TradingPackage.STRATEGY__COMMENT, oldComment, comment));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getMode() {
        return mode;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setMode(String newMode) {
        String oldMode = mode;
        mode = newMode;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, TradingPackage.STRATEGY__MODE, oldMode, mode));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setCreationDate(Date newCreationDate) {
        Date oldCreationDate = creationDate;
        creationDate = newCreationDate;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, TradingPackage.STRATEGY__CREATION_DATE, oldCreationDate, creationDate));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case TradingPackage.STRATEGY__STRATEGY_ID:
                return getStrategyId();
            case TradingPackage.STRATEGY__COMMENT:
                return getComment();
            case TradingPackage.STRATEGY__MODE:
                return getMode();
            case TradingPackage.STRATEGY__CREATION_DATE:
                return getCreationDate();
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
            case TradingPackage.STRATEGY__STRATEGY_ID:
                setStrategyId((Long) newValue);
                return;
            case TradingPackage.STRATEGY__COMMENT:
                setComment((String) newValue);
                return;
            case TradingPackage.STRATEGY__MODE:
                setMode((String) newValue);
                return;
            case TradingPackage.STRATEGY__CREATION_DATE:
                setCreationDate((Date) newValue);
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
            case TradingPackage.STRATEGY__STRATEGY_ID:
                setStrategyId(STRATEGY_ID_EDEFAULT);
                return;
            case TradingPackage.STRATEGY__COMMENT:
                setComment(COMMENT_EDEFAULT);
                return;
            case TradingPackage.STRATEGY__MODE:
                setMode(MODE_EDEFAULT);
                return;
            case TradingPackage.STRATEGY__CREATION_DATE:
                setCreationDate(CREATION_DATE_EDEFAULT);
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
            case TradingPackage.STRATEGY__STRATEGY_ID:
                return strategyId != STRATEGY_ID_EDEFAULT;
            case TradingPackage.STRATEGY__COMMENT:
                return COMMENT_EDEFAULT == null ? comment != null : !COMMENT_EDEFAULT.equals(comment);
            case TradingPackage.STRATEGY__MODE:
                return MODE_EDEFAULT == null ? mode != null : !MODE_EDEFAULT.equals(mode);
            case TradingPackage.STRATEGY__CREATION_DATE:
                return CREATION_DATE_EDEFAULT == null ? creationDate != null : !CREATION_DATE_EDEFAULT.equals(creationDate);
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
        result.append(" (strategyId: ");
        result.append(strategyId);
        result.append(", comment: ");
        result.append(comment);
        result.append(", mode: ");
        result.append(mode);
        result.append(", creationDate: ");
        result.append(creationDate);
        result.append(')');
        return result.toString();
    }
}
