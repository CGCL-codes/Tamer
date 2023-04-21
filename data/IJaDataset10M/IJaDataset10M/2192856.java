package com.art.anette.exceptions;

/**
 * Repräsentiert einen Fehler der genau dann eintritt, wenn eine Duration
 * auf dem Server eingetragen werden soll, nachdem Ende des entsprechenden
 * Root-WPs.
 *
 * @author Alexander von Bremen-Kühne
 */
public class DurationTooOldException extends Exception {

    /**
     * Erzeugt eine neue DurationTooOld Fehlermeldung mit dem angegebenen Text.
     *
     * @param msg Text der Fehlermeldung.
     */
    public DurationTooOldException(String msg) {
        super(msg);
    }
}
