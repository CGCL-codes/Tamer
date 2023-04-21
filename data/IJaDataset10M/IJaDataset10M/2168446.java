package net.sf.vietpad;

import java.awt.*;
import java.awt.event.*;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import net.sf.vietpad.inputmethod.VietKeyListener;

/**
 *  Find and Replace Dialog
 *
 *@author     Quan Nguyen
 *@author     Gero Herrmann
 *@version    1.8, 5 May 2007
 *@see        http://vietpad.sourceforge.net
 */
class FindDialog extends JDialog {

    private VietPad m_owner;

    private JTabbedPane m_tb;

    private JTextField m_txtFind1, m_txtFind2, txtReplace;

    private JComboBox m_cbFind1, m_cbFind2, cbReplace;

    private JButton btFindNext1, btFindNext2;

    private Document m_docFind, m_docReplace;

    private ButtonModel m_modelWord, m_modelCase, m_modelDiacritics, m_modelUp, m_modelDown;

    private int m_searchIndex = -1;

    private boolean m_searchUp = false;

    private String m_searchData;

    private Font dialogFont;

    private static boolean mouse = false;

    private ResourceBundle myResources = ResourceBundle.getBundle("net.sf.vietpad.Resources");

    /**
     *  Constructor for the FindDialog object
     *
     *@param owner  VietPad
     *@param index  Find or Replace
     */
    public FindDialog(final VietPad owner, final int index) {
        super(owner, ResourceBundle.getBundle("net.sf.vietpad.Resources").getString("Find_and_Replace"), false);
        setLocale(owner.getLocale());
        setResizable(VietPad.MAC_OS_X);
        m_owner = owner;
        m_tb = new JTabbedPane();
        dialogFont = new Font(VietPad.MAC_OS_X ? "Lucida Grande" : "Tahoma", 0, 12);
        KeyboardFocusManager focusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        focusManager.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent e) {
                if ("focusOwner".equals(e.getPropertyName()) && (e.getNewValue() instanceof JTextField) && !mouse) {
                    ((JTextField) e.getNewValue()).selectAll();
                }
            }
        });
        JPanel p1 = new JPanel(new BorderLayout());
        JPanel pc1 = new JPanel(new BorderLayout());
        JPanel pf = new JPanel();
        pf.setLayout(new DialogLayout2(20, 5));
        pf.setBorder(new EmptyBorder(8, 5, 8, 0));
        pf.add(new JLabel(myResources.getString("Search_For:")));
        ActionListener findAction = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                populateComboBox("Find");
                findNext(false, true);
            }
        };
        MouseListener mouseLst = new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                mouse = true;
            }

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
        m_txtFind1.setFont(dialogFont);
        m_docFind = m_txtFind1.getDocument();
        pf.add(m_cbFind1);
        pc1.add(pf, BorderLayout.CENTER);
        JPanel po = new JPanel(new GridLayout(3, 2, 28, 2));
        po.setBorder(new TitledBorder(new EtchedBorder(), myResources.getString("Options")));
        JCheckBox chkCase = new JCheckBox(myResources.getString("Match_Case"));
        chkCase.setMnemonic('s');
        m_modelCase = chkCase.getModel();
        po.add(chkCase);
        ButtonGroup bg = new ButtonGroup();
        JRadioButton rdUp = new JRadioButton(myResources.getString("Search_Up"));
        rdUp.setMnemonic('u');
        m_modelUp = rdUp.getModel();
        bg.add(rdUp);
        po.add(rdUp);
        JCheckBox chkDiacritics = new JCheckBox(myResources.getString("Match_Diacritics"));
        m_modelDiacritics = chkDiacritics.getModel();
        po.add(chkDiacritics);
        JRadioButton rdDown = new JRadioButton(myResources.getString("Search_Down"), true);
        rdDown.setMnemonic('d');
        m_modelDown = rdDown.getModel();
        bg.add(rdDown);
        po.add(rdDown);
        pc1.add(po, BorderLayout.SOUTH);
        JCheckBox chkWord = new JCheckBox(myResources.getString("Match_Whole_Word"));
        chkWord.setMnemonic('w');
        m_modelWord = chkWord.getModel();
        po.add(chkWord);
        p1.add(pc1, BorderLayout.CENTER);
        JPanel p01 = new JPanel(new FlowLayout());
        JPanel p = new JPanel(new GridLayout(2, 1, 2, 8));
        KeyListener findKeyLst = new KeyAdapter() {

            public void keyPressed(KeyEvent ke) {
                if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
                    findNext(false, true);
                }
            }
        };
        btFindNext1 = new JButton(myResources.getString("Find_Next"));
        btFindNext1.addActionListener(findAction);
        btFindNext1.addKeyListener(findKeyLst);
        btFindNext1.setMnemonic('f');
        p.add(btFindNext1);
        ActionListener closeAction = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        };
        JButton btClose = new JButton(myResources.getString("Close"));
        btClose.addActionListener(closeAction);
        btClose.setDefaultCapable(true);
        p.add(btClose);
        p01.add(p);
        p1.add(p01, BorderLayout.EAST);
        PreferencesDialog.setOpaque(p1, false);
        m_tb.addTab(myResources.getString("Find"), p1);
        JPanel p2 = new JPanel(new BorderLayout());
        JPanel pc2 = new JPanel(new BorderLayout());
        JPanel pc = new JPanel();
        pc.setLayout(new DialogLayout2(20, 5));
        pc.setBorder(new EmptyBorder(8, 5, 8, 0));
        pc.add(new JLabel(myResources.getString("Search_For:")));
        m_cbFind2 = new JComboBox();
        m_cbFind2.setModel(m_cbFind1.getModel());
        m_cbFind2.setEditable(true);
        m_cbFind2.setMaximumRowCount(7);
        m_txtFind2 = (JTextField) m_cbFind2.getEditor().getEditorComponent();
        m_txtFind2.getInputMap().put(KeyStroke.getKeyStroke("ctrl H"), "none");
        m_txtFind2.addActionListener(findAction);
        m_txtFind2.addMouseListener(mouseLst);
        m_txtFind2.addKeyListener(new VietKeyListener(m_txtFind2));
        m_txtFind2.setFont(dialogFont);
        m_txtFind2.setDocument(m_docFind);
        pc.add(m_cbFind2);
        pc.add(new JLabel(myResources.getString("Replace_With:")));
        ActionListener replaceAction = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                populateComboBox("Replace");
                findNext(true, true);
            }
        };
        cbReplace = new JComboBox();
        cbReplace.setEditable(true);
        cbReplace.setMaximumRowCount(7);
        txtReplace = (JTextField) cbReplace.getEditor().getEditorComponent();
        txtReplace.getInputMap().put(KeyStroke.getKeyStroke("ctrl H"), "none");
        txtReplace.addActionListener(findAction);
        txtReplace.addMouseListener(mouseLst);
        txtReplace.addKeyListener(new VietKeyListener(txtReplace));
        txtReplace.setFont(dialogFont);
        m_docReplace = txtReplace.getDocument();
        pc.add(cbReplace);
        pc2.add(pc);
        po = new JPanel(new GridLayout(3, 2, 28, 2));
        po.setBorder(new TitledBorder(new EtchedBorder(), myResources.getString("Options")));
        ItemListener focus4Find2 = new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                if (m_tb.getSelectedIndex() == 0) {
                    btFindNext1.requestFocusInWindow();
                } else {
                    btFindNext2.requestFocusInWindow();
                }
            }
        };
        chkCase = new JCheckBox(myResources.getString("Match_Case"));
        chkCase.addItemListener(focus4Find2);
        chkCase.setMnemonic('s');
        chkCase.setModel(m_modelCase);
        po.add(chkCase);
        bg = new ButtonGroup();
        rdUp = new JRadioButton(myResources.getString("Search_Up"));
        rdUp.addItemListener(focus4Find2);
        rdUp.setMnemonic('u');
        rdUp.setModel(m_modelUp);
        bg.add(rdUp);
        po.add(rdUp);
        chkDiacritics = new JCheckBox(myResources.getString("Match_Diacritics"));
        chkDiacritics.addItemListener(focus4Find2);
        chkDiacritics.setModel(m_modelDiacritics);
        po.add(chkDiacritics);
        rdDown = new JRadioButton(myResources.getString("Search_Down"), true);
        rdDown.addItemListener(focus4Find2);
        rdDown.setMnemonic('d');
        rdDown.setModel(m_modelDown);
        bg.add(rdDown);
        po.add(rdDown);
        pc2.add(po, BorderLayout.SOUTH);
        chkWord = new JCheckBox(myResources.getString("Match_Whole_Word"));
        chkWord.addItemListener(focus4Find2);
        chkWord.setMnemonic('w');
        chkWord.setModel(m_modelWord);
        po.add(chkWord);
        p2.add(pc2);
        JPanel p02 = new JPanel(new FlowLayout());
        p = new JPanel(new GridLayout(4, 1, 2, 8));
        btFindNext2 = new JButton(myResources.getString("Find_Next"));
        btFindNext2.addActionListener(findAction);
        btFindNext2.addKeyListener(findKeyLst);
        btFindNext2.setMnemonic('f');
        p.add(btFindNext2);
        KeyListener replaceKeyLst = new KeyAdapter() {

            public void keyPressed(KeyEvent ke) {
                if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
                    findNext(true, true);
                }
            }
        };
        JButton btReplace = new JButton(myResources.getString("Replace"));
        btReplace.addActionListener(replaceAction);
        btReplace.addKeyListener(replaceKeyLst);
        btReplace.setMnemonic('r');
        p.add(btReplace);
        ActionListener replaceAllAction = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                populateComboBox("Replace");
                int counter = 0;
                JTextArea monitor = m_owner.getTextArea();
                if (m_modelUp.isSelected()) {
                    monitor.setCaretPosition(monitor.getDocument().getLength());
                } else {
                    monitor.setCaretPosition(0);
                }
                m_owner.undoSupport.beginUpdate();
                while (true) {
                    int result = findNext(true, false);
                    if (result < 0) {
                        return;
                    } else if (result == 0) {
                        break;
                    }
                    counter++;
                }
                m_owner.undoSupport.endUpdate();
                warning(counter + (counter == 1 ? myResources.getString("_replacement_has_been_done.") : myResources.getString("_replacements_have_been_done.")));
            }
        };
        JButton btReplaceAll = new JButton(myResources.getString("Replace_All"));
        btReplaceAll.addActionListener(replaceAllAction);
        btReplaceAll.setMnemonic('a');
        p.add(btReplaceAll);
        btClose = new JButton(myResources.getString("Close"));
        btClose.addActionListener(closeAction);
        btClose.setDefaultCapable(true);
        p.add(btClose);
        p02.add(p);
        p2.add(p02, BorderLayout.EAST);
        p01.setPreferredSize(p02.getPreferredSize());
        PreferencesDialog.setOpaque(p2, false);
        m_tb.addTab(myResources.getString("Replace"), p2);
        m_tb.setSelectedIndex(index);
        m_tb.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                worker();
            }
        });
        JPanel pp = new JPanel(new BorderLayout());
        pp.setBorder(new EmptyBorder(5, 5, 5, 5));
        pp.add(m_tb, BorderLayout.CENTER);
        getContentPane().add(pp, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(owner);
        KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
        Action escapeAction = new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        };
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeKeyStroke, "ESCAPE");
        getRootPane().getActionMap().put("ESCAPE", escapeAction);
        WindowListener flst = new WindowAdapter() {

            public void windowActivated(WindowEvent e) {
                getInputContext().selectInputMethod(owner.getInputContext().getLocale());
                m_searchIndex = -1;
                worker();
            }

            public void windowDeactivated(WindowEvent e) {
                m_searchData = null;
            }
        };
        addWindowListener(flst);
    }

    /**
     * Populates the combobox with entries from the corresponding text field
     */
    void populateComboBox(String button) {
        String text;
        JComboBox comboBox;
        if (button == "Find") {
            text = m_txtFind1.getText();
            comboBox = m_cbFind1;
        } else {
            text = txtReplace.getText();
            comboBox = cbReplace;
        }
        if (text.equals("")) return;
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
        final SwingWorker worker = new SwingWorker() {

            public Object construct() {
                if (m_tb.getSelectedIndex() == 0) {
                    SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                            m_txtFind1.requestFocusInWindow();
                            m_txtFind1.selectAll();
                        }
                    });
                } else {
                    SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                            m_txtFind2.requestFocusInWindow();
                            m_txtFind2.selectAll();
                        }
                    });
                }
                return m_tb;
            }
        };
        worker.start();
    }

    /**
     *  Sets Find or Replace tab
     *
     *@param index  Index
     */
    public void setSelectedIndex(int index) {
        m_tb.setSelectedIndex(index);
        setVisible(true);
        m_searchIndex = -1;
    }

    /**
     *  Find next occurrence of search string
     *
     *@param doReplace     Replace operation
     *@param showWarnings  Enable warning message
     *@return              Number of matches
     */
    public int findNext(boolean doReplace, boolean showWarnings) {
        JTextArea monitor = m_owner.getTextArea();
        Document doc = monitor.getDocument();
        int pos = monitor.getCaretPosition();
        if (m_modelUp.isSelected() != m_searchUp) {
            m_searchUp = m_modelUp.isSelected();
            m_searchIndex = -1;
        }
        if (m_searchIndex == -1) {
            try {
                m_searchIndex = pos;
                if (m_searchUp) {
                    m_searchData = doc.getText(0, pos);
                } else {
                    m_searchData = doc.getText(pos, doc.getLength() - pos);
                    pos = 0;
                }
            } catch (BadLocationException ex) {
                warning(ex.toString());
                return -1;
            }
        }
        String key = "";
        try {
            key = m_docFind.getText(0, m_docFind.getLength());
            key = key.replaceAll("\\\\\\\\n", "\f").replaceAll("\\\\n", "\n").replaceAll("\\f", "\\\\n");
            key = key.replaceAll("\\\\\\\\t", "\f").replaceAll("\\\\t", "\t").replaceAll("\\f", "\\\\t");
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
        if (key.length() == 0) {
            warning(myResources.getString("Please_enter_the_target_to_search"));
            return -1;
        }
        if (!m_modelCase.isSelected()) {
            m_searchData = m_searchData.toLowerCase();
            key = key.toLowerCase();
        }
        if (!m_modelDiacritics.isSelected()) {
            m_searchData = VietUtilities.stripDiacritics(m_searchData);
            key = VietUtilities.stripDiacritics(key);
        }
        if (m_modelWord.isSelected()) {
            for (int k = 0; k < WORD_SEPARATORS.length; k++) {
                if (key.indexOf(WORD_SEPARATORS[k]) >= 0) {
                    warning(myResources.getString("The_text_target_contains_an_illegal_character_\'") + WORD_SEPARATORS[k] + "\'.");
                    return -1;
                }
            }
        }
        String replacement = "";
        if (doReplace) {
            try {
                replacement = m_docReplace.getText(0, m_docReplace.getLength());
                replacement = replacement.replaceAll("\\\\\\\\n", "\f").replaceAll("\\\\n", "\n").replaceAll("\\f", "\\\\n");
                replacement = replacement.replaceAll("\\\\\\\\t", "\f").replaceAll("\\\\t", "\t").replaceAll("\\f", "\\\\t");
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        }
        int xStart = -1;
        int xFinish = -1;
        while (true) {
            if (m_searchUp) {
                xStart = m_searchData.lastIndexOf(key, pos - 1);
            } else {
                xStart = m_searchData.indexOf(key, pos);
            }
            if (xStart < 0) {
                if (m_owner.getSelection() != null && doReplace) {
                    xStart = monitor.getSelectionStart();
                    monitor.replaceSelection(replacement);
                    m_owner.setSelection(xStart, xStart + replacement.length(), m_searchUp);
                }
                if (showWarnings) {
                    int n = JOptionPane.showConfirmDialog(this, myResources.getString("Cannot_find_\"") + m_txtFind2.getText() + "\".\n" + myResources.getString("Continue_search_from_") + (m_searchUp ? myResources.getString("end?") : myResources.getString("beginning?")), "VietPad", JOptionPane.YES_NO_OPTION);
                    if (n == JOptionPane.YES_OPTION) {
                        m_searchIndex = -1;
                        if (m_searchUp) {
                            if (doc.getLength() != 0) {
                                monitor.setCaretPosition(doc.getLength() - 1);
                            }
                        } else {
                            monitor.setCaretPosition(0);
                        }
                        findNext(doReplace, showWarnings);
                    } else if (n == JOptionPane.NO_OPTION) {
                        requestFocusInWindow();
                    }
                }
                return 0;
            }
            xFinish = xStart + key.length();
            if (m_modelWord.isSelected()) {
                boolean s1 = xStart > 0;
                boolean b1 = s1 && !isSeparator(m_searchData.charAt(xStart - 1));
                boolean s2 = xFinish < m_searchData.length();
                boolean b2 = s2 && !isSeparator(m_searchData.charAt(xFinish));
                if (b1 || b2) {
                    if (m_searchUp && s1) {
                        pos = xStart;
                        continue;
                    }
                    if (!m_searchUp && s2) {
                        pos = xFinish + 1;
                        continue;
                    }
                    if (showWarnings) {
                        warning(myResources.getString("Cannot_find_\"") + m_txtFind2.getText() + "\".");
                    }
                    requestFocusInWindow();
                    return 0;
                }
            }
            break;
        }
        if (!m_searchUp) {
            xStart += m_searchIndex;
            xFinish += m_searchIndex;
        }
        if (doReplace) {
            if (m_owner.getSelection() == null) {
                findNext(false, true);
                return 1;
            }
            monitor.replaceSelection(replacement);
            if (!m_searchUp) {
                xStart += replacement.length() - key.length();
            }
            m_owner.setSelection(xStart, xStart + key.length(), m_searchUp);
        } else {
            m_owner.setSelection(xStart, xFinish, m_searchUp);
        }
        m_searchIndex = -1;
        return 1;
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
     *  Non-alphanumeric characters that separate words
     */
    public final char[] WORD_SEPARATORS = { ' ', '\t', '\n', '\r', '\f', '.', ',', ':', '-', '(', ')', '[', ']', '{', '}', '<', '>', '/', '|', '\\', '\'', '\"' };

    /**
     *  Determine whether a character is a word separator
     *
     *@param  ch        A character
     *@return
     */
    public boolean isSeparator(char ch) {
        for (int k = 0; k < WORD_SEPARATORS.length; k++) {
            if (ch == WORD_SEPARATORS[k]) {
                return true;
            }
        }
        return false;
    }
}
