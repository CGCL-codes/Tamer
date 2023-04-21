package org.jamwiki.persistency.db;

import java.util.Properties;
import org.jamwiki.Environment;
import org.jamwiki.utils.Pagination;
import org.jamwiki.utils.Utilities;
import org.jamwiki.utils.WikiLogger;

/**
 * Microsoft SQL Server-specific implementation of the QueryHandler interface.
 * This class implements SQL Server-specific methods for instances where SQL Server
 * does not support the default ASCII SQL syntax.
 */
public class MSSqlQueryHandler extends DefaultQueryHandler {

    private static WikiLogger logger = WikiLogger.getLogger(MSSqlQueryHandler.class.getName());

    private static final String SQL_PROPERTY_FILE_NAME = "sql.mssql.properties";

    private static Properties props = null;

    private static Properties defaults = null;

    /**
	 *
	 */
    protected MSSqlQueryHandler() {
        defaults = Environment.loadProperties(DefaultQueryHandler.SQL_PROPERTY_FILE_NAME);
        props = Environment.loadProperties(SQL_PROPERTY_FILE_NAME, defaults);
        super.init(props);
    }

    /**
	 *
	 */
    public WikiResultSet getCategories(int virtualWikiId, Pagination pagination) throws Exception {
        WikiPreparedStatement stmt = new WikiPreparedStatement(STATEMENT_SELECT_CATEGORIES);
        stmt.setInt(1, pagination.getEnd());
        stmt.setInt(2, pagination.getNumResults());
        stmt.setInt(3, virtualWikiId);
        return stmt.executeQuery();
    }

    /**
	 *
	 */
    public WikiResultSet getRecentChanges(String virtualWiki, Pagination pagination, boolean descending) throws Exception {
        WikiPreparedStatement stmt = new WikiPreparedStatement(STATEMENT_SELECT_RECENT_CHANGES);
        stmt.setInt(1, pagination.getEnd());
        stmt.setInt(2, pagination.getNumResults());
        stmt.setString(3, virtualWiki);
        return stmt.executeQuery();
    }

    /**
	 *
	 */
    public WikiResultSet getRecentChanges(int topicId, Pagination pagination, boolean descending) throws Exception {
        WikiPreparedStatement stmt = new WikiPreparedStatement(STATEMENT_SELECT_RECENT_CHANGES_TOPIC);
        stmt.setInt(1, pagination.getEnd());
        stmt.setInt(2, pagination.getNumResults());
        stmt.setInt(3, topicId);
        return stmt.executeQuery();
    }

    /**
	 *
	 */
    public WikiResultSet getUserContributions(String virtualWiki, String userString, Pagination pagination, boolean descending) throws Exception {
        WikiPreparedStatement stmt = null;
        if (Utilities.isIpAddress(userString)) {
            stmt = new WikiPreparedStatement(STATEMENT_SELECT_WIKI_USER_CHANGES_ANONYMOUS);
        } else {
            stmt = new WikiPreparedStatement(STATEMENT_SELECT_WIKI_USER_CHANGES_LOGIN);
        }
        stmt.setInt(1, pagination.getEnd());
        stmt.setInt(2, pagination.getNumResults());
        stmt.setString(3, virtualWiki);
        stmt.setString(4, userString);
        return stmt.executeQuery();
    }

    /**
	 *
	 */
    public WikiResultSet lookupTopicByType(int virtualWikiId, int topicType, Pagination pagination) throws Exception {
        WikiPreparedStatement stmt = new WikiPreparedStatement(STATEMENT_SELECT_TOPIC_BY_TYPE);
        stmt.setInt(1, pagination.getEnd());
        stmt.setInt(2, pagination.getNumResults());
        stmt.setInt(3, virtualWikiId);
        stmt.setInt(4, topicType);
        return stmt.executeQuery();
    }
}
