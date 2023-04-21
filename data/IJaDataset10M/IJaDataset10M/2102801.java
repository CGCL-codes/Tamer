package net.pepperbytes.eqc.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.KeyListener;
import java.util.Calendar;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import net.pepperbytes.eqc.database.persistentobjects.EquipmentUser;
import net.pepperbytes.eqc.database.persistentobjects.Item;
import net.pepperbytes.eqc.database.persistentobjects.TransferRecord;
import net.pepperbytes.eqc.gui.inputcomponents.SerialInputPanel;
import org.apache.commons.logging.LogFactory;

public class TransferDialog extends JDialog {

    /**
	 * Generated
	 */
    private static final long serialVersionUID = 4831991931931065284L;

    private JPanel backBoard;

    private JLabel mainLabel;

    private UserLoadPanel fromUserPanel;

    private UserLoadPanel toUserPanel;

    private SerialInputPanel serialInputPanel;

    private EscapeKeyListener escapeKeyListener;

    public TransferDialog(MainWindow mainWindow, boolean modal) {
        super(mainWindow, modal);
        initialize();
    }

    private void initialize() {
        this.setContentPane(getBackBoard());
        this.setTitle("Transfer");
        this.pack();
        getToUserPanel().setNextFocusPanel(getSerialInputPanel());
    }

    private Container getBackBoard() {
        if (backBoard == null) {
            backBoard = new JPanel();
            backBoard.setLayout(new BorderLayout());
            backBoard.add(getMainLabel(), BorderLayout.NORTH);
            backBoard.add(getToUserPanel(), BorderLayout.EAST);
            backBoard.add(getSerialInputPanel(), BorderLayout.SOUTH);
        }
        return backBoard;
    }

    protected SerialInputPanel getSerialInputPanel() {
        if (serialInputPanel == null) {
            serialInputPanel = new SerialInputPanel();
            serialInputPanel.setSerialInputFont(new Font("Arial", Font.PLAIN, 20));
            serialInputPanel.setLabelFont(new Font("Arial", Font.PLAIN, 20));
            serialInputPanel.addKeyListener(getEscapeKeyListener());
            SerialEnteredListener sel = new SerialEnteredListener() {

                public void serialEntered(String serial) {
                    attemptTransfer(getFromUserPanel().getLoadedUser(), getToUserPanel().getLoadedUser(), getSerialInputPanel());
                }
            };
            serialInputPanel.addSerialListener(sel);
        }
        return serialInputPanel;
    }

    protected boolean attemptTransfer(EquipmentUser fromUser, EquipmentUser toUser, SerialInputPanel serialInputPanel2) {
        boolean result = false;
        boolean toOk = checkUser(toUser);
        boolean serialInputOk = false;
        if (!toOk) {
            JOptionPane.showMessageDialog(null, "You need a valid non-store user in TO field");
            getToUserPanel().setPinSelected(true);
            return false;
        }
        Item item = Item.load(serialInputPanel2.getSerialString());
        if (item == null) {
            JOptionPane.showMessageDialog(null, "You will have to check item '" + serialInputPanel2.getSerialString() + "' in first, since it does not exist at present.", "Item does not exist", JOptionPane.WARNING_MESSAGE);
            return false;
        } else if (item.getCurrentUser().getIsStore()) {
            JOptionPane.showMessageDialog(null, "Ok, in this case the item is apparently still in store '" + item.getCurrentUser().getCombindedName() + "', so you need to check it out first.");
            return false;
        } else {
            serialInputOk = true;
        }
        TransferRecord tr = new TransferRecord();
        tr.setFromUser(fromUser);
        tr.setToUser(toUser);
        tr.setTheSubject(item);
        tr.setTransactionDate(Calendar.getInstance().getTime());
        TransferRecord.saveObject(tr);
        item.setCurrentUser(toUser);
        Item.updateObject(item);
        result = toOk & serialInputOk;
        if (result == true) {
            getSerialInputPanel().clear();
        }
        return result;
    }

    private boolean checkUser(EquipmentUser eu) {
        LogFactory.getLog(this.getClass()).debug("Checking user: " + eu);
        return eu != null && !eu.getIsStore();
    }

    private UserLoadPanel getToUserPanel() {
        if (toUserPanel == null) {
            toUserPanel = new UserLoadPanel(true);
            toUserPanel.setPinInputLabelText("To: ");
            toUserPanel.addKeyListener(getEscapeKeyListener());
        }
        return toUserPanel;
    }

    private KeyListener getEscapeKeyListener() {
        if (escapeKeyListener == null) {
            escapeKeyListener = new EscapeKeyListener(this);
        }
        return escapeKeyListener;
    }

    private UserLoadPanel getFromUserPanel() {
        if (fromUserPanel == null) {
            fromUserPanel = new UserLoadPanel(true);
            fromUserPanel.setPinInputLabelText("From: ");
        }
        return fromUserPanel;
    }

    private Component getMainLabel() {
        if (mainLabel == null) {
            mainLabel = new JLabel();
            mainLabel.setText("Transfer Equipment");
            mainLabel.setAlignmentY(JLabel.CENTER_ALIGNMENT);
            mainLabel.setFont(new Font("Arial", Font.BOLD, 24));
        }
        return mainLabel;
    }
}
