package net.sf.brightside.mobilestock.service.hibernate;

import net.sf.brightside.mobilestock.core.spring.IBeansProvider;
import net.sf.brightside.mobilestock.metamodel.Shareholder;
import net.sf.brightside.mobilestock.service.ICreateAccount;
import net.sf.brightside.mobilestock.service.IRegisterShareholder;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

public class RegisterShareholderImpl extends HibernateDaoSupport implements IRegisterShareholder {

    private IBeansProvider provider;

    public void setProvider(IBeansProvider provider) {
        this.provider = provider;
    }

    private Session getManager() {
        return getSessionFactory().getCurrentSession();
    }

    @Transactional
    public Shareholder registerShareholder(Shareholder shareholder) {
        getManager().saveOrUpdate(shareholder);
        ICreateAccount accountCmd = provider.getBean(ICreateAccount.class);
        accountCmd.createAccount(shareholder);
        return shareholder;
    }
}
