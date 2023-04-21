package uk.org.ogsadai.test.server;

import junit.framework.TestCase;
import uk.org.ogsadai.client.toolkit.DataRequestExecutionResource;
import uk.org.ogsadai.client.toolkit.Server;
import uk.org.ogsadai.client.toolkit.exception.ResourceUnknownException;
import uk.org.ogsadai.resource.ResourceID;
import uk.org.ogsadai.resource.ResourceType;
import uk.org.ogsadai.test.server.ServerTestProperties;
import uk.org.ogsadai.test.server.TestServerProxyFactory;
import uk.org.ogsadai.test.server.Utility;
import uk.org.ogsadai.util.xml.XML;

/**
 * General server tests. This class expects  
 * test properties to be provided in a file whose location is
 * specified in a system property,
 * <code>ogsadai.test.properties</code>. The following properties need
 * to be provided:
 * <ul>
 * <li>General server test properties:
 * <ul>
 * <li>
 * <code>server.url</code> - server URL (depends on server type).
 * </li>
 * <li>
 * <code>server.proxy.factory</code> - name of class used to create
 * client toolkit proxty server (depends on server type).
 * </li>
 * <li>
 * <code>server.version</code> - server version ID (depends on server type). 
 * </li>
 * <li>
 * <code>server.drer.id</code> - DRER ID on test server.
 * </li>
 * <li>
 * Additional properties may be required depending on the server type.
 * </li>
 * </ul>
 * </li>
 * <li>
 * Test-specific properties.
 * <ul>
 * <li>
 * <code>server.drer.property.list</code> - comma-separated list of
 * one or more test properties in this file that provide DRER IDs. 
 * </li>
 * <li>
 * <code>server.data.resource.property.list</code> - comma-separated
 * list of one or more test properties in this file that provide data
 * resource IDs.
 * </li>
 * </ul>
 * </li>
 * </ul>
 *
 * @author The OGSA-DAI Project Team
 */
public class ServerTest extends TestCase {

    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2007-2009.";

    /** Expected DRERs property name. */
    public static final String DRERS = "server.drer.property.list";

    /** Expected data resources property name. */
    public static final String DATA_RESOURCES = "server.data.resource.property.list";

    /** Test properties. */
    private final ServerTestProperties mProperties;

    /** Basic (unsecure) server. */
    private Server mServer;

    /**
     * Constructor.
     *
     * @param name
     *     Test case name.
     * @throws Exception
     *     If any problems arise in reading the test properties.
     */
    public ServerTest(String name) throws Exception {
        super(name);
        mProperties = new ServerTestProperties();
    }

    /**
     * {@inheritDoc}
     */
    public void setUp() throws Exception {
        mServer = TestServerProxyFactory.getServerProxy(mProperties);
    }

    /**
     * Run test for command line.
     * 
     * @param args
     *     Not used.
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(ServerTest.class);
    }

    /**
     * Tests getting the version from the server.
     * 
     * @throws Exception if an unexpected error occurs.
     */
    public void testGetVersion() throws Exception {
        String version = mServer.getVersion(ResourceType.DATA_REQUEST_EXECUTION_RESOURCE);
        assertEquals("Unexpected server version", mProperties.getServerVersion(), version);
    }

    /**
     * Tests the listResources method. In this test we just look for
     * DRERs and data resources.
     * 
     * @throws Exception if an unexpected error occurs.
     */
    public void testListResources() throws Exception {
        String[] drers = (mProperties.getProperty(DRERS)).split(",");
        String[] dataResources = (mProperties.getProperty(DATA_RESOURCES)).split(",");
        ResourceID[] drerIDs = new ResourceID[drers.length];
        for (int i = 0; i < drers.length; i++) {
            drerIDs[i] = new ResourceID(mProperties.getProperty(drers[i]));
        }
        ResourceID[] dataResourceIDs = new ResourceID[dataResources.length];
        for (int i = 0; i < dataResources.length; i++) {
            dataResourceIDs[i] = new ResourceID(mProperties.getProperty(dataResources[i]));
        }
        ResourceID[] resources = mServer.listResources(ResourceType.DATA_REQUEST_EXECUTION_RESOURCE);
        Utility.checkResourceList(resources, drerIDs, dataResourceIDs);
        resources = mServer.listResources(ResourceType.DATA_RESOURCE);
        Utility.checkResourceList(resources, dataResourceIDs, drerIDs);
    }
}
