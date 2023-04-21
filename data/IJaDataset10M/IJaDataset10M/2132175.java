package org.greenscape.mail.imap;

import org.greenscape.openmail.*;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author smsajid
 */
@Entity
@Table(name = "m_imap_mail")
public class IMAPMail extends MailAccount {

    private static final long serialVersionUID = -7248103260721585529L;

    protected String host;

    protected Integer port;

    protected EncryptionType encryptionType;

    public IMAPMail() {
        type = "IMAP";
    }

    /**
     * @return the host
     */
    @Column(name = "host")
    public String getHost() {
        return host;
    }

    /**
     * @param host the host to set
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * @return the port
     */
    @Column(name = "port")
    public Integer getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(Integer port) {
        this.port = port;
    }

    /**
     * @return the encryptionType
     */
    @Column(name = "encryption")
    public EncryptionType getEncryptionType() {
        return encryptionType;
    }

    /**
     * @param encryptionType the encryptionType to set
     */
    public void setEncryptionType(EncryptionType encryptionType) {
        this.encryptionType = encryptionType;
    }
}
