package fi.mmmtike.tiira.invoker.http.server;

import java.util.List;
import javax.servlet.Servlet;
import fi.mmmtike.tiira.account.AccountJettyTest;
import fi.mmmtike.tiira.account.common.Account;
import fi.mmmtike.tiira.account.common.AccountException;
import fi.mmmtike.tiira.account.common.AccountService;
import fi.mmmtike.tiira.resolver.TiiraClasspathServiceResolver;

/**
 * Integaatiotesti AccountServicen testaamiseksi.
 */
public class TestTiiraHttpInvokerServlet extends AccountJettyTest {

    public void setUp() {
        this.setServlet(createServlet());
        super.setUp();
    }

    private Servlet createServlet() {
        TiiraHttpInvokerDispatcherServlet servlet = new TiiraHttpInvokerDispatcherServlet();
        TiiraClasspathServiceResolver resolver = new TiiraClasspathServiceResolver("fi.mmmtike.tiira.account.*ServiceImpl");
        servlet.setServiceResolver(resolver);
        return servlet;
    }

    public void testAccounts() {
        AccountService accountService = (AccountService) this.fetchAccountService();
        List<Account> accounts = accountService.getAccounts("foo");
        assertTrue(accounts.size() == 1);
        assertTrue(accounts.get(0).getName().equals(getTestName()));
    }

    protected String getTestName() {
        return "Ronnie James Dio";
    }

    public void testUnknownException() {
        AccountService accountService = (AccountService) this.fetchAccountService();
        try {
            accountService.testUnknownException();
            fail("no exception");
        } catch (Exception ex) {
            System.err.println("Client caught exception: " + ex.getMessage() + ". Check that stack trace is printed in server log.");
        }
    }

    public void testValidationException() {
        AccountService accountService = (AccountService) this.fetchAccountService();
        try {
            accountService.testValidationException();
            fail("no exception");
        } catch (AccountException ex) {
            System.err.println("Client caught AccountException. Check that stack trace is NOT printed in server log.");
        } catch (Exception ex) {
            fail("wrong exception type:" + ex);
        }
    }
}
