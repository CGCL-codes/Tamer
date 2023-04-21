package org.demis.elf.bank;

import java.util.Collection;
import org.demis.elf.*;
import org.hibernate.criterion.*;
import org.hibernate.*;

public class BankDAOHibernateImpl implements BankDAO {

    private static final org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory.getLog(BankDAOHibernateImpl.class);

    public Bank findById(java.lang.String bankId) {
        Bank result = null;
        try {
            result = (Bank) getCurrentSession().get(Bank.class, bankId);
        } catch (HibernateException he) {
        }
        return result;
    }

    public Collection<Bank> findByExemple(final Bank bank) {
        Criteria criteria = getCriteria(bank);
        Collection result = null;
        try {
            result = criteria.list();
        } catch (HibernateException he) {
        }
        return (Collection<Bank>) result;
    }

    public int findCount(final Bank bank) {
        return 0;
    }

    public void save(Bank bank) {
        getCurrentSession().saveOrUpdate(bank);
    }

    public void saveAll(final Collection<Bank> banks) {
        for (Bank bank : banks) {
            getCurrentSession().saveOrUpdate(bank);
        }
    }

    public void delete(Bank bank) {
        getCurrentSession().delete(bank);
    }

    public void deleteAll(final Collection<Bank> banks) {
        for (Bank bank : banks) {
            getCurrentSession().delete(bank);
        }
    }

    private Criteria getCriteria(final Bank bank) {
        Criteria criteria = getCurrentSession().createCriteria(Bank.class);
        if (bank != null) {
            criteria.add(getExample(bank));
        }
        return criteria;
    }

    private Example getExample(final Bank bank) {
        Example example = Example.create(bank);
        return example;
    }

    private Session getCurrentSession() {
        return HibernateManager.getInstance().getCurrentSession();
    }
}
