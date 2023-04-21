package cn.myapps.core.counter.ejb;

import static junit.framework.Assert.*;
import org.junit.Test;
import cn.myapps.base.AbstractRuntimeProcessBeanTest;
import cn.myapps.core.counter.dao.CounterDAO;
import cn.myapps.util.CreateProcessException;
import cn.myapps.util.ProcessFactory;

public class CounterProcessTest extends AbstractRuntimeProcessBeanTest {

    public CounterProcessTest() throws Exception {
        super();
    }

    @Test
    public void testGetDAO() throws Exception {
        CounterProcessBean bean = new CounterProcessBean(getApplicationId());
        CounterDAO dao = (CounterDAO) bean.getDAO();
        assertNotNull(dao);
    }

    @Test
    public void testGetInstance() throws ClassNotFoundException, CreateProcessException {
        CounterProcess bean = (CounterProcess) ProcessFactory.createRuntimeProcess(CounterProcess.class, getApplicationId());
        assertNotNull(bean);
    }

    @Test
    public void testDoRemoveByName() throws Exception {
        CounterProcess bean = (CounterProcess) ProcessFactory.createRuntimeProcess(CounterProcess.class, getApplicationId());
        int num = bean.getNextValue("test", getApplicationId(), "domainid");
        assertTrue(num == 1);
        bean.doRemoveByName("test", getApplicationId(), "domainid");
    }

    @Test
    public void testGetNextValue() throws Exception {
        CounterProcess bean = (CounterProcess) ProcessFactory.createRuntimeProcess(CounterProcess.class, getApplicationId());
        for (int i = 0; i < 100; i++) {
            int num = bean.getNextValue("test", getApplicationId(), "domainid");
            assertTrue(num == i + 1);
        }
    }
}
