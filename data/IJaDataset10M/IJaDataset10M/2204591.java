package org.netbeans.modules.exceptions.entity;

import javax.persistence.EntityManager;
import org.junit.Test;
import org.netbeans.server.uihandler.DatabaseTestCase;

/**
 *
 * @author Jindrich Sedek
 */
public class PrefnameTest extends DatabaseTestCase {

    public PrefnameTest(String name) {
        super(name);
    }

    @Test
    public void testGetExistOrNew() {
        String nameStr = "test1";
        EntityManager em = perUtils.createEntityManager();
        em.getTransaction().begin();
        Prefname name = Prefname.getExistsOrNew(em, nameStr);
        assertNotNull(name);
        name = Prefname.getExistsOrNew(em, nameStr);
        assertNotNull(name);
        em.getTransaction().rollback();
        em = perUtils.createEntityManager();
        em.getTransaction().begin();
        name = Prefname.getExistsOrNew(em, nameStr);
        assertNotNull(name);
        assertNotNull(name.getName());
        em.getTransaction().commit();
    }
}
