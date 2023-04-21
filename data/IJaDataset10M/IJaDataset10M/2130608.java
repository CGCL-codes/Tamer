package de.kugihan.dictionaryformids.hmi_java_se;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Event;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;
import java.util.jar.JarFile;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import de.kugihan.dictionaryformids.dataaccess.DictionaryDataFile;
import de.kugihan.dictionaryformids.dataaccess.content.FontStyle;
import de.kugihan.dictionaryformids.dataaccess.fileaccess.FileAccessHandler;
import de.kugihan.dictionaryformids.dataaccess.fileaccess.JarInputStreamAccess;
import de.kugihan.dictionaryformids.general.DictionaryException;
import de.kugihan.dictionaryformids.general.Util;
import de.kugihan.dictionaryformids.general.UtilWin;
import de.kugihan.dictionaryformids.hmi_common.content.ContentParser;
import de.kugihan.dictionaryformids.hmi_common.content.StringColourItemText;
import de.kugihan.dictionaryformids.hmi_common.content.StringColourItemTextPart;
import de.kugihan.dictionaryformids.translation.SingleTranslation;
import de.kugihan.dictionaryformids.translation.TextOfLanguage;
import de.kugihan.dictionaryformids.translation.TranslationExecution;
import de.kugihan.dictionaryformids.translation.TranslationExecutionCallback;
import de.kugihan.dictionaryformids.translation.TranslationParameters;
import de.kugihan.dictionaryformids.translation.TranslationResult;

/**
* J2SE Version of DictionaryForMids
* Copyright (C) 2005-2007 Stefan Martens (stefan@stefan1200.de)
* 
* GPL applies - see file COPYING for copyright statement.
*
* @author Stefan "Stefan1200" Martens
* @version 3.1.0 ALPHA 3 (11.02.2007)
*/
public class DictionaryForSE extends JFrame implements ActionListener, TranslationExecutionCallback, MouseListener, DocumentListener {

    private static String CONFIG_NAME = "DictionaryForMIDs.ini";

    private JPanel pMainFrame = (JPanel) getContentPane();

    private JTextField tfLeft = new JTextField();

    private JTextField tfRight = new JTextField();

    private JLabel lStatus = new JLabel();

    private DefaultTableModel dtm = new DefaultTableModel() {

        public Class getColumnClass(int columnIndex) {
            switch(columnIndex) {
                default:
                    return String.class;
            }
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }
    };

    private JTable table = new SortedTable(dtm);

    private AWTFileDialog fd = new AWTFileDialog();

    private JMenuBar menuBar = new JMenuBar();

    private JMenu leftLangSubMenu;

    private JMenu rightLangSubMenu;

    private JMenu dictHistoryMenu = new JMenu("Dictionary History");

    private JCheckBoxMenuItem colourTextMenu = new JCheckBoxMenuItem("Coloured Text", true);

    private JPopupMenu pmListL = createPopupMenu("L");

    private JPopupMenu pmListR = createPopupMenu("R");

    private ButtonGroup bgLeftLang;

    private ButtonGroup bgRightLang;

    private boolean colourText = true;

    private boolean rightSearch = false;

    private boolean configLoaded = false;

    private UtilWin utilObj;

    private Clipboard clip = getToolkit().getSystemClipboard();

    private StringSelection cont;

    private javax.swing.Timer timerStatus = new javax.swing.Timer(500, this);

    private Vector dictHistory = new Vector();

    private Vector languages = new Vector();

    private Vector languagesAll = new Vector();

    private String lastSearch = "";

    private String oldStatus = "";

    private Thread searchThread;

    private Properties prop = new Properties();

    public static JarFile jar;

    private JDialog ppd;

    private JButton bUse;

    private JButton bCancelPrefs;

    private JLabel lFontTest;

    private JComboBox cbFont;

    private String fontName = "SansSerif";

    public static void main(String[] args) throws DictionaryException {
        UtilWin utilObj = new UtilWin();
        Util.setUtil(utilObj);
        DictionaryForSE frame = new DictionaryForSE();
        frame.setTitle("DictionaryForMIDs " + Util.getUtil().getApplicationVersionString());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public DictionaryForSE() throws DictionaryException {
        initGUI();
        loadPrefs();
        if (jar == null) {
            loadDictionary();
        }
        timerStatus.setRepeats(false);
        DictionarySettings.setMaxHits(10000);
        fillTableColums();
        createGUI();
        TranslationExecution.setTranslationExecutionCallback(this);
        lStatus.setText("Welcome to DictionaryForMIDs " + Util.getUtil().getApplicationVersionString() + " (c) by Stefan Martens & Gert Nuber");
    }

    private void initGUI() {
        menuBar.add(createFileMenu());
        menuBar.add(createPrefsMenu());
        setJMenuBar(menuBar);
        tfLeft.setToolTipText("Type a search word here and press enter");
        tfLeft.addActionListener(this);
        tfLeft.addMouseListener(this);
        tfLeft.getDocument().addDocumentListener(this);
        tfRight.setToolTipText("Type a search word here and press enter");
        tfRight.addActionListener(this);
        tfRight.addMouseListener(this);
        tfRight.getDocument().addDocumentListener(this);
        colourTextMenu.addActionListener(this);
        table.addMouseListener(this);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setColumnSelectionAllowed(false);
        table.setRowSelectionAllowed(true);
        table.setShowGrid(false);
        table.setShowHorizontalLines(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setDefaultRenderer(String.class, new MyTableCellRenderer());
        ((SortedTable) table).setResortOnModelChange(false);
        JTableHeader th = table.getTableHeader();
        th.setDefaultRenderer(new MyHeaderCellRenderer());
    }

    private void createGUI() {
        pMainFrame.setLayout(new GridBagLayout());
        pMainFrame.add(tfLeft, getGBC(0, 0, 1, 1, 10, 0, new Insets(1, 1, 1, 5)));
        pMainFrame.add(tfRight, getGBC(1, 0, 1, 1, 10, 0, new Insets(1, 5, 1, 1)));
        pMainFrame.add(new JScrollPane(table), getGBC(0, 1, 2, 1, 10, 10, new Insets(1, 1, 1, 1)));
        pMainFrame.add(lStatus, getGBC(0, 2, 2, 1, 10, 0, new Insets(1, 1, 1, 1)));
    }

    private GridBagConstraints getGBC(int gridx, int gridy, int gridwidth, int gridheight, int weightx, int weighty, Insets inset) {
        return new GridBagConstraints(gridx, gridy, gridwidth, gridheight, weightx, weighty, GridBagConstraints.CENTER, GridBagConstraints.BOTH, inset, 0, 0);
    }

    private JMenu createFileMenu() {
        JMenu ret = new JMenu("File");
        ret.setMnemonic('F');
        JMenuItem mi;
        mi = new JMenuItem("Open Dictionary", 'o');
        setCtrlAccelerator(mi, 'O');
        mi.setActionCommand("openDict");
        mi.addActionListener(this);
        ret.add(mi);
        mi = new JMenuItem("Dictionary Information", 'i');
        setCtrlAccelerator(mi, 'I');
        mi.setActionCommand("dictInfo");
        mi.addActionListener(this);
        ret.add(mi);
        ret.add(createDictHistoryMenu());
        ret.addSeparator();
        mi = new JMenuItem("About", 'a');
        mi.setActionCommand("about");
        mi.addActionListener(this);
        ret.add(mi);
        ret.addSeparator();
        mi = new JMenuItem("Quit", 'q');
        setCtrlAccelerator(mi, 'Q');
        mi.setActionCommand("quit");
        mi.addActionListener(this);
        ret.add(mi);
        return ret;
    }

    private JMenu createPrefsMenu() {
        JMenu ret = new JMenu("Preferences");
        JMenuItem mi;
        ret.setMnemonic('F');
        mi = new JMenuItem("Change Font", 'f');
        setCtrlAccelerator(mi, 'F');
        mi.setActionCommand("changeFont");
        mi.addActionListener(this);
        ret.add(mi);
        ret.add(colourTextMenu);
        ret.add(createLeftLangSubMenu());
        ret.add(createRightLangSubMenu());
        ret.addSeparator();
        mi = new JMenuItem("Save Preferences", 's');
        setCtrlAccelerator(mi, 'S');
        mi.setActionCommand("savePrefs");
        mi.addActionListener(this);
        ret.add(mi);
        return ret;
    }

    private JMenu createLeftLangSubMenu() {
        leftLangSubMenu = new JMenu("Left Language");
        JRadioButtonMenuItem rbm;
        bgLeftLang = new ButtonGroup();
        for (int i = 0; i < languagesAll.size(); i++) {
            rbm = new JRadioButtonMenuItem(languagesAll.elementAt(i).toString());
            rbm.addActionListener(this);
            rbm.setActionCommand("LangL" + languagesAll.elementAt(i).toString());
            bgLeftLang.add(rbm);
            leftLangSubMenu.add(rbm);
        }
        return leftLangSubMenu;
    }

    private JMenu createRightLangSubMenu() {
        rightLangSubMenu = new JMenu("Right Language");
        JRadioButtonMenuItem rbm;
        bgRightLang = new ButtonGroup();
        for (int i = 0; i < languagesAll.size(); i++) {
            rbm = new JRadioButtonMenuItem(languagesAll.elementAt(i).toString());
            rbm.addActionListener(this);
            rbm.setActionCommand("LangR" + languagesAll.elementAt(i).toString());
            bgRightLang.add(rbm);
            rightLangSubMenu.add(rbm);
        }
        return rightLangSubMenu;
    }

    private JMenu createDictHistoryMenu() {
        dictHistoryMenu.removeAll();
        JMenuItem mi;
        if (dictHistory.size() == 0) {
            mi = new JMenuItem("Nothing");
            mi.setEnabled(false);
            dictHistoryMenu.add(mi);
        } else {
            for (int i = 0; i < dictHistory.size(); i++) {
                mi = new JMenuItem(getMenuFilename(dictHistory.elementAt(i).toString()));
                mi.addActionListener(this);
                mi.setActionCommand("HistoryOpen" + dictHistory.elementAt(i).toString());
                dictHistoryMenu.add(mi);
            }
        }
        dictHistoryMenu.addSeparator();
        mi = new JMenuItem("Clear History", 'c');
        mi.addActionListener(this);
        mi.setActionCommand("clearHistory");
        dictHistoryMenu.add(mi);
        dictHistoryMenu.validate();
        return dictHistoryMenu;
    }

    private JPopupMenu createPopupMenu(String ident) {
        JPopupMenu pm = new JPopupMenu("Popup");
        JMenuItem mi;
        mi = new JMenuItem("Copy", 'c');
        mi.setActionCommand(ident + "pmCopy");
        mi.addActionListener(this);
        pm.add(mi);
        mi = new JMenuItem("Copy All", 'a');
        mi.setActionCommand(ident + "pmCopyAll");
        mi.addActionListener(this);
        pm.add(mi);
        mi = new JMenuItem("Cut");
        mi.setActionCommand(ident + "pmCut");
        mi.addActionListener(this);
        pm.add(mi);
        mi = new JMenuItem("Paste", 'p');
        mi.setActionCommand(ident + "pmPaste");
        mi.addActionListener(this);
        pm.add(mi);
        mi = new JMenuItem("Paste & Replace", 'p');
        mi.setActionCommand(ident + "pmPasteReplace");
        mi.addActionListener(this);
        pm.add(mi);
        return pm;
    }

    private void setCtrlAccelerator(JMenuItem mi, char acc) {
        KeyStroke ks = KeyStroke.getKeyStroke(acc, Event.CTRL_MASK);
        mi.setAccelerator(ks);
    }

    private void fillTableColums() {
        deleteTable();
        dtm.setColumnCount(0);
        dtm.addColumn(languages.elementAt(0));
        dtm.addColumn(languages.elementAt(1));
        tfLeft.setEnabled(DictionaryDataFile.supportedLanguages[languagesAll.indexOf(languages.elementAt(0))].isSearchable);
        tfRight.setEnabled(DictionaryDataFile.supportedLanguages[languagesAll.indexOf(languages.elementAt(1))].isSearchable);
    }

    private String getMenuFilename(String path) {
        int maxMenuTextLength = 80;
        if (path.length() > maxMenuTextLength) {
            StringBuffer sb = new StringBuffer(path.substring(path.length() - (maxMenuTextLength - 3)));
            sb.insert(0, "...");
            return sb.toString();
        }
        return path;
    }

    private void addTableRow(Vector newRow) {
        try {
            dtm.addRow(newRow.toArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteTable() {
        dtm.setRowCount(0);
    }

    private void loadDictionary() {
        loadDictionary(fd.show("Choose DictionaryForMIDs jar file", "LOAD", null, "DictionaryForMIDs.jar"));
    }

    private void loadDictionary(String fileName) {
        if (fileName == null) {
            if (languagesAll.size() == 0) {
                loadLanguages();
            }
        } else if (DictionaryForSE.loadJar(fileName)) {
            utilObj = new UtilWin();
            Util.setUtil(utilObj);
            try {
                try {
                    DictionaryDataFile.initValues(false);
                } catch (DictionaryException e) {
                    showDictInitError();
                }
                loadLanguages();
                lStatus.setText("Dictionary successfully loaded: " + (new File(fileName)).getName());
                if (configLoaded) {
                    saveDictHistory(fileName, false);
                }
            } catch (Throwable t) {
                loadLanguages();
                showDictionaryError(t.toString());
            }
        } else {
            showErrorFileNotFound(fileName);
            if (languagesAll.size() == 0) {
                loadLanguages();
            }
        }
    }

    private void loadLanguages() {
        try {
            languagesAll.clear();
            boolean colourSupportNeeded = false;
            for (int language = 0; language < DictionaryDataFile.numberOfAvailableLanguages; ++language) {
                languagesAll.addElement(DictionaryDataFile.supportedLanguages[language].languageDisplayText);
                if (DictionaryDataFile.supportedLanguages[language].contentDefinitionAvailable) {
                    colourSupportNeeded = true;
                }
            }
            colourTextMenu.setEnabled(colourSupportNeeded);
            if (colourSupportNeeded) {
                colourText = colourTextMenu.isSelected();
            } else {
                colourText = colourSupportNeeded;
            }
            if (languagesAll.size() == 0) {
                languagesAll.addElement("N/A");
            }
            leftLangSubMenu.removeAll();
            rightLangSubMenu.removeAll();
            menuBar.remove(1);
            menuBar.add(createPrefsMenu());
            ((JRadioButtonMenuItem) leftLangSubMenu.getItem(0)).setSelected(true);
            ((JRadioButtonMenuItem) rightLangSubMenu.getItem(rightLangSubMenu.getItemCount() - 1)).setSelected(true);
            menuBar.validate();
            languages.clear();
            languages.addElement(languagesAll.elementAt(0));
            languages.addElement(languagesAll.lastElement());
            DictionarySettings.setInputLanguage(0);
            DictionarySettings.setOutputLanguage(createBooleanArray(rightLangSubMenu.getItemCount() - 1, rightLangSubMenu.getItemCount()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean[] createBooleanArray(int sel, int max) {
        boolean[] retValue = new boolean[max];
        for (int i = 0; i < max; i++) {
            if (i == sel) {
                retValue[i] = true;
            } else {
                retValue[i] = false;
            }
        }
        return retValue;
    }

    private String prepareCopy(String text) {
        if (text.startsWith("<html>")) {
            text = text.replaceAll("<br>", System.getProperty("line.separator", "\n"));
            text = text.replaceAll("</?\\b[^>]*>", "");
        }
        return text;
    }

    private void copyToClip() {
        String tmp = null;
        if (table.getSelectedRow() != -1) {
            tmp = table.getValueAt(table.getSelectedRow(), table.getSelectedColumn()).toString();
        }
        tmp = prepareCopy(tmp);
        if (copyToClip(tmp)) {
            setStatus(5000, "Selected text \"" + tmp + "\" copied!");
        }
    }

    private boolean copyToClip(String message) {
        if ((message != null) && (message.length() > 0)) {
            cont = new StringSelection(message);
            clip.setContents(cont, null);
            return true;
        }
        return false;
    }

    private void setStatus(int time, String text) {
        if (!timerStatus.isRunning()) {
            oldStatus = lStatus.getText();
        }
        lStatus.setText(text);
        timerStatus.setInitialDelay(time);
        if (timerStatus.isRunning()) {
            timerStatus.stop();
        }
        timerStatus.start();
    }

    private static boolean loadJar(String jarFile) {
        try {
            if (jarFile == null) {
                return false;
            }
            File check = new File(jarFile);
            if (check.exists()) {
                jar = new JarFile(check);
                FileAccessHandler.setDictionaryDataFileISAccess(new JarInputStreamAccess(jar));
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showDictionaryError(String error) {
        JOptionPane.showMessageDialog(this, "You choose an incompatible dictionary!\nPlease make sure that you use an up to date version of DictionaryForMIDs!\nError: " + error, "Error", JOptionPane.WARNING_MESSAGE);
    }

    private void showDictInitError() {
        JOptionPane.showMessageDialog(this, "The dictionary uses own programcode and may not work as expected.\nPlease check for an update of the 'DictionaryForMIDs on PC' version!", "Error", JOptionPane.WARNING_MESSAGE);
    }

    private void showSearchInProgress() {
        JOptionPane.showMessageDialog(this, "Search is already in progress!", "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showDictInfo() {
        JOptionPane.showMessageDialog(this, DictionaryDataFile.infoText, "Dictionary Information", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showErrorFileNotFound(String path) {
        JOptionPane.showMessageDialog(this, "Dictionary File not found!\n" + path, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void performSearch(String search) {
        lastSearch = search;
        searchThread = new Thread(new Runnable() {

            public void run() {
                try {
                    lStatus.setText("Searching...");
                    boolean[] inputLanguages = new boolean[DictionaryDataFile.numberOfAvailableLanguages];
                    for (int languageCount = 0; languageCount < DictionaryDataFile.numberOfAvailableLanguages; ++languageCount) inputLanguages[languageCount] = false;
                    inputLanguages[DictionarySettings.getInputLanguage()] = true;
                    boolean[] outputLanguages = DictionarySettings.getOutputLanguage();
                    TranslationParameters translationParametersObj = new TranslationParameters(lastSearch, inputLanguages, outputLanguages, false, DictionarySettings.getMaxHits(), DictionarySettings.getDurationForCancelSearch());
                    TranslationExecution.executeTranslation(translationParametersObj);
                } catch (Throwable t) {
                    lStatus.setText("Error while searching...");
                    showDictionaryError(t.toString());
                }
            }
        });
        searchThread.start();
    }

    private void loadPrefs() {
        try {
            prop.load(new FileInputStream(CONFIG_NAME));
            loadDictionary(prop.getProperty("LoadedDictionary"));
            fontName = prop.getProperty("FontName", fontName);
            colourText = new Boolean(prop.getProperty("ColourText", new Boolean(colourText).toString())).booleanValue();
            colourTextMenu.setSelected(colourText);
            languages.clear();
            languages.addElement(prop.getProperty("InputLanguage"));
            languages.addElement(prop.getProperty("OutputLanguage"));
            ((JRadioButtonMenuItem) leftLangSubMenu.getItem(languagesAll.indexOf(languages.elementAt(0)))).setSelected(true);
            ((JRadioButtonMenuItem) rightLangSubMenu.getItem(languagesAll.indexOf(languages.elementAt(1)))).setSelected(true);
            dictHistory.clear();
            for (int i = 0; prop.getProperty("DictHistory." + Integer.toString(i)) != null; i++) {
                if (prop.getProperty("DictHistory." + Integer.toString(i)).length() > 0) {
                    dictHistory.addElement(prop.getProperty("DictHistory." + Integer.toString(i)));
                }
            }
            deleteDoubleEntries();
            createDictHistoryMenu();
            changeFonts();
            configLoaded = true;
        } catch (Exception e) {
        }
    }

    private void savePrefs() {
        try {
            if (jar != null) {
                prop.setProperty("LoadedDictionary", jar.getName());
            }
            prop.setProperty("FontName", fontName);
            prop.setProperty("ColourText", new Boolean(colourText).toString());
            prop.setProperty("InputLanguage", languages.elementAt(0).toString());
            prop.setProperty("OutputLanguage", languages.elementAt(1).toString());
            prop.store(new FileOutputStream(CONFIG_NAME, false), "DictionaryForMIDs " + Util.getUtil().getApplicationVersionString());
        } catch (Exception e) {
            lStatus.setText("Can't save configuration file to disk! Maybe write protected?");
        }
    }

    private void deleteDoubleEntries() {
        Vector tmpDB = new Vector();
        for (int i = 0; i < dictHistory.size(); i++) {
            tmpDB.addElement(dictHistory.elementAt(i).toString().toUpperCase());
        }
        for (int i = 0; i < dictHistory.size(); i++) {
            int tmp = tmpDB.indexOf(tmpDB.elementAt(i), i + 1);
            if (tmp != -1) {
                tmpDB.removeElementAt(tmp);
                dictHistory.removeElementAt(tmp);
                i--;
            }
        }
    }

    private void saveDictHistory(Properties propTmp) {
        for (int i = 0; i < dictHistory.size(); i++) {
            propTmp.setProperty("DictHistory." + Integer.toString(i), dictHistory.elementAt(i).toString());
        }
    }

    private void clearDictHistory(Properties propTmp) {
        for (int i = 0; i < dictHistory.size(); i++) {
            propTmp.setProperty("DictHistory." + Integer.toString(i), "");
        }
        dictHistory.clear();
    }

    void saveDictHistory(String path, boolean clear) {
        if (path != null) {
            dictHistory.insertElementAt(path, 0);
            deleteDoubleEntries();
            if (dictHistory.size() > 10) {
                dictHistory.setSize(10);
            }
        }
        prop.clear();
        File checkConfig = new File(CONFIG_NAME);
        if (checkConfig.exists()) {
            try {
                prop.load(new FileInputStream(checkConfig));
            } catch (Exception e) {
            }
        }
        if (clear) {
            clearDictHistory(prop);
        } else {
            saveDictHistory(prop);
        }
        try {
            prop.store(new FileOutputStream(CONFIG_NAME, false), Util.getUtil().getApplicationVersionString());
        } catch (Exception e) {
        }
        createDictHistoryMenu();
    }

    /**
	 * Open the Prefs Dialog for the print output!
	 */
    private void openFontPrefsDialog() {
        String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        ppd = new JDialog(this, "Font Prefs Dialog", true);
        ppd.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        JPanel ppdRoot = (JPanel) ppd.getContentPane();
        lFontTest = new JLabel("Demo Text: JDiskDBPro");
        bUse = new JButton("Use");
        bUse.addActionListener(this);
        bCancelPrefs = new JButton("Cancel");
        bCancelPrefs.addActionListener(this);
        JToolBar tbBottom = new JToolBar();
        cbFont = new JComboBox(fontNames);
        cbFont.setSelectedItem(fontName);
        cbFont.addActionListener(this);
        cbFont.setMaximumRowCount(20);
        lFontTest.setFont(new Font(cbFont.getSelectedItem().toString(), Font.BOLD, 12));
        tbBottom.setLayout(new FlowLayout());
        tbBottom.setFloatable(false);
        tbBottom.add(bUse);
        tbBottom.add(bCancelPrefs);
        ppdRoot.setLayout(new BorderLayout());
        ppdRoot.add(lFontTest, BorderLayout.NORTH);
        ppdRoot.add(cbFont, BorderLayout.CENTER);
        ppdRoot.add(tbBottom, BorderLayout.SOUTH);
        ppd.setSize(200, 110);
        ppd.setLocationRelativeTo(this);
        ppd.setResizable(false);
        ppd.setVisible(true);
    }

    /**
	 * Close the print prefs dialog
	 */
    private void closeFontPrefsDialog() {
        ppd.setVisible(false);
        ppd.dispose();
    }

    private void changeFonts() {
        tfLeft.setFont(new Font(fontName, Font.PLAIN, 12));
        tfRight.setFont(new Font(fontName, Font.PLAIN, 12));
        table.setFont(new Font(fontName, Font.PLAIN, 12));
    }

    private String getColouredHTMLText(String text, int languageIndex) {
        try {
            ContentParser cp = new ContentParser();
            StringColourItemText result = cp.determineItemsFromContent(new TextOfLanguage(text, languageIndex), false, false);
            StringBuffer returnString = new StringBuffer();
            String lastColour = null;
            if (colourText) {
                returnString.append("<html><nobr>");
            }
            for (int i = 0; i < ((StringColourItemText) result).size(); i++) {
                StringColourItemTextPart part = ((StringColourItemText) result).getItemTextPart(i);
                if (colourText) {
                    lastColour = part.getColour().getHexValue();
                    returnString.append("<font color=\"");
                    returnString.append(lastColour);
                    returnString.append("\">");
                    switch(part.getStyle().style) {
                        case FontStyle.bold:
                            returnString.append("<b>");
                            break;
                        case FontStyle.italic:
                            returnString.append("<i>");
                            break;
                        case FontStyle.underlined:
                            returnString.append("<u>");
                            break;
                    }
                    returnString.append(part.getText().replaceAll("\n", "</font></nobr><br><nobr><font color=\"" + lastColour + "\">"));
                    switch(part.getStyle().style) {
                        case FontStyle.bold:
                            returnString.append("</b>");
                            break;
                        case FontStyle.italic:
                            returnString.append("</i>");
                            break;
                        case FontStyle.underlined:
                            returnString.append("</u>");
                            break;
                    }
                    returnString.append("</font>");
                } else {
                    returnString.append(part.getText().replaceAll("\n", " - "));
                }
            }
            if (colourText) {
                returnString.append("</nobr></html>");
            }
            return returnString.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return text;
        }
    }

    public void deletePreviousTranslationResult() {
        deleteTable();
    }

    public void newTranslationResult(TranslationResult resultOfTranslation) {
        Vector tmp = new Vector();
        if (resultOfTranslation.translationFound()) {
            Enumeration translationsEnum = resultOfTranslation.getAllTranslations();
            while (translationsEnum.hasMoreElements()) {
                tmp.clear();
                SingleTranslation singleTranslation = (SingleTranslation) translationsEnum.nextElement();
                String fromTextString = ((TextOfLanguage) singleTranslation.getFromText()).getText();
                String toTextString = ((TextOfLanguage) singleTranslation.getToTexts().elementAt(0)).getText();
                if (rightSearch) {
                    tmp.addElement(getColouredHTMLText(toTextString, languagesAll.indexOf(languages.elementAt(0))));
                    tmp.addElement(getColouredHTMLText(fromTextString, languagesAll.indexOf(languages.elementAt(1))));
                } else {
                    tmp.addElement(getColouredHTMLText(fromTextString, languagesAll.indexOf(languages.elementAt(0))));
                    tmp.addElement(getColouredHTMLText(toTextString, languagesAll.indexOf(languages.elementAt(1))));
                }
                addTableRow(tmp);
            }
            lStatus.setText(Long.toString(resultOfTranslation.numberOfFoundTranslations()) + " entries found in " + Double.toString(resultOfTranslation.executionTime / 1000.0) + " seconds!");
        } else {
            lStatus.setText("Nothing found for \"" + lastSearch + "\"!");
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == tfLeft) {
            DictionarySettings.setInputLanguage(languagesAll.indexOf(languages.elementAt(0)));
            DictionarySettings.setOutputLanguage(createBooleanArray(languagesAll.indexOf(languages.elementAt(1)), languagesAll.size()));
            if (searchThread != null) {
                if (searchThread.isAlive()) {
                    showSearchInProgress();
                } else {
                    rightSearch = false;
                    performSearch(tfLeft.getText());
                }
            } else {
                rightSearch = false;
                performSearch(tfLeft.getText());
            }
            tfLeft.selectAll();
        } else if (e.getSource() == tfRight) {
            DictionarySettings.setInputLanguage(languagesAll.indexOf(languages.elementAt(1)));
            DictionarySettings.setOutputLanguage(createBooleanArray(languagesAll.indexOf(languages.elementAt(0)), languagesAll.size()));
            if (searchThread != null) {
                if (searchThread.isAlive()) {
                    showSearchInProgress();
                } else {
                    rightSearch = true;
                    performSearch(tfRight.getText());
                }
            } else {
                rightSearch = true;
                performSearch(tfRight.getText());
            }
            tfRight.selectAll();
        } else if (e.getSource().equals(colourTextMenu)) {
            colourText = colourTextMenu.isSelected();
        } else if (e.getSource().equals(timerStatus)) {
            lStatus.setText(oldStatus);
        } else if (e.getActionCommand().startsWith("LangL")) {
            DictionarySettings.setInputLanguage(languagesAll.indexOf(e.getActionCommand().substring(5)));
            languages.setElementAt(e.getActionCommand().substring(5), 0);
            fillTableColums();
        } else if (e.getActionCommand().startsWith("LangR")) {
            DictionarySettings.setOutputLanguage(createBooleanArray(languagesAll.indexOf(e.getActionCommand().substring(5)), languagesAll.size()));
            languages.setElementAt(e.getActionCommand().substring(5), 1);
            fillTableColums();
        } else if (e.getActionCommand().equals("openDict")) {
            loadDictionary();
            fillTableColums();
        } else if (e.getActionCommand().startsWith("HistoryOpen")) {
            loadDictionary(e.getActionCommand().substring(11));
            fillTableColums();
        } else if (e.getActionCommand().equals("dictInfo")) {
            showDictInfo();
        } else if (e.getActionCommand().equals("about")) {
            try {
                JOptionPane.showMessageDialog(this, "DictionaryForMIDs " + Util.getUtil().getApplicationVersionString() + " (11.02.2007)\n(c) 2005-2007 by Stefan Martens & Gert Nuber\n\nVisit our homepages:\nhttp://dictionarymid.sourceforge.net/\nhttp://www.stefan1200.de", "About", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        } else if (e.getActionCommand().equals("quit")) {
            this.setVisible(false);
            this.dispose();
            System.exit(0);
        } else if (e.getActionCommand().equals("savePrefs")) {
            savePrefs();
        } else if (e.getActionCommand().equals("changeFont")) {
            openFontPrefsDialog();
        } else if (e.getActionCommand().equals("clearHistory")) {
            saveDictHistory(null, true);
        } else if (e.getSource().equals(cbFont)) {
            lFontTest.setFont(new Font(cbFont.getSelectedItem().toString(), Font.BOLD, 12));
        } else if (e.getSource().equals(bUse)) {
            fontName = cbFont.getSelectedItem().toString();
            closeFontPrefsDialog();
            changeFonts();
        } else if (e.getSource().equals(bCancelPrefs)) {
            closeFontPrefsDialog();
        } else if (e.getActionCommand().equals("LpmCopy")) {
            tfLeft.copy();
        } else if (e.getActionCommand().equals("RpmCopy")) {
            tfRight.copy();
        } else if (e.getActionCommand().equals("LpmCopyAll")) {
            copyToClip(tfLeft.getText());
        } else if (e.getActionCommand().equals("RpmCopyAll")) {
            copyToClip(tfRight.getText());
        } else if (e.getActionCommand().equals("LpmCut")) {
            tfLeft.cut();
        } else if (e.getActionCommand().equals("RpmCut")) {
            tfRight.cut();
        } else if (e.getActionCommand().equals("LpmPaste")) {
            tfLeft.paste();
        } else if (e.getActionCommand().equals("RpmPaste")) {
            tfRight.paste();
        } else if (e.getActionCommand().equals("LpmPasteReplace")) {
            tfLeft.selectAll();
            tfLeft.paste();
        } else if (e.getActionCommand().equals("RpmPasteReplace")) {
            tfRight.selectAll();
            tfRight.paste();
        }
    }

    public void mousePressed(MouseEvent e) {
        if (e.getSource().equals(table)) {
            if (e.isPopupTrigger()) {
                copyToClip();
            }
        } else if (e.getSource().equals(tfLeft)) {
            if (e.isPopupTrigger()) {
                pmListL.show(tfLeft, e.getX(), e.getY());
            }
        } else if (e.getSource().equals(tfRight)) {
            if (e.isPopupTrigger()) {
                pmListR.show(tfRight, e.getX(), e.getY());
            }
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (e.getSource().equals(table)) {
            if (e.isPopupTrigger()) {
                copyToClip();
            }
        } else if (e.getSource().equals(tfLeft)) {
            if (e.isPopupTrigger()) {
                pmListL.show(tfLeft, e.getX(), e.getY());
            }
        } else if (e.getSource().equals(tfRight)) {
            if (e.isPopupTrigger()) {
                pmListR.show(tfRight, e.getX(), e.getY());
            }
        }
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void insertUpdate(DocumentEvent e) {
        if (tfLeft.getDocument() == e.getDocument()) {
        } else if (tfRight.getDocument() == e.getDocument()) {
        }
    }

    public void removeUpdate(DocumentEvent e) {
        if (tfLeft.getDocument() == e.getDocument()) {
        } else if (tfRight.getDocument() == e.getDocument()) {
        }
    }

    public void changedUpdate(DocumentEvent e) {
    }
}

class MyTableCellRenderer extends DefaultTableCellRenderer {

    public MyTableCellRenderer() {
        setOpaque(true);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            super.setForeground(table.getSelectionForeground());
            super.setBackground(table.getSelectionBackground());
        } else {
            super.setForeground(table.getForeground());
            super.setBackground(table.getBackground());
        }
        if (hasFocus) {
            Border border = null;
            if (isSelected) {
                border = UIManager.getBorder("Table.focusSelectedCellHighlightBorder");
            }
            if (border == null) {
                border = UIManager.getBorder("Table.focusCellHighlightBorder");
            }
            setBorder(border);
            if (!isSelected && table.isCellEditable(row, column)) {
                Color col;
                col = UIManager.getColor("Table.focusCellForeground");
                if (col != null) {
                    super.setForeground(col);
                }
                col = UIManager.getColor("Table.focusCellBackground");
                if (col != null) {
                    super.setBackground(col);
                }
            }
        } else {
            setBorder(noFocusBorder);
        }
        setFont(table.getFont());
        setText(null);
        setIcon(null);
        if (value == null) {
            setText("null");
        } else if (value instanceof Icon) {
            setIcon((Icon) value);
        } else if (value instanceof Color) {
            Color color = (Color) value;
            setForeground(color);
            setText(color.getRed() + ", " + color.getGreen() + ", " + color.getBlue());
        } else if (value instanceof Boolean) {
            if (((Boolean) value).booleanValue()) {
                setText("yes");
            } else {
                setText("no");
            }
        } else {
            setText(value.toString());
        }
        switch(column) {
            case 0:
                setHorizontalAlignment(LEFT);
                break;
            case 1:
                setHorizontalAlignment(LEFT);
                break;
            case 2:
                setHorizontalAlignment(LEFT);
                break;
            case 3:
                setHorizontalAlignment(LEFT);
                break;
            case 4:
                setHorizontalAlignment(LEFT);
                break;
            default:
                setHorizontalAlignment(LEFT);
                break;
        }
        if (table.getRowHeight(row) < getPreferredSize().height) {
            table.setRowHeight(row, getPreferredSize().height);
        }
        return this;
    }
}

class MyHeaderCellRenderer extends JLabel implements TableCellRenderer {

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setText(value.toString());
        setFont(new Font(table.getFont().getName(), Font.BOLD, table.getFont().getSize()));
        setBorder(UIManager.getBorder("TableHeader.cellBorder"));
        setHorizontalAlignment(SwingConstants.CENTER);
        return this;
    }
}

class AWTFileDialog extends Frame {

    private String saveLastDir = System.getProperty("user.dir", "");

    String show(String title, String mode, String dir, String fileName) {
        if (dir == null) {
            dir = saveLastDir;
        }
        if (mode.equalsIgnoreCase("LOAD")) {
            FileDialog chooser = new FileDialog(this, title, FileDialog.LOAD);
            chooser.setDirectory(dir);
            chooser.setFile(fileName);
            chooser.setModal(true);
            chooser.show();
            if (chooser.getFile() == null) {
                return null;
            } else {
                saveLastDir = chooser.getDirectory();
                return chooser.getDirectory() + chooser.getFile();
            }
        } else if (mode.equalsIgnoreCase("SAVE")) {
            FileDialog chooser = new FileDialog(this, title, FileDialog.SAVE);
            chooser.setDirectory(dir);
            chooser.setFile(fileName);
            chooser.setModal(true);
            chooser.show();
            if (chooser.getFile() == null) {
                return null;
            } else {
                saveLastDir = chooser.getDirectory();
                return chooser.getDirectory() + chooser.getFile();
            }
        }
        if (mode.equalsIgnoreCase("DIR")) {
            FileDialog chooser = new FileDialog(this, title, FileDialog.LOAD);
            chooser.setDirectory(dir);
            chooser.setFile(fileName);
            chooser.setModal(true);
            chooser.show();
            if (chooser.getFile() == null) {
                return null;
            } else {
                saveLastDir = chooser.getDirectory();
                return chooser.getDirectory();
            }
        } else {
            return null;
        }
    }
}
