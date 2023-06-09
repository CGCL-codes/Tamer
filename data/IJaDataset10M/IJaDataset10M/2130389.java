package com.myJava.util.errors;

import java.util.Hashtable;
import com.myJava.util.collections.SimpleEnumeration;

public class ActionError implements SimpleEnumeration {

    /**
     * Code
     */
    private int code;

    private String message;

    /**
     * Liste de champs associ�s � l'erreur
     */
    private Hashtable fields;

    /**
     * Constructeur
     */
    public ActionError(int code) {
        this.code = code;
        this.fields = new Hashtable();
    }

    /**
     * Constructeur
     */
    public ActionError(int code, String message) {
        this.code = code;
        this.message = message;
        this.fields = new Hashtable();
    }

    public String getMessage() {
        return message;
    }

    /**
     * Ajout d'un champ
     */
    public void addField(String key, Object value) {
        this.fields.put(key, value);
    }

    /**
     * Retourne la valeur du champ
     */
    public Object getValue(String key) {
        return this.fields.get(key);
    }

    /**
     * Retourne la valeur du champ sous forme de cha�ne de caract�res
     */
    public String getString(String key) {
        return this.getValue(key).toString();
    }

    /**
     * Retourne le code d'erreur
     */
    public int getCode() {
        return this.code;
    }

    /**
     * Red�finition toString
     */
    public String toString() {
        return "" + this.code;
    }
}
