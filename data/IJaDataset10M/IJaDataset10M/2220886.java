package csa.jportal.ai.test;

import csa.gui.CSATableModel;
import csa.gui.CSATablePanel;
import csa.gui.DoubleClickAction;
import csa.jportal.JPortalView;
import csa.jportal.ai.namedai.NamedAIData;
import csa.jportal.ai.namedai.NamedAIDataPool;
import csa.jportal.card.CardList;
import csa.jportal.carddeck.CardDeck;
import csa.jportal.carddeck.CardDeckListEditPanel;
import csa.jportal.config.Configuration;
import csa.jportal.gui.GetSelectionDialog;
import csa.jportal.gui.JPortalInternalFrame;
import csa.jportal.gui.QuickHelpModal;
import csa.jportal.gui.Windowable;
import csa.jportal.match.Match;
import csa.jportal.match.MatchListener;
import csa.jportal.match.MatchStartOptions;
import csa.jportal.match.TestMatch;
import csa.jportal.match.display.DefaultMatchDisplay;
import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListModel;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author malban
 */
public class AITestPanel extends javax.swing.JPanel implements Windowable, Runnable {

    private AITestRunData mAITestRunData = new AITestRunData();

    private AITestRunDataPool mAITestRunDataPool;

    private AITestData mAITestData = new AITestData();

    private AITestDataPool mAITestDataPool;

    private CSATablePanel csaPanel = null;

    private int mClassSetting = 0;

    private boolean targetError = true;

    boolean mBreak = false;

    Thread testerThread = null;

    Vector<AITestData> mTestResult = new Vector<AITestData>();

    private JPortalView mParent = null;

    private javax.swing.JMenuItem mParentMenuItem = null;

    public void closing() {
    }

    public void setParentWindow(JPortalView jpv) {
        mParent = jpv;
    }

    public void setMenuItem(javax.swing.JMenuItem item) {
        mParentMenuItem = item;
        mParentMenuItem.setText("AI Tester");
    }

    public javax.swing.JMenuItem getMenuItem() {
        return mParentMenuItem;
    }

    public javax.swing.JPanel getPanel() {
        return this;
    }

    private void resetCombo() {
        mAITestDataPool = new AITestDataPool();
        mAITestRunDataPool = new AITestRunDataPool();
        File path = new File(csa.Global.mBaseDir + File.separator + "testmatches" + File.separator);
        File files[];
        files = path.listFiles();
        if (files == null) return;
        Vector<String> list = new Vector<String>();
        Arrays.sort(files);
        for (int i = 0, n = files.length; i < n; i++) {
            String aName = files[i].toString();
            if (!files[i].isDirectory()) {
                if (aName.indexOf("Deck.xml") != -1) {
                    aName = csa.util.UtilityString.replace(aName, "Deck.xml", "");
                    int nameStart = aName.lastIndexOf(File.separator);
                    aName = aName.substring(nameStart + 1);
                    list.addElement(aName);
                }
            }
        }
        Collections.sort(list, new Comparator<String>() {

            public final int compare(String s1, String s2) {
                return (s1.compareTo(s2));
            }
        });
        jComboBoxSourceState.removeAllItems();
        jComboBoxTargetState.removeAllItems();
        for (int i = 0; i < list.size(); i++) {
            String string = list.elementAt(i);
            jComboBoxSourceState.addItem(string);
            jComboBoxTargetState.addItem(string);
        }
    }

    /** Creates new form AITest */
    int mRow = 0;

    int mCol = 0;

    public AITestPanel() {
        initComponents();
        jButtonUnpause.setVisible(false);
        resetCombo();
        resetConfigPool(false, "");
        resetConfigPoolTest(false, "");
        setAllTestRunsToCollection("");
        NamedAIDataPool mNamedAIDataPool = new NamedAIDataPool();
        Vector<String> ais = new Vector<String>();
        Collection<NamedAIData> colNAI = mNamedAIDataPool.getHashMap().values();
        Iterator<NamedAIData> iterNAI = colNAI.iterator();
        while (iterNAI.hasNext()) {
            NamedAIData item = iterNAI.next();
            ais.addElement(item.mName);
        }
        jComboBoxAITest.removeAllItems();
        jComboBoxAIFixed.removeAllItems();
        for (int i = 0; i < ais.size(); i++) {
            String string = ais.elementAt(i);
            jComboBoxAITest.addItem(string);
            jComboBoxAIFixed.addItem(string);
        }
        jLabelRunning.setVisible(false);
        mTestResult = new Vector<AITestData>();
        CSATableModel t = CSATableModel.buildTableModel(getTableModel());
        csaPanel = new CSATablePanel(t);
        csaPanel.setDoubleClickAction(new DoubleClickAction() {

            public void doIt() {
                JTable table = (JTable) evt.getSource();
                mRow = table.convertRowIndexToModel(table.rowAtPoint(evt.getPoint()));
                mCol = table.convertColumnIndexToModel(table.columnAtPoint(evt.getPoint()));
                mCol = csaPanel.getModel().convertEnabledColToRealCol(mCol);
                tableDoubleClicked();
            }
        });
        csaPanel.setXMLId("TestRunnerTable");
        csaPanel.setTableStyleSwitchingEnabled(false);
        csaPanel.setTablePopupEnabled(true);
        jPanelTablepanel.removeAll();
        javax.swing.GroupLayout jPanelTablepanelLayout = new javax.swing.GroupLayout(jPanelTablepanel);
        jPanelTablepanel.setLayout(jPanelTablepanelLayout);
        jPanelTablepanelLayout.setHorizontalGroup(jPanelTablepanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(csaPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 691, Short.MAX_VALUE));
        jPanelTablepanelLayout.setVerticalGroup(jPanelTablepanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(csaPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 425, Short.MAX_VALUE));
        jComboBoxKlasse.getItemCount();
        if (jComboBoxKlasse.getItemCount() > 0) jComboBoxKlasse.setSelectedIndex(0);
    }

    private void resetConfigPoolTest(boolean select, String klasseToSet) {
        mClassSetting++;
        Collection<String> collectionKlasse = mAITestDataPool.getKlassenHashMap().values();
        Iterator<String> iterKlasse = collectionKlasse.iterator();
        int i = 0;
        String klasse = "";
        Collection<AITestData> colC = mAITestDataPool.getMapForKlasse(klasse).values();
        Iterator<AITestData> iterC = colC.iterator();
        jComboBoxNameTest.removeAllItems();
        i = 0;
        while (iterC.hasNext()) {
            AITestData item = iterC.next();
            jComboBoxNameTest.addItem(item.mName);
            if ((i == 0) && (select)) {
                jComboBoxNameTest.setSelectedIndex(0);
                mAITestData = mAITestDataPool.get(item.mName);
                setAllFromCurrentTest();
            }
            i++;
        }
        if (!select) jComboBoxNameTest.setSelectedIndex(-1);
        mClassSetting--;
    }

    private void clearAllTest() {
        mClassSetting++;
        mAITestData = new AITestData();
        setAllFromCurrentTest();
        mClassSetting--;
    }

    private void setAllFromCurrentTest() {
        mClassSetting++;
        jComboBoxNameTest.setSelectedItem(mAITestData.mName);
        jTextFieldNameTest.setText(mAITestData.mName);
        jTextFieldP0Health.setText("" + mAITestData.mTarget0Health);
        jTextFieldP1Health.setText("" + mAITestData.mTarget1Health);
        jTextFieldP0HealthStart.setText("" + mAITestData.mPlayer1StartHealth);
        jTextFieldP1HealthStart.setText("" + mAITestData.mPlayer2StartHealth);
        jCheckBoxSwitchPlayer.setSelected(mAITestData.mSourceSwitch);
        jCheckBoxSwitchPlayerTarget.setSelected(mAITestData.mTargetSwitch);
        jComboBoxSourceState.setSelectedItem(mAITestData.mSourceState);
        jComboBoxTargetState.setSelectedItem(mAITestData.mTargetState);
        jTextAreaComment.setText(mAITestData.mComment);
        jTextField4.setText("" + mAITestData.mAfterRounds);
        jComboBoxAIFixed.setSelectedItem(mAITestData.mFixedAIName);
        jComboBox3.setSelectedIndex(mAITestData.mFixedAINumber);
        mClassSetting--;
    }

    private void readAllToCurrentTest() {
        mAITestData.mClass = "";
        mAITestData.mName = jTextFieldNameTest.getText();
        mAITestData.mTarget0Health = csa.util.UtilityString.IntX(jTextFieldP0Health.getText(), 20);
        mAITestData.mTarget1Health = csa.util.UtilityString.IntX(jTextFieldP1Health.getText(), 20);
        mAITestData.mAfterRounds = csa.util.UtilityString.IntX(jTextField4.getText(), 1);
        mAITestData.mPlayer1StartHealth = csa.util.UtilityString.IntX(jTextFieldP0HealthStart.getText(), 20);
        mAITestData.mPlayer2StartHealth = csa.util.UtilityString.IntX(jTextFieldP1HealthStart.getText(), 20);
        mAITestData.mSourceSwitch = jCheckBoxSwitchPlayer.isSelected();
        mAITestData.mTargetSwitch = jCheckBoxSwitchPlayerTarget.isSelected();
        mAITestData.mComment = jTextAreaComment.getText();
        if (jComboBoxAIFixed.getSelectedIndex() == -1) {
            mAITestData.mFixedAIName = "";
        } else {
            mAITestData.mFixedAIName = jComboBoxAIFixed.getSelectedItem().toString();
        }
        mAITestData.mFixedAINumber = jComboBox3.getSelectedIndex();
        if (jComboBoxSourceState.getSelectedIndex() == -1) {
            mAITestData.mSourceState = "";
        } else {
            mAITestData.mSourceState = jComboBoxSourceState.getSelectedItem().toString();
        }
        if (jComboBoxTargetState.getSelectedIndex() == -1) {
            mAITestData.mTargetState = "";
        } else {
            mAITestData.mTargetState = jComboBoxTargetState.getSelectedItem().toString();
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jComboBoxAITest = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jTextFieldName = new javax.swing.JTextField();
        jTextFieldKlasse = new javax.swing.JTextField();
        jComboBoxKlasse = new javax.swing.JComboBox();
        jComboBoxName = new javax.swing.JComboBox();
        jButtonNew = new javax.swing.JButton();
        jButtonSave = new javax.swing.JButton();
        jButtonSaveAsNew = new javax.swing.JButton();
        jButtonDelete = new javax.swing.JButton();
        jLabelRunning = new javax.swing.JLabel();
        jCheckBoxDebug = new javax.swing.JCheckBox();
        jButtonUnpause = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel9 = new javax.swing.JPanel();
        jPanelTablepanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jPanel3 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jListDeckAll = new javax.swing.JList();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jListDecksKnown = new javax.swing.JList();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jComboBoxSourceState = new javax.swing.JComboBox();
        jCheckBoxSwitchPlayer = new javax.swing.JCheckBox();
        jButton3 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jComboBoxTargetState = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldP0Health = new javax.swing.JTextField();
        jTextFieldP1Health = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jButton11 = new javax.swing.JButton();
        jTextFieldP1HealthStart = new javax.swing.JTextField();
        jTextFieldP0HealthStart = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jCheckBoxSwitchPlayerTarget = new javax.swing.JCheckBox();
        jPanel10 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jTextFieldNameTest = new javax.swing.JTextField();
        jComboBoxNameTest = new javax.swing.JComboBox();
        jButtonSaveTest = new javax.swing.JButton();
        jButtonNewTest = new javax.swing.JButton();
        jButtonDeleteTest = new javax.swing.JButton();
        jButtonSaveAsNewTest = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jComboBoxAIFixed = new javax.swing.JComboBox();
        jLabel19 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextAreaComment = new javax.swing.JTextArea();
        jPanel4 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel12 = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();
        jTextField2 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jButton8 = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        jLabel17 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jButton9 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Test Runs"));
        jPanel1.setName("jPanel1");
        jLabel5.setText("Test AI:");
        jLabel5.setName("jLabel5");
        jComboBoxAITest.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxAITest.setName("jComboBoxAITest");
        jButton1.setText("Run test series");
        jButton1.setName("jButton1");
        jButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jButton2.setText("?");
        jButton2.setName("jButton2");
        jButton2.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jLabel9.setText("Class");
        jLabel9.setName("jLabel9");
        jLabel10.setText("Name");
        jLabel10.setName("jLabel10");
        jTextFieldName.setName("jTextFieldName");
        jTextFieldKlasse.setName("jTextFieldKlasse");
        jComboBoxKlasse.setName("jComboBoxKlasse");
        jComboBoxKlasse.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxKlasseActionPerformed(evt);
            }
        });
        jComboBoxName.setName("jComboBoxName");
        jComboBoxName.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxNameActionPerformed(evt);
            }
        });
        jButtonNew.setText("New");
        jButtonNew.setName("jButtonNew");
        jButtonNew.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNewActionPerformed(evt);
            }
        });
        jButtonSave.setText("Save");
        jButtonSave.setName("jButtonSave");
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });
        jButtonSaveAsNew.setText("Save as new");
        jButtonSaveAsNew.setName("jButtonSaveAsNew");
        jButtonSaveAsNew.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveAsNewActionPerformed(evt);
            }
        });
        jButtonDelete.setText("Delete");
        jButtonDelete.setName("jButtonDelete");
        jButtonDelete.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteActionPerformed(evt);
            }
        });
        jLabelRunning.setFont(jLabelRunning.getFont().deriveFont(jLabelRunning.getFont().getStyle() | java.awt.Font.BOLD));
        jLabelRunning.setForeground(new java.awt.Color(0, 204, 0));
        jLabelRunning.setText("Running!");
        jLabelRunning.setName("jLabelRunning");
        jCheckBoxDebug.setText("Open eai - debug on");
        jCheckBoxDebug.setName("jCheckBoxDebug");
        jButtonUnpause.setText("Un-Pause");
        jButtonUnpause.setName("jButtonUnpause");
        jButtonUnpause.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonUnpauseActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel9).addComponent(jLabel10)).addGap(16, 16, 16).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jTextFieldName, javax.swing.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE).addComponent(jTextFieldKlasse, javax.swing.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jComboBoxKlasse, 0, 315, Short.MAX_VALUE).addComponent(jComboBoxName, 0, 315, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jButtonSave).addComponent(jButtonNew)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jButtonSaveAsNew).addComponent(jButtonDelete)).addContainerGap()).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addComponent(jLabel5).addGap(18, 18, 18).addComponent(jComboBoxAITest, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 400, Short.MAX_VALUE).addComponent(jButtonUnpause).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)).addGroup(jPanel1Layout.createSequentialGroup().addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jCheckBoxDebug).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabelRunning).addComponent(jButton1))))));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addComponent(jButtonDelete).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jButtonSave).addComponent(jButtonSaveAsNew))).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jComboBoxKlasse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jButtonNew).addComponent(jTextFieldKlasse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel9)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jComboBoxName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jTextFieldName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel10)))).addGap(18, 18, 18).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGap(0, 6, Short.MAX_VALUE).addComponent(jLabelRunning).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jButton1).addComponent(jButton2).addComponent(jButtonUnpause))).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jComboBoxAITest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel5).addComponent(jCheckBoxDebug))).addContainerGap()));
        jTabbedPane1.setName("jTabbedPane1");
        jPanel9.setName("jPanel9");
        jPanelTablepanel.setName("jPanelTablepanel");
        jScrollPane1.setName("jScrollPane1");
        jTable1.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { null, null, null, null }, { null, null, null, null }, { null, null, null, null }, { null, null, null, null } }, new String[] { "Title 1", "Title 2", "Title 3", "Title 4" }));
        jTable1.setName("jTable1");
        jScrollPane1.setViewportView(jTable1);
        javax.swing.GroupLayout jPanelTablepanelLayout = new javax.swing.GroupLayout(jPanelTablepanel);
        jPanelTablepanel.setLayout(jPanelTablepanelLayout);
        jPanelTablepanelLayout.setHorizontalGroup(jPanelTablepanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 847, Short.MAX_VALUE));
        jPanelTablepanelLayout.setVerticalGroup(jPanelTablepanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE));
        jScrollPane3.setName("jScrollPane3");
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setName("jTextArea1");
        jScrollPane3.setViewportView(jTextArea1);
        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanelTablepanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 847, Short.MAX_VALUE));
        jPanel9Layout.setVerticalGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel9Layout.createSequentialGroup().addComponent(jPanelTablepanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jTabbedPane1.addTab("Run results", jPanel9);
        jPanel3.setName("jPanel3");
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("AllTests"));
        jPanel2.setName("jPanel2");
        jScrollPane4.setName("jScrollPane4");
        jListDeckAll.setName("jListDeckAll");
        jListDeckAll.setVisibleRowCount(5);
        jScrollPane4.setViewportView(jListDeckAll);
        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 234, Short.MAX_VALUE).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 405, Short.MAX_VALUE).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE)));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Selected Test"));
        jPanel5.setName("jPanel5");
        jScrollPane5.setName("jScrollPane5");
        jListDecksKnown.setName("jListDecksKnown");
        jListDecksKnown.setVisibleRowCount(5);
        jScrollPane5.setViewportView(jListDecksKnown);
        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE));
        jPanel5Layout.setVerticalGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE));
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/csa/jportal/resources/right.png")));
        jButton5.setIconTextGap(0);
        jButton5.setMargin(new java.awt.Insets(2, 1, 1, 2));
        jButton5.setName("jButton5");
        jButton5.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/csa/jportal/resources/left.png")));
        jButton6.setMargin(new java.awt.Insets(1, 1, 2, 2));
        jButton6.setName("jButton6");
        jButton6.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addContainerGap().addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton6).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton5).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGap(287, 287, 287)));
        jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addContainerGap().addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jButton5).addComponent(jButton6).addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))));
        jTabbedPane1.addTab("Run definition", jPanel3);
        jPanel6.setName("jPanel6");
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Source"));
        jPanel7.setName("jPanel7");
        jLabel1.setText("Gamefile");
        jLabel1.setName("jLabel1");
        jComboBoxSourceState.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxSourceState.setName("jComboBoxSourceState");
        jCheckBoxSwitchPlayer.setText("Switch players");
        jCheckBoxSwitchPlayer.setName("jCheckBoxSwitchPlayer");
        jButton3.setText("Edit state");
        jButton3.setName("jButton3");
        jButton3.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jButton10.setText("Show");
        jButton10.setName("jButton10");
        jButton10.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel7Layout.createSequentialGroup().addContainerGap().addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel7Layout.createSequentialGroup().addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel7Layout.createSequentialGroup().addComponent(jCheckBoxSwitchPlayer).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 132, Short.MAX_VALUE).addComponent(jButton3)).addComponent(jComboBoxSourceState, 0, 306, Short.MAX_VALUE))).addComponent(jButton10, javax.swing.GroupLayout.Alignment.TRAILING)).addContainerGap()));
        jPanel7Layout.setVerticalGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel7Layout.createSequentialGroup().addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jComboBoxSourceState, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel1)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jCheckBoxSwitchPlayer).addComponent(jButton3)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton10).addContainerGap(21, Short.MAX_VALUE)));
        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Result"));
        jPanel8.setName("jPanel8");
        jLabel2.setText("Gamefile");
        jLabel2.setName("jLabel2");
        jComboBoxTargetState.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxTargetState.setName("jComboBoxTargetState");
        jLabel3.setText("Dif 0 health:");
        jLabel3.setName("jLabel3");
        jLabel4.setText("Dif 1 health:");
        jLabel4.setName("jLabel4");
        jTextFieldP0Health.setText("0");
        jTextFieldP0Health.setName("jTextFieldP0Health");
        jTextFieldP1Health.setText("0");
        jTextFieldP1Health.setName("jTextFieldP1Health");
        jButton4.setText("Edit state");
        jButton4.setName("jButton4");
        jButton4.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jLabel18.setText("After # rounds:");
        jLabel18.setName("jLabel18");
        jTextField4.setText("1");
        jTextField4.setName("jTextField4");
        jButton11.setText("Show");
        jButton11.setName("jButton11");
        jButton11.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });
        jTextFieldP1HealthStart.setText("0");
        jTextFieldP1HealthStart.setName("jTextFieldP1HealthStart");
        jTextFieldP0HealthStart.setText("0");
        jTextFieldP0HealthStart.setName("jTextFieldP0HealthStart");
        jLabel20.setText("start 0 health:");
        jLabel20.setName("jLabel20");
        jLabel21.setText("start 1 health:");
        jLabel21.setName("jLabel21");
        jCheckBoxSwitchPlayerTarget.setText("Switch players");
        jCheckBoxSwitchPlayerTarget.setName("jCheckBoxSwitchPlayerTarget");
        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel8Layout.createSequentialGroup().addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel8Layout.createSequentialGroup().addContainerGap().addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel8Layout.createSequentialGroup().addComponent(jLabel2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jComboBoxTargetState, 0, 306, Short.MAX_VALUE)).addGroup(jPanel8Layout.createSequentialGroup().addComponent(jLabel18).addGap(0, 275, Short.MAX_VALUE)).addGroup(jPanel8Layout.createSequentialGroup().addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel8Layout.createSequentialGroup().addComponent(jLabel3).addGap(18, 18, 18).addComponent(jTextFieldP0Health, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)).addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel8Layout.createSequentialGroup().addComponent(jLabel4).addGap(18, 18, 18).addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(jTextFieldP1Health, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE).addComponent(jTextField4)))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel8Layout.createSequentialGroup().addComponent(jLabel20).addGap(18, 18, 18).addComponent(jTextFieldP0HealthStart, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)).addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel8Layout.createSequentialGroup().addComponent(jLabel21).addGap(18, 18, 18).addComponent(jTextFieldP1HealthStart, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jButton11, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jButton4, javax.swing.GroupLayout.Alignment.TRAILING))))).addGroup(jPanel8Layout.createSequentialGroup().addGap(62, 62, 62).addComponent(jCheckBoxSwitchPlayerTarget))).addContainerGap()));
        jPanel8Layout.setVerticalGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel8Layout.createSequentialGroup().addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel2).addComponent(jComboBoxTargetState, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel8Layout.createSequentialGroup().addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel3).addComponent(jTextFieldP0Health, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jButton4)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel4).addComponent(jTextFieldP1Health, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jButton11))).addGroup(jPanel8Layout.createSequentialGroup().addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel20).addComponent(jTextFieldP0HealthStart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel21).addComponent(jTextFieldP1HealthStart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel18).addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jCheckBoxSwitchPlayerTarget).addContainerGap(13, Short.MAX_VALUE)));
        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder("Run"));
        jPanel10.setName("jPanel10");
        jLabel8.setText("Name");
        jLabel8.setName("jLabel8");
        jTextFieldNameTest.setName("jTextFieldNameTest");
        jComboBoxNameTest.setName("jComboBoxNameTest");
        jComboBoxNameTest.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxNameTestActionPerformed(evt);
            }
        });
        jButtonSaveTest.setText("Save");
        jButtonSaveTest.setName("jButtonSaveTest");
        jButtonSaveTest.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveTestActionPerformed(evt);
            }
        });
        jButtonNewTest.setText("New");
        jButtonNewTest.setName("jButtonNewTest");
        jButtonNewTest.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNewTestActionPerformed(evt);
            }
        });
        jButtonDeleteTest.setText("Delete");
        jButtonDeleteTest.setName("jButtonDeleteTest");
        jButtonDeleteTest.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteTestActionPerformed(evt);
            }
        });
        jButtonSaveAsNewTest.setText("Save as new");
        jButtonSaveAsNewTest.setName("jButtonSaveAsNewTest");
        jButtonSaveAsNewTest.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveAsNewTestActionPerformed(evt);
            }
        });
        jLabel6.setText("Fixed AI:");
        jLabel6.setName("jLabel6");
        jComboBoxAIFixed.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxAIFixed.setName("jComboBoxAIFixed");
        jLabel19.setText("for player");
        jLabel19.setName("jLabel19");
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2" }));
        jComboBox3.setName("jComboBox3");
        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup().addContainerGap().addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel6).addComponent(jLabel8)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(jTextFieldNameTest, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(jPanel10Layout.createSequentialGroup().addComponent(jComboBoxAIFixed, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jLabel19))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel10Layout.createSequentialGroup().addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 307, Short.MAX_VALUE).addComponent(jButtonSaveTest)).addGroup(jPanel10Layout.createSequentialGroup().addComponent(jComboBoxNameTest, 0, 336, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButtonNewTest))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jButtonSaveAsNewTest).addComponent(jButtonDeleteTest)).addContainerGap()));
        jPanel10Layout.setVerticalGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel10Layout.createSequentialGroup().addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jButtonDeleteTest).addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jComboBoxNameTest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel8).addComponent(jTextFieldNameTest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(jButtonNewTest))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jButtonSaveTest).addComponent(jButtonSaveAsNewTest)).addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel6).addComponent(jComboBoxAIFixed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel19).addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))));
        jLabel7.setText("Comment");
        jLabel7.setName("jLabel7");
        jScrollPane2.setName("jScrollPane2");
        jTextAreaComment.setColumns(20);
        jTextAreaComment.setLineWrap(true);
        jTextAreaComment.setRows(5);
        jTextAreaComment.setTabSize(2);
        jTextAreaComment.setWrapStyleWord(true);
        jTextAreaComment.setName("jTextAreaComment");
        jScrollPane2.setViewportView(jTextAreaComment);
        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel6Layout.createSequentialGroup().addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(jPanel6Layout.createSequentialGroup().addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(jPanel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jPanel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel6Layout.createSequentialGroup().addComponent(jLabel7).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 413, Short.MAX_VALUE)).addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 458, Short.MAX_VALUE)))).addContainerGap()));
        jPanel6Layout.setVerticalGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel6Layout.createSequentialGroup().addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addGroup(jPanel6Layout.createSequentialGroup().addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGap(41, 41, 41)).addGroup(jPanel6Layout.createSequentialGroup().addGap(17, 17, 17).addComponent(jLabel7).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 316, Short.MAX_VALUE))).addContainerGap()));
        jTabbedPane1.addTab("Test definition", jPanel6);
        jPanel4.setName("jPanel4");
        jLabel11.setText("Base directory:");
        jLabel11.setName("jLabel11");
        jTextField1.setText("debugmatches");
        jTextField1.setName("ss");
        jComboBox1.setName("jComboBox1");
        jLabel12.setText("Saved games:");
        jLabel12.setName("jLabel12");
        jButton7.setText("Apply");
        jButton7.setName("jButton7");
        jButton7.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jTextField2.setText("testmatches");
        jTextField2.setName("jTextField2");
        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel13.setText("Source");
        jLabel13.setName("jLabel13");
        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel14.setText("Target");
        jLabel14.setName("jLabel14");
        jLabel15.setText("Base directory:");
        jLabel15.setName("jLabel15");
        jButton8.setText("Apply");
        jButton8.setName("jButton8");
        jButton8.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jLabel16.setText("Saved games:");
        jLabel16.setName("jLabel16");
        jComboBox2.setName("jComboBox2");
        jLabel17.setText("Save source as:");
        jLabel17.setName("jLabel17");
        jTextField3.setName("jTextField3");
        jButton9.setText("Save");
        jButton9.setName("jButton9");
        jButton9.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        jButton12.setText("Show");
        jButton12.setName("jButton12");
        jButton12.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel4Layout.createSequentialGroup().addContainerGap().addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(jLabel13).addComponent(jLabel14).addGroup(jPanel4Layout.createSequentialGroup().addGap(10, 10, 10).addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup().addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel11).addComponent(jLabel12)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(jComboBox1, 0, 210, Short.MAX_VALUE).addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE))).addGroup(jPanel4Layout.createSequentialGroup().addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel15).addComponent(jLabel16).addComponent(jLabel17)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jTextField2).addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jButton9).addComponent(jButton7).addComponent(jButton8).addComponent(jButton12)))).addGap(394, 394, 394)));
        jPanel4Layout.setVerticalGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel4Layout.createSequentialGroup().addContainerGap().addComponent(jLabel13).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel11).addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jButton8)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel12).addComponent(jButton12)).addGap(31, 31, 31).addComponent(jLabel14).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel15).addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jButton7)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel16)).addGap(35, 35, 35).addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel17).addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jButton9)).addContainerGap(197, Short.MAX_VALUE)));
        jTabbedPane1.addTab("Import game states", jPanel4);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 852, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 471, Short.MAX_VALUE)));
    }

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {
        if (jComboBoxSourceState.getSelectedIndex() == -1) return;
        CardDeckListEditPanel cx = new CardDeckListEditPanel(jComboBoxSourceState.getSelectedItem().toString(), "testmatches" + java.io.File.separator);
        mParent.addPanel(cx);
        mParent.setMainPanel(cx);
        mParent.windowMe(cx, 1000, 700, "Edit test source state.");
    }

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {
        if (jComboBoxTargetState.getSelectedIndex() == -1) return;
        CardDeckListEditPanel cx = new CardDeckListEditPanel(jComboBoxTargetState.getSelectedItem().toString(), "testmatches" + java.io.File.separator);
        mParent.addPanel(cx);
        mParent.setMainPanel(cx);
        mParent.windowMe(cx, 1000, 700, "Edit test target state.");
    }

    private void jButtonSaveAsNewTestActionPerformed(java.awt.event.ActionEvent evt) {
        mAITestData = new AITestData();
        readAllToCurrentTest();
        mAITestDataPool.putAsNew(mAITestData);
        mAITestDataPool.save();
        mClassSetting++;
        String klasse = "";
        String oldName = mAITestData.mName;
        resetConfigPoolTest(true, klasse);
        jComboBoxNameTest.setSelectedItem(mAITestData.mName);
        reorgCollection();
        mClassSetting--;
        jComboBoxNameTest.setSelectedItem(oldName);
    }

    private void jButtonDeleteTestActionPerformed(java.awt.event.ActionEvent evt) {
        readAllToCurrentTest();
        mAITestDataPool.remove(mAITestData);
        mAITestDataPool.save();
        mClassSetting++;
        String klasse = "";
        resetConfigPoolTest(true, klasse);
        if (jComboBoxNameTest.getSelectedIndex() == -1) {
            clearAllTest();
        }
        if (jComboBoxNameTest.getSelectedIndex() != -1) {
            String key = jComboBoxNameTest.getSelectedItem().toString();
            mAITestData = mAITestDataPool.get(key);
        } else {
            mAITestData = new AITestData();
        }
        setAllFromCurrentTest();
        reorgCollection();
        mClassSetting--;
    }

    private void jButtonSaveTestActionPerformed(java.awt.event.ActionEvent evt) {
        readAllToCurrentTest();
        mAITestDataPool.put(mAITestData);
        mAITestDataPool.save();
        mClassSetting++;
        String klasse = "";
        String oldName = mAITestData.mName;
        resetConfigPoolTest(true, klasse);
        reorgCollection();
        mClassSetting--;
        jComboBoxNameTest.setSelectedItem(oldName);
    }

    private void jButtonNewTestActionPerformed(java.awt.event.ActionEvent evt) {
        mClassSetting++;
        mAITestData = new AITestData();
        clearAllTest();
        resetConfigPoolTest(false, "");
        mClassSetting--;
    }

    private void jComboBoxNameTestActionPerformed(java.awt.event.ActionEvent evt) {
        if (mClassSetting > 0) return;
        String key = jComboBoxNameTest.getSelectedItem().toString();
        mAITestData = mAITestDataPool.get(key);
        setAllFromCurrentTest();
    }

    private void jComboBoxKlasseActionPerformed(java.awt.event.ActionEvent evt) {
        if (mClassSetting > 0) return;
        mClassSetting++;
        ;
        String selected = jComboBoxKlasse.getSelectedItem().toString();
        clearAll();
        resetConfigPool(true, selected);
        jTextFieldKlasse.setText(jComboBoxKlasse.getSelectedItem().toString());
        String key = jComboBoxName.getSelectedItem().toString();
        mAITestRunData = mAITestRunDataPool.get(key);
        setAllFromCurrent();
        mClassSetting--;
    }

    private void jComboBoxNameActionPerformed(java.awt.event.ActionEvent evt) {
        if (mClassSetting > 0) return;
        String key = jComboBoxName.getSelectedItem().toString();
        mAITestRunData = mAITestRunDataPool.get(key);
        setAllFromCurrent();
    }

    private void jButtonNewActionPerformed(java.awt.event.ActionEvent evt) {
        mClassSetting++;
        mAITestRunData = new AITestRunData();
        clearAll();
        resetConfigPool(false, "");
        mClassSetting--;
    }

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {
        readAllToCurrent();
        mAITestRunDataPool.put(mAITestRunData);
        mAITestRunDataPool.save();
        mClassSetting++;
        String klasse = jTextFieldKlasse.getText();
        resetConfigPool(true, klasse);
        jComboBoxName.setSelectedItem(mAITestRunData.mName);
        mClassSetting--;
    }

    private void jButtonSaveAsNewActionPerformed(java.awt.event.ActionEvent evt) {
        mAITestRunData = new AITestRunData();
        readAllToCurrent();
        mAITestRunDataPool.putAsNew(mAITestRunData);
        mAITestRunDataPool.save();
        mClassSetting++;
        String klasse = jTextFieldKlasse.getText();
        resetConfigPool(true, klasse);
        jComboBoxName.setSelectedItem(mAITestRunData.mName);
        mClassSetting--;
    }

    private void jButtonDeleteActionPerformed(java.awt.event.ActionEvent evt) {
        readAllToCurrent();
        mAITestRunDataPool.remove(mAITestRunData);
        mAITestRunDataPool.save();
        mClassSetting++;
        String klasse = jTextFieldKlasse.getText();
        resetConfigPool(true, klasse);
        if (jComboBoxName.getSelectedIndex() == -1) {
            clearAll();
        } else {
            String key = jComboBoxName.getSelectedItem().toString();
            mAITestRunData = mAITestRunDataPool.get(key);
            setAllFromCurrent();
        }
        mClassSetting--;
    }

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {
        moveListSelection(jListDeckAll, jListDecksKnown);
    }

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {
        moveListSelection(jListDecksKnown, jListDeckAll);
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        String help = "";
        help += "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\"> ";
        help += "<html> <head>   ";
        help += "<title></title> </head>";
        help += "<body> <h3>Test Run - Quick-Help</h3> Here you can execute Tests. Tests are generally speaking ";
        help += "two saved game states, which are compared.";
        help += " First state is the base, than one game round is executed, ";
        help += "and the result of one game round (all phases from 1-10 executed) is compared";
        help += " to the second gamestate. ";
        help += "If the game after execution resembles the result game state, than the test is ";
        help += "assume to have passed, - otherwise failed.<P></p>";
        help += "Taken as input are saved gamestates found in directory: <b>testmatches</B> ";
        help += "The states are NOT saved to that directory, in order to have any states in there ";
        help += "you have to copy them manually from the directory \"debgumatches\".";
        help += "<br>";
        help += "</body>";
        help += "</html>";
        QuickHelpModal.showHelpHtml(help);
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        jTextArea1.setText("");
        Runnable caller = new Runnable() {

            public void run() {
                testerThread = new Thread(AITestPanel.this, "Test");
                testerThread.start();
            }
        };
        javax.swing.SwingUtilities.invokeLater(caller);
    }

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {
        String base = jTextField1.getText() + java.io.File.separator;
        File path = new File(csa.Global.mBaseDir + File.separator + base);
        File files[];
        files = path.listFiles();
        if (files == null) return;
        Vector<String> list = new Vector<String>();
        Arrays.sort(files);
        for (int i = 0, n = files.length; i < n; i++) {
            String aName = files[i].toString();
            if (!files[i].isDirectory()) {
                if (aName.indexOf("Deck.xml") != -1) {
                    aName = csa.util.UtilityString.replace(aName, "Deck.xml", "");
                    int nameStart = aName.lastIndexOf(File.separator);
                    aName = aName.substring(nameStart + 1);
                    list.addElement(aName);
                }
            }
        }
        Collections.sort(list, new Comparator<String>() {

            public final int compare(String s1, String s2) {
                return (s1.compareTo(s2));
            }
        });
        jComboBox1.removeAllItems();
        for (int i = 0; i < list.size(); i++) {
            String string = list.elementAt(i);
            jComboBox1.addItem(string);
        }
    }

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {
        String base = jTextField2.getText() + java.io.File.separator;
        File path = new File(csa.Global.mBaseDir + File.separator + base);
        File files[];
        files = path.listFiles();
        if (files == null) {
            targetError = true;
            return;
        }
        targetError = false;
        Vector<String> list = new Vector<String>();
        Arrays.sort(files);
        for (int i = 0, n = files.length; i < n; i++) {
            String aName = files[i].toString();
            if (!files[i].isDirectory()) {
                if (aName.indexOf("Deck.xml") != -1) {
                    aName = csa.util.UtilityString.replace(aName, "Deck.xml", "");
                    int nameStart = aName.lastIndexOf(File.separator);
                    aName = aName.substring(nameStart + 1);
                    list.addElement(aName);
                }
            }
        }
        Collections.sort(list, new Comparator<String>() {

            public final int compare(String s1, String s2) {
                return (s1.compareTo(s2));
            }
        });
        jComboBox2.removeAllItems();
        for (int i = 0; i < list.size(); i++) {
            String string = list.elementAt(i);
            jComboBox2.addItem(string);
        }
    }

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {
        if (jComboBox1.getSelectedIndex() == -1) return;
        if (jTextField3.getText().length() == 0) return;
        if (targetError) return;
        int PLAYER_COUNT = 2;
        CardList[] mDecks = new CardList[PLAYER_COUNT];
        CardList[] mLibrary = new CardList[PLAYER_COUNT];
        CardList[] mGraveyard = new CardList[PLAYER_COUNT];
        CardList[] mHand = new CardList[PLAYER_COUNT];
        CardList[] mBattlefield = new CardList[PLAYER_COUNT];
        CardList[] mLand = new CardList[PLAYER_COUNT];
        CardList[] mDiscarded = new CardList[PLAYER_COUNT];
        String className = jComboBox1.getSelectedItem().toString();
        String poolBaseName = jTextField1.getText() + java.io.File.separator;
        try {
            CardList list;
            String deckName;
            CardDeck deck;
            for (int i = 0; i < PLAYER_COUNT; i++) {
                String deckBase = "1.Player";
                if (i == 1) {
                    deckBase = "2.Player";
                }
                deckName = deckBase + " deck";
                deck = new CardDeck(deckName, className, poolBaseName);
                mDecks[i] = new CardList(deck.getCards());
                deckName = deckBase + " library";
                deck = new CardDeck(deckName, className, poolBaseName);
                mLibrary[i] = new CardList(deck.getCards());
                deckName = deckBase + " hand";
                deck = new CardDeck(deckName, className, poolBaseName);
                mHand[i] = new CardList(deck.getCards());
                deckName = deckBase + " creature";
                deck = new CardDeck(deckName, className, poolBaseName);
                mBattlefield[i] = new CardList(deck.getCards());
                deckName = deckBase + " land";
                deck = new CardDeck(deckName, className, poolBaseName);
                mLand[i] = new CardList(deck.getCards());
                deckName = deckBase + " graveyard";
                deck = new CardDeck(deckName, className, poolBaseName);
                mGraveyard[i] = new CardList(deck.getCards());
                deckName = deckBase + " discarded";
                deck = new CardDeck(deckName, className, poolBaseName);
                mDiscarded[i] = new CardList(deck.getCards());
            }
        } catch (Throwable e) {
            return;
        }
        CardList list;
        String deckName;
        poolBaseName = jTextField2.getText() + java.io.File.separator;
        ;
        className = jTextField3.getText();
        CardDeck deck;
        for (int i = 0; i < PLAYER_COUNT; i++) {
            String deckBase = "1.Player";
            if (i == 1) {
                deckBase = "2.Player";
            }
            deckName = deckBase + " deck";
            list = mDecks[i];
            deck = new CardDeck(list, deckName, className, poolBaseName);
            deck.save();
            deckName = deckBase + " library";
            list = mLibrary[i];
            deck = new CardDeck(list, deckName, className, poolBaseName);
            deck.save();
            deckName = deckBase + " graveyard";
            list = mGraveyard[i];
            deck = new CardDeck(list, deckName, className, poolBaseName);
            deck.save();
            deckName = deckBase + " hand";
            list = mHand[i];
            deck = new CardDeck(list, deckName, className, poolBaseName);
            deck.save();
            deckName = deckBase + " creature";
            list = mBattlefield[i];
            deck = new CardDeck(list, deckName, className, poolBaseName);
            deck.save();
            deckName = deckBase + " land";
            list = mLand[i];
            deck = new CardDeck(list, deckName, className, poolBaseName);
            deck.save();
            deckName = deckBase + " discarded";
            list = mDiscarded[i];
            deck = new CardDeck(list, deckName, className, poolBaseName);
            deck.save();
        }
        resetCombo();
    }

    void showOneGameState(String stateBase, String stateName) {
        final TestMatch match = new TestMatch();
        boolean oldConfig = Configuration.getConfiguration().isBackImageShown();
        Configuration.getConfiguration().setBackImageShown(false);
        match.displayAnyway = true;
        Match.loadMatchUnstated(stateName, "Default", "Default", false, mParent, match, stateBase);
        if (match != null) {
            DefaultMatchDisplay display = (DefaultMatchDisplay) match.getPlayer(0).getMatchDisplay();
            match.mGameStarted = true;
            display.setUseParentSizing(false);
            int w = 700;
            int h = 500;
            display.dontShowPlayers();
            display.arrange(w - 10, h - 60);
            display.disableButtons();
            mParent.showPanelModal(display, stateName, w, h);
            match.mGameStarted = false;
            match.deinitCompletely();
        }
        Configuration.getConfiguration().setBackImageShown(oldConfig);
    }

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {
        if (jComboBoxSourceState.getSelectedIndex() == -1) return;
        showOneGameState("testmatches" + File.separator, jComboBoxSourceState.getSelectedItem().toString());
    }

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {
        if (jComboBoxTargetState.getSelectedIndex() == -1) return;
        showOneGameState("testmatches" + File.separator, jComboBoxTargetState.getSelectedItem().toString());
    }

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {
        if (jComboBox1.getSelectedIndex() == -1) return;
        if (jTextField1.getText().length() == 0) return;
        showOneGameState(jTextField1.getText() + File.separator, jComboBox1.getSelectedItem().toString());
    }

    private void jButtonUnpauseActionPerformed(java.awt.event.ActionEvent evt) {
        if (unpauseMatch != null) unpauseMatch.setPause(false);
    }

    private javax.swing.JButton jButton1;

    private javax.swing.JButton jButton10;

    private javax.swing.JButton jButton11;

    private javax.swing.JButton jButton12;

    private javax.swing.JButton jButton2;

    private javax.swing.JButton jButton3;

    private javax.swing.JButton jButton4;

    private javax.swing.JButton jButton5;

    private javax.swing.JButton jButton6;

    private javax.swing.JButton jButton7;

    private javax.swing.JButton jButton8;

    private javax.swing.JButton jButton9;

    private javax.swing.JButton jButtonDelete;

    private javax.swing.JButton jButtonDeleteTest;

    private javax.swing.JButton jButtonNew;

    private javax.swing.JButton jButtonNewTest;

    private javax.swing.JButton jButtonSave;

    private javax.swing.JButton jButtonSaveAsNew;

    private javax.swing.JButton jButtonSaveAsNewTest;

    private javax.swing.JButton jButtonSaveTest;

    private javax.swing.JButton jButtonUnpause;

    private javax.swing.JCheckBox jCheckBoxDebug;

    private javax.swing.JCheckBox jCheckBoxSwitchPlayer;

    private javax.swing.JCheckBox jCheckBoxSwitchPlayerTarget;

    private javax.swing.JComboBox jComboBox1;

    private javax.swing.JComboBox jComboBox2;

    private javax.swing.JComboBox jComboBox3;

    private javax.swing.JComboBox jComboBoxAIFixed;

    private javax.swing.JComboBox jComboBoxAITest;

    private javax.swing.JComboBox jComboBoxKlasse;

    private javax.swing.JComboBox jComboBoxName;

    private javax.swing.JComboBox jComboBoxNameTest;

    private javax.swing.JComboBox jComboBoxSourceState;

    private javax.swing.JComboBox jComboBoxTargetState;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel10;

    private javax.swing.JLabel jLabel11;

    private javax.swing.JLabel jLabel12;

    private javax.swing.JLabel jLabel13;

    private javax.swing.JLabel jLabel14;

    private javax.swing.JLabel jLabel15;

    private javax.swing.JLabel jLabel16;

    private javax.swing.JLabel jLabel17;

    private javax.swing.JLabel jLabel18;

    private javax.swing.JLabel jLabel19;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel20;

    private javax.swing.JLabel jLabel21;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JLabel jLabel8;

    private javax.swing.JLabel jLabel9;

    private javax.swing.JLabel jLabelRunning;

    private javax.swing.JList jListDeckAll;

    private javax.swing.JList jListDecksKnown;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel10;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JPanel jPanel4;

    private javax.swing.JPanel jPanel5;

    private javax.swing.JPanel jPanel6;

    private javax.swing.JPanel jPanel7;

    private javax.swing.JPanel jPanel8;

    private javax.swing.JPanel jPanel9;

    private javax.swing.JPanel jPanelTablepanel;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JScrollPane jScrollPane2;

    private javax.swing.JScrollPane jScrollPane3;

    private javax.swing.JScrollPane jScrollPane4;

    private javax.swing.JScrollPane jScrollPane5;

    private javax.swing.JTabbedPane jTabbedPane1;

    private javax.swing.JTable jTable1;

    private javax.swing.JTextArea jTextArea1;

    private javax.swing.JTextArea jTextAreaComment;

    private javax.swing.JTextField jTextField1;

    private javax.swing.JTextField jTextField2;

    private javax.swing.JTextField jTextField3;

    private javax.swing.JTextField jTextField4;

    private javax.swing.JTextField jTextFieldKlasse;

    private javax.swing.JTextField jTextFieldName;

    private javax.swing.JTextField jTextFieldNameTest;

    private javax.swing.JTextField jTextFieldP0Health;

    private javax.swing.JTextField jTextFieldP0HealthStart;

    private javax.swing.JTextField jTextFieldP1Health;

    private javax.swing.JTextField jTextFieldP1HealthStart;

    private void resetConfigPool(boolean select, String klasseToSet) {
        mClassSetting++;
        Collection<String> collectionKlasse = mAITestRunDataPool.getKlassenHashMap().values();
        Iterator<String> iterKlasse = collectionKlasse.iterator();
        int i = 0;
        String klasse = "";
        jComboBoxKlasse.removeAllItems();
        while (iterKlasse.hasNext()) {
            String item = iterKlasse.next();
            jComboBoxKlasse.addItem(item);
            if (select) {
                if (klasseToSet.length() == 0) {
                    if (i == 0) {
                        jComboBoxKlasse.setSelectedIndex(i);
                        jTextFieldKlasse.setText(item);
                        klasse = item;
                    }
                } else {
                    if (klasseToSet.equalsIgnoreCase(item)) {
                        jComboBoxKlasse.setSelectedIndex(i);
                        jTextFieldKlasse.setText(item);
                        klasse = item;
                    }
                }
            }
            i++;
        }
        if ((select) && (klasse.length() == 0)) {
            if (jComboBoxKlasse.getItemCount() > 0) {
                jComboBoxKlasse.setSelectedIndex(0);
                jTextFieldKlasse.setText(jComboBoxKlasse.getSelectedItem().toString());
                klasse = jComboBoxKlasse.getSelectedItem().toString();
            }
        }
        if (!select) jComboBoxKlasse.setSelectedIndex(-1);
        Collection<AITestRunData> colC = mAITestRunDataPool.getMapForKlasse(klasse).values();
        Iterator<AITestRunData> iterC = colC.iterator();
        jComboBoxName.removeAllItems();
        i = 0;
        while (iterC.hasNext()) {
            AITestRunData item = iterC.next();
            jComboBoxName.addItem(item.mName);
            if ((i == 0) && (select)) {
                jComboBoxName.setSelectedIndex(0);
                mAITestRunData = mAITestRunDataPool.get(item.mName);
                setAllFromCurrent();
            }
            i++;
        }
        if (!select) jComboBoxName.setSelectedIndex(-1);
        mClassSetting--;
    }

    private void clearAll() {
        mClassSetting++;
        mAITestRunData = new AITestRunData();
        setAllFromCurrent();
        mClassSetting--;
    }

    private void setAllFromCurrent() {
        mClassSetting++;
        jComboBoxKlasse.setSelectedItem(mAITestRunData.mClass);
        jTextFieldKlasse.setText(mAITestRunData.mClass);
        jComboBoxName.setSelectedItem(mAITestRunData.mName);
        jTextFieldName.setText(mAITestRunData.mName);
        setAllTestRunsToCollection("");
        for (int i = 0; i < mAITestRunData.mTestName.size(); i++) {
            String name = mAITestRunData.mTestName.elementAt(i);
            jListDeckAll.setSelectedValue(name, false);
            moveListSelection(jListDeckAll, jListDecksKnown);
        }
        mClassSetting--;
    }

    private void readAllToCurrent() {
        mAITestRunData.mClass = jTextFieldKlasse.getText();
        mAITestRunData.mName = jTextFieldName.getText();
        ListModel model = jListDecksKnown.getModel();
        Vector<String> formelSelected = new Vector<String>();
        for (int i = 0; i < model.getSize(); i++) formelSelected.addElement((String) model.getElementAt(i));
        mAITestRunData.mTestName = formelSelected;
    }

    private void setAllTestRunsToCollection(String klasse) {
        mClassSetting++;
        int i = 0;
        Collection<AITestData> colC;
        if ((klasse == null) || (klasse.length() == 0)) {
            colC = mAITestDataPool.getHashMap().values();
        } else {
            colC = mAITestDataPool.getMapForKlasse(klasse).values();
        }
        Iterator<AITestData> iterC = colC.iterator();
        Vector<String> names = new Vector<String>();
        while (iterC.hasNext()) {
            AITestData item = iterC.next();
            names.addElement(item.mName);
            i++;
        }
        Collections.sort(names, new Comparator<String>() {

            public int compare(String s1, String s2) {
                return s1.compareTo(s2);
            }
        });
        jListDeckAll.setListData(names);
        jListDecksKnown.setListData(new Vector<String>());
        mClassSetting--;
    }

    void moveListSelection(JList from, JList to) {
        int[] indeces = from.getSelectedIndices();
        Object[] values = from.getSelectedValues();
        ListModel model = from.getModel();
        Vector<String> values3 = new Vector<String>();
        for (int i = 0; i < model.getSize(); i++) {
            Object object = model.getElementAt(i);
            boolean toAdd = true;
            for (int j = 0; j < indeces.length; j++) {
                int index = indeces[j];
                if (index == i) toAdd = false;
            }
            if (toAdd) values3.addElement((String) object);
        }
        from.setListData(values3);
        model = to.getModel();
        Vector<String> values2 = new Vector<String>();
        for (int i = 0; i < values.length; i++) {
            Object object = values[i];
            values2.addElement((String) object);
        }
        for (int i = 0; i < model.getSize(); i++) {
            Object object = model.getElementAt(i);
            values2.addElement((String) object);
        }
        to.setListData(values2);
    }

    void removeListSelection(JList from) {
        int[] indeces = from.getSelectedIndices();
        Object[] values = from.getSelectedValues();
        ListModel model = from.getModel();
        Vector<String> values3 = new Vector<String>();
        for (int i = 0; i < model.getSize(); i++) {
            Object object = model.getElementAt(i);
            boolean toAdd = true;
            for (int j = 0; j < indeces.length; j++) {
                int index = indeces[j];
                if (index == i) toAdd = false;
            }
            if (toAdd) values3.addElement((String) object);
        }
        from.setListData(values3);
    }

    private void reorgCollection() {
        Object c = jComboBoxKlasse.getSelectedItem();
        readAllToCurrent();
        String klasse = "";
        if (c != null) klasse = c.toString();
        mClassSetting++;
        setAllTestRunsToCollection("");
        for (int i = 0; i < mAITestRunData.mTestName.size(); i++) {
            String formel = mAITestRunData.mTestName.elementAt(i);
            jListDeckAll.setSelectedValue(formel, true);
            moveListSelection(jListDeckAll, jListDecksKnown);
        }
        mClassSetting--;
    }

    String reason = "";

    String error = "";

    boolean compare(Match eMatch, AITestData test, int orhHealth0, int orgHealth1) {
        boolean ret = true;
        reason = "ok";
        error = "";
        TestMatch tmatch = new TestMatch();
        Match.loadMatchLists(test.mTargetState, test.getTargetSwitch(), tmatch, "testmatches" + File.separator);
        ret = ret && CardList.compareID(eMatch.getBattlefield(0), tmatch.getBattlefield(0));
        if (!ret) {
            reason = "Battlefield 0 dif";
            error = "Expected: " + tmatch.getBattlefield(0) + "\n" + "got     : " + eMatch.getBattlefield(0);
            return false;
        }
        ret = ret && CardList.compareID(eMatch.getHand(0), tmatch.getHand(0));
        if (!ret) {
            reason = "Hand 0 dif";
            error = "Expected: " + tmatch.getHand(0) + "\n" + "got     : " + eMatch.getHand(0);
            return false;
        }
        ret = ret && CardList.compareID(eMatch.getLand(0), tmatch.getLand(0));
        if (!ret) {
            reason = "Land 0 dif";
            error = "Expected: " + tmatch.getLand(0) + "\n" + "got     : " + eMatch.getLand(0);
            return false;
        }
        ret = ret && CardList.compareID(eMatch.getLibrary(0), tmatch.getLibrary(0));
        if (!ret) {
            reason = "Library 0 dif";
            error = "Expected: " + tmatch.getLibrary(0) + "\n" + "got     : " + eMatch.getLibrary(0);
            return false;
        }
        ret = ret && CardList.compareID(eMatch.getGraveyard(0), tmatch.getGraveyard(0));
        if (!ret) {
            reason = "Graveyard 0 dif";
            error = "Expected: " + tmatch.getGraveyard(0) + "\n" + "got     : " + eMatch.getGraveyard(0);
            return false;
        }
        ret = ret && CardList.compareID(eMatch.getDiscarded(0), tmatch.getDiscarded(0));
        if (!ret) {
            reason = "Discard 0 dif";
            error = "Expected: " + tmatch.getDiscarded(0) + "\n" + "got     : " + eMatch.getDiscarded(0);
            return false;
        }
        ret = ret && CardList.compareID(eMatch.getBattlefield(1), tmatch.getBattlefield(1));
        if (!ret) {
            reason = "Battlefield 1 dif";
            error = "Expected: " + tmatch.getBattlefield(1) + "\n" + "got     : " + eMatch.getBattlefield(1);
            return false;
        }
        ret = ret && CardList.compareID(eMatch.getHand(1), tmatch.getHand(1));
        if (!ret) {
            reason = "Hand 1 dif";
            error = "Expected: " + tmatch.getHand(1) + "\n" + "got     : " + eMatch.getHand(1);
            return false;
        }
        ret = ret && CardList.compareID(eMatch.getLand(1), tmatch.getLand(1));
        if (!ret) {
            reason = "Land 1 dif";
            error = "Expected: " + tmatch.getLand(1) + "\n" + "got     : " + eMatch.getLand(1);
            return false;
        }
        ret = ret && CardList.compareID(eMatch.getLibrary(1), tmatch.getLibrary(1));
        if (!ret) {
            reason = "Library 1 dif";
            error = "Expected: " + tmatch.getLibrary(1) + "\n" + "got     : " + eMatch.getLibrary(1);
            return false;
        }
        ret = ret && CardList.compareID(eMatch.getGraveyard(1), tmatch.getGraveyard(1));
        if (!ret) {
            reason = "Graveyard 1 dif";
            error = "Expected: " + tmatch.getGraveyard(1) + "\n" + "got     : " + eMatch.getGraveyard(1);
            return false;
        }
        ret = ret && CardList.compareID(eMatch.getDiscarded(1), tmatch.getDiscarded(1));
        if (!ret) {
            reason = "Discard 1 dif";
            error = "Expected: " + tmatch.getDiscarded(1) + "\n" + "got     : " + eMatch.getDiscarded(1);
            return false;
        }
        ret = ret && (eMatch.getLife(0) - orhHealth0 == test.getTarget0Health());
        if (!ret) {
            reason = "Health 0 dif";
            error = "Expected: " + (test.getTarget0Health()) + "\n" + "got     : " + (eMatch.getLife(0) - orhHealth0);
            return false;
        }
        ret = ret && (eMatch.getLife(1) - orgHealth1 == test.getTarget1Health());
        if (!ret) {
            reason = "Health 1 dif";
            error = "Expected: " + test.getTarget1Health() + "\n" + "got     : " + (eMatch.getLife(1) - orgHealth1);
            return false;
        }
        return ret;
    }

    String reason2 = "";

    String error2 = "";

    Match unpauseMatch = null;

    private boolean executeOneTest(final AITestData test) {
        class Result {

            public boolean result = false;

            public String reason = "ok";

            public String error = "";
        }
        final Result result = new Result();
        final TestMatch match = new TestMatch();
        String AI1 = "";
        String AI2 = "";
        if (test.mFixedAIName.length() == 0) {
            error2 = "No fixed AI name!";
            reason2 = "No fixed AI name!";
            return false;
        }
        if (test.mFixedAINumber == 0) {
            AI1 = test.mFixedAIName;
            AI2 = jComboBoxAITest.getSelectedItem().toString();
        } else {
            AI2 = test.mFixedAIName;
            AI1 = jComboBoxAITest.getSelectedItem().toString();
        }
        Match.loadMatchUnstated(test.mSourceState, AI1, AI2, test.getSourceSwitch(), mParent, match, "testmatches" + File.separator);
        match.setLife(0, test.mPlayer1StartHealth);
        match.setLife(1, test.mPlayer2StartHealth);
        jButtonUnpause.setVisible(false);
        if (match != null) {
            final int orgHealth0 = match.getLife(0);
            final int orgHealth1 = match.getLife(1);
            match.setTurnsOnly(new MatchListener() {

                public synchronized void matchDoneNotify() {
                    result.result = compare(match, test, orgHealth0, orgHealth1);
                    result.reason = reason;
                    result.error = error;
                    mBreak = true;
                    if (testerThread != null) testerThread.interrupt();
                }
            }, test.mAfterRounds);
            if (match.didMatchStart()) {
                Match.mGameStarted = true;
                JPortalInternalFrame frame = null;
                if (jCheckBoxDebug.isSelected()) {
                    frame = mParent.showEAIDebug((mAITestData.mFixedAINumber + 1) % 2, match);
                }
                Match.match = match;
                match.startMatch();
                mBreak = false;
                {
                    try {
                        while (!mBreak) {
                            testerThread.sleep(100);
                            if (match.isPause()) {
                                jLabelRunning.setForeground(new java.awt.Color(204, 0, 0));
                                jLabelRunning.setText("Pause!");
                                jButtonUnpause.setVisible(true);
                                unpauseMatch = match;
                            } else {
                                jLabelRunning.setForeground(new java.awt.Color(0, 204, 0));
                                jLabelRunning.setText("Running!");
                                jButtonUnpause.setVisible(false);
                                unpauseMatch = null;
                            }
                            if (mBreak) {
                                continue;
                            }
                        }
                    } catch (InterruptedException e) {
                        System.out.println(e);
                    } catch (Throwable e) {
                        System.out.println(e);
                        e.printStackTrace();
                    }
                }
                if (frame != null) {
                    try {
                        frame.setClosed(true);
                    } catch (Throwable e) {
                    }
                }
                Match.match = null;
                Match.mGameStarted = false;
            }
        }
        jButtonUnpause.setVisible(false);
        error2 = result.error;
        reason2 = result.reason;
        return result.result;
    }

    public void run() {
        mTestResult = new Vector<AITestData>();
        updateTestResults();
        jButton1.setEnabled(false);
        jLabelRunning.setVisible(true);
        executeTestRuns();
        jButton1.setEnabled(true);
        jLabelRunning.setVisible(false);
    }

    private void executeTestRuns() {
        if (jComboBoxAITest.getSelectedIndex() == -1) return;
        for (int i = 0; i < mAITestRunData.mTestName.size(); i++) {
            String testName = mAITestRunData.mTestName.elementAt(i);
            AITestData test = mAITestDataPool.get(testName);
            boolean tResult = executeOneTest(test);
            test.mResult = tResult;
            test.mReason = reason2;
            test.mError = error2;
            mTestResult.addElement(test);
            updateTestResults();
        }
    }

    void updateTestResults() {
        csaPanel.reInit();
    }

    javax.swing.table.TableModel getTableModel() {
        AbstractTableModel model = new AbstractTableModel() {

            @Override
            public String getColumnName(int col) {
                if (col == 0) return "Test name";
                if (col == 1) return "Reason";
                if (col == 2) return "Result";
                return "";
            }

            public int getRowCount() {
                return mTestResult.size();
            }

            public int getColumnCount() {
                return 3;
            }

            public Object getValueAt(int row, int col) {
                if (row >= mTestResult.size()) {
                    System.out.println("Test Result Table error!");
                    return "";
                }
                AITestData ms = mTestResult.elementAt(row);
                if (col == 0) return ms.mName;
                if (col == 1) return ms.mReason;
                if (col == 2) return ms.mResult;
                return "";
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }

            @Override
            public Class getColumnClass(int col) {
                if (col == 0) return String.class;
                if (col == 1) return String.class;
                if (col == 2) return Boolean.class;
                return String.class;
            }
        };
        return model;
    }

    private void tableDoubleClicked() {
        if (mRow < 0) return;
        AITestData ms = mTestResult.elementAt(mRow);
        jTextArea1.setText(ms.mError);
    }
}
