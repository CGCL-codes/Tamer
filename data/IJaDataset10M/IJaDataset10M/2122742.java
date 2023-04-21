package org.pwr.odwa.common.metadata;

import java.io.Serializable;

/** Klasa bazowa dla klas będących elementami bazy metadanych.
 *
 *  Każdy element bazy metadanych cechuje się trzema podstawowymi
 *  atrybutami:
 *
 *   [1] Unikalne ID.
 *   [2] Przyjazna dla urzytkownika nazwa.
 *   [3] Opis przeznaczenia danego elementu bazy metadanych.
 *
 *  Zawartość poszczególnych atrybutów może być ustalana jedynie w
 *  module metadaych, natomias moduły korzystające z elementów bazy
 *  metadanych (głównie moduł GUI) może jedynie czytać zawartość
 *  atrybutów.
 *
 *  @author Mateusz Paprocki
 *  @author Maria Łabaziewicz
 */
public class MetaElement implements Serializable {

    protected MetaID m_id = null;

    protected String m_name = null;

    protected String m_notes = null;

    public void setID(MetaID id) {
        m_id = id;
    }

    public void setName(String name) {
        m_name = name;
    }

    public void setNotes(String notes) {
        m_notes = notes;
    }

    public MetaID getID() {
        return m_id;
    }

    public String getName() {
        return m_name;
    }

    public String getNotes() {
        return m_notes;
    }

    public String toString() {
        return "SLOT(" + m_id + ", " + m_name + ", " + m_notes + ")";
    }
}
