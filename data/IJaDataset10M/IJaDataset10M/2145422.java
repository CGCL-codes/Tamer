package net.brutex.xmlbridge.ws.jaxws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;

public class Script {

    @XmlElement(name = "classpath")
    public org.apache.tools.ant.types.Path classpathAttribute;

    @XmlElement(name = "taskname")
    public java.lang.String tasknameAttribute;

    @XmlElement(name = "classpathref")
    public org.apache.tools.ant.types.Reference classpathrefAttribute;

    @XmlElement(name = "manager")
    public java.lang.String managerAttribute;

    @XmlElement(name = "description")
    public java.lang.String descriptionAttribute;

    @XmlElement(name = "setbeans")
    public boolean setbeansAttribute;

    @XmlElement(name = "src")
    public java.lang.String srcAttribute;

    @XmlElement(name = "language")
    public java.lang.String languageAttribute;
}
