package backend.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "getDadosConta", namespace = "http://backend/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getDadosConta", namespace = "http://backend/", propOrder = { "idConta", "senha" })
public class GetDadosConta {

    @XmlElement(name = "idConta", namespace = "")
    private String idConta;

    @XmlElement(name = "senha", namespace = "")
    private String senha;

    /**
     * 
     * @return
     *     returns String
     */
    public String getIdConta() {
        return this.idConta;
    }

    /**
     * 
     * @param idConta
     *     the value for the idConta property
     */
    public void setIdConta(String idConta) {
        this.idConta = idConta;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getSenha() {
        return this.senha;
    }

    /**
     * 
     * @param senha
     *     the value for the senha property
     */
    public void setSenha(String senha) {
        this.senha = senha;
    }
}
