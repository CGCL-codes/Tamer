package net.sf.brightside.mobilestock.service.hibernate.main;

import java.util.Date;
import java.util.List;
import net.sf.brightside.mobilestock.metamodel.api.Account;
import net.sf.brightside.mobilestock.metamodel.api.Company;
import net.sf.brightside.mobilestock.metamodel.api.IPO;
import net.sf.brightside.mobilestock.metamodel.api.InsufficientShareAmountException;
import net.sf.brightside.mobilestock.metamodel.api.Ownership;
import net.sf.brightside.mobilestock.metamodel.api.Share;
import net.sf.brightside.mobilestock.service.api.exceptions.ShareAlreadyRegisteredException;
import net.sf.brightside.mobilestock.service.api.main.ICreateIPO;
import net.sf.brightside.mobilestock.service.api.servicelocator.IBeansProvider;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

public class CreateIPOImpl extends HibernateDaoSupport implements ICreateIPO {

    private IBeansProvider provider;

    public void setProvider(IBeansProvider provider) {
        this.provider = provider;
    }

    private Session getManager() {
        return getSessionFactory().getCurrentSession();
    }

    @SuppressWarnings("unused")
    private boolean shareExists(Share which, List<Ownership> where) {
        for (Ownership shareAndAmount : where) {
            if (shareAndAmount.getShare().equals(which)) return true;
        }
        return false;
    }

    private IPO createIPO(Share share, int amount, Company company) {
        IPO ipo = provider.getBean(IPO.class);
        ipo.setAmount(amount);
        ipo.setCompany(company);
        ipo.setShare(share);
        ipo.setDate(new Date(System.currentTimeMillis()));
        return ipo;
    }

    @Transactional
    public IPO create(Share share, int amount, Company company) throws ShareAlreadyRegisteredException, InsufficientShareAmountException {
        IPO ipo = createIPO(share, amount, company);
        getManager().update(company);
        getManager().saveOrUpdate(share);
        Criteria c = getManager().createCriteria(IPO.class);
        c.add(Restrictions.eq("company", company));
        c.add(Restrictions.eq("share", share));
        IPO fromDB = (IPO) c.uniqueResult();
        if (fromDB != null) {
            throw new ShareAlreadyRegisteredException();
        } else {
            getManager().saveOrUpdate(ipo);
        }
        Account account = (Account) getManager().createCriteria(Account.class).add(Restrictions.eq("shareholder", company)).uniqueResult();
        if (account == null) return null;
        Ownership ownership = provider.getBean(Ownership.class);
        ownership.setAmount(amount);
        ownership.setShare(share);
        account.addOwnership(ownership);
        getManager().persist(account);
        return ipo;
    }
}
