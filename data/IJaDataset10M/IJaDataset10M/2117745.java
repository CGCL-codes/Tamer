package com.ctp.arquilliandemo.ex2.dao;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.ctp.arquilliandemo.ex2.domain.TradeTransaction;
import com.ctp.arquilliandemo.ex2.domain.TradeTransaction_;
import com.ctp.arquilliandemo.ex2.domain.User;

@Stateless
public class TradeTransactionDao {

    @PersistenceContext
    EntityManager entityManager;

    public List<TradeTransaction> getTransactions(User user) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<TradeTransaction> query = cb.createQuery(TradeTransaction.class);
        Root<TradeTransaction> root = query.from(TradeTransaction.class);
        query.where(cb.equal(root.get(TradeTransaction_.user), user)).orderBy(cb.desc(root.get(TradeTransaction_.timestamp)));
        return entityManager.createQuery(query).getResultList();
    }

    public TradeTransaction save(TradeTransaction tradeTransaction) {
        if (tradeTransaction.getId() == null) {
            entityManager.persist(tradeTransaction);
        } else {
            tradeTransaction = entityManager.merge(tradeTransaction);
        }
        return tradeTransaction;
    }
}
