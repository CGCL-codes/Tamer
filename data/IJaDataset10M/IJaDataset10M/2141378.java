package uips.support.settings;

import java.util.List;
import java.util.logging.Level;
import uips.support.settings.tree.ICommInterfaceSettings;
import uips.support.settings.tree.IFileStorageSettings;
import uips.support.settings.tree.IHandlerSettings;
import uips.support.settings.tree.IHandlersLoaderSettings;
import uips.support.settings.tree.IHttpServerSettings;
import uips.support.settings.tree.IPortalCommunicationSettings;

public interface ISettingsInstance {

    /**
     * Console severity.
     *
     * @return Console severity.
     */
    public Level getConsoleSeverity();

    /**
     * Database severity.
     *
     * @return Database severity.
     */
    public Level getDatabaseSeverity();

    /**
     * Root directory of UIP handlers
     *
     * @return Root directory of UIP handlers
     */
    public String getHandlersRoot();

    /**
     * Load handlers dynamicaly every time handler is called (true)
     *
     * @return Load handlers dynamicaly every time handler is called (true)
     */
    public boolean isLoadHandlersDynamically();

    /**
     * Console logging output enabled
     *
     * @return Console logging output enabled
     */
    public boolean isLogConsole();

    /**
     * Database logging output enabled
     *
     * @return Database logging output enabled
     */
    public boolean isLogDatabase();

    /**
     * File logging output enabled
     *
     * @return File logging output enabled
     */
    public boolean isLogFile();

    /**
     * Log file severity.
     *
     * @return Log file severity.
     */
    public Level getFileSeverity();

    /**
     * Root direcory of media files.
     *
     * @return Root direcory of media files.
     */
    public String getMediaRoot();

    /**
     * Root directory of UIP xmls
     *
     * @return Root directory of UIP xmls
     */
    public String getConcreteRoot();

    /**
     * Delay to stop instance in seconds if no client is connected,
     * if 0 instances rest in memory to server shutdown
     *
     * @return Delay to stop instance in seconds if no client is connected,
     * if 0 instances rest in memory to server shutdown
     */
    public int getInstanceDisposeDelay();

    /**
     * Maximum count of connected clients per one instance, if 0 is not limited
     *
     * @return Maximum count of connected clients per one instance, if 0 is not limited
     */
    public int getMaxClientsPerInstance();

    /**
     * Maximum count of clients connected to HTTP server, if 0 is not limited
     *
     * @return Maximum count of clients connected to HTTP server, if 0 is not limited
     */
    public int getMaxHttpClients();

    /**
     * Maximum count of running instances, if 0 is not limited
     *
     * @return Maximum count of running instances, if 0 is not limited
     */
    public int getMaxInstances();

    /**
     * Maximum count of clients connected to Signal server, if 0 is not limited
     *
     * @return Maximum count of clients connected to Signal server, if 0 is not limited
     */
    public int getMaxServerCommunicationClients();

    /**
     * Absolute path without separator in the end (/ or \)
     * where are stored UIP applications
     *
     * @return Absolute path without separator in the end (/ or \)
     * where are stored UIP applications
     */
    public String getApplicationsRoot();

    /**
     * Cache dir for storing files from UIP applications storage.
     *
     * @return Cache dir for storing files from UIP applications storage.
     */
    public String getStorageCacheFolder();

    /**
     * Identifier send to UIPPortal, usefull if UIPPortal has defined more UIPServers
     *
     * @return Identifier send to UIPPortal, usefull if UIPPortal has defined more UIPServers
     */
    public String getUipServerId();

    public IPortalCommunicationSettings getPortalCommunication();

    public List<ICommInterfaceSettings> getUipCommunicationInterfaces();

    public IHttpServerSettings getHttpServer();

    public List<ICommInterfaceSettings> getServerCommunicationInterfaces();

    public List<IHandlerSettings> getServerHandlers();

    public IFileStorageSettings getFileStorage();

    public List<IHandlerSettings> getAutonomousHandlers();

    public List<IHandlersLoaderSettings> getHandlersLoaders();
}
