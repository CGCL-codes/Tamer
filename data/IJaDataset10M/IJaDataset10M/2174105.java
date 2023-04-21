package net.brutex.xmlbridge.ws.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "RenameExtensions", namespace = "http://ws.xmlbridge.brutex.net/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RenameExtensions", namespace = "http://ws.xmlbridge.brutex.net/")
public class InvokeRenameExtensions {

    @XmlElement(name = "arg0", namespace = "")
    private RenameExtensions arg0;

    /**
     * 
     * @return
     *     returns RenameExtensions
     */
    public RenameExtensions getArg0() {
        return this.arg0;
    }

    /**
     * 
     * @param arg0
     *     the value for the arg0 property
     */
    public void setArg0(RenameExtensions arg0) {
        this.arg0 = arg0;
    }
}
