package jimo.osgi.modules.core;

public interface CoreConstants {

    String BUNDLE_INSTALLFILE = "bundles.inst";

    String INSTANCE_LOCK = "instance.lock";

    String KEY_STARTUP = "jimo.core.startup";

    String KEY_SHUTDOWN = "jimo.core.shutdown";

    String KEY_CREATEINSTALL = "jimo.core.createinstall";

    String KEY_READINSTALL = "jimo.core.readinstall";

    String KEY_UNRESOLVEDIMPORTS = "jimo.core.unresolvedimports";

    String KEY_STARTUPERROR = "jimo.core.startup.error";

    String KEY_SHUTDOWNBUNDLE = "jimo.core.shutdownbundle";

    String KEY_SHUTDOWNBUNDLEERROR = "jimo.core.shutdownbundle.error";

    String KEY_BUNDLEALREADYREGISTERED = "jimo.core.bundleAlreadyRegisteredError";

    String KEY_ERRINSTANCE = "jimo.core.instanceerror";

    String KEY_POSTEVENT = "jimo.core.postEvent";

    String KEY_SENDEVENT = "jimo.core.sendEvent";

    String KEY_HANDLEEVENT = "jimo.core.handleEvent";

    String KEY_ONCOMMAND = "jimo.core.onCommand";

    String KEY_COMMANDNOTFOUND = "jimo.core.commandNotFound";

    String KEY_BUNDLEINFO = "jimo.core.bundleInfo";

    String KEY_BUNDLEREGSERVICES = "jimo.core.bundleInfo.registeredServices";

    String KEY_BUNDLESERVICESINUSE = "jimo.core.bundleInfo.servicesInUse";

    String KEY_CORECONFIGINFO = "jimo.core.configInfo";

    String KEY_APPCLASSERROR = "jimo.core.appClassError";

    String KEY_COREHELP = "jimo.core.commandHelp";

    String KEY_CORESERVICEINFO = "jimo.core.serviceInfo";

    String KEY_BUNDLENOTSTARTEDERROR = "jimo.core.bundleNotStarted";

    String KEY_DUPLICATEAPPID = "jimo.core.duplicateAppId";

    String KEY_INSTALLEDBUNDLE = "jimo.core.installedBundle";

    String KEY_NOSUCHBUNDLE = "jimo.core.noSuchBundle";

    String KEY_STOPPEDBUNDLE = "jimo.core.stoppedBundle";

    String KEY_STARTEDBUNDLE = "jimo.core.startedBundle";

    String KEY_UNINSTALLEDBUNDLE = "jimo.core.uninstalledBundle";

    String KEY_UPDATEDBUNDLE = "jimo.core.updatedBundle";

    String KEY_LOADINGRC = "jimo.core.loadingRC";

    String KEY_STARTINGAPPLICATION = "jimo.core.startingApp";

    String KEY_APPTHREADSTARTED = "jimo.core.appThreadStarted";

    String KEY_APPTHREADSTOPPING = "jimo.core.appThreadStopping";

    String KEY_CONFIGURATIONSTORE = "jimo.core.configurationStore";

    String KEY_BUNDLEREGSTORE = "jimo.core.bundleStore";

    String KEY_COMMANDHELP = "jimo.core.helpText";

    String KEY_PARTYLINE = "jimo.core.peer.partyLine";

    /**
	 * Events are sent to this topic whenever a command is received.
	 * The topic the commands are sent to will be this constant, plus
	 * a frontslash, plus the command without the arguments (ie. the first
	 * word of the input line).
	 */
    String TOPIC_COMMAND = "jimo/command/event";

    /**
	 * Events sent to this topic will be evaluated by the command interpreter.
	 */
    String TOPIC_COMMANDEXEC = "jimo/command/execute";

    /**
	 * Command events have this property set to the command string.
	 */
    String EVENT_COMMAND = "jimo.command";

    /**
	 * Command events have this property set to the command arguments.
	 */
    String EVENT_COMMANDPARAMETERS = "jimo.command.parameters";

    /**
	 * This string is the COMMANDNAME for CoreCommandHandler.
	 * If the framework receives this command as a string it will invoke
	 * the CoreCommandHandler.
	 */
    String CORE_COMMANDNAME = "core";

    /**
	 * Disconnect command
	 */
    String DISCONNECT_COMMANDNAME = "disconnect";

    String HELP_COMMANDNAME = "help";

    String LIST_COMMANDNAME = "list";

    /**
	 * Start a bundle - <code>core start <i>id</i></code>
	 */
    String CORECOMMAND_STARTBUNDLE = "start";

    /**
	 * Send an event:<br/>
	 * <code>core event [(<i>property</i>=<i>value</i>) (<i>property</i>=<i>value</i>,<i>value</i>,...) (...)]</code>
	 * <br/>
	 * (The square braces '[', ']' are literal characters).
	 */
    String CORECOMMAND_EVENT = "event";

    /**
	 * Stop the framework
	 */
    String CORECOMMAND_STOP = "close";

    /**
	 * Restart the framework
	 */
    String CORECOMMAND_RESTART = "restart";

    /**
	 * Rebuild the framework
	 */
    String CORECOMMAND_REBUILD = "rebuild";

    /**
	 * Print info
	 */
    String CORECOMMAND_INFO = "info";

    /**
	 * Refresh the bundle list
	 */
    String CORECOMMAND_REFRESH = "refresh";

    /**
	 * Install a bundle - <code>core install <i>url ...</i></code>
	 */
    String CORECOMMAND_INSTALL = "install";

    /**
	 * Uninstall a bundle - <code>core uninstall <i>id ...</i></code>
	 */
    String CORECOMMAND_UNINSTALL = "uninstall";

    /**
	 * Stop a bundle or bundles - <code>core stop <i>id ..</i></code>
	 */
    String CORECOMMAND_STOPBUNDLE = "stop";

    /**
	 * Update a bundle or bundles - <code>core update <i>id</i></code>
	 */
    String CORECOMMAND_UPDATEBUNDLE = "update";

    /**
	 * Process commands from a url - <code>core rc <i>url</i></code>
	 */
    String CORECOMMAND_RC = "rc";

    String CORECOMMAND_HELP = "help";

    String CORECOMMAND_SERVICES = "services";

    String CORECOMMAND_SERVICE = "service";

    String CORECOMMAND_PROPGET = "propget";

    String CORECOMMAND_PROPSET = "propset";

    String CORECOMMAND_PANIC = "panic";

    String CORECOMMAND_STATUS = "status";

    String CORECOMMAND_IMPORT = "importcfg";

    String CORECOMMAND_FACTORYCONFIGURATION = "factoryconfig";

    String CORECOMMAND_GETCONFIGURATION = "getconfig";

    String CORECOMMAND_LISTCONFIGURATIONS = "configs";

    String CORECOMMAND_DELETECONFIGURATION = "delconfig";

    String SERVICECOMMAND_REGISTER = "register";

    String SERVICECOMMAND_DEREGISTER = "deregister";

    String SERVICECOMMAND_GET = "get";

    String SERVICECOMMAND_UNGET = "unget";
}
