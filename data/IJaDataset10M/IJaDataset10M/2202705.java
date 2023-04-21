package de.fhg.igd.logging;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import de.fhg.igd.io.StringSubstitutionInputStream;
import de.fhg.igd.io.URLOutputStream;
import de.fhg.igd.io.VariableSubstitutionInputStream;
import de.fhg.igd.util.URL;

/**
 * This class allows easy maintenance and configuration of the logging
 * framework at runtime. For this purpose, a set of convenience methods is
 * being provided. Additionally, the {@link #main main} method supports a
 * similar set of command line options. Detailed information is available
 * via the {@link #help help} printout:
 * <p><nobreak><tt>
 * /:0> java de.fhg.igd.logging.LoggingConfiguration -help
 * </tt></nobreak>
 * <p>On initialization, the current configuration will be read from a
 * file whose name will be looked up in the Java system property
 * {@link #CONFIG_FILE_PROPERTY de.fhg.igd.logging.config}.
 * If the system property is not defined or the specified destination file
 * cannot be read, the initialization will look for the resource file
 * {@link #CONFIG_RESOURCE de/fhg/igd/logging/logging.properties} instead.
 * In case both attempts (system property and system resource) fail,
 * {@link ConfigurationParameters#getDefault() the default configuration}
 * will be loaded.
 * <p>Within the logging configuration file the following properties are
 * supported:
 * <ul>
 * <li><tt>logger=&lt;<i>logger</i>&gt;</tt><br>
 * Defines the underlying logging mechanism to be used.<br>
 * See {@link #setLogger} for the list of valid <tt>&lt;<i>logger</i>&gt;</tt>
 * values and further information.
 * <li><tt>interval=&lt;<i>interval</i>&gt;</tt><br>
 * Defines the interval between anchor timestamp log entries.<br>
 * See {@link #setInterval(String)} for the list of valid
 * <tt>&lt;<i>interval</i>&gt;</tt> values and further information.
 * <li><tt>&lt;URL&gt;,buffer=&lt;<i>buffer</i>&gt;</tt><br>
 * Defines the size of the internal message buffer to be used for specific
 * URLs.<br>
 * See {@link #setBuffer(String,String)} for the list of valid
 * <tt>&lt;<i>buffer</i>&gt;</tt> values and further information.
 * <li><tt>[&lt;name&gt;],output=&lt;URL&gt;[{,&lt;URL&gt;}]</tt><br>
 * Defines output destinations for specific packages.<br>
 * See {@link #addOutput(String,String)} for further information.
 * <li><tt>[&lt;name&gt;],loglevel=&lt;<i>loglevel</i>&gt;</tt><br>
 * Defines the log level of specific packages.<br>
 * See {@link #setLogLevel(String,String)} for the list of valid
 * <tt>&lt;<i>loglevel</i>&gt;</tt> values and further information.
 * </ul>
 * <p><b>NOTICE:</b>
 * <ul>
 * <li>As an additional feature, the logging configuration supports dynamic
 * substitution of system properties:
 * For example, if the directory of an output file of a package
 * <tt>foo.bar</tt> depends on the system property <tt>log.dir</tt>,
 * the following line may be put into the configuration file:<br>
 * <tt>foo.bar,output=file:///${log.dir}/foobar.log</tt>
 * <li>Besides the definitions of
 * <a href="http://sunsite.auc.dk/RFC/rfc/rfc1738.html" target="_blank">
 * RFC 1738</a>,
 * &quot;<tt>socket</tt>&quot; will be accepted as an additional protocol
 * identifier for the URL of a TCP socket: e.g.
 * <tt>socket://192.168.0.1:30000</tt>
 * </ul>
 *
 * @author Matthias Pressfreund
 * @author Jan Peters
 * @version "$Id: LoggingConfiguration.java 1913 2007-08-08 02:41:53Z jpeters $"
 */
public class LoggingConfiguration {

    /**
     * The name of the system property that contains
     * the path to the logging configuration file
     */
    public static final String CONFIG_FILE_PROPERTY = "de.fhg.igd.logging.config";

    /**
     * The name of the resource file that will be loaded instead of
     * an undefined or missing logging configuration file
     */
    public static final String CONFIG_RESOURCE = "de/fhg/igd/logging/logging.properties";

    /**
     * The system dependent line separator
     */
    public static final String CR = System.getProperty("line.separator");

    /**
     * The storage for registered {@link ConfigurationChangeListener} objects
     */
    protected Set configurationChangeListeners_;

    /**
     * The configuration parameters
     */
    protected ConfigurationParameters parameters_;

    /**
     * The singleton instance
     */
    protected static LoggingConfiguration instance_;

    /**
     * Initialize the singleton instance
     */
    static {
        instance_ = new LoggingConfiguration();
        try {
            instance_.init();
        } catch (Exception e) {
            String msg = e.getMessage();
            System.err.println("[LoggingConfiguration] " + msg);
            throw new RuntimeException(msg);
        }
    }

    /**
     * Hidden construction.
     */
    private LoggingConfiguration() {
        configurationChangeListeners_ = new HashSet();
        parameters_ = new ConfigurationParameters();
    }

    /**
     * Initialize the current configuration by first trying to read the
     * logging configuration file defined in the
     * {@link #CONFIG_FILE_PROPERTY system property}.
     * If the system property is not defined or loading the configuration
     * fails, try to read the {@link #CONFIG_RESOURCE resource file}.
     * In case initialization even with the system resource file fails,
     * load the
     * {@link ConfigurationParameters#getDefault() default configuration}.
     */
    protected void init() {
        ConfigurationParameters dcp;
        InputStream is;
        String prp;
        is = null;
        prp = System.getProperty(CONFIG_FILE_PROPERTY);
        if (prp != null) {
            try {
                is = new FileInputStream(prp);
            } catch (Exception e) {
                System.err.println("[LoggingConfiguration] " + "Failed loading logging configuration file '" + prp + "': " + e.getMessage());
            }
        }
        if (is == null) {
            if (prp != null) {
                System.err.println("[LoggingConfiguration] " + "Trying configuration with resource file '" + CONFIG_RESOURCE + "'");
            }
            is = ClassLoader.getSystemResourceAsStream(CONFIG_RESOURCE);
        }
        if (is != null) {
            load(is);
        } else {
            System.err.println("[LoggingConfiguration] " + "Initializing logging configuration failed, " + "resource file '" + CONFIG_RESOURCE + "' not available");
            dcp = ConfigurationParameters.getDefault();
            System.err.println("[LoggingConfiguration] " + "Loading default configuration: " + dcp);
            apply(dcp);
        }
    }

    /**
     * Get the current configuration.
     *
     * @return The current configuration instance
     */
    public static LoggingConfiguration atPresent() {
        return instance_;
    }

    /**
     * Reinitialize the current configuration from a file.
     *
     * @param file The file to load the logging configuration from
     *
     * @throws IllegalArgumentException if the file name is <code>null</code>
     *   or empty
     * @throws LoggingException if initializing the configuration failed
     */
    public void load(String file) {
        if (file == null || file.length() == 0) {
            throw new IllegalArgumentException("No file name specified");
        }
        try {
            load(new FileInputStream(file));
        } catch (IOException e) {
            throw new LoggingException("Failed loading logging configuration from '" + file + "': " + e.getMessage());
        }
    }

    /**
     * Reinitialize the current configuration from a stream.
     *
     * @param is The stream to load the logging configuration from
     *
     * @throws IllegalArgumentException if the argument is <code>null</code>
     * @throws LoggingException if initializing the configuration failed
     */
    public void load(InputStream is) {
        Properties properties;
        Map variables;
        String value;
        String key;
        Iterator i;
        Map subst;
        if (is == null) {
            throw new IllegalArgumentException("No input stream specified");
        }
        properties = new Properties();
        variables = new HashMap();
        for (i = System.getProperties().keySet().iterator(); i.hasNext(); ) {
            key = (String) i.next();
            value = System.getProperty(key);
            variables.put(key, value);
        }
        subst = new HashMap(2);
        subst.put("\\", "/");
        subst.put(":", "\\:");
        try {
            properties.load(new StringSubstitutionInputStream(new VariableSubstitutionInputStream(is, variables), subst));
        } catch (IOException e) {
            throw new LoggingException("Cannot read input stream: " + e.getMessage());
        }
        apply(ConfigurationParameters.create(properties));
    }

    /**
     * Write the current configuration to a file.
     *
     * @param file The target logging configuration file
     *
     * @throws IllegalArgumentException if the argument is <code>null</code>
     *   or empty
     * @throws LoggingException in case the logging configuration file could
     *   not be written
     */
    public void save(String file) {
        if (file == null || file.length() == 0) {
            throw new IllegalArgumentException("No file name specified");
        }
        try {
            save(new FileOutputStream(file));
        } catch (IOException e) {
            throw new LoggingException("Writing logging configuration file failed: '" + file + "'");
        }
    }

    /**
     * Write the current configuration to a stream.
     *
     * @param os The target stream
     *
     * @throws IllegalArgumentException if the argument is <code>null</code>
     */
    public synchronized void save(OutputStream os) {
        if (os == null) {
            throw new IllegalArgumentException("No output stream specified");
        }
        parameters_.save(os);
    }

    /**
     * Apply the given configuration by trying to minimize changes
     * in order to keep alive as many output connections as possible.
     *
     * @param parameters The new configuration parameters
     *
     * @throws IllegalArgumentException if the argument is <code>null</code>
     */
    public synchronized void apply(ConfigurationParameters parameters) {
        LogLevel loglevel;
        Map.Entry entry;
        Integer buffer;
        String logger;
        Long interval;
        String name;
        Iterator i;
        Iterator j;
        Set remove;
        Set exist;
        Set urls;
        Set add;
        URL url;
        if (parameters == null) {
            throw new IllegalArgumentException("No parameters specified");
        }
        for (i = parameters.getLoglevelMap().entrySet().iterator(); i.hasNext(); ) {
            entry = (Map.Entry) i.next();
            name = (String) entry.getKey();
            loglevel = (LogLevel) entry.getValue();
            try {
                setLogLevel(name, loglevel);
            } catch (Exception e) {
                System.err.println("[LoggingConfiguration] Setting '" + name + ConfigurationParameters.PROPERTY_LOGLEVEL + "=" + loglevel + "' failed: " + e.getMessage());
            }
        }
        remove = new HashSet(parameters_.getLoglevelMap().keySet());
        remove.removeAll(parameters.getLoglevelMap().keySet());
        for (i = remove.iterator(); i.hasNext(); ) {
            name = (String) i.next();
            try {
                unsetLogLevel(name);
            } catch (Exception e) {
                System.err.println("[LoggingConfiguration] Unsetting '" + name + ConfigurationParameters.PROPERTY_LOGLEVEL + "' failed: " + e.getMessage());
            }
        }
        for (i = parameters.getOutputMap().entrySet().iterator(); i.hasNext(); ) {
            entry = (Map.Entry) i.next();
            name = (String) entry.getKey();
            urls = (Set) entry.getValue();
            add = new HashSet(urls);
            exist = parameters_.getOutput(name);
            add.removeAll(exist);
            for (j = add.iterator(); j.hasNext(); ) {
                url = (URL) j.next();
                try {
                    addOutput(name, url);
                } catch (Exception e) {
                    System.err.println("[LoggingConfiguration] Adding '" + name + ConfigurationParameters.PROPERTY_OUTPUT + "=" + url + "' failed: " + e.getMessage());
                }
            }
            remove = new HashSet(exist);
            remove.removeAll(urls);
            for (j = remove.iterator(); j.hasNext(); ) {
                url = (URL) j.next();
                try {
                    removeOutput(name, url);
                } catch (Exception e) {
                    System.err.println("[LoggingConfiguration] Removing '" + name + ConfigurationParameters.PROPERTY_OUTPUT + "=" + url + "' failed: " + e.getMessage());
                }
            }
        }
        remove = new HashSet(parameters_.getOutputMap().keySet());
        remove.removeAll(parameters.getOutputMap().keySet());
        for (i = remove.iterator(); i.hasNext(); ) {
            name = (String) i.next();
            try {
                removeOutput(name);
            } catch (Exception e) {
                System.err.println("[LoggingConfiguration] Removing '" + name + ConfigurationParameters.PROPERTY_OUTPUT + "' failed: " + e.getMessage());
            }
        }
        for (i = parameters.buffers_.entrySet().iterator(); i.hasNext(); ) {
            entry = (Map.Entry) i.next();
            url = (URL) entry.getKey();
            buffer = (Integer) entry.getValue();
            try {
                setBuffer(url, buffer);
            } catch (Exception e) {
                System.err.println("[LoggingConfiguration] Setting '" + url + ConfigurationParameters.PROPERTY_BUFFER + "=" + buffer + "' failed: " + e.getMessage());
            }
        }
        remove = new HashSet(parameters_.getBufferMap().keySet());
        remove.removeAll(parameters.getBufferMap().keySet());
        for (i = remove.iterator(); i.hasNext(); ) {
            url = (URL) i.next();
            try {
                unsetBuffer(url);
            } catch (Exception e) {
                System.err.println("[LoggingConfiguration] Unsetting '" + url + ConfigurationParameters.PROPERTY_BUFFER + "' failed: " + e.getMessage());
            }
        }
        interval = parameters.getInterval();
        if (interval != null) {
            try {
                setInterval(interval);
            } catch (Exception e) {
                System.err.println("[LoggingConfiguration] Setting '" + ConfigurationParameters.PROPERTY_INTERVAL + "=" + interval + "' failed: " + e.getMessage());
            }
        }
        if (parameters_.getInterval() == null) {
            System.err.println("[LoggingConfiguration] No interval specified");
            if (!ConfigurationParameters.DEFAULT_INTERVAL.equals(interval)) {
                System.err.println("[LoggingConfiguration] Loading default: " + ConfigurationParameters.PROPERTY_INTERVAL + "=" + ConfigurationParameters.DEFAULT_INTERVAL);
                setInterval(ConfigurationParameters.DEFAULT_INTERVAL);
            }
        }
        logger = parameters.getLogger();
        if (logger != null) {
            try {
                setLogger(logger);
            } catch (Exception e) {
                System.err.println("[LoggingConfiguration] Setting '" + ConfigurationParameters.PROPERTY_LOGGER + "=" + logger + "' failed: " + e.getMessage());
            }
        }
        if (parameters_.getLogger() == null) {
            System.err.println("[LoggingConfiguration] No logger specified");
            if (!ConfigurationParameters.DEFAULT_LOGGER.equals(logger)) {
                System.err.println("[LoggingConfiguration] Loading default: " + ConfigurationParameters.PROPERTY_LOGGER + "=" + ConfigurationParameters.DEFAULT_LOGGER);
                setLogger(ConfigurationParameters.DEFAULT_LOGGER);
            }
        }
        if (parameters_.getOutput("").isEmpty()) {
            System.err.println("[LoggingConfiguration] No root output specified");
            System.err.println("[LoggingConfiguration] Loading default: " + ConfigurationParameters.PROPERTY_OUTPUT + "=" + ConfigurationParameters.DEFAULT_OUTPUT);
            addOutput("", ConfigurationParameters.DEFAULT_OUTPUT);
        }
        if (parameters_.getLogLevel("") == null) {
            System.err.println("[LoggingConfiguration] No root log level specified");
            System.err.println("[LoggingConfiguration] Loading default: " + ConfigurationParameters.PROPERTY_LOGLEVEL + "=" + ConfigurationParameters.DEFAULT_LOGLEVEL);
            setLogLevel("", ConfigurationParameters.DEFAULT_LOGLEVEL);
        }
    }

    /**
     * @see #init()
     */
    public void reinit() {
        init();
    }

    /**
     * Change the <i>logger</i> property.
     * In case an exception is thrown while trying to apply the requested
     * property value changes, the previous configuration will be restored.
     *
     * @param logger The new <i>logger</i> value (as to be used in the
     *   logging configuration file), which might be as follows:
     *   <ul>
     *   <li><tt>auto</tt>
     *   <li><tt>sun</tt>
     *   <li><tt>log4j</tt>
     *   </ul>
     *   If set to <tt>auto</tt>, the logging mechanism with the highest
     *   priority will be chosen automatically. Priorities are as follows:
     *   <ol>
     *   <li><tt>sun</tt>
     *   <li><tt>log4j</tt>
     *   </ol>
     *   Default value is: <tt>auto</tt>
     *
     * @return <code>true</code> if the property value has been
     *   changed successfully, <code>false</code> if the property value
     *   did not need to be changed since it is already equal to the
     *   given value
     *
     * @throws IllegalArgumentException if the argument is <code>null</code>
     * @throws LoggingException in case the requested property value could not
     *   be changed
     */
    public synchronized boolean setLogger(String logger) {
        boolean changed;
        String prev;
        if (logger == null) {
            throw new IllegalArgumentException("No logger specified");
        }
        prev = parameters_.getLogger();
        changed = parameters_.setLogger(logger);
        if (changed) {
            try {
                LoggerFactory.changeLogger(logger);
            } catch (LoggingException e) {
                parameters_.undo();
                throw e;
            }
            fireLoggerChanged(prev, logger);
        }
        return changed;
    }

    /**
     * Get the current <i>logger</i> property value.
     *
     * @return The <i>logger</i> property value
     */
    public String getLogger() {
        return parameters_.getLogger();
    }

    /**
     * Change the <i>interval</i> property.
     * In case an exception is thrown while trying to apply the requested
     * property value changes, the previous configuration will be restored.
     *
     * @param interval The new <i>interval</i> value (as to be used in the
     *   logging configuration file), which might be as follows:
     *   <ul>
     *   <li><tt>off</tt>
     *   <li><tt>&lt;millis&gt;</tt><br>
     *      (number of millseconds)
     *   <li><tt>&lt;secs&gt;s</tt><br>
     *      (number of seconds, e.g. <tt>5s</tt>)
     *   <li><tt>&lt;mins&gt;m</tt><br>
     *      (number of minutes, e.g. <tt>10m</tt>)
     *   <li><tt>&lt;hrs&gt;h</tt><br>
     *      (number of hours, e.g. <tt>2.5h</tt>)
     *   <li><tt>&lt;days&gt;d</tt><br>
     *      (number of days, e.g. <tt>2d</tt>)
     *   </ul>
     *   If set to <tt>0</tt> (zero) or <tt>off</tt>, no periodic anchor
     *   timestamps, but instead, the full timestamp will be written into
     *   every log entry. Default value is: <tt>off</tt>
     *
     * @return <code>true</code> if the property value has been
     *   changed successfully, <code>false</code> if the property value
     *   did not need to be changed since it is already equal to the
     *   given value
     */
    public boolean setInterval(String interval) {
        return setInterval(ConfigurationParameters.parseInterval(interval));
    }

    /**
     * @see #setInterval(String)
     *
     * @throws IllegalArgumentException if the argument is <code>null</code>
     *   or less than zero
     */
    public synchronized boolean setInterval(Long interval) {
        boolean changed;
        Long prev;
        if (interval == null) {
            throw new IllegalArgumentException("No interval specified");
        }
        if (interval.longValue() < 0) {
            throw new IllegalArgumentException("Invalid interval: " + interval);
        }
        prev = parameters_.getInterval();
        changed = parameters_.setInterval(interval);
        if (changed) {
            Chronometer.changeInterval(interval);
            fireIntervalChanged(prev, interval);
        }
        return changed;
    }

    /**
     * Get the current <i>interval</i> value.
     *
     * @return The current <i>interval</i> value
     */
    public Long getInterval() {
        return parameters_.getInterval();
    }

    /**
     * Set the <i>buffer</i> property for a specific output destination.
     * In case an exception is thrown while trying to apply the requested
     * property value changes, the previous configuration will be restored.
     *
     * @param output The output destination <code>URL</code> to set the
     *   <i>buffer</i> for
     * @param buffer The new <i>buffer</i> property value (as to be used
     *   in the logging configuration file), which might be as follows:
     *   <ul>
     *   <li><tt>off</tt>
     *   <li><tt>&lt;msgs&gt;</tt><br>
     *      (number of messages)
     *   <li><tt>&lt;msgs/k&gt;k</tt><br>
     *      (number of thousands of messages;
     *      e.g. <tt>2.5k</tt> corresponds to 2,500 messages)
     *   <li><tt>&lt;msgs/M&gt;m</tt><br>
     *      (number of millions of messages;
     *      e.g. <tt>1.5m</tt> corresponds to 1,500,000 messages)
     *   </ul>
     *   According to the buffer size, logging will be carried out either
     *   synchronously or asynchronously. For synchronous dispatching, the
     *   buffer size needs to be <tt>0</tt> (zero) or <tt>off</tt>. Buffer
     *   sizes other than <tt>off</tt> or greater than zero will implicitly
     *   activate the Asynchronous Message Dispatcher. If there is no buffer
     *   size defined for a specific URL, the default (which depends on the
     *   protocol identifier) will be used:
     *   <ul>
     *   <li>for files: <tt>off</tt> (synchronous dispatching)
     *   <li>otherwise: <tt>10k</tt> (asynchronous dispatching)
     *   </ul>
     *
     * @return <code>true</code> if the property value has been
     *   changed successfully, <code>false</code> if the property value
     *   did not need to be changed since it is already equal to the
     *   given value
     */
    public boolean setBuffer(String output, String buffer) {
        return setBuffer(ConfigurationParameters.parseOutput(output), ConfigurationParameters.parseBuffer(buffer));
    }

    /**
     * @see #setBuffer(String,String)
     *
     * @throws IllegalArgumentException if any argument is <code>null</code>
     *   or the buffer size is less than zero
     * @throws LoggingException in case the requested property value could not
     *   be set
     */
    public synchronized boolean setBuffer(URL output, Integer buffer) {
        boolean changed;
        Integer prev;
        if (output == null) {
            throw new IllegalArgumentException("No output specified");
        }
        if (buffer == null) {
            throw new IllegalArgumentException("No buffer specified");
        }
        if (buffer.intValue() < 0) {
            throw new IllegalArgumentException("Invalid buffer size: " + buffer);
        }
        checkOutput(output);
        prev = parameters_.getBuffer(output);
        changed = parameters_.setBuffer(output, buffer);
        if (changed) {
            try {
                AbstractWrapper.changeBuffer(output, buffer);
            } catch (LoggingException e) {
                parameters_.undo();
                throw e;
            }
            fireBufferChanged(output, prev, buffer);
        }
        return changed;
    }

    /**
     * Unset the <i>buffer</i> property of a specific output destination.
     * In case an exception is thrown while trying to apply the requested
     * property value changes, the previous configuration will be restored.
     *
     * @param output The output destination <code>URL</code> to unset the
     *   <i>buffer</i> for
     *
     * @return <code>true</code> if the property value has been
     *   changed successfully, <code>false</code> if the property value
     *   did not need to be changed since it is already equal to the
     *   given value
     */
    public boolean unsetBuffer(String output) {
        return unsetBuffer(ConfigurationParameters.parseOutput(output));
    }

    /**
     * @see #unsetBuffer(String)
     *
     * @throws IllegalArgumentException if the argument is <code>null</code>
     * @throws LoggingException in case the requested property value could not
     *   be unset
     */
    public synchronized boolean unsetBuffer(URL output) {
        boolean changed;
        Integer prev;
        if (output == null) {
            throw new IllegalArgumentException("No output specified");
        }
        prev = parameters_.getBuffer(output);
        changed = parameters_.unsetBuffer(output);
        if (changed) {
            try {
                AbstractWrapper.changeBuffer(output, getBuffer(output));
            } catch (LoggingException e) {
                parameters_.undo();
                throw e;
            }
            fireBufferChanged(output, prev, null);
        }
        return changed;
    }

    /**
     * Get the <i>buffer</i> value of an output destination <code>URL</code>.
     * In case there is no buffer value configured for the given
     * <code>URL</code>, the default value (which depends on the protocol
     * identifier) will be returned.
     *
     * @param output The corresponding <code>URL</code>
     *
     * @return The corresponding <i>buffer</i> value or the default value
     *   in case no buffer is defined for the given <code>URL</code>
     */
    public synchronized Integer getBuffer(URL output) {
        if (output == null) {
            return null;
        }
        return parameters_.getBuffer(output);
    }

    /**
     * Add an <i>output</i> destination <code>URL</code>.
     * In case an exception is thrown while trying to apply the requested
     * property value changes, the previous configuration will be restored.
     *
     * @param name The class/package name to add the new output
     *   destination to
     * @param output The new <i>output</i> destination <code>URL</code>
     *   To configure the root logger, simply omit the package name. The root
     *   logger always needs to be configured. If omitted, the default will
     *   be used. The default value is: <tt>file:///./default.log</tt>
     *
     * @return <code>true</code> if the property value has been
     *   changed successfully, <code>false</code> if the property value
     *   did not need to be changed since it is already equal to the
     *   given value
     */
    public boolean addOutput(String name, String output) {
        return addOutput(name, ConfigurationParameters.parseOutput(output));
    }

    /**
     * @see #addOutput(String,String)
     *
     * @throws IllegalArgumentException if any argument is <code>null</code>
     * @throws LoggingException in case the requested property value could not
     *   be added
     */
    public synchronized boolean addOutput(String name, URL output) {
        boolean added;
        Set urls;
        if (name == null) {
            throw new IllegalArgumentException("No name specified");
        }
        if (output == null) {
            throw new IllegalArgumentException("No output specified");
        }
        checkOutput(output);
        urls = parameters_.getOutput(name);
        added = parameters_.addOutput(name, output);
        if (added) {
            try {
                if (urls == null) {
                    AbstractWrapper.initOutput(name, output);
                } else {
                    AbstractWrapper.addOutput(name, output);
                }
            } catch (LoggingException e) {
                parameters_.undo();
                throw e;
            }
            if (urls == null) {
                try {
                    LoggerFactory.updateProxies();
                } catch (LoggingException e) {
                    parameters_.undo();
                    try {
                        AbstractWrapper.deleteOutput(name);
                    } catch (LoggingException e2) {
                    }
                    throw e;
                }
            }
            fireOutputChanged(name, ((urls != null) ? new HashSet(urls) : null), output);
        }
        return added;
    }

    /**
     * Remove an <i>output</i> destination.
     * In case an exception is thrown while trying to apply the requested
     * property value changes, the previous configuration will be restored.
     *
     * @param name The class/package name to remove the output
     *   destination from
     * @param output The <i>output</i> destination <code>URL</code>
     *   to be removed
     *
     * @return <code>true</code> if the property value has been
     *   changed successfully, <code>false</code> if the property value
     *   did not need to be changed since it is already equal to the
     *   given value
     */
    public boolean removeOutput(String name, String output) {
        return removeOutput(name, ConfigurationParameters.parseOutput(output));
    }

    /**
     * @see #removeOutput(String,String)
     *
     * @throws IllegalArgumentException if any argument is <code>null</code> or
     *   on the attempt to remove the last <tt>output</tt> <code>URL</code> of
     *   the root entry
     * @throws LoggingException in case the requested property value could not
     *   be removed
     */
    public synchronized boolean removeOutput(String name, URL output) {
        boolean removed;
        Set urls;
        if (name == null) {
            throw new IllegalArgumentException("No name specified");
        }
        if (output == null) {
            throw new IllegalArgumentException("No output specified");
        }
        urls = parameters_.getOutput(name);
        if (name.length() > 0 || urls.size() > 1) {
            removed = parameters_.removeOutput(name, output);
            if (removed) {
                if (urls.isEmpty()) {
                    try {
                        LoggerFactory.updateProxies();
                    } catch (LoggingException e) {
                        parameters_.undo();
                        throw e;
                    }
                }
                try {
                    if (urls.isEmpty()) {
                        AbstractWrapper.deleteOutput(name);
                    } else {
                        AbstractWrapper.removeOutput(name, output);
                    }
                } catch (LoggingException e) {
                    parameters_.undo();
                    if (urls.isEmpty()) {
                        try {
                            LoggerFactory.updateProxies();
                        } catch (LoggingException e2) {
                        }
                    }
                    throw e;
                }
                fireOutputChanged(name, output, ((urls != null) ? new HashSet(urls) : null));
            }
        } else {
            throw new IllegalArgumentException("The last root output URL cannot be removed");
        }
        return removed;
    }

    /**
     * Remove all <i>output</i> destinations of the given name.
     * <p><b>Notice:</b> In case an exception is thrown while trying to apply
     * necessary property value changes, the previous configuration will
     * <em>not</em> be restored.
     *
     * @param name The class/package name to remove all output
     *   destinations from
     *
     * @return <code>true</code> if all <i>output</i> destinations have been
     *   removed successfully, <code>false</code> if at least one <i>output</i>
     *   destination could not be removed
     *
     * @throws IllegalArgumentException if the argument is <code>null</code>
     */
    public synchronized boolean removeOutput(String name) {
        boolean removed;
        Iterator i;
        Set urls;
        if (name == null) {
            throw new IllegalArgumentException("No name specified");
        }
        removed = true;
        urls = parameters_.getOutput(name);
        if (urls != null) {
            for (i = new HashSet(urls).iterator(); i.hasNext(); ) {
                try {
                    removed &= removeOutput(name, (URL) i.next());
                } catch (LoggingException e) {
                    removed = false;
                }
            }
        }
        return removed;
    }

    /**
     * Get all output <code>URL</code>s for the specified name. If there
     * are none defined for the given name, find the closest match.
     *
     * @param name The name to search the output URLs for
     *
     * @return The best matching name assigned to the corresponding
     *   <code>Set</code> of output destination {@link URL} objects
     */
    public synchronized ConfigurationParameters.BestMatch getOutput(String name) {
        if (name == null) {
            return null;
        }
        return parameters_.getBestOutput(name);
    }

    /**
     * Set the <i>loglevel</i> property of a specific class/package name.
     * In case an exception is thrown while trying to apply the requested
     * property value changes, the previous configuration will be restored.
     *
     * @param name The class/package name to set the log level for
     * @param loglevel The new <i>loglevel</i> value (as to be used in the
     *   logging configuration file), which might be as follows:
     *   <ul>
     *   <li><tt>ALL</tt>
     *   <li><tt>TRACE</tt>
     *   <li><tt>DEBUG</tt>
     *   <li><tt>INFO</tt>
     *   <li><tt>WARNING</tt>
     *   <li><tt>ERROR</tt>
     *   <li><tt>SEVERE</tt>
     *   <li><tt>FATAL</tt>
     *   <li><tt>OFF</tt>
     *   </ul>
     *   To configure the root logger, simply omit the package name. The root
     *   logger always needs to be configured. If omitted, the default will
     *   be used. The default value is: <tt>ERROR</tt>
     *
     * @return <code>true</code> if the property value has been
     *   changed successfully, <code>false</code> if the property value
     *   did not need to be changed since it is already equal to the
     *   given value
     */
    public boolean setLogLevel(String name, String loglevel) {
        return setLogLevel(name, ConfigurationParameters.parseLogLevel(loglevel));
    }

    /**
     * @see #setLogLevel(String,String)
     *
     * @throws IllegalArgumentException if any argument is <code>null</code>
     * @throws LoggingException in case the requested property value could not
     *   be set
     */
    public synchronized boolean setLogLevel(String name, LogLevel loglevel) {
        boolean changed;
        LogLevel prev;
        if (name == null) {
            throw new IllegalArgumentException("No name specified");
        }
        if (loglevel == null) {
            throw new IllegalArgumentException("No loglevel specified");
        }
        prev = parameters_.getLogLevel(name);
        changed = parameters_.setLogLevel(name, loglevel);
        if (changed) {
            try {
                if (prev == null) {
                    AbstractWrapper.addLogLevel(name, loglevel);
                } else {
                    AbstractWrapper.changeLogLevel(name, loglevel);
                }
            } catch (LoggingException e) {
                parameters_.undo();
                throw e;
            }
            if (prev == null) {
                try {
                    LoggerFactory.updateProxies();
                } catch (LoggingException e) {
                    parameters_.undo();
                    try {
                        AbstractWrapper.removeLogLevel(name);
                    } catch (LoggingException e2) {
                    }
                    throw e;
                }
            }
            fireLogLevelChanged(name, prev, loglevel);
        }
        return changed;
    }

    /**
     * Unset the <i>loglevel</i> property of a specific class/package name.
     * In case an exception is thrown while trying to apply the requested
     * property value changes, the previous configuration will be restored.
     *
     * @param name The class/package name to unset the log level for
     *
     * @return <code>true</code> if the property value has been
     *   changed successfully, <code>false</code> if the property value
     *   did not need to be changed since it is already equal to the
     *   given value
     *
     * @throws IllegalArgumentException if the argument is <code>null</code> on
     *   the attempt to remove the root <tt>loglevel</tt>
     * @throws LoggingException in case the requested property value could not
     *   be unset
     */
    public synchronized boolean unsetLogLevel(String name) {
        boolean changed;
        LogLevel prev;
        if (name == null) {
            throw new IllegalArgumentException("No name specified");
        }
        if (name.length() == 0) {
            throw new IllegalArgumentException("The root log level needs to be defined");
        }
        prev = parameters_.getLogLevel(name);
        changed = parameters_.unsetLogLevel(name);
        if (changed) {
            try {
                LoggerFactory.updateProxies();
            } catch (LoggingException e) {
                parameters_.undo();
                throw e;
            }
            try {
                AbstractWrapper.removeLogLevel(name);
            } catch (LoggingException e) {
                parameters_.undo();
                try {
                    LoggerFactory.updateProxies();
                } catch (LoggingException e2) {
                }
                throw e;
            }
            fireLogLevelChanged(name, prev, null);
        }
        return changed;
    }

    /**
     * Get the log level of a logger by its package name. If there
     * is none defined for the given name, find the closest match.
     *
     * @param name The name to search the log level for
     *
     * @return The best matching name assigned to the corresponding
     *   {@link LogLevel} object
     */
    public synchronized ConfigurationParameters.BestMatch getLogLevel(String name) {
        if (name == null) {
            return null;
        }
        return parameters_.getBestLogLevel(name);
    }

    /**
     * Get a printout of all properties for a specific class/package name.
     *
     * @param name The class/package name to get the printout for
     *
     * @return The listed properties for the given name
     */
    public synchronized String list(String name) {
        ConfigurationParameters.BestMatch loglevel;
        ConfigurationParameters.BestMatch output;
        StringBuffer list;
        Integer buf;
        Iterator i;
        String tmp;
        URL url;
        int j;
        if (name == null) {
            return "";
        }
        list = new StringBuffer("Logging Configuration for ");
        list.append((name.length() > 0) ? (tmp = name) : (tmp = "<root>")).append(CR + "==========================");
        for (j = tmp.length(); j > 0; j--) {
            list.append("=");
        }
        list.append(CR + "Logger:   " + LoggerFactory.underlyingLoggingMechanism());
        list.append(CR + "Interval: " + ConfigurationParameters.convertInterval(parameters_.getInterval()));
        output = getOutput(name);
        tmp = output.getName();
        list.append(CR + "Output:   " + "from " + ((tmp.length() > 0 ? tmp : "<root>")));
        for (i = ((Set) output.getValue()).iterator(); i.hasNext(); ) {
            url = (URL) i.next();
            buf = getBuffer(url);
            list.append(CR + "          " + url + " (" + ((buf.intValue() > 0) ? ("asynchronous,buffer=" + ConfigurationParameters.convertBuffer(buf)) : "synchronous") + ")");
        }
        loglevel = getLogLevel(name);
        tmp = loglevel.getName();
        list.append(CR + "LogLevel: from " + ((tmp.length() > 0) ? tmp : "<root>") + CR + "          " + ((LogLevel) loglevel.getValue()).getName());
        return list.toString();
    }

    /**
     * @see ConfigurationParameters#findBestName(String)
     */
    protected String findBestName(String target) {
        return parameters_.findBestName(target);
    }

    /**
     * Check the given <i>output</i> destination <code>URL</code> by trying
     * to establish a connection and closing the stream right after.
     *
     * @param output The <i>output</i> destination <code>URL</code> to test
     *
     * @throws LoggingException if establishing the connection failed
     * @throws NullPointerException if the <i>output</i> <code>URL</code> is
     *   <code>null</code>
     */
    protected static void checkOutput(URL output) {
        URLOutputStream test;
        if (output == null) {
            throw new NullPointerException("output");
        }
        test = null;
        try {
            test = new URLOutputStream(output);
        } catch (IOException e) {
            throw new LoggingException(e.getMessage());
        } finally {
            if (test != null) {
                try {
                    test.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * Print out some information about available command line options.
     */
    public void help() {
        System.out.println(CommandLineInterface.usage());
    }

    /**
     * Register for configuration change notifications
     */
    public void addConfigurationChangeListener(ConfigurationChangeListener listener) {
        configurationChangeListeners_.add(listener);
    }

    /**
     * Deregister from configuration change notifications
     */
    public void removeConfigurationChangeListener(ConfigurationChangeListener listener) {
        configurationChangeListeners_.remove(listener);
    }

    /**
     * Fire an appropriate {@link ConfigurationChangeEvent} to
     * {@link ConfigurationChangeListener#loggerChanged} of all
     * registered listener objects.
     *
     * @param oldval The previous <i>logger</i> property value
     * @param newval The new <i>logger</i> property value
     */
    private void fireLoggerChanged(String oldval, String newval) {
        ConfigurationChangeEvent cce;
        Iterator i;
        cce = new ConfigurationChangeEvent(this, null, oldval, newval);
        synchronized (configurationChangeListeners_) {
            for (i = configurationChangeListeners_.iterator(); i.hasNext(); ) {
                ((ConfigurationChangeListener) i.next()).loggerChanged(cce);
            }
        }
    }

    /**
     * Fire an appropriate {@link ConfigurationChangeEvent} to
     * {@link ConfigurationChangeListener#intervalChanged} of all
     * registered listener objects.
     *
     * @param oldval The previous <i>interval</i> property value
     * @param newval The new <i>interval</i> property value
     */
    private void fireIntervalChanged(Long oldval, Long newval) {
        ConfigurationChangeEvent cce;
        Iterator i;
        cce = new ConfigurationChangeEvent(this, null, oldval, newval);
        synchronized (configurationChangeListeners_) {
            for (i = configurationChangeListeners_.iterator(); i.hasNext(); ) {
                ((ConfigurationChangeListener) i.next()).intervalChanged(cce);
            }
        }
    }

    /**
     * Fire an appropriate {@link ConfigurationChangeEvent} to
     * {@link ConfigurationChangeListener#bufferChanged} of all
     * registered listener objects.
     *
     * @param url The <code>URL</code> of the changed buffer value
     * @param oldval The previous <i>buffer</i> property value
     * @param newval The current <i>buffer</i> property value
     */
    private void fireBufferChanged(URL url, Integer oldval, Integer newval) {
        ConfigurationChangeEvent cce;
        Iterator i;
        cce = new ConfigurationChangeEvent(this, url, oldval, newval);
        synchronized (configurationChangeListeners_) {
            for (i = configurationChangeListeners_.iterator(); i.hasNext(); ) {
                ((ConfigurationChangeListener) i.next()).bufferChanged(cce);
            }
        }
    }

    /**
     * Fire an appropriate {@link ConfigurationChangeEvent} to
     * {@link ConfigurationChangeListener#outputChanged} of all
     * registered listener objects.
     *
     * @param name The class/package name of the changed output destination
     *   <code>URL</code>
     * @param oldval The previous output destination(s);
     *   in case a new {@link URL} (to be found in <i>curval</i>) has been
     *   added, or <code>null</code> if there were no output destinations
     *   defined yet, or a <code>Set</code> of previously assigned
     *   <code>URL</code>s for the given name; if a single <code>URL</code>,
     *   this one has been removed (possibly remaining destinations may be
     *   found in <i>newval</i>)
     * @param newval The current output destination(s);
     *   in case an existing {@link URL} (to be found in <i>preval</i>) has
     *   been removed, or <code>null</code> if there are no output
     *   destinations defined anymore, or a <code>Set</code> of remaining
     *   <code>URL</code>s for the given name; if a single <code>URL</code>,
     *   this one has been added (previously existing destinations may be
     *   found in <i>preval</i>)
     */
    private void fireOutputChanged(String name, Object oldval, Object newval) {
        ConfigurationChangeEvent cce;
        Iterator i;
        cce = new ConfigurationChangeEvent(this, name, oldval, newval);
        synchronized (configurationChangeListeners_) {
            for (i = configurationChangeListeners_.iterator(); i.hasNext(); ) {
                ((ConfigurationChangeListener) i.next()).outputChanged(cce);
            }
        }
    }

    /**
     * Fire an appropriate {@link ConfigurationChangeEvent} to
     * {@link ConfigurationChangeListener#logLevelChanged} of all
     * registered listener objects.
     *
     * @param name The class/package name of the changed {@link LogLevel}
     * @param oldval The previous <i>loglevel</i> property value of the
     *   given name
     * @param newval The current <i>loglevel</i> property value of the
     *   given name
     */
    private void fireLogLevelChanged(String name, LogLevel oldval, LogLevel newval) {
        ConfigurationChangeEvent cce;
        Iterator i;
        cce = new ConfigurationChangeEvent(this, name, oldval, newval);
        synchronized (configurationChangeListeners_) {
            for (i = configurationChangeListeners_.iterator(); i.hasNext(); ) {
                ((ConfigurationChangeListener) i.next()).logLevelChanged(cce);
            }
        }
    }

    /**
     * Provides a {@link CommandLineInterface} which publishes
     * almost all public methods. For more information just run
     * <p><nobreak><tt>java LoggingConfiguration -help</tt></nobreak>
     *
     * @param args The command line arguments
     */
    public static void main(String[] args) {
        try {
            CommandLineInterface.getInstance().process(args);
        } catch (LoggingException e) {
            System.err.println("[LoggingConfiguration] " + e.getMessage());
        } catch (Throwable t) {
            System.err.println("Internal error. " + "Please report the following stacktrace to the SeMoA team:");
            t.printStackTrace();
        }
    }
}
