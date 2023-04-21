package edu.purdue.rcac.ccsm4.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "configureCase", namespace = "http://ws.ccsm4.rcac.purdue.edu/")
@XmlType(name = "configureCase", namespace = "http://ws.ccsm4.rcac.purdue.edu/")
@XmlAccessorType(XmlAccessType.FIELD)
public class ConfigureCase {

    @XmlElement(name = "userName", namespace = "", nillable = false, required = true)
    private String userName;

    @XmlElement(name = "caseName", namespace = "", nillable = false, required = true)
    private String caseName;

    @XmlElement(name = "token", namespace = "", nillable = false, required = true)
    private String token;

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCaseName() {
        return this.caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
