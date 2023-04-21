package org.norecess.nolatte;

import org.norecess.nolatte.ast.Datum;

/**
 * @author Jeremy D. Frens
 */
public class NoLatteTypeException extends RuntimeException {

    private static final long serialVersionUID = -3712090704950270639L;

    private final int myLine;

    private final String myWhere;

    private final Datum myDatum;

    private final Class<? extends Datum> myExpectedType;

    public NoLatteTypeException(int line, String where, Datum datum, Class<? extends Datum> expectedType) {
        super(datum + " of type " + datum.getClass().getName() + ", expected type " + expectedType.getName());
        myLine = line;
        myWhere = where;
        myDatum = datum;
        myExpectedType = expectedType;
    }

    public Datum getDatum() {
        return myDatum;
    }

    public String getWhere() {
        return myWhere;
    }

    public Class<? extends Datum> getExpectedType() {
        return myExpectedType;
    }

    public int getLineNumber() {
        return myLine;
    }
}
