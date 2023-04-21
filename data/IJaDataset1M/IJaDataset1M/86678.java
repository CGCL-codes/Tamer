package ftn.edu.ais.dao;

import ftn.edu.ais.dao.BaseDaoTestCase;
import ftn.edu.ais.model.Zaposleni;
import org.springframework.dao.DataAccessException;

public class ZaposleniDaoTest extends BaseDaoTestCase {

    private ZaposleniDao zaposleniDao;

    private ZanimanjeDao zanimanjeDao;

    public void setZaposleniDao(ZaposleniDao zaposleniDao) {
        this.zaposleniDao = zaposleniDao;
    }

    public void setZanimanjeDao(ZanimanjeDao zanimanjeDao) {
        this.zanimanjeDao = zanimanjeDao;
    }

    public void testAddAndRemoveZaposleni() throws Exception {
        Zaposleni zaposleni = new Zaposleni();
        zaposleni.setZanimanje(zanimanjeDao.get(-1L));
        zaposleni.setAdrz("LcPcUsXfYwFrZtDgWkXwNrFsLaWeRgPvHvNrMaFwIjKiWhOwZk");
        zaposleni.setGradz("WeSzGuLaZuWrDpAmBlNv");
        zaposleni.setImez("JhMbJxAtXzClOaFiWuRj");
        zaposleni.setJmbz("DnAeOhZxJlLxL");
        zaposleni.setPrzz("WgGoNoHeYoXiDkRpOeZr");
        zaposleni.setZipz("JaXbFsVmIp");
        log.debug("adding zaposleni...");
        zaposleni = zaposleniDao.save(zaposleni);
        zaposleni = zaposleniDao.get(zaposleni.getIdz());
        assertNotNull(zaposleni.getIdz());
        log.debug("removing zaposleni...");
        zaposleniDao.remove(zaposleni.getIdz());
        try {
            zaposleniDao.get(zaposleni.getIdz());
            fail("Zaposleni found in database");
        } catch (DataAccessException e) {
            log.debug("Expected exception: " + e.getMessage());
            assertNotNull(e);
        }
    }
}
