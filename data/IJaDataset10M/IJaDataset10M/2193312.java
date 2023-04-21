package fr.excilys.gwt.clientbundle.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class Contact extends Composite {

    private static ContactUiBinder uiBinder = GWT.create(ContactUiBinder.class);

    interface ContactUiBinder extends UiBinder<Widget, Contact> {
    }

    @UiField
    TextBox idField;

    @UiField
    TextBox nameField;

    @UiField
    TextBox mailField;

    public Contact() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public void setUser(String user) {
        this.idField.setText(Integer.toString(user.hashCode()));
        this.nameField.setText(user);
        String mail = user;
        mail = mail.toLowerCase();
        mail = mail.replaceAll(" ", ".");
        mail = mail.concat("@excilys.com");
        this.mailField.setText(mail);
    }
}
