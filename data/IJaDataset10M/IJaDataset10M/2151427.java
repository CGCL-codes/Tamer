package net.cepra.test.util;

import javax.persistence.EntityManager;
import net.cepra.test.PersistenceHelper;
import org.junit.After;
import org.junit.Before;

public class ModelTest {

    private EntityManager em;

    @Before
    public void setup() {
        em = PersistenceHelper.createEntityManager();
    }

    @After
    public void cleanup() {
        em.close();
    }
}
