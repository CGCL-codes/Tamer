package uk.gov.dti.og.fox.dbinterface2;

import uk.gov.dti.og.fox.track.Track;

public class DbResultColumn extends Track {

    public final String mColumnName;

    public final int mColumnType;

    public final String mColumnTypeName;

    public final String mColumnClass;

    public String mDomDatatype = null;

    protected DbResultColumn(String pColumnName, int pColumnType, String pColumnTypeName, String pColumnClass) {
        mColumnName = pColumnName;
        mColumnType = pColumnType;
        mColumnTypeName = pColumnTypeName;
        mColumnClass = pColumnClass;
    }
}
