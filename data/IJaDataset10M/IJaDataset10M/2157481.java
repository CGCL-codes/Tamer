package net.brutex.xmlbridge.ws.jaxws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;

public class VerifyJar {

    @XmlElement(name = "storepass")
    public java.lang.String storepassAttribute;

    @XmlElement(name = "certificates")
    public boolean certificatesAttribute;

    @XmlElement(name = "jar")
    public java.io.File jarAttribute;

    @XmlElement(name = "verbose")
    public boolean verboseAttribute;

    @XmlElement(name = "keypass")
    public java.lang.String keypassAttribute;

    @XmlElement(name = "maxmemory")
    public java.lang.String maxmemoryAttribute;

    @XmlElement(name = "alias")
    public java.lang.String aliasAttribute;

    @XmlElement(name = "keystore")
    public java.lang.String keystoreAttribute;

    @XmlElement(name = "taskname")
    public java.lang.String tasknameAttribute;

    @XmlElement(name = "description")
    public java.lang.String descriptionAttribute;

    @XmlElement(name = "storetype")
    public java.lang.String storetypeAttribute;
}
