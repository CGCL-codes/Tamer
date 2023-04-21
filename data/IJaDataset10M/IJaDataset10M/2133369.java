package net.brutex.xmlbridge.ws.jaxws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;

public class CSharp {

    @XmlElement(name = "excludes")
    public java.lang.String excludesAttribute;

    @XmlElement(name = "executable")
    public java.lang.String executableAttribute;

    @XmlElement(name = "destfile")
    public java.io.File destfileAttribute;

    @XmlElement(name = "win32icon")
    public java.io.File win32iconAttribute;

    @XmlElement(name = "definitions")
    public java.lang.String definitionsAttribute;

    @XmlElement(name = "referencefiles")
    public org.apache.tools.ant.types.Path referencefilesAttribute;

    @XmlElement(name = "fullpaths")
    public boolean fullpathsAttribute;

    @XmlElement(name = "description")
    public java.lang.String descriptionAttribute;

    @XmlElement(name = "outputfile")
    public java.io.File outputfileAttribute;

    @XmlElement(name = "includes")
    public java.lang.String includesAttribute;

    @XmlElement(name = "mainclass")
    public java.lang.String mainclassAttribute;

    @XmlElement(name = "utf8output")
    public boolean utf8outputAttribute;

    @XmlElement(name = "useresponsefile")
    public boolean useresponsefileAttribute;

    @XmlElement(name = "failonerror")
    public boolean failonerrorAttribute;

    @XmlElement(name = "destdir")
    public java.io.File destdirAttribute;

    @XmlElement(name = "debug")
    public boolean debugAttribute;

    @XmlElement(name = "includedefaultreferences")
    public boolean includedefaultreferencesAttribute;

    @XmlElement(name = "warnlevel")
    public int warnlevelAttribute;

    @XmlElement(name = "win32res")
    public java.io.File win32resAttribute;

    @XmlElement(name = "casesensitive")
    public boolean casesensitiveAttribute;

    @XmlElement(name = "followsymlinks")
    public boolean followsymlinksAttribute;

    @XmlElement(name = "defaultexcludes")
    public boolean defaultexcludesAttribute;

    @XmlElement(name = "extraoptions")
    public java.lang.String extraoptionsAttribute;

    @XmlElement(name = "incremental")
    public boolean incrementalAttribute;

    @XmlElement(name = "noconfig")
    public boolean noconfigAttribute;

    @XmlElement(name = "includesfile")
    public java.io.File includesfileAttribute;

    @XmlElement(name = "references")
    public java.lang.String referencesAttribute;

    @XmlElement(name = "taskname")
    public java.lang.String tasknameAttribute;

    @XmlElement(name = "srcdir")
    public java.io.File srcdirAttribute;

    @XmlElement(name = "optimize")
    public boolean optimizeAttribute;

    @XmlElement(name = "additionalmodules")
    public java.lang.String additionalmodulesAttribute;

    @XmlElement(name = "docfile")
    public java.io.File docfileAttribute;

    @XmlElement(name = "excludesfile")
    public java.io.File excludesfileAttribute;

    @XmlElement(name = "unsafe")
    public boolean unsafeAttribute;

    @XmlElement(name = "filealign")
    public int filealignAttribute;

    @XmlElement(name = "targettype")
    public org.apache.tools.ant.taskdefs.optional.dotnet.DotnetCompile.TargetTypes targettypeAttribute;
}
