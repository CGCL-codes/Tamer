package com.mentorgen.tools.profile.runtime;

public class ServiceInternals {

    private String serviceClass;

    private String serviceID;

    private String inData;

    private String outData;

    private String totalData;

    public ServiceInternals(String _serviceClass) {
        serviceClass = _serviceClass;
    }

    public ServiceInternals() {
        serviceClass = null;
    }

    public void setServiceClass(String _serviceClass) throws Exception {
        if (serviceClass == null) serviceClass = _serviceClass; else throw new Exception("service name already set");
    }

    public void setServiceID(String _serviceID) {
        serviceID = _serviceID;
    }

    public void setInData(String _inData) {
        inData = _inData;
    }

    public void setOutData(String _outData) {
        outData = _outData;
    }

    public void setTotalData(String _totalData) {
        totalData = _totalData;
    }

    public String getServiceClass() {
        return serviceClass;
    }

    public String getServiceID() {
        return serviceID;
    }

    public String getInData() {
        return inData;
    }

    public String getOutData() {
        return outData;
    }

    public String getTotalData() {
        return totalData;
    }
}
