package net.brutex.xmlbridge.ws.jaxws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;

public class CCCheckout {

    @XmlElement(name = "reserved")
    public boolean reservedAttribute;

    @XmlElement(name = "viewpath")
    public java.lang.String viewpathAttribute;

    @XmlElement(name = "out")
    public java.lang.String outAttribute;

    @XmlElement(name = "notco")
    public boolean notcoAttribute;

    @XmlElement(name = "comment")
    public java.lang.String commentAttribute;

    @XmlElement(name = "nodata")
    public boolean nodataAttribute;

    @XmlElement(name = "version")
    public boolean versionAttribute;

    @XmlElement(name = "nowarn")
    public boolean nowarnAttribute;

    @XmlElement(name = "objselect")
    public java.lang.String objselectAttribute;

    @XmlElement(name = "failonerr")
    public boolean failonerrAttribute;

    @XmlElement(name = "cleartooldir")
    public java.lang.String cleartooldirAttribute;

    @XmlElement(name = "taskname")
    public java.lang.String tasknameAttribute;

    @XmlElement(name = "branch")
    public java.lang.String branchAttribute;

    @XmlElement(name = "description")
    public java.lang.String descriptionAttribute;

    @XmlElement(name = "commentfile")
    public java.lang.String commentfileAttribute;
}
