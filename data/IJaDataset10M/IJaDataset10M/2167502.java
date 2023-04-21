package de.cologneintelligence.fitgoodies.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import de.cologneintelligence.fitgoodies.RowFixture;
import de.cologneintelligence.fitgoodies.dynamic.ResultSetWrapper;

/**
 * This class is a extended version of a row fixture, which takes a <code>ResultSet</code>
 * object and compares it with the given table. Therefore it creates a new,
 * temporary class which wraps the <code>ResultSet</code> using a
 * {@link de.cologneintelligence.fitgoodies.dynamic.DynamicObjectFactory} and fills these objects
 * with the individual rows of the <code>ResultSet</code>.
 *
 * @author jwierum
 * @version $Id: ResultSetFixture.java 46 2011-09-04 14:59:16Z jochen_wierum $
 */
public class ResultSetFixture extends RowFixture {

    private ResultSetWrapper table;

    /**
	 * Sets the ResultSet which is compared with the input table.
	 * @param resultSet <code>ResultSet</code> to use
	 * @throws SQLException Exception thrown by the <code>ResultSet</code>. You can propagate
	 * 		it to fit.
	 */
    public final void setResultSet(final ResultSet resultSet) throws SQLException {
        table = new ResultSetWrapper(resultSet);
    }

    /**
	 * Gets the type of the dynamic created target class.
	 * @return the type of the target class.
	 */
    @Override
    public final Class<?> getTargetClass() {
        return table.getClazz();
    }

    /**
	 * Gets an array which represents the ResultSet as an object array.
	 * The type of these objects can be determined via <code>getTargetClass()</code>.
	 */
    @Override
    public final Object[] query() throws Exception {
        return table.getRows();
    }
}
