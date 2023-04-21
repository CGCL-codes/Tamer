package net.brutex.xmlbridge.ws.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "SoundTask", namespace = "http://ws.xmlbridge.brutex.net/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SoundTask", namespace = "http://ws.xmlbridge.brutex.net/")
public class InvokeSoundTask {

    @XmlElement(name = "arg0", namespace = "")
    private SoundTask arg0;

    /**
     * 
     * @return
     *     returns SoundTask
     */
    public SoundTask getArg0() {
        return this.arg0;
    }

    /**
     * 
     * @param arg0
     *     the value for the arg0 property
     */
    public void setArg0(SoundTask arg0) {
        this.arg0 = arg0;
    }
}
