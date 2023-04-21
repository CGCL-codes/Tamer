package com.patientis.model.common;

import com.patientis.framework.logging.Log;
import java.beans.PropertyChangeListener;
import com.patientis.ejb.reference.IGetReference;
import static com.patientis.model.common.ModelReference.*;
import com.patientis.framework.controls.exceptions.ISValidateControlException;
import com.patientis.model.reference.*;

/**
 * Identifier
 * 
 */
public class IdentifierDataModel extends BaseModel {

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
	 * Check digit for MRN validation
	 */
    private String checkDigit = null;

    /**
	 * Deleted indicator 1=Deleted 0=Not deleted
	 */
    private int deletedInd = 0;

    /**
	 * Sequence to enforce uniqueness
	 */
    private int identifierSequence = 0;

    /**
	 * Identifier value
	 */
    private String idvalue = null;

    /**
	 * Uppercase without spaces value of idvalue
	 */
    private String idvalueIndex = null;

    /**
	 * metaphone value of idvalue
	 */
    private String idvalueSound = null;

    /**
	 * Date the record was created
	 */
    private DateTimeModel insertDt = DateTimeModel.getNow();

    /**
	 * User that created this record
	 */
    private DisplayModel insertUserRef = new DisplayModel(this);

    /**
	 * Source of identifier generation foreign key to reference: IdentifierSource
	 */
    private DisplayModel sourceRef = new DisplayModel(this);

    /**
	 * Identifies the system which originally created this row if reference
	 */
    private DisplayModel systemRef = new DisplayModel(this, 100099);

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
            firePropertyChange(String.valueOf(IDENTIFIERS_IDENTIFIERID), oldid, id);
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
            firePropertyChange(String.valueOf(IDENTIFIERS_ACTIVEIND), oldactiveInd, activeInd);
            if (activeInd == 1) {
                setNotDeleted();
            }
        }
    }

    /**
	 * Check digit for MRN validation
	 */
    public String getCheckDigit() {
        return this.checkDigit;
    }

    /**
	 * Check digit for MRN validation
	 */
    public void setCheckDigit(String checkDigit) {
        if (Converter.isDifferent(this.checkDigit, checkDigit)) {
            String oldcheckDigit = null;
            oldcheckDigit = this.checkDigit;
            this.checkDigit = checkDigit;
            setModified("checkDigit");
            firePropertyChange(String.valueOf(IDENTIFIERS_CHECKDIGIT), oldcheckDigit, checkDigit);
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
            firePropertyChange(String.valueOf(IDENTIFIERS_DELETEDIND), olddeletedInd, deletedInd);
            if (deletedInd == 1) {
                setNotActive();
            }
        }
    }

    /**
	 * Sequence to enforce uniqueness
	 */
    public int getIdentifierSequence() {
        return this.identifierSequence;
    }

    /**
	 * Sequence to enforce uniqueness
	 */
    public void setIdentifierSequence(int identifierSequence) {
        if (!(this.identifierSequence == identifierSequence)) {
            int oldidentifierSequence = 0;
            oldidentifierSequence = this.identifierSequence;
            this.identifierSequence = identifierSequence;
            setModified("identifierSequence");
            firePropertyChange(String.valueOf(IDENTIFIERS_IDENTIFIERSEQUENCE), oldidentifierSequence, identifierSequence);
        }
    }

    /**
	 * Identifier value
	 */
    public String getIdvalue() {
        return this.idvalue;
    }

    /**
	 * Identifier value
	 */
    public void setIdvalue(String idvalue) {
        if (Converter.isDifferent(this.idvalue, idvalue)) {
            String oldidvalue = null;
            oldidvalue = this.idvalue;
            this.idvalue = idvalue;
            setModified("idvalue");
            firePropertyChange(String.valueOf(IDENTIFIERS_IDVALUE), oldidvalue, idvalue);
        }
    }

    /**
	 * Uppercase without spaces value of idvalue
	 */
    public String getIdvalueIndex() {
        return this.idvalueIndex;
    }

    /**
	 * Uppercase without spaces value of idvalue
	 */
    public void setIdvalueIndex(String idvalueIndex) {
        if (Converter.isDifferent(this.idvalueIndex, idvalueIndex)) {
            String oldidvalueIndex = null;
            oldidvalueIndex = this.idvalueIndex;
            this.idvalueIndex = idvalueIndex;
            setModified("idvalueIndex");
            firePropertyChange(String.valueOf(IDENTIFIERS_IDVALUEINDEX), oldidvalueIndex, idvalueIndex);
        }
    }

    /**
	 * metaphone value of idvalue
	 */
    public String getIdvalueSound() {
        return this.idvalueSound;
    }

    /**
	 * metaphone value of idvalue
	 */
    public void setIdvalueSound(String idvalueSound) {
        if (Converter.isDifferent(this.idvalueSound, idvalueSound)) {
            String oldidvalueSound = null;
            oldidvalueSound = this.idvalueSound;
            this.idvalueSound = idvalueSound;
            setModified("idvalueSound");
            firePropertyChange(String.valueOf(IDENTIFIERS_IDVALUESOUND), oldidvalueSound, idvalueSound);
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
            firePropertyChange(String.valueOf(IDENTIFIERS_INSERTDT), oldinsertDt, insertDt);
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
            firePropertyChange(String.valueOf(IDENTIFIERS_INSERTUSERREFID), oldinsertUserRef, insertUserRef);
        }
    }

    /**
	 * Source of identifier generation foreign key to reference: IdentifierSource
	 */
    public DisplayModel getSourceRef() {
        return this.sourceRef;
    }

    /**
	 * Source of identifier generation foreign key to reference: IdentifierSource
	 */
    public void setSourceRef(DisplayModel sourceRef) {
        if (Converter.isDifferent(this.sourceRef, sourceRef)) {
            DisplayModel oldsourceRef = new DisplayModel(this);
            oldsourceRef.copyAllFrom(this.sourceRef);
            this.sourceRef.copyAllFrom(sourceRef);
            setModified("sourceRef");
            firePropertyChange(String.valueOf(IDENTIFIERS_SOURCEREFID), oldsourceRef, sourceRef);
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
            DisplayModel oldsystemRef = new DisplayModel(this, 100099);
            oldsystemRef.copyAllFrom(this.systemRef);
            this.systemRef.copyAllFrom(systemRef);
            setModified("systemRef");
            firePropertyChange(String.valueOf(IDENTIFIERS_SYSTEMREFID), oldsystemRef, systemRef);
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
            firePropertyChange(String.valueOf(IDENTIFIERS_SYSTEMSEQUENCE), oldsystemSequence, systemSequence);
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
            firePropertyChange(String.valueOf(IDENTIFIERS_SYSTEMTRANSACTIONSEQ), oldsystemTransactionSeq, systemTransactionSeq);
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
            firePropertyChange(String.valueOf(IDENTIFIERS_UPDATEDT), oldupdateDt, updateDt);
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
            firePropertyChange(String.valueOf(IDENTIFIERS_UPDATEUSERREFID), oldupdateUserRef, updateUserRef);
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
            firePropertyChange(String.valueOf(IDENTIFIERS_VERSION), oldversion, version);
        }
    }

    /**
	 * @see com.patientis.model.common.IBaseModel#clear()
	 */
    public void clear() {
        copyFrom(new IdentifierDataModel(), false);
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
    public IdentifierModel createNewCopy() {
        IdentifierModel newCopy = new IdentifierModel();
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
        IdentifierDataModel m = (IdentifierDataModel) model;
        if (modifiedOnly && !m.isModified()) {
            return;
        }
        if (!modifiedOnly || m.isModified("activeInd")) setActiveInd(m.getActiveInd());
        if (!modifiedOnly || m.isModified("checkDigit")) setCheckDigit(m.getCheckDigit());
        if (!modifiedOnly || m.isModified("deletedInd")) setDeletedInd(m.getDeletedInd());
        if (!modifiedOnly || m.isModified("identifierSequence")) setIdentifierSequence(m.getIdentifierSequence());
        if (!modifiedOnly || m.isModified("idvalue")) setIdvalue(m.getIdvalue());
        if (!modifiedOnly || m.isModified("idvalueIndex")) setIdvalueIndex(m.getIdvalueIndex());
        if (!modifiedOnly || m.isModified("idvalueSound")) setIdvalueSound(m.getIdvalueSound());
        if (!modifiedOnly || m.isModified("insertDt")) setInsertDt(m.getInsertDt());
        if (!modifiedOnly || m.isModified("insertUserRef")) setInsertUserRef(m.getInsertUserRef());
        if (!modifiedOnly || m.isModified("sourceRef")) setSourceRef(m.getSourceRef());
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
        IdentifierDataModel m = (IdentifierDataModel) model;
        if (modifiedOnly && !m.isModified()) {
            return;
        }
        if (!modifiedOnly || m.isModified("activeInd")) setActiveInd(m.getActiveInd());
        if (!modifiedOnly || m.isModified("checkDigit")) setCheckDigit(m.getCheckDigit());
        if (!modifiedOnly || m.isModified("deletedInd")) setDeletedInd(m.getDeletedInd());
        if (!modifiedOnly || m.isModified("identifierSequence")) setIdentifierSequence(m.getIdentifierSequence());
        if (!modifiedOnly || m.isModified("idvalue")) setIdvalue(m.getIdvalue());
        if (!modifiedOnly || m.isModified("idvalueIndex")) setIdvalueIndex(m.getIdvalueIndex());
        if (!modifiedOnly || m.isModified("idvalueSound")) setIdvalueSound(m.getIdvalueSound());
        if (!modifiedOnly || m.isModified("insertDt")) setInsertDt(m.getInsertDt());
        if (!modifiedOnly || m.isModified("insertUserRef")) setInsertUserRef(m.getInsertUserRef());
        if (!modifiedOnly || m.isModified("sourceRef")) setSourceRef(m.getSourceRef());
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
                case IDENTIFIERS_ACTIVEIND:
                    setActiveInd(Converter.convertInteger(value));
                    break;
                case IDENTIFIERS_CHECKDIGIT:
                    setCheckDigit(Converter.convertString(value));
                    break;
                case IDENTIFIERS_DELETEDIND:
                    setDeletedInd(Converter.convertInteger(value));
                    break;
                case IDENTIFIERS_IDENTIFIERSEQUENCE:
                    setIdentifierSequence(Converter.convertInteger(value));
                    break;
                case IDENTIFIERS_IDVALUE:
                    setIdvalue(Converter.convertString(value));
                    break;
                case IDENTIFIERS_IDVALUEINDEX:
                    setIdvalueIndex(Converter.convertString(value));
                    break;
                case IDENTIFIERS_IDVALUESOUND:
                    setIdvalueSound(Converter.convertString(value));
                    break;
                case IDENTIFIERS_INSERTDT:
                    setInsertDt(Converter.convertDateTimeModel(value));
                    break;
                case IDENTIFIERS_INSERTUSERREFID:
                    setInsertUserRef(Converter.convertDisplayModel(value));
                    break;
                case IDENTIFIERS_SOURCEREFID:
                    setSourceRef(Converter.convertDisplayModel(value));
                    break;
                case IDENTIFIERS_SYSTEMREFID:
                    setSystemRef(Converter.convertDisplayModel(value));
                    break;
                case IDENTIFIERS_SYSTEMSEQUENCE:
                    setSystemSequence(Converter.convertLong(value));
                    break;
                case IDENTIFIERS_SYSTEMTRANSACTIONSEQ:
                    setSystemTransactionSeq(Converter.convertLong(value));
                    break;
                case IDENTIFIERS_UPDATEDT:
                    setUpdateDt(Converter.convertDateTimeModel(value));
                    break;
                case IDENTIFIERS_UPDATEUSERREFID:
                    setUpdateUserRef(Converter.convertDisplayModel(value));
                    break;
                case IDENTIFIERS_VERSION:
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
            case IDENTIFIERS_IDVALUE:
                return getIdvalue();
            case IDENTIFIERS_DELETEDIND:
                return getDeletedInd();
            case IDENTIFIERS_SYSTEMSEQUENCE:
                return getSystemSequence();
            case IDENTIFIERS_UPDATEUSERREFID:
                return getUpdateUserRef();
            case IDENTIFIERS_IDENTIFIERID:
                return getId();
            case IDENTIFIERS_IDENTIFIERSEQUENCE:
                return getIdentifierSequence();
            case IDENTIFIERS_SYSTEMREFID:
                return getSystemRef();
            case IDENTIFIERS_INSERTDT:
                return getInsertDt();
            case IDENTIFIERS_ACTIVEIND:
                return getActiveInd();
            case IDENTIFIERS_IDVALUEINDEX:
                return getIdvalueIndex();
            case IDENTIFIERS_VERSION:
                return getVersion();
            case IDENTIFIERS_IDVALUESOUND:
                return getIdvalueSound();
            case IDENTIFIERS_CHECKDIGIT:
                return getCheckDigit();
            case IDENTIFIERS_UPDATEDT:
                return getUpdateDt();
            case IDENTIFIERS_INSERTUSERREFID:
                return getInsertUserRef();
            case IDENTIFIERS_SYSTEMTRANSACTIONSEQ:
                return getSystemTransactionSeq();
            case IDENTIFIERS_SOURCEREFID:
                return getSourceRef();
            default:
                return super.getValue(modelRefId);
        }
    }

    /**
	 * @see com.patientis.model.common.IBaseModel#hasField(java.lang.int)
	 */
    public boolean hasField(int modelRefId) {
        switch(modelRefId) {
            case IDENTIFIERS_IDVALUE:
                return true;
            case IDENTIFIERS_DELETEDIND:
                return true;
            case IDENTIFIERS_SYSTEMSEQUENCE:
                return true;
            case IDENTIFIERS_UPDATEUSERREFID:
                return true;
            case IDENTIFIERS_IDENTIFIERID:
                return true;
            case IDENTIFIERS_IDENTIFIERSEQUENCE:
                return true;
            case IDENTIFIERS_SYSTEMREFID:
                return true;
            case IDENTIFIERS_INSERTDT:
                return true;
            case IDENTIFIERS_ACTIVEIND:
                return true;
            case IDENTIFIERS_IDVALUEINDEX:
                return true;
            case IDENTIFIERS_VERSION:
                return true;
            case IDENTIFIERS_IDVALUESOUND:
                return true;
            case IDENTIFIERS_CHECKDIGIT:
                return true;
            case IDENTIFIERS_UPDATEDT:
                return true;
            case IDENTIFIERS_INSERTUSERREFID:
                return true;
            case IDENTIFIERS_SYSTEMTRANSACTIONSEQ:
                return true;
            case IDENTIFIERS_SOURCEREFID:
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
            case IDENTIFIERS_IDVALUE:
                return true;
            case IDENTIFIERS_DELETEDIND:
                return true;
            case IDENTIFIERS_SYSTEMSEQUENCE:
                return true;
            case IDENTIFIERS_UPDATEUSERREFID:
                return true;
            case IDENTIFIERS_IDENTIFIERID:
                return true;
            case IDENTIFIERS_IDENTIFIERSEQUENCE:
                return true;
            case IDENTIFIERS_SYSTEMREFID:
                return true;
            case IDENTIFIERS_INSERTDT:
                return true;
            case IDENTIFIERS_ACTIVEIND:
                return true;
            case IDENTIFIERS_IDVALUEINDEX:
                return true;
            case IDENTIFIERS_VERSION:
                return true;
            case IDENTIFIERS_IDVALUESOUND:
                return true;
            case IDENTIFIERS_CHECKDIGIT:
                return true;
            case IDENTIFIERS_UPDATEDT:
                return true;
            case IDENTIFIERS_INSERTUSERREFID:
                return true;
            case IDENTIFIERS_SYSTEMTRANSACTIONSEQ:
                return true;
            case IDENTIFIERS_SOURCEREFID:
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
                case IDENTIFIERS_IDVALUE:
                    return new ModelField("idvalue", "idvalue", IDENTIFIERS_IDVALUE, String.class);
                case IDENTIFIERS_DELETEDIND:
                    return new ModelField("deleted_ind", "deletedInd", IDENTIFIERS_DELETEDIND, Integer.class);
                case IDENTIFIERS_SYSTEMSEQUENCE:
                    return new ModelField("system_sequence", "systemSequence", IDENTIFIERS_SYSTEMSEQUENCE, Long.class);
                case IDENTIFIERS_UPDATEUSERREFID:
                    return new ModelField("update_user_ref_id", "updateUserRef", IDENTIFIERS_UPDATEUSERREFID, DisplayModel.class, UserReference.groupName());
                case IDENTIFIERS_IDENTIFIERID:
                    return new ModelField("identifier_id", "identifierId", IDENTIFIERS_IDENTIFIERID, Long.class);
                case IDENTIFIERS_IDENTIFIERSEQUENCE:
                    return new ModelField("identifier_sequence", "identifierSequence", IDENTIFIERS_IDENTIFIERSEQUENCE, Integer.class);
                case IDENTIFIERS_SYSTEMREFID:
                    return new ModelField("system_ref_id", "systemRef", IDENTIFIERS_SYSTEMREFID, DisplayModel.class, SystemReference.groupName());
                case IDENTIFIERS_INSERTDT:
                    return new ModelField("insert_dt", "insertDt", IDENTIFIERS_INSERTDT, DateTimeModel.class);
                case IDENTIFIERS_ACTIVEIND:
                    return new ModelField("active_ind", "activeInd", IDENTIFIERS_ACTIVEIND, Integer.class);
                case IDENTIFIERS_IDVALUEINDEX:
                    return new ModelField("idvalue_index", "idvalueIndex", IDENTIFIERS_IDVALUEINDEX, String.class);
                case IDENTIFIERS_VERSION:
                    return new ModelField("version", "version", IDENTIFIERS_VERSION, Integer.class);
                case IDENTIFIERS_IDVALUESOUND:
                    return new ModelField("idvalue_sound", "idvalueSound", IDENTIFIERS_IDVALUESOUND, String.class);
                case IDENTIFIERS_CHECKDIGIT:
                    return new ModelField("check_digit", "checkDigit", IDENTIFIERS_CHECKDIGIT, String.class);
                case IDENTIFIERS_UPDATEDT:
                    return new ModelField("update_dt", "updateDt", IDENTIFIERS_UPDATEDT, DateTimeModel.class);
                case IDENTIFIERS_INSERTUSERREFID:
                    return new ModelField("insert_user_ref_id", "insertUserRef", IDENTIFIERS_INSERTUSERREFID, DisplayModel.class, UserReference.groupName());
                case IDENTIFIERS_SYSTEMTRANSACTIONSEQ:
                    return new ModelField("system_transaction_seq", "systemTransactionSeq", IDENTIFIERS_SYSTEMTRANSACTIONSEQ, Long.class);
                case IDENTIFIERS_SOURCEREFID:
                    return new ModelField("source_ref_id", "sourceRef", IDENTIFIERS_SOURCEREFID, DisplayModel.class, IdentifierSourceReference.groupName());
                default:
                    return null;
            }
        } catch (Exception ex) {
            Log.exception(ex);
            return null;
        }
    }

    public static Class getModelClass() {
        return IdentifierModel.class;
    }

    /**
	 * @see com.patientis.model.common.IBaseModel#getModelField(java.lang.int)
	 */
    public static ModelField getModelField(int modelRefId) {
        try {
            switch(modelRefId) {
                case IDENTIFIERS_IDVALUE:
                    return new ModelField("idvalue", "idvalue", IDENTIFIERS_IDVALUE, String.class);
                case IDENTIFIERS_DELETEDIND:
                    return new ModelField("deleted_ind", "deletedInd", IDENTIFIERS_DELETEDIND, Integer.class);
                case IDENTIFIERS_SYSTEMSEQUENCE:
                    return new ModelField("system_sequence", "systemSequence", IDENTIFIERS_SYSTEMSEQUENCE, Long.class);
                case IDENTIFIERS_UPDATEUSERREFID:
                    return new ModelField("update_user_ref_id", "updateUserRef", IDENTIFIERS_UPDATEUSERREFID, DisplayModel.class, UserReference.groupName());
                case IDENTIFIERS_IDENTIFIERID:
                    return new ModelField("identifier_id", "identifierId", IDENTIFIERS_IDENTIFIERID, Long.class);
                case IDENTIFIERS_IDENTIFIERSEQUENCE:
                    return new ModelField("identifier_sequence", "identifierSequence", IDENTIFIERS_IDENTIFIERSEQUENCE, Integer.class);
                case IDENTIFIERS_SYSTEMREFID:
                    return new ModelField("system_ref_id", "systemRef", IDENTIFIERS_SYSTEMREFID, DisplayModel.class, SystemReference.groupName());
                case IDENTIFIERS_INSERTDT:
                    return new ModelField("insert_dt", "insertDt", IDENTIFIERS_INSERTDT, DateTimeModel.class);
                case IDENTIFIERS_ACTIVEIND:
                    return new ModelField("active_ind", "activeInd", IDENTIFIERS_ACTIVEIND, Integer.class);
                case IDENTIFIERS_IDVALUEINDEX:
                    return new ModelField("idvalue_index", "idvalueIndex", IDENTIFIERS_IDVALUEINDEX, String.class);
                case IDENTIFIERS_VERSION:
                    return new ModelField("version", "version", IDENTIFIERS_VERSION, Integer.class);
                case IDENTIFIERS_IDVALUESOUND:
                    return new ModelField("idvalue_sound", "idvalueSound", IDENTIFIERS_IDVALUESOUND, String.class);
                case IDENTIFIERS_CHECKDIGIT:
                    return new ModelField("check_digit", "checkDigit", IDENTIFIERS_CHECKDIGIT, String.class);
                case IDENTIFIERS_UPDATEDT:
                    return new ModelField("update_dt", "updateDt", IDENTIFIERS_UPDATEDT, DateTimeModel.class);
                case IDENTIFIERS_INSERTUSERREFID:
                    return new ModelField("insert_user_ref_id", "insertUserRef", IDENTIFIERS_INSERTUSERREFID, DisplayModel.class, UserReference.groupName());
                case IDENTIFIERS_SYSTEMTRANSACTIONSEQ:
                    return new ModelField("system_transaction_seq", "systemTransactionSeq", IDENTIFIERS_SYSTEMTRANSACTIONSEQ, Long.class);
                case IDENTIFIERS_SOURCEREFID:
                    return new ModelField("source_ref_id", "sourceRef", IDENTIFIERS_SOURCEREFID, DisplayModel.class, IdentifierSourceReference.groupName());
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
        fields.add(new ModelField("idvalue", "idvalue", IDENTIFIERS_IDVALUE, String.class));
        fields.add(new ModelField("deleted_ind", "deletedInd", IDENTIFIERS_DELETEDIND, Integer.class));
        fields.add(new ModelField("system_sequence", "systemSequence", IDENTIFIERS_SYSTEMSEQUENCE, Long.class));
        fields.add(new ModelField("update_user_ref_id", "updateUserRef", IDENTIFIERS_UPDATEUSERREFID, DisplayModel.class, UserReference.groupName()));
        fields.add(new ModelField("identifier_id", "identifierId", IDENTIFIERS_IDENTIFIERID, Long.class));
        fields.add(new ModelField("identifier_sequence", "identifierSequence", IDENTIFIERS_IDENTIFIERSEQUENCE, Integer.class));
        fields.add(new ModelField("system_ref_id", "systemRef", IDENTIFIERS_SYSTEMREFID, DisplayModel.class, SystemReference.groupName()));
        fields.add(new ModelField("insert_dt", "insertDt", IDENTIFIERS_INSERTDT, DateTimeModel.class));
        fields.add(new ModelField("active_ind", "activeInd", IDENTIFIERS_ACTIVEIND, Integer.class));
        fields.add(new ModelField("idvalue_index", "idvalueIndex", IDENTIFIERS_IDVALUEINDEX, String.class));
        fields.add(new ModelField("version", "version", IDENTIFIERS_VERSION, Integer.class));
        fields.add(new ModelField("idvalue_sound", "idvalueSound", IDENTIFIERS_IDVALUESOUND, String.class));
        fields.add(new ModelField("check_digit", "checkDigit", IDENTIFIERS_CHECKDIGIT, String.class));
        fields.add(new ModelField("update_dt", "updateDt", IDENTIFIERS_UPDATEDT, DateTimeModel.class));
        fields.add(new ModelField("insert_user_ref_id", "insertUserRef", IDENTIFIERS_INSERTUSERREFID, DisplayModel.class, UserReference.groupName()));
        fields.add(new ModelField("system_transaction_seq", "systemTransactionSeq", IDENTIFIERS_SYSTEMTRANSACTIONSEQ, Long.class));
        fields.add(new ModelField("source_ref_id", "sourceRef", IDENTIFIERS_SOURCEREFID, DisplayModel.class, IdentifierSourceReference.groupName()));
        return fields;
    }

    /**
	 * @see com.patientis.model.common.IBaseModel#getModelFields()
	 */
    public static java.util.List<ModelField> getModelFields() {
        java.util.List<ModelField> fields = new java.util.ArrayList<ModelField>();
        fields.add(new ModelField("idvalue", "idvalue", IDENTIFIERS_IDVALUE, String.class));
        fields.add(new ModelField("deleted_ind", "deletedInd", IDENTIFIERS_DELETEDIND, Integer.class));
        fields.add(new ModelField("system_sequence", "systemSequence", IDENTIFIERS_SYSTEMSEQUENCE, Long.class));
        fields.add(new ModelField("update_user_ref_id", "updateUserRef", IDENTIFIERS_UPDATEUSERREFID, DisplayModel.class, UserReference.groupName()));
        fields.add(new ModelField("identifier_id", "identifierId", IDENTIFIERS_IDENTIFIERID, Long.class));
        fields.add(new ModelField("identifier_sequence", "identifierSequence", IDENTIFIERS_IDENTIFIERSEQUENCE, Integer.class));
        fields.add(new ModelField("system_ref_id", "systemRef", IDENTIFIERS_SYSTEMREFID, DisplayModel.class, SystemReference.groupName()));
        fields.add(new ModelField("insert_dt", "insertDt", IDENTIFIERS_INSERTDT, DateTimeModel.class));
        fields.add(new ModelField("active_ind", "activeInd", IDENTIFIERS_ACTIVEIND, Integer.class));
        fields.add(new ModelField("idvalue_index", "idvalueIndex", IDENTIFIERS_IDVALUEINDEX, String.class));
        fields.add(new ModelField("version", "version", IDENTIFIERS_VERSION, Integer.class));
        fields.add(new ModelField("idvalue_sound", "idvalueSound", IDENTIFIERS_IDVALUESOUND, String.class));
        fields.add(new ModelField("check_digit", "checkDigit", IDENTIFIERS_CHECKDIGIT, String.class));
        fields.add(new ModelField("update_dt", "updateDt", IDENTIFIERS_UPDATEDT, DateTimeModel.class));
        fields.add(new ModelField("insert_user_ref_id", "insertUserRef", IDENTIFIERS_INSERTUSERREFID, DisplayModel.class, UserReference.groupName()));
        fields.add(new ModelField("system_transaction_seq", "systemTransactionSeq", IDENTIFIERS_SYSTEMTRANSACTIONSEQ, Long.class));
        fields.add(new ModelField("source_ref_id", "sourceRef", IDENTIFIERS_SOURCEREFID, DisplayModel.class, IdentifierSourceReference.groupName()));
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
        if (getSourceRef().getId() != 0) getSourceRef().setDisplay(reference.getDisplayValuePrimitive(getSourceRef().getId()), reference.getShortDisplayValuePrimitive(getSourceRef().getId()), 0L);
        if (getSystemRef().getId() != 0) getSystemRef().setDisplay(reference.getDisplayValuePrimitive(getSystemRef().getId()), reference.getShortDisplayValuePrimitive(getSystemRef().getId()), 0L);
        if (getUpdateUserRef().getId() != 0) getUpdateUserRef().setDisplay(reference.getDisplayValuePrimitive(getUpdateUserRef().getId()), reference.getShortDisplayValuePrimitive(getUpdateUserRef().getId()), 0L);
    }

    /**
	 * @see com.patientis.model.common.IBaseModel#setIndexFields(IIndexFields)
	 */
    public void setIndexFields(IIndexFields indexFields) {
        setIdvalueIndex(indexFields.createUppercaseIndex(getIdvalue()));
        setIdvalueSound(indexFields.createSoundIndex(getIdvalue()));
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
        if (getCheckDigit() != null && getCheckDigit().length() > 10) {
            throw new FieldTooLongException("CheckDigit");
        }
        if (getIdvalue() != null && getIdvalue().length() > 255) {
            throw new FieldTooLongException("Idvalue");
        }
        if (getIdvalueIndex() != null && getIdvalueIndex().length() > 255) {
            throw new FieldTooLongException("IdvalueIndex");
        }
        if (getIdvalueSound() != null && getIdvalueSound().length() > 255) {
            throw new FieldTooLongException("IdvalueSound");
        }
    }

    /**
	 * @see com.patientis.model.common.IBaseModel#isValid()
	 */
    public boolean isValid() {
        if (getCheckDigit() != null && getCheckDigit().length() > 10) {
            return false;
        }
        if (getIdvalue() != null && getIdvalue().length() > 255) {
            return false;
        }
        if (getIdvalueIndex() != null && getIdvalueIndex().length() > 255) {
            return false;
        }
        if (getIdvalueSound() != null && getIdvalueSound().length() > 255) {
            return false;
        }
        return true;
    }
}
