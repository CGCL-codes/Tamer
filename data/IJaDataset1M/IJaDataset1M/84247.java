package org.vardb.users.dao;

import org.fest.assertions.Assertions;
import org.junit.Test;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.orm.hibernate.HibernateUnitils;
import org.unitils.spring.annotation.SpringBean;
import org.vardb.AbstractDaoTest;

public class TestUserDaoImpl extends AbstractDaoTest {

    @SpringBean("userDao")
    private IUserDao userDao;

    @Test
    @DataSet("user.xml")
    public void testSearchByLastName() {
        CUser user = this.userDao.findUserByUsername("cnh1");
        Assertions.assertThat(user.getLastname()).isEqualTo("Hayes");
    }

    @Test
    public void testMappingToDatabase() {
        HibernateUnitils.assertMappingWithDatabaseConsistent();
    }
}
