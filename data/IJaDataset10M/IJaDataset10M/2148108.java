package serviceImplementations.payrollScenario;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * This service performs the role of the "Bank" in the payroll
 * processing scenarion. 
 * 
 * @author Michael Sch�fer
 *
 */
public class BankAccounts {

    private Hashtable<Integer, Integer> accounts;

    private Hashtable<Integer, String> accountNames;

    private ProtocolWriter eventLog;

    private ProtocolWriter accountBalanceOverview;

    private PropertyReader propertyReader;

    /**
	 * Constructs a new bank accounts object.
	 *
	 */
    public BankAccounts() {
        this.propertyReader = new PropertyReader();
        this.accounts = new Hashtable<Integer, Integer>();
        this.accountNames = new Hashtable<Integer, String>();
        File eventLogFile = new File(this.propertyReader.getFileBankEventLog());
        File accountBalanceOverviewFile = new File(this.propertyReader.getFileBankAccountBalanceOverview());
        this.eventLog = new ProtocolWriter(eventLogFile);
        this.accountBalanceOverview = new ProtocolWriter(accountBalanceOverviewFile);
    }

    /**
	 * Transfers the specified amount of money from the debit account to the
	 * credit account.
	 * @param debitAccountNumber The account from which the money will be taken.
	 * @param creditAccountNumber The account to which the money will be transferred.
	 * @param amount The amount of money that will be transferred.
	 */
    public void transfer(int debitAccountNumber, int creditAccountNumber, int amount) {
        if (this.accounts.containsKey(debitAccountNumber) && this.accounts.containsKey(creditAccountNumber)) {
            int debitAccountBalance = this.accounts.get(debitAccountNumber);
            int creditAccountBalance = this.accounts.get(creditAccountNumber);
            debitAccountBalance -= amount;
            creditAccountBalance += amount;
            this.accounts.put(debitAccountNumber, debitAccountBalance);
            this.accounts.put(creditAccountNumber, creditAccountBalance);
            this.eventLog.clearData();
            this.eventLog.addData("Transfer [debit: " + debitAccountNumber + " | credit: " + creditAccountNumber + " | amount: " + amount + "]");
            try {
                this.eventLog.writeFile();
            } catch (IOException e) {
                System.out.println("BankAccounts: Event log could not be written.");
            }
            this.updateBalance();
        }
    }

    /**
	 * Adds a new account.
	 * @param accountNumber The account number.
	 * @param accountName The account name.
	 * @param startBalance The initial balance of the account.
	 */
    public void addAccount(int accountNumber, String accountName, int startBalance) {
        this.accounts.put(accountNumber, startBalance);
        this.accountNames.put(accountNumber, accountName);
        this.eventLog.clearData();
        this.eventLog.addData("New Account [number: " + accountNumber + " | name: " + accountName + " | initial: " + startBalance + "]");
        try {
            this.eventLog.writeFile();
        } catch (IOException e) {
            System.out.println("BankAccounts: Event log could not be written.");
        }
        this.updateBalance();
    }

    /**
	 * Returns the current balance of the account with the given account number.
	 * @param accountNumber The number of the account.
	 * @return The current balance.
	 */
    public int getBalance(int accountNumber) {
        return this.accounts.get(accountNumber);
    }

    /**
	 * Updates the account balance overview protol file.
	 *
	 */
    private void updateBalance() {
        this.accountBalanceOverview.clearData();
        Enumeration<Integer> keys = this.accounts.keys();
        Integer key;
        Integer value;
        String lineNames = "";
        String lineValues = "";
        int counter = 0;
        while (keys.hasMoreElements()) {
            key = keys.nextElement();
            value = this.accounts.get(key);
            lineNames += "\t";
            lineNames += this.accountNames.get(key);
            if (counter > 0) lineValues += "\t\t"; else lineValues += "\t";
            lineValues += value;
            counter++;
        }
        this.accountBalanceOverview.addData(lineNames);
        this.accountBalanceOverview.addData(lineValues);
        try {
            this.accountBalanceOverview.writeFile();
        } catch (IOException e) {
            System.out.println("BankAccounts: Account balance overview could not be written.");
        }
    }
}
