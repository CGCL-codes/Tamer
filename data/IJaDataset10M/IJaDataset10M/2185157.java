package org.fao.fenix.persistence.commodity;

import java.util.Iterator;
import java.util.List;
import org.fao.fenix.domain.commodity.HS;
import org.fao.fenix.persistence.BaseDaoTest;

public class HSDaoTest extends BaseDaoTest {

    HSDao hsDao;

    public void testConfiguration() {
        assertNotNull(hsDao);
    }

    public void _testFindAllLevel0() {
        assertTrue(hsDao.findAll().size() == 0);
        hsDao.restore();
        assertTrue(hsDao.findAll().size() != 0);
        List list = hsDao.findAllLevel0();
        for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
            HS name = (HS) iterator.next();
            printHS(name);
        }
    }

    public void _testFindAllLevel1() {
        assertTrue(hsDao.findAll().size() == 0);
        hsDao.restore();
        assertTrue(hsDao.findAll().size() != 0);
        List list = hsDao.findAllLevel1("08");
        for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
            HS hs = (HS) iterator.next();
            printHS(hs);
        }
    }

    public void _testFindAllLevel2() {
        assertTrue(hsDao.findAll().size() == 0);
        hsDao.restore();
        assertTrue(hsDao.findAll().size() != 0);
        List list = hsDao.findAllLevel2("0203");
        for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
            HS hs = (HS) iterator.next();
            printHS(hs);
        }
    }

    public void printHS(HS hs) {
        System.out.println("CODE: " + hs.getCode() + ", DESCRIPTION: " + hs.getDescription());
    }

    public void setHsDao(HSDao hsDao) {
        this.hsDao = hsDao;
    }
}
