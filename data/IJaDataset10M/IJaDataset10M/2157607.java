package ru.cybertek.domain;

import ru.cybertek.domain.constant.AccountType;
import ru.cybertek.domain.Company;
import ru.cybertek.domain.Person;
import ru.cybertek.domain.Account;

public class PayerImpl implements Payer {

    private Account account;

    public PayerImpl() {
    }

    ;

    public PayerImpl(Account account) {
        this.account = account;
    }

    public String getName() {
        String name = null;
        if (account.getAccountType() == AccountType.PERSONAL) {
            Person owner = (Person) account.getOwner();
            name = owner.getFirstName() + " " + owner.getLastName();
        } else if (account.getAccountType() == AccountType.CORPORATE) {
            Company owner = (Company) account.getOwner();
            name = owner.getShortName();
        }
        return name;
    }

    public String getInn() {
        String inn = null;
        if (account.getAccountType() == AccountType.PERSONAL) {
            Person owner = (Person) account.getOwner();
            inn = owner.getFinancialDetails().getInn();
        } else if (account.getAccountType() == AccountType.CORPORATE) {
            Company owner = (Company) account.getOwner();
            inn = owner.getFinancialDetails().getInn();
        }
        return inn;
    }

    public String getKpp() {
        String kpp = null;
        if (account.getAccountType() == AccountType.CORPORATE) {
            Company owner = (Company) account.getOwner();
            kpp = owner.getFinancialDetails().getKpp();
        }
        return kpp;
    }

    public String getAccountNumber() {
        return account.getAccountNumber();
    }

    public String getBankAccountNumber() {
        return account.getBank().getCurrentAccount();
    }

    public String getBankName() {
        return account.getBank().getShortName();
    }

    public String getBankBic() {
        return account.getBank().getBic();
    }

    public Integer getAccountId() {
        return account.getId();
    }
}
