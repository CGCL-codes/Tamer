package com.internetcds.jdbc.tds;

import java.util.Vector;
import java.sql.*;
import com.internetcds.jdbc.tds.Column;

/**
 *  Information about the columns in a result set.
 *
 *@author     Craig Spannring
 *@created    17 March 2001
 */
public class Columns {

    private Vector columns = null;

    private int columnCount = 0;

    /**
     *@todo    Description of the Field
     */
    public static final String cvsVersion = "$Id: Columns.java,v 1.2 2001-08-31 12:47:20 curthagenlocher Exp $";

    public Columns() {
        columns = new Vector();
        columnCount = 0;
    }

    public void setName(int columnNumber, String value) {
        resize(columnNumber);
        if (columns.elementAt(columnNumber - 1) == null) {
            columns.setElementAt(new Column(), columnNumber - 1);
        }
        ((Column) (columns.elementAt(columnNumber - 1))).setName(value);
    }

    public void setDisplaySize(int columnNumber, int value) {
        resize(columnNumber);
        ((Column) (columns.elementAt(columnNumber - 1))).setDisplaySize(value);
    }

    public void setLabel(int columnNumber, String value) {
        ((Column) (columns.elementAt(columnNumber - 1))).setLabel(value);
    }

    public void setNativeType(int columnNumber, int value) {
        resize(columnNumber);
        ((Column) (columns.elementAt(columnNumber - 1))).setType(value);
    }

    public void setPrecision(int columnNumber, int value) {
        resize(columnNumber);
        ((Column) (columns.elementAt(columnNumber - 1))).setPrecision(value);
    }

    public void setScale(int columnNumber, int value) {
        resize(columnNumber);
        ((Column) (columns.elementAt(columnNumber - 1))).setScale(value);
    }

    public void setAutoIncrement(int columnNumber, boolean value) {
        resize(columnNumber);
        ((Column) (columns.elementAt(columnNumber - 1))).setAutoIncrement(value);
    }

    public void setNullable(int columnNumber, int value) {
        resize(columnNumber);
        ((Column) (columns.elementAt(columnNumber - 1))).setNullable(value);
    }

    public void setReadOnly(int columnNumber, boolean value) {
        resize(columnNumber);
        ((Column) columns.elementAt(columnNumber - 1)).setReadOnly(value);
    }

    public void setJdbcType(int columnNumber, int jdbcType) throws SQLException {
        try {
            setNativeType(columnNumber, Tds.cvtJdbcTypeToNativeType(jdbcType));
        } catch (TdsException e) {
            e.printStackTrace();
            throw new SQLException("TDS error- " + e.getMessage());
        }
    }

    /**
     *@return    The ColumnCount value
     */
    public int getColumnCount() {
        return columnCount;
    }

    public String getName(int columnNumber) {
        return ((Column) columns.elementAt(columnNumber - 1)).getName();
    }

    public int getDisplaySize(int columnNumber) {
        return ((Column) columns.elementAt(columnNumber - 1)).getDisplaySize();
    }

    public String getLabel(int columnNumber) {
        return ((Column) columns.elementAt(columnNumber - 1)).getLabel();
    }

    public int getNativeType(int columnNumber) {
        return ((Column) columns.elementAt(columnNumber - 1)).getType();
    }

    public int getJdbcType(int index) throws SQLException {
        try {
            return Tds.cvtNativeTypeToJdbcType(getNativeType(index), getDisplaySize(index));
        } catch (TdsException e) {
            e.printStackTrace();
            throw new SQLException("TDS error- " + e.getMessage());
        }
    }

    public int getPrecision(int columnNumber) {
        return ((Column) columns.elementAt(columnNumber - 1)).getPrecision();
    }

    public int getScale(int columnNumber) {
        return ((Column) columns.elementAt(columnNumber - 1)).getScale();
    }

    public boolean isAutoIncrement(int columnNumber) {
        return ((Column) columns.elementAt(columnNumber - 1)).isAutoIncrement();
    }

    public int isNullable(int columnNumber) {
        return ((Column) columns.elementAt(columnNumber - 1)).isNullable();
    }

    public boolean isReadOnly(int columnNumber) {
        return ((Column) columns.elementAt(columnNumber - 1)).isReadOnly();
    }

    /**
     *  merge the data from two instances of Columns. The 4.2 TDS protocol gives
     *  the column information in multiple pieces. Each piece gives us a
     *  specific piece of information for all the columns in the result set. We
     *  must join those pieces of information together to use as the basis for
     *  the ResultSetMetaData class.
     *
     *@param  other
     *@return
     *@exception  TdsException  thrown if the two instances of Columns can't be
     *      merged. This can happen if the number of columns isn't identical or
     *      if there is conflicting data.
     *@todo                     Description of Parameter
     *@todo                     Description of the Returned Value
     */
    public Columns merge(Columns other) throws TdsException {
        int tmp;
        int i;
        if (this.columns.size() != other.columns.size()) {
            throw new TdsException("Confused.  Mismatch in number of columns");
        }
        for (i = 1; i <= columnCount; i++) {
            if (this.getName(i) == null) {
                this.setName(i, other.getName(i));
            } else if (other.getName(i) == null) {
            } else {
                throw new TdsException("Trying to merge two non-null columns");
            }
            if (this.getDisplaySize(i) == -1) {
                this.setDisplaySize(i, other.getDisplaySize(i));
            } else if (other.getDisplaySize(i) == -1) {
            } else {
                throw new TdsException("Trying to merge two non-null columns");
            }
            if (this.getLabel(i) == null) {
                this.setLabel(i, other.getLabel(i));
            } else if (other.getLabel(i) == null) {
            } else {
                throw new TdsException("Trying to merge two non-null columns");
            }
            if (this.getNativeType(i) == -1) {
                this.setNativeType(i, other.getNativeType(i));
            } else if (other.getNativeType(i) == -1) {
            } else {
                throw new TdsException("Trying to merge two non-null columns");
            }
            if (this.getPrecision(i) == -1) {
                this.setPrecision(i, other.getPrecision(i));
            } else if (other.getPrecision(i) == -1) {
            } else {
                throw new TdsException("Trying to merge two non-null columns");
            }
            if (this.getScale(i) == -1) {
                this.setScale(i, other.getScale(i));
            } else if (other.getScale(i) == -1) {
            } else {
                throw new TdsException("Trying to merge two non-null columns");
            }
            if ((this.nullableWasSet(i)) && (other.nullableWasSet(i))) {
                throw new TdsException("Trying to merge two non-null columns");
            } else if ((!this.nullableWasSet(i)) && (other.nullableWasSet(i))) {
                this.setNullable(i, other.isNullable(i));
            } else {
            }
            if ((this.readOnlyWasSet(i)) && (other.readOnlyWasSet(i))) {
                throw new TdsException("Trying to merge two non-null columns");
            } else if ((!this.readOnlyWasSet(i)) && (other.readOnlyWasSet(i))) {
                this.setReadOnly(i, other.isReadOnly(i));
            } else {
            }
            if ((this.autoIncrementWasSet(i)) && (other.autoIncrementWasSet(i))) {
                throw new TdsException("Trying to merge two non-null columns");
            } else if ((!this.autoIncrementWasSet(i)) && (other.autoIncrementWasSet(i))) {
                this.setAutoIncrement(i, other.isAutoIncrement(i));
            } else {
            }
        }
        return this;
    }

    public boolean autoIncrementWasSet(int columnNumber) {
        return ((Column) (columns.elementAt(columnNumber - 1))).autoIncrementWasSet();
    }

    public boolean nullableWasSet(int columnNumber) {
        return (isNullable(columnNumber) != java.sql.ResultSetMetaData.columnNullableUnknown);
    }

    public boolean readOnlyWasSet(int columnNumber) {
        return ((Column) (columns.elementAt(columnNumber - 1))).readOnlyWasSet();
    }

    private void resize(int columnNumber) {
        if (columnNumber > columnCount) {
            columnCount = columnNumber;
        }
        if (columns.size() <= columnNumber) {
            columns.setSize(columnNumber + 1);
        }
        if (columns.elementAt(columnNumber - 1) == null) {
            columns.setElementAt(new Column(), columnNumber - 1);
        }
    }
}
