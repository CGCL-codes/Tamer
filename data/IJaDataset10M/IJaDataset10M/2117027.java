package br.com.jnfe.core;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.xml.transform.Source;

/**
 * Classe base para armazenar o xml assinado.
 * 
 * @author mauriciofernandesdecastro
 */
@javax.persistence.MappedSuperclass
public class AbstractXml implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;

    private Integer version;

    private long internalNumber;

    private String comoXml;

    private Source source;

    /**
     * Chave prim�ria.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Vers�o do registro na base de dados.
     */
    @Version
    public Integer getVersion() {
        return this.version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
	 * N�mero de controle da NFe.
	 * @return
	 */
    public long getInternalNumber() {
        return internalNumber;
    }

    public void setInternalNumber(long internalNumber) {
        this.internalNumber = internalNumber;
    }

    /**
	 * Conte�do processado da NFe.
	 */
    @Column(length = 8192)
    public String getComoXml() {
        return comoXml;
    }

    public void setComoXml(String comoXml) {
        this.comoXml = comoXml;
    }

    /**
	 * <<Transient>> Origem Xml (antes da transforma��o).
	 */
    @Transient
    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }
}
