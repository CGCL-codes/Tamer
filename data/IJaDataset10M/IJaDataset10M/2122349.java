package jbossaop.testing.bank;

import jbossaop.testing.customer.*;

public final class BankBusiness {

    private final BankAccountDAO bankAccountDAO;

    public BankBusiness() {
        bankAccountDAO = BankAccountDAOFactory.getBankAccountDAOSerializer();
    }

    public double getSumOfAllAccounts(Customer c) {
        double sum = 0;
        for (long accountNo : c.getAccounts()) {
            BankAccount a = bankAccountDAO.getBankAccount(accountNo);
            if (a != null) {
                sum += a.getBalance();
            }
        }
        return sum;
    }
}
