package net.sourceforge.buddiplugins.plugins.imports.qif;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Collection;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.ProgressMonitorInputStream;
import net.sourceforge.buddi.api.manager.ImportManager;
import net.sourceforge.buddi.api.model.ImmutableAccount;
import net.sourceforge.buddi.api.model.ImmutableCategory;
import net.sourceforge.buddi.api.model.MutableTransaction;
import org.homeunix.thecave.moss.util.Log;
import net.sourceforge.buddiplugins.plugins.imports.AbstractAccountTransactionsImportPlugin;
import net.sourceforge.buddiplugins.plugins.imports.qif.helper.QIFParser;
import net.sourceforge.buddiplugins.plugins.imports.qif.helper.QIFParser.Transaction;
import net.sourceforge.buddiplugins.plugins.imports.qif.helper.QIFParser.UnsupportedException;

/**
 * This class implements the importing of QIF data into Buddi
 * 
 * @author Matthew Lieder, Mike Kienenberger
 * @date Apr 27, 2007
 */
public class ImportQIF extends AbstractAccountTransactionsImportPlugin {

    protected Object backgroundCode(ImportManager importManager, JFrame frame, File file) {
        Vector<MutableTransaction> transactions = null;
        QIFParser qifParser = null;
        try {
            final ImmutableAccount account = importManager.getSelectedAccount();
            transactions = new Vector<MutableTransaction>();
            InputStream dataIS = new ProgressMonitorInputStream(frame, "Reading " + file.getCanonicalPath(), new FileInputStream(file));
            qifParser = new QIFParser();
            Collection<Transaction> qifTransactions = qifParser.parseQIFTransactions(dataIS);
            for (QIFParser.Transaction qifTransaction : qifTransactions) {
                MutableTransaction transaction = importManager.createTransaction();
                transaction.setDate(qifTransaction.getDate());
                transaction.setCleared(qifTransaction.isCleared());
                transaction.setNumber(qifTransaction.getNumber());
                transaction.setMemo(qifTransaction.getMemo());
                double amount = qifTransaction.getAmount().doubleValue() * 100;
                transaction.setAmount(Math.abs(Math.round(amount)));
                if (amount >= 0) {
                    String strCat = qifTransaction.getPayee();
                    if (strCat != null) {
                        ImmutableCategory category = findCategory(importManager, strCat);
                        if (null == category) {
                            transaction.setFrom(createCategory(importManager, strCat, true));
                        } else {
                            transaction.setFrom(category);
                        }
                    }
                    transaction.setTo(account);
                } else {
                    transaction.setFrom(account);
                    String strCat = qifTransaction.getPayee();
                    if (strCat != null) {
                        ImmutableCategory category = findCategory(importManager, strCat);
                        if (null == category) {
                            transaction.setTo(createCategory(importManager, strCat, true));
                        } else {
                            transaction.setTo(category);
                        }
                    }
                }
                transaction.setDescription(qifTransaction.getCategory());
                if (transaction.getNumber() == null) transaction.setNumber("");
                if (transaction.getMemo() == null) transaction.setMemo("");
                if (transaction.getDescription() == null) transaction.setDescription("");
                if (transaction.getFrom() == null) transaction.setFrom(getFirstCategory(importManager, true));
                if (transaction.getTo() == null) transaction.setTo(getFirstCategory(importManager, false));
                Log.debug(transaction);
            }
        } catch (ParseException ex) {
            if (null != qifParser) {
                return new Exception(ex.toString() + " at line #" + qifParser.getLineNumber() + ": " + qifParser.getLine(), ex);
            } else {
                return ex;
            }
        } catch (UnsupportedException ex) {
            if (null != qifParser) {
                return new Exception(ex.toString() + " at line #" + qifParser.getLineNumber() + ": " + qifParser.getLine(), ex);
            } else {
                return ex;
            }
        } catch (IOException ex) {
            if (null != qifParser) {
                return new Exception(ex.toString() + " at line #" + qifParser.getLineNumber() + ": " + qifParser.getLine(), ex);
            } else {
                return ex;
            }
        }
        return transactions;
    }

    protected String getFileEnding() {
        return ".qif";
    }

    protected String getFileDescriptionKey() {
        return "QIF_FILES";
    }

    protected String getDescriptionKey() {
        return "IMPORTQIF";
    }
}
