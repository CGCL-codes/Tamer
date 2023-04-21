package net.brutex.xmlbridge.ws.jaxws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;

public class ScriptDef {

    @XmlElement(name = "src")
    public java.io.File srcAttribute;

    @XmlElement(name = "name")
    public java.lang.String nameAttribute;

    @XmlElement(name = "uri")
    public java.lang.String uriAttribute;

    @XmlElement(name = "classpath")
    public org.apache.tools.ant.types.Path classpathAttribute;

    @XmlElement(name = "manager")
    public java.lang.String managerAttribute;

    @XmlElement(name = "language")
    public java.lang.String languageAttribute;

    @XmlElement(name = "classpathref")
    public org.apache.tools.ant.types.Reference classpathrefAttribute;

    @XmlElement(name = "loaderref")
    public org.apache.tools.ant.types.Reference loaderrefAttribute;

    @XmlElement(name = "taskname")
    public java.lang.String tasknameAttribute;

    @XmlElement(name = "description")
    public java.lang.String descriptionAttribute;

    @XmlElement(name = "reverseloader")
    public boolean reverseloaderAttribute;
}
