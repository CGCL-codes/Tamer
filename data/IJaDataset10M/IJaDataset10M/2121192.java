package com.centropresse.dto;

import java.io.*;

public class Cliente implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private String id_cliente;

    private String id_user;

    private String username_cliente;

    private int accessLevel;

    private String nome_cliente;

    private String cognome_cliente;

    private String indirizzo_cliente;

    private String provincia_cliente;

    private String citta_cliente;

    private String cap_cliente;

    private String ragioneSociale;

    private String telefono_cliente;

    private String altrotelefono_cliente;

    private String postaelettronica_cliente;

    private String cfpiva_cliente;

    public Cliente() {
    }

    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
    }

    public String getTelefono_cliente() {
        return telefono_cliente;
    }

    public String getProvincia_cliente() {
        return provincia_cliente;
    }

    public String getPostaelettronica_cliente() {
        return postaelettronica_cliente;
    }

    public String getNome_cliente() {
        return nome_cliente;
    }

    public String getIndirizzo_cliente() {
        return indirizzo_cliente;
    }

    public String getCognome_cliente() {
        return cognome_cliente;
    }

    public String getCitta_cliente() {
        return citta_cliente;
    }

    public String getCfpiva_cliente() {
        return cfpiva_cliente;
    }

    public String getCap_cliente() {
        return cap_cliente;
    }

    public String getAltrotelefono_cliente() {
        return altrotelefono_cliente;
    }

    public void setAltrotelefono_cliente(String altrotelefono_cliente) {
        this.altrotelefono_cliente = altrotelefono_cliente;
    }

    public void setCap_cliente(String cap_cliente) {
        this.cap_cliente = cap_cliente;
    }

    public void setCfpiva_cliente(String cfpiva_cliente) {
        this.cfpiva_cliente = cfpiva_cliente;
    }

    public void setCitta_cliente(String citta_cliente) {
        this.citta_cliente = citta_cliente;
    }

    public void setCognome_cliente(String cognome_cliente) {
        this.cognome_cliente = cognome_cliente;
    }

    public void setIndirizzo_cliente(String indirizzo_cliente) {
        this.indirizzo_cliente = indirizzo_cliente;
    }

    public void setNome_cliente(String nome_cliente) {
        this.nome_cliente = nome_cliente;
    }

    public void setPostaelettronica_cliente(String postaelettronica_cliente) {
        this.postaelettronica_cliente = postaelettronica_cliente;
    }

    public void setProvincia_cliente(String provincia_cliente) {
        this.provincia_cliente = provincia_cliente;
    }

    public void setTelefono_cliente(String telefono_cliente) {
        this.telefono_cliente = telefono_cliente;
    }

    public String getId_cliente() {
        return id_cliente;
    }

    public String getRagioneSociale() {
        return ragioneSociale;
    }

    public void setId_cliente(String id_cliente) {
        this.id_cliente = id_cliente;
    }

    public void setRagioneSociale(String ragioneSociale) {
        this.ragioneSociale = ragioneSociale;
    }

    /**
 * @return the accessLevel
 */
    public int getAccessLevel() {
        return accessLevel;
    }

    /**
 * @param accessLevel the accessLevel to set
 */
    public void setAccessLevel(int accessLevel) {
        this.accessLevel = accessLevel;
    }

    /**
 * @return the username_cliente
 */
    public String getUsername_cliente() {
        return username_cliente;
    }

    /**
 * @param username_cliente the username_cliente to set
 */
    public void setUsername_cliente(String username_cliente) {
        this.username_cliente = username_cliente;
    }

    /**
 * @return the id_user
 */
    public String getId_user() {
        return id_user;
    }

    /**
 * @param id_user the id_user to set
 */
    public void setId_user(String id_user) {
        this.id_user = id_user;
    }
}
