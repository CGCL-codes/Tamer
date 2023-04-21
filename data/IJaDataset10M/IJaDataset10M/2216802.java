package net.brutex.xmlbridge.ws.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "CallTarget", namespace = "http://ws.xmlbridge.brutex.net/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CallTarget", namespace = "http://ws.xmlbridge.brutex.net/")
public class InvokeCallTarget {

    @XmlElement(name = "arg0", namespace = "")
    private CallTarget arg0;

    /**
     * 
     * @return
     *     returns CallTarget
     */
    public CallTarget getArg0() {
        return this.arg0;
    }

    /**
     * 
     * @param arg0
     *     the value for the arg0 property
     */
    public void setArg0(CallTarget arg0) {
        this.arg0 = arg0;
    }
}
