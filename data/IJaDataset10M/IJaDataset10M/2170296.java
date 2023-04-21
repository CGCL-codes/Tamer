package net.sf.mustang.db;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.webmacro.Broker;
import org.webmacro.Context;
import net.sf.mustang.Mustang;
import net.sf.mustang.log.KLog;

public class QueryContext extends Context {

    private static KLog log = Mustang.getLog(QueryContext.class);

    private static final String QPARAMETERS_KEY = "qparams";

    private static final String PARAMETERS_KEY = "params";

    private static final String CONNECTION_KEY = "Connection";

    private static final String CHANNEL_KEY = "Channel";

    private Connection connection = null;

    private String channel = null;

    private Hashtable<String, Statement> statements = new Hashtable();

    private Hashtable<String, Vector<Statement>> batches = new Hashtable();

    private Parameters parameters = new Parameters();

    public QueryContext(Broker broker, String channel) throws Exception {
        super(broker);
        this.channel = channel;
        try {
            this.connection = DataSourceManager.getInstance().getConnection(channel);
        } catch (Exception e) {
            throw new Exception("no connection obtained for '" + channel + "': " + e);
        }
        if (log.isNotice()) log.notice("obtained connection for: " + channel + " (" + getConnection() + ")");
        put(QPARAMETERS_KEY, getParameters());
        put(PARAMETERS_KEY, getParameters());
        put(CONNECTION_KEY, getConnection());
        put(CHANNEL_KEY, channel);
    }

    public void release() throws Exception {
        Enumeration<Statement> en = statements.elements();
        while (en.hasMoreElements()) en.nextElement().close();
        DataSourceManager.getInstance().releaseConnection(channel, connection);
        if (log.isNotice()) log.notice("released connection for: " + channel);
        connection = null;
        channel = null;
        statements = null;
        parameters = null;
    }

    public String getChannel() {
        return channel;
    }

    public Connection getConnection() {
        return connection;
    }

    public Parameters getParameters() {
        return parameters;
    }

    public Hashtable<String, Statement> getStatements() {
        return statements;
    }

    public Hashtable<String, Vector<Statement>> getBatches() {
        return batches;
    }
}
