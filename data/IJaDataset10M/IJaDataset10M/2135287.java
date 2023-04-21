package com.rise.rois.ui.wizards.newuser;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Date;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import com.rise.rois.server.UserManagerWrapper;
import com.rise.rois.server.UserManagerStub.UserDAO;
import com.rise.rois.server.UserManagerStub.UserFieldChoices;
import com.rise.rois.ui.util.WidgetUtil;
import com.toedter.calendar.JDateChooser;

public class NewUserDetailsPanel extends NewUserPanel {

    private static final long serialVersionUID = 1L;

    private JTextField firstnameField;

    private JLabel firstNameError;

    private JTextField surnameField;

    private JLabel surnameError;

    private JDateChooser dobField;

    private JComboBox genderField;

    private JComponent ageGroupField;

    private JComponent itSkillField;

    private JComponent internetAccessField;

    public NewUserDetailsPanel(INewUserListener modifyListener) {
        super(modifyListener);
    }

    protected JPanel getContentPanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        JLabel firstnameLabel = new JLabel();
        firstnameLabel.setText("First Name:");
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipadx = 100;
        c.gridx = 0;
        c.gridy = 1;
        contentPanel.add(firstnameLabel, c);
        firstnameField = new JTextField();
        firstnameField.addKeyListener(keyListener);
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.ipadx = 200;
        c.gridx = 1;
        c.gridy = 1;
        contentPanel.add(firstnameField, c);
        firstNameError = createErrorField(contentPanel, 2);
        JLabel surnameLabel = new JLabel();
        surnameLabel.setText("Surname:");
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 3;
        contentPanel.add(surnameLabel, c);
        surnameField = new JTextField();
        surnameField.addKeyListener(keyListener);
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.ipadx = 200;
        c.gridx = 1;
        c.gridy = 3;
        contentPanel.add(surnameField, c);
        surnameError = createErrorField(contentPanel, 4);
        UserFieldChoices userFieldChoices = UserManagerWrapper.getUserField("age_group");
        JLabel agegroupLabel = new JLabel();
        agegroupLabel.setText(userFieldChoices.getLabel());
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 5;
        contentPanel.add(agegroupLabel, c);
        ageGroupField = WidgetUtil.createField(userFieldChoices);
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.ipadx = 200;
        c.gridx = 1;
        c.gridy = 5;
        contentPanel.add(ageGroupField, c);
        JLabel dobLabel = new JLabel();
        dobLabel.setText("Date of Birth:");
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 6;
        contentPanel.add(dobLabel, c);
        dobField = new JDateChooser();
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.ipadx = 200;
        c.gridx = 1;
        c.gridy = 6;
        contentPanel.add(dobField, c);
        JLabel genderLabel = new JLabel();
        genderLabel.setText("Gender:");
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 7;
        contentPanel.add(genderLabel, c);
        genderField = new JComboBox();
        genderField.addItem("Male");
        genderField.addItem("Female");
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.ipadx = 200;
        c.gridx = 1;
        c.gridy = 7;
        contentPanel.add(genderField, c);
        userFieldChoices = UserManagerWrapper.getUserField("it_skill_level");
        JLabel itskillLabel = new JLabel();
        itskillLabel.setText(userFieldChoices.getLabel());
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 8;
        contentPanel.add(itskillLabel, c);
        itSkillField = WidgetUtil.createField(userFieldChoices);
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.ipadx = 200;
        c.gridx = 1;
        c.gridy = 8;
        contentPanel.add(itSkillField, c);
        userFieldChoices = UserManagerWrapper.getUserField("internet_access");
        JLabel internetAccessLabel = new JLabel();
        internetAccessLabel.setText(userFieldChoices.getLabel());
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 9;
        contentPanel.add(internetAccessLabel, c);
        internetAccessField = WidgetUtil.createField(userFieldChoices);
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.ipadx = 200;
        c.gridx = 1;
        c.gridy = 9;
        contentPanel.add(internetAccessField, c);
        return contentPanel;
    }

    @Override
    public void getUserDetails(UserDAO userDAO) {
        userDAO.setFirstname(firstnameField.getText());
        userDAO.setSurname(surnameField.getText());
        Date dateOfBirth = dobField.getDate();
        if (dateOfBirth != null) {
            userDAO.setDate_of_birth(dateOfBirth.getTime());
        } else {
            userDAO.setDate_of_birth(0);
        }
        userDAO.setAgegroup(WidgetUtil.getText(ageGroupField));
        userDAO.setGender(genderField.getSelectedItem().toString());
        userDAO.setIt_skill_level(WidgetUtil.getText(itSkillField));
        userDAO.setInternet_access(WidgetUtil.getText(internetAccessField));
    }

    @Override
    protected boolean validateData() {
        boolean valid = true;
        if (firstnameField.getText().length() == 0) {
            firstNameError.setText("* - please enter First Name");
            valid = false;
        } else {
            firstNameError.setText("");
        }
        if (surnameField.getText().length() == 0) {
            surnameError.setText("* - please enter Surname");
            valid = false;
        } else {
            surnameError.setText("");
        }
        if (modifyListener != null) {
            modifyListener.change(valid);
        }
        return valid;
    }
}
