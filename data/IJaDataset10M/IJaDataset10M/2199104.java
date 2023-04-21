package com.genia.toolbox.projects.uml_test.test.persistence.test.one_to_one_bidi;

import javax.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.junit.Assert.assertEquals;
import com.genia.toolbox.model.association.one_to_one_bidi.CarOneToOneBidi;
import com.genia.toolbox.model.association.one_to_one_bidi.DriverOneToOneBidi;
import com.genia.toolbox.persistence.criteria.Criteria;
import com.genia.toolbox.persistence.criteria.CriteriaType;
import com.genia.toolbox.persistence.dao.AbstractDAO;
import com.genia.toolbox.persistence.exception.PersistenceException;
import com.genia.toolbox.persistence.manager.PersistenceManager;
import com.genia.toolbox.spring.initializer.PluginContextLoader;

/**
 * Testing one-to-one product with hibernate.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = PluginContextLoader.class)
public class TestProduct extends AbstractDAO {

    /**
   * setter for the {@link PersistenceManager} property.
   * 
   * @see com.genia.toolbox.persistence.dao.AbstractDAO#setPersistenceManager(com.genia.toolbox.persistence.manager.PersistenceManager)
   */
    @Resource
    @Override
    public void setPersistenceManager(PersistenceManager persistenceManager) {
        super.setPersistenceManager(persistenceManager);
    }

    @Test
    public void testProduct1() throws PersistenceException {
        Criteria<CarOneToOneBidi> criteria = getCriteria(CarOneToOneBidi.class);
        CriteriaType<DriverOneToOneBidi> criteriaType = criteria.join(DriverOneToOneBidi.class);
        criteria.addRestriction(equals(getPropertyField(CarOneToOneBidi.NAME_DRIVER), getObjectField(criteriaType)));
        assertEquals(0, find(criteria).size());
    }

    @Test
    public void testProduct2() throws PersistenceException {
        Criteria<DriverOneToOneBidi> criteria = getCriteria(DriverOneToOneBidi.class);
        CriteriaType<CarOneToOneBidi> criteriaType = criteria.join(CarOneToOneBidi.class);
        criteria.addRestriction(equals(getPropertyField(DriverOneToOneBidi.NAME_CAR), getObjectField(criteriaType)));
        assertEquals(0, find(criteria).size());
    }
}
