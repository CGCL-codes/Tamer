package jcash;

public interface AccountExporter {

    public void addName(String s);

    public void addDescription(String s);

    public void addDefaultOffset(Account a);

    public void addSuperAccount(Account a);

    public void addAccountType(AccountType a);
}
