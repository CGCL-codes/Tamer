package nu.lazy8.ledger.reports;

import java.awt.Dimension;
import java.io.File;
import java.sql.Types;
import javax.swing.JLabel;
import javax.swing.JPanel;
import nu.lazy8.ledger.jdbc.DataComboBox;
import nu.lazy8.ledger.jdbc.JdbcTable;
import nu.lazy8.util.gen.Fileio;
import nu.lazy8.util.gen.IntHolder;
import nu.lazy8.util.help.IntegerField;
import nu.lazy8.util.gen.Translator;
import nu.lazy8.util.help.HelpedLabel;
import org.gjt.sp.jedit.View;
import org.gjt.sp.util.Log;
import org.jfree.report.JFreeReport;

/**
 *  Description of the Class
 *
 * @author     Lazy Eight Data HB, Thomas Dilts
 * @created    den 5 mars 2002
 */
public class CustomerSums extends AccountReport {

    /**
   *Constructor for the CustomerSums object
   *
   * @param  view  Description of the Parameter
   */
    public CustomerSums(View view) {
        super(view, Translator.getTranslation("Customer/project accounts report"));
    }

    /**
   *  Description of the Method
   *
   * @param  isJFreeReport  Description of the Parameter
   * @param  rapName        Description of the Parameter
   * @param  compId         Description of the Parameter
   * @return                Description of the Return Value
   */
    public Object buttonGetReportOld(boolean isJFreeReport, String rapName, int compId) {
        JdbcTable db = getJdbcTable(compId, true);
        if (isJFreeReport) {
            if (db == null) return null;
            db.GetFirstRecord();
            try {
                return new AccountReportTable(db.resultSet, new java.sql.Date(jTextField1.getDate().getTime()), new java.sql.Date(jTextField2.getDate().getTime()), compId);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        if (db == null) return "";
        if (!db.GetFirstRecord()) {
            return "";
        }
        if (((Integer) customerComboBox.getSelectedItemsKey()).intValue() != 0) {
            jTextArea.append(Translator.getTranslation("Customer") + "     " + customerComboBox.getSelectedItemsSecondaryKey());
            jTextArea.append(newline);
        }
        AddReportHeaders();
        Integer processingAccount = (Integer) db.getObject("Amount.Account", null);
        Integer processingCustomer = (Integer) db.getObject("Amount.Customer", null);
        boolean bIsNewAccount = true;
        boolean bIsNewCustomer = true;
        boolean bIsNewPost = false;
        double fDebitTotal = 0;
        double fCreditTotal = 0;
        double fCustDebitTotal = 0;
        double fCustCreditTotal = 0;
        do {
            if (bIsNewAccount) {
                jTextArea.append("               " + Translator.getTranslation("Account") + " " + IntegerField.ConvertIntToLocalizedString(processingAccount) + " : " + (String) db.getObject("AccDesc", null));
                jTextArea.append(newline);
                jTextArea.append(newline);
            }
            jTextArea.append("               " + Translator.getTranslation("Customer") + " " + IntegerField.ConvertIntToLocalizedString(processingCustomer) + " : " + (String) db.getObject("CustName", null));
            jTextArea.append(newline);
            jTextArea.append(newline);
            initializeRow(fieldSize, 6);
            addField(Translator.getTranslation("Transaction"), fieldSize, 1, -1, false);
            addField(Translator.getTranslation("InvDate"), fieldSize, 2, -1, false);
            addField(Translator.getTranslation("Commentary"), fieldSize, 3, -1, false);
            addField(Translator.getTranslation("Commentary"), fieldSize, 4, -1, false);
            addField(Translator.getTranslation("Customer"), fieldSize, 5, -1, false);
            addField(Translator.getTranslation("Debit"), fieldSize, 6, -1, true);
            addField(Translator.getTranslation("Credit"), fieldSize, 7, -1, true);
            jTextArea.append(sbRow.toString());
            jTextArea.append(newline);
            do {
                jTextArea.append(renderIntegerField(db.getObject("Amount.Act_id", null), fieldSize[1]));
                jTextArea.append(renderDateField(db.getObject("InvDate", null), fieldSize[2]));
                jTextArea.append(renderField((String) db.getObject("Activity2.Notes", null), fieldSize[3]));
                jTextArea.append(renderField((String) db.getObject("Amount.Notes", null), fieldSize[4]));
                jTextArea.append(renderField((String) db.getObject("CustName", null), fieldSize[5]));
                jTextArea.append(renderDecimalField(((Double) db.getObject("Debit", null)), fieldSize[6]));
                fCustDebitTotal += ((Double) db.getObject("Debit", null)).doubleValue();
                fDebitTotal += ((Double) db.getObject("Debit", null)).doubleValue();
                jTextArea.append(renderDecimalField(((Double) db.getObject("Credit", null)), fieldSize[7]));
                fCustCreditTotal += ((Double) db.getObject("Credit", null)).doubleValue();
                fCreditTotal += ((Double) db.getObject("Credit", null)).doubleValue();
                jTextArea.append(newline);
                bIsNewPost = db.GetNextRecord();
                if (bIsNewPost) {
                    bIsNewAccount = processingAccount.compareTo((Integer) db.getObject("Amount.Account", null)) != 0;
                    bIsNewCustomer = processingCustomer.compareTo((Integer) db.getObject("Amount.Customer", null)) != 0;
                }
            } while (bIsNewPost && !bIsNewAccount && !bIsNewCustomer);
            initializeRow(fieldSize, 7);
            addField("-----------------------", fieldSize, 6, -1, false);
            addField("-----------------------", fieldSize, 7, -1, false);
            jTextArea.append(sbRow.toString());
            jTextArea.append(newline);
            initializeRow(fieldSize, 7);
            addField(Translator.getTranslation("Balance"), fieldSize, 4, -1, false);
            addField(new Double(fCustDebitTotal - fCustCreditTotal), fieldSize, 5, Types.DOUBLE, false);
            addField(new Double(fCustDebitTotal), fieldSize, 6, Types.DOUBLE, false);
            addField(new Double(fCustCreditTotal), fieldSize, 7, Types.DOUBLE, false);
            jTextArea.append(sbRow.toString());
            jTextArea.append(newline);
            processingCustomer = (Integer) db.getObject("Amount.Customer", null);
            fCustCreditTotal = 0;
            fCustDebitTotal = 0;
            if (bIsNewAccount || !bIsNewPost) {
                initializeRow(fieldSize, 7);
                addField("-----------------------", fieldSize, 6, -1, false);
                addField("-----------------------", fieldSize, 7, -1, false);
                jTextArea.append(sbRow.toString());
                jTextArea.append(newline);
                initializeRow(fieldSize, 7);
                addField(Translator.getTranslation("Balance"), fieldSize, 4, -1, false);
                addField(new Double(fDebitTotal - fCreditTotal), fieldSize, 5, Types.DOUBLE, false);
                addField(new Double(fDebitTotal), fieldSize, 6, Types.DOUBLE, false);
                addField(new Double(fCreditTotal), fieldSize, 7, Types.DOUBLE, false);
                jTextArea.append(sbRow.toString());
                jTextArea.append(newline);
                processingAccount = (Integer) db.getObject("Amount.Account", null);
                fCreditTotal = 0;
                fDebitTotal = 0;
            }
        } while (bIsNewPost);
        return jTextField3.getText();
    }

    /**
   *  Gets the jFreeReportFile attribute of the CustomerSums object
   *
   * @return    The jFreeReportFile value
   */
    public File getJFreeReportFile() {
        File file1 = null;
        try {
            file1 = Fileio.getFile("CustSums2.xml", "reports", false, false);
        } catch (Exception ee) {
            Log.log(Log.DEBUG, this, "No file? error=" + ee);
        }
        return file1;
    }

    /**
   *  Sets the reportProperties attribute of the CustomerSums object
   *
   * @param  report1    The new reportProperties value
   * @param  rapName    The new reportProperties value
   * @param  imagePath  The new reportProperties value
   * @param  compName   The new reportProperties value
   */
    public void setReportProperties(JFreeReport report1, String rapName, String imagePath, String compName, int compId) {
        super.setReportProperties(report1, rapName, imagePath, compName, compId);
        report1.setProperty("custaccountreport", Translator.getTranslation("Customer/project accounts report"));
    }
}
