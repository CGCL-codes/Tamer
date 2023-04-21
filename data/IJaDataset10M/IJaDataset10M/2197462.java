package it.uniromadue.portaleuni.test.integration.action;

import it.uniromadue.portaleuni.action.ForumSezioniAction;
import it.uniromadue.portaleuni.dao.PostDAO;
import it.uniromadue.portaleuni.dto.Post;
import it.uniromadue.portaleuni.dto.Utenti;
import it.uniromadue.portaleuni.form.ForumSezioniForm;
import it.uniromadue.portaleuni.manager.SessionManager;
import it.uniromadue.portaleuni.service.ServiceLocator;
import it.uniromadue.portaleuni.session.SessionAttributes;
import junit.framework.TestCase;
import it.uniromadue.portaleuni.test.integration.mock.*;

public class ForumSezioniActionTest extends TestCase {

    private ForumSezioniAction forumSezioniAction;

    private MockHttpServletRequest request;

    private MockHttpServletResponse response;

    private MockActionMapping mapping;

    private ForumSezioniForm form;

    private PostDAO postDAO;

    protected void setUp() throws Exception {
        super.setUp();
        forumSezioniAction = new ForumSezioniAction();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        mapping = new MockActionMapping();
        form = new ForumSezioniForm();
        postDAO = PostDAO.getFromApplicationContext(ServiceLocator.getSpringContext());
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testInit() throws Exception {
        Utenti user = new Utenti(new Integer(1), "Mattia", "Colombo", "mattia", "colombo", "assistente");
        SessionManager.addUser(request.getSession(), user);
        SessionManager.addAttribute(request.getSession(), SessionAttributes.SEZIONE_FORUM, "analisiMatematica");
        forumSezioniAction.init(mapping, form, request, response);
        assertEquals("result", mapping.getForward());
        assertNotNull(form.getSezione());
        assertNotNull(form.getPostIt());
        assertNotNull(form.getTitolo());
    }

    public void testInserisciPost() throws Exception {
        Utenti user = new Utenti(new Integer(1), "Mattia", "Colombo", "mattia", "colombo", "assistente");
        SessionManager.addUser(request.getSession(), user);
        SessionManager.addAttribute(request.getSession(), SessionAttributes.SEZIONE_FORUM, "analisiMatematica");
        form.setNomeDiscussione("Flex e Bison");
        form.setPostIt("");
        form.setTitolo("test");
        forumSezioniAction.inserisciPost(mapping, form, request, response);
        assertEquals("ricarica", mapping.getForward());
        assertNotNull(form.getUtenteLoggato());
    }

    public void testCancellaPost() throws Exception {
        Utenti user = new Utenti(new Integer(1), "Mattia", "Colombo", "mattia", "colombo", "assistente");
        SessionManager.addUser(request.getSession(), user);
        SessionManager.addAttribute(request.getSession(), SessionAttributes.SEZIONE_FORUM, "analisiMatematica");
        form.setIdPost(((Post) postDAO.findByProperty("oggetto", "test").get(0)).getIdPost().intValue());
        forumSezioniAction.cancellaPost(mapping, form, request, response);
        assertEquals("ricarica", mapping.getForward());
    }
}
