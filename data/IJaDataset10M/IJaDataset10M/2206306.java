package org.regola.dao.jpa;

public class HibernateJpaGenericDaoTest extends AbstractJpaGenericDaoTest {

    @Override
    public String getConfigPath() {
        return "applicationContext-hibernate.xml";
    }
}
