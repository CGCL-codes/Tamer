package test.org.magicbox.service;

import junit.framework.TestCase;
import org.magicbox.domain.PageImpl;
import org.magicbox.domain.PageLightImpl;
import org.magicbox.domain.PageLight;
import org.magicbox.domain.Page;
import org.magicbox.service.PagesService;
import org.springframework.context.ApplicationContext;
import test.org.magicbox.SpringFactory;
import test.org.magicbox.dbunit.DBCentro;
import test.org.magicbox.dbunit.DBPages;

public class PageServiceTest extends TestCase {

    public void setUp() throws Exception {
        _ctx = SpringFactory.getXmlWebApplicationContext();
        _service = (PagesService) _ctx.getBean("magicbox.pagesService");
    }

    public void testGetPages() {
        DBCentro dbCentro = new DBCentro();
        dbCentro.preparaDb();
        DBPages db = new DBPages();
        db.preparaDb();
        assertTrue(_service.getPages().size() == 10);
        db.pulisciDb();
        dbCentro.pulisciDb();
        db = null;
        dbCentro = null;
    }

    public void testGetPagesAdmin() {
        DBCentro dbCentro = new DBCentro();
        dbCentro.preparaDb();
        DBPages db = new DBPages();
        db.preparaDb();
        assertTrue(_service.getPages().size() == 10);
        assertEquals(_service.getPagesAdmin().size(), 3);
        db.pulisciDb();
        dbCentro.pulisciDb();
        db = null;
        dbCentro = null;
    }

    public void testGetPagesCentro() {
        DBCentro dbCentro = new DBCentro();
        dbCentro.preparaDb();
        DBPages db = new DBPages();
        db.preparaDb();
        assertTrue(_service.getPagesCentro(46).size() == 7);
        db.pulisciDb();
        dbCentro.pulisciDb();
        db = null;
        dbCentro = null;
    }

    public void testGetPage() {
        DBCentro dbCentro = new DBCentro();
        dbCentro.preparaDb();
        DBPages db = new DBPages();
        PageLight pageLight = new PageLightImpl(0, "prova");
        Page pagina = new PageImpl("blah blah", false, 1, 46, pageLight);
        assertTrue(_service.getPages().size() == 0);
        Long id = _service.savePage(pagina);
        assertNotNull(_service.getPage(id));
        db.pulisciDb();
        dbCentro.pulisciDb();
        db = null;
        dbCentro = null;
    }

    public void testInserisciPage() {
        DBCentro dbCentro = new DBCentro();
        dbCentro.preparaDb();
        DBPages db = new DBPages();
        PageLight pageLight = new PageLightImpl(0, "prova");
        Page pagina = new PageImpl("blah blah", false, 1, 46, pageLight);
        assertTrue(_service.getPages().size() == 0);
        _service.savePage(pagina);
        assertTrue(_service.getPages().size() == 1);
        db.pulisciDb();
        dbCentro.preparaDb();
        db = null;
        dbCentro = null;
    }

    public void testCancellaPage() {
        DBCentro dbCentro = new DBCentro();
        dbCentro.preparaDb();
        DBPages db = new DBPages();
        PageLight pageLight = new PageLightImpl(0, "prova");
        Page pagina = new PageImpl("blah blah", false, 1, 46, pageLight);
        assertTrue(_service.getPages().size() == 0);
        Long id = _service.savePage(pagina);
        assertTrue(_service.getPages().size() == 1);
        assertEquals(_service.cancellaPage(id), true);
        assertTrue(_service.getPages().size() == 0);
        db.pulisciDb();
        dbCentro.preparaDb();
        db = null;
        dbCentro = null;
    }

    public void testAggiornaPage() {
        DBCentro dbCentro = new DBCentro();
        dbCentro.preparaDb();
        DBPages db = new DBPages();
        PageLight pageLight = new PageLightImpl(0, "prova");
        Page pagina = new PageImpl("blah blah", false, 1, 46, pageLight);
        assertTrue(_service.getPages().size() == 0);
        Long id = _service.savePage(pagina);
        assertTrue(_service.getPages().size() == 1);
        PageLight pageLightUpdate = new PageLightImpl(id, "test");
        Page paginaUpdate = new PageImpl("mumble mumble", false, 2, 46, pageLightUpdate);
        assertTrue(_service.savePage(paginaUpdate) == 1);
        Page recuperata = _service.getPage(id);
        assertEquals(recuperata.getId(), paginaUpdate.getId());
        assertEquals(recuperata.getContent(), paginaUpdate.getContent());
        assertEquals(recuperata.getIdCentro(), paginaUpdate.getIdCentro());
        assertEquals(recuperata.isAdmin(), paginaUpdate.isAdmin());
        assertEquals(recuperata.getTitolo(), paginaUpdate.getTitolo());
        assertEquals(recuperata.getOrdine(), paginaUpdate.getOrdine());
        db.pulisciDb();
        dbCentro.preparaDb();
        db = null;
        dbCentro = null;
    }

    public void tearDown() throws Exception {
    }

    private PagesService _service;

    private ApplicationContext _ctx;
}
