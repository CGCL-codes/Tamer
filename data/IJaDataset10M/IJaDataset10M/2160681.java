package com.patientis.model.order;

import com.patientis.framework.logging.Log;
import java.beans.PropertyChangeListener;
import com.patientis.ejb.reference.IGetReference;
import static com.patientis.model.common.ModelReference.*;
import com.patientis.framework.controls.exceptions.ISValidateControlException;
import com.patientis.model.common.*;
import com.patientis.model.reference.*;

/**
 * OrderState
 * 
 */
public class OrderStateDataModel extends BaseModel {

    private static final long serialVersionUID = 1;

    /**
	 * Unique identifier
	 */
    private Long id;

    /**
	 * Active indicator 1=Active 0=Inactive
	 */
    private int activeInd = 1;

    /**
	 * Color to override the default order color.  RGB value
	 */
    private int backgroundColor = 0;

    /**
	 * Override default border color
	 */
    private int borderColor = 0;

    /**
	 * Deleted indicator 1=Deleted 0=Not deleted
	 */
    private int deletedInd = 0;

    /**
	 * Override default foreground color
	 */
    private int foregroundColor = 0;

    /**
	 * Date the record was created
	 */
    private DateTimeModel insertDt = DateTimeModel.getNow();

    /**
	 * User that created this record
	 */
    private DisplayModel insertUserRef = new DisplayModel(this);

    /**
	 * Icon used for this order type.  Foreign key to refs on Icon
	 */
    private DisplayModel orderIconRef = new DisplayModel(this);

    /**
	 * Order state.  Foreign key to refs on OrderState
	 */
    private DisplayModel orderStateRef = new DisplayModel(this);

    /**
	 * Order status this state is in.  Foreign key to refs on OrderStatus
	 */
    private DisplayModel orderStatusRef = new DisplayModel(this);

    /**
	 * Sequential progression of this state
	 */
    private int stateSequence = 0;

    /**
	 * Identifies the system which originally created this row if reference
	 */
    private DisplayModel systemRef = new DisplayModel(this, 100100);

    /**
	 * 0: record or user data >0 unique identifier for reference data
	 */
    private Long systemSequence = 0L;

    /**
	 * Transaction number assigned by the system
	 */
    private Long systemTransactionSeq = 0L;

    /**
	 * Date the record was updated
	 */
    private DateTimeModel updateDt = DateTimeModel.getNow();

    /**
	 * Date the record was last updated
	 */
    private DisplayModel updateUserRef = new DisplayModel(this);

    /**
	 * Number of times this record has been updated
	 */
    private int version = 0;

    /**
	 * Get primary identifier
	 */
    public Long getId() {
        if (this.id == null) {
            return 0L;
        } else {
            return this.id;
        }
    }

    /**
	 * Set primary identifier
	 */
    public void setId(Long id) {
        if (Converter.isDifferent(getId(), id)) {
            Long oldid = getId();
            this.id = id;
            firePropertyChange(String.valueOf(ORDERSTATES_ORDERSTATEID), oldid, id);
        }
    }

    /**
	 * Active indicator 1=Active 0=Inactive
	 */
    public int getActiveInd() {
        return this.activeInd;
    }

    /**
	 * Active indicator 1=Active 0=Inactive
	 */
    public boolean isActive() {
        return getActiveInd() == 1;
    }

    /**
	 * Active indicator 1=Active 0=Inactive
	 */
    public boolean isNotActive() {
        return getActiveInd() == 0;
    }

    /**
	 * Active indicator 1=Active 0=Inactive
	 */
    public void setActive() {
        setActiveInd(1);
    }

    /**
	 * Active indicator 1=Active 0=Inactive
	 */
    public void setNotActive() {
        setActiveInd(0);
    }

    /**
	 * Active indicator 1=Active 0=Inactive
	 */
    public void setActiveInd(int activeInd) {
        if (!(this.activeInd == activeInd)) {
            int oldactiveInd = 1;
            oldactiveInd = this.activeInd;
            this.activeInd = activeInd;
            setModified("activeInd");
            firePropertyChange(String.valueOf(ORDERSTATES_ACTIVEIND), oldactiveInd, activeInd);
            if (activeInd == 1) {
                setNotDeleted();
            }
        }
    }

    /**
	 * Color to override the default order color.  RGB value
	 */
    public int getBackgroundColor() {
        return this.backgroundColor;
    }

    /**
	 * Color to override the default order color.  RGB value
	 */
    public void setBackgroundColor(int backgroundColor) {
        if (!(this.backgroundColor == backgroundColor)) {
            int oldbackgroundColor = 0;
            oldbackgroundColor = this.backgroundColor;
            this.backgroundColor = backgroundColor;
            setModified("backgroundColor");
            firePropertyChange(String.valueOf(ORDERSTATES_BACKGROUNDCOLOR), oldbackgroundColor, backgroundColor);
        }
    }

    /**
	 * Override default border color
	 */
    public int getBorderColor() {
        return this.borderColor;
    }

    /**
	 * Override default border color
	 */
    public void setBorderColor(int borderColor) {
        if (!(this.borderColor == borderColor)) {
            int oldborderColor = 0;
            oldborderColor = this.borderColor;
            this.borderColor = borderColor;
            setModified("borderColor");
            firePropertyChange(String.valueOf(ORDERSTATES_BORDERCOLOR), oldborderColor, borderColor);
        }
    }

    /**
	 * Deleted indicator 1=Deleted 0=Not deleted
	 */
    public int getDeletedInd() {
        return this.deletedInd;
    }

    /**
	 * Deleted indicator 1=Deleted 0=Not deleted
	 */
    public boolean isDeleted() {
        return getDeletedInd() == 1;
    }

    /**
	 * Deleted indicator 1=Deleted 0=Not deleted
	 */
    public boolean isNotDeleted() {
        return getDeletedInd() == 0;
    }

    /**
	 * Deleted indicator 1=Deleted 0=Not deleted
	 */
    public void setDeleted() {
        setDeletedInd(1);
    }

    /**
	 * Deleted indicator 1=Deleted 0=Not deleted
	 */
    public void setNotDeleted() {
        setDeletedInd(0);
    }

    /**
	 * Deleted indicator 1=Deleted 0=Not deleted
	 */
    public void setDeletedInd(int deletedInd) {
        if (!(this.deletedInd == deletedInd)) {
            int olddeletedInd = 0;
            olddeletedInd = this.deletedInd;
            this.deletedInd = deletedInd;
            setModified("deletedInd");
            firePropertyChange(String.valueOf(ORDERSTATES_DELETEDIND), olddeletedInd, deletedInd);
            if (deletedInd == 1) {
                setNotActive();
            }
        }
    }

    /**
	 * Override default foreground color
	 */
    public int getForegroundColor() {
        return this.foregroundColor;
    }

    /**
	 * Override default foreground color
	 */
    public void setForegroundColor(int foregroundColor) {
        if (!(this.foregroundColor == foregroundColor)) {
            int oldforegroundColor = 0;
            oldforegroundColor = this.foregroundColor;
            this.foregroundColor = foregroundColor;
            setModified("foregroundColor");
            firePropertyChange(String.valueOf(ORDERSTATES_FOREGROUNDCOLOR), oldforegroundColor, foregroundColor);
        }
    }

    /**
	 * Date the record was created
	 */
    public DateTimeModel getInsertDt() {
        return this.insertDt;
    }

    /**
	 * Date the record was created
	 */
    public void setInsertDt(DateTimeModel insertDt) {
        if (Converter.isDifferent(this.insertDt, insertDt)) {
            DateTimeModel oldinsertDt = DateTimeModel.getNow();
            oldinsertDt.copyAllFrom(this.insertDt);
            this.insertDt.copyAllFrom(insertDt);
            setModified("insertDt");
            firePropertyChange(String.valueOf(ORDERSTATES_INSERTDT), oldinsertDt, insertDt);
        }
    }

    /**
	 * User that created this record
	 */
    public DisplayModel getInsertUserRef() {
        return this.insertUserRef;
    }

    /**
	 * User that created this record
	 */
    public void setInsertUserRef(DisplayModel insertUserRef) {
        if (Converter.isDifferent(this.insertUserRef, insertUserRef)) {
            DisplayModel oldinsertUserRef = new DisplayModel(this);
            oldinsertUserRef.copyAllFrom(this.insertUserRef);
            this.insertUserRef.copyAllFrom(insertUserRef);
            setModified("insertUserRef");
            firePropertyChange(String.valueOf(ORDERSTATES_INSERTUSERREFID), oldinsertUserRef, insertUserRef);
        }
    }

    /**
	 * Icon used for this order type.  Foreign key to refs on Icon
	 */
    public DisplayModel getOrderIconRef() {
        return this.orderIconRef;
    }

    /**
	 * Icon used for this order type.  Foreign key to refs on Icon
	 */
    public void setOrderIconRef(DisplayModel orderIconRef) {
        if (Converter.isDifferent(this.orderIconRef, orderIconRef)) {
            DisplayModel oldorderIconRef = new DisplayModel(this);
            oldorderIconRef.copyAllFrom(this.orderIconRef);
            this.orderIconRef.copyAllFrom(orderIconRef);
            setModified("orderIconRef");
            firePropertyChange(String.valueOf(ORDERSTATES_ORDERICONREFID), oldorderIconRef, orderIconRef);
        }
    }

    /**
	 * Order state.  Foreign key to refs on OrderState
	 */
    public DisplayModel getOrderStateRef() {
        return this.orderStateRef;
    }

    /**
	 * Order state.  Foreign key to refs on OrderState
	 */
    public void setOrderStateRef(DisplayModel orderStateRef) {
        if (Converter.isDifferent(this.orderStateRef, orderStateRef)) {
            DisplayModel oldorderStateRef = new DisplayModel(this);
            oldorderStateRef.copyAllFrom(this.orderStateRef);
            this.orderStateRef.copyAllFrom(orderStateRef);
            setModified("orderStateRef");
            firePropertyChange(String.valueOf(ORDERSTATES_ORDERSTATEREFID), oldorderStateRef, orderStateRef);
        }
    }

    /**
	 * Order status this state is in.  Foreign key to refs on OrderStatus
	 */
    public DisplayModel getOrderStatusRef() {
        return this.orderStatusRef;
    }

    /**
	 * Order status this state is in.  Foreign key to refs on OrderStatus
	 */
    public void setOrderStatusRef(DisplayModel orderStatusRef) {
        if (Converter.isDifferent(this.orderStatusRef, orderStatusRef)) {
            DisplayModel oldorderStatusRef = new DisplayModel(this);
            oldorderStatusRef.copyAllFrom(this.orderStatusRef);
            this.orderStatusRef.copyAllFrom(orderStatusRef);
            setModified("orderStatusRef");
            firePropertyChange(String.valueOf(ORDERSTATES_ORDERSTATUSREFID), oldorderStatusRef, orderStatusRef);
        }
    }

    /**
	 * Sequential progression of this state
	 */
    public int getStateSequence() {
        return this.stateSequence;
    }

    /**
	 * Sequential progression of this state
	 */
    public void setStateSequence(int stateSequence) {
        if (!(this.stateSequence == stateSequence)) {
            int oldstateSequence = 0;
            oldstateSequence = this.stateSequence;
            this.stateSequence = stateSequence;
            setModified("stateSequence");
            firePropertyChange(String.valueOf(ORDERSTATES_STATESEQUENCE), oldstateSequence, stateSequence);
        }
    }

    /**
	 * Identifies the system which originally created this row if reference
	 */
    public DisplayModel getSystemRef() {
        return this.systemRef;
    }

    /**
	 * Identifies the system which originally created this row if reference
	 */
    public void setSystemRef(DisplayModel systemRef) {
        if (Converter.isDifferent(this.systemRef, systemRef)) {
            DisplayModel oldsystemRef = new DisplayModel(this, 100100);
            oldsystemRef.copyAllFrom(this.systemRef);
            this.systemRef.copyAllFrom(systemRef);
            setModified("systemRef");
            firePropertyChange(String.valueOf(ORDERSTATES_SYSTEMREFID), oldsystemRef, systemRef);
        }
    }

    /**
	 * 0: record or user data >0 unique identifier for reference data
	 */
    public Long getSystemSequence() {
        return this.systemSequence;
    }

    /**
	 * 0: record or user data >0 unique identifier for reference data
	 */
    public void setSystemSequence(Long systemSequence) {
        if (!(this.systemSequence.longValue() == systemSequence.longValue())) {
            Long oldsystemSequence = 0L;
            oldsystemSequence = this.systemSequence.longValue();
            this.systemSequence = systemSequence.longValue();
            setModified("systemSequence");
            firePropertyChange(String.valueOf(ORDERSTATES_SYSTEMSEQUENCE), oldsystemSequence, systemSequence);
        }
    }

    /**
	 * Transaction number assigned by the system
	 */
    public Long getSystemTransactionSeq() {
        return this.systemTransactionSeq;
    }

    /**
	 * Transaction number assigned by the system
	 */
    public void setSystemTransactionSeq(Long systemTransactionSeq) {
        if (!(this.systemTransactionSeq.longValue() == systemTransactionSeq.longValue())) {
            Long oldsystemTransactionSeq = 0L;
            oldsystemTransactionSeq = this.systemTransactionSeq.longValue();
            this.systemTransactionSeq = systemTransactionSeq.longValue();
            setModified("systemTransactionSeq");
            firePropertyChange(String.valueOf(ORDERSTATES_SYSTEMTRANSACTIONSEQ), oldsystemTransactionSeq, systemTransactionSeq);
        }
    }

    /**
	 * Date the record was updated
	 */
    public DateTimeModel getUpdateDt() {
        return this.updateDt;
    }

    /**
	 * Date the record was updated
	 */
    public void setUpdateDt(DateTimeModel updateDt) {
        if (Converter.isDifferent(this.updateDt, updateDt)) {
            DateTimeModel oldupdateDt = DateTimeModel.getNow();
            oldupdateDt.copyAllFrom(this.updateDt);
            this.updateDt.copyAllFrom(updateDt);
            setModified("updateDt");
            firePropertyChange(String.valueOf(ORDERSTATES_UPDATEDT), oldupdateDt, updateDt);
        }
    }

    /**
	 * Date the record was last updated
	 */
    public DisplayModel getUpdateUserRef() {
        return this.updateUserRef;
    }

    /**
	 * Date the record was last updated
	 */
    public void setUpdateUserRef(DisplayModel updateUserRef) {
        if (Converter.isDifferent(this.updateUserRef, updateUserRef)) {
            DisplayModel oldupdateUserRef = new DisplayModel(this);
            oldupdateUserRef.copyAllFrom(this.updateUserRef);
            this.updateUserRef.copyAllFrom(updateUserRef);
            setModified("updateUserRef");
            firePropertyChange(String.valueOf(ORDERSTATES_UPDATEUSERREFID), oldupdateUserRef, updateUserRef);
        }
    }

    /**
	 * Number of times this record has been updated
	 */
    public int getVersion() {
        return this.version;
    }

    /**
	 * Number of times this record has been updated
	 */
    public void setVersion(int version) {
        if (!(this.version == version)) {
            int oldversion = 0;
            oldversion = this.version;
            this.version = version;
            setModified("version");
            firePropertyChange(String.valueOf(ORDERSTATES_VERSION), oldversion, version);
        }
    }

    /**
	 * @see com.patientis.model.common.IBaseModel#clear()
	 */
    public void clear() {
        copyFrom(new OrderStateDataModel(), false);
        setId(0L);
    }

    /**
	 * @see com.patientis.model.common.IBaseModel#setStoreDefault(com.patientis.model.common.DateTimeModel, long)
	 */
    public void setStoreDefault(DateTimeModel now, long storeUserId) {
        if (getInsertDt() == null || getInsertDt().isNull()) {
            setInsertDt(now);
        }
        if (getInsertUserRef() == null || getInsertUserRef().getId() == 0L) {
            setInsertUserRef(new DisplayModel(storeUserId));
        }
        setUpdateDt(now);
        setUpdateUserRef(new DisplayModel(storeUserId));
    }

    /**
	 * @see com.patientis.model.common.IBaseModel#createNewCopy()
	 */
    public OrderStateModel createNewCopy() {
        OrderStateModel newCopy = new OrderStateModel();
        newCopy.shallowCopyFrom(this, false);
        newCopy.resetIds();
        newCopy.setInsertDt(new DateTimeModel());
        newCopy.setInsertUserRef(new DisplayModel());
        newCopy.setUpdateDt(new DateTimeModel());
        newCopy.setUpdateUserRef(new DisplayModel());
        return newCopy;
    }

    /**
	 * @see com.patientis.model.common.IBaseModel#resetIds()
	 */
    public void resetIds() {
        setId(null);
    }

    /**
	 * @see com.patientis.model.common.IBaseModel#getChildren()
	 */
    public java.util.List<IBaseModel> getChildren() {
        java.util.List<IBaseModel> list = new java.util.ArrayList<IBaseModel>();
        return list;
    }

    /**
	 * @see com.patientis.model.common.IBaseModel#getAllChildren()
	 */
    public java.util.List<IBaseModel> getAllChildren() {
        java.util.List<IBaseModel> list = new java.util.ArrayList<IBaseModel>();
        return list;
    }

    /**
	 * @see com.patientis.model.common.IBaseModel#copyFrom(IBaseModel, boolean)
	 */
    public void copyFrom(IBaseModel model, boolean modifiedOnly) {
        if (model == null) {
            return;
        }
        if (!modifiedOnly) {
            setId(model.getId());
        }
        OrderStateDataModel m = (OrderStateDataModel) model;
        if (modifiedOnly && !m.isModified()) {
            return;
        }
        if (!modifiedOnly || m.isModified("activeInd")) setActiveInd(m.getActiveInd());
        if (!modifiedOnly || m.isModified("backgroundColor")) setBackgroundColor(m.getBackgroundColor());
        if (!modifiedOnly || m.isModified("borderColor")) setBorderColor(m.getBorderColor());
        if (!modifiedOnly || m.isModified("deletedInd")) setDeletedInd(m.getDeletedInd());
        if (!modifiedOnly || m.isModified("foregroundColor")) setForegroundColor(m.getForegroundColor());
        if (!modifiedOnly || m.isModified("insertDt")) setInsertDt(m.getInsertDt());
        if (!modifiedOnly || m.isModified("insertUserRef")) setInsertUserRef(m.getInsertUserRef());
        if (!modifiedOnly || m.isModified("orderIconRef")) setOrderIconRef(m.getOrderIconRef());
        if (!modifiedOnly || m.isModified("orderStateRef")) setOrderStateRef(m.getOrderStateRef());
        if (!modifiedOnly || m.isModified("orderStatusRef")) setOrderStatusRef(m.getOrderStatusRef());
        if (!modifiedOnly || m.isModified("stateSequence")) setStateSequence(m.getStateSequence());
        if (!modifiedOnly || m.isModified("systemRef")) setSystemRef(m.getSystemRef());
        if (!modifiedOnly || m.isModified("systemSequence")) setSystemSequence(m.getSystemSequence());
        if (!modifiedOnly || m.isModified("systemTransactionSeq")) setSystemTransactionSeq(m.getSystemTransactionSeq());
        if (!modifiedOnly || m.isModified("updateDt")) setUpdateDt(m.getUpdateDt());
        if (!modifiedOnly || m.isModified("updateUserRef")) setUpdateUserRef(m.getUpdateUserRef());
        if (!modifiedOnly || m.isModified("version")) setVersion(m.getVersion());
    }

    /**
	 * @see com.patientis.model.common.IBaseModel#getChildModels()
	 */
    public java.util.List<IBaseModel> getChildModels() {
        java.util.List<IBaseModel> list = new java.util.ArrayList<IBaseModel>();
        return list;
    }

    /**
	 * @see com.patientis.model.common.IBaseModel#getDefinedChildModels()
	 */
    public java.util.List<IBaseModel> getDefinedChildModels() {
        java.util.List<IBaseModel> childTypes = new java.util.ArrayList<IBaseModel>();
        return childTypes;
    }

    /**
	 * @see com.patientis.model.common.IBaseModel#giveDefinedChildModels()
	 */
    public java.util.List<IBaseModel> giveDefinedChildModels() {
        java.util.List<IBaseModel> childTypes = new java.util.ArrayList<IBaseModel>();
        return childTypes;
    }

    /**
	 * @see com.patientis.model.common.IBaseModel#shallowCopyFrom(IBaseModel, boolean)
	 */
    public void shallowCopyFrom(IBaseModel model, boolean modifiedOnly) {
        if (model == null) {
            return;
        }
        if (!modifiedOnly) {
            setId(model.getId());
        }
        OrderStateDataModel m = (OrderStateDataModel) model;
        if (modifiedOnly && !m.isModified()) {
            return;
        }
        if (!modifiedOnly || m.isModified("activeInd")) setActiveInd(m.getActiveInd());
        if (!modifiedOnly || m.isModified("backgroundColor")) setBackgroundColor(m.getBackgroundColor());
        if (!modifiedOnly || m.isModified("borderColor")) setBorderColor(m.getBorderColor());
        if (!modifiedOnly || m.isModified("deletedInd")) setDeletedInd(m.getDeletedInd());
        if (!modifiedOnly || m.isModified("foregroundColor")) setForegroundColor(m.getForegroundColor());
        if (!modifiedOnly || m.isModified("insertDt")) setInsertDt(m.getInsertDt());
        if (!modifiedOnly || m.isModified("insertUserRef")) setInsertUserRef(m.getInsertUserRef());
        if (!modifiedOnly || m.isModified("orderIconRef")) setOrderIconRef(m.getOrderIconRef());
        if (!modifiedOnly || m.isModified("orderStateRef")) setOrderStateRef(m.getOrderStateRef());
        if (!modifiedOnly || m.isModified("orderStatusRef")) setOrderStatusRef(m.getOrderStatusRef());
        if (!modifiedOnly || m.isModified("stateSequence")) setStateSequence(m.getStateSequence());
        if (!modifiedOnly || m.isModified("systemRef")) setSystemRef(m.getSystemRef());
        if (!modifiedOnly || m.isModified("systemSequence")) setSystemSequence(m.getSystemSequence());
        if (!modifiedOnly || m.isModified("systemTransactionSeq")) setSystemTransactionSeq(m.getSystemTransactionSeq());
        if (!modifiedOnly || m.isModified("updateDt")) setUpdateDt(m.getUpdateDt());
        if (!modifiedOnly || m.isModified("updateUserRef")) setUpdateUserRef(m.getUpdateUserRef());
        if (!modifiedOnly || m.isModified("version")) setVersion(m.getVersion());
    }

    /**
	 * @see com.patientis.model.common.BaseModel#copyAllFrom(com.patientis.model.common.IBaseModel)
	 */
    public void copyAllFrom(IBaseModel model) {
        copyFrom(model, false);
    }

    /**
	 * @see com.patientis.model.common.BaseModel#copyModifiedFrom(com.patientis.model.common.IBaseModel)
	 */
    public void copyModifiedFrom(IBaseModel model) {
        copyFrom(model, true);
    }

    /**
	 * Is model modified
	 */
    public boolean isModified() {
        if (super.isModified()) {
            return true;
        }
        return false;
    }

    /**
	 * @see com.patientis.model.common.IBaseModel#setValue(java.lang.int, java.lang.Object)
	 */
    public void setValue(int modelRefId, Object value) {
        try {
            switch(modelRefId) {
                case ORDERSTATES_ACTIVEIND:
                    setActiveInd(Converter.convertInteger(value));
                    break;
                case ORDERSTATES_BACKGROUNDCOLOR:
                    setBackgroundColor(Converter.convertInteger(value));
                    break;
                case ORDERSTATES_BORDERCOLOR:
                    setBorderColor(Converter.convertInteger(value));
                    break;
                case ORDERSTATES_DELETEDIND:
                    setDeletedInd(Converter.convertInteger(value));
                    break;
                case ORDERSTATES_FOREGROUNDCOLOR:
                    setForegroundColor(Converter.convertInteger(value));
                    break;
                case ORDERSTATES_INSERTDT:
                    setInsertDt(Converter.convertDateTimeModel(value));
                    break;
                case ORDERSTATES_INSERTUSERREFID:
                    setInsertUserRef(Converter.convertDisplayModel(value));
                    break;
                case ORDERSTATES_ORDERICONREFID:
                    setOrderIconRef(Converter.convertDisplayModel(value));
                    break;
                case ORDERSTATES_ORDERSTATEREFID:
                    setOrderStateRef(Converter.convertDisplayModel(value));
                    break;
                case ORDERSTATES_ORDERSTATUSREFID:
                    setOrderStatusRef(Converter.convertDisplayModel(value));
                    break;
                case ORDERSTATES_STATESEQUENCE:
                    setStateSequence(Converter.convertInteger(value));
                    break;
                case ORDERSTATES_SYSTEMREFID:
                    setSystemRef(Converter.convertDisplayModel(value));
                    break;
                case ORDERSTATES_SYSTEMSEQUENCE:
                    setSystemSequence(Converter.convertLong(value));
                    break;
                case ORDERSTATES_SYSTEMTRANSACTIONSEQ:
                    setSystemTransactionSeq(Converter.convertLong(value));
                    break;
                case ORDERSTATES_UPDATEDT:
                    setUpdateDt(Converter.convertDateTimeModel(value));
                    break;
                case ORDERSTATES_UPDATEUSERREFID:
                    setUpdateUserRef(Converter.convertDisplayModel(value));
                    break;
                case ORDERSTATES_VERSION:
                    setVersion(Converter.convertInteger(value));
                    break;
                default:
                    super.setValue(modelRefId, value);
            }
        } catch (Exception ex) {
            Log.exception(ex);
        }
    }

    /**
	 * @see com.patientis.model.common.IBaseModel#getValue(java.lang.int)
	 */
    public Object getValue(int modelRefId) {
        switch(modelRefId) {
            case ORDERSTATES_ACTIVEIND:
                return getActiveInd();
            case ORDERSTATES_INSERTDT:
                return getInsertDt();
            case ORDERSTATES_BORDERCOLOR:
                return getBorderColor();
            case ORDERSTATES_UPDATEUSERREFID:
                return getUpdateUserRef();
            case ORDERSTATES_BACKGROUNDCOLOR:
                return getBackgroundColor();
            case ORDERSTATES_INSERTUSERREFID:
                return getInsertUserRef();
            case ORDERSTATES_ORDERICONREFID:
                return getOrderIconRef();
            case ORDERSTATES_SYSTEMTRANSACTIONSEQ:
                return getSystemTransactionSeq();
            case ORDERSTATES_ORDERSTATEID:
                return getId();
            case ORDERSTATES_ORDERSTATEREFID:
                return getOrderStateRef();
            case ORDERSTATES_ORDERSTATUSREFID:
                return getOrderStatusRef();
            case ORDERSTATES_DELETEDIND:
                return getDeletedInd();
            case ORDERSTATES_STATESEQUENCE:
                return getStateSequence();
            case ORDERSTATES_SYSTEMSEQUENCE:
                return getSystemSequence();
            case ORDERSTATES_SYSTEMREFID:
                return getSystemRef();
            case ORDERSTATES_UPDATEDT:
                return getUpdateDt();
            case ORDERSTATES_VERSION:
                return getVersion();
            case ORDERSTATES_FOREGROUNDCOLOR:
                return getForegroundColor();
            default:
                return super.getValue(modelRefId);
        }
    }

    /**
	 * @see com.patientis.model.common.IBaseModel#hasField(java.lang.int)
	 */
    public boolean hasField(int modelRefId) {
        switch(modelRefId) {
            case ORDERSTATES_ACTIVEIND:
                return true;
            case ORDERSTATES_INSERTDT:
                return true;
            case ORDERSTATES_BORDERCOLOR:
                return true;
            case ORDERSTATES_UPDATEUSERREFID:
                return true;
            case ORDERSTATES_BACKGROUNDCOLOR:
                return true;
            case ORDERSTATES_INSERTUSERREFID:
                return true;
            case ORDERSTATES_ORDERICONREFID:
                return true;
            case ORDERSTATES_SYSTEMTRANSACTIONSEQ:
                return true;
            case ORDERSTATES_ORDERSTATEID:
                return true;
            case ORDERSTATES_ORDERSTATEREFID:
                return true;
            case ORDERSTATES_ORDERSTATUSREFID:
                return true;
            case ORDERSTATES_DELETEDIND:
                return true;
            case ORDERSTATES_STATESEQUENCE:
                return true;
            case ORDERSTATES_SYSTEMSEQUENCE:
                return true;
            case ORDERSTATES_SYSTEMREFID:
                return true;
            case ORDERSTATES_UPDATEDT:
                return true;
            case ORDERSTATES_VERSION:
                return true;
            case ORDERSTATES_FOREGROUNDCOLOR:
                return true;
            default:
                return false;
        }
    }

    /**
	 * @see com.patientis.model.common.IBaseModel#hasModelField(java.lang.int)
	 */
    public static boolean hasModelField(int modelRefId) {
        switch(modelRefId) {
            case ORDERSTATES_ACTIVEIND:
                return true;
            case ORDERSTATES_INSERTDT:
                return true;
            case ORDERSTATES_BORDERCOLOR:
                return true;
            case ORDERSTATES_UPDATEUSERREFID:
                return true;
            case ORDERSTATES_BACKGROUNDCOLOR:
                return true;
            case ORDERSTATES_INSERTUSERREFID:
                return true;
            case ORDERSTATES_ORDERICONREFID:
                return true;
            case ORDERSTATES_SYSTEMTRANSACTIONSEQ:
                return true;
            case ORDERSTATES_ORDERSTATEID:
                return true;
            case ORDERSTATES_ORDERSTATEREFID:
                return true;
            case ORDERSTATES_ORDERSTATUSREFID:
                return true;
            case ORDERSTATES_DELETEDIND:
                return true;
            case ORDERSTATES_STATESEQUENCE:
                return true;
            case ORDERSTATES_SYSTEMSEQUENCE:
                return true;
            case ORDERSTATES_SYSTEMREFID:
                return true;
            case ORDERSTATES_UPDATEDT:
                return true;
            case ORDERSTATES_VERSION:
                return true;
            case ORDERSTATES_FOREGROUNDCOLOR:
                return true;
            default:
                return false;
        }
    }

    /**
	 * @see com.patientis.model.common.IBaseModel#getField(java.lang.int)
	 */
    public ModelField getField(int modelRefId) {
        try {
            switch(modelRefId) {
                case ORDERSTATES_ACTIVEIND:
                    return new ModelField("active_ind", "activeInd", ORDERSTATES_ACTIVEIND, Integer.class);
                case ORDERSTATES_INSERTDT:
                    return new ModelField("insert_dt", "insertDt", ORDERSTATES_INSERTDT, DateTimeModel.class);
                case ORDERSTATES_BORDERCOLOR:
                    return new ModelField("border_color", "borderColor", ORDERSTATES_BORDERCOLOR, Integer.class);
                case ORDERSTATES_UPDATEUSERREFID:
                    return new ModelField("update_user_ref_id", "updateUserRef", ORDERSTATES_UPDATEUSERREFID, DisplayModel.class, UserReference.groupName());
                case ORDERSTATES_BACKGROUNDCOLOR:
                    return new ModelField("background_color", "backgroundColor", ORDERSTATES_BACKGROUNDCOLOR, Integer.class);
                case ORDERSTATES_INSERTUSERREFID:
                    return new ModelField("insert_user_ref_id", "insertUserRef", ORDERSTATES_INSERTUSERREFID, DisplayModel.class, UserReference.groupName());
                case ORDERSTATES_ORDERICONREFID:
                    return new ModelField("order_icon_ref_id", "orderIconRef", ORDERSTATES_ORDERICONREFID, DisplayModel.class, IconReference.groupName());
                case ORDERSTATES_SYSTEMTRANSACTIONSEQ:
                    return new ModelField("system_transaction_seq", "systemTransactionSeq", ORDERSTATES_SYSTEMTRANSACTIONSEQ, Long.class);
                case ORDERSTATES_ORDERSTATEID:
                    return new ModelField("order_state_id", "orderStateId", ORDERSTATES_ORDERSTATEID, Long.class);
                case ORDERSTATES_ORDERSTATEREFID:
                    return new ModelField("order_state_ref_id", "orderStateRef", ORDERSTATES_ORDERSTATEREFID, DisplayModel.class, OrderStateReference.groupName());
                case ORDERSTATES_ORDERSTATUSREFID:
                    return new ModelField("order_status_ref_id", "orderStatusRef", ORDERSTATES_ORDERSTATUSREFID, DisplayModel.class, OrderStatusReference.groupName());
                case ORDERSTATES_DELETEDIND:
                    return new ModelField("deleted_ind", "deletedInd", ORDERSTATES_DELETEDIND, Integer.class);
                case ORDERSTATES_STATESEQUENCE:
                    return new ModelField("state_sequence", "stateSequence", ORDERSTATES_STATESEQUENCE, Integer.class);
                case ORDERSTATES_SYSTEMSEQUENCE:
                    return new ModelField("system_sequence", "systemSequence", ORDERSTATES_SYSTEMSEQUENCE, Long.class);
                case ORDERSTATES_SYSTEMREFID:
                    return new ModelField("system_ref_id", "systemRef", ORDERSTATES_SYSTEMREFID, DisplayModel.class, SystemReference.groupName());
                case ORDERSTATES_UPDATEDT:
                    return new ModelField("update_dt", "updateDt", ORDERSTATES_UPDATEDT, DateTimeModel.class);
                case ORDERSTATES_VERSION:
                    return new ModelField("version", "version", ORDERSTATES_VERSION, Integer.class);
                case ORDERSTATES_FOREGROUNDCOLOR:
                    return new ModelField("foreground_color", "foregroundColor", ORDERSTATES_FOREGROUNDCOLOR, Integer.class);
                default:
                    return null;
            }
        } catch (Exception ex) {
            Log.exception(ex);
            return null;
        }
    }

    public static Class getModelClass() {
        return OrderStateModel.class;
    }

    /**
	 * @see com.patientis.model.common.IBaseModel#getModelField(java.lang.int)
	 */
    public static ModelField getModelField(int modelRefId) {
        try {
            switch(modelRefId) {
                case ORDERSTATES_ACTIVEIND:
                    return new ModelField("active_ind", "activeInd", ORDERSTATES_ACTIVEIND, Integer.class);
                case ORDERSTATES_INSERTDT:
                    return new ModelField("insert_dt", "insertDt", ORDERSTATES_INSERTDT, DateTimeModel.class);
                case ORDERSTATES_BORDERCOLOR:
                    return new ModelField("border_color", "borderColor", ORDERSTATES_BORDERCOLOR, Integer.class);
                case ORDERSTATES_UPDATEUSERREFID:
                    return new ModelField("update_user_ref_id", "updateUserRef", ORDERSTATES_UPDATEUSERREFID, DisplayModel.class, UserReference.groupName());
                case ORDERSTATES_BACKGROUNDCOLOR:
                    return new ModelField("background_color", "backgroundColor", ORDERSTATES_BACKGROUNDCOLOR, Integer.class);
                case ORDERSTATES_INSERTUSERREFID:
                    return new ModelField("insert_user_ref_id", "insertUserRef", ORDERSTATES_INSERTUSERREFID, DisplayModel.class, UserReference.groupName());
                case ORDERSTATES_ORDERICONREFID:
                    return new ModelField("order_icon_ref_id", "orderIconRef", ORDERSTATES_ORDERICONREFID, DisplayModel.class, IconReference.groupName());
                case ORDERSTATES_SYSTEMTRANSACTIONSEQ:
                    return new ModelField("system_transaction_seq", "systemTransactionSeq", ORDERSTATES_SYSTEMTRANSACTIONSEQ, Long.class);
                case ORDERSTATES_ORDERSTATEID:
                    return new ModelField("order_state_id", "orderStateId", ORDERSTATES_ORDERSTATEID, Long.class);
                case ORDERSTATES_ORDERSTATEREFID:
                    return new ModelField("order_state_ref_id", "orderStateRef", ORDERSTATES_ORDERSTATEREFID, DisplayModel.class, OrderStateReference.groupName());
                case ORDERSTATES_ORDERSTATUSREFID:
                    return new ModelField("order_status_ref_id", "orderStatusRef", ORDERSTATES_ORDERSTATUSREFID, DisplayModel.class, OrderStatusReference.groupName());
                case ORDERSTATES_DELETEDIND:
                    return new ModelField("deleted_ind", "deletedInd", ORDERSTATES_DELETEDIND, Integer.class);
                case ORDERSTATES_STATESEQUENCE:
                    return new ModelField("state_sequence", "stateSequence", ORDERSTATES_STATESEQUENCE, Integer.class);
                case ORDERSTATES_SYSTEMSEQUENCE:
                    return new ModelField("system_sequence", "systemSequence", ORDERSTATES_SYSTEMSEQUENCE, Long.class);
                case ORDERSTATES_SYSTEMREFID:
                    return new ModelField("system_ref_id", "systemRef", ORDERSTATES_SYSTEMREFID, DisplayModel.class, SystemReference.groupName());
                case ORDERSTATES_UPDATEDT:
                    return new ModelField("update_dt", "updateDt", ORDERSTATES_UPDATEDT, DateTimeModel.class);
                case ORDERSTATES_VERSION:
                    return new ModelField("version", "version", ORDERSTATES_VERSION, Integer.class);
                case ORDERSTATES_FOREGROUNDCOLOR:
                    return new ModelField("foreground_color", "foregroundColor", ORDERSTATES_FOREGROUNDCOLOR, Integer.class);
                default:
                    return null;
            }
        } catch (Exception ex) {
            Log.exception(ex);
            return null;
        }
    }

    /**
	 * @see com.patientis.model.common.IBaseModel#getFields()
	 */
    public java.util.List<ModelField> getFields() {
        java.util.List<ModelField> fields = new java.util.ArrayList<ModelField>();
        fields.add(new ModelField("active_ind", "activeInd", ORDERSTATES_ACTIVEIND, Integer.class));
        fields.add(new ModelField("insert_dt", "insertDt", ORDERSTATES_INSERTDT, DateTimeModel.class));
        fields.add(new ModelField("border_color", "borderColor", ORDERSTATES_BORDERCOLOR, Integer.class));
        fields.add(new ModelField("update_user_ref_id", "updateUserRef", ORDERSTATES_UPDATEUSERREFID, DisplayModel.class, UserReference.groupName()));
        fields.add(new ModelField("background_color", "backgroundColor", ORDERSTATES_BACKGROUNDCOLOR, Integer.class));
        fields.add(new ModelField("insert_user_ref_id", "insertUserRef", ORDERSTATES_INSERTUSERREFID, DisplayModel.class, UserReference.groupName()));
        fields.add(new ModelField("order_icon_ref_id", "orderIconRef", ORDERSTATES_ORDERICONREFID, DisplayModel.class, IconReference.groupName()));
        fields.add(new ModelField("system_transaction_seq", "systemTransactionSeq", ORDERSTATES_SYSTEMTRANSACTIONSEQ, Long.class));
        fields.add(new ModelField("order_state_id", "orderStateId", ORDERSTATES_ORDERSTATEID, Long.class));
        fields.add(new ModelField("order_state_ref_id", "orderStateRef", ORDERSTATES_ORDERSTATEREFID, DisplayModel.class, OrderStateReference.groupName()));
        fields.add(new ModelField("order_status_ref_id", "orderStatusRef", ORDERSTATES_ORDERSTATUSREFID, DisplayModel.class, OrderStatusReference.groupName()));
        fields.add(new ModelField("deleted_ind", "deletedInd", ORDERSTATES_DELETEDIND, Integer.class));
        fields.add(new ModelField("state_sequence", "stateSequence", ORDERSTATES_STATESEQUENCE, Integer.class));
        fields.add(new ModelField("system_sequence", "systemSequence", ORDERSTATES_SYSTEMSEQUENCE, Long.class));
        fields.add(new ModelField("system_ref_id", "systemRef", ORDERSTATES_SYSTEMREFID, DisplayModel.class, SystemReference.groupName()));
        fields.add(new ModelField("update_dt", "updateDt", ORDERSTATES_UPDATEDT, DateTimeModel.class));
        fields.add(new ModelField("version", "version", ORDERSTATES_VERSION, Integer.class));
        fields.add(new ModelField("foreground_color", "foregroundColor", ORDERSTATES_FOREGROUNDCOLOR, Integer.class));
        return fields;
    }

    /**
	 * @see com.patientis.model.common.IBaseModel#getModelFields()
	 */
    public static java.util.List<ModelField> getModelFields() {
        java.util.List<ModelField> fields = new java.util.ArrayList<ModelField>();
        fields.add(new ModelField("active_ind", "activeInd", ORDERSTATES_ACTIVEIND, Integer.class));
        fields.add(new ModelField("insert_dt", "insertDt", ORDERSTATES_INSERTDT, DateTimeModel.class));
        fields.add(new ModelField("border_color", "borderColor", ORDERSTATES_BORDERCOLOR, Integer.class));
        fields.add(new ModelField("update_user_ref_id", "updateUserRef", ORDERSTATES_UPDATEUSERREFID, DisplayModel.class, UserReference.groupName()));
        fields.add(new ModelField("background_color", "backgroundColor", ORDERSTATES_BACKGROUNDCOLOR, Integer.class));
        fields.add(new ModelField("insert_user_ref_id", "insertUserRef", ORDERSTATES_INSERTUSERREFID, DisplayModel.class, UserReference.groupName()));
        fields.add(new ModelField("order_icon_ref_id", "orderIconRef", ORDERSTATES_ORDERICONREFID, DisplayModel.class, IconReference.groupName()));
        fields.add(new ModelField("system_transaction_seq", "systemTransactionSeq", ORDERSTATES_SYSTEMTRANSACTIONSEQ, Long.class));
        fields.add(new ModelField("order_state_id", "orderStateId", ORDERSTATES_ORDERSTATEID, Long.class));
        fields.add(new ModelField("order_state_ref_id", "orderStateRef", ORDERSTATES_ORDERSTATEREFID, DisplayModel.class, OrderStateReference.groupName()));
        fields.add(new ModelField("order_status_ref_id", "orderStatusRef", ORDERSTATES_ORDERSTATUSREFID, DisplayModel.class, OrderStatusReference.groupName()));
        fields.add(new ModelField("deleted_ind", "deletedInd", ORDERSTATES_DELETEDIND, Integer.class));
        fields.add(new ModelField("state_sequence", "stateSequence", ORDERSTATES_STATESEQUENCE, Integer.class));
        fields.add(new ModelField("system_sequence", "systemSequence", ORDERSTATES_SYSTEMSEQUENCE, Long.class));
        fields.add(new ModelField("system_ref_id", "systemRef", ORDERSTATES_SYSTEMREFID, DisplayModel.class, SystemReference.groupName()));
        fields.add(new ModelField("update_dt", "updateDt", ORDERSTATES_UPDATEDT, DateTimeModel.class));
        fields.add(new ModelField("version", "version", ORDERSTATES_VERSION, Integer.class));
        fields.add(new ModelField("foreground_color", "foregroundColor", ORDERSTATES_FOREGROUNDCOLOR, Integer.class));
        return fields;
    }

    /**
	 * @see com.patientis.model.common.BaseModel#firePropertyChange()
	 */
    public void firePropertyChange(int modelRefId, Object oldvalue, Object value) {
        firePropertyChange(String.valueOf(modelRefId), oldvalue, value);
    }

    /**
	 * @see com.patientis.model.common.IBaseModel#setReference(IGetReference)
	 */
    public void setReference(IGetReference reference) {
        if (getInsertUserRef().getId() != 0) getInsertUserRef().setDisplay(reference.getDisplayValuePrimitive(getInsertUserRef().getId()), reference.getShortDisplayValuePrimitive(getInsertUserRef().getId()), 0L);
        if (getOrderIconRef().getId() != 0) getOrderIconRef().setDisplay(reference.getDisplayValuePrimitive(getOrderIconRef().getId()), reference.getShortDisplayValuePrimitive(getOrderIconRef().getId()), 0L);
        if (getOrderStateRef().getId() != 0) getOrderStateRef().setDisplay(reference.getDisplayValuePrimitive(getOrderStateRef().getId()), reference.getShortDisplayValuePrimitive(getOrderStateRef().getId()), 0L);
        if (getOrderStatusRef().getId() != 0) getOrderStatusRef().setDisplay(reference.getDisplayValuePrimitive(getOrderStatusRef().getId()), reference.getShortDisplayValuePrimitive(getOrderStatusRef().getId()), 0L);
        if (getSystemRef().getId() != 0) getSystemRef().setDisplay(reference.getDisplayValuePrimitive(getSystemRef().getId()), reference.getShortDisplayValuePrimitive(getSystemRef().getId()), 0L);
        if (getUpdateUserRef().getId() != 0) getUpdateUserRef().setDisplay(reference.getDisplayValuePrimitive(getUpdateUserRef().getId()), reference.getShortDisplayValuePrimitive(getUpdateUserRef().getId()), 0L);
    }

    /**
	 * @see com.patientis.model.common.IBaseModel#setIndexFields(IIndexFields)
	 */
    public void setIndexFields(IIndexFields indexFields) {
    }

    /**
	 * @see com.patientis.model.common.IBaseModel#setTimeZoneOffset()
	 */
    public void setTimeZoneOffset() {
    }

    /**
	 * @see com.patientis.model.common.IBaseModel#setValue(java.lang.int, RefModel, java.lang.Object)
	 */
    public void setValue(int modelRefId, RefModel sourceRef, Object o) {
        Log.warn(sourceRef.getReferenceGroup() + " not found");
    }

    /**
	 * @see com.patientis.model.common.IBaseModel#getValue(java.lang.int, RefModel, java.lang.Object)
	 */
    public Object getValue(int modelRefId, RefModel sourceRef) {
        return null;
    }

    /**
	 * @see com.patientis.model.common.IBaseModel#addPropertyChangeListener()
	 */
    public final synchronized void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        super.addPropertyChangeListener(propertyName, listener);
    }

    /**
	 * @see com.patientis.model.common.IBaseModel#validateDataModel()
	 */
    public void validateDataModel() throws ISValidateControlException {
        super.validateDataModel();
    }

    /**
	 * @see com.patientis.model.common.IBaseModel#isValid()
	 */
    public boolean isValid() {
        return true;
    }
}
