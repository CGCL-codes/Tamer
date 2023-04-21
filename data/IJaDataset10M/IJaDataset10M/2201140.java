package org.openwms.core.integration.file;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openwms.core.domain.preferences.ApplicationPreference;
import org.openwms.core.domain.preferences.ModulePreference;
import org.openwms.core.domain.system.AbstractPreference;
import org.openwms.core.domain.system.PreferenceKey;
import org.openwms.core.domain.system.PropertyScope;
import org.openwms.core.domain.system.usermanagement.UserPreference;
import org.openwms.core.integration.PreferenceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A PreferencesDaoTest.
 * 
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 * @version $Revision: $
 * @since 0.1
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/org/openwms/core/integration/file/Test-context.xml")
public class PreferencesDaoTest {

    @Autowired
    private PreferenceDao<PreferenceKey> dao;

    /**
     * Test method for
     * {@link org.openwms.core.integration.file.PreferencesDaoImpl#findByKey(org.openwms.core.domain.system.PreferenceKey)}
     * .
     */
    @Test
    public final void testFindAPP1ByKey() {
        AbstractPreference pref = dao.findByKey(new PreferenceKey(PropertyScope.APPLICATION, "APP1"));
        assertContent("{" + PropertyScope.APPLICATION + ",APP1},<null>,,<null>,0,0", pref);
    }

    /**
     * Test method for
     * {@link org.openwms.core.integration.file.PreferencesDaoImpl#findByKey(org.openwms.core.domain.system.PreferenceKey)}
     * .
     */
    @Test
    public final void testFindAPP2ByKey() {
        AbstractPreference pref = dao.findByKey(new PreferenceKey(PropertyScope.APPLICATION, "APP2"));
        assertContent("{" + PropertyScope.APPLICATION + ",APP2},Some value,Description is used as element value,100.0,5,100", pref);
    }

    /**
     * Test method for
     * {@link org.openwms.core.integration.file.PreferencesDaoImpl#findByKey(org.openwms.core.domain.system.PreferenceKey)}
     * .
     */
    @Test
    public final void testFindModuleCOREByKey() {
        AbstractPreference pref = dao.findByKey(new PreferenceKey(PropertyScope.MODULE, "CORE", "module.name"));
        assertContent("{MODULE,CORE,module.name},CORE Module,This is also a description value,<null>,0,0", pref);
    }

    /**
     * Test method for
     * {@link org.openwms.core.integration.file.PreferencesDaoImpl#findByKey(org.openwms.core.domain.system.PreferenceKey)}
     * .
     */
    @Test
    public final void testFindModuleCOMMON1ByKey() {
        AbstractPreference pref = dao.findByKey(new PreferenceKey(PropertyScope.MODULE, "COMMON", "host.ip-adress"));
        assertContent("{MODULE,COMMON,host.ip-adress},127.0.0.1,,<null>,0,0", pref);
    }

    /**
     * Test method for
     * {@link org.openwms.core.integration.file.PreferencesDaoImpl#findByKey(org.openwms.core.domain.system.PreferenceKey)}
     * .
     */
    @Test
    public final void testFindUserTestByKey() {
        AbstractPreference pref = dao.findByKey(new PreferenceKey(PropertyScope.USER, "testuser", "testKey"));
        assertContent("{USER,testuser,testKey},<null>,,<null>,0,0", pref);
    }

    /**
     * Test method for
     * {@link org.openwms.core.integration.file.PreferencesDaoImpl#findByType(org.openwms.core.domain.system.AbstractPreference)}
     * .
     */
    @Test
    public final void testApplicationPreferencesFindByType() {
        assertEquals(2, dao.findByType(ApplicationPreference.class).size());
    }

    /**
     * Test method for
     * {@link org.openwms.core.integration.file.PreferencesDaoImpl#findByType(org.openwms.core.domain.system.AbstractPreference)}
     * .
     */
    @Test
    public final void testModulePreferencesFindByType() {
        assertEquals(3, dao.findByType(ModulePreference.class).size());
    }

    /**
     * Test method for
     * {@link org.openwms.core.integration.file.PreferencesDaoImpl#findByType(org.openwms.core.domain.system.AbstractPreference)}
     * .
     */
    @Test
    public final void testUserPreferencesFindByType() {
        assertEquals(1, dao.findByType(UserPreference.class).size());
    }

    /**
     * Test method for
     * {@link org.openwms.core.integration.file.PreferencesDaoImpl#findAll()}.
     */
    @Test
    public final void testFindAll() {
        assertEquals(6, dao.findAll().size());
    }

    private void assertContent(String expected, AbstractPreference pref) {
        if (null == pref) {
            fail("Preference not found");
        }
        assertEquals("Not a valid transformed ApplicationPreference", expected, pref.getPropertiesAsString());
    }
}
