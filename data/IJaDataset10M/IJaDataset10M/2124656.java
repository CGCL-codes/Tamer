package org.objectstyle.cayenne.dba.mysql;

import java.sql.Connection;
import org.objectstyle.cayenne.access.jdbc.SelectAction;
import org.objectstyle.cayenne.access.trans.SelectTranslator;
import org.objectstyle.cayenne.dba.DbAdapter;
import org.objectstyle.cayenne.dba.JdbcActionBuilder;
import org.objectstyle.cayenne.map.EntityResolver;
import org.objectstyle.cayenne.query.SQLAction;
import org.objectstyle.cayenne.query.SelectQuery;

/**
 * @since 1.2
 * @author Andrus Adamchik
 */
class MySQLActionBuilder extends JdbcActionBuilder {

    MySQLActionBuilder(DbAdapter adapter, EntityResolver resolver) {
        super(adapter, resolver);
    }

    public SQLAction objectSelectAction(SelectQuery query) {
        return new SelectAction(query, adapter, entityResolver) {

            protected SelectTranslator createTranslator(Connection connection) {
                SelectTranslator translator = new MySQLSelectTranslator();
                translator.setQuery(query);
                translator.setAdapter(adapter);
                translator.setEntityResolver(getEntityResolver());
                translator.setConnection(connection);
                return translator;
            }
        };
    }
}
