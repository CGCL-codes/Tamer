package Dao;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.math.*;

/**
 * Klasa encji Classification odpowiada tabeli classification w bazie danych.<br />
 * Pola klasy:<br />
 * classId - identyfikator klasyfikacji.<br />
 * className - nazwa klasyfikacji.<br />
 * classDesc - opis klasyfikacji.<br />
 * @author Marek Wilniewiec
 * @author Jacek Raczykowski
 * @author Marcin Łuczak
 */
public class Classification implements Cloneable, Serializable {

    private int classId;

    private int users_userid;

    private String className;

    private String classDesc;

    /**
     * Konstruktor domyślny klasy Classification.
     */
    public Classification() {
    }

    /**
     * Konstruktor sparametryzowany klasy Classification.
     * @param classIdIn ustawia pole klasy classId.
     */
    public Classification(int classIdIn) {
        this.classId = classIdIn;
    }

    /**
     * Zwraca pole identyfikatora classId.
     * @return classID
     */
    public int getClassId() {
        return this.classId;
    }

    /**
     * Ustawia pole identyfikatora classId.
     * @param classIdIn ustawia pole klasy classId.
     */
    public void setClassId(int classIdIn) {
        this.classId = classIdIn;
    }

    public void setUsers_userid(int userId) {
        this.users_userid = userId;
    }

    public int getUsers_userid() {
        return this.users_userid;
    }

    /**
     * Zwraca pole nazwy className.
     * @return className
     */
    public String getClassName() {
        return this.className;
    }

    /**
     * Ustawia pole nazwy className.
     * @param classNameIn ustawia pole klasy className.
     */
    public void setClassName(String classNameIn) {
        this.className = classNameIn;
    }

    /**
     * Zwraca pole opisu classDesc.
     * @return classDesc
     */
    public String getClassDesc() {
        return this.classDesc;
    }

    /**
     * Ustawia pole opisu classDesc.
     * @param classDescIn ustawia pole klasy classDesc.
     */
    public void setClassDesc(String classDescIn) {
        this.classDesc = classDescIn;
    }

    /**
     * Ustawia wszystkie pola klasy.
     * @param classIdIn ustawia pole klasy classId.
     * @param classNameIn ustawia pole klasy className.
     * @param classDescIn ustawia pole klasy classDesc.
     */
    public void setAll(int classIdIn, int userId, String classNameIn, String classDescIn) {
        this.classId = classIdIn;
        this.users_userid = userId;
        this.className = classNameIn;
        this.classDesc = classDescIn;
    }

    /**
     * Sprawdza czy dwa obiekty mają takie samo mapowanie.
     * Obiekt przekazany w parametrze porównywany z obiektem wywołującym metodę.
     * @param valueObject obiekt klasy Classification do porównania.
     */
    public boolean hasEqualMapping(Classification valueObject) {
        if (valueObject.getClassId() != this.classId) {
            return (false);
        }
        if (valueObject.getUsers_userid() != this.getUsers_userid()) {
            return (false);
        }
        if (this.className == null) {
            if (valueObject.getClassName() != null) return (false);
        } else if (!this.className.equals(valueObject.getClassName())) {
            return (false);
        }
        if (this.classDesc == null) {
            if (valueObject.getClassDesc() != null) return (false);
        } else if (!this.classDesc.equals(valueObject.getClassDesc())) {
            return (false);
        }
        return true;
    }

    /**
     * Zwraca pola klasy do wyświetlenia jako String.
     * @return pola klasy w postaci Stringów gotowych do wyświetlenia na standardowym wyjściu.
     */
    public String toString() {
        StringBuffer out = new StringBuffer();
        out.append("Classification: \n");
        out.append("classId = " + this.classId + "\n");
        out.append("userId = " + this.users_userid + "\n");
        out.append("className = " + this.className + "\n");
        out.append("classDesc = " + this.classDesc + "\n");
        return out.toString();
    }

    /**
     * Duplikuje obiekt klasy.
     * @return cloned nowy obiekt wypełniony skopiowanymi danymi.
     */
    public Object clone() {
        Classification cloned = new Classification();
        cloned.setClassId(this.classId);
        cloned.setUsers_userid(this.users_userid);
        if (this.className != null) cloned.setClassName(new String(this.className));
        if (this.classDesc != null) cloned.setClassDesc(new String(this.classDesc));
        return cloned;
    }
}
