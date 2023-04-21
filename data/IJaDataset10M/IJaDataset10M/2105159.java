package org.nightlabs.jfire.prop;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import org.nightlabs.jfire.prop.exception.PropertyException;
import org.nightlabs.jfire.prop.id.StructFieldID;
import org.nightlabs.jfire.prop.validation.IDataFieldValidator;
import org.nightlabs.jfire.prop.validation.ValidationResult;

/**
 * Base class for all types of data fields that can be stored in a {@link PropertySet}.
 * It contains the primary key of a data field that is a composed of the
 * reference to the structure block and field the datafield corresponds and
 * the reference to the PropertySet it is stored in.
 * <p>
 * Custom data field types should be build by extending this class and adding the
 * custom data. The jdo inheritance strategy should be "new-table" for custom fields
 * and the fetch-group {@value PropertySet#FETCH_GROUP_FULL_DATA} should be re-declared
 * and the custom fields should be added there.
 * </p>
 *
 * @author Alexander Bieber <alex[AT]nightlabs[DOT]de>
 * @author Tobias Langner <!-- tobias[dot]langner[at]nightlabs[dot]de -->
 *
 * @jdo.persistence-capable identity-type="application"
 *                          objectid-class="org.nightlabs.jfire.prop.id.DataFieldID"
 *                          detachable="true"
 *                          table="JFireBase_Prop_DataField"
 *
 * @jdo.inheritance strategy="new-table"
 *
 * @jdo.create-objectid-class field-order="organisationID, propertySetID,
 *                            structBlockOrganisationID, structBlockID,
 *                            dataBlockID, structFieldOrganisationID,
 *                            structFieldID"
 *                            include-imports="id/DataFieldID.imports.inc"
 *                            include-body="id/DataFieldID.body.inc"
 *
 * @jdo.query name="getDataFieldInstanceCountByStructFieldType" query="SELECT
 *            UNIQUE count(this.structBlockOrganisationID) WHERE
 *            this.structBlockOrganisationID == paramStructBlockOrganisationID &&
 *            this.structBlockID == paramStructBlockID &&
 *            this.structFieldOrganisationID == paramStructFieldOrganisationID &&
 *            this.structFieldID == paramStructFieldID PARAMETERS String
 *            paramStructBlockOrganisationID, String paramStructBlockID, String
 *            paramStructFieldOrganisationID, String paramStructFieldID"
 *
 * @jdo.fetch-group name="FetchGroupsProp.fullData" fetch-groups="default"
 */
public abstract class DataField implements Serializable, Comparable<DataField>, IDataField {

    private static final long serialVersionUID = 1L;

    protected DataField() {
    }

    /**
	 * Create a new {@link DataField} with its primary key fields
	 * set according to the given {@link DataBlock} and {@link StructField}-
	 *
	 * @param _dataBlock The {@link DataBlock} the new {@link DataField} should virtually be in.
	 * @param _structField The {@link StructField} the new {@link DataField} should represent.
	 */
    public DataField(DataBlock _dataBlock, StructField<? extends DataField> _structField) {
        this.structFieldOrganisationID = _structField.getStructFieldOrganisationID();
        this.structFieldID = _structField.getStructFieldID();
        this.organisationID = _dataBlock.getOrganisationID();
        this.propertySetID = _dataBlock.getPropertySetID();
        this.structBlockOrganisationID = _dataBlock.getStructBlockOrganisationID();
        this.structBlockID = _dataBlock.getStructBlockID();
        this.dataBlockID = _dataBlock.getDataBlockID();
    }

    /**
	 * Create a new {@link DataField} that will have the same location (DataBlock)
	 * as the given one but will be inside the {@link PropertySet} referenced
	 * by the given organisationID and propertySetID.
	 *
	 * @param organisationID The organisation ID referencing the {@link PropertySet} the new {@link DataField} should be in.
	 * @param propertySetID The propertySetID referencing the {@link PropertySet} the new {@link DataField} should be in.
	 * @param cloneField The {@link DataField} inside another PropertySet whose position
	 *                   in the referenced {@link PropertySet} the new {@link DataField} should have.
	 */
    public DataField(String organisationID, long propertySetID, DataField cloneField) {
        this.structFieldOrganisationID = cloneField.getStructFieldOrganisationID();
        this.structFieldID = cloneField.getStructFieldID();
        this.organisationID = organisationID;
        this.propertySetID = propertySetID;
        this.structBlockOrganisationID = cloneField.getStructBlockOrganisationID();
        this.structBlockID = cloneField.getStructBlockID();
        this.dataBlockID = cloneField.getDataBlockID();
    }

    /**
	 * @jdo.field primary-key="true"
	 * @jdo.column length="100"
	 */
    private String organisationID;

    /**
	 * @jdo.field primary-key="true"
	 */
    private long propertySetID;

    /**
	 * This field is set when inflating and set to <code>null</code> when deflating. Thus it is never transmitted to the server.
	 *
	 * @jdo.field persistence-modifier="none"
	 */
    private StructField<? extends DataField> structField;

    /**
	 * @jdo.field primary-key="true"
	 * @jdo.column length="100"
	 */
    private String structBlockOrganisationID;

    /**
	 * @jdo.field primary-key="true"
	 * @jdo.column length="100"
	 */
    private String structBlockID;

    /**
	 * @jdo.field primary-key="true"
	 */
    private int dataBlockID;

    /**
	 * @jdo.field primary-key="true"
	 * @jdo.column length="100"
	 */
    private String structFieldOrganisationID;

    /**
	 * @jdo.field primary-key="true"
	 * @jdo.column length="100"
	 */
    private String structFieldID;

    /**
	 * This field is meant to indicate the order index of the {@link DataBlock} that contains this {@link DataField}. All {@link DataField}s of the
	 * same {@link DataBlock} should hold the same index value. Additionally, the indices of the {@link DataBlock}s should be greater than 0 and
	 * subsequent.
	 *
	 * @jdo.field persistence-modifier="persistent"
	 */
    private int dataBlockIndex;

    public abstract boolean isEmpty();

    /**
	 * @return Returns the organisationID.
	 */
    public String getOrganisationID() {
        return organisationID;
    }

    /**
	 * @return Returns the structBlockID.
	 */
    public String getStructBlockID() {
        return structBlockID;
    }

    /**
	 * @return Returns the structBlockOrganisationID.
	 */
    public String getStructBlockOrganisationID() {
        return structBlockOrganisationID;
    }

    /**
	 * @return Returns the structFieldID.
	 */
    public String getStructFieldID() {
        return structFieldID;
    }

    /**
	 * Returns the {@link StructFieldID} (id-object) of the associated {@link StructField}.
	 * @return The {@link StructFieldID} (id-object) of the associated {@link StructField}.
	 */
    public StructFieldID getStructFieldIDObj() {
        return StructFieldID.create(structBlockOrganisationID, structBlockID, structFieldOrganisationID, structFieldID);
    }

    /**
	 * @return Returns the structFieldOrganisationID.
	 */
    public String getStructFieldOrganisationID() {
        return structFieldOrganisationID;
    }

    /**
	 * @return Returns the propertySetID.
	 */
    public long getPropertySetID() {
        return this.propertySetID;
    }

    /**
	 * @return Returns the dataBlockID.
	 */
    public int getDataBlockID() {
        return dataBlockID;
    }

    /**
	 * Package visible modifier for the propertySetID.
	 *
	 * @param _propID
	 */
    void setPropertySetID(long _propID) {
        this.propertySetID = _propID;
    }

    /**
	 * @jdo.field persistence-modifier="none"
	 */
    private String structBlockKey = null;

    /**
	 * @return Returns the structBlockKey.
	 */
    public String getStructBlockKey() {
        if (structBlockKey == null) structBlockKey = structBlockOrganisationID + "/" + structBlockID;
        return structBlockKey;
    }

    /**
	 * @jdo.field persistence-modifier="none"
	 */
    private String propRelativePK = null;

    /**
	 * @return Returns the propRelativePK.
	 */
    public String getPropRelativePK() {
        if (propRelativePK == null) propRelativePK = structBlockOrganisationID + "/" + structBlockID + "/" + Integer.toString(dataBlockID) + "/" + structFieldOrganisationID + "/" + structFieldID;
        return propRelativePK;
    }

    /**
	 * @jdo.field persistence-modifier="none"
	 */
    private String structFieldPK = null;

    /**
	 * @return Returns the structFieldPK.
	 */
    public String getStructFieldPK() {
        if (structFieldPK == null) structFieldPK = StructField.getPrimaryKey(structBlockOrganisationID, structBlockID, structFieldOrganisationID, structFieldID);
        return structFieldPK;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DataField) {
            DataField other = (DataField) obj;
            return (this.organisationID.equals(other.organisationID)) && (this.propertySetID == other.propertySetID) && (this.structBlockOrganisationID.equals(other.structBlockOrganisationID)) && (this.structBlockID.equals(other.structBlockID)) && (this.dataBlockID == other.dataBlockID) && (this.structFieldOrganisationID.equals(other.structFieldOrganisationID)) && (this.structFieldID.equals(other.structFieldID));
        } else return super.equals(obj);
    }

    @Override
    public String toString() {
        return this.organisationID + "/(" + this.structBlockOrganisationID + "." + this.structBlockID + ")/(" + this.structFieldOrganisationID + "." + this.structFieldID + ")";
    }

    /**
	 * @jdo.field persistence-modifier="none"
	 */
    private int priority = Integer.MAX_VALUE;

    /**
	 * @param priority
	 *          The priority to set.
	 */
    public void setPriority(int priority) {
        this.priority = priority;
    }

    /**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
    public int compareTo(DataField o) {
        if (o.getStructBlockKey().equals(this.getStructBlockKey())) {
            if (o.priority < this.priority) return 1; else if (o.priority > this.priority) return -1; else {
                if (this.equals(o)) {
                    return 0;
                } else return 1;
            }
        } else return 1;
    }

    /**
	 * Returns the count of {@link DataField}s referencing the given {@link StructField} that were already persisted.
	 *
	 * @param pm The {@link PersistenceManager} to use. (Connection to a certain datastore).
	 * @param structFieldID The {@link StructFieldID} persisted {@link DataField}s should be searched for.
	 * @return The count of {@link DataField}s referencing the given {@link StructField}
	 */
    public static long getDataFieldInstanceCountByStructFieldType(PersistenceManager pm, StructFieldID structFieldID) {
        Query q = pm.newNamedQuery(DataField.class, "getDataFieldInstanceCountByStructFieldType");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("paramStructBlockOrganisationID", structFieldID.structBlockOrganisationID);
        params.put("paramStructBlockID", structFieldID.structBlockID);
        params.put("paramStructFieldOrganisationID", structFieldID.structFieldOrganisationID);
        params.put("paramStructFieldID", structFieldID.structFieldID);
        return (Long) q.executeWithMap(params);
    }

    /**
	 * Subclasses should create a new {@link DataField} of their type
	 * here and <b>copy</b> their data to the new instance.
	 * <p>
	 * The clone should be connected to the given {@link PropertySet},
	 * the constructor {@link #DataField(String, long, DataField)}
	 * is intended to help constructing the new instance.
	 * </p>
	 * @param propertySet The {@link PropertySet} the new {@link DataField}
	 *                    instance should be linked to.
	 * @return A new {@link DataField} instance of this {@link DataField}
	 *         with a copy of its data and linked to the given {@link PropertySet}.
	 */
    public abstract DataField cloneDataField(PropertySet propertySet);

    /**
	 * Returns the order index of the {@link DataBlock} that contains this {@link DataField}.
	 * @return The order index of the {@link DataBlock} that contains this {@link DataField}.
	 */
    int getDataBlockIndex() {
        return dataBlockIndex;
    }

    /**
	 * Sets the order index of the {@link DataBlock} that contains this {@link DataField}. All {@link DataField}s of a {@link DataBlock} should
	 * have the same value for this field. If that is not the case, the values are corrected upon exploding.
	 * @param dataBlockIndex The order index of the {@link DataBlock} to be set.
	 */
    void setDataBlockIndex(int dataBlockIndex) {
        this.dataBlockIndex = dataBlockIndex;
    }

    /**
	 * Validates this instance against the given {@link IStruct} and returns a list of all {@link ValidationFailureResult}
	 * occurred during the validation or <code>null</code> if the validation succeeded.
	 * @param struct The {@link IStruct} against which to validated.
	 * @return A list of all {@link ValidationFailureResult} occurred during the validation or <code>null</code> if the validation succeeded..
	 */
    @SuppressWarnings("unchecked")
    public List<ValidationResult> validate(IStruct struct) {
        List<ValidationResult> results = new LinkedList<ValidationResult>();
        try {
            StructField<DataField> structField = (StructField<DataField>) struct.getStructField(this);
            for (IDataFieldValidator<DataField, StructField<DataField>> validator : structField.getDataFieldValidators()) {
                ValidationResult result = validator.validate(this, structField);
                if (result != null) results.add(result);
            }
        } catch (PropertyException e) {
            throw new IllegalArgumentException("StructField for this datafield was not found.");
        }
        if (results.isEmpty()) return null; else return results;
    }

    /**
	 * Returns the {@link StructField} of this instance. This method returns null if it is called on a {@link DataField}
	 * that is part of a deflated structure.
	 *
	 * @return the {@link StructField} of this instance.
	 */
    public StructField<? extends DataField> getStructField() {
        return structField;
    }

    /**
	 * Sets the {@link StructField} of this instance. This should be done while inflating, on deflation it should be set to null again.
	 *
	 * @param structField The {@link StructField} to be set.
	 */
    public void setStructField(StructField<? extends DataField> structField) {
        this.structField = structField;
    }
}
