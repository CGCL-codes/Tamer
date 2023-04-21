package net.sourceforge.xconf.toolbox.jms;

import javax.jms.Connection;

/**
 * @author Tom Czarniecki
 */
class PooledJmsTemplateHelper {

    private static final ThreadLocal LOCAL = new ThreadLocal();

    public static void registerCurrentConnection(Connection conn) {
        LOCAL.set(PooledConnection.getPooledConnection(conn));
    }

    public static void invalidateCurrentConnection() {
        PooledConnection conn = (PooledConnection) LOCAL.get();
        if (conn != null) {
            conn.invalidate();
        }
    }
}
