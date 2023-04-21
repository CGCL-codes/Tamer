package org.jcompany.control.adm;

import java.io.Serializable;
import java.util.Date;

/**
 * jCompany. Value Object. Encapsula dados de uma sess�o corrente, para caching
 * "Quem est� on-line?".
 * @since jCompany 3.0
 * @version $Id: PlcUserOnlineEntity.java,v 1.2 2006/05/17 20:38:13 rogerio_baldini Exp $
 */
public class PlcUserOnlineEntity implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -2190057573650775920L;

    private String idSession = "---";

    private String IP = "---";

    private String login = "An�nimo";

    private Date since = new java.util.Date();

    private Date lastRequest = new java.util.Date();

    private Long memoryUsed = new Long("0");

    private String timeoutUser = "";

    public PlcUserOnlineEntity() {
    }

    public String getIdSession() {
        return idSession;
    }

    public String getIP() {
        return IP;
    }

    public String getLogin() {
        return login;
    }

    public Date getSince() {
        return since;
    }

    public Date getLastRequest() {
        return lastRequest;
    }

    public Long getMemoryUsed() {
        return memoryUsed;
    }

    public String getTimeoutUser() {
        return timeoutUser;
    }

    public void setIdSession(String idSession) {
        this.idSession = idSession;
    }

    public void setIP(String ip) {
        IP = ip;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setSince(Date since) {
        this.since = since;
    }

    public void setLastRequest(Date lastRequest) {
        this.lastRequest = lastRequest;
    }

    public void setMemoryUsed(Long memoryUsed) {
        this.memoryUsed = memoryUsed;
    }

    public void setTimeoutUser(String timeoutUser) {
        this.timeoutUser = timeoutUser;
    }
}
