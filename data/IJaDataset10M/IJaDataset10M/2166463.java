package com.controltier.ctl.common;

import com.controltier.ctl.CtlException;
import com.controltier.ctl.Constants;
import com.controltier.ctl.authentication.AuthenticationMgrFactory;
import com.controltier.ctl.authentication.Authenticator;
import com.controltier.ctl.authentication.INodeAuthResolutionStrategy;
import com.controltier.ctl.authentication.NodeAuthResolutionStrategyFactory;
import com.controltier.ctl.authorization.Authorization;
import com.controltier.ctl.authorization.AuthorizationMgrFactory;
import com.controltier.ctl.utils.IPropertyLookup;
import com.controltier.ctl.utils.PropertyLookup;
import org.apache.log4j.Logger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.input.InputHandler;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Manages the elements of the Ctl framework. Provides access to the various
 * kinds of framework resource managers like
 * {@link DepotResourceMgr}, {@link ModuleMgr}, {@link Authenticator}, {@link Authorization}.
 * <p/>
 * User: alexh
 * Date: Jun 4, 2004
 * Time: 8:16:42 PM
 */
public class Framework extends FrameworkResourceParent {

    public static final Logger logger = Logger.getLogger(Framework.class);

    static final String NODEAUTH_CLS_PROP = "framework.nodeauthentication.classname";

    static final String AUTHENT_CLS_PROP = "framework.authentication.class";

    static final String AUTHORIZE_CLS_PROP = "framework.authorization.class";

    static final String DEPOTMGR_NAME = "depotResourceMgr";

    static final String EXTMGR_NAME = "extensionMgr";

    static final String MODMGR_NAME = "moduleMgr";

    private final IPropertyLookup lookup;

    private final File moduleBase;

    private final File depotsBase;

    private final File extensionsBase;

    private final File ctlHome;

    private final File antHome;

    private DepotResourceMgr depotResourceMgr;

    private ExtensionMgr extensionMgr;

    private boolean allowUserInput = true;

    private static final String FRAMEWORK_USERINPUT_DISABLED = "framework.userinput.disabled";

    /**
     * This is the root. Does not return a parent.
     *
     * @throws FrameworkResourceException Throws an exception if this is called
     */
    public IFrameworkResourceParent getParent() {
        throw new FrameworkResourceException("Framework has no parent resource.", this);
    }

    public boolean childCouldBeLoaded(String name) {
        return false;
    }

    public IFrameworkResource loadChild(String name) {
        return null;
    }

    public Collection listChildNames() {
        return new ArrayList();
    }

    /**
     * Initialize children, the various resource management objects
     */
    public void initialize() {
        final boolean initialize = true;
        depotResourceMgr = DepotResourceMgr.create(DEPOTMGR_NAME, depotsBase, this, moduleBase);
        extensionMgr = ExtensionMgr.create(EXTMGR_NAME, extensionsBase, this, initialize);
        if (null == authenticationMgr) {
            authenticationMgr = AuthenticationMgrFactory.create(lookup.getProperty(AUTHENT_CLS_PROP), this).getAuthenticationMgr();
        }
        if (null == authorizationMgr) {
            authorizationMgr = AuthorizationMgrFactory.create(lookup.getProperty(AUTHORIZE_CLS_PROP), this, getBaseDir()).getAuthorizationMgr();
        }
        if (null == nodeAuthResolutionStrategy) {
            final String nodeAuthClassname;
            if (lookup.hasProperty(NODEAUTH_CLS_PROP)) {
                nodeAuthClassname = lookup.getProperty(NODEAUTH_CLS_PROP);
            } else {
                nodeAuthClassname = Constants.DEFAULT_NODE_AUTHSTRATEGY_CLASSNAME;
                logger.info("Framework setting, " + NODEAUTH_CLS_PROP + ", not set. " + "Defaulted to " + nodeAuthClassname);
            }
            nodeAuthResolutionStrategy = NodeAuthResolutionStrategyFactory.create(nodeAuthClassname, this);
        }
    }

    /**
     * Reference to class instance to authorizationMgr
     */
    private Authorization authorizationMgr;

    public Authorization getAuthorizationMgr() {
        return authorizationMgr;
    }

    /**
     * Reference to class instance to authenicate
     */
    private Authenticator authenticationMgr;

    /**
     * Gets Authenticator for this framework instance
     *
     * @return returns instance of Authenticator
     */
    public Authenticator getAuthenticationMgr() {
        return authenticationMgr;
    }

    private INodeAuthResolutionStrategy nodeAuthResolutionStrategy;

    public INodeAuthResolutionStrategy getNodeAuthResolutionStrategy() {
        return nodeAuthResolutionStrategy;
    }

    /**
     * Gets DepotMgr for this framework instance
     * @return returns instance of IDepotMgr
     */
    public IDepotMgr getDepotResourceMgr() {
        return depotResourceMgr;
    }

    /**
     * Gets ModuleMgr for this framework instance
     *
     * @return  returns instance of IModuleLookup
     */
    public IModuleLookup getModuleLookup() {
        return depotResourceMgr.getModuleLookup();
    }

    /**
     * Gets ExtensionMgr for this framework instance
     *
     * @return returns instance of ExtensionMgr
     */
    public ExtensionMgr getExtensionMgr() {
        return extensionMgr;
    }

    /**
     * Standard constructor
     *
     * @param ctl_base_dir path name to the ctl_base
     * @param module_base_dir  path name to the module_base
     * @param depots_base_dir  path name to the depots base
     */
    private Framework(final String ctl_base_dir, final String module_base_dir, final String depots_base_dir) {
        this(ctl_base_dir, module_base_dir, depots_base_dir, null, null);
    }

    /**
     * Standard constructor
     *
     * @param ctl_base_dir path name to the ctl_base
     * @param module_base_dir  path name to the module_base
     * @param depots_base_dir  path name to the depots base
     */
    private Framework(final String ctl_base_dir, final String module_base_dir, final String depots_base_dir, final Authenticator authentication, final Authorization authorization) {
        super("framework", new File(null == ctl_base_dir ? Constants.getSystemCtlBase() : ctl_base_dir), null);
        if (null == getBaseDir()) {
            throw new NullPointerException("ctl_base_dir was not set in constructor and system property ctl.base was not defined");
        }
        final String moduleBaseDir = null == module_base_dir ? getBaseDir() + Constants.FILE_SEP + "modules" : module_base_dir;
        final String depotsBaseDir = null == depots_base_dir ? getBaseDir() + Constants.FILE_SEP + "depots" : depots_base_dir;
        if (null == depotsBaseDir) {
            throw new CtlException("depots base dir could not be determined.");
        }
        if (null == moduleBaseDir) {
            throw new CtlException("modules base dir could not be determined.");
        }
        logger.debug("creating new Framework instance." + "  ctl_base_dir=" + getBaseDir() + ", module_base_dir=" + moduleBaseDir + ", depots_base_dir=" + depotsBaseDir);
        if (!getBaseDir().exists()) throw new IllegalArgumentException("ctl_base directory does not exist. " + ctl_base_dir);
        moduleBase = new File(moduleBaseDir);
        if (!moduleBase.exists()) throw new IllegalArgumentException("module base directory does not exist. " + moduleBaseDir);
        depotsBase = new File(depotsBaseDir);
        if (!depotsBase.exists()) throw new IllegalArgumentException("depot base directory does not exist. " + depotsBaseDir);
        File propertyFile = new File(getConfigDir(), "framework.properties");
        PropertyLookup lookup1 = PropertyLookup.create(propertyFile);
        lookup1.expand();
        lookup = lookup1;
        if (!lookup.hasProperty(AUTHENT_CLS_PROP)) {
            throw new IllegalArgumentException("\"" + AUTHENT_CLS_PROP + "\" property not set");
        }
        if (!lookup.hasProperty(AUTHORIZE_CLS_PROP)) {
            throw new IllegalArgumentException("\"" + AUTHORIZE_CLS_PROP + "\" property not set");
        }
        if (lookup.hasProperty("ctl.home")) {
            ctlHome = new File(lookup.getProperty("ctl.home"));
        } else {
            final String ctlhome = Constants.getSystemCtlHome();
            if (null == ctlhome) throw new FrameworkResourceException("failed looking up value for ctl.home ", this);
            ctlHome = new File(ctlhome);
        }
        if (lookup.hasProperty("ant.home")) {
            antHome = new File(lookup.getProperty("ant.home"));
        } else {
            throw new IllegalArgumentException("\"ant.home\" property not set in framework.properties");
        }
        if (null != ctlHome) {
            extensionsBase = new File(ctlHome, "lib" + Constants.FILE_SEP + "extensions");
        } else if (lookup.hasProperty("framework.extensions.dir")) {
            extensionsBase = new File(lookup.getProperty("framework.extensions.dir"));
        } else {
            throw new IllegalArgumentException("\"framework.extensions.dir\" property not set");
        }
        this.authenticationMgr = authentication;
        this.authorizationMgr = authorization;
        long start = System.currentTimeMillis();
        initialize();
        long end = System.currentTimeMillis();
    }

    /**
     * Returns the singleton instance of Framework object.  If any of the
     * supplied directory paths are null, then the value from {@link Constants} is used.
     *
     * @param ctl_base_dir     path name to the ctl_base
     * @param module_base_dir      path name to the modle base
     * @param depots_base_dir path name to the depots base
     * @return a Framework instance
     */
    public static Framework getInstance(final String ctl_base_dir, final String module_base_dir, final String depots_base_dir) {
        return new Framework(ctl_base_dir, module_base_dir, depots_base_dir);
    }

    /**
     * Get the Framework instance from an Ant project.
     * @param project
     * @return
     */
    public static Framework getInstance(Project project) {
        return getInstance(project, true);
    }

    /**
     * Get the Framework instance from an Ant project.
     * @param project
     * @return
     */
    public static Framework getInstance(Project project, boolean fail) {
        Object o = project.getReference(Framework.class.getName() + ".instance");
        if (null != o && o instanceof Framework) {
            return (Framework) o;
        } else {
            if (fail) {
                throw new IllegalArgumentException("Project does not contain a reference to the Framework instance.");
            } else {
                return null;
            }
        }
    }

    /**
     * Retrieve a Framework from the project in several ways: look for embedded reference, otherwise construct with
     * 'ctl.base' property value from the project, otherwise create new Framework from system property
     * 'ctl.base' value.
     *
     * @param project ant project
     *
     * @return existing or new Framework instance
     */
    public static Framework getInstanceOrCreate(final Project project) {
        Framework fw = null;
        if (null != project) {
            fw = getInstance(project, false);
        }
        if (null != project && null == fw && null != project.getProperty("ctl.base")) {
            fw = Framework.getInstance(project.getProperty("ctl.base"), project.getProperty("modules.dir"), project.getProperty("depots.dir"));
            fw.configureFromProject(project);
            fw.configureProject(project);
        }
        if (null == fw) {
            fw = Framework.getInstance(Constants.getSystemCtlBase());
            fw.configureFromProject(project);
            fw.configureProject(project);
        }
        return fw;
    }

    /**
     * Returns an instance of Framework object.
     * Specify the ctl_base path and let the depots and modules dir be constructed from it.
     *
     * @param ctl_base_dir path name to the ctl_base
     *
     * @return a Framework instance
     */
    public static Framework getInstance(final String ctl_base_dir) {
        return getInstance(ctl_base_dir, (Authenticator) null, (Authorization) null);
    }

    /**
     * Returns an instance of Framework object. Specify the ctl_base path and let the depots and modules dir be
     * constructed from it.
     *
     * @param ctl_base_dir path name to the ctl_base
     *
     * @return a Framework instance
     */
    public static Framework getInstance(final String ctl_base_dir, final Authenticator authenticator, final Authorization authorization) {
        logger.debug("creating new Framework instance." + "  ctl_base_dir=" + ctl_base_dir);
        if (null == ctl_base_dir && null == Constants.getSystemCtlBase()) {
            throw new RuntimeException("Unable to determine ctl base directory: system property ctl.base is not set");
        }
        Framework instance = new Framework(ctl_base_dir, null, null, authenticator, authorization);
        return instance;
    }

    /**
     * Factory method to getting the singleton instance of the Framework object. Info about the
     * ctl.base, depots.base and modules.base are retrieved via {@link Constants}.
     *
     * @return returns Framework singleton instance. Creates it using info from
     * {@link Constants} data. Assumes ctl.home and ctl.base System props are set
     */
    public static Framework getInstance() {
        logger.debug("creating new Framework using info from com.controltier.ctl.Constants");
        return Framework.getInstance((String) null, (String) null, (String) null);
    }

    /**
     * Set properties of this Framework instance based on project properties
     *
     * @param project the ant project
     */
    private void configureFromProject(final Project project) {
        if ("true".equals(project.getProperty(FRAMEWORK_USERINPUT_DISABLED))) {
            setAllowUserInput(false);
        } else {
            setAllowUserInput(true);
        }
    }

    /**
     * Configure a project to embed this framework instance as a reference.
     *
     * @param project the ant project
     */
    public void configureProject(final Project project) {
        project.addReference(Framework.class.getName() + ".instance", this);
        if (!isAllowUserInput()) {
            final InputHandler h = project.getInputHandler();
            project.setInputHandler(new FailInputHandler(h));
            project.setProperty(FRAMEWORK_USERINPUT_DISABLED, "true");
        } else {
            final InputHandler h = project.getInputHandler();
            if (h instanceof FailInputHandler) {
                final InputHandler orig = ((FailInputHandler) h).getOriginal();
                project.setInputHandler(orig);
            }
            project.setProperty(FRAMEWORK_USERINPUT_DISABLED, "false");
        }
    }

    /**
     * An InputHandler implementation which simply throws an exception.  It also stores an original implementation that
     * it may have replaced.
     */
    static class FailInputHandler implements InputHandler {

        private InputHandler orig;

        public FailInputHandler(final InputHandler h) {
            this.orig = h;
        }

        public void handleInput(final org.apache.tools.ant.input.InputRequest request) throws BuildException {
            throw new CtlException("User input is not available.");
        }

        public InputHandler getOriginal() {
            return orig;
        }
    }

    /**
     * Return the property value by name
     *
     * @param name Property key
     * @return property value
     */
    public String getProperty(final String name) {
        return lookup.getProperty(name);
    }

    public boolean existsProperty(final String key) {
        return lookup.hasProperty(key);
    }

    public IPropertyLookup getPropertyLookup() {
        return lookup;
    }

    /**
     * Returns a string with useful information for debugging.
     *
     * @return Formatted string
     */
    public String toString() {
        return "Framework{" + "baseDir=" + getBaseDir() + ", modulesBaseDir=" + moduleBase + ", depotsBaseDir=" + depotsBase + "}";
    }

    /**
     * Gets the value of "framework.node.hostname" property
     *
     * @return Returns value of framework.node.hostname property
     */
    public String getFrameworkNodeHostname() {
        String hostname = getProperty("framework.node.hostname");
        if (null != hostname) {
            return hostname.trim();
        } else {
            return hostname;
        }
    }

    /**
     * Gets the value of "framework.node.name" property
     *
     * @return Returns value of framework.node.name property
     */
    public String getFrameworkNodeName() {
        String name = getProperty("framework.node.name");
        if (null != name) {
            return name.trim();
        } else {
            return name;
        }
    }

    public File getConfigDir() {
        return new File(getBaseDir(), "etc");
    }

    /**
     * Return the Nodedispatch command handler.
     * @return
     */
    public File getNodedispatchHandler() {
        return new File(ctlHome, "lib" + File.separator + "ant" + File.separator + "ctl" + File.separator + "nodedispatch.xml");
    }

    public File getBaseModuleLibDir() {
        return moduleBase;
    }

    public File getDepotsBaseDir() {
        return depotsBase;
    }

    public void setAuthorizationMgr(Authorization authorizationMgr) {
        this.authorizationMgr = authorizationMgr;
    }

    public void setAuthenticationMgr(Authenticator authenticationMgr) {
        this.authenticationMgr = authenticationMgr;
    }

    public File getCtlHome() {
        return ctlHome;
    }

    public File getAntHome() {
        return antHome;
    }

    public boolean isAllowUserInput() {
        return allowUserInput;
    }

    public void setAllowUserInput(boolean allowUserInput) {
        this.allowUserInput = allowUserInput;
    }

    /**
     * References the {@link INodeDesc} instance representing the framework node.
     */
    private INodeDesc nodedesc;

    /**
     * Gets the {@link INodeDesc} value describing the framework node
     * @return the singleton {@link INodeDesc} object for this framework instance
     */
    public INodeDesc getNodeDesc() {
        if (null == nodedesc) {
            nodedesc = createNodeDesc(getFrameworkNodeHostname());
        }
        return nodedesc;
    }

    /**
     * Creates a new {@link INodeDesc} object for the specified hostname
     * @param hostname The node hostname value
     * @return a new INodeDesc
     */
    public INodeDesc createNodeDesc(String hostname) {
        return NodeEntryImpl.create(hostname, getFrameworkNodeHostname());
    }
}
