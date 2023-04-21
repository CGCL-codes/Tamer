package net.sf.indricotherium.util.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

/**
 * @author M�rio Valentim Junior.
 * @since $Revision: 1.1 $
 * @since 07/03/2007
 */
public class PreparedStatementHelper {

    private PreparedStatementHelper() {
    }

    /**
	 * @param parameters
	 * @param statement
	 * @throws SQLException 
	 */
    public static void mapParameters(List parameters, PreparedStatement statement) throws SQLException {
        if (parameters == null) return;
        int i = 0;
        for (Iterator iter = parameters.iterator(); iter.hasNext(); i++) {
            statement.setObject(i, iter.next());
        }
    }

    /**
	 * @param parameters
	 * @param types
	 * @param statement
	 * @throws Exception
	 */
    public static void mapParameters(List parameters, List types, PreparedStatement statement) throws Exception {
        if (parameters == null || types == null) return;
        if (parameters.size() != types.size()) {
            throw new Exception();
        }
        int i = 0;
        for (Iterator iter = parameters.iterator(); iter.hasNext(); i++) {
            Integer type = ((Integer) types.get(i));
            if (type == null) {
                throw new Exception();
            }
            statement.setObject(i, iter.next(), type.intValue());
        }
    }
}
