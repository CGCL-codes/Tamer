package redora.junit;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import redora.api.fetch.Scope;
import redora.exceptions.LazyException;
import redora.exceptions.RedoraException;
import redora.test.rdo.model.JUnitChild;
import redora.test.rdo.model.JUnitMaster;
import redora.test.rdo.model.JUnitPig;
import redora.test.rdo.model.enums.Sex;
import redora.test.rdo.service.JUnitChildService;
import redora.test.rdo.service.JUnitMasterService;
import redora.test.rdo.service.ServiceFactory;
import static redora.api.fetch.Scope.Table;
import static redora.junit.AbstractDBTest.*;
import static redora.test.rdo.service.ServiceFactory.jUnitChildService;
import static redora.test.rdo.service.ServiceFactory.jUnitMasterService;
import static redora.util.JUnitUtil.assertRedoraPersist;

/**
 * Check the n-to-1 relations.
 * 
 * @author Nanjing RedOrange (www.red-orange.cn)
 */
public class ObjectTest {

    static JUnitMasterService jUnitMasterService;

    static JUnitChildService jUnitChildService;

    @BeforeClass
    public static void startService() throws RedoraException {
        makeTestTables();
        jUnitMasterService = ServiceFactory.jUnitMasterService();
        jUnitChildService = ServiceFactory.jUnitChildService();
    }

    @AfterClass
    public static void stopService() throws RedoraException {
        dropTestTables();
        jUnitMasterService.close();
        jUnitChildService.close();
    }

    /** Basic insert and remove tests */
    @Test
    public void insertRemove() throws RedoraException {
        JUnitMaster master = new JUnitMaster();
        master.avoidNull();
        assertRedoraPersist(jUnitMasterService.persist(master));
        JUnitChild child = new JUnitChild();
        child.setJUnitMaster(master);
        assertRedoraPersist(jUnitChildService.persist(child));
        child = jUnitChildService.findById(child.getId(), Table);
        assertNotNull("Dad failed to associate with son", child.getJUnitMaster());
        child.setJUnitMaster(null);
        assertRedoraPersist(jUnitChildService.persist(child));
        child = jUnitChildService.findById(child.getId(), Table);
        assertNull("Dad failed to divorce from son", child.getJUnitMaster());
    }

    /**
     * Checks a null (optional) object. The fetchLazy should not invoke when the
     * object is null.
     */
    @Test
    public void nullObject() throws RedoraException {
        JUnitChild child = new JUnitChild();
        child.setName("No master");
        assertRedoraPersist(jUnitChildService.persist(child));
        child = jUnitChildService.findById(child.getId(), Table);
        assertNull("This should return null", child.getJUnitMaster());
        assertEquals("Because fetchLazy should be untouched, the fetchscope should also still be at Table.", Table, child.fetchScope);
        child.setJUnitMaster(null);
        assertEquals("Added nothing", 0, child.dirty.size());
        child.setJUnitMaster(new JUnitMaster());
        assertEquals("Added empty master", 1, child.dirty.size());
    }

    /** Test if updates on the son are handled */
    @Test
    public void update() throws RedoraException {
        JUnitMaster master = new JUnitMaster();
        master.avoidNull();
        assertRedoraPersist(jUnitMasterService.persist(master));
        JUnitChild child = new JUnitChild();
        child.setJUnitMaster(master);
        child.setSex(Sex.male);
        assertRedoraPersist(jUnitChildService.persist(child));
        child.setSex(Sex.female);
        assertRedoraPersist(jUnitMasterService.persist(master));
        assertNull("Son was updated, wrong!", child.getUpdateDate());
        child.setSex(Sex.male);
        assertRedoraPersist(jUnitMasterService.persist(master));
        assertNull("Son was not updated", child.getUpdateDate());
    }

    @Test
    public void circular() throws LazyException {
        try {
            JUnitPig master = new JUnitPig();
            master.setDad(master);
            fail("Expected AssertionError");
        } catch (AssertionError e) {
        }
    }
}
