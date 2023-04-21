package org.eventreaper.model.data;

public class ResultMessage {

    protected ResultType resultType;

    protected String message;

    public ResultType getStatus() {
        return resultType;
    }

    public void setStatus(ResultType pResultType) {
        resultType = pResultType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String pMessage) {
        message = pMessage;
    }
}
