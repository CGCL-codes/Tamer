package de.ios.framework.db;

import java.io.*;
import de.ios.framework.basic.EUCalendar;

public class DBDateAttr extends DBStringAttr {

    public DBDateAttr() {
        super();
    }

    /**
   * Konstruktor mit Angabe des Spaltennamens
   */
    public DBDateAttr(DBObject o, String name, int key) {
        super(o, name, key);
    }

    /**
   * Wert auslesen
   */
    public String get() {
        if (value == null) return new String();
        return EUCalendar.compressedDateToDate(new String(value, 0));
    }

    /**
   * Wert setzen
   */
    public void set(String v) {
        if (v != null) {
            if (v.indexOf('.') > 0) value = EUCalendar.dateToCompressedDate(v).trim().getBytes(); else value = v.trim().getBytes();
        } else value = null;
        isValueSet = true;
    }

    /**
   * Wert als SQL-String liefern. (Implementiert DBAttribute.SQLValue)
   */
    public String SQLValue() {
        return SQLStringValue(new String(value, 0));
    }
}

;
