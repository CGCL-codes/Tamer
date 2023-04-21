package it.uniromadue.portaleuni.test.integration.dao;

import it.uniromadue.portaleuni.dao.PrenotazioneDAO;
import it.uniromadue.portaleuni.dto.Aule;
import it.uniromadue.portaleuni.dto.Esami;
import it.uniromadue.portaleuni.dto.Insegnamenti;
import it.uniromadue.portaleuni.dto.PeriodicitaCorsi;
import it.uniromadue.portaleuni.dto.Prenotazione;
import it.uniromadue.portaleuni.dto.Utenti;
import it.uniromadue.portaleuni.service.ServiceLocator;
import java.util.List;
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

/**
 * A data access object (DAO) providing persistence and search support for
 * Prenotazione entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see it.uniromadue.portaleuni.dao.TestPrenotazione
 * @author MyEclipse Persistence Tools
 */
public class TestPrenotazioneDAO extends TestCase {

    private static final Log log = LogFactory.getLog(TestPrenotazioneDAO.class);

    private Prenotazione prenotazione;

    private PrenotazioneDAO prenotazioneDao;

    private Utenti docente, studente;

    private Esami esame;

    private Insegnamenti insegnamento;

    private Aule scritto, orale, consegna;

    private PeriodicitaCorsi periodicita;

    protected void initDao() {
    }

    public TestPrenotazioneDAO() {
        try {
            setUp();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void setUp() throws Exception {
        ApplicationContext springContext = ServiceLocator.getSpringContext();
        prenotazioneDao = PrenotazioneDAO.getFromApplicationContext(springContext);
        insegnamento = new Insegnamenti(new Integer(9), periodicita, docente, "AM1", "Analisi Matematico 1", new Integer(6), new Integer(1), "informatica");
        scritto = new Aule(new Integer(4), new Integer(2), new Integer(1));
        orale = new Aule(new Integer(6), new Integer(2), new Integer(1));
        consegna = new Aule(new Integer(13), new Integer(2), new Integer(1));
        esame = new Esami(new Integer(3), scritto, consegna, orale, insegnamento, "prima", null, null, null, null);
        studente = new Utenti(new Integer(15), "studente1", "cfstudente1", "studente1", "studente1", "studente");
        prenotazione = new Prenotazione(new Integer(20), studente, esame);
    }

    protected void tearDown() throws Exception {
        prenotazioneDao = null;
        prenotazione = null;
    }

    public void testPrenotazioneDAO() {
        try {
            save();
            findById();
            findByExample();
            findByProperty();
            attachDirty();
            delete();
        } catch (RuntimeException re) {
            throw re;
        }
    }

    public void findById() {
        log.debug("getting Prenotazione instance with id: ");
        try {
            prenotazione = (Prenotazione) prenotazioneDao.findById(prenotazione.getId());
            assertNotNull(prenotazione);
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    public void findByExample() {
        log.debug("finding Prenotazione instance by example");
        try {
            List prenotazioni = prenotazioneDao.findByExample(prenotazione);
            assertNotNull(prenotazioni);
            assertTrue(prenotazioni.size() > 0);
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }

    public void findByProperty() {
        log.debug("finding Prenotazione instance with property: ");
        try {
            List prenotazioni = prenotazioneDao.findByProperty("id", prenotazione.getId());
            assertNotNull(prenotazioni);
            assertTrue(prenotazioni.size() > 0);
        } catch (RuntimeException re) {
            log.error("find by property name failed", re);
            throw re;
        }
    }

    public void save() {
        log.debug("saving Prenotazione instance");
        try {
            prenotazioneDao.save(prenotazione);
            assertNotNull(prenotazione);
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }

    public void attachDirty() {
        log.debug("attaching dirty Prenotazione instance");
        try {
            prenotazioneDao.attachDirty(prenotazione);
            prenotazione = (Prenotazione) prenotazioneDao.findById(prenotazione.getId());
            assertNotNull(prenotazione);
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void delete() {
        log.debug("deleting Prenotazione instance");
        try {
            prenotazioneDao.delete(prenotazione);
            prenotazione = (Prenotazione) prenotazioneDao.findById(prenotazione.getId());
            assertNull(prenotazione);
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
}
