package edu.caltech.sbw;

import java.util.Hashtable;
import java.util.List;

/**
 * A <i>module descriptor</i> describes a module. It embodies the static data
 * recorded by the SBW Broker in the module registry. The corresponding real
 * module instance may or may not be running.
 * <p>
 * It may seem confusing at first that there may be no running module instance
 * corresponding to a module descriptor returned by SBW. However, the utility of
 * this becomes clear when one considers the need to list all modules known by
 * the system. SBW provides methods, such as {@link SBW#getModuleDescriptors},
 * that return module descriptors for all modules known by the Broker. It would
 * be inefficient, and in some cases downright impractical, if every query for a
 * module descriptor resulted in a module instance being started by SBW. Thus,
 * module descriptors are decoupled from the running state of a module.
 * <p>
 * Module descriptors are useful for finding out a module's basic
 * characteristics, such as its name, without necessarily starting up an
 * instance of the module.
 * <p>
 * It is possible for a module to connect to an SBW Broker without advertising
 * the availability of any services and without registering itself with a name
 * and other attributes. In that case, the values of some attributes may be
 * empty.
 * 
 * @author Michael Hucka
 * @author $Author: fbergmann $
 * @version $Revision: 1.2 $
 */
public class ModuleDescriptor {

    /**
	 * Returns the unique identification of the module. This name typically
	 * follows a notational convention such as that outlined in in the SBW Java
	 * Programming Manual. For example, a module's unique name might be
	 * ``edu.caltech.trigonometry''.
	 * <p>
	 * 
	 * @return a string containing the module's name
	 * @see <a href="http://www.cds.caltech.edu/erato/sbw/docs/index.html">
	 *      Systems Biology Workbench Java Programming Manual</a>
	 */
    public String getName() {
        return name;
    }

    /**
	 * Returns the display name of the module. This is suitable for showing to
	 * users in a menu or list.
	 * <p>
	 * 
	 * @return a string containing the module's name
	 */
    public String getDisplayName() {
        return displayName;
    }

    /**
	 * Returns an integer indicating the management type of the module.
	 * <p>
	 * 
	 * @return a string containing the module's name
	 * @see ModuleImpl
	 * @see Module
	 */
    public int getManagementType() {
        return moduleType;
    }

    /**
	 * Returns the command line used to start up this module. Depending on how
	 * the module is implemented, the command line may have been constructed
	 * automatically by SBW or it may have been explicitly supplied by the
	 * programmer.
	 * <p>
	 * 
	 * @return a string containing the command line string for the module
	 * @see ModuleImpl
	 */
    public String getCommandLine() {
        return commandLine;
    }

    /**
	 * Returns the help string for the module. This is an optional description
	 * of the module provided by the module's author(s). It is informational
	 * only and not actually required in the definition of a module.
	 * <p>
	 * 
	 * @return the help string for the module
	 * @see ModuleImpl
	 * @see Module
	 */
    public String getHelp() {
        return helpString;
    }

    /**
	 * Returns an array of ServiceDescriptors for the service implemented by
	 * this module. The SBW Broker is queried dynamically for this information,
	 * thus this method may throw an exception if a communications problem
	 * occurs.
	 * <p>
	 * 
	 * @return the ServiceDescriptor objects for the module's services
	 * @throws SBWCommunicationException
	 *             if a problem occurs contacting the Broker for the service
	 *             information
	 * @see ModuleImpl
	 * @see Module
	 */
    public ServiceDescriptor[] getServiceDescriptors() throws SBWCommunicationException {
        try {
            BrokerInterface broker = SBW.getBrokerService();
            List[] list = broker.getServiceDescriptors(name);
            ServiceDescriptor[] descriptors = new ServiceDescriptor[list.length];
            for (int i = 0; i < list.length; i++) descriptors[i] = new ServiceDescriptor(list[i]);
            return descriptors;
        } catch (SBWModuleNotFoundException e) {
            String msg = "Module '" + name + "' no longer seems to exist";
            SBWLog.exception(msg, e);
            throw new SBWCommunicationException(msg, e.getDetailedMessage());
        } catch (SBWException e) {
            String msg = "Unable to get services descriptors for module '" + name + "'";
            SBWLog.exception(msg, e);
            throw new SBWCommunicationException(msg, e.getDetailedMessage());
        }
    }

    /**
	 * Indicates whether some other object is "equal to" this one. This method
	 * implements equality for {@link ModuleDescriptor} objects, overriding the
	 * method of the same name on the basic Java {@link Object} class. If the
	 * given object is a {@link ModuleDescriptor} object and its various fields
	 * are equal to this object's, then this method returns true, else it
	 * returns false.
	 * <p>
	 * 
	 * @param other
	 *            the object with which to compare this one.
	 * @return true if the argument is a {@link ModuleDescriptor} object that
	 *         refers to the same module as this object, false otherwise.
	 * @see java.lang.Object#equals
	 */
    public boolean equals(Object other) {
        if (this == other) return true;
        ModuleDescriptor md = (ModuleDescriptor) other;
        return (other instanceof ModuleDescriptor) && (name.equals(md.getName())) && (displayName.equals(md.getDisplayName())) && (moduleType == md.getManagementType()) && (commandLine.equals(md.getCommandLine())) && (helpString.equals(md.getHelp()));
    }

    /**
	 * Constructs a {@link ModuleDescriptor} object.
	 * <p>
	 * This has package scope because there client applications can never create
	 * module descriptors. They are informational only, created by SBW for the
	 * benefit of client applications.
	 * <p>
	 * 
	 * @param moduleName
	 *            a unique name for the module
	 * @param moduleDisplay
	 *            the module name intended for human consumption
	 * @param moduleType
	 *            how the module is managed: {@link ModuleImpl#SELF_MANAGED},
	 *            {@link ModuleImpl#SBW_MANAGED}, or {@link ModuleImpl#UNIQUE}
	 * @param moduleMainClass
	 *            the class containing the module's <code>main()</code>
	 * @param helpString
	 *            a string describing the purpose of this module and offering
	 *            some information about its intended usage
	 * @see #ModuleDescriptor(List list)
	 */
    public ModuleDescriptor(String moduleName, String moduleDisplayName, int moduleType, String commandLine, String help) {
        this.name = moduleName;
        this.displayName = moduleDisplayName;
        this.moduleType = moduleType;
        this.commandLine = commandLine;
        this.helpString = help;
    }

    /**
	 * Constructs a {@link ModuleDescriptor} object from a list of the necessary
	 * components.
	 * <p>
	 * This has package scope because there client applications can never create
	 * module descriptors. They are informational only, created by SBW for the
	 * benefit of client applications.
	 * <p>
	 * 
	 * @param list
	 *            a list of 5 items that correspond to the components of a
	 *            module descriptor
	 */
    public ModuleDescriptor(List list) {
        Integer type = (Integer) list.get(2);
        this.name = (String) list.get(0);
        this.displayName = (String) list.get(1);
        this.moduleType = type.intValue();
        this.commandLine = (String) list.get(3);
        this.helpString = (String) list.get(4);
    }

    /**
	 * Returns the hash table of services for this module. This is used
	 * internally in SBW.
	 */
    Hashtable getServices() {
        return services;
    }

    /** The module's unique name. * */
    private String name;

    /** A module name intended for human consumption. * */
    private String displayName;

    /**
	 * How the module is managed; the value is one of {@link
	 * ModuleImpl#SELF_MANAGED}, {@link ModuleImpl#SBW_MANAGED}, or {@link
	 * ModuleImpl#UNIQUE}.
	 */
    private int moduleType;

    /** The command line used to start up this module. * */
    private String commandLine;

    /** An informational string describing this module. * */
    private String helpString;

    /**
	 * Hash table that maps service names to ServiceDescriptors for this
	 * module's services. This uses Hashtable because it's synchronized.
	 */
    private Hashtable services = new Hashtable(10);

    static {
        Config.recordClassVersion(ModuleDescriptor.class, "$Id: ModuleDescriptor.java,v 1.2 2007/07/24 23:08:23 fbergmann Exp $");
    }
}
