package backend.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import backend.DadosConta;

@XmlRootElement(name = "getDadosContaResponse", namespace = "http://backend/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getDadosContaResponse", namespace = "http://backend/")
public class GetDadosContaResponse {

    @XmlElement(name = "return", namespace = "")
    private DadosConta _return;

    /**
     * 
     * @return
     *     returns DadosConta
     */
    public DadosConta getReturn() {
        return this._return;
    }

    /**
     * 
     * @param _return
     *     the value for the _return property
     */
    public void setReturn(DadosConta _return) {
        this._return = _return;
    }
}
