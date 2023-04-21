package com.neoworks.jukex.query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.sql.RowSet;
import com.neoworks.jukex.Attribute;
import com.neoworks.jukex.AttributeValue;
import com.neoworks.jukex.TrackStore;
import com.neoworks.jukex.TrackStoreFactory;
import com.neoworks.jukex.sqlimpl.JukeXAttributeValue;

/**
 * This class represents the results of an Attribute query.  It is meant to work
 * using a subset of the result API.
 *
 * All indexes in this class are 1 based to be consistent with the ResultSet/RowSet
 * interfaces, although I believe this to be a moronic way of doing things.
 *
 * @author Nick Vincent <a href="mailto:nick@neoworks.com">nick@neoworks.com</a>
 */
public class AttributeValueResultSet {

    RowSet crs = null;

    Map columns = null;

    List selectAttributes = null;

    /** 
	 * Creates a new instance of AttributeValueResultSet
	 *
	 * @param crs A <b>disconnected</b> RowSet object containing the results from
	 * a Query operation.
	 * @param selectAttributes A List of Attribute objects representing the
	 * ordered SELECT clause from the query that produced these results.
	 */
    AttributeValueResultSet(RowSet crs, List selectAttributes) throws SQLException {
        crs.beforeFirst();
        this.crs = crs;
        this.selectAttributes = selectAttributes;
    }

    /**
	 * Get the attribute value with the specified index from the current row
	 *
	 * @param index The index of the AttributeValue to retrieve
	 * @return The AttributeValue specified
	 */
    public AttributeValue getAttributeValue(int index) throws SQLException {
        AttributeValue retval = null;
        int type = this.crs.getInt((3 * index) - 1);
        Attribute tmpAttribute = TrackStoreFactory.getTrackStore().getAttribute((String) this.selectAttributes.get(index - 1));
        if (type == Attribute.TYPE_STRING) {
            retval = new JukeXAttributeValue(this.crs.getLong(3 * index), tmpAttribute, this.crs.getString((3 * index) + 1));
        } else {
            retval = new JukeXAttributeValue(tmpAttribute, this.crs.getInt((3 * index) + 1));
        }
        return retval;
    }

    /**
	 * Return an AttributeValue object for the column with the specified Attribute name
	 * @param colname The name of the column.  This is the name of the Attribute.
	 * @return The AttributeValue for that column in the current row
	 */
    public AttributeValue getAttributeValue(String colname) throws SQLException {
        AttributeValue retval = null;
        if (this.columns == null) getMetaData();
        Integer i = (Integer) this.columns.get(colname);
        if (i != null) retval = getAttributeValue(i.intValue());
        return retval;
    }

    /**
	 * Return the String represntation of the column at the given index
	 *
	 * @param index The index of the column
	 * @return The String representation of the data in the column
	 */
    public String getString(int index) throws SQLException {
        return this.crs.getString((3 * index) + 1);
    }

    /**
	 * Return the integer representation of the column at the given index
	 * 
	 * @param index The index of the column
	 * @return The integer value of the data in that column
	 */
    public int getInt(int index) throws SQLException {
        return this.crs.getInt((3 * index) + 1);
    }

    /**
	 * Return the String representation of the column with the given name
	 *
	 * @param colname The name of the column
	 * @return The String representation of the data in that column
	 */
    public String getString(String colname) throws SQLException {
        return this.crs.getString(colname);
    }

    /**
	 * Return the integer representation of the column with the given name
	 *
	 * @param colname The name of the column
	 * @return The integer representation of the data in that column
	 */
    public int getInt(String colname) throws SQLException {
        return this.crs.getInt(colname);
    }

    /**
	 * Return the ID of the Track referred to by the current row
	 * 
	 * @return The ID of the Track referred to by the current row
	 */
    public long getTrackId() throws SQLException {
        return this.crs.getLong(1);
    }

    /** 
	 * Move to the next row in the result set
	 *
	 * @return <code>true</code> if there is a next row, <code>false</code> otherwise.
	 */
    public boolean next() throws SQLException {
        return this.crs.next();
    }

    /**
	 * Move the current row to be before the first row.
	 *
	 * This means that calling next() will move you to the first record
	 */
    public void beforeFirst() throws SQLException {
        this.crs.beforeFirst();
    }

    /**
	 * Return the number of columns present in this query
	 *
	 * @return The number of columns present in this query
	 */
    public int getColumnCount() {
        return this.selectAttributes.size();
    }

    /**
	 * Extract the metadata from the actual resultset and store it internally
	 */
    private void getMetaData() throws SQLException {
        ResultSetMetaData metaData = this.crs.getMetaData();
        int colCount = metaData.getColumnCount();
        this.columns = new HashMap(colCount);
        for (int x = colCount; x >= 0; x++) {
            this.columns.put(metaData.getColumnName(x), new Integer(x));
        }
    }
}
