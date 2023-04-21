package nz.org.venice.quote;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.prefs.Preferences;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import nz.org.venice.main.Module;
import nz.org.venice.main.ModuleFrame;
import nz.org.venice.prefs.ProxyPage;
import nz.org.venice.prefs.PreferencesManager;
import nz.org.venice.prefs.settings.Settings;
import nz.org.venice.main.CommandManager;
import nz.org.venice.ui.DesktopManager;
import nz.org.venice.ui.GridBagHelper;
import nz.org.venice.ui.ProgressDialog;
import nz.org.venice.ui.ProgressDialogManager;
import nz.org.venice.ui.TextViewDialog;
import nz.org.venice.util.Locale;
import nz.org.venice.util.Report;
import nz.org.venice.util.TradingDate;
import nz.org.venice.util.TradingDateFormatException;

/**
 * The import quote module allows importing of quotes into Venice.
 * It provides an interface to allow the user to perform a variety
 * of quote imports. The actual importing is handled by other
 * classes.
 *
 * @author Andrew Leppard
 * @see DatabaseQuoteSource
 * @see ExportQuoteModule
 * @see FileEODQuoteImport
 * @see YahooEODQuoteImport
 */
public class ImportQuoteModule extends JPanel implements Module {

    private JDesktopPane desktop;

    private PropertyChangeSupport propertySupport;

    private JRadioButton fromFiles;

    private JComboBox formatComboBox;

    private JRadioButton fromInternet;

    private JComboBox webSiteComboBox;

    private JLabel prefixOrSuffixLabel;

    private JTextField prefixOrSuffixTextField;

    private JTextField symbolList;

    private JTextField startDateTextField;

    private JTextField endDateTextField;

    private TradingDate startDate;

    private TradingDate endDate;

    private String prefixOrSuffix;

    private List symbols;

    private Settings settings;

    private EODQuoteFilter filter;

    private File files[];

    private static final int GOOGLE_SITE = 0;

    private static final int YAHOO_SITE = 1;

    private static final int FLOAT_SITE = 2;

    /**
     * Create a new import quote module.
     *
     * @param desktop the parent desktop
     */
    public ImportQuoteModule(JDesktopPane desktop) {
        this.desktop = desktop;
        propertySupport = new PropertyChangeSupport(this);
        setLayout(new BorderLayout());
        buildGUI();
    }

    /**
     * Layout the user interface.
     */
    private void buildGUI() {
        Preferences p = PreferencesManager.getUserNode("/import_quotes");
        String importFromSource = p.get("from", "internet");
        TitledBorder titledBorder = new TitledBorder(Locale.getString("IMPORT_FROM"));
        JPanel titledPanel = new JPanel();
        titledPanel.setBorder(titledBorder);
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        titledPanel.setLayout(gridbag);
        c.weightx = 1.0;
        c.ipadx = 5;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        {
            fromFiles = new JRadioButton(Locale.getString("FILES"));
            if (importFromSource.equals("files")) fromFiles.setSelected(true);
            c.gridwidth = 1;
            gridbag.setConstraints(fromFiles, c);
            titledPanel.add(fromFiles);
            formatComboBox = new JComboBox();
            List formats = EODQuoteFilterList.getInstance().getList();
            String selectedFilter = p.get("fileFilter", "MetaStock");
            for (Iterator iterator = formats.iterator(); iterator.hasNext(); ) {
                EODQuoteFilter filter = (EODQuoteFilter) iterator.next();
                formatComboBox.addItem(filter.getName());
                if (filter.getName().equals(selectedFilter)) formatComboBox.setSelectedItem(filter.getName());
            }
            c.gridwidth = GridBagConstraints.REMAINDER;
            gridbag.setConstraints(formatComboBox, c);
            titledPanel.add(formatComboBox);
        }
        {
            String webSite = p.get("webSite", "yahoo");
            fromInternet = new JRadioButton(Locale.getString("INTERNET"));
            if (importFromSource.equals("internet")) fromInternet.setSelected(true);
            c.gridwidth = 1;
            gridbag.setConstraints(fromInternet, c);
            titledPanel.add(fromInternet);
            webSiteComboBox = new JComboBox();
            webSiteComboBox.addItem(Locale.getString("GOOGLE_DISPLAY_URL"));
            webSiteComboBox.addItem(Locale.getString("YAHOO_DISPLAY_URL"));
            webSiteComboBox.addItem(Locale.getString("FLOAT_DISPLAY_URL"));
            if (webSite.equals("google")) webSiteComboBox.setSelectedIndex(GOOGLE_SITE); else if (webSite.equals("yahoo")) webSiteComboBox.setSelectedIndex(YAHOO_SITE); else webSiteComboBox.setSelectedIndex(FLOAT_SITE);
            c.gridwidth = GridBagConstraints.REMAINDER;
            gridbag.setConstraints(webSiteComboBox, c);
            titledPanel.add(webSiteComboBox);
            c.gridx = 1;
            symbolList = GridBagHelper.addTextRow(titledPanel, Locale.getString("SYMBOLS"), p.get("internetSymbolList", ""), gridbag, c, 11);
            c.gridx = 1;
            prefixOrSuffixLabel = new JLabel(Locale.getString("ADD_PREFIX"));
            c.gridwidth = 1;
            gridbag.setConstraints(prefixOrSuffixLabel, c);
            titledPanel.add(prefixOrSuffixLabel);
            prefixOrSuffixTextField = new JTextField(p.get("prefixOrSuffix", ""), 11);
            if (c.gridx != -1) c.gridx++;
            c.gridwidth = GridBagConstraints.REMAINDER;
            gridbag.setConstraints(prefixOrSuffixTextField, c);
            titledPanel.add(prefixOrSuffixTextField);
            c.gridx = 1;
            TradingDate today = new TradingDate();
            TradingDate previous = today.previous(30);
            startDateTextField = GridBagHelper.addTextRow(titledPanel, Locale.getString("START_DATE"), p.get("internetStartDate", previous.toString("dd/mm/yyyy")), gridbag, c, 11);
            c.gridx = 1;
            endDateTextField = GridBagHelper.addTextRow(titledPanel, Locale.getString("END_DATE"), p.get("internetEndDate", today.toString("dd/mm/yyyy")), gridbag, c, 11);
        }
        ButtonGroup group = new ButtonGroup();
        group.add(fromFiles);
        group.add(fromInternet);
        add(titledPanel, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        JButton importButton = new JButton(Locale.getString("IMPORT"));
        importButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                importQuotes();
            }
        });
        JButton cancelButton = new JButton(Locale.getString("CANCEL"));
        cancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                propertySupport.firePropertyChange(ModuleFrame.WINDOW_CLOSE_PROPERTY, 0, 1);
            }
        });
        buttonPanel.add(importButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
        fromFiles.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                checkDisabledStatus();
            }
        });
        fromInternet.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                checkDisabledStatus();
            }
        });
        webSiteComboBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                checkDisabledStatus();
            }
        });
        checkDisabledStatus();
    }

    /**
     * Enable/disable the appropriate widgets depending on which widgets
     * are checked.
     */
    private void checkDisabledStatus() {
        formatComboBox.setEnabled(fromFiles.isSelected());
        webSiteComboBox.setEnabled(fromInternet.isSelected());
        startDateTextField.setEnabled(fromInternet.isSelected());
        endDateTextField.setEnabled(fromInternet.isSelected());
        symbolList.setEnabled(fromInternet.isSelected() && (webSiteComboBox.getSelectedIndex() == GOOGLE_SITE || webSiteComboBox.getSelectedIndex() == YAHOO_SITE));
        boolean prefixOrSuffixEnabled = (fromInternet.isSelected() && (webSiteComboBox.getSelectedIndex() == GOOGLE_SITE || webSiteComboBox.getSelectedIndex() == YAHOO_SITE));
        prefixOrSuffixTextField.setEnabled(prefixOrSuffixEnabled);
        if (prefixOrSuffixEnabled) {
            if (webSiteComboBox.getSelectedIndex() == GOOGLE_SITE) prefixOrSuffixLabel.setText(Locale.getString("ADD_PREFIX")); else prefixOrSuffixLabel.setText(Locale.getString("ADD_SUFFIX"));
        }
    }

    /**
     * Import quotes into venice.
     */
    private void importQuotes() {
        saveConfiguration();
        Thread thread = new Thread() {

            public void run() {
                if (fromFiles.isSelected()) importQuotesFromFiles(); else {
                    assert fromInternet.isSelected();
                    importQuotesFromInternet();
                }
                CommandManager.getInstance().triggeredAlerts();
            }
        };
        thread.start();
    }

    /**
     * Save the configuration on screen to the preferences file
     */
    private void saveConfiguration() {
        Preferences p = PreferencesManager.getUserNode("/import_quotes");
        if (fromFiles.isSelected()) p.put("from", "files"); else p.put("from", "internet");
        p.put("internetSymbolList", symbolList.getText());
        p.put("internetStartDate", startDateTextField.getText());
        p.put("internetEndDate", endDateTextField.getText());
        p.put("fileFilter", (String) formatComboBox.getSelectedItem());
        if (webSiteComboBox.getSelectedIndex() == GOOGLE_SITE) p.put("webSite", "google"); else if (webSiteComboBox.getSelectedIndex() == YAHOO_SITE) p.put("webSite", "yahoo"); else p.put("webSite", "float");
        p.put("prefixOrSuffix", prefixOrSuffixTextField.getText());
    }

    /**
     * Import quotes from files.
     */
    private void importQuotesFromFiles() {
        if (parseFileFields()) {
            Report report = new Report();
            int quotesImported = 0;
            DatabaseQuoteSource database = getDatabaseSource();
            propertySupport.firePropertyChange(ModuleFrame.WINDOW_CLOSE_PROPERTY, 0, 1);
            ProgressDialog progress = ProgressDialogManager.getProgressDialog();
            progress.setIndeterminate(false);
            progress.setMaximum(files.length);
            progress.setProgress(0);
            progress.setMaster(true);
            progress.show(Locale.getString("IMPORTING"));
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                progress.setNote(Locale.getString("IMPORTING_FILE", file.getName()));
                quotesImported += importQuotesFromSingleFile(database, report, file);
                if (Thread.currentThread().isInterrupted()) break;
                progress.increment();
            }
            QuoteSourceManager.flush();
            ProgressDialogManager.closeProgressDialog(progress);
            displayReport(report, quotesImported);
        }
    }

    /**
     * Import quotes from single file.
     *
     * @param database database to store quotes
     * @param report   report to update
     * @param file     file to import
     * @return number of quotes imported
     */
    private int importQuotesFromSingleFile(DatabaseQuoteSource database, Report report, File file) {
        int quotesImported = 0;
        FileEODQuoteImport importer = new FileEODQuoteImport(report, filter);
        if (importer.open(file)) {
            while (importer.isNext()) {
                List quotes = importer.importNext();
                if (quotes.size() > 0) quotesImported += database.importQuotes(quotes);
            }
            importer.close();
        }
        if (quotesImported > 0) report.addMessage(file.getName() + ": " + Locale.getString("IMPORTED_QUOTES", quotesImported));
        return quotesImported;
    }

    /**
     * Parse all the fields for file import.
     *
     * @return <code>true</code> if the fields were successfully parsed, <code>false</code>
     *         otherwise
     */
    private boolean parseFileFields() {
        JFileChooser chooser;
        String lastDirectory = PreferencesManager.getDirectoryLocation("importer");
        if (lastDirectory != null) chooser = new JFileChooser(lastDirectory); else chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(true);
        int action = chooser.showOpenDialog(desktop);
        if (action == JFileChooser.APPROVE_OPTION) {
            lastDirectory = chooser.getCurrentDirectory().getAbsolutePath();
            PreferencesManager.putDirectoryLocation("importer", lastDirectory);
            files = chooser.getSelectedFiles();
            if (files.length != 0) {
                String format = (String) formatComboBox.getSelectedItem();
                filter = EODQuoteFilterList.getInstance().getFilter(format);
                return true;
            }
        }
        return false;
    }

    /**
     * Import quotes from the internet.
     */
    private void importQuotesFromInternet() {
        if (parseInternetFields()) {
            if (webSiteComboBox.getSelectedIndex() == GOOGLE_SITE) importQuotesFromGoogle(); else if (webSiteComboBox.getSelectedIndex() == YAHOO_SITE) importQuotesFromYahoo(); else importQuotesFromFloat();
        }
    }

    /**
     * Import quotes from finance.yahoo.com.
     */
    private void importQuotesFromYahoo() {
        Report report = new Report();
        int quotesImported = 0;
        DatabaseQuoteSource database = getDatabaseSource();
        propertySupport.firePropertyChange(ModuleFrame.WINDOW_CLOSE_PROPERTY, 0, 1);
        ProxyPage.setupNetworking();
        String suffix = prefixOrSuffix;
        ProgressDialog progress = ProgressDialogManager.getProgressDialog();
        progress.setIndeterminate(false);
        progress.setMaximum(symbols.size());
        progress.setProgress(0);
        progress.setMaster(true);
        progress.show(Locale.getString("IMPORTING"));
        try {
            for (Iterator iterator = symbols.iterator(); iterator.hasNext(); ) {
                Symbol symbol = (Symbol) iterator.next();
                progress.setNote(Locale.getString("IMPORTING_SYMBOL", symbol.toString()));
                List quotes = YahooEODQuoteImport.importSymbol(report, symbol, suffix, startDate, endDate);
                if (quotes.size() > 0) {
                    int symbolQuotesImported = database.importQuotes(quotes);
                    report.addMessage(Locale.getString("YAHOO_DISPLAY_URL") + ":" + symbol + ": " + Locale.getString("IMPORTED_QUOTES", symbolQuotesImported));
                    quotesImported += symbolQuotesImported;
                }
                if (Thread.currentThread().isInterrupted()) break;
                progress.increment();
            }
        } catch (ImportExportException e) {
            DesktopManager.showErrorMessage(e.getMessage());
        }
        QuoteSourceManager.flush();
        ProgressDialogManager.closeProgressDialog(progress);
        displayReport(report, quotesImported);
    }

    /**
     * Import quotes from google.yahoo.com.
     */
    private void importQuotesFromGoogle() {
        Report report = new Report();
        int quotesImported = 0;
        DatabaseQuoteSource database = getDatabaseSource();
        propertySupport.firePropertyChange(ModuleFrame.WINDOW_CLOSE_PROPERTY, 0, 1);
        ProxyPage.setupNetworking();
        String prefix = prefixOrSuffix;
        ProgressDialog progress = ProgressDialogManager.getProgressDialog();
        progress.setIndeterminate(false);
        progress.setMaximum(symbols.size());
        progress.setProgress(0);
        progress.setMaster(true);
        progress.show(Locale.getString("IMPORTING"));
        try {
            for (Iterator iterator = symbols.iterator(); iterator.hasNext(); ) {
                Symbol symbol = (Symbol) iterator.next();
                progress.setNote(Locale.getString("IMPORTING_SYMBOL", symbol.toString()));
                List quotes = GoogleEODQuoteImport.importSymbol(report, symbol, prefix, startDate, endDate);
                if (quotes.size() > 0) {
                    int symbolQuotesImported = database.importQuotes(quotes);
                    report.addMessage(Locale.getString("GOOGLE_DISPLAY_URL") + ":" + symbol + ": " + Locale.getString("IMPORTED_QUOTES", symbolQuotesImported));
                    quotesImported += symbolQuotesImported;
                }
                if (Thread.currentThread().isInterrupted()) break;
                progress.increment();
            }
        } catch (ImportExportException e) {
            DesktopManager.showErrorMessage(e.getMessage());
        }
        QuoteSourceManager.flush();
        ProgressDialogManager.closeProgressDialog(progress);
        displayReport(report, quotesImported);
    }

    /**
     * Import quotes from float.com.au.
     */
    private void importQuotesFromFloat() {
        Report report = new Report();
        int quotesImported = 0;
        DatabaseQuoteSource database = getDatabaseSource();
        List dates = TradingDate.dateRangeToList(startDate, endDate);
        propertySupport.firePropertyChange(ModuleFrame.WINDOW_CLOSE_PROPERTY, 0, 1);
        ProxyPage.setupNetworking();
        ProgressDialog progress = ProgressDialogManager.getProgressDialog();
        progress.setIndeterminate(false);
        progress.setMaximum(dates.size());
        progress.setProgress(0);
        progress.setMaster(true);
        progress.show(Locale.getString("IMPORTING"));
        try {
            for (Iterator iterator = dates.iterator(); iterator.hasNext(); ) {
                TradingDate date = (TradingDate) iterator.next();
                progress.setNote(Locale.getString("IMPORTING_DATE", date.toString()));
                List quotes = FloatEODQuoteImport.importDate(report, date);
                int dateQuotesImported = database.importQuotes(quotes);
                report.addMessage(Locale.getString("FLOAT_DISPLAY_URL") + ":" + date + ": " + Locale.getString("IMPORTED_QUOTES", dateQuotesImported));
                quotesImported += dateQuotesImported;
                if (Thread.currentThread().isInterrupted()) break;
                progress.increment();
            }
        } catch (ImportExportException e) {
            DesktopManager.showErrorMessage(e.getMessage());
        }
        QuoteSourceManager.flush();
        ProgressDialogManager.closeProgressDialog(progress);
        displayReport(report, quotesImported);
    }

    /**
     * Parse all the fields for internet import.
     *
     * @return <code>true</code> if the fields were successfully parsed, <code>false</code>
     *         otherwise
     */
    private boolean parseInternetFields() {
        if (webSiteComboBox.getSelectedIndex() == GOOGLE_SITE || webSiteComboBox.getSelectedIndex() == YAHOO_SITE) {
            try {
                symbols = new ArrayList(Symbol.toSortedSet(symbolList.getText(), false));
            } catch (SymbolFormatException e) {
                JOptionPane.showInternalMessageDialog(desktop, e.getMessage(), Locale.getString("INVALID_SYMBOL_LIST"), JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if (symbols.size() == 0) {
                JOptionPane.showInternalMessageDialog(desktop, Locale.getString("MISSING_SYMBOLS"), Locale.getString("INVALID_SYMBOL_LIST"), JOptionPane.ERROR_MESSAGE);
                return false;
            }
            prefixOrSuffix = prefixOrSuffixTextField.getText().trim();
        }
        try {
            startDate = new TradingDate(startDateTextField.getText(), TradingDate.BRITISH);
            endDate = new TradingDate(endDateTextField.getText(), TradingDate.BRITISH);
        } catch (TradingDateFormatException e) {
            JOptionPane.showInternalMessageDialog(desktop, Locale.getString("ERROR_PARSING_DATE", e.getDate()), Locale.getString("INVALID_DATE"), JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (startDate.after(endDate)) {
            JOptionPane.showInternalMessageDialog(desktop, Locale.getString("DATE_RANGE_ERROR"), Locale.getString("INVALID_DATE"), JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * Display the import report to the user. Initially just show a simple dialog
     * which describes the number of quotes imported and if there were any
     * warnings or errors and allows the user to view the full report. Display
     * the full report to if the user wishes.
     *
     * @param report the report to display
     * @param quotesImported the number of quotes imported
     */
    private void displayReport(final Report report, final int quotesImported) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                String message = Locale.getString("IMPORTED_QUOTES", quotesImported);
                if ((report.getErrorCount() + report.getWarningCount()) > 0) message = message.concat("\n" + Locale.getString("IMPORTED_WARNINGS", report.getErrorCount(), report.getWarningCount()));
                Object[] options = { Locale.getString("OK"), Locale.getString("VIEW_REPORT") };
                int option = JOptionPane.showInternalOptionDialog(desktop, message, Locale.getString("IMPORT_COMPLETE_TITLE"), JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                if (option == 1) {
                    Thread thread = new Thread(new Runnable() {

                        public void run() {
                            TextViewDialog.showTextDialog(report.getText(), Locale.getString("IMPORT_REPORT"));
                        }
                    });
                    thread.start();
                }
            }
        });
    }

    /**
     * Return the database source to import to. We can only import to a database
     * source (either the internal or an external database). The only source
     * that is not a database is the samples source. If this is selected then
     * silently convert them over to the "Internal Database". This way Venice "just works".
     *
     * @return database quote source
     */
    private DatabaseQuoteSource getDatabaseSource() {
        int quoteSource = PreferencesManager.getQuoteSource();
        if (quoteSource == PreferencesManager.SAMPLES) {
            PreferencesManager.putQuoteSource(PreferencesManager.INTERNAL);
            QuoteSourceManager.flush();
        }
        return (DatabaseQuoteSource) QuoteSourceManager.getSource();
    }

    /**
     * Add a property change listener for module change events.
     *
     * @param	listener	listener
     */
    public void addModuleChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }

    /**
     * Remove a property change listener for module change events.
     *
     * @param	listener	listener
     */
    public void removeModuleChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }

    /**
     * Return displayed component for this module.
     *
     * @return the component to display.
     */
    public JComponent getComponent() {
        return this;
    }

    /**
     * Return menu bar for quote source preferences module.
     *
     * @return	the menu bar.
     */
    public JMenuBar getJMenuBar() {
        return null;
    }

    /**
     * Return frame icon for quote source preferences module.
     *
     * @return	the frame icon.
     */
    public ImageIcon getFrameIcon() {
        return null;
    }

    /**
     * Returns the window title.
     *
     * @return	the window title.
     */
    public String getTitle() {
        return Locale.getString("IMPORT_TITLE");
    }

    /**
     * Return whether the module should be enclosed in a scroll pane.
     *
     * @return	enclose module in scroll bar
     */
    public boolean encloseInScrollPane() {
        return true;
    }

    /**
     * Called when window is closing. We handle the saving explicitly so
     * this is only called when the user clicks on the close button in the
     * top right hand of the window. Dont trigger a save event for this.
     */
    public void save() {
    }

    public Settings getSettings() {
        return settings;
    }
}
