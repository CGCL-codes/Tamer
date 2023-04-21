package fi.mmmtike.tiira.account.server;

import java.util.ArrayList;
import java.util.List;
import fi.mmmtike.tiira.account.common.Account;
import fi.mmmtike.tiira.account.common.AccountException;
import fi.mmmtike.tiira.account.common.AccountService;

public class AccountServiceImpl implements AccountService {

    private int calls = 0;

    private String testName = "Ronnie James Dio";

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public List<Account> getAccounts(String name) {
        Account a = new Account();
        a.setName(this.testName);
        ArrayList<Account> list = new ArrayList<Account>();
        list.add(a);
        return list;
    }

    public int howManyCalls() {
        return ++calls;
    }

    public void testUnknownException() throws Exception {
        throw new Exception("Unexpected error in AccountService.");
    }

    public void testValidationException() throws AccountException {
        throw new AccountException("Some validation error.");
    }
}
