package fixtures;

import bank.AccountInfo;
import bank.BankSystem;
import bank.BankSystemWeb;

public class CreateBankAccountWebGP {

    private AccountInfo accountInfo = new AccountInfo();

    private BankSystem bankSystem = new BankSystemWeb();

    public boolean enterFirstNameOfApplicantAndSecondName(String applicant, String name) {
        accountInfo.setFirstName(applicant);
        accountInfo.setLastName(name);
        return true;
    }

    public boolean chooseAccountType(String type) {
        accountInfo.setAccountType(type);
        return true;
    }

    public boolean createBankAccount() {
        return bankSystem.createBankAccount(accountInfo);
    }

    public boolean checkIfAccountWithBankAccountNumberExists(String number) {
        return bankSystem.checkIfAccountExists(Integer.valueOf(number));
    }

    public boolean checkThatAccountTypeIs(String is) {
        return bankSystem.getAccountType(Integer.valueOf(this.accountInfo.getAccountNumber())).equals(is);
    }

    public boolean checkThatBankAccountDoesNotExist() {
        return !bankSystem.checkIfAccountExists(this.accountInfo.getAccountNumber());
    }

    public boolean checkThatBankAccountDoesExist() {
        return bankSystem.checkIfAccountExists(this.accountInfo.getAccountNumber());
    }
}
