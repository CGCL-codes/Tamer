package net.brutex.xmlbridge.ws.jaxws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;

public class P4Sync {

    @XmlElement(name = "force")
    public java.lang.String forceAttribute;

    @XmlElement(name = "port")
    public java.lang.String portAttribute;

    @XmlElement(name = "client")
    public java.lang.String clientAttribute;

    @XmlElement(name = "inerror")
    public boolean inerrorAttribute;

    @XmlElement(name = "user")
    public java.lang.String userAttribute;

    @XmlElement(name = "cmdopts")
    public java.lang.String cmdoptsAttribute;

    @XmlElement(name = "globalopts")
    public java.lang.String globaloptsAttribute;

    @XmlElement(name = "view")
    public java.lang.String viewAttribute;

    @XmlElement(name = "taskname")
    public java.lang.String tasknameAttribute;

    @XmlElement(name = "label")
    public java.lang.String labelAttribute;

    @XmlElement(name = "description")
    public java.lang.String descriptionAttribute;

    @XmlElement(name = "failonerror")
    public boolean failonerrorAttribute;

    @XmlElement(name = "errormessage")
    public java.lang.String errormessageAttribute;
}
