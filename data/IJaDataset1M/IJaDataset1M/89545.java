package net.brutex.xmlbridge.ws.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "BZip2", namespace = "http://ws.xmlbridge.brutex.net/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BZip2", namespace = "http://ws.xmlbridge.brutex.net/")
public class InvokeBZip2 {

    @XmlElement(name = "arg0", namespace = "")
    private BZip2 arg0;

    /**
     * 
     * @return
     *     returns BZip2
     */
    public BZip2 getArg0() {
        return this.arg0;
    }

    /**
     * 
     * @param arg0
     *     the value for the arg0 property
     */
    public void setArg0(BZip2 arg0) {
        this.arg0 = arg0;
    }
}
