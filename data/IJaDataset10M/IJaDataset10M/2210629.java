package webserver.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "pauseFTPSiteResponse", namespace = "http://webserver/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "pauseFTPSiteResponse", namespace = "http://webserver/")
public class PauseFTPSiteResponse {

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
