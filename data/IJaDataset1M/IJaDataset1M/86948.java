package net.sourceforge.squirrel_sql.client.plugin;

import net.sourceforge.squirrel_sql.client.session.ISession;

/**
 * Base interface for all plugins associated with a session.
 */
public interface ISessionPlugin extends IPlugin {

    /**
     * Called when a session started.
     *
     * @param   session     The session that is starting.
     *
     * @return  <TT>true</TT> if plugin is applicable to passed
     *          session else <TT>false</TT>.
     */
    boolean sessionStarted(ISession session);

    /**
     * Called when a session shutdown.
     */
    void sessionEnding(ISession session);

    /**
     * Let app know what extra types of objects in object tree that
     * plugin can handle.
     */
    IPluginDatabaseObjectType[] getObjectTypes(ISession session);
}
