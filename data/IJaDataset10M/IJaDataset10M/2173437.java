package de.fzi.injectj.frontend.swing;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import de.fzi.injectj.backend.GlobalSettings;
import de.fzi.injectj.backend.ScriptFileNamesVector;
import de.fzi.injectj.language.CodeMapper;

/** Panel to choose script source files. If the user hits the "Next"-button, the
  * script source files will be parsed.
  *
  *  The Panel looks like this:<br>
  *  <img src="doc-files/PnlScriptChoose.jpg">
  *
  *
  * @author Volker Kuttruff
  * @author Sven Luzar*/
public class PnlScriptChoose extends PnlInjectJ {

    private String defaultPath = null;

    private boolean veto;

    /** Reference to the Console Window*/
    protected FrmMain mainWindow;

    /** Buttonpanel for the next, previous, finish, cancel Buttons*/
    PnlButtons buttonPanel;

    /** TextField for the Script input box and the label*/
    PnlTextField textFieldPanel = new PnlTextField(GlobalSettings.getRecentScriptFiles());

    /** List Panel to show the added Scripts*/
    PnlListSelection listSelectionPanel = new PnlListSelection(true, true, false);

    /** Vector with the ScriptSources*/
    ScriptFileNamesVector scriptSources = null;

    /** Backend sends an information that a new Project was created
   *
   */
    public void projectChanged() {
        textFieldPanel.textField.setSelectedItem("");
    }

    /** Creates a new instance and cats the FrmMain with this Panel
   *
   */
    public PnlScriptChoose(FrmMain mainWindow) {
        try {
            this.mainWindow = mainWindow;
            buttonPanel = new PnlButtons(mainWindow);
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /** Sets the FrmMain
   *
   */
    public void setMainWindow(FrmMain mainWindow) {
        this.mainWindow = mainWindow;
        buttonPanel.setMainWindow(mainWindow);
    }

    /** Initialises the Swing Components
   *
   */
    private void jbInit() throws Exception {
        this.setLayout(new BorderLayout(10, 10));
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        textFieldPanel.label.setText("Please enter name of next script source file");
        textFieldPanel.textField.setEditable(true);
        textFieldPanel.textField.addKeyListener(new java.awt.event.KeyListener() {

            public void keyPressed(KeyEvent e) {
            }

            public void keyTyped(KeyEvent e) {
            }

            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    addScriptSourceButton_actionPerformed(null);
                }
            }
        });
        textFieldPanel.textField.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                textField_actionPerformed(e);
            }
        });
        textFieldPanel.chooseButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                chooseScriptSourceButton_actionPerformed(e);
            }
        });
        textFieldPanel.addButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                addScriptSourceButton_actionPerformed(e);
            }
        });
        listSelectionPanel.label.setText("Script source files");
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
        if (GlobalSettings.isFlat()) panel.setBorder(BorderFactory.createEtchedBorder()); else panel.setBorder(BorderFactory.createRaisedBevelBorder());
        panel.add(textFieldPanel, BorderLayout.SOUTH);
        panel.add(listSelectionPanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);
        this.add(panel, BorderLayout.CENTER);
        updateLabels();
    }

    /** Updates labels of all buttons */
    protected void updateLabels() {
        listSelectionPanel.label.setText(CodeMapper.getText("SCRIPTLIST_LABEL"));
        textFieldPanel.label.setText(CodeMapper.getText("SCRIPTTEXTFIELD_LABEL"));
        buttonPanel.updateLabels();
        textFieldPanel.updateLabels();
        listSelectionPanel.updateLabels();
    }

    /** Sets the windowTitle at the FrmMain
   *
   */
    public void setWindowTitle() {
        if (mainWindow != null) mainWindow.setTitle(CodeMapper.getText("SCRIPTCHOOSE_TITLE"));
    }

    public boolean panelActivated(boolean fromPreviousPanel) {
        if (fromPreviousPanel) {
            scriptSources = mainWindow.getProject().getScriptManager().getScriptFileNames();
            listSelectionPanel.list.setListData(scriptSources);
            textFieldPanel.textField.setSelectedItem("");
        }
        setWindowTitle();
        checkNextButton();
        return false;
    }

    /** Called if panel is to be deactivated. If switching to next panel, the
    * script source files are parsed (if not done before).
    *
    * @param toNextPanel true if switching to next panel, false otherwise
    * @return true, if an error occured (for example a parse error), false otherwise
    */
    public boolean panelDeactivated(boolean toNextPanel) {
        if (!toNextPanel) return false;
        mainWindow.setWaitCursor();
        mainWindow.setEnabled(false);
        veto = !scriptSources.check();
        mainWindow.setEnabled(true);
        mainWindow.toFront();
        mainWindow.setDefaultCursor();
        return veto;
    }

    void prevButton_actionPerformed(ActionEvent e) {
        mainWindow.switchToPrevious();
    }

    void nextButton_actionPerformed(ActionEvent e) {
        GlobalSettings.writeFileHistory();
        mainWindow.switchToNext();
    }

    void chooseScriptSourceButton_actionPerformed(ActionEvent e) {
        JFileChooser fileChooser;
        if (defaultPath == null) defaultPath = GlobalSettings.getAbsolutePath(null, GlobalSettings.getDefaultScriptPath());
        if (defaultPath != null) fileChooser = new JFileChooser(defaultPath); else fileChooser = new JFileChooser();
        InjectJFileFilter fileFilter = new InjectJFileFilter("ij", CodeMapper.getText("SCRIPTFILE_DESCRIPT"));
        fileChooser.addChoosableFileFilter(fileFilter);
        fileChooser.setFileFilter(fileFilter);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (file.isDirectory()) return;
            String name = null;
            if (file.exists() && file.isFile()) {
                try {
                    name = file.getCanonicalPath();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
            defaultPath = file.getParent();
            name = name.replace('\\', '/');
            if (name != null && (!name.equals(""))) {
                textFieldPanel.textField.insertItemAt(name, 0);
                textFieldPanel.textField.setSelectedItem(name);
                addScriptSourceButton_actionPerformed(null);
            }
        }
    }

    /** Adds a Script to the visible List
   *
   */
    void addScriptSourceButton_actionPerformed(ActionEvent e) {
        String fileName = (String) textFieldPanel.textField.getSelectedItem();
        if (fileName == null || fileName.equals("")) {
            chooseScriptSourceButton_actionPerformed(null);
            fileName = (String) textFieldPanel.textField.getSelectedItem();
            if (fileName == null || fileName.equals("")) return;
        }
        GlobalSettings.addRecentScriptFile(fileName);
        if (scriptSources.add(fileName)) {
            listSelectionPanel.list.setListData(scriptSources);
            textFieldPanel.textField.setSelectedItem("");
        } else {
        }
        checkNextButton();
    }

    void textField_actionPerformed(ActionEvent e) {
        String fileName = (String) textFieldPanel.textField.getSelectedItem();
        if (fileName == null || fileName.equals("")) {
            return;
        }
        GlobalSettings.addRecentScriptFile(fileName);
        if (scriptSources.add(fileName)) {
            listSelectionPanel.list.setListData(scriptSources);
            textFieldPanel.textField.setSelectedItem("");
        }
        checkNextButton();
    }

    void upButton_actionPerformed(ActionEvent e) {
        int[] toBeSelected = scriptSources.moveUp(listSelectionPanel.list.getSelectedIndices());
        listSelectionPanel.list.setListData(scriptSources);
        listSelectionPanel.list.setSelectedIndices(toBeSelected);
    }

    void downButton_actionPerformed(ActionEvent e) {
        int toBeSelected[] = scriptSources.moveDown(listSelectionPanel.list.getSelectedIndices());
        listSelectionPanel.list.setListData(scriptSources);
        listSelectionPanel.list.setSelectedIndices(toBeSelected);
    }

    void removeButton_actionPerformed(ActionEvent e) {
        scriptSources.remove(listSelectionPanel.list.getSelectedValues());
        listSelectionPanel.list.setListData(scriptSources);
        checkNextButton();
    }

    public String helpFilename() {
        return "scriptsources.html";
    }

    private void checkNextButton() {
        if (scriptSources == null || scriptSources.isEmpty()) {
            buttonPanel.nextButton.setEnabled(false);
        } else {
            buttonPanel.nextButton.setEnabled(true);
        }
    }
}
