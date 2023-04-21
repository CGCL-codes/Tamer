package cn.com.androidforfun.finance.data;

import java.util.Date;
import java.util.List;

public interface IDataProvider {

    int LIST_DETAIL_MODE_BOTH = 0;

    int LIST_DETAIL_MODE_FROM = 1;

    int LIST_DETAIL_MODE_TO = 2;

    void init();

    void destroyed();

    void reset();

    void deleteAllAccount();

    void deleteAllDetail();

    Account findAccount(String id);

    Account findAccount(String type, String name);

    void newAccount(Account account) throws DuplicateKeyException;

    void newAccount(String id, Account account) throws DuplicateKeyException;

    void newAccountNoCheck(String id, Account account);

    boolean updateAccount(String id, Account account);

    boolean deleteAccount(String id);

    List<Account> listAccount();

    Detail findDetail(int id);

    void newDetail(Detail detail);

    void newDetail(int id, Detail detail) throws DuplicateKeyException;

    void newDetailNoCheck(int id, Detail detail);

    boolean updateDetail(int id, Detail detail);

    boolean deleteDetail(int id);

    List<Detail> listAllDetail();

    int countDetail(Date start, Date end);

    int countDetail(int mode, Date start, Date end);

    int countDetail(Account account, int mode, Date start, Date end);

    List<Detail> listDetail(Date start, Date end, int max);

    List<Detail> listDetail(int mode, Date start, Date end, int max);

    List<Detail> listDetail(Account account, int mode, Date start, Date end, int max);

    double sumFrom(Date start, Date end);

    double sumFrom(Account account, Date start, Date end);

    double sumTo(Date start, Date end);

    double sumTo(Account account, Date start, Date end);

    Detail getFirstDetail();

    double sumInitialValue();

    void newPaymentsType(PaymentsType workingType) throws DuplicateKeyException;

    void updatePaymentsType(String string, PaymentsType workingType);

    PaymentsType findPaymentsType(String id);

    List<PaymentsType> listType(String string);

    void deletePType(String id) throws Exception;

    List<Account> listCanceledAccount();
}
