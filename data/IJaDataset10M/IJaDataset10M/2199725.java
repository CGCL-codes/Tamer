package org.opennms.netmgt.collectd;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Properties;
import org.easymock.EasyMock;
import org.opennms.netmgt.config.MibObject;
import org.opennms.netmgt.dao.IpInterfaceDao;
import org.opennms.netmgt.mock.MockDataCollectionConfig;
import org.opennms.netmgt.mock.MockPlatformTransactionManager;
import org.opennms.netmgt.model.OnmsIpInterface;
import org.opennms.netmgt.model.OnmsNode;
import org.opennms.netmgt.model.RrdRepository;
import org.opennms.netmgt.rrd.RrdConfig;
import org.opennms.netmgt.rrd.RrdUtils;
import org.opennms.netmgt.rrd.jrobin.JRobinRrdStrategy;
import org.opennms.test.FileAnticipator;
import org.opennms.test.mock.MockLogAppender;
import org.opennms.test.mock.MockUtil;
import org.springframework.transaction.PlatformTransactionManager;
import junit.framework.TestCase;

/**
 * JUnit TestCase for PersistOperationBuilder.
 *  
 * @author <a href="mailto:dj@opennms.org">DJ Gregor</a>
 */
public class PersistOperationBuilderTest extends TestCase {

    private static boolean s_rrdInitialized = false;

    private FileAnticipator m_fileAnticipator;

    private File m_snmpDirectory;

    private OnmsIpInterface m_intf;

    private OnmsNode m_node;

    private PlatformTransactionManager m_transMgr = new MockPlatformTransactionManager();

    private IpInterfaceDao m_ifDao;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        MockUtil.println("------------ Begin Test " + getName() + " --------------------------");
        MockLogAppender.setupLogging();
        m_fileAnticipator = new FileAnticipator();
        m_intf = new OnmsIpInterface();
        m_node = new OnmsNode();
        m_node.setId(1);
        m_intf.setNode(m_node);
        m_intf.setIpAddress("1.1.1.1");
        m_intf.setId(27);
        m_ifDao = EasyMock.createMock(IpInterfaceDao.class);
        EasyMock.expect(m_ifDao.load(m_intf.getId())).andReturn(m_intf).anyTimes();
        EasyMock.replay(m_ifDao);
        if (!s_rrdInitialized) {
            RrdConfig.setProperties(new Properties());
            RrdUtils.setStrategy(new JRobinRrdStrategy());
            RrdUtils.initialize();
            s_rrdInitialized = true;
        }
    }

    @Override
    protected void runTest() throws Throwable {
        super.runTest();
        MockLogAppender.assertNoWarningsOrGreater();
        m_fileAnticipator.deleteExpected();
    }

    @Override
    protected void tearDown() throws Exception {
        m_fileAnticipator.deleteExpected(true);
        m_fileAnticipator.tearDown();
        MockUtil.println("------------ End Test " + getName() + " --------------------------");
        super.tearDown();
    }

    private CollectionAgent getCollectionAgent() {
        return DefaultCollectionAgent.create(m_intf.getId(), m_ifDao, m_transMgr);
    }

    public void testCommitWithNoDeclaredAttributes() throws Exception {
        RrdRepository repository = createRrdRepository();
        CollectionAgent agent = getCollectionAgent();
        MockDataCollectionConfig dataCollectionConfig = new MockDataCollectionConfig();
        OnmsSnmpCollection collection = new OnmsSnmpCollection(agent, new ServiceParameters(new HashMap<String, String>()), dataCollectionConfig);
        NodeResourceType resourceType = new NodeResourceType(agent, collection);
        CollectionResource resource = new NodeInfo(resourceType, agent);
        PersistOperationBuilder builder = new PersistOperationBuilder(repository, resource, "rrdName");
        builder.commit();
    }

    public void testCommitWithDeclaredAttribute() throws Exception {
        File nodeDir = m_fileAnticipator.expecting(getSnmpRrdDirectory(), m_node.getId().toString());
        m_fileAnticipator.expecting(nodeDir, "rrdName" + RrdUtils.getExtension());
        RrdRepository repository = createRrdRepository();
        CollectionAgent agent = getCollectionAgent();
        MockDataCollectionConfig dataCollectionConfig = new MockDataCollectionConfig();
        OnmsSnmpCollection collection = new OnmsSnmpCollection(agent, new ServiceParameters(new HashMap<String, String>()), dataCollectionConfig);
        NodeResourceType resourceType = new NodeResourceType(agent, collection);
        CollectionResource resource = new NodeInfo(resourceType, agent);
        MibObject mibObject = new MibObject();
        mibObject.setOid(".1.1.1.1");
        mibObject.setAlias("mibObjectAlias");
        mibObject.setType("string");
        mibObject.setInstance("0");
        mibObject.setMaxval(null);
        mibObject.setMinval(null);
        SnmpAttributeType attributeType = new StringAttributeType(resourceType, "some-collection", mibObject, new AttributeGroupType("mibGroup", "ignore"));
        PersistOperationBuilder builder = new PersistOperationBuilder(repository, resource, "rrdName");
        builder.declareAttribute(attributeType);
        builder.commit();
    }

    public void testCommitWithDeclaredAttributeAndValue() throws Exception {
        File nodeDir = m_fileAnticipator.expecting(getSnmpRrdDirectory(), m_node.getId().toString());
        m_fileAnticipator.expecting(nodeDir, "rrdName" + RrdUtils.getExtension());
        RrdRepository repository = createRrdRepository();
        CollectionAgent agent = getCollectionAgent();
        MockDataCollectionConfig dataCollectionConfig = new MockDataCollectionConfig();
        OnmsSnmpCollection collection = new OnmsSnmpCollection(agent, new ServiceParameters(new HashMap<String, String>()), dataCollectionConfig);
        NodeResourceType resourceType = new NodeResourceType(agent, collection);
        CollectionResource resource = new NodeInfo(resourceType, agent);
        MibObject mibObject = new MibObject();
        mibObject.setOid(".1.1.1.1");
        mibObject.setAlias("mibObjectAlias");
        mibObject.setType("string");
        mibObject.setInstance("0");
        mibObject.setMaxval(null);
        mibObject.setMinval(null);
        SnmpAttributeType attributeType = new StringAttributeType(resourceType, "some-collection", mibObject, new AttributeGroupType("mibGroup", "ignore"));
        PersistOperationBuilder builder = new PersistOperationBuilder(repository, resource, "rrdName");
        builder.declareAttribute(attributeType);
        builder.setAttributeValue(attributeType, "6.022E23");
        builder.commit();
    }

    private RrdRepository createRrdRepository() throws IOException {
        RrdRepository repository = new RrdRepository();
        repository.setRrdBaseDir(getSnmpRrdDirectory());
        repository.setHeartBeat(600);
        repository.setStep(300);
        repository.setRraList(Collections.singletonList("RRA:AVERAGE:0.5:1:100"));
        return repository;
    }

    private File getSnmpRrdDirectory() throws IOException {
        if (m_snmpDirectory == null) {
            m_snmpDirectory = m_fileAnticipator.tempDir("snmp");
        }
        return m_snmpDirectory;
    }
}
