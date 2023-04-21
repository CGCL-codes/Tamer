package org.fao.fenix.persistence.security;

import org.fao.fenix.domain.security.FenixDoubleUser;
import org.fao.fenix.domain.security.FenixSecuredUser;
import org.fao.fenix.persistence.BaseDaoTest;

public class FenixSecuredUserDaoTest extends BaseDaoTest {

    private FenixSecuredUserDao fenixSecuredUserDao;

    private FenixDoubleUserDao fenixDoubleUserDao;

    public void testFindById() {
        FenixDoubleUser fenixDoubleUser = dog.getFenixDoubleUser();
        fenixDoubleUserDao.save(fenixDoubleUser);
        fenixSecuredUserDao.findById(fenixDoubleUser.getFenixSecuredUser().getId());
        assertEquals(1, fenixSecuredUserDao.findAll().size());
    }

    public void testFindByName() {
        FenixDoubleUser fenixDoubleUser = dog.getFenixDoubleUser();
        fenixDoubleUserDao.save(fenixDoubleUser);
        FenixSecuredUser suser = fenixSecuredUserDao.findByName(fenixDoubleUser.getFenixSecuredUser().getName());
        assertTrue(suser instanceof FenixSecuredUser);
        assertTrue(fenixDoubleUser.getFenixSecuredUser().getName().equals(suser.getName()));
    }

    public void setFenixSecuredUserDao(FenixSecuredUserDao fenixSecuredUserDao) {
        this.fenixSecuredUserDao = fenixSecuredUserDao;
    }

    public void setFenixDoubleUserDao(FenixDoubleUserDao fenixDoubleUserDao) {
        this.fenixDoubleUserDao = fenixDoubleUserDao;
    }
}
