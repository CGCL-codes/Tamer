package net.brutex.xmlbridge.ws.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "Expand", namespace = "http://ws.xmlbridge.brutex.net/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Expand", namespace = "http://ws.xmlbridge.brutex.net/")
public class InvokeExpand {

    @XmlElement(name = "arg0", namespace = "")
    private Expand arg0;

    /**
     * 
     * @return
     *     returns Expand
     */
    public Expand getArg0() {
        return this.arg0;
    }

    /**
     * 
     * @param arg0
     *     the value for the arg0 property
     */
    public void setArg0(Expand arg0) {
        this.arg0 = arg0;
    }
}
