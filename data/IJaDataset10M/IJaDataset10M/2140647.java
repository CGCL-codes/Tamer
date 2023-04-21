package com.vmix.simplemq.daemon;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.xml.DOMConfigurator;
import org.simpleframework.xml.*;
import org.simpleframework.xml.load.*;
import com.vmix.simplemq.daemon.config.*;
import com.vmix.simplemq.daemon.handlers.*;

public class ConfigurationManager {

    private static Logger logger;

    private static final String guidfile = ".guid_";

    private static final String configPath = "/etc/simpleMQ/";

    private static final String configDefault = configPath + "default-config.xml";

    private static final String log4jDefault = configPath + "mq-log4j.xml";

    private static ConfigurationManager instance;

    public static void initialize(String[] args) {
        boolean consoleLogging = true;
        String configFile = configDefault;
        String log4jConfig = log4jDefault;
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("-conf")) {
                    configFile = args[i + 1];
                } else if (args[i].equals("-silent") || args[i].equals("-s")) {
                    consoleLogging = false;
                    LogManager.getRootLogger().setLevel(Level.OFF);
                } else if (args[i].equals("-l") || args[i].equals("-log4j")) {
                    log4jConfig = args[i + 1];
                }
            }
        }
        logger = Logger.getLogger(ConfigurationManager.class);
        if (new File(log4jConfig).exists()) {
            consoleLogging = false;
            DOMConfigurator.configureAndWatch(log4jConfig);
            logger.debug("got log4j config file");
        }
        if (consoleLogging) {
            Layout layout = new PatternLayout("%-6r %-25F %m\n");
            ConsoleAppender appender = new ConsoleAppender(layout);
            BasicConfigurator.configure(appender);
            logger.debug("set up console logging");
        }
        logger.debug("logger appender configured");
        instance = new ConfigurationManager();
        instance.init(configFile);
    }

    public static void initialize(DaemonConfig configuration) {
        if (logger == null) {
            logger = Logger.getLogger(ConfigurationManager.class);
        }
        instance = new ConfigurationManager();
        instance.init(configuration);
    }

    public static int getBindPort() {
        return instance.config.port;
    }

    public static boolean isDefaultHostDaemon() {
        return instance.config.isDefaultHostDaemon;
    }

    public static Guid getDaemonGuid() {
        if (instance.daemonGuid == null) {
            logger.debug("don't have a guid yet");
            File guidFile = new File(getDataPath(guidfile + instance.config.port));
            if (guidFile.exists()) {
                try {
                    logger.debug("trying to get guid from file: " + guidFile.getAbsolutePath());
                    FileInputStream inStream = new FileInputStream(guidFile);
                    InputStreamReader inStreamReader = new InputStreamReader(inStream);
                    BufferedReader reader = new BufferedReader(inStreamReader);
                    String guidString = reader.readLine();
                    instance.daemonGuid = Guid.fromGuidString(guidString);
                    reader.close();
                    inStreamReader.close();
                    inStream.close();
                } catch (Exception e) {
                    logger.error("unable to write out daemon guid file", e);
                }
            }
            if (instance.daemonGuid == null || !instance.daemonGuid.isValid()) {
                logger.debug("need to create a new guid");
                instance.daemonGuid = Guid.getInstance("simpleMQ");
                try {
                    FileOutputStream outstream = new FileOutputStream(guidFile, false);
                    PrintWriter writer = new PrintWriter(outstream);
                    writer.println(instance.daemonGuid.toString());
                    writer.flush();
                    writer.close();
                    outstream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                logger.debug("ready to register guid");
                getRegistrar().registerDaemon(instance.daemonGuid);
                logger.debug("guid registered");
            }
        }
        return instance.daemonGuid;
    }

    public static MessageHandler getRegistrationQueueHandler() {
        return getMessageHandler(instance.config.registrationQueue, true);
    }

    public static String getDataPath(String fileName) {
        String path = instance.config.dataPath + fileName;
        createPath(path);
        return path;
    }

    public static String getLogPath(String fileName) {
        String path = instance.config.logPath + fileName;
        createPath(path);
        return path;
    }

    private static void createPath(String path) {
        File f = new File(path);
        f.getParentFile().mkdirs();
    }

    public static MessageHandler getMessageHandler(String queueName, boolean ignoreDefault) {
        MessageHandler handler = null;
        if (instance.handlers.containsKey(queueName)) {
            handler = instance.handlers.get(queueName);
            if (handler instanceof MessageHandlerAlias) {
                handler = ((MessageHandlerAlias) handler).getHandler();
            }
        } else if (!ignoreDefault && instance.defaultHandler != null) {
            handler = instance.defaultHandler;
        }
        return handler;
    }

    public static MessageHandler[] getMessageHandlers() {
        ArrayList<MessageHandler> uniqueHandlers = new ArrayList<MessageHandler>();
        for (MessageHandler handler : instance.handlers.values()) {
            if (!(handler instanceof MessageHandlerAlias)) {
                uniqueHandlers.add(handler);
            }
        }
        return uniqueHandlers.toArray(new MessageHandler[0]);
    }

    public static Registrar getRegistrar() {
        if (instance.registrar == null) {
            MessageHandler registrationQueueHandler = getRegistrationQueueHandler();
            instance.registrar = new Registrar(registrationQueueHandler);
        }
        return instance.registrar;
    }

    private String configFile;

    private DaemonConfig config;

    private HashMap<String, QueueConfig> configLookup;

    private HashMap<String, MessageHandler> handlers;

    private MessageHandler defaultHandler = null;

    private Registrar registrar;

    private Guid daemonGuid;

    private ConfigurationManager() {
    }

    private void init(String configFile) {
        this.configFile = configFile;
        DaemonConfig configuration = null;
        logger.debug("conf: " + this.configFile);
        Serializer serializer = new Persister();
        try {
            File source = new File(this.configFile);
            configuration = serializer.read(DaemonConfig.class, source);
        } catch (Exception e) {
            logger.error("ConfigurationManager failed to load config file", e);
            throw new ConfigurationException("Unable to read configuration file '" + this.configFile + "'", e);
        }
        init(configuration);
    }

    private void init(DaemonConfig configuration) {
        config = configuration;
        configLookup = new HashMap<String, QueueConfig>();
        handlers = new HashMap<String, MessageHandler>();
        String patternString = "^[0-9a-z-]+$";
        Pattern p = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
        ArrayList<QueueConfig> aliases = new ArrayList<QueueConfig>();
        for (QueueConfig queueConfig : config.queues) {
            queueConfig.name = queueConfig.name.toLowerCase();
            Matcher m = p.matcher(queueConfig.name);
            if (!m.find()) {
                throw new ConfigurationException("Illegal queue name: '" + queueConfig.name + "'");
            }
            if (configLookup.containsKey(queueConfig.name)) {
                throw new ConfigurationException("Duplicate queue definition for '" + queueConfig.name + "'");
            }
            configLookup.put(queueConfig.name, queueConfig);
            switch(queueConfig.handler) {
                case alias:
                    aliases.add(queueConfig);
                    break;
                case forward:
                    handlers.put(queueConfig.name, new StoreAndForwardQueue(queueConfig));
                    break;
                case pull:
                    handlers.put(queueConfig.name, new PullQueue(queueConfig));
                    break;
                case log:
                    handlers.put(queueConfig.name, new LogWriter(queueConfig));
                    break;
                case multiplex:
                    handlers.put(queueConfig.name, new Multiplexer(queueConfig));
                    break;
                case router:
                    handlers.put(queueConfig.name, new Router(queueConfig));
                    break;
                case sink:
                    handlers.put(queueConfig.name, new MessageSink(queueConfig));
                    break;
            }
        }
        for (QueueConfig aliasConfig : aliases) {
            MessageHandler handler = handlers.get(aliasConfig.aliasTarget);
            handlers.put(aliasConfig.name, new MessageHandlerAlias(aliasConfig, handler));
        }
        if (config.defaultQueue != null && config.defaultQueue.length() > 0) {
            defaultHandler = handlers.get(config.defaultQueue);
            if (defaultHandler == null) {
                throw new ConfigurationException("Unable to set default queue: No queue by the name '" + config.defaultQueue + "' exists");
            }
        }
        if (config.registrationQueue == null || config.registrationQueue.length() == 0) {
            throw new ConfigurationException("A registration queue must be defined");
        } else if (handlers.get(config.registrationQueue) == null) {
            throw new ConfigurationException("Unable to set registration queue queue: No queue by the name '" + config.registrationQueue + "' exists");
        }
        for (QueueConfig queueConfig : config.queues) {
            switch(queueConfig.handler) {
                case alias:
                    if (handlers.get(queueConfig.aliasTarget) == null) {
                        throw new ConfigurationException("Invalid alias '" + queueConfig.name + "': No target queue by the name '" + queueConfig.aliasTarget + "' exists");
                    }
                    break;
                case forward:
                    ForwardTargetConfig target = queueConfig.forwardTarget;
                    if (target == null) {
                        throw new ConfigurationException("Invalid forwarder '" + queueConfig.name + "': Missing forward target config section");
                    } else if (target.host == null || target.host.length() == 0) {
                        throw new ConfigurationException("Invalid forwarder '" + queueConfig.name + "': Forward host not defined");
                    } else if (target.port <= 0 || target.port > 33000) {
                        throw new ConfigurationException("Invalid forwarder '" + queueConfig.name + "': Bad port " + target.port);
                    }
                    break;
                case pull:
                    break;
                case log:
                    LogFileConfig log = queueConfig.logFile;
                    if (log == null) {
                        throw new ConfigurationException("Invalid logger '" + queueConfig.name + "': Missing log file definition");
                    } else if (log.fileRoot == null || log.fileRoot.length() == 0) {
                        throw new ConfigurationException("Invalid logger '" + queueConfig.name + "': Log root file not defined");
                    }
                    break;
                case multiplex:
                    if (queueConfig.destinations == null || queueConfig.destinations.size() == 0) {
                        throw new ConfigurationException("Invalid multiplexer '" + queueConfig.name + "': At least one destination must be defined");
                    }
                    for (DestinationConfig destination : queueConfig.destinations) {
                        if (handlers.get(destination.name) == null) {
                            throw new ConfigurationException("Invalid multiplexer '" + queueConfig.name + "': No destination queue by the name '" + destination.name + "' exists");
                        }
                    }
                    break;
                case router:
                    if (queueConfig.defaultRoute != null && queueConfig.defaultRoute.length() > 0) {
                        if (handlers.get(queueConfig.defaultRoute) == null) {
                            throw new ConfigurationException("Invalid router '" + queueConfig.name + "': No default route by the name '" + queueConfig.defaultRoute + "' exists");
                        }
                    }
                    for (DestinationConfig destination : queueConfig.destinations) {
                        if (handlers.get(destination.name) == null) {
                            throw new ConfigurationException("Invalid router '" + queueConfig.name + "': No destination queue by the name '" + destination.name + "' exists");
                        }
                        if (destination.source == null || destination.source.length() == 0) {
                            throw new ConfigurationException("Invalid router '" + queueConfig.name + "': Missing source for destination '" + destination.name + "' exists");
                        }
                    }
                    break;
                case sink:
                    break;
            }
        }
        for (QueueConfig queueConfig : config.queues) {
            checkRecursion(queueConfig.name);
        }
    }

    private void checkRecursion(String startingQueue) {
        ArrayList<String> queueStack = new ArrayList<String>();
        checkRecursion(queueStack, startingQueue);
    }

    private void checkRecursion(ArrayList<String> queueStack, String checkQueue) {
        if (queueStack.contains(checkQueue)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Infinite queue recursion detected with queue '");
            sb.append(checkQueue);
            sb.append("':\n");
            for (int i = 0; i < queueStack.size(); i++) {
                sb.append(queueStack.get(i));
                sb.append("->");
            }
            sb.append(checkQueue);
            throw new ConfigurationException(sb.toString());
        }
        QueueConfig queueConfig = configLookup.get(checkQueue);
        if (queueConfig == null) {
            throw new ConfigurationException("Queue '" + checkQueue + "' does not exist, but is referenced as a target");
        }
        queueStack.add(checkQueue);
        switch(queueConfig.handler) {
            case alias:
                checkRecursion(queueStack, queueConfig.aliasTarget);
                break;
            case multiplex:
                for (DestinationConfig destination : queueConfig.destinations) {
                    ArrayList<String> queueStack2 = (ArrayList<String>) queueStack.clone();
                    checkRecursion(queueStack2, destination.name);
                }
                break;
            case router:
                for (DestinationConfig destination : queueConfig.destinations) {
                    if (destination.source.equals(queueStack.get(0))) {
                        checkRecursion(queueStack, destination.name);
                        return;
                    }
                }
                if (queueConfig.defaultRoute != null && queueConfig.defaultRoute.length() > 0) {
                    checkRecursion(queueStack, queueConfig.defaultRoute);
                }
                break;
        }
    }
}
