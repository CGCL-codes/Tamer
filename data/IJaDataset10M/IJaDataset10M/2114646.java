package com.cartagena.financo.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import br.com.caelum.vraptor.ioc.Component;
import com.cartagena.financo.model.Category;
import com.cartagena.financo.model.Transaction;
import com.cartagena.financo.repository.TransactionRepository;

@Component
public class TransactionDao extends BaseDao<Transaction> implements TransactionRepository {

    TransactionDao(Session session) {
        super(session);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Transaction> listByPeriod(Date begin, Date end) {
        Criteria criteria = this.session.createCriteria(Transaction.class);
        criteria.add(Restrictions.between("date", begin, end));
        return criteria.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Transaction> listByCategory(Category category) {
        Criteria criteria = this.session.createCriteria(Transaction.class);
        criteria.add(Restrictions.like("category", category));
        return criteria.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Transaction> listByCategoryAndPeriod(Category category, Date begin, Date end) {
        Criteria criteria = this.session.createCriteria(Transaction.class);
        criteria.add(Restrictions.between("date", begin, end));
        if (category != null) {
            criteria.add(Restrictions.eq("category", category));
        }
        return criteria.list();
    }

    @Override
    public BigDecimal getPeriodTotal(Date begin, Date end, boolean includes, Category... categories) {
        Criteria criteria = this.session.createCriteria(Transaction.class);
        criteria.add(Restrictions.between("date", begin, end));
        criteria.setProjection(Projections.sum("value").as("total"));
        for (Category cat : categories) {
            Criterion restriction = includes ? Restrictions.eq("category", cat) : Restrictions.not(Restrictions.eq("category", cat));
            criteria.add(restriction);
        }
        return (BigDecimal) criteria.uniqueResult();
    }
}
