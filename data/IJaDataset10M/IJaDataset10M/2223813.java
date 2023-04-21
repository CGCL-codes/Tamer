package uk.ac.osswatch.simal.wicket;

import org.apache.wicket.Page;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.authentication.LoginPage;
import uk.ac.osswatch.simal.wicket.authentication.SimalSession;

public class SimalTester extends WicketTester {

    private SimalTester() throws SimalRepositoryException {
        super(new UserApplication());
    }

    /**
   * Get the tester for this run.
   * 
   * @return
   * @throws SimalRepositoryException 
   */
    public static SimalTester get() throws SimalRepositoryException {
        return new SimalTester();
    }

    /**
   * Log the test user in.
   * 
   * @throws SimalRepositoryException
   */
    public void login() throws SimalRepositoryException {
        Page page = getLastRenderedPage();
        if (page instanceof LoginPage) {
            FormTester formTester = newFormTester("loginForm");
            formTester.setValue("username", "simal");
            formTester.setValue("password", "simal");
            formTester.submit("login");
        }
        SimalSession.get().authenticate("simal", "simal");
    }

    /**
   * If the test user is logged in then log them out.
   * 
   * @throws SimalRepositoryException
   */
    public void logout() throws SimalRepositoryException {
        SimalSession.get().logout();
    }
}
