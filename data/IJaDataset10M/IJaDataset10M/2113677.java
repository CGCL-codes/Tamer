package org.wapps.persistence.impl;

/**
 *
 * @author hupe1980 at users.sourceforge.net
 *
 */
public class HibernateGenericJpaDaoTest extends BaseGenericJpaDaoTest {

    protected String[] getConfigLocations() {
        return new String[] { "applicationContext-hibernate.xml" };
    }
}
