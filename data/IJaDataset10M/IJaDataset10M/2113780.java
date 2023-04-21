package edu.ftn.ais.webapp.action;

import org.apache.struts2.ServletActionContext;
import org.springframework.mock.web.MockHttpServletRequest;
import com.opensymphony.xwork2.ActionSupport;
import edu.ftn.ais.model.Klijent;
import edu.ftn.ais.service.KlijentManager;

public class KlijentActionTest extends BaseActionTestCase {

    private KlijentAction action;

    @Override
    @SuppressWarnings("unchecked")
    protected void onSetUpBeforeTransaction() throws Exception {
        super.onSetUpBeforeTransaction();
        action = new KlijentAction();
        KlijentManager klijentManager = (KlijentManager) applicationContext.getBean("klijentManager");
        action.setKlijentManager(klijentManager);
        Klijent klijent = new Klijent();
        klijent.setAdresa("DwOdCxIvQyAvLvOyKjHsLpCwChErBiGhVbDvQkNcBsUmE");
        klijent.setMesto("GnWuEhTzRuEfEyMpGtWtDgUcJ");
        klijent.setNaziv("GeOsDzSzPaMeVhJeNoNbCqCaFiKgKtAbRmOsKoCfEtJvQ");
        klijentManager.save(klijent);
    }

    public void testSearch() throws Exception {
        assertEquals(action.list(), ActionSupport.SUCCESS);
        assertTrue(action.getKlijents().size() >= 1);
    }

    public void testEdit() throws Exception {
        log.debug("testing edit...");
        action.setIdk(-1L);
        assertNull(action.getKlijent());
        assertEquals("success", action.edit());
        assertNotNull(action.getKlijent());
        assertFalse(action.hasActionErrors());
    }

    public void testSave() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        ServletActionContext.setRequest(request);
        action.setIdk(-1L);
        assertEquals("success", action.edit());
        assertNotNull(action.getKlijent());
        Klijent klijent = action.getKlijent();
        klijent.setAdresa("TgYbCkEmFbQqLrGzZoOaYaVsThBiGsYrWrAkIyAkHuWbK");
        klijent.setMesto("QrRxXaUbMpYvQmRwWgZbExQwK");
        klijent.setNaziv("ZhSiOmRgJyUdOeIfHeGmEnWlDvNiWyNeHlHsGgOvGqTgD");
        action.setKlijent(klijent);
        assertEquals("input", action.save());
        assertFalse(action.hasActionErrors());
        assertFalse(action.hasFieldErrors());
        assertNotNull(request.getSession().getAttribute("messages"));
    }

    public void testRemove() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        ServletActionContext.setRequest(request);
        action.setDelete("");
        Klijent klijent = new Klijent();
        klijent.setIdk(-2L);
        action.setKlijent(klijent);
        assertEquals("success", action.delete());
        assertNotNull(request.getSession().getAttribute("messages"));
    }
}
