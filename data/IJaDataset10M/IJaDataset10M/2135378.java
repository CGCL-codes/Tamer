package de.fzi.injectj.frontend.swing;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;
import de.fzi.injectj.backend.GlobalSettings;
import de.fzi.injectj.backend.ScriptNameExecOrderVector;
import de.fzi.injectj.language.CodeMapper;

/** Panel to choose the execution order of parsed scripts. If user hits the
  * "Next"-Button, the dependencies between scripts are checked.
  *
  *  The Panel looks like this:<br>
  *  <img src="doc-files/PnlScriptOrder.jpg">
  *
  * @author Volker Kuttruff
  * @author Sven Luzar*/
public class PnlScriptOrder extends PnlInjectJ {

    /** Reference to the Console Window*/
    FrmMain mainWindow;

    /** List shows the ScriptOrder*/
    PnlListSelection listSelectionPanel = new PnlListSelection(true, true, false);

    /** Panel which contains the ComboBox to select
   *  Scripts*/
    PnlComboBox comboBoxPanel = new PnlComboBox();

    /** Buttonpanel for the next, previous, finish, cancel Buttons*/
    PnlButtons buttonPanel;

    /** Contains ScriptNames as String values
   *  in the right order to process*/
    ScriptNameExecOrderVector execOrder = null;

    /** Contains all available ScriptNames
   *  as String
   */
    Vector allScriptNames = new Vector();

    /** Flag for Can Skip State
   *
   */
    private boolean canSkip = false;

    /** Is called from the Backend if a new Project is created
   *
   */
    public void projectChanged() {
    }

    /** Creates a new Instance
   *
   *  @param mainWindow a Reference to the Console Window
   *
   */
    public PnlScriptOrder(FrmMain mainWindow) {
        try {
            this.mainWindow = mainWindow;
            buttonPanel = new PnlButtons(mainWindow);
            execOrder = null;
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /** Initialises the Swing Components
   *
   */
    private void jbInit() throws Exception {
        this.setLayout(new BorderLayout(10, 10));
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        buttonPanel.prevButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                prevButton_actionPerformed(e);
            }
        });
        buttonPanel.nextButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                nextButton_actionPerformed(e);
            }
        });
        comboBoxPanel.label.setText("Please choose script to add to execution order");
        comboBoxPanel.addButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                addScriptButton_actionPerformed(e);
            }
        });
        listSelectionPanel.label.setText("Script execution order");
        listSelectionPanel.list.addKeyListener(new java.awt.event.KeyListener() {

            public void keyPressed(KeyEvent e) {
            }

            public void keyTyped(KeyEvent e) {
            }

            public void keyReleased(KeyEvent e) {
                if (e.getKeyChar() == '+') {
                    downButton_actionPerformed(null);
                }
                if (e.getKeyChar() == '-') {
                    upButton_actionPerformed(null);
                }
            }
        });
        listSelectionPanel.upButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                upButton_actionPerformed(e);
            }
        });
        listSelectionPanel.downButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                downButton_actionPerformed(e);
            }
        });
        listSelectionPanel.removeButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                removeButton_actionPerformed(e);
            }
        });
        if (GlobalSettings.isFlat()) panel.setBorder(BorderFactory.createEtchedBorder()); else panel.setBorder(BorderFactory.createRaisedBevelBorder());
        panel.add(comboBoxPanel, BorderLayout.SOUTH);
        panel.add(listSelectionPanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);
        this.add(panel, BorderLayout.CENTER);
        updateLabels();
    }

    /** Updates labels of all buttons */
    protected void updateLabels() {
        listSelectionPanel.label.setText(CodeMapper.getText("ORDERLIST_LABEL"));
        comboBoxPanel.label.setText(CodeMapper.getText("ADDSCRIPTTOORDER_LABEL"));
        buttonPanel.updateLabels();
        comboBoxPanel.updateLabels();
        listSelectionPanel.updateLabels();
    }

    /** Sets the Title of the Console Window
   *
   */
    public void setWindowTitle() {
        if (mainWindow != null) mainWindow.setTitle(CodeMapper.getText("SCRIPTEXECORDER_TITLE"));
    }

    /** Updates the List and the ComboBox.
   *  If only one Script is available,
   *  it returns true and
   *  you don't need to select the Script.
   *  Otherwise false.
   */
    public boolean panelActivated(boolean fromPreviousPanel) {
        setWindowTitle();
        if (fromPreviousPanel) {
            execOrder = mainWindow.getProject().getScriptManager().getScriptNameExecOrder();
            listSelectionPanel.list.setListData(execOrder);
            DefaultComboBoxModel oMCBM = new DefaultComboBoxModel(mainWindow.getProject().getScriptManager().getAllScriptNamesWithoutInterfaces());
            comboBoxPanel.comboBox.setModel(oMCBM);
            int iSize = comboBoxPanel.comboBox.getModel().getSize();
            if (iSize <= 1) {
                for (int i = 0; i < iSize; i++) {
                    String scriptName = (String) comboBoxPanel.comboBox.getModel().getElementAt(i);
                    if (!execOrder.contains(scriptName)) execOrder.addElement(scriptName);
                }
                canSkip = true;
            } else canSkip = false;
        }
        checkNextButton();
        return false;
    }

    /** Checks the count of the script files and returns false
   *
   */
    public boolean panelDeactivated(boolean toNextPanel) {
        execOrder.check();
        return false;
    }

    /** Calls mainWindow.switchToPrevious()
   *
   *  @see FrmMain#switchToPrevious()
   */
    void prevButton_actionPerformed(ActionEvent e) {
        mainWindow.switchToPrevious();
    }

    /** Calls <code>mainWindow.switchToNext()</code> and
   *  calls <code>checkScriptDependencies()</code>
   *
   *  @see FrmMain#switchToNext()
   *  @see ScriptNameExecOrderVector#checkScriptDependencies()
   */
    void nextButton_actionPerformed(ActionEvent e) {
        if (!execOrder.check()) return;
        mainWindow.switchToNext();
    }

    /** Moves the selected Scripts up in the Vector
   *  and updates the GUI
   *
   */
    void upButton_actionPerformed(ActionEvent e) {
        int[] toBeSelected = execOrder.moveUp(listSelectionPanel.list.getSelectedIndices());
        listSelectionPanel.list.setListData(execOrder);
        listSelectionPanel.list.setSelectedIndices(toBeSelected);
    }

    /** Moves the selected Scripts down in the Vector
   *  and updates the GUI
   *
   */
    void downButton_actionPerformed(ActionEvent e) {
        int[] toBeSelected = execOrder.moveDown(listSelectionPanel.list.getSelectedIndices());
        listSelectionPanel.list.setListData(execOrder);
        listSelectionPanel.list.setSelectedIndices(toBeSelected);
    }

    /** Removes the selected Script from the Vector
   *
   */
    void removeButton_actionPerformed(ActionEvent e) {
        execOrder.remove(listSelectionPanel.list.getSelectedValues());
        listSelectionPanel.list.setListData(execOrder);
    }

    /** Adds the Selected Script to the execOrder Vector
   *
   */
    void addScriptButton_actionPerformed(ActionEvent e) {
        Object selectedItem = comboBoxPanel.comboBox.getSelectedItem();
        if (selectedItem == null) return;
        String scriptName = selectedItem.toString();
        execOrder.addElement(scriptName);
        listSelectionPanel.list.setListData(execOrder);
        checkNextButton();
    }

    /** returns true if Skip is allowed
   *
   */
    public boolean canSkipPanel() {
        return canSkip;
    }

    /** returns the JavaHelp Pagename
   *
   */
    public String helpFilename() {
        return "scriptorder.html";
    }

    /** Sets the nextButton enable or not
   *
   */
    private void checkNextButton() {
        if (execOrder == null || execOrder.isEmpty()) {
            buttonPanel.nextButton.setEnabled(false);
        } else {
            buttonPanel.nextButton.setEnabled(true);
        }
    }
}
