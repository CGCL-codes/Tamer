package com.ekeyman.securecreditlib.dao;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Repository;
import com.ekeyman.securecredit.dto.CardTransactionGrid;
import com.ekeyman.securecreditlib.domain.CardTransaction;
import com.ekeyman.securecreditlib.domain.CreditCard;
import com.ekeymanlib.dto.SearchFilter;

@Repository("cardTransactionDao")
public class CardTransactionDaoImpl extends AbstractDao implements CardTransactionDao {

    public void saveCardTransaction(long creditCardId, CardTransaction cardTransaction) {
        try {
            CreditCard creditCard = getHibernateTemplate().get(CreditCard.class, creditCardId);
            cardTransaction.setCreditCard(creditCard);
            getHibernateTemplate().saveOrUpdate(cardTransaction);
        } catch (Exception ex) {
            System.out.println("CardTransactionDaoImpl ex:" + ex.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public CardTransaction findCardTransaction(String vendorCode) {
        CardTransaction cardTransaction = null;
        String queryString = "from CardTransaction as ct " + "where ct.vendorCode = :vendorCode ";
        String[] paramNames = new String[1];
        paramNames[0] = "vendorCode";
        Object[] values = new Object[1];
        values[0] = vendorCode;
        List<CardTransaction> cardTransactions = getHibernateTemplate().findByNamedParam(queryString, paramNames, values);
        if (cardTransactions != null && !cardTransactions.isEmpty()) {
            cardTransaction = cardTransactions.get(0);
        }
        return cardTransaction;
    }

    public void deleteCardTransaction(long id) {
        CardTransaction cardTransaction = (CardTransaction) getHibernateTemplate().get(CardTransaction.class, id);
        if (cardTransaction != null) {
            getHibernateTemplate().delete(cardTransaction);
        }
    }

    public CardTransaction findCardTransaction(long id) {
        return (CardTransaction) getHibernateTemplate().get(CardTransaction.class, id);
    }

    public void updateCardTransaction(CardTransactionGrid cardTransactionGrid) {
        CardTransaction cardTransaction = (CardTransaction) getHibernateTemplate().get(CardTransaction.class, cardTransactionGrid.getId());
        if (cardTransaction != null) {
            cardTransaction.setTransactionNumber(cardTransactionGrid.getTransactionNumber());
            cardTransaction.setVendorCode(cardTransactionGrid.getVendorCode());
            cardTransaction.setTransactionDate(cardTransactionGrid.getTransactionDate());
            cardTransaction.setPostDate(cardTransactionGrid.getPostDate());
            cardTransaction.setAmount(new BigDecimal(cardTransactionGrid.getAmount()));
            getHibernateTemplate().update(cardTransaction);
        }
    }

    @SuppressWarnings("unchecked")
    public List<CardTransaction> listCreditCardTransactions(String creditCardId, List<SearchFilter> searchFilters, String sortIndex, String sortOrder, int firstResult, int maxResults) {
        if (creditCardId != null && !creditCardId.equalsIgnoreCase("")) {
            SearchFilter sf = new SearchFilter();
            sf.setField("creditCard.id");
            sf.setOp("eqIdLong");
            sf.setData(creditCardId);
            searchFilters.add(sf);
        }
        return findAll(searchFilters, CardTransaction.class, sortIndex, sortOrder, firstResult, maxResults);
    }

    @SuppressWarnings("unchecked")
    public List<CardTransaction> listCardTransactions(List<SearchFilter> searchFilters, String sortIndex, String sortOrder, int firstResult, int maxResults) {
        return findAll(searchFilters, CardTransaction.class, sortIndex, sortOrder, firstResult, maxResults);
    }

    public long getCardTransactionCount(List<SearchFilter> searchFilters) {
        return getCount(searchFilters, CardTransaction.class);
    }

    public long getCreditCardTransactionCount(String creditCardId, List<SearchFilter> searchFilters) {
        if (creditCardId != null && !creditCardId.equalsIgnoreCase("")) {
            SearchFilter sf = new SearchFilter();
            sf.setField("creditCard.id");
            sf.setOp("eqIdLong");
            sf.setData(creditCardId);
            searchFilters.add(sf);
        }
        return getCount(searchFilters, CardTransaction.class);
    }
}
