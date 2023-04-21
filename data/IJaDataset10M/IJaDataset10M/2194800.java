package org.mydragonfly.pj;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.LayoutStyle;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import org.apache.log4j.Logger;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationActionMap;
import org.mydragonfly.pj.data.Journal;
import org.mydragonfly.pj.service.JournalService;
import org.mydragonfly.pj.service.exception.DatabaseLockedException;
import org.mydragonfly.pj.util.StringUtil;

/**
 * Dialog allows user to pick a Journal to work with and validates password.
 * 
 * @author Lee Clarke
 */
public class SelectJournalDialog extends javax.swing.JDialog implements KeyListener {

    static Logger logger = Logger.getLogger(SelectJournalDialog.class);

    private static final long serialVersionUID = -9088325117898289068L;

    private JLabel jLabel2;

    private JPasswordField jPasswordField1;

    private JCheckBox jCheckBox1;

    private JButton okButton;

    private JButton cancelButton;

    private JButton newButton;

    private JLabel jLabel3;

    private JLabel jLabel1;

    private JComboBox journalCBX;

    private List<Journal> journals;

    private boolean firstrun;

    /**
	 * inits Dialog
	 * 
	 * @param frame
	 * @throws DatabaseLockedException
	 */
    public SelectJournalDialog(JFrame frame) throws DatabaseLockedException {
        super(frame);
        initGUI();
        setUpCombo();
    }

    public SelectJournalDialog(JFrame frame, boolean firstrun) throws DatabaseLockedException {
        super(frame);
        this.firstrun = firstrun;
        initGUI();
        setUpCombo();
        pack();
    }

    /**
	 * Retrieves the Journal List from the App and populates the combobox.
	 * 
	 * @throws DatabaseLockedException -
	 *             if the database has been opened already.
	 */
    private void setUpCombo() throws DatabaseLockedException {
        DragonflyJournal parent = (DragonflyJournal) Application.getInstance();
        journals = JournalService.getJournals();
        if (logger.isDebugEnabled()) logger.debug("db init done in secs:" + StringUtil.getTimeDifToNow(parent.loadTime));
        ArrayList<String> jnames = new ArrayList<String>();
        for (Journal j : journals) {
            jnames.add(j.getTitle());
        }
        ComboBoxModel journalCBXModel = new DefaultComboBoxModel(jnames.toArray());
        journalCBX.setModel(journalCBXModel);
        if (logger.isDebugEnabled()) logger.debug("done cbx pop in secs:" + StringUtil.getTimeDifToNow(parent.loadTime));
    }

    private void initGUI() throws DatabaseLockedException {
        try {
            this.setTitle("Open Journal");
            GroupLayout thisLayout = new GroupLayout((JComponent) getContentPane());
            getContentPane().setLayout(thisLayout);
            {
                journalCBX = new JComboBox();
            }
            {
                jLabel1 = new JLabel();
                jLabel1.setName("jLabel1");
            }
            {
                jLabel2 = new JLabel();
                jLabel2.setName("jLabel2");
            }
            {
                jPasswordField1 = new JPasswordField(30);
                jPasswordField1.setName("jPasswordField1");
                jPasswordField1.addKeyListener(this);
            }
            {
                jLabel3 = new JLabel();
                jLabel3.setName("jLabel3");
            }
            {
                jCheckBox1 = new JCheckBox();
                jCheckBox1.setName("guestCheckBox");
            }
            {
                okButton = new JButton();
                okButton.setName("okButton");
                okButton.setAction(getAppActionMap().get("okButton"));
            }
            {
                newButton = new JButton();
                newButton.setName("newButton");
                newButton.setAction(getAppActionMap().get("newJournal"));
            }
            {
                cancelButton = new JButton();
                cancelButton.setName("cancelButton");
                cancelButton.setAction(getAppActionMap().get("cancel"));
            }
            thisLayout.setVerticalGroup(thisLayout.createSequentialGroup().addContainerGap().addGroup(thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(journalCBX, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(jLabel1, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addGroup(thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(jPasswordField1, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(jLabel2, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)).addGap(18).addGroup(thisLayout.createParallelGroup().addComponent(jLabel3, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(jCheckBox1, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)).addGap(0, 21, Short.MAX_VALUE).addGroup(thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(okButton, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(newButton, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(cancelButton, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)).addContainerGap());
            thisLayout.setHorizontalGroup(thisLayout.createSequentialGroup().addContainerGap().addGroup(thisLayout.createParallelGroup().addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup().addComponent(okButton, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE).addGap(37).addComponent(newButton, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE).addGap(37).addComponent(cancelButton, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)).addGroup(thisLayout.createSequentialGroup().addGroup(thisLayout.createParallelGroup().addComponent(jLabel3, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 124, GroupLayout.PREFERRED_SIZE).addComponent(jLabel2, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 124, GroupLayout.PREFERRED_SIZE).addComponent(jLabel1, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 124, GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(thisLayout.createParallelGroup().addGroup(thisLayout.createSequentialGroup().addComponent(jCheckBox1, GroupLayout.PREFERRED_SIZE, 180, GroupLayout.PREFERRED_SIZE)).addGroup(thisLayout.createSequentialGroup().addComponent(jPasswordField1, GroupLayout.PREFERRED_SIZE, 180, GroupLayout.PREFERRED_SIZE)).addGroup(thisLayout.createSequentialGroup().addComponent(journalCBX, GroupLayout.PREFERRED_SIZE, 180, GroupLayout.PREFERRED_SIZE))))).addContainerGap(19, Short.MAX_VALUE));
            thisLayout.linkSize(SwingConstants.HORIZONTAL, new Component[] { jCheckBox1, jPasswordField1, journalCBX });
            thisLayout.linkSize(SwingConstants.HORIZONTAL, new Component[] { jLabel2, jLabel1, jLabel3 });
            Application.getInstance().getContext().getResourceMap(getClass()).injectComponents(getContentPane());
            if (!this.firstrun) pack();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Action
    public void okButton() {
        Journal selectedJournal = JournalService.validatePassword((String) this.journalCBX.getSelectedItem(), new String(this.jPasswordField1.getPassword()));
        if (selectedJournal != null) {
            DragonflyJournal parent = (DragonflyJournal) Application.getInstance();
            parent.setCurrentJournal(selectedJournal);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Password is incorrect, please try again.", "Access error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Action
    public void cancel() {
        dispose();
    }

    @Action
    public void newJournal() throws DatabaseLockedException {
        NewJournalDialog newJ = new NewJournalDialog(null);
        newJ.setModalityType(ModalityType.APPLICATION_MODAL);
        newJ.pack();
        newJ.setLocationRelativeTo(null);
        newJ.setVisible(true);
        if (newJ.isNewJournalCreated()) dispose();
    }

    /**
	 * Returns the action map used by this application. Actions defined using the Action annotation
	 * are returned by this method
	 */
    private ApplicationActionMap getAppActionMap() {
        return Application.getInstance().getContext().getActionMap(this);
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) okButton();
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    /**
	 * Auto-generated main method to display this JDialog
	 */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                JFrame frame = new JFrame();
                SelectJournalDialog inst;
                try {
                    inst = new SelectJournalDialog(frame);
                    inst.setVisible(true);
                } catch (DatabaseLockedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
