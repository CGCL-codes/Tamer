package simpleBank;

public class BankSystemBranch implements BankSystem {

    private AccountInfo accountInfo = null;

    public boolean checkIfAccountExists(int accountNumber) {
        if (accountInfo != null) return accountInfo.getAccountNumber() != accountNumber;
        return false;
    }

    public boolean createBankAccount(AccountInfo accountInfo) {
        accountInfo.setAccountNumber(12345);
        this.accountInfo = accountInfo;
        return true;
    }

    public String getAccountType(int accountNumber) {
        return this.accountInfo.getAccountType();
    }
}
