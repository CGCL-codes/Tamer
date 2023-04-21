package com.rapidminer.example.table;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import com.rapidminer.example.Attribute;
import com.rapidminer.tools.LogService;

/**
 * Unlike a {@link FileDataRowReader} that reads examples from a file, objects
 * of this class read examples from a {@link ResultSet}, a data structure that
 * is returned from a database query.
 * 
 * @see com.rapidminer.tools.jdbc.DatabaseHandler
 * @see com.rapidminer.operator.io.DatabaseDataReader
 * @see com.rapidminer.operator.io.KDBExampleSource
 * @author Simon Fischer, Ingo Mierswa
 *          ingomierswa Exp $
 */
public class ResultSetDataRowReader extends AbstractDataRowReader {

    private Attribute[] attributes;

    private ResultSet resultSet;

    private static final int DONT_KNOW_YET = 0;

    private static final int YES = 1;

    private static final int NO = 2;

    private int hasNext = DONT_KNOW_YET;

    /**
	 * Constructor.
	 * 
	 * @param attributeList
	 *            List of attributes
	 * @param resultSet
	 *            A ResultSet as returned from a database query
	 */
    public ResultSetDataRowReader(DataRowFactory dataRowFactory, List<Attribute> attributeList, ResultSet resultSet) {
        super(dataRowFactory);
        this.resultSet = resultSet;
        this.attributes = new Attribute[attributeList.size()];
        attributeList.toArray(this.attributes);
    }

    public boolean hasNext() {
        switch(hasNext) {
            case YES:
                return true;
            case NO:
                return false;
            case DONT_KNOW_YET:
                try {
                    if (resultSet.next()) {
                        hasNext = YES;
                        return true;
                    } else {
                        hasNext = NO;
                        resultSet.close();
                        return false;
                    }
                } catch (SQLException e) {
                    LogService.getGlobal().logError("While reading examples from result set: " + e.getMessage());
                    return false;
                }
            default:
                return false;
        }
    }

    public DataRow next() {
        if (hasNext()) {
            hasNext = DONT_KNOW_YET;
            try {
                DataRow row = getFactory().create(attributes.length);
                for (int i = 0; i < attributes.length; i++) {
                    double value = DatabaseDataRow.readColumn(resultSet, attributes[i]);
                    row.set(attributes[i], value);
                }
                row.trim();
                return row;
            } catch (SQLException sqle) {
                throw new RuntimeException("Error accessing the result of a query:" + sqle.toString());
            }
        } else {
            return null;
        }
    }
}
