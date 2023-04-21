package javax.enterprise.deploy.spi;

/**
 * A TargetModuleID interface represents a unique identifier for a deployed
 * application module. A deployable application module can be an EAR, JAR, WAR
 * or RAR file.
 * 
 * A TargetModuleID can represent a root module or a child module. A root module
 * TargetModuleID has no parent. It represents a deployed EAR file or stand
 * alone module. A child module TargetModuleID represents a deployed sub module
 * of a J2EE application.
 * 
 * A child TargetModuleID has only one parent, the super module it was bundled
 * and deployed with.
 * 
 * The identifier consists of the target name and the unique identifier for the
 * deployed application module.
 */
public interface TargetModuleID {

    /**
	 * Retrieve the name of the target server. this module was deployed to.
	 * 
	 * @return Target an object representing a server target.
	 */
    public Target getTarget();

    /**
	 * Retrieve the id assigned to represent the deployed module.
	 */
    public String getModuleID();

    /**
	 * If this TargetModulID represents a web module retrieve the URL for it.
	 * 
	 * @return the URL of a web module or null if the module is not a web
	 *         module.
	 */
    public String getWebURL();

    /**
	 * Retrieve the identifier representing the deployed module.
	 */
    public String toString();

    /**
	 * Retrieve the identifier of the parent object of this deployed module. If
	 * there is no parent then this is the root object deployed. The root could
	 * represent an EAR file or it could be a stand alone module that was
	 * deployed.
	 * 
	 * @return the TargetModuleID of the parent of this object. A
	 *         <code>null</code> value means this module is the root object
	 *         deployed.
	 */
    public TargetModuleID getParentTargetModuleID();

    /**
	 * Retrieve a list of identifiers of the children of this deployed module.
	 * 
	 * @return a list of TargetModuleIDs identifying the childern of this
	 *         object. A <code>null</code> value means this module has no
	 *         childern
	 */
    public TargetModuleID[] getChildTargetModuleID();
}
