package net.brutex.xmlbridge.ws.jaxws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;

public class JavaCC {

    @XmlElement(name = "jdkversion")
    public java.lang.String jdkversionAttribute;

    @XmlElement(name = "debugparser")
    public boolean debugparserAttribute;

    @XmlElement(name = "javacchome")
    public java.io.File javacchomeAttribute;

    @XmlElement(name = "optimizetokenmanager")
    public boolean optimizetokenmanagerAttribute;

    @XmlElement(name = "commontokenaction")
    public boolean commontokenactionAttribute;

    @XmlElement(name = "forcelacheck")
    public boolean forcelacheckAttribute;

    @XmlElement(name = "buildtokenmanager")
    public boolean buildtokenmanagerAttribute;

    @XmlElement(name = "sanitycheck")
    public boolean sanitycheckAttribute;

    @XmlElement(name = "buildparser")
    public boolean buildparserAttribute;

    @XmlElement(name = "otherambiguitycheck")
    public int otherambiguitycheckAttribute;

    @XmlElement(name = "lookahead")
    public int lookaheadAttribute;

    @XmlElement(name = "debuglookahead")
    public boolean debuglookaheadAttribute;

    @XmlElement(name = "choiceambiguitycheck")
    public int choiceambiguitycheckAttribute;

    @XmlElement(name = "unicodeinput")
    public boolean unicodeinputAttribute;

    @XmlElement(name = "usercharstream")
    public boolean usercharstreamAttribute;

    @XmlElement(name = "debugtokenmanager")
    public boolean debugtokenmanagerAttribute;

    @XmlElement(name = "keeplinecolumn")
    public boolean keeplinecolumnAttribute;

    @XmlElement(name = "description")
    public java.lang.String descriptionAttribute;

    @XmlElement(name = "taskname")
    public java.lang.String tasknameAttribute;

    @XmlElement(name = "outputdirectory")
    public java.io.File outputdirectoryAttribute;

    @XmlElement(name = "javaunicodeescape")
    public boolean javaunicodeescapeAttribute;

    @XmlElement(name = "cachetokens")
    public boolean cachetokensAttribute;

    @XmlElement(name = "static")
    public boolean staticAttribute;

    @XmlElement(name = "usertokenmanager")
    public boolean usertokenmanagerAttribute;

    @XmlElement(name = "target")
    public java.io.File targetAttribute;

    @XmlElement(name = "ignorecase")
    public boolean ignorecaseAttribute;

    @XmlElement(name = "errorreporting")
    public boolean errorreportingAttribute;
}
