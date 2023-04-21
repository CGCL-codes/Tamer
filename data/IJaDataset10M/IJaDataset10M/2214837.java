package net.sf.nodeInsecure.dao.computer;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import net.sf.nodeInsecure.computer.Machine;
import net.sf.nodeInsecure.computer.MachineConfiguration;
import net.sf.nodeInsecure.hibernate.DbTestUtil;
import net.sf.nodeInsecure.hibernate.HibernateUtil;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

/**
 * @author: janmejay.singh
 * Date: Aug 28, 2007
 * Time: 6:09:35 PM
 */
public class MachineDAOImplTest implements DbTestUtil.TestResourcesAware {

    private MachineDAO machineDAO;

    @Before
    public void setup() {
        DbTestUtil.injectResorces(this);
    }

    @Test
    public void getAllConfigurations() {
        List<MachineConfiguration> configs = machineDAO.getAllConfigurations();
        assertNotNull(configs);
        assertEquals(6, configs.size());
    }

    @Test
    public void getMachineById() {
        Machine machine = machineDAO.byId(1);
        assertNotNull(machine);
    }

    public void setHibernateUtil(HibernateUtil hibernateUtil) {
        machineDAO = MachineDAOImpl.getInstance(hibernateUtil);
    }
}
