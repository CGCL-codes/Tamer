package com.antlersoft.analyzer;

import com.antlersoft.odb.ObjectDB;

/**
 * Catch clause in a method
 * @author Michael A. MacDonald
 *
 */
public class DBCatch extends DBReference {

    public static final String CATCH_TARGET = "CATCH_TARGET";

    /**
	 * @param s Method containing catch
	 * @param caught Exception class caught
	 * @param l line number
	 */
    public DBCatch(DBMethod s, DBClass caught, int l) {
        super(s, caught, l);
        ObjectDB.makePersistent(this);
    }

    public DBClass getCaught() {
        return (DBClass) target.getReferenced();
    }

    public String toString() {
        return "Catch of " + getCaught().toString() + " from " + getSource().toString() + " at line " + String.valueOf(lineNumber);
    }

    public int hashCode() {
        return getSource().hashCode() ^ lineNumber ^ target.hashCode();
    }

    public boolean equals(Object toCompare) {
        if (toCompare instanceof DBCatch) {
            DBCatch f = (DBCatch) toCompare;
            return f.getSource().equals(getSource()) && f.lineNumber == lineNumber && f.getCaught().equals(getCaught());
        }
        return false;
    }
}
