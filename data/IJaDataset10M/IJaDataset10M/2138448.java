package test.org.magicbox.validator;

import junit.framework.TestCase;
import org.magicbox.domain.Credenziali;
import org.magicbox.validator.CredentialsValidator;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.context.support.XmlWebApplicationContext;
import test.org.magicbox.SpringFactory;
import test.org.magicbox.dbunit.DBCredentials;

public class CredentialsValidatorTest extends TestCase {

    public void setUp() throws Exception {
        _ctx = SpringFactory.getXmlWebApplicationContext();
        _validator = (CredentialsValidator) _ctx.getBean("magicbox.credentialsValidator");
    }

    public void testEmpty() {
        Credenziali creds = new Credenziali();
        Errors errors = new BindException(creds, "command");
        _validator.validate(creds, errors);
        assertTrue(errors.hasErrors());
        assertTrue(errors.hasFieldErrors("oldPassword"));
        assertTrue(errors.hasFieldErrors("username"));
        assertTrue(errors.getErrorCount() == 2);
    }

    public void testWrongPassword() {
        DBCredentials dbCredentials = new DBCredentials();
        dbCredentials.preparaDb();
        Credenziali creds = new Credenziali();
        creds.setUsername("pippo");
        creds.setOldPassword("giulio");
        creds.setPassword("");
        creds.setConfermaPassword("");
        Errors errors = new BindException(creds, "command");
        _validator.validate(creds, errors);
        assertTrue(errors.hasErrors());
        assertTrue(errors.hasFieldErrors("oldPassword"));
        assertTrue(errors.getErrorCount() == 1);
        dbCredentials.pulisciDb();
        dbCredentials = null;
    }

    public void testWrongusername() {
        DBCredentials dbCredentials = new DBCredentials();
        dbCredentials.preparaDb();
        Credenziali creds = new Credenziali();
        creds.setUsername("gargoile");
        creds.setOldPassword("giulio");
        creds.setPassword("");
        creds.setConfermaPassword("");
        Errors errors = new BindException(creds, "command");
        _validator.validate(creds, errors);
        assertTrue(errors.hasErrors());
        assertTrue(errors.hasFieldErrors("oldPassword"));
        assertTrue(errors.getErrorCount() == 1);
        dbCredentials.pulisciDb();
    }

    public void testCorrectPassword() {
        DBCredentials dbCredentials = new DBCredentials();
        dbCredentials.preparaDb();
        Credenziali creds = new Credenziali();
        creds.setUsername("pippo");
        creds.setOldPassword("pippo");
        creds.setPassword("");
        creds.setConfermaPassword("");
        Errors errors = new BindException(creds, "command");
        _validator.validate(creds, errors);
        assertTrue(!errors.hasErrors());
        assertTrue(errors.getErrorCount() == 0);
        dbCredentials.pulisciDb();
        dbCredentials = null;
    }

    public void tearDown() throws Exception {
    }

    private CredentialsValidator _validator;

    private XmlWebApplicationContext _ctx;
}
