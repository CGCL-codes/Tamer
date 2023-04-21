package net.sf.brightside.belex.service.usage.hibernate;

import org.testng.annotations.Test;
import net.sf.brightside.belex.core.exception.BelexException;
import net.sf.brightside.belex.core.spring.AbstractSpringTest;
import net.sf.brightside.belex.metamodel.Stockholder;
import net.sf.brightside.belex.service.usage.api.LoginCommand;
import net.sf.brightside.belex.service.usage.api.StockholderRegistrationCommand;
import net.sf.brightside.belex.service.usage.exceptions.StockholderAuthenticationFailedException;

public class LoginCommandImplTest extends AbstractSpringTest {

    private LoginCommand<Stockholder> stockholderLoginUnderTest;

    private Stockholder helpStockholder;

    @Override
    protected LoginCommand<Stockholder> createUnderTest() {
        return (LoginCommand<Stockholder>) applicationContext.getBean(LoginCommand.class.getName());
    }

    private Stockholder provideStockholder() {
        return (Stockholder) applicationContext.getBean(Stockholder.class.getName());
    }

    @Override
    public void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        stockholderLoginUnderTest = createUnderTest();
        helpStockholder = provideStockholder();
        helpStockholder.setName("Nemanja");
        helpStockholder.setUsername("Darth Neman");
        helpStockholder.setPassword("nemanja");
    }

    @Test(expectedExceptions = StockholderAuthenticationFailedException.class)
    public void testLoging() throws BelexException {
        stockholderLoginUnderTest.setPassword(helpStockholder.getPassword());
        stockholderLoginUnderTest.setUsername(helpStockholder.getUsername());
        stockholderLoginUnderTest.execute();
    }
}
