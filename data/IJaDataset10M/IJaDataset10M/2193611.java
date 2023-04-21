package com.passionatelife.familytree.webservice.common;

public class BaseRequest {

    private String requestingSystemInstanceId;

    private String requestingSystemName;

    public String getRequestingSystemInstanceId() {
        return requestingSystemInstanceId;
    }

    public void setRequestingSystemInstanceId(String requestingSystemInstanceId) {
        this.requestingSystemInstanceId = requestingSystemInstanceId;
    }

    public String getRequestingSystemName() {
        return requestingSystemName;
    }

    public void setRequestingSystemName(String requestingSystemName) {
        this.requestingSystemName = requestingSystemName;
    }
}
