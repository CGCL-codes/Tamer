package org.magicbox.exception;

/**
 * Eccezione per utente non trovato
 * 
 * @author Massimiliano Dess� (desmax74@yahoo.it)
 * @since jdk 1.6.0
 * @version 3.0
 */
@SuppressWarnings("serial")
public class UtenteNonTrovatoException extends Exception {

    public UtenteNonTrovatoException() {
        new Exception("Id Donatore non trovato");
    }
}
