package com.patientis.model.scheduling;

import com.patientis.framework.logging.Log;
import java.beans.PropertyChangeListener;
import com.patientis.ejb.reference.IGetReference;
import static com.patientis.model.common.ModelReference.*;
import com.patientis.framework.controls.exceptions.ISValidateControlException;
import com.patientis.model.common.*;
import com.patientis.model.reference.*;

/**
 * FrequencyValue
 * 
 */
public class FrequencyValueDataModel extends BaseModel {

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
	 * Foreign key to the frequencies table
	 */
    private Long frequencyId = 0L;

    /**
	 * Date the record was created
	 */
    private DateTimeModel insertDt = DateTimeModel.getNow();

    /**
	 * User that created this record
	 */
    private DisplayModel insertUserRef = new DisplayModel(this);

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
	 * Time value in value units
	 */
    private int value = 1;

    /**
	 * Type of value.  Foreign key to reference on ValueUnit
	 */
    private DisplayModel valueUnitRef = new DisplayModel(this);

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
            firePropertyChange(String.valueOf(FREQUENCYVALUES_FREQUENCYVALUEID), oldid, id);
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
            firePropertyChange(String.valueOf(FREQUENCYVALUES_ACTIVEIND), oldactiveInd, activeInd);
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
            firePropertyChange(String.valueOf(FREQUENCYVALUES_DELETEDIND), olddeletedInd, deletedInd);
            if (deletedInd == 1) {
                setNotActive();
            }
        }
    }

    /**
	 * Foreign key to the frequencies table
	 */
    public Long getFrequencyId() {
        return this.frequencyId;
    }

    /**
	 * Foreign key to the frequencies table
	 */
    public void setFrequencyId(Long frequencyId) {
        if (!(this.frequencyId.longValue() == frequencyId.longValue())) {
            Long oldfrequencyId = 0L;
            oldfrequencyId = this.frequencyId.longValue();
            this.frequencyId = frequencyId.longValue();
            setModified("frequencyId");
            firePropertyChange(String.valueOf(FREQUENCYVALUES_FREQUENCYID), oldfrequencyId, frequencyId);
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
            firePropertyChange(String.valueOf(FREQUENCYVALUES_INSERTDT), oldinsertDt, insertDt);
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
            firePropertyChange(String.valueOf(FREQUENCYVALUES_INSERTUSERREFID), oldinsertUserRef, insertUserRef);
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
            firePropertyChange(String.valueOf(FREQUENCYVALUES_SYSTEMREFID), oldsystemRef, systemRef);
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
            firePropertyChange(String.valueOf(FREQUENCYVALUES_SYSTEMSEQUENCE), oldsystemSequence, systemSequence);
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
            firePropertyChange(String.valueOf(FREQUENCYVALUES_SYSTEMTRANSACTIONSEQ), oldsystemTransactionSeq, systemTransactionSeq);
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
            firePropertyChange(String.valueOf(FREQUENCYVALUES_UPDATEDT), oldupdateDt, updateDt);
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
            firePropertyChange(String.valueOf(FREQUENCYVALUES_UPDATEUSERREFID), oldupdateUserRef, updateUserRef);
        }
    }

    /**
	 * Time value in value units
	 */
    public int getValue() {
        return this.value;
    }

    /**
	 * Time value in value units
	 */
    public void setValue(int value) {
        if (!(this.value == value)) {
            int oldvalue = 1;
            oldvalue = this.value;
            this.value = value;
            setModified("value");
            firePropertyChange(String.valueOf(FREQUENCYVALUES_VALUE), oldvalue, value);
        }
    }

    /**
	 * Type of value.  Foreign key to reference on ValueUnit
	 */
    public DisplayModel getValueUnitRef() {
        return this.valueUnitRef;
    }

    /**
	 * Type of value.  Foreign key to reference on ValueUnit
	 */
    public void setValueUnitRef(DisplayModel valueUnitRef) {
        if (Converter.isDifferent(this.valueUnitRef, valueUnitRef)) {
            DisplayModel oldvalueUnitRef = new DisplayModel(this);
            oldvalueUnitRef.copyAllFrom(this.valueUnitRef);
            this.valueUnitRef.copyAllFrom(valueUnitRef);
            setModified("valueUnitRef");
            firePropertyChange(String.valueOf(FREQUENCYVALUES_VALUEUNITREFID), oldvalueUnitRef, valueUnitRef);
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
            firePropertyChange(String.valueOf(FREQUENCYVALUES_VERSION), oldversion, version);
        }
    }

    /**
	 * @see com.patientis.model.common.IBaseModel#clear()
	 */
    public void clear() {
        copyFrom(new FrequencyValueDataModel(), false);
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
    public FrequencyValueModel createNewCopy() {
        FrequencyValueModel newCopy = new FrequencyValueModel();
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
        FrequencyValueDataModel m = (FrequencyValueDataModel) model;
        if (modifiedOnly && !m.isModified()) {
            return;
        }
        if (!modifiedOnly || m.isModified("activeInd")) setActiveInd(m.getActiveInd());
        if (!modifiedOnly || m.isModified("deletedInd")) setDeletedInd(m.getDeletedInd());
        if (!modifiedOnly || m.isModified("frequencyId")) setFrequencyId(m.getFrequencyId());
        if (!modifiedOnly || m.isModified("insertDt")) setInsertDt(m.getInsertDt());
        if (!modifiedOnly || m.isModified("insertUserRef")) setInsertUserRef(m.getInsertUserRef());
        if (!modifiedOnly || m.isModified("systemRef")) setSystemRef(m.getSystemRef());
        if (!modifiedOnly || m.isModified("systemSequence")) setSystemSequence(m.getSystemSequence());
        if (!modifiedOnly || m.isModified("systemTransactionSeq")) setSystemTransactionSeq(m.getSystemTransactionSeq());
        if (!modifiedOnly || m.isModified("updateDt")) setUpdateDt(m.getUpdateDt());
        if (!modifiedOnly || m.isModified("updateUserRef")) setUpdateUserRef(m.getUpdateUserRef());
        if (!modifiedOnly || m.isModified("value")) setValue(m.getValue());
        if (!modifiedOnly || m.isModified("valueUnitRef")) setValueUnitRef(m.getValueUnitRef());
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
        FrequencyValueDataModel m = (FrequencyValueDataModel) model;
        if (modifiedOnly && !m.isModified()) {
            return;
        }
        if (!modifiedOnly || m.isModified("activeInd")) setActiveInd(m.getActiveInd());
        if (!modifiedOnly || m.isModified("deletedInd")) setDeletedInd(m.getDeletedInd());
        if (!modifiedOnly || m.isModified("frequencyId")) setFrequencyId(m.getFrequencyId());
        if (!modifiedOnly || m.isModified("insertDt")) setInsertDt(m.getInsertDt());
        if (!modifiedOnly || m.isModified("insertUserRef")) setInsertUserRef(m.getInsertUserRef());
        if (!modifiedOnly || m.isModified("systemRef")) setSystemRef(m.getSystemRef());
        if (!modifiedOnly || m.isModified("systemSequence")) setSystemSequence(m.getSystemSequence());
        if (!modifiedOnly || m.isModified("systemTransactionSeq")) setSystemTransactionSeq(m.getSystemTransactionSeq());
        if (!modifiedOnly || m.isModified("updateDt")) setUpdateDt(m.getUpdateDt());
        if (!modifiedOnly || m.isModified("updateUserRef")) setUpdateUserRef(m.getUpdateUserRef());
        if (!modifiedOnly || m.isModified("value")) setValue(m.getValue());
        if (!modifiedOnly || m.isModified("valueUnitRef")) setValueUnitRef(m.getValueUnitRef());
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
                case FREQUENCYVALUES_ACTIVEIND:
                    setActiveInd(Converter.convertInteger(value));
                    break;
                case FREQUENCYVALUES_DELETEDIND:
                    setDeletedInd(Converter.convertInteger(value));
                    break;
                case FREQUENCYVALUES_FREQUENCYID:
                    setFrequencyId(Converter.convertLong(value));
                    break;
                case FREQUENCYVALUES_INSERTDT:
                    setInsertDt(Converter.convertDateTimeModel(value));
                    break;
                case FREQUENCYVALUES_INSERTUSERREFID:
                    setInsertUserRef(Converter.convertDisplayModel(value));
                    break;
                case FREQUENCYVALUES_SYSTEMREFID:
                    setSystemRef(Converter.convertDisplayModel(value));
                    break;
                case FREQUENCYVALUES_SYSTEMSEQUENCE:
                    setSystemSequence(Converter.convertLong(value));
                    break;
                case FREQUENCYVALUES_SYSTEMTRANSACTIONSEQ:
                    setSystemTransactionSeq(Converter.convertLong(value));
                    break;
                case FREQUENCYVALUES_UPDATEDT:
                    setUpdateDt(Converter.convertDateTimeModel(value));
                    break;
                case FREQUENCYVALUES_UPDATEUSERREFID:
                    setUpdateUserRef(Converter.convertDisplayModel(value));
                    break;
                case FREQUENCYVALUES_VALUE:
                    setValue(Converter.convertInteger(value));
                    break;
                case FREQUENCYVALUES_VALUEUNITREFID:
                    setValueUnitRef(Converter.convertDisplayModel(value));
                    break;
                case FREQUENCYVALUES_VERSION:
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
            case FREQUENCYVALUES_FREQUENCYID:
                return getFrequencyId();
            case FREQUENCYVALUES_FREQUENCYVALUEID:
                return getId();
            case FREQUENCYVALUES_UPDATEUSERREFID:
                return getUpdateUserRef();
            case FREQUENCYVALUES_SYSTEMREFID:
                return getSystemRef();
            case FREQUENCYVALUES_INSERTDT:
                return getInsertDt();
            case FREQUENCYVALUES_SYSTEMTRANSACTIONSEQ:
                return getSystemTransactionSeq();
            case FREQUENCYVALUES_VALUEUNITREFID:
                return getValueUnitRef();
            case FREQUENCYVALUES_SYSTEMSEQUENCE:
                return getSystemSequence();
            case FREQUENCYVALUES_UPDATEDT:
                return getUpdateDt();
            case FREQUENCYVALUES_ACTIVEIND:
                return getActiveInd();
            case FREQUENCYVALUES_DELETEDIND:
                return getDeletedInd();
            case FREQUENCYVALUES_VALUE:
                return getValue();
            case FREQUENCYVALUES_VERSION:
                return getVersion();
            case FREQUENCYVALUES_INSERTUSERREFID:
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
            case FREQUENCYVALUES_FREQUENCYID:
                return true;
            case FREQUENCYVALUES_FREQUENCYVALUEID:
                return true;
            case FREQUENCYVALUES_UPDATEUSERREFID:
                return true;
            case FREQUENCYVALUES_SYSTEMREFID:
                return true;
            case FREQUENCYVALUES_INSERTDT:
                return true;
            case FREQUENCYVALUES_SYSTEMTRANSACTIONSEQ:
                return true;
            case FREQUENCYVALUES_VALUEUNITREFID:
                return true;
            case FREQUENCYVALUES_SYSTEMSEQUENCE:
                return true;
            case FREQUENCYVALUES_UPDATEDT:
                return true;
            case FREQUENCYVALUES_ACTIVEIND:
                return true;
            case FREQUENCYVALUES_DELETEDIND:
                return true;
            case FREQUENCYVALUES_VALUE:
                return true;
            case FREQUENCYVALUES_VERSION:
                return true;
            case FREQUENCYVALUES_INSERTUSERREFID:
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
            case FREQUENCYVALUES_FREQUENCYID:
                return true;
            case FREQUENCYVALUES_FREQUENCYVALUEID:
                return true;
            case FREQUENCYVALUES_UPDATEUSERREFID:
                return true;
            case FREQUENCYVALUES_SYSTEMREFID:
                return true;
            case FREQUENCYVALUES_INSERTDT:
                return true;
            case FREQUENCYVALUES_SYSTEMTRANSACTIONSEQ:
                return true;
            case FREQUENCYVALUES_VALUEUNITREFID:
                return true;
            case FREQUENCYVALUES_SYSTEMSEQUENCE:
                return true;
            case FREQUENCYVALUES_UPDATEDT:
                return true;
            case FREQUENCYVALUES_ACTIVEIND:
                return true;
            case FREQUENCYVALUES_DELETEDIND:
                return true;
            case FREQUENCYVALUES_VALUE:
                return true;
            case FREQUENCYVALUES_VERSION:
                return true;
            case FREQUENCYVALUES_INSERTUSERREFID:
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
                case FREQUENCYVALUES_FREQUENCYID:
                    return new ModelField("frequency_id", "frequencyId", FREQUENCYVALUES_FREQUENCYID, Long.class);
                case FREQUENCYVALUES_FREQUENCYVALUEID:
                    return new ModelField("frequency_value_id", "frequencyValueId", FREQUENCYVALUES_FREQUENCYVALUEID, Long.class);
                case FREQUENCYVALUES_UPDATEUSERREFID:
                    return new ModelField("update_user_ref_id", "updateUserRef", FREQUENCYVALUES_UPDATEUSERREFID, DisplayModel.class, UserReference.groupName());
                case FREQUENCYVALUES_SYSTEMREFID:
                    return new ModelField("system_ref_id", "systemRef", FREQUENCYVALUES_SYSTEMREFID, DisplayModel.class, SystemReference.groupName());
                case FREQUENCYVALUES_INSERTDT:
                    return new ModelField("insert_dt", "insertDt", FREQUENCYVALUES_INSERTDT, DateTimeModel.class);
                case FREQUENCYVALUES_SYSTEMTRANSACTIONSEQ:
                    return new ModelField("system_transaction_seq", "systemTransactionSeq", FREQUENCYVALUES_SYSTEMTRANSACTIONSEQ, Long.class);
                case FREQUENCYVALUES_VALUEUNITREFID:
                    return new ModelField("value_unit_ref_id", "valueUnitRef", FREQUENCYVALUES_VALUEUNITREFID, DisplayModel.class, ValueUnitReference.groupName());
                case FREQUENCYVALUES_SYSTEMSEQUENCE:
                    return new ModelField("system_sequence", "systemSequence", FREQUENCYVALUES_SYSTEMSEQUENCE, Long.class);
                case FREQUENCYVALUES_UPDATEDT:
                    return new ModelField("update_dt", "updateDt", FREQUENCYVALUES_UPDATEDT, DateTimeModel.class);
                case FREQUENCYVALUES_ACTIVEIND:
                    return new ModelField("active_ind", "activeInd", FREQUENCYVALUES_ACTIVEIND, Integer.class);
                case FREQUENCYVALUES_DELETEDIND:
                    return new ModelField("deleted_ind", "deletedInd", FREQUENCYVALUES_DELETEDIND, Integer.class);
                case FREQUENCYVALUES_VALUE:
                    return new ModelField("value", "value", FREQUENCYVALUES_VALUE, Integer.class);
                case FREQUENCYVALUES_VERSION:
                    return new ModelField("version", "version", FREQUENCYVALUES_VERSION, Integer.class);
                case FREQUENCYVALUES_INSERTUSERREFID:
                    return new ModelField("insert_user_ref_id", "insertUserRef", FREQUENCYVALUES_INSERTUSERREFID, DisplayModel.class, UserReference.groupName());
                default:
                    return null;
            }
        } catch (Exception ex) {
            Log.exception(ex);
            return null;
        }
    }

    public static Class getModelClass() {
        return FrequencyValueModel.class;
    }

    /**
	 * @see com.patientis.model.common.IBaseModel#getModelField(java.lang.int)
	 */
    public static ModelField getModelField(int modelRefId) {
        try {
            switch(modelRefId) {
                case FREQUENCYVALUES_FREQUENCYID:
                    return new ModelField("frequency_id", "frequencyId", FREQUENCYVALUES_FREQUENCYID, Long.class);
                case FREQUENCYVALUES_FREQUENCYVALUEID:
                    return new ModelField("frequency_value_id", "frequencyValueId", FREQUENCYVALUES_FREQUENCYVALUEID, Long.class);
                case FREQUENCYVALUES_UPDATEUSERREFID:
                    return new ModelField("update_user_ref_id", "updateUserRef", FREQUENCYVALUES_UPDATEUSERREFID, DisplayModel.class, UserReference.groupName());
                case FREQUENCYVALUES_SYSTEMREFID:
                    return new ModelField("system_ref_id", "systemRef", FREQUENCYVALUES_SYSTEMREFID, DisplayModel.class, SystemReference.groupName());
                case FREQUENCYVALUES_INSERTDT:
                    return new ModelField("insert_dt", "insertDt", FREQUENCYVALUES_INSERTDT, DateTimeModel.class);
                case FREQUENCYVALUES_SYSTEMTRANSACTIONSEQ:
                    return new ModelField("system_transaction_seq", "systemTransactionSeq", FREQUENCYVALUES_SYSTEMTRANSACTIONSEQ, Long.class);
                case FREQUENCYVALUES_VALUEUNITREFID:
                    return new ModelField("value_unit_ref_id", "valueUnitRef", FREQUENCYVALUES_VALUEUNITREFID, DisplayModel.class, ValueUnitReference.groupName());
                case FREQUENCYVALUES_SYSTEMSEQUENCE:
                    return new ModelField("system_sequence", "systemSequence", FREQUENCYVALUES_SYSTEMSEQUENCE, Long.class);
                case FREQUENCYVALUES_UPDATEDT:
                    return new ModelField("update_dt", "updateDt", FREQUENCYVALUES_UPDATEDT, DateTimeModel.class);
                case FREQUENCYVALUES_ACTIVEIND:
                    return new ModelField("active_ind", "activeInd", FREQUENCYVALUES_ACTIVEIND, Integer.class);
                case FREQUENCYVALUES_DELETEDIND:
                    return new ModelField("deleted_ind", "deletedInd", FREQUENCYVALUES_DELETEDIND, Integer.class);
                case FREQUENCYVALUES_VALUE:
                    return new ModelField("value", "value", FREQUENCYVALUES_VALUE, Integer.class);
                case FREQUENCYVALUES_VERSION:
                    return new ModelField("version", "version", FREQUENCYVALUES_VERSION, Integer.class);
                case FREQUENCYVALUES_INSERTUSERREFID:
                    return new ModelField("insert_user_ref_id", "insertUserRef", FREQUENCYVALUES_INSERTUSERREFID, DisplayModel.class, UserReference.groupName());
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
        fields.add(new ModelField("frequency_id", "frequencyId", FREQUENCYVALUES_FREQUENCYID, Long.class));
        fields.add(new ModelField("frequency_value_id", "frequencyValueId", FREQUENCYVALUES_FREQUENCYVALUEID, Long.class));
        fields.add(new ModelField("update_user_ref_id", "updateUserRef", FREQUENCYVALUES_UPDATEUSERREFID, DisplayModel.class, UserReference.groupName()));
        fields.add(new ModelField("system_ref_id", "systemRef", FREQUENCYVALUES_SYSTEMREFID, DisplayModel.class, SystemReference.groupName()));
        fields.add(new ModelField("insert_dt", "insertDt", FREQUENCYVALUES_INSERTDT, DateTimeModel.class));
        fields.add(new ModelField("system_transaction_seq", "systemTransactionSeq", FREQUENCYVALUES_SYSTEMTRANSACTIONSEQ, Long.class));
        fields.add(new ModelField("value_unit_ref_id", "valueUnitRef", FREQUENCYVALUES_VALUEUNITREFID, DisplayModel.class, ValueUnitReference.groupName()));
        fields.add(new ModelField("system_sequence", "systemSequence", FREQUENCYVALUES_SYSTEMSEQUENCE, Long.class));
        fields.add(new ModelField("update_dt", "updateDt", FREQUENCYVALUES_UPDATEDT, DateTimeModel.class));
        fields.add(new ModelField("active_ind", "activeInd", FREQUENCYVALUES_ACTIVEIND, Integer.class));
        fields.add(new ModelField("deleted_ind", "deletedInd", FREQUENCYVALUES_DELETEDIND, Integer.class));
        fields.add(new ModelField("value", "value", FREQUENCYVALUES_VALUE, Integer.class));
        fields.add(new ModelField("version", "version", FREQUENCYVALUES_VERSION, Integer.class));
        fields.add(new ModelField("insert_user_ref_id", "insertUserRef", FREQUENCYVALUES_INSERTUSERREFID, DisplayModel.class, UserReference.groupName()));
        return fields;
    }

    /**
	 * @see com.patientis.model.common.IBaseModel#getModelFields()
	 */
    public static java.util.List<ModelField> getModelFields() {
        java.util.List<ModelField> fields = new java.util.ArrayList<ModelField>();
        fields.add(new ModelField("frequency_id", "frequencyId", FREQUENCYVALUES_FREQUENCYID, Long.class));
        fields.add(new ModelField("frequency_value_id", "frequencyValueId", FREQUENCYVALUES_FREQUENCYVALUEID, Long.class));
        fields.add(new ModelField("update_user_ref_id", "updateUserRef", FREQUENCYVALUES_UPDATEUSERREFID, DisplayModel.class, UserReference.groupName()));
        fields.add(new ModelField("system_ref_id", "systemRef", FREQUENCYVALUES_SYSTEMREFID, DisplayModel.class, SystemReference.groupName()));
        fields.add(new ModelField("insert_dt", "insertDt", FREQUENCYVALUES_INSERTDT, DateTimeModel.class));
        fields.add(new ModelField("system_transaction_seq", "systemTransactionSeq", FREQUENCYVALUES_SYSTEMTRANSACTIONSEQ, Long.class));
        fields.add(new ModelField("value_unit_ref_id", "valueUnitRef", FREQUENCYVALUES_VALUEUNITREFID, DisplayModel.class, ValueUnitReference.groupName()));
        fields.add(new ModelField("system_sequence", "systemSequence", FREQUENCYVALUES_SYSTEMSEQUENCE, Long.class));
        fields.add(new ModelField("update_dt", "updateDt", FREQUENCYVALUES_UPDATEDT, DateTimeModel.class));
        fields.add(new ModelField("active_ind", "activeInd", FREQUENCYVALUES_ACTIVEIND, Integer.class));
        fields.add(new ModelField("deleted_ind", "deletedInd", FREQUENCYVALUES_DELETEDIND, Integer.class));
        fields.add(new ModelField("value", "value", FREQUENCYVALUES_VALUE, Integer.class));
        fields.add(new ModelField("version", "version", FREQUENCYVALUES_VERSION, Integer.class));
        fields.add(new ModelField("insert_user_ref_id", "insertUserRef", FREQUENCYVALUES_INSERTUSERREFID, DisplayModel.class, UserReference.groupName()));
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
        if (getValueUnitRef().getId() != 0) getValueUnitRef().setDisplay(reference.getDisplayValuePrimitive(getValueUnitRef().getId()), reference.getShortDisplayValuePrimitive(getValueUnitRef().getId()), 0L);
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
