package com.rapidminer.operator.io;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import com.rapidminer.example.Attribute;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.table.DatabaseExampleTable;
import com.rapidminer.example.table.ExampleTable;
import com.rapidminer.gui.wizards.DBExampleSourceConfigurationWizardCreator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.UserError;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.parameter.ParameterTypeBoolean;
import com.rapidminer.parameter.ParameterTypeCategory;
import com.rapidminer.parameter.ParameterTypeConfiguration;
import com.rapidminer.parameter.ParameterTypeFile;
import com.rapidminer.parameter.ParameterTypePassword;
import com.rapidminer.parameter.ParameterTypeString;
import com.rapidminer.parameter.ParameterTypeText;
import com.rapidminer.parameter.TextType;
import com.rapidminer.parameter.UndefinedParameterError;
import com.rapidminer.tools.Tools;
import com.rapidminer.tools.jdbc.DatabaseHandler;
import com.rapidminer.tools.jdbc.DatabaseService;

/**
 * <p>This operator reads an {@link com.rapidminer.example.ExampleSet} from an SQL
 * database. The SQL query can be passed to RapidMiner via a parameter or, in case of
 * long SQL statements, in a separate file. Please note that column names are
 * often case sensitive. Databases may behave differently here.</p>
 * 
 * <p>The most convenient way of defining the necessary parameters is the 
 * configuration wizard. The most important parameters (database URL and user name) will
 * be automatically determined by this wizard and it is also possible to define
 * the special attributes like labels or ids.</p>
 * 
 * <p>Please note that this operator supports two basic working modes:</p>
 * <ol>
 * <li>reading the data from the database and creating an example table in main memory</li>
 * <li>keeping the data in the database and directly working on the database table </li>
 * </ol>
 * <p>The latter possibility will be turned on by the parameter &quot;work_on_database&quot;.
 * Please note that this working mode is still regarded as experimental and errors might
 * occur. In order to ensure proper data changes the database working mode is only allowed
 * on a single table which must be defined with the parameter &quot;table_name&quot;. 
 * IMPORTANT: If you encounter problems during data updates (e.g. messages that the result set is not 
 * updatable) you probably have to define a primary key for your table.</p>
 * 
 * <p>If you are not directly working on the database, the data will be read with an arbitrary
 * SQL query statement (SELECT ... FROM ... WHERE ...) defined by &quot;query&quot; or &quot;query_file&quot;.
 * The memory mode is the recommended way of using this operator. This is especially important for
 * following operators like learning schemes which would often load (most of) the data into main memory
 * during the learning process. In these cases a direct working on the database is not recommended
 * anyway.</p>
 * 
 * <h5>Warning</h5>
 * As the java <code>ResultSetMetaData</code> interface does not provide
 * information about the possible values of nominal attributes, the internal
 * indices the nominal values are mapped to will depend on the ordering
 * they appear in the table. This may cause problems only when processes are
 * split up into a training process and an application or testing process.
 * For learning schemes which are capable of handling nominal attributes, this
 * is not a problem. If a learning scheme like a SVM is used with nominal data,
 * RapidMiner pretends that nominal attributes are numerical and uses indices for the
 * nominal values as their numerical value. A SVM may perform well if there are
 * only two possible values. If a test set is read in another process, the
 * nominal values may be assigned different indices, and hence the SVM trained
 * is useless. This is not a problem for label attributes, since the classes can
 * be specified using the <code>classes</code> parameter and hence, all
 * learning schemes intended to use with nominal data are safe to use.
 * 
 * @rapidminer.todo Fix the above problem. This may not be possible effeciently since
 *            it is not supported by the Java ResultSet interface.
 * 
 * @author Ingo Mierswa
 *          ingomierswa Exp $
 */
public class DatabaseExampleSource extends ResultSetExampleSource {

    /** The parameter name for &quot;If set to true, the data read from the database is NOT copied to main memory. All operations that change data will modify the database.&quot; */
    public static final String PARAMETER_WORK_ON_DATABASE = "work_on_database";

    /** The parameter name for &quot;Indicates the used database system&quot; */
    public static final String PARAMETER_DATABASE_SYSTEM = "database_system";

    /** The parameter name for &quot;The complete URL connection string for the database, e.g. 'jdbc:mysql://foo.bar:portnr/database'&quot; */
    public static final String PARAMETER_DATABASE_URL = "database_url";

    /** The parameter name for &quot;Database username.&quot; */
    public static final String PARAMETER_USERNAME = "username";

    /** The parameter name for &quot;Password for the database.&quot; */
    public static final String PARAMETER_PASSWORD = "password";

    /** The parameter name for &quot;SQL query. If not set, the query is read from the file specified by 'query_file'.&quot; */
    public static final String PARAMETER_QUERY = "query";

    /** The parameter name for &quot;File containing the query. Only evaluated if 'query' is not set.&quot; */
    public static final String PARAMETER_QUERY_FILE = "query_file";

    /** The parameter name for &quot;Use this table if work_on_database is true or no other query is specified.&quot; */
    public static final String PARAMETER_TABLE_NAME = "table_name";

    /** The parameter name for &quot;Whitespace separated list of possible class values of the label attribute.&quot; */
    public static final String PARAMETER_CLASSES = "classes";

    /** The database connection handler. */
    private DatabaseHandler databaseHandler;

    /** This is only used for the case that the data is read into memory. */
    private Statement statement;

    public DatabaseExampleSource(OperatorDescription description) {
        super(description);
    }

    public ExampleSet createExampleSet() throws OperatorException {
        if (!getParameterAsBoolean(PARAMETER_WORK_ON_DATABASE)) {
            ExampleSet output = super.createExampleSet();
            disconnect();
            return output;
        } else {
            try {
                String tableName = getParameterAsString(PARAMETER_TABLE_NAME);
                if (tableName == null) {
                    throw new UserError(this, 201, new Object[] { "table_name", "work_on_database", "true" });
                }
                ExampleTable table = DatabaseExampleTable.createDatabaseExampleTable(getConnectedDatabaseHandler(), tableName);
                ExampleSet exampleSet = createExampleSet(table, this);
                exampleSet.recalculateAllAttributeStatistics();
                return exampleSet;
            } catch (SQLException e) {
                throw new UserError(this, e, 304, e.getMessage());
            }
        }
    }

    public void tearDown() {
        if (this.statement != null) {
            try {
                this.statement.close();
            } catch (SQLException e) {
                logWarning("Cannot close statement.");
            }
            this.statement = null;
        }
    }

    public void setNominalValues(List attributeList, ResultSet resultSet, Attribute label) throws UndefinedParameterError {
        setNominalValuesForLabel(label);
    }

    private void setNominalValuesForLabel(Attribute label) throws UndefinedParameterError {
        if (label != null) {
            String classes = getParameterAsString(PARAMETER_CLASSES);
            if (label.isNominal()) {
                if (classes != null) {
                    String labelClasses[] = classes.split(" ");
                    for (int i = 0; i < labelClasses.length; i++) {
                        label.getMapping().mapString(labelClasses[i].trim());
                    }
                }
            } else {
                if ((classes != null) && (classes.length() > 0)) logWarning("Ignoring classes for non-nominal attribute " + label.getName() + ".");
            }
        }
    }

    private String getQuery() throws OperatorException {
        String query = getParameterAsString(PARAMETER_QUERY);
        if (query != null) query = query.trim();
        String parameterUsed = null;
        boolean warning = false;
        if ((query == null) || (query.length() == 0)) {
            File queryFile = getParameterAsFile(PARAMETER_QUERY_FILE);
            if (queryFile != null) {
                try {
                    query = Tools.readTextFile(queryFile);
                    parameterUsed = "query_file";
                } catch (IOException ioe) {
                    throw new UserError(this, ioe, 302, new Object[] { queryFile, ioe.getMessage() });
                }
                if ((query == null) || (query.trim().length() == 0)) {
                    throw new UserError(this, 205, queryFile);
                }
            }
        } else {
            parameterUsed = "query";
            if (isParameterSet(PARAMETER_QUERY_FILE)) {
                warning = true;
            }
        }
        if ((query == null) || (query.trim().length() == 0)) {
            if (isParameterSet(PARAMETER_TABLE_NAME)) {
                query = "SELECT * FROM " + getParameterAsString(PARAMETER_TABLE_NAME);
                parameterUsed = "table_name";
            }
        } else if (isParameterSet(PARAMETER_TABLE_NAME)) {
            warning = true;
        }
        if (query == null) {
            throw new UserError(this, 202, new Object[] { "query", "query_file", "table_name" });
        }
        if (warning) {
            logWarning("Only one of the parameters 'query', 'query_file', and 'table_name' have to be set. Using value of '" + parameterUsed + "'.");
        }
        return query;
    }

    protected DatabaseHandler getConnectedDatabaseHandler() throws OperatorException, SQLException {
        String databaseURL = getParameterAsString(PARAMETER_DATABASE_URL);
        String username = getParameterAsString(PARAMETER_USERNAME);
        String password = getParameterAsString(PARAMETER_PASSWORD);
        return DatabaseHandler.getConnectedDatabaseHandler(databaseURL, username, password, DatabaseService.getJDBCProperties().get(getParameterAsInt(PARAMETER_DATABASE_SYSTEM)), this);
    }

    /**
	 * This method reads the file whose name is given, extracts the database
	 * access information and the query from it and executes the query. The
	 * query result is returned as a ResultSet.
	 */
    public ResultSet getResultSet() throws OperatorException {
        ResultSet rs = null;
        try {
            databaseHandler = getConnectedDatabaseHandler();
            String query = getQuery();
            log("Executing query: '" + query + "'");
            this.statement = databaseHandler.createStatement(false);
            rs = this.statement.executeQuery(query);
            log("Query executed.");
        } catch (SQLException sqle) {
            throw new UserError(this, sqle, 304, sqle.getMessage());
        }
        return rs;
    }

    public void processFinished() {
        disconnect();
    }

    private void disconnect() {
        tearDown();
        if (databaseHandler != null) {
            try {
                databaseHandler.disconnect();
                databaseHandler = null;
            } catch (SQLException e) {
                logWarning("Cannot disconnect from database: " + e);
            }
        }
    }

    public List<ParameterType> getParameterTypes() {
        List<ParameterType> types = new LinkedList<ParameterType>();
        Map<String, String> wizardParameters = new HashMap<String, String>();
        wizardParameters.put(DBExampleSourceConfigurationWizardCreator.PARAMETER_ONLY_TABLE_NAMES, "false");
        wizardParameters.put(DBExampleSourceConfigurationWizardCreator.PARAMETER_SHOW_DATABASE_CONFIGURATION, "true");
        ParameterType type = new ParameterTypeConfiguration(DBExampleSourceConfigurationWizardCreator.class, wizardParameters, this);
        type.setExpert(false);
        types.add(type);
        types.add(new ParameterTypeBoolean(PARAMETER_WORK_ON_DATABASE, "(EXPERIMENTAL!) If set to true, the data read from the database is NOT copied to main memory. All operations that change data will modify the database.", false));
        type = new ParameterTypeCategory(PARAMETER_DATABASE_SYSTEM, "Indicates the used database system", DatabaseService.getDBSystemNames(), 0);
        type.setExpert(false);
        types.add(type);
        types.add(new ParameterTypeString(PARAMETER_DATABASE_URL, "The complete URL connection string for the database, e.g. 'jdbc:mysql://foo.bar:portnr/database'", false));
        types.add(new ParameterTypeString(PARAMETER_USERNAME, "Database username.", false));
        types.add(new ParameterTypePassword(PARAMETER_PASSWORD, "Password for the database."));
        type = new ParameterTypeText(PARAMETER_QUERY, "SQL query. If not set, the query is read from the file specified by 'query_file'.", TextType.SQL);
        type.setExpert(false);
        types.add(type);
        types.add(new ParameterTypeFile(PARAMETER_QUERY_FILE, "File containing the query. Only evaluated if 'query' is not set.", null, true));
        types.add(new ParameterTypeString(PARAMETER_TABLE_NAME, "Use this table if work_on_database is true or no other query is specified."));
        types.addAll(super.getParameterTypes());
        types.add(new ParameterTypeString(PARAMETER_CLASSES, "Whitespace separated list of possible class values of the label attribute."));
        return types;
    }
}
