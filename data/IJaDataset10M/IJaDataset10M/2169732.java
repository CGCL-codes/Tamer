package prisms.records;

/**
 * Represents a change that could not be correctly retrieved, deserialized, etc. This allows the
 * change to be passed around and stored even if the information represented by the change cannot be
 * retrieved due to an error.
 */
public class ChangeRecordError extends ChangeRecord {

    /** The subject type of a ChangeRecordError */
    public static SubjectType ErrorSubjectType = new SubjectType() {

        public String name() {
            return "Error";
        }

        public Class<? extends Enum<? extends ChangeType>> getChangeTypes() {
            return ErrorChangeTypes.class;
        }

        public Class<?> getMajorType() {
            return Object.class;
        }

        public Class<?> getMetadataType1() {
            return null;
        }

        public Class<?> getMetadataType2() {
            return null;
        }
    };

    private static enum ErrorChangeTypes implements ChangeType {

        VALUE;

        public Class<?> getMinorType() {
            return null;
        }

        public Class<?> getObjectType() {
            return null;
        }

        public boolean isObjectIdentifiable() {
            return false;
        }

        public String toString(int additivity) {
            return null;
        }

        public String toString(int additivity, Object majorSubject, Object minorSubject) {
            return null;
        }

        public String toString(int additivity, Object majorSubject, Object minorSubject, Object before, Object after) {
            return null;
        }
    }

    private String theSubjectType;

    private String theChangeType;

    private int theAdditivity;

    private Object theMajorSubject;

    private long theMajorSubjectID;

    private Object theMinorSubject;

    private long theMinorSubjectID;

    private Object theData1;

    private long theData1ID;

    private Object theData2;

    private long theData2ID;

    private Object thePreValue;

    private Object theSerializePreValue;

    /**
	 * Creates a ChangeRecordError with the minimum set of information. The rest of the information
	 * in ChangeRecord is filled in with minimal data. The actual data relevant to the change is set
	 * with the setter methods.
	 * 
	 * @param _id The ID of the change
	 * @param local Whether the change is local only
	 * @param _time The time of the change
	 * @param _user The user that caused the change
	 */
    public ChangeRecordError(long _id, boolean local, long _time, RecordUser _user) {
        super(_id, local, _time, _user, ErrorSubjectType, ErrorChangeTypes.VALUE, 0, ErrorSubjectType, null, null, null, null);
    }

    /** @return The name of the subject type of the actual change */
    public String getSubjectType() {
        return theSubjectType;
    }

    /** @param subjectType The name of the subject type of the actual change */
    public void setSubjectType(String subjectType) {
        theSubjectType = subjectType;
    }

    /** @return The name of the change type of the actual change */
    public String getChangeType() {
        return theChangeType;
    }

    /** @param changeType The name of the change type of the actual change */
    public void setChangeType(String changeType) {
        theChangeType = changeType;
    }

    /** @return The additivity of the actual change */
    public int getAdditivity() {
        return theAdditivity;
    }

    /** @param add The additivity of the actual change */
    public void setAdditivity(int add) {
        theAdditivity = add;
    }

    /** @return The major subject of the actual change, if available--may be null */
    public Object getMajorSubject() {
        return theMajorSubject;
    }

    /** @return The ID of the major subject of the actual change */
    public long getMajorSubjectID() {
        return theMajorSubjectID;
    }

    /**
	 * @param value The major subject of the actual change, if available--may be null
	 * @param msID The ID of the major subject
	 */
    public void setMajorSubject(Object value, long msID) {
        theMajorSubject = value;
        theMajorSubjectID = msID;
    }

    /** @return The minor subject of the actual change, if avaialable--may be null */
    public Object getMinorSubject() {
        return theMinorSubject;
    }

    /** @return The ID of the minor subject of the actual change */
    public long getMinorSubjectID() {
        return theMinorSubjectID;
    }

    /**
	 * @param value The minor subject of the actual change, if available--may be null
	 * @param msID The ID of the minor subject
	 */
    public void setMinorSubject(Object value, long msID) {
        theMinorSubject = value;
        theMinorSubjectID = msID;
    }

    /** @return The first metadata of the actual change, if available--may be null */
    public Object getData1() {
        return theData1;
    }

    /** @return The ID of the first metadata of the actual change */
    public long getData1ID() {
        return theData1ID;
    }

    /**
	 * @param value The first metadata of the actual change, if available--may be null
	 * @param d1ID The ID of the first metadata
	 */
    public void setData1(Object value, long d1ID) {
        theData1 = value;
        theData1ID = d1ID;
    }

    /** @return The second metadata of the actual change, if available--may be null */
    public Object getData2() {
        return theData2;
    }

    /** @return The ID of the second metadata of the actual change */
    public long getData2ID() {
        return theData2ID;
    }

    /**
	 * @param value The second metadata of the actual change, if available--may be null
	 * @param d2ID The ID of the second metadata
	 */
    public void setData2(Object value, long d2ID) {
        theData2 = value;
        theData2ID = d2ID;
    }

    /** @return The previous value of the actual change, if available--may be null */
    public Object getPreValue() {
        return thePreValue;
    }

    /** @return The serialized previous value (may be of type String or Number) */
    public Object getSerializedPreValue() {
        return theSerializePreValue;
    }

    /**
	 * @param value The previous value of the actual change, if available--may be null
	 * @param serialized The serialized previous value (may be of type String or Number)
	 */
    public void setPreValue(Object value, Object serialized) {
        thePreValue = value;
        if (serialized != null && !(serialized instanceof String) && !(serialized instanceof Number)) throw new IllegalArgumentException("Serialized pre-value must be string or number");
        theSerializePreValue = serialized;
    }
}
