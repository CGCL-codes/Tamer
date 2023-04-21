package com.fdaoud.rayures.extension;

import static org.testng.Assert.*;
import org.testng.annotations.Test;
import com.fdaoud.rayures.action.SpringActionBean;
import com.fdaoud.rayures.service.ADao;
import com.fdaoud.rayures.service.impl.ADaoImpl;
import com.fdaoud.rayures.test.TestWithMockContainer;

/**
 *
 * @author Frederic Daoud
 */
@Test
public class RayuresObjectFactoryTest extends TestWithMockContainer {

    public String getWebXmlPath() {
        return "/web-test.xml";
    }

    public void testSpringBeanInjection() throws Exception {
        SpringActionBean bean = doRequestOn(SpringActionBean.class);
        ADao adao = bean.getAdao();
        assertNotNull(adao, "injected property should not be null");
        assertEquals(adao.getClass(), ADaoImpl.class, "injected Dao should be DaoImpl");
    }
}
