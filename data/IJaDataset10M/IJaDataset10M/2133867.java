package com.antlersoft.analyzer;

import com.antlersoft.odb.ObjectDB;

public class DBCall extends DBReference {

    static final String CALL_TARGET = "CALL_TARGET";

    DBCall(DBMethod s, DBMethod t, int l) {
        super(s, t, l);
        ObjectDB.makePersistent(this);
    }

    public DBMethod getTarget() {
        return (DBMethod) target.getReferenced();
    }

    public String toString() {
        return "Call to " + getTarget().toString() + " from " + getSource().toString() + " at line " + String.valueOf(lineNumber);
    }

    public int hashCode() {
        return getSource().hashCode() ^ lineNumber ^ target.hashCode();
    }

    public boolean equals(Object toCompare) {
        if (toCompare instanceof DBCall) {
            DBCall f = (DBCall) toCompare;
            return f.getSource().equals(getSource()) && f.lineNumber == lineNumber && f.getTarget().equals(getTarget());
        }
        return false;
    }
}
