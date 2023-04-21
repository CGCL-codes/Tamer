package com.tristia.entity;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "errors", propOrder = { "errorInfo" })
@XmlRootElement(name = "errors")
public class ErrorInfo {

    @XmlElement(name = "error", required = true)
    private List<String> errorInfo;

    public List<String> getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(List<String> errorInfo) {
        this.errorInfo = errorInfo;
    }
}
