package com.ohioedge.j2ee.api.org;

import javax.xml.bind.annotation.XmlRootElement;
import org.j2eebuilder.util.LogManager;

/**
 * @(#)RequestDetailBean.java 1.3.1 10/15/2002 RequestDetailBean is a java bean with the
 *                      main function of facilitating communication between JSPs
 *                      and Request EJB
 * @version 1.3.1
 */
@XmlRootElement()
public class RequestParameterBean extends org.j2eebuilder.model.ManagedTransientObjectImpl {

    private static transient LogManager log = new LogManager(RequestParameterBean.class);

    private Integer requestParameterID;

    private Integer requestID;

    private String parameterName;

    private String parameterValue;

    public RequestParameterBean() {
    }

    public Integer getRequestParameterID() {
        return requestParameterID;
    }

    public void setRequestParameterID(Integer requestParameterID) {
        this.requestParameterID = requestParameterID;
    }

    public Integer getRequestID() {
        return requestID;
    }

    public void setRequestID(Integer requestID) {
        this.requestID = requestID;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getParameterValue() {
        return parameterValue;
    }

    public void setParameterValue(String parameterValue) {
        this.parameterValue = parameterValue;
    }

    /**
	 * name/description
	 */
    public String getName() {
        return this.getParameterName();
    }

    public String getDescription() {
        return this.getParameterValue();
    }
}
