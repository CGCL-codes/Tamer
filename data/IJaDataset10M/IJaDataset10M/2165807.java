package ftn.edu.ais.webapp.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import ftn.edu.ais.service.ZanimanjeManager;
import ftn.edu.ais.model.Zanimanje;
import ftn.edu.ais.webapp.action.BaseActionTestCase;
import org.springframework.mock.web.MockHttpServletRequest;

public class ZanimanjeActionTest extends BaseActionTestCase {

    private ZanimanjeAction action;

    @Override
    @SuppressWarnings("unchecked")
    protected void onSetUpBeforeTransaction() throws Exception {
        super.onSetUpBeforeTransaction();
        action = new ZanimanjeAction();
        ZanimanjeManager zanimanjeManager = (ZanimanjeManager) applicationContext.getBean("zanimanjeManager");
        action.setZanimanjeManager(zanimanjeManager);
        Zanimanje zanimanje = new Zanimanje();
        zanimanje.setNazzan("NaOmJlOnYsFrXgUzFgCrEwSoFiLwLa");
        zanimanjeManager.save(zanimanje);
    }

    public void testSearch() throws Exception {
        assertEquals(action.list(), ActionSupport.SUCCESS);
        assertTrue(action.getZanimanjes().size() >= 1);
    }

    public void testEdit() throws Exception {
        log.debug("testing edit...");
        action.setIdzan(-1L);
        assertNull(action.getZanimanje());
        assertEquals("success", action.edit());
        assertNotNull(action.getZanimanje());
        assertFalse(action.hasActionErrors());
    }

    public void testSave() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        ServletActionContext.setRequest(request);
        action.setIdzan(-1L);
        assertEquals("success", action.edit());
        assertNotNull(action.getZanimanje());
        Zanimanje zanimanje = action.getZanimanje();
        zanimanje.setNazzan("KnZaUaZzSyRuShQoHvVcEgDsNjYnDx");
        action.setZanimanje(zanimanje);
        assertEquals("input", action.save());
        assertFalse(action.hasActionErrors());
        assertFalse(action.hasFieldErrors());
        assertNotNull(request.getSession().getAttribute("messages"));
    }

    public void testRemove() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        ServletActionContext.setRequest(request);
        action.setDelete("");
        Zanimanje zanimanje = new Zanimanje();
        zanimanje.setIdzan(-2L);
        action.setZanimanje(zanimanje);
        assertEquals("success", action.delete());
        assertNotNull(request.getSession().getAttribute("messages"));
    }
}
