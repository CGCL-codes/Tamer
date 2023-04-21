package hambo.svc.database;

/**
 * Oracle implementation of the DBQueryLibrary.
 */
class DBQueryLibraryOracleImpl implements DBQueryLibrary {

    /**
     * Will create a String to be used to get the last inserted OID on
     * the table given by the tableName paramenter.
     * 
     * Eample: 
     * <pre>getQueryCurrentOID(con, "my_table", "newoid")</pre>
     * Will return
     * <pre>select my_table_seq.curval as newoid from dual</pre>
     *
     * @param tableName the name of the table
     * @param fieldName the name of the field name to assign the last
     *        OID.
     * 
     */
    public String getQueryCurrentOID(String tableName, String fieldName) {
        StringBuffer str = new StringBuffer("select ");
        str.append(tableName).append("_S.currval as ").append(fieldName).append(" from dual");
        return str.toString();
    }

    /**
     * This method will create a sql query String with a bit and
     * operation on two Strings
     *
     * Example:
     * <pre>getQueryBitAnd("col1", "16")</pre>
     * will return 
     * <pre>bitand(col1,16)</pre>
     *  
     */
    public String getQueryBitAnd(String fieldName, String value) {
        return " bitand(" + fieldName + "," + value + ") ";
    }

    public String getQueryJoin(String col1, String col2) {
        return " " + col1 + " = " + col2 + "(+) ";
    }

    public String getQueryUpper(String str) {
        return " upper(" + str + ") ";
    }

    public String getQueryDateDiff(int unit, String field1, String field2) {
        StringBuffer buf = new StringBuffer(" ((");
        buf.append(field2).append(" - ").append(field1);
        switch(unit) {
            case java.util.Calendar.YEAR:
                buf.append(") / 365.25)");
                break;
            case java.util.Calendar.DAY_OF_YEAR:
            case java.util.Calendar.DAY_OF_MONTH:
            case java.util.Calendar.DAY_OF_WEEK:
                buf.append("))");
                break;
            case java.util.Calendar.HOUR:
                buf.append(") * 24)");
                break;
            case java.util.Calendar.MINUTE:
                buf.append(") * 24 * 60)");
                break;
            case java.util.Calendar.SECOND:
                buf.append(") * 24 * 60 * 60)");
                break;
            default:
                throw new IllegalArgumentException("Bad time unit: " + unit);
        }
        return buf.toString();
    }
}
