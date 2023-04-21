package net.sf.jmoney.paypal;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import net.sf.jmoney.isolation.TransactionManager;
import net.sf.jmoney.model2.Account;
import net.sf.jmoney.model2.CapitalAccount;
import net.sf.jmoney.model2.Currency;
import net.sf.jmoney.model2.DatastoreManager;
import net.sf.jmoney.model2.Entry;
import net.sf.jmoney.model2.IncomeExpenseAccount;
import net.sf.jmoney.model2.Session;
import net.sf.jmoney.model2.Transaction;
import net.sf.jmoney.reconciliation.ReconciliationEntryInfo;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import au.com.bytecode.opencsv.CSVReader;

/**
 * A wizard to import data from a comma-separated file that has been downloaded
 * from Paypal.
 * <P>
 * Currently this wizard if a single page wizard that asks only for the file.
 * This feature is implemented as a wizard because the Eclipse workbench import
 * action requires all import implementations to be wizards.
 */
public class CsvImportWizard extends Wizard implements IImportWizard {

    private IWorkbenchWindow window;

    private CsvImportWizardPage filePage;

    /**
	 * The transaction manager for all changes made by the import, the
	 * transaction being committed when the file has been fully imported.
	 */
    TransactionManager transactionManager;

    /**
	 * Session, being the version inside the transaction so changes
	 * are not applied to the datastore until the transaction is
	 * committed.
	 */
    private Session session;

    private PaypalAccount paypalAccount;

    public CsvImportWizard() {
        IDialogSettings workbenchSettings = Activator.getDefault().getDialogSettings();
        IDialogSettings section = workbenchSettings.getSection("CsvImportWizard");
        if (section == null) {
            section = workbenchSettings.addNewSection("CsvImportWizard");
        }
        setDialogSettings(section);
    }

    /**
	 * We will cache window object in order to be able to provide parent shell
	 * for the message dialog.
	 */
    public void init(IWorkbench workbench, IStructuredSelection selection) {
        this.window = workbench.getActiveWorkbenchWindow();
        DatastoreManager sessionManager = (DatastoreManager) window.getActivePage().getInput();
        if (sessionManager == null) {
            MessageDialog waitDialog = new MessageDialog(window.getShell(), "Disabled Action Selected", null, "You cannot import data into an accounting session unless you have a session open.  You must first open a session or create a new session.", MessageDialog.INFORMATION, new String[] { IDialogConstants.OK_LABEL }, 0);
            waitDialog.open();
            return;
        }
        transactionManager = new TransactionManager(sessionManager);
        session = transactionManager.getSession();
        PaypalAccount accountOutside = null;
        for (Iterator<CapitalAccount> iter = sessionManager.getSession().getCapitalAccountIterator(); iter.hasNext(); ) {
            CapitalAccount eachAccount = iter.next();
            if (eachAccount instanceof PaypalAccount) {
                if (accountOutside != null) {
                    MessageDialog.openError(window.getShell(), "Problem", "Multiple Paypal accounts.  Don't know which to use.  If you have multiple Paypal accounts, please submit a patch.");
                    return;
                }
                accountOutside = (PaypalAccount) eachAccount;
            }
        }
        if (accountOutside == null) {
            MessageDialog.openError(window.getShell(), "Problem", "No Paypal account has been created");
            return;
        }
        paypalAccount = transactionManager.getCopyInTransaction(accountOutside);
        filePage = new CsvImportWizardPage(window);
        addPage(filePage);
    }

    @Override
    public boolean performFinish() {
        String fileName = filePage.getFileName();
        if (fileName != null) {
            File csvFile = new File(fileName);
            importFile(csvFile);
        }
        return true;
    }

    public void importFile(File file) {
        try {
            CSVReader reader = new CSVReader(new FileReader(file));
            Collection<Row> refunds = new ArrayList<Row>();
            Collection<Row> reversals = new ArrayList<Row>();
            Currency currency = paypalAccount.getCurrency();
            reader.readNext();
            Row row = readRow(reader);
            while (row != null) {
                boolean readAlreadyDone = false;
                if (row.type.equals("Shopping Cart Payment Sent")) {
                    List<Row> rowItems = new ArrayList<Row>();
                    Row rowItem = readRow(reader);
                    while (rowItem.type.equals("Shopping Cart Item")) {
                        if (rowItem.shippingAndHandlingAmount != row.shippingAndHandlingAmount) {
                        }
                        rowItems.add(rowItem);
                        rowItem = readRow(reader);
                    }
                    distribute(row.shippingAndHandlingAmount, rowItems);
                    Transaction trans = session.createTransaction();
                    trans.setDate(row.date);
                    PaypalEntry mainEntry = trans.createEntry().getExtension(PaypalEntryInfo.getPropertySet(), true);
                    mainEntry.setAccount(paypalAccount);
                    mainEntry.setAmount(row.grossAmount);
                    mainEntry.setMemo("payment - " + row.payeeName);
                    mainEntry.setMerchantEmail(row.merchantEmail);
                    mainEntry.setPropertyValue(ReconciliationEntryInfo.getUniqueIdAccessor(), row.transactionId);
                    for (Row rowItem2 : rowItems) {
                        createCategoryEntry(trans, rowItem2, paypalAccount.getSaleAndPurchaseAccount());
                    }
                    if (row.status.equals("Partially Refunded")) {
                        long refundAmount = 0;
                        for (Iterator<Row> iter = refunds.iterator(); iter.hasNext(); ) {
                            Row refund = iter.next();
                            if (refund.payeeName.equals(row.payeeName)) {
                                PaypalEntry refundEntry = trans.createEntry().getExtension(PaypalEntryInfo.getPropertySet(), true);
                                refundEntry.setAccount(paypalAccount);
                                refundEntry.setAmount(refund.grossAmount);
                                refundEntry.setMemo("refund - " + refund.payeeName);
                                refundEntry.setValuta(refund.date);
                                refundEntry.setMerchantEmail(refund.merchantEmail);
                                refundEntry.setPropertyValue(ReconciliationEntryInfo.getUniqueIdAccessor(), refund.transactionId);
                                refundAmount += refund.grossAmount;
                                iter.remove();
                            }
                        }
                        PaypalEntry lineItemEntry = trans.createEntry().getExtension(PaypalEntryInfo.getPropertySet(), true);
                        lineItemEntry.setAccount(paypalAccount.getSaleAndPurchaseAccount());
                        lineItemEntry.setAmount(-refundAmount);
                        lineItemEntry.setMemo(row.payeeName + " - amount refunded");
                    }
                    row = rowItem;
                    readAlreadyDone = true;
                    assertValid(trans);
                } else if (row.type.equals("Shopping Cart Item")) {
                    throw new UnexpectedDataException("'Shopping Cart Item' row found but it is not preceeded by a 'Shopping Cart Payment Sent' or 'eBay Payment Sent' row.");
                } else if (row.type.equals("Refund")) {
                    refunds.add(row);
                } else if (row.type.equals("Reversal")) {
                    reversals.add(row);
                } else if (row.type.equals("eBay Payment Sent") || row.type.equals("eBay Payment Received") || row.type.equals("Payment Received") || row.type.equals("Payment Sent") || row.type.equals("Web Accept Payment Sent")) {
                    if (row.status.equals("Refunded")) {
                        Row match = null;
                        for (Row refund : refunds) {
                            if (refund.payeeName.equals(row.payeeName) && refund.grossAmount == -row.grossAmount) {
                                match = refund;
                                break;
                            }
                        }
                        if (match == null) {
                            throw new UnexpectedDataException("An entry was found that says it was refunded, but no matching 'Refund' entry was found.");
                        }
                        refunds.remove(match);
                        createRefundTransaction(row, match);
                    } else if (row.status.equals("Reversed")) {
                        Row match = null;
                        for (Row reversal : reversals) {
                            if (reversal.payeeName.equals(row.payeeName) && reversal.grossAmount == -row.grossAmount) {
                                match = reversal;
                                break;
                            }
                        }
                        if (match == null) {
                            throw new UnexpectedDataException("An entry was found that says it was reversed, but no matching 'Reversal' entry was found.");
                        }
                        reversals.remove(match);
                    } else {
                        Transaction trans = session.createTransaction();
                        trans.setDate(row.date);
                        PaypalEntry mainEntry = trans.createEntry().getExtension(PaypalEntryInfo.getPropertySet(), true);
                        mainEntry.setAccount(paypalAccount);
                        mainEntry.setAmount(row.netAmount);
                        mainEntry.setMemo("payment - " + row.payeeName);
                        mainEntry.setValuta(row.date);
                        mainEntry.setMerchantEmail(row.merchantEmail);
                        mainEntry.setPropertyValue(ReconciliationEntryInfo.getUniqueIdAccessor(), row.transactionId);
                        if (row.status.equals("Partially Refunded")) {
                            long refundAmount = 0;
                            for (Iterator<Row> iter = refunds.iterator(); iter.hasNext(); ) {
                                Row refund = iter.next();
                                if (refund.payeeName.equals(row.payeeName)) {
                                    PaypalEntry refundEntry = trans.createEntry().getExtension(PaypalEntryInfo.getPropertySet(), true);
                                    refundEntry.setAccount(paypalAccount);
                                    refundEntry.setAmount(refund.grossAmount);
                                    refundEntry.setMemo("refund - " + refund.payeeName);
                                    refundEntry.setValuta(refund.date);
                                    refundEntry.setMerchantEmail(refund.merchantEmail);
                                    refundEntry.setPropertyValue(ReconciliationEntryInfo.getUniqueIdAccessor(), refund.transactionId);
                                    refundAmount += refund.grossAmount;
                                    iter.remove();
                                }
                            }
                            if (-row.netAmount - refundAmount == row.shippingAndHandlingAmount) {
                                row.memo = row.memo + " (s&h not refunded after return)";
                            } else {
                                row.memo = row.memo + " ($" + currency.format(-row.netAmount) + " less $" + currency.format(refundAmount) + " refunded)";
                            }
                            row.netAmount += refundAmount;
                            row.grossAmount += refundAmount;
                        }
                        if (row.type.equals("eBay Payment Sent")) {
                            Row thisRow = row;
                            Row nextRow = readRow(reader);
                            if (nextRow.type.equals("Shopping Cart Item")) {
                                thisRow.quantityString = nextRow.quantityString;
                            } else {
                                row = nextRow;
                                readAlreadyDone = true;
                            }
                            createCategoryEntry(trans, thisRow, paypalAccount.getSaleAndPurchaseAccount());
                        } else {
                            if (row.fee != 0) {
                                if (row.type.equals("Payment Received") || row.type.equals("Payment Sent")) {
                                    if (paypalAccount.getPaypalFeesAccount() == null) {
                                        throw new UnexpectedDataException("A Paypal fee has been found in the imported data.  However, no category has been configured in the properties for this Paypal account for such fees.");
                                    }
                                    Entry feeEntry = trans.createEntry();
                                    feeEntry.setAccount(paypalAccount.getPaypalFeesAccount());
                                    feeEntry.setAmount(-row.fee);
                                    feeEntry.setMemo("Paypal");
                                    row.fee = 0L;
                                    row.netAmount = row.grossAmount;
                                }
                            }
                            if (row.memo.length() == 0) {
                                if (row.type.equals("Payment Received")) {
                                    row.memo = row.payeeName + " - gross payment";
                                }
                                if (row.type.equals("Payment Sent")) {
                                    row.memo = row.payeeName + " - payment";
                                }
                            }
                            createCategoryEntry(trans, row, paypalAccount.getSaleAndPurchaseAccount());
                        }
                        assertValid(trans);
                    }
                } else if (row.type.equals("Donation Sent")) {
                    if (paypalAccount.getDonationAccount() == null) {
                        throw new UnexpectedDataException("A donation has been found in the imported data.  However, no category was set for donations.  Please go to the Paypal account properties and select a category to be used for donations.");
                    }
                    createTransaction(row, "donation sent", paypalAccount.getDonationAccount(), row.payeeName);
                } else if (row.type.equals("Add Funds from a Bank Account")) {
                    if (paypalAccount.getTransferBank() == null) {
                        throw new UnexpectedDataException("A bank account transfer has been found in the imported data.  However, no bank account has been set in the properties for this Paypal account.");
                    }
                    createTransaction(row, "transfer from bank", paypalAccount.getTransferBank(), "transfer to Paypal");
                } else if (row.type.equals("Update to eCheck Sent")) {
                } else if (row.type.equals("Update to eCheck Received")) {
                } else if (row.type.equals("Update to Payment Received")) {
                } else if (row.type.equals("eCheck Sent")) {
                    if (paypalAccount.getSaleAndPurchaseAccount() == null) {
                        throw new UnexpectedDataException("An eCheck entry has been found in the imported data.  However, no sale and purchase account has been set in the properties for this Paypal account.");
                    }
                    createTransaction(row, "payment by transfer", paypalAccount.getSaleAndPurchaseAccount(), "transfer from Paypal");
                } else if (row.type.equals("Express Checkout Payment Sent")) {
                    if (paypalAccount.getSaleAndPurchaseAccount() == null) {
                        throw new UnexpectedDataException("An 'Express Checkout' entry has been found in the imported data.  However, no sale and purchase account has been set in the properties for this Paypal account.");
                    }
                    if (row.status.equals("Refunded")) {
                        Row match = null;
                        for (Row refund : refunds) {
                            if (refund.payeeName.equals(row.payeeName) && refund.grossAmount == -row.grossAmount) {
                                match = refund;
                                break;
                            }
                        }
                        if (match == null) {
                            throw new UnexpectedDataException("An entry was found that says it was refunded, but no matching 'Refund' entry was found.");
                        }
                        refunds.remove(match);
                        createRefundTransaction(row, match);
                    } else {
                        createTransaction(row, row.payeeName, paypalAccount.getSaleAndPurchaseAccount(), row.payeeName + " - Paypal payment");
                    }
                } else if (row.type.equals("Charge From Credit Card")) {
                    if (paypalAccount.getTransferCreditCard() == null) {
                        throw new UnexpectedDataException("A credit card charge has been found in the imported data.  However, no credit card account has been set in the properties for this Paypal account.");
                    }
                    createTransaction(row, "payment from credit card", paypalAccount.getTransferCreditCard(), "transfer to Paypal");
                } else if (row.type.equals("Credit to Credit Card")) {
                    if (paypalAccount.getTransferCreditCard() == null) {
                        throw new UnexpectedDataException("A credit card refund has been found in the imported data.  However, no credit card account has been set in the properties for this Paypal account.");
                    }
                    createTransaction(row, "refund to credit card", paypalAccount.getTransferCreditCard(), "refund from Paypal");
                } else {
                    MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Unable to read CSV file", "Entry found with unknown type: '" + row.type + "'.");
                }
                if (!readAlreadyDone) {
                    row = readRow(reader);
                }
            }
            transactionManager.commit("Import Paypal " + file.getName());
        } catch (UnexpectedDataException e) {
            MessageDialog.openError(window.getShell(), "Import Failed", e.getLocalizedMessage());
        } catch (IOException e) {
            MessageDialog.openError(window.getShell(), "Import Failed", "A file I/O error occurred. " + e.getLocalizedMessage());
        }
    }

    private void assertValid(Transaction trans) {
        long total = 0;
        for (Entry entry : trans.getEntryCollection()) {
            total += entry.getAmount();
        }
        if (total != 0) {
            System.out.println("unbalanced");
        }
        assert (total == 0);
    }

    /**
	 * The gross and net amounts differ only by the fee.  This method will
	 * absorb the fee into the proceeds (i.e. the amount shown in the accounts
	 * that the item was sold for will be reduced by the fee).  If this is not
	 * an item sale but a funds transfer then the fee is not absorbed.  It is
	 * accounted for as a separate split entry in the transaction.  In that case
	 * the caller will have zeroed out the fee and set the net amount to be the same
	 * as the gross amount.
	 * 
	 * @param trans
	 * @param rowItem
	 * @param account
	 */
    private void createCategoryEntry(Transaction trans, Row row, IncomeExpenseAccount account) {
        PaypalEntry lineItemEntry = trans.createEntry().getExtension(PaypalEntryInfo.getPropertySet(), true);
        lineItemEntry.setAccount(account);
        if (row.type.equals("Shopping Cart Item")) {
            lineItemEntry.setAmount(row.grossAmount);
        } else {
            lineItemEntry.setAmount(-row.netAmount);
        }
        if (row.itemUrlString.length() != 0) {
            try {
                URL itemUrl = new URL(row.itemUrlString);
                lineItemEntry.setItemUrl(itemUrl);
            } catch (MalformedURLException e) {
            }
        }
        StringBuffer adjustmentsBuffer = new StringBuffer();
        Currency currency = paypalAccount.getCurrency();
        String separator = "";
        long baseAmount = lineItemEntry.getAmount();
        String memo = row.memo;
        if (row.quantityString.length() != 0 && !row.quantityString.equals("0") && !row.quantityString.equals("1")) {
            memo = memo + " x" + row.quantityString;
        }
        if (row.shippingAndHandlingAmount != 0) {
            adjustmentsBuffer.append("s&h $").append(currency.format(row.shippingAndHandlingAmount)).append(separator);
            separator = ", ";
            baseAmount -= row.shippingAndHandlingAmount;
        }
        if (row.insurance != 0) {
            adjustmentsBuffer.append("insurance $").append(currency.format(row.insurance)).append(separator);
            separator = ", ";
            baseAmount -= row.insurance;
        }
        if (row.salesTax != 0) {
            adjustmentsBuffer.append("tax $").append(currency.format(row.salesTax)).append(separator);
            separator = ", ";
            baseAmount -= row.salesTax;
        }
        if (row.fee != 0) {
            adjustmentsBuffer.append("less Paypal fee $").append(currency.format(row.fee)).append(separator);
            separator = ", ";
            baseAmount -= row.fee;
        }
        if (adjustmentsBuffer.length() == 0) {
            lineItemEntry.setMemo(memo);
        } else {
            lineItemEntry.setMemo(memo + " ($" + currency.format(baseAmount) + " + " + adjustmentsBuffer.toString() + ")");
        }
    }

    /**
	 * We distribute the shipping and handling among the items in proportion
	 * to the price of each item.  This is the preference of the author.
	 * If this is not your preference then please add a preference to the preferences
	 * to indicate if a separate line item should instead be created for the
	 * shipping and handling and implement it.
	 * @throws UnexpectedDataException 
	 */
    private void distribute(long toDistribute, List<Row> rowItems) throws UnexpectedDataException {
        long netTotal = 0;
        for (Row rowItem : rowItems) {
            if (rowItem.grossAmount <= 0) {
                throw new UnexpectedDataException("Shopping Cart Item with zero or negative gross amount");
            }
            netTotal += rowItem.grossAmount;
        }
        long leftToDistribute = toDistribute;
        for (Row rowItem : rowItems) {
            long amount = toDistribute * rowItem.grossAmount / netTotal;
            rowItem.shippingAndHandlingAmount = amount;
            leftToDistribute -= amount;
        }
        for (Row rowItem : rowItems) {
            if (leftToDistribute > 0) {
                rowItem.shippingAndHandlingAmount++;
                leftToDistribute--;
            }
        }
        assert (leftToDistribute == 0);
        for (Row rowItem : rowItems) {
            rowItem.grossAmount += rowItem.shippingAndHandlingAmount;
        }
    }

    private Row readRow(CSVReader reader) throws IOException, UnexpectedDataException {
        String[] nextLine = reader.readNext();
        if (nextLine != null && (nextLine.length == 42 || nextLine.length == 43)) {
            Row row = new Row();
            String dateString = nextLine[0];
            row.payeeName = nextLine[3];
            row.type = nextLine[4];
            row.status = nextLine[5];
            row.grossAmount = getAmount(nextLine[6]);
            row.fee = getAmount(nextLine[7]);
            row.netAmount = getAmount(nextLine[8]);
            if (row.type.equals("Refund") || row.type.equals("Reversal") || row.type.equals("Payment Received") || row.type.equals("eBay Payment Received")) {
                row.merchantEmail = nextLine[9];
            } else {
                row.merchantEmail = nextLine[10];
            }
            row.transactionId = nextLine[11];
            row.memo = nextLine[14];
            row.shippingAndHandlingAmount = getAmount(nextLine[16]);
            row.insurance = getAmount(nextLine[17]);
            row.salesTax = getAmount(nextLine[18]);
            row.itemUrlString = nextLine[25];
            row.quantityString = nextLine[32];
            row.balance = getAmount(nextLine[34]);
            DateFormat df = new SimpleDateFormat("MM/dd/yy");
            try {
                row.date = df.parse(dateString);
            } catch (ParseException e) {
                throw new UnexpectedDataException("Date", dateString);
            }
            return row;
        }
        assert (nextLine.length == 0);
        return null;
    }

    private void createTransaction(Row row, String paypalAccountMemo, Account otherAccount, String otherAccountMemo) {
        Transaction trans = session.createTransaction();
        trans.setDate(row.date);
        PaypalEntry mainEntry = trans.createEntry().getExtension(PaypalEntryInfo.getPropertySet(), true);
        mainEntry.setAccount(paypalAccount);
        mainEntry.setAmount(row.grossAmount);
        mainEntry.setMemo(paypalAccountMemo);
        mainEntry.setValuta(row.date);
        mainEntry.setPropertyValue(ReconciliationEntryInfo.getUniqueIdAccessor(), row.transactionId);
        Entry otherEntry = trans.createEntry();
        otherEntry.setAccount(otherAccount);
        otherEntry.setAmount(-row.grossAmount);
        otherEntry.setMemo(otherAccountMemo);
    }

    /**
	 * This is a helper method that creates a transaction where there are just two entries
	 * and both are in the Paypal account.  This occurs when an entry is refunded in full.
	 */
    private void createRefundTransaction(Row originalRow, Row refundRow) {
        Transaction trans = session.createTransaction();
        trans.setDate(originalRow.date);
        PaypalEntry mainEntry = trans.createEntry().getExtension(PaypalEntryInfo.getPropertySet(), true);
        mainEntry.setAccount(paypalAccount);
        mainEntry.setAmount(originalRow.grossAmount);
        mainEntry.setMemo(originalRow.payeeName);
        mainEntry.setValuta(originalRow.date);
        mainEntry.setPropertyValue(ReconciliationEntryInfo.getUniqueIdAccessor(), originalRow.transactionId);
        Entry refundEntry = trans.createEntry();
        refundEntry.setAccount(paypalAccount);
        refundEntry.setAmount(-originalRow.grossAmount);
        refundEntry.setMemo("refund - " + originalRow.payeeName);
        refundEntry.setValuta(refundRow.date);
        refundEntry.setPropertyValue(ReconciliationEntryInfo.getUniqueIdAccessor(), refundRow.transactionId);
    }

    long getAmount(String amountString) {
        if (amountString.length() == 0) {
            return 0;
        }
        boolean negate = false;
        if (amountString.charAt(0) == '-') {
            amountString = amountString.substring(1);
            negate = true;
        }
        try {
            String parts[] = amountString.replaceAll(",", "").split("\\.");
            long amount = Long.parseLong(parts[0]) * 100;
            if (parts.length > 1) {
                if (parts[1].length() == 1) {
                    parts[1] += "0";
                }
                amount += Long.parseLong(parts[1]);
            }
            return negate ? -amount : amount;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public class Row {

        Date date;

        String payeeName;

        String type;

        String status;

        Long grossAmount;

        Long fee;

        Long netAmount;

        String merchantEmail;

        String transactionId;

        String memo;

        long shippingAndHandlingAmount;

        long insurance;

        long salesTax;

        String itemUrlString;

        String quantityString;

        Long balance;
    }
}
