package simpleBank;

public class BankSystemWeb implements BankSystem {

    private AccountInfo accountInfo = null;

    public boolean checkIfAccountExists(int accountNumber) {
        if (accountInfo != null) return accountInfo.getAccountNumber() == accountNumber;
        return false;
    }

    public boolean createBankAccount(AccountInfo accountInfo) {
        this.accountInfo = accountInfo;
        return true;
    }

    public String getAccountType(int accountNumber) {
        return this.accountInfo.getAccountType();
    }
}
