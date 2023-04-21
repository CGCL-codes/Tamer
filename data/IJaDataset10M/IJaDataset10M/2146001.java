package backend.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "getPagarTitulo", namespace = "http://backend/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getPagarTitulo", namespace = "http://backend/")
public class GetPagarTitulo {

    @XmlElement(name = "codigoBarras", namespace = "")
    private String codigoBarras;

    /**
     * 
     * @return
     *     returns String
     */
    public String getCodigoBarras() {
        return this.codigoBarras;
    }

    /**
     * 
     * @param codigoBarras
     *     the value for the codigoBarras property
     */
    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }
}
