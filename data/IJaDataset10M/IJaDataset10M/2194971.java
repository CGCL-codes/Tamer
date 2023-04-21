package net.sf.jmoney.ofx;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import net.sf.jmoney.importer.matcher.ImportMatcher;
import net.sf.jmoney.importer.model.PatternMatcherAccountInfo;
import net.sf.jmoney.isolation.TransactionManager;
import net.sf.jmoney.model2.Account;
import net.sf.jmoney.model2.BankAccount;
import net.sf.jmoney.model2.Commodity;
import net.sf.jmoney.model2.DatastoreManager;
import net.sf.jmoney.model2.Entry;
import net.sf.jmoney.model2.ScalarPropertyAccessor;
import net.sf.jmoney.model2.Session;
import net.sf.jmoney.model2.Session.NoAccountFoundException;
import net.sf.jmoney.model2.Session.SeveralAccountsFoundException;
import net.sf.jmoney.model2.Transaction;
import net.sf.jmoney.ofx.model.OfxEntryInfo;
import net.sf.jmoney.ofx.parser.SimpleDOMParser;
import net.sf.jmoney.ofx.parser.SimpleElement;
import net.sf.jmoney.ofx.parser.TagNotFoundException;
import net.sf.jmoney.stocks.model.SecurityInfo;
import net.sf.jmoney.stocks.model.Stock;
import net.sf.jmoney.stocks.model.StockAccount;
import net.sf.jmoney.stocks.model.StockEntry;
import net.sf.jmoney.stocks.model.StockEntryInfo;
import net.sf.jmoney.stocks.model.StockInfo;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;

public class OfxImporter {

    private IWorkbenchWindow window;

    public OfxImporter(IWorkbenchWindow window) {
        this.window = window;
    }

    public void importFile(File file) {
        DatastoreManager sessionManager = (DatastoreManager) window.getActivePage().getInput();
        if (sessionManager == null) {
            MessageDialog waitDialog = new MessageDialog(window.getShell(), "Disabled Action Selected", null, "You cannot import data into an accounting session unless you have a session open.  You must first open a session or create a new session.", MessageDialog.ERROR, new String[] { IDialogConstants.OK_LABEL }, 0);
            waitDialog.open();
            return;
        }
        try {
            TransactionManager transactionManager = new TransactionManager(sessionManager);
            BufferedReader buffer = null;
            buffer = new BufferedReader(new FileReader(file));
            SimpleDOMParser parser = new SimpleDOMParser();
            SimpleElement rootElement = null;
            rootElement = parser.parse(buffer);
            Session session = transactionManager.getSession();
            Session sessionOutsideTransaction = sessionManager.getSession();
            SimpleElement statementResultElement = rootElement.getDescendant("BANKMSGSRSV1", "STMTTRNRS", "STMTRS");
            if (statementResultElement != null) {
                importBankStatement(transactionManager, rootElement, session, sessionOutsideTransaction, statementResultElement);
            } else {
                statementResultElement = rootElement.getDescendant("INVSTMTMSGSRSV1", "INVSTMTTRNRS", "INVSTMTRS");
                if (statementResultElement != null) {
                    importStockStatement(transactionManager, rootElement, session, sessionOutsideTransaction, statementResultElement);
                } else {
                    MessageDialog.openWarning(window.getShell(), "OFX file not imported", MessageFormat.format("{0} did not contain expected nodes for either a bank or a stock account.", file.getName()));
                    return;
                }
            }
            if (transactionManager.hasChanges()) {
                String transactionDescription = MessageFormat.format("Import {0}", file.getName());
                transactionManager.commit(transactionDescription);
                StringBuffer combined = new StringBuffer();
                combined.append(file.getName());
                combined.append(" was successfully imported. ");
                MessageDialog.openInformation(window.getShell(), "OFX file imported", combined.toString());
            } else {
                MessageDialog.openWarning(window.getShell(), "OFX file not imported", MessageFormat.format("{0} was not imported because all the data in it had already been imported.", file.getName()));
            }
        } catch (IOException e) {
            MessageDialog.openError(window.getShell(), "Unable to read OFX file", e.getLocalizedMessage());
        } catch (TagNotFoundException e) {
            MessageDialog.openError(window.getShell(), "Unable to read OFX file", e.getLocalizedMessage());
        }
    }

    private void importBankStatement(TransactionManager transactionManager, SimpleElement rootElement, Session session, Session sessionOutsideTransaction, SimpleElement statementResultElement) throws TagNotFoundException {
        SimpleElement accountFromElement = statementResultElement.getDescendant("BANKACCTFROM");
        String accountNumber = accountFromElement.getString("ACCTID");
        BankAccount account = null;
        BankAccount accountOutsideTransaction = null;
        for (Account eachAccount : sessionOutsideTransaction.getAccountCollection()) {
            if (eachAccount instanceof BankAccount) {
                BankAccount bankAccount = (BankAccount) eachAccount;
                if (accountNumber.equals(bankAccount.getAccountNumber())) {
                    accountOutsideTransaction = bankAccount;
                    account = transactionManager.getCopyInTransaction(accountOutsideTransaction);
                }
            }
        }
        if (account == null) {
            MessageDialog.openError(window.getShell(), "No Matching Account Found", "The OFX file contains data for bank account number " + accountNumber + ".  However no bank account exists with such an account number.  You probably need to set the account number property for the appropriate account.");
            return;
        }
        SimpleElement currencyElement = rootElement.findElement("CURDEF");
        if (currencyElement != null) {
            String currencyCode = currencyElement.getTrimmedText();
            if (!account.getCurrency().getCode().equals(currencyCode)) {
                MessageDialog.openError(window.getShell(), "Currency Mismatch", MessageFormat.format("A currency mismatch prevents the import.  The OFX file indicates it contains entries in {0} but the {2} account uses {1}.", currencyCode, account.getCurrency().getCode(), account.getName()));
                return;
            }
        }
        Set<String> fitIds = new HashSet<String>();
        for (Entry entry : accountOutsideTransaction.getEntries()) {
            String fitId = entry.getPropertyValue(OfxEntryInfo.getFitidAccessor());
            if (fitId != null) {
                fitIds.add(fitId);
            }
        }
        SimpleElement transListElement = statementResultElement.getDescendant("BANKTRANLIST");
        ImportMatcher matcher = new ImportMatcher(account.getExtension(PatternMatcherAccountInfo.getPropertySet(), true));
        for (SimpleElement transactionElement : transListElement.getChildElements()) {
            if (transactionElement.getTagName().equals("DTSTART")) {
            } else if (transactionElement.getTagName().equals("DTEND")) {
            } else if (transactionElement.getTagName().equals("STMTTRN")) {
                SimpleElement stmtTrnElement = transactionElement;
                Date postedDate = stmtTrnElement.getDate("DTPOSTED");
                Date transactionDate = postedDate;
                long amount = stmtTrnElement.getAmount("TRNAMT");
                String fitid = stmtTrnElement.getString("FITID");
                String checkNumber = stmtTrnElement.getString("CHECKNUM");
                if (checkNumber != null) {
                    if (checkNumber.equals("0")) {
                        checkNumber = null;
                    }
                }
                String memo = null;
                if (account.getBank().equals("Citibank UK")) {
                    String name = stmtTrnElement.getString("NAME");
                    if (name.equals("PURCHASE")) {
                        memo = stmtTrnElement.getString("MEMO");
                        Pattern compiledPattern;
                        try {
                            compiledPattern = Pattern.compile("(.*[^\\s])\\s*TRANS  (\\d\\d.\\d\\d.\\d\\d)");
                            DateFormat df = new SimpleDateFormat("dd/MM/yy");
                            Matcher m = compiledPattern.matcher(memo);
                            System.out.println(compiledPattern + ", " + memo);
                            if (m.matches()) {
                                String transDateString = m.group(2);
                                transactionDate = df.parse(transDateString);
                                memo = m.group(1);
                            }
                        } catch (PatternSyntaxException e) {
                            compiledPattern = null;
                        } catch (ParseException e) {
                        }
                    } else if (name.startsWith("GBP CHEQUE")) {
                        Pattern compiledPattern;
                        try {
                            compiledPattern = Pattern.compile("GBP CHEQUE\\s*(\\d\\d\\d\\d\\d\\d)");
                            Matcher m = compiledPattern.matcher(name);
                            if (m.matches()) {
                                checkNumber = m.group(1);
                            }
                        } catch (PatternSyntaxException e) {
                            compiledPattern = null;
                        }
                    } else {
                        memo = stmtTrnElement.getString("MEMO");
                    }
                } else {
                    memo = stmtTrnElement.getString("NAME");
                }
                if (fitIds.contains(fitid)) {
                    continue;
                }
                Collection<Entry> possibleMatches = new ArrayList<Entry>();
                for (Entry entry : accountOutsideTransaction.getEntries()) {
                    if (entry.getPropertyValue(OfxEntryInfo.getFitidAccessor()) == null && entry.getAmount() == amount) {
                        System.out.println("amount: " + amount);
                        if (entry.getCheck() == null) {
                            if (entry.getTransaction().getDate().equals(postedDate)) {
                                possibleMatches.add(entry);
                                break;
                            } else {
                                Calendar fiveDaysLater = Calendar.getInstance();
                                fiveDaysLater.setTime(entry.getTransaction().getDate());
                                fiveDaysLater.add(Calendar.DAY_OF_MONTH, 5);
                                if ((checkNumber == null || checkNumber.length() == 0) && (postedDate.equals(entry.getTransaction().getDate()) || postedDate.after(entry.getTransaction().getDate())) && postedDate.before(fiveDaysLater.getTime())) {
                                    possibleMatches.add(entry);
                                }
                            }
                        } else {
                            Calendar twentyDaysLater = Calendar.getInstance();
                            twentyDaysLater.setTime(entry.getTransaction().getDate());
                            twentyDaysLater.add(Calendar.DAY_OF_MONTH, 20);
                            if (entry.getCheck().equals(checkNumber) && (postedDate.equals(entry.getTransaction().getDate()) || postedDate.after(entry.getTransaction().getDate())) && postedDate.before(twentyDaysLater.getTime())) {
                                possibleMatches.add(entry);
                                break;
                            }
                        }
                    }
                }
                if (possibleMatches.size() == 1) {
                    Entry match = possibleMatches.iterator().next();
                    Entry entryInTrans = transactionManager.getCopyInTransaction(match);
                    entryInTrans.setValuta(postedDate);
                    entryInTrans.setCheck(checkNumber);
                    entryInTrans.setPropertyValue(OfxEntryInfo.getFitidAccessor(), fitid);
                    continue;
                }
                Transaction transaction = session.createTransaction();
                Entry firstEntry = transaction.createEntry();
                firstEntry.setAccount(account);
                firstEntry.setPropertyValue(OfxEntryInfo.getFitidAccessor(), fitid);
                transaction.setDate(transactionDate);
                firstEntry.setValuta(postedDate);
                firstEntry.setCheck(checkNumber);
                firstEntry.setAmount(amount);
                Entry otherEntry = transaction.createEntry();
                otherEntry.setAmount(-amount);
                String textToMatch = "";
                if (memo != null) {
                    textToMatch += "memo=" + memo;
                }
                String trnType = stmtTrnElement.getString("TRNTYPE");
                if (trnType != null) {
                    textToMatch += "type=" + trnType;
                }
                BigDecimal myAmount = new BigDecimal(amount).scaleByPowerOfTen(-2);
                textToMatch += "amount=" + myAmount;
                String defaultDescription;
                if (memo != null) {
                    defaultDescription = MessageFormat.format("{0}: {1}", trnType.toLowerCase(), toTitleCase(memo));
                } else {
                    defaultDescription = trnType.toLowerCase();
                }
                matcher.matchAndFill(textToMatch, firstEntry, otherEntry, toTitleCase(memo), defaultDescription);
            } else {
                System.out.println("Unknown element ignored: " + transactionElement.getTagName());
                String elementXml = transactionElement.toXMLString(0);
                System.out.println(elementXml);
            }
        }
    }

    private void importStockStatement(TransactionManager transactionManager, SimpleElement rootElement, Session session, Session sessionOutsideTransaction, SimpleElement statementResultElement) throws TagNotFoundException {
        SimpleElement accountFromElement = statementResultElement.getDescendant("INVACCTFROM");
        String accountNumber = accountFromElement.getString("ACCTID");
        StockAccount account = null;
        StockAccount accountOutsideTransaction = null;
        for (Account eachAccount : sessionOutsideTransaction.getAccountCollection()) {
            if (eachAccount instanceof StockAccount) {
                StockAccount stockAccount = (StockAccount) eachAccount;
                if (accountNumber.equals(stockAccount.getAccountNumber())) {
                    accountOutsideTransaction = stockAccount;
                    account = transactionManager.getCopyInTransaction(accountOutsideTransaction);
                }
            }
        }
        if (account == null) {
            MessageDialog.openError(window.getShell(), "No Matching Account Found", "The OFX file contains data for brokerage account number " + accountNumber + ".  However no stock account exists with such an account number.  You probably need to set the account number property for the appropriate account.");
            return;
        }
        StockAccount worthlessStockAccountOutsideTransaction;
        try {
            worthlessStockAccountOutsideTransaction = (StockAccount) sessionOutsideTransaction.getAccountByShortName("worthless stock and options");
        } catch (SeveralAccountsFoundException e) {
            throw new RuntimeException(e);
        } catch (NoAccountFoundException e) {
            throw new RuntimeException(e);
        }
        StockAccount worthlessStockAccount = transactionManager.getCopyInTransaction(worthlessStockAccountOutsideTransaction);
        SimpleElement currencyElement = rootElement.findElement("CURDEF");
        if (currencyElement != null) {
            String currencyCode = currencyElement.getTrimmedText();
            if (!account.getCurrency().getCode().equals(currencyCode)) {
                MessageDialog.openError(window.getShell(), "Currency Mismatch", MessageFormat.format("A currency mismatch prevents the import.  The OFX file indicates it contains entries in {0} but the {2} account uses {1}.", currencyCode, account.getCurrency().getCode(), account.getName()));
                return;
            }
        }
        if (account.getDividendAccount() == null) {
            MessageDialog.openError(window.getShell(), "Account Not Configured", "The " + account.getName() + " account does not have an account set to hold the dividend payments.  Select the " + account.getName() + " from the Navigator view, then open the properties view and select a dividend account.");
            return;
        }
        if (account.getCommissionAccount() == null) {
            MessageDialog.openError(window.getShell(), "Account Not Configured", "The " + account.getName() + " account does not have an account set to hold the commission amounts.  Select the " + account.getName() + " from the Navigator view, then open the properties view and select a commission account.");
            return;
        }
        if (account.getTax1Account() == null) {
            MessageDialog.openError(window.getShell(), "Account Not Configured", "The " + account.getName() + " account does not have an account set to hold the tax 1 payments.  Select the " + account.getName() + " from the Navigator view, then open the properties view and select a tax 1 account.");
            return;
        }
        SimpleElement secList = rootElement.getDescendant("SECLISTMSGSRSV1", "SECLIST");
        for (SimpleElement securityElement : secList.getChildElements()) {
            if (securityElement.getTagName().equals("STOCKINFO") || securityElement.getTagName().equals("MFINFO")) {
                SimpleElement secInfoElement = securityElement.findElement("SECINFO");
                SimpleElement secIdElement = securityElement.findElement("SECID");
                String name = toTitleCase(secInfoElement.getString("SECNAME"));
                String symbol = secInfoElement.getString("TICKER");
                Stock stock = findStock(session, secIdElement);
                String defaultName = secIdElement.getString("UNIQUEIDTYPE") + ": " + secIdElement.getString("UNIQUEID");
                if (stock.getName().equals(defaultName)) {
                    stock.setName(name);
                }
                if (stock.getSymbol() == null) {
                    stock.setSymbol(symbol);
                }
            } else {
                System.out.println("unknown element in SECLIST");
                String elementXml = securityElement.toXMLString(0);
                System.out.println(elementXml);
            }
        }
        Set<String> fitIds = new HashSet<String>();
        for (Entry entry : accountOutsideTransaction.getEntries()) {
            String fitId = entry.getPropertyValue(OfxEntryInfo.getFitidAccessor());
            if (fitId != null) {
                fitIds.add(fitId);
            }
        }
        SimpleElement transListElement = statementResultElement.getDescendant("INVTRANLIST");
        ImportMatcher matcher = new ImportMatcher(account.getExtension(PatternMatcherAccountInfo.getPropertySet(), true));
        for (SimpleElement transactionElement : transListElement.getChildElements()) {
            if (transactionElement.getTagName().equals("DTSTART")) {
            } else if (transactionElement.getTagName().equals("DTEND")) {
            } else if (transactionElement.getTagName().equals("INVBANKTRAN")) {
                SimpleElement stmtTrnElement = transactionElement.findElement("STMTTRN");
                Date postedDate = stmtTrnElement.getDate("DTPOSTED");
                long amount = stmtTrnElement.getAmount("TRNAMT");
                String fitid = stmtTrnElement.getString("FITID");
                String memo = stmtTrnElement.getString("MEMO");
                String checkNumber = stmtTrnElement.getString("CHECKNUM");
                if (checkNumber != null) {
                    if (checkNumber.equals("0")) {
                        checkNumber = null;
                    }
                }
                if (fitIds.contains(fitid)) {
                    continue;
                }
                Collection<Entry> possibleMatches = new ArrayList<Entry>();
                for (Entry entry : accountOutsideTransaction.getEntries()) {
                    if (entry.getPropertyValue(OfxEntryInfo.getFitidAccessor()) == null && entry.getAmount() == amount) {
                        System.out.println("amount: " + amount);
                        if (entry.getCheck() == null) {
                            if (entry.getTransaction().getDate().equals(postedDate)) {
                                possibleMatches.add(entry);
                                break;
                            } else {
                                Calendar fiveDaysLater = Calendar.getInstance();
                                fiveDaysLater.setTime(entry.getTransaction().getDate());
                                fiveDaysLater.add(Calendar.DAY_OF_MONTH, 5);
                                if ((checkNumber == null || checkNumber.length() == 0) && (postedDate.equals(entry.getTransaction().getDate()) || postedDate.after(entry.getTransaction().getDate())) && postedDate.before(fiveDaysLater.getTime())) {
                                    possibleMatches.add(entry);
                                }
                            }
                        } else {
                            Calendar twentyDaysLater = Calendar.getInstance();
                            twentyDaysLater.setTime(entry.getTransaction().getDate());
                            twentyDaysLater.add(Calendar.DAY_OF_MONTH, 20);
                            if (entry.getCheck().equals(checkNumber) && (postedDate.equals(entry.getTransaction().getDate()) || postedDate.after(entry.getTransaction().getDate())) && postedDate.before(twentyDaysLater.getTime())) {
                                possibleMatches.add(entry);
                                break;
                            }
                        }
                    }
                }
                if (possibleMatches.size() == 1) {
                    Entry match = possibleMatches.iterator().next();
                    Entry entryInTrans = transactionManager.getCopyInTransaction(match);
                    entryInTrans.setValuta(postedDate);
                    entryInTrans.setCheck(checkNumber);
                    entryInTrans.setPropertyValue(OfxEntryInfo.getFitidAccessor(), fitid);
                    continue;
                }
                Transaction transaction = session.createTransaction();
                Entry firstEntry = transaction.createEntry();
                firstEntry.setAccount(account);
                firstEntry.setPropertyValue(OfxEntryInfo.getFitidAccessor(), fitid);
                transaction.setDate(postedDate);
                firstEntry.setValuta(postedDate);
                firstEntry.setAmount(amount);
                Entry otherEntry = transaction.createEntry();
                otherEntry.setAmount(-amount);
                String trnType = stmtTrnElement.getString("TRNTYPE");
                String textToMatch = MessageFormat.format("TRNTYPE={0}\nMEMO={1}", trnType, memo);
                String defaultDescription = MessageFormat.format("{0}: {1}", trnType.toLowerCase(), toTitleCase(memo));
                matcher.matchAndFill(textToMatch, firstEntry, otherEntry, toTitleCase(memo), defaultDescription);
            } else {
                SimpleElement invTransElement = transactionElement.findElement("INVTRAN");
                if (invTransElement == null) {
                    String elementXml = transactionElement.toXMLString(0);
                    System.out.println(elementXml);
                    throw new RuntimeException("missing INVTRAN");
                }
                String fitid = invTransElement.getString("FITID");
                Date tradeDate = invTransElement.getDate("DTTRADE");
                Date settleDate = invTransElement.getDate("DTSETTLE");
                String memo = invTransElement.getString("MEMO");
                if (fitIds.contains(fitid)) {
                    continue;
                }
                Transaction transaction = session.createTransaction();
                Entry firstEntry = transaction.createEntry();
                firstEntry.setAccount(account);
                firstEntry.setPropertyValue(OfxEntryInfo.getFitidAccessor(), fitid);
                SimpleElement secIdElement = transactionElement.findElement("SECID");
                Stock stock = findStock(session, secIdElement);
                memo = memo.replace(stock.getName().toUpperCase(), "");
                if (stock.getSymbol() != null) {
                    memo.replace(stock.getSymbol(), "<ticker>");
                }
                if (stock.getCusip() != null) {
                    memo.replace(stock.getCusip(), "<CUSIP>");
                }
                transaction.setDate(tradeDate);
                firstEntry.setValuta(settleDate);
                long total = 0;
                if (!transactionElement.getTagName().startsWith("JRNLSEC") && !transactionElement.getTagName().startsWith("CLOSUREOPT") && !transactionElement.getTagName().startsWith("TRANSFER")) {
                    total = transactionElement.getAmount("TOTAL");
                    firstEntry.setAmount(total);
                } else {
                    System.out.println("here");
                }
                firstEntry.setMemo(memo);
                if (transactionElement.getTagName().startsWith("BUY") || transactionElement.getTagName().startsWith("SELL")) {
                    String units = transactionElement.getString("UNITS");
                    String unitPrice = transactionElement.getString("UNITPRICE");
                    long commission = transactionElement.getAmount("COMMISSION", 0);
                    if (commission != 0) {
                        StockEntry commissionEntry = transaction.createEntry().getExtension(StockEntryInfo.getPropertySet(), true);
                        commissionEntry.setAccount(account.getCommissionAccount());
                        commissionEntry.setAmount(commission);
                        commissionEntry.setSecurity(stock);
                    }
                    long fees = transactionElement.getAmount("FEES", 0);
                    if (fees != 0) {
                        StockEntry feesEntry = transaction.createEntry().getExtension(StockEntryInfo.getPropertySet(), true);
                        feesEntry.setAccount(account.getTax1Account());
                        feesEntry.setAmount(fees);
                        feesEntry.setSecurity(stock);
                    }
                    if (units == null) {
                        units = "1";
                    }
                    Long quantity = stock.parse(units);
                    StockEntry saleEntry = transaction.createEntry().getExtension(StockEntryInfo.getPropertySet(), true);
                    saleEntry.setAccount(account);
                    if (transactionElement.getTagName().startsWith("BUY")) {
                        saleEntry.setAmount(quantity);
                    } else {
                        saleEntry.setAmount(quantity);
                    }
                    saleEntry.setCommodity(stock);
                    if (transactionElement.getTagName().equals("BUYMF")) {
                    } else if (transactionElement.getTagName().equals("BUYSTOCK")) {
                    } else if (transactionElement.getTagName().equals("BUYOTHER")) {
                    } else if (transactionElement.getTagName().equals("SELLMF")) {
                    } else if (transactionElement.getTagName().equals("SELLSTOCK")) {
                    } else if (transactionElement.getTagName().equals("SELLOTHER")) {
                    } else {
                        System.out.println("unknown element: " + transactionElement.getTagName());
                        String elementXml = transactionElement.toXMLString(0);
                        System.out.println(elementXml);
                        throw new RuntimeException("unknown element: " + transactionElement.getTagName());
                    }
                } else if (transactionElement.getTagName().equals("INCOME") || transactionElement.getTagName().equals("REINVEST")) {
                    String reinvestMemo = "";
                    String incomeType = transactionElement.getString("INCOMETYPE");
                    if ("DIV".equals(incomeType)) {
                        StockEntry dividendEntry = transaction.createEntry().getExtension(StockEntryInfo.getPropertySet(), true);
                        dividendEntry.setAccount(account.getDividendAccount());
                        dividendEntry.setAmount(-total);
                        dividendEntry.setMemo("dividend");
                        dividendEntry.setSecurity(stock);
                        reinvestMemo = " dividend";
                    } else if ("CGLONG".equals(incomeType)) {
                        StockEntry dividendEntry = transaction.createEntry().getExtension(StockEntryInfo.getPropertySet(), true);
                        dividendEntry.setAccount(account.getDividendAccount());
                        dividendEntry.setAmount(-total);
                        dividendEntry.setMemo("capitial gains distribution - long term");
                        dividendEntry.setSecurity(stock);
                        reinvestMemo = " capital gains";
                    } else {
                        Entry otherEntry = transaction.createEntry();
                        otherEntry.setAmount(-total);
                        String textToMatch = MessageFormat.format("INCOMETYPE={0}\nMEMO={1}", incomeType, memo);
                        String defaultDescription = MessageFormat.format("{0}: {1}", incomeType.toLowerCase(), toTitleCase(memo));
                        matcher.matchAndFill(textToMatch, firstEntry, otherEntry, toTitleCase(memo), defaultDescription);
                    }
                    if (transactionElement.getTagName().equals("REINVEST")) {
                        Transaction reinvestTransaction = session.createTransaction();
                        Entry firstReinvestEntry = reinvestTransaction.createEntry();
                        firstReinvestEntry.setAccount(account);
                        firstReinvestEntry.setPropertyValue(OfxEntryInfo.getFitidAccessor(), fitid);
                        reinvestTransaction.setDate(tradeDate);
                        firstReinvestEntry.setValuta(settleDate);
                        firstReinvestEntry.setAmount(total);
                        firstReinvestEntry.setMemo("re-invest" + reinvestMemo);
                        String units = transactionElement.getString("UNITS");
                        Long quantity = stock.parse(units);
                        StockEntry buyEntry = reinvestTransaction.createEntry().getExtension(StockEntryInfo.getPropertySet(), true);
                        buyEntry.setAccount(account);
                        buyEntry.setAmount(quantity);
                        buyEntry.setCommodity(stock);
                    }
                } else if (transactionElement.getTagName().equals("TRANSFER")) {
                    String units = transactionElement.getString("UNITS");
                    Long quantity = stock.parse(units);
                    if (quantity != 0) {
                        StockEntry firstStockEntry = firstEntry.getExtension(StockEntryInfo.getPropertySet(), true);
                        firstStockEntry.setAmount(quantity);
                        firstStockEntry.setCommodity(stock);
                        StockEntry otherStockEntry = transaction.createEntry().getExtension(StockEntryInfo.getPropertySet(), true);
                        otherStockEntry.setAccount(account);
                        otherStockEntry.setAmount(-quantity);
                        otherStockEntry.setCommodity(stock);
                    }
                } else if (transactionElement.getTagName().equals("CLOSUREOPT")) {
                    String units = transactionElement.getString("UNITS");
                    Long quantity = stock.parse(units);
                    StockEntry firstStockEntry = firstEntry.getExtension(StockEntryInfo.getPropertySet(), true);
                    firstStockEntry.setAmount(quantity);
                    firstStockEntry.setCommodity(stock);
                    StockEntry otherStockEntry = transaction.createEntry().getExtension(StockEntryInfo.getPropertySet(), true);
                    otherStockEntry.setAccount(worthlessStockAccount);
                    otherStockEntry.setAmount(-quantity);
                    otherStockEntry.setCommodity(stock);
                } else if (transactionElement.getTagName().equals("JRNLSEC")) {
                    String units = transactionElement.getString("UNITS");
                    Long quantity = stock.parse(units);
                    StockEntry firstStockEntry = firstEntry.getExtension(StockEntryInfo.getPropertySet(), true);
                    firstStockEntry.setAmount(quantity);
                    firstStockEntry.setCommodity(stock);
                    StockEntry otherStockEntry = transaction.createEntry().getExtension(StockEntryInfo.getPropertySet(), true);
                    otherStockEntry.setAccount(account);
                    otherStockEntry.setAmount(-quantity);
                    otherStockEntry.setCommodity(stock);
                } else {
                    System.out.println("unknown element: " + transactionElement.getTagName());
                    String elementXml = transactionElement.toXMLString(0);
                    System.out.println(elementXml);
                    throw new RuntimeException("unknown element: " + transactionElement.getTagName());
                }
            }
        }
    }

    private String toTitleCase(String text) {
        if (text == null) {
            return null;
        }
        String lowerCaseText = text.toLowerCase();
        char[] charArray = lowerCaseText.toCharArray();
        Pattern pattern = Pattern.compile("\\b([a-z])");
        Matcher matcher = pattern.matcher(lowerCaseText);
        while (matcher.find()) {
            int index = matcher.end(1) - 1;
            charArray[index] = Character.toUpperCase(charArray[index]);
        }
        return new String(charArray);
    }

    private Stock findStock(Session session, SimpleElement secIdElement) {
        String uniqueId = secIdElement.getString("UNIQUEID");
        String uniqueIdType = secIdElement.getString("UNIQUEIDTYPE");
        ScalarPropertyAccessor<String> securityIdField = null;
        if ("CUSIP".equals(uniqueIdType)) {
            securityIdField = SecurityInfo.getCusipAccessor();
        } else {
            securityIdField = SecurityInfo.getSymbolAccessor();
        }
        if (uniqueId.length() == 0) {
            throw new RuntimeException("can this ever happen?");
        }
        Stock stock = null;
        for (Commodity commodity : session.getCommodityCollection()) {
            if (commodity instanceof Stock) {
                Stock eachStock = (Stock) commodity;
                if (uniqueId.equals(eachStock.getPropertyValue(securityIdField))) {
                    stock = eachStock;
                    break;
                }
            }
        }
        if (stock == null) {
            stock = session.createCommodity(StockInfo.getPropertySet());
            if (securityIdField != null) {
                stock.setPropertyValue(securityIdField, uniqueId);
            }
            stock.setName(uniqueIdType + ": " + uniqueId);
        }
        return stock;
    }
}
