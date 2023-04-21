package org.wybecom.talk.jtapi.ws.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "DoNotDisturb", namespace = "http://ws.jtapi.talk.wybecom.org/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DoNotDisturb", namespace = "http://ws.jtapi.talk.wybecom.org/")
public class DoNotDisturb {

    @XmlElement(name = "caller", namespace = "")
    private String caller;

    /**
     * 
     * @return
     *     returns String
     */
    public String getCaller() {
        return this.caller;
    }

    /**
     * 
     * @param caller
     *     the value for the caller property
     */
    public void setCaller(String caller) {
        this.caller = caller;
    }
}
