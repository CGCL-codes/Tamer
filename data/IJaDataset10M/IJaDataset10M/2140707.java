package abid.password.swing.form;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import javax.swing.AbstractButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import abid.password.parameters.TimeParameter;
import abid.password.swing.Application;
import com.jeta.forms.components.panel.FormPanel;

public class RomanNumeralPasswordTab extends AbstractTab {

    private JLabel feedbackLabel;

    private JTextField usernameField;

    private JTextField passwordField;

    private JComboBox parameterField;

    private AbstractButton saveButton;

    private AbstractButton cancelButton;

    private ActionListener actionListener = new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            if (validateComponents()) {
                String username = usernameField.getText();
                String password = passwordField.getText();
                TimeParameter parameter = (TimeParameter) parameterField.getSelectedItem();
                getApplication().getController().saveRomanNumeralUser(username, password, parameter);
                getApplication().getController().loadLoginUI();
            } else if (e.getSource() == cancelButton) {
                getApplication().getController().loadLoginUI();
            }
        }
    };

    public RomanNumeralPasswordTab(Application application, FormPanel form) {
        super(application);
        initComponents(form);
    }

    public void initComponents(FormPanel form) {
        feedbackLabel = form.getLabel("roman.feedbackLabel");
        setFeedbackLabel(feedbackLabel);
        setInfo(null);
        usernameField = form.getTextField("roman.usernameField");
        passwordField = form.getTextField("roman.passwordField");
        parameterField = form.getComboBox("roman.parameterField");
        List<TimeParameter> choices = Arrays.asList(TimeParameter.values());
        for (TimeParameter param : choices) {
            parameterField.addItem(param);
        }
        saveButton = form.getButton("roman.saveButton");
        saveButton.addActionListener(actionListener);
        cancelButton = form.getButton("roman.cancelButton");
        cancelButton.addActionListener(actionListener);
    }

    public boolean validateComponents() {
        setInfo(null);
        if (usernameField.getText().equals("")) {
            setError("Username field cannot be blank");
            return false;
        }
        if (passwordField.getText().equals("")) {
            setError("Password field cannot be blank");
            return false;
        }
        return true;
    }
}
