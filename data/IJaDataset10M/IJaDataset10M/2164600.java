package net.sourceforge.vietpad.components;

import java.awt.*;
import java.awt.event.*;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.text.*;
import java.beans.*;
import java.util.regex.*;
import net.sourceforge.vietpad.PreferencesDialog;
import net.sourceforge.vietpad.VietPad;
import net.sourceforge.vietpad.inputmethod.VietKeyListener;
import net.sourceforge.vietpad.utilities.VietUtilities;

/**
 *  Find and Replace Dialog
 *
 *@author     Quan Nguyen
 *@author     Gero Herrmann
 *@version    1.9, 28 February 2010
 *@see        "http://vietpad.sourceforge.net"
 */
public class FindDialog extends JDialog {

    private VietPad m_owner;

    private JTabbedPane m_tb;

    private JTextField m_txtFind1, m_txtFind2, m_txtReplace;

    private JCheckBox chkDiacritics, chkRegex, chkWholeWord, chkCase;

    private JComboBox m_cbFind1, m_cbFind2, cbReplace;

    private JButton btFindNext1, btFindNext2, btReplace, btReplaceAll;

    private Document m_docFind;

    private ButtonModel m_modelWholeWord, m_modelCase, m_modelDiacritics, m_modelRegex, m_modelUp, m_modelDown;

    private JTextComponent txtbox;

    private static boolean mouse = false;

    private ResourceBundle bundle;

    final boolean LINUX = System.getProperty("os.name").equals("Linux");

    /**
     *  Constructor for the FindDialog object
     *
     *@param owner  VietPad
     *@param index  Find or Replace
     */
    public FindDialog(final VietPad owner, final int index) {
        super(owner, false);
        setLocale(owner.getLocale());
        bundle = ResourceBundle.getBundle("net.sourceforge.vietpad.components.FindDialog");
        m_owner = owner;
        this.setTitle(bundle.getString("Find_and_Replace"));
        txtbox = this.m_owner.getTextArea();
        setResizable(VietPad.MAC_OS_X);
        initComponents();
        m_tb.setSelectedIndex(index);
        KeyboardFocusManager focusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        focusManager.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if ("focusOwner".equals(e.getPropertyName()) && (e.getNewValue() instanceof JTextField) && !mouse) {
                    if (!LINUX) {
                        ((JTextField) e.getNewValue()).selectAll();
                    }
                }
            }
        });
        KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
        Action escapeAction = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        };
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeKeyStroke, "ESCAPE");
        getRootPane().getActionMap().put("ESCAPE", escapeAction);
        WindowListener flst = new WindowAdapter() {

            @Override
            public void windowOpened(WindowEvent e) {
                super.windowOpened(e);
                boolean textExists = m_docFind.getLength() > 0;
                btFindNext1.setEnabled(textExists);
                btFindNext2.setEnabled(textExists);
                btReplace.setEnabled(textExists);
                btReplaceAll.setEnabled(textExists && !(chkRegex.isSelected() && !chkDiacritics.isSelected()));
            }

            @Override
            public void windowActivated(WindowEvent e) {
                getInputContext().selectInputMethod(getOwner().getInputContext().getLocale());
                worker();
            }
        };
        addWindowListener(flst);
    }

    /**
     *  Called from within the constructor to initialize the form
     */
    private void initComponents() {
        m_tb = new JTabbedPane();
        JPanel p1 = new JPanel(new BorderLayout());
        JPanel pc1 = new JPanel(new BorderLayout());
        JPanel pf = new JPanel();
        pf.setLayout(new DialogLayout2(20, 5));
        pf.setBorder(new EmptyBorder(8, 5, 8, 0));
        pf.add(new JLabel(bundle.getString("Search_For") + ":"));
        ActionListener findAction = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                populateComboBox("Find");
                FindNext();
            }
        };
        MouseListener mouseLst = new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                mouse = true;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mouse = false;
            }
        };
        m_cbFind1 = new JComboBox();
        m_cbFind1.setEditable(true);
        m_cbFind1.setMaximumRowCount(7);
        m_txtFind1 = (JTextField) m_cbFind1.getEditor().getEditorComponent();
        m_txtFind1.getInputMap().put(KeyStroke.getKeyStroke("ctrl H"), "none");
        m_txtFind1.addActionListener(findAction);
        m_txtFind1.addMouseListener(mouseLst);
        m_txtFind1.addKeyListener(new VietKeyListener(m_txtFind1));
        m_docFind = m_txtFind1.getDocument();
        m_txtFind1.addCaretListener(new CaretListener() {

            @Override
            public void caretUpdate(CaretEvent e) {
                boolean textExists = m_docFind.getLength() > 0;
                btFindNext1.setEnabled(textExists);
                btFindNext2.setEnabled(textExists);
                btReplace.setEnabled(textExists);
                btReplaceAll.setEnabled(textExists && !(chkRegex.isSelected() && !chkDiacritics.isSelected()));
            }
        });
        pf.add(m_cbFind1);
        pc1.add(pf, BorderLayout.CENTER);
        JPanel po = new JPanel(new GridLayout(3, 2, 28, 2));
        po.setBorder(new TitledBorder(new EtchedBorder(), bundle.getString("Options")));
        chkCase = new JCheckBox(bundle.getString("Match_Case"));
        chkCase.setMnemonic('s');
        m_modelCase = chkCase.getModel();
        po.add(chkCase);
        ButtonGroup bg = new ButtonGroup();
        JRadioButton rdUp = new JRadioButton(bundle.getString("Search_Up"));
        rdUp.setMnemonic('u');
        m_modelUp = rdUp.getModel();
        bg.add(rdUp);
        po.add(rdUp);
        chkWholeWord = new JCheckBox(bundle.getString("Match_Whole_Word"));
        chkWholeWord.setMnemonic('w');
        m_modelWholeWord = chkWholeWord.getModel();
        po.add(chkWholeWord);
        JRadioButton rdDown = new JRadioButton(bundle.getString("Search_Down"), true);
        rdDown.setMnemonic('d');
        m_modelDown = rdDown.getModel();
        bg.add(rdDown);
        po.add(rdDown);
        pc1.add(po, BorderLayout.SOUTH);
        chkDiacritics = new JCheckBox(bundle.getString("Match_Diacritics"));
        m_modelDiacritics = chkDiacritics.getModel();
        po.add(chkDiacritics);
        chkRegex = new JCheckBox("Regular Expression");
        m_modelRegex = chkRegex.getModel();
        po.add(chkRegex);
        p1.add(pc1, BorderLayout.CENTER);
        JPanel p01 = new JPanel(new FlowLayout());
        JPanel p = new JPanel(new GridLayout(2, 1, 2, 8));
        KeyListener findKeyLst = new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent ke) {
                if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
                    FindNext();
                }
            }
        };
        btFindNext1 = new JButton(bundle.getString("Find_Next"));
        btFindNext1.addActionListener(findAction);
        btFindNext1.addKeyListener(findKeyLst);
        btFindNext1.setMnemonic('f');
        p.add(btFindNext1);
        ActionListener closeAction = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        };
        JButton btClose = new JButton(bundle.getString("Close"));
        btClose.addActionListener(closeAction);
        btClose.setDefaultCapable(true);
        p.add(btClose);
        p01.add(p);
        p1.add(p01, BorderLayout.EAST);
        PreferencesDialog.setOpaque(p1, false);
        m_tb.addTab(bundle.getString("Find"), p1);
        JPanel p2 = new JPanel(new BorderLayout());
        JPanel pc2 = new JPanel(new BorderLayout());
        JPanel pc = new JPanel();
        pc.setLayout(new DialogLayout2(20, 5));
        pc.setBorder(new EmptyBorder(8, 5, 8, 0));
        pc.add(new JLabel(bundle.getString("Search_For") + ":"));
        m_cbFind2 = new JComboBox();
        m_cbFind2.setModel(m_cbFind1.getModel());
        m_cbFind2.setEditable(true);
        m_cbFind2.setMaximumRowCount(7);
        m_txtFind2 = (JTextField) m_cbFind2.getEditor().getEditorComponent();
        m_txtFind2.getInputMap().put(KeyStroke.getKeyStroke("ctrl H"), "none");
        m_txtFind2.addActionListener(findAction);
        m_txtFind2.addMouseListener(mouseLst);
        m_txtFind2.addKeyListener(new VietKeyListener(m_txtFind2));
        m_txtFind2.setDocument(m_docFind);
        pc.add(m_cbFind2);
        pc.add(new JLabel(bundle.getString("Replace_With") + ":"));
        cbReplace = new JComboBox();
        cbReplace.setEditable(true);
        cbReplace.setMaximumRowCount(7);
        m_txtReplace = (JTextField) cbReplace.getEditor().getEditorComponent();
        m_txtReplace.getInputMap().put(KeyStroke.getKeyStroke("ctrl H"), "none");
        m_txtReplace.addActionListener(findAction);
        m_txtReplace.addMouseListener(mouseLst);
        m_txtReplace.addKeyListener(new VietKeyListener(m_txtReplace));
        pc.add(cbReplace);
        pc2.add(pc);
        po = new JPanel(new GridLayout(3, 2, 28, 2));
        po.setBorder(new TitledBorder(new EtchedBorder(), bundle.getString("Options")));
        ItemListener focus4Find2 = new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (m_tb.getSelectedIndex() == 0) {
                    btFindNext1.requestFocusInWindow();
                } else {
                    btFindNext2.requestFocusInWindow();
                }
                Object source = e.getItemSelectable();
                if (source == chkDiacritics || source == chkRegex) {
                    btReplaceAll.setEnabled(m_docFind.getLength() > 0 && !(chkRegex.isSelected() && !chkDiacritics.isSelected()));
                }
                if (source == chkRegex) {
                    chkWholeWord.setEnabled(!chkRegex.isSelected());
                }
            }
        };
        chkCase = new JCheckBox(bundle.getString("Match_Case"));
        chkCase.addItemListener(focus4Find2);
        chkCase.setMnemonic('s');
        chkCase.setModel(m_modelCase);
        po.add(chkCase);
        bg = new ButtonGroup();
        rdUp = new JRadioButton(bundle.getString("Search_Up"));
        rdUp.addItemListener(focus4Find2);
        rdUp.setMnemonic('u');
        rdUp.setModel(m_modelUp);
        bg.add(rdUp);
        po.add(rdUp);
        chkWholeWord = new JCheckBox(bundle.getString("Match_Whole_Word"));
        chkWholeWord.addItemListener(focus4Find2);
        chkWholeWord.setMnemonic('w');
        chkWholeWord.setModel(m_modelWholeWord);
        po.add(chkWholeWord);
        rdDown = new JRadioButton(bundle.getString("Search_Down"), true);
        rdDown.addItemListener(focus4Find2);
        rdDown.setMnemonic('d');
        rdDown.setModel(m_modelDown);
        bg.add(rdDown);
        po.add(rdDown);
        pc2.add(po, BorderLayout.SOUTH);
        chkDiacritics = new JCheckBox(bundle.getString("Match_Diacritics"));
        chkDiacritics.addItemListener(focus4Find2);
        chkDiacritics.setModel(m_modelDiacritics);
        po.add(chkDiacritics);
        chkRegex = new JCheckBox("Regular Expression");
        chkRegex.addItemListener(focus4Find2);
        chkRegex.setModel(m_modelRegex);
        po.add(chkRegex);
        p2.add(pc2);
        JPanel p02 = new JPanel(new FlowLayout());
        p = new JPanel(new GridLayout(4, 1, 2, 8));
        btFindNext2 = new JButton(bundle.getString("Find_Next"));
        btFindNext2.addActionListener(findAction);
        btFindNext2.addKeyListener(findKeyLst);
        btFindNext2.setMnemonic('f');
        p.add(btFindNext2);
        ActionListener replaceAction = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                populateComboBox("Replace");
                Replace();
            }
        };
        KeyListener replaceKeyLst = new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent ke) {
                if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
                    Replace();
                }
            }
        };
        btReplace = new JButton(bundle.getString("Replace"));
        btReplace.addActionListener(replaceAction);
        btReplace.addKeyListener(replaceKeyLst);
        btReplace.setMnemonic('r');
        p.add(btReplace);
        ActionListener replaceAllAction = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                populateComboBox("Replace");
                m_owner.getUndoSupport().beginUpdate();
                ReplaceAll();
                m_owner.getUndoSupport().endUpdate();
            }
        };
        btReplaceAll = new JButton(bundle.getString("Replace_All"));
        btReplaceAll.addActionListener(replaceAllAction);
        btReplaceAll.setMnemonic('a');
        p.add(btReplaceAll);
        btClose = new JButton(bundle.getString("Close"));
        btClose.addActionListener(closeAction);
        btClose.setDefaultCapable(true);
        p.add(btClose);
        p02.add(p);
        p2.add(p02, BorderLayout.EAST);
        p01.setPreferredSize(p02.getPreferredSize());
        PreferencesDialog.setOpaque(p2, false);
        m_tb.addTab(bundle.getString("Replace"), p2);
        m_tb.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                worker();
            }
        });
        JPanel pp = new JPanel(new BorderLayout());
        pp.setBorder(new EmptyBorder(5, 5, 5, 5));
        pp.add(m_tb, BorderLayout.CENTER);
        getContentPane().add(pp, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
    }

    /**
     * Populates the combobox with entries from the corresponding text field
     */
    void populateComboBox(String button) {
        String text;
        JComboBox comboBox;
        if (button.equals("Find")) {
            text = m_txtFind1.getText();
            comboBox = m_cbFind1;
        } else {
            text = m_txtReplace.getText();
            comboBox = cbReplace;
        }
        if (text.equals("")) {
            return;
        }
        boolean isEntryExisted = false;
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            if (text.equals((String) comboBox.getItemAt(i))) {
                isEntryExisted = true;
                break;
            }
        }
        if (!isEntryExisted) {
            comboBox.insertItemAt(text, 0);
            comboBox.setSelectedIndex(0);
        }
    }

    /**
     *  Resolves the race condition with requesting focus
     */
    void worker() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                if (m_tb.getSelectedIndex() == 0) {
                    m_txtFind1.requestFocusInWindow();
                    if (!LINUX) {
                        m_txtFind1.selectAll();
                    }
                } else {
                    m_txtFind2.requestFocusInWindow();
                    if (!LINUX) {
                        m_txtFind2.selectAll();
                    }
                }
            }
        });
    }

    /**
     *  Sets Find or Replace tab
     *
     *@param index  Index
     */
    public void setSelectedIndex(int index) {
        m_tb.setSelectedIndex(index);
        setVisible(true);
    }

    /**
     * Finds next occurrence of find string.
     * @return
     */
    boolean FindNext() {
        String searchData, strFind;
        if (!m_modelDiacritics.isSelected()) {
            searchData = VietUtilities.stripDiacritics(txtbox.getText());
            strFind = VietUtilities.stripDiacritics(this.m_txtFind1.getText());
        } else {
            searchData = txtbox.getText();
            strFind = this.m_txtFind1.getText();
        }
        if (m_modelDown.isSelected()) {
            int iStart = txtbox.getSelectionEnd();
            if (m_modelRegex.isSelected() || m_modelWholeWord.isSelected()) {
                if (m_modelWholeWord.isSelected() && m_modelWholeWord.isEnabled()) {
                    strFind = "\\b" + Pattern.quote(strFind) + "\\b";
                }
                try {
                    Pattern regex = Pattern.compile((m_modelCase.isSelected() ? "" : "(?i)") + strFind, Pattern.MULTILINE);
                    Matcher m = regex.matcher(searchData);
                    m.region(iStart, txtbox.getDocument().getLength());
                    if (m.find()) {
                        txtbox.select(m.start(), m.end());
                        return true;
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e.getMessage(), bundle.getString("Regex_Error"), JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            } else {
                while (iStart + strFind.length() <= txtbox.getDocument().getLength()) {
                    if (searchData.regionMatches(!m_modelCase.isSelected(), iStart, strFind, 0, strFind.length())) {
                        txtbox.select(iStart, iStart + strFind.length());
                        return true;
                    }
                    iStart++;
                }
            }
        } else {
            if (m_modelRegex.isSelected() || m_modelWholeWord.isSelected()) {
                if (m_modelWholeWord.isSelected() && m_modelWholeWord.isEnabled()) {
                    strFind = "\\b" + Pattern.quote(strFind) + "\\b";
                }
                int iEnd = txtbox.getSelectionStart();
                try {
                    Pattern regex = Pattern.compile((m_modelCase.isSelected() ? "" : "(?i)") + String.format("%1$s(?!.*%1$s)", strFind), Pattern.MULTILINE | Pattern.DOTALL);
                    Matcher m = regex.matcher(searchData);
                    m.region(0, iEnd);
                    if (m.find()) {
                        txtbox.select(m.start(), m.end());
                        return true;
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e.getMessage(), bundle.getString("Regex_Error"), JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            } else {
                int iStart = txtbox.getSelectionStart() - strFind.length();
                while (iStart >= 0) {
                    if (searchData.regionMatches(!m_modelCase.isSelected(), iStart, strFind, 0, strFind.length())) {
                        txtbox.select(iStart, iStart + strFind.length());
                        return true;
                    }
                    iStart--;
                }
            }
        }
        int n = JOptionPane.showConfirmDialog(this, bundle.getString("Cannot_find_\"") + m_txtFind1.getText() + "\".\n" + bundle.getString("Continue_search_from_") + (m_modelDown.isSelected() ? bundle.getString("beginning") : bundle.getString("end")) + "?", "VietPad", JOptionPane.YES_NO_OPTION);
        if (n == JOptionPane.YES_OPTION) {
            if (m_modelDown.isSelected()) {
                txtbox.setSelectionStart(0);
            } else {
                txtbox.setSelectionStart(txtbox.getDocument().getLength());
            }
            txtbox.select(txtbox.getSelectionStart(), txtbox.getSelectionStart());
            FindNext();
        }
        return false;
    }

    /**
     * Replaces currently selected text with replacement string.
     */
    void Replace() {
        String strFind = this.m_txtFind2.getText();
        String selectedText = txtbox.getSelectedText();
        if (selectedText == null) {
            FindNext();
            return;
        }
        if (!m_modelDiacritics.isSelected()) {
            strFind = VietUtilities.stripDiacritics(strFind);
            selectedText = VietUtilities.stripDiacritics(selectedText);
        }
        String strReplace = this.m_txtReplace.getText();
        int start = txtbox.getSelectionStart();
        if (m_modelRegex.isSelected()) {
            try {
                Pattern regex = Pattern.compile((m_modelCase.isSelected() ? "" : "(?i)") + strFind, Pattern.MULTILINE);
                txtbox.replaceSelection(regex.matcher(selectedText).replaceAll(unescape(strReplace)));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), bundle.getString("Regex_Error"), JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else if ((m_modelCase.isSelected() && selectedText.compareTo(strFind) == 0) || (!m_modelCase.isSelected() && selectedText.compareToIgnoreCase(strFind) == 0)) {
            txtbox.replaceSelection(strReplace);
        }
        if (!m_modelDown.isSelected()) {
            txtbox.setSelectionStart(start);
            txtbox.setSelectionEnd(start);
        }
        FindNext();
    }

    /**
     * Replaces all occurrences of find string with replacement.
     */
    void ReplaceAll() {
        String strFind = this.m_txtFind2.getText();
        String str = txtbox.getText();
        String strTemp;
        if (!m_modelDiacritics.isSelected()) {
            strFind = VietUtilities.stripDiacritics(strFind);
            strTemp = VietUtilities.stripDiacritics(str);
        } else {
            strTemp = str;
        }
        String strReplace = this.m_txtReplace.getText();
        int count = 0;
        if (m_modelRegex.isSelected() || m_modelDiacritics.isSelected()) {
            String patt = m_modelRegex.isSelected() ? strFind : Pattern.quote(strFind);
            if (m_modelWholeWord.isSelected() && m_modelWholeWord.isEnabled()) {
                patt = "\\b" + patt + "\\b";
            }
            try {
                Pattern regex = Pattern.compile((m_modelCase.isSelected() ? "" : "(?i)") + patt, Pattern.MULTILINE);
                Matcher matcher = regex.matcher(str);
                while (matcher.find()) {
                    count++;
                }
                matcher.reset();
                str = regex.matcher(str).replaceAll(unescape(strReplace));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), bundle.getString("Regex_Error"), JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else {
            StringBuilder strB = new StringBuilder(str);
            try {
                Pattern wholewordPatt = Pattern.compile((m_modelCase.isSelected() ? "" : "(?i)") + "\\b" + strFind + "\\b", Pattern.MULTILINE);
                for (int i = 0; i <= strB.length() - strFind.length(); ) {
                    if (strTemp.regionMatches(!m_modelCase.isSelected(), i, strFind, 0, strFind.length())) {
                        if (m_modelWholeWord.isSelected()) {
                            Matcher m = wholewordPatt.matcher(strTemp);
                            if (m.find(i)) {
                                if (i != m.start()) {
                                    i++;
                                    continue;
                                }
                            } else {
                                i++;
                                continue;
                            }
                        }
                        strB.delete(i, i + strFind.length());
                        strB.insert(i, strReplace);
                        if (!m_modelDiacritics.isSelected()) {
                            strTemp = VietUtilities.stripDiacritics(strB.toString());
                        } else {
                            strTemp = strB.toString();
                        }
                        i += strReplace.length();
                        count++;
                    } else {
                        i++;
                    }
                }
                str = strB.toString();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), bundle.getString("Regex_Error"), JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        if (!str.equals(txtbox.getText())) {
            txtbox.setText(str);
            txtbox.select(0, 0);
        }
        warning(String.format(bundle.getString("ReplacedOccurrence"), count));
    }

    private String unescape(String input) {
        return input.replace("\\n", "\n").replace("\\r", "\r").replace("\\t", "\t");
    }

    /**
     *  Display warning message
     *
     *@param message  Warning message
     */
    protected void warning(String message) {
        JOptionPane.showMessageDialog(this, message, VietPad.APP_NAME, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * @return the state of matchCase chkbox
     */
    public boolean isMatchCase() {
        return this.chkCase.isSelected();
    }

    /**
     * @param matchCase the matchCase to set
     */
    public void setMatchCase(boolean matchCase) {
        this.chkCase.setSelected(matchCase);
    }

    /**
     * @return the state of matchDiacritics chkbox
     */
    public boolean isMatchDiacritics() {
        return this.chkDiacritics.isSelected();
    }

    /**
     * @param matchDiacritics the matchDiacritics to set
     */
    public void setMatchDiacritics(boolean matchDiacritics) {
        this.chkDiacritics.setSelected(matchDiacritics);
    }

    /**
     * @return the state of regEx chkbox
     */
    public boolean isRegEx() {
        return this.chkRegex.isSelected();
    }

    /**
     * @param regEx the regEx to set
     */
    public void setRegEx(boolean regEx) {
        this.chkRegex.setSelected(regEx);
    }

    /**
     * @return the state of matchWholeWord chkbox
     */
    public boolean isMatchWholeWord() {
        return this.chkWholeWord.isSelected();
    }

    /**
     * @param matchWholeWord the matchWholeWord to set
     */
    public void setMatchWholeWord(boolean matchWholeWord) {
        this.chkWholeWord.setSelected(matchWholeWord);
    }
}
