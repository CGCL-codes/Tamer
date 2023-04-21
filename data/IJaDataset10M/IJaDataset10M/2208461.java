package it.uniromadue.portaleuni.test.integration.manager;

import it.uniromadue.portaleuni.dto.Utenti;
import it.uniromadue.portaleuni.form.AssegnaInsegnamentoForm;
import it.uniromadue.portaleuni.form.OrarioLezioniForm;
import it.uniromadue.portaleuni.manager.Manager;
import it.uniromadue.portaleuni.service.ServiceLocator;
import junit.framework.TestCase;

public class OrarioLezioniManagerTest extends TestCase {

    private Manager orarioLezioniManager;

    private OrarioLezioniForm form;

    protected void setUp() throws Exception {
        super.setUp();
        orarioLezioniManager = ServiceLocator.getManager(ServiceLocator.getSpringContext(), "orarioLezioniManager");
        form = new OrarioLezioniForm();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testLoadData() throws Exception {
        Utenti user = new Utenti(new Integer(1), "Mattia", "Colombo", "mattia", "colombo", "assistente");
        form.setUtente(user);
        orarioLezioniManager.loadData(form);
        assertNotNull(form.getListaInsegnamenti());
        assertNotNull(form.getListaAule());
    }

    public void testSaveData() throws Exception {
        Utenti user = new Utenti(new Integer(1), "Mattia", "Colombo", "mattia", "colombo", "assistente");
        form.setUtente(user);
        form.setIdInsegnamento("1");
        form.setIdAula("1");
        form.setGiorno("lundi");
        form.setAnno("4");
        form.setOra("9");
        orarioLezioniManager.saveData(form);
    }

    public void testDeleteData() throws Exception {
        Utenti user = new Utenti(new Integer(1), "Mattia", "Colombo", "mattia", "colombo", "assistente");
        form.setUtente(user);
        form.setIdInsegnamento("1");
        form.setIdAula("1");
        form.setGiorno("lundi");
        form.setAnno("4");
        form.setOra("9");
        orarioLezioniManager.deleteData(form);
    }
}
