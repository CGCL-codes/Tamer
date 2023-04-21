package net.assimilator.qa.core;

import com.sun.jini.qa.harness.QAConfig;
import com.sun.jini.qa.harness.QATest;
import com.sun.jini.qa.harness.TestException;
import net.assimilator.core.OperationalString;
import net.assimilator.core.OperationalStringManager;
import net.assimilator.monitor.DeployAdmin;
import net.assimilator.monitor.ProvisionMonitor;
import net.assimilator.opstring.OpStringLoader;
import net.jini.config.Configuration;
import net.jini.core.discovery.LookupLocator;
import net.jini.core.lookup.ServiceItem;
import net.jini.core.lookup.ServiceTemplate;
import net.jini.discovery.LookupDiscoveryManager;
import net.jini.lease.LeaseRenewalManager;
import net.jini.lookup.ServiceDiscoveryManager;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

/**
 * The base class for tests run within the Assimilator QA framework. This class:
 * <ul>
 * <li>Maintains a {@link AssimilatorManager} object that can be used to manage
 * Assimilator services.
 * <p/>
 * <li>Maintains a {@link HostTable} object representing the table of
 * hosts participating in the test.
 * <p/>
 * <li>Maintains a <code>LookupDiscoveryManager</code> and a
 * <code>ServiceDiscoveryManager</code>. These managers can be used
 * to discover services participating in the test. The class
 * automatically configures the <code>LookupDiscoveryManager</code>
 * to use groups and lookup locators defined by the <code>
 * testGroupsAndLocators</code> property.
 * <p/>
 * <li>If the <code>testRunWebsters</code> property is <code>true</code>
 * (the default), starts a Webster on each test host including the
 * master during the test setup.
 * These Websters serve JAR files that Rio services and tests
 * usually need. Each Webster is started on the port defined by the
 * <code> net.assimilator.qa.core.port</code> property. Each
 * Webster is configured to serve files from the directories defined
 * by the <code>net.assimilator.qa.core.dldirs</code> property.
 * <p/>
 * <li>If the <code>testRunLookupService</code> property is <code>true</code>
 * (the default), starts a lookup service on one of the test hosts
 * during the test setup.
 * <p/>
 * <li>If the <code>testRunProvisionMonitor</code> property is <code>true
 * </code> (the default), starts a Provision Monitor on one of the test
 * hosts during the test setup.
 * <p/>
 * <li>If the <code>testRunCybernodes</code> property is <code>true</code>
 * (the default), starts a Cybernode on each test host including the
 * master during the test setup.
 * <p/>
 * <li>Provides support for deploying OpStrings.
 * </ul>
 *
 * @version $Id$
 */
public abstract class AssimilatorTest extends QATest {

    /**
     * The logger used by this class.
     */
    private static Logger logger = Logger.getLogger("net.assimilator.qa.core");

    /**
     * The Rio service manager.
     */
    private AssimilatorManager assimilatorManager;

    /**
     * The table of hosts participating in the test.
     */
    private HostTable hostTable;

    /**
     * The lookup discovery manager maintained by this test.
     */
    private LookupDiscoveryManager lookupDiscoveryManager;

    /**
     * The service discovery manager maintained by this test.
     */
    private ServiceDiscoveryManager serviceDiscoveryManager;

    /**
     * Constructs a <code>AssimilatorTest</code>.
     */
    public AssimilatorTest() {
    }

    /**
     * Retrieves the Assimilator service manager.
     *
     * @return the Assimilator service manager
     */
    public AssimilatorManager getManager() {
        return assimilatorManager;
    }

    /**
     * Retrieves the table of hosts participating in the test.
     *
     * @return the table of hosts participating in the test
     */
    public HostTable getHostTable() {
        return hostTable;
    }

    /**
     * Retrieves the lookup discovery manager maintained by this
     * <code>AssimilatorTest</code>.
     * This lookup discovery manager is configured to use groups and
     * locators defined by the <code>testGroupsAndLocators</code>
     * property.
     * This lookup discovery manager can be used by the test to discover
     * lookup services participating in the test.
     *
     * @return the lookup discovery manager
     */
    public LookupDiscoveryManager getLookupDiscoveryManager() {
        return lookupDiscoveryManager;
    }

    /**
     * Retrieves the service discovery manager maintained by this
     * <code>AssimilatorTest</code>.
     * This service discovery manager is configured to use the lookup
     * discovery manager accessible via {@link #getLookupDiscoveryManager()}.
     * This service discovery manager can be used by the test to
     * discover services participating in the test.
     *
     * @return the service discovery manager
     */
    public ServiceDiscoveryManager getServiceDiscoveryManager() {
        return serviceDiscoveryManager;
    }

    /**
     * Retrieves the names of the hosts participating in the test.
     * The method is different from {@link QAConfig#getHostList()} in
     * that the list returned by the method is never empty. That is,
     * if the set of hosts consists of only one host, the list returned
     * by the method contains one <code>null</code> item.
     *
     * @return the list of host names
     */
    public ArrayList getHostList() {
        ArrayList hostList = (ArrayList) config.getHostList().clone();
        if (hostList.size() == 0) {
            hostList.add(null);
        }
        return hostList;
    }

    /**
     * Retrieves the OpString URL specified by the <code>testOpString</code>
     * property.
     *
     * @return the OpString URL specified by the <code>testOpString</code>
     *         property
     * @throws TestException         if the <code>testOpString</code> property is
     *                               not specified
     * @throws MalformedURLException if conversion of the
     *                               <code>testOpString</code> property into URL fails due to
     *                               unknown protocol
     */
    public URL getOpString() throws TestException, MalformedURLException {
        String testOpString = config.getStringConfigVal("testOpString", null);
        if (testOpString == null) {
            throw new TestException("testOpString not specified");
        }
        return new URL(testOpString);
    }

    /**
     * The method is overridden to do class-specific initialization.
     *
     * @throws Exception if any failure occurs during setup
     */
    public void setup(QAConfig config) throws Exception {
        super.setup(config);
        assimilatorManager = new AssimilatorManager(manager, config);
        hostTable = new HostTable(config.getHostList());
        initDiscoveryManagers();
        String propName = "testRunWebsters";
        if (config.getBooleanConfigVal(propName, true)) {
            assimilatorManager.startWebsters();
        }
        propName = "testRunLookupService";
        if (config.getBooleanConfigVal(propName, true)) {
            assimilatorManager.startLookupService(null);
        }
        propName = "testRunProvisionMonitor";
        if (config.getBooleanConfigVal(propName, true)) {
            assimilatorManager.startProvisionMonitor(null);
        }
        propName = "testRunCybernodes";
        if (config.getBooleanConfigVal(propName, true)) {
            for (int i = 0; i < getHostList().size(); i++) {
                assimilatorManager.startCybernode(i);
            }
        }
    }

    /**
     * Initializes the lookup and service discovery managers.
     *
     * @throws Exception if any failure occurs during the operation
     */
    private void initDiscoveryManagers() throws Exception {
        logger.info("");
        logger.info("Initializing discovery managers");
        String propName = "testGroupsAndLocators";
        String groupsAndLocators = config.getStringConfigVal(propName, null);
        logger.info(propName + ": " + groupsAndLocators);
        ArrayList groupList = new ArrayList();
        ArrayList<String> locatorList = new ArrayList<String>();
        ConfigUtils.parseGroupsAndLocators(groupsAndLocators, groupList, locatorList);
        String[] groups = (String[]) groupList.toArray(new String[groupList.size()]);
        logger.info("Groups:   " + Arrays.asList(groups));
        LookupLocator[] locators = locatorNamesToObjects(locatorList);
        logger.info("Locators: " + Arrays.asList(locators));
        Configuration configuration = config.getConfiguration();
        lookupDiscoveryManager = new LookupDiscoveryManager(groups, locators, null, configuration);
        LeaseRenewalManager lrm = new LeaseRenewalManager(configuration);
        serviceDiscoveryManager = new ServiceDiscoveryManager(lookupDiscoveryManager, lrm, configuration);
    }

    /**
     * Converts a list of lookup locator names to an array of
     * <code>LookupLocator</code> objects.
     *
     * @param locatorNames the list of lookup locator names
     * @return the array of <code>LookupLocator</code> objects
     * @throws MalformedURLException if any of the lookup locator
     *                               names could not be parsed
     */
    private LookupLocator[] locatorNamesToObjects(ArrayList<String> locatorNames) throws MalformedURLException {
        LookupLocator[] locators = new LookupLocator[locatorNames.size()];
        for (int i = 0; i < locatorNames.size(); i++) {
            String s = locatorNames.get(i);
            locators[i] = new LookupLocator(s);
        }
        return locators;
    }

    /**
     * Deploys the OpString referenced by the <code>testOpString</code>
     * test property. The <code>testOpString</code> property must be
     * a URL referencing a file containing an OpString definition.
     * The method uses the <code>AssimilatorTest</code>'s service discovery
     * manager to discover provision monitors and uses the first
     * discovered provision monitor to deploy the OpString.
     *
     * @throws Exception if any failure occurs during the operation
     */
    public void deployOpString() throws Exception {
        deployOpString(getOpString());
    }

    /**
     * Deploys the OpString referenced by the <code>testOpString</code>
     * test property using a given Provision Monitor. The
     * <code>testOpString</code> property must be a URL referencing a
     * file containing an OpString definition.
     *
     * @param monitor the provision monitor to use to deploy the OpString
     * @throws Exception if any failure occurs during the operation
     */
    public void deployOpString(ProvisionMonitor monitor) throws Exception {
        deployOpString(getOpString(), monitor);
    }

    /**
     * Deploys a given OpString. The method uses the <code>AssimilatorTest</code>'s
     * service discovery manager to discover provision monitors and uses
     * the first discovered provision monitor to deploy the OpString.
     *
     * @param opStringUrl the URL of the file containing the OpString
     *                    definition
     * @throws Exception if any failure occurs during the operation
     */
    public void deployOpString(URL opStringUrl) throws Exception {
        logger.info("");
        logger.info("Discovering ProvisionMonitor ...");
        Class[] types = new Class[] { ProvisionMonitor.class };
        ServiceTemplate template = new ServiceTemplate(null, types, null);
        ServiceDiscoveryManager sdm = getServiceDiscoveryManager();
        ServiceItem serviceItem = sdm.lookup(template, null, 60000);
        if (serviceItem == null) {
            throw new TestException("Unable to discover ProvisionMonitor");
        }
        ProvisionMonitor monitor = (ProvisionMonitor) serviceItem.service;
        logger.info("ProvisionMonitor has been discovered");
        deployOpString(opStringUrl, monitor);
    }

    /**
     * Deploys a given OpString using a given Provision Monitor.
     *
     * @param opStringUrl the URL of the file containing the OpString
     *                    definition
     * @param monitor     the provision monitor to use to deploy the OpString
     * @throws Exception if any failure occurs during the operation
     */
    public void deployOpString(URL opStringUrl, ProvisionMonitor monitor) throws Exception {
        logger.info("");
        logger.info("Loading OpString [" + opStringUrl + "] ...");
        OpStringLoader opStringLoader = new OpStringLoader(this.getClass().getClassLoader());
        OperationalString[] opStrings = opStringLoader.parseOperationalString(opStringUrl);
        logger.info("OpString [" + opStringUrl + "] has been loaded");
        logger.info("");
        OperationalString opString = opStrings[0];
        String name = opString.getName();
        DeployAdmin deployAdmin = (DeployAdmin) monitor.getAdmin();
        if (deployAdmin.hasDeployed(name)) {
            logger.info("Deploying OpString [" + name + "] (updating) ...");
            OperationalStringManager opStringMgr = deployAdmin.getOperationalStringManager(name);
            opStringMgr.updateOperationalString(opString);
        } else {
            logger.info("Deploying OpString [" + name + "] ...");
            deployAdmin.deploy(opString);
        }
        logger.info("OpString [" + name + "] has been deployed");
    }
}
