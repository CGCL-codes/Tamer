package com.ekeyman.securecreditlib.business;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.ekeyman.securecredit.dto.CreditCardGrid;
import com.ekeyman.securecreditlib.dao.CreditCardDao;
import com.ekeyman.securecreditlib.domain.CreditCard;
import com.ekeymanlib.dto.SearchFilter;

@Service("creditCardBO")
public class CreditCardBO {

    @Autowired
    private CreditCardDao creditCardDao;

    public void saveCreditCard(CreditCard creditCard) {
        doSaveCreditCard(creditCard);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    private void doSaveCreditCard(CreditCard creditCard) {
        getCreditCardDao().saveCreditCard(creditCard);
    }

    public void saveExistingCreditCard(CreditCardGrid creditCardGrid) {
        doSaveExistingCreditCard(creditCardGrid);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    private void doSaveExistingCreditCard(CreditCardGrid creditCardGrid) {
        getCreditCardDao().updateCreditCard(creditCardGrid);
    }

    public CreditCard findCreditCard(String fourDigits, String nameOnCard) {
        return doFindCreditCard(fourDigits, nameOnCard);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED, readOnly = true)
    private CreditCard doFindCreditCard(String fourDigits, String nameOnCard) {
        return getCreditCardDao().findCreditCard(fourDigits, nameOnCard);
    }

    public List<CreditCard> listCreditCards(List<SearchFilter> searchFilters, String sortIndex, String sortOrder, int firstResult, int maxResults) {
        return doListCreditCards(searchFilters, sortIndex, sortOrder, firstResult, maxResults);
    }

    public List<CreditCardGrid> gridList(List<SearchFilter> searchFilters, String sortIndex, String sortOrder, int firstResult, int maxResults) {
        List<CreditCard> creditCards = listCreditCards(searchFilters, sortIndex, sortOrder, firstResult, maxResults);
        return gridTransformation(creditCards);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED, readOnly = true)
    private List<CreditCard> doListCreditCards(List<SearchFilter> searchFilters, String sortIndex, String sortOrder, int firstResult, int maxResults) {
        return getCreditCardDao().listCreditCards(searchFilters, sortIndex, sortOrder, firstResult, maxResults);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    public void deleteCreditCard(long id) {
        getCreditCardDao().deleteCreditCard(id);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED, readOnly = true)
    public long getCreditCardCount(List<SearchFilter> searchFilters) {
        return getCreditCardDao().getCreditCardCount(searchFilters);
    }

    private List<CreditCardGrid> gridTransformation(List<CreditCard> creditCards) {
        List<CreditCardGrid> gridCreditCards = new ArrayList<CreditCardGrid>();
        for (CreditCard cc : creditCards) {
            CreditCardGrid ccg = new CreditCardGrid();
            ccg.setId(cc.getId());
            ccg.setCreditCardNumber(cc.getCreditCardNumber());
            ccg.setCardType(cc.getCardType());
            ccg.setExpirationDate(cc.getExpirationDate());
            ccg.setFourDigits(cc.getFourDigits());
            ccg.setNameOnCard(cc.getNameOnCard());
            gridCreditCards.add(ccg);
        }
        return gridCreditCards;
    }

    public void setCreditCardDao(CreditCardDao creditCardDao) {
        this.creditCardDao = creditCardDao;
    }

    public CreditCardDao getCreditCardDao() {
        return creditCardDao;
    }
}
