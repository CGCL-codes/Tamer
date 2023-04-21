package org.opencdspowered.opencds.core.plugin;

import org.opencdspowered.opencds.core.plugin.PluginInterface;
import java.util.*;

/**
 * This Plugin class is what plugins need to extend to be able to be installed.
 *  This class represents one plugin.
 *
 * @author  Lars 'Levia' Wesselius
*/
public abstract class Plugin {

    private String m_Name;

    private String m_FileName;

    private String m_Version;

    private String m_Description;

    protected boolean m_Created;

    protected boolean m_IsRunning;

    private Thread m_Thread;

    protected PluginLogger m_Logger;

    protected PluginLocalisation m_Localisation;

    protected PluginInterface m_Interface;

    /**
     * The plugin constructor.
    */
    public Plugin() {
        m_Created = false;
    }

    /**
     * Creates the plugin.
     *
     * @param   name        The name of the plugin.
     * @param   fileName    The filename of the plugin.
     * @param   version     The version of the plugin.
     * @param   description The plugin description.
    */
    public void create(String name, String fileName, String version, String description) {
        m_Name = name;
        m_FileName = fileName;
        m_Version = version;
        m_Created = true;
        m_Description = description;
        m_Logger = PluginLogger.getLogger(this);
        m_Localisation = PluginLocalisation.getLocalisation(this);
    }

    /**
     * Initializes the plugin, creating all it's components.
     *
     * @param   pluginInterface The interface through which the plugin acts.
    */
    public void initialize(PluginInterface pluginInterface) {
        m_Interface = pluginInterface;
    }

    /**
     * Destroys the plugin, destroying all it's components.
    */
    public abstract void destroy();

    /**
     * Get the name of the plugin.
    */
    public String getName() {
        return m_Name;
    }

    /**
     * Get the filename of the plugin.
    */
    public String getFileName() {
        return m_FileName;
    }

    /**
     * Get the name of the file.
    */
    public String getJarName() {
        String[] parts = m_FileName.split("\\\\");
        String lastPart = parts[parts.length - 1];
        return lastPart.substring(0, lastPart.length() - 4);
    }

    /**
     * Get the version of this plugin.
    */
    public String getVersion() {
        return m_Version;
    }

    /**
     * Get the description of this plugin.
    */
    public String getDescription() {
        return m_Description;
    }

    /**
     * Get the plugin's logger.
     * 
     * @return  Reference to the logger.
    */
    public PluginLogger getLogger() {
        return m_Logger;
    }

    /**
     * Get the localisation of the plugin.
     * 
     * @return  A reference to the localisation.
    */
    public PluginLocalisation getLocalisation() {
        return m_Localisation;
    }

    /**
     * Get the created state of the plugin.
     *
     * @return  True if its created, false if not.
    */
    public boolean isCreated() {
        return m_Created;
    }

    /**
     * Set if this plugin is running or not.
     * 
     * @param   running True if it is running, false if not.
    */
    public void setIsRunning(boolean running) {
        m_IsRunning = running;
    }

    /**
     * Check whether this plugin is running or not.
     * 
     * @return  True if so, false if not.
    */
    public boolean isRunning() {
        return m_IsRunning;
    }

    /**
     * Set the thread.
     *
     * @param   t  The thread to set to this plugin.
    */
    public void setThread(Thread t) {
        m_Thread = t;
    }

    /**
     * Get the thread.
     *
     * @return  The thread used by this plugin.
    */
    public Thread getThread() {
        return m_Thread;
    }
}
