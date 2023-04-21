package models.requests;

import connection.WhisperTransportConnection;

/**
 * This class is responsible for requesting a group search search.
 * 
 * @author Thomas Pedley
 */
public class RequestGroupSearch extends Request {

    /** The name to search for. */
    private String queryString;

    /**
	 * Constructor.
	 * 
	 * @param connection The connection over which the request will be sent.
	 * @param queryString The query string to search for.
	 */
    public RequestGroupSearch(WhisperTransportConnection connection, String queryString) {
        super(connection);
        this.queryString = queryString;
    }

    /**
	 * Execute the request.
	 */
    @Override
    public void execute() {
        addArgument("QueryString", queryString);
        String request = constructRequest();
        connection.send(request);
    }

    /**
	 * Get the name of the request.
	 * 
	 * @return The name of the request.
	 */
    @Override
    protected String getName() {
        return "GroupSearch";
    }
}
