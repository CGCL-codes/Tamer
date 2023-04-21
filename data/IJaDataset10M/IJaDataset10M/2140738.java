package com.patientis.model.system;

import com.patientis.framework.logging.Log;
import java.beans.PropertyChangeListener;
import com.patientis.ejb.reference.IGetReference;
import static com.patientis.model.common.ModelReference.*;
import com.patientis.framework.controls.exceptions.ISValidateControlException;
import com.patientis.model.common.*;
import com.patientis.model.reference.*;

/**
 * HelpContentKeyword
 * 
 */
public class HelpContentKeywordDataModel extends BaseModel {

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
	 * 
	 */
    private Long helpContentId = 0L;

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
    private String keywordName = null;

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
            firePropertyChange(String.valueOf(HELPCONTENTKEYWORDS_HELPCONTENTKEYWORDID), oldid, id);
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
            firePropertyChange(String.valueOf(HELPCONTENTKEYWORDS_ACTIVEIND), oldactiveInd, activeInd);
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
            firePropertyChange(String.valueOf(HELPCONTENTKEYWORDS_DELETEDIND), olddeletedInd, deletedInd);
            if (deletedInd == 1) {
                setNotActive();
            }
        }
    }

    /**
	 * 
	 */
    public Long getHelpContentId() {
        return this.helpContentId;
    }

    /**
	 * 
	 */
    public void setHelpContentId(Long helpContentId) {
        if (!(this.helpContentId.longValue() == helpContentId.longValue())) {
            Long oldhelpContentId = 0L;
            oldhelpContentId = this.helpContentId.longValue();
            this.helpContentId = helpContentId.longValue();
            setModified("helpContentId");
            firePropertyChange(String.valueOf(HELPCONTENTKEYWORDS_HELPCONTENTID), oldhelpContentId, helpContentId);
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
            firePropertyChange(String.valueOf(HELPCONTENTKEYWORDS_INSERTDT), oldinsertDt, insertDt);
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
            firePropertyChange(String.valueOf(HELPCONTENTKEYWORDS_INSERTUSERREFID), oldinsertUserRef, insertUserRef);
        }
    }

    /**
	 * 
	 */
    public String getKeywordName() {
        return this.keywordName;
    }

    /**
	 * 
	 */
    public void setKeywordName(String keywordName) {
        if (Converter.isDifferent(this.keywordName, keywordName)) {
            String oldkeywordName = null;
            oldkeywordName = this.keywordName;
            this.keywordName = keywordName;
            setModified("keywordName");
            firePropertyChange(String.valueOf(HELPCONTENTKEYWORDS_KEYWORDNAME), oldkeywordName, keywordName);
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
            firePropertyChange(String.valueOf(HELPCONTENTKEYWORDS_SYSTEMREFID), oldsystemRef, systemRef);
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
            firePropertyChange(String.valueOf(HELPCONTENTKEYWORDS_SYSTEMSEQUENCE), oldsystemSequence, systemSequence);
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
            firePropertyChange(String.valueOf(HELPCONTENTKEYWORDS_SYSTEMTRANSACTIONSEQ), oldsystemTransactionSeq, systemTransactionSeq);
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
            firePropertyChange(String.valueOf(HELPCONTENTKEYWORDS_UPDATEDT), oldupdateDt, updateDt);
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
            firePropertyChange(String.valueOf(HELPCONTENTKEYWORDS_UPDATEUSERREFID), oldupdateUserRef, updateUserRef);
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
            firePropertyChange(String.valueOf(HELPCONTENTKEYWORDS_VERSION), oldversion, version);
        }
    }

    /**
	 * @see com.patientis.model.common.IBaseModel#clear()
	 */
    public void clear() {
        copyFrom(new HelpContentKeywordDataModel(), false);
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
    public HelpContentKeywordModel createNewCopy() {
        HelpContentKeywordModel newCopy = new HelpContentKeywordModel();
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
        HelpContentKeywordDataModel m = (HelpContentKeywordDataModel) model;
        if (modifiedOnly && !m.isModified()) {
            return;
        }
        if (!modifiedOnly || m.isModified("activeInd")) setActiveInd(m.getActiveInd());
        if (!modifiedOnly || m.isModified("deletedInd")) setDeletedInd(m.getDeletedInd());
        if (!modifiedOnly || m.isModified("helpContentId")) setHelpContentId(m.getHelpContentId());
        if (!modifiedOnly || m.isModified("insertDt")) setInsertDt(m.getInsertDt());
        if (!modifiedOnly || m.isModified("insertUserRef")) setInsertUserRef(m.getInsertUserRef());
        if (!modifiedOnly || m.isModified("keywordName")) setKeywordName(m.getKeywordName());
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
        HelpContentKeywordDataModel m = (HelpContentKeywordDataModel) model;
        if (modifiedOnly && !m.isModified()) {
            return;
        }
        if (!modifiedOnly || m.isModified("activeInd")) setActiveInd(m.getActiveInd());
        if (!modifiedOnly || m.isModified("deletedInd")) setDeletedInd(m.getDeletedInd());
        if (!modifiedOnly || m.isModified("helpContentId")) setHelpContentId(m.getHelpContentId());
        if (!modifiedOnly || m.isModified("insertDt")) setInsertDt(m.getInsertDt());
        if (!modifiedOnly || m.isModified("insertUserRef")) setInsertUserRef(m.getInsertUserRef());
        if (!modifiedOnly || m.isModified("keywordName")) setKeywordName(m.getKeywordName());
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
                case HELPCONTENTKEYWORDS_ACTIVEIND:
                    setActiveInd(Converter.convertInteger(value));
                    break;
                case HELPCONTENTKEYWORDS_DELETEDIND:
                    setDeletedInd(Converter.convertInteger(value));
                    break;
                case HELPCONTENTKEYWORDS_HELPCONTENTID:
                    setHelpContentId(Converter.convertLong(value));
                    break;
                case HELPCONTENTKEYWORDS_INSERTDT:
                    setInsertDt(Converter.convertDateTimeModel(value));
                    break;
                case HELPCONTENTKEYWORDS_INSERTUSERREFID:
                    setInsertUserRef(Converter.convertDisplayModel(value));
                    break;
                case HELPCONTENTKEYWORDS_KEYWORDNAME:
                    setKeywordName(Converter.convertString(value));
                    break;
                case HELPCONTENTKEYWORDS_SYSTEMREFID:
                    setSystemRef(Converter.convertDisplayModel(value));
                    break;
                case HELPCONTENTKEYWORDS_SYSTEMSEQUENCE:
                    setSystemSequence(Converter.convertLong(value));
                    break;
                case HELPCONTENTKEYWORDS_SYSTEMTRANSACTIONSEQ:
                    setSystemTransactionSeq(Converter.convertLong(value));
                    break;
                case HELPCONTENTKEYWORDS_UPDATEDT:
                    setUpdateDt(Converter.convertDateTimeModel(value));
                    break;
                case HELPCONTENTKEYWORDS_UPDATEUSERREFID:
                    setUpdateUserRef(Converter.convertDisplayModel(value));
                    break;
                case HELPCONTENTKEYWORDS_VERSION:
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
            case HELPCONTENTKEYWORDS_KEYWORDNAME:
                return getKeywordName();
            case HELPCONTENTKEYWORDS_SYSTEMTRANSACTIONSEQ:
                return getSystemTransactionSeq();
            case HELPCONTENTKEYWORDS_SYSTEMSEQUENCE:
                return getSystemSequence();
            case HELPCONTENTKEYWORDS_HELPCONTENTID:
                return getHelpContentId();
            case HELPCONTENTKEYWORDS_HELPCONTENTKEYWORDID:
                return getId();
            case HELPCONTENTKEYWORDS_SYSTEMREFID:
                return getSystemRef();
            case HELPCONTENTKEYWORDS_DELETEDIND:
                return getDeletedInd();
            case HELPCONTENTKEYWORDS_UPDATEDT:
                return getUpdateDt();
            case HELPCONTENTKEYWORDS_INSERTDT:
                return getInsertDt();
            case HELPCONTENTKEYWORDS_UPDATEUSERREFID:
                return getUpdateUserRef();
            case HELPCONTENTKEYWORDS_INSERTUSERREFID:
                return getInsertUserRef();
            case HELPCONTENTKEYWORDS_VERSION:
                return getVersion();
            case HELPCONTENTKEYWORDS_ACTIVEIND:
                return getActiveInd();
            default:
                return super.getValue(modelRefId);
        }
    }

    /**
	 * @see com.patientis.model.common.IBaseModel#hasField(java.lang.int)
	 */
    public boolean hasField(int modelRefId) {
        switch(modelRefId) {
            case HELPCONTENTKEYWORDS_KEYWORDNAME:
                return true;
            case HELPCONTENTKEYWORDS_SYSTEMTRANSACTIONSEQ:
                return true;
            case HELPCONTENTKEYWORDS_SYSTEMSEQUENCE:
                return true;
            case HELPCONTENTKEYWORDS_HELPCONTENTID:
                return true;
            case HELPCONTENTKEYWORDS_HELPCONTENTKEYWORDID:
                return true;
            case HELPCONTENTKEYWORDS_SYSTEMREFID:
                return true;
            case HELPCONTENTKEYWORDS_DELETEDIND:
                return true;
            case HELPCONTENTKEYWORDS_UPDATEDT:
                return true;
            case HELPCONTENTKEYWORDS_INSERTDT:
                return true;
            case HELPCONTENTKEYWORDS_UPDATEUSERREFID:
                return true;
            case HELPCONTENTKEYWORDS_INSERTUSERREFID:
                return true;
            case HELPCONTENTKEYWORDS_VERSION:
                return true;
            case HELPCONTENTKEYWORDS_ACTIVEIND:
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
            case HELPCONTENTKEYWORDS_KEYWORDNAME:
                return true;
            case HELPCONTENTKEYWORDS_SYSTEMTRANSACTIONSEQ:
                return true;
            case HELPCONTENTKEYWORDS_SYSTEMSEQUENCE:
                return true;
            case HELPCONTENTKEYWORDS_HELPCONTENTID:
                return true;
            case HELPCONTENTKEYWORDS_HELPCONTENTKEYWORDID:
                return true;
            case HELPCONTENTKEYWORDS_SYSTEMREFID:
                return true;
            case HELPCONTENTKEYWORDS_DELETEDIND:
                return true;
            case HELPCONTENTKEYWORDS_UPDATEDT:
                return true;
            case HELPCONTENTKEYWORDS_INSERTDT:
                return true;
            case HELPCONTENTKEYWORDS_UPDATEUSERREFID:
                return true;
            case HELPCONTENTKEYWORDS_INSERTUSERREFID:
                return true;
            case HELPCONTENTKEYWORDS_VERSION:
                return true;
            case HELPCONTENTKEYWORDS_ACTIVEIND:
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
                case HELPCONTENTKEYWORDS_KEYWORDNAME:
                    return new ModelField("keyword_name", "keywordName", HELPCONTENTKEYWORDS_KEYWORDNAME, String.class);
                case HELPCONTENTKEYWORDS_SYSTEMTRANSACTIONSEQ:
                    return new ModelField("system_transaction_seq", "systemTransactionSeq", HELPCONTENTKEYWORDS_SYSTEMTRANSACTIONSEQ, Long.class);
                case HELPCONTENTKEYWORDS_SYSTEMSEQUENCE:
                    return new ModelField("system_sequence", "systemSequence", HELPCONTENTKEYWORDS_SYSTEMSEQUENCE, Long.class);
                case HELPCONTENTKEYWORDS_HELPCONTENTID:
                    return new ModelField("help_content_id", "helpContentId", HELPCONTENTKEYWORDS_HELPCONTENTID, Long.class);
                case HELPCONTENTKEYWORDS_HELPCONTENTKEYWORDID:
                    return new ModelField("help_content_keyword_id", "helpContentKeywordId", HELPCONTENTKEYWORDS_HELPCONTENTKEYWORDID, Long.class);
                case HELPCONTENTKEYWORDS_SYSTEMREFID:
                    return new ModelField("system_ref_id", "systemRef", HELPCONTENTKEYWORDS_SYSTEMREFID, DisplayModel.class, SystemReference.groupName());
                case HELPCONTENTKEYWORDS_DELETEDIND:
                    return new ModelField("deleted_ind", "deletedInd", HELPCONTENTKEYWORDS_DELETEDIND, Integer.class);
                case HELPCONTENTKEYWORDS_UPDATEDT:
                    return new ModelField("update_dt", "updateDt", HELPCONTENTKEYWORDS_UPDATEDT, DateTimeModel.class);
                case HELPCONTENTKEYWORDS_INSERTDT:
                    return new ModelField("insert_dt", "insertDt", HELPCONTENTKEYWORDS_INSERTDT, DateTimeModel.class);
                case HELPCONTENTKEYWORDS_UPDATEUSERREFID:
                    return new ModelField("update_user_ref_id", "updateUserRef", HELPCONTENTKEYWORDS_UPDATEUSERREFID, DisplayModel.class, UserReference.groupName());
                case HELPCONTENTKEYWORDS_INSERTUSERREFID:
                    return new ModelField("insert_user_ref_id", "insertUserRef", HELPCONTENTKEYWORDS_INSERTUSERREFID, DisplayModel.class, UserReference.groupName());
                case HELPCONTENTKEYWORDS_VERSION:
                    return new ModelField("version", "version", HELPCONTENTKEYWORDS_VERSION, Integer.class);
                case HELPCONTENTKEYWORDS_ACTIVEIND:
                    return new ModelField("active_ind", "activeInd", HELPCONTENTKEYWORDS_ACTIVEIND, Integer.class);
                default:
                    return null;
            }
        } catch (Exception ex) {
            Log.exception(ex);
            return null;
        }
    }

    public static Class getModelClass() {
        return HelpContentKeywordModel.class;
    }

    /**
	 * @see com.patientis.model.common.IBaseModel#getModelField(java.lang.int)
	 */
    public static ModelField getModelField(int modelRefId) {
        try {
            switch(modelRefId) {
                case HELPCONTENTKEYWORDS_KEYWORDNAME:
                    return new ModelField("keyword_name", "keywordName", HELPCONTENTKEYWORDS_KEYWORDNAME, String.class);
                case HELPCONTENTKEYWORDS_SYSTEMTRANSACTIONSEQ:
                    return new ModelField("system_transaction_seq", "systemTransactionSeq", HELPCONTENTKEYWORDS_SYSTEMTRANSACTIONSEQ, Long.class);
                case HELPCONTENTKEYWORDS_SYSTEMSEQUENCE:
                    return new ModelField("system_sequence", "systemSequence", HELPCONTENTKEYWORDS_SYSTEMSEQUENCE, Long.class);
                case HELPCONTENTKEYWORDS_HELPCONTENTID:
                    return new ModelField("help_content_id", "helpContentId", HELPCONTENTKEYWORDS_HELPCONTENTID, Long.class);
                case HELPCONTENTKEYWORDS_HELPCONTENTKEYWORDID:
                    return new ModelField("help_content_keyword_id", "helpContentKeywordId", HELPCONTENTKEYWORDS_HELPCONTENTKEYWORDID, Long.class);
                case HELPCONTENTKEYWORDS_SYSTEMREFID:
                    return new ModelField("system_ref_id", "systemRef", HELPCONTENTKEYWORDS_SYSTEMREFID, DisplayModel.class, SystemReference.groupName());
                case HELPCONTENTKEYWORDS_DELETEDIND:
                    return new ModelField("deleted_ind", "deletedInd", HELPCONTENTKEYWORDS_DELETEDIND, Integer.class);
                case HELPCONTENTKEYWORDS_UPDATEDT:
                    return new ModelField("update_dt", "updateDt", HELPCONTENTKEYWORDS_UPDATEDT, DateTimeModel.class);
                case HELPCONTENTKEYWORDS_INSERTDT:
                    return new ModelField("insert_dt", "insertDt", HELPCONTENTKEYWORDS_INSERTDT, DateTimeModel.class);
                case HELPCONTENTKEYWORDS_UPDATEUSERREFID:
                    return new ModelField("update_user_ref_id", "updateUserRef", HELPCONTENTKEYWORDS_UPDATEUSERREFID, DisplayModel.class, UserReference.groupName());
                case HELPCONTENTKEYWORDS_INSERTUSERREFID:
                    return new ModelField("insert_user_ref_id", "insertUserRef", HELPCONTENTKEYWORDS_INSERTUSERREFID, DisplayModel.class, UserReference.groupName());
                case HELPCONTENTKEYWORDS_VERSION:
                    return new ModelField("version", "version", HELPCONTENTKEYWORDS_VERSION, Integer.class);
                case HELPCONTENTKEYWORDS_ACTIVEIND:
                    return new ModelField("active_ind", "activeInd", HELPCONTENTKEYWORDS_ACTIVEIND, Integer.class);
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
        fields.add(new ModelField("keyword_name", "keywordName", HELPCONTENTKEYWORDS_KEYWORDNAME, String.class));
        fields.add(new ModelField("system_transaction_seq", "systemTransactionSeq", HELPCONTENTKEYWORDS_SYSTEMTRANSACTIONSEQ, Long.class));
        fields.add(new ModelField("system_sequence", "systemSequence", HELPCONTENTKEYWORDS_SYSTEMSEQUENCE, Long.class));
        fields.add(new ModelField("help_content_id", "helpContentId", HELPCONTENTKEYWORDS_HELPCONTENTID, Long.class));
        fields.add(new ModelField("help_content_keyword_id", "helpContentKeywordId", HELPCONTENTKEYWORDS_HELPCONTENTKEYWORDID, Long.class));
        fields.add(new ModelField("system_ref_id", "systemRef", HELPCONTENTKEYWORDS_SYSTEMREFID, DisplayModel.class, SystemReference.groupName()));
        fields.add(new ModelField("deleted_ind", "deletedInd", HELPCONTENTKEYWORDS_DELETEDIND, Integer.class));
        fields.add(new ModelField("update_dt", "updateDt", HELPCONTENTKEYWORDS_UPDATEDT, DateTimeModel.class));
        fields.add(new ModelField("insert_dt", "insertDt", HELPCONTENTKEYWORDS_INSERTDT, DateTimeModel.class));
        fields.add(new ModelField("update_user_ref_id", "updateUserRef", HELPCONTENTKEYWORDS_UPDATEUSERREFID, DisplayModel.class, UserReference.groupName()));
        fields.add(new ModelField("insert_user_ref_id", "insertUserRef", HELPCONTENTKEYWORDS_INSERTUSERREFID, DisplayModel.class, UserReference.groupName()));
        fields.add(new ModelField("version", "version", HELPCONTENTKEYWORDS_VERSION, Integer.class));
        fields.add(new ModelField("active_ind", "activeInd", HELPCONTENTKEYWORDS_ACTIVEIND, Integer.class));
        return fields;
    }

    /**
	 * @see com.patientis.model.common.IBaseModel#getModelFields()
	 */
    public static java.util.List<ModelField> getModelFields() {
        java.util.List<ModelField> fields = new java.util.ArrayList<ModelField>();
        fields.add(new ModelField("keyword_name", "keywordName", HELPCONTENTKEYWORDS_KEYWORDNAME, String.class));
        fields.add(new ModelField("system_transaction_seq", "systemTransactionSeq", HELPCONTENTKEYWORDS_SYSTEMTRANSACTIONSEQ, Long.class));
        fields.add(new ModelField("system_sequence", "systemSequence", HELPCONTENTKEYWORDS_SYSTEMSEQUENCE, Long.class));
        fields.add(new ModelField("help_content_id", "helpContentId", HELPCONTENTKEYWORDS_HELPCONTENTID, Long.class));
        fields.add(new ModelField("help_content_keyword_id", "helpContentKeywordId", HELPCONTENTKEYWORDS_HELPCONTENTKEYWORDID, Long.class));
        fields.add(new ModelField("system_ref_id", "systemRef", HELPCONTENTKEYWORDS_SYSTEMREFID, DisplayModel.class, SystemReference.groupName()));
        fields.add(new ModelField("deleted_ind", "deletedInd", HELPCONTENTKEYWORDS_DELETEDIND, Integer.class));
        fields.add(new ModelField("update_dt", "updateDt", HELPCONTENTKEYWORDS_UPDATEDT, DateTimeModel.class));
        fields.add(new ModelField("insert_dt", "insertDt", HELPCONTENTKEYWORDS_INSERTDT, DateTimeModel.class));
        fields.add(new ModelField("update_user_ref_id", "updateUserRef", HELPCONTENTKEYWORDS_UPDATEUSERREFID, DisplayModel.class, UserReference.groupName()));
        fields.add(new ModelField("insert_user_ref_id", "insertUserRef", HELPCONTENTKEYWORDS_INSERTUSERREFID, DisplayModel.class, UserReference.groupName()));
        fields.add(new ModelField("version", "version", HELPCONTENTKEYWORDS_VERSION, Integer.class));
        fields.add(new ModelField("active_ind", "activeInd", HELPCONTENTKEYWORDS_ACTIVEIND, Integer.class));
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
        if (getKeywordName() != null && getKeywordName().length() > 64) {
            throw new FieldTooLongException("KeywordName");
        }
    }

    /**
	 * @see com.patientis.model.common.IBaseModel#isValid()
	 */
    public boolean isValid() {
        if (getKeywordName() != null && getKeywordName().length() > 64) {
            return false;
        }
        return true;
    }
}
