package org.horus.miniframewrk;

import org.apache.log4j.Logger;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Adrián
 */
public class DAOFactoryTest {

    private Logger log = Logger.getLogger(DAOFactoryTest.class);

    @Test
    public void testGetDAO() throws Exception {
        log.info("getDAO");
        String name = "stringDAO";
        DAOFactory instance = new DAOFactory("org.horus.miniframewrk.testbundles", null);
        StringDAO result = instance.getDAO(name, StringDAO.class);
        assertNotNull(result);
    }

    @Test(expected = NoSuchDAOException.class)
    public void testNotFoundDAO() throws Exception {
        log.info("getDAO");
        String name = "noexisto";
        DAOFactory instance = new DAOFactory("org.horus.miniframewrk.testbundles", null);
        instance.getDAO(name, StringDAO.class);
    }
}
