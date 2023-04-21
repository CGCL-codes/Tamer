package org.firewaterframework.mappers.jdbc;

import org.antlr.stringtemplate.StringTemplate;
import org.firewaterframework.WSException;
import org.firewaterframework.rest.*;
import org.firewaterframework.rest.representation.Representation;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is responsible for servicing the PUT, POST and DELETE method Requests for
 * JDBC resources.  It allows for more than one statement to be executed and for keys
 * generated or accessed in earlier SQL statements to be available to later statements.
 * It uses the StringTemplate template engine and binds incoming Request arguments as
 * template variables.
 *
 * @author Tim Spurway
 */
public class UpdateMapper extends JDBCMapper {

    protected static final Logger log = LoggerFactory.getLogger(UpdateMapper.class);

    protected QueryHolder[] queries;

    /**
     * Handle a REST Request by 'executing' a sequence of SQL statements.  Bind the Request
     * arguments before assigning them as StringTemplate variables.  This allows validation
     * and data conversions before execution of the actual SQL against the database.
     * <p>
     * This method keeps track of all generated keys from the execution of each SQL
     * statement.  These keys can be accessed in the StringTemplate query using the <code>keyName</code> assigned
     * to each query (optionally) in the configuration.  Only keys generated in previously excuted statements
     * will be available for processing in subsequent statements.
     *
     * @param request the incoming REST Request, typically indicating a PUT, POST, or DELETE operation
     * @return a DocumentResponse object containg a simple XML document describing the sucessful execution
     * @throws WSException on error conditions, typically a 500 if, for some reason, the executions fail
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
    public Response handle(Request request) {
        Representation rval = getRepresentation(request);
        rval.setName("result");
        Map<String, Object> keys = new HashMap<String, Object>();
        Map<String, Object> translatedArgs = bind(request);
        try {
            for (QueryHolder query : queries) {
                StringTemplate queryTemplate = new StringTemplate(query.query);
                KeyHolder keyHolder = new GeneratedKeyHolder();
                UpdateMapperStatementCreator updateStatement = new UpdateMapperStatementCreator(queryTemplate, translatedArgs, keys, query.hasKey());
                String updateSQL = updateStatement.queryTemplate.toString().trim();
                if (updateSQL.length() > 0) {
                    log.debug("Update Query: " + updateSQL);
                    Integer rowsAffected = 0;
                    try {
                        if (query.hasKey()) {
                            rowsAffected = template.update(updateStatement, keyHolder);
                        } else {
                            rowsAffected = template.update(updateStatement);
                        }
                    } catch (Exception xx) {
                        log.error("Exception executing update: " + xx.getMessage() + " for query: " + updateSQL);
                        log.error("Query: " + updateSQL);
                        log.error("Request: " + request);
                    }
                    Representation element = rval.addChild("update");
                    element.addAttribute("rowsAffected", rowsAffected.toString());
                    if (query.hasKey()) {
                        element.addAttribute("updateID", query.keyName);
                    }
                    try {
                        if (query.hasKey() && keyHolder.getKeyList().size() > 0) {
                            Object key = ((Map) keyHolder.getKeyList().get(0)).values().iterator().next();
                            keys.put(query.keyName, key);
                            element.addAttribute("key", key.toString());
                        }
                    } catch (Exception e) {
                        log.error("Error accessing key: " + query.getKeyName() + " for query: " + updateStatement.queryTemplate.toString());
                        throw e;
                    }
                }
            }
            Response response = new Response(Status.STATUS_OK, rval);
            return response;
        } catch (Exception e) {
            String errorQuery = "<empty query>";
            if (queries.length > 0) errorQuery = queries[0].query;
            throw new WSException("Internal Error processing update.");
        }
    }

    /**
     * Used to create the prepared statement for execution by the Mapper.
     */
    public class UpdateMapperStatementCreator implements PreparedStatementCreator {

        Map<String, Object> args;

        StringTemplate queryTemplate;

        Map<String, Object> keys;

        boolean generateKey;

        public UpdateMapperStatementCreator(StringTemplate queryTemplate, Map<String, Object> args, Map<String, Object> keys, boolean generateKey) {
            this.args = args;
            this.queryTemplate = queryTemplate;
            this.keys = keys;
            this.generateKey = generateKey;
            for (Map.Entry<String, Object> entry : args.entrySet()) {
                if (entry.getValue() == null) {
                    queryTemplate.setAttribute(entry.getKey(), "null");
                } else {
                    queryTemplate.setAttribute(entry.getKey(), entry.getValue());
                }
            }
            for (Map.Entry<String, Object> key : keys.entrySet()) {
                queryTemplate.setAttribute(key.getKey(), key.getValue());
            }
        }

        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
            if (generateKey) {
                return connection.prepareStatement(queryTemplate.toString(), Statement.RETURN_GENERATED_KEYS);
            } else {
                return connection.prepareStatement(queryTemplate.toString());
            }
        }
    }

    public QueryHolder[] getQueries() {
        return queries;
    }

    /**
     * The queries will be executed in array order.  Each subsequent query will have access to the keys generated
     * by the preceding queries (if any).
     *
     * @param queries an array of StringTemplate query strings
     */
    public void setQueries(QueryHolder[] queries) {
        this.queries = queries;
    }

    @Override
    public Representation getOptions(Request request) {
        Representation rval = getRepresentation(request);
        rval.setName("update");
        Representation fieldOptions = super.getOptions(request);
        if (fieldOptions != null) {
            rval.addChild(fieldOptions);
        }
        return rval;
    }
}
