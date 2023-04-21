package net.sf.nodeInsecure.computer;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import net.sf.nodeInsecure.hibernate.DbTestUtil;
import static net.sf.nodeInsecure.hibernate.DbTestUtil.injectResorces;
import static net.sf.nodeInsecure.hibernate.DbTestUtil.resetSchema;
import net.sf.nodeInsecure.hibernate.HibernateUtil;
import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author: janmejay.singh
 * Date: Aug 28, 2007
 * Time: 7:18:14 PM
 */
public class MachineTest implements DbTestUtil.TestResourcesAware {

    private HibernateUtil hibernateUtil;

    private Session session;

    private Machine machine;

    @Before
    public void setup() {
        injectResorces(this);
        session = hibernateUtil.getSessionFactory().openSession();
        machine = (Machine) session.get(Machine.class, 1);
    }

    @After
    public void tearDown() {
        session.close();
        resetSchema();
    }

    @Test
    public void directoryResolution_at_root_level() {
        Directory directory = machine.getDir("/home");
        assertNotNull(directory);
        assertEquals("home", directory.getName());
    }

    @Test
    public void directoryResolution_for_root_directory() {
        Directory directory = machine.getDir("/");
        assertNotNull(directory);
        assertEquals(directory, machine.getFileSystem().getRootDir());
    }

    @Test
    public void directoryResolution_one_level_beyond_root_level() {
        Directory directory = machine.getDir("/home/testUsr");
        assertNotNull(directory);
        assertEquals("testUsr", directory.getName());
    }

    @Test
    public void fileResolution_one_level_beyond_root_level() {
        File file = machine.getFile("/home/system.so.1");
        assertNotNull(file);
        assertEquals("system.so.1", file.getName());
    }

    public void setHibernateUtil(HibernateUtil hibernateUtil) {
        this.hibernateUtil = hibernateUtil;
    }
}
