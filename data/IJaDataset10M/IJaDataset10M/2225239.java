package com.patientis.model.patient;

import com.patientis.framework.logging.Log;
import java.beans.PropertyChangeListener;
import com.patientis.ejb.reference.IGetReference;
import static com.patientis.model.common.ModelReference.*;
import com.patientis.framework.controls.exceptions.ISValidateControlException;
import com.patientis.model.common.*;
import com.patientis.model.reference.*;

/**
 * PatientPortalUser
 * 
 */
public class PatientPortalUserDataModel extends BaseModel {

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
	 * Unique identifier for patient. Foreign key to patients on patient_id
	 */
    private Long patientId = 0L;

    /**
	 * Role of portal_user.  Foreign key to refs on PortalRole
	 */
    private DisplayModel portalRoleRef = new DisplayModel(this);

    /**
	 * portal_user.  Foreign key to refs on portal_user
	 */
    private Long portalUser = 0L;

    /**
	 * Date the role ends or ended
	 */
    private DateTimeModel roleEndDt = DateTimeModel.getNow();

    /**
	 * 
	 */
    private int roleEndDtOffset = 0;

    /**
	 * Date precision.  Foreign key to refs on DateAccuracy
	 */
    private DisplayModel roleEndDtPrecisionRef = new DisplayModel(this);

    /**
	 * Date the role started or starts
	 */
    private DateTimeModel roleStartDt = DateTimeModel.getNow();

    /**
	 * 
	 */
    private int roleStartDtOffset = 0;

    /**
	 * Date precision.  Foreign key to refs on DateAccuracy
	 */
    private DisplayModel roleStartDtPrecisionRef = new DisplayModel(this);

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
            firePropertyChange(String.valueOf(PATIENTPORTALUSERS_PATIENTPORTALUSERID), oldid, id);
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
            firePropertyChange(String.valueOf(PATIENTPORTALUSERS_ACTIVEIND), oldactiveInd, activeInd);
            if (activeInd == 1) {
                setNotDeleted();
            }
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
            firePropertyChange(String.valueOf(PATIENTPORTALUSERS_DELETEDIND), olddeletedInd, deletedInd);
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
            firePropertyChange(String.valueOf(PATIENTPORTALUSERS_INSERTDT), oldinsertDt, insertDt);
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
            firePropertyChange(String.valueOf(PATIENTPORTALUSERS_INSERTUSERREFID), oldinsertUserRef, insertUserRef);
        }
    }

    /**
	 * Unique identifier for patient. Foreign key to patients on patient_id
	 */
    public Long getPatientId() {
        return this.patientId;
    }

    /**
	 * Unique identifier for patient. Foreign key to patients on patient_id
	 */
    public void setPatientId(Long patientId) {
        if (!(this.patientId.longValue() == patientId.longValue())) {
            Long oldpatientId = 0L;
            oldpatientId = this.patientId.longValue();
            this.patientId = patientId.longValue();
            setModified("patientId");
            firePropertyChange(String.valueOf(PATIENTPORTALUSERS_PATIENTID), oldpatientId, patientId);
        }
    }

    /**
	 * Role of portal_user.  Foreign key to refs on PortalRole
	 */
    public DisplayModel getPortalRoleRef() {
        return this.portalRoleRef;
    }

    /**
	 * Role of portal_user.  Foreign key to refs on PortalRole
	 */
    public void setPortalRoleRef(DisplayModel portalRoleRef) {
        if (Converter.isDifferent(this.portalRoleRef, portalRoleRef)) {
            DisplayModel oldportalRoleRef = new DisplayModel(this);
            oldportalRoleRef.copyAllFrom(this.portalRoleRef);
            this.portalRoleRef.copyAllFrom(portalRoleRef);
            setModified("portalRoleRef");
            firePropertyChange(String.valueOf(PATIENTPORTALUSERS_PORTALROLEREFID), oldportalRoleRef, portalRoleRef);
        }
    }

    /**
	 * portal_user.  Foreign key to refs on portal_user
	 */
    public Long getPortalUser() {
        return this.portalUser;
    }

    /**
	 * portal_user.  Foreign key to refs on portal_user
	 */
    public void setPortalUser(Long portalUser) {
        if (!(this.portalUser.longValue() == portalUser.longValue())) {
            Long oldportalUser = 0L;
            oldportalUser = this.portalUser.longValue();
            this.portalUser = portalUser.longValue();
            setModified("portalUser");
            firePropertyChange(String.valueOf(PATIENTPORTALUSERS_PORTALUSERID), oldportalUser, portalUser);
        }
    }

    /**
	 * Date the role ends or ended
	 */
    public DateTimeModel getRoleEndDt() {
        return this.roleEndDt;
    }

    /**
	 * Date the role ends or ended
	 */
    public void setRoleEndDt(DateTimeModel roleEndDt) {
        if (Converter.isDifferent(this.roleEndDt, roleEndDt)) {
            DateTimeModel oldroleEndDt = DateTimeModel.getNow();
            oldroleEndDt.copyAllFrom(this.roleEndDt);
            this.roleEndDt.copyAllFrom(roleEndDt);
            setModified("roleEndDt");
            firePropertyChange(String.valueOf(PATIENTPORTALUSERS_ROLEENDDT), oldroleEndDt, roleEndDt);
        }
    }

    /**
	 * 
	 */
    public int getRoleEndDtOffset() {
        return this.roleEndDtOffset;
    }

    /**
	 * 
	 */
    public void setRoleEndDtOffset(int roleEndDtOffset) {
        if (!(this.roleEndDtOffset == roleEndDtOffset)) {
            int oldroleEndDtOffset = 0;
            oldroleEndDtOffset = this.roleEndDtOffset;
            this.roleEndDtOffset = roleEndDtOffset;
            setModified("roleEndDtOffset");
            firePropertyChange(String.valueOf(PATIENTPORTALUSERS_ROLEENDDTOFFSET), oldroleEndDtOffset, roleEndDtOffset);
        }
    }

    /**
	 * Date precision.  Foreign key to refs on DateAccuracy
	 */
    public DisplayModel getRoleEndDtPrecisionRef() {
        return this.roleEndDtPrecisionRef;
    }

    /**
	 * Date precision.  Foreign key to refs on DateAccuracy
	 */
    public void setRoleEndDtPrecisionRef(DisplayModel roleEndDtPrecisionRef) {
        if (Converter.isDifferent(this.roleEndDtPrecisionRef, roleEndDtPrecisionRef)) {
            DisplayModel oldroleEndDtPrecisionRef = new DisplayModel(this);
            oldroleEndDtPrecisionRef.copyAllFrom(this.roleEndDtPrecisionRef);
            this.roleEndDtPrecisionRef.copyAllFrom(roleEndDtPrecisionRef);
            setModified("roleEndDtPrecisionRef");
            firePropertyChange(String.valueOf(PATIENTPORTALUSERS_ROLEENDDTPRECISIONREFID), oldroleEndDtPrecisionRef, roleEndDtPrecisionRef);
        }
    }

    /**
	 * Date the role started or starts
	 */
    public DateTimeModel getRoleStartDt() {
        return this.roleStartDt;
    }

    /**
	 * Date the role started or starts
	 */
    public void setRoleStartDt(DateTimeModel roleStartDt) {
        if (Converter.isDifferent(this.roleStartDt, roleStartDt)) {
            DateTimeModel oldroleStartDt = DateTimeModel.getNow();
            oldroleStartDt.copyAllFrom(this.roleStartDt);
            this.roleStartDt.copyAllFrom(roleStartDt);
            setModified("roleStartDt");
            firePropertyChange(String.valueOf(PATIENTPORTALUSERS_ROLESTARTDT), oldroleStartDt, roleStartDt);
        }
    }

    /**
	 * 
	 */
    public int getRoleStartDtOffset() {
        return this.roleStartDtOffset;
    }

    /**
	 * 
	 */
    public void setRoleStartDtOffset(int roleStartDtOffset) {
        if (!(this.roleStartDtOffset == roleStartDtOffset)) {
            int oldroleStartDtOffset = 0;
            oldroleStartDtOffset = this.roleStartDtOffset;
            this.roleStartDtOffset = roleStartDtOffset;
            setModified("roleStartDtOffset");
            firePropertyChange(String.valueOf(PATIENTPORTALUSERS_ROLESTARTDTOFFSET), oldroleStartDtOffset, roleStartDtOffset);
        }
    }

    /**
	 * Date precision.  Foreign key to refs on DateAccuracy
	 */
    public DisplayModel getRoleStartDtPrecisionRef() {
        return this.roleStartDtPrecisionRef;
    }

    /**
	 * Date precision.  Foreign key to refs on DateAccuracy
	 */
    public void setRoleStartDtPrecisionRef(DisplayModel roleStartDtPrecisionRef) {
        if (Converter.isDifferent(this.roleStartDtPrecisionRef, roleStartDtPrecisionRef)) {
            DisplayModel oldroleStartDtPrecisionRef = new DisplayModel(this);
            oldroleStartDtPrecisionRef.copyAllFrom(this.roleStartDtPrecisionRef);
            this.roleStartDtPrecisionRef.copyAllFrom(roleStartDtPrecisionRef);
            setModified("roleStartDtPrecisionRef");
            firePropertyChange(String.valueOf(PATIENTPORTALUSERS_ROLESTARTDTPRECISIONREFID), oldroleStartDtPrecisionRef, roleStartDtPrecisionRef);
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
            firePropertyChange(String.valueOf(PATIENTPORTALUSERS_SYSTEMREFID), oldsystemRef, systemRef);
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
            firePropertyChange(String.valueOf(PATIENTPORTALUSERS_SYSTEMSEQUENCE), oldsystemSequence, systemSequence);
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
            firePropertyChange(String.valueOf(PATIENTPORTALUSERS_SYSTEMTRANSACTIONSEQ), oldsystemTransactionSeq, systemTransactionSeq);
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
            firePropertyChange(String.valueOf(PATIENTPORTALUSERS_UPDATEDT), oldupdateDt, updateDt);
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
            firePropertyChange(String.valueOf(PATIENTPORTALUSERS_UPDATEUSERREFID), oldupdateUserRef, updateUserRef);
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
            firePropertyChange(String.valueOf(PATIENTPORTALUSERS_VERSION), oldversion, version);
        }
    }

    /**
	 * @see com.patientis.model.common.IBaseModel#clear()
	 */
    public void clear() {
        copyFrom(new PatientPortalUserDataModel(), false);
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
    public PatientPortalUserModel createNewCopy() {
        PatientPortalUserModel newCopy = new PatientPortalUserModel();
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
        PatientPortalUserDataModel m = (PatientPortalUserDataModel) model;
        if (modifiedOnly && !m.isModified()) {
            return;
        }
        if (!modifiedOnly || m.isModified("activeInd")) setActiveInd(m.getActiveInd());
        if (!modifiedOnly || m.isModified("deletedInd")) setDeletedInd(m.getDeletedInd());
        if (!modifiedOnly || m.isModified("insertDt")) setInsertDt(m.getInsertDt());
        if (!modifiedOnly || m.isModified("insertUserRef")) setInsertUserRef(m.getInsertUserRef());
        if (!modifiedOnly || m.isModified("patientId")) setPatientId(m.getPatientId());
        if (!modifiedOnly || m.isModified("portalRoleRef")) setPortalRoleRef(m.getPortalRoleRef());
        if (!modifiedOnly || m.isModified("portalUser")) setPortalUser(m.getPortalUser());
        if (!modifiedOnly || m.isModified("roleEndDt")) setRoleEndDt(m.getRoleEndDt());
        if (!modifiedOnly || m.isModified("roleEndDtOffset")) setRoleEndDtOffset(m.getRoleEndDtOffset());
        if (!modifiedOnly || m.isModified("roleEndDtPrecisionRef")) setRoleEndDtPrecisionRef(m.getRoleEndDtPrecisionRef());
        if (!modifiedOnly || m.isModified("roleStartDt")) setRoleStartDt(m.getRoleStartDt());
        if (!modifiedOnly || m.isModified("roleStartDtOffset")) setRoleStartDtOffset(m.getRoleStartDtOffset());
        if (!modifiedOnly || m.isModified("roleStartDtPrecisionRef")) setRoleStartDtPrecisionRef(m.getRoleStartDtPrecisionRef());
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
        PatientPortalUserDataModel m = (PatientPortalUserDataModel) model;
        if (modifiedOnly && !m.isModified()) {
            return;
        }
        if (!modifiedOnly || m.isModified("activeInd")) setActiveInd(m.getActiveInd());
        if (!modifiedOnly || m.isModified("deletedInd")) setDeletedInd(m.getDeletedInd());
        if (!modifiedOnly || m.isModified("insertDt")) setInsertDt(m.getInsertDt());
        if (!modifiedOnly || m.isModified("insertUserRef")) setInsertUserRef(m.getInsertUserRef());
        if (!modifiedOnly || m.isModified("patientId")) setPatientId(m.getPatientId());
        if (!modifiedOnly || m.isModified("portalRoleRef")) setPortalRoleRef(m.getPortalRoleRef());
        if (!modifiedOnly || m.isModified("portalUser")) setPortalUser(m.getPortalUser());
        if (!modifiedOnly || m.isModified("roleEndDt")) setRoleEndDt(m.getRoleEndDt());
        if (!modifiedOnly || m.isModified("roleEndDtOffset")) setRoleEndDtOffset(m.getRoleEndDtOffset());
        if (!modifiedOnly || m.isModified("roleEndDtPrecisionRef")) setRoleEndDtPrecisionRef(m.getRoleEndDtPrecisionRef());
        if (!modifiedOnly || m.isModified("roleStartDt")) setRoleStartDt(m.getRoleStartDt());
        if (!modifiedOnly || m.isModified("roleStartDtOffset")) setRoleStartDtOffset(m.getRoleStartDtOffset());
        if (!modifiedOnly || m.isModified("roleStartDtPrecisionRef")) setRoleStartDtPrecisionRef(m.getRoleStartDtPrecisionRef());
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
                case PATIENTPORTALUSERS_ACTIVEIND:
                    setActiveInd(Converter.convertInteger(value));
                    break;
                case PATIENTPORTALUSERS_DELETEDIND:
                    setDeletedInd(Converter.convertInteger(value));
                    break;
                case PATIENTPORTALUSERS_INSERTDT:
                    setInsertDt(Converter.convertDateTimeModel(value));
                    break;
                case PATIENTPORTALUSERS_INSERTUSERREFID:
                    setInsertUserRef(Converter.convertDisplayModel(value));
                    break;
                case PATIENTPORTALUSERS_PATIENTID:
                    setPatientId(Converter.convertLong(value));
                    break;
                case PATIENTPORTALUSERS_PORTALROLEREFID:
                    setPortalRoleRef(Converter.convertDisplayModel(value));
                    break;
                case PATIENTPORTALUSERS_PORTALUSERID:
                    setPortalUser(Converter.convertLong(value));
                    break;
                case PATIENTPORTALUSERS_ROLEENDDT:
                    setRoleEndDt(Converter.convertDateTimeModel(value));
                    break;
                case PATIENTPORTALUSERS_ROLEENDDTOFFSET:
                    setRoleEndDtOffset(Converter.convertInteger(value));
                    break;
                case PATIENTPORTALUSERS_ROLEENDDTPRECISIONREFID:
                    setRoleEndDtPrecisionRef(Converter.convertDisplayModel(value));
                    break;
                case PATIENTPORTALUSERS_ROLESTARTDT:
                    setRoleStartDt(Converter.convertDateTimeModel(value));
                    break;
                case PATIENTPORTALUSERS_ROLESTARTDTOFFSET:
                    setRoleStartDtOffset(Converter.convertInteger(value));
                    break;
                case PATIENTPORTALUSERS_ROLESTARTDTPRECISIONREFID:
                    setRoleStartDtPrecisionRef(Converter.convertDisplayModel(value));
                    break;
                case PATIENTPORTALUSERS_SYSTEMREFID:
                    setSystemRef(Converter.convertDisplayModel(value));
                    break;
                case PATIENTPORTALUSERS_SYSTEMSEQUENCE:
                    setSystemSequence(Converter.convertLong(value));
                    break;
                case PATIENTPORTALUSERS_SYSTEMTRANSACTIONSEQ:
                    setSystemTransactionSeq(Converter.convertLong(value));
                    break;
                case PATIENTPORTALUSERS_UPDATEDT:
                    setUpdateDt(Converter.convertDateTimeModel(value));
                    break;
                case PATIENTPORTALUSERS_UPDATEUSERREFID:
                    setUpdateUserRef(Converter.convertDisplayModel(value));
                    break;
                case PATIENTPORTALUSERS_VERSION:
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
            case PATIENTPORTALUSERS_UPDATEDT:
                return getUpdateDt();
            case PATIENTPORTALUSERS_INSERTDT:
                return getInsertDt();
            case PATIENTPORTALUSERS_PATIENTPORTALUSERID:
                return getId();
            case PATIENTPORTALUSERS_SYSTEMTRANSACTIONSEQ:
                return getSystemTransactionSeq();
            case PATIENTPORTALUSERS_ROLEENDDTPRECISIONREFID:
                return getRoleEndDtPrecisionRef();
            case PATIENTPORTALUSERS_ROLEENDDTOFFSET:
                return getRoleEndDtOffset();
            case PATIENTPORTALUSERS_ROLESTARTDTPRECISIONREFID:
                return getRoleStartDtPrecisionRef();
            case PATIENTPORTALUSERS_ROLESTARTDTOFFSET:
                return getRoleStartDtOffset();
            case PATIENTPORTALUSERS_ROLESTARTDT:
                return getRoleStartDt();
            case PATIENTPORTALUSERS_ROLEENDDT:
                return getRoleEndDt();
            case PATIENTPORTALUSERS_PATIENTID:
                return getPatientId();
            case PATIENTPORTALUSERS_ACTIVEIND:
                return getActiveInd();
            case PATIENTPORTALUSERS_VERSION:
                return getVersion();
            case PATIENTPORTALUSERS_SYSTEMSEQUENCE:
                return getSystemSequence();
            case PATIENTPORTALUSERS_DELETEDIND:
                return getDeletedInd();
            case PATIENTPORTALUSERS_UPDATEUSERREFID:
                return getUpdateUserRef();
            case PATIENTPORTALUSERS_PORTALUSERID:
                return getPortalUser();
            case PATIENTPORTALUSERS_SYSTEMREFID:
                return getSystemRef();
            case PATIENTPORTALUSERS_PORTALROLEREFID:
                return getPortalRoleRef();
            case PATIENTPORTALUSERS_INSERTUSERREFID:
                return getInsertUserRef();
            default:
                return super.getValue(modelRefId);
        }
    }

    /**
	 * @see com.patientis.model.common.IBaseModel#hasField(java.lang.int)
	 */
    public boolean hasField(int modelRefId) {
        switch(modelRefId) {
            case PATIENTPORTALUSERS_UPDATEDT:
                return true;
            case PATIENTPORTALUSERS_INSERTDT:
                return true;
            case PATIENTPORTALUSERS_PATIENTPORTALUSERID:
                return true;
            case PATIENTPORTALUSERS_SYSTEMTRANSACTIONSEQ:
                return true;
            case PATIENTPORTALUSERS_ROLEENDDTPRECISIONREFID:
                return true;
            case PATIENTPORTALUSERS_ROLEENDDTOFFSET:
                return true;
            case PATIENTPORTALUSERS_ROLESTARTDTPRECISIONREFID:
                return true;
            case PATIENTPORTALUSERS_ROLESTARTDTOFFSET:
                return true;
            case PATIENTPORTALUSERS_ROLESTARTDT:
                return true;
            case PATIENTPORTALUSERS_ROLEENDDT:
                return true;
            case PATIENTPORTALUSERS_PATIENTID:
                return true;
            case PATIENTPORTALUSERS_ACTIVEIND:
                return true;
            case PATIENTPORTALUSERS_VERSION:
                return true;
            case PATIENTPORTALUSERS_SYSTEMSEQUENCE:
                return true;
            case PATIENTPORTALUSERS_DELETEDIND:
                return true;
            case PATIENTPORTALUSERS_UPDATEUSERREFID:
                return true;
            case PATIENTPORTALUSERS_PORTALUSERID:
                return true;
            case PATIENTPORTALUSERS_SYSTEMREFID:
                return true;
            case PATIENTPORTALUSERS_PORTALROLEREFID:
                return true;
            case PATIENTPORTALUSERS_INSERTUSERREFID:
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
            case PATIENTPORTALUSERS_UPDATEDT:
                return true;
            case PATIENTPORTALUSERS_INSERTDT:
                return true;
            case PATIENTPORTALUSERS_PATIENTPORTALUSERID:
                return true;
            case PATIENTPORTALUSERS_SYSTEMTRANSACTIONSEQ:
                return true;
            case PATIENTPORTALUSERS_ROLEENDDTPRECISIONREFID:
                return true;
            case PATIENTPORTALUSERS_ROLEENDDTOFFSET:
                return true;
            case PATIENTPORTALUSERS_ROLESTARTDTPRECISIONREFID:
                return true;
            case PATIENTPORTALUSERS_ROLESTARTDTOFFSET:
                return true;
            case PATIENTPORTALUSERS_ROLESTARTDT:
                return true;
            case PATIENTPORTALUSERS_ROLEENDDT:
                return true;
            case PATIENTPORTALUSERS_PATIENTID:
                return true;
            case PATIENTPORTALUSERS_ACTIVEIND:
                return true;
            case PATIENTPORTALUSERS_VERSION:
                return true;
            case PATIENTPORTALUSERS_SYSTEMSEQUENCE:
                return true;
            case PATIENTPORTALUSERS_DELETEDIND:
                return true;
            case PATIENTPORTALUSERS_UPDATEUSERREFID:
                return true;
            case PATIENTPORTALUSERS_PORTALUSERID:
                return true;
            case PATIENTPORTALUSERS_SYSTEMREFID:
                return true;
            case PATIENTPORTALUSERS_PORTALROLEREFID:
                return true;
            case PATIENTPORTALUSERS_INSERTUSERREFID:
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
                case PATIENTPORTALUSERS_UPDATEDT:
                    return new ModelField("update_dt", "updateDt", PATIENTPORTALUSERS_UPDATEDT, DateTimeModel.class);
                case PATIENTPORTALUSERS_INSERTDT:
                    return new ModelField("insert_dt", "insertDt", PATIENTPORTALUSERS_INSERTDT, DateTimeModel.class);
                case PATIENTPORTALUSERS_PATIENTPORTALUSERID:
                    return new ModelField("patient_portal_user_id", "patientPortalUser", PATIENTPORTALUSERS_PATIENTPORTALUSERID, Long.class);
                case PATIENTPORTALUSERS_SYSTEMTRANSACTIONSEQ:
                    return new ModelField("system_transaction_seq", "systemTransactionSeq", PATIENTPORTALUSERS_SYSTEMTRANSACTIONSEQ, Long.class);
                case PATIENTPORTALUSERS_ROLEENDDTPRECISIONREFID:
                    return new ModelField("role_end_dt_precision_ref_id", "roleEndDtPrecisionRef", PATIENTPORTALUSERS_ROLEENDDTPRECISIONREFID, DisplayModel.class, DateAccuracyReference.groupName());
                case PATIENTPORTALUSERS_ROLEENDDTOFFSET:
                    return new ModelField("role_end_dt_offset", "roleEndDtOffset", PATIENTPORTALUSERS_ROLEENDDTOFFSET, Integer.class);
                case PATIENTPORTALUSERS_ROLESTARTDTPRECISIONREFID:
                    return new ModelField("role_start_dt_precision_ref_id", "roleStartDtPrecisionRef", PATIENTPORTALUSERS_ROLESTARTDTPRECISIONREFID, DisplayModel.class, DateAccuracyReference.groupName());
                case PATIENTPORTALUSERS_ROLESTARTDTOFFSET:
                    return new ModelField("role_start_dt_offset", "roleStartDtOffset", PATIENTPORTALUSERS_ROLESTARTDTOFFSET, Integer.class);
                case PATIENTPORTALUSERS_ROLESTARTDT:
                    return new ModelField("role_start_dt", "roleStartDt", PATIENTPORTALUSERS_ROLESTARTDT, DateTimeModel.class);
                case PATIENTPORTALUSERS_ROLEENDDT:
                    return new ModelField("role_end_dt", "roleEndDt", PATIENTPORTALUSERS_ROLEENDDT, DateTimeModel.class);
                case PATIENTPORTALUSERS_PATIENTID:
                    return new ModelField("patient_id", "patientId", PATIENTPORTALUSERS_PATIENTID, Long.class);
                case PATIENTPORTALUSERS_ACTIVEIND:
                    return new ModelField("active_ind", "activeInd", PATIENTPORTALUSERS_ACTIVEIND, Integer.class);
                case PATIENTPORTALUSERS_VERSION:
                    return new ModelField("version", "version", PATIENTPORTALUSERS_VERSION, Integer.class);
                case PATIENTPORTALUSERS_SYSTEMSEQUENCE:
                    return new ModelField("system_sequence", "systemSequence", PATIENTPORTALUSERS_SYSTEMSEQUENCE, Long.class);
                case PATIENTPORTALUSERS_DELETEDIND:
                    return new ModelField("deleted_ind", "deletedInd", PATIENTPORTALUSERS_DELETEDIND, Integer.class);
                case PATIENTPORTALUSERS_UPDATEUSERREFID:
                    return new ModelField("update_user_ref_id", "updateUserRef", PATIENTPORTALUSERS_UPDATEUSERREFID, DisplayModel.class, UserReference.groupName());
                case PATIENTPORTALUSERS_PORTALUSERID:
                    return new ModelField("portal_user_id", "portalUser", PATIENTPORTALUSERS_PORTALUSERID, Long.class);
                case PATIENTPORTALUSERS_SYSTEMREFID:
                    return new ModelField("system_ref_id", "systemRef", PATIENTPORTALUSERS_SYSTEMREFID, DisplayModel.class, SystemReference.groupName());
                case PATIENTPORTALUSERS_PORTALROLEREFID:
                    return new ModelField("portal_role_ref_id", "portalRoleRef", PATIENTPORTALUSERS_PORTALROLEREFID, DisplayModel.class, PortalRoleReference.groupName());
                case PATIENTPORTALUSERS_INSERTUSERREFID:
                    return new ModelField("insert_user_ref_id", "insertUserRef", PATIENTPORTALUSERS_INSERTUSERREFID, DisplayModel.class, UserReference.groupName());
                default:
                    return null;
            }
        } catch (Exception ex) {
            Log.exception(ex);
            return null;
        }
    }

    public static Class getModelClass() {
        return PatientPortalUserModel.class;
    }

    /**
	 * @see com.patientis.model.common.IBaseModel#getModelField(java.lang.int)
	 */
    public static ModelField getModelField(int modelRefId) {
        try {
            switch(modelRefId) {
                case PATIENTPORTALUSERS_UPDATEDT:
                    return new ModelField("update_dt", "updateDt", PATIENTPORTALUSERS_UPDATEDT, DateTimeModel.class);
                case PATIENTPORTALUSERS_INSERTDT:
                    return new ModelField("insert_dt", "insertDt", PATIENTPORTALUSERS_INSERTDT, DateTimeModel.class);
                case PATIENTPORTALUSERS_PATIENTPORTALUSERID:
                    return new ModelField("patient_portal_user_id", "patientPortalUser", PATIENTPORTALUSERS_PATIENTPORTALUSERID, Long.class);
                case PATIENTPORTALUSERS_SYSTEMTRANSACTIONSEQ:
                    return new ModelField("system_transaction_seq", "systemTransactionSeq", PATIENTPORTALUSERS_SYSTEMTRANSACTIONSEQ, Long.class);
                case PATIENTPORTALUSERS_ROLEENDDTPRECISIONREFID:
                    return new ModelField("role_end_dt_precision_ref_id", "roleEndDtPrecisionRef", PATIENTPORTALUSERS_ROLEENDDTPRECISIONREFID, DisplayModel.class, DateAccuracyReference.groupName());
                case PATIENTPORTALUSERS_ROLEENDDTOFFSET:
                    return new ModelField("role_end_dt_offset", "roleEndDtOffset", PATIENTPORTALUSERS_ROLEENDDTOFFSET, Integer.class);
                case PATIENTPORTALUSERS_ROLESTARTDTPRECISIONREFID:
                    return new ModelField("role_start_dt_precision_ref_id", "roleStartDtPrecisionRef", PATIENTPORTALUSERS_ROLESTARTDTPRECISIONREFID, DisplayModel.class, DateAccuracyReference.groupName());
                case PATIENTPORTALUSERS_ROLESTARTDTOFFSET:
                    return new ModelField("role_start_dt_offset", "roleStartDtOffset", PATIENTPORTALUSERS_ROLESTARTDTOFFSET, Integer.class);
                case PATIENTPORTALUSERS_ROLESTARTDT:
                    return new ModelField("role_start_dt", "roleStartDt", PATIENTPORTALUSERS_ROLESTARTDT, DateTimeModel.class);
                case PATIENTPORTALUSERS_ROLEENDDT:
                    return new ModelField("role_end_dt", "roleEndDt", PATIENTPORTALUSERS_ROLEENDDT, DateTimeModel.class);
                case PATIENTPORTALUSERS_PATIENTID:
                    return new ModelField("patient_id", "patientId", PATIENTPORTALUSERS_PATIENTID, Long.class);
                case PATIENTPORTALUSERS_ACTIVEIND:
                    return new ModelField("active_ind", "activeInd", PATIENTPORTALUSERS_ACTIVEIND, Integer.class);
                case PATIENTPORTALUSERS_VERSION:
                    return new ModelField("version", "version", PATIENTPORTALUSERS_VERSION, Integer.class);
                case PATIENTPORTALUSERS_SYSTEMSEQUENCE:
                    return new ModelField("system_sequence", "systemSequence", PATIENTPORTALUSERS_SYSTEMSEQUENCE, Long.class);
                case PATIENTPORTALUSERS_DELETEDIND:
                    return new ModelField("deleted_ind", "deletedInd", PATIENTPORTALUSERS_DELETEDIND, Integer.class);
                case PATIENTPORTALUSERS_UPDATEUSERREFID:
                    return new ModelField("update_user_ref_id", "updateUserRef", PATIENTPORTALUSERS_UPDATEUSERREFID, DisplayModel.class, UserReference.groupName());
                case PATIENTPORTALUSERS_PORTALUSERID:
                    return new ModelField("portal_user_id", "portalUser", PATIENTPORTALUSERS_PORTALUSERID, Long.class);
                case PATIENTPORTALUSERS_SYSTEMREFID:
                    return new ModelField("system_ref_id", "systemRef", PATIENTPORTALUSERS_SYSTEMREFID, DisplayModel.class, SystemReference.groupName());
                case PATIENTPORTALUSERS_PORTALROLEREFID:
                    return new ModelField("portal_role_ref_id", "portalRoleRef", PATIENTPORTALUSERS_PORTALROLEREFID, DisplayModel.class, PortalRoleReference.groupName());
                case PATIENTPORTALUSERS_INSERTUSERREFID:
                    return new ModelField("insert_user_ref_id", "insertUserRef", PATIENTPORTALUSERS_INSERTUSERREFID, DisplayModel.class, UserReference.groupName());
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
        fields.add(new ModelField("update_dt", "updateDt", PATIENTPORTALUSERS_UPDATEDT, DateTimeModel.class));
        fields.add(new ModelField("insert_dt", "insertDt", PATIENTPORTALUSERS_INSERTDT, DateTimeModel.class));
        fields.add(new ModelField("patient_portal_user_id", "patientPortalUser", PATIENTPORTALUSERS_PATIENTPORTALUSERID, Long.class));
        fields.add(new ModelField("system_transaction_seq", "systemTransactionSeq", PATIENTPORTALUSERS_SYSTEMTRANSACTIONSEQ, Long.class));
        fields.add(new ModelField("role_end_dt_precision_ref_id", "roleEndDtPrecisionRef", PATIENTPORTALUSERS_ROLEENDDTPRECISIONREFID, DisplayModel.class, DateAccuracyReference.groupName()));
        fields.add(new ModelField("role_end_dt_offset", "roleEndDtOffset", PATIENTPORTALUSERS_ROLEENDDTOFFSET, Integer.class));
        fields.add(new ModelField("role_start_dt_precision_ref_id", "roleStartDtPrecisionRef", PATIENTPORTALUSERS_ROLESTARTDTPRECISIONREFID, DisplayModel.class, DateAccuracyReference.groupName()));
        fields.add(new ModelField("role_start_dt_offset", "roleStartDtOffset", PATIENTPORTALUSERS_ROLESTARTDTOFFSET, Integer.class));
        fields.add(new ModelField("role_start_dt", "roleStartDt", PATIENTPORTALUSERS_ROLESTARTDT, DateTimeModel.class));
        fields.add(new ModelField("role_end_dt", "roleEndDt", PATIENTPORTALUSERS_ROLEENDDT, DateTimeModel.class));
        fields.add(new ModelField("patient_id", "patientId", PATIENTPORTALUSERS_PATIENTID, Long.class));
        fields.add(new ModelField("active_ind", "activeInd", PATIENTPORTALUSERS_ACTIVEIND, Integer.class));
        fields.add(new ModelField("version", "version", PATIENTPORTALUSERS_VERSION, Integer.class));
        fields.add(new ModelField("system_sequence", "systemSequence", PATIENTPORTALUSERS_SYSTEMSEQUENCE, Long.class));
        fields.add(new ModelField("deleted_ind", "deletedInd", PATIENTPORTALUSERS_DELETEDIND, Integer.class));
        fields.add(new ModelField("update_user_ref_id", "updateUserRef", PATIENTPORTALUSERS_UPDATEUSERREFID, DisplayModel.class, UserReference.groupName()));
        fields.add(new ModelField("portal_user_id", "portalUser", PATIENTPORTALUSERS_PORTALUSERID, Long.class));
        fields.add(new ModelField("system_ref_id", "systemRef", PATIENTPORTALUSERS_SYSTEMREFID, DisplayModel.class, SystemReference.groupName()));
        fields.add(new ModelField("portal_role_ref_id", "portalRoleRef", PATIENTPORTALUSERS_PORTALROLEREFID, DisplayModel.class, PortalRoleReference.groupName()));
        fields.add(new ModelField("insert_user_ref_id", "insertUserRef", PATIENTPORTALUSERS_INSERTUSERREFID, DisplayModel.class, UserReference.groupName()));
        return fields;
    }

    /**
	 * @see com.patientis.model.common.IBaseModel#getModelFields()
	 */
    public static java.util.List<ModelField> getModelFields() {
        java.util.List<ModelField> fields = new java.util.ArrayList<ModelField>();
        fields.add(new ModelField("update_dt", "updateDt", PATIENTPORTALUSERS_UPDATEDT, DateTimeModel.class));
        fields.add(new ModelField("insert_dt", "insertDt", PATIENTPORTALUSERS_INSERTDT, DateTimeModel.class));
        fields.add(new ModelField("patient_portal_user_id", "patientPortalUser", PATIENTPORTALUSERS_PATIENTPORTALUSERID, Long.class));
        fields.add(new ModelField("system_transaction_seq", "systemTransactionSeq", PATIENTPORTALUSERS_SYSTEMTRANSACTIONSEQ, Long.class));
        fields.add(new ModelField("role_end_dt_precision_ref_id", "roleEndDtPrecisionRef", PATIENTPORTALUSERS_ROLEENDDTPRECISIONREFID, DisplayModel.class, DateAccuracyReference.groupName()));
        fields.add(new ModelField("role_end_dt_offset", "roleEndDtOffset", PATIENTPORTALUSERS_ROLEENDDTOFFSET, Integer.class));
        fields.add(new ModelField("role_start_dt_precision_ref_id", "roleStartDtPrecisionRef", PATIENTPORTALUSERS_ROLESTARTDTPRECISIONREFID, DisplayModel.class, DateAccuracyReference.groupName()));
        fields.add(new ModelField("role_start_dt_offset", "roleStartDtOffset", PATIENTPORTALUSERS_ROLESTARTDTOFFSET, Integer.class));
        fields.add(new ModelField("role_start_dt", "roleStartDt", PATIENTPORTALUSERS_ROLESTARTDT, DateTimeModel.class));
        fields.add(new ModelField("role_end_dt", "roleEndDt", PATIENTPORTALUSERS_ROLEENDDT, DateTimeModel.class));
        fields.add(new ModelField("patient_id", "patientId", PATIENTPORTALUSERS_PATIENTID, Long.class));
        fields.add(new ModelField("active_ind", "activeInd", PATIENTPORTALUSERS_ACTIVEIND, Integer.class));
        fields.add(new ModelField("version", "version", PATIENTPORTALUSERS_VERSION, Integer.class));
        fields.add(new ModelField("system_sequence", "systemSequence", PATIENTPORTALUSERS_SYSTEMSEQUENCE, Long.class));
        fields.add(new ModelField("deleted_ind", "deletedInd", PATIENTPORTALUSERS_DELETEDIND, Integer.class));
        fields.add(new ModelField("update_user_ref_id", "updateUserRef", PATIENTPORTALUSERS_UPDATEUSERREFID, DisplayModel.class, UserReference.groupName()));
        fields.add(new ModelField("portal_user_id", "portalUser", PATIENTPORTALUSERS_PORTALUSERID, Long.class));
        fields.add(new ModelField("system_ref_id", "systemRef", PATIENTPORTALUSERS_SYSTEMREFID, DisplayModel.class, SystemReference.groupName()));
        fields.add(new ModelField("portal_role_ref_id", "portalRoleRef", PATIENTPORTALUSERS_PORTALROLEREFID, DisplayModel.class, PortalRoleReference.groupName()));
        fields.add(new ModelField("insert_user_ref_id", "insertUserRef", PATIENTPORTALUSERS_INSERTUSERREFID, DisplayModel.class, UserReference.groupName()));
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
        if (getPortalRoleRef().getId() != 0) getPortalRoleRef().setDisplay(reference.getDisplayValuePrimitive(getPortalRoleRef().getId()), reference.getShortDisplayValuePrimitive(getPortalRoleRef().getId()), 0L);
        if (getRoleEndDtPrecisionRef().getId() != 0) getRoleEndDtPrecisionRef().setDisplay(reference.getDisplayValuePrimitive(getRoleEndDtPrecisionRef().getId()), reference.getShortDisplayValuePrimitive(getRoleEndDtPrecisionRef().getId()), 0L);
        if (getRoleStartDtPrecisionRef().getId() != 0) getRoleStartDtPrecisionRef().setDisplay(reference.getDisplayValuePrimitive(getRoleStartDtPrecisionRef().getId()), reference.getShortDisplayValuePrimitive(getRoleStartDtPrecisionRef().getId()), 0L);
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
        this.setRoleEndDtOffset(getRoleEndDt().getTimeZoneOffset());
        this.setRoleStartDtOffset(getRoleStartDt().getTimeZoneOffset());
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
