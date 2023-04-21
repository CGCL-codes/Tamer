package scaffoldtree.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * An input dialog to create a new database property.
 * 
 * @author Nils Kriege
 */
public class AddPropertyDialog extends JDialog implements ActionListener {

    private int status = -1;

    private ResourceBundle messages;

    private JTextField idTextField;

    private JTextField titleTextField;

    private JTextArea descriptionTextArea;

    private JButton cancelButton;

    private JButton addButton;

    AddPropertyDialog(JDialog owner) {
        super(owner, true);
        messages = ResourceBundle.getBundle("scaffoldtree.gui.MessagesBundle", Locale.ENGLISH);
        setTitle(i18n("AddPropertyDialog.Title"));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        cancelButton = new JButton(i18n("AddPropertyDialog.Cancel"));
        cancelButton.addActionListener(this);
        addButton = new JButton(i18n("AddPropertyDialog.Add"));
        addButton.addActionListener(this);
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0);
        c.gridx = 0;
        buttonPanel.add(cancelButton, c);
        c.gridx = 1;
        buttonPanel.add(addButton, c);
        JLabel idLabel = new JLabel(i18n("AddPropertyDialog.Property.ID") + ":");
        idTextField = new JTextField();
        JLabel titleLabel = new JLabel(i18n("AddPropertyDialog.Property.Title") + ":");
        titleTextField = new JTextField();
        JLabel descriptionLabel = new JLabel(i18n("AddPropertyDialog.Property.Description") + ":");
        descriptionTextArea = new JTextArea(3, 20);
        descriptionTextArea.setLineWrap(true);
        JScrollPane descriptionScrollPane = new JScrollPane(descriptionTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        JPanel p = new JPanel(new GridBagLayout());
        c = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0);
        c.gridy = 0;
        p.add(idLabel, c);
        c.gridy = 1;
        p.add(titleLabel, c);
        c.gridy = 2;
        p.add(descriptionLabel, c);
        c.gridx = 1;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 0;
        p.add(idTextField, c);
        c.gridy = 1;
        p.add(titleTextField, c);
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        c.gridy = 2;
        p.add(descriptionScrollPane, c);
        c.gridy = 3;
        c.gridwidth = 2;
        c.insets = new Insets(20, 3, 3, 3);
        c.anchor = GridBagConstraints.CENTER;
        c.weighty = 0.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        p.add(buttonPanel, c);
        add(p);
        pack();
        setLocationRelativeTo(getOwner());
    }

    AddPropertyDialog(JDialog owner, String defaultID, String defaultTitle) {
        this(owner);
        if (defaultID != null) idTextField.setText(defaultID);
        if (defaultTitle != null) titleTextField.setText(defaultTitle);
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == cancelButton) {
            status = JOptionPane.CANCEL_OPTION;
            dispose();
        } else if (source == addButton) {
            status = JOptionPane.OK_OPTION;
            dispose();
        }
    }

    public int getStatus() {
        return status;
    }

    public String getPropertyID() {
        return idTextField.getText();
    }

    public String getPropertyTitle() {
        return titleTextField.getText();
    }

    public String getPropertyDescription() {
        return descriptionTextArea.getText();
    }

    private String i18n(String key) {
        return messages.getString(key);
    }
}
