package org.hardtokenmgmt.admin.ui.panels.mantokens;

import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import org.hardtokenmgmt.admin.ui.AdminUIUtils;
import org.hardtokenmgmt.common.vo.NotificationMessageTemplate;
import org.hardtokenmgmt.core.InterfaceFactory;
import org.hardtokenmgmt.core.settings.GlobalSettings;
import org.hardtokenmgmt.core.settings.LastEnteredDataStack;
import org.hardtokenmgmt.core.ui.UIHelper;
import org.hardtokenmgmt.core.util.CertUtils;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * Dialog used to edit the send notification message.
 * 
 * 
 * @author Philip Vendil 17 feb 2010
 *
 * @version $Id$
 */
public class EditNotificationMsgDialog extends JDialog {

    private JTextField carbonCopyTextField;

    private JComboBox subjectComboBox;

    private static final long serialVersionUID = 1L;

    private Dimension DIALOG_SIZE = new Dimension(800, 500);

    private LastEnteredDataStack<NotificationMessageTemplate> messages = new LastEnteredDataStack<NotificationMessageTemplate>("notificationmsg.template");

    private NotificationMessageTemplate activeTemplate = null;

    private GlobalSettings orgGS;

    private JEditorPane messageEditorPane;

    private List<NotificationMessageTemplate> currentMessageTemplates;

    public EditNotificationMsgDialog() {
        super();
        initComponents();
    }

    public EditNotificationMsgDialog(GlobalSettings orgGS, Window owner) {
        super(owner, ModalityType.APPLICATION_MODAL);
        this.orgGS = orgGS;
        initComponents();
    }

    private void initComponents() {
        setSize(DIALOG_SIZE);
        setTitle(UIHelper.getText("mantokens.notificationmsgtitle"));
        setIconImage(AdminUIUtils.getAdminWindowIcon());
        getContentPane().setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("53dlu"), FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("43dlu"), FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("99dlu:grow(1.0)"), FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("100dlu"), FormFactory.RELATED_GAP_COLSPEC }, new RowSpec[] { FormFactory.UNRELATED_GAP_ROWSPEC, RowSpec.decode("34dlu"), FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("16dlu"), RowSpec.decode("13dlu"), FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("130dlu:grow(1.0)"), FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("12dlu"), RowSpec.decode("18dlu"), RowSpec.decode("34dlu"), FormFactory.RELATED_GAP_ROWSPEC }));
        final JButton cancelButton = new JButton();
        cancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                dispose();
            }
        });
        cancelButton.setIcon(UIHelper.getImage("exit.png"));
        cancelButton.setText(UIHelper.getText("cancel"));
        getContentPane().add(cancelButton, new CellConstraints(2, 11, 3, 1, CellConstraints.DEFAULT, CellConstraints.FILL));
        final JButton saveButton = new JButton();
        saveButton.setText(UIHelper.getText("adminconfig.savebutton"));
        saveButton.setIcon(UIHelper.getImage("saveprops.gif"));
        saveButton.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                saveMessage();
            }
        });
        getContentPane().add(saveButton, new CellConstraints(8, 11, CellConstraints.DEFAULT, CellConstraints.FILL));
        final JLabel headLabel = new JLabel();
        headLabel.setText(UIHelper.getText("mantokens.notificationmsgtitle"));
        headLabel.setFont(UIHelper.getTitleFont());
        getContentPane().add(headLabel, new CellConstraints(6, 2, 3, 1, CellConstraints.LEFT, CellConstraints.CENTER));
        final JLabel iconLabel = new JLabel();
        iconLabel.setIcon(UIHelper.getImage("mailsigner_view.gif"));
        getContentPane().add(iconLabel, new CellConstraints(4, 2, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        currentMessageTemplates = messages.getItems();
        Vector<NotificationMessageTemplate> comboBoxItems = new Vector<NotificationMessageTemplate>();
        for (NotificationMessageTemplate tmp : currentMessageTemplates) {
            comboBoxItems.add(tmp);
        }
        subjectComboBox = new JComboBox(comboBoxItems);
        subjectComboBox.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent actionEvent) {
                Object selected = subjectComboBox.getSelectedItem();
                if (selected instanceof NotificationMessageTemplate) {
                    for (NotificationMessageTemplate tmp : currentMessageTemplates) {
                        if (tmp.equals(selected)) {
                            activeTemplate = tmp;
                            messageEditorPane.setText(activeTemplate.getMessage());
                            carbonCopyTextField.setText(activeTemplate.getCarbonCopyOf());
                        }
                    }
                } else {
                    if (selected instanceof String) {
                        activeTemplate = new NotificationMessageTemplate((String) selected, "", "");
                        messageEditorPane.setText(activeTemplate.getMessage());
                        carbonCopyTextField.setText(activeTemplate.getCarbonCopyOf());
                    }
                }
            }
        });
        subjectComboBox.setEditable(true);
        getContentPane().add(subjectComboBox, new CellConstraints(6, 5, 3, 1));
        final JScrollPane scrollPane = new JScrollPane();
        getContentPane().add(scrollPane, new CellConstraints("6, 7, 3, 1, fill, fill"));
        messageEditorPane = new JEditorPane();
        scrollPane.setViewportView(messageEditorPane);
        carbonCopyTextField = new JTextField();
        getContentPane().add(carbonCopyTextField, new CellConstraints(6, 9, 3, 1));
        final JLabel subjectLabel = new JLabel();
        subjectLabel.setText(UIHelper.getText("mantokens.notificatiosubject"));
        subjectLabel.setFont(UIHelper.getLabelFontBold());
        getContentPane().add(subjectLabel, new CellConstraints(2, 5, 3, 1, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        final JLabel messageBodyLabel = new JLabel();
        messageBodyLabel.setText(UIHelper.getText("mantokens.notificationmsg"));
        messageBodyLabel.setFont(UIHelper.getLabelFontBold());
        getContentPane().add(messageBodyLabel, new CellConstraints(2, 7, 3, 1, CellConstraints.RIGHT, CellConstraints.TOP));
        final JLabel carbonCopyLabel = new JLabel();
        carbonCopyLabel.setText(UIHelper.getText("mantokens.notificationcc"));
        carbonCopyLabel.setFont(UIHelper.getLabelFontBold());
        getContentPane().add(carbonCopyLabel, new CellConstraints(2, 9, 3, 1, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        final JLabel helpLabel = new JLabel();
        helpLabel.setText("");
        AdminUIUtils.setNoDelayToolTip(helpLabel);
        helpLabel.setIcon(UIHelper.getImage("help.png"));
        helpLabel.setToolTipText(AdminUIUtils.formatMultilineToolTipIntoHTML("mantokens.notificationmsghelp"));
        getContentPane().add(helpLabel, new CellConstraints(8, 4, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        setTextValues();
    }

    private void setTextValues() {
        List<NotificationMessageTemplate> templates = messages.getItems();
        if (templates.size() == 0) {
            String subject = orgGS.getProperty("mantokens.notification.subject", "");
            String message = orgGS.getProperty("mantokens.notification.message", "");
            activeTemplate = new NotificationMessageTemplate(subject, message, "");
            messages.addItem(activeTemplate);
        } else {
            activeTemplate = templates.get(0);
        }
        subjectComboBox.setSelectedItem(activeTemplate);
        messageEditorPane.setText(activeTemplate.getMessage());
        carbonCopyTextField.setText(activeTemplate.getCarbonCopyOf());
    }

    private void saveMessage() {
        String subject = subjectComboBox.getSelectedItem().toString();
        String message = messageEditorPane.getText();
        String carbonCopy = carbonCopyTextField.getText().trim();
        if (!subject.trim().equals("") && !message.trim().equals("")) {
            if (carbonCopy.equals("") || UIHelper.isValidEmailAddress(carbonCopy, ";")) {
                NotificationMessageTemplate newTemplate = new NotificationMessageTemplate(subject, message, carbonCopy);
                activeTemplate = newTemplate;
                messages.addItem(newTemplate);
                dispose();
            } else {
                Object[] options = { UIHelper.getText("ok") };
                JOptionPane.showOptionDialog(this, UIHelper.getText("mantokens.invalidccaddressmsg"), UIHelper.getText("mantokens.invalidccaddresstitle"), JOptionPane.OK_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
            }
        } else {
            Object[] options = { UIHelper.getText("ok") };
            JOptionPane.showOptionDialog(this, UIHelper.getText("mantokens.invalidnotificationmsg"), UIHelper.getText("mantokens.invalidnotificationtitle"), JOptionPane.OK_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
        }
    }

    public NotificationMessageTemplate getSelectedNotificationMessageTemplate() {
        return activeTemplate;
    }

    public static void main(String[] args) throws Exception {
        CertUtils.installBCProvider();
        UIHelper.setTheme();
        EditNotificationMsgDialog d = new EditNotificationMsgDialog(InterfaceFactory.getGlobalSettings(), null);
        d.setVisible(true);
    }
}
