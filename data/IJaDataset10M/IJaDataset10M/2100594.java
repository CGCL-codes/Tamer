package org.fao.fenix.hibernate.datastore.dao;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

/**
 * Implementation of the CarPartsInventory using the HibernateTemplate API. This class does not need an
 * 
 * @Repository annotation as opposed to the {@link PlainHibernateCarPartsInventoryImpl} for exception translation since
 *             the HibernateTemplate already takes care of this for you.
 * 
 * @author Alef Arendsen
 * @since 2.0.4.
 */
public abstract class BaseDaoTest extends AbstractTransactionalDataSourceSpringContextTests {

    @Override
    protected String[] getConfigLocations() {
        return new String[] { "applicationContext-test.xml" };
    }

    protected Gaul0FeatureDao getGaul0FeatureDao() {
        return (Gaul0FeatureDao) applicationContext.getBean("gaul0FeatureDao");
    }
}
