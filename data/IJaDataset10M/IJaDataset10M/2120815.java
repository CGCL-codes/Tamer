package org.opennms.netmgt.provision.persist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opennms.netmgt.dao.db.OpenNMSConfigurationExecutionListener;
import org.opennms.netmgt.provision.persist.foreignsource.ForeignSource;
import org.opennms.netmgt.provision.persist.foreignsource.PluginConfig;
import org.opennms.netmgt.provision.persist.requisition.Requisition;
import org.opennms.netmgt.provision.persist.requisition.RequisitionInterface;
import org.opennms.netmgt.provision.persist.requisition.RequisitionNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.UrlResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({ OpenNMSConfigurationExecutionListener.class, DependencyInjectionTestExecutionListener.class })
@ContextConfiguration(locations = { "classpath:/testForeignSourceContext.xml" })
public class FusedForeignSourceRepositoryTest {

    @Autowired
    @Qualifier("pending")
    private ForeignSourceRepository m_pending;

    @Autowired
    @Qualifier("deployed")
    private ForeignSourceRepository m_active;

    @Autowired
    @Qualifier("fused")
    private ForeignSourceRepository m_repository;

    @Before
    public void setUp() {
        for (ForeignSource fs : m_pending.getForeignSources()) {
            m_pending.delete(fs);
        }
        for (ForeignSource fs : m_active.getForeignSources()) {
            m_active.delete(fs);
        }
        for (Requisition r : m_pending.getRequisitions()) {
            m_pending.delete(r);
        }
        for (Requisition r : m_active.getRequisitions()) {
            m_active.delete(r);
        }
    }

    @Test
    public void integrationTest() {
        Requisition pendingReq = new Requisition("test");
        RequisitionNode node = new RequisitionNode();
        node.setForeignId("1");
        node.setNodeLabel("node label");
        RequisitionInterface iface = new RequisitionInterface();
        iface.setIpAddr("192.168.0.1");
        node.putInterface(iface);
        pendingReq.putNode(node);
        m_pending.save(pendingReq);
        ForeignSource pendingSource = m_repository.getForeignSource("test");
        assertTrue(pendingSource.isDefault());
        pendingSource.setDetectors(new ArrayList<PluginConfig>());
        m_pending.save(pendingSource);
        Requisition activeReq = m_repository.importResourceRequisition(new UrlResource(m_pending.getRequisitionURL("test")));
        ForeignSource activeSource = m_active.getForeignSource("test");
        assertEquals("active foreign source should match pending foreign source", activeSource, pendingSource);
        assertEquals("the requisitions should match too", activeReq, pendingReq);
        System.err.println("requisition = " + m_pending.getRequisition("test"));
        assertNull("the requisition should be null in the pending repo", m_pending.getRequisition("test"));
        assertTrue("the foreign source should be default since there's no specific in the pending repo", m_pending.getForeignSource("test").isDefault());
    }
}
