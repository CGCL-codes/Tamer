package mobilestock.server.service.hibernate;

import java.util.LinkedList;
import java.util.List;
import mobilestock.server.core.spring.AbstractSpringTest;
import mobilestock.server.core.spring.BeansProvider;
import mobilestock.server.core.spring.IBeansProvider;
import mobilestock.server.metamodel.Account;
import mobilestock.server.metamodel.Share;
import mobilestock.server.metamodel.User;
import mobilestock.server.service.api.hibernate.IAddSharesToUserAccount;
import mobilestock.server.service.api.hibernate.ICreateShare;
import mobilestock.server.service.api.hibernate.ICreateUser;
import mobilestock.server.service.api.hibernate.IDeleteSharesFromUserAccount;
import mobilestock.server.service.api.hibernate.IGetUser;
import org.testng.annotations.Test;

public class DeleteSharesFromUserAccountIntegrationTest extends AbstractSpringTest {

    private IBeansProvider beansProvider;

    private IDeleteSharesFromUserAccount deleteSharesFromUserAccountUnderTest;

    private ICreateUser createUserCommand;

    private ICreateShare createSharecommand;

    private IGetUser getUser;

    private IAddSharesToUserAccount addSharesToUserAccountCommand;

    protected IDeleteSharesFromUserAccount createUnderTest() {
        return beansProvider.getBean(IDeleteSharesFromUserAccount.class);
    }

    @Override
    public void onSetUpInTransaction() throws Exception {
        beansProvider = new BeansProvider();
        super.onSetUpInTransaction();
        deleteSharesFromUserAccountUnderTest = createUnderTest();
        createUserCommand = beansProvider.getBean(ICreateUser.class);
        createSharecommand = beansProvider.getBean(ICreateShare.class);
        getUser = beansProvider.getBean(IGetUser.class);
        addSharesToUserAccountCommand = beansProvider.getBean(IAddSharesToUserAccount.class);
    }

    @Test
    public void testDeleteShares() {
        String username = "testdsfua";
        User user = createUserCommand.creteUser("Test DSFUA", username, "test1234");
        List<Share> shares = new LinkedList<Share>();
        shares.add(createSharecommand.createShare("testSym", "Test name", 321.00));
        shares.add(createSharecommand.createShare("testSym2", "Test name2", 321.00));
        addSharesToUserAccountCommand.addSharesToAccoun(shares, user);
        assertEquals(shares, getUser.getUser(username).getAccount().getShares());
        Account account = deleteSharesFromUserAccountUnderTest.delete(shares, user);
        assertEquals(0, getUser.getUser(username).getAccount().getShares().size());
        assertEquals(account.getShares().size(), getUser.getUser(username).getAccount().getShares().size());
    }
}
