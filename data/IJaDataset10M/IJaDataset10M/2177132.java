package com.patientis.model.security;

import com.patientis.framework.logging.Log;
import java.beans.PropertyChangeListener;
import com.patientis.ejb.reference.IGetReference;
import static com.patientis.model.common.ModelReference.*;
import com.patientis.framework.controls.exceptions.ISValidateControlException;
import com.patientis.model.common.*;
import com.patientis.model.reference.*;

/**
 * UserState
 * 
 */
public class UserStateDataModel extends BaseModel {

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
	 * Current location of user.  Foreign key to refs on Location
	 */
    private DisplayModel currentLocationRef = new DisplayModel(this);

    /**
	 * Users currently selected role.  Foreign key to refs on Role
	 */
    private DisplayModel currentRoleRef = new DisplayModel(this);

    /**
	 * Deleted indicator 1=Deleted 0=Not deleted
	 */
    private int deletedInd = 0;

    /**
	 * Date the record was created
	 */
    private DateTimeModel insertDt = DateTimeModel.getNow();

    /**
	 * User that created this record
	 */
    private DisplayModel insertUserRef = new DisplayModel(this);

    /**
	 * 
	 */
    private int onlineInd = 0;

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
	 * 
	 */
    private Long userId = 0L;

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
            firePropertyChange(String.valueOf(USERSTATES_USERSTATEID), oldid, id);
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
            firePropertyChange(String.valueOf(USERSTATES_ACTIVEIND), oldactiveInd, activeInd);
            if (activeInd == 1) {
                setNotDeleted();
            }
        }
    }

    /**
	 * Current location of user.  Foreign key to refs on Location
	 */
    public DisplayModel getCurrentLocationRef() {
        return this.currentLocationRef;
    }

    /**
	 * Current location of user.  Foreign key to refs on Location
	 */
    public void setCurrentLocationRef(DisplayModel currentLocationRef) {
        if (Converter.isDifferent(this.currentLocationRef, currentLocationRef)) {
            DisplayModel oldcurrentLocationRef = new DisplayModel(this);
            oldcurrentLocationRef.copyAllFrom(this.currentLocationRef);
            this.currentLocationRef.copyAllFrom(currentLocationRef);
            setModified("currentLocationRef");
            firePropertyChange(String.valueOf(USERSTATES_CURRENTLOCATIONREFID), oldcurrentLocationRef, currentLocationRef);
        }
    }

    /**
	 * Users currently selected role.  Foreign key to refs on Role
	 */
    public DisplayModel getCurrentRoleRef() {
        return this.currentRoleRef;
    }

    /**
	 * Users currently selected role.  Foreign key to refs on Role
	 */
    public void setCurrentRoleRef(DisplayModel currentRoleRef) {
        if (Converter.isDifferent(this.currentRoleRef, currentRoleRef)) {
            DisplayModel oldcurrentRoleRef = new DisplayModel(this);
            oldcurrentRoleRef.copyAllFrom(this.currentRoleRef);
            this.currentRoleRef.copyAllFrom(currentRoleRef);
            setModified("currentRoleRef");
            firePropertyChange(String.valueOf(USERSTATES_CURRENTROLEREFID), oldcurrentRoleRef, currentRoleRef);
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
            firePropertyChange(String.valueOf(USERSTATES_DELETEDIND), olddeletedInd, deletedInd);
            if (deletedInd == 1) {
                setNotActive();
            }
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
            firePropertyChange(String.valueOf(USERSTATES_INSERTDT), oldinsertDt, insertDt);
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
            firePropertyChange(String.valueOf(USERSTATES_INSERTUSERREFID), oldinsertUserRef, insertUserRef);
        }
    }

    /**
	 * 
	 */
    public int getOnlineInd() {
        return this.onlineInd;
    }

    /**
	 * 
	 */
    public boolean isOnline() {
        return getOnlineInd() == 1;
    }

    /**
	 * 
	 */
    public boolean isNotOnline() {
        return getOnlineInd() == 0;
    }

    /**
	 * 
	 */
    public void setOnline() {
        setOnlineInd(1);
    }

    /**
	 * 
	 */
    public void setNotOnline() {
        setOnlineInd(0);
    }

    /**
	 * 
	 */
    public void setOnlineInd(int onlineInd) {
        if (!(this.onlineInd == onlineInd)) {
            int oldonlineInd = 0;
            oldonlineInd = this.onlineInd;
            this.onlineInd = onlineInd;
            setModified("onlineInd");
            firePropertyChange(String.valueOf(USERSTATES_ONLINEIND), oldonlineInd, onlineInd);
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
            firePropertyChange(String.valueOf(USERSTATES_SYSTEMREFID), oldsystemRef, systemRef);
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
            firePropertyChange(String.valueOf(USERSTATES_SYSTEMSEQUENCE), oldsystemSequence, systemSequence);
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
            firePropertyChange(String.valueOf(USERSTATES_SYSTEMTRANSACTIONSEQ), oldsystemTransactionSeq, systemTransactionSeq);
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
            firePropertyChange(String.valueOf(USERSTATES_UPDATEDT), oldupdateDt, updateDt);
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
            firePropertyChange(String.valueOf(USERSTATES_UPDATEUSERREFID), oldupdateUserRef, updateUserRef);
        }
    }

    /**
	 * 
	 */
    public Long getUserId() {
        return this.userId;
    }

    /**
	 * 
	 */
    public void setUserId(Long userId) {
        if (!(this.userId.longValue() == userId.longValue())) {
            Long olduserId = 0L;
            olduserId = this.userId.longValue();
            this.userId = userId.longValue();
            setModified("userId");
            firePropertyChange(String.valueOf(USERSTATES_USERID), olduserId, userId);
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
            firePropertyChange(String.valueOf(USERSTATES_VERSION), oldversion, version);
        }
    }

    /**
	 * @see com.patientis.model.common.IBaseModel#clear()
	 */
    public void clear() {
        copyFrom(new UserStateDataModel(), false);
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
    public UserStateModel createNewCopy() {
        UserStateModel newCopy = new UserStateModel();
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
        UserStateDataModel m = (UserStateDataModel) model;
        if (modifiedOnly && !m.isModified()) {
            return;
        }
        if (!modifiedOnly || m.isModified("activeInd")) setActiveInd(m.getActiveInd());
        if (!modifiedOnly || m.isModified("currentLocationRef")) setCurrentLocationRef(m.getCurrentLocationRef());
        if (!modifiedOnly || m.isModified("currentRoleRef")) setCurrentRoleRef(m.getCurrentRoleRef());
        if (!modifiedOnly || m.isModified("deletedInd")) setDeletedInd(m.getDeletedInd());
        if (!modifiedOnly || m.isModified("insertDt")) setInsertDt(m.getInsertDt());
        if (!modifiedOnly || m.isModified("insertUserRef")) setInsertUserRef(m.getInsertUserRef());
        if (!modifiedOnly || m.isModified("onlineInd")) setOnlineInd(m.getOnlineInd());
        if (!modifiedOnly || m.isModified("systemRef")) setSystemRef(m.getSystemRef());
        if (!modifiedOnly || m.isModified("systemSequence")) setSystemSequence(m.getSystemSequence());
        if (!modifiedOnly || m.isModified("systemTransactionSeq")) setSystemTransactionSeq(m.getSystemTransactionSeq());
        if (!modifiedOnly || m.isModified("updateDt")) setUpdateDt(m.getUpdateDt());
        if (!modifiedOnly || m.isModified("updateUserRef")) setUpdateUserRef(m.getUpdateUserRef());
        if (!modifiedOnly || m.isModified("userId")) setUserId(m.getUserId());
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
        UserStateDataModel m = (UserStateDataModel) model;
        if (modifiedOnly && !m.isModified()) {
            return;
        }
        if (!modifiedOnly || m.isModified("activeInd")) setActiveInd(m.getActiveInd());
        if (!modifiedOnly || m.isModified("currentLocationRef")) setCurrentLocationRef(m.getCurrentLocationRef());
        if (!modifiedOnly || m.isModified("currentRoleRef")) setCurrentRoleRef(m.getCurrentRoleRef());
        if (!modifiedOnly || m.isModified("deletedInd")) setDeletedInd(m.getDeletedInd());
        if (!modifiedOnly || m.isModified("insertDt")) setInsertDt(m.getInsertDt());
        if (!modifiedOnly || m.isModified("insertUserRef")) setInsertUserRef(m.getInsertUserRef());
        if (!modifiedOnly || m.isModified("onlineInd")) setOnlineInd(m.getOnlineInd());
        if (!modifiedOnly || m.isModified("systemRef")) setSystemRef(m.getSystemRef());
        if (!modifiedOnly || m.isModified("systemSequence")) setSystemSequence(m.getSystemSequence());
        if (!modifiedOnly || m.isModified("systemTransactionSeq")) setSystemTransactionSeq(m.getSystemTransactionSeq());
        if (!modifiedOnly || m.isModified("updateDt")) setUpdateDt(m.getUpdateDt());
        if (!modifiedOnly || m.isModified("updateUserRef")) setUpdateUserRef(m.getUpdateUserRef());
        if (!modifiedOnly || m.isModified("userId")) setUserId(m.getUserId());
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
                case USERSTATES_ACTIVEIND:
                    setActiveInd(Converter.convertInteger(value));
                    break;
                case USERSTATES_CURRENTLOCATIONREFID:
                    setCurrentLocationRef(Converter.convertDisplayModel(value));
                    break;
                case USERSTATES_CURRENTROLEREFID:
                    setCurrentRoleRef(Converter.convertDisplayModel(value));
                    break;
                case USERSTATES_DELETEDIND:
                    setDeletedInd(Converter.convertInteger(value));
                    break;
                case USERSTATES_INSERTDT:
                    setInsertDt(Converter.convertDateTimeModel(value));
                    break;
                case USERSTATES_INSERTUSERREFID:
                    setInsertUserRef(Converter.convertDisplayModel(value));
                    break;
                case USERSTATES_ONLINEIND:
                    setOnlineInd(Converter.convertInteger(value));
                    break;
                case USERSTATES_SYSTEMREFID:
                    setSystemRef(Converter.convertDisplayModel(value));
                    break;
                case USERSTATES_SYSTEMSEQUENCE:
                    setSystemSequence(Converter.convertLong(value));
                    break;
                case USERSTATES_SYSTEMTRANSACTIONSEQ:
                    setSystemTransactionSeq(Converter.convertLong(value));
                    break;
                case USERSTATES_UPDATEDT:
                    setUpdateDt(Converter.convertDateTimeModel(value));
                    break;
                case USERSTATES_UPDATEUSERREFID:
                    setUpdateUserRef(Converter.convertDisplayModel(value));
                    break;
                case USERSTATES_USERID:
                    setUserId(Converter.convertLong(value));
                    break;
                case USERSTATES_VERSION:
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
            case USERSTATES_INSERTUSERREFID:
                return getInsertUserRef();
            case USERSTATES_DELETEDIND:
                return getDeletedInd();
            case USERSTATES_USERID:
                return getUserId();
            case USERSTATES_USERSTATEID:
                return getId();
            case USERSTATES_UPDATEUSERREFID:
                return getUpdateUserRef();
            case USERSTATES_SYSTEMSEQUENCE:
                return getSystemSequence();
            case USERSTATES_ONLINEIND:
                return getOnlineInd();
            case USERSTATES_VERSION:
                return getVersion();
            case USERSTATES_ACTIVEIND:
                return getActiveInd();
            case USERSTATES_UPDATEDT:
                return getUpdateDt();
            case USERSTATES_INSERTDT:
                return getInsertDt();
            case USERSTATES_SYSTEMTRANSACTIONSEQ:
                return getSystemTransactionSeq();
            case USERSTATES_CURRENTLOCATIONREFID:
                return getCurrentLocationRef();
            case USERSTATES_SYSTEMREFID:
                return getSystemRef();
            case USERSTATES_CURRENTROLEREFID:
                return getCurrentRoleRef();
            default:
                return super.getValue(modelRefId);
        }
    }

    /**
	 * @see com.patientis.model.common.IBaseModel#hasField(java.lang.int)
	 */
    public boolean hasField(int modelRefId) {
        switch(modelRefId) {
            case USERSTATES_INSERTUSERREFID:
                return true;
            case USERSTATES_DELETEDIND:
                return true;
            case USERSTATES_USERID:
                return true;
            case USERSTATES_USERSTATEID:
                return true;
            case USERSTATES_UPDATEUSERREFID:
                return true;
            case USERSTATES_SYSTEMSEQUENCE:
                return true;
            case USERSTATES_ONLINEIND:
                return true;
            case USERSTATES_VERSION:
                return true;
            case USERSTATES_ACTIVEIND:
                return true;
            case USERSTATES_UPDATEDT:
                return true;
            case USERSTATES_INSERTDT:
                return true;
            case USERSTATES_SYSTEMTRANSACTIONSEQ:
                return true;
            case USERSTATES_CURRENTLOCATIONREFID:
                return true;
            case USERSTATES_SYSTEMREFID:
                return true;
            case USERSTATES_CURRENTROLEREFID:
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
            case USERSTATES_INSERTUSERREFID:
                return true;
            case USERSTATES_DELETEDIND:
                return true;
            case USERSTATES_USERID:
                return true;
            case USERSTATES_USERSTATEID:
                return true;
            case USERSTATES_UPDATEUSERREFID:
                return true;
            case USERSTATES_SYSTEMSEQUENCE:
                return true;
            case USERSTATES_ONLINEIND:
                return true;
            case USERSTATES_VERSION:
                return true;
            case USERSTATES_ACTIVEIND:
                return true;
            case USERSTATES_UPDATEDT:
                return true;
            case USERSTATES_INSERTDT:
                return true;
            case USERSTATES_SYSTEMTRANSACTIONSEQ:
                return true;
            case USERSTATES_CURRENTLOCATIONREFID:
                return true;
            case USERSTATES_SYSTEMREFID:
                return true;
            case USERSTATES_CURRENTROLEREFID:
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
                case USERSTATES_INSERTUSERREFID:
                    return new ModelField("insert_user_ref_id", "insertUserRef", USERSTATES_INSERTUSERREFID, DisplayModel.class, UserReference.groupName());
                case USERSTATES_DELETEDIND:
                    return new ModelField("deleted_ind", "deletedInd", USERSTATES_DELETEDIND, Integer.class);
                case USERSTATES_USERID:
                    return new ModelField("user_id", "userId", USERSTATES_USERID, Long.class);
                case USERSTATES_USERSTATEID:
                    return new ModelField("user_state_id", "userStateId", USERSTATES_USERSTATEID, Long.class);
                case USERSTATES_UPDATEUSERREFID:
                    return new ModelField("update_user_ref_id", "updateUserRef", USERSTATES_UPDATEUSERREFID, DisplayModel.class, UserReference.groupName());
                case USERSTATES_SYSTEMSEQUENCE:
                    return new ModelField("system_sequence", "systemSequence", USERSTATES_SYSTEMSEQUENCE, Long.class);
                case USERSTATES_ONLINEIND:
                    return new ModelField("online_ind", "onlineInd", USERSTATES_ONLINEIND, Integer.class);
                case USERSTATES_VERSION:
                    return new ModelField("version", "version", USERSTATES_VERSION, Integer.class);
                case USERSTATES_ACTIVEIND:
                    return new ModelField("active_ind", "activeInd", USERSTATES_ACTIVEIND, Integer.class);
                case USERSTATES_UPDATEDT:
                    return new ModelField("update_dt", "updateDt", USERSTATES_UPDATEDT, DateTimeModel.class);
                case USERSTATES_INSERTDT:
                    return new ModelField("insert_dt", "insertDt", USERSTATES_INSERTDT, DateTimeModel.class);
                case USERSTATES_SYSTEMTRANSACTIONSEQ:
                    return new ModelField("system_transaction_seq", "systemTransactionSeq", USERSTATES_SYSTEMTRANSACTIONSEQ, Long.class);
                case USERSTATES_CURRENTLOCATIONREFID:
                    return new ModelField("current_location_ref_id", "currentLocationRef", USERSTATES_CURRENTLOCATIONREFID, DisplayModel.class, LocationReference.groupName());
                case USERSTATES_SYSTEMREFID:
                    return new ModelField("system_ref_id", "systemRef", USERSTATES_SYSTEMREFID, DisplayModel.class, SystemReference.groupName());
                case USERSTATES_CURRENTROLEREFID:
                    return new ModelField("current_role_ref_id", "currentRoleRef", USERSTATES_CURRENTROLEREFID, DisplayModel.class, RoleReference.groupName());
                default:
                    return null;
            }
        } catch (Exception ex) {
            Log.exception(ex);
            return null;
        }
    }

    public static Class getModelClass() {
        return UserStateModel.class;
    }

    /**
	 * @see com.patientis.model.common.IBaseModel#getModelField(java.lang.int)
	 */
    public static ModelField getModelField(int modelRefId) {
        try {
            switch(modelRefId) {
                case USERSTATES_INSERTUSERREFID:
                    return new ModelField("insert_user_ref_id", "insertUserRef", USERSTATES_INSERTUSERREFID, DisplayModel.class, UserReference.groupName());
                case USERSTATES_DELETEDIND:
                    return new ModelField("deleted_ind", "deletedInd", USERSTATES_DELETEDIND, Integer.class);
                case USERSTATES_USERID:
                    return new ModelField("user_id", "userId", USERSTATES_USERID, Long.class);
                case USERSTATES_USERSTATEID:
                    return new ModelField("user_state_id", "userStateId", USERSTATES_USERSTATEID, Long.class);
                case USERSTATES_UPDATEUSERREFID:
                    return new ModelField("update_user_ref_id", "updateUserRef", USERSTATES_UPDATEUSERREFID, DisplayModel.class, UserReference.groupName());
                case USERSTATES_SYSTEMSEQUENCE:
                    return new ModelField("system_sequence", "systemSequence", USERSTATES_SYSTEMSEQUENCE, Long.class);
                case USERSTATES_ONLINEIND:
                    return new ModelField("online_ind", "onlineInd", USERSTATES_ONLINEIND, Integer.class);
                case USERSTATES_VERSION:
                    return new ModelField("version", "version", USERSTATES_VERSION, Integer.class);
                case USERSTATES_ACTIVEIND:
                    return new ModelField("active_ind", "activeInd", USERSTATES_ACTIVEIND, Integer.class);
                case USERSTATES_UPDATEDT:
                    return new ModelField("update_dt", "updateDt", USERSTATES_UPDATEDT, DateTimeModel.class);
                case USERSTATES_INSERTDT:
                    return new ModelField("insert_dt", "insertDt", USERSTATES_INSERTDT, DateTimeModel.class);
                case USERSTATES_SYSTEMTRANSACTIONSEQ:
                    return new ModelField("system_transaction_seq", "systemTransactionSeq", USERSTATES_SYSTEMTRANSACTIONSEQ, Long.class);
                case USERSTATES_CURRENTLOCATIONREFID:
                    return new ModelField("current_location_ref_id", "currentLocationRef", USERSTATES_CURRENTLOCATIONREFID, DisplayModel.class, LocationReference.groupName());
                case USERSTATES_SYSTEMREFID:
                    return new ModelField("system_ref_id", "systemRef", USERSTATES_SYSTEMREFID, DisplayModel.class, SystemReference.groupName());
                case USERSTATES_CURRENTROLEREFID:
                    return new ModelField("current_role_ref_id", "currentRoleRef", USERSTATES_CURRENTROLEREFID, DisplayModel.class, RoleReference.groupName());
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
        fields.add(new ModelField("insert_user_ref_id", "insertUserRef", USERSTATES_INSERTUSERREFID, DisplayModel.class, UserReference.groupName()));
        fields.add(new ModelField("deleted_ind", "deletedInd", USERSTATES_DELETEDIND, Integer.class));
        fields.add(new ModelField("user_id", "userId", USERSTATES_USERID, Long.class));
        fields.add(new ModelField("user_state_id", "userStateId", USERSTATES_USERSTATEID, Long.class));
        fields.add(new ModelField("update_user_ref_id", "updateUserRef", USERSTATES_UPDATEUSERREFID, DisplayModel.class, UserReference.groupName()));
        fields.add(new ModelField("system_sequence", "systemSequence", USERSTATES_SYSTEMSEQUENCE, Long.class));
        fields.add(new ModelField("online_ind", "onlineInd", USERSTATES_ONLINEIND, Integer.class));
        fields.add(new ModelField("version", "version", USERSTATES_VERSION, Integer.class));
        fields.add(new ModelField("active_ind", "activeInd", USERSTATES_ACTIVEIND, Integer.class));
        fields.add(new ModelField("update_dt", "updateDt", USERSTATES_UPDATEDT, DateTimeModel.class));
        fields.add(new ModelField("insert_dt", "insertDt", USERSTATES_INSERTDT, DateTimeModel.class));
        fields.add(new ModelField("system_transaction_seq", "systemTransactionSeq", USERSTATES_SYSTEMTRANSACTIONSEQ, Long.class));
        fields.add(new ModelField("current_location_ref_id", "currentLocationRef", USERSTATES_CURRENTLOCATIONREFID, DisplayModel.class, LocationReference.groupName()));
        fields.add(new ModelField("system_ref_id", "systemRef", USERSTATES_SYSTEMREFID, DisplayModel.class, SystemReference.groupName()));
        fields.add(new ModelField("current_role_ref_id", "currentRoleRef", USERSTATES_CURRENTROLEREFID, DisplayModel.class, RoleReference.groupName()));
        return fields;
    }

    /**
	 * @see com.patientis.model.common.IBaseModel#getModelFields()
	 */
    public static java.util.List<ModelField> getModelFields() {
        java.util.List<ModelField> fields = new java.util.ArrayList<ModelField>();
        fields.add(new ModelField("insert_user_ref_id", "insertUserRef", USERSTATES_INSERTUSERREFID, DisplayModel.class, UserReference.groupName()));
        fields.add(new ModelField("deleted_ind", "deletedInd", USERSTATES_DELETEDIND, Integer.class));
        fields.add(new ModelField("user_id", "userId", USERSTATES_USERID, Long.class));
        fields.add(new ModelField("user_state_id", "userStateId", USERSTATES_USERSTATEID, Long.class));
        fields.add(new ModelField("update_user_ref_id", "updateUserRef", USERSTATES_UPDATEUSERREFID, DisplayModel.class, UserReference.groupName()));
        fields.add(new ModelField("system_sequence", "systemSequence", USERSTATES_SYSTEMSEQUENCE, Long.class));
        fields.add(new ModelField("online_ind", "onlineInd", USERSTATES_ONLINEIND, Integer.class));
        fields.add(new ModelField("version", "version", USERSTATES_VERSION, Integer.class));
        fields.add(new ModelField("active_ind", "activeInd", USERSTATES_ACTIVEIND, Integer.class));
        fields.add(new ModelField("update_dt", "updateDt", USERSTATES_UPDATEDT, DateTimeModel.class));
        fields.add(new ModelField("insert_dt", "insertDt", USERSTATES_INSERTDT, DateTimeModel.class));
        fields.add(new ModelField("system_transaction_seq", "systemTransactionSeq", USERSTATES_SYSTEMTRANSACTIONSEQ, Long.class));
        fields.add(new ModelField("current_location_ref_id", "currentLocationRef", USERSTATES_CURRENTLOCATIONREFID, DisplayModel.class, LocationReference.groupName()));
        fields.add(new ModelField("system_ref_id", "systemRef", USERSTATES_SYSTEMREFID, DisplayModel.class, SystemReference.groupName()));
        fields.add(new ModelField("current_role_ref_id", "currentRoleRef", USERSTATES_CURRENTROLEREFID, DisplayModel.class, RoleReference.groupName()));
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
        if (getCurrentLocationRef().getId() != 0) getCurrentLocationRef().setDisplay(reference.getDisplayValuePrimitive(getCurrentLocationRef().getId()), reference.getShortDisplayValuePrimitive(getCurrentLocationRef().getId()), 0L);
        if (getCurrentRoleRef().getId() != 0) getCurrentRoleRef().setDisplay(reference.getDisplayValuePrimitive(getCurrentRoleRef().getId()), reference.getShortDisplayValuePrimitive(getCurrentRoleRef().getId()), 0L);
        if (getInsertUserRef().getId() != 0) getInsertUserRef().setDisplay(reference.getDisplayValuePrimitive(getInsertUserRef().getId()), reference.getShortDisplayValuePrimitive(getInsertUserRef().getId()), 0L);
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
