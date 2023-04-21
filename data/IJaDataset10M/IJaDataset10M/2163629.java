package org.jcompany.control.adm;

import java.io.Serializable;
import java.util.Date;

/**
 * jCompany. Value Object. Encapsula dados de um aviso
 * @since jCompany 3.0
 * @version $Id: PlcWarningOnlineEntity.java,v 1.2 2006/05/17 20:38:13 rogerio_baldini Exp $
 */
public class PlcWarningOnlineEntity implements Serializable {

    private static final long serialVersionUID = 5355404325485681253L;

    private Long id;

    private String warning = "";

    private String loginAuthor;

    private Date since = new java.util.Date();

    private Date expiration;

    private String criticalFactor = "normal";

    public PlcWarningOnlineEntity() {
    }

    /**
	 * @return Mensagem em formato HTML
	 */
    public String getWarning() {
        return warning;
    }

    /**
	 * @return Data de cria��o da mensagem
	 */
    public Date getSince() {
        return since;
    }

    /**
	 * @return Data marcada de expira��o do aviso
	 */
    public Date getExpiration() {
        return expiration;
    }

    /**
	 * @return Fator Cr�tico da mensagem escolhida pela autor, podendo ser
	 * 'N'-Normal, 'U'-Urgente, 'E'-Emerg�ncia
	 */
    public String getCriticalFactor() {
        return criticalFactor;
    }

    /**
	 * @return Identificador da mensagem, que � um sequencial gerado
	 * internamente
	 */
    public Long getId() {
        return id;
    }

    /**
	 * @return Login (username) do autor da mensagem
	 */
    public String getLoginAuthor() {
        return loginAuthor;
    }

    /**
	 * @param string
	 */
    public void setWarning(String warning) {
        this.warning = warning;
    }

    /**
	 * @param date
	 */
    public void setSince(Date since) {
        this.since = since;
    }

    /**
	 * @param date
	 */
    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    /**
	 * @param string
	 */
    public void setCriticalFactor(String criticalFactor) {
        this.criticalFactor = criticalFactor;
    }

    /**
	 * @param long1
	 */
    public void setId(Long long1) {
        id = long1;
    }

    /**
	 * @param login
	 */
    public void setLoginAuthor(String loginAuthor) {
        this.loginAuthor = loginAuthor;
    }
}
