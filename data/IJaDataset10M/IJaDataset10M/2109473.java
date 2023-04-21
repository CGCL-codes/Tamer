package pedro.soa.alerts;

import pedro.mda.model.RecordModelFactory;
import pedro.mda.config.PedroConfigurationReader;
import pedro.system.*;
import pedro.util.SystemLog;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class AlertEditingPanel extends JPanel implements ActionListener {

    private Alert alert;

    private MatchingCriteriaView matchingCriteriaView;

    private JLabel nameLabel;

    private JTextField nameField;

    private JLabel messageLabel;

    private JTextArea messageField;

    private JScrollPane messagePane;

    private JLabel authorLabel;

    private JTextField authorField;

    private JLabel institutionLabel;

    private JTextField institutionField;

    private JLabel emailAddressLabel;

    private JTextField emailAddressField;

    private JButton save;

    private boolean isDisabled;

    private Color enabledTextAreaColour;

    private Color disabledTextAreaColour;

    private PedroAlertsEditor alertsEditor;

    private PedroFormContext pedroFormContext;

    private PedroUIFactory pedroUIFactory;

    public AlertEditingPanel(PedroAlertsEditor alertsEditor, PedroFormContext formContext) {
        this.alertsEditor = alertsEditor;
        setLayout(new GridBagLayout());
        this.pedroUIFactory = (PedroUIFactory) formContext.getApplicationProperty(PedroApplicationContext.USER_INTERFACE_FACTORY);
        GridBagConstraints panelGC = pedroUIFactory.createGridBagConstraints();
        matchingCriteriaView = new MatchingCriteriaView(formContext);
        panelGC.fill = GridBagConstraints.HORIZONTAL;
        panelGC.weightx = 100;
        panelGC.ipady = 10;
        add(createTopPanel(), panelGC);
        panelGC.fill = GridBagConstraints.HORIZONTAL;
        panelGC.gridy++;
        add(pedroUIFactory.createSeparator(), panelGC);
        panelGC.fill = GridBagConstraints.BOTH;
        panelGC.weightx = 100;
        panelGC.weighty = 100;
        panelGC.gridy++;
        panelGC.gridx = 0;
        add(matchingCriteriaView, panelGC);
        panelGC.fill = GridBagConstraints.NONE;
        panelGC.anchor = GridBagConstraints.SOUTHEAST;
        panelGC.gridy++;
        add(createButtonPanel(), panelGC);
        isDisabled = false;
    }

    private JPanel createButtonPanel() {
        JPanel panel = pedroUIFactory.createPanel();
        GridBagConstraints panelGC = pedroUIFactory.createGridBagConstraints();
        panelGC.ipady = 5;
        panelGC.anchor = GridBagConstraints.SOUTHEAST;
        String saveText = PedroResources.getMessage("buttons.save");
        save = pedroUIFactory.createButton(saveText);
        save.addActionListener(this);
        panel.add(save, panelGC);
        return panel;
    }

    private JPanel createTopPanel() {
        JPanel panel = pedroUIFactory.createPanel();
        GridBagConstraints panelGC = pedroUIFactory.createGridBagConstraints();
        panelGC.ipady = 10;
        String nameFieldText = PedroResources.getMessage("pedro.alerts.alert.name");
        nameLabel = pedroUIFactory.createLabel(nameFieldText);
        nameField = pedroUIFactory.createTextField(20);
        panelGC.fill = GridBagConstraints.NONE;
        panelGC.weightx = 0;
        panel.add(nameLabel, panelGC);
        panelGC.gridx++;
        panelGC.fill = GridBagConstraints.HORIZONTAL;
        panelGC.weightx = 100;
        panel.add(nameField, panelGC);
        String messageFieldText = PedroResources.getMessage("pedro.alerts.alert.message");
        messageLabel = pedroUIFactory.createLabel(messageFieldText);
        messageField = pedroUIFactory.createTextArea(5, 20);
        enabledTextAreaColour = messageField.getBackground();
        disabledTextAreaColour = (new JPanel()).getBackground();
        panelGC.fill = GridBagConstraints.NONE;
        panelGC.weightx = 0;
        panelGC.gridy++;
        panelGC.gridx = 0;
        panel.add(messageLabel, panelGC);
        panelGC.gridx++;
        panelGC.fill = GridBagConstraints.HORIZONTAL;
        panelGC.weightx = 100;
        messagePane = pedroUIFactory.createScrollPane(messageField);
        panel.add(messagePane, panelGC);
        String authorFieldText = PedroResources.getMessage("pedro.alerts.alert.author");
        authorLabel = pedroUIFactory.createLabel(authorFieldText);
        authorField = pedroUIFactory.createTextField(20);
        panelGC.fill = GridBagConstraints.NONE;
        panelGC.weightx = 0;
        panelGC.gridy++;
        panelGC.gridx = 0;
        panel.add(authorLabel, panelGC);
        panelGC.gridx++;
        panelGC.fill = GridBagConstraints.HORIZONTAL;
        panelGC.weightx = 100;
        panel.add(authorField, panelGC);
        String institutionFieldText = PedroResources.getMessage("pedro.alerts.alert.institution");
        institutionLabel = pedroUIFactory.createLabel(institutionFieldText);
        institutionField = pedroUIFactory.createTextField(20);
        panelGC.fill = GridBagConstraints.NONE;
        panelGC.weightx = 0;
        panelGC.gridy++;
        panelGC.gridx = 0;
        panel.add(institutionLabel, panelGC);
        panelGC.gridx++;
        panelGC.fill = GridBagConstraints.HORIZONTAL;
        panelGC.weightx = 100;
        panel.add(institutionField, panelGC);
        String emailAddressFieldText = PedroResources.getMessage("pedro.alerts.alert.email");
        emailAddressLabel = pedroUIFactory.createLabel(emailAddressFieldText);
        emailAddressField = pedroUIFactory.createTextField(20);
        panelGC.fill = GridBagConstraints.NONE;
        panelGC.weightx = 0;
        panelGC.gridy++;
        panelGC.gridx = 0;
        panel.add(emailAddressLabel, panelGC);
        panelGC.gridx++;
        panelGC.fill = GridBagConstraints.HORIZONTAL;
        panelGC.weightx = 100;
        panel.add(emailAddressField, panelGC);
        return panel;
    }

    /**
     * Get the value of alert.
     *
     * @return value of alert.
     */
    public Alert getAlert() {
        return alert;
    }

    public boolean isDisabled() {
        return isDisabled;
    }

    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        if (source == save) {
            String errors = validateAlert();
            if (errors.equals(PedroResources.EMPTY_STRING) == false) {
                SystemLog.addError(errors);
                return;
            }
            if (isDirty() == true) {
                save();
            }
        }
    }

    /**
     * Set the value of alert.
     *
     * @param currentAlert value to assign to alert.
     */
    public void setAlert(Alert currentAlert) {
        if (currentAlert == null) {
            nameField.setEditable(false);
            messageField.setEditable(false);
            messageField.setBackground(disabledTextAreaColour);
            institutionField.setEditable(false);
            emailAddressField.setEditable(false);
            authorField.setEditable(false);
            isDisabled = true;
            alert = new Alert();
            matchingCriteriaView.setEnabled(false);
            save.setEnabled(false);
        } else {
            isDisabled = false;
            nameField.setEditable(true);
            messageField.setEditable(true);
            messageField.setBackground(enabledTextAreaColour);
            institutionField.setEditable(true);
            emailAddressField.setEditable(true);
            authorField.setEditable(true);
            alert = currentAlert;
            matchingCriteriaView.setEnabled(true);
            save.setEnabled(true);
        }
        nameField.setText(alert.getName());
        messageField.setText(alert.getMessage());
        authorField.setText(alert.getAuthor());
        institutionField.setText(alert.getInstitution());
        emailAddressField.setText(alert.getEmailAddress());
        matchingCriteriaView.setMatchingCriteria(alert.getMatchingCriteria());
    }

    public boolean isDirty() {
        if (alert.getName().equals(nameField.getText().trim()) == false) {
            return true;
        }
        if (alert.getMessage().equals(messageField.getText().trim()) == false) {
            return true;
        }
        if (alert.getAuthor().equals(authorField.getText().trim()) == false) {
            return true;
        }
        if (alert.getInstitution().equals(institutionField.getText().trim()) == false) {
            return true;
        }
        if (alert.getEmailAddress().equals(emailAddressField.getText().trim()) == false) {
            return true;
        }
        return matchingCriteriaView.isDirty();
    }

    public String validateAlert() {
        StringBuffer errorMessages = new StringBuffer();
        String nameValue = nameField.getText().trim();
        if (nameValue.equals(PedroResources.EMPTY_STRING) == true) {
            nameLabel.setForeground(Color.red);
            String message = PedroResources.getMessage("pedro.alerts.blankNameError");
            errorMessages.append("*");
            errorMessages.append(message);
            errorMessages.append("\n");
        } else {
            nameLabel.setForeground(Color.black);
        }
        ArrayList alerts = matchingCriteriaView.validateCriteria();
        AlertWriter alertWriter = new AlertWriter();
        errorMessages.append(alertWriter.writeText(alerts));
        return errorMessages.toString();
    }

    public void save() {
        String currentName = nameField.getText().trim();
        String savedName = alert.getName();
        alert.setName(currentName);
        if (currentName.equals(savedName) == false) {
            alertsEditor.updateDisplayName(alert);
        }
        alert.setMessage(messageField.getText().trim());
        alert.setAuthor(authorField.getText().trim());
        alert.setInstitution(institutionField.getText().trim());
        alert.setEmailAddress(emailAddressField.getText().trim());
        matchingCriteriaView.save();
    }
}
