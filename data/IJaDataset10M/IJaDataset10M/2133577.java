package org.magicbox.domain;

import org.magicbox.util.Cripto;

/**
 * DTO per credenziali
 * 
 * @author Massimiliano Dess� (desmax74@yahoo.it)
 * @since jdk 1.6.0
 * @version 3.0
 */
public class Credenziali {

    public Credenziali() {
        id = new Long(-1);
    }

    public Credenziali(String username, String password) {
        this.username = username;
        this.password = Cripto.stringToSHA(password);
        id = new Long(-1);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = Cripto.stringToSHA(password);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = Cripto.stringToSHA(oldPassword);
    }

    public String getConfermaPassword() {
        return confermaPassword;
    }

    public void setConfermaPassword(String confermaPassword) {
        this.confermaPassword = Cripto.stringToSHA(confermaPassword);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private String username;

    private String password;

    private String confermaPassword;

    private String oldPassword;

    private Long id = 0l;
}
