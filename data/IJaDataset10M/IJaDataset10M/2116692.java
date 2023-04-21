package net.sourceforge.ws_jdbc.jdbc;

public class ResultSetMetaData implements java.sql.ResultSetMetaData {

    net.sourceforge.ws_jdbc.client_stubs.ResultSetMetaData_T theData;

    public ResultSetMetaData(net.sourceforge.ws_jdbc.client_stubs.ResultSetMetaData_T theData) {
        this.theData = theData;
    }

    /*****************************************************
 **********                                 **********
 ********** Beginning of API implementation **********
 **********                                 **********
 *****************************************************/
    public int getColumnCount() throws java.sql.SQLException {
        return theData.getColumnCount();
    }

    public boolean isAutoIncrement(int column) throws java.sql.SQLException {
        return theData.getAutoIncrements()[column - 1];
    }

    public boolean isCaseSensitive(int column) throws java.sql.SQLException {
        return theData.getCaseSensitives()[column - 1];
    }

    public boolean isSearchable(int column) throws java.sql.SQLException {
        return theData.getSearchables()[column - 1];
    }

    public boolean isCurrency(int column) throws java.sql.SQLException {
        return theData.getCurrencies()[column - 1];
    }

    public int isNullable(int column) throws java.sql.SQLException {
        return theData.getNullables()[column - 1];
    }

    public boolean isSigned(int column) throws java.sql.SQLException {
        return theData.getSigneds()[column - 1];
    }

    public int getColumnDisplaySize(int column) throws java.sql.SQLException {
        return theData.getColumnDisplaySizes()[column - 1];
    }

    public String getColumnLabel(int column) throws java.sql.SQLException {
        return theData.getColumnLabels()[column - 1];
    }

    public String getColumnName(int column) throws java.sql.SQLException {
        return theData.getColumnNames()[column - 1];
    }

    public String getSchemaName(int column) throws java.sql.SQLException {
        return theData.getSchemaNames()[column - 1];
    }

    public int getPrecision(int column) throws java.sql.SQLException {
        return theData.getPrecisions()[column - 1];
    }

    public int getScale(int column) throws java.sql.SQLException {
        return theData.getScales()[column - 1];
    }

    public String getTableName(int column) throws java.sql.SQLException {
        return theData.getTableNames()[column - 1];
    }

    public String getCatalogName(int column) throws java.sql.SQLException {
        return theData.getCatalogNames()[column - 1];
    }

    public int getColumnType(int column) throws java.sql.SQLException {
        return theData.getColumnTypes()[column - 1];
    }

    public String getColumnTypeName(int column) throws java.sql.SQLException {
        return theData.getColumnTypeNames()[column - 1];
    }

    public boolean isReadOnly(int column) throws java.sql.SQLException {
        return theData.getReadOnlys()[column - 1];
    }

    public boolean isWritable(int column) throws java.sql.SQLException {
        return theData.getWritables()[column - 1];
    }

    public boolean isDefinitelyWritable(int column) throws java.sql.SQLException {
        return theData.getDefinitelyWritables()[column - 1];
    }

    public String getColumnClassName(int column) throws java.sql.SQLException {
        return theData.getColumnClassNames()[column - 1];
    }

    /*****************************************************
	 **********                                 **********
	 **********    End of API implementation    **********
	 **********                                 **********
	 *****************************************************/
    public void showMetaData() {
        try {
            int columnCount = getColumnCount();
            System.out.println("Number of columns: " + columnCount);
            System.out.println("\n\tAutoIn\tCaseSe\tCurren\tDefWri\tR/O   \tSearch\tSigned\tWritab");
            for (int i = 0; i < columnCount; i++) {
                System.out.print((i + 1));
                System.out.print("\t" + isAutoIncrement(i + 1));
                System.out.print("\t" + isCaseSensitive(i + 1));
                System.out.print("\t" + isCurrency(i + 1));
                System.out.print("\t" + isDefinitelyWritable(i + 1));
                System.out.print("\t" + isReadOnly(i + 1));
                System.out.print("\t" + isSearchable(i + 1));
                System.out.print("\t" + isSigned(i + 1));
                System.out.print("\t" + isWritable(i + 1));
                System.out.println();
            }
            System.out.println("\n\tColDispSize\tColType\tNullable\tPrecision\tScale");
            for (int i = 0; i < columnCount; i++) {
                System.out.print((i + 1));
                System.out.print("\t" + getColumnDisplaySize(i + 1));
                System.out.print("\t\t\t" + getColumnType(i + 1));
                System.out.print("\t\t" + isNullable(i + 1));
                System.out.print("\t\t\t" + getPrecision(i + 1));
                System.out.print("\t\t\t" + getScale(i + 1));
                System.out.println();
            }
            System.out.println("\n\tCatName\tColClassName\t\tColLabel\t\tColName\t\tColTypeName\tSchemaName\tTableName");
            for (int i = 0; i < columnCount; i++) {
                System.out.print((i + 1));
                System.out.print("\t" + getCatalogName(i + 1));
                System.out.print("\t" + getColumnClassName(i + 1));
                System.out.print("\t\t" + getColumnLabel(i + 1));
                if (getColumnLabel(i + 1).length() < 8) System.out.print("\t");
                System.out.print("\t\t" + getColumnName(i + 1));
                if (getColumnLabel(i + 1).length() < 8) System.out.print("\t");
                System.out.print("\t" + getColumnTypeName(i + 1));
                System.out.print("\t\t" + getSchemaName(i + 1));
                System.out.print("\t\t" + getTableName(i + 1));
                System.out.println();
            }
        } catch (java.sql.SQLException e) {
            System.err.println("SQLException: " + e);
        }
    }
}
