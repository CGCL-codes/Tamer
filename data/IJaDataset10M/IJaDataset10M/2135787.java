package net.sf.brightside.mobilestock.service.hibernate.main;

import net.sf.brightside.mobilestock.metamodel.api.Shareholder;
import net.sf.brightside.mobilestock.service.api.ICreateAccount;
import net.sf.brightside.mobilestock.service.api.IGetShareholderByUserName;
import net.sf.brightside.mobilestock.service.api.exceptions.NameAlreadyExistsException;
import net.sf.brightside.mobilestock.service.api.exceptions.NameEmptyException;
import net.sf.brightside.mobilestock.service.api.main.IRegisterShareholder;
import net.sf.brightside.mobilestock.service.api.servicelocator.IBeansProvider;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

public class RegisterShareholderImpl extends HibernateDaoSupport implements IRegisterShareholder {

    private IBeansProvider provider;

    private IGetShareholderByUserName getShareholderByNameCmd;

    public IGetShareholderByUserName getGetShareholderByNameCmd() {
        return getShareholderByNameCmd;
    }

    public void setGetShareholderByNameCmd(IGetShareholderByUserName getShareholderByNameCmd) {
        this.getShareholderByNameCmd = getShareholderByNameCmd;
    }

    public void setProvider(IBeansProvider provider) {
        this.provider = provider;
    }

    private Session getManager() {
        return getSessionFactory().getCurrentSession();
    }

    @Transactional
    public Shareholder registerShareholder(Shareholder shareholder) throws NameEmptyException, NameAlreadyExistsException {
        if (shareholder.getUserName() == null || shareholder.getUserName().trim().equals("")) {
            throw new NameEmptyException();
        }
        if (getShareholderByNameCmd.getByUserName(shareholder.getUserName()) != null) {
            throw new NameAlreadyExistsException();
        }
        getManager().saveOrUpdate(shareholder);
        ICreateAccount accountCmd = provider.getBean(ICreateAccount.class);
        accountCmd.createAccount(shareholder);
        return shareholder;
    }
}
