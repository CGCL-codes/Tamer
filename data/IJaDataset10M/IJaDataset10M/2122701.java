package org.dasein.cloud.euca;

public class WalrusException extends Exception {

    private static final long serialVersionUID = -1187862739180492610L;

    private String code = null;

    private String requestId = null;

    private int status = 0;

    public WalrusException(int status, String requestId, String code, String message) {
        super(message);
        this.requestId = requestId;
        this.code = code;
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public String getRequestId() {
        return requestId;
    }

    public int getStatus() {
        return status;
    }

    public String getSummary() {
        return (status + "/" + requestId + "/" + code + ": " + getMessage());
    }
}
