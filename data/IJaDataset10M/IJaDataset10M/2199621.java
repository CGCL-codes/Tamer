package csa.jportal.script;

import csa.util.syntax.Syntax.HighlightedDocument;
import csa.jportal.ai.*;
import csa.jportal.script.InterpreterUtility;
import bsh.Interpreter;
import csa.gui.*;
import java.io.IOError;
import javax.swing.*;
import java.util.*;
import java.io.*;
import csa.jportal.match.*;
import csa.jportal.match.communication.*;
import csa.jportal.config.*;
import csa.jportal.card.*;
import csa.jportal.carddeck.*;
import csa.jportal.*;
import csa.jportal.gui.*;
import javax.swing.text.*;
import java.awt.*;

/**
 *
 * @author Malban
 */
public class ScriptPanel extends javax.swing.JPanel {

    private HighlightedDocument document = new HighlightedDocument();

    /** The text pane displaying the document. */
    private String mTemplateType = "";

    private String mTemplateAbility = "";

    private Scriptable mProvider = null;

    private ScriptDataPool mScriptDataPool = null;

    private ScriptData mScriptData = null;

    private Card mCard = null;

    private int mClassSetting = 0;

    private JPortalView mMainParent = null;

    public void setMainParent(JPortalView mp) {
        mMainParent = mp;
    }

    public void setCard(Card card) {
        mCard = card;
    }

    /** Creates new form AIPlayerPanel */
    public ScriptPanel() {
        initComponents();
        jTextPaneScript.setDocument(document);
        document.setHighlightStyle(HighlightedDocument.JAVA_STYLE);
        setTabs(jTextPaneScript, 4);
        resetConfigPool(false, "");
        refreshScriptCombo();
        jComboBoxStatelessMatches.removeAllItems();
        File path = new File(csa.Global.mBaseDir + File.separator + Match.getDebugMatchPoolBaseName(false));
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
                    jComboBoxStatelessMatches.addItem(aName);
                }
            }
        }
        jComboBoxPhase.removeAllItems();
        String[] phases = Match.PHASE_STRINGS;
        for (int i = 0; i < phases.length; i++) {
            String string = phases[i];
            jComboBoxPhase.addItem(string);
        }
        Configuration C = Configuration.getConfiguration();
        jCheckBoxDebugOff.setSelected(C.isDebugOff());
        jTextFieldDebugLevel.setText("" + C.getDebugLevel());
        jTextFieldFiles.setText("" + C.getDebugFiles());
        jTextFieldClasses.setText("" + C.getDebugClasses());
        jTextFieldMethods.setText("" + C.getDebugMethods());
        jCheckBoxDebugTiming.setSelected(C.isDebugTiming());
    }

    @Override
    protected void finalize() throws Throwable {
        if (mLogListener != null) Configuration.getConfiguration().getDebugEntity().removeLogListener(mLogListener);
        mLogListener = null;
        if (mMatch != null) {
            mMatch.quitMatch();
            mMatch = null;
        }
        super.finalize();
    }

    public void setTabs(JTextPane textPane, int charactersPerTab) {
        FontMetrics fm = textPane.getFontMetrics(textPane.getFont());
        int charWidth = fm.charWidth('w');
        int tabWidth = charWidth * charactersPerTab;
        TabStop[] tabs = new TabStop[10];
        for (int j = 0; j < tabs.length; j++) {
            int tab = j + 1;
            tabs[j] = new TabStop(tab * tabWidth);
        }
        TabSet tabSet = new TabSet(tabs);
        SimpleAttributeSet attributes = new SimpleAttributeSet();
        StyleConstants.setTabSet(attributes, tabSet);
        int length = textPane.getDocument().getLength();
        textPane.getStyledDocument().setParagraphAttributes(0, length, attributes, false);
    }

    private void resetConfigPool(boolean select, String name) {
        mClassSetting++;
        jComboBoxSituationKey.removeAllItems();
        if (mProvider == null) {
            jComboBoxSituationKey.setSelectedIndex(-1);
            clearAll();
            mClassSetting--;
            return;
        }
        String[] keys = mProvider.getSituations().getSituationKeys();
        for (int i = 0; i < keys.length; i++) {
            jComboBoxSituationKey.addItem(keys[i]);
            if (select) {
                if ((name != null) && (name.length() > 0)) {
                    if (name.equals(keys[i])) {
                        jComboBoxSituationKey.setSelectedIndex(i);
                        mScriptData = mScriptDataPool.get(keys[i]);
                        setAllFromCurrent();
                    }
                } else if (i == 0) {
                    jComboBoxSituationKey.setSelectedIndex(i);
                    mScriptData = mScriptDataPool.get(keys[i]);
                    setAllFromCurrent();
                }
            }
        }
        if (!select) {
            jComboBoxSituationKey.setSelectedIndex(-1);
            clearAll();
        }
        mClassSetting--;
    }

    public void clearAll() {
        mClassSetting++;
        mScriptData = new ScriptData();
        setAllFromCurrent();
        mClassSetting--;
    }

    private void setAllFromCurrent() {
        mClassSetting++;
        if ((mScriptData.mName == null) || (mScriptData.mName.length() == 0)) {
            jComboBoxSituationKey.setSelectedIndex(-1);
        } else jComboBoxSituationKey.setSelectedItem(mScriptData.mName);
        if (mProvider != null) jTextAreaSitComment.setText(mProvider.getSituations().getComment(mScriptData.mName)); else jTextAreaSitComment.setText("");
        jTextPaneScript.setText(mScriptData.mScript);
        jTextAreaReturn.setText("");
        mClassSetting--;
    }

    public void readAllToCurrent() {
        mScriptData.mScript = jTextPaneScript.getText();
    }

    public void save() {
        readAllToCurrent();
        if (mCard != null) {
            String basedir = mCard.getScriptPoolBaseDir();
            File f = new File(csa.Global.mBaseDir + File.separator + basedir);
            if (!f.exists()) {
                boolean b = f.mkdir();
                if (!b) System.out.println("FALSE: " + f.toString());
            }
        }
        if (mScriptDataPool != null) mScriptDataPool.save();
    }

    public void remove() {
        mScriptDataPool.remove(mScriptData);
        mScriptDataPool.save();
        clearAll();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jPanel4 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTextArea4 = new javax.swing.JTextArea();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTextArea5 = new javax.swing.JTextArea();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        jTextArea6 = new javax.swing.JTextArea();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        jTextArea7 = new javax.swing.JTextArea();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane11 = new javax.swing.JScrollPane();
        jTextArea8 = new javax.swing.JTextArea();
        jPanel12 = new javax.swing.JPanel();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jPanel14 = new javax.swing.JPanel();
        jScrollPane12 = new javax.swing.JScrollPane();
        jTextArea9 = new javax.swing.JTextArea();
        jPanel15 = new javax.swing.JPanel();
        jScrollPane13 = new javax.swing.JScrollPane();
        jTextArea10 = new javax.swing.JTextArea();
        jPanel13 = new javax.swing.JPanel();
        jScrollPane14 = new javax.swing.JScrollPane();
        jTextArea11 = new javax.swing.JTextArea();
        jPanel5 = new javax.swing.JPanel();
        jTabbedPane4 = new javax.swing.JTabbedPane();
        jPanel16 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTextArea12 = new javax.swing.JTextArea();
        jPanel17 = new javax.swing.JPanel();
        jScrollPane15 = new javax.swing.JScrollPane();
        jTextArea3 = new javax.swing.JTextArea();
        jPanel18 = new javax.swing.JPanel();
        jScrollPane16 = new javax.swing.JScrollPane();
        jTextArea13 = new javax.swing.JTextArea();
        jPanel19 = new javax.swing.JPanel();
        jScrollPane17 = new javax.swing.JScrollPane();
        jTextArea14 = new javax.swing.JTextArea();
        jPanel20 = new javax.swing.JPanel();
        jScrollPane18 = new javax.swing.JScrollPane();
        jTextArea15 = new javax.swing.JTextArea();
        jPanel21 = new javax.swing.JPanel();
        jScrollPane19 = new javax.swing.JScrollPane();
        jTextArea16 = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        jButtonExecuteTest = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextAreaReturn = new javax.swing.JTextArea();
        jLabel6 = new javax.swing.JLabel();
        jComboBoxStatelessMatches = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jComboBoxPlayer = new javax.swing.JComboBox();
        jComboBoxPhase = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        jComboBoxLittleScripts = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jComboBoxDebugSettings = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jComboBoxTestMatch = new javax.swing.JComboBox();
        jTextFieldTestMatch = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox();
        jCheckBoxTryAsCom = new javax.swing.JCheckBox();
        jButtonSTOPP = new javax.swing.JButton();
        jCheckBoxMy = new javax.swing.JCheckBox();
        jTabbedPane5 = new javax.swing.JTabbedPane();
        jPanel22 = new javax.swing.JPanel();
        jScrollPane21 = new javax.swing.JScrollPane();
        jTextAreaDebug = new javax.swing.JTextArea();
        jPanel23 = new javax.swing.JPanel();
        jScrollPane20 = new javax.swing.JScrollPane();
        jTextAreaLog = new javax.swing.JTextArea();
        jPanel24 = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jTextFieldDebugLevel = new javax.swing.JTextField();
        jCheckBoxDebugWindow = new javax.swing.JCheckBox();
        jButtonClearDebug = new javax.swing.JButton();
        jCheckBoxDebugTiming = new javax.swing.JCheckBox();
        jTextFieldFiles = new javax.swing.JTextField();
        jTextFieldClasses = new javax.swing.JTextField();
        jTextFieldMethods = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jCheckBoxDebugOff = new javax.swing.JCheckBox();
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextPaneScript = new javax.swing.JTextPane();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaSitComment = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        jPanel26 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jComboBoxSituationKey = new javax.swing.JComboBox();
        jTabbedPane1.setName("jTabbedPane1");
        jTabbedPane1.setPreferredSize(new java.awt.Dimension(227, 100));
        jPanel3.setName("jPanel3");
        jScrollPane2.setName("jScrollPane2");
        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Monospaced", 0, 12));
        jTextArea1.setRows(5);
        jTextArea1.setText("MatchPlayable: \tplayer \nMatchPlayable: \topponent \nCard: \t\tcard \t\nMatch: \t\tmatch \nCardShellEnvironment: ev \n\nBoolean: \tmaybeTaken \nBoolean: \tcancled \nBoolean: \tsuccessfull \nBoolean: \tselectYes \nBoolean: \tselectNo \n\n\nMatchPlayable: targetPlayer \n\nCard: \t\ttargetCard \nCard: \t\tattacker \nCard: \t\tblocker \nTemporaryEffect: effect \nint:\t \tamount \nString: \tthisScript \nCardTrigger:\ttrigger\nLogable: \tD \nLogable: \tL");
        jTextArea1.setName("jTextArea1");
        jScrollPane2.setViewportView(jTextArea1);
        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 471, Short.MAX_VALUE));
        jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 599, Short.MAX_VALUE));
        jTabbedPane1.addTab("Vars", jPanel3);
        jPanel4.setName("jPanel4");
        jTabbedPane2.setName("jTabbedPane2");
        jPanel6.setName("jPanel6");
        jScrollPane5.setName("jScrollPane5");
        jTextArea2.setColumns(20);
        jTextArea2.setFont(new java.awt.Font("Monospaced", 0, 12));
        jTextArea2.setRows(5);
        jTextArea2.setText("void askForNothing(MatchPlayable p, Card card, CardShellEnvironment ev)\nvoid askForAttackingTargetCreature(MatchPlayable p, Card card, CardShellEnvironment ev)\nvoid askForTargetCreature(MatchPlayable p, Card card, CardShellEnvironment ev)\nvoid askForCreatureOrPlayer(MatchPlayable p, Card card, CardShellEnvironment ev)\nvoid askForUntappedTargetCreature(MatchPlayable p, Card card, CardShellEnvironment ev)\nvoid askForTappedTargetCreature(MatchPlayable p, Card card, CardShellEnvironment ev)\nvoid askForTargetCreatureFromGrave(MatchPlayable p, Card card, CardShellEnvironment ev)\nvoid askYesNo(MatchPlayable p, Card card, CardShellEnvironment ev)\nvoid askSelectCardFromHand(MatchPlayable p, Card card, CardShellEnvironment ev)\nvoid askForTargetHandCreature(MatchPlayable p, Card card, CardShellEnvironment ev)\nvoid askForTargetHandLand(MatchPlayable p, Card card, CardShellEnvironment ev)\nvoid askSelectFromLand(MatchPlayable p, Card card, CardShellEnvironment ev)\nvoid askSelectFromOwnLand(MatchPlayable p, Card card, CardShellEnvironment ev)\nvoid askForLandFromLibrary(MatchPlayable p, Card card, CardShellEnvironment ev)\n");
        jTextArea2.setName("jTextArea2");
        jScrollPane5.setViewportView(jTextArea2);
        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE));
        jPanel6Layout.setVerticalGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 553, Short.MAX_VALUE));
        jTabbedPane2.addTab("Ask", jPanel6);
        jPanel7.setName("jPanel7");
        jScrollPane7.setName("jScrollPane7");
        jTextArea4.setColumns(20);
        jTextArea4.setFont(new java.awt.Font("Monospaced", 0, 12));
        jTextArea4.setRows(5);
        jTextArea4.setText("void moveCardFromLibraryToLand(MatchPlayable p, Card card)\nvoid drawLibraryCardToHand(MatchPlayable p, int count)\nCard drawLibraryToHand(MatchPlayable p)\n\nvoid moveCardFromHandToGraveyard(MatchPlayable p, Card card)\n\nvoid moveCardFromCreatureToHand(MatchPlayable p, Card card)\nvoid moveCardFromCreatureToGraveyard(MatchPlayable p, Card card)\n\nvoid moveCardFromLandToGraveyard(MatchPlayable p, Card card)\n\nvoid moveCardFromGraveyardToHand(MatchPlayable p, Card card)\nvoid moveCardFromGraveyardToLibrary(MatchPlayable p, Card card)\n\n");
        jTextArea4.setName("jTextArea4");
        jScrollPane7.setViewportView(jTextArea4);
        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE));
        jPanel7Layout.setVerticalGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 553, Short.MAX_VALUE));
        jTabbedPane2.addTab("Move", jPanel7);
        jPanel8.setName("jPanel8");
        jScrollPane8.setName("jScrollPane8");
        jTextArea5.setColumns(20);
        jTextArea5.setFont(new java.awt.Font("Monospaced", 0, 12));
        jTextArea5.setRows(5);
        jTextArea5.setText("String getGlobalScriptBaseKey(String script, MatchPlayable p, Card card)\n\nint getIntData(String key) \nvoid setIntData(String key, int value) \nvoid removeIntData(String key) \n\nObject getObjectData(String key) \nvoid setObjectData(String key, Object value) \nvoid removeObjectData(String key) \n\nboolean getBoolData(String key) \nvoid setBoolData(String key, boolean value) \nvoid removeBoolData(String key) \n");
        jTextArea5.setName("jTextArea5");
        jScrollPane8.setViewportView(jTextArea5);
        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE));
        jPanel8Layout.setVerticalGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 553, Short.MAX_VALUE));
        jTabbedPane2.addTab("Globals", jPanel8);
        jPanel9.setName("jPanel9");
        jScrollPane9.setName("jScrollPane9");
        jTextArea6.setColumns(20);
        jTextArea6.setFont(new java.awt.Font("Monospaced", 0, 12));
        jTextArea6.setRows(5);
        jTextArea6.setText("CardList getHand(MatchPlayable p)\nCardList getLand(MatchPlayable p)\nCardList getCreature(MatchPlayable p) \nCardList getGraveyard(MatchPlayable p)\nCardList getLibrary(MatchPlayable p) \nCardList getDiscarded(MatchPlayable p)\n\nCardList getHandOpponent(MatchPlayable p)    \nCardList getLandOpponent(MatchPlayable p)    \nCardList getCreatureOpponent(MatchPlayable p)\nCardList getGraveyardOpponent(MatchPlayable p)\nCardList getLibraryOpponent(MatchPlayable p) \nCardList getDiscardedOpponent(MatchPlayable p)\n");
        jTextArea6.setName("jTextArea6");
        jScrollPane9.setViewportView(jTextArea6);
        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE));
        jPanel9Layout.setVerticalGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 553, Short.MAX_VALUE));
        jTabbedPane2.addTab("Lists", jPanel9);
        jPanel10.setName("jPanel10");
        jScrollPane10.setName("jScrollPane10");
        jTextArea7.setColumns(20);
        jTextArea7.setFont(new java.awt.Font("Monospaced", 0, 12));
        jTextArea7.setRows(5);
        jTextArea7.setText("boolean incMana(MatchPlayable p, String color)\nboolean decMana(MatchPlayable p, String color)\nint getManaOpponent(MatchPlayable p, String color)\nint getManaOpponent(MatchPlayable p)\nint getMana(MatchPlayable p, String color)\nint getMana(MatchPlayable p)\n\nint getLifeOpponent(MatchPlayable p)\nint getLife(MatchPlayable p)\nvoid addLifeToPlayer(MatchPlayable ini,MatchPlayable tar, int healing, Card fromCard)\n\nboolean isActive(MatchPlayable p)\nboolean isMyTurn(MatchPlayable p)\nMatchPlayable getOpponent(MatchPlayable p)\n\nboolean isLibraryDrawn(MatchPlayable p)\n");
        jTextArea7.setName("jTextArea7");
        jScrollPane10.setViewportView(jTextArea7);
        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE));
        jPanel10Layout.setVerticalGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 553, Short.MAX_VALUE));
        jTabbedPane2.addTab("Player", jPanel10);
        jPanel11.setName("jPanel11");
        jScrollPane11.setName("jScrollPane11");
        jTextArea8.setColumns(20);
        jTextArea8.setFont(new java.awt.Font("Monospaced", 0, 12));
        jTextArea8.setRows(5);
        jTextArea8.setText("void setEffect(Card source, Card target, MatchPlayable targetPlayer, int no)\nvoid cancelEffect(Card source, Card target, MatchPlayable targetPlayer, int no)\n\nvoid addTrigger(CardTrigger trigger)\nvoid removeTrigger(CardTrigger trigger)\n\nvoid damageCreature(MatchPlayable initiator, MatchPlayable target, Card creature, int damage)\nvoid tapCreature(Card creature)\n\nvoid shuffleLibrary(MatchPlayable p)\nvoid openLibrary(MatchPlayable p)\nvoid closeLibrary(MatchPlayable p, boolean shuffle)\n\nvoid openHand(MatchPlayable toOpen, MatchPlayable fromPlayer)\nvoid closeHand(MatchPlayable toClose, MatchPlayable fromPlayer)\n\nboolean isAttacker(MatchPlayable p)\nVector<Card> getBlocker(Card attacker)\nVector<Card> getAttacker()\nCardList getAttackerList()\n\nMatchPlayable getOwner(Card card)\nint getPhase()\nvoid setPlayerCardMessage(MatchPlayable p, String w)\n");
        jTextArea8.setName("jTextArea8");
        jScrollPane11.setViewportView(jTextArea8);
        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE));
        jPanel11Layout.setVerticalGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 553, Short.MAX_VALUE));
        jTabbedPane2.addTab("Other", jPanel11);
        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jTabbedPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 471, Short.MAX_VALUE));
        jPanel4Layout.setVerticalGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jTabbedPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 599, Short.MAX_VALUE));
        jTabbedPane1.addTab("Match", jPanel4);
        jPanel12.setName("jPanel12");
        jTabbedPane3.setName("jTabbedPane3");
        jPanel14.setName("jPanel14");
        jScrollPane12.setName("jScrollPane12");
        jTextArea9.setColumns(20);
        jTextArea9.setFont(new java.awt.Font("Monospaced", 0, 12));
        jTextArea9.setRows(5);
        jTextArea9.setText("void addIntData(String key, int value)\n\nvoid subIntData(String key, int value)\nvoid setIntData(String key, int value) \nint getIntData(String key) \nvoid removeIntData(String key)\n\nvoid setBoolData(String key, boolean value) \nboolean getBoolData(String key) \nvoid removeBoolData(String key) \nString getUniqueID() \n\nvoid setObjectData(String key, Object value) \nObject getObjectData(String key) \nvoid removeObjectData(String key)\n\nString CARD_TAPPED\nString CARD_SICKNESS \nString CARD_ATTACKER \nString CARD_BLOCKER\nString CARD_DAMAG\nString CARD_POWER_UNUSED \n\nString TEMP_POWER_MOD \nString TEMP_TOUGHNESS_MOD \nString TEMP_ABILITIES_MOD \n\n\nString CARD_DEATH \n");
        jTextArea9.setName("jTextArea9");
        jScrollPane12.setViewportView(jTextArea9);
        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE));
        jPanel14Layout.setVerticalGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 553, Short.MAX_VALUE));
        jTabbedPane3.addTab("Volatile", jPanel14);
        jPanel15.setName("jPanel15");
        jScrollPane13.setName("jScrollPane13");
        jTextArea10.setColumns(20);
        jTextArea10.setFont(new java.awt.Font("Monospaced", 0, 12));
        jTextArea10.setRows(5);
        jTextArea10.setText("\nboolean isTapped() {return mVData.getBoolData(CARD_TAPPED);}\nboolean isAttacker() {return mVData.getBoolData(CARD_ATTACKER);}\nboolean isBlocker() {return mVData.getBoolData(CARD_BLOCKER);}\n\nboolean isLand()\nboolean isCreature()\nboolean isInstant()\nboolean isSorcery()\n\nString getType()\nString getSubtype()\n\nint getNowPower()\nint getNowToughness()\n\nString getNowColor()\n\nint getManaCost()\nint getManaCostUncolored()\nint getManaCost(String color)\n\nString getName()\nString getText()\n\nVector <String> getNowCardAbilities()\nboolean hasAbility(String ability)");
        jTextArea10.setName("jTextArea10");
        jScrollPane13.setViewportView(jTextArea10);
        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane13, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE));
        jPanel15Layout.setVerticalGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane13, javax.swing.GroupLayout.DEFAULT_SIZE, 553, Short.MAX_VALUE));
        jTabbedPane3.addTab("Other", jPanel15);
        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jTabbedPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 471, Short.MAX_VALUE));
        jPanel12Layout.setVerticalGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jTabbedPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 599, Short.MAX_VALUE));
        jTabbedPane1.addTab("Card", jPanel12);
        jPanel13.setName("jPanel13");
        jScrollPane14.setName("jScrollPane14");
        jTextArea11.setColumns(20);
        jTextArea11.setFont(new java.awt.Font("Monospaced", 0, 12));
        jTextArea11.setRows(5);
        jTextArea11.setText("Vector<Card> getCards()\nvoid addCard(Card card)\nvoid addCard(Card card, int pos)\nCard getCard(int pos)\nint getPos(Card c)\nint size()\n\nCardList getSubListByType(String type)\nCardList getSubListBySubType(String type)\nCardList getSubListByColor(String color)\nCardList getSubListByManaCost(int cost)\nCardList getSubListByManaPower(int power)\nCardList getSubListByManaToghness(int toughness)\nCardList getSubListByRarity(String rare)\nCardList getSubListTapState(boolean tapped)\nCardList getSubListBooleanState(String key , boolean state)    \nCardList getDeadList()   \n\nCardList drawRandom(int drawsize) \n\nboolean isInList(Card card)  \n\nvoid addList(CardList list)\nboolean moveCardTo(Card card, CardList list)\nboolean moveCardTo(Card card, CardList list, int pos)\n\nint getLandCount()\nint getLandCount(String color)\n\nvoid sortListByManaCost()\nvoid sortListByManaCost(final String color)\nvoid sortListByToughness()   \nvoid sortListByPower()\n       \n");
        jTextArea11.setName("jTextArea11");
        jScrollPane14.setViewportView(jTextArea11);
        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane14, javax.swing.GroupLayout.DEFAULT_SIZE, 471, Short.MAX_VALUE));
        jPanel13Layout.setVerticalGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane14, javax.swing.GroupLayout.DEFAULT_SIZE, 599, Short.MAX_VALUE));
        jTabbedPane1.addTab("CardList", jPanel13);
        jPanel5.setName("jPanel5");
        jTabbedPane4.setName("jTabbedPane4");
        jPanel16.setName("jPanel16");
        jScrollPane6.setName("jScrollPane6");
        jTextArea12.setColumns(20);
        jTextArea12.setFont(new java.awt.Font("Monospaced", 0, 11));
        jTextArea12.setRows(5);
        jTextArea12.setText("// Assassin's Blade played start\nint debugLevel = 3;\nD.addLog(\"4208: Assassin's Blade entered!\",debugLevel);\n\nif (match.getCreature(opponent).size()==0)\n{\n\tD.addLog(\"4208: No creature in play, nothing to destroy...\",debugLevel);\n\tD.addLog(\"4208: Instant is lost - STUPID!\", debugLevel);\n\tD.addLog(\"4208: Played card end. \", debugLevel);\n\tL.addLog(\"Assassin's Blade uselessly stabs at empty air.\");\n\tmatch.setPlayerCardMessage(player,\"Assassin's Blade uselessly stabs at empty air.\");\n\tbRet=true;\n\treturn;\n}\n\nif (targetCard == null)\n{\n\tif (successfull)\n\t{\n\t\tD.addLog(\"4208: No creature selected, nothing to destroy...\",debugLevel);\n\t\tD.addLog(\"4208: Instant is lost - STUPID!\", debugLevel);\n\t\tD.addLog(\"4208: Played card end. \", debugLevel);\n\t\tL.addLog(\"Assassin's Blade uselessly stabs at empty air.\");\n\t\tmatch.setPlayerCardMessage(player,\"Assassin's Blade uselessly stabs at empty air.\");\n\t\tbRet=true;\n\t\treturn;\n\t}\n\n\tD.addLog(\"4208: Chose a (non black) creature!\",debugLevel);\n\tmatch.askForAttackingTargetCreature(player, card, ev);\n\tmatch.setPlayerCardMessage(player,\"Select a non black attacking creature to go to the graveyard.\");\n\tbRet=false;\n\treturn;\n}\n\nD.addLog(\"4208: Player selected a creature to destroy: \"+targetCard, debugLevel);\nif (targetCard.getNowColor().equals(\"B\"))\n{\n\tD.addLog(\"4208: Are you blind? Thats a black creature! - Well - bad luck!\", debugLevel);\n\tD.addLog(\"4208: Instant is lost - STUPID!\", debugLevel);\n\tD.addLog(\"4208: Played card end. \", debugLevel);\n\tL.addLog(\"Assassin's Blade uselessly stabs black creature at empty air.\");\n\tmatch.setPlayerCardMessage(player,\"Assassin's Blade uselessly stabs black creature at empty air.\");\n\tbRet = true;\n\treturn;\n}\n\nif (!targetCard.isAttacker())\n{\n\tD.addLog(\"4208: Are you blind? That one isn't atacking!\", debugLevel);\n\tD.addLog(\"4208: Instant is lost - STUPID!\", debugLevel);\n\tD.addLog(\"4208: Played card end. \", debugLevel);\n\tL.addLog(\"Assassin's Blade uselessly stabs at non attacking creature at empty air.\");\n\tmatch.setPlayerCardMessage(player,\"Assassin's Blade uselessly stabs at non attacking creature at empty air.\");\n\tbRet = true;\n\treturn;\n}\n\nL.addLog(\"Assassin's Blade destroyed: \"+targetCard);\nmatch.setPlayerCardMessage(match.getOwner(targetCard),\"Assassin's Blade destroyed: \"+targetCard);\n\nmatch.moveCardFromCreatureToGraveyard(match.getOwner(targetCard), targetCard);\nbRet = true;\nD.addLog(\"4208: Assassin's Blade ended!\",debugLevel);\n// Assassin's Blade played end");
        jTextArea12.setName("jTextArea12");
        jScrollPane6.setViewportView(jTextArea12);
        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE));
        jPanel16Layout.setVerticalGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 553, Short.MAX_VALUE));
        jTabbedPane4.addTab("A Blade", jPanel16);
        jPanel17.setName("jPanel17");
        jScrollPane15.setName("jScrollPane15");
        jTextArea3.setColumns(20);
        jTextArea3.setFont(new java.awt.Font("Monospaced", 0, 11));
        jTextArea3.setRows(5);
        jTextArea3.setText("//PLAYED Defiant Stand start\nD.addLog(\"4380: Enter card played\",3);\nint debugLevel=3;\nif (targetCard == null)\n{\n\t// ask to select a target creature\n\tD.addLog(\"4380: Defiant Stand, chose a creature to buf with +1/+3.\",debugLevel);\n\tmatch.askForTargetCreature(player, card, ev);\n\tmatch.setPlayerCardMessage(player, \"Who to buf with +1/+3/untap?\" );\n\tbRet=false;\n\treturn;\n}\n\nD.addLog(\"4380: Player selected a creature to buf: \"+targetCard, debugLevel);\n\nmatch.setEffect(card, targetCard, null,0);\nbRet = true;\n\nD.addLog(\"4380: End card played\",3);\n//PLAYED Defiant Stand end\n\n\n");
        jTextArea3.setName("jTextArea3");
        jScrollPane15.setViewportView(jTextArea3);
        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane15, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE));
        jPanel17Layout.setVerticalGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane15, javax.swing.GroupLayout.DEFAULT_SIZE, 553, Short.MAX_VALUE));
        jTabbedPane4.addTab("D Stand", jPanel17);
        jPanel18.setName("jPanel18");
        jScrollPane16.setName("jScrollPane16");
        jTextArea13.setColumns(20);
        jTextArea13.setFont(new java.awt.Font("Monospaced", 0, 11));
        jTextArea13.setRows(5);
        jTextArea13.setText("//PLAYED Harsh Justice start\nint debugLevel=3;\nD.addLog(\"4386: Enter card played\",debugLevel);\n\nmatch.setEffect(card, null, player,0);\nbRet = true;\n\nD.addLog(\"4386: End card played\",debugLevel);\n//PLAYED Harsh Justice end\n\n\n\n\n\n// ENTER APPLAY EFFECT Hars Justice\nint debugLevel=3;\nD.addLog(\"4386: Apply effect Harsh Justice start\",debugLevel);\n\neffect.expireType = TemporaryEffect.EFFECT_DURATION_TILL_END_OF_ATTACK;\neffect.effectVisibleAsCard=false;\neffect.effectVisibleAsCardOnTargetPlayer=true;\neffect.effectReach=TemporaryEffect.EFFECT_REACH_TYPE_TARGET_PLAYER;\n\nCardTrigger trigger = CardTrigger.createPlayerDamageTrigger(card,player, effect.targetPlayer);\nmatch.addTrigger(trigger);\n\neffect.trigger=trigger;\n\nL.addLog(effect.targetPlayer+\" Harsh Justice added.\");\nD.addLog(\"4386: Apply effect Harsh Justice end\",debugLevel);\n\n// End APPLAY EFFECT Hars Justice\n\n\n\nmatch.removeTrigger(effect.trigger);\nL.addLog(effect.targetPlayer+\" Harsh Justice removed.\");\n\n\n\n//TRIGGER Harsh Justice start\nint debugLevel=3;\nD.addLog(\"4386: Enter card Trigger\",debugLevel);\n//match.modifyPlayerLife(match.getOpponent(trigger.targetPlayer), ev.amount);\nmatch.addLifeToPlayer(trigger.targetPlayer, match.getOpponent(trigger.targetPlayer), ev.amount, card);\nD.addLog(\"4386: End card trigger\",debugLevel);\n//TRIGGER Harsh Justice end\n");
        jTextArea13.setName("jTextArea13");
        jScrollPane16.setViewportView(jTextArea13);
        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane16, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE));
        jPanel18Layout.setVerticalGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane16, javax.swing.GroupLayout.DEFAULT_SIZE, 553, Short.MAX_VALUE));
        jTabbedPane4.addTab("H Justice", jPanel18);
        jPanel19.setName("jPanel19");
        jScrollPane17.setName("jScrollPane17");
        jTextArea14.setColumns(20);
        jTextArea14.setFont(new java.awt.Font("Monospaced", 0, 11));
        jTextArea14.setRows(5);
        jTextArea14.setText("// Seasoned Marshal declared as an attacker start\nint debugLevel = 3;\nD.addLog(\"4394: Seasoned Marshal declared as attacker start!\",debugLevel);\nif (maybeTaken)\n{\n\tD.addLog(\"4394: Maybe taken!\",debugLevel);\n\tD.addLog(\"4394: Seasoned Marshal declared as attacker end!\",debugLevel);\n\tbRet=true;\n\treturn;\n}\nif (targetCard == null)\n{\n\tD.addLog(\"4394: Target = null, asking for target..\",debugLevel);\n\tmatch.askForUntappedTargetCreature(player, card, ev);\n\tmatch.setPlayerCardMessage(match.getOwner(card), \"Select a creatur to tap!\" );\n\tbRet=false;\n\treason=\"select an (untapped) target creature\";\n\treturn;\n}\nif (targetCard.isTapped())\n{\n\tD.addLog(\"4394: Selected target is already tapped, select again...\",debugLevel);\n\tmatch.askForUntappedTargetCreature(player, card, ev);\n\tmatch.setPlayerCardMessage(match.getOwner(card), \"Select a creatur to tap!\" );\n\tbRet=false;\n\treason=\"selected creature must not be tapped!\";\n\treturn;\n}\n\nif(targetCard == card)\n{\n\tD.addLog(\"4394: Self select == Maybe taken, select cancled attacking without tapping.\",debugLevel);\n\n\tbRet=true; // cancel - dont tap, but continue with attack\nD.addLog(\"4394: Seasoned Marshal declared as attacker end!\",debugLevel);\n\treturn;\n}\n\nD.addLog(\"4394: Tapping selected target: \"+targetCard ,debugLevel);\nbRet=true;\nmatch.tapCreature(targetCard);\nL.addLog(\"Seasoned Marshal tapped \"+targetCard +\" upon attacking.\");\nD.addLog(\"4394: Seasoned Marshal declared as attacker end!\",debugLevel);\n\n// Seasoned Marshal declared as an attacker end");
        jTextArea14.setName("jTextArea14");
        jScrollPane17.setViewportView(jTextArea14);
        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane17, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE));
        jPanel19Layout.setVerticalGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane17, javax.swing.GroupLayout.DEFAULT_SIZE, 553, Short.MAX_VALUE));
        jTabbedPane4.addTab("S Marshal", jPanel19);
        jPanel20.setName("jPanel20");
        jScrollPane18.setName("jScrollPane18");
        jTextArea15.setColumns(20);
        jTextArea15.setFont(new java.awt.Font("Monospaced", 0, 11));
        jTextArea15.setRows(5);
        jTextArea15.setText("// Ebon Dragon played start\nint debugLevel = 3;\nD.addLog(\"4219: Played card start: \"+card,debugLevel);\nbRet = false;\n\nif (match.getHand(opponent).size()==0)\n{\n\tD.addLog(\"4219: No card on hand, nothing to put away...\",debugLevel);\n\tD.addLog(\"4219: Played card end. \", debugLevel);\n\tbRet=true;\n\treturn;\n}\n\nif (targetCard == null)\n{\n\tif ((selectYes == null) && (selectNo==null))\n\t{\n\t\tD.addLog(\"4219: Decided YES/NO\", debugLevel);\n\t\tmatch.askYesNo(player, card, ev);\n\t\treason=\"Decide yes/no\";\n\t\treturn;\n\t}\n\tif (selectNo==true)\n\t{\n\t\tD.addLog(\"4219: Decided not to take Ebon dragons ability\",debugLevel);\n\t\tD.addLog(\"4219: Played card end. \", debugLevel);\n\t\tbRet=true;\n\t\treturn;\n\t}\n\tD.addLog(\"4219: Decided to take Ebon dragons ability\",debugLevel);\n\tD.addLog(\"4219: Asking opponent to select a card.\",debugLevel);\n\tmatch.askSelectCardFromHand(opponent, card, ev);\n\tmatch.setPlayerCardMessage(opponent, \"\"+card+\" played, you must select a card from your hand to dispose to the graveyard.\");\n\treturn;\n}\nD.addLog(\"4219: Opponent selected a card to drop: \"+targetCard,debugLevel);\nL.addLog(\"Ebon Dragon forced \"+opponent+\" to drop a card: \"+targetCard);\nmatch.moveCardFromHandToGraveyard(opponent, targetCard);\nbRet = true;\nD.addLog(\"4219: Played card end. \", debugLevel);\n// Ebon Dragon played end\n");
        jTextArea15.setName("jTextArea15");
        jScrollPane18.setViewportView(jTextArea15);
        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane18, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE));
        jPanel20Layout.setVerticalGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane18, javax.swing.GroupLayout.DEFAULT_SIZE, 553, Short.MAX_VALUE));
        jTabbedPane4.addTab("E Dragon", jPanel20);
        jPanel21.setName("jPanel21");
        jScrollPane19.setName("jScrollPane19");
        jTextArea16.setColumns(20);
        jTextArea16.setFont(new java.awt.Font("Monospaced", 0, 11));
        jTextArea16.setRows(5);
        jTextArea16.setText("// Primeval Force played out start\n// could destroy 3 forests at once\n// the way I did it here was to work with \"globals...\"\n\nint debugLevel=3;\nbRet=false;\nD.addLog(\"4314: Enter card played: \"+card, debugLevel);\n\nString baseKey = match.getGlobalScriptBaseKey(thisScript, player, card);\n\nint counter = match.getIntData(baseKey+\"Counter\");\n\n// only check in first \"round\"\nif (counter == 0)\n{\n\tif (match.getLand(player).getSubListBySubType(\"Forest\").size()<3)\n\t{\n\t\tD.addLog(\"4314: You have not enough forests in play - Primeval Force destroyed!\", debugLevel);\n\t\tmatch.moveCardFromHandToGraveyard(player, card);\n\t\tD.addLog(\"4314: Card played end.\",debugLevel);\n\t\tbRet=true;\n\t\treturn;\n\t}\n}\n\nif (maybeTaken)\n{\n\tD.addLog(\"4314: Maybe taken, Primeval Force are destroyed\", debugLevel);\n\tL.addLog(\"Primeval Force played, forests were not payed, Primeval Force destroyed!\");\n\tmatch.moveCardFromHandToGraveyard(player, card);\n\tbRet=true;\n\treturn;\n}\n\n\nif (targetCard == null)\n{\n\tD.addLog(\"4314: No forest selected yet, asking for forest.\", debugLevel);\n\tmatch.askSelectFromOwnLand(player, card, ev);\n\tmatch.setPlayerCardMessage(player, \"Select forests to destroy!\" );\n\treason=\"select a forest from land\";\n\treturn;\n}\nD.addLog(\"4314: Target selected: \"+targetCard, debugLevel);\n\nif (!targetCard.getSubtype().equals(\"Forest\"))\n{\n\tD.addLog(\"4314: You must select a forest!\", debugLevel);\n\tmatch.askSelectFromOwnLand(player, card, ev);\n\tmatch.setPlayerCardMessage(player, \"Select forests to destroy!\" );\n\treason=\"must select forest!\";\n\treturn;\n}\n\nCard lastForest = (Card) match.getObjectData(baseKey+\"LastForest\");\nif (targetCard==lastForest)\n{\n\tD.addLog(\"4314: You must select different forest!\", debugLevel);\n\tmatch.askSelectFromOwnLand(player, card, ev);\n\tmatch.setPlayerCardMessage(player, \"Select forests to destroy!\" );\n\treason=\"must select forest!\";\n\treturn;\n}\nmatch.setObjectData(baseKey+\"LastForest\", targetCard);\ncounter++;\nmatch.setIntData(baseKey+\"Counter\", counter);\n\nD.addLog(\"4314: Destroying forest now!\", debugLevel);\nmatch.moveCardFromLandToGraveyard(player, targetCard);\nif (counter < 3)\n{\n\tD.addLog(\"4314: Do it again Sam - must be 3 forests, current count: \"+counter, debugLevel);\n\tmatch.askSelectFromOwnLand(player, card, ev);\n\tmatch.setPlayerCardMessage(player, \"Select forests to destroy!\" );\n\treason=\"must select forest!\";\n\treturn;\n}\n\nmatch.removeObjectData(baseKey+\"LastForest\");\nmatch.removeIntData(baseKey+\"Counter\");\n\nL.addLog(\"Primeval Force played, 3 forests were destroyed in the process!\");\nbRet=true;\nD.addLog(\"4314: Card played end.\",debugLevel);\n// Primeval Force played out end        \n");
        jTextArea16.setName("jTextArea16");
        jScrollPane19.setViewportView(jTextArea16);
        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane19, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE));
        jPanel21Layout.setVerticalGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane19, javax.swing.GroupLayout.DEFAULT_SIZE, 553, Short.MAX_VALUE));
        jTabbedPane4.addTab("P Force", jPanel21);
        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jTabbedPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 471, Short.MAX_VALUE));
        jPanel5Layout.setVerticalGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jTabbedPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 599, Short.MAX_VALUE));
        jTabbedPane1.addTab("Examples", jPanel5);
        jPanel2.setName("jPanel2");
        jButtonExecuteTest.setText("Test");
        jButtonExecuteTest.setName("jButtonExecuteTest");
        jButtonExecuteTest.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExecuteTestActionPerformed(evt);
            }
        });
        jScrollPane3.setName("jScrollPane3");
        jTextAreaReturn.setColumns(20);
        jTextAreaReturn.setEditable(false);
        jTextAreaReturn.setLineWrap(true);
        jTextAreaReturn.setRows(5);
        jTextAreaReturn.setName("jTextAreaReturn");
        jScrollPane3.setViewportView(jTextAreaReturn);
        jLabel6.setText("Return");
        jLabel6.setName("jLabel6");
        jComboBoxStatelessMatches.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxStatelessMatches.setName("jComboBoxStatelessMatches");
        jLabel3.setText("Match");
        jLabel3.setName("jLabel3");
        jLabel4.setText("Player");
        jLabel4.setName("jLabel4");
        jComboBoxPlayer.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0", "1" }));
        jComboBoxPlayer.setName("jComboBoxPlayer");
        jComboBoxPhase.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxPhase.setName("jComboBoxPhase");
        jLabel7.setText("Phase");
        jLabel7.setName("jLabel7");
        jComboBoxLittleScripts.setName("jComboBoxLittleScripts");
        jLabel9.setText("Add Manual");
        jLabel9.setName("jLabel9");
        jButton2.setText("...");
        jButton2.setMargin(new java.awt.Insets(1, 0, 1, 0));
        jButton2.setName("jButton2");
        jButton2.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jComboBoxDebugSettings.setEnabled(false);
        jComboBoxDebugSettings.setName("jComboBoxDebugSettings");
        jLabel10.setText("Debug settings");
        jLabel10.setName("jLabel10");
        jButton3.setText("Save");
        jButton3.setEnabled(false);
        jButton3.setName("jButton3");
        jLabel8.setText("Name");
        jLabel8.setName("jLabel8");
        jComboBoxTestMatch.setEnabled(false);
        jComboBoxTestMatch.setName("jComboBoxTestMatch");
        jTextFieldTestMatch.setEnabled(false);
        jTextFieldTestMatch.setName("jTextFieldTestMatch");
        jLabel15.setText("Name");
        jLabel15.setName("jLabel15");
        jButton5.setText("Load");
        jButton5.setEnabled(false);
        jButton5.setName("jButton5");
        jComboBox1.setEnabled(false);
        jComboBox1.setName("jComboBox1");
        jCheckBoxTryAsCom.setText("Try as Communication");
        jCheckBoxTryAsCom.setName("jCheckBoxTryAsCom");
        jButtonSTOPP.setText("X");
        jButtonSTOPP.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonSTOPP.setName("jButtonSTOPP");
        jButtonSTOPP.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSTOPPActionPerformed(evt);
            }
        });
        jCheckBoxMy.setSelected(true);
        jCheckBoxMy.setText("My");
        jCheckBoxMy.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        jCheckBoxMy.setName("jCheckBoxMy");
        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel3).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel4).addComponent(jLabel7))).addGap(18, 18, 18).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jComboBoxStatelessMatches, 0, 395, Short.MAX_VALUE).addGroup(jPanel2Layout.createSequentialGroup().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jComboBoxPlayer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jCheckBoxMy)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jComboBoxPhase, javax.swing.GroupLayout.Alignment.TRAILING, 0, 329, Short.MAX_VALUE).addComponent(jComboBox1, 0, 329, Short.MAX_VALUE))))).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup().addContainerGap().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup().addComponent(jLabel10).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 354, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup().addComponent(jLabel15).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jButton3)).addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup().addComponent(jLabel9).addGap(18, 18, 18).addComponent(jButton2)).addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup().addComponent(jLabel8).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jButton5))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jComboBoxTestMatch, 0, 274, Short.MAX_VALUE).addComponent(jComboBoxDebugSettings, javax.swing.GroupLayout.Alignment.TRAILING, 0, 274, Short.MAX_VALUE).addComponent(jComboBoxLittleScripts, 0, 274, Short.MAX_VALUE).addComponent(jTextFieldTestMatch, javax.swing.GroupLayout.DEFAULT_SIZE, 274, Short.MAX_VALUE)))).addGap(1, 1, 1)).addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 471, Short.MAX_VALUE).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup().addContainerGap().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel6).addGroup(jPanel2Layout.createSequentialGroup().addComponent(jButtonSTOPP).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 121, Short.MAX_VALUE).addComponent(jCheckBoxTryAsCom).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButtonExecuteTest)))));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jComboBoxStatelessMatches, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel3)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel4).addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jComboBoxPlayer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jComboBoxPhase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel7).addComponent(jCheckBoxMy)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jComboBoxLittleScripts, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel9).addComponent(jButton2)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel10).addComponent(jComboBoxDebugSettings, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel8).addComponent(jComboBoxTestMatch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jButton5)).addGap(8, 8, 8).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel15).addComponent(jButton3).addComponent(jTextFieldTestMatch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jButtonExecuteTest).addComponent(jCheckBoxTryAsCom)).addGroup(jPanel2Layout.createSequentialGroup().addComponent(jLabel6).addGap(1, 1, 1).addComponent(jButtonSTOPP))).addGap(18, 18, 18).addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE)));
        jTabbedPane1.addTab("Testing", jPanel2);
        jTabbedPane5.setName("jTabbedPane5");
        jPanel22.setName("jPanel22");
        jScrollPane21.setName("jScrollPane21");
        jTextAreaDebug.setColumns(20);
        jTextAreaDebug.setFont(new java.awt.Font("Monospaced", 0, 11));
        jTextAreaDebug.setRows(5);
        jTextAreaDebug.setName("jTextAreaDebug");
        jScrollPane21.setViewportView(jTextAreaDebug);
        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane21, javax.swing.GroupLayout.DEFAULT_SIZE, 467, Short.MAX_VALUE));
        jPanel22Layout.setVerticalGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane21, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE));
        jTabbedPane5.addTab("Debug", jPanel22);
        jPanel23.setName("jPanel23");
        jScrollPane20.setName("jScrollPane20");
        jTextAreaLog.setColumns(20);
        jTextAreaLog.setFont(new java.awt.Font("Monospaced", 0, 11));
        jTextAreaLog.setRows(5);
        jTextAreaLog.setName("jTextAreaLog");
        jScrollPane20.setViewportView(jTextAreaLog);
        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane20, javax.swing.GroupLayout.DEFAULT_SIZE, 467, Short.MAX_VALUE));
        jPanel23Layout.setVerticalGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane20, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE));
        jTabbedPane5.addTab("Log", jPanel23);
        jPanel24.setName("jPanel24");
        jPanel25.setBorder(javax.swing.BorderFactory.createTitledBorder("Debug"));
        jPanel25.setName("jPanel25");
        jLabel11.setText("Debug level");
        jLabel11.setName("jLabel11");
        jTextFieldDebugLevel.setName("jTextFieldDebugLevel");
        jCheckBoxDebugWindow.setText("Debug in Window");
        jCheckBoxDebugWindow.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        jCheckBoxDebugWindow.setName("jCheckBoxDebugWindow");
        jButtonClearDebug.setText("Clear Debug log");
        jButtonClearDebug.setName("jButtonClearDebug");
        jButtonClearDebug.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonClearDebugActionPerformed(evt);
            }
        });
        jCheckBoxDebugTiming.setText("Add Timing Info");
        jCheckBoxDebugTiming.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        jCheckBoxDebugTiming.setName("jCheckBoxDebugTiming");
        jTextFieldFiles.setName("jTextFieldFiles");
        jTextFieldClasses.setName("jTextFieldClasses");
        jTextFieldMethods.setName("jTextFieldMethods");
        jLabel12.setText("Files");
        jLabel12.setName("jLabel12");
        jLabel13.setText("Classes");
        jLabel13.setName("jLabel13");
        jLabel14.setText("Methods");
        jLabel14.setName("jLabel14");
        jCheckBoxDebugOff.setText("Debug Off");
        jCheckBoxDebugOff.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        jCheckBoxDebugOff.setName("jCheckBoxDebugOff");
        jButton1.setText("Apply");
        jButton1.setToolTipText("Apply onl, settings not saved!");
        jButton1.setName("jButton1");
        jButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel25Layout.createSequentialGroup().addContainerGap().addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel25Layout.createSequentialGroup().addComponent(jLabel11).addGap(12, 12, 12).addComponent(jTextFieldDebugLevel, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE)).addGroup(jPanel25Layout.createSequentialGroup().addGap(10, 10, 10).addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jButton1).addComponent(jCheckBoxDebugOff)))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel12).addComponent(jLabel13).addComponent(jLabel14)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jTextFieldClasses, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE).addComponent(jTextFieldFiles, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE).addComponent(jTextFieldMethods, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jButtonClearDebug, javax.swing.GroupLayout.Alignment.TRAILING).addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jCheckBoxDebugTiming, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jCheckBoxDebugWindow, javax.swing.GroupLayout.Alignment.TRAILING)))));
        jPanel25Layout.setVerticalGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel25Layout.createSequentialGroup().addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel25Layout.createSequentialGroup().addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel11).addComponent(jTextFieldDebugLevel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jCheckBoxDebugOff).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jButton1)).addGroup(jPanel25Layout.createSequentialGroup().addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false).addComponent(jTextFieldFiles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel12)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false).addComponent(jTextFieldClasses, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel13)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false).addComponent(jTextFieldMethods, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel14))).addGroup(jPanel25Layout.createSequentialGroup().addComponent(jCheckBoxDebugTiming).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jCheckBoxDebugWindow).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButtonClearDebug))).addContainerGap()));
        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 467, Short.MAX_VALUE).addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jPanel24Layout.setVerticalGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 133, Short.MAX_VALUE).addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel24Layout.createSequentialGroup().addComponent(jPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE).addContainerGap())));
        jTabbedPane5.addTab("Debug settings", jPanel24);
        jPanel1.setName("jPanel1");
        jPanel1.setPreferredSize(new java.awt.Dimension(674, 100));
        jScrollPane4.setName("jScrollPane4");
        jTextPaneScript.setName("jTextPaneScript");
        jTextPaneScript.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextPaneScriptFocusLost(evt);
            }
        });
        jScrollPane4.setViewportView(jTextPaneScript);
        jLabel5.setText("Script");
        jLabel5.setName("jLabel5");
        jScrollPane1.setName("jScrollPane1");
        jTextAreaSitComment.setColumns(20);
        jTextAreaSitComment.setLineWrap(true);
        jTextAreaSitComment.setRows(5);
        jTextAreaSitComment.setName("jTextAreaSitComment");
        jScrollPane1.setViewportView(jTextAreaSitComment);
        jLabel2.setText("Coment");
        jLabel2.setName("jLabel2");
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 468, Short.MAX_VALUE).addGroup(jPanel1Layout.createSequentialGroup().addComponent(jLabel2).addContainerGap(419, Short.MAX_VALUE)).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 468, Short.MAX_VALUE).addGroup(jPanel1Layout.createSequentialGroup().addComponent(jLabel5).addContainerGap(432, Short.MAX_VALUE)))));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addComponent(jLabel2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel5).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)));
        jPanel26.setName("jPanel26");
        jLabel1.setText("Situation Key");
        jLabel1.setName("jLabel1");
        jComboBoxSituationKey.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxSituationKey.setName("jComboBoxSituationKey");
        jComboBoxSituationKey.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxSituationKeyActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel26Layout.createSequentialGroup().addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jComboBoxSituationKey, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(134, Short.MAX_VALUE)));
        jPanel26Layout.setVerticalGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel26Layout.createSequentialGroup().addGap(9, 9, 9).addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1).addComponent(jComboBoxSituationKey, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jTabbedPane5, 0, 0, Short.MAX_VALUE).addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 488, Short.MAX_VALUE).addComponent(jPanel26, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 492, Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 645, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addComponent(jPanel26, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTabbedPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)));
    }

    private boolean aiTesting = false;

    private String aiName = "";

    public void setAITesting(String name) {
        aiName = name;
        aiTesting = true;
    }

    private Match mMatch = null;

    private LogListener mLogListener = null;

    private void jButtonExecuteTestActionPerformed(java.awt.event.ActionEvent evt) {
        jButtonSTOPPActionPerformed(null);
        mMatch = null;
        if (jComboBoxStatelessMatches.getSelectedItem() == null) return;
        if (jComboBoxPhase.getSelectedIndex() == -1) return;
        if (jComboBoxPlayer.getSelectedIndex() == -1) return;
        if (jComboBoxSituationKey.getSelectedIndex() == -1) return;
        if (aiTesting) {
            testAI();
            return;
        }
        if (mCard == null) return;
        TestMatch match = new TestMatch();
        MatchStartOptions options = new MatchStartOptions();
        CardDeck.mDummyLoadingActive = true;
        options.setPlayer1(new MatchComputerPlayer(new AIPlayer("SimpleAI")));
        options.setPlayer2(new MatchComputerPlayer(new AIPlayer("SimpleAI")));
        CardDeck.mDummyLoadingActive = false;
        Match.loadMatchUnstated(jComboBoxStatelessMatches.getSelectedItem().toString(), null, match, options);
        if (!match.didMatchStart()) {
            jTextAreaReturn.setText("Match did not start :-(");
            return;
        }
        match.setPhase(jComboBoxPhase.getSelectedIndex());
        int turn = jComboBoxPlayer.getSelectedIndex();
        match.setPlayerTurn(turn, jCheckBoxMy.isSelected());
        match.addCardToPlayersHand(jComboBoxPlayer.getSelectedIndex(), mCard);
        if (mCard.isCreature()) match.addCardToPlayersCreature(jComboBoxPlayer.getSelectedIndex(), mCard);
        jButtonClearDebugActionPerformed(null);
        String ss = "";
        if (jComboBoxLittleScripts.getSelectedIndex() != -1) {
            NamedScriptDataPool namedScriptDataPool = new NamedScriptDataPool();
            String key = jComboBoxLittleScripts.getSelectedItem().toString();
            NamedScriptData namedScriptData = namedScriptDataPool.get(key);
            String script = namedScriptData.getScript();
            Configuration.getConfiguration().getDebugEntity().addLog("SmallScript start:");
            CardShellEnvironment ev = new CardShellEnvironment(script, match, mCard);
            ev.execute();
            Configuration.getConfiguration().getDebugEntity().addLog("SmallScript end:");
            ss += "SmallScript start:\n";
            ss += ev.iRet + "" + ev;
            ss += "SmallScript end:\n-------\n";
        }
        if (!jCheckBoxTryAsCom.isSelected()) {
            CardShellEnvironment ev = new CardShellEnvironment(match, mCard, (String) jComboBoxSituationKey.getSelectedItem());
            ev.execute();
            jTextAreaLog.setText(Configuration.getConfiguration().getLogEntity().getLog());
            jTextAreaDebug.setText(Configuration.getConfiguration().getDebugEntity().getLog());
            jTextAreaReturn.setText(ss + ev.iRet + "" + ev);
            return;
        }
        if (((String) jComboBoxSituationKey.getSelectedItem()).equals(CardSituation.CARD_PLAYED_KEY)) {
            mMatch = match;
            jTextAreaReturn.setText("");
            mLogListener = new LogListener() {

                public void logChanged() {
                }

                public void logAddedChanged(String text) {
                    jTextAreaDebug.setText(jTextAreaDebug.getText() + text + "\n");
                }
            };
            Configuration.getConfiguration().getDebugEntity().addLogListener(mLogListener);
            match.executeCardPlayed(mCard);
        }
    }

    private void jComboBoxSituationKeyActionPerformed(java.awt.event.ActionEvent evt) {
        jTextAreaReturn.setText("");
        if (jComboBoxSituationKey.getSelectedIndex() == -1) {
            jTextAreaSitComment.setText("");
            jTextPaneScript.setText("");
        } else {
            String key = (String) jComboBoxSituationKey.getSelectedItem();
            jTextAreaSitComment.setText(mProvider.getSituations().getComment(key));
            jTextAreaSitComment.setCaretPosition(0);
            mScriptData = mScriptDataPool.get(Card.buildTemplateKey(key, mTemplateType, mTemplateAbility));
            if (mScriptData == null) {
                mScriptData = new ScriptData();
                mScriptData.mName = Card.buildTemplateKey(key, mTemplateType, mTemplateAbility);
                mScriptData.mClass = "Script";
                mScriptDataPool.put(mScriptData);
            }
            jTextPaneScript.setText(mScriptData.mScript);
        }
    }

    private void jTextPaneScriptFocusLost(java.awt.event.FocusEvent evt) {
        if (jComboBoxSituationKey.getSelectedIndex() != -1) {
            String code = jTextPaneScript.getText();
            mScriptData.mScript = code;
        }
    }

    private void jButtonClearDebugActionPerformed(java.awt.event.ActionEvent evt) {
        Configuration C = Configuration.getConfiguration();
        C.clearDebugLog();
        jTextAreaDebug.setText(Configuration.getConfiguration().getDebugEntity().getLog());
    }

    private void jButtonSTOPPActionPerformed(java.awt.event.ActionEvent evt) {
        Configuration.getConfiguration().getDebugEntity().removeLogListener(mLogListener);
        jTextAreaLog.setText(Configuration.getConfiguration().getLogEntity().getLog());
        mLogListener = null;
        if (mMatch == null) return;
        mMatch.quitMatch();
        mMatch = null;
    }

    private void refreshScriptCombo() {
        mClassSetting++;
        NamedScriptDataPool mNamedScriptDataPool = new NamedScriptDataPool();
        Collection<String> collectionKlasse = mNamedScriptDataPool.getKlassenHashMap().values();
        Iterator<String> iterKlasse = collectionKlasse.iterator();
        int i = 0;
        String klasse = "";
        Collection<NamedScriptData> colC = mNamedScriptDataPool.getHashMap().values();
        Iterator<NamedScriptData> iterC = colC.iterator();
        jComboBoxLittleScripts.removeAllItems();
        i = 0;
        while (iterC.hasNext()) {
            NamedScriptData item = iterC.next();
            jComboBoxLittleScripts.addItem(item.mName);
            i++;
        }
        jComboBoxLittleScripts.setSelectedIndex(-1);
        mClassSetting--;
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        final JPortalInternalFrame frame = new JPortalInternalFrame();
        NamedScriptDataPanel p1 = new NamedScriptDataPanel();
        frame.add(p1);
        frame.setTitle("Scripty, the small script window!");
        frame.setBounds(200, 200, 600, 300);
        mMainParent.addInternalFrame(frame);
        frame.setVisible(true);
        final javax.swing.event.InternalFrameAdapter l = new javax.swing.event.InternalFrameAdapter() {

            javax.swing.event.InternalFrameAdapter ll = this;

            @Override
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent e) {
                refreshScriptCombo();
                frame.removeInternalFrameListener(ll);
            }
        };
        frame.addInternalFrameListener(l);
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        Configuration C = Configuration.getConfiguration();
        Integer I = 100;
        try {
            C.setDebugLevel(Integer.parseInt(jTextFieldDebugLevel.getText()));
        } catch (Throwable e) {
        }
        C.setDebugFiles(jTextFieldFiles.getText());
        C.setDebugClasses(jTextFieldClasses.getText());
        C.setDebugMethods(jTextFieldMethods.getText());
        C.setDebugTiming(jCheckBoxDebugTiming.isSelected());
        C.setDebugOff(jCheckBoxDebugOff.isSelected());
    }

    private javax.swing.JButton jButton1;

    private javax.swing.JButton jButton2;

    private javax.swing.JButton jButton3;

    private javax.swing.JButton jButton5;

    private javax.swing.JButton jButtonClearDebug;

    private javax.swing.JButton jButtonExecuteTest;

    private javax.swing.JButton jButtonSTOPP;

    private javax.swing.JCheckBox jCheckBoxDebugOff;

    private javax.swing.JCheckBox jCheckBoxDebugTiming;

    private javax.swing.JCheckBox jCheckBoxDebugWindow;

    private javax.swing.JCheckBox jCheckBoxMy;

    private javax.swing.JCheckBox jCheckBoxTryAsCom;

    private javax.swing.JComboBox jComboBox1;

    private javax.swing.JComboBox jComboBoxDebugSettings;

    private javax.swing.JComboBox jComboBoxLittleScripts;

    private javax.swing.JComboBox jComboBoxPhase;

    private javax.swing.JComboBox jComboBoxPlayer;

    private javax.swing.JComboBox jComboBoxSituationKey;

    private javax.swing.JComboBox jComboBoxStatelessMatches;

    private javax.swing.JComboBox jComboBoxTestMatch;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel10;

    private javax.swing.JLabel jLabel11;

    private javax.swing.JLabel jLabel12;

    private javax.swing.JLabel jLabel13;

    private javax.swing.JLabel jLabel14;

    private javax.swing.JLabel jLabel15;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JLabel jLabel8;

    private javax.swing.JLabel jLabel9;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel10;

    private javax.swing.JPanel jPanel11;

    private javax.swing.JPanel jPanel12;

    private javax.swing.JPanel jPanel13;

    private javax.swing.JPanel jPanel14;

    private javax.swing.JPanel jPanel15;

    private javax.swing.JPanel jPanel16;

    private javax.swing.JPanel jPanel17;

    private javax.swing.JPanel jPanel18;

    private javax.swing.JPanel jPanel19;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel jPanel20;

    private javax.swing.JPanel jPanel21;

    private javax.swing.JPanel jPanel22;

    private javax.swing.JPanel jPanel23;

    private javax.swing.JPanel jPanel24;

    private javax.swing.JPanel jPanel25;

    private javax.swing.JPanel jPanel26;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JPanel jPanel4;

    private javax.swing.JPanel jPanel5;

    private javax.swing.JPanel jPanel6;

    private javax.swing.JPanel jPanel7;

    private javax.swing.JPanel jPanel8;

    private javax.swing.JPanel jPanel9;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JScrollPane jScrollPane10;

    private javax.swing.JScrollPane jScrollPane11;

    private javax.swing.JScrollPane jScrollPane12;

    private javax.swing.JScrollPane jScrollPane13;

    private javax.swing.JScrollPane jScrollPane14;

    private javax.swing.JScrollPane jScrollPane15;

    private javax.swing.JScrollPane jScrollPane16;

    private javax.swing.JScrollPane jScrollPane17;

    private javax.swing.JScrollPane jScrollPane18;

    private javax.swing.JScrollPane jScrollPane19;

    private javax.swing.JScrollPane jScrollPane2;

    private javax.swing.JScrollPane jScrollPane20;

    private javax.swing.JScrollPane jScrollPane21;

    private javax.swing.JScrollPane jScrollPane3;

    private javax.swing.JScrollPane jScrollPane4;

    private javax.swing.JScrollPane jScrollPane5;

    private javax.swing.JScrollPane jScrollPane6;

    private javax.swing.JScrollPane jScrollPane7;

    private javax.swing.JScrollPane jScrollPane8;

    private javax.swing.JScrollPane jScrollPane9;

    private javax.swing.JTabbedPane jTabbedPane1;

    private javax.swing.JTabbedPane jTabbedPane2;

    private javax.swing.JTabbedPane jTabbedPane3;

    private javax.swing.JTabbedPane jTabbedPane4;

    private javax.swing.JTabbedPane jTabbedPane5;

    private javax.swing.JTextArea jTextArea1;

    private javax.swing.JTextArea jTextArea10;

    private javax.swing.JTextArea jTextArea11;

    private javax.swing.JTextArea jTextArea12;

    private javax.swing.JTextArea jTextArea13;

    private javax.swing.JTextArea jTextArea14;

    private javax.swing.JTextArea jTextArea15;

    private javax.swing.JTextArea jTextArea16;

    private javax.swing.JTextArea jTextArea2;

    private javax.swing.JTextArea jTextArea3;

    private javax.swing.JTextArea jTextArea4;

    private javax.swing.JTextArea jTextArea5;

    private javax.swing.JTextArea jTextArea6;

    private javax.swing.JTextArea jTextArea7;

    private javax.swing.JTextArea jTextArea8;

    private javax.swing.JTextArea jTextArea9;

    private javax.swing.JTextArea jTextAreaDebug;

    private javax.swing.JTextArea jTextAreaLog;

    private javax.swing.JTextArea jTextAreaReturn;

    private javax.swing.JTextArea jTextAreaSitComment;

    private javax.swing.JTextField jTextFieldClasses;

    private javax.swing.JTextField jTextFieldDebugLevel;

    private javax.swing.JTextField jTextFieldFiles;

    private javax.swing.JTextField jTextFieldMethods;

    private javax.swing.JTextField jTextFieldTestMatch;

    private javax.swing.JTextPane jTextPaneScript;

    public void setScripter(Scriptable provider) {
        mProvider = provider;
        mScriptDataPool = new ScriptDataPool(provider.getScriptPoolBaseName() + ".xml");
        resetConfigPool(false, "");
    }

    public void setTemplateType(String ttype) {
        mTemplateType = ttype;
    }

    public void setTemplateAbility(String tab) {
        mTemplateAbility = tab;
    }

    public String getSituationKey() {
        if (jComboBoxSituationKey.getSelectedIndex() == -1) return "";
        return (String) jComboBoxSituationKey.getSelectedItem();
    }

    public void setScript(String s) {
        jTextPaneScript.setText(s);
    }

    private void testAI() {
        jButtonSTOPPActionPerformed(null);
        mMatch = null;
        TestMatch match = new TestMatch();
        MatchStartOptions options = new MatchStartOptions();
        CardDeck.mDummyLoadingActive = true;
        AIPlayer aiPlayer = new AIPlayer(aiName);
        MatchComputerPlayer player = new MatchComputerPlayer(aiPlayer);
        options.setPlayer1(new MatchComputerPlayer(new AIPlayer("SimpleAI")));
        options.setPlayer2(new MatchComputerPlayer(new AIPlayer("SimpleAI")));
        if (jComboBoxPlayer.getSelectedIndex() == 0) {
            options.setPlayer1(player);
            options.setPlayer2(new MatchComputerPlayer(new AIPlayer("SimpleAI")));
        } else {
            options.setPlayer1(new MatchComputerPlayer(new AIPlayer("SimpleAI")));
            options.setPlayer2(player);
        }
        CardDeck.mDummyLoadingActive = false;
        Match.loadMatchUnstated(jComboBoxStatelessMatches.getSelectedItem().toString(), null, match, options);
        if (!match.didMatchStart()) {
            jTextAreaReturn.setText("Match did not start :-(");
            return;
        }
        match.setPhase(jComboBoxPhase.getSelectedIndex());
        int turn = jComboBoxPlayer.getSelectedIndex();
        match.setPlayerTurn(turn, jCheckBoxMy.isSelected());
        jButtonClearDebugActionPerformed(null);
        String ss = "";
        if (jComboBoxLittleScripts.getSelectedIndex() != -1) {
            NamedScriptDataPool namedScriptDataPool = new NamedScriptDataPool();
            String key = jComboBoxLittleScripts.getSelectedItem().toString();
            NamedScriptData namedScriptData = namedScriptDataPool.get(key);
            String script = namedScriptData.getScript();
            Configuration.getConfiguration().getDebugEntity().addLog("SmallScript start:");
            CardShellEnvironment ev = new CardShellEnvironment(script, match, mCard);
            ev.execute();
            Configuration.getConfiguration().getDebugEntity().addLog("SmallScript end:");
            ss += "SmallScript start:\n";
            ss += ev.iRet + "" + ev;
            ss += "SmallScript end:\n-------\n";
        }
        if (!jCheckBoxTryAsCom.isSelected()) {
            AIPlayerShellEnvironment ev = new AIPlayerShellEnvironment(match, player, (String) jComboBoxSituationKey.getSelectedItem());
            ev.execute();
            jTextAreaLog.setText(Configuration.getConfiguration().getLogEntity().getLog());
            jTextAreaDebug.setText(Configuration.getConfiguration().getDebugEntity().getLog());
            jTextAreaReturn.setText(ss + ev.iRet + "" + ev);
            return;
        }
        {
            mMatch = match;
            jTextAreaReturn.setText("");
            mLogListener = new LogListener() {

                public void logChanged() {
                }

                public void logAddedChanged(String text) {
                    jTextAreaDebug.setText(jTextAreaDebug.getText() + text + "\n");
                }
            };
            Configuration.getConfiguration().getDebugEntity().addLogListener(mLogListener);
            match.startMatch();
        }
    }
}
