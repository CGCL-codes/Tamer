package uk.co.kimble.cobra;

import java.util.Enumeration;
import java.lang.reflect.Method;
import uk.co.kimble.cobra.criteria.PersistentCriteria;
import uk.co.kimble.cobra.criteria.SelectionCriteria;
import java.sql.PreparedStatement;

/**
 *	Builds SQL Select Statements for an object
 * 
 *  @author		David B George
 *  @version	$\Revision$
 */
public class SQLSelectStatement extends SQLStatement {

    /**
     *  Build a Select statement to retrieve a single instance of the object
     *  based on the primary keys
     *
     *	@param	cd	- Class Dictionary instance
     *	@throws	PersistentException
     */
    public SQLSelectStatement(ClassDictionary cd) throws PersistentException {
        super(cd);
        sql = "SELECT * FROM " + cd.getTable();
    }

    public String buildForObject() throws PersistentException {
        String key_values = null;
        Enumeration pk = class_dictionary.getPrimaryKeys();
        while (pk.hasMoreElements()) {
            String key = (String) pk.nextElement();
            if (key_values != null) {
                key_values = key_values + " AND " + class_dictionary.getColumnName(key) + "= ?";
            } else {
                key_values = class_dictionary.getColumnName(key) + "= ?";
            }
        }
        if (key_values == null) {
            PersistentException pex = new PersistentException("Can't load object: " + class_dictionary.getClassName() + ", no primary keys");
            throw pex;
        }
        return sql + " WHERE " + key_values;
    }
}
