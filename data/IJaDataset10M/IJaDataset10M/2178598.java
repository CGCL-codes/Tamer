package com.misyshealthcare.connect.doc.ccd;

/**
 *  
 *
 * @author Wenzhi Li
 * @version 3.0, Nov 27, 2007
 */
public class Error {

    public enum SeverityType {

        ERROR("ERROR"), WARNING("WARNING");

        private String type = null;

        private SeverityType(String type) {
            this.type = type;
        }

        public String getType() {
            return this.type;
        }
    }

    public SeverityType severityType = null;

    public String message = null;

    public Error(SeverityType severityType, String message) {
        this.severityType = severityType;
        this.message = message;
    }

    /**
	 * @return the severityType
	 */
    public SeverityType getSeverityType() {
        return severityType;
    }

    /**
	 * @param severityType the severityType to set
	 */
    public void setSeverityType(SeverityType severityType) {
        this.severityType = severityType;
    }

    /**
	 * @return the message
	 */
    public String getMessage() {
        return message;
    }

    /**
	 * @param message the message to set
	 */
    public void setMessage(String message) {
        this.message = message;
    }
}
