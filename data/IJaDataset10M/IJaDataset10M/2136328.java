package de.hartmut.gwt.client.admin;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import de.hartmut.gwt.client.LdapUser;
import de.hartmut.gwt.client.MainHandler;
import de.hartmut.gwt.client.utils.LabeledTextBox;

/**
 *
 * @author hartmut
 */
public class NewUserWidget extends Composite {

    private VerticalPanel panel;

    private LabeledTextBox userId;

    private LabeledTextBox firstname;

    private LabeledTextBox lastname;

    private LabeledTextBox email;

    private LabeledTextBox passwordBox;

    private Button createButton;

    private Button cancelButton;

    private final MainHandler mainHandler;

    public NewUserWidget(MainHandler mainHandler) {
        this.mainHandler = mainHandler;
        panel = new VerticalPanel();
        userId = new LabeledTextBox("UserId:", "");
        panel.add(userId);
        firstname = new LabeledTextBox("Firstname:", "");
        panel.add(firstname);
        lastname = new LabeledTextBox("Lastname:", "");
        panel.add(lastname);
        email = new LabeledTextBox("Email:", "");
        panel.add(email);
        passwordBox = new LabeledTextBox("Password:", "", true);
        panel.add(passwordBox);
        panel.add(createButtonBar());
        initWidget(panel);
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        userId.addKeyUpHandler(new UidKeyListener());
    }

    private Widget createButtonBar() {
        HorizontalPanel buttonPanel = new HorizontalPanel();
        createButton = new Button("Create");
        createButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                if (mainHandler != null) {
                    LdapUser newUser = new LdapUser();
                    newUser.setEmail(email.getTextBox());
                    newUser.setFirstname(firstname.getTextBox());
                    newUser.setLastname(lastname.getTextBox());
                    newUser.setPassword(passwordBox.getTextBox());
                    newUser.setUserId(userId.getTextBox());
                    mainHandler.createAccount(newUser);
                }
            }
        });
        cancelButton = new Button("Cancel");
        cancelButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                resetValues();
            }
        });
        buttonPanel.add(createButton);
        buttonPanel.add(cancelButton);
        return buttonPanel;
    }

    public void resetValues() {
        userId.setTextBox("");
        firstname.setTextBox("");
        lastname.setTextBox("");
        email.setTextBox("");
        passwordBox.setTextBox("");
    }

    private class UidKeyListener implements KeyUpHandler {

        public void onKeyUp(KeyUpEvent event) {
            String userIdStr = userId.getTextBox();
            if (userIdStr.length() > 0) {
                mainHandler.checkUserIdExists(userIdStr);
            }
        }
    }
}
