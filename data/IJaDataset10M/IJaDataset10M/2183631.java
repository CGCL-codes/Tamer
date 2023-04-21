package com.continuent.tungsten.manager.directory.test;

import java.util.List;
import junit.framework.TestCase;
import org.apache.log4j.Logger;
import com.continuent.tungsten.commons.cluster.resource.ResourceType;
import com.continuent.tungsten.commons.config.TungstenProperties;
import com.continuent.tungsten.commons.directory.Directory;
import com.continuent.tungsten.commons.directory.ResourceNode;
import com.continuent.tungsten.manager.resource.shared.ResourceConfiguration;

/**
 * Implements a directory test for manager-defined functions, such as managing
 * nodes for extensions and services. These cases cannot be be tested in commons
 * as they create circular dependencies on types.
 * 
 * @author <a href="mailto:robert.hodges@continuent.com">Robert Hodges</a>
 * @version 1.0
 */
public class ManagerDirectoryTest extends TestCase {

    private static Logger logger = Logger.getLogger(ManagerDirectoryTest.class);

    /**
     * Verify that we can add service nodes and read them back.
     */
    public void testLocateService() throws Exception {
        Directory directory = Directory.createLocalInstance(Directory.DEFAULT_CLUSTER_NAME, "testMember");
        String sessionID = directory.connect("test", 0, "testSessionId");
        TungstenProperties serviceProps = createMysqlProperties("mysql");
        String servicePath = addService(directory, sessionID, "mysql", serviceProps);
        TungstenProperties mysql2 = createMysqlProperties("mysql2");
        addService(directory, sessionID, "mysql2", mysql2);
        ResourceNode serviceNode2 = directory.locate(sessionID, servicePath);
        assertNotNull("Must be able to locate node", serviceNode2);
        ResourceConfiguration config2 = (ResourceConfiguration) serviceNode2.getResource();
        assertEquals("Check resource name", "mysql", config2.getName());
        TungstenProperties serviceProps2 = config2.getProperties();
        assertEquals("Resource contents must be equal", serviceProps, serviceProps2);
        directory.disconnect(sessionID);
    }

    /**
     * Verify that directory correctly handles commands to returns service 
     * configurations including cases where no service matches, host does not 
     * exist, one service matches, or we select all services. 
     * 
     * @throws Exception
     */
    public void testReturnService() throws Exception {
        Directory directory = Directory.createLocalInstance(Directory.DEFAULT_CLUSTER_NAME, "testMember");
        String sessionID = directory.connect("test", 0, "testSessionId");
        TungstenProperties mysqlProps = createMysqlProperties("mysql");
        addService(directory, sessionID, "mysql", mysqlProps);
        TungstenProperties mysql2Props = createMysqlProperties("mysql2");
        addService(directory, sessionID, "mysql2", mysql2Props);
        List<TungstenProperties> svcList0 = directory.getServiceConfig(sessionID, "non-existent", "mysql2");
        assertEquals("No service for non-existent member", 0, svcList0.size());
        List<TungstenProperties> svcList1 = directory.getServiceConfig(sessionID, "testMember", "none");
        assertEquals("No service for non-existent name", 0, svcList1.size());
        List<TungstenProperties> svcList2 = directory.getServiceConfig(sessionID, "testMember", "mysql2");
        assertEquals("Single service should be selected", 1, svcList2.size());
        TungstenProperties case1 = svcList2.get(0);
        assertEquals("Resource contents must be equal", mysql2Props, case1);
        List<TungstenProperties> svcList3 = directory.getServiceConfig(sessionID, "testMember", null);
        assertEquals("Both services should be selected", 2, svcList3.size());
        directory.disconnect(sessionID);
    }

    private TungstenProperties createMysqlProperties(String name) {
        TungstenProperties serviceProps = new TungstenProperties();
        serviceProps.setString("name", name);
        serviceProps.setString("command.start", "sudo /etc/init.d/mysql start");
        serviceProps.setString("command.restart", "sudo /etc/init.d/mysql restart");
        serviceProps.setString("command.status", "sudo /etc/init.d/mysql status");
        serviceProps.setString("command.stop", "sudo /etc/init.d/mysql stop");
        serviceProps.setString("resource.unique", "false");
        return serviceProps;
    }

    private String addService(Directory directory, String sessionID, String name, TungstenProperties props) throws Exception {
        ResourceConfiguration config = new ResourceConfiguration(name, props);
        String servicePath = String.format("/%s/%s/conf/%s/%s", Directory.DEFAULT_CLUSTER_NAME, "testMember", "service", name);
        config.setExecutable(true);
        logger.info(String.format("Adding %s definition: %s", ResourceType.SERVICE, name));
        ResourceNode serviceNode = directory.create(sessionID, servicePath, true);
        assertNotNull("New node must not be null", serviceNode);
        serviceNode.setResource(config);
        return servicePath;
    }
}
