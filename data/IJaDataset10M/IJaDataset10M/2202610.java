package edu.psu.citeseerx.oai.verbs;

/**
 * Store information about an OAI error 
 * @author Pradeep Teregowda
 * @author Juan Pablo Fernandez Ramirez
 * @version $Rev: 876 $ $Date: 2009-01-09 12:52:33 -0500 (Fri, 09 Jan 2009) $
 */
public class OAIError {

    public static final String BAD_VERB_ERROR = "badVerb";

    public static final String BAD_ARGUMENT_ERROR = "badArgument";

    public static final String ID_DOES_NOT_EXISTS_ERROR = "idDoesNotExist";

    public static final String NO_RECORDS_MATCH_ERROR = "noRecordsMatch";

    public static final String NO_SET_HIERARCHY_ERROR = "noSetHierarchy";

    public static final String BAD_RESUMPTION_TOKEN_ERROR = "badResumptionToken";

    public static final String CANNOT_DISEMINATE_FORMAT_ERROR = "cannotDisseminateFormat";

    public static final String NO_METADATA_FORMATS_ERROR = "noMetadataFormats";

    private String message;

    private String errorCode;

    public OAIError(String message, String errorCode) {
        super();
        this.message = message;
        this.errorCode = errorCode;
    }

    /**
	 * Gets the error message
	 * @return the message encapsulated in this object
	 */
    public String getMessage() {
        return message;
    }

    /**
	 * Sets the error message
	 * @param message
	 */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
	 * Returns the error code
	 * @return the statusCode
	 */
    public String getErrorCode() {
        return errorCode;
    }

    /**
	 * @param errorCode the error code for this Error
	 */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return errorCode + ": " + message;
    }
}
