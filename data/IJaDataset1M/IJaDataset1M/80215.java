package org.homeunix.thecave.plugins.imports;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.homeunix.drummer.model.Account;
import org.homeunix.drummer.model.Category;
import org.homeunix.drummer.model.DataInstance;
import org.homeunix.drummer.model.Schedule;
import org.homeunix.drummer.model.Source;
import org.homeunix.drummer.model.Transaction;
import org.homeunix.drummer.model.Type;
import org.homeunix.drummer.model.impl.DataModelImpl;
import org.homeunix.thecave.buddi.i18n.keys.BudgetCategoryTypes;
import org.homeunix.thecave.buddi.plugin.api.BuddiImportPlugin;
import org.homeunix.thecave.buddi.plugin.api.exception.ModelException;
import org.homeunix.thecave.buddi.plugin.api.exception.PluginException;
import org.homeunix.thecave.buddi.plugin.api.exception.PluginMessage;
import org.homeunix.thecave.buddi.plugin.api.model.MutableAccount;
import org.homeunix.thecave.buddi.plugin.api.model.MutableAccountType;
import org.homeunix.thecave.buddi.plugin.api.model.MutableBudgetCategory;
import org.homeunix.thecave.buddi.plugin.api.model.MutableDocument;
import org.homeunix.thecave.buddi.plugin.api.model.MutableModelFactory;
import org.homeunix.thecave.buddi.plugin.api.model.MutableScheduledTransaction;
import org.homeunix.thecave.buddi.plugin.api.model.MutableSource;
import org.homeunix.thecave.buddi.plugin.api.model.MutableTransaction;
import org.homeunix.thecave.buddi.plugin.api.util.TextFormatter;
import ca.digitalcave.moss.application.document.exception.DocumentLoadException;
import ca.digitalcave.moss.common.DateUtil;
import ca.digitalcave.moss.swing.MossDocumentFrame;

public class ImportLegacyData extends BuddiImportPlugin {

    @Override
    public void importData(final MutableDocument model, final MossDocumentFrame callingFrame, final File file) throws PluginException, PluginMessage {
        try {
            DataInstance.getInstance().loadDataFile(file);
            final DataModelImpl oldModel = (DataModelImpl) DataInstance.getInstance().getDataModel();
            if (file == null) return;
            String message = convert(model, oldModel);
            if (message.length() > 0) {
                Logger.getLogger(this.getClass().getName()).warning(TextFormatter.getTranslation(ImportLegacyDataKeys.IMPORT_LEGACY_POTENTIAL_WARNINGS) + "\n" + message);
                throw new PluginMessage(TextFormatter.getTranslation(ImportLegacyDataKeys.IMPORT_LEGACY_POTENTIAL_WARNINGS), message, TextFormatter.getTranslation(ImportLegacyDataKeys.IMPORT_LEGACY_POTENTIAL_WARNINGS_TITLE), JOptionPane.WARNING_MESSAGE);
            }
        } catch (DocumentLoadException dle) {
            throw new PluginException(dle);
        }
    }

    public String getName() {
        return ImportLegacyDataKeys.IMPORT_LEGACY_BUDDI_FORMAT.toString();
    }

    @Override
    public String[] getFileExtensions() {
        return new String[] { ".buddi" };
    }

    @Override
    public String getProcessingMessage() {
        return ImportLegacyDataKeys.IMPORT_LEGACY_MESSAGE_CONVERTING_LEGACY_DATA_FILE.toString();
    }

    @Override
    public String getDescription() {
        return ImportLegacyDataKeys.IMPORT_LEGACY_BUDDI_FILES.toString();
    }

    public String convert(MutableDocument model, DataModelImpl oldModel) throws DocumentLoadException {
        StringBuilder sb = new StringBuilder();
        Boolean checkInvalidDates = null;
        try {
            Map<Type, MutableAccountType> typeMap = new HashMap<Type, MutableAccountType>();
            Map<MutableAccountType, List<MutableAccount>> typeAccountMap = new HashMap<MutableAccountType, List<MutableAccount>>();
            Map<Category, MutableBudgetCategory> categoryMap = new HashMap<Category, MutableBudgetCategory>();
            Map<Source, MutableSource> sourceMap = new HashMap<Source, MutableSource>();
            for (Object oldTypeObject : oldModel.getAllTypes().getTypes()) {
                org.homeunix.drummer.model.Type oldType = (org.homeunix.drummer.model.Type) oldTypeObject;
                if (model.getAccountType(oldType.getName()) == null) {
                    MutableAccountType newType = MutableModelFactory.createMutableAccountType(oldType.getName(), oldType.isCredit());
                    typeMap.put(oldType, newType);
                    typeAccountMap.put(newType, new LinkedList<MutableAccount>());
                    model.addAccountType(newType);
                } else {
                    typeMap.put(oldType, (MutableAccountType) model.getAccountType(oldType.getName()));
                    typeAccountMap.put((MutableAccountType) model.getAccountType(oldType.getName()), new LinkedList<MutableAccount>());
                }
            }
            for (Object oldAccountObject : oldModel.getAllAccounts().getAccounts()) {
                Account oldAccount = (Account) oldAccountObject;
                if (model.getAccount(oldAccount.getName()) == null) {
                    MutableAccount newAccount = MutableModelFactory.createMutableAccount(oldAccount.getName(), oldAccount.getStartingBalance(), typeMap.get(oldAccount.getAccountType()));
                    newAccount.setStartingBalance(oldAccount.getStartingBalance());
                    newAccount.setDeleted(oldAccount.isDeleted());
                    newAccount.setOverdraftCreditLimit(oldAccount.getCreditLimit());
                    model.addAccount(newAccount);
                    sourceMap.put(oldAccount, newAccount);
                } else {
                    MutableAccount newAccount = (MutableAccount) model.getAccount(oldAccount.getName());
                    if (!newAccount.getAccountType().getName().equals(oldAccount.getAccountType().getName())) {
                        sb.append(newAccount.getFullName());
                        sb.append(":\n ");
                        sb.append(TextFormatter.getTranslation(ImportLegacyDataKeys.IMPORT_LEGACY_ACCOUNT_TYPE_DIFFERS));
                        sb.append("\n\n");
                    }
                    if (newAccount.getStartingBalance() != oldAccount.getStartingBalance()) {
                        sb.append(newAccount.getFullName());
                        sb.append(":\n");
                        sb.append(TextFormatter.getTranslation(ImportLegacyDataKeys.IMPORT_LEGACY_ACCOUNT_STARTING_BALANCE_DIFFERS));
                        sb.append("\n\n");
                    }
                    if (newAccount.isDeleted() != oldAccount.isDeleted()) {
                        sb.append(newAccount.getFullName());
                        sb.append(":\n");
                        sb.append(TextFormatter.getTranslation(ImportLegacyDataKeys.IMPORT_LEGACY_ACCOUNT_DELETE_STATUS_DIFFERS));
                        sb.append("\n\n");
                    }
                    if (newAccount.getOverdraftCreditLimit() != oldAccount.getCreditLimit()) {
                        sb.append(newAccount.getFullName());
                        sb.append(":\n");
                        sb.append(TextFormatter.getTranslation(ImportLegacyDataKeys.IMPORT_LEGACY_ACCOUNT_OVERDRAFT_CREDIT_VALUE_DIFFERS));
                        sb.append("\n\n");
                    }
                    sourceMap.put(oldAccount, newAccount);
                }
            }
            for (Object oldCategoryObject : oldModel.getAllCategories().getCategories()) {
                Category oldCategory = (Category) oldCategoryObject;
                if (model.getBudgetCategory(oldCategory.getFullName()) == null) {
                    MutableBudgetCategory newBudgetCategory = MutableModelFactory.createMutableBudgetCategory(oldCategory.getFullName(), model.getBudgetCategoryType(BudgetCategoryTypes.BUDGET_CATEGORY_TYPE_MONTH), oldCategory.isIncome());
                    newBudgetCategory.setDeleted(oldCategory.isDeleted());
                    newBudgetCategory.setAmount(new Date(), oldCategory.getBudgetedAmount());
                    categoryMap.put(oldCategory, newBudgetCategory);
                    sourceMap.put(oldCategory, newBudgetCategory);
                    model.addBudgetCategory(newBudgetCategory);
                } else {
                    MutableBudgetCategory newBudgetCategory = (MutableBudgetCategory) model.getBudgetCategory(oldCategory.getFullName());
                    if (newBudgetCategory.getAmount(new Date()) == 0) newBudgetCategory.setAmount(new Date(), oldCategory.getBudgetedAmount());
                    if (newBudgetCategory.isDeleted() != oldCategory.isDeleted()) {
                        sb.append(newBudgetCategory.getFullName());
                        sb.append(":\n");
                        sb.append(TextFormatter.getTranslation(ImportLegacyDataKeys.IMPORT_LEGACY_BUDGET_CATEGORY_DELETE_STATUS_DIFFERS));
                        sb.append("\n\n");
                    }
                    if (newBudgetCategory.isIncome() != oldCategory.isIncome()) {
                        sb.append(newBudgetCategory.getFullName());
                        sb.append(":\n");
                        sb.append(TextFormatter.getTranslation(ImportLegacyDataKeys.IMPORT_LEGACY_BUDGET_CATEGORY_TYPE_DIFFERS));
                        sb.append("\n\n");
                    }
                    categoryMap.put(oldCategory, newBudgetCategory);
                    sourceMap.put(oldCategory, newBudgetCategory);
                }
            }
            for (Object oldCategoryObject : oldModel.getAllCategories().getCategories()) {
                Category oldCategory = (Category) oldCategoryObject;
                if (oldCategory.getParent() != null) {
                    Category oldParent = oldCategory.getParent();
                    if (model.getBudgetCategory(oldCategory.getFullName()) != null) {
                        ((MutableBudgetCategory) model.getBudgetCategory(oldCategory.getFullName())).setParent((MutableBudgetCategory) model.getBudgetCategory(oldParent.getFullName()));
                    }
                }
            }
            for (Object oldTransactionObject : oldModel.getAllTransactions().getTransactions()) {
                Transaction oldTransaction = (Transaction) oldTransactionObject;
                if (checkInvalidDates == null && (DateUtil.getYear(oldTransaction.getDate()) < 2000 || DateUtil.getYear(oldTransaction.getDate()) > 2010)) {
                    int reply = JOptionPane.showConfirmDialog(null, "I found a date which appears to be incorrect: the year was\nset to '" + DateUtil.getYear(oldTransaction.getDate()) + "'.\n\nDo you want me to change this (and any other potentially\ninvalid) dates to more sane values?", "Possibly Incorrect Date Range", JOptionPane.YES_NO_OPTION);
                    if (reply == JOptionPane.YES_OPTION) {
                        checkInvalidDates = true;
                    } else {
                        checkInvalidDates = false;
                    }
                }
                Date oldDate = oldTransaction.getDate();
                if (checkInvalidDates != null && checkInvalidDates) {
                    int year = DateUtil.getYear(oldDate);
                    if (year < 2000 || year > 2010) {
                        if (year % 100 > 0 && year % 100 < 10) year = year % 100 + 2000; else {
                            year = 2000;
                            sb.append(TextFormatter.getTranslation(ImportLegacyDataKeys.IMPORT_LEGACY_DATE_POSSIBLY_INVALID));
                            sb.append("\n\n");
                        }
                        DateUtil.setDate(oldDate, year, DateUtil.getMonth(oldDate), DateUtil.getDay(oldDate));
                    }
                }
                MutableTransaction newTransaction = MutableModelFactory.createMutableTransaction(oldDate, oldTransaction.getDescription(), oldTransaction.getAmount(), sourceMap.get(oldTransaction.getFrom()), sourceMap.get(oldTransaction.getTo()));
                newTransaction.setClearedFrom(oldTransaction.isCleared());
                newTransaction.setClearedTo(oldTransaction.isCleared());
                newTransaction.setMemo(oldTransaction.getMemo());
                newTransaction.setNumber(oldTransaction.getNumber());
                newTransaction.setReconciledFrom(oldTransaction.isReconciled());
                newTransaction.setReconciledTo(oldTransaction.isReconciled());
                newTransaction.setFrom(sourceMap.get(oldTransaction.getFrom()));
                newTransaction.setTo(sourceMap.get(oldTransaction.getTo()));
                newTransaction.setScheduled(oldTransaction.isScheduled());
                model.addTransaction(newTransaction);
            }
            for (Object oldScheduledTransactionObject : oldModel.getAllTransactions().getScheduledTransactions()) {
                Schedule oldScheduledTransaction = (Schedule) oldScheduledTransactionObject;
                MutableScheduledTransaction newScheduledTransaction = MutableModelFactory.createMutableScheduledTransaction(oldScheduledTransaction.getScheduleName(), oldScheduledTransaction.getMessage(), oldScheduledTransaction.getStartDate(), oldScheduledTransaction.getEndDate(), oldScheduledTransaction.getFrequencyType(), oldScheduledTransaction.getScheduleDay(), oldScheduledTransaction.getScheduleWeek(), oldScheduledTransaction.getScheduleMonth(), oldScheduledTransaction.getDate(), oldScheduledTransaction.getDescription(), oldScheduledTransaction.getAmount(), sourceMap.get(oldScheduledTransaction.getFrom()), sourceMap.get(oldScheduledTransaction.getTo()));
                newScheduledTransaction.setClearedFrom(oldScheduledTransaction.isCleared());
                newScheduledTransaction.setClearedTo(oldScheduledTransaction.isCleared());
                newScheduledTransaction.setLastDayCreated(oldScheduledTransaction.getLastDateCreated());
                newScheduledTransaction.setMemo(oldScheduledTransaction.getMemo());
                newScheduledTransaction.setNumber(oldScheduledTransaction.getNumber());
                newScheduledTransaction.setReconciledFrom(oldScheduledTransaction.isReconciled());
                newScheduledTransaction.setReconciledTo(oldScheduledTransaction.isReconciled());
                model.addScheduledTransaction(newScheduledTransaction);
            }
            Date earliest = new Date();
            for (MutableAccount a : model.getMutableAccounts()) {
                if (earliest.after(a.getStartDate())) earliest = a.getStartDate();
            }
            for (MutableBudgetCategory bc : model.getMutableBudgetCategories()) {
                Date tempDate = new Date();
                long amount = bc.getAmount(new Date());
                if (earliest.before(tempDate)) {
                    while (earliest.before(tempDate)) {
                        bc.setAmount(tempDate, amount);
                        tempDate = bc.getBudgetPeriodType().getBudgetPeriodOffset(tempDate, -1);
                    }
                }
            }
            return sb.toString();
        } catch (ModelException me) {
            throw new DocumentLoadException(me);
        }
    }

    @Override
    public boolean isCreateNewFile() {
        return true;
    }
}
