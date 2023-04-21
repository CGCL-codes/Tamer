package org.damour.base.client.ui.admin;

import java.util.Date;
import org.damour.base.client.objects.User;
import org.damour.base.client.service.BaseServiceCache;
import org.damour.base.client.ui.IGenericCallback;
import org.damour.base.client.ui.buttons.Button;
import org.damour.base.client.ui.datepicker.MyDatePicker;
import org.damour.base.client.ui.dialogs.MessageDialogBox;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.DefaultFormat;

public class EditAccountPanel extends FlexTable {

    public EditAccountPanel(final IGenericCallback<User> callback, final User user) {
        final TextBox usernameTextBox = new TextBox();
        final PasswordTextBox passwordTextBox = new PasswordTextBox();
        final TextBox passwordHintTextBox = new TextBox();
        final TextBox firstnameTextBox = new TextBox();
        final TextBox lastnameTextBox = new TextBox();
        final TextBox emailTextBox = new TextBox();
        Date birthday = new Date(user.getBirthday());
        DefaultFormat format = new DefaultFormat(DateTimeFormat.getMediumDateFormat());
        final DateBox birthdayPicker = new DateBox(new MyDatePicker(), birthday, format);
        final CheckBox validatedCheckBox = new CheckBox("Validated");
        final CheckBox administratorCheckBox = new CheckBox("Administrator");
        usernameTextBox.setText(user.getUsername());
        passwordHintTextBox.setText(user.getPasswordHint());
        firstnameTextBox.setText(user.getFirstname());
        lastnameTextBox.setText(user.getLastname());
        emailTextBox.setText(user.getEmail());
        validatedCheckBox.setValue(user.isValidated());
        administratorCheckBox.setValue(user.isAdministrator());
        birthdayPicker.setValue(birthday);
        Button applyButton = new Button("Apply");
        applyButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                user.setUsername(usernameTextBox.getText());
                user.setPasswordHint(passwordHintTextBox.getText());
                user.setFirstname(firstnameTextBox.getText());
                user.setLastname(lastnameTextBox.getText());
                user.setEmail(emailTextBox.getText());
                user.setBirthday(birthdayPicker.getValue().getTime());
                user.setAdministrator(administratorCheckBox.getValue());
                user.setValidated(validatedCheckBox.getValue());
                final AsyncCallback<User> loginCallback = new AsyncCallback<User>() {

                    public void onFailure(Throwable caught) {
                        MessageDialogBox dialog = new MessageDialogBox("Error", "Could not edit account.", true, true, true);
                        dialog.center();
                    }

                    public void onSuccess(User user) {
                        if (user == null) {
                            MessageDialogBox dialog = new MessageDialogBox("Error", "Could not edit account.", true, true, true);
                            dialog.center();
                        } else {
                            MessageDialogBox dialog = new MessageDialogBox("Success", "Account modified.", true, true, true);
                            dialog.center();
                            callback.invoke(user);
                        }
                    }

                    ;
                };
                BaseServiceCache.getService().createOrEditAccount(user, passwordTextBox.getText(), null, loginCallback);
            }
        });
        applyButton.setTitle("Apply Changes");
        int row = 0;
        setWidget(row, 0, new Label("Username"));
        setWidget(row, 1, usernameTextBox);
        setWidget(++row, 0, new Label("Password"));
        setWidget(row, 1, passwordTextBox);
        setWidget(++row, 0, new Label("Password Hint"));
        setWidget(row, 1, passwordHintTextBox);
        setWidget(++row, 0, new Label("Firstname"));
        setWidget(row, 1, firstnameTextBox);
        setWidget(++row, 0, new Label("Lastname"));
        setWidget(row, 1, lastnameTextBox);
        setWidget(++row, 0, new Label("Email"));
        setWidget(row, 1, emailTextBox);
        setWidget(++row, 0, new Label("Birthday"));
        setWidget(row, 1, birthdayPicker);
        setWidget(++row, 0, new Label());
        setWidget(row, 1, validatedCheckBox);
        setWidget(++row, 0, new Label());
        setWidget(row, 1, administratorCheckBox);
        setWidget(++row, 0, new Label(""));
        setWidget(row, 1, applyButton);
    }
}
