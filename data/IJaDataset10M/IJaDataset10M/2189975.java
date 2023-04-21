package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * An abstract class representing some kind of data
 * saved in one collumn in the database
 *
 */
public abstract class DatabaseCol {

    /**
	 * Name used for the col in the database
	 */
    protected final String name;

    /**
	 * Create a new instance
	 * @param name string with name of the col
	 * @param value an object holding the inital value
	 */
    public DatabaseCol(String name) {
        this.name = name;
    }

    /**
	 * Tells wether the data has been changed
	 */
    private boolean isChanged = false;

    /**
	 * Extract data from the result set to this col
	 * @param resultSet resultSet to get the data from
	 * @throws SQLException
	 */
    protected final void populate(ResultSet resultSet) throws SQLException {
        populateCol(resultSet, name);
        isChanged = false;
    }

    /**
	 * Get a generated key from the result set and save it to this col
	 * @param resultSet resultSet from statement.getGeneratedKeys()
	 * @throws SQLException
	 */
    protected void setGenerated(ResultSet resultSet) throws SQLException {
        populateCol(resultSet, "GENERATED_KEY");
        isChanged = false;
    }

    /**
	 * Abstract class to extract data from the result set to the col
	 * @param resultSet resultSet to get the data from
	 * @param colName name of the col to get
	 * @throws SQLException
	 */
    protected abstract void populateCol(ResultSet resultSet, String colName) throws SQLException;

    /**
	 * Abstract class to put add data to a prepared statement
	 * @param stmt prepatedStatement to add data to
	 * @param index location in the stmt to add the data to
	 * @throws SQLException
	 */
    protected abstract void setStatement(PreparedStatement stmt, int index) throws SQLException;

    /**
	 * Get the status of the isChanged flag
	 * @return boolean with the value of the isChanged flag
	 */
    public boolean isChanged() {
        return isChanged;
    }

    /**
	 * Sets the isChanged flag
	 * @param boolean with new flag
	 */
    public void setChanged(boolean b) {
        isChanged = b;
    }

    /**
	 * Get the name of this col
	 * @return
	 */
    public String getName() {
        return name;
    }

    /**
	 * Check if the two objects are equal, has support fo null
	 * @param o1 object to compare
	 * @param o2 object to compare
	 * @return boolean result
	 */
    protected boolean isEqual(Object o1, Object o2) {
        return (o1 == null ? o1 == o2 : o1.equals(o2));
    }
}
