package org.kwantu.wesemo.jaxws.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "isUsernameAvailableResponse", namespace = "http://jaxws.wesemo.kwantu.org/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "isUsernameAvailableResponse", namespace = "http://jaxws.wesemo.kwantu.org/")
public class IsUsernameAvailableResponse {

    @XmlElement(name = "return", namespace = "")
    private boolean _return;

    /**
     * 
     * @return
     *     returns boolean
     */
    public boolean get_return() {
        return this._return;
    }

    /**
     * 
     * @param _return
     *     the value for the _return property
     */
    public void set_return(boolean _return) {
        this._return = _return;
    }
}
