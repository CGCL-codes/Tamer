package com.patientis.model.clinical;

import com.patientis.framework.logging.Log;
import java.beans.PropertyChangeListener;
import com.patientis.ejb.reference.IGetReference;
import static com.patientis.model.common.ModelReference.*;
import com.patientis.framework.controls.exceptions.ISValidateControlException;
import com.patientis.model.common.*;
import com.patientis.model.reference.*;

/**
 * FormIdentifier
 * 
 */
public class FormIdentifierDataModel extends BaseModel {

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
	 * 
	 */
    private Long formId = 0L;

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
            firePropertyChange(String.valueOf(FORMIDENTIFIERS_FORMIDENTIFIERID), oldid, id);
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
            firePropertyChange(String.valueOf(FORMIDENTIFIERS_ACTIVEIND), oldactiveInd, activeInd);
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
            firePropertyChange(String.valueOf(FORMIDENTIFIERS_CHECKDIGIT), oldcheckDigit, checkDigit);
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
            firePropertyChange(String.valueOf(FORMIDENTIFIERS_DELETEDIND), olddeletedInd, deletedInd);
            if (deletedInd == 1) {
                setNotActive();
            }
        }
    }

    /**
	 * 
	 */
    public Long getFormId() {
        return this.formId;
    }

    /**
	 * 
	 */
    public void setFormId(Long formId) {
        if (!(this.formId.longValue() == formId.longValue())) {
            Long oldformId = 0L;
            oldformId = this.formId.longValue();
            this.formId = formId.longValue();
            setModified("formId");
            firePropertyChange(String.valueOf(FORMIDENTIFIERS_FORMID), oldformId, formId);
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
            firePropertyChange(String.valueOf(FORMIDENTIFIERS_IDENTIFIERSEQUENCE), oldidentifierSequence, identifierSequence);
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
            firePropertyChange(String.valueOf(FORMIDENTIFIERS_IDVALUE), oldidvalue, idvalue);
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
            firePropertyChange(String.valueOf(FORMIDENTIFIERS_IDVALUEINDEX), oldidvalueIndex, idvalueIndex);
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
            firePropertyChange(String.valueOf(FORMIDENTIFIERS_IDVALUESOUND), oldidvalueSound, idvalueSound);
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
            firePropertyChange(String.valueOf(FORMIDENTIFIERS_INSERTDT), oldinsertDt, insertDt);
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
            firePropertyChange(String.valueOf(FORMIDENTIFIERS_INSERTUSERREFID), oldinsertUserRef, insertUserRef);
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
            firePropertyChange(String.valueOf(FORMIDENTIFIERS_SOURCEREFID), oldsourceRef, sourceRef);
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
            firePropertyChange(String.valueOf(FORMIDENTIFIERS_SYSTEMREFID), oldsystemRef, systemRef);
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
            firePropertyChange(String.valueOf(FORMIDENTIFIERS_SYSTEMSEQUENCE), oldsystemSequence, systemSequence);
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
            firePropertyChange(String.valueOf(FORMIDENTIFIERS_SYSTEMTRANSACTIONSEQ), oldsystemTransactionSeq, systemTransactionSeq);
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
            firePropertyChange(String.valueOf(FORMIDENTIFIERS_UPDATEDT), oldupdateDt, updateDt);
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
            firePropertyChange(String.valueOf(FORMIDENTIFIERS_UPDATEUSERREFID), oldupdateUserRef, updateUserRef);
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
            firePropertyChange(String.valueOf(FORMIDENTIFIERS_VERSION), oldversion, version);
        }
    }

    /**
	 * @see com.patientis.model.common.IBaseModel#clear()
	 */
    public void clear() {
        copyFrom(new FormIdentifierDataModel(), false);
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
    public FormIdentifierModel createNewCopy() {
        FormIdentifierModel newCopy = new FormIdentifierModel();
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
        FormIdentifierDataModel m = (FormIdentifierDataModel) model;
        if (modifiedOnly && !m.isModified()) {
            return;
        }
        if (!modifiedOnly || m.isModified("activeInd")) setActiveInd(m.getActiveInd());
        if (!modifiedOnly || m.isModified("checkDigit")) setCheckDigit(m.getCheckDigit());
        if (!modifiedOnly || m.isModified("deletedInd")) setDeletedInd(m.getDeletedInd());
        if (!modifiedOnly || m.isModified("formId")) setFormId(m.getFormId());
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
        FormIdentifierDataModel m = (FormIdentifierDataModel) model;
        if (modifiedOnly && !m.isModified()) {
            return;
        }
        if (!modifiedOnly || m.isModified("activeInd")) setActiveInd(m.getActiveInd());
        if (!modifiedOnly || m.isModified("checkDigit")) setCheckDigit(m.getCheckDigit());
        if (!modifiedOnly || m.isModified("deletedInd")) setDeletedInd(m.getDeletedInd());
        if (!modifiedOnly || m.isModified("formId")) setFormId(m.getFormId());
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
                case FORMIDENTIFIERS_ACTIVEIND:
                    setActiveInd(Converter.convertInteger(value));
                    break;
                case FORMIDENTIFIERS_CHECKDIGIT:
                    setCheckDigit(Converter.convertString(value));
                    break;
                case FORMIDENTIFIERS_DELETEDIND:
                    setDeletedInd(Converter.convertInteger(value));
                    break;
                case FORMIDENTIFIERS_FORMID:
                    setFormId(Converter.convertLong(value));
                    break;
                case FORMIDENTIFIERS_IDENTIFIERSEQUENCE:
                    setIdentifierSequence(Converter.convertInteger(value));
                    break;
                case FORMIDENTIFIERS_IDVALUE:
                    setIdvalue(Converter.convertString(value));
                    break;
                case FORMIDENTIFIERS_IDVALUEINDEX:
                    setIdvalueIndex(Converter.convertString(value));
                    break;
                case FORMIDENTIFIERS_IDVALUESOUND:
                    setIdvalueSound(Converter.convertString(value));
                    break;
                case FORMIDENTIFIERS_INSERTDT:
                    setInsertDt(Converter.convertDateTimeModel(value));
                    break;
                case FORMIDENTIFIERS_INSERTUSERREFID:
                    setInsertUserRef(Converter.convertDisplayModel(value));
                    break;
                case FORMIDENTIFIERS_SOURCEREFID:
                    setSourceRef(Converter.convertDisplayModel(value));
                    break;
                case FORMIDENTIFIERS_SYSTEMREFID:
                    setSystemRef(Converter.convertDisplayModel(value));
                    break;
                case FORMIDENTIFIERS_SYSTEMSEQUENCE:
                    setSystemSequence(Converter.convertLong(value));
                    break;
                case FORMIDENTIFIERS_SYSTEMTRANSACTIONSEQ:
                    setSystemTransactionSeq(Converter.convertLong(value));
                    break;
                case FORMIDENTIFIERS_UPDATEDT:
                    setUpdateDt(Converter.convertDateTimeModel(value));
                    break;
                case FORMIDENTIFIERS_UPDATEUSERREFID:
                    setUpdateUserRef(Converter.convertDisplayModel(value));
                    break;
                case FORMIDENTIFIERS_VERSION:
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
            case FORMIDENTIFIERS_INSERTUSERREFID:
                return getInsertUserRef();
            case FORMIDENTIFIERS_FORMIDENTIFIERID:
                return getId();
            case FORMIDENTIFIERS_INSERTDT:
                return getInsertDt();
            case FORMIDENTIFIERS_VERSION:
                return getVersion();
            case FORMIDENTIFIERS_SYSTEMSEQUENCE:
                return getSystemSequence();
            case FORMIDENTIFIERS_SYSTEMREFID:
                return getSystemRef();
            case FORMIDENTIFIERS_FORMID:
                return getFormId();
            case FORMIDENTIFIERS_ACTIVEIND:
                return getActiveInd();
            case FORMIDENTIFIERS_DELETEDIND:
                return getDeletedInd();
            case FORMIDENTIFIERS_UPDATEDT:
                return getUpdateDt();
            case FORMIDENTIFIERS_CHECKDIGIT:
                return getCheckDigit();
            case FORMIDENTIFIERS_IDENTIFIERSEQUENCE:
                return getIdentifierSequence();
            case FORMIDENTIFIERS_SYSTEMTRANSACTIONSEQ:
                return getSystemTransactionSeq();
            case FORMIDENTIFIERS_IDVALUEINDEX:
                return getIdvalueIndex();
            case FORMIDENTIFIERS_IDVALUESOUND:
                return getIdvalueSound();
            case FORMIDENTIFIERS_IDVALUE:
                return getIdvalue();
            case FORMIDENTIFIERS_UPDATEUSERREFID:
                return getUpdateUserRef();
            case FORMIDENTIFIERS_SOURCEREFID:
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
            case FORMIDENTIFIERS_INSERTUSERREFID:
                return true;
            case FORMIDENTIFIERS_FORMIDENTIFIERID:
                return true;
            case FORMIDENTIFIERS_INSERTDT:
                return true;
            case FORMIDENTIFIERS_VERSION:
                return true;
            case FORMIDENTIFIERS_SYSTEMSEQUENCE:
                return true;
            case FORMIDENTIFIERS_SYSTEMREFID:
                return true;
            case FORMIDENTIFIERS_FORMID:
                return true;
            case FORMIDENTIFIERS_ACTIVEIND:
                return true;
            case FORMIDENTIFIERS_DELETEDIND:
                return true;
            case FORMIDENTIFIERS_UPDATEDT:
                return true;
            case FORMIDENTIFIERS_CHECKDIGIT:
                return true;
            case FORMIDENTIFIERS_IDENTIFIERSEQUENCE:
                return true;
            case FORMIDENTIFIERS_SYSTEMTRANSACTIONSEQ:
                return true;
            case FORMIDENTIFIERS_IDVALUEINDEX:
                return true;
            case FORMIDENTIFIERS_IDVALUESOUND:
                return true;
            case FORMIDENTIFIERS_IDVALUE:
                return true;
            case FORMIDENTIFIERS_UPDATEUSERREFID:
                return true;
            case FORMIDENTIFIERS_SOURCEREFID:
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
            case FORMIDENTIFIERS_INSERTUSERREFID:
                return true;
            case FORMIDENTIFIERS_FORMIDENTIFIERID:
                return true;
            case FORMIDENTIFIERS_INSERTDT:
                return true;
            case FORMIDENTIFIERS_VERSION:
                return true;
            case FORMIDENTIFIERS_SYSTEMSEQUENCE:
                return true;
            case FORMIDENTIFIERS_SYSTEMREFID:
                return true;
            case FORMIDENTIFIERS_FORMID:
                return true;
            case FORMIDENTIFIERS_ACTIVEIND:
                return true;
            case FORMIDENTIFIERS_DELETEDIND:
                return true;
            case FORMIDENTIFIERS_UPDATEDT:
                return true;
            case FORMIDENTIFIERS_CHECKDIGIT:
                return true;
            case FORMIDENTIFIERS_IDENTIFIERSEQUENCE:
                return true;
            case FORMIDENTIFIERS_SYSTEMTRANSACTIONSEQ:
                return true;
            case FORMIDENTIFIERS_IDVALUEINDEX:
                return true;
            case FORMIDENTIFIERS_IDVALUESOUND:
                return true;
            case FORMIDENTIFIERS_IDVALUE:
                return true;
            case FORMIDENTIFIERS_UPDATEUSERREFID:
                return true;
            case FORMIDENTIFIERS_SOURCEREFID:
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
                case FORMIDENTIFIERS_INSERTUSERREFID:
                    return new ModelField("insert_user_ref_id", "insertUserRef", FORMIDENTIFIERS_INSERTUSERREFID, DisplayModel.class, UserReference.groupName());
                case FORMIDENTIFIERS_FORMIDENTIFIERID:
                    return new ModelField("form_identifier_id", "formIdentifierId", FORMIDENTIFIERS_FORMIDENTIFIERID, Long.class);
                case FORMIDENTIFIERS_INSERTDT:
                    return new ModelField("insert_dt", "insertDt", FORMIDENTIFIERS_INSERTDT, DateTimeModel.class);
                case FORMIDENTIFIERS_VERSION:
                    return new ModelField("version", "version", FORMIDENTIFIERS_VERSION, Integer.class);
                case FORMIDENTIFIERS_SYSTEMSEQUENCE:
                    return new ModelField("system_sequence", "systemSequence", FORMIDENTIFIERS_SYSTEMSEQUENCE, Long.class);
                case FORMIDENTIFIERS_SYSTEMREFID:
                    return new ModelField("system_ref_id", "systemRef", FORMIDENTIFIERS_SYSTEMREFID, DisplayModel.class, SystemReference.groupName());
                case FORMIDENTIFIERS_FORMID:
                    return new ModelField("form_id", "formId", FORMIDENTIFIERS_FORMID, Long.class);
                case FORMIDENTIFIERS_ACTIVEIND:
                    return new ModelField("active_ind", "activeInd", FORMIDENTIFIERS_ACTIVEIND, Integer.class);
                case FORMIDENTIFIERS_DELETEDIND:
                    return new ModelField("deleted_ind", "deletedInd", FORMIDENTIFIERS_DELETEDIND, Integer.class);
                case FORMIDENTIFIERS_UPDATEDT:
                    return new ModelField("update_dt", "updateDt", FORMIDENTIFIERS_UPDATEDT, DateTimeModel.class);
                case FORMIDENTIFIERS_CHECKDIGIT:
                    return new ModelField("check_digit", "checkDigit", FORMIDENTIFIERS_CHECKDIGIT, String.class);
                case FORMIDENTIFIERS_IDENTIFIERSEQUENCE:
                    return new ModelField("identifier_sequence", "identifierSequence", FORMIDENTIFIERS_IDENTIFIERSEQUENCE, Integer.class);
                case FORMIDENTIFIERS_SYSTEMTRANSACTIONSEQ:
                    return new ModelField("system_transaction_seq", "systemTransactionSeq", FORMIDENTIFIERS_SYSTEMTRANSACTIONSEQ, Long.class);
                case FORMIDENTIFIERS_IDVALUEINDEX:
                    return new ModelField("idvalue_index", "idvalueIndex", FORMIDENTIFIERS_IDVALUEINDEX, String.class);
                case FORMIDENTIFIERS_IDVALUESOUND:
                    return new ModelField("idvalue_sound", "idvalueSound", FORMIDENTIFIERS_IDVALUESOUND, String.class);
                case FORMIDENTIFIERS_IDVALUE:
                    return new ModelField("idvalue", "idvalue", FORMIDENTIFIERS_IDVALUE, String.class);
                case FORMIDENTIFIERS_UPDATEUSERREFID:
                    return new ModelField("update_user_ref_id", "updateUserRef", FORMIDENTIFIERS_UPDATEUSERREFID, DisplayModel.class, UserReference.groupName());
                case FORMIDENTIFIERS_SOURCEREFID:
                    return new ModelField("source_ref_id", "sourceRef", FORMIDENTIFIERS_SOURCEREFID, DisplayModel.class, IdentifierSourceReference.groupName());
                default:
                    return null;
            }
        } catch (Exception ex) {
            Log.exception(ex);
            return null;
        }
    }

    public static Class getModelClass() {
        return FormIdentifierModel.class;
    }

    /**
	 * @see com.patientis.model.common.IBaseModel#getModelField(java.lang.int)
	 */
    public static ModelField getModelField(int modelRefId) {
        try {
            switch(modelRefId) {
                case FORMIDENTIFIERS_INSERTUSERREFID:
                    return new ModelField("insert_user_ref_id", "insertUserRef", FORMIDENTIFIERS_INSERTUSERREFID, DisplayModel.class, UserReference.groupName());
                case FORMIDENTIFIERS_FORMIDENTIFIERID:
                    return new ModelField("form_identifier_id", "formIdentifierId", FORMIDENTIFIERS_FORMIDENTIFIERID, Long.class);
                case FORMIDENTIFIERS_INSERTDT:
                    return new ModelField("insert_dt", "insertDt", FORMIDENTIFIERS_INSERTDT, DateTimeModel.class);
                case FORMIDENTIFIERS_VERSION:
                    return new ModelField("version", "version", FORMIDENTIFIERS_VERSION, Integer.class);
                case FORMIDENTIFIERS_SYSTEMSEQUENCE:
                    return new ModelField("system_sequence", "systemSequence", FORMIDENTIFIERS_SYSTEMSEQUENCE, Long.class);
                case FORMIDENTIFIERS_SYSTEMREFID:
                    return new ModelField("system_ref_id", "systemRef", FORMIDENTIFIERS_SYSTEMREFID, DisplayModel.class, SystemReference.groupName());
                case FORMIDENTIFIERS_FORMID:
                    return new ModelField("form_id", "formId", FORMIDENTIFIERS_FORMID, Long.class);
                case FORMIDENTIFIERS_ACTIVEIND:
                    return new ModelField("active_ind", "activeInd", FORMIDENTIFIERS_ACTIVEIND, Integer.class);
                case FORMIDENTIFIERS_DELETEDIND:
                    return new ModelField("deleted_ind", "deletedInd", FORMIDENTIFIERS_DELETEDIND, Integer.class);
                case FORMIDENTIFIERS_UPDATEDT:
                    return new ModelField("update_dt", "updateDt", FORMIDENTIFIERS_UPDATEDT, DateTimeModel.class);
                case FORMIDENTIFIERS_CHECKDIGIT:
                    return new ModelField("check_digit", "checkDigit", FORMIDENTIFIERS_CHECKDIGIT, String.class);
                case FORMIDENTIFIERS_IDENTIFIERSEQUENCE:
                    return new ModelField("identifier_sequence", "identifierSequence", FORMIDENTIFIERS_IDENTIFIERSEQUENCE, Integer.class);
                case FORMIDENTIFIERS_SYSTEMTRANSACTIONSEQ:
                    return new ModelField("system_transaction_seq", "systemTransactionSeq", FORMIDENTIFIERS_SYSTEMTRANSACTIONSEQ, Long.class);
                case FORMIDENTIFIERS_IDVALUEINDEX:
                    return new ModelField("idvalue_index", "idvalueIndex", FORMIDENTIFIERS_IDVALUEINDEX, String.class);
                case FORMIDENTIFIERS_IDVALUESOUND:
                    return new ModelField("idvalue_sound", "idvalueSound", FORMIDENTIFIERS_IDVALUESOUND, String.class);
                case FORMIDENTIFIERS_IDVALUE:
                    return new ModelField("idvalue", "idvalue", FORMIDENTIFIERS_IDVALUE, String.class);
                case FORMIDENTIFIERS_UPDATEUSERREFID:
                    return new ModelField("update_user_ref_id", "updateUserRef", FORMIDENTIFIERS_UPDATEUSERREFID, DisplayModel.class, UserReference.groupName());
                case FORMIDENTIFIERS_SOURCEREFID:
                    return new ModelField("source_ref_id", "sourceRef", FORMIDENTIFIERS_SOURCEREFID, DisplayModel.class, IdentifierSourceReference.groupName());
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
        fields.add(new ModelField("insert_user_ref_id", "insertUserRef", FORMIDENTIFIERS_INSERTUSERREFID, DisplayModel.class, UserReference.groupName()));
        fields.add(new ModelField("form_identifier_id", "formIdentifierId", FORMIDENTIFIERS_FORMIDENTIFIERID, Long.class));
        fields.add(new ModelField("insert_dt", "insertDt", FORMIDENTIFIERS_INSERTDT, DateTimeModel.class));
        fields.add(new ModelField("version", "version", FORMIDENTIFIERS_VERSION, Integer.class));
        fields.add(new ModelField("system_sequence", "systemSequence", FORMIDENTIFIERS_SYSTEMSEQUENCE, Long.class));
        fields.add(new ModelField("system_ref_id", "systemRef", FORMIDENTIFIERS_SYSTEMREFID, DisplayModel.class, SystemReference.groupName()));
        fields.add(new ModelField("form_id", "formId", FORMIDENTIFIERS_FORMID, Long.class));
        fields.add(new ModelField("active_ind", "activeInd", FORMIDENTIFIERS_ACTIVEIND, Integer.class));
        fields.add(new ModelField("deleted_ind", "deletedInd", FORMIDENTIFIERS_DELETEDIND, Integer.class));
        fields.add(new ModelField("update_dt", "updateDt", FORMIDENTIFIERS_UPDATEDT, DateTimeModel.class));
        fields.add(new ModelField("check_digit", "checkDigit", FORMIDENTIFIERS_CHECKDIGIT, String.class));
        fields.add(new ModelField("identifier_sequence", "identifierSequence", FORMIDENTIFIERS_IDENTIFIERSEQUENCE, Integer.class));
        fields.add(new ModelField("system_transaction_seq", "systemTransactionSeq", FORMIDENTIFIERS_SYSTEMTRANSACTIONSEQ, Long.class));
        fields.add(new ModelField("idvalue_index", "idvalueIndex", FORMIDENTIFIERS_IDVALUEINDEX, String.class));
        fields.add(new ModelField("idvalue_sound", "idvalueSound", FORMIDENTIFIERS_IDVALUESOUND, String.class));
        fields.add(new ModelField("idvalue", "idvalue", FORMIDENTIFIERS_IDVALUE, String.class));
        fields.add(new ModelField("update_user_ref_id", "updateUserRef", FORMIDENTIFIERS_UPDATEUSERREFID, DisplayModel.class, UserReference.groupName()));
        fields.add(new ModelField("source_ref_id", "sourceRef", FORMIDENTIFIERS_SOURCEREFID, DisplayModel.class, IdentifierSourceReference.groupName()));
        return fields;
    }

    /**
	 * @see com.patientis.model.common.IBaseModel#getModelFields()
	 */
    public static java.util.List<ModelField> getModelFields() {
        java.util.List<ModelField> fields = new java.util.ArrayList<ModelField>();
        fields.add(new ModelField("insert_user_ref_id", "insertUserRef", FORMIDENTIFIERS_INSERTUSERREFID, DisplayModel.class, UserReference.groupName()));
        fields.add(new ModelField("form_identifier_id", "formIdentifierId", FORMIDENTIFIERS_FORMIDENTIFIERID, Long.class));
        fields.add(new ModelField("insert_dt", "insertDt", FORMIDENTIFIERS_INSERTDT, DateTimeModel.class));
        fields.add(new ModelField("version", "version", FORMIDENTIFIERS_VERSION, Integer.class));
        fields.add(new ModelField("system_sequence", "systemSequence", FORMIDENTIFIERS_SYSTEMSEQUENCE, Long.class));
        fields.add(new ModelField("system_ref_id", "systemRef", FORMIDENTIFIERS_SYSTEMREFID, DisplayModel.class, SystemReference.groupName()));
        fields.add(new ModelField("form_id", "formId", FORMIDENTIFIERS_FORMID, Long.class));
        fields.add(new ModelField("active_ind", "activeInd", FORMIDENTIFIERS_ACTIVEIND, Integer.class));
        fields.add(new ModelField("deleted_ind", "deletedInd", FORMIDENTIFIERS_DELETEDIND, Integer.class));
        fields.add(new ModelField("update_dt", "updateDt", FORMIDENTIFIERS_UPDATEDT, DateTimeModel.class));
        fields.add(new ModelField("check_digit", "checkDigit", FORMIDENTIFIERS_CHECKDIGIT, String.class));
        fields.add(new ModelField("identifier_sequence", "identifierSequence", FORMIDENTIFIERS_IDENTIFIERSEQUENCE, Integer.class));
        fields.add(new ModelField("system_transaction_seq", "systemTransactionSeq", FORMIDENTIFIERS_SYSTEMTRANSACTIONSEQ, Long.class));
        fields.add(new ModelField("idvalue_index", "idvalueIndex", FORMIDENTIFIERS_IDVALUEINDEX, String.class));
        fields.add(new ModelField("idvalue_sound", "idvalueSound", FORMIDENTIFIERS_IDVALUESOUND, String.class));
        fields.add(new ModelField("idvalue", "idvalue", FORMIDENTIFIERS_IDVALUE, String.class));
        fields.add(new ModelField("update_user_ref_id", "updateUserRef", FORMIDENTIFIERS_UPDATEUSERREFID, DisplayModel.class, UserReference.groupName()));
        fields.add(new ModelField("source_ref_id", "sourceRef", FORMIDENTIFIERS_SOURCEREFID, DisplayModel.class, IdentifierSourceReference.groupName()));
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

    /**
	 * @see com.patientis.model.common.BaseModel#getReportXml(java.lang.String)
	 */
    @Override
    public String getReportXml(String modelType) {
        if (Converter.isNotEmpty(getSourceRef().getDisplay())) {
            return super.getReportXml(getSourceRef().getDisplay() + modelType);
        } else {
            return "";
        }
    }

    /**
	 * @see com.patientis.model.common.BaseModel#getReportXml(java.lang.String)
	 */
    @Override
    public String getReportFields(String modelType) {
        if (Converter.isNotEmpty(getSourceRef().getDisplay())) {
            return super.getReportFields(modelType + getSourceRef().getDisplay());
        } else {
            return "";
        }
    }

    /**
	 * Reference group for the defining type ref
	 */
    public String getSourceRefGroup() {
        return IdentifierSourceReference.groupName();
    }
}
