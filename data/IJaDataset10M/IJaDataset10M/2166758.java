package org.gbif.portal.model;

/**
 * Represents the data captured for a single identifier
 * 
 * @author Donald Hobern
 */
public class IdentifierRecord extends ModelObject {

    /**
	 * Generated
	 */
    private static final long serialVersionUID = 2585614955165166761L;

    protected long dataResourceId;

    protected long occurrenceId;

    protected int identifierType;

    protected String identifier;

    /**
     * Default
     */
    public IdentifierRecord() {
    }

    /**
	 * Convienience
	 */
    public IdentifierRecord(long dataResourceId, long occurrenceId, int identifierType, String identifier) {
        this.dataResourceId = dataResourceId;
        this.occurrenceId = occurrenceId;
        this.identifierType = identifierType;
        this.identifier = identifier;
    }

    /**
	 * Convienience
	 */
    public IdentifierRecord(long id, long dataResourceId, long occurrenceId, int identifierType, String identifier) {
        this.id = id;
        this.dataResourceId = dataResourceId;
        this.occurrenceId = occurrenceId;
        this.identifierType = identifierType;
        this.identifier = identifier;
    }

    /**
	 * @return Returns the dataResourceId.
	 */
    public long getDataResourceId() {
        return dataResourceId;
    }

    /**
	 * @param dataResourceId The dataResourceId to set.
	 */
    public void setDataResourceId(long dataResourceId) {
        this.dataResourceId = dataResourceId;
    }

    /**
	 * @return Returns the identifier.
	 */
    public String getIdentifier() {
        return identifier;
    }

    /**
	 * @param identifier The identifier to set.
	 */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    /**
	 * @return Returns the identifierType.
	 */
    public int getIdentifierType() {
        return identifierType;
    }

    /**
	 * @param identifierType The identifierType to set.
	 */
    public void setIdentifierType(int identifierType) {
        this.identifierType = identifierType;
    }

    /**
	 * @return Returns the occurrenceId.
	 */
    public long getOccurrenceId() {
        return occurrenceId;
    }

    /**
	 * @param occurrenceId The occurrenceId to set.
	 */
    public void setOccurrenceId(long occurrenceId) {
        this.occurrenceId = occurrenceId;
    }
}
