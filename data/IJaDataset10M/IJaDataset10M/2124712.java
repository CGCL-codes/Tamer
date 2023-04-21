package com.jvantage.ce.facilities.relationship;

import java.io.*;
import java.util.*;
import org.apache.commons.lang.StringUtils;

public class M2MRelationshipSet implements java.io.Serializable {

    private Hashtable<String, M2MRelationship> mRelationships = null;

    public M2MRelationshipSet() {
        super();
        mRelationships = new Hashtable<String, M2MRelationship>();
    }

    public void addRelationship(M2MRelationship relationship) {
        if (relationship == null) {
            return;
        }
        String name = relationship.getName();
        if (name == null) {
            return;
        }
        mRelationships.put(name.toUpperCase(), relationship);
    }

    public void clear() {
        mRelationships.clear();
    }

    /**
     * This method returns exactly zero (null) or one M2MRelationship that involves
     * the argument tableName.  The intended use for this method is as a convenience
     * when the developer knows that a given table only has one m2m relationship, but
     * does not necessarily know, or want to go to the effort to find out what the
     * relationship name is for that table in order to lookup assocated objects that
     * are involved in the relationship on an instance basis.
     * 
     * @param tableName
     * @return exactly one M2MRelationship instance that involves the argument tableName.
     */
    public M2MRelationship getRelationshipInvolvingTableName(String tableName) throws RelationshipFacilitiesException {
        if (StringUtils.isBlank(tableName)) {
            return null;
        }
        Vector<M2MRelationship> relVector = getRelationshipsInvolvingTableName(tableName);
        if (relVector == null) {
            return null;
        }
        if (relVector.size() < 1) {
            return null;
        }
        if (relVector.size() > 1) {
            StringBuffer msg = new StringBuffer();
            msg.append("Table ").append(tableName).append(" is involved in ").append(relVector.size()).append(" relationships.  This method is intended to specifically return exactly one relationship ").append(" and throw an exception if more than one relationship involving the argument table is found.");
            throw new RelationshipFacilitiesException(msg.toString());
        }
        return relVector.firstElement();
    }

    /**
     * 
     * @param tableName
     * @return Returns a list of M2MRelationships that include the argument tableName on either
     * side, left or right hand side.
     */
    public Vector<M2MRelationship> getRelationshipsInvolvingTableName(String tableName) {
        if (StringUtils.isBlank(tableName)) {
            return null;
        }
        Vector<M2MRelationship> relVector = new Vector<M2MRelationship>();
        M2MRelationship relationship = null;
        Enumeration en = mRelationships.elements();
        while (en.hasMoreElements()) {
            relationship = (M2MRelationship) en.nextElement();
            if (relationship != null) {
                if (tableName.equalsIgnoreCase(relationship.getLHSTableName()) || tableName.equalsIgnoreCase(relationship.getRHSTableName())) {
                    relVector.add(relationship);
                }
            }
        }
        return relVector;
    }

    /**
     * Checks to see if the argument tableName is used at least once on both the
     * LHS and RHS in the set.  This does not mean the table must be on both sides
     * of the same relationship (although it can), it means that the table appears
     * at least once on each side among all the relationships.
     * @param tableName
     * @return
     */
    public boolean tableAppearsInLocalAndRemoteRelationships(String tableName) {
        if (StringUtils.isBlank(tableName)) {
            return false;
        }
        return tableNameAppearsOnLhsOfAtLeastOneRelationship(tableName) && tableNameAppearsOnRhsOfAtLeastOneRelationship(tableName);
    }

    /**
     * Checks to see if the argument tableName appears on the Left Hand Side (lhs)
     * of any relationships in the set.
     * 
     * @param tableName
     * @return true or false;
     */
    public boolean tableNameAppearsOnLhsOfAtLeastOneRelationship(String tableName) {
        return tableNameAppearsInAtLeastOneRelationship(tableName, true);
    }

    /**
     * Checks to see if the argument tableName appears on the Right Hand Side (rhs)
     * of any relationships in the set.
     * 
     * @param tableName
     * @return true or false;
     */
    public boolean tableNameAppearsOnRhsOfAtLeastOneRelationship(String tableName) {
        return tableNameAppearsInAtLeastOneRelationship(tableName, false);
    }

    private boolean tableNameAppearsInAtLeastOneRelationship(String tableName, boolean checkLhsSide) {
        if (StringUtils.isBlank(tableName)) {
            return false;
        }
        M2MRelationship relationship = null;
        Enumeration en = mRelationships.elements();
        while (en.hasMoreElements()) {
            relationship = (M2MRelationship) en.nextElement();
            if (relationship != null) {
                if (tableName.equalsIgnoreCase(checkLhsSide ? relationship.getLHSTableName() : relationship.getRHSTableName())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @param tableName - tableName we're checking.
     * @return true if there are more than one relationships in the set with
     * the same table on the left hand side of the relationship.
     */
    public boolean hasMoreThanOneRelationshipWithLhsTableName(String tableName) {
        return hasMoreThanOneRelationshipWithTableName(tableName, true);
    }

    /**
     * @param tableName - tableName we're checking.
     * @return true if there are more than one relationships in the set with
     * the same table on the right hand side of the relationship.
     */
    public boolean hasMoreThanOneRelationshipWithRhsTableName(String tableName) {
        return hasMoreThanOneRelationshipWithTableName(tableName, false);
    }

    /**
     * 
     * @param tableName - tableName we're checking
     * @param checkLhsSide - if true, the tableName is compared against the lhs table,
     * otherwise the tableName is compared against the rhs table name.
     * @return true if there are more than one relationships in the set with
     * the same table on the left or right hand side of the relationship.
     */
    private boolean hasMoreThanOneRelationshipWithTableName(String tableName, boolean checkLhsSide) {
        if (StringUtils.isBlank(tableName)) {
            return false;
        }
        boolean isMultipleTables = false;
        M2MRelationship relationship = null;
        Enumeration en = mRelationships.elements();
        while (en.hasMoreElements()) {
            relationship = (M2MRelationship) en.nextElement();
            if (relationship != null) {
                if (tableName.equalsIgnoreCase(checkLhsSide ? relationship.getLHSTableName() : relationship.getRHSTableName())) {
                    if (isMultipleTables) {
                        return true;
                    } else {
                        isMultipleTables = true;
                    }
                }
            }
        }
        return false;
    }

    public boolean containsRelationshipWithName(String name) {
        if (name == null) {
            return false;
        }
        return mRelationships.containsKey(name.toUpperCase());
    }

    public Vector getForeignRelationshipNames(long tableID) {
        Vector names = new Vector();
        Enumeration relationshipKeys = mRelationships.keys();
        while (relationshipKeys.hasMoreElements()) {
            String key = (String) relationshipKeys.nextElement();
            M2MRelationship rel = (M2MRelationship) mRelationships.get(key);
            names.add(rel.getForeignRelationshipName(tableID));
        }
        return names;
    }

    public M2MRelationship getRelationship(String name) {
        if (name == null) {
            return null;
        }
        return (M2MRelationship) mRelationships.get(name.toUpperCase());
    }

    public Vector getRelationshipNames() {
        Vector names = new Vector();
        Enumeration keys = mRelationships.keys();
        while (keys.hasMoreElements()) {
            names.add((String) keys.nextElement());
        }
        return names;
    }

    public Vector<M2MRelationship> getRelationshipsAsVector() {
        Vector<M2MRelationship> relVector = new Vector<M2MRelationship>();
        Enumeration<String> keys = mRelationships.keys();
        while (keys.hasMoreElements()) {
            relVector.add(mRelationships.get(keys.nextElement()));
        }
        return relVector;
    }

    public boolean isEmpty() {
        return mRelationships.isEmpty();
    }

    public boolean isNotEmpty() {
        return !mRelationships.isEmpty();
    }

    public void removeRelationship(String name) {
        if (name == null) {
            return;
        }
        mRelationships.remove(name.toUpperCase());
    }

    public int size() {
        return mRelationships.size();
    }

    @Override
    public String toString() {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        pw.println();
        pw.println("Many to Many RelationshipSet contains [" + mRelationships.size() + "] elements.");
        int i = 0;
        Enumeration<M2MRelationship> en = mRelationships.elements();
        while (en.hasMoreElements()) {
            M2MRelationship rel = en.nextElement();
            pw.println(++i + "> " + rel.toString());
        }
        pw.flush();
        pw.close();
        return sw.toString();
    }
}
