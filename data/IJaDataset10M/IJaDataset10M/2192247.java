package org.oauth4j.jaxb;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "error")
public class OAuth4JError {

    private static final String STATUS = "status";

    private static final String TIMESTAMP = "timestamp";

    private static final String ERR_CODE = "error-code";

    private static final String MSG = "message";

    private int status;

    private long timestamp;

    private int errorCode;

    private String message;

    public int getStatus() {
        return status;
    }

    @XmlElement(name = STATUS)
    public void setStatus(int status) {
        this.status = status;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @XmlElement(name = TIMESTAMP)
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getErrorCode() {
        return errorCode;
    }

    @XmlElement(name = ERR_CODE)
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    @XmlElement(name = MSG)
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OAuth4JError [status=").append(status).append(", timestamp=").append(timestamp).append(", errorCode=").append(errorCode).append(", message=").append(message).append("]");
        return builder.toString();
    }
}
