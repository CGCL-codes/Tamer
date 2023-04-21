package net.sf.brightside.mobilestock.service.hibernate.integration;

import java.util.List;
import net.sf.brightside.mobilestock.core.spring.abstracttest.AbstractSpringTest;
import net.sf.brightside.mobilestock.metamodel.api.Share;
import net.sf.brightside.mobilestock.metamodel.api.Shareholder;
import net.sf.brightside.mobilestock.metamodel.api.User;
import net.sf.brightside.mobilestock.service.api.exceptions.NameAlreadyExistsException;
import net.sf.brightside.mobilestock.service.api.exceptions.NameEmptyException;
import net.sf.brightside.mobilestock.service.api.generic.IGetById;
import net.sf.brightside.mobilestock.service.api.generic.IGetByType;
import net.sf.brightside.mobilestock.service.api.main.IRegisterShareholder;
import net.sf.brightside.mobilestock.service.api.servicelocator.IBeansProvider;
import net.sf.brightside.mobilestock.service.hibernate.servicelocator.BeansProvider;
import org.hibernate.Session;
import org.testng.annotations.Test;

public class GetByIdIntegrationTest extends AbstractSpringTest<Session> {

    private IBeansProvider provider;

    private IRegisterShareholder registerShareholderCmd;

    private IGetById getByIdCommand;

    private IGetByType getbyTypeCmd;

    @Override
    public void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        provider = new BeansProvider();
        registerShareholderCmd = provider.getBean(IRegisterShareholder.class);
        getByIdCommand = provider.getBean(IGetById.class);
        getbyTypeCmd = provider.getBean(IGetByType.class);
    }

    @Test
    public void testGetById() throws NameEmptyException, NameAlreadyExistsException {
        Shareholder pureShareholder = provider.getBean(User.class);
        pureShareholder.setUserName("Username__");
        pureShareholder.setName("Name__");
        pureShareholder.setPassword("pass");
        Shareholder savedShareholder = registerShareholderCmd.registerShareholder(pureShareholder);
        Shareholder shareholder = getByIdCommand.get(savedShareholder.getId(), Shareholder.class);
        assertEquals(shareholder, savedShareholder);
        assertEquals(shareholder.getId(), savedShareholder.getId());
        List<Share> shares = getbyTypeCmd.getByType(Share.class);
        for (Share share : shares) {
            assertEquals(share, getByIdCommand.get(share.getId(), Share.class));
        }
    }
}
