package com.patientis.model.common;

import com.patientis.framework.logging.Log;
import java.beans.PropertyChangeListener;
import com.patientis.ejb.reference.IGetReference;
import static com.patientis.model.common.ModelReference.*;
import com.patientis.framework.controls.exceptions.ISValidateControlException;
import com.patientis.model.reference.*;

/**
 * Address
 * 
 */
public class AddressDataModel extends BaseModel {

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
	 * Any errors associated with this address.
	 */
    private String addressError = null;

    /**
	 * Type of address.  Foreign key to reference on AddressType
	 */
    private DisplayModel addressTypeRef = new DisplayModel(this);

    /**
	 * City or town entered as free text
	 */
    private String cityFreeText = null;

    /**
	 * City or town.  Foreign key to reference on City
	 */
    private DisplayModel cityRef = new DisplayModel(this);

    /**
	 * Country entered as free text
	 */
    private String countryFreeText = null;

    /**
	 * Country.  Foreign key to reference on Country
	 */
    private DisplayModel countryRef = new DisplayModel(this);

    /**
	 * County or district entered as free text
	 */
    private String countyFreeText = null;

    /**
	 * County or district.  Foreign key to reference on County
	 */
    private DisplayModel countyRef = new DisplayModel(this);

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
	 * Free text note or comment for this address.
	 */
    private String notes = null;

    /**
	 * Second line of address
	 */
    private String otherDesignation = null;

    /**
	 * Postal code or zip code
	 */
    private String postalCode = null;

    /**
	 * State or province entered as free text
	 */
    private String stateFreeText = null;

    /**
	 * State or province.  Foreign key to reference on State
	 */
    private DisplayModel stateRef = new DisplayModel(this);

    /**
	 * Street or first line of address
	 */
    private String street = null;

    /**
	 * Identifies the system which originally created this row if reference
	 */
    private DisplayModel systemRef = new DisplayModel(this, 100091);

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
            firePropertyChange(String.valueOf(ADDRESSES_ADDRESSID), oldid, id);
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
            firePropertyChange(String.valueOf(ADDRESSES_ACTIVEIND), oldactiveInd, activeInd);
            if (activeInd == 1) {
                setNotDeleted();
            }
        }
    }

    /**
	 * Any errors associated with this address.
	 */
    public String getAddressError() {
        return this.addressError;
    }

    /**
	 * Any errors associated with this address.
	 */
    public void setAddressError(String addressError) {
        if (Converter.isDifferent(this.addressError, addressError)) {
            String oldaddressError = null;
            oldaddressError = this.addressError;
            this.addressError = addressError;
            setModified("addressError");
            firePropertyChange(String.valueOf(ADDRESSES_ADDRESSERROR), oldaddressError, addressError);
        }
    }

    /**
	 * Type of address.  Foreign key to reference on AddressType
	 */
    public DisplayModel getAddressTypeRef() {
        return this.addressTypeRef;
    }

    /**
	 * Type of address.  Foreign key to reference on AddressType
	 */
    public void setAddressTypeRef(DisplayModel addressTypeRef) {
        if (Converter.isDifferent(this.addressTypeRef, addressTypeRef)) {
            DisplayModel oldaddressTypeRef = new DisplayModel(this);
            oldaddressTypeRef.copyAllFrom(this.addressTypeRef);
            this.addressTypeRef.copyAllFrom(addressTypeRef);
            setModified("addressTypeRef");
            firePropertyChange(String.valueOf(ADDRESSES_ADDRESSTYPEREFID), oldaddressTypeRef, addressTypeRef);
        }
    }

    /**
	 * City or town entered as free text
	 */
    public String getCityFreeText() {
        return this.cityFreeText;
    }

    /**
	 * City or town entered as free text
	 */
    public void setCityFreeText(String cityFreeText) {
        if (Converter.isDifferent(this.cityFreeText, cityFreeText)) {
            String oldcityFreeText = null;
            oldcityFreeText = this.cityFreeText;
            this.cityFreeText = cityFreeText;
            setModified("cityFreeText");
            firePropertyChange(String.valueOf(ADDRESSES_CITYFREETEXT), oldcityFreeText, cityFreeText);
        }
    }

    /**
	 * City or town.  Foreign key to reference on City
	 */
    public DisplayModel getCityRef() {
        return this.cityRef;
    }

    /**
	 * City or town.  Foreign key to reference on City
	 */
    public void setCityRef(DisplayModel cityRef) {
        if (Converter.isDifferent(this.cityRef, cityRef)) {
            DisplayModel oldcityRef = new DisplayModel(this);
            oldcityRef.copyAllFrom(this.cityRef);
            this.cityRef.copyAllFrom(cityRef);
            setModified("cityRef");
            firePropertyChange(String.valueOf(ADDRESSES_CITYREFID), oldcityRef, cityRef);
        }
    }

    /**
	 * Country entered as free text
	 */
    public String getCountryFreeText() {
        return this.countryFreeText;
    }

    /**
	 * Country entered as free text
	 */
    public void setCountryFreeText(String countryFreeText) {
        if (Converter.isDifferent(this.countryFreeText, countryFreeText)) {
            String oldcountryFreeText = null;
            oldcountryFreeText = this.countryFreeText;
            this.countryFreeText = countryFreeText;
            setModified("countryFreeText");
            firePropertyChange(String.valueOf(ADDRESSES_COUNTRYFREETEXT), oldcountryFreeText, countryFreeText);
        }
    }

    /**
	 * Country.  Foreign key to reference on Country
	 */
    public DisplayModel getCountryRef() {
        return this.countryRef;
    }

    /**
	 * Country.  Foreign key to reference on Country
	 */
    public void setCountryRef(DisplayModel countryRef) {
        if (Converter.isDifferent(this.countryRef, countryRef)) {
            DisplayModel oldcountryRef = new DisplayModel(this);
            oldcountryRef.copyAllFrom(this.countryRef);
            this.countryRef.copyAllFrom(countryRef);
            setModified("countryRef");
            firePropertyChange(String.valueOf(ADDRESSES_COUNTRYREFID), oldcountryRef, countryRef);
        }
    }

    /**
	 * County or district entered as free text
	 */
    public String getCountyFreeText() {
        return this.countyFreeText;
    }

    /**
	 * County or district entered as free text
	 */
    public void setCountyFreeText(String countyFreeText) {
        if (Converter.isDifferent(this.countyFreeText, countyFreeText)) {
            String oldcountyFreeText = null;
            oldcountyFreeText = this.countyFreeText;
            this.countyFreeText = countyFreeText;
            setModified("countyFreeText");
            firePropertyChange(String.valueOf(ADDRESSES_COUNTYFREETEXT), oldcountyFreeText, countyFreeText);
        }
    }

    /**
	 * County or district.  Foreign key to reference on County
	 */
    public DisplayModel getCountyRef() {
        return this.countyRef;
    }

    /**
	 * County or district.  Foreign key to reference on County
	 */
    public void setCountyRef(DisplayModel countyRef) {
        if (Converter.isDifferent(this.countyRef, countyRef)) {
            DisplayModel oldcountyRef = new DisplayModel(this);
            oldcountyRef.copyAllFrom(this.countyRef);
            this.countyRef.copyAllFrom(countyRef);
            setModified("countyRef");
            firePropertyChange(String.valueOf(ADDRESSES_COUNTYREFID), oldcountyRef, countyRef);
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
            firePropertyChange(String.valueOf(ADDRESSES_DELETEDIND), olddeletedInd, deletedInd);
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
            firePropertyChange(String.valueOf(ADDRESSES_INSERTDT), oldinsertDt, insertDt);
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
            firePropertyChange(String.valueOf(ADDRESSES_INSERTUSERREFID), oldinsertUserRef, insertUserRef);
        }
    }

    /**
	 * Free text note or comment for this address.
	 */
    public String getNotes() {
        return this.notes;
    }

    /**
	 * Free text note or comment for this address.
	 */
    public void setNotes(String notes) {
        if (Converter.isDifferent(this.notes, notes)) {
            String oldnotes = null;
            oldnotes = this.notes;
            this.notes = notes;
            setModified("notes");
            firePropertyChange(String.valueOf(ADDRESSES_NOTES), oldnotes, notes);
        }
    }

    /**
	 * Second line of address
	 */
    public String getOtherDesignation() {
        return this.otherDesignation;
    }

    /**
	 * Second line of address
	 */
    public void setOtherDesignation(String otherDesignation) {
        if (Converter.isDifferent(this.otherDesignation, otherDesignation)) {
            String oldotherDesignation = null;
            oldotherDesignation = this.otherDesignation;
            this.otherDesignation = otherDesignation;
            setModified("otherDesignation");
            firePropertyChange(String.valueOf(ADDRESSES_OTHERDESIGNATION), oldotherDesignation, otherDesignation);
        }
    }

    /**
	 * Postal code or zip code
	 */
    public String getPostalCode() {
        return this.postalCode;
    }

    /**
	 * Postal code or zip code
	 */
    public void setPostalCode(String postalCode) {
        if (Converter.isDifferent(this.postalCode, postalCode)) {
            String oldpostalCode = null;
            oldpostalCode = this.postalCode;
            this.postalCode = postalCode;
            setModified("postalCode");
            firePropertyChange(String.valueOf(ADDRESSES_POSTALCODE), oldpostalCode, postalCode);
        }
    }

    /**
	 * State or province entered as free text
	 */
    public String getStateFreeText() {
        return this.stateFreeText;
    }

    /**
	 * State or province entered as free text
	 */
    public void setStateFreeText(String stateFreeText) {
        if (Converter.isDifferent(this.stateFreeText, stateFreeText)) {
            String oldstateFreeText = null;
            oldstateFreeText = this.stateFreeText;
            this.stateFreeText = stateFreeText;
            setModified("stateFreeText");
            firePropertyChange(String.valueOf(ADDRESSES_STATEFREETEXT), oldstateFreeText, stateFreeText);
        }
    }

    /**
	 * State or province.  Foreign key to reference on State
	 */
    public DisplayModel getStateRef() {
        return this.stateRef;
    }

    /**
	 * State or province.  Foreign key to reference on State
	 */
    public void setStateRef(DisplayModel stateRef) {
        if (Converter.isDifferent(this.stateRef, stateRef)) {
            DisplayModel oldstateRef = new DisplayModel(this);
            oldstateRef.copyAllFrom(this.stateRef);
            this.stateRef.copyAllFrom(stateRef);
            setModified("stateRef");
            firePropertyChange(String.valueOf(ADDRESSES_STATEREFID), oldstateRef, stateRef);
        }
    }

    /**
	 * Street or first line of address
	 */
    public String getStreet() {
        return this.street;
    }

    /**
	 * Street or first line of address
	 */
    public void setStreet(String street) {
        if (Converter.isDifferent(this.street, street)) {
            String oldstreet = null;
            oldstreet = this.street;
            this.street = street;
            setModified("street");
            firePropertyChange(String.valueOf(ADDRESSES_STREET), oldstreet, street);
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
            DisplayModel oldsystemRef = new DisplayModel(this, 100091);
            oldsystemRef.copyAllFrom(this.systemRef);
            this.systemRef.copyAllFrom(systemRef);
            setModified("systemRef");
            firePropertyChange(String.valueOf(ADDRESSES_SYSTEMREFID), oldsystemRef, systemRef);
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
        if (!(this.systemSequence == systemSequence)) {
            Long oldsystemSequence = 0L;
            oldsystemSequence = this.systemSequence.longValue();
            this.systemSequence = systemSequence.longValue();
            setModified("systemSequence");
            firePropertyChange(String.valueOf(ADDRESSES_SYSTEMSEQUENCE), oldsystemSequence, systemSequence);
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
        if (!(this.systemTransactionSeq == systemTransactionSeq)) {
            Long oldsystemTransactionSeq = 0L;
            oldsystemTransactionSeq = this.systemTransactionSeq.longValue();
            this.systemTransactionSeq = systemTransactionSeq.longValue();
            setModified("systemTransactionSeq");
            firePropertyChange(String.valueOf(ADDRESSES_SYSTEMTRANSACTIONSEQ), oldsystemTransactionSeq, systemTransactionSeq);
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
            firePropertyChange(String.valueOf(ADDRESSES_UPDATEDT), oldupdateDt, updateDt);
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
            firePropertyChange(String.valueOf(ADDRESSES_UPDATEUSERREFID), oldupdateUserRef, updateUserRef);
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
            firePropertyChange(String.valueOf(ADDRESSES_VERSION), oldversion, version);
        }
    }

    /**
	 * @see com.patientis.model.common.IBaseModel#clear()
	 */
    public void clear() {
        copyFrom(new AddressDataModel(), false);
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
    public AddressModel createNewCopy() {
        AddressModel newCopy = new AddressModel();
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
        AddressDataModel m = (AddressDataModel) model;
        if (modifiedOnly && !m.isModified()) {
            return;
        }
        if (!modifiedOnly || m.isModified("activeInd")) setActiveInd(m.getActiveInd());
        if (!modifiedOnly || m.isModified("addressError")) setAddressError(m.getAddressError());
        if (!modifiedOnly || m.isModified("addressTypeRef")) setAddressTypeRef(m.getAddressTypeRef());
        if (!modifiedOnly || m.isModified("cityFreeText")) setCityFreeText(m.getCityFreeText());
        if (!modifiedOnly || m.isModified("cityRef")) setCityRef(m.getCityRef());
        if (!modifiedOnly || m.isModified("countryFreeText")) setCountryFreeText(m.getCountryFreeText());
        if (!modifiedOnly || m.isModified("countryRef")) setCountryRef(m.getCountryRef());
        if (!modifiedOnly || m.isModified("countyFreeText")) setCountyFreeText(m.getCountyFreeText());
        if (!modifiedOnly || m.isModified("countyRef")) setCountyRef(m.getCountyRef());
        if (!modifiedOnly || m.isModified("deletedInd")) setDeletedInd(m.getDeletedInd());
        if (!modifiedOnly || m.isModified("insertDt")) setInsertDt(m.getInsertDt());
        if (!modifiedOnly || m.isModified("insertUserRef")) setInsertUserRef(m.getInsertUserRef());
        if (!modifiedOnly || m.isModified("notes")) setNotes(m.getNotes());
        if (!modifiedOnly || m.isModified("otherDesignation")) setOtherDesignation(m.getOtherDesignation());
        if (!modifiedOnly || m.isModified("postalCode")) setPostalCode(m.getPostalCode());
        if (!modifiedOnly || m.isModified("stateFreeText")) setStateFreeText(m.getStateFreeText());
        if (!modifiedOnly || m.isModified("stateRef")) setStateRef(m.getStateRef());
        if (!modifiedOnly || m.isModified("street")) setStreet(m.getStreet());
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
        AddressDataModel m = (AddressDataModel) model;
        if (modifiedOnly && !m.isModified()) {
            return;
        }
        if (!modifiedOnly || m.isModified("activeInd")) setActiveInd(m.getActiveInd());
        if (!modifiedOnly || m.isModified("addressError")) setAddressError(m.getAddressError());
        if (!modifiedOnly || m.isModified("addressTypeRef")) setAddressTypeRef(m.getAddressTypeRef());
        if (!modifiedOnly || m.isModified("cityFreeText")) setCityFreeText(m.getCityFreeText());
        if (!modifiedOnly || m.isModified("cityRef")) setCityRef(m.getCityRef());
        if (!modifiedOnly || m.isModified("countryFreeText")) setCountryFreeText(m.getCountryFreeText());
        if (!modifiedOnly || m.isModified("countryRef")) setCountryRef(m.getCountryRef());
        if (!modifiedOnly || m.isModified("countyFreeText")) setCountyFreeText(m.getCountyFreeText());
        if (!modifiedOnly || m.isModified("countyRef")) setCountyRef(m.getCountyRef());
        if (!modifiedOnly || m.isModified("deletedInd")) setDeletedInd(m.getDeletedInd());
        if (!modifiedOnly || m.isModified("insertDt")) setInsertDt(m.getInsertDt());
        if (!modifiedOnly || m.isModified("insertUserRef")) setInsertUserRef(m.getInsertUserRef());
        if (!modifiedOnly || m.isModified("notes")) setNotes(m.getNotes());
        if (!modifiedOnly || m.isModified("otherDesignation")) setOtherDesignation(m.getOtherDesignation());
        if (!modifiedOnly || m.isModified("postalCode")) setPostalCode(m.getPostalCode());
        if (!modifiedOnly || m.isModified("stateFreeText")) setStateFreeText(m.getStateFreeText());
        if (!modifiedOnly || m.isModified("stateRef")) setStateRef(m.getStateRef());
        if (!modifiedOnly || m.isModified("street")) setStreet(m.getStreet());
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
                case ADDRESSES_ACTIVEIND:
                    setActiveInd(Converter.convertInteger(value));
                    break;
                case ADDRESSES_ADDRESSERROR:
                    setAddressError(Converter.convertString(value));
                    break;
                case ADDRESSES_ADDRESSTYPEREFID:
                    setAddressTypeRef(Converter.convertDisplayModel(value));
                    break;
                case ADDRESSES_CITYFREETEXT:
                    setCityFreeText(Converter.convertString(value));
                    break;
                case ADDRESSES_CITYREFID:
                    setCityRef(Converter.convertDisplayModel(value));
                    break;
                case ADDRESSES_COUNTRYFREETEXT:
                    setCountryFreeText(Converter.convertString(value));
                    break;
                case ADDRESSES_COUNTRYREFID:
                    setCountryRef(Converter.convertDisplayModel(value));
                    break;
                case ADDRESSES_COUNTYFREETEXT:
                    setCountyFreeText(Converter.convertString(value));
                    break;
                case ADDRESSES_COUNTYREFID:
                    setCountyRef(Converter.convertDisplayModel(value));
                    break;
                case ADDRESSES_DELETEDIND:
                    setDeletedInd(Converter.convertInteger(value));
                    break;
                case ADDRESSES_INSERTDT:
                    setInsertDt(Converter.convertDateTimeModel(value));
                    break;
                case ADDRESSES_INSERTUSERREFID:
                    setInsertUserRef(Converter.convertDisplayModel(value));
                    break;
                case ADDRESSES_NOTES:
                    setNotes(Converter.convertString(value));
                    break;
                case ADDRESSES_OTHERDESIGNATION:
                    setOtherDesignation(Converter.convertString(value));
                    break;
                case ADDRESSES_POSTALCODE:
                    setPostalCode(Converter.convertString(value));
                    break;
                case ADDRESSES_STATEFREETEXT:
                    setStateFreeText(Converter.convertString(value));
                    break;
                case ADDRESSES_STATEREFID:
                    setStateRef(Converter.convertDisplayModel(value));
                    break;
                case ADDRESSES_STREET:
                    setStreet(Converter.convertString(value));
                    break;
                case ADDRESSES_SYSTEMREFID:
                    setSystemRef(Converter.convertDisplayModel(value));
                    break;
                case ADDRESSES_SYSTEMSEQUENCE:
                    setSystemSequence(Converter.convertLong(value));
                    break;
                case ADDRESSES_SYSTEMTRANSACTIONSEQ:
                    setSystemTransactionSeq(Converter.convertLong(value));
                    break;
                case ADDRESSES_UPDATEDT:
                    setUpdateDt(Converter.convertDateTimeModel(value));
                    break;
                case ADDRESSES_UPDATEUSERREFID:
                    setUpdateUserRef(Converter.convertDisplayModel(value));
                    break;
                case ADDRESSES_VERSION:
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
            case ADDRESSES_UPDATEDT:
                return getUpdateDt();
            case ADDRESSES_CITYREFID:
                return getCityRef();
            case ADDRESSES_UPDATEUSERREFID:
                return getUpdateUserRef();
            case ADDRESSES_NOTES:
                return getNotes();
            case ADDRESSES_VERSION:
                return getVersion();
            case ADDRESSES_CITYFREETEXT:
                return getCityFreeText();
            case ADDRESSES_COUNTRYFREETEXT:
                return getCountryFreeText();
            case ADDRESSES_OTHERDESIGNATION:
                return getOtherDesignation();
            case ADDRESSES_COUNTRYREFID:
                return getCountryRef();
            case ADDRESSES_COUNTYFREETEXT:
                return getCountyFreeText();
            case ADDRESSES_COUNTYREFID:
                return getCountyRef();
            case ADDRESSES_POSTALCODE:
                return getPostalCode();
            case ADDRESSES_ACTIVEIND:
                return getActiveInd();
            case ADDRESSES_DELETEDIND:
                return getDeletedInd();
            case ADDRESSES_STATEFREETEXT:
                return getStateFreeText();
            case ADDRESSES_STATEREFID:
                return getStateRef();
            case ADDRESSES_STREET:
                return getStreet();
            case ADDRESSES_SYSTEMREFID:
                return getSystemRef();
            case ADDRESSES_INSERTDT:
                return getInsertDt();
            case ADDRESSES_INSERTUSERREFID:
                return getInsertUserRef();
            case ADDRESSES_SYSTEMSEQUENCE:
                return getSystemSequence();
            case ADDRESSES_SYSTEMTRANSACTIONSEQ:
                return getSystemTransactionSeq();
            case ADDRESSES_ADDRESSID:
                return getId();
            case ADDRESSES_ADDRESSERROR:
                return getAddressError();
            case ADDRESSES_ADDRESSTYPEREFID:
                return getAddressTypeRef();
            default:
                return super.getValue(modelRefId);
        }
    }

    /**
	 * @see com.patientis.model.common.IBaseModel#hasField(java.lang.int)
	 */
    public boolean hasField(int modelRefId) {
        switch(modelRefId) {
            case ADDRESSES_UPDATEDT:
                return true;
            case ADDRESSES_CITYREFID:
                return true;
            case ADDRESSES_UPDATEUSERREFID:
                return true;
            case ADDRESSES_NOTES:
                return true;
            case ADDRESSES_VERSION:
                return true;
            case ADDRESSES_CITYFREETEXT:
                return true;
            case ADDRESSES_COUNTRYFREETEXT:
                return true;
            case ADDRESSES_OTHERDESIGNATION:
                return true;
            case ADDRESSES_COUNTRYREFID:
                return true;
            case ADDRESSES_COUNTYFREETEXT:
                return true;
            case ADDRESSES_COUNTYREFID:
                return true;
            case ADDRESSES_POSTALCODE:
                return true;
            case ADDRESSES_ACTIVEIND:
                return true;
            case ADDRESSES_DELETEDIND:
                return true;
            case ADDRESSES_STATEFREETEXT:
                return true;
            case ADDRESSES_STATEREFID:
                return true;
            case ADDRESSES_STREET:
                return true;
            case ADDRESSES_SYSTEMREFID:
                return true;
            case ADDRESSES_INSERTDT:
                return true;
            case ADDRESSES_INSERTUSERREFID:
                return true;
            case ADDRESSES_SYSTEMSEQUENCE:
                return true;
            case ADDRESSES_SYSTEMTRANSACTIONSEQ:
                return true;
            case ADDRESSES_ADDRESSID:
                return true;
            case ADDRESSES_ADDRESSERROR:
                return true;
            case ADDRESSES_ADDRESSTYPEREFID:
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
            case ADDRESSES_UPDATEDT:
                return true;
            case ADDRESSES_CITYREFID:
                return true;
            case ADDRESSES_UPDATEUSERREFID:
                return true;
            case ADDRESSES_NOTES:
                return true;
            case ADDRESSES_VERSION:
                return true;
            case ADDRESSES_CITYFREETEXT:
                return true;
            case ADDRESSES_COUNTRYFREETEXT:
                return true;
            case ADDRESSES_OTHERDESIGNATION:
                return true;
            case ADDRESSES_COUNTRYREFID:
                return true;
            case ADDRESSES_COUNTYFREETEXT:
                return true;
            case ADDRESSES_COUNTYREFID:
                return true;
            case ADDRESSES_POSTALCODE:
                return true;
            case ADDRESSES_ACTIVEIND:
                return true;
            case ADDRESSES_DELETEDIND:
                return true;
            case ADDRESSES_STATEFREETEXT:
                return true;
            case ADDRESSES_STATEREFID:
                return true;
            case ADDRESSES_STREET:
                return true;
            case ADDRESSES_SYSTEMREFID:
                return true;
            case ADDRESSES_INSERTDT:
                return true;
            case ADDRESSES_INSERTUSERREFID:
                return true;
            case ADDRESSES_SYSTEMSEQUENCE:
                return true;
            case ADDRESSES_SYSTEMTRANSACTIONSEQ:
                return true;
            case ADDRESSES_ADDRESSID:
                return true;
            case ADDRESSES_ADDRESSERROR:
                return true;
            case ADDRESSES_ADDRESSTYPEREFID:
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
                case ADDRESSES_UPDATEDT:
                    return new ModelField("update_dt", "updateDt", ADDRESSES_UPDATEDT, DateTimeModel.class);
                case ADDRESSES_CITYREFID:
                    return new ModelField("city_ref_id", "cityRef", ADDRESSES_CITYREFID, DisplayModel.class, CityReference.groupName());
                case ADDRESSES_UPDATEUSERREFID:
                    return new ModelField("update_user_ref_id", "updateUserRef", ADDRESSES_UPDATEUSERREFID, DisplayModel.class, UserReference.groupName());
                case ADDRESSES_NOTES:
                    return new ModelField("notes", "notes", ADDRESSES_NOTES, String.class);
                case ADDRESSES_VERSION:
                    return new ModelField("version", "version", ADDRESSES_VERSION, Integer.class);
                case ADDRESSES_CITYFREETEXT:
                    return new ModelField("city_free_text", "cityFreeText", ADDRESSES_CITYFREETEXT, String.class);
                case ADDRESSES_COUNTRYFREETEXT:
                    return new ModelField("country_free_text", "countryFreeText", ADDRESSES_COUNTRYFREETEXT, String.class);
                case ADDRESSES_OTHERDESIGNATION:
                    return new ModelField("other_designation", "otherDesignation", ADDRESSES_OTHERDESIGNATION, String.class);
                case ADDRESSES_COUNTRYREFID:
                    return new ModelField("country_ref_id", "countryRef", ADDRESSES_COUNTRYREFID, DisplayModel.class, CountryReference.groupName());
                case ADDRESSES_COUNTYFREETEXT:
                    return new ModelField("county_free_text", "countyFreeText", ADDRESSES_COUNTYFREETEXT, String.class);
                case ADDRESSES_COUNTYREFID:
                    return new ModelField("county_ref_id", "countyRef", ADDRESSES_COUNTYREFID, DisplayModel.class, CountyReference.groupName());
                case ADDRESSES_POSTALCODE:
                    return new ModelField("postal_code", "postalCode", ADDRESSES_POSTALCODE, String.class);
                case ADDRESSES_ACTIVEIND:
                    return new ModelField("active_ind", "activeInd", ADDRESSES_ACTIVEIND, Integer.class);
                case ADDRESSES_DELETEDIND:
                    return new ModelField("deleted_ind", "deletedInd", ADDRESSES_DELETEDIND, Integer.class);
                case ADDRESSES_STATEFREETEXT:
                    return new ModelField("state_free_text", "stateFreeText", ADDRESSES_STATEFREETEXT, String.class);
                case ADDRESSES_STATEREFID:
                    return new ModelField("state_ref_id", "stateRef", ADDRESSES_STATEREFID, DisplayModel.class, StateReference.groupName());
                case ADDRESSES_STREET:
                    return new ModelField("street", "street", ADDRESSES_STREET, String.class);
                case ADDRESSES_SYSTEMREFID:
                    return new ModelField("system_ref_id", "systemRef", ADDRESSES_SYSTEMREFID, DisplayModel.class, SystemReference.groupName());
                case ADDRESSES_INSERTDT:
                    return new ModelField("insert_dt", "insertDt", ADDRESSES_INSERTDT, DateTimeModel.class);
                case ADDRESSES_INSERTUSERREFID:
                    return new ModelField("insert_user_ref_id", "insertUserRef", ADDRESSES_INSERTUSERREFID, DisplayModel.class, UserReference.groupName());
                case ADDRESSES_SYSTEMSEQUENCE:
                    return new ModelField("system_sequence", "systemSequence", ADDRESSES_SYSTEMSEQUENCE, Long.class);
                case ADDRESSES_SYSTEMTRANSACTIONSEQ:
                    return new ModelField("system_transaction_seq", "systemTransactionSeq", ADDRESSES_SYSTEMTRANSACTIONSEQ, Long.class);
                case ADDRESSES_ADDRESSID:
                    return new ModelField("address_id", "addressId", ADDRESSES_ADDRESSID, Long.class);
                case ADDRESSES_ADDRESSERROR:
                    return new ModelField("address_error", "addressError", ADDRESSES_ADDRESSERROR, String.class);
                case ADDRESSES_ADDRESSTYPEREFID:
                    return new ModelField("address_type_ref_id", "addressTypeRef", ADDRESSES_ADDRESSTYPEREFID, DisplayModel.class, AddressTypeReference.groupName());
                default:
                    return null;
            }
        } catch (Exception ex) {
            Log.exception(ex);
            return null;
        }
    }

    public static Class getModelClass() {
        return AddressModel.class;
    }

    /**
	 * @see com.patientis.model.common.IBaseModel#getModelField(java.lang.int)
	 */
    public static ModelField getModelField(int modelRefId) {
        try {
            switch(modelRefId) {
                case ADDRESSES_UPDATEDT:
                    return new ModelField("update_dt", "updateDt", ADDRESSES_UPDATEDT, DateTimeModel.class);
                case ADDRESSES_CITYREFID:
                    return new ModelField("city_ref_id", "cityRef", ADDRESSES_CITYREFID, DisplayModel.class, CityReference.groupName());
                case ADDRESSES_UPDATEUSERREFID:
                    return new ModelField("update_user_ref_id", "updateUserRef", ADDRESSES_UPDATEUSERREFID, DisplayModel.class, UserReference.groupName());
                case ADDRESSES_NOTES:
                    return new ModelField("notes", "notes", ADDRESSES_NOTES, String.class);
                case ADDRESSES_VERSION:
                    return new ModelField("version", "version", ADDRESSES_VERSION, Integer.class);
                case ADDRESSES_CITYFREETEXT:
                    return new ModelField("city_free_text", "cityFreeText", ADDRESSES_CITYFREETEXT, String.class);
                case ADDRESSES_COUNTRYFREETEXT:
                    return new ModelField("country_free_text", "countryFreeText", ADDRESSES_COUNTRYFREETEXT, String.class);
                case ADDRESSES_OTHERDESIGNATION:
                    return new ModelField("other_designation", "otherDesignation", ADDRESSES_OTHERDESIGNATION, String.class);
                case ADDRESSES_COUNTRYREFID:
                    return new ModelField("country_ref_id", "countryRef", ADDRESSES_COUNTRYREFID, DisplayModel.class, CountryReference.groupName());
                case ADDRESSES_COUNTYFREETEXT:
                    return new ModelField("county_free_text", "countyFreeText", ADDRESSES_COUNTYFREETEXT, String.class);
                case ADDRESSES_COUNTYREFID:
                    return new ModelField("county_ref_id", "countyRef", ADDRESSES_COUNTYREFID, DisplayModel.class, CountyReference.groupName());
                case ADDRESSES_POSTALCODE:
                    return new ModelField("postal_code", "postalCode", ADDRESSES_POSTALCODE, String.class);
                case ADDRESSES_ACTIVEIND:
                    return new ModelField("active_ind", "activeInd", ADDRESSES_ACTIVEIND, Integer.class);
                case ADDRESSES_DELETEDIND:
                    return new ModelField("deleted_ind", "deletedInd", ADDRESSES_DELETEDIND, Integer.class);
                case ADDRESSES_STATEFREETEXT:
                    return new ModelField("state_free_text", "stateFreeText", ADDRESSES_STATEFREETEXT, String.class);
                case ADDRESSES_STATEREFID:
                    return new ModelField("state_ref_id", "stateRef", ADDRESSES_STATEREFID, DisplayModel.class, StateReference.groupName());
                case ADDRESSES_STREET:
                    return new ModelField("street", "street", ADDRESSES_STREET, String.class);
                case ADDRESSES_SYSTEMREFID:
                    return new ModelField("system_ref_id", "systemRef", ADDRESSES_SYSTEMREFID, DisplayModel.class, SystemReference.groupName());
                case ADDRESSES_INSERTDT:
                    return new ModelField("insert_dt", "insertDt", ADDRESSES_INSERTDT, DateTimeModel.class);
                case ADDRESSES_INSERTUSERREFID:
                    return new ModelField("insert_user_ref_id", "insertUserRef", ADDRESSES_INSERTUSERREFID, DisplayModel.class, UserReference.groupName());
                case ADDRESSES_SYSTEMSEQUENCE:
                    return new ModelField("system_sequence", "systemSequence", ADDRESSES_SYSTEMSEQUENCE, Long.class);
                case ADDRESSES_SYSTEMTRANSACTIONSEQ:
                    return new ModelField("system_transaction_seq", "systemTransactionSeq", ADDRESSES_SYSTEMTRANSACTIONSEQ, Long.class);
                case ADDRESSES_ADDRESSID:
                    return new ModelField("address_id", "addressId", ADDRESSES_ADDRESSID, Long.class);
                case ADDRESSES_ADDRESSERROR:
                    return new ModelField("address_error", "addressError", ADDRESSES_ADDRESSERROR, String.class);
                case ADDRESSES_ADDRESSTYPEREFID:
                    return new ModelField("address_type_ref_id", "addressTypeRef", ADDRESSES_ADDRESSTYPEREFID, DisplayModel.class, AddressTypeReference.groupName());
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
        fields.add(new ModelField("update_dt", "updateDt", ADDRESSES_UPDATEDT, DateTimeModel.class));
        fields.add(new ModelField("city_ref_id", "cityRef", ADDRESSES_CITYREFID, DisplayModel.class, CityReference.groupName()));
        fields.add(new ModelField("update_user_ref_id", "updateUserRef", ADDRESSES_UPDATEUSERREFID, DisplayModel.class, UserReference.groupName()));
        fields.add(new ModelField("notes", "notes", ADDRESSES_NOTES, String.class));
        fields.add(new ModelField("version", "version", ADDRESSES_VERSION, Integer.class));
        fields.add(new ModelField("city_free_text", "cityFreeText", ADDRESSES_CITYFREETEXT, String.class));
        fields.add(new ModelField("country_free_text", "countryFreeText", ADDRESSES_COUNTRYFREETEXT, String.class));
        fields.add(new ModelField("other_designation", "otherDesignation", ADDRESSES_OTHERDESIGNATION, String.class));
        fields.add(new ModelField("country_ref_id", "countryRef", ADDRESSES_COUNTRYREFID, DisplayModel.class, CountryReference.groupName()));
        fields.add(new ModelField("county_free_text", "countyFreeText", ADDRESSES_COUNTYFREETEXT, String.class));
        fields.add(new ModelField("county_ref_id", "countyRef", ADDRESSES_COUNTYREFID, DisplayModel.class, CountyReference.groupName()));
        fields.add(new ModelField("postal_code", "postalCode", ADDRESSES_POSTALCODE, String.class));
        fields.add(new ModelField("active_ind", "activeInd", ADDRESSES_ACTIVEIND, Integer.class));
        fields.add(new ModelField("deleted_ind", "deletedInd", ADDRESSES_DELETEDIND, Integer.class));
        fields.add(new ModelField("state_free_text", "stateFreeText", ADDRESSES_STATEFREETEXT, String.class));
        fields.add(new ModelField("state_ref_id", "stateRef", ADDRESSES_STATEREFID, DisplayModel.class, StateReference.groupName()));
        fields.add(new ModelField("street", "street", ADDRESSES_STREET, String.class));
        fields.add(new ModelField("system_ref_id", "systemRef", ADDRESSES_SYSTEMREFID, DisplayModel.class, SystemReference.groupName()));
        fields.add(new ModelField("insert_dt", "insertDt", ADDRESSES_INSERTDT, DateTimeModel.class));
        fields.add(new ModelField("insert_user_ref_id", "insertUserRef", ADDRESSES_INSERTUSERREFID, DisplayModel.class, UserReference.groupName()));
        fields.add(new ModelField("system_sequence", "systemSequence", ADDRESSES_SYSTEMSEQUENCE, Long.class));
        fields.add(new ModelField("system_transaction_seq", "systemTransactionSeq", ADDRESSES_SYSTEMTRANSACTIONSEQ, Long.class));
        fields.add(new ModelField("address_id", "addressId", ADDRESSES_ADDRESSID, Long.class));
        fields.add(new ModelField("address_error", "addressError", ADDRESSES_ADDRESSERROR, String.class));
        fields.add(new ModelField("address_type_ref_id", "addressTypeRef", ADDRESSES_ADDRESSTYPEREFID, DisplayModel.class, AddressTypeReference.groupName()));
        return fields;
    }

    /**
	 * @see com.patientis.model.common.IBaseModel#getModelFields()
	 */
    public static java.util.List<ModelField> getModelFields() {
        java.util.List<ModelField> fields = new java.util.ArrayList<ModelField>();
        fields.add(new ModelField("update_dt", "updateDt", ADDRESSES_UPDATEDT, DateTimeModel.class));
        fields.add(new ModelField("city_ref_id", "cityRef", ADDRESSES_CITYREFID, DisplayModel.class, CityReference.groupName()));
        fields.add(new ModelField("update_user_ref_id", "updateUserRef", ADDRESSES_UPDATEUSERREFID, DisplayModel.class, UserReference.groupName()));
        fields.add(new ModelField("notes", "notes", ADDRESSES_NOTES, String.class));
        fields.add(new ModelField("version", "version", ADDRESSES_VERSION, Integer.class));
        fields.add(new ModelField("city_free_text", "cityFreeText", ADDRESSES_CITYFREETEXT, String.class));
        fields.add(new ModelField("country_free_text", "countryFreeText", ADDRESSES_COUNTRYFREETEXT, String.class));
        fields.add(new ModelField("other_designation", "otherDesignation", ADDRESSES_OTHERDESIGNATION, String.class));
        fields.add(new ModelField("country_ref_id", "countryRef", ADDRESSES_COUNTRYREFID, DisplayModel.class, CountryReference.groupName()));
        fields.add(new ModelField("county_free_text", "countyFreeText", ADDRESSES_COUNTYFREETEXT, String.class));
        fields.add(new ModelField("county_ref_id", "countyRef", ADDRESSES_COUNTYREFID, DisplayModel.class, CountyReference.groupName()));
        fields.add(new ModelField("postal_code", "postalCode", ADDRESSES_POSTALCODE, String.class));
        fields.add(new ModelField("active_ind", "activeInd", ADDRESSES_ACTIVEIND, Integer.class));
        fields.add(new ModelField("deleted_ind", "deletedInd", ADDRESSES_DELETEDIND, Integer.class));
        fields.add(new ModelField("state_free_text", "stateFreeText", ADDRESSES_STATEFREETEXT, String.class));
        fields.add(new ModelField("state_ref_id", "stateRef", ADDRESSES_STATEREFID, DisplayModel.class, StateReference.groupName()));
        fields.add(new ModelField("street", "street", ADDRESSES_STREET, String.class));
        fields.add(new ModelField("system_ref_id", "systemRef", ADDRESSES_SYSTEMREFID, DisplayModel.class, SystemReference.groupName()));
        fields.add(new ModelField("insert_dt", "insertDt", ADDRESSES_INSERTDT, DateTimeModel.class));
        fields.add(new ModelField("insert_user_ref_id", "insertUserRef", ADDRESSES_INSERTUSERREFID, DisplayModel.class, UserReference.groupName()));
        fields.add(new ModelField("system_sequence", "systemSequence", ADDRESSES_SYSTEMSEQUENCE, Long.class));
        fields.add(new ModelField("system_transaction_seq", "systemTransactionSeq", ADDRESSES_SYSTEMTRANSACTIONSEQ, Long.class));
        fields.add(new ModelField("address_id", "addressId", ADDRESSES_ADDRESSID, Long.class));
        fields.add(new ModelField("address_error", "addressError", ADDRESSES_ADDRESSERROR, String.class));
        fields.add(new ModelField("address_type_ref_id", "addressTypeRef", ADDRESSES_ADDRESSTYPEREFID, DisplayModel.class, AddressTypeReference.groupName()));
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
        if (getAddressTypeRef().getId() != 0) getAddressTypeRef().setDisplay(reference.getDisplayValuePrimitive(getAddressTypeRef().getId()), reference.getShortDisplayValuePrimitive(getAddressTypeRef().getId()), reference.getCategoryReferenceId(getAddressTypeRef().getId()));
        if (getCityRef().getId() != 0) getCityRef().setDisplay(reference.getDisplayValuePrimitive(getCityRef().getId()), reference.getShortDisplayValuePrimitive(getCityRef().getId()), reference.getCategoryReferenceId(getCityRef().getId()));
        if (getCountryRef().getId() != 0) getCountryRef().setDisplay(reference.getDisplayValuePrimitive(getCountryRef().getId()), reference.getShortDisplayValuePrimitive(getCountryRef().getId()), reference.getCategoryReferenceId(getCountryRef().getId()));
        if (getCountyRef().getId() != 0) getCountyRef().setDisplay(reference.getDisplayValuePrimitive(getCountyRef().getId()), reference.getShortDisplayValuePrimitive(getCountyRef().getId()), reference.getCategoryReferenceId(getCountyRef().getId()));
        if (getInsertUserRef().getId() != 0) getInsertUserRef().setDisplay(reference.getDisplayValuePrimitive(getInsertUserRef().getId()), reference.getShortDisplayValuePrimitive(getInsertUserRef().getId()), reference.getCategoryReferenceId(getInsertUserRef().getId()));
        if (getStateRef().getId() != 0) getStateRef().setDisplay(reference.getDisplayValuePrimitive(getStateRef().getId()), reference.getShortDisplayValuePrimitive(getStateRef().getId()), reference.getCategoryReferenceId(getStateRef().getId()));
        if (getSystemRef().getId() != 0) getSystemRef().setDisplay(reference.getDisplayValuePrimitive(getSystemRef().getId()), reference.getShortDisplayValuePrimitive(getSystemRef().getId()), reference.getCategoryReferenceId(getSystemRef().getId()));
        if (getUpdateUserRef().getId() != 0) getUpdateUserRef().setDisplay(reference.getDisplayValuePrimitive(getUpdateUserRef().getId()), reference.getShortDisplayValuePrimitive(getUpdateUserRef().getId()), reference.getCategoryReferenceId(getUpdateUserRef().getId()));
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
        if (getAddressError() != null && getAddressError().length() > 255) {
            throw new FieldTooLongException("AddressError");
        }
        if (getCityFreeText() != null && getCityFreeText().length() > 100) {
            throw new FieldTooLongException("CityFreeText");
        }
        if (getCountryFreeText() != null && getCountryFreeText().length() > 50) {
            throw new FieldTooLongException("CountryFreeText");
        }
        if (getCountyFreeText() != null && getCountyFreeText().length() > 100) {
            throw new FieldTooLongException("CountyFreeText");
        }
        if (getOtherDesignation() != null && getOtherDesignation().length() > 255) {
            throw new FieldTooLongException("OtherDesignation");
        }
        if (getPostalCode() != null && getPostalCode().length() > 50) {
            throw new FieldTooLongException("PostalCode");
        }
        if (getStateFreeText() != null && getStateFreeText().length() > 100) {
            throw new FieldTooLongException("StateFreeText");
        }
        if (getStreet() != null && getStreet().length() > 255) {
            throw new FieldTooLongException("Street");
        }
    }

    /**
	 * @see com.patientis.model.common.IBaseModel#isValid()
	 */
    public boolean isValid() {
        if (getAddressError() != null && getAddressError().length() > 255) {
            return false;
        }
        if (getCityFreeText() != null && getCityFreeText().length() > 100) {
            return false;
        }
        if (getCountryFreeText() != null && getCountryFreeText().length() > 50) {
            return false;
        }
        if (getCountyFreeText() != null && getCountyFreeText().length() > 100) {
            return false;
        }
        if (getOtherDesignation() != null && getOtherDesignation().length() > 255) {
            return false;
        }
        if (getPostalCode() != null && getPostalCode().length() > 50) {
            return false;
        }
        if (getStateFreeText() != null && getStateFreeText().length() > 100) {
            return false;
        }
        if (getStreet() != null && getStreet().length() > 255) {
            return false;
        }
        return true;
    }

    /**
	 * @see com.patientis.model.common.BaseModel#getReportXml(java.lang.String)
	 */
    @Override
    public String getReportXml(String modelType) {
        if (Converter.isNotEmpty(getAddressTypeRef().getDisplay())) {
            return super.getReportXml(getAddressTypeRef().getDisplay() + modelType);
        } else {
            return "";
        }
    }

    /**
	 * @see com.patientis.model.common.BaseModel#getReportXml(java.lang.String)
	 */
    @Override
    public String getReportFields(String modelType) {
        if (Converter.isNotEmpty(getAddressTypeRef().getDisplay())) {
            return super.getReportFields(modelType + getAddressTypeRef().getDisplay());
        } else {
            return "";
        }
    }

    /**
	 * Reference group for the defining type ref
	 */
    public String getSourceRefGroup() {
        return AddressTypeReference.groupName();
    }
}
