package net.brutex.xmlbridge.ws.jaxws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;

public class WLRun {

    @XmlElement(name = "name")
    public java.lang.String nameAttribute;

    @XmlElement(name = "classpath")
    public org.apache.tools.ant.types.Path classpathAttribute;

    @XmlElement(name = "pkpassword")
    public java.lang.String pkpasswordAttribute;

    @XmlElement(name = "beahome")
    public java.io.File beahomeAttribute;

    @XmlElement(name = "domain")
    public java.lang.String domainAttribute;

    @XmlElement(name = "username")
    public java.lang.String usernameAttribute;

    @XmlElement(name = "weblogicmainclass")
    public java.lang.String weblogicmainclassAttribute;

    @XmlElement(name = "wlclasspath")
    public org.apache.tools.ant.types.Path wlclasspathAttribute;

    @XmlElement(name = "args")
    public java.lang.String argsAttribute;

    @XmlElement(name = "policy")
    public java.lang.String policyAttribute;

    @XmlElement(name = "properties")
    public java.lang.String propertiesAttribute;

    @XmlElement(name = "home")
    public java.io.File homeAttribute;

    @XmlElement(name = "taskname")
    public java.lang.String tasknameAttribute;

    @XmlElement(name = "description")
    public java.lang.String descriptionAttribute;

    @XmlElement(name = "password")
    public java.lang.String passwordAttribute;

    @XmlElement(name = "jvmargs")
    public java.lang.String jvmargsAttribute;
}
