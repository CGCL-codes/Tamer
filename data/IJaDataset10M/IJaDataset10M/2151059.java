package net.brutex.xmlbridge.ws.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "Delete", namespace = "http://ws.xmlbridge.brutex.net/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Delete", namespace = "http://ws.xmlbridge.brutex.net/")
public class InvokeDelete {

    @XmlElement(name = "arg0", namespace = "")
    private Delete arg0;

    /**
     * 
     * @return
     *     returns Delete
     */
    public Delete getArg0() {
        return this.arg0;
    }

    /**
     * 
     * @param arg0
     *     the value for the arg0 property
     */
    public void setArg0(Delete arg0) {
        this.arg0 = arg0;
    }
}
