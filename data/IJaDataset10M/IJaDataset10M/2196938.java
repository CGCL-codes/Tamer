package it.uniromadue.portaleuni.test.integration.manager;

import it.uniromadue.portaleuni.dto.Utenti;
import it.uniromadue.portaleuni.form.GestisciDatiAnagraficiForm;
import it.uniromadue.portaleuni.manager.Manager;
import it.uniromadue.portaleuni.service.ServiceLocator;
import junit.framework.TestCase;

public class GestisciDatiAnagraficiManagerTest extends TestCase {

    private Manager gestisciDatiAnagraficiManager;

    private GestisciDatiAnagraficiForm form;

    protected void setUp() throws Exception {
        super.setUp();
        gestisciDatiAnagraficiManager = ServiceLocator.getManager(ServiceLocator.getSpringContext(), "gestisciDatiAnagraficiManager");
        form = new GestisciDatiAnagraficiForm();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testLoadData() throws Exception {
        Utenti user = new Utenti(new Integer(1), "Mattia", "Colombo", "mattia", "colombo", "assistente");
        form.setUtente(user);
        form.setType(new Integer(2));
        form.setTipoUtente("studente");
        gestisciDatiAnagraficiManager.loadData(form);
        assertNotNull(form.getUtenti());
    }

    public void testSaveData() throws Exception {
        Utenti user = new Utenti(new Integer(1), "Mattia", "Colombo", "mattia", "colombo", "assistente");
        form.setUtente(user);
        form.setType(new Integer(1));
        gestisciDatiAnagraficiManager.saveData(form);
        assertNotNull(form.getEsito());
    }

    public void testDeleteData() throws Exception {
    }
}
