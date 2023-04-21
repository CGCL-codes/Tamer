package org.magicbox.domain;

/**
 * Interfaccia che descrive un recapito telefonico
 * 
 * @author Massimiliano Dess&igrave; (desmax74@yahoo.it)
 * @since jdk 1.6.0
 * @version 3.0
 */
public interface RecapitoTelefonico {

    /**
     * Indica la capacit� di essere contattaile telefonicamente
     * 
     * @return possibilt� di essere contattato
     */
    public boolean isContattabileViaSms();

    /**
     * Restituisce il numero del cellulare
     * 
     * @return String
     */
    public String getCellulare();

    /**
     * Restituisce il numero del telefono di casa
     * 
     * @return String
     */
    public String getTelefonoCasa();

    /**
     * Restituisce il numero del telefono di domicilio
     * 
     * @return String
     */
    public String getTelefonoDomicilio();

    /**
     * Restituisce il numero del telefono del posto di lavoro
     * 
     * @return String
     */
    public String getTelefonoLavoro();
}
