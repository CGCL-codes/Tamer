package fitnesse.responders;

import fitnesse.FitNesseContext;
import fitnesse.Responder;
import fitnesse.http.Request;
import fitnesse.http.Response;
import fitnesse.http.SimpleResponse;
import fitnesse.responders.sql.SqlPoolHelper;
import fitnesse.responders.sql.SqlRunner;
import fitnesse.util.PropertiesUtil;

public class SqlResponder implements Responder {

    public static String STIQ_DATABASE_DRIVER = "STIQDatabaseDriver";

    public static String STIQ_DATABASE_CONNECTION_STRING = "STIQDatabaseConnectionString";

    public static String STIQ_DATABASE_USERNAME = "STIQDatabaseUsername";

    public static String STIQ_DATABASE_PASSWORD = "STIQDatabasePassword";

    /**
	 * Serves up SQL responses from a database as XML.
	 * 
	 * @see fitnesse.Responder#makeResponse(fitnesse.FitNesseContext,
	 *      fitnesse.http.Request)
	 */
    public Response makeResponse(final FitNesseContext context, final Request request) throws Exception {
        final SimpleResponse response = new SimpleResponse();
        response.setContentType(getContentType(request));
        response.setContent("Hello world SQL 2!!!");
        final String results = retrieveResults(request);
        response.setContent(results);
        return response;
    }

    private String retrieveResults(final Request request) throws Exception {
        final String driver = PropertiesUtil.getPropertyOrFail(STIQ_DATABASE_DRIVER);
        final String connectionString = PropertiesUtil.getPropertyOrFail(STIQ_DATABASE_CONNECTION_STRING);
        final String username = PropertiesUtil.getPropertyOrFail(STIQ_DATABASE_USERNAME);
        final String password = PropertiesUtil.getPropertyOrFail(STIQ_DATABASE_PASSWORD);
        final SqlPoolHelper sqlHelper = SqlPoolHelper.getInstance(driver, connectionString, username, password);
        final String sqlStatement = String.valueOf(request.getInput("command"));
        final SqlRunner sqlRunner = new SqlRunner();
        sqlRunner.executeStatement(sqlHelper.acquireConnection(), sqlStatement);
        return sqlRunner.getResultsAsXml();
    }

    private String getContentType(final Request request) {
        if (request.getResource().endsWith(".hta")) {
            return "application/hta";
        }
        return "application/xml";
    }
}
