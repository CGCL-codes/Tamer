package org.openwms.core.integration.jpa;

import static junit.framework.Assert.assertTrue;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.Before;
import org.junit.Test;
import org.openwms.core.domain.preferences.ApplicationPreference;
import org.openwms.core.domain.system.AbstractPreference;
import org.openwms.core.integration.PreferenceWriter;
import org.openwms.core.test.AbstractJpaSpringContextTests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;

/**
 * A PreferencesDaoTest.
 * 
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 * @version $Revision: $
 * @since 0.1
 */
@ContextConfiguration("classpath:/org/openwms/core/integration/jpa/Test-context.xml")
public class PreferencesDaoTest extends AbstractJpaSpringContextTests {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    @Qualifier("preferencesJpaDao")
    private PreferenceWriter<Long> dao;

    /**
     * Setup some test data.
     */
    @Before
    public final void onSetup() {
        em.persist(new ApplicationPreference("APP1"));
        em.flush();
        em.clear();
    }

    /**
     * Test whether the returned instance is the same.
     */
    @Test
    public final void testEquality() {
        List<AbstractPreference> prefs = dao.findAll();
        AbstractPreference abPref = new ApplicationPreference("APP1");
        assertTrue(prefs.get(0).equals(abPref));
        assertTrue(abPref.equals(prefs.get(0)));
        assertTrue(prefs.contains(abPref));
    }
}
