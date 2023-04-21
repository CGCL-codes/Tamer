package org.datanucleus.metadata;

import java.io.Serializable;

/**
 * Representation of the primary query languages.
 * Other query languages can be supported but this is just for the primary ones as shortcuts.
 */
public class QueryLanguage implements Serializable {

    /** language="JDOQL" */
    public static final QueryLanguage JDOQL = new QueryLanguage(1);

    /** language="SQL" */
    public static final QueryLanguage SQL = new QueryLanguage(2);

    /** language="JPQL" */
    public static final QueryLanguage JPQL = new QueryLanguage(3);

    /** language="STOREDPROC" */
    public static final QueryLanguage STOREDPROC = new QueryLanguage(4);

    private final int typeId;

    private QueryLanguage(int i) {
        this.typeId = i;
    }

    public int hashCode() {
        return typeId;
    }

    public boolean equals(Object o) {
        if (o instanceof QueryLanguage) {
            return ((QueryLanguage) o).typeId == typeId;
        }
        return false;
    }

    /**
     * Returns a string representation of the object.
     * @return a string representation of the object.
     */
    public String toString() {
        switch(typeId) {
            case 1:
                return "JDOQL";
            case 2:
                return "SQL";
            case 3:
                return "JPQL";
            case 4:
                return "STOREDPROC";
        }
        return "";
    }

    /**
     * Accessor to the query language type
     * @return the type
     */
    public int getType() {
        return typeId;
    }

    /**
     * Return QueryLanguage from String.
     * @param value identity-type attribute value
     * @return Instance of QueryLanguage. If parse failed, return null.
     */
    public static QueryLanguage getQueryLanguage(final String value) {
        if (value == null) {
            return QueryLanguage.JDOQL;
        } else if (QueryLanguage.JDOQL.toString().equalsIgnoreCase(value)) {
            return QueryLanguage.JDOQL;
        } else if (QueryLanguage.SQL.toString().equalsIgnoreCase(value)) {
            return QueryLanguage.SQL;
        } else if (QueryLanguage.JPQL.toString().equalsIgnoreCase(value)) {
            return QueryLanguage.JPQL;
        } else if (QueryLanguage.STOREDPROC.toString().equalsIgnoreCase(value)) {
            return QueryLanguage.STOREDPROC;
        }
        return null;
    }
}
