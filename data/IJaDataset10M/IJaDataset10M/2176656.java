package nu.lazy8.ledger.reports;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import nu.lazy8.ledger.forms.CompanyComponents;
import nu.lazy8.ledger.jdbc.DataConnection;
import nu.lazy8.ledger.main.Lazy8Ledger;
import nu.lazy8.ledger.port.SieExport;
import nu.lazy8.util.gen.Fileio;
import nu.lazy8.util.gen.SetupInfo;
import nu.lazy8.util.gen.Translator;
import nu.lazy8.util.gen.SystemLog;
import nu.lazy8.util.help.HelpedButton;
import nu.lazy8.util.help.HelpedComboBox;
import nu.lazy8.util.help.HelpedLabel;
import nu.lazy8.util.help.HelpedTextField;
import org.gjt.sp.jedit.EBComponent;
import org.gjt.sp.jedit.EBMessage;
import org.gjt.sp.jedit.EditBus;
import org.gjt.sp.jedit.View;
import org.gjt.sp.jedit.gui.DockableWindowManager;

/**
 *  Description of the Class
 *
 * @author     Lazy Eight Data HB, Thomas Dilts
 * @created    den 5 mars 2002
 */
public class AllReports extends JPanel implements EBComponent {

    private static final int KNOWN_REPORT_BALANCE = 0;

    private static final int KNOWN_REPORT_TRIALBALANCE = 1;

    private static final int KNOWN_REPORT_TRANSACTIONS = 2;

    private static final int KNOWN_REPORT_ACCOUNTS = 3;

    private static final int KNOWN_REPORT_CHART_ACCOUNTS = 4;

    private static final int KNOWN_REPORT_CUSTOMERS = 5;

    private static final int KNOWN_REPORT_CUSTOMER_ACCOUNTS = 6;

    private static final int KNOWN_REPORT_CUSTOMER_TRANS = 7;

    private static final int UNKNOWN_REPORTS = 8;

    private Object[] reportFiles = null;

    private TextReport nowSelectedReport = null;

    public CompanyComponents cc = null;

    JPanel jpImage = null;

    public JComboBox jReports;

    View view;

    /**
   *Constructor for the AllReports object
   *
   * @param  view  Description of the Parameter
   */
    public AllReports(View view) {
        EditBus.addToBus(this);
        this.view = view;
        DataConnection dc = DataConnection.getInstance(view);
        if (dc == null || !dc.bIsConnectionMade) {
            buttonExit();
            return;
        }
        nowSelectedReport = getTextReport(KNOWN_REPORT_BALANCE);
        CreateAllComponents();
        ReCreateThePanel();
    }

    /**
   *  Description of the Method
   *
   * @param  jAddhere  Description of the Parameter
   */
    protected void AddButtonComponents(JPanel jAddhere) {
        JButton pdfReportBut = new HelpedButton(Translator.getTranslation("Get report"), "GetReportPDF", "allreports", view);
        jAddhere.add(pdfReportBut);
        pdfReportBut.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonGetJFreeReportPreperation();
            }
        });
        JButton b10 = new HelpedButton(Translator.getTranslation("Get text only report"), "GetReport", "allreports", view);
        jAddhere.add(b10);
        b10.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonGetReportPreperation(false);
            }
        });
        jAddhere.add(b10);
        JButton b11 = new HelpedButton(Translator.getTranslation("Exit"), "Exit", "allreports", view);
        jAddhere.add(b11);
        b11.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonExit();
            }
        });
        JButton b12 = new HelpedButton(Translator.getTranslation("Help"), "Help", "allreports", view);
        jAddhere.add(b12);
        b12.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonHelp();
            }
        });
    }

    private boolean reportAlreadyExists(String findString) {
        for (int i = 0; i < reportFiles.length; i++) if (findString.substring(0, findString.length() - 6).equals(((String) reportFiles[i]).substring(0, findString.length() - 6)) && !findString.equals(reportFiles[i])) return true;
        return false;
    }

    /**
   *  Description of the Method
   */
    public void CreateAllComponents() {
        jReports = new HelpedComboBox("Reports", "allreports", view);
        jReports.setMaximumRowCount(9);
        jReports.addItem(Translator.getTranslation("BalanceResult"));
        jReports.addItem(Translator.getTranslation("Trial balance report"));
        jReports.addItem(Translator.getTranslation("Transaction report"));
        jReports.addItem(Translator.getTranslation("Account summary"));
        jReports.addItem(Translator.getTranslation("Chart of accounts"));
        jReports.addItem(Translator.getTranslation("Customer"));
        jReports.addItem(Translator.getTranslation("Customer/project accounts report"));
        jReports.addItem(Translator.getTranslation("Customer/project transactions report"));
        ArrayList aList = Fileio.getFileNames("spcrep");
        reportFiles = aList.toArray();
        if (reportFiles != null) {
            String presentLanguage = SetupInfo.getProperty(SetupInfo.PRESENT_LANGUAGE);
            int iListCounter = 0;
            for (int i = 0; i < reportFiles.length; i++) {
                if (((String) reportFiles[i]).matches(".*\\." + presentLanguage + "\\.bin") || !((String) reportFiles[i]).matches(".*\\...\\.bin") || (((String) reportFiles[i]).matches(".*\\.en\\.bin") && !reportAlreadyExists((String) reportFiles[i]))) {
                    jReports.addItem(Translator.getTranslation(((String) reportFiles[i]).substring(0, ((String) reportFiles[i]).length() - 4)));
                } else {
                    aList.remove(iListCounter--);
                }
                iListCounter++;
            }
            reportFiles = aList.toArray();
        }
        jReports.setSelectedIndex(0);
        jReports.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        reportChanged();
                    }
                });
            }
        });
        cc = new CompanyComponents(null, Translator.getTranslation("Company"), false, "allreports", view) {

            public void periodChange() {
                try {
                    nowSelectedReport.resetDate((java.util.Date) cc.comboBoxPeriod.getSelectedItemsKey(), (java.util.Date) cc.comboBoxPeriod.getSelectedItemsSecondaryKey());
                } catch (Exception e) {
                }
            }

            public void updatePeriod() {
                try {
                    nowSelectedReport.resetDate((java.util.Date) cc.comboBoxPeriod.getSelectedItemsKey(), (java.util.Date) cc.comboBoxPeriod.getSelectedItemsSecondaryKey());
                } catch (Exception e) {
                }
            }
        };
        cc.AddPeriod(null, Translator.getTranslation("Period"), false);
        nowSelectedReport.jTextField3 = new HelpedTextField("ReportTitle", "allreports", view);
        jpImage = new JPanel();
        jpImage.setLayout(new BoxLayout(jpImage, BoxLayout.X_AXIS));
        nowSelectedReport.jTextField4 = new HelpedTextField("headerimage", "allreports", view);
        jpImage.add(nowSelectedReport.jTextField4);
        JButton browse = new HelpedButton(Translator.getTranslation("Browse"), "headerimage", "allreports", view);
        jpImage.add(browse);
        browse.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonBrowseImage();
            }
        });
        getReportFields();
    }

    /**
   *  Description of the Method
   */
    public void ReCreateThePanel() {
        removeAll();
        JPanel mePanel = new JPanel();
        mePanel.setLayout(new GridLayout(8 + nowSelectedReport.getNumGridRows(), 2));
        mePanel.add(new HelpedLabel(Translator.getTranslation("Report"), "Reports", "allreports", view));
        mePanel.add(jReports);
        cc.reAddComponents(mePanel);
        mePanel.add(new HelpedLabel(Translator.getTranslation("Report title"), "ReportTitle", "allreports", view));
        mePanel.add(nowSelectedReport.jTextField3);
        mePanel.add(new HelpedLabel(Translator.getTranslation("Report header image"), "headerimage", "allreports", view));
        jpImage = new JPanel();
        jpImage.setLayout(new BoxLayout(jpImage, BoxLayout.X_AXIS));
        jpImage.add(nowSelectedReport.jTextField4);
        JButton browse = new HelpedButton(Translator.getTranslation("Browse"), "headerimage", "allreports", view);
        jpImage.add(browse);
        browse.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonBrowseImage();
            }
        });
        mePanel.add(jpImage);
        if (nowSelectedReport.getNumGridRows() != 0) {
            nowSelectedReport.getSpecialGuiPane(mePanel);
        }
        AddButtonComponents(mePanel);
        JPanel horizontalSpacePanel = new JPanel();
        horizontalSpacePanel.setLayout(new BoxLayout(horizontalSpacePanel, BoxLayout.X_AXIS));
        horizontalSpacePanel.add(Box.createHorizontalGlue());
        horizontalSpacePanel.add(mePanel);
        horizontalSpacePanel.add(Box.createHorizontalGlue());
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(Box.createVerticalGlue());
        add(horizontalSpacePanel);
        add(Box.createVerticalGlue());
        getReportFields();
        validate();
    }

    /**
   *  Description of the Method
   */
    protected void buttonBrowseImage() {
        JFileChooser fileDialog = new JFileChooser();
        javax.swing.filechooser.FileFilter filter = new javax.swing.filechooser.FileFilter() {

            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }
                String extension = SieExport.getExtension(f);
                if (extension != null) {
                    if (extension.equals("jpg") || extension.equals("jpeg") || extension.equals("gif") || extension.equals("png")) {
                        return true;
                    } else {
                        return false;
                    }
                }
                return false;
            }

            public String getDescription() {
                return Translator.getTranslation("JPEG,JPG,PNG or GIF image");
            }
        };
        fileDialog.setFileFilter(filter);
        if (fileDialog.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            nowSelectedReport.jTextField4.setText(fileDialog.getSelectedFile().getPath());
        }
    }

    /**
   *  Description of the Method
   */
    public void buttonExit() {
        DockableWindowManager mgr = ((View) view).getDockableWindowManager();
        mgr.removeDockableWindow("lazy8ledger-report");
        EditBus.removeFromBus(this);
        if (cc != null) {
            cc.exit();
        }
    }

    /**
   *  Description of the Method
   */
    public void buttonGetJFreeReportPreperation() {
        saveReportFields();
        nowSelectedReport.buttonGetJFreeReportPreperation(nowSelectedReport.jTextField3.getText(), nowSelectedReport.jTextField4.getText(), ((Integer) cc.comboBox.getSelectedItemsKey()).intValue(), (String) cc.comboBox.getSelectedItemsSecondaryKey());
    }

    /**
   *  Description of the Method
   *
   * @param  IsOnClipboard  Description of the Parameter
   */
    public void buttonGetReportPreperation(boolean IsOnClipboard) {
        saveReportFields();
        nowSelectedReport.buttonGetReportPreperation(IsOnClipboard, nowSelectedReport.jTextField3.getText(), nowSelectedReport.jTextField4.getText(), ((Integer) cc.comboBox.getSelectedItemsKey()).intValue());
    }

    /**
   *  Description of the Method
   */
    public void buttonHelp() {
        Lazy8Ledger.ShowHelp(view, "allreports", "");
    }

    /**
   *  Gets the reportFields attribute of the AllReports object
   */
    public void getReportFields() {
        nowSelectedReport.jTextField3.setText(SetupInfo.getProperty("lazy8ledger.report.ReportTitle." + jReports.getSelectedIndex()));
        nowSelectedReport.jTextField4.setText(SetupInfo.getProperty("lazy8ledger.report.headerimage." + jReports.getSelectedIndex()));
        try {
            nowSelectedReport.resetDate((java.util.Date) cc.comboBoxPeriod.getSelectedItemsKey(), (java.util.Date) cc.comboBoxPeriod.getSelectedItemsSecondaryKey());
        } catch (Exception e) {
        }
    }

    /**
   *  Gets the textReport attribute of the AllReports object
   *
   * @param  reportNum  Description of the Parameter
   * @return            The textReport value
   */
    private TextReport getTextReport(int reportNum) {
        switch(reportNum) {
            case KNOWN_REPORT_BALANCE:
                return new BalanceReport(view);
            case KNOWN_REPORT_TRIALBALANCE:
                return new TrialBalanceReport(view);
            case KNOWN_REPORT_TRANSACTIONS:
                return new TransactionReport(view);
            case KNOWN_REPORT_ACCOUNTS:
                return new AccountReport(view);
            case KNOWN_REPORT_CHART_ACCOUNTS:
                return new AccChartReport(view);
            case KNOWN_REPORT_CUSTOMERS:
                return new CustomerReport(view);
            case KNOWN_REPORT_CUSTOMER_ACCOUNTS:
                return new CustomerSums(view);
            case KNOWN_REPORT_CUSTOMER_TRANS:
                return new CustomerTransactions(view);
            default:
                int i = reportNum - UNKNOWN_REPORTS;
                return new AccountSumReporter(view, ((String) reportFiles[i]).substring(0, ((String) reportFiles[i]).length() - 4));
        }
    }

    /**
   *  Description of the Method
   *
   * @param  msg  Description of the Parameter
   */
    public void handleMessage(EBMessage msg) {
        if (nowSelectedReport != null) {
            nowSelectedReport.handleMessage(msg);
        }
    }

    /**
   *  Description of the Method
   */
    public void reportChanged() {
        nowSelectedReport = getTextReport(jReports.getSelectedIndex());
        removeAll();
        setLayout(new GridLayout(1, 1));
        add(new JLabel(" "));
        validate();
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                nowSelectedReport.jTextField3 = new HelpedTextField("ReportTitle", "allreports", view);
                nowSelectedReport.jTextField4 = new HelpedTextField("headerimage", "allreports", view);
                nowSelectedReport.cc = cc;
                ReCreateThePanel();
            }
        });
    }

    /**
   *  Description of the Method
   */
    private void saveReportFields() {
        SetupInfo.setProperty("lazy8ledger.report.ReportTitle." + jReports.getSelectedIndex(), nowSelectedReport.jTextField3.getText());
        SetupInfo.setProperty("lazy8ledger.report.headerimage." + jReports.getSelectedIndex(), nowSelectedReport.jTextField4.getText());
        SetupInfo.store();
    }
}
