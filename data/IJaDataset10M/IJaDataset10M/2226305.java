package org.clin4j.framework.asepct;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.clin4j.framework.aspect.IDynamicProxy;
import org.clin4j.framework.aspect.Interceptor;
import org.clin4j.framework.ioc.ServiceProvider;
import org.clin4j.framework.service.ITransactionService;
import org.clin4j.framework.service.TransactionService;
import org.clin4j.framework.domain.dao.IAccountDao;
import org.clin4j.framework.domain.model.User;
import org.clin4j.framework.domain.model.User.Gender;
import org.clin4j.framework.util.GlobalKeys;

public class TransactionTest {

    private ServiceProvider service;

    private IAccountDao accountdao;

    private TransactionService ts;

    private Interceptor ti;

    private IDynamicProxy proxy;

    private ITransactionService tsproxy;

    @Before
    public void setUp() throws Exception {
        this.service = ServiceProvider.instantiate();
        this.accountdao = (IAccountDao) this.service.provide(GlobalKeys.ACCOUNT_SERVICE);
        this.ts = (TransactionService) this.service.provide(GlobalKeys.TX_SERVICE);
        this.ti = (Interceptor) this.service.provide(GlobalKeys.TX_INTERCEPTOR);
        this.proxy = (IDynamicProxy) this.service.provide(GlobalKeys.PROXY_ASPECT);
        this.tsproxy = this.proxy.create(ITransactionService.class, ts, ti);
        assertNotNull(this.service);
    }

    @Test
    public void testTransactionAnnotation() {
        User user = tsproxy.createUser("test@hotmail.com", "Smith", "smith", Gender.Male, "address");
        assertEquals(user.getAccount(), "test@hotmail.com");
    }

    @After
    public void tearDown() {
        User u = this.accountdao.findUserByAccount("test@hotmail.com");
        assertNotNull(u);
        this.tsproxy.removeUser(u);
    }
}
