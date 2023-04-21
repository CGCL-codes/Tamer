package net.sourceforge.taverna.scuflworkers.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import net.sourceforge.taverna.baclava.DataThingAdapter;
import org.embl.ebi.escience.baclava.DataThing;
import org.embl.ebi.escience.scuflworkers.java.LocalWorker;
import uk.ac.soton.itinnovation.taverna.enactor.entities.TaskExecutionException;

/**
 * This processor is used to execute SQL update/insert statements.
 * 
 * @author Mark
 * @version $Revision: 1.1 $
 * 
 * @tavinput url The jdbc database URL.
 * @tavinput driver A fully qualified driver classname.
 * @tavinput userid The userid required for database access.
 * @tavinput password The password required for database access.
 * @tavinput sql The SQL statement to be executed.
 * @tavinput params A list of parameters that need to be bound to the query.
 * 
 * @tavinput resultList Returns "update successful".
 */
public class SQLUpdateWorker extends SQLQueryWorker implements LocalWorker {

    private static final String URL = "url";

    private static final String DRIVER = "driver";

    private static final String USERID = "userid";

    private static final String PASSWORD = "password";

    private static final String SQL = "sql";

    private static final String PARAMS = "params";

    private static final String RESULT_LIST = "resultList";

    private static final String TEXT_PLAIN = "'text/plain'";

    private static final String L_TEXT_PLAIN = "l('text/plain')";

    /**
	 * @see org.embl.ebi.escience.scuflworkers.java.LocalWorker#execute(java.util.Map)
	 */
    public Map<String, DataThing> execute(Map inputMap) throws TaskExecutionException {
        DataThingAdapter inAdapter = new DataThingAdapter(inputMap);
        String driverName = inAdapter.getString(DRIVER);
        if (driverName == null || driverName.equals("")) {
            throw new TaskExecutionException("The '" + DRIVER + "' port cannot be empty");
        }
        String url = inAdapter.getString(URL);
        if (url == null || url.equals("")) {
            throw new TaskExecutionException("The '" + URL + "' port cannot be empty");
        }
        String username = inAdapter.getString(USERID);
        if (username == null || username.equals("")) {
            throw new TaskExecutionException("The '" + USERID + "' port cannot be empty");
        }
        String password = inAdapter.getString(PASSWORD);
        String[] params = inAdapter.getStringArray(PARAMS);
        String sql = inAdapter.getString(SQL);
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            Class.forName(driverName);
            connection = DriverManager.getConnection(url, username, password);
            try {
                ps = connection.prepareStatement(sql);
                for (int i = 0; i < params.length; i++) {
                    ps.setObject(i + 1, params[i]);
                }
                ps.executeUpdate();
            } finally {
                if (ps != null) {
                    ps.close();
                }
                connection.close();
            }
        } catch (ClassNotFoundException e) {
            throw new TaskExecutionException(e);
        } catch (SQLException e) {
            throw new TaskExecutionException(e);
        }
        HashMap<String, DataThing> outputMap = new HashMap<String, DataThing>();
        DataThingAdapter outAdapter = new DataThingAdapter(outputMap);
        outAdapter.putString(RESULT_LIST, "update successful");
        return outputMap;
    }

    public String[] inputNames() {
        return new String[] { URL, DRIVER, USERID, PASSWORD, SQL, PARAMS };
    }

    public String[] inputTypes() {
        return new String[] { TEXT_PLAIN, TEXT_PLAIN, TEXT_PLAIN, TEXT_PLAIN, TEXT_PLAIN, L_TEXT_PLAIN };
    }

    public String[] outputNames() {
        return new String[] { RESULT_LIST };
    }

    public String[] outputTypes() {
        return new String[] { TEXT_PLAIN };
    }
}
