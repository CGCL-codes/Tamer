package net.sourceforge.configured.examples.dao;

import net.sourceforge.configured.examples.BaseTestCase;
import net.sourceforge.configured.examples.domain.dao.GenericDao;
import net.sourceforge.configured.examples.domain.entities.BaseDomainObject;
import net.sourceforge.configured.examples.equality.EqualityTester;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseDaoTest<T extends BaseDomainObject> extends BaseTestCase {

    protected Logger logger = LoggerFactory.getLogger(BaseDaoTest.class);

    protected abstract GenericDao<T> getDao();

    protected abstract EqualityTester<T> getEqualityTester();

    protected abstract T getTestEntity();

    @Test
    public void testRoundTrip() throws Exception {
        GenericDao<T> dao = getDao();
        EqualityTester<T> equalityTester = getEqualityTester();
        T entity = getTestEntity();
        T e2 = null;
        try {
            dao.saveOrUpdate(entity);
            e2 = dao.findById(entity.getId());
            equalityTester.assertEquals(entity, e2);
        } finally {
            if (entity.getId() != null && e2 != null) {
                try {
                    dao.delete(e2);
                } catch (Exception e) {
                    logger.error("error deleting test entity: " + entity.getClass(), e);
                }
            }
        }
    }
}
