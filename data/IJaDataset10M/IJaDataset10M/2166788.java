package net.wgen.op.db.lob;

import net.wgen.op.util.ReflectionUtils;
import java.sql.Connection;

/**
 * @author Paul Feuer, Wireless Generation, Inc.
 * @version $Id: LobHandlerFactory.java 22 2007-06-26 01:11:24Z paulfeuer $
 */
public class LobHandlerFactory {

    public static final String SYSTEM_PROPERTY = LobHandlerFactory.class.getName() + ".implementation";

    private static final String JBOSS_CX = "org.jboss.resource.adapter.jdbc.WrappedConnection";

    private static final String ORA_CX = "oracle.jdbc.OracleConnection";

    private static final String MYSQL_CX = "com.mysql.jdbc.Connection";

    private static final String MYSQL_WRAPPED_CX = "com.mysql.jdbc.jdbc2.optional.ConnectionWrapper";

    private static final String ORA_LOB = "net.wgen.op.db.lob.OracleLobHandler";

    private static final String JBOSS_LOB = "net.wgen.op.db.lob.JbossLobHandler";

    private static final String MYSQL_LOB = "net.wgen.op.db.lob.MySqlLobHandler";

    private static LobHandlerFactory _delegatedFactory;

    static {
        String className = System.getProperty(SYSTEM_PROPERTY, LobHandlerFactory.class.getName());
        _delegatedFactory = (LobHandlerFactory) ReflectionUtils.instantiate(className);
    }

    public static void setDelegatedFactory(LobHandlerFactory delegatedFactory) {
        _delegatedFactory = delegatedFactory;
    }

    public static LobHandler createForConnection(Connection connection) {
        return _delegatedFactory._createForConnection(connection);
    }

    public LobHandlerFactory() {
    }

    protected LobHandler _createForConnection(Connection connection) {
        return getLobHandler(connection);
    }

    protected LobHandler getLobHandler(Connection connection) {
        String cxClassName = connection.getClass().getName();
        boolean isJbossWrapped = JBOSS_CX.equals(cxClassName);
        if (isJbossWrapped) {
            Object wrappedConnection = ReflectionUtils.invoke(connection, "getUnderlyingConnection");
            cxClassName = wrappedConnection.getClass().getName();
        }
        LobHandler dbLobHandler = null;
        if (cxClassName.toLowerCase().indexOf("oracle") > -1) {
            dbLobHandler = (LobHandler) ReflectionUtils.instantiate(ORA_LOB);
        } else if (MYSQL_CX.equals(cxClassName) || MYSQL_WRAPPED_CX.equals(cxClassName)) {
            dbLobHandler = (LobHandler) ReflectionUtils.instantiate(MYSQL_LOB);
        } else {
            dbLobHandler = new LobHandler();
        }
        if (isJbossWrapped) {
            dbLobHandler = (LobHandler) ReflectionUtils.instantiate(JBOSS_LOB, new Class[] { LobHandler.class }, new Object[] { dbLobHandler });
        }
        return dbLobHandler;
    }
}
