package edu.caltech.sbw;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides various general-purpose methods for such things as
 * connecting/disconnecting from SBW, obtaining module instances, and searching
 * for services. All of the methods on this class are static.
 * 
 * @author Andrew Finney, Michael Hucka
 * @author $Author: fbergmann $
 * @version $Revision: 1.3 $
 */
public class SBW {

    /**
	 * This does nothing; it is here to prevent clients from trying to create an
	 * instance of this class.
	 */
    private SBW() {
    }

    /**
	 * Connect to SBW as a client, indicating that this application does not
	 * provide any services. The connection is anonymous. Other modules querying
	 * the Broker for what is connected to SBW will be informed that a module is
	 * connected, but without a name or other information.
	 * <p>
	 * A client must connect to SBW before performing other operations such as
	 * querying the system for services.
	 * <p>
	 * 
	 * @throws SBWCommunicationException
	 *             if a communication error occurs or this client is already
	 *             connected to SBW
	 */
    public static void connect() throws SBWCommunicationException, SBWBrokerStartException {
        if (Sys.OSIsMac() || Sys.OSIsUnix()) {
            System.setProperty("java.net.preferIPv4Stack", "true");
            System.setProperty("sbw.broker.allow-remote-modules", "true");
        }
        if (rpc.isConnected()) {
            throw new SBWCommunicationException("Already connected to SBW", "A call to connect to the SBW Broker has been attempted," + " but this client is already connected.");
        } else {
            rpc.connect("", "localhost");
        }
        broker = null;
        getBrokerService();
    }

    /**
	 * <b>DEPRECATED; the introduction of proper networked distributed operation
	 * in SBW version 1.0 makes this method obsolete. Programs should always
	 * connect to their local Broker using {@link #connect()} and connect
	 * multiple Brokers together using {@link #link(String hostNameOrAddess)}.</b>
	 * 
	 * Connects (as a client) to the SBW Broker running on the host computer
	 * identified by the host name or IP address in
	 * <code>hostNameOrAddress</code>. The connection is anonymous and the
	 * Broker is informed that this application does not provide any services.
	 * Other modules querying the Broker for what is connected to SBW will be
	 * informed that a module is connected, but without a name or other
	 * information.
	 * <p>
	 * This is a variation on the plain {@link #connect()} method described
	 * elsewhere.
	 * <p>
	 * Note that a client can only be connected to one SBW Broker at a time.
	 * Attempting to connect multiple times will result in an exception.
	 * <p>
	 * 
	 * @param hostNameOrAddress
	 *            the host name or IP address to connect to
	 * @throws SBWCommunicationException
	 *             if a communication error occurs or this client is already
	 *             connected to SBW
	 */
    public static void connect(String hostNameOrAddress) throws SBWCommunicationException, SBWBrokerStartException {
        if (rpc.isConnected()) {
            throw new SBWCommunicationException("Already connected to SBW", "A call to connect to the SBW Broker has been attempted," + " but this client is already connected.");
        } else {
            rpc.connect("", hostNameOrAddress);
        }
        broker = null;
        getBrokerService();
    }

    /**
	 * Tells the local SBW Broker to connect to another SBW Broker running on a
	 * remote computer, starting the remote Broker first if necessary. The
	 * remote host is identified by the host name or IP address in
	 * <code>hostNameOrAddress</code>.
	 * <p>
	 * 
	 * @param hostNameOrAddress
	 *            the host name or IP address to connect to
	 * @throws SBWCommunicationException
	 *             if a communication error occurs or this client is already
	 *             connected to SBW
	 * @throws SBWBrokerStartException
	 *             if the remote Broker cannot be started up
	 */
    public static void link(String hostNameOrAddress) throws SBWBrokerStartException, SBWCommunicationException {
        checkConnected();
        try {
            broker.linkBroker(hostNameOrAddress);
        } catch (SBWBrokerStartException e) {
            throw e;
        } catch (SBWCommunicationException e) {
            throw e;
        } catch (SBWException e) {
            String msg = "Problem linking to Broker on remote host '" + hostNameOrAddress + "'";
            SBWLog.trace(msg, e);
            throw new SBWCommunicationException(e.getMessage(), e.getDetailedMessage());
        }
    }

    /**
	 * Notifies SBW that this application will no longer be providing or
	 * consuming services. Invoking this method will first send a notification
	 * message to the Broker, then close the network socket and terminate the
	 * thread handling message processing. If any errors are encountered while
	 * notifying the Broker or closing the socket, no exceptions are raised and
	 * all of the operations are still performed.
	 */
    public static void disconnect() {
        try {
            rpc.disconnect();
        } catch (Exception e) {
            SBWLog.trace("Unable to disconnect properly", e);
        }
    }

    /**
	 * Returns an instance of the given module. If the module instance has
	 * management type {@link ModuleImpl#UNIQUE}, then a {@link Module} object
	 * corresponding to an existing module instance is returned. Otherwise, a
	 * new module instance is launched by the SBW Broker. A new instance is
	 * always launched if no existing instance exists.
	 * <p>
	 * Various unusual conditions may lead to exceptions. These include: null
	 * module names, inability to contact the broker, inability to start the
	 * requested module, etc.
	 * <p>
	 * 
	 * @param moduleName
	 *            the unique name of the module
	 * @return a <code>Module</code> object representing the module instance
	 * @throws SBWModuleNotFoundException
	 *             if the module named <code>moduleName</code> is null or the
	 *             module is unknown to the Broker
	 * @throws SBWModuleStartException
	 *             if an error occurs while attempting to start the requested
	 *             module
	 * @throws SBWCommunicationException
	 *             if a communication error occurs
	 */
    public static Module getModuleInstance(String moduleName) throws SBWCommunicationException, SBWModuleStartException, SBWModuleNotFoundException {
        if (StringUtil.empty(moduleName)) {
            throw new SBWModuleNotFoundException("Null module name", "'Null' is not a valid module name.");
        }
        checkConnected();
        moduleName = moduleName.trim();
        try {
            SBWLog.trace("Asking for an instance of module " + moduleName);
            return new Module(broker.getModuleInstance(moduleName));
        } catch (SBWModuleNotFoundException e) {
            String msg = "Module '" + moduleName + "' is not known to SBW";
            SBWLog.trace(msg, e);
            throw e;
        } catch (SBWModuleStartException e) {
            String msg = "Unable to start module '" + moduleName + "'";
            SBWLog.trace(msg, e);
            throw e;
        } catch (SBWException e) {
            String msg = "Problem getting module instance";
            SBWLog.trace(msg, e);
            throw new SBWModuleStartException(msg, e.getDetailedMessage());
        }
    }

    /**
	 * Returns an array of {@link Module} objects corresponding to every module
	 * connected to the system at this time.
	 * <p>
	 * 
	 * @return an array of {@link Module} object instances
	 * @throws SBWCommunicationException
	 *             if there is a problem contacting the SBW Broker
	 */
    public static Module[] getExistingModuleInstances() throws SBWCommunicationException {
        checkConnected();
        try {
            int[] ids = broker.getExistingModuleInstanceIds();
            Module[] modules = new Module[ids.length];
            for (int i = 0; i < ids.length; i++) {
                modules[i] = new Module(ids[i]);
            }
            return modules;
        } catch (SBWException e) {
            String msg = "Problem getting module list from Broker";
            SBWLog.trace(msg, e);
            throw new SBWCommunicationException(e.getMessage(), e.getDetailedMessage());
        }
    }

    /**
	 * Returns an array of {@link Module} objects corresponding to every running
	 * module that has the given module unique name. (Multiple instances of a
	 * module may be running if the module does not have management type
	 * {@link ModuleImpl#UNIQUE} and multiple client modules have requested
	 * instances.)
	 * <p>
	 * 
	 * @param name
	 *            the name of the module
	 * @return an array of {@link Module} object instances
	 * @throws SBWCommunicationException
	 *             if there is a problem contacting the SBW Broker
	 */
    public static Module[] getExistingModuleInstances(String name) throws SBWCommunicationException {
        checkConnected();
        name = name.trim();
        try {
            int[] ids = broker.getExistingModuleInstanceIds();
            ArrayList running = new ArrayList(ids.length);
            for (int i = 0; i < ids.length; i++) {
                try {
                    List desc = broker.getModuleDescriptor(ids[i]);
                    String thisModuleName = (String) desc.get(0);
                    if (thisModuleName.equals(name)) {
                        running.add(new Module(ids[i]));
                    }
                } catch (SBWModuleNotFoundException e) {
                }
            }
            return (Module[]) running.toArray(new Module[0]);
        } catch (SBWException e) {
            String msg = "Problem getting module list from Broker";
            SBWLog.trace(msg, e);
            throw new SBWCommunicationException(e.getMessage(), e.getDetailedMessage());
        }
    }

    /**
	 * Returns an array of {@link ModuleDescriptor} objects corresponding to all
	 * modules known to the local SBW Broker. If the boolean flag
	 * <code>includeRunning</code> is <code>false</code>, only registered
	 * modules will be included (since those are the only kinds of modules for
	 * which the Broker will have registration information). If the flag is
	 * <code>true</code>, this method additionally includes running module
	 * instances. The difference in behavior is relevant when there are
	 * unregistered modules connected to SBW: when <code>includeRunning</code>
	 * is <code>false</code>, unregistered modules are not included in the
	 * array of module descriptors returned.
	 * <p>
	 * 
	 * @param includeRunning
	 *            whether to include modules that may be running unregistered
	 * @return an array of {@link ModuleDescriptor} object instances
	 * @throws SBWCommunicationException
	 *             if there is a problem contacting the SBW Broker
	 */
    public static ModuleDescriptor[] getModuleDescriptors(boolean includeRunning) throws SBWCommunicationException {
        return getModuleDescriptors(includeRunning, true);
    }

    /**
	 * Returns an array of {@link ModuleDescriptor} objects corresponding to all
	 * modules known to SBW. If the boolean flag <code>includeRunning</code>
	 * is <code>false</code>, only registered modules will be included (since
	 * those are the only kinds of modules for which the Broker will have
	 * registration information). If the flag is <code>true</code>, this
	 * method additionally includes running module instances. The difference in
	 * behavior is relevant when there are unregistered modules connected to
	 * SBW: when <code>includeRunning</code> is <code>false</code>,
	 * unregistered modules are not included in the array of module descriptors
	 * returned.
	 * <p>
	 * The second flag <code>localOnly</code> specifies whether to include
	 * only local modules or all modules in a network of interacting SBW
	 * Brokers. If the flag is <code>true</code>, the module descriptors
	 * returned refer only to the local broker; if the flag is
	 * <code>false</code>, information from all connected brokers is included
	 * in the module descriptor list.
	 * <p>
	 * 
	 * @param includeRunning
	 *            whether to include modules that may be running unregistered
	 * @param localOnly
	 *            whether to include only modules on the local broker or all
	 *            known brokers.
	 * @return an array of {@link ModuleDescriptor} object instances
	 * @throws SBWCommunicationException
	 *             if there is a problem contacting the SBW Broker
	 */
    public static ModuleDescriptor[] getModuleDescriptors(boolean includeRunning, boolean localOnly) throws SBWCommunicationException {
        checkConnected();
        try {
            List[] records = broker.getModuleDescriptors(localOnly, includeRunning);
            ModuleDescriptor[] desc = new ModuleDescriptor[records.length];
            for (int i = 0; i < records.length; i++) {
                desc[i] = new ModuleDescriptor(records[i]);
            }
            return desc;
        } catch (SBWException e) {
            String msg = "Problem getting module descriptors from Broker";
            SBWLog.trace(msg, e);
            throw new SBWCommunicationException(e.getMessage(), e.getDetailedMessage());
        }
    }

    /**
	 * Returns a {@link ModuleDescriptor} object corresponding to the module
	 * whose unique name is given as the first argument.
	 * <p>
	 * 
	 * @param moduleName
	 *            the module name
	 * @return an array of {@link ModuleDescriptor} object instances
	 * @throws SBWCommunicationException
	 *             if there is a problem contacting the SBW Broker
	 */
    public static ModuleDescriptor getModuleDescriptor(String moduleName) throws SBWModuleNotFoundException, SBWBrokerStartException, SBWCommunicationException {
        checkConnected();
        try {
            List record = broker.getModuleDescriptor(moduleName, false);
            if (record != null) {
                return new ModuleDescriptor(record);
            }
        } catch (SBWModuleNotFoundException e) {
            throw e;
        } catch (SBWBrokerStartException e) {
            throw e;
        } catch (SBWException e) {
            String msg = "Problem getting module descriptors from Broker";
            SBWLog.trace(msg, e);
            throw new SBWCommunicationException(e.getMessage(), e.getDetailedMessage());
        }
        throw new SBWModuleNotFoundException("Cannot find module named '" + moduleName + "'", "The SBW Broker does not have any information about a module" + " named '" + moduleName + "'");
    }

    /**
	 * Returns an array of {@link ServiceDescriptor} objects corresponding to
	 * all of the services registered with SBW in the given
	 * <code>category</code>. The boolean flag <code>recursive</code>
	 * indicates whether the search should be performed recursively through the
	 * service hierarchy. If <code>true</code>, the string
	 * <code>category</code> is matched against all categories and
	 * subcategories. An example use of this method might be to use the category
	 * "Analysis" to query for all the services which provide analyses of SBML
	 * models.
	 * <p>
	 * 
	 * @param category
	 *            the category of services to search for
	 * @param recursive
	 *            whether to search recursively through the service hierarchy
	 *            starting from the point indicated by <code>category</code>
	 * @throws SBWCommunicationException
	 *             if there is a problem contacting the SBW Broker
	 * @throws SBWIncorrectCategorySyntaxException
	 *             if the string <code>category</code> has incorrect syntax
	 */
    public static ServiceDescriptor[] findServices(String category, boolean recursive) throws SBWCommunicationException, SBWIncorrectCategorySyntaxException {
        checkConnected();
        category = category.trim();
        try {
            List[] records = broker.findServices(category, recursive);
            ServiceDescriptor[] descriptors = new ServiceDescriptor[records.length];
            ModuleDescriptor[] modules = getModuleDescriptors(true, true);
            for (int i = 0; i < records.length; i++) {
                String moduleName = (String) records[i].get(0);
                ModuleDescriptor md = null;
                for (int j = 0; j < modules.length; j++) {
                    if (modules[j].getName().equals(moduleName)) {
                        md = modules[j];
                    }
                }
                descriptors[i] = new ServiceDescriptor((String) records[i].get(1), (String) records[i].get(2), (String) records[i].get(3), md, (String) records[i].get(4));
            }
            return descriptors;
        } catch (SBWException e) {
            String msg = "Problem finding services for category " + category;
            SBWLog.trace(msg, e);
            throw new SBWCommunicationException(e.getMessage(), e.getDetailedMessage());
        }
    }

    /**
	 * Returns an array of strings listing all of the immediate subcategories of
	 * the given parameter <code>parentCategory</code>. An empty string as
	 * the value of <code>parentCategory</code> stands for the root of the
	 * hierarchy tree; thus, <code>getServiceCategories("")</code> returns an
	 * array of the top-level categories known to SBW.
	 * <p>
	 * 
	 * @param parentCategory
	 *            the category for which to search for subcategories
	 * @return an array of strings listing the known subcategories of the given
	 *         argument
	 * @throws SBWCommunicationException
	 *             if an error occurs trying to contact the SBW Broker
	 * @throws SBWIncorrectCategorySyntaxException
	 *             if there is an error in the syntax of the given category
	 *             pathname <code>parentCategory</code>
	 */
    public static String[] getServiceCategories(String parentCategory) throws SBWCommunicationException, SBWIncorrectCategorySyntaxException {
        checkConnected();
        parentCategory = parentCategory.trim();
        try {
            return broker.getServiceCategories(parentCategory);
        } catch (SBWException e) {
            String msg = "Problem getting service categories from Broker";
            SBWLog.trace(msg, e);
            throw new SBWCommunicationException(e.getMessage(), e.getDetailedMessage());
        }
    }

    /**
	 * returns the version of this client library
	 */
    public static String getVersion() {
        return Config.getAPIVersion();
    }

    /**
	 * returns the version of the broker
	 */
    public static String getBrokerVersion() throws SBWCommunicationException {
        try {
            return broker.getVersion();
        } catch (SBWException e) {
            String msg = "Problem getting version from Broker";
            SBWLog.trace(msg, e);
            throw new SBWCommunicationException(e.getMessage(), e.getDetailedMessage());
        }
    }

    /**
	 * Returns the path to the home directory of the SBW installation.
	 * <p>
	 * 
	 * @return a {@link java.io.File} object corresponding to the SBW home
	 *         directory
	 */
    public static File getSBWHome() {
        return Config.getSBWHome();
    }

    /**
	 * Adds the given listener object to the list of listeners that SBW will
	 * call when certain events occur. ("SBW Listeners" are used in SBW to
	 * notify an application of certain events such as module startups and
	 * shutdowns.)
	 * <p>
	 * The order in which listeners are called is unspecified.
	 * <p>
	 * 
	 * @see SBWListener
	 * @param listener
	 *            the {@link SBWListener} object to add to the list of listeners
	 *            for special events
	 */
    public static void addListener(SBWListener listener) {
        rpc.addListener(listener);
    }

    /**
	 * Removes the given listener object from the list of listeners that SBW
	 * calls when certain events occur. ("SBW Listeners" are used in SBW to
	 * notify an application of certain events such as module startups and
	 * shutdowns.)
	 * <p>
	 * 
	 * @see SBWListener
	 * @param listener
	 *            the {@link SBWListener} object to add to the list of listeners
	 *            for special events
	 */
    public static void removeListener(SBWListener listener) {
        rpc.removeListener(listener);
    }

    /**
	 * Returns a {@link Module} object corresponding to the current module
	 * instance.
	 * <p>
	 * 
	 * @return a {@link Module} object
	 */
    public static Module getThisModule() {
        return new Module(rpc.getModuleId());
    }

    /**
	 * Returns an object implementing the {@link SBWLowLevel} interface for the
	 * low-level SBW API.
	 * <p>
	 * 
	 * @return an {@link SBWLowLevel} object
	 */
    public static SBWLowLevel getLowLevelAPI() {
        return (SBWLowLevel) rpc;
    }

    /**
	 * Create and return a proxy object implementing the
	 * <code>BrokerInterface</code> interface for the SBW Broker. This is used
	 * internally for implementing various SBW capabilities.
	 * <p>
	 * 
	 * @return an object implementing BrokerInterface
	 * @throws SBWCommunicationException
	 *             if a problem occurs during communications with the Broker
	 */
    static final BrokerInterface getBrokerService() throws SBWCommunicationException {
        if (broker != null) {
            return broker;
        }
        SBWLog.trace("Obtaining instance of Broker interface.");
        Module m = new Module(SBWLowLevel.BROKER_MODULE);
        Service s = m.findServiceByName(SBWLowLevel.BROKER_SERVICE_NAME);
        try {
            broker = (BrokerInterface) s.getServiceObject(BrokerInterface.class);
            return broker;
        } catch (SBWServiceNotFoundException e) {
            SBWLog.error("Unable to find \"BROKER\" service on Broker", e);
            return null;
        } catch (SBWMethodNotFoundException e) {
            SBWLog.error("Internal Broker interface definition mismatch", e);
            return null;
        } catch (SBWUnsupportedObjectTypeException e) {
            SBWLog.error("Unexpected exception", e);
            return null;
        }
    }

    /**
	 * Returns an array of strings listing the names of all services implemented
	 * by the module whose identifier is given by the argument
	 * <code>moduleId</code>.
	 * <p>
	 * 
	 * @param moduleId
	 *            the module whose services should be listed
	 * @return an array of strings naming the services implemented by the module
	 * @throws SBWCommunicationException
	 *             if a problem occurs during communications with the Broker or
	 *             the given module
	 */
    static String[] getServiceNamesFromModule(int moduleId) throws SBWCommunicationException {
        SBWLog.trace("Querying module " + moduleId + " about its services");
        DataBlockWriter args = new DataBlockWriter();
        try {
            DataBlockReader result = rpc.call(moduleId, SBWLowLevel.SYSTEM_SERVICE, SBWLowLevel.GET_SERVICES_METHOD, args);
            String[] names = result.get1DStringArray();
            for (int i = 0; i < names.length; i++) {
                names[i] = names[i].trim();
            }
            return names;
        } catch (SBWException e) {
            String msg = "Unable to get services list from module " + moduleId;
            SBWLog.trace(msg);
            throw new SBWCommunicationException(e.getMessage(), e.getDetailedMessage());
        } finally {
            args.release();
        }
    }

    /**
	 * Returns an array of strings listing the method signatures of the methods
	 * on the service identified by <code>serviceId</code> on the module
	 * identified by <code>moduleId</code>.
	 * <p>
	 * 
	 * @param moduleId
	 *            the identifier of the module to query
	 * @param serviceId
	 *            the identifier of the service whose methods we are interested
	 *            in
	 * @return an array of strings giving the method signatures of the methods
	 *         on the service
	 * @throws SBWCommunicationException
	 *             if a problem occurs during communications with the Broker or
	 *             the given module
	 */
    static String[] getSignatureStringsFromModule(int moduleId, int serviceId) throws SBWCommunicationException {
        SBWLog.trace("Querying module " + moduleId + " about methods for service " + serviceId);
        DataBlockWriter args = new DataBlockWriter();
        try {
            args.add(serviceId);
            DataBlockReader result = rpc.call(moduleId, SBWLowLevel.SYSTEM_SERVICE, SBWLowLevel.GET_METHODS_METHOD, args);
            String[] signatureStrings = result.get1DStringArray();
            for (int i = 0; i < signatureStrings.length; i++) {
                signatureStrings[i] = signatureStrings[i].trim();
            }
            return signatureStrings;
        } catch (SBWException e) {
            String msg = "Unable to get methods list from module " + moduleId + " for service id " + serviceId;
            SBWLog.trace(msg, e);
            throw new SBWCommunicationException(msg, e.getDetailedMessage());
        } finally {
            args.release();
        }
    }

    /**
	 * Cleans up a category name by: (1) trimming leading and trailing spaces,
	 * (2) converting the empty string to "/", (3) removing trailing '/', and
	 * (4) removing spaces around '/' separators. E.g.,
	 * 
	 * <pre>
	 *    / foo / bar     -&gt; /foo/bar
	 *    foo bar / baz   -&gt; foo bar/baz
	 * </pre>
	 * 
	 * foo bar / baz / -> foo bar/baz
	 * 
	 * </pre>
	 * 
	 * <p>
	 * 
	 * @param category
	 *            the category path name string
	 * @return the cleaned-up version of the category
	 */
    static String normalizeCategoryName(String category) {
        if (StringUtil.empty(category)) {
            return "/";
        }
        category = category.trim();
        if (category.equals("")) {
            return "/";
        } else {
            try {
                String newCategoryStr = "";
                int lastPos = 0;
                int currPos = 0;
                if (category.indexOf('/') == 0) {
                    lastPos = 1;
                }
                while ((currPos = category.indexOf('/', lastPos)) >= 0) {
                    String piece = category.substring(lastPos, currPos).trim();
                    newCategoryStr = newCategoryStr + "/" + piece;
                    lastPos = currPos + 1;
                }
                currPos = category.length();
                newCategoryStr = newCategoryStr + "/" + category.substring(lastPos, currPos).trim();
                return newCategoryStr;
            } catch (Exception e) {
                return category;
            }
        }
    }

    /**
	 * Internal method used to return the {@link SBWLowLevelInternal} interface
	 * object for this client.
	 * <p>
	 * 
	 * @return an {SBWLowLevelInternal} object
	 */
    static SBWLowLevelInternal getInternalAPI() {
        return rpc;
    }

    /**
	 * Internal method used to set the {@link SBWLowLevelInternal} interface
	 * object for this client.
	 * <p>
	 * 
	 * @param r
	 *            the {SBWLowLevelInternal} object to use
	 */
    static void setInternalAPI(SBWLowLevelInternal r) {
        rpc = r;
    }

    /**
	 * Check if we are connected to the Broker. If not, thrown an exception. If
	 * we <i>are</i> connected, get a copy of the Broker service interface if
	 * necessary.
	 * <p>
	 * 
	 * @throws SBWCommunicationException
	 *             if this client is not connected to the SBW Broker or there is
	 *             a communications problem attempting to connect to it.
	 */
    private static final void checkConnected() throws SBWCommunicationException {
        if (!rpc.isConnected()) {
            throw new SBWCommunicationException("Not connected to SBW Broker", "This module is not currently connected to the SBW Broker." + " Either SBW.connect() has not been invoked or a" + " communications problem occurred.");
        }
        if (broker == null) {
            getBrokerService();
        }
    }

    /**
	 * Pointer to low-level rpc interface object, either
	 * <code>SBWModuleRPC</code> in the case of a module, or
	 * <code>SBWBrokerRPC</code> in the case of the SBW Broker.
	 * <p>
	 * Currently this is set by making the default be <code>SBWModuleRPC</code>
	 * and using the {@link #setLowLevelAPI(SBWLowLevel rpc)} method in the
	 * Broker to reset the value of this variable. This is not a clean solution
	 * and should be improved.
	 */
    private static SBWLowLevelInternal rpc = new SBWModuleRPC();

    /** Broker interface object. * */
    private static BrokerInterface broker;

    static {
        Config.recordClassVersion(SBW.class, "$Id: SBW.java,v 1.3 2007/07/24 23:08:19 fbergmann Exp $");
    }
}
