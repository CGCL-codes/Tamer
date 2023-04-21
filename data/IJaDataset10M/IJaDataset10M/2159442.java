package net.brutex.xmlbridge.ws.jaxws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;

public class Patch {

    @XmlElement(name = "strip")
    public int stripAttribute;

    @XmlElement(name = "dir")
    public java.io.File dirAttribute;

    @XmlElement(name = "destfile")
    public java.io.File destfileAttribute;

    @XmlElement(name = "quiet")
    public boolean quietAttribute;

    @XmlElement(name = "originalfile")
    public java.io.File originalfileAttribute;

    @XmlElement(name = "patchfile")
    public java.io.File patchfileAttribute;

    @XmlElement(name = "reverse")
    public boolean reverseAttribute;

    @XmlElement(name = "taskname")
    public java.lang.String tasknameAttribute;

    @XmlElement(name = "backups")
    public boolean backupsAttribute;

    @XmlElement(name = "ignorewhitespace")
    public boolean ignorewhitespaceAttribute;

    @XmlElement(name = "description")
    public java.lang.String descriptionAttribute;
}
