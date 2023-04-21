package com.jvantage.ce.facilities.relationship;

import java.io.*;
import java.util.*;

public class O2MRelationshipSet implements java.io.Serializable {

    private Vector mRelationships = null;

    /**
     * ForeignKeyRelationshipSet constructor comment.
     */
    public O2MRelationshipSet() {
        super();
        mRelationships = new Vector();
    }

    /**
     * This method was created in VisualAge.
     */
    public void addO2MRelationship(O2MRelationship relationship) {
        mRelationships.addElement(relationship);
    }

    public boolean containsRelationshipWithManyToOneName(String name) {
        if ((name == null) || (mRelationships == null)) {
            return false;
        }
        Enumeration en = mRelationships.elements();
        while (en.hasMoreElements()) {
            O2MRelationship rel = (O2MRelationship) en.nextElement();
            if (name.equalsIgnoreCase(rel.getManySideRelationshipName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines whether more than one relationship from the one-side
     * of this relationship points to the same foreign table.
     * 
     * @param tableName
     * @return true if more than one relationship points from the one side
     *         of this relationship to the same foreign table.
     */
    public boolean containsMultipleReferencesToManySideTable(String tableName) {
        if ((tableName == null) || (mRelationships == null)) {
            return false;
        }
        int refCount = 0;
        Enumeration en = mRelationships.elements();
        while (en.hasMoreElements()) {
            O2MRelationship rel = (O2MRelationship) en.nextElement();
            if (tableName.equalsIgnoreCase(rel.getManySideTableName())) {
                if (++refCount > 1) return true;
            }
        }
        return false;
    }

    /**
     * Determines whether more than one relationship from the many-side
     * of this relationship points to the same foreign table (one-side).
     * 
     * @param tableName
     * @return true if more than one relationship points from the one side
     *         of this relationship to the same foreign table.
     */
    public boolean containsMultipleReferencesToOneSideTable(String tableName) {
        if ((tableName == null) || (mRelationships == null)) {
            return false;
        }
        int refCount = 0;
        Enumeration en = mRelationships.elements();
        while (en.hasMoreElements()) {
            O2MRelationship rel = (O2MRelationship) en.nextElement();
            if (tableName.equalsIgnoreCase(rel.getOneSideTableName())) {
                if (++refCount > 1) return true;
            }
        }
        return false;
    }

    public boolean containsRelationshipWithOneToManyName(String name) {
        if ((name == null) || (mRelationships == null)) {
            return false;
        }
        Enumeration en = mRelationships.elements();
        while (en.hasMoreElements()) {
            O2MRelationship rel = (O2MRelationship) en.nextElement();
            if (name.equalsIgnoreCase(rel.getOneSideRelationshipName())) {
                return true;
            }
        }
        return false;
    }

    public Vector getAllManySideRelationshipNames() {
        Vector names = new Vector();
        Enumeration en = mRelationships.elements();
        while (en.hasMoreElements()) {
            O2MRelationship rel = (O2MRelationship) en.nextElement();
            if (rel.getManySideRelationshipName() != null) {
                names.addElement(rel.getManySideRelationshipName());
            }
        }
        return names;
    }

    public Vector getAllOneSideRelationshipNames() {
        Vector names = new Vector();
        Enumeration en = mRelationships.elements();
        while (en.hasMoreElements()) {
            O2MRelationship rel = (O2MRelationship) en.nextElement();
            if (rel.getOneSideRelationshipName() != null) {
                names.addElement(rel.getOneSideRelationshipName());
            }
        }
        return names;
    }

    /**
     * This method was created in VisualAge.
     */
    public Vector getRelationshipsAsVector() {
        return mRelationships;
    }

    public O2MRelationship getRelationshipWithManyToOneName(String name) {
        if ((name == null) || (mRelationships == null)) {
            return null;
        }
        Enumeration en = mRelationships.elements();
        while (en.hasMoreElements()) {
            O2MRelationship rel = (O2MRelationship) en.nextElement();
            if (name.equalsIgnoreCase(rel.getManySideRelationshipName())) {
                return rel;
            }
        }
        return null;
    }

    public O2MRelationship getRelationshipWithOneToManyName(String name) {
        if ((name == null) || (mRelationships == null)) {
            return null;
        }
        Enumeration en = mRelationships.elements();
        while (en.hasMoreElements()) {
            O2MRelationship rel = (O2MRelationship) en.nextElement();
            if (name.equalsIgnoreCase(rel.getOneSideRelationshipName())) {
                return rel;
            }
        }
        return null;
    }

    public boolean isEmpty() {
        return mRelationships.isEmpty();
    }

    public boolean isNotEmpty() {
        return !mRelationships.isEmpty();
    }

    public int size() {
        return mRelationships.size();
    }

    /**
     * This method was created in VisualAge.
     */
    public String toString() {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        pw.println();
        if (mRelationships == null) {
            pw.println("There are no relationships in this O2MRelationshipSet.");
            pw.println();
            pw.flush();
            pw.close();
            return sw.toString();
        }
        int index = 0;
        int cardinality = mRelationships.size();
        if (cardinality == 0) {
            pw.println("There are no relationships in this relationshipSet.");
        }
        Enumeration en = mRelationships.elements();
        while (en.hasMoreElements()) {
            pw.println("Showing relationship " + ++index + " of " + cardinality + ".");
            pw.println(en.nextElement().toString());
        }
        pw.flush();
        pw.close();
        return sw.toString();
    }
}
