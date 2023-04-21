package net.coolcoders.showcase.web.vaadin.panel;

import com.vaadin.data.util.BeanItem;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;
import net.coolcoders.showcase.model.User;
import net.coolcoders.showcase.web.vaadin.ShowcaseApplication;
import java.util.Arrays;

/**
 * @author <a href="mailto:andreas@bambo.it">Andreas Baumgartner, andreas@bambo.it</a>
 *         Date: 23.10.2010
 *         Time: 18:17:27
 */
public class RegisterPanel extends VerticalLayout {

    public RegisterPanel(final ShowcaseApplication application) {
        final Form registerForm = new Form();
        registerForm.setStyleName("marginTop");
        registerForm.setCaption("Register");
        registerForm.setWidth(260, Sizeable.UNITS_PIXELS);
        final User currentUser = application.getCurrentUser();
        BeanItem<User> userItem = new BeanItem<User>(currentUser);
        registerForm.setItemDataSource(userItem);
        registerForm.setFormFieldFactory(application.getUserFieldFactory());
        registerForm.setVisibleItemProperties(Arrays.asList("username", "fullname", "email", "gender", "password", "birthday", "categories"));
        this.addComponent(registerForm);
        this.setComponentAlignment(registerForm, Alignment.MIDDLE_CENTER);
        HorizontalLayout btnPanel = new HorizontalLayout();
        btnPanel.setStyleName("marginBottom");
        btnPanel.setWidth(160, Sizeable.UNITS_PIXELS);
        btnPanel.setSpacing(true);
        Button okBtn = new Button("Ok", new Button.ClickListener() {

            public void buttonClick(Button.ClickEvent event) {
                registerForm.commit();
                application.getUserService().persist(currentUser);
                application.goToLoginPanel();
            }
        });
        btnPanel.addComponent(okBtn);
        Button cancelBtn = new Button("Cancel", new Button.ClickListener() {

            public void buttonClick(Button.ClickEvent event) {
                application.goToLoginPanel();
            }
        });
        btnPanel.addComponent(cancelBtn);
        this.addComponent(btnPanel);
        this.setComponentAlignment(btnPanel, Alignment.MIDDLE_CENTER);
    }
}
