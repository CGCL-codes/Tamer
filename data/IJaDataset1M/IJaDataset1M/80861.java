package net.sf.jmoney.ameritrade;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import net.sf.jmoney.importer.wizards.AssociationMetadata;
import net.sf.jmoney.importer.wizards.CsvImportWizard;
import net.sf.jmoney.importer.wizards.ImportException;
import net.sf.jmoney.model2.Account;
import net.sf.jmoney.model2.Commodity;
import net.sf.jmoney.model2.Entry;
import net.sf.jmoney.model2.Session;
import net.sf.jmoney.model2.Session.NoAccountFoundException;
import net.sf.jmoney.model2.Session.SeveralAccountsFoundException;
import net.sf.jmoney.model2.Transaction;
import net.sf.jmoney.stocks.model.Bond;
import net.sf.jmoney.stocks.model.BondInfo;
import net.sf.jmoney.stocks.model.Security;
import net.sf.jmoney.stocks.model.Stock;
import net.sf.jmoney.stocks.model.StockAccount;
import net.sf.jmoney.stocks.model.StockEntry;
import net.sf.jmoney.stocks.model.StockEntryInfo;
import net.sf.jmoney.stocks.model.StockInfo;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

/**
 * A wizard to import data from a comma-separated file that has been downloaded
 * from Ameritrade.
 */
public class AmeritradeImportWizard extends CsvImportWizard {

    private ImportedDateColumn column_date = new ImportedDateColumn("DATE", new SimpleDateFormat("MM/dd/yyyy"));

    private ImportedTextColumn column_uniqueId = new ImportedTextColumn("TRANSACTION ID");

    private ImportedTextColumn column_description = new ImportedTextColumn("DESCRIPTION");

    private ImportedTextColumn column_quantity = new ImportedTextColumn("QUANTITY");

    private ImportedTextColumn column_symbol = new ImportedTextColumn("SYMBOL");

    private ImportedTextColumn column_price = new ImportedTextColumn("PRICE");

    private ImportedTextColumn column_commission = new ImportedTextColumn("COMMISSION");

    private ImportedTextColumn column_amount = new ImportedTextColumn("AMOUNT");

    private ImportedTextColumn column_salesFee = new ImportedTextColumn("SALES FEE");

    private ImportedTextColumn column_shortTermRedemptionFee = new ImportedTextColumn("SHORT-TERM RDM FEE");

    private ImportedTextColumn column_fundRedemptionFee = new ImportedTextColumn("FUND REDEMPTION FEE");

    private ImportedTextColumn column_deferredSalesCharge = new ImportedTextColumn("DEFERRED SALES CHARGE");

    Pattern patternAdr;

    Pattern patternForeignTax;

    Pattern patternBondInterest;

    Pattern patternBondInterestRate;

    Pattern patternBondMaturityDate;

    Pattern patternMandatoryReverseSplit;

    private StockAccount account;

    private MultiRowTransaction currentMultiRowProcessor = null;

    private long priorIntraAccountTransferAmount = 0;

    private Account interestAccount;

    private Account expensesAccount;

    private Session session;

    private static DateFormat maturityDateFormat = new SimpleDateFormat("MM/dd/yyyy");

    public AmeritradeImportWizard() {
        IDialogSettings workbenchSettings = Activator.getDefault().getDialogSettings();
        IDialogSettings section = workbenchSettings.getSection("AmeritradeImportWizard");
        if (section == null) {
            section = workbenchSettings.addNewSection("AmeritradeImportWizard");
        }
        setDialogSettings(section);
    }

    @Override
    protected void setAccount(Account accountInsideTransaction) throws ImportException {
        if (!(accountInsideTransaction instanceof StockAccount)) {
            throw new ImportException("Bad configuration: This import can be used for stock accounts only.");
        }
        this.account = (StockAccount) accountInsideTransaction;
        this.session = accountInsideTransaction.getSession();
        currentMultiRowProcessor = null;
        priorIntraAccountTransferAmount = 0;
        try {
            interestAccount = session.getAccountByShortName("Interest - Ameritrade");
        } catch (NoAccountFoundException e) {
            MessageDialog.openError(Display.getDefault().getActiveShell(), "Account not Set Up", "No account exists called 'Interest - Ameritrade'");
            throw new RuntimeException(e);
        } catch (SeveralAccountsFoundException e) {
            MessageDialog.openError(Display.getDefault().getActiveShell(), "Multiple Accounts Set Up", "Multiple accounts exists called 'Interest - Ameritrade'");
            throw new RuntimeException(e);
        }
        try {
            expensesAccount = session.getAccountByShortName("Stock - Expenses (US)");
        } catch (NoAccountFoundException e) {
            MessageDialog.openError(Display.getDefault().getActiveShell(), "Account not Set Up", "No account exists called 'Stock - Expenses (US)'");
            throw new RuntimeException(e);
        } catch (SeveralAccountsFoundException e) {
            MessageDialog.openError(Display.getDefault().getActiveShell(), "Multiple Accounts Set Up", "Multiple accounts exists called 'Stock - Expenses (US)'");
            throw new RuntimeException(e);
        }
        try {
            patternAdr = Pattern.compile("ADR FEES \\([A-Z]*\\)");
            patternForeignTax = Pattern.compile("FOREIGN TAX WITHHELD \\([A-Z]*\\)");
            patternBondInterest = Pattern.compile("INTEREST INCOME - SECURITIES \\(([0-9,A-Z]*)\\)");
            patternBondInterestRate = Pattern.compile(" coupon\\: (\\d\\.\\d\\d)\\%");
            patternBondMaturityDate = Pattern.compile(" maturity: (\\d\\d/\\d\\d/20\\d\\d)");
            patternMandatoryReverseSplit = Pattern.compile("MANDATORY REVERSE SPLIT \\(([0-9,A-Z]*)\\)");
        } catch (PatternSyntaxException e) {
            throw new RuntimeException("pattern failed");
        }
    }

    @Override
    public void importLine(String[] line) throws ImportException {
        Date date = column_date.getDate();
        String uniqueId = line[1];
        String memo = line[2];
        String quantityString = line[3];
        String security = line[4];
        String price = line[5];
        String commissionString = line[6];
        String totalString = line[7];
        String salesFee = line[8];
        String shortTermRedemptionFee = line[9];
        String fundRedemptionFee = line[10];
        String deferredSalesCharge = line[11];
        Long total = getAmount(totalString);
        if (total == 0) {
            total = getAmount(shortTermRedemptionFee);
        }
        Long commission = getAmount(commissionString);
        long totalSalesFee = getAmount(salesFee) + getAmount(deferredSalesCharge);
        long totalRedemptionFee = getAmount(shortTermRedemptionFee) + getAmount(fundRedemptionFee);
        boolean processed = false;
        if (currentMultiRowProcessor != null) {
            processed = currentMultiRowProcessor.process(date, memo, security, quantityString, total, session, account);
            if (currentMultiRowProcessor.isDone()) {
                currentMultiRowProcessor.createTransaction(session, account);
                currentMultiRowProcessor = null;
            }
        }
        if (!processed) {
            Matcher matcher;
            if (memo.startsWith("Sold ") || memo.startsWith("Bought ")) {
                Security stockOrBond;
                if (security.isEmpty()) {
                    if (memo.equals("Bought " + quantityString + "M @ " + price)) {
                        Bond unknownBond = null;
                        for (Commodity commodity : session.getCommodityCollection()) {
                            if (commodity instanceof Bond) {
                                Bond eachBond = (Bond) commodity;
                                if (eachBond.getName().equals("unknown bond")) {
                                    unknownBond = eachBond;
                                    break;
                                }
                            }
                        }
                        if (unknownBond == null) {
                            unknownBond = session.createCommodity(BondInfo.getPropertySet());
                            unknownBond.setName("unknown bond");
                        }
                        stockOrBond = unknownBond;
                    } else {
                        throw new RuntimeException("unknown row");
                    }
                    quantityString += "000";
                } else {
                    stockOrBond = getStockBySymbol(session, security);
                }
                long quantity = stockOrBond.parse(quantityString);
                Transaction trans = session.createTransaction();
                trans.setDate(date);
                StockEntry mainEntry = createStockEntry(trans);
                mainEntry.setAccount(account);
                mainEntry.setAmount(total);
                StockEntry saleEntry = createStockEntry(trans);
                saleEntry.setAccount(account);
                if (memo.startsWith("Bought ")) {
                    saleEntry.setAmount(quantity);
                } else {
                    saleEntry.setAmount(-quantity);
                }
                saleEntry.setCommodity(stockOrBond);
                if (commission != null && commission.longValue() != 0) {
                    StockEntry commissionEntry = trans.createEntry().getExtension(StockEntryInfo.getPropertySet(), true);
                    commissionEntry.setAccount(account.getCommissionAccount());
                    commissionEntry.setAmount(commission);
                    commissionEntry.setSecurity(stockOrBond);
                }
                if (totalSalesFee != 0) {
                    StockEntry entry = trans.createEntry().getExtension(StockEntryInfo.getPropertySet(), true);
                    entry.setAccount(account.getTax1Account());
                    entry.setAmount(totalSalesFee);
                    entry.setSecurity(stockOrBond);
                }
                if (totalRedemptionFee != 0) {
                    StockEntry entry = trans.createEntry().getExtension(StockEntryInfo.getPropertySet(), true);
                    entry.setAccount(account.getTax2Account());
                    entry.setAmount(totalRedemptionFee);
                    entry.setSecurity(stockOrBond);
                }
            } else if ((matcher = patternBondInterest.matcher(memo)).matches()) {
                Transaction trans = session.createTransaction();
                trans.setDate(date);
                Entry mainEntry = trans.createEntry();
                mainEntry.setAccount(account);
                mainEntry.setMemo("bond interest");
                mainEntry.setAmount(total);
                StockEntry otherEntry = trans.createEntry().getExtension(StockEntryInfo.getPropertySet(), true);
                otherEntry.setAccount(account.getDividendAccount());
                otherEntry.setMemo("bond interest");
                otherEntry.setAmount(-total);
                String cusip = matcher.group(1);
                Security security2 = findSecurityByCusip(session, cusip);
                if (security2 != null && !(security2 instanceof Bond)) {
                    throw new RuntimeException("Bond sale entry, but cusip matches a non-bond security");
                }
                Bond bond = (Bond) security2;
                for (Commodity commodity : session.getCommodityCollection()) {
                    if (commodity instanceof Bond) {
                        Bond eachBond = (Bond) commodity;
                        if (security.equals(eachBond.getCusip())) {
                            bond = eachBond;
                            break;
                        }
                    }
                }
                if (bond == null) {
                    Matcher interestRateMatcher = patternBondInterestRate.matcher(quantityString);
                    Matcher maturityDateMatcher = patternBondMaturityDate.matcher(security);
                    if (interestRateMatcher.matches() && maturityDateMatcher.matches()) {
                        String maturityDateString = maturityDateMatcher.group(1);
                        String interestRateString = interestRateMatcher.group(1);
                        Date maturityDate;
                        try {
                            maturityDate = maturityDateFormat.parse(maturityDateString);
                        } catch (ParseException e) {
                            throw new ImportException("bad maturity date");
                        }
                        bond = session.createCommodity(BondInfo.getPropertySet());
                        bond.setName(cusip);
                        bond.setCusip(cusip);
                        bond.setMaturityDate(maturityDate);
                        bond.setInterestRate(new BigDecimal(interestRateString).multiply(new BigDecimal(100)).intValueExact());
                    } else {
                        throw new RuntimeException("invalid bond interest");
                    }
                }
                otherEntry.setSecurity(bond);
            } else if (memo.startsWith("QUALIFIED DIVIDEND ")) {
                Stock stock = getStockBySymbol(session, security);
                Transaction trans = session.createTransaction();
                trans.setDate(date);
                Entry mainEntry = trans.createEntry();
                mainEntry.setAccount(account);
                mainEntry.setMemo("qualified dividend");
                mainEntry.setAmount(total);
                StockEntry otherEntry = trans.createEntry().getExtension(StockEntryInfo.getPropertySet(), true);
                otherEntry.setAccount(account.getDividendAccount());
                otherEntry.setMemo("qualified");
                otherEntry.setAmount(-total);
                otherEntry.setSecurity(stock);
            } else if (memo.equals("W-8 WITHHOLDING") || memo.startsWith("BACKUP WITHHOLDING (W-9)")) {
                Transaction trans = session.createTransaction();
                trans.setDate(date);
                Entry mainEntry = trans.createEntry();
                mainEntry.setAccount(account);
                mainEntry.setMemo("withholding");
                mainEntry.setAmount(total);
                Entry otherEntry = trans.createEntry();
                otherEntry.setAccount(account.getWithholdingTaxAccount());
                if (memo.equals("W-8 WITHHOLDING")) {
                    otherEntry.setMemo("W-8");
                } else {
                    otherEntry.setMemo("W-9");
                }
                otherEntry.setAmount(-total);
            } else if (memo.equals("FREE BALANCE INTEREST ADJUSTMENT")) {
                Transaction trans = session.createTransaction();
                trans.setDate(date);
                Entry mainEntry = trans.createEntry();
                mainEntry.setAccount(account);
                mainEntry.setMemo("interest");
                mainEntry.setAmount(total);
                Entry otherEntry = trans.createEntry();
                otherEntry.setAccount(interestAccount);
                otherEntry.setAmount(-total);
            } else if (memo.equals("MARGIN INTEREST ADJUSTMENT")) {
                Transaction trans = session.createTransaction();
                trans.setDate(date);
                Entry mainEntry = trans.createEntry();
                mainEntry.setAccount(account);
                mainEntry.setMemo("margin interest");
                mainEntry.setAmount(total);
                Entry otherEntry = trans.createEntry();
                otherEntry.setAccount(interestAccount);
                otherEntry.setAmount(-total);
            } else if (memo.startsWith("FOREIGN TAX WITHHELD ")) {
                Stock stock = getStockBySymbol(session, security);
                Transaction trans = session.createTransaction();
                trans.setDate(date);
                Entry mainEntry = trans.createEntry();
                mainEntry.setAccount(account);
                mainEntry.setMemo("Foreign tax withheld");
                mainEntry.setAmount(total);
                StockEntry otherEntry = trans.createEntry().getExtension(StockEntryInfo.getPropertySet(), true);
                otherEntry.setAccount(expensesAccount);
                otherEntry.setMemo("Foreign tax withheld???");
                otherEntry.setAmount(-total);
                otherEntry.setSecurity(stock);
            } else if ((matcher = patternAdr.matcher(memo)).matches()) {
                Stock stock = getStockBySymbol(session, security);
                Transaction trans = session.createTransaction();
                trans.setDate(date);
                Entry mainEntry = trans.createEntry();
                mainEntry.setAccount(account);
                mainEntry.setMemo("ADR fee");
                mainEntry.setAmount(total);
                StockEntry otherEntry = createStockEntry(trans);
                otherEntry.setAccount(expensesAccount);
                otherEntry.setMemo("ADR fee");
                otherEntry.setAmount(-total);
                otherEntry.setSecurity(stock);
            } else if (memo.equals("INTRA-ACCOUNT TRANSFER")) {
                if (priorIntraAccountTransferAmount == 0) {
                    priorIntraAccountTransferAmount = total;
                } else {
                    if (priorIntraAccountTransferAmount + total != 0) {
                        MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Unable to read CSV file", "Entry found with unbalanced account transfer: '" + memo + "'.");
                    }
                    priorIntraAccountTransferAmount = 0;
                }
            } else if ((matcher = patternMandatoryReverseSplit.matcher(memo)).matches()) {
                String extractedSecurity = matcher.group(1);
                if (currentMultiRowProcessor != null) {
                    throw new RuntimeException("something is wrong");
                }
                currentMultiRowProcessor = new MandatorySplit(session, date, extractedSecurity, quantityString);
            } else if (memo.startsWith("MANDATORY - EXCHANGE ")) {
                Stock stock = getStockBySymbol(session, security);
                Long quantity = stock.parse(quantityString);
                if (currentMultiRowProcessor != null) {
                    throw new RuntimeException("something is wrong");
                }
                currentMultiRowProcessor = new MandatoryExchange(date, quantity, stock);
            } else {
                MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Unable to read CSV file", "Entry found with unknown memo: '" + memo + "'.");
            }
        }
    }

    private Stock getStockBySymbol(Session session, String symbol) {
        Stock stock = null;
        if (symbol.length() != 0) {
            Security security = findSecurityBySymbol(session, symbol);
            if (security != null && !(security instanceof Stock)) {
                throw new RuntimeException("mismatched symbol");
            }
            stock = (Stock) security;
            if (stock == null) {
                stock = session.createCommodity(StockInfo.getPropertySet());
                stock.setName(symbol);
                stock.setSymbol(symbol);
            }
        }
        return stock;
    }

    private Security findSecurityBySymbol(Session session, String symbol) {
        for (Commodity commodity : session.getCommodityCollection()) {
            if (commodity instanceof Security) {
                Security eachSecurity = (Security) commodity;
                if (symbol.equals(eachSecurity.getSymbol())) {
                    return eachSecurity;
                }
            }
        }
        return null;
    }

    private Security findSecurityByCusip(Session session, String cusip) {
        for (Commodity commodity : session.getCommodityCollection()) {
            if (commodity instanceof Security) {
                Security eachSecurity = (Security) commodity;
                if (cusip.equals(eachSecurity.getCusip())) {
                    return eachSecurity;
                }
            }
        }
        return null;
    }

    private Stock findStockBySymbol(Session session, String symbol) {
        for (Commodity commodity : session.getCommodityCollection()) {
            if (commodity instanceof Stock) {
                Stock eachStock = (Stock) commodity;
                if (symbol.equals(eachStock.getSymbol())) {
                    return eachStock;
                }
            }
        }
        return null;
    }

    private Stock findStockByCusip(Session session, String cusip) {
        for (Commodity commodity : session.getCommodityCollection()) {
            if (commodity instanceof Stock) {
                Stock eachStock = (Stock) commodity;
                if (cusip.equals(eachStock.getCusip())) {
                    return eachStock;
                }
            }
        }
        return null;
    }

    public StockEntry createStockEntry(Transaction trans) {
        return trans.createEntry().getExtension(StockEntryInfo.getPropertySet(), true);
    }

    long getAmount(String amountString) {
        if (amountString.length() == 0) {
            return 0;
        }
        boolean negate = false;
        if (amountString.startsWith("-")) {
            amountString = amountString.substring(1);
            negate = true;
        }
        String parts[] = amountString.split("\\.");
        long amount = Long.parseLong(parts[0]) * 100;
        if (parts.length > 1) {
            amount += Long.parseLong(parts[1]);
        }
        if (negate) {
            amount = -amount;
        }
        return amount;
    }

    public interface MultiRowTransaction {

        /**
		 * 
		 * @return true if this row was processed, false if this row is not a
		 * 		part of this transaction and should be separately processed
		 * 		by the caller
		 */
        boolean process(Date date, String memo, String security, String quantityString, long total, Session session, StockAccount account);

        /**
		 * 
		 * @return true if this transaction has received all its row and is
		 * 		ready to be created in the datastore, false if there may be
		 * 		more rows in this transaction
		 */
        boolean isDone();

        void createTransaction(Session session, StockAccount account);
    }

    public class MandatoryExchange implements MultiRowTransaction {

        private Date date;

        private long originalQuantity;

        private Stock originalStock;

        private long newQuantity;

        private Stock newStock;

        private Date fractionalSharesDate;

        private long fractionalSharesAmount;

        private boolean done = false;

        /**
		 * Initial constructor called when first "Mandatory Exchange" row found.
		 * 
		 * @param date
		 * @param quantity
		 * @param stock
		 */
        public MandatoryExchange(Date date, long quantity, Stock stock) {
            this.date = date;
            this.originalQuantity = quantity;
            this.originalStock = stock;
        }

        /**
		 * Called when second "Mandatory Exchange" row found.
		 * 
		 * @param quantity
		 * @param stock
		 */
        private void setReplacementStock(long quantity, Stock stock) {
            this.newQuantity = quantity;
            this.newStock = stock;
        }

        /**
		 * Called when an amount indicating it is paid in lieu of fractional
		 * shares is found.  The transaction can complete if this row is not
		 * found.
		 * 
		 * @param date
		 * @param total
		 */
        private void setCashForFractionalShares(Date date, long total) {
            this.fractionalSharesDate = date;
            this.fractionalSharesAmount = total;
        }

        public void createTransaction(Session session, StockAccount account) {
            Transaction trans = session.createTransaction();
            trans.setDate(date);
            StockEntry originalSharesEntry = trans.createEntry().getExtension(StockEntryInfo.getPropertySet(), true);
            originalSharesEntry.setAccount(account);
            originalSharesEntry.setAmount(-originalQuantity);
            originalSharesEntry.setCommodity(originalStock);
            originalSharesEntry.setMemo("mandatory exchange");
            StockEntry newSharesEntry = trans.createEntry().getExtension(StockEntryInfo.getPropertySet(), true);
            newSharesEntry.setAccount(account);
            newSharesEntry.setAmount(newQuantity);
            newSharesEntry.setCommodity(newStock);
            newSharesEntry.setMemo("mandatory exchange");
            if (fractionalSharesAmount != 0) {
                StockEntry fractionalEntry = trans.createEntry().getExtension(StockEntryInfo.getPropertySet(), true);
                fractionalEntry.setAccount(account);
                fractionalEntry.setAmount(fractionalSharesAmount);
                fractionalEntry.setValuta(fractionalSharesDate);
                fractionalEntry.setCommodity(account.getCurrency());
                fractionalEntry.setSecurity(newStock);
                fractionalEntry.setMemo("cash in lieu of fractional shares");
            } else {
                StockEntry fractionalEntry = trans.createEntry().getExtension(StockEntryInfo.getPropertySet(), true);
                fractionalEntry.setAccount(account);
                fractionalEntry.setAmount(fractionalSharesAmount);
                fractionalEntry.setMemo("exchange of stock");
            }
        }

        public boolean process(Date date2, String memo, String security, String quantityString, long total, Session session, StockAccount account) {
            if (memo.startsWith("MANDATORY - EXCHANGE ")) {
                Stock stock = getStockBySymbol(session, security);
                Long quantity = stock.parse(quantityString);
                if (!this.date.equals(date)) {
                    throw new RuntimeException("dates don't match");
                }
                setReplacementStock(quantity, stock);
                return true;
            } else if (memo.startsWith("CASH IN LIEU OF FRACTIONAL SHARES ")) {
                setCashForFractionalShares(date, total);
                createTransaction(session, account);
                done = true;
                return true;
            }
            return false;
        }

        public boolean isDone() {
            return done;
        }
    }

    public class MandatorySplit implements MultiRowTransaction {

        private Date date;

        private long originalQuantity;

        private Stock originalStock;

        private long newQuantity;

        private Stock newStock;

        private Date fractionalSharesDate;

        private long fractionalSharesAmount;

        private Pattern patternMandatoryReverseSplit;

        private boolean done = false;

        /**
		 * Initial constructor called when first "Mandatory Reverse Split" row found.
		 * 
		 * @param date
		 * @param extractedSecurity
		 * @param quantityString
		 */
        public MandatorySplit(Session sessionInTransaction, Date date, String extractedSecurity, String quantityString) {
            this.date = date;
            originalStock = getStockBySymbolOrCusip(sessionInTransaction, extractedSecurity);
            originalQuantity = originalStock.parse(quantityString);
            try {
                patternMandatoryReverseSplit = Pattern.compile("MANDATORY REVERSE SPLIT \\(([0-9,A-Z]*)\\)");
            } catch (PatternSyntaxException e) {
                throw new RuntimeException("pattern failed", e);
            }
        }

        /**
		 * Called when second "Mandatory Exchange" row found.
		 * 
		 * @param quantity
		 * @param stock
		 */
        private void setReplacementStock(long quantity, Stock stock) {
            this.newQuantity = quantity;
            this.newStock = stock;
        }

        /**
		 * Called when an amount indicating it is paid in lieu of fractional
		 * shares is found.  The transaction can complete if this row is not
		 * found.
		 * 
		 * @param date
		 * @param total
		 */
        private void setCashForFractionalShares(Date date, long total) {
            this.fractionalSharesDate = date;
            this.fractionalSharesAmount = total;
        }

        public void createTransaction(Session sessionInTransaction, StockAccount account) {
            Transaction trans = sessionInTransaction.createTransaction();
            trans.setDate(date);
            StockEntry originalSharesEntry = trans.createEntry().getExtension(StockEntryInfo.getPropertySet(), true);
            originalSharesEntry.setAccount(account);
            originalSharesEntry.setAmount(-originalQuantity);
            originalSharesEntry.setCommodity(originalStock);
            originalSharesEntry.setMemo("mandatory reverse split");
            StockEntry newSharesEntry = trans.createEntry().getExtension(StockEntryInfo.getPropertySet(), true);
            newSharesEntry.setAccount(account);
            newSharesEntry.setAmount(newQuantity);
            newSharesEntry.setCommodity(newStock);
            newSharesEntry.setMemo("mandatory reverse split");
            if (fractionalSharesAmount != 0) {
                StockEntry fractionalEntry = trans.createEntry().getExtension(StockEntryInfo.getPropertySet(), true);
                fractionalEntry.setAccount(account);
                fractionalEntry.setAmount(fractionalSharesAmount);
                fractionalEntry.setValuta(fractionalSharesDate);
                fractionalEntry.setCommodity(account.getCurrency());
                fractionalEntry.setSecurity(newStock);
                fractionalEntry.setMemo("cash in lieu of fractional shares");
            } else {
                StockEntry fractionalEntry = trans.createEntry().getExtension(StockEntryInfo.getPropertySet(), true);
                fractionalEntry.setAccount(account);
                fractionalEntry.setAmount(fractionalSharesAmount);
                fractionalEntry.setMemo("exchange of stock");
            }
        }

        public boolean process(Date date, String memo, String securityIgnored, String quantityString, long total, Session sessionInTransaction, StockAccount account) {
            Matcher matcher = patternMandatoryReverseSplit.matcher(memo);
            if (matcher.matches()) {
                String security = matcher.group(1);
                Stock stock = getStockBySymbolOrCusip(sessionInTransaction, security);
                Long quantity = stock.parse(quantityString);
                if (!this.date.equals(date)) {
                    throw new RuntimeException("dates don't match");
                }
                setReplacementStock(quantity, stock);
                return true;
            } else if (memo.startsWith("CASH IN LIEU OF FRACTIONAL SHARES ")) {
                setCashForFractionalShares(date, total);
                createTransaction(sessionInTransaction, account);
                done = true;
                return true;
            }
            done = true;
            return false;
        }

        private Stock getStockBySymbolOrCusip(Session sessionInTransaction, String security) {
            Stock stock = findStockBySymbol(sessionInTransaction, security);
            if (stock == null) {
                stock = findStockByCusip(sessionInTransaction, security);
                if (stock == null) {
                    stock = sessionInTransaction.createCommodity(StockInfo.getPropertySet());
                    stock.setName(security);
                    if (security.length() <= 5) {
                        stock.setSymbol(security);
                    } else {
                        stock.setCusip(security);
                    }
                }
            }
            return stock;
        }

        public boolean isDone() {
            return done;
        }
    }

    @Override
    protected ImportedColumn[] getExpectedColumns() {
        return new ImportedColumn[] { column_date, column_uniqueId, column_description, column_quantity, column_symbol, column_price, column_commission, column_amount, column_salesFee, column_shortTermRedemptionFee, column_fundRedemptionFee, column_deferredSalesCharge };
    }

    @Override
    protected String getSourceLabel() {
        return "Ameritrade";
    }

    @Override
    public AssociationMetadata[] getAssociationMetadata() {
        return new AssociationMetadata[] { new AssociationMetadata("net.sf.jmoney.ameritrade.interest", "Interest Account"), new AssociationMetadata("net.sf.jmoney.ameritrade.expenses", "Expenses Account") };
    }
}
