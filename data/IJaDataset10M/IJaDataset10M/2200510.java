package net.brutex.xmlbridge.ws.jaxws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;

public class Property {

    @XmlElement(name = "refid")
    public org.apache.tools.ant.types.Reference refidAttribute;

    @XmlElement(name = "name")
    public java.lang.String nameAttribute;

    @XmlElement(name = "url")
    public java.net.URL urlAttribute;

    @XmlElement(name = "classpath")
    public org.apache.tools.ant.types.Path classpathAttribute;

    @XmlElement(name = "userproperty")
    public boolean userpropertyAttribute;

    @XmlElement(name = "file")
    public java.io.File fileAttribute;

    @XmlElement(name = "resource")
    public java.lang.String resourceAttribute;

    @XmlElement(name = "environment")
    public java.lang.String environmentAttribute;

    @XmlElement(name = "prefix")
    public java.lang.String prefixAttribute;

    @XmlElement(name = "classpathref")
    public org.apache.tools.ant.types.Reference classpathrefAttribute;

    @XmlElement(name = "taskname")
    public java.lang.String tasknameAttribute;

    @XmlElement(name = "value")
    public java.lang.String valueAttribute;

    @XmlElement(name = "location")
    public java.io.File locationAttribute;

    @XmlElement(name = "description")
    public java.lang.String descriptionAttribute;
}
